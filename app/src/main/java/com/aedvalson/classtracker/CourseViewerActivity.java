package com.aedvalson.classtracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.GregorianCalendar;

public class CourseViewerActivity extends AppCompatActivity {

    private static final int COURSE_NOTE_LIST_ACTIVITY_CODE = 11111;
    private static final int ASSESSMENT_LIST_ACTIVITY_CODE = 22222;

    private Menu menu;

    private Uri courseUri;
    private long courseId;
    private _Course course;

    private TextView tvCourseName;
    private TextView tvStartDate;
    private TextView tvEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(DataProvider.COURSE_CONTENT_TYPE);
        courseId = Long.parseLong(courseUri.getLastPathSegment());
        course = DataManager.getCourse(this, courseId);


        findElements();
    }

    private void findElements() {
        tvCourseName = (TextView) findViewById(R.id.tvCourseName);
        tvStartDate = (TextView) findViewById(R.id.tvCourseStart);
        tvEndDate = (TextView) findViewById(R.id.tvCourseEnd);

        tvCourseName.setText(course.courseName);
        tvStartDate.setText(course.courseStart);
        tvEndDate.setText(course.courseEnd);

    }


    public void openClassNotesList(View view) {
        Intent intent = new Intent(CourseViewerActivity.this, CourseNoteListActivity.class);
        Uri uri = Uri.parse(DataProvider.COURSE_URI + "/" + courseId);
        intent.putExtra(DataProvider.COURSE_CONTENT_TYPE, uri);
        startActivityForResult(intent, COURSE_NOTE_LIST_ACTIVITY_CODE);
    }

    public void openAssessments(View view) {
        Intent intent = new Intent(CourseViewerActivity.this, AssessmentListActivity.class);
        Uri uri = Uri.parse(DataProvider.COURSE_URI + "/" + courseId);
        intent.putExtra(DataProvider.COURSE_CONTENT_TYPE, uri);
        startActivityForResult(intent, ASSESSMENT_LIST_ACTIVITY_CODE);
    }

    /// Setup menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_viewer, menu);
        this.menu = menu;
        showAppropriateMenuOptions();
        return true;
    }

    private void showAppropriateMenuOptions() {
        SharedPreferences sp = getSharedPreferences(AlarmHandler.courseAlarmFile, Context.MODE_PRIVATE);
        MenuItem item;

        menu.findItem(R.id.action_enable_notifications).setVisible(true);
        menu.findItem(R.id.action_disable_notifications).setVisible(true);

        if (course.courseNotifications) {
            item = menu.findItem(R.id.action_enable_notifications);
        } else {
            item = menu.findItem(R.id.action_disable_notifications);
        }

        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete_course:
                return deleteCourse();
            case R.id.action_enable_notifications:
                return enableNotifications();
            case R.id.action_disable_notifications:
                return disableNotifications();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean disableNotifications() {
        course.courseNotifications = false;
        course.saveChanges(this);
        showAppropriateMenuOptions();
        return true;
    }

    private long getDateTimestamp(String Date) {
        String[] dateArray = Date.split("-");
        int year = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1]);
        int date = Integer.parseInt(dateArray[2]);
        GregorianCalendar gc = new GregorianCalendar(year, month, date);

        long timeStamp = gc.getTimeInMillis();
        return timeStamp;
    }

    private boolean enableNotifications() {
        long now = DateUtil.todayLong();
        Long tsLong = System.currentTimeMillis();
        String nowString = DateUtil.dateFormat.format(tsLong);
        long courseStartLong = DateUtil.getDateTimestamp(course.courseStart);
        long threeDaysBeforeStart = DateUtil.getDateTimestamp(course.courseStart) - (3 * 24 * 60 * 60 * 1000);
        long oneDaysBeforeStart = DateUtil.getDateTimestamp(course.courseStart) - (24 * 60 * 60 * 1000);

        if (now <= DateUtil.getDateTimestamp(course.courseStart)) {
            AlarmHandler.scheduleCourseAlarm(getApplicationContext(), (int) courseId, DateUtil.getDateTimestamp(course.courseStart), "Course starts Today!", course.courseName + " begins on " + course.courseStart);
        }
        if (now <= DateUtil.getDateTimestamp(course.courseStart) - 3 * 24 * 60 * 60 * 1000) {
            AlarmHandler.scheduleCourseAlarm(getApplicationContext(), (int) courseId, DateUtil.getDateTimestamp(course.courseStart) - 3 * 24 * 60 * 60 * 1000, "Course starts in 3 days", course.courseName + " begins on " + course.courseStart);
        }
        if (now <= DateUtil.getDateTimestamp(course.courseStart) - 24 * 60 * 60 * 1000) {
            AlarmHandler.scheduleCourseAlarm(getApplicationContext(), (int) courseId, DateUtil.getDateTimestamp(course.courseStart) - 24 * 60 * 60 * 1000, "Course starts tomorrow", course.courseName + " begins on " + course.courseStart);
        }

        if (now <= DateUtil.getDateTimestamp(course.courseEnd)) {
            AlarmHandler.scheduleCourseAlarm(getApplicationContext(), (int) courseId, DateUtil.getDateTimestamp(course.courseEnd), "Course ends Today!", course.courseName + " ends on " + course.courseEnd);
        }
        if (now <= DateUtil.getDateTimestamp(course.courseEnd) - 3 * 24 * 60 * 60 * 1000) {
            AlarmHandler.scheduleCourseAlarm(getApplicationContext(), (int) courseId, DateUtil.getDateTimestamp(course.courseEnd) - 3 * 24 * 60 * 60 * 1000, "Course ends in 3 days", course.courseName + " ends on " + course.courseEnd);
        }
        if (now <= DateUtil.getDateTimestamp(course.courseEnd) - 24 * 60 * 60 * 1000) {
            AlarmHandler.scheduleCourseAlarm(getApplicationContext(), (int) courseId, DateUtil.getDateTimestamp(course.courseEnd) - 24 * 60 * 60 * 1000, "Course ends tomorrow", course.courseName + " ends on " + course.courseEnd);
        }

        course.courseNotifications = true;
        course.saveChanges(this);

        showAppropriateMenuOptions();

        return true;
    }

    private boolean deleteCourse() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            DataManager.deleteCourse(CourseViewerActivity.this, courseId);
                            setResult(RESULT_OK);
                            finish();

                            // Notify that delete was completed
                            Toast.makeText(CourseViewerActivity.this,
                                    R.string.course_deleted,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete_course)
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

        return true;
    }
}
