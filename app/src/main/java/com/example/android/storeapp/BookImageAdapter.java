package com.example.android.storeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Noemi on 1/13/2018.
 */

public class BookImageAdapter extends BaseAdapter {

    ArrayList<Integer> arrayList;
    Context context;

    public BookImageAdapter(Context context, ArrayList<Integer> imageList){
        this.context = context;
        arrayList = imageList;
    }

    @Override
    public int getCount() {
        return arrayList.size();

    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        }
        Integer bImage = arrayList.get(position);

        ImageView insertImage = convertView.findViewById(R.id.image_list_item);
        insertImage.setImageResource(bImage);

        return convertView;
    }
}