package com.aedvalson.classtracker;

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
    private Uri assessmentUri;
    private Uri courseUri;

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


        assessmentUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_CONTENT_TYPE);
        if (assessmentUri == null) {
            // new Assessment
            courseUri = getIntent().getParcelableExtra(DataProvider.COURSE_CONTENT_TYPE);
        }
        else {
            Long assessmentId = Long.parseLong(assessmentUri.getLastPathSegment());
            assessment = DataManager.getAssessment(this, assessmentId);
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

}
