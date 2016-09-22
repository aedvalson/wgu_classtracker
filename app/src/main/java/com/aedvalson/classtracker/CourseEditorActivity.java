package com.aedvalson.classtracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class CourseEditorActivity extends AppCompatActivity {

    private String action;
    private _Course _course;

    private Uri termUri;
    private Uri courseUri;

    private EditText etCourseName;
    private EditText etCourseStart;
    private EditText etCourseEnd;
    private EditText etCourseMentor;
    private EditText etCourseMentorPhone;
    private EditText etCourseMentorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViews();

        Intent intent = getIntent();
        courseUri = intent.getParcelableExtra(DataProvider.COURSE_CONTENT_TYPE);
        termUri = intent.getParcelableExtra(DataProvider.TERM_CONTENT_TYPE);

        // no uri means we're creating a new term
        if (courseUri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.add_new_course));
        } else {
            action = Intent.ACTION_EDIT;
            setTitle("Edit Course");
            long classId = Long.parseLong(courseUri.getLastPathSegment());
            _course = DataManager.getCourse(this, classId);
            fillCourseForm(_course);
        }
    }

    private void findViews() {
        etCourseName = (EditText) findViewById(R.id.etCourseName);
        etCourseStart = (EditText) findViewById(R.id.etCourseStart);
        etCourseEnd = (EditText) findViewById(R.id.etCourseEnd);
        etCourseMentor = (EditText) findViewById(R.id.etCourseMentor);
        etCourseMentorEmail = (EditText) findViewById(R.id.etCourseMentorEmail);
        etCourseMentorPhone = (EditText) findViewById(R.id.etCourseMentorPhone);
    }

    private void fillCourseForm(_Course c) {
        etCourseName.setText(c.courseName);
        etCourseStart.setText(c.courseStart);
        etCourseEnd.setText(c.courseEnd);
        etCourseMentor.setText(c.courseMentor);
        etCourseMentorEmail.setText(c.courseMentorEmail);
        etCourseMentorPhone.setText(c.courseMentorPhone);
    }

    public void saveCourseChanges(View view) {
        if (action == Intent.ACTION_INSERT) {
            long termId = Long.parseLong(termUri.getLastPathSegment());
            DataManager.insertCourse(this, termId,
                    etCourseName.getText().toString().trim(),
                    etCourseStart.getText().toString().trim(),
                    etCourseEnd.getText().toString().trim(),
                    etCourseMentor.getText().toString().trim(),
                    etCourseMentorEmail.getText().toString().trim(),
                    etCourseMentorPhone.getText().toString().trim(),
                    CourseStatus.PLAN_TO_TAKE
            );

            setResult(RESULT_OK);
        }
        else if (action == Intent.ACTION_EDIT) {
            _course.courseName = etCourseName.getText().toString().trim();
            _course.courseStart = etCourseStart.getText().toString().trim();
            _course.courseEnd = etCourseEnd.getText().toString().trim();
            _course.courseMentor = etCourseMentor.getText().toString().trim();
            _course.courseMentorEmail = etCourseMentorEmail.getText().toString().trim();
            _course.courseMentorPhone = etCourseMentorPhone.getText().toString().trim();

            _course.saveChanges(this);
            setResult(RESULT_OK);
        }

        finish();
    }
}
