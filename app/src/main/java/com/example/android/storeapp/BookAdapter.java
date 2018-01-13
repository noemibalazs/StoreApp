package com.example.android.storeapp;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.INotificationSideChannel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.storeapp.data.BookContract.BookEntry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
;

/**
 * Created by Noemi on 1/11/2018.
 */

public class BookAdapter extends CursorAdapter {

    public BookAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameView = view.findViewById(R.id.adapter_name);
        TextView authorView = view.findViewById(R.id.adapter_author);
        TextView numberView = view.findViewById(R.id.adapter_number);
        ImageView imageView = view.findViewById(R.id.adapter_image);

        int nameIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_TITLE);
        int authorIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_AUTHOR);
        int quantityIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_QUANTITY);
        int imageIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_IMAGE);

        String name = cursor.getString(nameIndex);
        String author = cursor.getString(authorIndex);
        int quantity = cursor.getInt(quantityIndex);

        byte[] imageInByte = cursor.getBlob(imageIndex);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageInByte);
        Bitmap theImage = BitmapFactory.decodeStream(inputStream);

        nameView.setText(name);
        authorView.setText(author);
        numberView.setText(Integer.toString(quantity));
        imageView.setImageBitmap(theImage);
        


    }
}
