package com.project.android.musisearch.utils;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Sessions {
    public static final String REALM_DB_NAME = "db_realm_musisi.realm";
    public static final String PREFS_LOGIN = "SESSIONLOGIN";
    public static final String PREFS_LOGIN_GOOGLE = "SESSIONLOGINGOOGLE";
    public static final String PREFS_LOGIN_STEP2 = "SESSIONLOGINSTEP2";

    public Sessions() {
        super();
    }

    public static void setupSessionLoginGoogle(Activity activity, int id, String fullName, String email, String phoneNumber, String dateBirth, String imagePath) {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREFS_LOGIN_GOOGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("id", id);
        editor.putString("fullname", fullName);
        editor.putString("email", email);
        editor.putString("imagePath", imagePath);
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("dateBirth", dateBirth);
        editor.apply();
    }

    public static void setupSessionLoginGoogle(Activity activity, String imagePath) {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREFS_LOGIN_GOOGLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("imagePath", imagePath);
        editor.apply();
    }
}
