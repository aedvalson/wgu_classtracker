package com.aedvalson.classtracker;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by a_edv on 9/22/2016.
 */
public class _Assessment {
    private long assessmentId;
    public String code;
    public String name;
    public long courseId;
    public String description;
    public String dateTime;

    public _Assessment(long id) {
        assessmentId = id;
    }
    public _Assessment() {}

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_COURSE_ID, courseId);
        values.put(DBOpenHelper.ASSESSMENT_CODE, code);
        values.put(DBOpenHelper.ASSESSMENT_NAME, name);
        values.put(DBOpenHelper.ASSESSMENT_DATE_TIME, dateTime);
        values.put(DBOpenHelper.ASSESSMENT_DESCRIPTION, description);

        context.getContentResolver().update(DataProvider.ASSESSMENT_URI, values, DBOpenHelper.ASSESSMENT_TABLE_ID + "=" + assessmentId, null);
    }
}
