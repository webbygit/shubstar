package com.salestrackmobileapp.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.salestrackmobileapp.android.activities.BaseActivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;

/**
 * Created by kanchan on 3/23/2017.
 */

public class BaseFragment extends Fragment {
    public BaseActivity baseActivity;
    public PrefsHelper sharedPreference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (BaseActivity) getActivity();
        sharedPreference = new PrefsHelper(baseActivity);
    }

}
