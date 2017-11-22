package com.ostsoft.games.smtrack;

import com.ostsoft.smsplit.observer.EventType;
import com.ostsoft.smsplit.observer.Observer;

public class StreamUpdater implements Observer {
    private final TrackData trackData;
    private String last = "";

    public StreamUpdater(TrackData trackData) {
        this.trackData = trackData;
        trackData.getAutoData().addObserver(this);
    }


    @Override
    public void handleEvent(EventType eventType, String s) {
        switch (eventType) {
            case UPDATED_GAME_IMAGE:
                checkForUpdate();
                break;
        }
    }

    private String getName() {
        if (trackData.getState() == TrackingState.LOCKED) {
            return trackData.getRoom().getDisplayName();
        }
        return "";
    }

    private String getDescription() {
        if (trackData.getState() == TrackingState.LOCKED) {
            String description = trackData.getRoom().description;
            if (description != null) {
                return description;
            }
        }
        return "";
    }

    private void checkForUpdate() {
        String x = "State: " + trackData.getState().toString();
        if (trackData.getState() == TrackingState.LOCKED) {
            x = "Room: " + getName() + " @ " + getArea() + " | " + getDescription();
        }
        if (!x.equals(last)) {
            System.out.println(x);
            last = x;
        }
    }

    private String getArea() {
        if (trackData.getState() == TrackingState.LOCKED || trackData.getState() == TrackingState.AREA_SEARCH) {
            return trackData.getArea().name().substring(0, 1).toUpperCase() + trackData.getArea().name().substring(1).toLowerCase();
        }
        return "";
    }
}
