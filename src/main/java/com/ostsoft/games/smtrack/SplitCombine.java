package com.ostsoft.games.smtrack;

import com.ostsoft.games.smtrack.escapetimer.EscapeTimer;
import com.ostsoft.smsplit.xml.XMLUtil;
import com.ostsoft.smsplit.xml.config.ConfigXML;
import com.ostsoft.smsplit.xml.config.ItemBox;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SplitCombine {
    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        ConfigXML configXML = XMLUtil.decodeConfig("config.xml");

        List<ItemBox> digits = new ArrayList<>();
        for (ItemBox itemBox : configXML.itemBoxes.itemBox) {
            if (itemBox.name.startsWith("Timer ")) {
                digits.add(itemBox);
            }
        }

        // Combine
        List<ItemBox> numbers = new ArrayList<>();
        for (ItemBox itemBox : configXML.itemBoxes.itemBox) {
            if (itemBox.name.startsWith("Number ")) {
                numbers.add(itemBox);
            }
        }
        configXML.itemBoxes.itemBox.removeAll(numbers);
        if (!numbers.isEmpty()) {
            String name = numbers.get(0).name;
            int number = Integer.parseInt(name.substring(7, 8));
            digits.get(number).rectangles.clear();
            for (ItemBox itemBox : numbers) {
                digits.get(number).rectangles.addAll(itemBox.rectangles);
            }
        }


        // Split
        int index = 5;
        int[][] digitRectangles = EscapeTimer.getDigitRectangles();
        for (int i = 0; i < digitRectangles.length; i++) {
            ItemBox itemBox = new ItemBox();
            itemBox.name = "Number " + index + ":" + i;
            for (int j = 0; j < digitRectangles[i].length; j++) {
                itemBox.rectangles.add(digits.get(index).rectangles.get(digitRectangles[i][j]));
            }
            configXML.itemBoxes.itemBox.add(itemBox);
        }

        XMLUtil.encodeConfig("config.xml", configXML);
    }

}
