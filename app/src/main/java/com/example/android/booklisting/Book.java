package com.example.android.booklisting;

public class Book {
    private String mImageUrl;
    private String mBookTitle;
    private String mBookAuthor;

    public Book (String imageUrl, String bookTitle, String bookAuthor){
        mImageUrl = imageUrl;
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmBookTitle() {
        return mBookTitle;
    }

    public String getmBookAuthor() {
        return mBookAuthor;
    }
}
