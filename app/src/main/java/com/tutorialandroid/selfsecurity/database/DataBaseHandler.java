package com.tutorialandroid.selfsecurity.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tutorialandroid.selfsecurity.model.ContactDetails;

import java.util.ArrayList;

/**
 * Created by VenkatPc on 5/25/2017.
 */
public class DataBaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contact";
    public static final String TABLE_NAME = "contactdetails";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_NUMBER = "number";

    private static final String CREATE_TABLE =
            " CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    KEY_NAME + " TEXT ," +
                    KEY_NUMBER + " TEXT " + " )";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void savedetails(ContactDetails contactDetails) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contactDetails.getName());
        values.put(KEY_NUMBER, contactDetails.getNumber());
        db.insert(TABLE_NAME, null, values);
        db.close();

    }

    public ArrayList<ContactDetails> getContacts() {
        ArrayList<ContactDetails> detailses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String getQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(getQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ContactDetails contactDetails = new ContactDetails();
                contactDetails.setId(Integer.parseInt(cursor.getString(0)));
                contactDetails.setName(cursor.getString(1));
                contactDetails.setNumber(cursor.getString(2));
                detailses.add(contactDetails);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return detailses;


    }

    public void deleteRec(String ids) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ? ", new String[]{ids});
        db.close();
    }
}