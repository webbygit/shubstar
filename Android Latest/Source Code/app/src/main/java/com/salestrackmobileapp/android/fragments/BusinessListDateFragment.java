package com.salestrackmobileapp.android.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.ApplicationClass;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.OrderHistoryAccDateAdapter;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.Business;
import com.salestrackmobileapp.android.gson.BusinessOrder;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.salestrackmobileapp.android.activities.GoalsActivities.actionBarTitle;


public class BusinessListDateFragment extends BaseFragment implements RecyclerClick {

    RecyclerView preOrderDetail;
    Custome_TextView title_txt;
    private static Dialog mProgressDialog;

    private OrderHistoryAccDateAdapter mAdapter;
    List<Business> listBusiness;
    List<BusinessOrder> businessOrderList;
    private LinearLayoutManager mLayoutManager;
    String checkPriOrder = "";

    /* @BindView(R.id.null_value_layout)*/
    LinearLayout nullValueLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_list_date, container, false);
        preOrderDetail = (RecyclerView) view.findViewById(R.id.listOrderRv);
        nullValueLayout = (LinearLayout) view.findViewById(R.id.null_value_layout);
        title_txt = (Custome_TextView) view.findViewById(R.id.title_txt);
        GoalsActivities.homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
        //  getDate();
        gerOrderAccordingBusinessId();
        mLayoutManager = new LinearLayoutManager(baseActivity);
        mAdapter = new OrderHistoryAccDateAdapter(baseActivity, this);
        preOrderDetail.setLayoutManager(mLayoutManager);

        if (getArguments() != null) {

            if (getArguments().getString("checkOrder") == null) {
                checkPriOrder = "";
            } else {
                checkPriOrder = getArguments().getString("checkOrder");
            }
        }

        return view;
    }

    public void gerOrderAccordingBusinessId() {

        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(baseActivity);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setContentView(R.layout.l_progress_view);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        BusinessOrder.deleteAll(BusinessOrder.class);
        if (Business.listAll(Business.class).size() != 0) {
            Business.deleteAll(Business.class);
        }
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Calendar cal = Calendar.getInstance();
        String startDate = dateFormat.format(cal.getTime());
//        Log.e("@@@","::startDate:"+startDate);


        //  Toast.makeText(getContext(), "" + dateFormat.format(cal.getTime()), Toast.LENGTH_SHORT).show();
        Calendar calReturn = Calendar.getInstance();
        calReturn.add(Calendar.DATE, -30);
        String endDate = dateFormat.format(calReturn.getTime());
//        Log.e("@@@","::endDate:"+endDate);


        //  Toast.makeText(getContext(), "" + dateFormat.format(calReturn.getTime()), Toast.LENGTH_SHORT).show();
//        Log.e("@@@",":::BusinessID"+sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID));
//        Log.e("@@@",":::USER_ID"+sharedPreference.getStringValue(PrefsHelper.USER_ID));
        serviceHandler.getBusinessAccDate(sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID), sharedPreference.getStringValue(PrefsHelper.USER_ID), endDate, startDate, "approved", new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                if (BusinessOrder.listAll(BusinessOrder.class).size() != 0) {
                    BusinessOrder.deleteAll(BusinessOrder.class);
                }
                String arr = CommonUtils.getServerResponse(response);
//                Log.e("@@@","::::array"+arr);
                listBusiness = new ArrayList<Business>();
                try {
                    JSONArray jsonArr = new JSONArray(arr);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObject = jsonArr.getJSONObject(i);
                        BusinessOrder businessOrder = builder.fromJson(String.valueOf(jsonArr.getJSONObject(0)), BusinessOrder.class);
                        Business business = builder.fromJson(jsonObject.getJSONObject("Business").toString(), Business.class);
                        businessOrder.save();
                        business.save();
                    }

                    businessOrderList = BusinessOrder.listAll(BusinessOrder.class);

                    if (businessOrderList.size() != 0) {

                        preOrderDetail.setVisibility(View.VISIBLE);
                        nullValueLayout.setVisibility(View.GONE);
                        mAdapter.setSaveOrderList(businessOrderList);
                        preOrderDetail.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                    } else {
                        preOrderDetail.setVisibility(View.GONE);
                        nullValueLayout.setVisibility(View.VISIBLE);
                       /* AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("No history are available");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Toast.makeText(getContext(), "Back is pressed", Toast.LENGTH_LONG).show();
                                if (mProgressDialog != null && mProgressDialog.isShowing())
                                    mProgressDialog.dismiss();
                            }
                        });
                        builder1.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                gerOrderAccordingBusinessId();
                            }
                        });
                        builder1.show();*/
                    }
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                if (error.getMessage().equals("timeout")) {

                    gerOrderAccordingBusinessId();
                }
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });

    }

    public void getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Calendar cal = Calendar.getInstance();
        Calendar calReturn = Calendar.getInstance();
        title_txt.setText(dateFormat.format(cal.getTime()));
        // Toast.makeText(getContext(), "" + dateFormat.format(cal.getTime()), Toast.LENGTH_SHORT).show();
        calReturn.add(Calendar.DATE, 30);
        title_txt.setText(dateFormat.format(calReturn.getTime()));
        // Toast.makeText(getContext(), "" + dateFormat.format(calReturn.getTime()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void productClick(View v, int position) {
        BusinessOrder businessOrder = businessOrderList.get(position);
        int itemPosition = preOrderDetail.getChildPosition(v);
        Intent intent = new Intent(baseActivity, GoalsActivities.class);
        intent.putExtra("nameActivity", "businessOrderhistroryDetail");
        if (businessOrder.getOrderID() == 0) {
            intent.putExtra("orderId", businessOrder.getOrderID());
        } else {


            intent.putExtra("orderId", businessOrder.getOrderID());
            intent.putExtra("businessName", businessOrder.getBusinessName());
            intent.putExtra("totalAmount", businessOrder.getTotalOrderValue());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
            try {
                dateFormat.parse(businessOrder.getOrderDate() + "");
                intent.putExtra("orderDate", dateFormat.parse(businessOrder.getOrderDate() + ""));
            } catch (ParseException e) {
                e.printStackTrace();
                intent.putExtra("orderDate", businessOrder.getOrderDate() + "");
            }

            startActivity(intent);
        }
    }


    public void onBackPressed() {
        if (checkPriOrder.equals("checkOrder")) {
            actionBarTitle.setText("BUSINESS");
            CheckInPriOrderFragment fragment = new CheckInPriOrderFragment();
            Bundle bundle = new Bundle();

            fragment.setArguments(bundle);
            FragmentTransaction ft = ((GoalsActivities) baseActivity).getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            ft.replace(R.id.container, fragment);
            ft.commit();
        } else {
            //getFragmentManager().beginTransaction().replace(R.id.container, new BusinessDetailFragment()).commit();
            actionBarTitle.setText("BUSINESS");
            CheckInPriOrderFragment fragment = new CheckInPriOrderFragment();
            Bundle bundle = new Bundle();

            fragment.setArguments(bundle);
            FragmentTransaction ft = ((GoalsActivities) baseActivity).getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            ft.replace(R.id.container, fragment);
            ft.commit();
        }

    }
}
