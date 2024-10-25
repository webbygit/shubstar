package com.salestrackmobileapp.android.activities;

import android.app.Dialog;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.adapter.MyGoalsAdapter;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllDeal;
import com.salestrackmobileapp.android.gson.AllVariants;
import com.salestrackmobileapp.android.gson.Business;
import com.salestrackmobileapp.android.gson.CheckIn;
import com.salestrackmobileapp.android.gson.DashboardArray;
import com.salestrackmobileapp.android.gson.GoalBusiness;
import com.salestrackmobileapp.android.gson.GoalsAccDate;
import com.salestrackmobileapp.android.gson.LatLng;
import com.salestrackmobileapp.android.gson.PendingOrderItem;
import com.salestrackmobileapp.android.gson.ProductList;
import com.salestrackmobileapp.android.gson.SaveOrder;
import com.salestrackmobileapp.android.gson.UserInfoProfile;
import com.salestrackmobileapp.android.gson.VariantProduct;
import com.salestrackmobileapp.android.gson.VisitedGoals;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.services.LocationService;
import com.salestrackmobileapp.android.singleton.Singleton;
import com.salestrackmobileapp.android.utils.AlertDialogUtils;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.CountUpTimer;
import com.salestrackmobileapp.android.utils.LocationUtils;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.salestrackmobileapp.android.activities.ForgetPasswordActivity.mypreference;

public class DashboardActivity extends BaseActivity implements RecyclerClick, NavigationView.OnNavigationItemSelectedListener {

    public static int LOCATION_UPDATE_REQUEST = 11;

    @BindView(R.id.setting_dots_img)
    ImageView threeDotsImg;

    @BindView(R.id.mygoals_ll)
    LinearLayout myGoalsLL;
    @BindView(R.id.myorder_ll)
    LinearLayout myOrdersLL;
    @BindView(R.id.product_ll)
    LinearLayout productLL;
    @BindView(R.id.business_ll)
    LinearLayout businessLL;
    @BindView(R.id.chat_ll)
    LinearLayout chatLL;
    @BindView(R.id.deals_ll)
    LinearLayout dealsLL;


    @BindView(R.id.my_goals_tv)
    TextView myGoalsTV;
    @BindView(R.id.my_order_tv)
    TextView myOrdersTV;
    @BindView(R.id.my_product_tv)
    TextView productTV;
    @BindView(R.id.my_business_tv)
    TextView businessTV;
    @BindView(R.id.my_chat_tv)
    TextView chatTV;
    @BindView(R.id.my_deals_tv)
    TextView dealsTV;


    @BindView(R.id.my_goals_img)
    ImageView myGoalsImg;
    @BindView(R.id.my_order_img)
    ImageView myOrdersImg;
    @BindView(R.id.product_img)
    ImageView productImg;
    @BindView(R.id.buisness_img)
    ImageView businessImg;
    @BindView(R.id.chat_img)
    ImageView chatImg;
    @BindView(R.id.deals_img)
    ImageView dealsImg;
    @BindView(R.id.username_name_tv)
    Custome_BoldTextView userNameTv;
    @BindView(R.id.daily_goal_rv)
    RecyclerView dailyGoalsRv;
    @BindView(R.id.todays_goal_tv)
    Custome_TextView todaysGoalTitle;
    @BindView(R.id.acheived_target_txt)
    Custome_TextView acheivedTargetTxt;
    @BindView(R.id.monthly_target_txt)
    Custome_TextView monthlyTargetTxt;
    @BindView(R.id.ic_internet)
    public ImageView ivInternet;


    @BindView(R.id.profile_img)
    ImageView profileImg;
    @BindView(R.id.coordinateLayout)
    CoordinatorLayout coordinateLayout;
    @BindView(R.id.start_day)
    Button startDayBtn;
    @BindView(R.id.stop_day)
    Button stopDayBtn;
    public static boolean backToCheckIn = false;
    Chronometer chronometer;
    static String userName = "";
    String dateSt;
    Gson builder;
    List<GoalsAccDate> listAllGoals;
    List<GoalBusiness> goalBusinessList;
    ServiceHandler serviceHandler;
    private MyGoalsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static int loadDeal = 0;
    private static Dialog mProgressDialog;
    static CircleImageView imageView;
    public static TextView notificationNumberTV;
    boolean isDashboard;


    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    IntentFilter filter;
    static int i = 0;
    Custome_BoldTextView nav_user;
    CircleImageView prImg;
    long stopTime = 0;
    boolean isChronometerRunning = false;
  private static CountDownTimer countDownTimer;

