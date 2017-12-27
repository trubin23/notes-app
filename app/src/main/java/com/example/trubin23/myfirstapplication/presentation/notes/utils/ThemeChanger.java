package com.example.trubin23.myfirstapplication.presentation.notes.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.trubin23.myfirstapplication.R;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by trubin23 on 05.12.17.
 */

public class ThemeChanger {

    private enum Theme {
        RED(R.style.AppThemeRed),
        GREEN(R.style.AppThemeGreen),
        BLUE(R.style.AppThemeBlue);

        @StyleRes
        int mThemeId;

        Theme(@StyleRes int themeId) {
            mThemeId = themeId;
        }

        @NonNull
        public static String[] getNames() {
            return Arrays.toString(Theme.values())
                    .replaceAll("^.|.$", "").split(", ");
        }
    }

    private final static String TYPE_THEME = "type_theme";
    private static final String LOG = "ThemeChanger";

    private AlertDialog mAlertDialog;

    public ThemeChanger(@NonNull final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.pick_theme);

        final String[] themeNames = ThemeChanger.Theme.getNames();
        final List<String> themeNameList = Arrays.asList(themeNames);

        String themeName = loadTheme(activity);

        builder.setSingleChoiceItems(themeNames, themeNameList.indexOf(themeName),
                (dialogInterface, which) -> {
                    changeToTheme(activity, themeNameList.get(which));
                    dismiss();
                });

        mAlertDialog = builder.create();
    }

    public void showDialog() {
        mAlertDialog.show();
    }

    private void dismiss() {
        mAlertDialog.dismiss();
        mAlertDialog = null;//Why need null ?
    }

    private void changeToTheme(@NonNull Activity activity, @NonNull String themeName) {
        saveTheme(activity, themeName);
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(@NonNull Activity activity) {
        String themeName = loadTheme(activity);

        Theme theme = Theme.RED;
        try {
            theme = Theme.valueOf(themeName);
        } catch (IllegalArgumentException e) {
            Log.e(LOG, "static void onActivityCreateSetTheme(@NonNull Activity activity)", e);
        }

        activity.setTheme(theme.mThemeId);
    }

    private void saveTheme(@NonNull Activity activity, @NonNull String themeName) {
        SharedPreferences sPref = activity.
                getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(TYPE_THEME, themeName);
        editor.apply();
    }

    @NonNull
    private static String loadTheme(@NonNull Activity activity) {
        SharedPreferences sPref = activity.
                getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);

        return sPref.getString(TYPE_THEME, Theme.RED.toString());
    }
}
