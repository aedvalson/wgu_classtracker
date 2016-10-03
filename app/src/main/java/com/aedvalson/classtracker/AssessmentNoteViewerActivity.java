package com.aedvalson.classtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AssessmentNoteViewerActivity extends AppCompatActivity {

    private static final int ASSESSMENT_NOTE_EDITOR_ACTIVITY_CODE = 11111;

    private Uri noteUri;
    private TextView tvNoteText;
    private long assessmentNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_note_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentNoteViewerActivity.this, AssessmentNoteEditorActivity.class);
                intent.putExtra(DataProvider.ASSESSMENT_NOTE_CONTENT_TYPE, noteUri);
                startActivityForResult(intent, ASSESSMENT_NOTE_EDITOR_ACTIVITY_CODE);
            }
        });

        tvNoteText = (TextView) findViewById(R.id.tvNoteText);

        // Reusing this view for assessment notes, so let's check for multiple types of Intent Extras
        noteUri = getIntent().getParcelableExtra(DataProvider.ASSESSMENT_NOTE_CONTENT_TYPE);
        if (noteUri != null) {
            assessmentNoteId = Long.parseLong(noteUri.getLastPathSegment());
            setTitle("View Assessment Note");
            loadNote();
        }
    }

    private void loadNote() {
        _AssessmentNote cn = DataManager.getAssessmentNote(this, assessmentNoteId);
        tvNoteText.setText(cn.text);
        tvNoteText.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            loadNote();
        }
    }


    /// Setup menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_assessment_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete_assessment_note:
                return deleteAssessmentNote();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean deleteAssessmentNote() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {

                            DataManager.deleteAssessmentNote(AssessmentNoteViewerActivity.this, assessmentNoteId);
                            setResult(RESULT_OK);
                            finish();

                            // Notify that delete was completed
                            Toast.makeText(AssessmentNoteViewerActivity.this,
                                    R.string.note_deleted,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete_note)
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

        return true;
    }
}
