package com.aedvalson.classtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


    /*
    *
    * Andrew Edvalson - C198 Mobile Application Development
    *
    * https://github.com/aedvalson/wgu_classtracker
    *
     */

public class DBOpenHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "wgu_classes.db";
    private static final int DATABASE_VERSION = 2;

    //Constants for identifying table and columns

    // Term Table
    public static final String TABLE_TERMS = "terms";
    public static final String TERM_TABLE_ID = "_id";
    public static final String TERM_NAME = "termName";
    public static final String TERM_START = "termStart";
    public static final String TERM_END = "termEnd";
    public static final String TERM_CREATED = "_created";

    // Class Table
    public static final String TABLE_CLASSES = "classes";
    public static final String CLASS_TERM_ID = "termId";
    public static final String CLASS_TABLE_ID = "_id";
    public static final String CLASS_NAME = "className";
    public static final String CLASS_START = "classStart";
    public static final String CLASS_END = "classEnd";
    public static final String CLASS_CREATED = "_created";
    public static final String CLASS_DESCRIPTION = "classDescription";
    public static final String CLASS_MENTOR = "classMentor";
    public static final String CLASS_MENTOR_PHONE = "classMentorPhone";
    public static final String CLASS_MENTOR_EMAIL = "classMentorEmail";
    public static final String CLASS_STATUS = "classStatus";

    public static final String[] TERM_COLUMNS = {TERM_TABLE_ID, TERM_NAME, TERM_START,
            TERM_END, TERM_CREATED};

    public static final String[] CLASS_COLUMNS = {CLASS_TABLE_ID, CLASS_NAME, CLASS_START,
            CLASS_END, CLASS_CREATED, CLASS_DESCRIPTION, CLASS_MENTOR, CLASS_MENTOR_EMAIL,
            CLASS_MENTOR_PHONE};

    //SQL to create term table
    private static final String TERM_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TERMS + " (" +
                    TERM_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_NAME + " TEXT, " +
                    TERM_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    TERM_START + " DATE, " +
                    TERM_END + " DATE" +
                    ")";

    // SQL to create class table
    private static final String CLASS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_CLASSES + " (" +
                    CLASS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CLASS_TERM_ID + " INTEGER, " +
                    CLASS_NAME + " TEXT, " +
                    CLASS_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    CLASS_START + " DATE, " +
                    CLASS_END + " DATE, " +
                    CLASS_DESCRIPTION + " TEXT, " +
                    CLASS_MENTOR + " TEXT, " +
                    CLASS_MENTOR_EMAIL + " TEXT, " +
                    CLASS_MENTOR_PHONE + " TEXT, " +
                    CLASS_STATUS + " TEXT, " +
                    "FOREIGN KEY(" + CLASS_TERM_ID + ") REFERENCES " + TABLE_TERMS + "(" + TERM_TABLE_ID + ")" +
                    ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TERM_TABLE_CREATE);
        db.execSQL(CLASS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        onCreate(db);
    }
}
