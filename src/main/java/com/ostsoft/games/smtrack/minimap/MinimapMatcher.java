package com.ostsoft.games.smtrack.minimap;

import java.util.ArrayList;
import java.util.List;

public class MinimapMatcher {

    public boolean isBlack(int[][] minimap) {
        for (int mapX = 0; mapX < minimap.length; mapX++) {
            for (int mapY = 0; mapY < minimap[0].length; mapY++) {
                if (minimap[mapX][mapY] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Coordinate> checkMinimap(int[][] minimap, int[][] map) {
        List<Coordinate> points = new ArrayList<>();
        for (int mapX = -2; mapX < map.length; mapX++) {
            for (int mapY = -1; mapY < map[0].length; mapY++) {
                if (checkMinimapAt(mapX, mapY, minimap, map)) {
                    points.add(new Coordinate(mapX + 2, mapY + 1));
                }
            }
        }
        return points;
    }

    public List<Coordinate> checkMinimap(int[][] minimap, int[][] map, int x, int y, int width, int height) {
        List<Coordinate> points = new ArrayList<>();
        for (int mapX = x - 2; mapX < x + width; mapX++) {
            for (int mapY = y - 1; mapY < y + height; mapY++) {
                if (checkMinimapAt(mapX, mapY, minimap, map)) {
                    points.add(new Coordinate(mapX + 2, mapY + 1));
                }
            }
        }
        return points;
    }

    private boolean checkMinimapAt(int mapX, int mapY, int[][] minimap, int[][] map) {
        for (int minimapY = 0; minimapY < minimap[0].length; minimapY++) {
            for (int minimapX = 0; minimapX < minimap.length; minimapX++) {
                int minimapTile = minimap[minimapX][minimapY];
                try {
                    if (mapX + minimapX < 0 || mapY + minimapY < 0) {
                        continue;
                    }
                    int mapTile = map[mapX + minimapX][mapY + minimapY];
                    if (minimapTile == 0) {
                        continue;
                    }
                    if (mapTile != minimapTile) {
                        return false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    if (minimapTile != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
