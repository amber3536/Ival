package com.hfad.ival_revisited;

import android.content.SharedPreferences;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Position {
    private String mPosition;
    private int mPercentage;
    private int made;
    private int missed;



    public Position(String pos, int per, int md, int msd) {
        mPosition = pos;
        mPercentage = per;
        made = md;
        missed = msd;
    }

    public String getPosition() {
        return mPosition;
    }

    public int getPercentage() {
        return mPercentage;
    }

    public int getMade() { return made; }

    public int getMissed() { return missed; }

    public static ArrayList<Position> createContactsList(String names[], int percent[], int md[], int msd[]) {
        ArrayList<Position> contacts = new ArrayList<Position>();

        for (int i = 0; i < names.length; i++) {
            contacts.add(new Position(names[i], percent[i], md[i], msd[i]));
        }


        return contacts;
    }
}
