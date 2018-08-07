package com.example.android.bookstoreapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.bookstoreapp.data.BookContract;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class BookCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.book_element, parent, false);
    }

    /**
     * This method binds the book data (in the current row pointed to by cursor) to the given
     * list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final int quantity;
        float price;
        String strQuantity;
        final String productName;
        final Uri singleBookUri;

        TextView productTV;
        TextView authorTV;
        TextView priceTV;
        TextView quantityTV;
        ImageButton buttonRemove;
        CardView myCardView;

        // Figure out the index of each column
        int idColunmIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
        int productColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_TITLE);
        int authorColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_AUTHOR);
        int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);

        // Initialize the TV and buttons variables
        productTV = view.findViewById(R.id.productNameTV);
        authorTV = view.findViewById(R.id.authorTV);
        priceTV = view.findViewById(R.id.priceTV);
        quantityTV = view.findViewById(R.id.quantityTV);
        buttonRemove = view.findViewById(R.id.button_remove);
        myCardView = view.findViewById(R.id.productCardView);

        // Assign to the TV the values coming from the cursor
        productName = cursor.getString(productColumnIndex);
        productTV.setText(productName);
        authorTV.setText(cursor.getString(authorColumnIndex));
        //Price
        price = cursor.getInt(priceColumnIndex);
        price = price / 100;
        //priceTV.setText(Float.toString(price));
        priceTV.setText(NumberFormat.getCurrencyInstance(Locale.ITALY).format(price));

        // for quantity we show a different message based on the quantity variable
        quantity = cursor.getInt(quantityColumnIndex);
        if (quantity > 0) {
            strQuantity = context.getString(R.string.in_stock, String.valueOf(quantity));
            quantityTV.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else {
            strQuantity = context.getString(R.string.out_of_stock);
            quantityTV.setBackgroundColor(context.getResources().getColor(R.color.primary_dark));
        }


        quantityTV.setText(strQuantity);

        //We create the Uri for the single Book. We will use the Uri to update the Quantity and set the Intent
        // to open the Editor Activity
        singleBookUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, cursor.getInt(idColunmIndex));

        myCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(context, ProductEditActivity.class);
                editIntent.setData(singleBookUri);
                context.startActivity(editIntent);
                Log.e("Pippo", "Click sulla cardView");
            }
        });

        // Sets a ClickListener to decrease the quantity by one unit
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Bottone", "Sell button Clicked for Book " + productName);

                ContentValues myBooksData = new ContentValues();

                // if the in stock quantity of the product is already 0, we cannot decrease it further
                if (quantity == 0) {
                    Utility.showToast(context.getString(R.string.quantity_already_zero), context);
                    return;
                }
                // if the in stock quantity of the product is higher than 0, we can decrease it further
                myBooksData.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, quantity - 1);

                // Insert the new row, returning the primary key value of the new row
                int nRowsUpdated = context.getContentResolver().update(singleBookUri, myBooksData, null, null);
                if (nRowsUpdated == 1)
                    Utility.showToast(context.getString(R.string.item_sold), context);
            }
        });


    }
}
