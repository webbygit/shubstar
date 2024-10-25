package com.salestrackmobileapp.android.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.ApplicationClass;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllBusiness;
import com.salestrackmobileapp.android.gson.CheckedDataHit;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.singleton.Singleton;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BusinessDetailFragment extends BaseFragment {

    @BindView(R.id.business_img)
    ImageView businessImg;
    @BindView(R.id.ttle_business_tv)
    Custome_BoldTextView businessNameTv;
    @BindView(R.id.contact_person_name_tv)
    Custome_TextView contactPersonName;
    @BindView(R.id.address_tv)
    Custome_TextView addressTv;
    @BindView(R.id.contact_number_tv)
    Custome_TextView contactNumber;
    AllBusiness businessObject;
    @BindView(R.id.check_in_btn)
    Button checkInBtn;
    @BindView(R.id.view_past_order)
    Button viewPastOrder;

    @BindView(R.id.notes_btn)
    Button notesBtn;
    CheckedDataHit checkedDataHit;
    Gson builder;
    ServiceHandler serviceHandler;
    static Boolean checkedInStatus = false;
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;

    int businessLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_detail, container, false);
        ButterKnife.bind(this, view);


        if (getArguments() != null) {
            businessObject = (AllBusiness) getArguments().getSerializable("BusinessObject");
            if (getArguments().containsKey("businessLoc")) {
                businessLocation = getArguments().getInt("businessLoc");
            } else {
            }
        }
        try {
            if (getArguments() != null) {
                if (getArguments().getString("state") == null) {
                    if (!sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID).equals("business_location")) {
                        List<AllBusiness> listGoal = AllBusiness.listAll(AllBusiness.class);
                        if (listGoal.size() != 0) {
                            int location = Integer.parseInt(sharedPreference.getStringValue(PrefsHelper.BUSINESS_LOCATION));
                            businessObject = listGoal.get(location);
                            checkedInStatus = businessObject.getCheckedIN();
                        }

                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (!sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID).equals("business_location")) {
                List<AllBusiness> listGoal = Select.from(AllBusiness.class).where(Condition.prop("business_id").eq(sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID))).list();
                if (listGoal.size() != 0) {
                    int location = Integer.parseInt(sharedPreference.getStringValue(PrefsHelper.BUSINESS_LOCATION));
                    if (listGoal.size() > location) {
                        businessObject = listGoal.get(location);
                        checkedInStatus = businessObject.getCheckedIN();
                    }
                }

            }
        }
        if (businessObject != null) {
            Picasso.with(getContext()).load(businessObject.getImageName()).placeholder(getContext().getResources().getDrawable(R.drawable.calendar_icon)).error(R.drawable.calendar_icon).into(businessImg);
            businessImg.setScaleType(ImageView.ScaleType.FIT_XY);
            businessNameTv.setText(businessObject.getBusnessName() + "");
            contactPersonName.setText(businessObject.getContactPersonName() + "(" + businessObject.getContactPersonEmail() + ") " + businessObject.getContactPersonPhone());
            contactNumber.setText(businessObject.getAddress1() + " " + businessObject.getAddress1() + " " + businessObject.getCity() + " " + businessObject.getState());
            checkedInStatus = businessObject.getCheckedIN();
        } else {
            Toast.makeText(ApplicationClass.getAppContext(), "No detail available", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    @OnClick((R.id.view_past_order))
    public void viewPastOrder() {

        Intent intent = new Intent(getContext(), GoalsActivities.class);
        intent.putExtra("nameActivity", "businessListFragment");
        startActivity(intent);
        //getFragmentManager().beginTransaction().replace(R.id.container,new BusinessListDateFragment()).commit();
    }

    @OnClick(R.id.check_in_btn)
    public void setCheckInBtn() {
        if (checkedInStatus) {
            ProductInCart.businessID = String.valueOf(businessObject.getBusinessID());
            if (ProductInCart.listAll(ProductInCart.class).size() != 0) {
                if (sharedPreference.getStringValue(PrefsHelper.NAVI_PRODUCT_BUSINESS).equals("product")) {
                    Toast.makeText(ApplicationClass.getAppContext(), "You have already checked in ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(baseActivity, GoalsActivities.class);
                    intent.putExtra("nameActivity", "AllProduct");
                    intent.putExtra("businesscheckin", "true");
                    Log.d("successfully checkin", "checkined");
                    startActivity(intent);
                } else {
                    FragmentTransaction ft = ((GoalsActivities) baseActivity).getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                    Bundle data = new Bundle();
                    data.putString("business_loc", String.valueOf(businessLocation));
                    /*sharedPreference.saveStringData(PrefsHelper.NAVI_PRODUCT_BUSINESS, "business");*/
                    CartFragment fragment = new CartFragment();
                    fragment.setArguments(data);
                    ft.replace(R.id.container, fragment);
                    ft.commit();
                }
            } else {
                Toast.makeText(ApplicationClass.getAppContext(), "You have already checked in ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(baseActivity, GoalsActivities.class);
                intent.putExtra("nameActivity", "AllProduct");
                intent.putExtra("businesscheckin", "true");
                Log.d("successfully checkin", "checkined");
                startActivity(intent);
            }
        } else {

            if (Connectivity.isNetworkAvailable(ApplicationClass.getAppContext())) {
                Log.e("NOt connection", "::::::");
                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                receiver = new NetworkChangeReceiver();
                if (ApplicationClass.getAppContext() != null)
                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).registerReceiver(receiver, filter);
                if (checkedInStatus) {
//                    Log.e("checkedInStatus","::::::true");
                    Toast.makeText(ApplicationClass.getAppContext(), "You have already checkin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(baseActivity, GoalsActivities.class);
                    intent.putExtra("nameActivity", "AllProduct");
                    intent.putExtra("backoncheckin", "true");
                    Log.d("successfully checkin", "checkined");
                    startActivity(intent);
                    if (baseActivity != null)
                        baseActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } else {
                    checkInApiCall();
                }
            } else {

//                Log.e("checkedInStatus","::::::false_else case");
                if (checkedInStatus) {
                    Toast.makeText(ApplicationClass.getAppContext(), "You have already checkin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(baseActivity, GoalsActivities.class);
                    intent.putExtra("nameActivity", "AllProduct");
                    intent.putExtra("backoncheckin", "true");
                    Log.d("successfully checkin", "checkined");
                    startActivity(intent);
                    baseActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } else {
//                    Log.e("checkedInStatus","::::::false");
                    Toast.makeText(ApplicationClass.getAppContext(), "You are not checked in,you need Internet connection for check in ", Toast.LENGTH_SHORT).show();


                }

            }
        }
    }


    @OnClick(R.id.notes_btn)
    public void noteFragmentCall() {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putSerializable("BusinessObject", businessObject);
        fragment.setArguments(args);
        if (baseActivity != null)
            baseActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    private void checkInApiCall() {
        if (Connectivity.isNetworkAvailable(ApplicationClass.getAppContext())) {
            builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                    .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
            serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
            try {
                JSONObject object = new JSONObject();
                object.put("UserCheckInId", "0");
                object.put("UserProfileID", sharedPreference.getStringValue(PrefsHelper.USER_ID));
                object.put("Longitude", String.valueOf(Singleton.instance.SLNG));
                object.put("Latitude", String.valueOf(Singleton.instance.SLAT));
                object.put("CheckInDate", "2017-04-13T06:54:04.543Z");
                object.put("BusinessID", businessObject.getBusinessID());

                checkedDataHit = builder.fromJson(object.toString(), CheckedDataHit.class);
                serviceHandler.checkedInSave(checkedDataHit, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        String arr = CommonUtils.getServerResponse(response);
                        sharedPreference.saveBooleanValue("IsBusinessDetailFrag", false);

                        try {
                            JSONObject jsonArr = new JSONObject(arr);
                            if (jsonArr.getString("Message").equals("Success")) {
                                ProductInCart.businessID = String.valueOf(businessObject.getBusinessID());

                                if (ProductInCart.listAll(ProductInCart.class).size() != 0) {

                                    if (sharedPreference.getStringValue(PrefsHelper.NAVI_PRODUCT_BUSINESS).equals("business")) {
                                        Toast.makeText(ApplicationClass.getAppContext(), "You have already checked in ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(baseActivity, GoalsActivities.class);
                                        intent.putExtra("nameActivity", "AllProduct");
                                        intent.putExtra("businesscheckin", "true");
                                        Log.d("successfully checkin", "checkined");
                                        startActivity(intent);

                                    } else {
                                        FragmentTransaction ft = ((GoalsActivities) baseActivity).getSupportFragmentManager().beginTransaction();
                                        ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                                        Bundle data = new Bundle();
                                        data.putString("moveto", "business");
                                        data.putString("business_loc", String.valueOf(businessLocation));
                                        // sharedPreference.saveStringData(PrefsHelper.NAVI_PRODUCT_BUSINESS, "business");
                                        CartFragment fragment = new CartFragment();
                                        fragment.setArguments(data);
                                        ft.replace(R.id.container, fragment);
                                        ft.commit();
                                    }
                           /*     FragmentTransaction ft = ((GoalsActivities) getContext()).getSupportFragmentManager().beginTransaction();
                                ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                                Bundle data = new Bundle();
                                data.putString("business_loc", String.valueOf(businessLocation));
                                data.putString("moveto", "business");
                                sharedPreference.saveStringData(PrefsHelper.NAVI_PRODUCT_BUSINESS, "business");
                                CartFragment fragment = new CartFragment();
                                fragment.setArguments(data);
                                ft.replace(R.id.container, fragment);
                                ft.commit();*/

                                } else {

                                    Intent intent = new Intent(baseActivity, GoalsActivities.class);
                                    intent.putExtra("nameActivity", "AllProduct");
                                    intent.putExtra("businesCheckIn", "true");
                                    Log.d("successfully checkin", "checkined");
                                    startActivity(intent);
                                    baseActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                                }
                                checkedInStatus = true;
                                businessObject.setCheckedIN(true);
                                businessObject.save();
                            } else {
                                Log.d("unsuccessful", "data not found in response of api ");
                                Toast.makeText(ApplicationClass.getAppContext(), "No data found", Toast.LENGTH_SHORT).show();
                            }

                            sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, String.valueOf(businessObject.getBusinessID()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // Toast.makeText(getContext(), "json data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        error.printStackTrace();
                        Toast.makeText(ApplicationClass.getAppContext(), "Please try again", Toast.LENGTH_SHORT).show();
                        CommonUtils.dismissProgress();
                        //Log.d("checkInApiCall Retrofit", "checkin error");
                        //  Toast.makeText(getContext(), "checkInApiCall Retrofit", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(ApplicationClass.getAppContext(), "Please do login again", Toast.LENGTH_SHORT).show();
            }
            CommonUtils.dismissProgress();
        } else {
            Toast.makeText(ApplicationClass.getAppContext(), "You need Internet connection for check-in", Toast.LENGTH_SHORT).show();
            CommonUtils.dismissProgress();

        }

    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.v("Business Detail", "Receieved notification about network status");
            if (Connectivity.isNetworkAvailable(context)) {
                if (!isConnected) {
                    Log.v("Business Detail", "Now you are connected to Internet!");
                    isConnected = true;
                    sharedPreference.saveBooleanValue("IsBusinessDetailFrag", true);
                    //  Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                    if (!checkedInStatus) {
                        checkInApiCall();
                    }
                    if (ApplicationClass.getAppContext() != null)
                        LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);
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
                                Log.v("Business Detail", "Now you are connected to Internet!");
                                isConnected = true;
                                sharedPreference.saveBooleanValue("IsBusinessDetailFrag", true);
                                //  Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                                if (!checkedInStatus) {
                                    checkInApiCall();
                                }
                                if (ApplicationClass.getAppContext() != null)
                                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);
                            }
                            return true;
                        }
                    }
                }
            }
//            if (!sharedPreference.getBooleanValue("IsBusinessDetailFrag")){
//                Toast.makeText(getActivity(),"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//
//            }
            Log.v("Business Detail", "You are not connected to Internet!");
            //(context, "not Connected", Toast.LENGTH_SHORT).show();
            isConnected = false;
            return false;
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(baseActivity, GoalsActivities.class);
        intent.putExtra("nameActivity", "AllBusiness");
        startActivity(intent);
    }
}

