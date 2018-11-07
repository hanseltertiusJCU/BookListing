package com.example.android.booklisting;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /** Tag for the log messages */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView booksListView = (ListView) findViewById(R.id.list);

        ArrayList<Book> books = QueryUtils.extractBooks(QueryUtils.BOOKS_API_REQUEST_URL);

        // Create a new {@link ArrayAdapter} of books
        adapter = new BookAdapter(this, books);

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

        BookAsyncTask task = new BookAsyncTask();
        task.execute(QueryUtils.BOOKS_API_REQUEST_URL);

    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Book> books = QueryUtils.fetchBooksData(urls[0]);

            // Return the {@link List<Book>} object
            return books;
        }

        @Override
        protected void onPostExecute(List<Book> book) {
            // Clear the adapter of previous earthquake data
            adapter.clear();
            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (book != null && ! book.isEmpty()) {
                adapter.addAll(book);
            }
        }
    }
}
