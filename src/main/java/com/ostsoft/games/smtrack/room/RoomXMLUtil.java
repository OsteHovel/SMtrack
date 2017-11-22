package com.ostsoft.games.smtrack.room;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class RoomXMLUtil {
    public static RoomsXML decodeRooms(String fileName) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(RoomsXML.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (RoomsXML) jaxbUnmarshaller.unmarshal(new FileInputStream(fileName));
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void encodeRooms(String fileName, RoomsXML configXML) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(RoomsXML.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(configXML, new FileOutputStream(fileName));
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
