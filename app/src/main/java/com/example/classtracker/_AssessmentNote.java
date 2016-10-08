package com.example.classtracker;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by a_edv on 10/1/2016.
 */

public class _AssessmentNote {
    private long assessmentNoteId;
    public long assessmentId;
    public String text;

    public _AssessmentNote(long assessmentNoteId) {
        this.assessmentNoteId = assessmentNoteId;
    }

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENT_NOTE_TEXT, text);
        values.put(DBOpenHelper.ASSESSMENT_NOTE_ASSESSMENT_ID, assessmentId);

        context.getContentResolver().update(DataProvider.ASSESSMENT_NOTE_URI, values, DBOpenHelper.ASSESSMENT_NOTE_TABLE_ID + "=" + assessmentNoteId, null);
    }
}
