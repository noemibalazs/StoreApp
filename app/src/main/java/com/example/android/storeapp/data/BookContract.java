package com.example.android.storeapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.storeapp.R;

import java.net.PortUnreachableException;
import java.security.PublicKey;

/**
 * Created by Noemi on 1/11/2018.
 */

public final class BookContract {

    private BookContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.storeapp";
    public static final String BOOK_PATH = "books";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class BookEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, BOOK_PATH);

        public static final String DYR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + BOOK_PATH;

        public static final String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + BOOK_PATH;

        public static final String TABLE_NAME = "books";

        public static final String BOOK_COLUMN_ID = BaseColumns._ID;

        public static final String BOOK_COLUMN_TITLE = "Product";
        public static final String BOOK_COLUMN_AUTHOR = "Author";
        public static final String BOOK_COLUMN_PRICE = "Price";
        public static final String BOOK_COLUMN_QUANTITY = "Quantity";
        public static final String BOOK_COLUMN_SUPPLIER_NAME = "Supplier_Name";
        public static final String BOOK_COLUMN_SUPPLIER_EMAIL = "Supplier_Email";
        public static final String BOOK_COLUMN_SUPPLIER_PHONE_NUMBER = "Supplier_Phone_Number";
        public static final String BOOK_COLUMN_IMAGE = "Image";

        public static final byte BOOK_EBOOK = (byte) R.drawable.ebook;
        public static final byte BOOK_BOOK = (byte) R.drawable.book;
        public static final byte BOOK_AUDIO = (byte)R.drawable.b_a;


    }
}
