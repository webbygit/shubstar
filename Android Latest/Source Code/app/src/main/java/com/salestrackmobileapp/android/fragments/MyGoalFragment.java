package com.salestrackmobileapp.android.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.ApplicationClass;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.MyGoalsAdapter;
import com.salestrackmobileapp.android.gson.CheckedDataHit;
import com.salestrackmobileapp.android.gson.GoalBusiness;
import com.salestrackmobileapp.android.gson.GoalBusinessBackup;
import com.salestrackmobileapp.android.gson.GoalsAccDate;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.salestrackmobileapp.android.utils.SelectDateFragment;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.salestrackmobileapp.android.activities.GoalsActivities.actionBarTitle;


public class MyGoalFragment extends BaseFragment implements RecyclerClick {

    @BindView(R.id.list_goals_rv)
    RecyclerView listGoalRv;

    @BindView(R.id.date_tv)
    TextView dateTV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    public static int yy, mm, dd;

    public static String dateForApiST;
    public static String dateForApi;
    public static String dateForOffline;
    List<GoalsAccDate> listAllGoals;
    List<Integer> goalIDlist;
    List<GoalBusiness> goalBusinessList;
    List<GoalBusinessBackup> goalBusinessBackupsList;
    private MyGoalsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int year;
    private int month;
    private int day;
    int itemPosition;

    Gson builder;
    ServiceHandler serviceHandler;
    CheckedDataHit checkedDataHit;
    GoalBusiness goalBusiness;
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    private ProgressDialog mProgressDialog;

    @BindView(R.id.null_value_layout)
    LinearLayout nullValueLayout;

    @BindView(R.id.inc_img)
    ImageView incImg;
    @BindView(R.id.dec_img)
    ImageView decImg;
    String currentDate;

