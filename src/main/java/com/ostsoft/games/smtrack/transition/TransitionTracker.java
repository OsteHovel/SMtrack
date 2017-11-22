package com.ostsoft.games.smtrack.transition;

import com.ostsoft.games.smtrack.TrackData;
import com.ostsoft.smsplit.observer.EventType;
import com.ostsoft.smsplit.observer.Observer;
import com.ostsoft.smsplit.xml.config.ItemBox;
import com.ostsoft.smsplit.xml.config.RectangleXML;

public class TransitionTracker implements Observer {
    private final TrackData trackData;
    private ItemBox doorVerticalTransition;
    private ItemBox doorVertical;

    private ItemBox doorHorizontalTransition;
    private ItemBox doorHorizontal;

    private ItemBox black;

    private int previousLeftRightPosition = -1;
    private int previousUpDownPosition = -1;
    private int[] votes = new int[4];

    public TransitionTracker(TrackData trackData) {
        this.trackData = trackData;
        trackData.getAutoData().addObserver(this);

        for (ItemBox itemBox : trackData.getAutoData().config.itemBoxes.itemBox) {
            if (itemBox.name.equals("Door vertical transition")) {
                doorVerticalTransition = itemBox;
            }
            else if (itemBox.name.equals("Door vertical")) {
                doorVertical = itemBox;
            }
            else if (itemBox.name.equals("Door horizontal transition")) {
                doorHorizontalTransition = itemBox;
            }
            else if (itemBox.name.equals("Door horizontal")) {
                doorHorizontal = itemBox;
            }
            else if (itemBox.name.equals("Black")) {
                black = itemBox;
            }
        }
    }

    @Override
    public void handleEvent(EventType eventType, String s) {
        switch (eventType) {
            case UPDATED_GAME_IMAGE:
                cycle();
                break;
        }
    }

    private void cycle() {
        checkIngame();
        trackVoters();
    }

    private void checkIngame() {
        trackData.setIngame(!black.matching);
    }

    private void trackVoters() {
        if (!trackData.isIngame()) {
            return;
        }

        if (doorVerticalTransition.matching || doorHorizontalTransition.matching) {
            trackData.setDoorTransition(true);
        }
        else {
            trackData.setDoorTransition(false);
        }

        if (!doorVerticalTransition.matching && !doorHorizontalTransition.matching) {
            if (votes[0] > 0 || votes[1] > 0 || votes[2] > 0 || votes[3] > 0) {
                int voteHighest = 0;
                for (int i = 0; i < votes.length; i++) {
                    if (Math.max(votes[voteHighest], votes[i]) > votes[voteHighest]) {
                        voteHighest = i;
                    }
                }
//                Direction direction = Direction.getDirection(voteHighest);
//                autoData.setDirection(direction);
                System.out.println("Votes are in and we think the door is going " + voteHighest + " with " + votes[voteHighest]);

                trackData.setDoorTransitionDone(true);
            }
            votes[0] = 0;
            votes[1] = 0;
            votes[2] = 0;
            votes[3] = 0;
            previousUpDownPosition = -1;
            previousLeftRightPosition = -1;
            return;
        }


        int doorLeftRightPosition = getDoorPosition(doorVertical);
        if (doorLeftRightPosition > -1 && previousLeftRightPosition > -1)

        {
            if (doorLeftRightPosition < previousLeftRightPosition) {
                System.out.println("Going RIGHT!");
                votes[3]++;
            }
            else if (doorLeftRightPosition > previousLeftRightPosition) {
                System.out.println("Going LEFT!");
                votes[2]++;
            }
        }

        previousLeftRightPosition = doorLeftRightPosition;

        int doorUpDownPosition = getDoorPosition(doorHorizontal);
        if (doorUpDownPosition > -1 && previousUpDownPosition > -1)

        {
            if (doorUpDownPosition < previousUpDownPosition) {
                System.out.println("Going DOWN!");
                votes[1]++;

            }
            else if (doorUpDownPosition > previousUpDownPosition) {
                System.out.println("Going UP!");
                votes[0]++;
            }
        }

        previousUpDownPosition = doorUpDownPosition;

    }

    private int getDoorPosition(ItemBox door) {
        int i = 0;
        for (RectangleXML rectangle : door.rectangles) {
            if (rectangle.matching) {
                System.out.println("Door is at " + i + "%");
                return i;
            }
            i = i + 10;
        }
        return -1;
    }
}


