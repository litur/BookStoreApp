package com.example.android.bookstoreapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.bookstoreapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private BookContract() {
    }

    public static final class BookEntry implements BaseColumns {

        public static final String PATH_BOOKS = "books";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);


        /**
         * Name of database table for books
         */
        public final static String TABLE_NAME = "books";

        /**
         * Unique ID number for the book (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Title of the book.
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_TITLE ="title";

        /**
         * Author of the book
         * Type: TEXT
         */
        public final static String COLUMN_BOOK_AUTHOR = "author";

        /**
         * Language of the book
         * Type: INTEGER
         */
        public final static String COLUMN_BOOK_LANGUAGE = "language";

        /**
         * Number of available copies of the book
         * Type: INTEGER
         */
        public final static String COLUMN_BOOK_QUANTITY = "quantity";

        /**
         * Price of the book in Eurocent
         * Type: REAL
         */
        public final static String COLUMN_BOOK_PRICE = "price";

        /**
         * ID of the supplier of the book. It references a record on the Supplier Table
         * Type: INTEGER
         */
        public final static String COLUMN_BOOK_SUPPLIER_ID = "supplier";

        /**
         * Possible values for the language of the book.
         */
        public static final int LANGUAGE_ENGLISH = 1;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single book.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;


    }
}
