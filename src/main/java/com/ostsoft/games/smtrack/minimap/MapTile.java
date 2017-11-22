package com.ostsoft.games.smtrack.minimap;

import com.ostsoft.games.jsm.util.ByteStream;

public class MapTile {
    private int tile;
    private int flip;

    public void load(ByteStream stream) {
        this.tile = stream.readUnsignedByte();
        this.flip = stream.readUnsignedByte();
    }

    public int getTile() {
        return tile;
    }

    public void setTile(int tile) {
        this.tile = tile;
    }

    public int getFlip() {
        return flip;
    }

    public void setFlip(int flip) {
        this.flip = flip;
    }
}
