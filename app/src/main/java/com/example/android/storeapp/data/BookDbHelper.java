package com.example.android.storeapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.storeapp.data.BookContract.BookEntry;


/**
 * Created by Noemi on 1/11/2018.
 */

public class BookDbHelper extends SQLiteOpenHelper {

    private static final int BOOK_VERSION = 1;
    private static final String BOOK_DATABASE = "library.db";

    public BookDbHelper (Context context){
        super(context, BOOK_DATABASE, null, BOOK_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " ("
                + BookEntry.BOOK_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.BOOK_COLUMN_TITLE + " TEXT NOT NULL, "
                + BookEntry.BOOK_COLUMN_AUTHOR + " TEXT NOT NULL, "
                + BookEntry.BOOK_COLUMN_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + BookEntry.BOOK_COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + BookEntry.BOOK_COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL, "
                + BookEntry.BOOK_COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
