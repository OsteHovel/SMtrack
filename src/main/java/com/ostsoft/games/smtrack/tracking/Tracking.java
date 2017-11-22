package com.ostsoft.games.smtrack.tracking;

import com.ostsoft.games.jsm.room.Door;
import com.ostsoft.games.jsm.room.Room;
import com.ostsoft.games.jsm.util.BitHelper;
import com.ostsoft.games.smtrack.TrackData;
import com.ostsoft.games.smtrack.TrackingState;
import com.ostsoft.games.smtrack.room.RoomXML;
import com.ostsoft.smsplit.observer.EventType;
import com.ostsoft.smsplit.observer.Observer;

public class Tracking implements Observer {
    private final TrackData trackData;

    public Tracking(TrackData trackData) {
        this.trackData = trackData;
        trackData.getAutoData().addObserver(this);
    }

    @Override
    public void handleEvent(EventType eventType, String s) {
        switch (eventType) {
            case UPDATED_GAME_IMAGE:
                track();
                break;
        }

    }

    private void track() {
        if (trackData.isDoorTransitionDone()) {
            trackData.setDoorTransitionDone(false);

            if (trackData.getState() == TrackingState.LOCKED) {
                trackData.setState(TrackingState.NEARBY_SEARCH);
                trackData.getPotentialRooms().clear();
                for (Door door : trackData.getSMRoom().getDoors()) {
                    for (RoomXML room : trackData.getRooms().rooms) {
                        if (BitHelper.snesToPc(door.roomId) == room.address) {
                            if (door.isChangingRegion()) {
                                trackData.setState(TrackingState.BLIND);

                            }
                            trackData.getPotentialRooms().add(room);
                            break;
                        }
                    }
                }

            }
            else {
                trackData.setState(TrackingState.BLIND);
            }
        }

        switch (trackData.getState()) {
            case AREA:
                trackData.setState(TrackingState.AREA_SEARCH);
                trackData.getPotentialRooms().clear();
                for (RoomXML room : trackData.getRooms().rooms) {
                    if (room.area == trackData.getArea()) {
                        trackData.getPotentialRooms().add(room);
                    }
                }
                break;
            case BLIND:
                trackData.setState(TrackingState.BLIND_SEARCH);
                trackData.getPotentialRooms().clear();
                trackData.getPotentialRooms().addAll(trackData.getRooms().rooms);
                break;

            case NEARBY_SEARCH:
            case AREA_SEARCH:
            case BLIND_SEARCH:
//                System.out.println("Number of potential rooms: " + trackData.getPotentialRooms().size() + " doing a " + trackData.getState().name());

                if (trackData.getPotentialRooms().size() == 1) {
                    System.out.println("Locked and loaded :-P");
                    RoomXML currentRoom = trackData.getPotentialRooms().iterator().next();
                    trackData.setArea(currentRoom.getArea());
                    trackData.setRoom(currentRoom);
                    for (Room room : trackData.getSuperMetroid().getRooms()) {
                        if (room.getOffset() == currentRoom.address) {
                            trackData.setSMRoom(room);
                        }
                    }
                    trackData.setState(TrackingState.LOCKED);
                }
                else if (trackData.getPotentialRooms().isEmpty()) {
                    if (trackData.getState() == TrackingState.NEARBY_SEARCH) {
                        trackData.setState(TrackingState.AREA);
                    }
                    else {
                        trackData.setState(TrackingState.BLIND);
                    }
                }
                break;
            case LOCKED:
//                System.out.println("We are in: " + trackData.getRoom().getDisplayName());
                break;
        }
    }
}
