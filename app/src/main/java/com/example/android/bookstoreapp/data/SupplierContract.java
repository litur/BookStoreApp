package com.example.android.bookstoreapp.data;

import android.provider.BaseColumns;

public final class SupplierContract {

    private SupplierContract(){}

    public static final class SupplierEntry implements BaseColumns {

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

    }
}