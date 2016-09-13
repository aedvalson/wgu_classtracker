package com.aedvalson.classtracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TermEditorActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MAIN_ACTIVITY_CODE = 1;
    private String action;
    private EditText termNameField;
    private EditText termStartDateField;
    private EditText termEndDateField;

    private DatePickerDialog termStartDateDialog;
    private DatePickerDialog termEndDateDialog;
    private SimpleDateFormat dateFormatter;

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
        termStartDateField.setInputType(InputType.TYPE_NULL);

        termEndDateField = (EditText) findViewById(R.id.termEndDateEditText);
        termEndDateField.setInputType(InputType.TYPE_NULL);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(DataProvider.TERM_CONTENT_TYPE);

        // no uri means we're creating a new term
        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.add_new_term));
        }

        setupDatePickers();


    }

    private void setupDatePickers() {
        termStartDateField.setOnClickListener(this);
        termEndDateField.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        termStartDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                termStartDateField.setText(dateFormatter.format(newDate.getTime()));
            }

        },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        termEndDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                termEndDateField.setText(dateFormatter.format(newDate.getTime()));
            }

        },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        termStartDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    termStartDateDialog.show();
            }
        });

        termEndDateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    termEndDateDialog.show();
            }
        });
    }

    public void saveTermChanges(View view) {
        if (action == Intent.ACTION_INSERT) {

            String newTermName = termNameField.getText().toString().trim();
            String newTermStartDate = termStartDateField.getText().toString().trim();
            String newTermEndDate = termEndDateField.getText().toString().trim();

            DataManager.insertTerm(this,
                    newTermName,
                    newTermStartDate,
                    newTermEndDate
            );

            // Notify that delete was completed
            Toast.makeText(this,
                    getString(R.string.term_saved),
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, MAIN_ACTIVITY_CODE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == termStartDateField) {
            termStartDateDialog.show();
        }
        if (v == termEndDateField) {
            termEndDateDialog.show();
        }
    }
}
