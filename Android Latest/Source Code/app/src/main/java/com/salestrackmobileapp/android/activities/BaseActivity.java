package com.salestrackmobileapp.android.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;

import com.salestrackmobileapp.android.utils.PrefsHelper;

/**
 * Created by kanchan on 3/15/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public LayoutInflater inflater;
    public PrefsHelper sharedPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sharedPreference = new PrefsHelper(this);

    }

}
