package com.aedvalson.classtracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class CourseViewerActivity extends AppCompatActivity {

    private static final int COURSE_NOTE_LIST_ACTIVITY_CODE = 11111;
    private static final int ASSESSMENT_LIST_ACTIVITY_CODE = 22222;


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
}