    private CountUpTimer timer;
    String date="";
    int minute=0;
    long lastLogout=0;
    SharedPreferences preferences;
    boolean isLogoutCalled=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigational_drawer_dashboard);
        ButterKnife.bind(this);
        preferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        Log.e("@@@","::::onCreate");


        String userID=sharedPreference.getStringValue(PrefsHelper.USER_EMAIL);
        String lastUserId=preferences.getString("LastEmailID","");
        Log.e("###","::userID"+userID);
        Log.e("###","::::"+lastUserId);


        if (userID.equals(lastUserId)){
            Log.e("if case","::::true");

        }
        else{
            Log.e("else case","::::true");
            SharedPreferences preferencesClear = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            editorTwo= preferencesClear.edit();
            editorTwo.clear();
            editorTwo.commit();
            try {
                if (Select.from(ProductInCart.class).first() != null) {
                    ProductInCart.deleteAll(ProductInCart.class);
                }
            }
            catch(Exception e){}


        }





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (Connectivity.isNetworkAvailable(getApplicationContext())) {
            ivInternet.setImageDrawable(getResources().getDrawable(R.drawable.ic_offline));

        } else {
            ivInternet.setImageDrawable(getResources().getDrawable(R.drawable.ic_online));

        }




        View hView = navigationView.getHeaderView(0);
        nav_user = (Custome_BoldTextView) hView.findViewById(R.id.username_name);
        prImg = (CircleImageView) hView.findViewById(R.id.imgProfilePicture);


        notificationNumberTV = (TextView) findViewById(R.id.no_notification);
        imageView = (CircleImageView) findViewById(R.id.imgProfilePicture);
        sessionWorkingWithServer();

        filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();


        if (sharedPreference.getStringValue(PrefsHelper.USERNAME) != null) {
            nav_user.setText("Welcome " + sharedPreference.getStringValue(PrefsHelper.USERNAME));
        }


      /*  final Handler handler = new Handler();
        try {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getActivity().registerReceiver(receiver, filter);
                }
            }, 20000);
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "please check your internet connection", Toast.LENGTH_SHORT).show();
        }*/


        Typeface custom_font = Typeface.createFromAsset(getAssets(), "Avenir-Next-LT-Pro_5196.ttf");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dateSt = df.format(Calendar.getInstance().getTime());
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        dailyGoalsRv.setLayoutManager(mLayoutManager);
        dailyGoalsRv.setNestedScrollingEnabled(false);
        myGoalsTV.setTypeface(custom_font);
        myOrdersTV.setTypeface(custom_font);
        productTV.setTypeface(custom_font);
        businessTV.setTypeface(custom_font);
        chatTV.setTypeface(custom_font);
        dealsTV.setTypeface(custom_font);




        if (sharedPreference.getBooleanValue(PrefsHelper.START_DAY)) {
            startDayBtn.setVisibility(View.GONE);
            stopDayBtn.setVisibility(View.VISIBLE);
        } else {
            startDayBtn.setVisibility(View.VISIBLE);
            stopDayBtn.setVisibility(View.GONE);
        }
        if (!userName.equals("")) {
            userNameTv.setText("Welcome " + userName);
        }
        serviceHandlerApiInit();
       /* getDailyGoals();*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.reports) {
            Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
            intent.putExtra("nameActivity", "orderhistory");
            intent.putExtra("isfrom", "dashboard");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(DashboardActivity.this, NotificationActivity.class);
            startActivity(intent);

        } else if (id == R.id.profile) {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.mycart) {
            if (ProductInCart.listAll(ProductInCart.class).size() != 0) {
                Intent intent = new Intent(DashboardActivity.this, GoalsActivities.class);
                intent.putExtra("Slider_cart",true);
                intent.putExtra("nameActivity", "");

                startActivity(intent);
            } else {
                Toast.makeText(DashboardActivity.this, "No Item found in Cart", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void serviceHandlerApiInit() {
        builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
    }


    @OnClick(R.id.start_day)
    public void startDay()
    {

        ApplicationClass.timerButton = stopDayBtn;

        Log.e("lastmilli","::::"+CommonUtils.lastMilliofActivity);
        Long tsLong = System.currentTimeMillis()/1000;
        long lastLogin=sharedPreference.getLongData("LastLogin");
        String todayDate=getDate(tsLong);
        if (lastLogin!=0) {


            String lastLoginDate=getDate(lastLogin);
            if (lastLoginDate.equals(todayDate)){


                String userID=sharedPreference.getStringValue(PrefsHelper.USER_EMAIL);
                if (userID.equals(preferences.getString("LastEmailID",""))) {
                    if (CommonUtils.lastMilliofActivity==0) {
                        lastLogout = preferences.getLong("LastLogOut", 0);


                    }
                    else{
                        lastLogout=CommonUtils.lastMilliofActivity;
                    }
                }
                else{

                    if (CommonUtils.lastMilliofActivity!=0) {

                        lastLogout = CommonUtils.lastMilliofActivity;
                    }
                }
            }
            else{
                if (CommonUtils.lastMilliofActivity!=0) {
                    lastLogout = CommonUtils.lastMilliofActivity;
                }
            }
        }else{

            if (CommonUtils.lastMilliofActivity!=0) {
                lastLogout = CommonUtils.lastMilliofActivity;
            }
        }

        ApplicationClass.setTimer(lastLogout);
        dayStartApiCall();
    }

    @OnClick(R.id.stop_day)
    public void stopDay() {
        Log.e("stop.millisup","::::"+ApplicationClass.millisup);
        editor = preferences.edit();
        editor.putLong("LastLogOut", ApplicationClass.millisup);

        editor.commit();

        ApplicationClass.stopCountdown();
        dayStopApiCall();
    }

    public void dayStartApiCall() {
        Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
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
                        LocationService.STARTDAY = true;
                        stopDayBtn.setVisibility(View.VISIBLE);
                        startDayBtn.setVisibility(View.GONE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                try {
                    String responseError = CommonUtils.getServerResponse(error.getResponse());
                    JSONObject jsonErrorObj = new JSONObject(responseError);
                    error.printStackTrace();
                  //  Toast.makeText(getApplicationContext(), jsonErrorObj.getString("Message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
//startAttendence
    }


    @OnClick(R.id.notification_img)
    public void callNotificationPage() {
        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        startActivity(intent);
    }

    public void dayStopApiCall() {
        Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.stopAttendence(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String serverResponse = CommonUtils.getServerResponse(response);
                try {
                    JSONObject jsonObject = new JSONObject(serverResponse);
                    Log.e("OBJECT",":::"+jsonObject.toString());
                    String message = jsonObject.getString("Message");
                    Log.e("Message:::","::"+message);
                    if (message.equals("Success")) {
                        sharedPreference.saveBooleanData(PrefsHelper.START_DAY, false);
                        LocationService.STARTDAY = false;
                        startDayBtn.setVisibility(View.VISIBLE);
                        stopDayBtn.setVisibility(View.GONE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                try {
                    String responseError = CommonUtils.getServerResponse(error.getResponse());
                    JSONObject jsonErrorObj = new JSONObject(responseError);
                    error.printStackTrace();
//                    Toast.makeText(getApplicationContext(), jsonErrorObj.getString("Message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//startAttendence
    }


    private void getAllDealsArray() {
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.getAllDealsArray(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String arr = CommonUtils.getServerResponse(response);
                Log.e("DEAL",":::rewsponse"+arr);


                try {
                    if (Select.from(AllDeal.class).first() != null) {
                        AllDeal.deleteAll(AllDeal.class);
                    }
                    if (Select.from(ProductList.class).first() != null) {
                        ProductList.deleteAll(ProductList.class);
                    }
                } catch (Exception es) {
                    es.printStackTrace();
                }

                try {
                    JSONArray jsonArr = new JSONArray(arr);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        AllDeal allDeal = builder.fromJson(jsonArr.get(i).toString(), AllDeal.class);
                        List<ProductList> productList = allDeal.getProductList();
                        for (ProductList productListObj : productList) {
                            productListObj.setDealID(allDeal.getDealID());
                            productListObj.setDealType(allDeal.getDealType());
                            productListObj.save();
                        }

                        allDeal.save();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();

                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                //   Toast.makeText(DashboardActivity.this, "Business not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDashBoardDetail() {
        //getDashboardDetail
        // CommonUtils.showProgress(this);
        builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.getDashboardDetail(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String serverResponse = CommonUtils.getServerResponse(response);
                try {
                    JSONArray jsonArray = new JSONArray(serverResponse);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Log.e("Dashboardarray","::::"+jsonArray.toString());
                    if (DashboardArray.listAll(DashboardArray.class).size() != 0) {
                        DashboardArray.deleteAll(DashboardArray.class);
                        CheckIn.deleteAll(CheckIn.class);
                        Business.deleteAll(Business.class);
                    }
                    DashboardArray dashboardArray = builder.fromJson(jsonObject.toString(), DashboardArray.class);
                    /*dashboardArray.getCheckIn()*/
                    mAdapter = new MyGoalsAdapter(DashboardActivity.this, DashboardActivity.this);
                    for (CheckIn checkin : dashboardArray.getCheckIn()) {
                        checkin.getBusiness().save();
                        checkin.save();

                        goalBusinessList = GoalBusiness.listAll(GoalBusiness.class);
                        if (goalBusinessList.size() != 0) {
                            mAdapter.setListGoals();
                            dailyGoalsRv.setAdapter(mAdapter);
                            todaysGoalTitle.setText("Today's Goals");
                            mAdapter.notifyDataSetChanged();
                        } else {
                            dailyGoalsRv.setVisibility(View.GONE);
                        }
                    }

                    dashboardArray.getMonthlyGoals().save();
                    Log.e("getMonthlyTarget",":::::"+dashboardArray.getMonthlyGoals().getMonthlyTarget());
                    monthlyTargetTxt.setText(dashboardArray.getMonthlyGoals().getMonthlyTarget() + "");
                    acheivedTargetTxt.setText(dashboardArray.getMonthlyGoals().getAchievedTarget() + "");
                    dashboardArray.save();
                    // CommonUtils.dismissProgress();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    CommonUtils.dismissProgress();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void getLatLongWithTiming() {
        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(this);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setContentView(R.layout.l_progress_view);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }


        Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            String date = df.format(Calendar.getInstance().getTime());//2017-05-02T20:30:26.933Z

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("Latitude", String.valueOf(Singleton.getInstance().SLAT));
            jsonObj.put("Longitude", String.valueOf(Singleton.getInstance().SLNG));
            jsonObj.put("TrackDate", date + "Z");
            jsonObj.put("SalesPersonName", userName);
            LatLng latLongObject = builder.fromJson(jsonObj.toString(), LatLng.class);

            ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
            serviceHandler.trackLatLong(latLongObject, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    String serverResponse = CommonUtils.getServerResponse(response);
                    try {
                        JSONObject jsonObject = new JSONObject(serverResponse);
                        Intent intentSer = new Intent(getApplicationContext(), LocationService.class);
                        if (sharedPreference.getBooleanValue(PrefsHelper.START_DAY)) {
                            intentSer.putExtra("start_day", true);
                        }
                        intentSer.putExtra("TIME_IN_MINUTES", Long.parseLong(jsonObject.getString("Message")));
                        intentSer.putExtra("ACCESS_TOKEN", sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN));
                        intentSer.putExtra("EXPIRTIME", sharedPreference.getStringValue(PrefsHelper.EXPIRE_ON));
                        startService(intentSer);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    //  CommonUtils.dismissProgress();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            // CommonUtils.dismissProgress();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            //CommonUtils.dismissProgress();
            //  CommonUtils.showErrorMessage("Check Internet Connection", coordinateLayout);
            // Toast.makeText(getApplicationContext(), "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
            // finish();
        }
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
          //  Toast.makeText(DashboardActivity.this, R.string.landing_unable_fetch_location, Toast.LENGTH_SHORT).show();
        } else if (!Connectivity.isConnected(this)) {
           // Toast.makeText(DashboardActivity.this, R.string.landing_unable_fetch_location, Toast.LENGTH_SHORT).show();
        } else {
            Singleton.getInstance().SLAT = location.getLatitude();
            Singleton.getInstance().SLNG = location.getLongitude();
            //  Toast.makeText(DashboardActivity.this, "" + Singleton.getInstance().SLAT + "" + Singleton.getInstance().SLNG, Toast.LENGTH_SHORT).show();
            //  AlertDialogUtils.closeAlertDialog(this);
        }
    }


    public void getTimerForCallService() {
        CommonUtils.showProgress(this);
        Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            String date = df.format(Calendar.getInstance().getTime());//2017-05-02T20:30:26.933Z

            final JSONObject jsonObj = new JSONObject();
            jsonObj.put("Latitude", String.valueOf(Singleton.getInstance().SLAT));
            jsonObj.put("Longitude", String.valueOf(Singleton.getInstance().SLNG));
            jsonObj.put("TrackDate", date + "Z");
            jsonObj.put("SalesPersonName", userName);

            LatLng latLongObject = builder.fromJson(jsonObj.toString(), LatLng.class);
            Log.e("Location_params",":::::Dashboard ::::::::"+jsonObj.toString());
            //Longitude

            final ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
            serviceHandler.trackLatLong(latLongObject, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    String serverResponse = CommonUtils.getServerResponse(response);

                    try {
                        JSONObject jsonObject = new JSONObject(serverResponse);
                        if (jsonObject.has("Message")) {
                            Log.e("Message", ":::::" + jsonObject.getString("Message"));
                        }
                        Intent intentSer = new Intent(getApplicationContext(), LocationService.class);

                        intentSer.putExtra("TIME_IN_MINUTES", Long.parseLong(jsonObject.getString("Message")));
                        intentSer.putExtra("ACCESS_TOKEN", sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN));
                        sharedPreference.saveStringData(PrefsHelper.HITTING_TIMING, jsonObject.getString("Message"));
                        sharedPreference.saveStringData(PrefsHelper.SALESPERSONNAME, userName);
                        //HITTING_TIMING
                        intentSer.putExtra("USERNAME", userName);
                        startService(intentSer);
                        //final long period = 0;


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
            CommonUtils.dismissProgress();
        } catch (Exception ex) {
            ex.printStackTrace();
            CommonUtils.dismissProgress();
        }
    }


    private void getUserInfo() {
        if (Select.from(UserInfoProfile.class).list().size() != 0) {
            UserInfoProfile.deleteAll(UserInfoProfile.class);
        }
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.getUserInfo(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String arr = CommonUtils.getServerResponse(response);
                Log.e("USER:::::","::::::"+arr);
                try {
                    UserInfoProfile userInfoProfile = builder.fromJson(arr, UserInfoProfile.class);
                    userName = userInfoProfile.getFirstName();
                    nav_user.setText("Welcome " + userName);
                    sharedPreference.saveStringData(PrefsHelper.USER_ID, String.valueOf(userInfoProfile.getUserProfileID()));
                    sharedPreference.saveStringData(PrefsHelper.USERNAME, userName);
                    userInfoProfile.save();
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    if (userInfoProfile.getImageUrl().equals("")) {
                        prImg.setImageResource(R.drawable.user_pic);
                    } else {
                        Picasso.with(getApplicationContext()).load(userInfoProfile.getImageUrl()).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.user_pic)).error(R.drawable.user_pic).into(prImg);
                    }
                    getTimerForCallService();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                //  CommonUtils.showErrorMessage("Please check your internet connectivity", coordinateLayout);
                //  Toast.makeText(getApplicationContext(), "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
                //  finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
       // timerUp();
        Log.e("@@@","::::On resume true");






        if (sharedPreference.getBooleanValue(PrefsHelper.START_DAY)) {
            startDayBtn.setVisibility(View.GONE);
            stopDayBtn.setVisibility(View.VISIBLE);

            ApplicationClass.timerButton = stopDayBtn;

            Long tsLong = System.currentTimeMillis()/1000;
            long lastLogin=sharedPreference.getLongData("LastLogin");
            String todayDate=getDate(tsLong);
            if (lastLogin!=0) {
                Log.e("@@@","::::1");

                String lastLoginDate=getDate(lastLogin);
                if (lastLoginDate.equals(todayDate)){
                    Log.e("@@@","::::2");

                    String userID=sharedPreference.getStringValue(PrefsHelper.USER_EMAIL);
                    if (userID.equals(preferences.getString("LastEmailID",""))) {
                        if (CommonUtils.lastMilliofActivity==0) {
                            lastLogout = preferences.getLong("LastLogOut", 0);
                            Log.e("@@@","::::3");

                        }
                        else{
                            lastLogout=CommonUtils.lastMilliofActivity;
                            Log.e("@@@","::::4");

                        }

                    }
                    else{
                        Log.e("@@@","::::5");


                        if (CommonUtils.lastMilliofActivity!=0) {
                            Log.e("@@@","::::6");

                            lastLogout = CommonUtils.lastMilliofActivity;
                        }
                    }



                }
                else{
                    Log.e("@@@","::::7");


                    if (CommonUtils.lastMilliofActivity!=0) {
                        lastLogout = CommonUtils.lastMilliofActivity;
                        Log.e("@@@","::::8");
                    }
                }

            }else{
                Log.e("@@@","::::9");

                if (CommonUtils.lastMilliofActivity!=0) {
                    Log.e("@@@","::::10");

                    lastLogout = CommonUtils.lastMilliofActivity;
                }
            }

            ApplicationClass.setTimer(lastLogout);
        } else {
            startDayBtn.setVisibility(View.VISIBLE);
            stopDayBtn.setVisibility(View.GONE);


        }

        myGoalsImg.setImageDrawable(getResources().getDrawable(R.drawable.my_goals));
        myGoalsTV.setTextColor(getResources().getColor(R.color.grey));
        myOrdersImg.setImageDrawable(getResources().getDrawable(R.drawable.orders));
        myOrdersTV.setTextColor(getResources().getColor(R.color.grey));
        productImg.setImageDrawable(getResources().getDrawable(R.drawable.products));
        productTV.setTextColor(getResources().getColor(R.color.grey));
        businessImg.setImageDrawable(getResources().getDrawable(R.drawable.business));
        businessTV.setTextColor(getResources().getColor(R.color.grey));
        chatImg.setImageDrawable(getResources().getDrawable(R.drawable.chat));
        chatTV.setTextColor(getResources().getColor(R.color.grey));
        dealsImg.setImageDrawable(getResources().getDrawable(R.drawable.deals));
        dealsTV.setTextColor(getResources().getColor(R.color.grey));
        try {

            registerReceiver(receiver, filter);
        } catch (Exception e) {
            Log.e("@Network_error", "::::" + e.toString());
        }
        fetchLocationAndResult();
        getTimerForCallService();
        getDashBoardDetail();

    }



    public void onPause() {
        super.onPause();
        Log.e("OnPAuse",":::::::");

            unregisterReceiver(receiver);

    }

    @OnClick(R.id.mygoals_ll)
    public void myGoalsNavigate() {

        myGoalsImg.setImageDrawable(getResources().getDrawable(R.drawable.my_goals_active));
        myGoalsTV.setTextColor(getResources().getColor(R.color.colorAccent));

        myOrdersImg.setImageDrawable(getResources().getDrawable(R.drawable.orders));
        myOrdersTV.setTextColor(getResources().getColor(R.color.grey));
        productImg.setImageDrawable(getResources().getDrawable(R.drawable.products));
        productTV.setTextColor(getResources().getColor(R.color.grey));
        businessImg.setImageDrawable(getResources().getDrawable(R.drawable.business));
        businessTV.setTextColor(getResources().getColor(R.color.grey));
        chatImg.setImageDrawable(getResources().getDrawable(R.drawable.chat));
        chatTV.setTextColor(getResources().getColor(R.color.grey));
        dealsImg.setImageDrawable(getResources().getDrawable(R.drawable.deals));
        dealsTV.setTextColor(getResources().getColor(R.color.grey));


        if (Connectivity.isConnected(getApplicationContext())) {


            Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
            intent.putExtra("nameActivity", "MyGoals");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else {

            Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
            intent.putExtra("nameActivity", "MyGoals");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            //  CommonUtils.showErrorMessage("Please check your internet connectivity", coordinateLayout);
            //   Toast.makeText(getApplicationContext(), "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
            //   finish();
        }

    }

    @OnClick(R.id.product_ll)
    public void allProductNavigate() {
        sharedPreference.saveStringData(PrefsHelper.NAVI_PRODUCT_BUSINESS, "product");
        productImg.setImageDrawable(getResources().getDrawable(R.drawable.products_active));
        productTV.setTextColor(getResources().getColor(R.color.colorAccent));

        myGoalsImg.setImageDrawable(getResources().getDrawable(R.drawable.my_goals));
        myGoalsTV.setTextColor(getResources().getColor(R.color.grey));
        myOrdersImg.setImageDrawable(getResources().getDrawable(R.drawable.orders));
        myOrdersTV.setTextColor(getResources().getColor(R.color.grey));
        businessImg.setImageDrawable(getResources().getDrawable(R.drawable.business));
        businessTV.setTextColor(getResources().getColor(R.color.grey));
        chatImg.setImageDrawable(getResources().getDrawable(R.drawable.chat));
        chatTV.setTextColor(getResources().getColor(R.color.grey));
        dealsImg.setImageDrawable(getResources().getDrawable(R.drawable.deals));
        dealsTV.setTextColor(getResources().getColor(R.color.grey));

        GoalsActivities.backToCheckIn = false;//for checkin fragment stopping distraction

        if (Connectivity.isConnected(getApplicationContext())) {
            Intent intent = new Intent(DashboardActivity.this, GoalsActivities.class);
            sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, null);
            intent.putExtra("nameActivity", "AllProductHome");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            Log.e("@@@", "::::Goalsdashboard");
        } else {
            // CommonUtils.showErrorMessage("Please check your internet connectivity", coordinateLayout);

            //  if (AllProduct.listAll(AllProduct.class).size() != 0) {
            Intent intent = new Intent(DashboardActivity.this, GoalsActivities.class);
            //  sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, null);
            intent.putExtra("nameActivity", "AllProductHome");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            Log.e("@@@", "::::Goalsdashboard");

            //  }
            // Toast.makeText(getApplicationContext(), "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
         /*   finish();*/
        }

    }

    @OnClick(R.id.business_ll)
    public void allBuisnessNavigate() {

        sharedPreference.saveStringData(PrefsHelper.NAVI_PRODUCT_BUSINESS, "business");
        businessImg.setImageDrawable(getResources().getDrawable(R.drawable.business_active));
        businessTV.setTextColor(getResources().getColor(R.color.colorAccent));

        myGoalsImg.setImageDrawable(getResources().getDrawable(R.drawable.my_goals));
        myGoalsTV.setTextColor(getResources().getColor(R.color.grey));
        myOrdersImg.setImageDrawable(getResources().getDrawable(R.drawable.orders));
        myOrdersTV.setTextColor(getResources().getColor(R.color.grey));
        productImg.setImageDrawable(getResources().getDrawable(R.drawable.products));
        productTV.setTextColor(getResources().getColor(R.color.grey));
        businessImg.setImageDrawable(getResources().getDrawable(R.drawable.business));
        businessTV.setTextColor(getResources().getColor(R.color.grey));
        chatImg.setImageDrawable(getResources().getDrawable(R.drawable.chat));
        chatTV.setTextColor(getResources().getColor(R.color.grey));
        dealsImg.setImageDrawable(getResources().getDrawable(R.drawable.deals));
        dealsTV.setTextColor(getResources().getColor(R.color.grey));

        if (Connectivity.isConnected(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
            intent.putExtra("nameActivity", "AllBusinessDashBoard");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else {
            // CommonUtils.showErrorMessage("Please check your internet connectivity", coordinateLayout);

            Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
            intent.putExtra("nameActivity", "AllBusinessDashBoard");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        }

    }


    @OnClick(R.id.deals_ll)
    public void allDeals() {
        dealsImg.setImageDrawable(getResources().getDrawable(R.drawable.deals_active));
        dealsTV.setTextColor(getResources().getColor(R.color.colorAccent));

        myGoalsImg.setImageDrawable(getResources().getDrawable(R.drawable.my_goals));
        myGoalsTV.setTextColor(getResources().getColor(R.color.grey));
        myOrdersImg.setImageDrawable(getResources().getDrawable(R.drawable.orders));
        myOrdersTV.setTextColor(getResources().getColor(R.color.grey));
        productImg.setImageDrawable(getResources().getDrawable(R.drawable.products));
        productTV.setTextColor(getResources().getColor(R.color.grey));
        businessImg.setImageDrawable(getResources().getDrawable(R.drawable.business));
        businessTV.setTextColor(getResources().getColor(R.color.grey));
        chatImg.setImageDrawable(getResources().getDrawable(R.drawable.chat));
        chatTV.setTextColor(getResources().getColor(R.color.grey));
        businessImg.setImageDrawable(getResources().getDrawable(R.drawable.business));
        businessTV.setTextColor(getResources().getColor(R.color.grey));

        if (Connectivity.isConnected(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
            intent.putExtra("nameActivity", "AllDeals");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else {
            // CommonUtils.showErrorMessage("Please check your internet connectivity", coordinateLayout);
            // if (AllDeal.listAll(AllDeal.class).size() != 0) {
            Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
            intent.putExtra("nameActivity", "AllDeals");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        }
    }

    @OnClick(R.id.myorder_ll)
    public void myorder() {
        Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
        intent.putExtra("nameActivity", "orderhistory");
        intent.putExtra("isfrom", "dashboard");
        startActivity(intent);
        Log.e("@@@", ":::::ORDER_dashbioard");
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

    @OnClick(R.id.profile_img)
    public void profilePageCalling() {
        if (Connectivity.isConnected(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else {
            // CommonUtils.showErrorMessage("Please check your internet connectivity", coordinateLayout);
//            if (UserInfoProfile.listAll(UserInfoProfile.class).size() != 0) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//            }
        }
    }
boolean isOnBAck=false;
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Confirm close app...");
        // Setting Dialog Message
        alertDialog.setMessage("Do you want to close this app");
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (!isLogoutCalled) {
                    if (!isOnBAck) {
                        editor = preferences.edit();
                        editor.putLong("LastLogOut", ApplicationClass.millisup);
                        CommonUtils.lastMilliofActivity = 0;

                        Log.e("###", ":::ID3" + sharedPreference.getStringValue(PrefsHelper.USER_EMAIL));
                        editor.putString("LastEmailID", sharedPreference.getStringValue(PrefsHelper.USER_EMAIL));
                        editor.commit();



                        ApplicationClass.stopCountdown();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
                        finish();
                        isOnBAck=true;
                    }
                }


                // Write your code here to invoke YES event
                //  Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                // Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void productClick(View v, int position) {

    }


    public class NetworkChangeReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.v("@@@Goals", "Receieved notification about network status");


            try {
                if(Connectivity.isNetworkAvailable(context)){
                    if (!isConnected) {
                        Log.v("is network Available", "Now you are connected to Internet!");
                        isConnected = true;
                        sharedPreference.saveBooleanValue("IsDashBoard", true);
                        ivInternet.setImageDrawable(getResources().getDrawable(R.drawable.ic_offline));
                        // Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                        getLatLongWithTiming();
                        getDashBoardDetail();
                        if (loadDeal >= 0) {
                            loadDeal++;
                            getAllDealsArray();
                        }
                        sessionWorkingWithServer();
                        try {
                            getUserInfo();
                        }
                        catch (Exception e){
                            Log.e("Exception",":::"+e.toString());
                        }


                    }
                    ivInternet.setImageDrawable(getResources().getDrawable(R.drawable.ic_offline));
                }
                else{
                    ivInternet.setImageDrawable(getResources().getDrawable(R.drawable.ic_online));


//            if (!sharedPreference.getBooleanValue("IsDashBoard")){
//                Toast.makeText(DashboardActivity.this,"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//            }
                    DashboardArray dashboardArray = Select.from(DashboardArray.class).first();
                    monthlyTargetTxt.setText(dashboardArray.getMonthlyGoals().getMonthlyTarget() + "");
                    acheivedTargetTxt.setText(dashboardArray.getMonthlyGoals().getAchievedTarget() + "");

                    UserInfoProfile userInfoProfile = Select.from(UserInfoProfile.class).first();
                    userNameTv.setText("Welcome " + userInfoProfile.getFirstName());
                    Picasso.with(getApplicationContext()).load(userInfoProfile.getImageUrl()).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.user_pic)).error(R.drawable.user_pic).into(imageView);
                    //  Toast.makeText(context, "not Connected", Toast.LENGTH_SHORT).show();
                    isConnected = false;
                }
            } catch (Exception e) {
                ivInternet.setImageDrawable(getResources().getDrawable(R.drawable.ic_online));



            }

        }


        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();

                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if (!isConnected) {
                                Log.v("is network Available", "Now you are connected to Internet!");
                                isConnected = true;
                                sharedPreference.saveBooleanValue("IsDashBoard", true);
                                ivInternet.setImageDrawable(getResources().getDrawable(R.drawable.ic_offline));
                                // Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                                getLatLongWithTiming();
                                getDashBoardDetail();
                                if (loadDeal >= 0) {
                                    loadDeal++;
                                    getAllDealsArray();
                                }
                                sessionWorkingWithServer();
                                try {
                                    getUserInfo();
                                }
                                catch (Exception e){
                                    Log.e("Exception",":::"+e.toString());
                                }


                            }

// else {
                            ivInternet.setImageDrawable(getResources().getDrawable(R.drawable.ic_offline));


                            return true;
                        }
                    }
                }
            }
            ivInternet.setImageDrawable(getResources().getDrawable(R.drawable.ic_online));



