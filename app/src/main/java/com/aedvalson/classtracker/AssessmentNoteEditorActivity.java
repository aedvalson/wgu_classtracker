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

public class AssessmentNoteEditorActivity extends AppCompatActivity {


    private Uri assessmentUri;
    private long assessmentId;

    private Uri assessmentNoteUri;
    private long assessmentNoteId;
    private _AssessmentNote cn;

    private EditText noteTextField;

    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_note_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTextField = (EditText) findViewById(R.id.etAssessmentNoteText);

        assessmentNoteUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_NOTE_CONTENT_TYPE);
        if (assessmentNoteUri == null) {
            // new Note
            setTitle("Enter New Note");
            assessmentUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_CONTENT_TYPE);
            assessmentId = Long.parseLong(assessmentUri.getLastPathSegment());
            action = Intent.ACTION_INSERT;
        }

        else {
            setTitle("Edit Note");
            assessmentNoteId = Long.parseLong(assessmentNoteUri.getLastPathSegment());
            cn = DataManager.getAssessmentNote(this, assessmentNoteId);
            assessmentId = cn.assessmentId;
            noteTextField.setText(cn.text);
            action = Intent.ACTION_EDIT;
        }
    }

    public void saveAssessmentNote(View view) {
        if (action == Intent.ACTION_INSERT) {
            DataManager.insertAssessmentNote(this, assessmentId, noteTextField.getText().toString().trim() );
            setResult(RESULT_OK);
            finish();
        }

        if (action == Intent.ACTION_EDIT) {
            cn.text = noteTextField.getText().toString().trim();
            cn.saveChanges(this);
            setResult(RESULT_OK);
            finish();
        }
    }
}
