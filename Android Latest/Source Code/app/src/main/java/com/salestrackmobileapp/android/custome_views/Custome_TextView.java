package com.salestrackmobileapp.android.custome_views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by kanchan on 3/21/2017.
 */

public class Custome_TextView extends android.support.v7.widget.AppCompatTextView {
    public Custome_TextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public Custome_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }
    public Custome_TextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = /*FontCache.getTypeface("assets/Avenir-Next-LT-Pro_5196.ttf", context);*/Typeface.createFromAsset(context.getAssets(), "Avenir-Next-LT-Pro_5196.ttf");
        setTypeface(customFont);
    }


}

