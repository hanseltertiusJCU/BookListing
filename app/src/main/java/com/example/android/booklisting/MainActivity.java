package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    /** Tag for the log messages */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    private BookAdapter adapter;
    private SearchView searchTextView;

    // The callbacks through which we will interact with the LoaderManager.
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView booksListView = (ListView) findViewById(R.id.list);

        searchTextView = (SearchView) findViewById(R.id.search_text);

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        // Set the SearchView listener for handling inputs
        searchTextView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Do something when we click "Submit" button
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Start the BookAsyncTask
                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
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

                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);

                // Search for LinearLayout
                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

                // Hide the keyboard after clicking on searchButton
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
            }
        });

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(QueryUtils.getUrl(searchTextView.getQuery().toString()));
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new BookLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> book) {
        // Clear the adapter of previous book data (in order to avoid deletion on empty adapter)
        if(adapter != null){
            adapter.clear();
        }
        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (book != null && ! book.isEmpty()) {
            adapter.addAll(book);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }

}
