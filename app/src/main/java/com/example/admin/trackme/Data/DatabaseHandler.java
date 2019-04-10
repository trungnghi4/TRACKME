package com.example.admin.trackme.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.admin.trackme.model.Session;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static DatabaseHandler sInstance;
    private static final String DATABASE_NAME = "sessionManager";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SESSION = "sessions";

    private static final String KEY_ID = "id";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_AVERAGESPEED = "averageSpeed";
    private static final String KEY_TIME = "time";
    private static final String KEY_IMAGE = "image";

    private Context context;
    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME, null  , DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SESSIONS_TABLE = "CREATE TABLE " + TABLE_SESSION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DISTANCE + " TEXT,"
                + KEY_AVERAGESPEED + " TEXT," + KEY_TIME + " TEXT,"
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

    public void addSession(Session session) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DISTANCE, session.getDistance());
        values.put(KEY_AVERAGESPEED, session.getAverageSpeed());
        values.put(KEY_TIME, session.getTime());
        values.put(KEY_IMAGE, session.getImage());

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
                session.setDistance(cursor.getString(1));
                session.setAverageSpeed(cursor.getString(2));
                session.setTime(cursor.getString(3));
                session.setImage(cursor.getBlob(4));
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
