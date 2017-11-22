package com.ostsoft.games.smtrack.room;

import com.ostsoft.smsplit.util.IntegerUtil;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PointerAdapter extends XmlAdapter<String, Integer> {
    public Integer unmarshal(final String xml) throws Exception {
        return IntegerUtil.decodeWithoutOctal(xml);
    }

    public String marshal(final Integer object) throws Exception {
        return "0x" + Integer.toHexString(object).toUpperCase();
    }
}
