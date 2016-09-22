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

public class CourseNoteViewerActivity extends AppCompatActivity {

    private static final int COURSE_NOTE_EDITOR_ACTIVITY_CODE = 11111;
    
    private Uri noteUri;
    private TextView tvNoteText;
    private long courseNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseNoteViewerActivity.this, CourseNoteEditorActivity.class);
                intent.putExtra(DataProvider.COURSE_NOTE_CONTENT_TYPE, noteUri);
                startActivityForResult(intent, COURSE_NOTE_EDITOR_ACTIVITY_CODE);
            }
        });

        tvNoteText = (TextView) findViewById(R.id.tvNoteText);

        // Reusing this view for assessment notes, so let's check for multiple types of Intent Extras
        noteUri = getIntent().getParcelableExtra(DataProvider.COURSE_NOTE_CONTENT_TYPE);
        if (noteUri != null) {
            courseNoteId = Long.parseLong(noteUri.getLastPathSegment());
            setTitle("View Course Note");
            loadNote();
        }
    }

    private void loadNote() {
        _CourseNote cn = DataManager.getCourseNote(this, courseNoteId);
        tvNoteText.setText(cn.text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            loadNote();
        }
    }

}
