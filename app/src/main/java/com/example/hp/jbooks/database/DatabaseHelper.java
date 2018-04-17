package com.example.hp.jbooks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.jbooks.Subject_Contents;
import com.example.hp.jbooks.database.model.RecentsContract;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "recents_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create notes table
        sqLiteDatabase.execSQL(RecentsContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecentsContract.TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);

    }

    public long insertRecent(String name, String link) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(RecentsContract.COLUMN_NAME, name);
        values.put(RecentsContract.COLUMN_LINK, link);

        // insert row
        long id = db.insert(RecentsContract.TABLE_NAME, null, values);

        // close db connection
        db.close();
        Log.w("db", "inserted");
        // return newly inserted row id
        return id;
    }

    public boolean checkRecent(String link) {
        // get readable database as we are not inserting anything
        Log.w("db", "checked");

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(RecentsContract.TABLE_NAME,
                new String[]{RecentsContract.COLUMN_LINK},
                RecentsContract.COLUMN_LINK + "=?",
                new String[]{link}, null, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.close();
            Log.w("db", "checked not null");
            Log.w("db", String.valueOf(getRecentsCount()));

            return true;
        } else {
            cursor.close();
            Log.w("db", "checked null");
            Log.w("db", String.valueOf(getRecentsCount()));
            return false;
        }
    }

    public List<Subject_Contents> getAllRecents() {
        List<Subject_Contents> recents = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + RecentsContract.TABLE_NAME + " ORDER BY " +
                RecentsContract.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Subject_Contents doc = new Subject_Contents();
                doc.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex(RecentsContract.COLUMN_ID))));
                doc.setName(cursor.getString(cursor.getColumnIndex(RecentsContract.COLUMN_NAME)));
                doc.setLink(cursor.getString(cursor.getColumnIndex(RecentsContract.COLUMN_LINK)));
                doc.setTimestamp(cursor.getString(cursor.getColumnIndex(RecentsContract.COLUMN_TIMESTAMP)));

                recents.add(doc);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return recents;
    }

    public int getRecentsCount() {
        String countQuery = "SELECT  * FROM " + RecentsContract.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public void deleteRecent(Subject_Contents recent) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(RecentsContract.TABLE_NAME, RecentsContract.COLUMN_ID + " = ?",
                new String[]{String.valueOf(recent.getId())});
        db.close();
    }


}
