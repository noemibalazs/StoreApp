package com.example.android.storeapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.storeapp.data.BookContract.BookEntry;

import java.util.ArrayList;

public class BookDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mBookName;
    private TextView mBookAuthor;
    private TextView mBookPrice;
    private TextView mBookQuantity;
    private TextView mSupName;
    private TextView mSupEmail;
    private TextView mSupPhone;
    private Spinner mImageSpinner;
    private Button mOrder;
    private Button mMinus;
    private Button mPlus;
    private int mQuantity;

    private Uri mCurrentUri;
    private static final int BOOK_LOADER =1;
    private int mImage = BookEntry.BOOK_BOOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        mBookName = findViewById(R.id.detail_name);
        mBookAuthor = findViewById(R.id.detail_author);
        mBookPrice = findViewById(R.id.detail_price);
        mBookQuantity = findViewById(R.id.detail_quantity);
        mSupName = findViewById(R.id.detail_sup_name);
        mSupEmail = findViewById(R.id.detail_sup_email);
        mSupPhone = findViewById(R.id.detail_sup_phone);
        mImageSpinner = findViewById(R.id.detail_image_spinner);
        mOrder = findViewById(R.id.button_order_detail);
        mPlus = findViewById(R.id.plus_detail);
        mMinus = findViewById(R.id.minus_detail);


        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        setupSpinner();

        getLoaderManager().initLoader(BOOK_LOADER, null, this);

        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mSupPhone.getText().toString()));
                if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    getApplicationContext().startActivity(intent);
                }
            }
        });

        mPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = mBookQuantity.getText().toString();
                if (Integer.parseInt(quantity) == 100){
                    Toast.makeText(BookDetailActivity.this, getString(R.string.are_you_sure), Toast.LENGTH_SHORT).show();
                } else {
                    mQuantity = Integer.parseInt(quantity);
                    mQuantity++;
                    mBookQuantity.setText(String.valueOf(mQuantity));
                }
            }
        });

        mMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = mBookQuantity.getText().toString();
                if (Integer.parseInt(quantity) == 0){
                    Toast.makeText(BookDetailActivity.this,getString(R.string.minus_value), Toast.LENGTH_SHORT).show();
                }
                else {
                    mQuantity = Integer.parseInt(quantity);
                    mQuantity--;
                    mBookQuantity.setText(String.valueOf(mQuantity));
                }
            }
        });

    }

    private void setupSpinner(){

        ArrayList<Integer> imagesList = new ArrayList<>();

        imagesList.add(R.drawable.book);
        imagesList.add(R.drawable.b_a);
        imagesList.add(R.drawable.ebook);

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

    private void deleteBook(){
        getContentResolver().delete(mCurrentUri, null, null);
        finish();
    }

    private void showsDeleteWarningDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_this_book);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

    private void saveBook(){
        String bookName = mBookName.getText().toString().trim();
        String bookAuthor = mBookAuthor.getText().toString().trim();
        String bookPrice = mBookPrice.getText().toString().trim();
        String bookQuantity = mBookQuantity.getText().toString().trim();
        String bookSupName = mSupName.getText().toString().trim();
        String bookSupEmail = mSupEmail.getText().toString().trim();
        String bookSupPhone = mSupPhone.getText().toString().trim();

        if (mCurrentUri  == null && TextUtils.isEmpty(bookName)&& TextUtils.isEmpty(bookAuthor) &&
                TextUtils.isEmpty(bookPrice) && TextUtils.isEmpty(bookQuantity) &&
                TextUtils.isEmpty(bookSupName) && TextUtils.isEmpty(bookSupEmail) && TextUtils.isEmpty(bookSupPhone) &&
                mImage == BookEntry.BOOK_BOOK){
            Toast.makeText(this, getString(R.string.cannot_save_a_blank_item), Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();

        if (!TextUtils.isEmpty(bookName)){
            values.put(BookEntry.BOOK_COLUMN_TITLE, bookName);}
        else {
            Toast.makeText(this,getString(R.string.book_title_is_required), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(bookAuthor)){
            values.put(BookEntry.BOOK_COLUMN_AUTHOR, bookAuthor);}
        else {
            Toast.makeText(this, getString(R.string.book_author_is_required), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, getString(R.string.book_supplier_name_is_required), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(bookSupEmail)){
            values.put(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL, bookSupEmail);}
        else {
            Toast.makeText(this, getString(R.string.book_supplier_email_addredd_is_required), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(bookSupPhone)){
            values.put(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE_NUMBER, bookSupPhone);}
        else {
            Toast.makeText(this, getString(R.string.book_supplier_phone_number_is_required), Toast.LENGTH_SHORT).show();
            return;
        }
        values.put(BookEntry.BOOK_COLUMN_IMAGE, mImage);

        int newBook = getContentResolver().update(mCurrentUri, values, null, null);
        if (newBook == 0){
            Toast.makeText(this, getString(R.string.book_update_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.book_updated), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.detail_save_menu:
                saveBook();
                finish();
                return true;
            case R.id.detail_edit:
                Intent intent = new Intent(BookDetailActivity.this, BookEditorActivity.class);
                intent.setData(mCurrentUri);
                startActivity(intent);
                return true;
            case R.id.detail_delete_menu:
                 showsDeleteWarningDialog();
                 return true;
        }
        return super.onOptionsItemSelected(item);
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
            double priceB= cursor.getDouble(price);
            int quantityB = cursor.getInt(quantity);
            String supplierB = cursor.getString(supplier);
            String emailB = cursor.getString(email);
            String phoneB = cursor.getString(phone);
            int imageB = cursor.getInt(image);

            mBookName.setText(nameB);
            mBookAuthor.setText(authorB);
            mBookPrice.setText(Double.toString(priceB));
            mBookQuantity.setText(Integer.toString(quantityB));
            mSupName.setText(supplierB);
            mSupEmail.setText(emailB);
            mSupPhone.setText(phoneB);

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
        mBookName.setText("");
        mBookAuthor.setText("");
        mBookPrice.setText("");
        mBookQuantity.setText("");
        mSupName.setText("");
        mSupEmail.setText("");
        mSupPhone.setText("");
        mImageSpinner.setSelection(0);
    }
}
