package com.example.android.storeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class BookEditorActivity extends AppCompatActivity {

    private EditText mNameBook;
    private EditText mAuthorBook;
    private EditText mPriceBook;
    private EditText mQuantityBook;
    private EditText mSupplierName;
    private EditText mSupplierEmail;
    private EditText mSupplierPhone;
    private Spinner mImageSpinner;


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
        setupSpinner();
    }

    private void setupSpinner(){
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.array_image_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mImageSpinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }


}
