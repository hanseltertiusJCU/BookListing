package com.example.android.booklisting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, List<Book> books){
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        Book currentBook = getItem(position);

        ImageView imageURL = (ImageView) listItemView.findViewById(R.id.imageItem);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(currentBook.getmImageUrl().openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageURL.setImageBitmap(bitmap);

        TextView bookTitle = (TextView) listItemView.findViewById(R.id.bookTitle);
        bookTitle.setText(currentBook.getmBookTitle());

        TextView bookAuthor = (TextView) listItemView.findViewById(R.id.bookAuthor);
        bookAuthor.setText(currentBook.getmBookAuthor());

        return listItemView;
    }
}
