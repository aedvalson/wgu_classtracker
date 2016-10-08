package com.example.classtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TermViewerActivity extends AppCompatActivity {

    private static final int TERM_EDITOR_ACTIVITY_CODE = 11111;

    private static final int COURSE_LIST_ACTIVITY_CODE = 33333;

    private Uri termUri;
    private _Term term;

    private CursorAdapter ca;

    private TextView tv_title;
    private TextView tv_start;
    private TextView tv_end;
    private Menu menu;

    private long termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        termUri = intent.getParcelableExtra(DataProvider.TERM_CONTENT_TYPE);

        findElements();
        loadTermData();
    }



    private void loadTermData() {
        if (termUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        }

        else {
            termId = Long.parseLong(termUri.getLastPathSegment());
            term = DataManager.getTerm(this, termId);

            setTitle("View Term");
            tv_title.setText(term.termName);
            tv_start.setText(term.termStartDate);
            tv_end.setText(term.termEndDate);
        }
    }

    private void findElements() {
        tv_title = (TextView) findViewById(R.id.tvTermViewTermTitle);
        tv_start = (TextView) findViewById(R.id.tvTermViewStartDate);
        tv_end = (TextView) findViewById(R.id.tvTermViewEndDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_viewer, menu);
        this.menu = menu;
        showAppropriateMenuOptions();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_mark_term_active:
                return markTermActive();
            case R.id.action_edit_term:
                Intent intent = new Intent(this, TermEditorActivity.class);
                Uri uri = Uri.parse(DataProvider.TERM_URI + "/" + term.termId);
                intent.putExtra(DataProvider.TERM_CONTENT_TYPE, uri);
                startActivityForResult(intent, TERM_EDITOR_ACTIVITY_CODE);
                break;
            case R.id.action_delete_term:
                return deleteTerm();
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private boolean markTermActive() {
        Cursor termCursor = getContentResolver().query(DataProvider.TERM_URI, null, null, null, null);
        ArrayList<_Term> termList = new ArrayList<_Term>();
        while  (termCursor.moveToNext()) {
            termList.add(DataManager.getTerm(this, termCursor.getLong(termCursor.getColumnIndex(DBOpenHelper.TERM_TABLE_ID))));
        }

        for(_Term _term : termList) {
            _term.deactivate(this);
        }

        this.term.activate(this);
        showAppropriateMenuOptions();

        Toast.makeText(TermViewerActivity.this,
                R.string.term_marked_active,
                Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean deleteTerm() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    // Confirm that user wishes to proceed
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            long classCount = term.getClassCount(TermViewerActivity.this);
                            if (classCount == 0) {
                                getContentResolver().delete(DataProvider.TERM_URI, DBOpenHelper.TERM_TABLE_ID + " = " + termId, null);

                                // Notify that delete was completed
                                Toast.makeText(TermViewerActivity.this,
                                        getString(R.string.term_deleted),
                                        Toast.LENGTH_SHORT).show();

                                setResult(RESULT_OK);
                                finish();
                            }
                            else {
                                Toast.makeText(TermViewerActivity.this,
                                        getString(R.string.too_many_courses),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_term_confirm))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

        return true;
    }


    private void showAppropriateMenuOptions() {
        if (term.active == 1) {
            menu.findItem(R.id.action_mark_term_active).setVisible(false);
        }

    }

    public void openClassList(View view) {
        Intent intent = new Intent(this, CourseListActivity.class);
        intent.putExtra(DataProvider.TERM_CONTENT_TYPE, termUri);
        startActivityForResult(intent, COURSE_LIST_ACTIVITY_CODE);
    }
}
