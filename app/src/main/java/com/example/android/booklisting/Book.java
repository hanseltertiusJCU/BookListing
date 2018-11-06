package com.example.android.booklisting;

import java.net.URL;

public class Book {
    private URL mImageUrl;
    private String mBookTitle;
    private String mBookAuthor;

    public Book (URL imageUrl, String bookTitle, String bookAuthor){
        mImageUrl = imageUrl;
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
    }

    public URL getmImageUrl() {
        return mImageUrl;
    }

    public String getmBookTitle() {
        return mBookTitle;
    }

    public String getmBookAuthor() {
        return mBookAuthor;
    }
}
