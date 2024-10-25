package com.salestrackmobileapp.android.custome_views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by kanchan on 3/22/2017.
 */

//avenir-next-lt-pro-demi
public class Custome_BoldTextView extends android.support.v7.widget.AppCompatTextView {
    public Custome_BoldTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public Custome_BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }
    public Custome_BoldTextView(Context context,AttributeSet attrs,int defStyle)
    {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = /*FontCache.getTypeface("assets/Avenir-Next-LT-Pro_5196.ttf", context);*/Typeface.createFromAsset(context.getAssets(), "avenir-next-lt-pro-demi.otf");
        setTypeface(customFont);
    }
}
