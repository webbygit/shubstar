package com.salestrackmobileapp.android.singleton;


/**
 * Created by kanchan on 3/23/2017.
 */

public class Singleton {

    public String COUPON_CODE = "";

    public static Double SLAT = 0.0d;
    public static Double SLNG = 0.0d;

    public Double DLAT2 = 0.0d;
    public Double DLNG2 = 0.0d;

    public static Singleton instance;

    public static Singleton getInstance() {

        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
