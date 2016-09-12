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
import android.widget.Toast;

public class TermEditorActivity extends AppCompatActivity {

    private static final int MAIN_ACTIVITY_CODE = 1;
    private String action;
    private EditText termNameField;
    private EditText termStartDateField;
    private EditText termEndDateField;

    private DataProvider db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DataProvider();

        termNameField = (EditText) findViewById(R.id.termNameEditText);
        termStartDateField = (EditText) findViewById(R.id.termStartDateEditText);
        termEndDateField = (EditText) findViewById(R.id.termEndDateEditText);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(DataProvider.TERM_CONTENT_TYPE);

        // no uri means we're creating a new term
        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.add_new_term));
        }
    }

    public void saveTermChanges(View view) {
        if (action == Intent.ACTION_INSERT) {
            DataManager.insertTerm(this,
                    termNameField.getText().toString(),
                    termStartDateField.getText().toString(),
                    termEndDateField.getText().toString()
            );

            // Notify that delete was completed
            Toast.makeText(this,
                    getString(R.string.term_saved),
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, MAIN_ACTIVITY_CODE);
        }
    }
}
