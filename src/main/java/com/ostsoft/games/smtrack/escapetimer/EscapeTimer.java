package com.ostsoft.games.smtrack.escapetimer;

import com.ostsoft.games.smtrack.TrackData;
import com.ostsoft.smsplit.observer.EventType;
import com.ostsoft.smsplit.observer.Observer;
import com.ostsoft.smsplit.xml.config.ItemBox;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EscapeTimer implements Observer {
    private static final int[] digitOrder = new int[]{8, 9, 5, 6, 7, 4, 3, 0, 2, 1};
    private static final int[] digitRectanglesLength = new int[]{4, 3, 3, 7, 3, 6, 6, 3, 7, 7};
    private static Logger logger = Logger.getLogger(EscapeTimer.class.getName());
    private static int[][] digitRectangles;
    private final TrackData trackData;
    private List<ItemBox> digits = new ArrayList<>();
    private ItemBox timeBox;
    private ItemBox blackBox;
    private LocalTime lastTime = LocalTime.MIN;

    public EscapeTimer(TrackData trackData) {
        this.trackData = trackData;
        for (ItemBox itemBox : trackData.getAutoData().config.itemBoxes.itemBox) {
            if (itemBox.name.equals("Time")) {
                timeBox = itemBox;
            }
            else if (itemBox.name.equals("Black")) {
                blackBox = itemBox;
            }
            else if (itemBox.name.startsWith("Timer ")) {
                digits.add(itemBox);
            }
        }
        trackData.getAutoData().addObserver(this);
    }

    public static int[][] getDigitRectangles() {
        if (digitRectangles == null) {
            digitRectangles = new int[10][];
            int iGlobal = 0;

            for (int i = 0; i < 10; i++) {
                digitRectangles[i] = new int[digitRectanglesLength[i]];
                for (int j = 0; j < digitRectanglesLength[i]; j++) {
                    digitRectangles[i][j] = iGlobal;
                    iGlobal++;
                }
            }
        }

        return digitRectangles;
    }

    private void track() {
//        if (trackData.isTransition() || !trackData.isIngame()) {
//            return;
//        }

        if (!timeBox.matching && blackBox.matching && lastTime.isAfter(LocalTime.MIN)) {
            System.out.println("Ceres time is: " + lastTime.toString());
            lastTime = LocalTime.MIN;
            return;
        }
        else if (!timeBox.matching) {
            return;
        }

        int[] timer = new int[6];
        for (int i = 0; i < digits.size(); i++) {
            for (int digit : digitOrder) {
                if (checkDigit(digits.get(i), digit)) {
                    timer[i] = digit;
                    break;
                }
            }
        }

        try {
            lastTime = LocalTime.of(0, (timer[0] * 10) + timer[1], (timer[2] * 10) + timer[3], ((timer[4] * 10) + timer[5]) * 10000000);
            System.out.println(lastTime.toString());
        } catch (DateTimeException e) {
            lastTime = LocalTime.MIN;
            e.printStackTrace();
        }
    }

    private boolean checkDigit(ItemBox itemBox, int digit) {
        boolean matching = true;
        for (int i : getDigitRectangles()[digit]) {
            if (!itemBox.rectangles.get(i).matching) {
                matching = false;
            }
        }
        return matching;
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
