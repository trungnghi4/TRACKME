package com.example.admin.trackme.views;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sessionManager";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SESSION = "sessions";

    private static final String KEY_ID = "id";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_AVERAGESPEED = "averageSpeed";
    private static final String KEY_STARTTIME = "startTime";
    private static final String KEY_ENDTIME = "endTime";
    private static final String KEY_IMAGE = "image";

    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME, null  , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SESSIONS_TABLE = "CREATE TABLE " + TABLE_SESSION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DISTANCE + " TEXT,"
                + KEY_AVERAGESPEED + " TEXT," + KEY_STARTTIME + " TEXT,"
                + KEY_ENDTIME + " TEXT,"
                + KEY_IMAGE + " BLOB" + ")";
        db.execSQL(CREATE_SESSIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);

// Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addSession(double distance,float averagespeed,long starttime,long endtime, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DISTANCE, distance);
        values.put(KEY_AVERAGESPEED, averagespeed);
        values.put(KEY_STARTTIME, starttime);
        values.put(KEY_ENDTIME, endtime);
        values.put(KEY_IMAGE, image);

// Inserting Row
        db.insert(TABLE_SESSION, null, values);
        db.close(); // Closing database connection
    }


    // Getting All Contacts
    public List<Session> getAllContacts() {
        List<Session> sessionsList = new ArrayList<Session>();
        // Select All Query
        String selectQuery = "SELECT * FROM sessions";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Session session = new Session();
                session.setId(Integer.parseInt(cursor.getString(0)));
                session.setDistance(cursor.getDouble(1));
                session.setAverageSpeed(cursor.getLong(2));
                session.setStartTime(cursor.getLong(3));
                session.setEndTime(cursor.getLong(4));
                session.setImage(cursor.getBlob(5));
        // Adding contact to list
                sessionsList.add(session);
            } while (cursor.moveToNext());
        }
        // close inserting data from database
        db.close();
        // return contact list
        return sessionsList;

    }


//    // Getting contacts Count
//    public int getContactsCount() {
//        String countQuery = "SELECT * FROM " + TABLE_CONTACTS;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//// return count
//        return cursor.getCount();
//    }
}
