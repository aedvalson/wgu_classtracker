package com.aedvalson.classtracker;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

public class ImageListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter ca;
    private ListView lv;

    private Uri parentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
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

        parentUri = getIntent().getParcelableExtra("ParentUri");

        bindImagesList();
        getLoaderManager().initLoader(0, null, this);
    }

    private void bindImagesList() {
        String[] from = { DBOpenHelper.IMAGE_TIMESTAMP, DBOpenHelper.IMAGE_TIMESTAMP };
        int[] to = { R.id.imageView, R.id.imageText };

        ca = new MySimpleCursorAdapter(this, R.layout.image_list_item, null, from, to);


        DataProvider db = new DataProvider();

        ListView list = (ListView) findViewById(R.id.lvImages);
        list.setAdapter(ca);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ImageListActivity.this, CourseNoteViewerActivity.class);
                Uri uri = Uri.parse(DataProvider.COURSE_NOTE_URI + "/" + id);
                intent.putExtra(DataProvider.COURSE_NOTE_CONTENT_TYPE, uri);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        restartLoader();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, DataProvider.IMAGE_URI, DBOpenHelper.IMAGE_COLUMNS, DBOpenHelper.IMAGE_PARENT_URI + " = '" + parentUri + "'", null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ca.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ca.swapCursor(null);
    }


    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }



    public class MySimpleCursorAdapter extends SimpleCursorAdapter {

        public MySimpleCursorAdapter(Context context, int layout, Cursor c,
                                     String[] from, int[] to) {
            super(context, layout, c, from, to);
        }

        @Override
        public void setViewText(TextView v, String text) {
            Long timestamp = Long.parseLong(text);
            Date date=new Date(timestamp);
            v.setText("Taken: " + DateUtil.dateTimeFormat.format(date));
        }

        @Override
        public void setViewImage(ImageView v, String id) {

            String path = getExternalFilesDir(null) + "/class_tracker_images/" + id + "_thumb.png";
            Bitmap b = BitmapFactory.decodeFile(path);
            v.setImageBitmap(b);

        }

    }

}
