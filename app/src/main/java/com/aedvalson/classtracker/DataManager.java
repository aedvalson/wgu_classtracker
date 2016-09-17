package com.aedvalson.classtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * Created by a_edv on 9/12/2016.
 */
public class DataManager {
    public static Uri insertTerm(Context context, String termName, String termStart, String termEnd) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_NAME, termName);
        values.put(DBOpenHelper.TERM_START, termStart);
        values.put(DBOpenHelper.TERM_END, termEnd);

        Uri termUri = context.getContentResolver().insert(DataProvider.TERM_URI, values);
        Log.d("DataManager", "Inserted Term: " + termUri.getLastPathSegment());

        return termUri;
    }

    public static Uri insertClass(Context context, long termId, String className, String classStart,
                                  String classEnd, String classMentor, String classMentorEmail,
                                  String classMentorPhone, ClassStatus status) {

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.CLASS_TERM_ID, termId);
        values.put(DBOpenHelper.CLASS_NAME, className);
        values.put(DBOpenHelper.CLASS_START, classStart);
        values.put(DBOpenHelper.CLASS_END, classEnd);
        values.put(DBOpenHelper.CLASS_MENTOR, classMentor);
        values.put(DBOpenHelper.CLASS_MENTOR_EMAIL, classMentorEmail);
        values.put(DBOpenHelper.CLASS_MENTOR_PHONE, classMentorPhone);
        values.put(DBOpenHelper.CLASS_STATUS, status.toString());

        Uri classUri = context.getContentResolver().insert(DataProvider.CLASS_URI, values);
        Log.d("DataManager", "Inserted Class: " + classUri.getLastPathSegment());

        return classUri;
    }

    public static Term getTerm(Context context, long id) {
        Cursor cursor = context.getContentResolver().query(DataProvider.TERM_URI, DBOpenHelper.TERM_COLUMNS, DBOpenHelper.TERM_TABLE_ID + "=" + id, null, null);

        cursor.moveToFirst();
        String termName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_NAME));
        String termStartDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_START));
        String termEndDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_END));

        Term t = new Term();
        t.termId = id;
        t.termName = termName;
        t.termStartDate = termStartDate;
        t.termEndDate = termEndDate;

        return t;
    }
}
