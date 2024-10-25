package com.salestrackmobileapp.android.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;


import com.salestrackmobileapp.android.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TakeOrderActivity extends BaseActivity {

    private DatePicker datePicker;
    private Calendar calendar;

    private int year, month, day;


    @BindView(R.id.date_picker_tv)
     TextView dateView;
    @BindView(R.id.title_order_tv)
    TextView titleOrderTv;
    @BindView(R.id.payment_tv)
    TextView paymentTV;
    @BindView(R.id.hint1_tv)
    TextView hint1TV;
    @BindView(R.id.card_number_tv)
    TextView carNumberTV;
    @BindView(R.id.hint2_tv)
    TextView hint2TV;
    @BindView(R.id.cvv_tv)
    TextView cvvTV;
    @BindView(R.id.cvv_hint_tv)
    TextView cvvHintTV;
    @BindView(R.id.exp_date_tv)
    TextView expDateTV;
    @BindView(R.id.address_1_tv)
    TextView address1TV;
    @BindView(R.id.address_2_tv)
    TextView address2Tv;
    @BindView(R.id.comment_tv)
    TextView commentTV;
    @BindView(R.id.city_tv)
    TextView cityTV;
    @BindView(R.id.state_tv)
    TextView stateTV;
    @BindView(R.id.zip_tv)
    TextView zipTV;
    @BindView(R.id.country_tv)
    TextView countryTV;
//

/*    @BindView(R.id.first_name_et)
    EditText firstNameET;
    @BindView(R.id.last_name_et)
    EditText lastNameET;
    @BindView(R.id.card_number_et)
    EditText cardNumberET;
    @BindView(R.id.cvv_et)
    EditText cvvET;
    @BindView(R.id.address_1_et)
    EditText address1ET;
    @BindView(R.id.address_2_et)
    EditText address2ET;
    @BindView(R.id.comment_et)
    EditText commentET;
    @BindView(R.id.city_et)
    EditText cityET;
    @BindView(R.id.state_et)
    EditText stateET;
    @BindView(R.id.zip_et)
    EditText zipET;
    @BindView(R.id.country_et)
    EditText countryET;*/

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);
        ButterKnife.bind(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "Avenir-Next-LT-Pro_5196.ttf");

        dateView.setTypeface(custom_font);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), CheckInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @OnClick(R.id.back_img)
    public void backHomeButtonClick() {
        Intent intent = new Intent(getApplicationContext(), CheckInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

}
