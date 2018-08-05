package com.example.android.bookstoreapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.android.bookstoreapp.data.BookContract.BASE_CONTENT_URI;
import static com.example.android.bookstoreapp.data.BookContract.CONTENT_AUTHORITY;

public final class SupplierContract {

    private SupplierContract(){}

    public static final class SupplierEntry implements BaseColumns {

        public static final String PATH_SUPPLIERS = "suppliers";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUPPLIERS);

            /** Name of database table for suppliers */
            public final static String TABLE_NAME = "suppliers";

            /**
             * Unique ID number for the supplier (only for use in the database table).
             * Type: INTEGER
             */
            public final static String _ID = BaseColumns._ID;

            /**
             * Name of the supplier.
             * Type: TEXT
             */
            public final static String COLUMN_SUPPLIER_NAME ="name";

            /**
             * Phone number of the supplier
             * Type: TEXT
             */
            public final static String COLUMN_SUPPLIER_PHONE = "phonenumber";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUPPLIERS;

    }
}