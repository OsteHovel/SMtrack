package com.ostsoft.games.smtrack.minimap;

public enum Area {
    CRATERIA(0, 0x1A8000 + (0x1000)),
    BRINSTAR(1, 0x1A8000),
    NORFAIR(2, 0x1A8000 + (2 * 0x1000)),
    WRECKEDSHIP(3, 0x1A8000 + (3 * 0x1000)),
    MARIDIA(4, 0x1A8000 + (4 * 0x1000)),
    TOURIAN(5, 0x1A8000 + (5 * 0x1000)),
    CERES(6, 0x1A8000 + (6 * 0x1000));

    private final int index;
    private final int address;

    Area(int index, int address) {
        this.index = index;
        this.address = address;
    }

    public static Area getIndex(int region) {
        for (Area area : values()) {
            if (area.getIndex() == region) {
                return area;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public int getAddress() {
        return address;
    }
}
