package com.ostsoft.games.smtrack.room;

import com.ostsoft.games.jsm.room.Room;
import com.ostsoft.games.smtrack.minimap.Area;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class RoomXML {
    public String name = "";
    public String altname;
    public String deanydName = "";
    @XmlJavaTypeAdapter(type = int.class, value = PointerAdapter.class)
    public int address;
    public Area area;
    public String description = "";

    @XmlTransient
    public Room room;

    public String getDisplayName() {
        if (name != null && !name.equals("")) {
            return name;
        }
        if (altname != null && !altname.equals("")) {
            return altname;
        }
        if (deanydName != null && !deanydName.equals("")) {
            return deanydName;
        }

        return "Room 0x" + Integer.toHexString(address).toUpperCase();
    }

    public Area getArea() {
        return area;
    }
}
