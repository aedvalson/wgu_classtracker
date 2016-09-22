package com.aedvalson.classtracker;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by a_edv on 9/22/2016.
 */
public class _CourseNote {
    private long courseNoteId;
    public long courseId;
    public String text;

    public _CourseNote(long courseNoteId) {
        this.courseNoteId = courseNoteId;
    }

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSE_NOTE_TEXT, text);
        values.put(DBOpenHelper.COURSE_NOTE_COURSE_ID, courseId);

        context.getContentResolver().update(DataProvider.COURSE_NOTE_URI, values, DBOpenHelper.COURSE_NOTE_TABLE_ID + "=" + courseNoteId, null);
    }
}
