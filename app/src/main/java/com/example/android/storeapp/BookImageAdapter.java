package com.example.android.storeapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Noemi on 1/13/2018.
 */

public class BookImageAdapter extends BaseAdapter {

    Context context;
    ArrayList<Integer> arrayList;

    public BookImageAdapter(Context context, ArrayList<Integer>imageList){

        this.context = context;
        arrayList = imageList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return arrayList.get(i);
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        }

        Integer bImage = arrayList.get(position);

        ImageView insertImage = (ImageView)convertView.findViewById(R.id.image_list_item);
        insertImage.setImageResource(bImage);

        return convertView;
    }
}
