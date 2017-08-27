package com.tutorialandroid.selfsecurity.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class ContactsProvider extends ContentProvider {

    private static final String AUTHORITY = "com.tutorialandroid.selfsecurity";
    private static final String BASE_PATH = "contacts";
    private static final String MESSAGE_PATH = "message";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final Uri MESSAGE_URI = Uri.parse("content://" + AUTHORITY + "/" + MESSAGE_PATH);


    private static final int CONTACTS = 1;
    private static final int CONTACT_ID = 2;
    private static final int MESSAGE = 3;
    private static final int MESSAGE_ID = 4;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, CONTACTS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CONTACT_ID);
        uriMatcher.addURI(AUTHORITY, MESSAGE_PATH, MESSAGE);
        uriMatcher.addURI(AUTHORITY, MESSAGE_PATH + "/#", MESSAGE_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DataBaseHandler helper = new DataBaseHandler(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                cursor = database.query(DataBaseHandler.TABLE_CONTACTS_NAME, DataBaseHandler.ALL_CONTACTS_COLUMNS,
                        s, null, null, null, DataBaseHandler.KEY_NAME + " ASC");
                break;
            case MESSAGE:
                cursor = database.query(DataBaseHandler.TABLE_MESSAGE_NAME, DataBaseHandler.ALL_MESSAGE_COLUMNS,
                        s, null, null, null, DataBaseHandler.KEY_TIME + " ASC");
                break;

            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                return "vnd.android.cursor.dir/contacts";

            case MESSAGE:
                return "vnd.android.cursor.dir/message";

            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri _uri = null;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                long id = database.insert(DataBaseHandler.TABLE_CONTACTS_NAME, null, contentValues);

                if (id > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI, id);
                    getContext().getContentResolver().notifyChange(_uri, null);

                }

break;
            case MESSAGE:
                long id1 = database.insert(DataBaseHandler.TABLE_MESSAGE_NAME, null, contentValues);

                if (id1 > 0) {
                    _uri = ContentUris.withAppendedId(MESSAGE_URI, id1);
                    getContext().getContentResolver().notifyChange(_uri, null);

                }


        }
        return _uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int delCount = 0;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                delCount = database.delete(DataBaseHandler.TABLE_CONTACTS_NAME, s, strings);
                break;
            case MESSAGE:
                delCount = database.delete(DataBaseHandler.TABLE_MESSAGE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updCount = 0;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                updCount = database.update(DataBaseHandler.TABLE_CONTACTS_NAME, contentValues, s, strings);
                break;
            case MESSAGE:
                updCount = database.update(DataBaseHandler.TABLE_MESSAGE_NAME, contentValues, "_id=?", new String[] {String.valueOf(0)});
                break;
            default:
                throw new IllegalArgumentException("This is an Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updCount;
    }
}
