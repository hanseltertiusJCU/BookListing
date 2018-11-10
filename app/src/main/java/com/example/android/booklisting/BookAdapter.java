package com.example.android.booklisting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, List<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        ImageView imageURL = (ImageView) listItemView.findViewById(R.id.imageItem);
        imageURL.setTag(currentBook.getmImageUrl());
        new DownloadImagesTask().execute(imageURL);

//        Picasso.with(getContext()).load(currentBook.getmImageUrl()).into(imageURL);

        TextView bookTitle = (TextView) listItemView.findViewById(R.id.bookTitle);
        bookTitle.setText(currentBook.getmBookTitle());

        TextView bookAuthor = (TextView) listItemView.findViewById(R.id.bookAuthor);
        bookAuthor.setText(currentBook.getmBookAuthor());

        return listItemView;
    }

}
