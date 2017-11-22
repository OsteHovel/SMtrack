package com.ostsoft.games.smtrack.minimap;

import com.ostsoft.games.jsm.SuperMetroid;
import com.ostsoft.games.jsm.layer3.Layer3;
import com.ostsoft.games.jsm.palette.PaletteEnum;
import com.ostsoft.games.jsm.util.ByteStream;
import com.ostsoft.games.jsm.util.ImagePanel;
import com.ostsoft.games.jsm.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Minimap {

    private Map<Area, int[][]> minimaps = new HashMap<>();

    public Minimap() {
        ByteStream stream = getByteStreamFromFile(new File("sm.smc"));
        if (stream == null) {
            return;
        }

        for (Area area : Area.values()) {
            stream.setPosition(area.getAddress());
            List<MapTile> mapTiles = new ArrayList<>(2048);
            for (int i = 0; i < 2048; i++) {
                MapTile element = new MapTile();
                element.load(stream);
                mapTiles.add(element);
            }

            minimaps.put(area, getMinimapArray(mapTiles));
        }
    }

    public static void main(String[] args) {
        Area area = Area.CRATERIA;

        Minimap minimap = new Minimap();
        int[][] mapArray = minimap.getMinimap(area);
        ByteStream stream = getByteStreamFromFile(new File("sm.smc"));

        stream.setPosition(area.getAddress());
        List<MapTile> mapTiles = new ArrayList<>(2048);
        for (int i = 0; i < 2048; i++) {
            MapTile element = new MapTile();
            element.load(stream);
            mapTiles.add(element);
        }
        int[][] minimapArrayFromGame = getMinimapArrayFromGame(mapTiles);

        SuperMetroid superMetroid = new SuperMetroid(stream, SuperMetroid.Flag.LAYER3.value | SuperMetroid.Flag.PALETTES.value);
        Layer3 layer3 = superMetroid.getLayer3List()[0x00];

        ImagePanel imagePanel = new ImagePanel();
        BufferedImage image = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        for (int x = 0; x < mapArray.length; x++) {
            for (int y = 0; y < mapArray[0].length; y++) {
                MinimapType miniType = MinimapType.getMiniType(mapArray[x][y]);
                int index = miniType.getTile();
                boolean flipX = (miniType.getFlip() & 0b01) > 0;
                boolean flipY = (miniType.getFlip() & 0b10) > 0;

//                int index = mapArray[x][y] >> 8;
//                boolean flipX = (mapArray[x][y] & 0x40) > 0;
//                boolean flipY = (mapArray[x][y] & 0x80) > 0;

                BufferedImage imageFromTile = ImageUtil.getImageFromTile(layer3.tiles, index, superMetroid.getPaletteManager().getPalette(PaletteEnum.PAUSE_SCREEN).getColors(), 0, 2, false);

                int x1 = x * 8;
                int y1 = y * 8;


                int sx1 = 0;
                int sy1 = 0;
                int sx2 = imageFromTile.getWidth();
                int sy2 = imageFromTile.getHeight();


                if (flipX) {
                    sx1 = imageFromTile.getWidth();
                    sx2 = 0;
                }

                if (flipY) {
                    sy1 = imageFromTile.getHeight();
                    sy2 = 0;
                }

                g.drawImage(imageFromTile, x1, y1, x1 + 8, y1 + 8, sx1, sy1, sx2, sy2, null);
            }
        }

        int offset = 200;
        for (int x = 0; x < minimapArrayFromGame.length; x++) {
            for (int y = 0; y < minimapArrayFromGame[0].length; y++) {
                int index = minimapArrayFromGame[x][y] >> 8;
                boolean flipX = (minimapArrayFromGame[x][y] & 0x40) > 0;
                boolean flipY = (minimapArrayFromGame[x][y] & 0x80) > 0;

                BufferedImage imageFromTile = ImageUtil.getImageFromTile(layer3.tiles, index, superMetroid.getPaletteManager().getPalette(PaletteEnum.PAUSE_SCREEN).getColors(), 0, 2, false);

                int x1 = x * 8;
                int y1 = y * 8;


                int sx1 = 0;
                int sy1 = 0;
                int sx2 = imageFromTile.getWidth();
                int sy2 = imageFromTile.getHeight();


                if (flipX) {
                    sx1 = imageFromTile.getWidth();
                    sx2 = 0;
                }

                if (flipY) {
                    sy1 = imageFromTile.getHeight();
                    sy2 = 0;
                }


                g.drawImage(imageFromTile, x1, offset + y1, x1 + 8, offset + y1 + 8, sx1, sy1, sx2, sy2, null);
            }
        }


//        BufferedImage image2 = ImageUtil.getImageFromTiles(layer3.tiles, superMetroid.getPaletteManager().getPalette(PaletteEnum.PAUSE_SCREEN).getColors(), 0, 16, 2);
//        BufferedImage image3 = ImageUtil.generateImageFromTileTable8x8(bytes, layer3.tiles, 32, false, superMetroid.getPaletteManager().getPalette(PaletteEnum.PAUSE_SCREEN), 2);
//        g.drawImage(image2, 10, 30, image2.getWidth() * 2, image2.getHeight() * 2, null);
//        g.drawImage(image3, image2.getWidth() + 128, 30, image3.getWidth() * 2, image3.getHeight() * 2, null);
        g.dispose();
        imagePanel.setImage(image);
        JFrame jFrame = new JFrame();
        jFrame.add(imagePanel);
        jFrame.setSize(1024, 768);
        jFrame.setVisible(true);


        printMinimap(mapArray);
    }

    private static void printMinimap(int[][] minimap) {
        for (int y = 0; y < minimap[0].length; y++) {
            for (int x = 0; x < minimap.length; x++) {
                if (minimap[x] == null) {
                    continue;
                }
                int s = minimap[x][y];
//                String tileString = Integer.toHexString(s).toUpperCase();
//                if (tileString.equals("1F00")) {
//                    tileString = "";
//                }
//                String tileString = MinimapType.getMiniType(s).name();
                MinimapType miniType = MinimapType.getMiniType(s);
                if (miniType == null) {
                    miniType = MinimapType.NOTHING;
                }
                String tileString = String.valueOf(miniType.getIdentifier());
                String padding = String.join("", Collections.nCopies(20 - tileString.length(), " "));
                System.out.print(padding + tileString + " ");
            }
            System.out.println();
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

    private static int[][] getMinimapArray(List<MapTile> mapTiles) {
        int currentX = 0;
        int currentY = 0;
        int overHalf = 0;


        int[][] mapArray = new int[64][];
        for (int i = 0; i < 2048; i++) {
            if (mapArray[currentX] == null) {
                mapArray[currentX] = new int[32];
            }

            mapArray[currentX][currentY] = MinimapType.getMiniType(mapTiles.get(i).getTile(), mapTiles.get(i).getFlip()).getIdentifier();

            currentX++;
            if (currentX >= 32 + overHalf) {
                currentY = currentY + 1;
                currentX = overHalf;
            }
            if (currentY >= 32) {
                overHalf = 32;
                currentY = 0;
                currentX = overHalf;
            }
        }
        return mapArray;
    }

    public static int[][] getMinimapArrayFromGame(List<MapTile> mapTiles) {
        int currentX = 0;
        int currentY = 0;
        int overHalf = 0;


        int[][] mapArray = new int[64][];
        for (int i = 0; i < 2048; i++) {
            if (mapArray[currentX] == null) {
                mapArray[currentX] = new int[32];
            }

            mapArray[currentX][currentY] = (mapTiles.get(i).getTile() << 8) + mapTiles.get(i).getFlip();
//            if (mapArray[currentX][currentY] == 0x1F0C) {
//                mapArray[currentX][currentY] = 0;
//            }

            currentX++;
            if (currentX >= 32 + overHalf) {
                currentY = currentY + 1;
                currentX = overHalf;
            }
            if (currentY >= 32) {
                overHalf = 32;
                currentY = 0;
                currentX = overHalf;
            }
        }
        return mapArray;
    }

    public int[][] getMinimap(Area area) {
        return minimaps.get(area);
    }
}
