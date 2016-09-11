package com.aedvalson.classtracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by a_edv on 9/11/2016.
 */
public class DataProvider extends ContentProvider {

    private static final String AUTHORITY = "com.aedvalson.classtracker.dataprovider";
    private static final String TERM_PATH = "terms";
    public static final String CLASS_PATH = "classes";
    public static final Uri TERM_URI = Uri.parse("content://" + AUTHORITY + "/" + TERM_PATH);
    public static final Uri CLASS_URI = Uri.parse("content://" + AUTHORITY + "/" + CLASS_PATH);

    // Constant to identify the requested operation
    private static final int TERMS = 1;
    public static final int TERMS_ID = 2;
    public static final int CLASSES = 3;
    public static final int CLASSES_ID = 4;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, TERM_PATH, TERMS);
        uriMatcher.addURI(AUTHORITY, TERM_PATH + "/#", TERMS_ID);
        uriMatcher.addURI(AUTHORITY, CLASS_PATH, CLASSES);
        uriMatcher.addURI(AUTHORITY, CLASS_PATH + "/#", CLASSES_ID);
    }

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
                return db.query(DBOpenHelper.TABLE_TERMS, DBOpenHelper.TERM_COLUMNS, selection, null, null, null, DBOpenHelper.TERM_CREATED + " DESC");
            case CLASSES:
                return db.query(DBOpenHelper.TABLE_CLASSES, DBOpenHelper.CLASS_COLUMNS, selection, null, null, null, DBOpenHelper.CLASS_CREATED + " DESC");
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
                return Uri.parse(TERM_PATH + "/" + id);
            case CLASSES:
                id = db.insert(DBOpenHelper.TABLE_CLASSES, null, values);
                return Uri.parse(CLASS_PATH + "/" + id);
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
            case CLASSES:
                return db.delete(DBOpenHelper.TABLE_CLASSES, selection, selectionArgs);
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
            case CLASSES:
                return db.update(DBOpenHelper.TABLE_CLASSES, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }
}
