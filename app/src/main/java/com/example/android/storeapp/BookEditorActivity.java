package com.example.android.storeapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Button;
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
    private Button mOrder;
    private Button mPlus;
    private Button mMinus;
    private int mQuantity;

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

        getLoaderManager().initLoader(BOOK_LOADER_MN, null, this);


        mNameBook = findViewById(R.id.book_name);
        mAuthorBook = findViewById(R.id.book_author);
        mPriceBook = findViewById(R.id.book_price);
        mQuantityBook = findViewById(R.id.book_quantity);
        mSupplierName = findViewById(R.id.sup_name);
        mSupplierEmail = findViewById(R.id.sup_email);
        mSupplierPhone = findViewById(R.id.sup_phone);
        mImageSpinner = findViewById(R.id.image_spinner);
        mOrder = findViewById(R.id.button_order_settings);
        mPlus = findViewById(R.id.plus);
        mMinus = findViewById(R.id.minus);

        mNameBook.setOnTouchListener(mTouch);
        mAuthorBook.setOnTouchListener(mTouch);
        mQuantityBook.setOnTouchListener(mTouch);
        mPriceBook.setOnTouchListener(mTouch);
        mSupplierName.setOnTouchListener(mTouch);
        mSupplierEmail.setOnTouchListener(mTouch);
        mSupplierPhone.setOnTouchListener(mTouch);
        mImageSpinner.setOnTouchListener(mTouch);
        mOrder.setOnTouchListener(mTouch);
        mPlus.setOnTouchListener(mTouch);
        mMinus.setOnTouchListener(mTouch);

        setupSpinner();

        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mSupplierPhone.getText().toString()));
                if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    getApplicationContext().startActivity(intent);
                }
            }
        });

        mPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = mQuantityBook.getText().toString();
                if (Integer.parseInt(quantity) == 100){
                    Toast.makeText(BookEditorActivity.this, getString(string.are_you_sure), Toast.LENGTH_SHORT).show();
                } else {
                    mQuantity = Integer.parseInt(quantity);
                    mQuantity++;
                    mQuantityBook.setText(String.valueOf(mQuantity));
                }
            }
        });

        mMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = mQuantityBook.getText().toString();
                if (Integer.parseInt(quantity) == 0){
                    Toast.makeText(BookEditorActivity.this,getString(string.minus_value), Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    mQuantity = Integer.parseInt(quantity);
                    mQuantity--;
                    mQuantityBook.setText(String.valueOf(mQuantity));
                }
            }
        });

        }

    private void setupSpinner(){

        ArrayList<Integer> imagesList = new ArrayList<>();

        imagesList.add(drawable.book);
        imagesList.add(drawable.b_a);
        imagesList.add(drawable.ebook);

       BookImageAdapter bookImageAdapter = new BookImageAdapter(getApplicationContext(), imagesList);
       mImageSpinner.setAdapter(bookImageAdapter);

        mImageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selection = (int) parent.getItemAtPosition(position);
                if (selection == BookEntry.BOOK_BOOK){
                    mImage = BookEntry.BOOK_BOOK;
                } else if (selection == BookEntry.BOOK_AUDIO){
                    mImage = BookEntry.BOOK_AUDIO;
                } else {
                    mImage = BookEntry.BOOK_EBOOK;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mImage = BookEntry.BOOK_EBOOK;

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
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

        if (mCurrentUri  == null && TextUtils.isEmpty(bookName)&& TextUtils.isEmpty(bookAuthor) &&
                TextUtils.isEmpty(bookPrice) && TextUtils.isEmpty(bookQuantity) &&
                TextUtils.isEmpty(bookSupName) && TextUtils.isEmpty(bookSupEmail) && TextUtils.isEmpty(bookSupPhone) &&
                mImage == BookEntry.BOOK_BOOK){
            Toast.makeText(this, getString(string.cannot_save_a_blank_item), Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();

        if (!TextUtils.isEmpty(bookName)){
            values.put(BookEntry.BOOK_COLUMN_TITLE, bookName);}
        else {
            Toast.makeText(this,getString(string.book_title_is_required), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(bookAuthor)){
            values.put(BookEntry.BOOK_COLUMN_AUTHOR, bookAuthor);}
        else {
            Toast.makeText(this, getString(string.book_author_is_required), Toast.LENGTH_SHORT).show();
            return;
        }

        double price = 0.00;
        if (!TextUtils.isEmpty(bookPrice)){
            price = Double.parseDouble(bookPrice);}
        values.put(BookEntry.BOOK_COLUMN_PRICE, price);

        int quantity = 0;
        if (!TextUtils.isEmpty(bookQuantity)){
            quantity = Integer.parseInt(bookQuantity);}
        values.put(BookEntry.BOOK_COLUMN_QUANTITY, quantity);

        if (!TextUtils.isEmpty(bookSupName)){
            values.put(BookEntry.BOOK_COLUMN_SUPPLIER_NAME, bookSupName);}
        else {
            Toast.makeText(this, getString(string.book_supplier_name_is_required), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(bookSupEmail)){
            values.put(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL, bookSupEmail);}
        else {
            Toast.makeText(this, getString(string.book_supplier_email_addredd_is_required), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(bookSupPhone)){
            values.put(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE_NUMBER, bookSupPhone);}
        else {
            Toast.makeText(this, getString(string.book_supplier_phone_number_is_required), Toast.LENGTH_SHORT).show();
            return;
        }
        values.put(BookEntry.BOOK_COLUMN_IMAGE, mImage);

        int newBook = getContentResolver().update(mCurrentUri, values, null, null);
        if (newBook == 0){
            Toast.makeText(this, getString(string.book_update_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(string.book_updated), Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteBook(){
        if (mCurrentUri != null){
       int deleteRow = getContentResolver().delete(mCurrentUri, null, null );
        if (deleteRow == 0) {
            Toast.makeText(this, getString(string.error_with_deleting_a_book), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(string.book_deleted), Toast.LENGTH_SHORT).show();
          }
        }
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

    @SuppressLint("SetTextI18n")
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
            double priceB= cursor.getDouble(price);
            int quantityB = cursor.getInt(quantity);
            String supplierB = cursor.getString(supplier);
            String emailB = cursor.getString(email);
            String phoneB = cursor.getString(phone);
            int imageB = cursor.getInt(image);

            mNameBook.setText(nameB);
            mAuthorBook.setText(authorB);
            mPriceBook.setText(Double.toString(priceB));
            mQuantityBook.setText(Integer.toString(quantityB));
            mSupplierName.setText(supplierB);
            mSupplierEmail.setText(emailB);
            mSupplierPhone.setText(phoneB);

            switch (imageB){
                case BookEntry.BOOK_AUDIO:
                    mImageSpinner.setSelection(1);
                    break;
                case BookEntry.BOOK_EBOOK:
                    mImageSpinner.setSelection(2);
                    break;
                default:
                    mImageSpinner.setSelection(0);
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
        mImageSpinner.setSelection(0);
    }
}
