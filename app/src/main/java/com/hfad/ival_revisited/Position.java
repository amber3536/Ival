package com.hfad.ival_revisited;

import java.util.ArrayList;

public class Position {
    private String mPosition;
    private int mPercentage;

    public Position(String pos, int per) {
        mPosition = pos;
        mPercentage = per;
    }

    public String getPosition() {
        return mPosition;
    }

    public int getPercentage() {
        return mPercentage;
    }

    private static int lastContactId = 0;

    public static ArrayList<Position> createContactsList(int numContacts) {
        ArrayList<Position> contacts = new ArrayList<Position>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Position("Person " + ++lastContactId, 3));
        }

        return contacts;
    }
}
