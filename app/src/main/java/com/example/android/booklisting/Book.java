package com.example.android.booklisting;

public class Book {
    private String mImageUrl;
    private String mBookTitle;
    private String mBookAuthor;
    private String mBookUrl;

    public Book (String imageUrl, String bookTitle, String bookAuthor, String bookUrl){
        mImageUrl = imageUrl;
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
        mBookUrl = bookUrl;
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

    public String getmBookUrl() {
        return mBookUrl;
    }
}
