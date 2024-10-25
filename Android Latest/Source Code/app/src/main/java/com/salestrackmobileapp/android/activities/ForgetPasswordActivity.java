package com.salestrackmobileapp.android.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_EditText;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.action_bar_title)
    Custome_TextView actionBarTitle;

    @BindView(R.id.sign_in_tv)
    Custome_TextView signInTv;

    @BindView(R.id.email_tv)
    Custome_TextView emailTV;
    @BindView(R.id.textView)
    Custome_TextView textView;

    @BindView(R.id.retriev_pwd_btn)
    Button retrievPwdBtn;
    SharedPreferences preferences;
    public static final String mypreference = "mypref";
    Custome_EditText firstname_et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        firstname_et=(Custome_EditText) findViewById(R.id.firstname_et);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "Avenir-Next-LT-Pro_5196.ttf");
        signInTv.setTypeface(custom_font);
        emailTV.setTypeface(custom_font);
        textView.setTypeface(custom_font);
        actionBarTitle.setTypeface(custom_font);
        retrievPwdBtn.setTypeface(custom_font);
        preferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

    }

    @OnClick(R.id.sign_in_tv)
    public void signInActivityCall() {
        hideDialog();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
boolean isAsyncTask=false;
    @OnClick(R.id.retriev_pwd_btn)
    public void dashBoardCall() {


        if (Connectivity.isNetworkAvailable(ForgetPasswordActivity.this)) {
            if (firstname_et.getText().toString()==null || firstname_et.getText().toString().equals(" ") ||firstname_et.getText().toString().length()==0) {
                Toast.makeText(ForgetPasswordActivity.this,"Please enter email",Toast.LENGTH_SHORT).show();

            }
            else{
                if (!preferences.getString("lastToken","").equals("")) {
                    forgetPasswordApiCall();
                }
                else{
                    Toast.makeText(ForgetPasswordActivity.this, "Sorry,session time out.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(ForgetPasswordActivity.this,"you need Internet connection for this",Toast.LENGTH_SHORT).show();
        }

    }

ProgressDialog mProgressAsynctAsk;
    public void forgetPasswordApiCall() {

        mProgressAsynctAsk = new ProgressDialog(ForgetPasswordActivity.this);
        mProgressAsynctAsk.setMessage("Loading data...");
        mProgressAsynctAsk.setCanceledOnTouchOutside(false);
        mProgressAsynctAsk.show();
        Log.e("TOKEN",":::"+preferences.getString("lastToken",""));
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(ForgetPasswordActivity.this, ServiceHandler.class,preferences.getString("lastToken",""), NetworkManager.BASE_URL);
        serviceHandler.forgetPassword(firstname_et.getText().toString(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String serverResponse = CommonUtils.getServerResponse(response);
                Log.e("serverResponse",":::"+serverResponse);
                try {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    if (jsonObject.has("Message"))

                    {
                        String Message=jsonObject.getString("Message");
                        Toast.makeText(ForgetPasswordActivity.this,Message,Toast.LENGTH_SHORT).show();
                    }
                    hideDialog();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    hideDialog();
                    Toast.makeText(getApplicationContext(), "problem in getting forget password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                hideDialog();

            }
        });
    }
      public void showDialog() {


        if (mProgressAsynctAsk != null && !mProgressAsynctAsk.isShowing()) {
            mProgressAsynctAsk.show();



        }
    }

    public void hideDialog() {


        if (mProgressAsynctAsk != null && mProgressAsynctAsk.isShowing()) {
            mProgressAsynctAsk.dismiss();


        }
    }

    @Override
    public void onBackPressed() {
        hideDialog();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
