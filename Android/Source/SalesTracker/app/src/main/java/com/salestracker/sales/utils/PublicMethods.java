package com.salestracker.sales.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.salestracker.sales.application.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by Tarun Kumar
 * on 15/8/15.
 */
public class PublicMethods {

    public static void showToast(String message) {
        final Toast toast = Toast.makeText(AppController.getInstance(), message, Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 200, 0);
        toast.show();

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1200);
    }

    public static void handleError(VolleyError volleyError, Context context) {

        String errorMessage = "Something went wrong";
        if (volleyError instanceof NoConnectionError)
            errorMessage = "No internet";
        else if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            errorMessage = new String(volleyError.networkResponse.data);

            volleyError.printStackTrace();

            try {
                JSONObject jsonObject = new JSONObject(errorMessage);
                errorMessage = jsonObject.getString("User_Error");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        handleError(errorMessage, context);
    }

    public static void handleError(String errorMessage, Context context) {
        if (context != null) {
            /*Snackbar.make(mEmailView, errorMessage, Snackbar.LENGTH_SHORT)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {

                        }
                    });*/
        }
        /*else*/
            Toast.makeText(AppController.getInstance(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    public static int getSystemHeight() {
        WindowManager wm = (WindowManager) AppController.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point outPoint = new Point();
        display.getSize(outPoint);
        return outPoint.y;
    }

    public static int getSystemWidth() {
        WindowManager wm = (WindowManager) AppController.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point outPoint = new Point();
        display.getSize(outPoint);
        return outPoint.x;
    }

    private static final String [] months = new String []{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static String getDate(String month, String year) {
        String monthText = "01";
        for (int i = 0; i < months.length; i++) {
            if (month.equals(months[i])) {
                monthText = (i + 1) + "";
                if (monthText.length() == 1)
                    monthText = "0" + monthText;
                break;
            }
        }

        return year + "-" + monthText + "-01";
    }


    private static final String [] jobs = new String []{"Full Time", "Part Time", "Internship", "Social Work", "Freelance", "Others"};
    private static final String [] jobsShort = new String []{"F", "P", "I", "S", "FR", "O"};

    public static String getJobShort(String jobText) {
        for (int i = 0; i < jobs.length; i++) {
            if (jobText.equals(jobs[i])) {
                return jobsShort[i];
            }
        }
        return "O";
    }

    static public String formatCurrency(long currencyAmount) {

        try {

            Locale currentLocale = new Locale("en", "in");

            NumberFormat currencyFormatter =
                    NumberFormat.getCurrencyInstance(currentLocale);
            DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) currencyFormatter).getDecimalFormatSymbols();
            decimalFormatSymbols.setCurrencySymbol("");
            currencyFormatter.setRoundingMode(RoundingMode.FLOOR);
            currencyFormatter.setMinimumFractionDigits(0);
            currencyFormatter.setMaximumFractionDigits(0);

            ((DecimalFormat) currencyFormatter).setDecimalFormatSymbols(decimalFormatSymbols);

            return currencyFormatter.format(currencyAmount).trim();
        } catch (Exception ex) {
        }

        return currencyAmount + "";
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    public static void cancelAllNotification(Context ctx) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancelAll();
    }

    public static int dpToPixel(int dp, Resources resources) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }
}
