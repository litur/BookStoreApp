package com.example.android.bookstoreapp.data;

import android.provider.BaseColumns;

public final class BookContract {

    private BookContract() {
    }

    public static final class BookEntry implements BaseColumns {

        /** Name of database table for pets */
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
        public static final int LANGUAGE_UNKNOWN = 0;
        public static final int LANGUAGE_ENGLISH = 1;
        public static final int LANGUAGE_ITALIAN = 2;

    }
}
