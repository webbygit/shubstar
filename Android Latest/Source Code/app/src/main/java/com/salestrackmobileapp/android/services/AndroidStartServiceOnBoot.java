package com.salestrackmobileapp.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by kanchan on 5/8/2017.
 */

public class AndroidStartServiceOnBoot extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
       // Toast.makeText(this, "Service Destroy", Toast.LENGTH_LONG).show();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
