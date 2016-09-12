package com.aedvalson.classtracker;

import android.content.ContentValues;
import android.content.Context;
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
        Log.d("MainActivity", "Inserted Term: " + termUri.getLastPathSegment());

        return termUri;
    }
}
