package com.example.android.booklisting;

import android.graphics.Bitmap;

public class Book {
    private Bitmap mImageUrlBitmap;
    private String mBookTitle;
    private String mBookAuthor;
    private String mBookUrl;

    public Book (Bitmap imageUrlBitmap, String bookTitle, String bookAuthor, String bookUrl){
        mImageUrlBitmap = imageUrlBitmap;
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
        mBookUrl = bookUrl;
    }

    public Bitmap getmImageUrlBitmap() {
        return mImageUrlBitmap;
    }

    public String getmBookTitle() {
        return mBookTitle;
    }

    public String getmBookAuthor() {
        return mBookAuthor;
    }

    public String getmBookUrl() {
        return mBookUrl;
    }
}
