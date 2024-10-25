package com.salestrackmobileapp.android.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kanchan on 3/15/2017.
 */

public interface RecyclerClickInterface {
    void productClick(View v, int position, RecyclerView recyclerView);
}
