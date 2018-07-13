package com.example.android.bookstoreapp;

import android.content.Context;
import android.widget.Toast;

public class Utility {

        /**
         * Displays the given Message in a toast
         * @param toastMessage the message (string) to be displayed by the toast
         * @param context the context from which the function is called
         */
        static void showToast(String toastMessage, Context context) {

            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, toastMessage, duration);
            toast.show();
        }
    }

