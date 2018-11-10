package com.example.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // We temporarily use public (and we have to find the way to adapt to search for links)
    private static final String BOOKS_API_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private static String imageUrl;
    private static String authorsList;

    private QueryUtils() {

    }

    /**
     * Get the url after entering the search text
     *
     * @param searchText
     * @return
     */
    public static String getUrl(String searchText) {
        return BOOKS_API_REQUEST_URL + searchText;
    }

    /**
     * Fetch book data and return an {@link List<Book>} object to represent
     * a list of books
     */
    public static List<Book> fetchBooksData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and
        // create an {@link List<Book>} object
        List<Book> books = extractBooks(jsonResponse);

        Log.e(LOG_TAG, "Fetching the data from List<Book> object");

        // Return the {@link List<Book>} object
        return books;
    }


    /**
     * Extract book data and return an {@link ArrayList<Book>} object to represent
     * a list of books
     */
    public static ArrayList<Book> extractBooks(String booksJSON) {

        // If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(booksJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        ArrayList<Book> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the SAMPLE_JSON_RESPONSE string
            JSONObject baseJsonResponse = new JSONObject(booksJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of items (or books).
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            // For each book in the bookArray, create an {@link Book} object
            for (int i = 0; i < bookArray.length(); i++) {

                // Get a single book at position i within the list of books
                JSONObject currentBook = bookArray.getJSONObject(i);

                // For a given books, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a list of all volume info
                // for that book.
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                // Extract the value for the key called "smallThumbnail" (need to get value imagelinks)
                if (volumeInfo.has("imageLinks")) {
                    imageUrl = (volumeInfo.getJSONObject("imageLinks")).getString("smallThumbnail");
                }


                // Extract the value for the key called "title"
                String bookTitle = volumeInfo.getString("title");

                // Get the JSONArray from JSONObject called volumeInfo that extracts
                // the value for the key called "authors"
                if (volumeInfo.has("authors")) {
                    JSONArray bookAuthorsList = volumeInfo.getJSONArray("authors");

                    // Create a String object that retrieve a list of authors
                    authorsList = "";

                    // Loop for adding object(s) in bookAuthorsList into the authorsList
                    for (int a = 0; a < bookAuthorsList.length(); a++) {
                        if (a == bookAuthorsList.length() - 1) {
                            authorsList += (String) bookAuthorsList.get(a);
                        } else {
                            authorsList += bookAuthorsList.get(a) + ", ";
                        }

                    }
                }

                // Extract the value for the key called "infoLink"
                String bookLink = volumeInfo.getString("infoLink");

                // Create a new {@link Book} object with the imageUrl, bookTitle and authorsList
                // from the JSON response.
                Book book = new Book(imageUrl, bookTitle, authorsList, bookLink);

                // Add the new {@link Book} to the list of books.
                books.add(book);

            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {

        // Initialize a String object that represents jsonResponse
        String jsonResponse = "";
        // if URL is null, we return empty jsonResponse
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error Response code: " +
                        urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,
                    "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
