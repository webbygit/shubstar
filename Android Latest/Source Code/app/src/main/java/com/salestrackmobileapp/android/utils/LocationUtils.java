package com.salestrackmobileapp.android.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by kanchan on 3/23/2017.
 */

public class LocationUtils {
    GoogleApiClient apiClient;
    private ProgressDialog progressDialog;

    private int LOCATION_REQUEST_INTERVEL = 200;// time in miiliseconds
    private int LOCATION_REQUEST_FASTEST_INTERVAL = 0;// 200 milliseconds

    public void getCurrentLocation(Context cntxt, final LocationUtils.LocationListener listener) {
        try {
            progressDialog = new ProgressDialog(cntxt);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (apiClient == null) {
            apiClient = new GoogleApiClient.Builder(cntxt)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                            LocationRequest locationRequest = new LocationRequest();
                            locationRequest.setInterval(LOCATION_REQUEST_INTERVEL);
                            locationRequest.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                            LocationServices.FusedLocationApi.requestLocationUpdates(
                                    apiClient, locationRequest, new com.google.android.gms.location.LocationListener() {
                                        @Override
                                        public void onLocationChanged(Location location) {
                                            listener.onLocationProvided(location);

                                            try {
                                                if (progressDialog != null) {
                                                    progressDialog.dismiss();
                                                }
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }


                                            apiClient.disconnect();
                                        }
                                    });

                        }

                        @Override
                        public void onConnectionSuspended(int i) {


                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }

                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            if (apiClient != null) {

                                apiClient.connect();
                            }

                        }
                    })
                    .build();
        }
        apiClient.connect();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public interface LocationListener {

        public void onLocationProvided(Location arg0);
    }

    public static boolean checkLocationEnable(Context context) {
        boolean bool = false;

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {


            bool = false;

        } else {
            bool = true;
        }

        return bool;
    }
}
