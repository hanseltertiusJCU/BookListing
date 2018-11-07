package com.example.android.booklisting;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

        ArrayList<Book> books = QueryUtils.extractBooks();

        // Create a new {@link ArrayAdapter} of books
        final BookAdapter adapter = new BookAdapter(this, books);

        booksListView.setAdapter(adapter);

        // Set onItemClickListener into the ListView
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get a Book object
                Book currentBook = adapter.getItem(position);

                // Create an Uri object from String URL object
                Uri bookUri = Uri.parse(currentBook.getmBookUrl());

                // Create an implicit intent that takes URI object
                Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                // Launch a new activity to view the earthquake URI
                startActivity(playStoreIntent);
            }
        });

    }
}
