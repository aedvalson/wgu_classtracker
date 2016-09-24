package com.aedvalson.classtracker;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AssessmentEditorActivity extends AppCompatActivity {

    private _Assessment assessment;
    private Uri assessmentUri;
    private Uri courseUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_editor);
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

        assessmentUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_CONTENT_TYPE);
        if (assessmentUri == null) {
            // new Assessment
            courseUri = getIntent().getParcelableExtra(DataProvider.COURSE_CONTENT_TYPE);
        }
        else {
            Long assessmentId = Long.parseLong(assessmentUri.getLastPathSegment());
            assessment = DataManager.getAssessment(this, assessmentId);
            
        }

    }

}
