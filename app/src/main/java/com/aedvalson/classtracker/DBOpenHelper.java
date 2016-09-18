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
    private static final int DATABASE_VERSION = 4;

    //Constants for identifying table and columns

    // _Term Table
    public static final String TABLE_TERMS = "terms";
    public static final String TERM_TABLE_ID = "_id";
    public static final String TERM_NAME = "termName";
    public static final String TERM_START = "termStart";
    public static final String TERM_END = "termEnd";
    public static final String TERM_CREATED = "_created";

    // Class Table
    public static final String TABLE_COURSES = "courses";
    public static final String COURSE_TERM_ID = "termId";
    public static final String COURSE_TABLE_ID = "_id";
    public static final String COURSE_NAME = "courseName";
    public static final String COURSE_START = "courseStart";
    public static final String COURSE_END = "courseEnd";
    public static final String COURSE_CREATED = "_created";
    public static final String COURSE_DESCRIPTION = "courseDescription";
    public static final String COURSE_MENTOR = "courseMentor";
    public static final String COURSE_MENTOR_PHONE = "courseMentorPhone";
    public static final String COURSE_MENTOR_EMAIL = "courseMentorEmail";
    public static final String COURSE_STATUS = "courseStatus";

    // course Note Table
    public static final String TABLE_COURSE_NOTES = "course_notes";
    public static final String COURSE_NOTE_TABLE_ID = "_id";
    public static final String COURSE_NOTE_COURSE_ID = "courseId";
    public static final String COURSE_NOTE_TEXT = "text";
    public static final String COURSE_NOTE_CREATED = "courseNoteCreated";

    public static final String[] TERM_COLUMNS = {TERM_TABLE_ID, TERM_NAME, TERM_START,
            TERM_END, TERM_CREATED};

    public static final String[] COURSE_COLUMNS = {COURSE_TABLE_ID, COURSE_NAME, COURSE_START,
            COURSE_END, COURSE_CREATED, COURSE_DESCRIPTION, COURSE_MENTOR, COURSE_MENTOR_EMAIL,
            COURSE_MENTOR_PHONE};

    public static final String[] COURSE_NOTE_COLUMNS = {COURSE_NOTE_TABLE_ID, COURSE_NOTE_COURSE_ID,
            COURSE_NOTE_TEXT};

    public static final String COURSE_NOTE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_COURSE_NOTES + " (" +
                    COURSE_NOTE_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_NOTE_COURSE_ID + " INTEGER, " +
                    COURSE_NOTE_TEXT + " TEXT, " +
                    COURSE_NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COURSE_NOTE_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COURSE_TABLE_ID + ")" +
                    ")";

    //SQL to create term table
    private static final String TERM_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TERMS + " (" +
                    TERM_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_NAME + " TEXT, " +
                    TERM_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    TERM_START + " DATE, " +
                    TERM_END + " DATE" +
                    ")";

    // SQL to create course table
    private static final String COURSE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_COURSES + " (" +
                    COURSE_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_TERM_ID + " INTEGER, " +
                    COURSE_NAME + " TEXT, " +
                    COURSE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    COURSE_START + " DATE, " +
                    COURSE_END + " DATE, " +
                    COURSE_DESCRIPTION + " TEXT, " +
                    COURSE_MENTOR + " TEXT, " +
                    COURSE_MENTOR_EMAIL + " TEXT, " +
                    COURSE_MENTOR_PHONE + " TEXT, " +
                    COURSE_STATUS + " TEXT, " +
                    "FOREIGN KEY(" + COURSE_TERM_ID + ") REFERENCES " + TABLE_TERMS + "(" + TERM_TABLE_ID + ")" +
                    ")";


    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TERM_TABLE_CREATE);
        db.execSQL(COURSE_TABLE_CREATE);
        db.execSQL(COURSE_NOTE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        onCreate(db);
    }
}
