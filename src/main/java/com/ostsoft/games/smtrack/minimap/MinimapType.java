package com.ostsoft.games.smtrack.minimap;

public enum MinimapType {
    NOTHING(0b00000, 0x1F, 0b00),

    ROOM(0b10000, 0x1B, 0b00),
    ROOM_FLIP(0b10000, 0x1B, 0b01),
    ROOM_FLIP2(0b10000, 0x1B, 0b10),
    ROOM_FLIP_ALL(0b10000, 0x1B, 0b11),
    WALL_UP(0b11000, 0x26, 0b00),
    WALL_UP_FLIP(0b11000, 0x26, 0b01),
    WALL_DOWN(0b10010, 0x26, 0b10),
    WALL_DOWN_FLIP(0b10010, 0x26, 0b11),
    WALL_LEFT(0b10001, 0x27, 0b01),
    WALL_LEFT_FLIP(0b10001, 0x27, 0b11),
    WALL_RIGHT(0b10100, 0x27, 0b00),
    WALL_RIGHT_FLIP(0b10100, 0x27, 0b10),

    WALL_UP_LEFT(0b11001, 0x25, 0b00),
    WALL_UP_RIGHT(0b11100, 0x25, 0b01),
    WALL_DOWN_LEFT(0b10011, 0x25, 0b10),
    WALL_DOWN_RIGHT(0b10110, 0x25, 0b11),

    WALL_UP_DOWN(0b11010, 0x22, 0b00),
    WALL_UP_DOWN_FLIP(0b11010, 0x22, 0b01),
    WALL_LEFT_RIGHT(0b10101, 0x23, 0b00),
    WALL_LEFT_RIGHT_FLIP(0b10101, 0x23, 0b10),

    WALL_UP_LEFT_DOWN(0b11011, 0x21, 0b00),
    WALL_UP_RIGHT_DOWN(0b11110, 0x21, 0b01),
    WALL_UP_RIGHT_DOWN_FLIP(0b11110, 0x21, 0b11),

    WALL_LEFT_UP_RIGHT(0b11101, 0x24, 0b00),
    WALL_LEFT_DOWN_RIGHT(0b10111, 0x24, 0b10),

    WALL_ALL(0b11111, 0x20, 0b00),
    WALL_ALL_FLIP_X(0b11111, 0x20, 0b01),
    WALL_ALL_FLIP_Y(0b11111, 0x20, 0b10),

    ELEVATOR_SHAFT_HORIZONTAL(0b10000, 0xCE, 0b00),
    ELEVATOR_SHAFT_VERTICAL(0b10000, 0x6D, 0b00),
    ELEVATOR_PLATFORM_DOWN(0b10010, 0x5F, 0b00),
    ELEVATOR_PLATFORM(0b0000, 0x10, 0b00),
    ELEVATOR_PLATFORM_LEFT_RIGHT(0b10101, 0x10, 0b10),
    ELEVATOR_TOP(0b11101, 0x4F, 0b00),
    ARROW_UP(0b00000, 0x11, 0b00),
    ARROW_DOWN(0b00000, 0x11, 0b10),

    SAVESTATION(0b11111, 0x4D, 0b00),

    ITEM_WALL(0b11111, 0x6F, 0b00),
    ITEM_LEFT(0b10001, 0x77, 0b00),
    ITEM_RIGHT(0b10100, 0x77, 0b01),
    ITEM_UP(0b11000, 0x76, 0b00),
    ITEM_DOWN(0b10010, 0x76, 0b10),
    ITEM_UP_DOWN(0b11010, 0x5E, 0b00),
    ITEM_UP_LEFT(0b11001, 0x8E, 0b00),
    ITEM_UP_RIGHT(0b11100, 0x8E, 0b01),
    ITEM_DOWN_LEFT(0b10011, 0x8E, 0b10),
    ITEM_DOWN_RIGHT(0b10110, 0x8E, 0b11),
    ITEM_UP_LEFT_RIGHT(0b11101, 0x6E, 0b00),
    ITEM_DOWN_LEFT_RIGHT(0b10111, 0x6E, 0b10),
    ITEM_UP_LEFT_DOWN(0b11011, 0x8F, 0b00),
    ITEM_UP_RIGHT_DOWN(0b11110, 0x8F, 0b01),


//    END(0, 0, 0)
    ;

    private final int identifier;
    private final int tile;
    private final int flip;

    MinimapType(int identifier, int tile, int flip) {
        this.identifier = identifier;
        this.tile = tile;
        this.flip = flip;
    }

    public static MinimapType getMiniType(int identifier) {
        for (MinimapType miniType : values()) {
            if (miniType.identifier == identifier) {
                return miniType;
            }
        }
        return null;
    }

    public static MinimapType getMiniType(int tile, int flip) {
        int onlyFlip = flip >> 6;

        for (MinimapType miniType : values()) {
            if (miniType.tile == tile && miniType.flip == onlyFlip) {
                return miniType;
            }
        }

        return NOTHING;
    }

    public int getValue() {
        return (tile << 4) + flip;
    }

    public int getTile() {
        return tile;
    }

    public int getFlip() {
        return flip;
    }

    public int getIdentifier() {
        return identifier;
    }
}