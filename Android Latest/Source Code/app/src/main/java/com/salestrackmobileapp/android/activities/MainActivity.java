package com.salestrackmobileapp.android.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.gson.UserLogin;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.singleton.Singleton;
import com.salestrackmobileapp.android.utils.AlertDialogUtils;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Config;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.LocationUtils;
import com.salestrackmobileapp.android.utils.NotificationUtils;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;


public class MainActivity extends BaseActivity implements OnClickListener, TextWatcher {


    public static int LOCATION_UPDATE_REQUEST = 11;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 66;

    @BindView(R.id.action_bar_title)
    TextView actionBarTitle;
    @BindView(R.id.firstname_tv)
    TextView userTitleNameTv;
    @BindView(R.id.password_tv)
    TextView passwordTv;
    @BindView(R.id.term_ser_tv)
    TextView termServiceTv;
    @BindView(R.id.and_tv)
    TextView andTv;
    @BindView(R.id.privacy_tv)
    TextView privacyTv;//forget_tv
    @BindView(R.id.forget_tv)
    TextView forgetTv;

    @BindView(R.id.firstname_et)
    EditText userNameEt;
    /* @BindView(R.id.password_et)*/
    EditText passwordEt;

    @BindView(R.id.img_eye_show_password)
    ImageView eyeShowPassword;
    @BindView(R.id.sign_btn)
    Button signBtn;

    String userNameSt, passwordSt, deviceidSt;
    UserLogin userLoginObject;
    static String userRole = "";
    private ProgressDialog mProgressDialog;
    TextView tv_invalid;


