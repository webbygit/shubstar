package com.salestrackmobileapp.android.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by kanchan on 5/8/2017.
 */

public class BootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(context.getApplicationContext().getSharedPreferences("SalesTrack",0)!=null)
        {
          //  Toast.makeText(context,"prefernces not null",Toast.LENGTH_SHORT).show();

        }

        Log.d("Boot start"," switch on");
    }
}
