package com.example.android.bookstoreapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.android.bookstoreapp.data.BookContract.BookEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mListView;
    private FloatingActionButton mFloatButton;

    // Setup an Adapter to create a list item for each row of book data in the Cursor.
    private BookCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sets the Toolbar as Action Bar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Defines custom behaviour on the Action Bar
        ActionBar myActionBar = getSupportActionBar();
        assert myActionBar != null;
        myActionBar.setTitle(com.example.android.bookstoreapp.R.string.mainActivity_title);
        myActionBar.setElevation(4);

        mListView = findViewById(R.id.bookListView);

        //Sets an Intent on the Add Button to open the EditActivity to insert a new Product/book
        mFloatButton = findViewById(R.id.float_add_button);
        mFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent insertIntent;
                insertIntent = new Intent(MainActivity.this, ProductEditActivity.class);
                startActivity(insertIntent);
            }
        });

        // Gets a reference to the LoaderManager, in order to interact with loaders.
        final android.app.LoaderManager myLoaderManager = getLoaderManager();

        myLoaderManager.initLoader(Constants.BOOKS_LOADER_ID, null, this).forceLoad();

        // sets the emptView for the listView
        mListView.setEmptyView(findViewById(R.id.empty_view));

        adapter = new BookCursorAdapter(this, null);

        // Attach the adapter to the ListView.
        mListView.setAdapter(adapter);

    }

    /**
     * * Inserts fake (dummy) data in the Book and supplier tables
     */
    private void insertDummyData(){

        // Inserts a record in books table
        ContentValues myBooksDummyData = new ContentValues();

        myBooksDummyData.put(BookEntry.COLUMN_BOOK_TITLE, "American Pastoral");
        myBooksDummyData.put(BookEntry.COLUMN_BOOK_AUTHOR,"Philip Roth");
        myBooksDummyData.put(BookEntry.COLUMN_BOOK_LANGUAGE,BookEntry.LANGUAGE_ENGLISH);
        myBooksDummyData.put(BookEntry.COLUMN_BOOK_QUANTITY,1);
        myBooksDummyData.put(BookEntry.COLUMN_BOOK_PRICE, 990);
        myBooksDummyData.put(BookEntry.COLUMN_BOOK_SUPPLIER_ID, "Penguin Books");

        // Insert the new row, returning the Uri of the new row
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, myBooksDummyData);
        if (newUri != null)
            Utility.showToast("Inserted a new book", this);
    }

    /**
     * Perform the deletion of all the books/products in the database
     */
    private void deleteAllProducts() {
        int nRowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
        if (nRowsDeleted > 0) {
            Utility.showToast(getString(R.string.delete_all_ok), this);
        } else
            Utility.showToast(getString(R.string.delete_all_error), this);
    }
    // Inflates buttons in the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Sets action on the buttons in the Action Bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if (id == R.id.action_insert_dummy_data) {
            insertDummyData();
            return true;
        }

        if (id == R.id.action_delete_all_data) {
            deleteAllProducts();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the Loader is created (see https://developer.android.com/reference/android/app/LoaderManager.LoaderCallbacks)
     * @param i The ID whose loader is to be created.
     * @param bundle Any arguments supplied by the caller.
     * @return a cursor Loader based on a query handled by the ContentProvider
     */
    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String mySortOrder;

        //Use SharedPreferences to define SortOrder
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        switch (orderBy) {
            case "name":
                mySortOrder = BookEntry.COLUMN_BOOK_TITLE + " ASC";
                break;
            case "author":
                mySortOrder = BookEntry.COLUMN_BOOK_AUTHOR + " ASC";
                break;
            case "quantity_ASC":
                mySortOrder = BookEntry.COLUMN_BOOK_QUANTITY + " ASC";
                break;
            default:
                mySortOrder = "_ID DESC";
                break;
        }

        String[] projection = {BookEntry._ID,
                BookEntry.COLUMN_BOOK_AUTHOR,
                BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_PRICE};

        return new CursorLoader(this, BookEntry.CONTENT_URI, projection, null, null, mySortOrder);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor newCursor) {
        // Swap the new cursor in.  (The framework will take care of closing the old cursor once we return.)
        adapter.swapCursor(newCursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        adapter.swapCursor(null);
    }


}
