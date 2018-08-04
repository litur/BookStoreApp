package com.example.android.bookstoreapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.bookstoreapp.data.BookContract;

public class ProductEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Cursor mCursor;
    Uri mCurrentBookUri;
    private EditText titleET;
    private EditText authorET;
    private EditText quantiyET;
    private EditText priceET;
    private Button saveButton;
    private String LOGTAG = "EditorActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);


        // Sets the Toolbar as Action Bar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Defines custom behaviour on the Action Bar
        ActionBar myActionBar = getSupportActionBar();
        assert myActionBar != null;
        myActionBar.setElevation(8);
        // Displays the Up button in the Action Bar
        myActionBar.setDisplayHomeAsUpEnabled(true);
        myActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);

        // Initializes views
        titleET = findViewById(R.id.titleET);
        authorET = findViewById(R.id.authorET);
        quantiyET = findViewById(R.id.quantityET);
        priceET = findViewById(R.id.priceET);
        saveButton = findViewById(R.id.saveBtn);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new item or editing an existing one.
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        // If the intent DOES NOT contain a pet content URI, then we know that we are
        // creating a new pet.
        if (mCurrentBookUri == null) {
            myActionBar.setTitle(getString(R.string.editor_activity_title_new_product));
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            //invalidateOptionsMenu();
        } else {
            myActionBar.setTitle(getString(R.string.editor_activity_title_edit_product));
            // Initialize a loader to read the product/book data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(Constants.SINGLE_BOOK_LOADER_ID, null, this);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(LOGTAG, "Click on Save Button");
                Save();
                finish();
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_BOOK_TITLE,
                BookContract.BookEntry.COLUMN_BOOK_AUTHOR,
                BookContract.BookEntry.COLUMN_BOOK_QUANTITY,
                BookContract.BookEntry.COLUMN_BOOK_PRICE
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,       // Parent activity context
                mCurrentBookUri,                 // Query the content URI for the current book/product
                projection,                     // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                // No selection arguments
                null);                 // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int titleColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_TITLE);
            int authorColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_AUTHOR);
            int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_BOOK_PRICE);

            // Extract out the value from the Cursor for the given column index
            String title = cursor.getString(titleColumnIndex);
            String author = cursor.getString(authorColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            float price = cursor.getInt(priceColumnIndex);
            price = price / 100;

            // Update the views on the screen with the values from the database
            titleET.setText(title);
            authorET.setText(author);
            quantiyET.setText(Integer.toString(quantity));
            priceET.setText(Float.toString(price));
        }
    }

    // This is called when the last Cursor provided to onLoadFinished()
    // above is about to be closed.  We need to make sure we are no
    // longer using it.

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        titleET.setText("");
        authorET.setText("");
        quantiyET.setText("");
    }

    /**
     * Saves the data in the form for both new and update cases
     */
    private void Save() {
        ContentValues myBookData = new ContentValues();
        Uri insertUri;

        // Retrieves the data from the views in the form
        String title = String.valueOf(titleET.getText());
        String author = String.valueOf(authorET.getText());
        int quantity = Integer.parseInt(quantiyET.getText().toString());
        float floatPrice = Float.parseFloat(priceET.getText().toString()) * 100;
        int intPrice = Math.round(floatPrice);

        //TODO Handle validation here

        myBookData.put(BookContract.BookEntry.COLUMN_BOOK_TITLE, title);
        myBookData.put(BookContract.BookEntry.COLUMN_BOOK_AUTHOR, author);
        myBookData.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        myBookData.put(BookContract.BookEntry.COLUMN_BOOK_PRICE, intPrice);

        if (mCurrentBookUri == null) {
            insertUri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, myBookData);
            if (insertUri != null)
                Utility.showToast(getString(R.string.editor_activity_newItemInserted), this);
            else
                Utility.showToast(getString(R.string.editor_activity_newItemNotInserted), this);
        } else {
            // Insert the new row, returning the primary key value of the new row
            int nRowsUpdated = getContentResolver().update(mCurrentBookUri, myBookData, null, null);
            if (nRowsUpdated == 1)
                Utility.showToast(getString(R.string.editor_activity_editItemUpdated), this);
            else
                Utility.showToast(getString(R.string.editor_activity_editItemNotUpdated), this);
        }

    }

    // Inflates buttons in the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);

        //Hides the search button
        MenuItem settings_item = menu.findItem(R.id.action_search);
        settings_item.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    // Implements the Up Button functionality as explained in https://developer.android.com/training/implementing-navigation/ancestral
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}