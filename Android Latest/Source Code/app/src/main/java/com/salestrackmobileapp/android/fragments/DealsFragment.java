package com.salestrackmobileapp.android.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.ApplicationClass;
import com.salestrackmobileapp.android.activities.DashboardActivity;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.DealsAdapter;
import com.salestrackmobileapp.android.gson.AllBusiness;
import com.salestrackmobileapp.android.gson.AllDeal;
import com.salestrackmobileapp.android.gson.ProductList;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;

import org.json.JSONArray;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DealsFragment extends BaseFragment implements RecyclerClick {

    @BindView(R.id.deals_rv)
    RecyclerView dealsRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayoutManager mLayoutManager;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private ProgressDialog mProgressDialog;
    private DealsAdapter mAdapter;
    List<AllDeal> listAllDeal;
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    IntentFilter filter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deals, container, false);
        ButterKnife.bind(this, view);
        GoalsActivities.totalAmt.setVisibility(View.GONE);
//        GoalsActivities..setVisibility(View.GONE);
        mLayoutManager = new LinearLayoutManager(baseActivity);
        dealsRv.setLayoutManager(mLayoutManager);
        receiver = new NetworkChangeReceiver();
        dealsRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = dealsRv.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    loading = true;
                }
            }
        });
        mProgressDialog = new ProgressDialog(baseActivity);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();
        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ApplicationClass.getAppContext() != null) {
                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).registerReceiver(receiver, filter);
                }
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        getAllDealsArray();

        return view;
    }

    private void getAllDealsArray() {
        mAdapter = new DealsAdapter(baseActivity, this);

        if (Connectivity.isNetworkAvailable(baseActivity)) {
            sharedPreference.saveBooleanValue("IsAllDeals", true);
            showDialog();


            final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                    .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
            ServiceHandler serviceHandler = NetworkManager.createRetrofitService(baseActivity, ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
            serviceHandler.getAllDealsArray(new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    AllDeal.deleteAll(AllDeal.class);
                    ProductList.deleteAll(ProductList.class);

                    String arr = CommonUtils.getServerResponse(response);
//                    Log.e("DEAL_respobnse", ":::" + arr);
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

                        listAllDeal = AllDeal.listAll(AllDeal.class);
                        if (listAllDeal.size() != 0) {
                            mAdapter.setListDeal();
                            dealsRv.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                            hideDialog();
                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(baseActivity);
                            builder1.setMessage("No Deals are available");
                            builder1.setCancelable(false);
                            builder1.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Toast.makeText(getContext(), "Back is pressed", Toast.LENGTH_LONG).show();
                                    Intent intent2 = new Intent(baseActivity, DashboardActivity.class);
                                    startActivity(intent2);
                                    hideDialog();
                                }
                            });
                            builder1.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getAllDealsArray();
                                }
                            });
                            builder1.show();
                        }
                    } catch (Exception ex) {
                        hideDialog();
                        ex.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
//
                    listAllDeal = AllDeal.listAll(AllDeal.class);
                    if (listAllDeal.size() != 0) {
                        mAdapter.setListDeal();
                        dealsRv.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                        hideDialog();
                    } else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(baseActivity);
                        builder1.setMessage("No Deals are available");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Toast.makeText(getContext(), "Back is pressed", Toast.LENGTH_LONG).show();
                                Intent intent2 = new Intent(baseActivity, DashboardActivity.class);
                                startActivity(intent2);
                                hideDialog();
                            }
                        });
                        builder1.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getAllDealsArray();
                            }
                        });
                        builder1.show();
                    }
                }
            });
        } else {
//            if (!sharedPreference.getBooleanValue("IsAllDeals")){
//                Toast.makeText(getActivity(), "You must visit every screen once after first time login with internet connected.", Toast.LENGTH_SHORT).show();
//
//            }
//            else{

            listAllDeal = AllDeal.listAll(AllDeal.class);
            if (listAllDeal.size() != 0) {
                mAdapter.setListDeal();
                dealsRv.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hideDialog();
            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(baseActivity);
                builder1.setMessage("No Deals are available");
                builder1.setCancelable(false);
                builder1.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(getContext(), "Back is pressed", Toast.LENGTH_LONG).show();
                        Intent intent2 = new Intent(baseActivity, DashboardActivity.class);
                        startActivity(intent2);
                        hideDialog();
                    }
                });
                builder1.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getAllDealsArray();
                    }
                });
                builder1.show();
            }

        }

        // }
    }

    @Override
    public void productClick(View v, int position) {
        int itemPosition = dealsRv.getChildPosition(v);
        List<AllDeal> allDealList = AllDeal.listAll(AllDeal.class);
        AllDeal allDealObj = allDealList.get(itemPosition);
        String orderDeal = "Order";
        if (!orderDeal.equals(allDealObj.getDealType())) {
            Bundle bundle = new Bundle();
            bundle.putInt("dealId", allDealObj.getDealID());
            ListOfDealsProductFragment fragment = new ListOfDealsProductFragment();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(baseActivity);
            builder1.setTitle("this Deal can be applied on Order");
            builder1.setMessage("Do you want to continue order?");
            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(baseActivity, GoalsActivities.class);
                    //  sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, null);
                    intent.putExtra("nameActivity", "AllProduct");
                    startActivity(intent);
                    baseActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            });

            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder1.show();
        }

    }


    public void onBackPressed() {
        Intent intent = new Intent(baseActivity, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        baseActivity.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void showDialog() {

        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
//            Log.e("showDialog", ":::::true");


        }
    }

    public void hideDialog() {


        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
//            Log.e("hideDialog", ":::::true");


        }
    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.v("Goals getting", "Receieved notification about network status");
            if (Connectivity.isNetworkAvailable(context)) {
                if (!isConnected) {
                    Log.v("is network Available", "Now you are connected to Internet!");
                    isConnected = true;
                    sharedPreference.saveBooleanValue("IsAllBusinessfrag", true);
                    //Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                    if (Connectivity.isNetworkAvailable(baseActivity)) {

//                            Log.e("###", "::::::Async calling...");
                        getAllDealsArray();


                    }
                    if (ApplicationClass.getAppContext() != null)
                        LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);


                }
            } else {
                listAllDeal = AllDeal.listAll(AllDeal.class);
                if (listAllDeal.size() != 0) {
                    mAdapter.setListDeal();
                    dealsRv.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    hideDialog();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(baseActivity);
                    builder1.setMessage("No Deals are available");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Toast.makeText(getContext(), "Back is pressed", Toast.LENGTH_LONG).show();
                            Intent intent2 = new Intent(baseActivity, DashboardActivity.class);
                            startActivity(intent2);
                            hideDialog();
                        }
                    });
                    builder1.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getAllDealsArray();
                        }
                    });
                    builder1.show();
                }

            }
            isConnected = false;

        }
    }
}
