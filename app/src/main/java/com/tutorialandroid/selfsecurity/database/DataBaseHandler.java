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
    public static final String DATABASE_NAME = "contacts.db";
    public static final String TABLE_CONTACTS_NAME = "contacts";
    public static final String TABLE_MESSAGE_NAME = "message";
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_TIME = "time";
    public static final String KEY_MESSAGE = "message";
    public static final String[] ALL_CONTACTS_COLUMNS =
            {KEY_ID,KEY_NAME,KEY_NUMBER};
    public static final String[] ALL_MESSAGE_COLUMNS =
            {KEY_ID,KEY_TIME,KEY_MESSAGE,KEY_NUMBER};
    private static final String CREATE_CONTACT_TABLE =
            " CREATE TABLE " + TABLE_CONTACTS_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_NAME + " TEXT ," +
                    KEY_NUMBER + " TEXT " + " )";
    private static final String CREATE_MESSAGE_TABLE =
            " CREATE TABLE " + TABLE_MESSAGE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_TIME + " TEXT ," +
                    KEY_MESSAGE + " TEXT ," +
                    KEY_NUMBER + " TEXT " + " )";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACT_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE_NAME);
        onCreate(db);
    }


//    public void savedetails(ContactDetails contactDetails) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, contactDetails.getName());
//        values.put(KEY_NUMBER, contactDetails.getNumber());
//        db.insert(TABLE_NAME, null, values);
//        db.close();
//
//    }
//
//    public ArrayList<ContactDetails> getContacts() {
//        ArrayList<ContactDetails> detailses = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String getQuery = "SELECT * FROM " + TABLE_NAME;
//        Cursor cursor = db.rawQuery(getQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                ContactDetails contactDetails = new ContactDetails();
//                contactDetails.setId(Integer.parseInt(cursor.getString(0)));
//                contactDetails.setName(cursor.getString(1));
//                contactDetails.setNumber(cursor.getString(2));
//                detailses.add(contactDetails);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return detailses;
//
//
//    }
//

   public void deleteRec(String ids) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS_NAME, KEY_ID + " = ? ", new String[]{ids});
        db.close();
    }
}