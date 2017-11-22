package com.ostsoft.games.smtrack.minimap;

import com.ostsoft.games.jsm.room.Room;
import com.ostsoft.games.smtrack.TrackData;
import com.ostsoft.games.smtrack.TrackingState;
import com.ostsoft.games.smtrack.room.RoomXML;
import com.ostsoft.smsplit.observer.EventType;
import com.ostsoft.smsplit.observer.Observer;
import com.ostsoft.smsplit.xml.config.ItemBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class MinimapTracker implements Observer {
    private static Logger logger = Logger.getLogger(MinimapTracker.class.getName());
    private final TrackData trackData;
    private MinimapMatcher minimapMatcher = new MinimapMatcher();
    private Minimap minimap = new Minimap();
    private ItemBox minimapItemBox;
    private ItemBox sidesItemBox;


    public MinimapTracker(TrackData trackData) {
        this.trackData = trackData;
        for (ItemBox itemBox : trackData.getAutoData().config.itemBoxes.itemBox) {
            if (itemBox.name.equals("Minimap")) {
                minimapItemBox = itemBox;
            }
            else if (itemBox.name.equals("Minimap - Sides")) {
                sidesItemBox = itemBox;
            }
        }

        trackData.getAutoData().addObserver(this);
    }

    private void track() {
        if (minimapItemBox.rectangles.get(15).matching || trackData.isTransition() || !trackData.isIngame()) {
            return;
        }

        int[][] currentMinimap = new int[5][3];

        for (int i = 0; i < sidesItemBox.rectangles.size(); i = i + 4) {
            if (i + 4 >= sidesItemBox.rectangles.size()) {
                continue;
            }

            int currentIterator = i / 4;
            int room = minimapItemBox.rectangles.get(currentIterator).matching ? 1 : 0;
            int up = sidesItemBox.rectangles.get(i).matching ? 1 : 0;
            int right = sidesItemBox.rectangles.get(i + 1).matching ? 1 : 0;
            int down = sidesItemBox.rectangles.get(i + 2).matching ? 1 : 0;
            int left = sidesItemBox.rectangles.get(i + 3).matching ? 1 : 0;
            int value = (room << 4) + (up << 3) + (right << 2) + (down << 1) + left;


            int y = currentIterator - (currentIterator / 3 * 3);
            int x = currentIterator / 3;

//            System.out.println(currentIterator + " = " + x + "x" + y);
            if (currentMinimap[x] == null) {
                currentMinimap[x] = new int[3];
            }
            currentMinimap[x][y] = value;
        }

        if (!minimapMatcher.isBlack(currentMinimap)) {
            if (trackData.getState() == TrackingState.LOCKED) {
                List<Coordinate> coordinates = minimapMatcher.checkMinimap(currentMinimap, this.minimap.getMinimap(trackData.getArea()), trackData.getSMRoom().getHeader().x, trackData.getSMRoom().getHeader().y, trackData.getSMRoom().getHeader().width, trackData.getSMRoom().getHeader().height);
                if (!coordinates.isEmpty()) {
                    for (Coordinate coordinate : coordinates) {
                        if (trackData.getCoordinate() == null || coordinate.x != trackData.getCoordinate().x || coordinate.y != trackData.getCoordinate().y) {
                            trackData.setCoordinate(coordinate);
                        }
                    }
                }

            }
            else if (trackData.getState() == TrackingState.BLIND_SEARCH) {
                Set<RoomXML> potentialRooms = new HashSet<>();

                for (Area area : Area.values()) {
                    potentialRooms.addAll(getPotentialRooms(currentMinimap, area));
                }

                trackData.getPotentialRooms().retainAll(potentialRooms);
            }
            else if (trackData.getState() == TrackingState.AREA_SEARCH || trackData.getState() == TrackingState.NEARBY_SEARCH) {
                if (trackData.getArea() == null) {
                    trackData.getPotentialRooms().clear();
                    return;
                }
                trackData.getPotentialRooms().retainAll(getPotentialRooms(currentMinimap, trackData.getArea()));
            }
        }
    }

    private Set<RoomXML> getPotentialRooms(int[][] currentMinimap, Area area) {
        int[][] map = this.minimap.getMinimap(area);
        Set<RoomXML> potentialRooms = new HashSet<>();

        List<Coordinate> coordinates = minimapMatcher.checkMinimap(currentMinimap, map);
        if (!coordinates.isEmpty()) {
//            System.out.println(area + " matched " + coordinates.size() + " times");
            for (Coordinate coordinate : coordinates) {
//                System.out.println("Current location: " + coordinate.x + "x" + coordinate.y + " in " + area.name());
                Set<Room> rooms = getRooms(coordinate, area);
                for (Room room : rooms) {
                    for (RoomXML roomXML : trackData.getRooms().rooms) {
                        if (room.getOffset() == roomXML.address) {
//                            System.out.println("We are currently matching " + roomXML.getDisplayName() + " in " + roomXML.getArea());
                            potentialRooms.add(roomXML);
                        }
                    }
                }
            }
        }

        return potentialRooms;
    }

    private Set<Room> getRooms(Coordinate coordinate, Area area) {
        Set<Room> matchingRoom = new HashSet<>();
        for (Room room : trackData.getSuperMetroid().getRooms()) {
            if (room.getHeader().region == area.getIndex() &&
                    room.getHeader().x <= coordinate.x &&
                    room.getHeader().x + room.getHeader().width > coordinate.x &&
                    room.getHeader().y <= coordinate.y - 1 &&
                    room.getHeader().y + room.getHeader().height > coordinate.y - 1
                    ) {
                matchingRoom.add(room);
            }
        }

        return matchingRoom;
    }


    @Override
    public void handleEvent(EventType eventType, String s) {
        switch (eventType) {
            case UPDATED_GAME_IMAGE:
                track();
                break;
        }

    }
}
