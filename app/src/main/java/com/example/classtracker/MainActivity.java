package com.example.classtracker;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private static final int TERM_LIST_ACTIVITY_CODE = 11111;
    private static final int TERM_VIEWER_ACTIVITY_CODE = 22222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void openTermList(View view) {
        Intent intent = new Intent(this, TermListActivity.class);
        startActivityForResult(intent, TERM_LIST_ACTIVITY_CODE);
    }

    public void openCurrentTerm(View view) {
        Cursor c = getContentResolver().query(DataProvider.TERM_URI, null, DBOpenHelper.TERM_ACTIVE + " = 1", null, null);
        while (c.moveToNext()) {
            Intent intent = new Intent(this, TermViewerActivity.class);
            long id = c.getLong(c.getColumnIndex(DBOpenHelper.TERM_TABLE_ID));
            Uri uri = Uri.parse(DataProvider.TERM_URI + "/" + id);
            intent.putExtra(DataProvider.TERM_CONTENT_TYPE, uri);
            startActivityForResult(intent, TERM_VIEWER_ACTIVITY_CODE);
            return;
        }

        Toast.makeText(this,
                "No term has been marked active. Set an active term from any term page",
                Toast.LENGTH_SHORT).show();
    }
}
