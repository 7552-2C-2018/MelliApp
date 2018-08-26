package com.a7552_2c_2018.melliapp;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class PopUpManager {

    public static void showToastError(Context context, CharSequence text) {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