    public static int flagTocallAllGoalsApi = 0;
    DialogFragment newFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_goal, container, false);
        ButterKnife.bind(this, view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        listGoalRv.setLayoutManager(mLayoutManager);
        listGoalRv.setNestedScrollingEnabled(false);


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);

        yy = year;
        mm = month;
        dd = day;

        String format = new SimpleDateFormat("MMM d, yyyy").format(cal.getTime());
        dateForApiST = new SimpleDateFormat("MMM d, yyyy").format(cal.getTime());
        dateForApi = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());
        dateForOffline = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        dateTV.setText(format + "");

        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                newFragment = new SelectDateFragment(dateTV, MyGoalFragment.this);
                newFragment.show(getFragmentManager(), "DatePicker");

                //getAllGoalsAccDate();
            }
        });
        currentDate = new SimpleDateFormat("MMM d, yyyy").format(cal.getTime());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllGoalsAccDate();
            }
        });
        if (!isAllGoals) {
            getAllGoalsAccDate();
        }
        incImg.setEnabled(true);
       /* IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(receiver, filter);*/


        return view;
    }


    @Override
    public void productClick(View v, int position) {
        goalBusiness = goalBusinessList.get(listGoalRv.getChildPosition(v));
        itemPosition = listGoalRv.getChildPosition(v);
        actionBarTitle.setText("BUSINESS");
        CheckInPriOrderFragment fragment = new CheckInPriOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("goal_position", itemPosition);
        bundle.putSerializable("goal_object", goalBusiness);
        bundle.putSerializable("business_id", goalBusiness.getBusinessID());

        sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, String.valueOf(goalBusiness.getBusinessID()));
        sharedPreference.saveStringData(PrefsHelper.GOAL_LOCATION, String.valueOf(itemPosition));
        fragment.setArguments(bundle);
        FragmentTransaction ft = ((GoalsActivities) baseActivity).getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    boolean isAllGoals = false;


    public void getAllGoalsAccDate() {
        isAllGoals = true;
        mProgressDialog = new ProgressDialog(baseActivity);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();
//        Log.e("###NETWORK","::::::::::"+ Connectivity.isNetworkAvailable(baseActivity));
        mAdapter = new MyGoalsAdapter(baseActivity, this);
        if (Connectivity.isNetworkAvailable(baseActivity)) {
            showDialog();


            builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                    .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
            serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
            String dateString = dateForApi;

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("HH:mm a");

            date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

            String localTime = date.format(currentLocalTime);

            GoalsAccDate.deleteAll(GoalsAccDate.class);
            GoalBusiness.deleteAll(GoalBusiness.class);
            serviceHandler.getGoalsAccDate(dateString, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    String arr = CommonUtils.getServerResponse(response);
                    GoalsAccDate.deleteAll(GoalsAccDate.class);
                    GoalBusiness.deleteAll(GoalBusiness.class);

//                    Log.e("RETROFIT",":Response:::::"+response.toString());
//                    Log.e("RETROFIT",":Response2:::::"+response2.getUrl());

                    try {
                        String jsonString = arr.replace("null", "\"\"");
                        JSONArray jsonArr = new JSONArray(jsonString);
//                        Log.e("JSON",":::::"+jsonArr.toString());
                        if (jsonArr.length() == 0) {
                            if (listAllGoals != null) {
                                if (listAllGoals.size() != 0) {
                                    listAllGoals.clear();
                                }
                            }
                        }
//            Log.e("ARRAy_LENGTH","::::"+jsonArr.length());
                        for (int i = 0; i < jsonArr.length(); i++) {
                            GoalsAccDate allGoalsDate = builder.fromJson(jsonArr.get(i).toString(), GoalsAccDate.class);
                            JSONObject jsonObject = (JSONObject) jsonArr.get(i);
                            JSONArray jsonArray = jsonObject.getJSONArray("BusinessMapping");


//                            Log.e("BusinessMapping", "::::" + allGoalsDate.getBusinessMapping().get(0).getBusiness().getBusnessName());

                            if (jsonArray.length() != 0) {
                                sharedPreference.saveBooleanValue("IsMyGoalFrag", true);
                                listGoalRv.setVisibility(View.VISIBLE);
                                nullValueLayout.setVisibility(View.GONE);
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject json = jsonArray.getJSONObject(j);

                                    GoalBusiness goalbusiness = builder.fromJson(json.getJSONObject("Business").toString(), GoalBusiness.class);
                                    goalbusiness.setGoalBusinessDate(dateForApi);

                                    goalbusiness.save();
                                    GoalBusinessBackup goalbusinessbackup = builder.fromJson(json.getJSONObject("Business").toString(), GoalBusinessBackup.class);
                                    goalbusinessbackup.setGoalBusinessDate(dateForApi);
//                                    Log.e("GOAL_ID_ONLINE","::::::"+allGoalsDate.getDailyGoalID());
                                    goalbusinessbackup.setDailyGoalID(allGoalsDate.getDailyGoalID());
                                    goalbusinessbackup.save();
                                }


                                allGoalsDate.save();


                            } else {
                                nullValueLayout.setVisibility(View.VISIBLE);
                                listGoalRv.setVisibility(View.GONE);

                            }
                            listAllGoals = GoalsAccDate.listAll(GoalsAccDate.class);
                            goalBusinessList = GoalBusiness.listAll(GoalBusiness.class);


                        }

                        listAllGoals = GoalsAccDate.listAll(GoalsAccDate.class);
                        goalBusinessList = GoalBusiness.listAll(GoalBusiness.class);
                        // Log.e("LIST_goals",":::Business::::"+listAllGoals.get(0).toString());

                        for (int p = 0; p < goalBusinessList.size(); p++) {
//                            if (goalBusinessList.get(p).getGoalBusinessDate() != null)
//                                Log.e("getGoalBusinessDate",":::getGoalBusinessDate::::"+goalBusinessList.get(p).getGoalBusinessDate());
                        }


                        if (listAllGoals != null) {
                            if (listAllGoals.size() != 0) {
                                mAdapter.setListGoals();
                                listGoalRv.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                                if (mProgressDialog != null && mProgressDialog.isShowing())
                                    mProgressDialog.dismiss();
                            } else {
                                listGoalRv.setVisibility(View.GONE);
                                nullValueLayout.setVisibility(View.VISIBLE);
                      /*      AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                            builder1.setMessage("No goals are available");
                            builder1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    nullValueLayout.setVisibility(View.VISIBLE);
                                    listGoalRv.setVisibility(View.GONE);
                                    if (mProgressDialog != null && mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();

                                }
                            });
                            builder1.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    Toast.makeText(getContext(), "Back is pressed", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                    startActivity(intent);
                                    if (mProgressDialog != null && mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                }
                            });
                            builder1.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getAllGoalsAccDate();

                                }
                            });
                            builder1.show();*/
                                hideDialog();
                            }
                        } else {

                            listGoalRv.setVisibility(View.GONE);
                            nullValueLayout.setVisibility(View.VISIBLE);
                            hideDialog();
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        hideDialog();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    try {
                        if (error.getMessage().equals("timeout")) {
                            getAllGoalsAccDate();
                        } else {
//                            Toast.makeText(getActivity(), "Goals not found", Toast.LENGTH_SHORT).show();

                            GoalBusiness.deleteAll(GoalBusiness.class);
//            if (!sharedPreference.getBooleanValue("IsMyGoalFrag")) {
//                Toast.makeText(getActivity(), "You must visit every screen once after first time login with internet connected.", Toast.LENGTH_SHORT).show();
//
//            }
                            if (listAllGoals != null) {
                                if (listAllGoals.size() != 0) {
                                    listAllGoals.clear();
                                }
                            }
                            listGoalRv.setVisibility(View.VISIBLE);
                            nullValueLayout.setVisibility(View.GONE);
//                            Log.e("date","::::::"+dateForOffline);
                            try {

                                listAllGoals = Select.from(GoalsAccDate.class).where(Condition.prop("Date").eq(dateForOffline + "T00:00:00")).list();
                            } catch (Exception e) {
//                                Log.e("Exception","::::"+e.toString());
                            }

//                            Log.e("listAllGoals","::::::"+listAllGoals.size());
                            if (GoalBusinessBackup.listAll(GoalBusinessBackup.class).size() > 0) {
                                try {
                                    goalBusinessBackupsList = Select.from(GoalBusinessBackup.class).where(Condition.prop("Date").eq(dateForOffline)).list();
                                    //goalBusinessBackupsList =GoalBusinessBackup.listAll(GoalBusinessBackup.class);
                                    goalIDlist = new ArrayList<>();
                                    for (int j = 0; j < goalBusinessBackupsList.size(); j++) {


                                        GoalBusinessBackup goalbusiness = goalBusinessBackupsList.get(j);

                                        if (goalIDlist.contains(goalbusiness.getDailyGoalID())) {
//                                        Log.e("GOAL_ID_IF", "::::::" + goalbusiness.getDailyGoalID());
                                        } else {
                                            goalIDlist.add(goalbusiness.getDailyGoalID());
//                                        Log.e("GOAL_ID_ELSE", "::::::" + goalbusiness.getDailyGoalID());
//                                        Log.e("getBusnessNameDATE", "::::::" + goalbusiness.getGoalBusinessDate());
                                            GoalBusiness goalBusiness = new GoalBusiness();
                                            goalBusiness.setGoalBusinessDate(goalbusiness.getGoalBusinessDate());
                                            goalBusiness.setAddress1(goalbusiness.getAddress1());
                                            goalBusiness.setAddress2(goalbusiness.getAddress2());
                                            goalBusiness.setBusnessName(goalbusiness.getBusnessName());
                                            goalBusiness.setCheckedIN(goalbusiness.getCheckedIN());
                                            goalBusiness.setCity(goalbusiness.getCity());
                                            goalBusiness.setContactPersonEmail(goalbusiness.getContactPersonEmail());
                                            goalBusiness.setContactPersonName(goalbusiness.getContactPersonName());
                                            goalBusiness.setContactPersonPhone(goalbusiness.getContactPersonPhone());
                                            goalBusiness.setCountry(goalbusiness.getCountry());
                                            goalBusiness.setBusinessID(goalbusiness.getBusinessID());
                                            goalBusiness.setDefaultGoalbusinessID(goalbusiness.getDefaultGoalbusinessID());
                                            goalBusiness.setImageName(goalbusiness.getImageName());
                                            goalBusiness.setState(goalbusiness.getState());
                                            goalBusiness.setZipCode(goalbusiness.getZipCode());
                                            goalBusiness.setId(goalbusiness.getId());
//                                        goalBusiness.setBusinesstype(goalbusiness.getBu);

                                            goalBusiness.save();
                                        }

                                    }
                                } catch (Exception e) {
//                                    Log.e("Exception","::::"+e.toString());
                                }
                            }

                            if (goalBusinessBackupsList != null) {
                                if (goalBusinessBackupsList.size() != 0) {
                                    mAdapter.setListGoals();
                                    listGoalRv.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                    if (mProgressDialog != null && mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                } else {
                                    listGoalRv.setVisibility(View.GONE);
                                    nullValueLayout.setVisibility(View.VISIBLE);

                                }

                            } else {
                                listGoalRv.setVisibility(View.GONE);
                                nullValueLayout.setVisibility(View.VISIBLE);
                            }
                            swipeRefreshLayout.setRefreshing(false);

                        }
                        hideDialog();
//                    getAllGoalsAccDate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            swipeRefreshLayout.setRefreshing(false);

        } else {
            GoalBusiness.deleteAll(GoalBusiness.class);
//            if (!sharedPreference.getBooleanValue("IsMyGoalFrag")) {
//                Toast.makeText(getActivity(), "You must visit every screen once after first time login with internet connected.", Toast.LENGTH_SHORT).show();
//
//            }
            if (listAllGoals != null) {
                if (listAllGoals.size() != 0) {
                    listAllGoals.clear();
                }
            }
            listGoalRv.setVisibility(View.VISIBLE);
            nullValueLayout.setVisibility(View.GONE);
//            Log.e("date","::::::"+dateForOffline);
            try {

                listAllGoals = Select.from(GoalsAccDate.class).where(Condition.prop("Date").eq(dateForOffline + "T00:00:00")).list();
//                Log.e("dateForApi", "::::" + dateForApi);

//                Log.e("listAllGoals", "::::::" + listAllGoals.size());
            } catch (Exception e) {
//                Log.e("Exception","::::"+e.toString());
            }
            if (GoalBusinessBackup.listAll(GoalBusinessBackup.class).size() > 0) {
                try {

                    goalBusinessBackupsList = Select.from(GoalBusinessBackup.class).where(Condition.prop("Date").eq(dateForOffline)).list();
                    //goalBusinessBackupsList =GoalBusinessBackup.listAll(GoalBusinessBackup.class);
                    goalIDlist = new ArrayList<>();
                    for (int j = 0; j < goalBusinessBackupsList.size(); j++) {


                        GoalBusinessBackup goalbusiness = goalBusinessBackupsList.get(j);

                        if (goalIDlist.contains(goalbusiness.getDailyGoalID())) {
//                            Log.e("GOAL_ID_IF", "::::::" + goalbusiness.getDailyGoalID());
                        } else {
                            goalIDlist.add(goalbusiness.getDailyGoalID());
//                            Log.e("GOAL_ID_ELSE", "::::::" + goalbusiness.getDailyGoalID());
//                            Log.e("getBusnessNameDATE", "::::::" + goalbusiness.getGoalBusinessDate());
                            GoalBusiness goalBusiness = new GoalBusiness();
                            goalBusiness.setGoalBusinessDate(goalbusiness.getGoalBusinessDate());
                            goalBusiness.setAddress1(goalbusiness.getAddress1());
                            goalBusiness.setAddress2(goalbusiness.getAddress2());
                            goalBusiness.setBusnessName(goalbusiness.getBusnessName());
                            goalBusiness.setCheckedIN(goalbusiness.getCheckedIN());
                            goalBusiness.setCity(goalbusiness.getCity());
                            goalBusiness.setContactPersonEmail(goalbusiness.getContactPersonEmail());
                            goalBusiness.setContactPersonName(goalbusiness.getContactPersonName());
                            goalBusiness.setContactPersonPhone(goalbusiness.getContactPersonPhone());
                            goalBusiness.setCountry(goalbusiness.getCountry());
                            goalBusiness.setBusinessID(goalbusiness.getBusinessID());
                            goalBusiness.setDefaultGoalbusinessID(goalbusiness.getDefaultGoalbusinessID());
                            goalBusiness.setImageName(goalbusiness.getImageName());
                            goalBusiness.setState(goalbusiness.getState());
                            goalBusiness.setZipCode(goalbusiness.getZipCode());
                            goalBusiness.setId(goalbusiness.getId());

                            goalBusiness.save();
                        }

                    }
                    //
                } catch (Exception e) {
//                    Log.e("Exception","::::"+e.toString());
                }
            }

            if (goalBusinessBackupsList != null) {
                if (goalBusinessBackupsList.size() != 0) {
                    mAdapter.setListGoals();
                    listGoalRv.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    hideDialog();
                } else {
                    listGoalRv.setVisibility(View.GONE);
                    nullValueLayout.setVisibility(View.VISIBLE);

                }

            } else {
                listGoalRv.setVisibility(View.GONE);
                nullValueLayout.setVisibility(View.VISIBLE);
            }
            hideDialog();
            swipeRefreshLayout.setRefreshing(false);


        }
    }

    @OnClick(R.id.inc_img)
    public void incDate() {
        if (currentDate.equals(dateForApiST)) {

        } else {
            SelectDateFragment.incDateByOne();
            dateTV.setText(dateForApiST + "");
            getAllGoalsAccDate();
        }
    }

    @OnClick(R.id.dec_img)
    public void decDate() {


        SelectDateFragment.decDateByOne();
        dateTV.setText(dateForApiST + "");
        getAllGoalsAccDate();
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.v("Goals getting", "Receieved notification about network status");
            if (Connectivity.isNetworkAvailable(context)) {
                if (!isConnected) {
                    Log.v("is network Available", "Now you are connected to Internet!");
                    isConnected = true;
                    sharedPreference.saveBooleanValue("IsMyGoalFrag", true);
                    //  Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                    if (!isAllGoals) {
                        getAllGoalsAccDate();
                    }
//                                if (getContext()!=null)
//                                    LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);                                //do your processing here ---
                    //if you need to post any data to the server or get status
                    //update from the server
                }
            } else {
                if (!isAllGoals) {
                    getAllGoalsAccDate();
                }
//            if (!sharedPreference.getBooleanValue("IsMyGoalFrag")){
//                Toast.makeText(getActivity(),"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//
//            }
                Log.v("not connected", "You are not connected to Internet!");
                //  Toast.makeText(context, "not Connected", Toast.LENGTH_SHORT).show();
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
                                Log.v("is network Available", "Now you are connected to Internet!");
                                isConnected = true;
                                sharedPreference.saveBooleanValue("IsMyGoalFrag", true);
                                //  Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                                if (!isAllGoals) {
                                    getAllGoalsAccDate();
                                }
//                                if (getContext()!=null)
//                                    LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);                                //do your processing here ---
                                //if you need to post any data to the server or get status
                                //update from the server
                            }
                            return true;
                        }
                    }
                }
            }
            if (!isAllGoals) {
                getAllGoalsAccDate();
            }
//            if (!sharedPreference.getBooleanValue("IsMyGoalFrag")){
//                Toast.makeText(getActivity(),"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//
//            }
            Log.v("not connected", "You are not connected to Internet!");
            //  Toast.makeText(context, "not Connected", Toast.LENGTH_SHORT).show();
            isConnected = false;
            return false;
        }
    }

    public void showDialog() {

        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
//            Log.e("showDialog",":true");


        }
    }

    public void hideDialog() {


        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
//            Log.e("hideDialog",":true");


        }
    }

}
