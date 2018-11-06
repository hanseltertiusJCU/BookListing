package com.example.android.booklisting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, List<Book> books){
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        ImageView imageURL = (ImageView) listItemView.findViewById(R.id.imageItem);
        Picasso.with(getContext()).load(currentBook.getmImageUrl()).into(imageURL);

        TextView bookTitle = (TextView) listItemView.findViewById(R.id.bookTitle);
        bookTitle.setText(currentBook.getmBookTitle());

        TextView bookAuthor = (TextView) listItemView.findViewById(R.id.bookAuthor);
        bookAuthor.setText(currentBook.getmBookAuthor());

        return listItemView;
    }

}
