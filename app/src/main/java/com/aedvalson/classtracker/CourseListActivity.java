package com.aedvalson.classtracker;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CourseListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int COURSE_VIEWER_ACTIVITY_CODE = 11111;
    private static final int COURSE_EDITOR_ACTIVITY_CODE = 22222;


    private long termId;
    private Uri termUri;
    private _Term term;

    private CursorAdapter ca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseListActivity.this, CourseEditorActivity.class);
                intent.putExtra(DataProvider.TERM_CONTENT_TYPE, termUri);
                startActivityForResult(intent, COURSE_EDITOR_ACTIVITY_CODE);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        termUri = intent.getParcelableExtra(DataProvider.TERM_CONTENT_TYPE);
        loadTermData();
        bindClassList();

        getLoaderManager().initLoader(0, null, this);
    }

    private void bindClassList() {
        String[] from = { DBOpenHelper.COURSE_NAME, DBOpenHelper.COURSE_START, DBOpenHelper.COURSE_END };
        int[] to = { R.id.tvClassName, R.id.tvClassStartDate, R.id.tvClassEndDate };

        ca = new SimpleCursorAdapter(this, R.layout.class_list_item, null, from, to, 0);
        DataProvider db = new DataProvider();

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(ca);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CourseListActivity.this, CourseViewerActivity.class);
                Uri uri = Uri.parse(DataProvider.COURSE_URI + "/" + id);
                intent.putExtra(DataProvider.COURSE_CONTENT_TYPE, uri);
                startActivityForResult(intent, COURSE_VIEWER_ACTIVITY_CODE);
            }
        });


    }

    private void loadTermData() {
        if (termUri == null) {
            setResult(RESULT_CANCELED);
            finish();
        }

        else {
            termId = Long.parseLong(termUri.getLastPathSegment());
            term = DataManager.getTerm(this, termId);
            setTitle("Courses");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DataProvider.COURSE_URI, DBOpenHelper.COURSE_COLUMNS, DBOpenHelper.COURSE_TERM_ID + " = " + this.termId, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ca.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ca.swapCursor(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            loadTermData();
            restartLoader();
        }
    }


    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

}
