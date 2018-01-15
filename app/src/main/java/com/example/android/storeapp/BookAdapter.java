package com.example.android.storeapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;


import com.example.android.storeapp.data.BookContract.BookEntry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


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
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView nameView = view.findViewById(R.id.adapter_name);
        TextView authorView = view.findViewById(R.id.adapter_author);
        final TextView numberView = view.findViewById(R.id.adapter_number);
        ImageView imageView = view.findViewById(R.id.adapter_image);

        int nameIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_TITLE);
        int authorIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_AUTHOR);
        final int quantityIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_QUANTITY);
        int imageIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_IMAGE);

        String name = cursor.getString(nameIndex);
        String author = cursor.getString(authorIndex);
        int quantity = cursor.getInt(quantityIndex);
        int image = cursor.getInt(imageIndex);

        nameView.setText(name);
        authorView.setText(author);
        numberView.setText(Integer.toString(quantity));
        imageView.setImageResource(image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = cursor.getInt(quantityIndex);
                if (quantity == 0){
                    Toast.makeText(context, "No more in stock. Please makes some new order!", Toast.LENGTH_SHORT).show();
                }else {
                    quantity = quantity - 1;
                    numberView.setText(Integer.toString(quantity));

                }
            }
        });
        


    }
}
