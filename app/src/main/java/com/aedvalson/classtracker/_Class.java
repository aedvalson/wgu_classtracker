package com.aedvalson.classtracker;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by a_edv on 9/17/2016.
 */
public class _Class {
    public long classId;
    public String className;
    public String classStart;
    public String classEnd;
    public String classMentor;
    public String classMentorEmail;
    public String classMentorPhone;
    public ClassStatus status;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.CLASS_NAME, className);
        values.put(DBOpenHelper.CLASS_START, classStart);
        values.put(DBOpenHelper.CLASS_END, classEnd);
        values.put(DBOpenHelper.CLASS_MENTOR, classMentor);
        values.put(DBOpenHelper.CLASS_MENTOR_EMAIL, classMentorEmail);
        values.put(DBOpenHelper.CLASS_MENTOR_PHONE, classMentorPhone);

        context.getContentResolver().update(DataProvider.CLASS_URI, values, DBOpenHelper.CLASS_TABLE_ID + "=" + classId, null);
    }

}
