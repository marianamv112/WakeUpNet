package com.neurosky.algo_sdk_sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mariana on 08/09/17.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "localDB";

    // Results table name
    private static final String TABLE_RESULTS = "localResTable";

    // Results Table Columns names
    private static final String KEY_USERNAME = "username";
    private static final String KEY_SESSIONID = "sessionNr";
    private static final String KEY_MAXWORK = "maxWorkDuration";
    private static final String KEY_NROFBREAKS = "nrOfBreaks";
    private static final String KEY_ISSYNCED = "isSynced";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RESULTS_TABLE = "CREATE TABLE " + TABLE_RESULTS + "("
                + KEY_USERNAME + " TEXT," + KEY_SESSIONID + " TEXT,"
                + KEY_MAXWORK + " TEXT," + KEY_NROFBREAKS + " TEXT," + KEY_ISSYNCED +" TEXT" + ")";
        db.execSQL(CREATE_RESULTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);

        // Create tables again
        onCreate(db);
    }

    public void addResults(User user, Session session) {

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_SESSIONID, session.getSessionID().toString());
        values.put(KEY_MAXWORK, session.getMaxWorkDuration().toString());
        values.put(KEY_NROFBREAKS, session.getNumOfBreaks().toString());
        values.put(KEY_ISSYNCED, "0");

        SQLiteDatabase db = this.getWritableDatabase();
        // Inserting Row
        db.insert(TABLE_RESULTS, null, values);
        db.close(); // Closing database connection
    }


    public int updateResults(Results results, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, results.getResUser().getUsername());
        values.put(KEY_SESSIONID, results.getResSessionID());
        values.put(KEY_MAXWORK, results.getResMaxWorkDuration());
        values.put(KEY_NROFBREAKS, results.getResNumOfBreaks());
        values.put(KEY_ISSYNCED, status);

        // updating row
        return db.update(TABLE_RESULTS, values, KEY_USERNAME + " = ? AND " + KEY_SESSIONID + " = ?",
                new String[] {
                        results.getResUser().getUsername(),
                        String.valueOf(results.getResSessionID()) });
    }


    public int updateIsSynced(User user, Session session) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ISSYNCED, 1);

        // updating row
        return db.update(TABLE_RESULTS, values, KEY_USERNAME + " = ? AND " + KEY_SESSIONID + " = ?",
                new String[] {
                        user.getUsername(),
                        String.valueOf(session.getSessionID()) });
    }

    public void deleteResults(User user, Session session) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESULTS, KEY_USERNAME + " = ? AND " + KEY_SESSIONID + " = ?",
                new String[] { user.getUsername(),
                        String.valueOf(session.getSessionID()) });
        db.close();
    }

    public String userExist(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_RESULTS, new String[] { KEY_USERNAME}, KEY_USERNAME + "=?",
                new String[] { username }, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() >= 1)
            return cursor.getString(0);
        else
            return null;
    }

    public ArrayList<Session> getSessions(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_RESULTS + " WHERE username = '" + username + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList <Session> sessions = new ArrayList<Session>();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Session session = new Session(0,0,0);
                session.setSessionID (Integer.parseInt(cursor.getString(1)));
                session.setMaxWorkDuration (Integer.parseInt(cursor.getString(2)));
                session.setNumOfBreaks (Integer.parseInt(cursor.getString(3)));
                // Adding contact to list
                sessions.add(session);
            } while (cursor.moveToNext());
        }

        return sessions;
    }

    public Cursor getUnsyncedRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_RESULTS + " WHERE " + KEY_ISSYNCED + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public List<Results> getAllRows() {
        List<Results> rows = new ArrayList<Results>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RESULTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                User user = new User("");
                user.setUsername(cursor.getString(0));

                Results results = new Results(user, 0, 0, 0);

                results.setResSessionID(Integer.valueOf(cursor.getString(1)));
                results.setResMaxWorkDuration(Integer.valueOf(cursor.getString(2)));
                results.resSetNumOfBreaks(Integer.valueOf(cursor.getString(3)));


                // Adding contact to list
                rows.add(results);
            } while (cursor.moveToNext());
        }

        // return contact list
        return rows;
    }




}
