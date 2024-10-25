package com.salestrackmobileapp.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;


import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.GoalsAccDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckInActivity extends BaseActivity {

    @BindView(R.id.home_icon_img)
    ImageView homeIconImg;
    //checked_in_userdetail
    @BindView(R.id.checked_in_userdetail)
    Custome_TextView detailUser;
    @BindView(R.id.take_order_btn)
    Button takeOrderBtn;
    int goalPosition;
    List<GoalsAccDate> listAllGoals;
    GoalsAccDate goalsAccDate;
    @BindView(R.id.action_bar_title)
    Custome_TextView actionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        ButterKnife.bind(this);
        actionBarTitle.setText("CHECKED IN ");
        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
        goalPosition = getIntent().getIntExtra("GoalPosition", 0);

        listAllGoals = GoalsAccDate.listAll(GoalsAccDate.class);
        if (listAllGoals.size() != 0) {
            goalsAccDate = listAllGoals.get(goalPosition);
            detailUser.setText("You checked in " + goalsAccDate.getGoalTitle());
        } else {
            Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
            intent.putExtra("nameActivity", "MyGoals");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }

    }

    @OnClick(R.id.take_order_btn)
    public void takeOrderCall() {
        Intent intent = new Intent(getApplicationContext(), TakeOrderActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @OnClick(R.id.view_past_order)
    public void viewPastOrder() {
        Intent intent = new Intent(getApplicationContext(), TakeOrderActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @OnClick(R.id.home_icon_img)
    public void backHomeButtonClick() {
        Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
        intent.putExtra("nameActivity", "MyGoals");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
        intent.putExtra("nameActivity", "MyGoals");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        finish();
    }
}
