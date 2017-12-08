package com.example.trubin23.myfirstapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.Log;

import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by trubin23 on 05.12.17.
 */

class ChangeTheme {

    private final static String TYPE_THEME = "type_theme";
    private static final String LOG = "ChangeTheme";

    enum Theme {
        RED(R.style.AppThemeRed),
        GREEN(R.style.AppThemeGreen),
        BLUE(R.style.AppThemeBlue);

        @StyleRes
        int themeId;

        Theme(@StyleRes int appThemeId) {
            themeId = appThemeId;
        }

        @NonNull
        public static String[] getNames(){
            return Arrays.toString(Theme.values())
                    .replaceAll("^.|.$", "").split(", ");
        }
    }

    static void changeToTheme(@NonNull Activity activity, @NonNull String themeName)
    {
        saveTheme(activity, themeName);
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    static void onActivityCreateSetTheme(@NonNull Activity activity)
    {
        String themeName = loadTheme(activity);

        Theme theme = Theme.RED;
        try {
            theme = Theme.valueOf(themeName);
        } catch (IllegalArgumentException e){
            Log.e(LOG, "static void onActivityCreateSetTheme(@NonNull Activity activity)", e);
        }

        activity.setTheme(theme.themeId);
    }

    private static void saveTheme(@NonNull Activity activity, @NonNull String themeName) {
        SharedPreferences sPref = activity.
                getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(TYPE_THEME, themeName);
        editor.apply();
    }

    @NonNull
    static String loadTheme(@NonNull Activity activity) {
        SharedPreferences sPref = activity.
                getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);
        //actually preference type ?
        return sPref.getString(TYPE_THEME, Theme.RED.toString());
    }
}
