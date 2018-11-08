package com.example.android.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

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

        final ListView booksListView = (ListView) findViewById(R.id.list);

        final SearchView searchTextView = (SearchView) findViewById(R.id.search_text);

        // Set the SearchView listener for handling inputs
        searchTextView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Do something when we click "Submit" button
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Start the BookAsyncTask
                BookAsyncTask task = new BookAsyncTask();
                task.execute(QueryUtils.getUrl(searchTextView.getQuery().toString()));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Search for search button
        Button searchButton = (Button) findViewById(R.id.search_button);

        // Create an {@link ArrayList<Book>} object by calling on extractBooks from QueryUtils
        ArrayList<Book> books = QueryUtils.extractBooks(QueryUtils.getUrl(searchTextView.getQuery().toString()));

        // Create a new {@link ArrayAdapter} of books
        adapter = new BookAdapter(MainActivity.this, books);

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
                // Launch a new activity to view the book URI
                startActivity(playStoreIntent);
            }
        });

        // Set onClickListener into the Button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the BookAsyncTask
                BookAsyncTask task = new BookAsyncTask();
                task.execute(QueryUtils.getUrl(searchTextView.getQuery().toString()));

                // Search for LinearLayout
                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

                // Hide the keyboard after clicking on searchButton
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
            }
        });

    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Book> booksList = QueryUtils.fetchBooksData(urls[0]);

            // Return the {@link List<Book>} object
            return booksList;
        }

        @Override
        protected void onPostExecute(List<Book> book) {
            // Clear the adapter of previous book data (in order to avoid )
            if(adapter != null){
                adapter.clear();
            }
            // If there is a valid list of {@link Book}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (book != null && ! book.isEmpty()) {
                adapter.addAll(book);
            }
        }
    }
}
