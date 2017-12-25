package com.example.trubin23.myfirstapplication;

import android.graphics.Color;
import android.support.annotation.NonNull;

/**
 * Created by trubin23 on 22.12.17.
 */

class Utils {
    static int colorText(@NonNull String stringColor) {
        int intColor = Color.parseColor(stringColor);

        int red = Color.red(intColor);
        int green = Color.green(intColor);
        int blue = Color.blue(intColor);
        int y = (int) (0.2126 * red + 0.7152 * green + 0.0722 * blue);
        if (y < 128) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }
}
