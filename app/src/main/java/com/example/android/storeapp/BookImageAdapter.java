package com.example.android.storeapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Noemi on 1/13/2018.
 */

public class BookImageAdapter extends ArrayAdapter<BookImage> {

    public BookImageAdapter(Context context, ArrayList<BookImage>image){
        super(context, 0, image);
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.image_item, parent, false);
        }

        BookImage bImage = getItem(position);

        ImageView insertImage = (ImageView)listItemView.findViewById(R.id.image_list_item);
        insertImage.setImageResource(bImage.getImage());

        return listItemView;
    }
}
