package com.salestracker.sales.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Tarun on 19/5/16.
 */
public class PreferenceUtils {

    private static PreferenceUtils instance;

    private SharedPreferences mUserPrefs;
    private SharedPreferences mAppPrefs;

    final public static String IS_USER_LOGGED_IN = "isUserLoggedIn";

    private PreferenceUtils() {}

    public static PreferenceUtils getInstance(Context context) {
        if (instance == null)
            instance = new PreferenceUtils();

        if (instance.mUserPrefs == null)
            instance.mUserPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (instance.mAppPrefs == null)
            instance.mAppPrefs = context.getSharedPreferences("AppPref", Context.MODE_PRIVATE);

        return instance;
    }

    public boolean isUserLoggedIn() {
        return mUserPrefs.getBoolean(IS_USER_LOGGED_IN, false);
    }

    public void setIsUserLoggedIn(boolean isUserLoggedIn) {
        SharedPreferences.Editor editor = mUserPrefs.edit();
        editor.putBoolean(IS_USER_LOGGED_IN, isUserLoggedIn);
        editor.apply();
    }

    public void clearUserData() {
        mUserPrefs.edit().clear().apply();
    }
}
