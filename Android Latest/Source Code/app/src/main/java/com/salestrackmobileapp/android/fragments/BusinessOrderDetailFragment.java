package com.salestrackmobileapp.android.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.ItemRVAdapter;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.PendingOrderItem;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BusinessOrderDetailFragment extends BaseFragment implements RecyclerClick {

    int orderId, totalAmount;
    String businessName, orderDate;
    private static Dialog mProgressDialog;
    List<PendingOrderItem> pendingOrderItems = new ArrayList<>();

    ItemRVAdapter itemRVAdapter;
    private LinearLayoutManager mLayoutManager;

    RecyclerView recyclerView;
    Custome_BoldTextView businessNameTv;
    Custome_TextView dateOrderTv, totalAmt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_order_detail, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_items_order);
        businessNameTv = (Custome_BoldTextView) view.findViewById(R.id.business_name_tv);
        dateOrderTv = (Custome_TextView) view.findViewById(R.id.date_order_tv);
        totalAmt = (Custome_TextView) view.findViewById(R.id.total_amt);

        mLayoutManager = new LinearLayoutManager(baseActivity);

        itemRVAdapter = new ItemRVAdapter(getContext(), this);
        if (getArguments() != null) {
            orderId = getArguments().getInt("orderId");
            totalAmount = getArguments().getInt("totalAmount");
            businessName = getArguments().getString("businessName");
            orderDate = getArguments().getString("orderDate");
            businessNameTv.setText(businessName);
            dateOrderTv.setText(orderDate + "");
            totalAmt.setText("Total :" + totalAmount + "");
            getOrderItem();
        }

        return view;
    }


    public void getOrderItem() {
        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(baseActivity);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setContentView(R.layout.l_progress_view);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.getOrderItems(orderId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    String arr = CommonUtils.getServerResponse(response);
                    JSONArray jsonArray = new JSONArray(arr);

                    for (int j = 0; j < jsonArray.length(); j++) {
                        String jsonOrderItemSt = jsonArray.getJSONObject(0).toString();
                        PendingOrderItem pendingOrderItem = builder.fromJson(jsonOrderItemSt, PendingOrderItem.class);
                        pendingOrderItems.add(pendingOrderItem);
                    }
                    itemRVAdapter.setItemArray(pendingOrderItems);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(itemRVAdapter);

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
                if (error.getMessage().equals("timeout")) {

                    getOrderItem();
                }
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void productClick(View v, int position) {

    }


    public void onBackPressed() {
        Intent intent = new Intent(getContext(), GoalsActivities.class);
        intent.putExtra("nameActivity", "businessListFragment");
        startActivity(intent);
        // getFragmentManager().beginTransaction().replace(R.id.container,new BusinessListDateFragment()).commit();
    }
}
