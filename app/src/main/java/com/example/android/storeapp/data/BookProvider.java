package com.example.android.storeapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.storeapp.data.BookContract.BookEntry;


/**
 * Created by Noemi on 1/12/2018.
 */

public class BookProvider extends ContentProvider {

    private static final String LOG_TAG = BookProvider.class.getSimpleName();

     private BookDbHelper mDbHelper;

     private static final int BOOK_ID = 111;
     private static final int BOOKS = 123;

     private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
     static {
         sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.BOOK_PATH, BOOKS);
         sUriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.BOOK_PATH + "/#", BOOK_ID);
     }

    @Override
    public boolean onCreate() {
        mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,  String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case BOOKS:
                cursor= database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BookEntry.BOOK_COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
         final int match = sUriMatcher.match(uri);
         switch (match){
             case BOOKS:
                 return insertBook(uri, contentValues);
             default:
                 throw new IllegalArgumentException("Insertion is not supported for " + uri);
         }
    }

    private Uri insertBook( Uri uri, ContentValues values){
        String name = values.getAsString(BookEntry.BOOK_COLUMN_TITLE);
        if (name == null){
            throw new IllegalArgumentException("Book requires a name");
        }
        String author = values.getAsString(BookEntry.BOOK_COLUMN_AUTHOR);
        if (author == null){
            throw  new IllegalArgumentException("Book requires an author");
        }
        Double price = values.getAsDouble(BookEntry.BOOK_COLUMN_PRICE);
        if (price <0 && price!=null){
            throw new IllegalArgumentException("Book requires a price");
        }
        Integer quantity = values.getAsInteger(BookEntry.BOOK_COLUMN_QUANTITY);
        if (quantity<0 && quantity!=null){
            throw new IllegalArgumentException("Book requires a quantity");
        }
        String supName = values.getAsString(BookEntry.BOOK_COLUMN_SUPPLIER_NAME);
        if (supName == null){
            throw new IllegalArgumentException("Book requires a supplier name");
        }
        String supEmail = values.getAsString(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL);
        if (supEmail == null){
            throw new IllegalArgumentException("Book requires a supplier email address");
        }
        String supPhone = values.getAsString(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE_NUMBER);
        if (supPhone == null){
            throw new IllegalArgumentException("Book requires a valid phone number");
        }
        Byte image = values.getAsByte(BookEntry.BOOK_COLUMN_IMAGE);
        if (image == null){
            throw new IllegalArgumentException("Book requires an image");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(BookEntry.TABLE_NAME, null, values);
        if (id == -1){
            Log.e(LOG_TAG, "Error insert row for" + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update( Uri uri, ContentValues values, String selection,  String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
               return updateBook(uri, values, selection, selectionArgs);
            case BOOK_ID:
                selection = BookEntry.BOOK_COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Cannot update for URI" + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection,  String[] selectionArgs ){
        if (values.containsKey(BookEntry.BOOK_COLUMN_TITLE)){
            String name = values.getAsString(BookEntry.BOOK_COLUMN_TITLE);
            if (name == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }
        if (values.containsKey(BookEntry.BOOK_COLUMN_AUTHOR)){
            String author = values.getAsString(BookEntry.BOOK_COLUMN_AUTHOR);
            if (author == null){
                throw new IllegalArgumentException("Book require an author");
            }
        }
        if (values.containsKey(BookEntry.BOOK_COLUMN_PRICE)){
            Double price = values.getAsDouble(BookEntry.BOOK_COLUMN_PRICE);
            if (price<0 && price!=null){
                throw new IllegalArgumentException("Book requires a price");
            }
        }
        if (values.containsKey(BookEntry.BOOK_COLUMN_QUANTITY)){
            Integer quantity = values.getAsInteger(BookEntry.BOOK_COLUMN_QUANTITY);
            if (quantity<0 && quantity!=null){
                throw new IllegalArgumentException("Book requires a quantity");
            }
        }
        if (values.containsKey(BookEntry.BOOK_COLUMN_SUPPLIER_NAME)){
            String supName = values.getAsString(BookEntry.BOOK_COLUMN_SUPPLIER_NAME);
            if (supName == null){
                throw new IllegalArgumentException("Book requires supplier's name");
            }
        }
        if (values.containsKey(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL)){
            String supEmail = values.getAsString(BookEntry.BOOK_COLUMN_SUPPLIER_EMAIL);
            if (supEmail == null){
                throw new IllegalArgumentException("Book requires supplier's email address");
            }
        }
        if (values.containsKey(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE_NUMBER)){
            String supPhone = values.getAsString(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE_NUMBER);
            if (supPhone == null){
                throw new IllegalArgumentException("Book requires supplier's phone number");
            }
        }
        if (values.containsKey(BookEntry.BOOK_COLUMN_IMAGE)){
            Byte image = values.getAsByte(BookEntry.BOOK_COLUMN_IMAGE);
            if (image == null){
                throw new IllegalArgumentException("Book requires image");
            }
        }
        if (values.size()==0){
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdate = database.update(BookEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdate != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdate;

    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match){
            case BOOKS:
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted!=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case BOOK_ID:
                selection= BookEntry.BOOK_COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted!=0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for URI "+ uri);
        }
    }



    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case BOOKS:
                return BookEntry.DYR_TYPE;
            case BOOK_ID:
                return BookEntry.ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + "with match " + match );
        }
    }

}
