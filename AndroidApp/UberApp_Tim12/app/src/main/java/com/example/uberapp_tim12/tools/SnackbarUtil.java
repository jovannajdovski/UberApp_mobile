package com.example.uberapp_tim12.tools;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtil {

    public static void show(View contextView, String message) {
        Snackbar.make(contextView, message, Snackbar.LENGTH_SHORT)
                .show();
    }
}
