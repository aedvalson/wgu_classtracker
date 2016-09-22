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
        Log.d("DataManager", "Inserted _Term: " + termUri.getLastPathSegment());

        return termUri;
    }

    public static Uri insertCourse(Context context, long termId, String courseName, String courseStart,
                                  String courseEnd, String courseMentor, String courseMentorEmail,
                                  String courseMentorPhone, CourseStatus status) {

        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_TERM_ID, termId);
        values.put(DBOpenHelper.COURSE_NAME, courseName);
        values.put(DBOpenHelper.COURSE_START, courseStart);
        values.put(DBOpenHelper.COURSE_END, courseEnd);
        values.put(DBOpenHelper.COURSE_MENTOR, courseMentor);
        values.put(DBOpenHelper.COURSE_MENTOR_EMAIL, courseMentorEmail);
        values.put(DBOpenHelper.COURSE_MENTOR_PHONE, courseMentorPhone);
        values.put(DBOpenHelper.COURSE_STATUS, status.toString());

        Uri courseUri = context.getContentResolver().insert(DataProvider.COURSE_URI, values);
        Log.d("DataManager", "Inserted Class: " + courseUri.getLastPathSegment());

        return courseUri;
    }

    public static _Term getTerm(Context context, long id) {
        Cursor cursor = context.getContentResolver().query(DataProvider.TERM_URI, DBOpenHelper.TERM_COLUMNS, DBOpenHelper.TERM_TABLE_ID + "=" + id, null, null);

        cursor.moveToFirst();
        String termName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_NAME));
        String termStartDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_START));
        String termEndDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERM_END));

        _Term t = new _Term();
        t.termId = id;
        t.termName = termName;
        t.termStartDate = termStartDate;
        t.termEndDate = termEndDate;

        return t;
    }

    public static _Course getCourse(Context context, long courseId) {
        Cursor cursor = context.getContentResolver().query(DataProvider.COURSE_URI, DBOpenHelper.COURSE_COLUMNS, DBOpenHelper.COURSE_TABLE_ID + "=" + courseId, null, null);

        cursor.moveToFirst();
        _Course c = new _Course();
        c.courseId = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COURSE_TABLE_ID));
        c.courseName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NAME));
        c.courseStart = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_START));
        c.courseEnd = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_END));
        c.courseMentor = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR));
        c.courseMentorEmail = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR_EMAIL));
        c.courseMentorPhone = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_MENTOR_PHONE));

        return c;
    }

    public static Uri insertCourseNote(Context context, long courseId, String text) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTE_TEXT, text);
        values.put(DBOpenHelper.COURSE_NOTE_COURSE_ID, courseId);

        Uri courseUri = context.getContentResolver().insert(DataProvider.COURSE_NOTE_URI, values);
        Log.d("DataManager", "Inserted Course Note: " + courseUri.getLastPathSegment());

        return courseUri;
    }

    public static _CourseNote getCourseNote(Context context, long courseNoteId) {
        Cursor cursor = context.getContentResolver().query(DataProvider.COURSE_NOTE_URI, DBOpenHelper.COURSE_NOTE_COLUMNS, DBOpenHelper.COURSE_NOTE_TABLE_ID + "=" + courseNoteId, null, null);

        cursor.moveToFirst();
        _CourseNote c = new _CourseNote(courseNoteId);
        c.text = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSE_NOTE_TEXT));
        c.courseId = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COURSE_NOTE_COURSE_ID));
        return c;
    }


}
