package com.salestrackmobileapp.android.networkManager;

import android.content.Context;
import android.util.Log;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by kanchan on 3/23/2017.
 */

public class NetworkManager {

    public static final String BASE_URL = "https://salestrackapi.azurewebsites.net/api";
    public static final int DRAW = 10;
    Context activity;

    public static <T> T createRetrofitService(Context activity, final Class<T> clazz, final String accessToken, final String endPoint) {
      
        T service = null;
        Log.e("TOKEN",":::::"+accessToken);
        Log.e("endPoint",":::::"+endPoint);
        if (!accessToken.equals("")) {
                RequestInterceptor requestInterceptor = new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Authorization", "bearer " + accessToken);
                        request.addHeader("content", "application/json");
                    }
                };
                final RestAdapter restAdapter = new RestAdapter.Builder()
                        .setRequestInterceptor(requestInterceptor)
                        .setEndpoint(endPoint)
                        .build();
                service = restAdapter.create(clazz);
            } else {
                final RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(endPoint)
                        .build();
                service = restAdapter.create(clazz);
            }

        return service;
    }
}
