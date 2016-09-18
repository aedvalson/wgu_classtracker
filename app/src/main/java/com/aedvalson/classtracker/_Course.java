package com.aedvalson.classtracker;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by a_edv on 9/17/2016.
 */
public class _Course {
    public long courseId;
    public String courseName;
    public String courseStart;
    public String courseEnd;
    public String courseMentor;
    public String courseMentorEmail;
    public String courseMentorPhone;
    public CourseStatus status;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NAME, courseName);
        values.put(DBOpenHelper.COURSE_START, courseStart);
        values.put(DBOpenHelper.COURSE_END, courseEnd);
        values.put(DBOpenHelper.COURSE_MENTOR, courseMentor);
        values.put(DBOpenHelper.COURSE_MENTOR_EMAIL, courseMentorEmail);
        values.put(DBOpenHelper.COURSE_MENTOR_PHONE, courseMentorPhone);

        context.getContentResolver().update(DataProvider.COURSE_URI, values, DBOpenHelper.COURSE_TABLE_ID + "=" + courseId, null);
    }

}
