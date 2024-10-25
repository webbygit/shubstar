package com.salestrackmobileapp.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kanchan on 3/15/2017.
 */

public class PrefsHelper {

    private static SharedPreferences preferences = null;
    public static final String DB_KEY_APP_START_TIME = "DB_KEY_APP_START_TIME";


    private static String PREFS_NAME = "SalesTrack";
    public static String REGID = "FCM_ID";
    public static String ACCESS_TOKEN = "Access_Token";
    public static String SALESPERSONNAME = "salesperson_name";
    public static String HITTING_TIMING = "hitting_time";
    public static String ISSUED_ON = "issued_on";
    public static String EXPIRE_ON = "expire_on";
    public static String USER_EMAIL = "user_email";
    public static String USER_ID = "user_id";
    public static String USERNAME = "user_name";

    public static String GOAL_LOCATION = "goal_location";
    public static String BUSINESS_LOCATION = "business_location";
    public static String STATE = "state";
    public static String STATE_PRODUCT="state";
    public static String CITY = "city";
    public static String BUSINESS_ID = "business_id";
    public static String CATEGORYID = "cat_id";
    public static String BRAND_NAME = "business_name";
    public static String PRODUCT_LOC = "product_loc";
    public static String PRODUCT_ID_FOR_DETAIL = "product_id";//
    public static String START_DAY = "start_day";
    public static String DEAL = "deal";
    public static String NAVI_PRODUCT_BUSINESS = "navigate_product_business";
    public static String BUSINESS_CHECKED= "business_checked";

    public static String TOTAL_AMOUNT = "total_amount";
    public static String TOTAL_DISCOUNT = "total_discount";
    public static String RAW_AMOUNT = "raw_amount";


    public PrefsHelper(Context context) {
        initSharedPrefs(context);
    }

  /*  public static String getStringForService(String value) {
        String returnValue = "";
        if (value.equals("timing")) {
            if (!HITTING_TIMING.equals("0")) {

                returnValue = HITTING_TIMING;
            } else {
                returnValue = "0";
            }

        } else if (value.equals("access_token")) {
            if (!ACCESS_TOKEN.equals("")) {

                returnValue = ACCESS_TOKEN;
            } else {
                returnValue = "0";
            }
        }
        return returnValue;
    }*/

    private void initSharedPrefs(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public static String getSharedPreferences() {
        return PREFS_NAME;
    }

    public void saveStringData(String keyName, String val) {
        preferences.edit().putString(keyName, val).commit();
    }

    public String getStringValue(String key) {
        return preferences.getString(key, "");
    }

    public void saveIntData(String keyName, int val) {
        preferences.edit().putInt(keyName, val).commit();
    }

    public int getIntValue(String key) {
        return preferences.getInt(key, 0);
    }


    public void saveBooleanValue(String keyName, boolean val) {
        preferences.edit().putBoolean(keyName, val).commit();
    }



    public void saveBooleanData(String keyName, boolean val) {
        preferences.edit().putBoolean(keyName, val).commit();
    }

    public boolean getBooleanValue(String key) {
        return preferences.getBoolean(key, false);
    }


    public void saveLongData(String keyName, long val) {
        preferences.edit().putLong(keyName, val).commit();
    }

    public long getLongData(String key) {
        return preferences.getLong(key, 0);
    }


    public static boolean clearPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, 0);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            return editor.commit();
        }
        return false;
    }
}
