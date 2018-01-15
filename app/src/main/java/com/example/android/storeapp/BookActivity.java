package com.example.android.storeapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.storeapp.data.BookContract.BookEntry;

import java.io.ByteArrayOutputStream;


public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private BookAdapter mAdapter;
    private static final int BOOK_LOADER = 1;
    private static final String LOG_TAG = BookActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        getApplicationContext().deleteDatabase("library.db");

        FloatingActionButton fb = findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookActivity.this, BookEditorActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = findViewById(R.id.list_view);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        mAdapter = new BookAdapter(this, null);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(BookActivity.this, BookEditorActivity.class);
                Uri currentUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                newIntent.setData(currentUri);
                startActivity(newIntent);

            }
        });

        getLoaderManager().initLoader(BOOK_LOADER, null,this );
    }

    private void insertBook(){
        ContentValues values = new ContentValues();
        values.put(BookEntry.BOOK_COLUMN_TITLE, "Inferno");
        values.put(BookEntry.BOOK_COLUMN_AUTHOR, "Dan Brown");
        values.put(BookEntry.BOOK_COLUMN_PRICE, 9);
        values.put(BookEntry.BOOK_COLUMN_QUANTITY, 1);
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_NAME, "Amazon");
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL, "support@amazon.com");
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE_NUMBER, 121);
        values.put(BookEntry.BOOK_COLUMN_IMAGE, BookEntry.BOOK_BOOK);

        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
    }

    private void deleteAllBooks(){
        int deletedRows = getContentResolver().delete(BookEntry.CONTENT_URI,null, null);
        Log.v(LOG_TAG, deletedRows + " rows deleted from database");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case R.id.add_a_book:
               insertBook();
               return true;
           case R.id.delete_all_books:
               deleteAllBooks();
               return true;
       }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection = new String[]{ BookEntry.BOOK_COLUMN_ID,
                                             BookEntry.BOOK_COLUMN_TITLE,
                                            BookEntry.BOOK_COLUMN_AUTHOR,
                                            BookEntry.BOOK_COLUMN_QUANTITY,
                                            BookEntry.BOOK_COLUMN_IMAGE};
        return new CursorLoader(this, BookEntry.CONTENT_URI, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }
}
