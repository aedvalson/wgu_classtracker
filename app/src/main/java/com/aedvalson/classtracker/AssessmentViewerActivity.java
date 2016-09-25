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

public class AssessmentViewerActivity extends AppCompatActivity {

    private static final int ASSESSMENT_EDITOR_ACTIVITY_CODE = 11111;
    private long assessmentId;

    private TextView tvAssessmentTitle;
    private TextView tvAssessmentDesc;
    private TextView tvAssessmentDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentViewerActivity.this, AssessmentEditorActivity.class);
                Uri uri = Uri.parse(DataProvider.ASSESSMENT_URI + "/" + assessmentId);
                intent.putExtra(DataProvider.ASSESSMENT_CONTENT_TYPE, uri);
                startActivityForResult(intent, ASSESSMENT_EDITOR_ACTIVITY_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadAssessment();
    }

    private void loadAssessment() {
        Uri assessmentUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_CONTENT_TYPE);
        assessmentId = Long.parseLong(assessmentUri.getLastPathSegment());
        _Assessment assessment = DataManager.getAssessment(this, assessmentId);

        tvAssessmentTitle = (TextView) findViewById(R.id.tvAssessmentTitle);
        tvAssessmentDesc = (TextView) findViewById(R.id.tvAssessmentDesc);
        tvAssessmentDateTime = (TextView) findViewById(R.id.tvAssessmentDateTime);


        tvAssessmentTitle.setText(assessment.code + ": " + assessment.name);
        tvAssessmentDesc.setText(assessment.description);
        tvAssessmentDateTime.setText(assessment.dateTime);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            loadAssessment();
        }
    }

}
