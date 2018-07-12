package com.example.android.bookstoreapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.bookstoreapp.data.BookContract.BookEntry;
import com.example.android.bookstoreapp.data.BookStoreDbHelper;
import com.example.android.bookstoreapp.data.SupplierContract;
import com.example.android.bookstoreapp.data.SupplierContract.SupplierEntry;

import static com.example.android.bookstoreapp.Utility.showToast;

public class MainActivity extends AppCompatActivity {

    private String LOGTAG = "Main Activity";
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
        myActionBar.setElevation(8);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        BookStoreDbHelper mDbHelper = new BookStoreDbHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String myTableName = "";

        // projection is an array containing the fields we want to retrieve with our query
        String[] projection = { BookEntry.TABLE_NAME + "." + BookEntry._ID, BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_BOOK_AUTHOR,
                BookEntry.COLUMN_BOOK_LANGUAGE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_SUPPLIER_ID,
                SupplierEntry.COLUMN_SUPPLIER_NAME};

        Cursor cursor = db.query(BookEntry.TABLE_NAME + " LEFT OUTER JOIN " + SupplierEntry.TABLE_NAME + " ON books." + BookEntry.COLUMN_BOOK_SUPPLIER_ID + " = suppliers._id", projection,
                null, null,
                null, null, null);

        TextView displayView = (TextView) findViewById(R.id.queryResult_textview);

        try {
            // Create a header in the Text View with the results from the Query

            displayView.setText("The book table contains " + cursor.getCount() + " books.\n\n");
            displayView.append(BookEntry._ID + " - " +
                    BookEntry.COLUMN_BOOK_TITLE +
                    " - " + BookEntry.COLUMN_BOOK_AUTHOR +
                    " - " + BookEntry.COLUMN_BOOK_LANGUAGE +
                    " - " + BookEntry.COLUMN_BOOK_QUANTITY +
                    " - " + BookEntry.COLUMN_BOOK_PRICE +
                    " - " + SupplierEntry.COLUMN_SUPPLIER_NAME +"\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_TITLE);
            int authorColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_AUTHOR);
            int languageColumnIndex =  cursor.getColumnIndex(BookEntry.COLUMN_BOOK_LANGUAGE);
            int quantityColumnIndex =  cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int supplierNameColumnIndex = cursor.getColumnIndex(SupplierEntry.COLUMN_SUPPLIER_NAME);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                String currentAuthor = cursor.getString(authorColumnIndex);
                int currentLanguage = cursor.getInt(languageColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                double currentPriceCent = cursor.getInt(priceColumnIndex);
                //BigDecimal currentPrice = new BigDecimal(currentPriceCent/100);
                double currentPrice = currentPriceCent/100;
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID +
                        " - " + currentTitle +
                        " - " + currentAuthor +
                        " - " + currentLanguage +
                        " - " + currentQuantity +
                        " - " + currentPrice) +
                        " - " + currentSupplierName);
            }
        }
            catch (Exception e){
            Log.e("Exception","Eccezione");

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

    }

    // Inserts fake (dummy) data in the Books table
    private void insertDummyData(){

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        BookStoreDbHelper mDbHelper = new BookStoreDbHelper(this);

        // Create and/or open a database to write in  it
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Inserts a record in supplier table
        ContentValues mySuppliersDummyData = new ContentValues();
        mySuppliersDummyData.put(SupplierContract.SupplierEntry.COLUMN_SUPPLIER_NAME, "Penguin Books");
        mySuppliersDummyData.put(SupplierContract.SupplierEntry.COLUMN_SUPPLIER_PHONE, "555-2537");
        // Insert the new row, returning the primary key value of the new row
        long newSupplierRowId = db.insert(SupplierEntry.TABLE_NAME, null, mySuppliersDummyData);
        Log.e(LOGTAG, "Inserted supplier row " + String.valueOf(newSupplierRowId));

        // Inserts a record in books table
        ContentValues myBooksDummyData = new ContentValues();

        myBooksDummyData.put(BookEntry.COLUMN_BOOK_TITLE,"America Pastoral");
        myBooksDummyData.put(BookEntry.COLUMN_BOOK_AUTHOR,"Philip Roth");
        myBooksDummyData.put(BookEntry.COLUMN_BOOK_LANGUAGE,BookEntry.LANGUAGE_ENGLISH);
        myBooksDummyData.put(BookEntry.COLUMN_BOOK_QUANTITY,1);
        myBooksDummyData.put(BookEntry.COLUMN_BOOK_PRICE, 990);
        // for the supplier ID we pass the ID  of the newly created row in supplier table
        myBooksDummyData.put(BookEntry.COLUMN_BOOK_SUPPLIER_ID,newSupplierRowId);

        // Insert the new row, returning the primary key value of the new row
        long newBookRowId = db.insert(BookEntry.TABLE_NAME, null, myBooksDummyData);
        Log.e(LOGTAG, "Inserted Book row " + String.valueOf(newBookRowId));
        showToast("Inserted Book row " + String.valueOf(newBookRowId),this);
    }

    // Inflates buttons in the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Sets action on the Settings button in the Action Bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // for now, it does nothing
            return true;
        }

        if (id == R.id.action_insert_dummy_data) {
            insertDummyData();
            displayDatabaseInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