//            if (!sharedPreference.getBooleanValue("IsDashBoard")){
//                Toast.makeText(DashboardActivity.this,"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//            }
            DashboardArray dashboardArray = Select.from(DashboardArray.class).first();
            monthlyTargetTxt.setText(dashboardArray.getMonthlyGoals().getMonthlyTarget() + "");
            acheivedTargetTxt.setText(dashboardArray.getMonthlyGoals().getAchievedTarget() + "");

            UserInfoProfile userInfoProfile = Select.from(UserInfoProfile.class).first();
            userNameTv.setText("Welcome " + userInfoProfile.getFirstName());
            Picasso.with(getApplicationContext()).load(userInfoProfile.getImageUrl()).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.user_pic)).error(R.drawable.user_pic).into(imageView);
            //  Toast.makeText(context, "not Connected", Toast.LENGTH_SHORT).show();
            isConnected = false;
            return false;
        }
    }


    public void sessionWorkingWithServer() {
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.checkSession(CommonUtils.getDeviceID(getApplicationContext()), sharedPreference.getStringValue(PrefsHelper.REGID), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String arr = CommonUtils.getServerResponse(response);
                try {
                    JSONObject jsonObject = new JSONObject(arr);
                    if (jsonObject.getBoolean("Status")) {

                        Log.d("continue ", "session connected");
                    } else {

                        Toast.makeText(getApplicationContext(), "Your session has been expired.please login again...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        finish();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                if (error.getMessage().equals("timeout")) {
                    sessionWorkingWithServer();
                }
            }
        });

    }


  /*  private void requestHttp() {


        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(this, ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        List<SaveOrder> listSaveOrderListArray = Select.from(SaveOrder.class).where(Condition.prop("send_to_server").eq("0")).list();
        for (SaveOrder saveOrderObj : listSaveOrderListArray
                ) {
            List<PendingOrderItem> orderItems = Select.from(PendingOrderItem.class).where(Condition.prop("order_number_id").eq(saveOrderObj.getOrderNo())).list();
            saveOrderObj.setPendingOrderItems(orderItems);
        }

        final List<SaveOrder> newListToSent = Select.from(SaveOrder.class).where(Condition.prop("send_to_server").eq("0")).list();
        if (ProductInCart.listAll(ProductInCart.class).size() != 0) {
            serviceHandler.placeOrder(newListToSent, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    String serverResponse = CommonUtils.getServerResponse(response);
                    try {
                        JSONObject jsonObject1 = new JSONObject(serverResponse);
                        if (jsonObject1.getString("Message").equals("Success")) {
                            for (SaveOrder saveOrder : newListToSent) {
                                SaveOrder newSaveOrder = Select.from(SaveOrder.class).where(Condition.prop("order_no").eq(saveOrder.getOrderNo())).first();
                                newSaveOrder.setSendToServer("1");
                                newSaveOrder.save();
                            }
                        }
                        ProductInCart.deleteAll(ProductInCart.class);

                        Toast.makeText(getApplicationContext(), "Your order has been successfully placed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
                        intent.putExtra("nameActivity", "orderhistory");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (error.getMessage().equals("timeout")) {
                        requestHttp();
                    }
                    error.printStackTrace();
                    Toast.makeText(getBaseContext(), "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
                }
            });
            CommonUtils.dismissProgress();
        }
    }*/
  SharedPreferences.Editor editor,editorTwo;
    @OnClick(R.id.setting_dots_img)
    public void showMenu() {
        final PopupMenu menu = new PopupMenu(getApplicationContext(), threeDotsImg);
        menu.inflate(R.menu.profile_menu);

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logout_menu) {

                    editor = preferences.edit();
                    editor.putLong("LastLogOut", ApplicationClass.millisup);

                    editor.commit();

                    ApplicationClass.stopCountdown();
                    dayStopApiCall();
                    Log.e("save_token",""+sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN));
                    Log.e("###",":::uswe_email:::"+sharedPreference.getStringValue(PrefsHelper.USER_EMAIL));
                    CommonUtils.lastMilliofActivity=0;

                    editor = preferences.edit();
                    editor.putString("lastToken", ""+sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN));
                    editor.putLong("LastLogOut",ApplicationClass.millisup);
                    editor.putString("LastEmailID",sharedPreference.getStringValue(PrefsHelper.USER_EMAIL));
                    editor.commit();
                    Log.e("###","::::ID::::"+  preferences.getString("LastEmailID",""));

                    SharedPreferences preferencesTwo = getSharedPreferences("SalesTrack", Context.MODE_PRIVATE);
                    editorTwo= preferencesTwo.edit();
                    editorTwo.clear();
                    editorTwo.commit();
                    Log.e("###","::::ID2::::"+  preferences.getString("LastEmailID",""));
                    isLogoutCalled=true;

                    //ApplicationClass.stopCountdown();
                    editor = preferences.edit();
                    editor.putLong("LastLogOut", ApplicationClass.millisup);

                    editor.commit();

                    ApplicationClass.stopCountdown();

                    dayStopApiCall();

                   /* SugarContext.terminate();
                    SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
                    schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
                    SugarContext.init(getApplicationContext());
                    schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());
                    finish();*/

                    if (Select.from(SaveOrder.class).first() != null) {
                        SaveOrder.deleteAll(SaveOrder.class);
                        if (Select.from(PendingOrderItem.class).first() != null) {
                            PendingOrderItem.deleteAll(PendingOrderItem.class);
                        }
                    }

                    if (Select.from(VisitedGoals.class).first() != null) {
                        VisitedGoals.deleteAll(VisitedGoals.class);
                    }
                    editor = preferences.edit();
                    editor.putLong("LastLogOut", ApplicationClass.millisup);

                    editor.commit();

                    ApplicationClass.stopCountdown();
                    dayStopApiCall();
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.edit_profile) {
                    // Toast.makeText(getApplicationContext(), "Edit Profile is clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
                return false;
            }
        });

        menu.show();
    }




    public void pauseTimer(){

    }





    public void onDestroy() {


        super.onDestroy();
        if (!isLogoutCalled) {
            if (!isOnBAck) {

                editor = preferences.edit();
                editor.putLong("LastLogOut", ApplicationClass.millisup);
                Log.e("###", ":::ID3" + sharedPreference.getStringValue(PrefsHelper.USER_EMAIL));
                editor.putString("LastEmailID", sharedPreference.getStringValue(PrefsHelper.USER_EMAIL));
                editor.commit();
                ApplicationClass.stopCountdown();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
                finish();
            }

        }




    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
    private String getMinute(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("mm", cal).toString();
        Log.e("Minute",":::"+date);
        return date;

    }




}



