package com.aedvalson.classtracker;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class TermListActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<Cursor> {

    /*
    *
    * Andrew Edvalson - C198 Mobile Application Development
    *
    * https://github.com/aedvalson/wgu_classtracker
    *
     */

    public static final int TERM_EDITOR_ACTIVITY_CODE = 11111;
    public static final int TERM_VIEWER_ACTIVITY_CODE = 22222;

    private CursorAdapter ca;
    private DataProvider db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] from = { DBOpenHelper.TERM_NAME, DBOpenHelper.TERM_START, DBOpenHelper.TERM_END };
        int[] to = { R.id.tvTerm, R.id.tvTermStartDate, R.id.tvTermEndDate };

        ca = new SimpleCursorAdapter(this, R.layout.term_list_item, null, from, to, 0);
        db = new DataProvider();

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(ca);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TermListActivity.this, TermViewerActivity.class);
                Uri uri = Uri.parse(DataProvider.TERM_URI + "/" + id);
                intent.putExtra(DataProvider.TERM_CONTENT_TYPE, uri);
                startActivityForResult(intent, TERM_VIEWER_ACTIVITY_CODE);
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            restartLoader();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_create_sample:
                return createSampleData();
            case R.id.action_delete_all_terms:
                return deleteAllTerms();
            case R.id.action_create_test_alarm:
                return createTestAlarm();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean deleteAllTerms() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            // Do the actual delete
                            getContentResolver().delete(DataProvider.TERM_URI, null, null);
                            getContentResolver().delete(DataProvider.COURSE_URI, null, null);
                            getContentResolver().delete(DataProvider.COURSE_NOTE_URI, null, null);
                            getContentResolver().delete(DataProvider.ASSESSMENT_URI, null, null);
                            getContentResolver().delete(DataProvider.ASSESSMENT_NOTE_URI, null, null);
                            restartLoader();

                            // Notify that delete was completed
                            Toast.makeText(TermListActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

        return true;
    }

    private boolean createSampleData() {
        Uri term1Uri = DataManager.insertTerm(this, "Spring 2016", "2016-01-01", "2016-06-30");
        Uri term2Uri = DataManager.insertTerm(this, "Fall 2016", "2016-07-01", "2016-12-31");
        Uri term3Uri = DataManager.insertTerm(this, "Spring 2017", "2017-01-01", "2017-06-30");
        Uri term4Uri = DataManager.insertTerm(this, "Fall 2017", "2017-07-01", "2017-12-31");
        Uri term5Uri = DataManager.insertTerm(this, "Spring 2018", "2018-01-01", "2018-06-30");
        Uri term6Uri = DataManager.insertTerm(this, "Fall 2018", "2018-07-01", "2018-12-31");

        Uri course1Uri = DataManager.insertCourse(this, Long.parseLong(term1Uri.getLastPathSegment()),
                "C196: Mobile Application Development", "2016-01-01", "2016-02-01",
                "Pubali Banerjee", "(801) 924-4710", "pubali.banerjee@wgu.edu",
                CourseStatus.IN_PROGRESS);

        DataManager.insertCourse(this, Long.parseLong(term1Uri.getLastPathSegment()),
                "C179: Business of IT - Applications", "2016-02-01", "2016-03-01",
                "Course Mentor Group", " ", "cmitbusiness@wgu.edu",
                CourseStatus.PLAN_TO_TAKE);

        DataManager.insertCourse(this, Long.parseLong(term1Uri.getLastPathSegment()),
                "C195: Software II - Advanced Java Concepts", "2016-03-01", "2016-06-30",
                "Course Mentor Group", "", "cmprogramming@wgu.edu",
                CourseStatus.PLAN_TO_TAKE);

        DataManager.insertCourseNote(this, Long.parseLong(course1Uri.getLastPathSegment()),
                "This is a short test note");

        DataManager.insertCourseNote(this, Long.parseLong(course1Uri.getLastPathSegment()),
                getString(R.string.long_test_note));

        Uri ass1Uri = DataManager.insertAssessment(this, Long.parseLong(course1Uri.getLastPathSegment()), "CLP1",
                "Mobile Application Development", "As a competent mobile application developer, your understanding of mobile application structure and design will help you to develop applications to meet customer requirements. The following project to develop a student scheduler/student progress tracking application, will help you to apply these skills in a familiar, real-world scenario. This task will allow you to demonstrate your ability to apply the skills learned in the course.\n" +
                        "\n" +
                        "You will develop a multiple-screen mobile application for WGU students to track their terms, courses associated with each term, and assessment(s) associated with each course. The application will allow students to enter, edit, and delete term, course, and assessment data. It should provide summary and detailed views of courses for each term and provide alerts for upcoming performance and objective assessments. This application will use a SQLite database.\n" +
                        "\n\n" +
                        "adding another line", "2016-10-01 02:30:00 PM");

        Uri ass2Uri = DataManager.insertAssessment(this, Long.parseLong(course1Uri.getLastPathSegment()), "ABC3",
                "Second Assessment, although this one has a name that won't fit on the grid",
                "Assessment Description", "2016-10-01 10:30:00 AM");

        DataManager.insertAssessmentNote(this, Long.parseLong(ass1Uri.getLastPathSegment()),
                "Assessment #1 Note #1");

        DataManager.insertAssessmentNote(this, Long.parseLong(ass1Uri.getLastPathSegment()),
                "Assessment #1 Note #2");

        DataManager.insertAssessmentNote(this, Long.parseLong(ass2Uri.getLastPathSegment()),
                "Assessment #2 Note #1");

        DataManager.insertAssessmentNote(this, Long.parseLong(ass2Uri.getLastPathSegment()),
                "Assessment #2 Note #2");

        DataManager.insertAssessmentNote(this, Long.parseLong(ass2Uri.getLastPathSegment()),
                "Assessment #2 Note #3");


        restartLoader();
        return true;
    }

    private boolean createTestAlarm() {
        // current time + 10 seconds
        Long time = new GregorianCalendar().getTimeInMillis() + 5000;

        // create an Intent for our alarmHandler class. AlarmHandler will handle the actual alarm
        Intent intentAlarm = new Intent(this, AlarmHandler.class);
        intentAlarm.putExtra("text", "This is a test notification triggered from the debug function in ClassTracker");
        intentAlarm.putExtra("title", "TEST Notification from ClassTracker");

        // create the object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time
        alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_ONE_SHOT));
        Toast.makeText(this, "Test alarm scheduled for 1 second from now", Toast.LENGTH_SHORT).show();

        return true;
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DataProvider.TERM_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ca.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ca.swapCursor(null);
    }

    public void openNewTermEditor(View view) {
        Intent intent = new Intent(this, TermEditorActivity.class);
        startActivityForResult(intent, TERM_EDITOR_ACTIVITY_CODE);
    }
}
