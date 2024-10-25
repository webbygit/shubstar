package com.salestrackmobileapp.android.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;


import com.salestrackmobileapp.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubMyGoalActivity extends BaseActivity {

    @BindView(R.id.home_icon_img)
    ImageView homeIconImg;

    @BindView(R.id.previous_order_rv)
    RecyclerView priviousOrderRV;

    @BindView(R.id.check_in_btn)
    Button checkInBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_my_goal);
        ButterKnife.bind(this);
        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "Avenir-Next-LT-Pro_5196.ttf");

        checkInBtn.setTypeface(custom_font);

       // getSupportFragmentManager().beginTransaction().replace(R.id.container, new CheckInPriOrderFragment()).commit();

    }

    @OnClick(R.id.check_in_btn)
    public void checkInCall() {
        Intent intent = new Intent(getApplicationContext(), CheckInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @OnClick(R.id.home_icon_img)
    public void backHomeButtonClick() {
        Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
        intent.putExtra("nameActivity", "MyGoals");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
