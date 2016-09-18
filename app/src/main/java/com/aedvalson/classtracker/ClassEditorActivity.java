package com.aedvalson.classtracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class ClassEditorActivity extends AppCompatActivity {

    private String action;
    private _Class _class;

    private Uri termUri;
    private Uri classUri;

    private EditText etClassName;
    private EditText etClassStart;
    private EditText etClassEnd;
    private EditText etClassMentor;
    private EditText etClassMentorPhone;
    private EditText etClassMentorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViews();

        Intent intent = getIntent();
        classUri = intent.getParcelableExtra(DataProvider.CLASS_CONTENT_TYPE);
        termUri = intent.getParcelableExtra(DataProvider.TERM_CONTENT_TYPE);

        // no uri means we're creating a new term
        if (classUri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.add_new_class));
        } else {
            action = Intent.ACTION_EDIT;
            setTitle("Edit Class");
            long classId = Long.parseLong(classUri.getLastPathSegment());
            _class = DataManager.getClass(this, classId);
            fillClassForm(_class);
        }
    }

    private void findViews() {
        etClassName = (EditText) findViewById(R.id.etClassName);
        etClassStart = (EditText) findViewById(R.id.etClassStart);
        etClassEnd = (EditText) findViewById(R.id.etClassEnd);
        etClassMentor = (EditText) findViewById(R.id.etClassMentor);
        etClassMentorEmail = (EditText) findViewById(R.id.etClassMentorEmail);
        etClassMentorPhone = (EditText) findViewById(R.id.etClassMentorPhone);
    }

    private void fillClassForm(_Class c) {
        etClassName.setText(c.className);
        etClassStart.setText(c.classStart);
        etClassEnd.setText(c.classEnd);
        etClassMentor.setText(c.classMentor);
        etClassMentorEmail.setText(c.classMentorEmail);
        etClassMentorPhone.setText(c.classMentorPhone);
    }

    public void saveClassChanges(View view) {
        if (action == Intent.ACTION_INSERT) {
            long termId = Long.parseLong(termUri.getLastPathSegment());
            DataManager.insertClass(this, termId,
                    etClassName.getText().toString().trim(),
                    etClassStart.getText().toString().trim(),
                    etClassEnd.getText().toString().trim(),
                    etClassMentor.getText().toString().trim(),
                    etClassMentorEmail.getText().toString().trim(),
                    etClassMentorPhone.getText().toString().trim(),
                    ClassStatus.PLAN_TO_TAKE
            );

            setResult(RESULT_OK);
        }
        else if (action == Intent.ACTION_EDIT) {
            _class.className = etClassName.getText().toString().trim();
            _class.classStart = etClassStart.getText().toString().trim();
            _class.classEnd = etClassEnd.getText().toString().trim();
            _class.classMentor = etClassMentor.getText().toString().trim();
            _class.classMentorEmail = etClassMentorEmail.getText().toString().trim();
            _class.classMentorPhone = etClassMentorPhone.getText().toString().trim();

            _class.saveChanges(this);
            setResult(RESULT_OK);
        }

        finish();
    }
}
