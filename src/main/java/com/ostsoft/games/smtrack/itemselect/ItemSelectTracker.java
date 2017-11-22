package com.ostsoft.games.smtrack.itemselect;

import artnet4j.packets.ArtDmxPacket;
import com.ostsoft.games.smtrack.TrackData;
import com.ostsoft.smsplit.observer.EventType;
import com.ostsoft.smsplit.observer.Observer;
import com.ostsoft.smsplit.xml.config.ItemBox;
import com.ostsoft.smsplit.xml.config.RectangleXML;

import java.net.InetAddress;

public class ItemSelectTracker implements Observer {
    private final TrackData trackData;
    private ItemBox itemSelectBox;
    private int lastSelected = 0;
    private int selected = 0;
//    private ArtNet artNet;
    private InetAddress target;

    public ItemSelectTracker(TrackData trackData) {
        this.trackData = trackData;
        trackData.getAutoData().addObserver(this);

        for (ItemBox itemBox : trackData.getAutoData().config.itemBoxes.itemBox) {
            if (itemBox.name.equals("ItemSelect")) {
                itemSelectBox = itemBox;
            }
        }

//        try {
//
//            artNet = new ArtNet();
//            artNet.init();
//
//            InetAddress me = InetAddress.getByName("Your IP");
//            artNet.start(me);
//            target = InetAddress.getByName("Destination Artnet IP");
//        } catch (IOException | ArtNetException e) {
//            e.printStackTrace();
//        }

    }

    private void cycle() {
        selected = 0;
        for (RectangleXML rectangle : itemSelectBox.rectangles) {
            if (rectangle.matching) {
                selected = itemSelectBox.rectangles.indexOf(rectangle) + 1;
                break;
            }
        }

        if (selected != lastSelected) {
            lastSelected = selected;


            byte[] dmxData = new byte[14];
            dmxData[5] = (byte) 0xFF;
            if (selected == 1) {
                dmxData[6] = (byte) 0xFF;
                dmxData[8] = (byte) 64;
            }
            else if (selected == 2) {
                dmxData[7] = (byte) 0xFF;
            }
            else if (selected == 3) {
                dmxData[6] = (byte) 0xFF;
                dmxData[7] = (byte) 0xFF;
            }
            else if (selected == 4) {
                dmxData[8] = (byte) 0xFF;
            }
            else if (selected == 5) {
                dmxData[9] = (byte) 0xFF;
            }

//            ArtDmxPacket artDmxPacket = new ArtDmxPacket();
//            artDmxPacket.setDMX(dmxData, dmxData.length);
//            artDmxPacket.setUniverse(0, 0);
//            artNet.unicastPacket(artDmxPacket, target);
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
}