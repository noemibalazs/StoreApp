package com.example.android.storeapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.storeapp.data.BookContract.BookEntry;

import java.util.ArrayList;

import static com.example.android.storeapp.R.*;


public class BookEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mNameBook;
    private EditText mAuthorBook;
    private EditText mPriceBook;
    private EditText mQuantityBook;
    private EditText mSupplierName;
    private EditText mSupplierEmail;
    private EditText mSupplierPhone;
    private Spinner mImageSpinner;

    private int mImage = BookEntry.BOOK_BOOK;
    private Uri mCurrentUri;
    private static final int BOOK_LOADER_MN = 1;

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
        setContentView(layout.activity_book_editor);

        Intent intent = getIntent();
        mCurrentUri = intent.getData();
        if (mCurrentUri == null ){
            setTitle(getString(R.string.add_a_book));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_a_book));
            getLoaderManager().initLoader(BOOK_LOADER_MN, null, this);
        }

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

       ArrayList<Integer> imagesList = new ArrayList<Integer>();

       imagesList.add(R.drawable.book);
       imagesList.add(R.drawable.ebook);
       imagesList.add(R.drawable.b_a);

       BookImageAdapter bookImageAdapter = new BookImageAdapter(getApplicationContext(), imagesList);
       mImageSpinner.setAdapter(bookImageAdapter);

       mImageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              int selection =(Integer)parent.getItemAtPosition(position);
              if (selection == BookEntry.BOOK_BOOK){
                  mImage = BookEntry.BOOK_BOOK;
              } else if (selection == BookEntry.BOOK_AUDIO){
                  mImage = BookEntry.BOOK_AUDIO;
              } else if (selection == BookEntry.BOOK_EBOOK){
                  mImage = BookEntry.BOOK_EBOOK;
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentUri == null){
            MenuItem menuItem = menu.findItem(R.id.edit_delete_menu);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        if (!mBookHasChanged){
        super.onBackPressed();
        return;}

        DialogInterface.OnClickListener dialogButton = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
        showsUnsavedChangesDialog(dialogButton);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_save_menu:
                saveBook();
                finish();
                return true;
            case R.id.edit_delete_menu:
                showsDeleteWarningDialog();
                return true;
            case android.R.id.home:
                if (!mBookHasChanged){
                NavUtils.navigateUpFromSameTask(this);
                return true;
                }
                DialogInterface.OnClickListener dialogButton = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(BookEditorActivity.this);
                    }
                };
                showsUnsavedChangesDialog(dialogButton);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveBook(){

        String bookName = mNameBook.getText().toString().trim();
        String bookAuthor = mAuthorBook.getText().toString().trim();
        String bookPrice = mPriceBook.getText().toString().trim();
        String bookQuantity = mQuantityBook.getText().toString().trim();
        String bookSupName = mSupplierName.getText().toString().trim();
        String bookSupEmail = mSupplierEmail.getText().toString().trim();
        String bookSupPhone = mSupplierPhone.getText().toString().trim();

        if ( mCurrentUri == null && TextUtils.isEmpty(bookName)&& TextUtils.isEmpty(bookAuthor) && TextUtils.isEmpty(bookPrice) &&
                TextUtils.isEmpty(bookQuantity) && TextUtils.isEmpty(bookSupName) && TextUtils.isEmpty(bookSupEmail) &&
                TextUtils.isEmpty(bookSupPhone) && mImage == BookEntry.BOOK_EBOOK){
            return;
        }

        ContentValues values = new ContentValues();

        values.put(BookEntry.BOOK_COLUMN_TITLE, bookName);
        values.put(BookEntry.BOOK_COLUMN_AUTHOR, bookAuthor);
        int price = 0;
        if (!TextUtils.isEmpty(bookPrice)){
            price = Integer.parseInt(bookPrice);
        }
        values.put(BookEntry.BOOK_COLUMN_PRICE, price);
        int quantity = 0;
        if (TextUtils.isEmpty(bookQuantity)){
            quantity = Integer.parseInt(bookQuantity);
        }
        values.put(BookEntry.BOOK_COLUMN_QUANTITY, quantity);
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_NAME, bookSupName);
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL, bookSupEmail);
        values.put(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE_NUMBER, bookSupPhone);
        values.put(BookEntry.BOOK_COLUMN_IMAGE, mImage);

        if (mCurrentUri == null){
            Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
            if (newUri == null){
                Toast.makeText(this, getString(string.error_with_saving_book), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(string.book_saved), Toast.LENGTH_SHORT).show();
            }
        }
        else {
           int newBook = getContentResolver().update(BookEntry.CONTENT_URI, values, null, null);
           if (newBook == 0){
               Toast.makeText(this, getString(string.book_update_failed), Toast.LENGTH_SHORT).show();
           } else {
               Toast.makeText(this, getString(string.book_updated), Toast.LENGTH_SHORT).show();
           }

        }
    }

    private void deleteBook(){
        if (mCurrentUri != null){
       int deleteRow = getContentResolver().delete(BookEntry.CONTENT_URI, null, null );
        if (deleteRow == 0) {
            Toast.makeText(this, getString(string.error_with_deleting_a_book), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(string.book_deleted), Toast.LENGTH_SHORT).show();
        }}
        finish();
    }

    private void showsDeleteWarningDialog(){
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle(string.delete_this_book);
       builder.setPositiveButton(string.delete, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               deleteBook();
           }
       });
       builder.setNegativeButton(string.cancel, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               if (dialog!=null){
                   dialog.dismiss();
               }
           }
       });
       AlertDialog dialog = builder.create();
       dialog.show();
    }

    private void showsUnsavedChangesDialog(DialogInterface.OnClickListener dialogButton){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(string.discard_your_changes_and_quit_editing);
        builder.setPositiveButton(string.discard, dialogButton);
        builder.setNegativeButton(string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog!=null){
                    dialog.dismiss();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection = new String[]{
                BookEntry.BOOK_COLUMN_ID,
                BookEntry.BOOK_COLUMN_TITLE,
                BookEntry.BOOK_COLUMN_AUTHOR,
                BookEntry.BOOK_COLUMN_PRICE,
                BookEntry.BOOK_COLUMN_QUANTITY,
                BookEntry.BOOK_COLUMN_SUPPLIER_NAME,
                BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL,
                BookEntry.BOOK_COLUMN_SUPPLIER_PHONE_NUMBER,
                BookEntry.BOOK_COLUMN_IMAGE};

        return new CursorLoader(this, mCurrentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount()<1){
            return;
        }
        if (cursor.moveToFirst()){
            int name = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_TITLE);
            int author = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_AUTHOR);
            int price = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_PRICE);
            int quantity = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_QUANTITY);
            int supplier = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_SUPPLIER_NAME);
            int email = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL);
            int phone = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE_NUMBER);
            int image = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_IMAGE);

            String nameB = cursor.getString(name);
            String authorB = cursor.getString(author);
            int priceB= cursor.getInt(price);
            int quantityB = cursor.getInt(quantity);
            String supplierB = cursor.getString(supplier);
            String emailB = cursor.getString(email);
            String phoneB = cursor.getString(phone);
            int imageB = cursor.getInt(image);

            mNameBook.setText(nameB);
            mAuthorBook.setText(authorB);
            mPriceBook.setText(Integer.toString(priceB));
            mQuantityBook.setText(Integer.toString(quantityB));
            mSupplierName.setText(supplierB);
            mSupplierEmail.setText(emailB);
            mSupplierPhone.setText(phoneB);

            switch (imageB){
                case BookEntry.BOOK_BOOK:
                    mImageSpinner.setSelection(BookEntry.BOOK_BOOK);
                    break;
                case BookEntry.BOOK_AUDIO:
                    mImageSpinner.setSelection(BookEntry.BOOK_AUDIO);
                    break;
                case BookEntry.BOOK_EBOOK:
                    mImageSpinner.setSelection(BookEntry.BOOK_EBOOK);
                    break;
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameBook.setText("");
        mAuthorBook.setText("");
        mPriceBook.setText("");
        mQuantityBook.setText("");
        mSupplierName.setText("");
        mSupplierEmail.setText("");
        mSupplierPhone.setText("");
        mImageSpinner.setSelection(BookEntry.BOOK_BOOK);


    }
}
