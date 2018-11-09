package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

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
    private TextView mEmptyStateTextView;
    private View loadingIndicator;
    private ListView booksListView;

    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        booksListView = (ListView) findViewById(R.id.list);

        // Search for View that shows loading indicator
        loadingIndicator = findViewById(R.id.loading_indicator);

        // Search for SearchView
        searchTextView = (SearchView) findViewById(R.id.search_text);

        // Create a LoaderManager object by calling getLoaderManager() method
        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        booksListView.setEmptyView(mEmptyStateTextView);

        // Set the SearchView listener for handling inputs
        searchTextView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Do something when we click "Submit" button
            @Override
            public boolean onQueryTextSubmit(String query) {

                mEmptyStateTextView.setText(null);

                booksListView.setVisibility(View.GONE);

                loadingIndicator.setVisibility(View.VISIBLE);

                // Restart the loader
                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);

                // Disable the searchTextView after clicking on "Submit/Enter" button
                searchTextView.clearFocus();
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

                mEmptyStateTextView.setText(null);

                booksListView.setVisibility(View.GONE);

                loadingIndicator.setVisibility(View.VISIBLE);

                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);

                // Disable the searchTextView after clicking on "Search" button
                searchTextView.clearFocus();
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

        // Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        booksListView.setVisibility(View.VISIBLE);

        // Set empty state text to display "There are no books to display."
        mEmptyStateTextView.setText(R.string.no_books_data);

        // Clear the adapter of previous book data (in order to avoid deletion on empty adapter)
        if(adapter != null){
            adapter.clear();
        }
        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (book != null && ! book.isEmpty()) {
            adapter.addAll(book);
        }

        // Restore the ListView scroll position after the loader finished on loading
        if (mListState != null)
            booksListView.onRestoreInstanceState(mListState);

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }

    // Restore the ListView scroll position
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mListState = state.getParcelable(LIST_STATE);
    }

    // Disable the SearchView when resuming the Activity as well as restoring the
    // ListView scroll position by calling onRestoreInstanceState method
    @Override
    protected void onResume() {
        super.onResume();
        searchTextView.clearFocus();
        if (mListState != null)
            booksListView.onRestoreInstanceState(mListState);
    }

    // Save the listView scroll position
    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        mListState = booksListView.onSaveInstanceState();
        state.putParcelable(LIST_STATE, mListState);
    }
}
