package com.example.android.storeapp;

import android.graphics.Bitmap;

import com.example.android.storeapp.data.BookContract;

/**
 * Created by Noemi on 1/13/2018.
 */

public class BookImage {

    public int mImage;

    public BookImage( int image){
        mImage = image;
    }

    public int getImage(){
        return mImage;
    }
}

