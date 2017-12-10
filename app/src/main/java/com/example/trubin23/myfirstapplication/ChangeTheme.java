package com.example.trubin23.myfirstapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by trubin23 on 05.12.17.
 */

class ChangeTheme {

    private final static String TYPE_THEME = "type_theme";
    private static final String LOG = "ChangeTheme";

    private AlertDialog mAlertDialog;

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
        public static String[] getNames() {
            return Arrays.toString(Theme.values())
                    .replaceAll("^.|.$", "").split(", ");
        }
    }

    ChangeTheme(final Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.pick_theme);

        final String[] themeNames = ChangeTheme.Theme.getNames();
        final List<String> itemsNames = Arrays.asList(themeNames);
        String themeCurrent = loadTheme(activity);

        builder.setSingleChoiceItems(themeNames, itemsNames.indexOf(themeCurrent),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        changeToTheme(activity, itemsNames.get(which));
                        dismiss();
                    }
                });

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    private void dismiss() {
        mAlertDialog.dismiss();
    }

    private void changeToTheme(@NonNull Activity activity, @NonNull String themeName) {
        saveTheme(activity, themeName);
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    static void onActivityCreateSetTheme(@NonNull Activity activity) {
        String themeName = loadTheme(activity);

        Theme theme = Theme.RED;
        try {
            theme = Theme.valueOf(themeName);
        } catch (IllegalArgumentException e) {
            Log.e(LOG, "static void onActivityCreateSetTheme(@NonNull Activity activity)", e);
        }

        activity.setTheme(theme.themeId);
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
        //actually preference type ?
        return sPref.getString(TYPE_THEME, Theme.RED.toString());
    }
}
