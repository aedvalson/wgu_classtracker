package com.aedvalson.classtracker;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TermViewerActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TERM_EDITOR_ACTIVITY_CODE = 11111;
    private static final int CLASS_EDITOR_ACTIVITY_CODE = 22222;
    private Term term;

    private CursorAdapter ca;

    private TextView tv_title;
    private TextView tv_start;
    private TextView tv_end;

    private long termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermViewerActivity.this, ClassEditorActivity.class);
                startActivityForResult(intent, CLASS_EDITOR_ACTIVITY_CODE);
            }
        });

        findElements();
        loadTermData();
        bindClassList();

        getLoaderManager().initLoader(0, null, this);
    }

    private void bindClassList() {
        String[] from = { DBOpenHelper.CLASS_NAME, DBOpenHelper.CLASS_START, DBOpenHelper.CLASS_END };
        int[] to = { R.id.tvClassName, R.id.tvClassStartDate, R.id.tvClassEndDate };

        ca = new SimpleCursorAdapter(this, R.layout.class_list_item, null, from, to, 0);
        DataProvider db = new DataProvider();

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(ca);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TermViewerActivity.this, TermViewerActivity.class);
                Uri uri = Uri.parse(DataProvider.TERM_URI + "/" + id);
                intent.putExtra(DataProvider.TERM_CONTENT_TYPE, uri);
                //startActivityForResult(intent, TERM_VIEWER_ACTIVITY_CODE);
            }
        });


    }

    private void loadTermData() {
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(DataProvider.TERM_CONTENT_TYPE);

        if (uri == null) {
            setResult(RESULT_CANCELED);
            finish();
        }

        else {
            termId = Long.parseLong(uri.getLastPathSegment());
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_edit_term:
                Intent intent = new Intent(this, TermEditorActivity.class);
                Uri uri = Uri.parse(DataProvider.TERM_URI + "/" + term.termId);
                intent.putExtra(DataProvider.TERM_CONTENT_TYPE, uri);
                startActivityForResult(intent, TERM_EDITOR_ACTIVITY_CODE);
            case R.id.action_delete_term:
                return deleteTerm();
            default:
                return super.onOptionsItemSelected(item);
        }
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
                                        getString(R.string.too_many_classes),
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            loadTermData();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DataProvider.CLASS_URI, DBOpenHelper.CLASS_COLUMNS, DBOpenHelper.CLASS_TERM_ID + " = " + this.termId, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ca.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ca.swapCursor(null);
    }

}
