package com.salestrackmobileapp.android.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.salestrackmobileapp.android.activities.BaseActivity;
import com.salestrackmobileapp.android.activities.DashboardActivity;
import com.salestrackmobileapp.android.activities.SplashActivity;
import com.salestrackmobileapp.android.gson.LatLng;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.singleton.Singleton;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by kanchan on 4/4/2017.
 */

public class LocationService extends Service implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final int TWO_MINUTES = 100 * 10;

    // The minimum time between updates in milliseconds
    private static long MIN_TIME_BW_UPDATES = 1000 * 10; // 30 seconds
    String username;
    private Context context;

    static boolean flagSourceDes = false;
    double latitude;
    double longitude;

    Location location = null;
    boolean isGPSEnabled = false;
    public static boolean isNetworkEnabled = false;
    protected LocationManager locationManager;
    public static String ACCESS_TOKEN = "Access_Token";
    public static String EXPIRTIME = "expire_time";
    public static Boolean STARTDAY = false;
    //start_day
    private int year;
    private int month;
    private int day;

    float distanceInMeters;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.context = this;
        try {

            if (intent!=null) {
                if (intent.hasExtra("TIME_IN_MINUTESC")) {
                    MIN_TIME_BW_UPDATES = intent.getLongExtra("TIME_IN_MINUTES", 1000);
                }
                if (intent.hasExtra("EXPIRTIME")) {
                    EXPIRTIME = intent.getStringExtra("EXPIRTIME");
                }
                if (intent.hasExtra("ACCESS_TOKEN")) {
                    ACCESS_TOKEN = intent.getStringExtra("ACCESS_TOKEN");
                }
                if (intent.hasExtra("USERNAME")) {
                    username = intent.getStringExtra("USERNAME");
                }
                if (intent.hasExtra("start_day")) {
                    STARTDAY = intent.getBooleanExtra("start_day", false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // getTimerForCallService();
        get_current_location();
//      Toast.makeText(context, "Lat"+latitude+"long"+longitude,Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }


    @Override
    public void onLocationChanged(Location location) {
        if ((location != null) && (location.getLatitude() != 0) && (location.getLongitude() != 0)) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            if (!flagSourceDes) {
                Singleton.getInstance().SLAT = location.getLatitude();
                Singleton.getInstance().SLNG = location.getLongitude();
                Singleton.getInstance().DLAT2 = location.getLatitude();
                Singleton.getInstance().DLNG2 = location.getLongitude();
                /*getTimerForCallService();*/
                flagSourceDes = true;
            } else {
                if ((Singleton.getInstance().SLAT == Singleton.getInstance().DLAT2) && (Singleton.getInstance().SLNG == Singleton.getInstance().DLNG2)) {
                    Singleton.getInstance().DLAT2 = location.getLatitude();
                    Singleton.getInstance().DLNG2 = location.getLongitude();
                    getDistance();
                } else {
                    getDistance();
                }
            }


            Log.d("location value", "" + latitude + "" + longitude);
          //  Toast.makeText(getApplicationContext(), latitude + "" + longitude, Toast.LENGTH_LONG).show();
            if (!STARTDAY) {
              //  Toast.makeText(getApplicationContext(), "Please start your day attendence", Toast.LENGTH_SHORT).show();
            }

            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.MONTH, month);
            String format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z").format(cal.getTime());
            // EXPIRTIME="Fri, 19 May 2017 15:50:20 GMT";
            /*"Fri, 02 Jun 2017 09:10:31 GMT"*/
            if (EXPIRTIME.equals("")) {

            } else if (EXPIRTIME.equals(format)) {
                Toast.makeText(getApplicationContext(), "You need to Login Again", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Log.d("timing matched", "time match");
            }
            getDistance();
            if (Math.round(distanceInMeters) > 50) {
                getTimerForCallService();
            }
        }
    }


    public void getDistance() {
        Location loc1 = new Location("");
        if (Singleton.SLAT != 0) {
            loc1.setLatitude(Singleton.getInstance().SLAT);
            loc1.setLongitude(Singleton.getInstance().SLNG);

            Location loc2 = new Location("");
            loc2.setLatitude(Singleton.getInstance().DLAT2);
            loc2.setLongitude(Singleton.getInstance().DLNG2);

            distanceInMeters = loc1.distanceTo(loc2);
        } else {

        }


      /*https://stackoverflow.com/questions/2741403/get-the-distance-between-two-geo-points*/

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /*
    *  Get Current Location
    */
    public Location get_current_location() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
           // Toast.makeText(getApplicationContext(), "GPS and internet connectivity problem", Toast.LENGTH_LONG).show();
        } else {
            if (isGPSEnabled) {

                if (location == null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Singleton.getInstance().SLAT = location.getLatitude();
                            Singleton.getInstance().SLNG = location.getLongitude();
                            Log.d("location value 1", "" + latitude + "" + longitude);
                        }
                    }
                }
            } else {

            }
            if (isNetworkEnabled) {

                if(distanceInMeters>50) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                }
                if (locationManager != null) {

                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Singleton.getInstance().SLAT = location.getLatitude();
                        Singleton.getInstance().SLNG = location.getLongitude();
                        Log.d("location value 2", "" + latitude + "" + longitude);
                        // Toast.makeText(context, "Latgps1" + latitude + "long" + longitude, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        return location;
    }


    public void getTimerForCallService() {
        // CommonUtils.showProgress(getApplicationContext());
        Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            String date = df.format(Calendar.getInstance().getTime());//2017-05-02T20:30:26.933Z

            final JSONObject jsonObj = new JSONObject();
            jsonObj.put("Latitude", String.valueOf(Singleton.getInstance().SLAT));
            jsonObj.put("Longitude", String.valueOf(Singleton.getInstance().SLNG));
            jsonObj.put("TrackDate", date + "Z");
            jsonObj.put("SalesPersonName", username);

            LatLng latLongObject = builder.fromJson(jsonObj.toString(), LatLng.class);
            Log.e("Location_params",":::::"+jsonObj.toString());
            //Longitude

            ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, ACCESS_TOKEN, NetworkManager.BASE_URL);
            serviceHandler.trackLatLong(latLongObject, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    String serverResponse = CommonUtils.getServerResponse(response);

                    try {
                        JSONObject jsonObject = new JSONObject(serverResponse);
                        MIN_TIME_BW_UPDATES = Long.parseLong(jsonObject.getString("Message"));
                        distanceInMeters=0;
                        // get_current_location();
                        //final long period = 0;

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
            CommonUtils.dismissProgress();
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.dismissProgress();
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        super.onDestroy();
    }


    public void dayStartApiCall() {
        Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, ((BaseActivity) context).sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.startAttendence(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String serverResponse = CommonUtils.getServerResponse(response);
                try {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    String message = jsonObject.getString("Message");
                    if (message.equals("Success")) {
                        ((BaseActivity) context).sharedPreference.saveBooleanData(PrefsHelper.START_DAY, true);
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
//startAttendence
    }
}