    Gson builder;
    ServiceHandler serviceHandler;
    boolean isLoginFirstTime=false;




    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String regId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tv_invalid=(TextView) findViewById(R.id.tv_invalid);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "Avenir-Next-LT-Pro_5196.ttf");

        actionBarTitle.setTypeface(custom_font);
        userTitleNameTv.setTypeface(custom_font);
        passwordTv.setTypeface(custom_font);
        termServiceTv.setTypeface(custom_font);
        andTv.setTypeface(custom_font);
        privacyTv.setTypeface(custom_font);
        userNameEt.setTypeface(custom_font);
        tv_invalid.setTypeface(custom_font);

        forgetTv.setTypeface(custom_font);
        signBtn.setTypeface(custom_font);
        passwordEt = (EditText) findViewById(R.id.password_et);
        passwordEt.setTypeface(custom_font);
        passwordEt.addTextChangedListener(this);
        tv_invalid.setVisibility(View.GONE);

        createNetworkObject();
        broadCasteReciverForReg();

        displayFirebaseRegId();

        eyeShowPassword = (ImageView) findViewById(R.id.img_eye_show_password);
        eyeShowPassword.setOnClickListener(this);
        eyeShowPassword.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        passwordEt.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });
        fetchLocationAndResult();
        deviceidSt = CommonUtils.getDeviceID(getApplicationContext());

    }



    public void broadCasteReciverForReg()
    {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

    }
    @Override
    protected void onResume() {
        super.onResume();
        fetchLocationAndResult();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
         regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);
        if (!TextUtils.isEmpty(regId)) {
            sharedPreference.saveStringData(PrefsHelper.REGID,regId);
         //   Toast.makeText(getApplicationContext(), "Firebase Reg Id: " + regId, Toast.LENGTH_SHORT).show();
            Log.d("Firebase Reg Id: " , regId+"");
        }
        else{
        Log.d("Firebase Reg Id is not","received yet");
        }
    }

    @OnClick(R.id.forget_tv)
    public void forgetPasswordCall() {
        Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @OnClick(R.id.sign_btn)
    public void signIn() {
        tv_invalid.setVisibility(View.GONE);

        try {
            userNameSt = userNameEt.getText().toString();
            passwordSt = passwordEt.getText().toString();
            if (isvalidation(userNameSt,passwordSt)) {
                Log.e("DEVICE_ID",":::::"+deviceidSt);
                loginCall(userNameSt, passwordSt, deviceidSt, regId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    boolean isvalidation(String userName,String password){

            if (userName.length()<=0 || userName==null || userName.equals("")){
                toast("Please Enter UserName");
                return false;}
            else if(password.length()<=0 || password==null || password.equals("")){
                toast("Please Enter Password.");
                return false;}

            return  true;

        }
    void toast(String toastMsg){
        Toast.makeText(MainActivity.this,toastMsg,Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.term_ser_tv)
    public void termAndConditionFun() {
        Intent intent = new Intent(getApplicationContext(), TermAndConditionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void fetchLocationAndResult() {

        if (!LocationUtils.checkLocationEnable(this)) {
            AlertDialogUtils.getInstance().singleButtonDialog(this, getString(R.string.discount_voucher_enable_location_enter_manually), "enable location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, LOCATION_UPDATE_REQUEST);
                }
            });
        } else {
            LocationUtils locationUtils = new LocationUtils();
            locationUtils.getCurrentLocation(this, new LocationUtils.LocationListener() {
                @Override
                public void onLocationProvided(Location arg0) {
                    onLocationUpdate(arg0);
                }
            });
        }
    }


    public void onLocationUpdate(Location location) {
        if (location == null) {
         //   Toast.makeText(MainActivity.this, R.string.landing_unable_fetch_location, Toast.LENGTH_SHORT).show();
        } else if (!Connectivity.isConnected(this)) {
          //  Toast.makeText(MainActivity.this, R.string.landing_unable_fetch_location, Toast.LENGTH_SHORT).show();
        } else {
            Singleton.getInstance().SLAT = location.getLatitude();
            Singleton.getInstance().SLNG = location.getLongitude();
        //    Toast.makeText(MainActivity.this, "" + Singleton.getInstance().SLAT + "" + Singleton.getInstance().SLNG, Toast.LENGTH_SHORT).show();
        }
    }

    public void createNetworkObject() {
        builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
    }

    private void loginCall(final String username, final String password, final String deviceId,final String regId) {
        final String userEmailID = username;
        Log.d("username::" + username + "password::" + password + "" + Singleton.instance.SLAT + "" + Singleton.instance.SLAT, "  :::::Login credentials ");
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        broadCasteReciverForReg();
        displayFirebaseRegId();

        try {
            JSONObject object = new JSONObject();
            object.put("Username", /*"kunal@sonyindia.com"*/username);
            object.put("Password", /*"Pass@123"*/password);
          /*  if(regId.equals(null) || regId.isEmpty())
            {
                object.put("DeviceId", deviceId*//*regId*//*);
            }else
            {
                object.put("DeviceId", regId*//*regId*//*);
            }*/
            object.put("DeviceId", deviceId/*regId*/);
            object.put("FCMRegID", regId/*regId*/);
            object.put("DeviceType", "Android");
            object.put("Longitude", String.valueOf(Singleton.getInstance().SLNG));
            object.put("Latitude", String.valueOf(Singleton.getInstance().SLAT));
            userLoginObject = builder.fromJson(object.toString(), UserLogin.class);
            serviceHandler.login(userLoginObject, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {

                        sharedPreference.saveLongData("LastLogin", System.currentTimeMillis()/1000);
                    // get header value
                    List<Header> headerList = response.getHeaders();
                    for (Header header : headerList) {
                        Log.d("header", header.getName() + " " + header.getValue());
                        if (header.getName() == null) {

                        } else {
                            if (header.getName().equals("UserRole")) {
                                userRole = header.getValue();
                            }
                        }
                    }
                    final String serverResponse = CommonUtils.getServerResponse(response);
                    try {
                        final JSONObject jsonObject = new JSONObject(serverResponse);
                        Log.e("REsponse::::","::"+jsonObject.toString());
//                        if (jsonObject.has("error_description")){
//
//                            Toast.makeText(MainActivity.this,""+jsonObject.getString("error_description"),Toast.LENGTH_SHORT).show();
//                        }
                        if (jsonObject.has("Message")){
                            if (jsonObject.getString("Message").contains("contact")){
                                tv_invalid.setText(""+jsonObject.getString("Message"));
                                tv_invalid.setVisibility(View.VISIBLE);
                            }
                          //  Toast.makeText(MainActivity.this,""+jsonObject.getString("Message"),Toast.LENGTH_SHORT).show();
                        }
                        if (userRole.equals("Salesman")) {
                            Log.e("***" + jsonObject.toString(), ":::::: Json Response after login");


//                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
//                            alert.setMessage("Start your Day!..");
//                            alert.setCancelable(false);
//                            alert.setPositiveButton("Star your Day", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    try {
//                                        sharedPreference.saveStringData(PrefsHelper.USER_EMAIL, userEmailID + "");
//                                        sharedPreference.saveStringData(PrefsHelper.ACCESS_TOKEN, jsonObject.getString("access_token"));
//                                        sharedPreference.saveStringData(PrefsHelper.ISSUED_ON, jsonObject.getString(".issued"));
//                                        sharedPreference.saveStringData(PrefsHelper.EXPIRE_ON, jsonObject.getString(".expires"));
//                                        dayStartApiCall();
//                                    } catch (Exception ex) {
//                                        ex.printStackTrace();
//                                    }
//                                }
//                            });
//                            alert.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    sharedPreference.saveBooleanData(PrefsHelper.START_DAY, false);
//                                    try {
//                                        sharedPreference.saveStringData(PrefsHelper.USER_EMAIL, userEmailID + "");
//                                        sharedPreference.saveStringData(PrefsHelper.ACCESS_TOKEN, jsonObject.getString("access_token"));
//                                        sharedPreference.saveStringData(PrefsHelper.ISSUED_ON, jsonObject.getString(".issued"));
//                                        sharedPreference.saveStringData(PrefsHelper.EXPIRE_ON, jsonObject.getString(".expires"));
//                                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
//                                        startActivity(intent);
//                                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//                                        finish();
//                                    } catch (Exception ex) {
//                                        ex.printStackTrace();
//                                    }
//                                }
//                            });
//                            alert.show();



                            sharedPreference.saveStringData(PrefsHelper.USER_EMAIL, userEmailID + "");
                            sharedPreference.saveStringData(PrefsHelper.ACCESS_TOKEN, jsonObject.getString("access_token"));
                            sharedPreference.saveStringData(PrefsHelper.ISSUED_ON, jsonObject.getString(".issued"));
                            sharedPreference.saveStringData(PrefsHelper.EXPIRE_ON, jsonObject.getString(".expires"));
                            dayStartApiCall();

                        }
//                        else {
//                            tv_invalid.setText("you are not a salesperson");
//                            tv_invalid.setVisibility(View.VISIBLE);
//                           // Toast.makeText(getApplicationContext(), "you are not a salesperson", Toast.LENGTH_SHORT).show();
//                        }

                      hideDialog();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        if (ex instanceof SocketTimeoutException) {
                            tv_invalid.setText("Socket Time out. Please try again.");
                            tv_invalid.setVisibility(View.VISIBLE);
                          //  Toast.makeText(MainActivity.this, "Socket Time out. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("RETRO_ERROR",":::::::"+error.toString());

                    if (error.getMessage().equals("timeout")) {

                        loginCall(userNameSt, passwordSt, deviceidSt,regId);
                    }
                    try {
                        if (error.getResponse() != null) {
                            String responseError = CommonUtils.getServerResponse(error.getResponse());
                            JSONObject jsonErrorObj = new JSONObject(responseError);
                            if (jsonErrorObj.has("error_description")){
                                tv_invalid.setText(jsonErrorObj.getString("error_description"));
                                tv_invalid.setVisibility(View.VISIBLE);
                               // Toast.makeText(MainActivity.this,""+jsonErrorObj.getString("error_description"),Toast.LENGTH_SHORT).show();
                            }
                         else if (jsonErrorObj.has("Message")){
                                tv_invalid.setText(jsonErrorObj.getString("Message"));
                                tv_invalid.setVisibility(View.VISIBLE);
                              //  Toast.makeText(MainActivity.this,""+jsonErrorObj.getString("Message"),Toast.LENGTH_SHORT).show();
                            }

                            error.printStackTrace();
                          //  Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Please check your Internet", Toast.LENGTH_SHORT).show();

                        }
                        hideDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            // Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            hideDialog();
        }
    }


    public void dayStartApiCall() {
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.startAttendence(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String serverResponse = CommonUtils.getServerResponse(response);
                try {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    String message = jsonObject.getString("Message");
                    if (message.equals("Success")) {
                        sharedPreference.saveBooleanData(PrefsHelper.START_DAY, true);
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        finish();
                        hideDialog();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    hideDialog();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                hideDialog();

            }
        });

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (passwordEt.getText().hashCode() == s.hashCode()) {
            passwordEt.setError(null);
            //  passwordEt.setError(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void showDialog() {

        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();


        }
    }

    public void hideDialog() {



        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();

        }
    }


}
