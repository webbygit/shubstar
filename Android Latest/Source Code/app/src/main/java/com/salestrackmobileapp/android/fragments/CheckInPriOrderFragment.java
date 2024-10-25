package com.salestrackmobileapp.android.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.ApplicationClass;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.activities.SplashActivity;
import com.salestrackmobileapp.android.adapter.PreOrderAdapter;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllBusiness;
import com.salestrackmobileapp.android.gson.CheckedDataHit;
import com.salestrackmobileapp.android.gson.GoalBusiness;
import com.salestrackmobileapp.android.gson.PrevOrder;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.singleton.Singleton;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CheckInPriOrderFragment extends BaseFragment implements RecyclerClick {


    static Boolean checkedInStatus = false;

    @BindView(R.id.check_in_btn)
    Button checkInBtn;
    @BindView(R.id.checkin_linear_layout)
    LinearLayout checkedLinearLayout;
    @BindView(R.id.previous_order_rv)
    RecyclerView preRcyclerView;

    @BindView(R.id.goal_title)
    Custome_BoldTextView titleGoalTV;
    @BindView(R.id.desc_tv)
    Custome_TextView descTV;
    @BindView(R.id.number_items_tv)
    Custome_TextView contactPersonTv;
    @BindView(R.id.business_name)
    Custome_TextView businessDetailTv;
    @BindView(R.id.amount_tv)
    Custome_TextView contactNumberTv;

    ImageView homeImg;
    int goal_location = 0;

    GoalBusiness goalBusiness;
    Gson builder;
    ServiceHandler serviceHandler;
    int businessId;
    CheckedDataHit checkedDataHit;
    private LinearLayoutManager mLayoutManager;
    private PreOrderAdapter mAdapter;
    static String totleGoalSt, descSt, contactNumber, amountSt;
    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;

    public CheckInPriOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_in_pri_order, container, false);
        ButterKnife.bind(this, view);


        if (getArguments() != null) {
            if (getArguments().getSerializable("goal_object") == null) {

            } else {
                goalBusiness = (GoalBusiness) getArguments().getSerializable("goal_object");
                businessId = goalBusiness.getBusinessID();
            }
        }

        if (!sharedPreference.getStringValue(PrefsHelper.GOAL_LOCATION).equals("goal_location")) {
            List<GoalBusiness> listGoal = GoalBusiness.listAll(GoalBusiness.class);
            if (listGoal.size() != 0) {
                String goalLoc = sharedPreference.getStringValue(PrefsHelper.GOAL_LOCATION);
                int location = Integer.parseInt(goalLoc);
                goalBusiness = listGoal.get(location);
                businessId = goalBusiness.getBusinessID();
                checkedInStatus = goalBusiness.getCheckedIN();
            }

        }
        if (goalBusiness != null) {
            checkedInStatus = goalBusiness.getCheckedIN();

            try {
                totleGoalSt = goalBusiness.getBusnessName().toString();
            } catch (Exception ex) {
                totleGoalSt = "";
            }
            try {
                descSt = goalBusiness.getAddress1().toString() + "" + goalBusiness.getAddress2().toString();
            } catch (Exception ex) {
                descSt = "";
            }
            try {
                contactNumber = goalBusiness.getContactPersonName().toString();
            } catch (Exception ex) {
                contactNumber = "";
            }
            try {
                amountSt = goalBusiness.getContactPersonPhone().toString();
            } catch (Exception ex) {
                amountSt = "";
            }

            titleGoalTV.setText(totleGoalSt + "");
            descTV.setText("" + descSt + "");
            contactPersonTv.setText(contactNumber + "");
            contactNumberTv.setText("Contact :" + amountSt + "");

            baseActivity.setTitle(goalBusiness.getAddress1() + " " + goalBusiness.getAddress2());
            //  contectDetailTv.setText(goalBusiness.getBusiness().getContactPersonName() + " " + goalBusiness.getBusiness().getContactPersonPhone());
        }
        mLayoutManager = new LinearLayoutManager(getActivity());
        preRcyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PreOrderAdapter(getActivity(), this);
        getPreOrderArray();
        return view;
    }


    @OnClick(R.id.notes_btn)
    public void noteFragmentCall() {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        AllBusiness allBusinessObj = new AllBusiness();
        allBusinessObj.setAddress1(goalBusiness.getAddress1() + "");
        allBusinessObj.setAddress2(goalBusiness.getAddress2() + "");
        allBusinessObj.setCheckedIN(goalBusiness.getCheckedIN());
        allBusinessObj.setBusinessID(goalBusiness.getBusinessID());
        allBusinessObj.setBusnessName(goalBusiness.getBusnessName() + "");
        allBusinessObj.setCity(goalBusiness.getCity() + "");
        allBusinessObj.setCountry(goalBusiness.getCountry() + "");
        allBusinessObj.setState(goalBusiness.getState() + "");
        allBusinessObj.setContactPersonName(goalBusiness.getContactPersonName() + "");
        allBusinessObj.setContactPersonPhone(goalBusiness.getContactPersonPhone() + "");
        allBusinessObj.setContactPersonEmail(goalBusiness.getContactPersonEmail() + "");
        allBusinessObj.setImageName(goalBusiness.getImageName() + "");
        allBusinessObj.setWebsiteName(goalBusiness.getWebsiteName() + "");
        allBusinessObj.setZipCode(goalBusiness.getZipCode() + "");


        args.putSerializable("BusinessObject", allBusinessObj);
        args.putString("checkin", "cop");
        fragment.setArguments(args);
        baseActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }


    public void getPreOrderArray() {
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(baseActivity, ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.getAllPreOrder(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String serverResponse = CommonUtils.getServerResponse(response);
                try {
                    if (!serverResponse.equals("[]\n")) {
                        JSONArray jsonArray = new JSONArray(serverResponse);
                        PrevOrder preOrder = builder.fromJson(jsonArray.get(0).toString(), PrevOrder.class);
                        preOrder.getBusiness().save();
                        preOrder.save();
                    } else {
                        Toast.makeText(ApplicationClass.getAppContext(), "no previous order available", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    PrefsHelper.clearPreference(baseActivity);
                    Toast.makeText(ApplicationClass.getAppContext(), "Your credentials is expired.Please login again", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(baseActivity, SplashActivity.class);
                    startActivity(intent);
                    baseActivity.finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(baseActivity, "No order available", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @OnClick(R.id.visited_business)
    public void visitedBusiness() {
        Toast.makeText(baseActivity, "You have already checked in ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(baseActivity, GoalsActivities.class);
        intent.putExtra("nameActivity", "visitsfragment");
        //   intent.putExtra("backoncheckin", "true");
        Log.d("successfully checkin", "checkined");
        startActivity(intent);


      /*  VisitedGoalsFragment fragment = new VisitedGoalsFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();*/
    }

    @OnClick(R.id.check_in_btn)
    public void placeOrderFragmentCall() {

        sharedPreference.saveStringData(PrefsHelper.NAVI_PRODUCT_BUSINESS, "CheckinBusiness");
        if (checkedInStatus) {
            Toast.makeText(baseActivity, "You have already checked in ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(baseActivity, GoalsActivities.class);
            intent.putExtra("nameActivity", "AllProduct");
            intent.putExtra("backoncheckin", "true");
            Log.d("successfully checkin", "checkined");
            startActivity(intent);
        } else {

            if (!Connectivity.isConnected(baseActivity)) {
                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                receiver = new NetworkChangeReceiver();
                if (ApplicationClass.getAppContext() != null)
                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).registerReceiver(receiver, filter);
            } else {
                if (checkedInStatus) {
                    Toast.makeText(baseActivity, "You have already checkin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), GoalsActivities.class);
                    intent.putExtra("nameActivity", "AllProduct");
                    intent.putExtra("backoncheckin", "true");
                    Log.d("successfully checkin", "checkined");
                    startActivity(intent);
                    baseActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } else {
                    checkInApiCall();
                }

            }
        }
    }

    private void checkInApiCall() {
        builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        try {
            JSONObject object = new JSONObject();
            object.put("UserCheckInId", 0);
            object.put("UserProfileID", Integer.parseInt(sharedPreference.getStringValue(PrefsHelper.USER_ID)));
            object.put("Longitude", Singleton.instance.SLNG);
            object.put("Latitude", Singleton.instance.SLAT);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            //2017-07-30T18:01.012Z
            String currentDateandTime = sdf.format(new Date());

            object.put("CheckInDate", currentDateandTime + "");
            // object.put("CheckInDate", "2017-04-13T06:54:04.543Z");
            object.put("BusinessID", businessId);

            checkedDataHit = builder.fromJson(object.toString(), CheckedDataHit.class);
            serviceHandler.checkedInSave(checkedDataHit, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    String arr = CommonUtils.getServerResponse(response);

                    try {
                        JSONObject jsonArr = new JSONObject(arr);
                        if (jsonArr.getString("Message").equals("Success")) {
                            Intent intent = new Intent(baseActivity, GoalsActivities.class);
                            intent.putExtra("nameActivity", "AllProduct");
                            intent.putExtra("backoncheckin", "true");
                            Log.d("successfully checkin", "checkined");
                            sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, String.valueOf(businessId));
                            sharedPreference.saveStringData(PrefsHelper.BUSINESS_CHECKED, "true");
                            checkedInStatus = true;
                            goalBusiness.setCheckedIN(true);
                            goalBusiness.save();
                            startActivity(intent);
                            baseActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                        } else {
                            Log.d("unsuccessful", "data not found in response of api ");
                            Toast.makeText(baseActivity, "Please check your Internet Connectivity", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        //   Toast.makeText(getContext(), "json data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    //Log.d("checkInApiCall Retrofit", "checkin error");
                    //  Toast.makeText(getContext(), "checkInApiCall Retrofit", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(ApplicationClass.getAppContext(), "Please do login again", Toast.LENGTH_SHORT).show();
        }
        CommonUtils.dismissProgress();

    }

    @OnClick(R.id.view_past_order)
    public void viewPastOrder() {
        Intent intent = new Intent(baseActivity, GoalsActivities.class);
        intent.putExtra("checkOrder", "checkOrder");
        intent.putExtra("nameActivity", "businessListFragment");
        startActivity(intent);
        baseActivity.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onBackPressed() {
        Intent intent = new Intent(baseActivity, GoalsActivities.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("nameActivity", "MyGoals");
        startActivity(intent);
        baseActivity.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void productClick(View v, int position) {

    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.v(LOG_TAG, "Receieved notification about network status");
            if (Connectivity.isNetworkAvailable(context)) {
                if (!isConnected) {
                    Log.v(LOG_TAG, "Now you are connected to Internet!");
                    isConnected = true;
                    sharedPreference.saveBooleanValue("IsCheckInPriOrderFrag", true);
                    //  Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                    if (!checkedInStatus) {
                        checkInApiCall();
                    }

                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);
                    //do your processing here ---
                    //if you need to post any data to the server or get status
                    //update from the server
                }
            } else {
                isConnected = false;
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
                                Log.v(LOG_TAG, "Now you are connected to Internet!");
                                isConnected = true;
                                sharedPreference.saveBooleanValue("IsCheckInPriOrderFrag", true);
                                //  Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                                if (!checkedInStatus) {
                                    checkInApiCall();
                                }
                                if (ApplicationClass.getAppContext() != null)
                                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);
                                //do your processing here ---
                                //if you need to post any data to the server or get status
                                //update from the server
                            }
                            return true;
                        }
                    }
                }
            }
//            if (!sharedPreference.getBooleanValue("IsCheckInPriOrderFrag")){
//                Toast.makeText(getActivity(),"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//
//            }
//            Log.v(LOG_TAG, "You are not connected to Internet!");
            // Toast.makeText(context, "not Connected", Toast.LENGTH_SHORT).show();
            isConnected = false;
            return false;
        }
    }
}
