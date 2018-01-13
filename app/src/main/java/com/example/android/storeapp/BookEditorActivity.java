package com.example.android.storeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.storeapp.data.BookContract.BookEntry;

import java.util.ArrayList;


public class BookEditorActivity extends AppCompatActivity {

    private EditText mNameBook;
    private EditText mAuthorBook;
    private EditText mPriceBook;
    private EditText mQuantityBook;
    private EditText mSupplierName;
    private EditText mSupplierEmail;
    private EditText mSupplierPhone;
    private Spinner mImageSpinner;

    private byte mImage = BookEntry.BOOK_BOOK;

    private boolean mBookHasChanged = false;
    private View.OnTouchListener mTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mBookHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_editor);

        mNameBook = findViewById(R.id.book_name);
        mAuthorBook = findViewById(R.id.book_author);
        mPriceBook = findViewById(R.id.book_price);
        mQuantityBook = findViewById(R.id.book_quantity);
        mSupplierName = findViewById(R.id.sup_name);
        mSupplierEmail = findViewById(R.id.sup_email);
        mSupplierPhone = findViewById(R.id.sup_phone);
        mImageSpinner = findViewById(R.id.image_spinner);

        mNameBook.setOnTouchListener(mTouch);
        mAuthorBook.setOnTouchListener(mTouch);
        mQuantityBook.setOnTouchListener(mTouch);
        mPriceBook.setOnTouchListener(mTouch);
        mSupplierName.setOnTouchListener(mTouch);
        mSupplierEmail.setOnTouchListener(mTouch);
        mSupplierPhone.setOnTouchListener(mTouch);
        mImageSpinner.setOnTouchListener(mTouch);

        setupSpinner();
    }

    private void setupSpinner(){

       final ArrayList<BookImage> imagesList = new ArrayList<BookImage>();

       imagesList.add(new BookImage(R.drawable.book));
       imagesList.add(new BookImage(R.drawable.ebook));
       imagesList.add(new BookImage(R.drawable.b_a));

       BookImageAdapter bookImageAdapter = new BookImageAdapter(getApplicationContext(), imagesList);
       mImageSpinner.setAdapter(bookImageAdapter);


       mImageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String selection = (String) parent.getItemAtPosition(position);
               if (!TextUtils.isEmpty(selection)){
                   if (selection.equals(BookEntry.BOOK_BOOK)){
                       mImage = BookEntry.BOOK_EBOOK;
                   } else if (selection.equals(BookEntry.BOOK_EBOOK)){
                       mImage = BookEntry.BOOK_EBOOK;
                   } else if (selection.equals(BookEntry.BOOK_AUDIO)){
                       mImage = BookEntry.BOOK_AUDIO;
                   }
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {
               mImage = BookEntry.BOOK_BOOK;

           }
       });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }


}
