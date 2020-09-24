package com.hfad.ival_revisited;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    public static final String MISS_SCORE_COLUMN_NAME = "missed";
    public static final String MADE_SCORE_COLUMN_NAME = "made";
    public static final String MINUTE_COLUMN_NAME = "minute";
    public static final String MONTH_COLUMN_NAME = "month";
    private static final String TAG = "DBHelper";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table scores " +
                        "(id integer primary key,position text,made integer,missed integer,day integer,month integer,year integer,day_of_year integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS scores");
        onCreate(db);
    }

    public boolean insertContact (String position, int made, int missed, int day, int month, int year, int day_of_year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("position", position);
        contentValues.put("made", made);
        contentValues.put("missed", missed);
        contentValues.put("day", day);
        contentValues.put("month", month);
        contentValues.put("year", year);
        contentValues.put("day_of_year", day_of_year);
        db.insert("scores", null, contentValues);
        return true;
    }

    public ArrayList<Integer> shotsMadeWithinLastWeek(String myPosition) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        // int month1 = LocalDate.now().getMonthValue();
        int day1 = LocalDate.now().getDayOfYear();
        int day0 = day1 - 6;
        // Log.i(TAG, "shotsMadeWithinLastMonth: " + month1);
        for (int i = 0; i < 7; i++) {
            arrayList.add(0);
        }
        Log.i(TAG, "shotsMadeWithinLastWeek: " + day0 + " " + day1);

       Cursor res =  db.rawQuery("SELECT *,SUM(made) as sumMade FROM scores WHERE position = '" + myPosition + "' AND day_of_year BETWEEN '" + day0  + "' AND '" + day1 + "' GROUP BY day_of_year", null );
        //
            //   Cursor res = db.rawQuery("SELECT * FROM scores WHERE position = '" + myPosition + "'", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Log.i(TAG, "withinLastMonth: " + res);
            //arrayList.add(res.getInt(res.getColumnIndex("sumMade")));
            arrayList.set(res.getInt(res.getColumnIndex("day_of_year")) - day0, res.getInt(res.getColumnIndex("sumMade")));
            res.moveToNext();
        }
        return arrayList;
    }

    public ArrayList<Integer> shotsMissedWithinLastWeek(String myPosition) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        // int month1 = LocalDate.now().getMonthValue();
        int day1 = LocalDate.now().getDayOfYear();
        int day0 = day1 - 6;
        // Log.i(TAG, "shotsMadeWithinLastMonth: " + month1);
        for (int i = 0; i < 7; i++) {
            arrayList.add(0);
        }
        Log.i(TAG, "shotsMadeWithinLastWeek: " + day0 + " " + day1);

        Cursor res =  db.rawQuery("SELECT *,SUM(missed) as sumMissed FROM scores WHERE position = '" + myPosition + "' AND day_of_year BETWEEN '" + day0  + "' AND '" + day1 + "' GROUP BY day_of_year", null );
        //
        //   Cursor res = db.rawQuery("SELECT * FROM scores WHERE position = '" + myPosition + "'", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Log.i(TAG, "withinLastMonth: " + res);
            //arrayList.add(res.getInt(res.getColumnIndex("sumMade")));
            arrayList.set(res.getInt(res.getColumnIndex("day_of_year")) - day0, res.getInt(res.getColumnIndex("sumMissed")));
            res.moveToNext();
        }
        return arrayList;
    }

    public ArrayList<Integer> shotsMadeWithinLastMonth() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        int month1 = LocalDate.now().getMonthValue();
        int day1 = LocalDate.now().getDayOfMonth();
        Log.i(TAG, "shotsMadeWithinLastMonth: " + month1);
        for (int i = 0; i < day1; i++) {
            arrayList.add(0);
        }

        Cursor res =  db.rawQuery("SELECT *,SUM(phone) as sumPhone FROM scores WHERE month = '" + month1  + "' GROUP BY day", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Log.i(TAG, "withinLastMonth: " + res);

            arrayList.set(res.getInt(res.getColumnIndex("day"))-1, res.getInt(res.getColumnIndex("sumPhone")));
            res.moveToNext();
        }
        return arrayList;
    }

    public ArrayList<Integer> shotsMadeWithinLastYear() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        int year1 = LocalDate.now().getYear();
        int month1 = LocalDate.now().getMonthValue();
        Log.i(TAG, "shotsMadeWithinLastMonth: " + month1);

        for (int i = 0; i < month1; i++) {
            arrayList.add(0);
        }

        Cursor res =  db.rawQuery("SELECT *,SUM(phone) as sumPhone FROM scores WHERE year = '" + year1  + "' GROUP BY month", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Log.i(TAG, "withinLastMonth: " + res);

            arrayList.set(res.getInt(res.getColumnIndex("month"))-1, res.getInt(res.getColumnIndex("sumPhone")));
            res.moveToNext();
        }
        return arrayList;
    }
}
