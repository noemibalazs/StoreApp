package com.example.android.storeapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.storeapp.data.BookContract.BookEntry;



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
        final TextView phoneView = view.findViewById(R.id.adapter_phone);

        int nameIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_TITLE);
        int authorIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_AUTHOR);
        final int quantityIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_QUANTITY);
        int imageIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_IMAGE);
        int phoneIndex = cursor.getColumnIndex(BookEntry.BOOK_COLUMN_SUPPLIER_PHONE_NUMBER);


        String name = cursor.getString(nameIndex);
        String author = cursor.getString(authorIndex);
        int quantity = cursor.getInt(quantityIndex);
        int image = cursor.getInt(imageIndex);
        String phone = cursor.getString(phoneIndex);


        nameView.setText(name);
        authorView.setText(author);
        numberView.setText(Integer.toString(quantity));
        imageView.setImageResource(image);
        phoneView.setText(phone);


        TextView sellText = view.findViewById(R.id.sell_list_view);
        sellText.setOnClickListener(new View.OnClickListener() {
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

        final TextView orderText = view.findViewById(R.id.order_list_view);
        orderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneView.getText()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }

            }
        });
        


    }
}
