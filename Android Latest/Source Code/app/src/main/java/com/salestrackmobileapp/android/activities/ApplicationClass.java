package com.salestrackmobileapp.android.activities;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Button;

import com.orm.SugarApp;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.PrefsHelper;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by kanchan on 3/21/2017.
 */

public class ApplicationClass extends SugarApp {
    private static CountDownTimer countDownTimer;
    public static Button timerButton;
    public static long millisup;
    public PrefsHelper sharedPreference;
    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        sharedPreference = new PrefsHelper(this);
    }
    /*  private static ApplicationClass enableMultiDex;
        public static Context context;

        public ApplicationClass(){
            enableMultiDex=this;
        }

        public static ApplicationClass getEnableMultiDexApp() {
            return enableMultiDex;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            context = getApplicationContext();

        }*/
    public static void setTimer(long milli) {

        millisup = milli;

        if (countDownTimer == null) {
            Calendar cal = Calendar.getInstance();
//            TimeZone tz = TimeZone.getTimeZone("GMT");
//            cal.setTimeZone(tz);
            // int minute = cal.get(Calendar.MINUTE);
            int Totalminute=24*60;
            Log.e("TOTAl_MINUTE","::::"+Totalminute);
            int currentminute=cal.get(Calendar.MINUTE);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            Log.e("hour",":::::"+hour);

            Log.e("currentminute",":::::"+currentminute);
            int currentMinuteOfDay = ((hour * 60) + currentminute);
            Log.e("currentMinuteOfDay",":::::"+currentMinuteOfDay);


            int minute=Totalminute-currentMinuteOfDay;
            Log.e("minute",":::::"+minute);

            // String getMinutes = minute.getText().toString();//Get minutes from edittexf
            //Check validation over edittext
            if (minute > 0) {
                int noOfMinutes = minute * 60 * 1000;//Convert minutes into milliseconds

                startTimer(noOfMinutes);//start countdown
                //  startTimer.setText(getString(R.string.stop_timer));//Change Text
//                Log.e("TImER_text",":::::::"+)

            } else {
                //Else stop timer and change text
                stopCountdown();
                // startTimer.setText(getString(R.string.start_timer));
            }
        }

    }
    public   static void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    //Start Countodwn method
    private static void startTimer(final int noOfMinutes) {

        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                Log.d("seconds elapsed: " , ""+(noOfMinutes - millis));

//                millisup=(noOfMinutes - millis);
                millisup = millisup + 1000;

                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisup), TimeUnit.MILLISECONDS.toMinutes(millisup) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisup)), TimeUnit.MILLISECONDS.toSeconds(millisup) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisup)));
                // countdownTimerText.setText(hms);//set text
//                Log.e("(((","::START_TIMER:::"+hms);
                if(timerButton != null){
//                    Log.e("(((","timerButton:not null");

                    timerButton.setText("       "+hms);
                }
                CommonUtils.lastMilliofActivity=millisup;
                Log.e("lastMilliofActivity",":::::"+CommonUtils.lastMilliofActivity);


            }

            public void onFinish() {

                countDownTimer = null;//set CountDownTimer to null
                Log.e("ON_FINISH",":::::TIME'S UP!!!");

            }
        }.start();


    }


    public void onCreate() {
        super.onCreate();

        ApplicationClass.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ApplicationClass.context;
    }
}
