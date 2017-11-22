package com.ostsoft.games.smtrack.room;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "rooms")
public class RoomsXML {
    @XmlElement(name = "room")
    public List<RoomXML> rooms = new ArrayList<>();

}
