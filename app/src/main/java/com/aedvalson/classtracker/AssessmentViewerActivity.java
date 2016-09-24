package com.aedvalson.classtracker;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class AssessmentViewerActivity extends AppCompatActivity {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Uri assessmentUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_CONTENT_TYPE);
        long assessmentId = Long.parseLong(assessmentUri.getLastPathSegment());
        _Assessment assessment = DataManager.getAssessment(this, assessmentId);

        tvAssessmentTitle = (TextView) findViewById(R.id.tvAssessmentTitle);
        tvAssessmentDesc = (TextView) findViewById(R.id.tvAssessmentDesc);
        tvAssessmentDateTime = (TextView) findViewById(R.id.tvAssessmentDateTime);


        tvAssessmentTitle.setText(assessment.code + ": " + assessment.name);
        tvAssessmentDesc.setText(assessment.description);
        tvAssessmentDateTime.setText(assessment.dateTime);
    }

}
