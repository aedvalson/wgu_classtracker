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

public class AssessmentEditorActivity extends AppCompatActivity {

    private _Assessment assessment;
    private long courseId;

    private String action;

    private EditText etAssessmentCode;
    private EditText etAssessmentName;
    private EditText etAssessmentDesc;
    private EditText etAssessmentDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etAssessmentCode = (EditText) findViewById(R.id.etAssessmentCode);
        etAssessmentName = (EditText) findViewById(R.id.etAssessmentName);
        etAssessmentDesc = (EditText) findViewById(R.id.etAssessmentDesc);
        etAssessmentDateTime = (EditText) findViewById(R.id.etAssessmentDateTime);


        Uri assessmentUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_CONTENT_TYPE);
        if (assessmentUri == null) {
            // new Assessment
            setTitle(getString(R.string.new_assessment));
            action = Intent.ACTION_INSERT;
            Uri courseUri = getIntent().getParcelableExtra(DataProvider.COURSE_CONTENT_TYPE);
            courseId = Long.parseLong(courseUri.getLastPathSegment());
            assessment = new _Assessment();
        }
        else {
            // existing assessment
            setTitle(getString(R.string.edit_assessment));
            action = Intent.ACTION_EDIT;
            Long assessmentId = Long.parseLong(assessmentUri.getLastPathSegment());
            assessment = DataManager.getAssessment(this, assessmentId);
            courseId = assessment.courseId;
            populateFields();
        }
    }

    private void populateFields() {
        if (assessment != null) {
            etAssessmentCode.setText(assessment.code);
            etAssessmentDateTime.setText(assessment.dateTime);
            etAssessmentName.setText(assessment.name);
            etAssessmentDesc.setText(assessment.description);
        }
    }

    private void getValuesFromFields() {
        assessment.code = etAssessmentCode.getText().toString().trim();
        assessment.dateTime = etAssessmentDateTime.getText().toString().trim();
        assessment.description = etAssessmentDesc.getText().toString().trim();
        assessment.name = etAssessmentName.getText().toString().trim();
    }

    public void saveAssessmentChanges(View view) {
        getValuesFromFields();
        switch (action) {
            case Intent.ACTION_INSERT:
                DataManager.insertAssessment(this, courseId, assessment.code, assessment.name,
                        assessment.description, assessment.dateTime);
                setResult(RESULT_OK);
                finish();
                break;

            case Intent.ACTION_EDIT:
                assessment.saveChanges(this);
                setResult(RESULT_OK);
                finish();
                break;

            default:
                throw new UnsupportedOperationException();
        }
    }
}
