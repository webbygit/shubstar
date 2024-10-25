package com.salestrackmobileapp.android.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.gson.UserInfoProfile;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.ivBg)
    ImageView backImage;

    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        CommonUtils.showProgress(this);

        if (Connectivity.isConnected(getApplicationContext())) {
         //   Toast.makeText(getApplicationContext(), "start the app ", Toast.LENGTH_SHORT).show();
            getLocationPermission();
        } else {
            Toast.makeText(getApplicationContext(), "You are not Connected with Internet. ", Toast.LENGTH_SHORT).show();
            if(Select.from(UserInfoProfile.class).first()!=null){
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();
            }
          //  finish();
        }
        CommonUtils.dismissProgress();

    }



    //get location permission
    private void getLocationPermission() {
        if ((int) Build.VERSION.SDK_INT < 23) {
            // getCity();
            startMainActivity();
        } else {
            askForLocationService();
        }
        CommonUtils.dismissProgress();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void askForLocationService() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startMainActivity();
        } else {
            requestPermissions(PERMISSIONS_LOCATION, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        CommonUtils.dismissProgress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocationPermission();
    }

    private int year;
    private int month;
    private int day;

    private void startMainActivity() {

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        //Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        String format = new SimpleDateFormat("E, d MMM yyyy hh:mm:ss").format(cal.getTime());
        Log.e("token_splash","::::"+sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN));
     //   Toast.makeText(getApplicationContext(), format + " GMT", Toast.LENGTH_LONG).show();

        if (!sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN).equals("")) {
            if (sharedPreference.getStringValue(PrefsHelper.EXPIRE_ON).equals(format + " GMT")) {
                PrefsHelper.clearPreference(SplashActivity.this);
                SugarContext.terminate();
                SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
                schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
                SugarContext.init(getApplicationContext());
                schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());
                startActivity(new Intent(SplashActivity.this, SplashActivity.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                CommonUtils.dismissProgress();
                finish();

            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CommonUtils.dismissProgress();
                        Intent intent=new Intent(SplashActivity.this, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        finish();
                    }
                }, 2000);
            }
        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    CommonUtils.dismissProgress();
                    Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();
                }
            }, 2000);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
