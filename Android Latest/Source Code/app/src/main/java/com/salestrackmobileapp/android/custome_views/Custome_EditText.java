package com.salestrackmobileapp.android.custome_views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by kanchan on 3/21/2017.
 */

public class Custome_EditText extends android.support.v7.widget.AppCompatEditText {
    public Custome_EditText(Context context) {
        super(context);
        applyCustomFont(context);
    }


    public Custome_EditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public Custome_EditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "Avenir-Next-LT-Pro_5196.ttf");
        setTypeface(customFont);
    }
}
