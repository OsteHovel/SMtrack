package com.ostsoft.games.smtrack;

import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.room.Room;
import com.ostsoft.games.smtrack.minimap.Area;
import com.ostsoft.games.smtrack.minimap.Coordinate;
import com.ostsoft.games.smtrack.room.RoomXML;
import com.ostsoft.games.smtrack.room.RoomsXML;
import com.ostsoft.smsplit.AutoData;

import java.util.HashSet;
import java.util.Set;

public class TrackData {

    private final AutoData autoData;
    private RoomsXML rooms;
    private SuperMetroid superMetroid;

    private TrackingState state = TrackingState.BLIND;
    private Set<RoomXML> potentialRooms = new HashSet<>();

    private RoomXML room = null;
    private Area area = null;
    private boolean ingame = false;
    private boolean doorTransition = false;
    private boolean doorTransitionDone = false;
    private long timeLastDoorTransition;
    private Room SMRoom;
    private Coordinate coordinate = new Coordinate(0, 0);

    public TrackData(AutoData autoData) {
        this.autoData = autoData;
    }

    public AutoData getAutoData() {
        return autoData;
    }

    public RoomsXML getRooms() {
        return rooms;
    }

    public void setRooms(RoomsXML rooms) {
        this.rooms = rooms;
    }

    public SuperMetroid getSuperMetroid() {
        return superMetroid;
    }

    public void setSuperMetroid(SuperMetroid superMetroid) {
        this.superMetroid = superMetroid;
    }

    public TrackingState getState() {
        return state;
    }

    public void setState(TrackingState state) {
        this.state = state;
    }

    public Set<RoomXML> getPotentialRooms() {
        return potentialRooms;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public RoomXML getRoom() {
        return room;
    }

    public void setRoom(RoomXML room) {
        this.room = room;
    }

    public boolean isIngame() {
        return ingame;
    }

    public void setIngame(boolean ingame) {
        this.ingame = ingame;
    }

    public boolean isDoorTransition() {
        return doorTransition;
    }

    public void setDoorTransition(boolean doorTransition) {
        this.doorTransition = doorTransition;
        if (doorTransition) {
            this.timeLastDoorTransition = System.currentTimeMillis();
        }
    }

    public boolean isDoorTransitionDone() {
        return doorTransitionDone;
    }

    public void setDoorTransitionDone(boolean doorTransitionDone) {
        this.doorTransitionDone = doorTransitionDone;
    }

    public boolean isTransition() {
        return doorTransition || timeLastDoorTransition + 1000 > System.currentTimeMillis();
    }

    public Room getSMRoom() {
        return SMRoom;
    }

    public void setSMRoom(Room SMRoom) {
        this.SMRoom = SMRoom;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
