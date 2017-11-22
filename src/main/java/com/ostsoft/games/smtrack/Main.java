package com.ostsoft.games.smtrack;

import com.ostsoft.games.jsm.RoomList;
import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.room.Room;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.smtrack.escapetimer.EscapeTimer;
import com.ostsoft.games.smtrack.itemselect.ItemSelectTracker;
import com.ostsoft.games.smtrack.minimap.MinimapTracker;
import com.ostsoft.games.smtrack.room.RoomXML;
import com.ostsoft.games.smtrack.room.RoomXMLUtil;
import com.ostsoft.games.smtrack.room.RoomsXML;
import com.ostsoft.games.smtrack.tracking.Tracking;
import com.ostsoft.games.smtrack.transition.TransitionTracker;
import com.ostsoft.smsplit.AutoSplit;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final float cycleTime = 1000 / 15f;
    private static Logger logger = Logger.getLogger(com.ostsoft.games.smtrack.Main.class.getName());
    private static TrackData trackData;

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        AutoSplit autoSplit = new AutoSplit();
        autoSplit.getAutoWindow().setLocation(autoSplit.getAutoWindow().getFrame(), 1, 0);
        autoSplit.getAutoWindow().getFrame().setExtendedState(Frame.MAXIMIZED_BOTH);

        trackData = new TrackData(autoSplit.getAutoData());

        RoomsXML roomsXML = RoomXMLUtil.decodeRooms("rooms.xml");
        trackData.setRooms(roomsXML);

        ByteStream byteStreamFromFile = getByteStreamFromFile(new File("sm.smc"));
        SuperMetroid superMetroid = new SuperMetroid(byteStreamFromFile, SuperMetroid.Flag.ROOMS.value, RoomList.getRoomList(), Collections.emptyList());
        trackData.setSuperMetroid(superMetroid);

        for (RoomXML roomXML : trackData.getRooms().rooms) {
            for (Room room : trackData.getSuperMetroid().getRooms()) {
                if (roomXML.address == room.getOffset()) {
                    roomXML.room = room;
                }
            }
        }


        MinimapTracker minimapTracker = new MinimapTracker(trackData);
        EscapeTimer escapeTimer = new EscapeTimer(trackData);
        TransitionTracker transitionTracker = new TransitionTracker(trackData);
        Tracking tracking = new Tracking(trackData);
        StreamUpdater streamUpdater = new StreamUpdater(trackData);
        ItemSelectTracker itemSelectTracker = new ItemSelectTracker(trackData);

        float time;
        long timeStart = System.nanoTime();
        long timeEnd;
        while (true) {
            long timeSinceLastUpdate = (System.nanoTime() - timeStart) / 1000000;
            timeStart = System.nanoTime();

            autoSplit.cycle(timeSinceLastUpdate);

            timeEnd = System.nanoTime();
            time = (timeEnd - timeStart) / 1000000.0f;

            try {
                if (time < cycleTime) {
                    Thread.sleep((long) (cycleTime - time));
                }
                else {
                    logger.log(Level.WARNING, "Warning! Skipping sleep! Cycle-time: " + time);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private static ByteStream getByteStreamFromFile(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            int len = stream.available();
            byte[] bytes = new byte[len];
            stream.read(bytes, 0, len);
            return new ByteStream(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
