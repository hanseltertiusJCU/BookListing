package com.example.android.booklisting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /** URL for earthquake data from the USGS dataset */
    private static final String BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView booksListView = (ListView) findViewById(R.id.list);

        ArrayList<Book> books = new ArrayList<>();

        books.add(new Book("http://books.google.com/books/content?id=qKFDDAAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api", "Android", "P.K. Dixit"));

        // Create a new {@link ArrayAdapter} of books
        BookAdapter adapter = new BookAdapter(this, books);

        booksListView.setAdapter(adapter);

    }
}
