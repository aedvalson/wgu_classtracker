package com.aedvalson.classtracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/*
*
* Andrew Edvalson - C198 Mobile Application Development
*
* https://github.com/aedvalson/wgu_classtracker
*
 */

public class DataProvider extends ContentProvider {

    private static final String AUTHORITY = "com.aedvalson.classtracker.dataprovider";
    private static final String TERM_PATH = "terms";
    public static final String COURSE_PATH = "courses";
    public static final Uri TERM_URI = Uri.parse("content://" + AUTHORITY + "/" + TERM_PATH);
    public static final Uri COURSE_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSE_PATH);

    // Constant to identify the requested operation
    private static final int TERMS = 1;
    public static final int TERMS_ID = 2;
    public static final int COURSES = 3;
    public static final int COURSES_ID = 4;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, TERM_PATH, TERMS);
        uriMatcher.addURI(AUTHORITY, TERM_PATH + "/#", TERMS_ID);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH, COURSES);
        uriMatcher.addURI(AUTHORITY, COURSE_PATH + "/#", COURSES_ID);
    }

    public static final String TERM_CONTENT_TYPE = "_Term";
    public static final String COURSE_CONTENT_TYPE = "COURSE";

    private SQLiteDatabase db;
    private String currentTable;

    @Override
    public boolean onCreate() {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(getContext());
        db = dbOpenHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.query(DBOpenHelper.TABLE_TERMS, DBOpenHelper.TERM_COLUMNS, selection, null, null, null, DBOpenHelper.TERM_TABLE_ID + " ASC");
            case COURSES:
                return db.query(DBOpenHelper.TABLE_COURSES, DBOpenHelper.COURSE_COLUMNS, selection, null, null, null, DBOpenHelper.COURSE_TABLE_ID + " ASC");
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }




    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = 0;
        switch (uriMatcher.match(uri)) {
            case TERMS:
                id = db.insert(DBOpenHelper.TABLE_TERMS, null, values);
                Log.d("TermListActivity", "Inserted _Term: " + id);
                return Uri.parse(TERM_PATH + "/" + id);
            case COURSES:
                id = db.insert(DBOpenHelper.TABLE_COURSES, null, values);
                Log.d("TermListActivity", "Inserted _Term: " + id);
                return Uri.parse(COURSE_PATH + "/" + id);
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.delete(DBOpenHelper.TABLE_TERMS, selection, selectionArgs);
            case COURSES:
                return db.delete(DBOpenHelper.TABLE_COURSES, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case TERMS:
                return db.update(DBOpenHelper.TABLE_TERMS, values, selection, selectionArgs);
            case COURSES:
                return db.update(DBOpenHelper.TABLE_COURSES, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }
}
