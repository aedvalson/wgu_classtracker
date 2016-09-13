package com.aedvalson.classtracker;

import android.content.ContentValues;
import android.content.Context;

/**
 * Created by a_edv on 9/13/2016.
 */
public class Term {
    public long termId;
    public String termName;
    public String termStartDate;
    public String termEndDate;

    public void saveChanges(Context context) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERM_NAME, termName);
        values.put(DBOpenHelper.TERM_START, termStartDate);
        values.put(DBOpenHelper.TERM_END, termEndDate);
        context.getContentResolver().update(DataProvider.TERM_URI, values, DBOpenHelper.TERM_TABLE_ID + "=" + termId, null);
    }
}
