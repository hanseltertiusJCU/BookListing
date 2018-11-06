package com.example.android.booklisting;

import java.net.URL;

public class Book {
    private URL mImageUrl;
    private String mBookName;
    private String mBookAuthor;

    public Book (URL imageUrl, String bookName, String bookAuthor){
        mImageUrl = imageUrl;
        mBookName = bookName;
        mBookAuthor = bookAuthor;
    }

    public URL getmImageUrl() {
        return mImageUrl;
    }

    public String getmBookName() {
        return mBookName;
    }

    public String getmBookAuthor() {
        return mBookAuthor;
    }
}
