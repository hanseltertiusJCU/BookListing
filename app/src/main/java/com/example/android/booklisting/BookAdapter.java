package com.example.android.booklisting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        ImageView bookImageView = (ImageView) listItemView.findViewById(R.id.imageItem);
        bookImageView.setImageBitmap(formatImageFromBitmap(currentBook.getmImageUrlBitmap()));

        TextView bookTitle = (TextView) listItemView.findViewById(R.id.bookTitle);
        bookTitle.setText(currentBook.getmBookTitle());

        TextView bookAuthor = (TextView) listItemView.findViewById(R.id.bookAuthor);
        bookAuthor.setText(currentBook.getmBookAuthor());

        return listItemView;
    }

    // Get the thumbnail image
    private Bitmap formatImageFromBitmap(Bitmap bookThumbnail) {
        // Bitmap for image
        Bitmap bitmapResult;
        // Check if the thumbnail is valid
        if (bookThumbnail == null) {
            // If not valid return default image
            bitmapResult = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_image_available);
        } else {
            // If valid return image based on book thumbnail
            bitmapResult = bookThumbnail;
        }
        // Return bitmap
        return bitmapResult;
    }

}

