package com.salestrackmobileapp.android.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.ApplicationClass;
import com.salestrackmobileapp.android.activities.DashboardActivity;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.OrderHistoryAdapter;
import com.salestrackmobileapp.android.adapter.PendingOrderHistoryAdapter;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.Business;
import com.salestrackmobileapp.android.gson.GoalBusiness;
import com.salestrackmobileapp.android.gson.OrderHistory;
import com.salestrackmobileapp.android.gson.OrderItem;
import com.salestrackmobileapp.android.gson.PendingOrderItem;
import com.salestrackmobileapp.android.gson.SaveOrder;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.RecyclerClickInterface;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OrderHistoryFragment extends BaseFragment implements RecyclerClickInterface, SearchView.OnQueryTextListener {
    // TODO: Rename parameter arguments, choose names that match

    @BindView(R.id.order_history_items)
    RecyclerView orderHistoryProductRV;

    @BindView(R.id.order_pending_items)
    RecyclerView orderPendingProductRV;

    @BindView(R.id.pending_title)
    TextView pendingValuetxt;
    SearchView mSearchView;

    @BindView(R.id.null_value_txt)
    Custome_TextView nullValuetxt;
    List<SaveOrder> saveOrderProduct = new ArrayList<SaveOrder>();

    private LinearLayoutManager mLayoutManager, layoutManager;
    PendingOrderHistoryAdapter pendingOrderHistoryAdapter;
    OrderHistoryAdapter orderHistoryAdapter;

    private ProgressDialog mProgressDialog;
    Gson builder;
    ServiceHandler serviceHandler;

    List<OrderHistory> orderHistoriesList;
    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);
        ButterKnife.bind(this, view);
        mSearchView = (SearchView) view.findViewById(R.id.searchview);
        EditText searchEditText = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.grey));

//
//        if (mProgressDialog == null) {
//            mProgressDialog = new Dialog(getActivity());
//            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            mProgressDialog.setContentView(R.layout.l_progress_view);
//            mProgressDialog.setCanceledOnTouchOutside(false);
//            mProgressDialog.show();
//        }

        mProgressDialog = new ProgressDialog(baseActivity);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();

        saveOrderProduct = Select.from(SaveOrder.class).where(Condition.prop("send_to_server").eq("0")).list();


        mLayoutManager = new LinearLayoutManager(baseActivity);
        layoutManager = new LinearLayoutManager(baseActivity);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        pendingOrderHistoryAdapter = new PendingOrderHistoryAdapter(baseActivity, this, orderPendingProductRV);
        orderPendingProductRV.setLayoutManager(mLayoutManager);
        orderHistoryAdapter = new OrderHistoryAdapter(baseActivity, this, orderHistoryProductRV);
        orderHistoryProductRV.setLayoutManager(layoutManager);

        pendingOrderHistoryAdapter.setSaveOrderList(saveOrderProduct);
        orderPendingProductRV.setAdapter(pendingOrderHistoryAdapter);


        if (saveOrderProduct.size() != 0) {
            nullValuetxt.setVisibility(View.GONE);
            orderPendingProductRV.setVisibility(View.VISIBLE);
            pendingValuetxt.setVisibility(View.VISIBLE);
        } else {
            nullValuetxt.setVisibility(View.VISIBLE);
            orderPendingProductRV.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            orderHistoryProductRV.setLayoutParams(lp);
        }

        getOrderHistoryArray();
        if (orderHistoriesList != null) {
            if (orderHistoriesList.size() == 0 || saveOrderProduct.size() == 0) {
                nullValuetxt.setVisibility(View.VISIBLE);
            }
        }

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        if (ApplicationClass.getAppContext() != null)
            LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).registerReceiver(receiver, filter);
        setupSearchView();

//        view.setFocusableInTouchMode(true);
//        view.setOnKeyListener( new View.OnKeyListener(){
//            @Override
//            public boolean onKey( View v, int keyCode, KeyEvent event ){
//                if( keyCode == KeyEvent.KEYCODE_BACK ){
//                    Log.e("#####View","onbackpressed");
//                    return true;
//                }
//                return false;
//            }
//        } );


        return view;
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setFocusableInTouchMode(true);
        mSearchView.setQueryHint("Search Business...");
//        mSearchView.requestFocusFromTouch();

    }

    @Override
    public void productClick(View v, int position, RecyclerView recyclerView) {
        if (recyclerView.equals(orderPendingProductRV)) {

            int itemPosition = orderPendingProductRV.getChildPosition(v);
            SaveOrder saveOrder = saveOrderProduct.get(itemPosition);
            Intent intent = new Intent(baseActivity, GoalsActivities.class);
            intent.putExtra("nameActivity", "orderPendingDetailFragment");
            intent.putExtra("lorderNumber", Integer.parseInt(saveOrder.getOrderNo()));
            intent.putExtra("orderPosition", itemPosition);
            intent.putExtra("saveorderObj", saveOrder);
            startActivity(intent);
        } else if (recyclerView.equals(orderHistoryProductRV)) {
            int itemPosition = orderHistoryProductRV.getChildPosition(v);
            OrderHistory orderHistory = orderHistoriesList.get(itemPosition);
            Intent intent = new Intent(baseActivity, GoalsActivities.class);
            intent.putExtra("nameActivity", "orderDetailFragment");
            intent.putExtra("orderId", orderHistory.getOrderID());
            intent.putExtra("orderhistoryObject", orderHistory);
            intent.putExtra("orderPosition", itemPosition);

            startActivity(intent);
        }
    }


    public void onBackPressed() {
//        Log.e("OnBAckPressed",":::::True");
        Intent intent = new Intent(baseActivity, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        baseActivity.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    ArrayList<Business> businesslist = new ArrayList<>();

    public void getOrderHistoryArray() {

        if (Connectivity.isNetworkAvailable(baseActivity)) {
            if (OrderHistory.listAll(OrderHistory.class).size() != 0) {
                OrderHistory.deleteAll(OrderHistory.class);
            }
            if (Business.listAll(Business.class).size() != 0) {
                Business.deleteAll(Business.class);
            }
            showDialog();
            builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                    .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
            serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
            serviceHandler.getOrderHistory("NEW", new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    String arr = CommonUtils.getServerResponse(response);
                    sharedPreference.saveBooleanValue("IsOrderHistoryFrag", true);
                    try {
                        String jsonString = arr.replace("null", "\"\"");
//                        Log.e("OrderJSON",":::::OrderHistory::::"+jsonString);
                        JSONArray jsonArr = new JSONArray(jsonString);
                        int count = jsonArr.length() - 1;

                        for (int i = 0; i < jsonArr.length(); i++) {
                            OrderHistory orderHistory = builder.fromJson(jsonArr.get(i).toString(), OrderHistory.class);
                            List<OrderItem> list = orderHistory.getPendingOrderItems();
                            JSONObject jsonObject = (JSONObject) jsonArr.get(i);

                            Business business = builder.fromJson(jsonObject.getJSONObject("Business").toString(), Business.class);
                            orderHistory.setOrderPositions(count);
                            orderHistory.save();
                            business.save();
                            count--;
                        }
                        orderHistoriesList = Select.from(OrderHistory.class).orderBy("id Desc").list();

                        if (orderHistoriesList.size() != 0) {
                            orderHistoryProductRV.setVisibility(View.VISIBLE);
                            orderHistoryAdapter.setSaveOrderList(orderHistoriesList);
                            orderHistoryProductRV.setAdapter(orderHistoryAdapter);
                            orderHistoryAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(baseActivity, "No server order history available", Toast.LENGTH_SHORT).show();
                            hideDialog();
                        }

                        orderHistoryAdapter.notifyDataSetChanged();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideDialog();
                            }
                        }, 1500);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        hideDialog();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
//                    Log.e("Exception","::::"+error.toString());
                    try {
                        // Toast.makeText(getActivity(), "History not found", Toast.LENGTH_SHORT).show();
//                        if (mProgressDialog != null && mProgressDialog.isShowing())
//                            mProgressDialog.dismiss();

                        orderHistoriesList = Select.from(OrderHistory.class).orderBy("id Desc").list();
                        if (orderHistoriesList.size() != 0) {
                            orderHistoryProductRV.setVisibility(View.VISIBLE);
                            orderHistoryAdapter.setSaveOrderList(orderHistoriesList);
                            orderHistoryProductRV.setAdapter(orderHistoryAdapter);
                            orderHistoryAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(baseActivity, "No server order history available", Toast.LENGTH_SHORT).show();
                            hideDialog();
                        }
                        hideDialog();

                        orderHistoryAdapter.notifyDataSetChanged();


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } else {

//            Log.e("ELSE_api","::::::");
//            if (!sharedPreference.getBooleanValue("IsOrderHistoryFrag")){
//                Toast.makeText(getActivity(),"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//
//            }
            //  else{


            orderHistoriesList = Select.from(OrderHistory.class).orderBy("id Desc").list();
            if (orderHistoriesList.size() != 0) {
                orderHistoryProductRV.setVisibility(View.VISIBLE);
                orderHistoryAdapter.setSaveOrderList(orderHistoriesList);
                orderHistoryProductRV.setAdapter(orderHistoryAdapter);
                orderHistoryAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(baseActivity, "No server order history available", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
            hideDialog();

            orderHistoryAdapter.notifyDataSetChanged();


        }


        // }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        orderHistoryAdapter.filter(newText);
        pendingOrderHistoryAdapter.filter(newText);
        return true;
    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.v(LOG_TAG, "Receieved notification about network status");
            if (Connectivity.isNetworkAvailable(context)) {
                if (!isConnected) {
                    Log.v(LOG_TAG, "Now you are connected to Internet!");
                    isConnected = true;
                    sharedPreference.saveBooleanValue("IsOrderHistoryFrag", true);
                    //  Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                    if (saveOrderProduct.size() != 0) {
                        requestHttp(saveOrderProduct);
                    }
                    if (ApplicationClass.getAppContext() != null)
                        LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);
                    //do your processing here ---
                    //if you need to post any data to the server or get status
                    //update from the server
                } else {
//                    Log.e("*******************","No Internet");

                }
            } else {
                hideDialog();
//                Log.e("*******************","No Internet");
//                Log.e("*******************","#######"+sharedPreference.getBooleanValue("IsOrderHistoryFrag"));
//            if (!sharedPreference.getBooleanValue("IsOrderHistoryFrag")){
//                Toast.makeText(getActivity(),"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//
//            }
//            else{
                getOrderHistoryArray();
                //  }

                Log.d("Internet not Connected", "??????");

                Log.v(LOG_TAG, "You are not connected to Internet!");
                //   Toast.makeText(context, "not Connected", Toast.LENGTH_SHORT).show();
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
                                sharedPreference.saveBooleanValue("IsOrderHistoryFrag", true);
                                //  Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                                if (saveOrderProduct.size() != 0) {
                                    requestHttp(saveOrderProduct);
                                }
                                if (ApplicationClass.getAppContext() != null)
                                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);
                                //do your processing here ---
                                //if you need to post any data to the server or get status
                                //update from the server
                            } else {
//                                Log.e("*******************","No Internet");
//                                Log.e("*******************","#######"+sharedPreference.getBooleanValue("IsOrderHistoryFrag"));
//                                if (!sharedPreference.getBooleanValue("IsOrderHistoryFrag")){
//                                    Toast.makeText(getActivity(),"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//
//                                }

                                Log.d("Internet not Connected", "??????");
                            }
                            return true;
                        }
                    }
                }
            }

            hideDialog();
//            Log.e("*******************","No Internet");
//            Log.e("*******************","#######"+sharedPreference.getBooleanValue("IsOrderHistoryFrag"));
//            if (!sharedPreference.getBooleanValue("IsOrderHistoryFrag")){
//                Toast.makeText(getActivity(),"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//
//            }
//            else{
            getOrderHistoryArray();
            //  }

            Log.d("Internet not Connected", "??????");

            Log.v(LOG_TAG, "You are not connected to Internet!");
            //   Toast.makeText(context, "not Connected", Toast.LENGTH_SHORT).show();
            isConnected = false;
            return false;
        }
    }

    private void requestHttp(List<SaveOrder> listSaveorderArray) {
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        final List<SaveOrder> listSaveOrderListArray = listSaveorderArray;

        serviceHandler.placeOrder(listSaveorderArray, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String serverResponse = CommonUtils.getServerResponse(response);
                try {
                    JSONObject jsonObject1 = new JSONObject(serverResponse);
                    if (jsonObject1.getString("Message").equals("Success")) {
                        for (SaveOrder saveOrder : listSaveOrderListArray) {
                            saveOrder.setSendToServer("1");
                            saveOrder.save();
                        }

                        PendingOrderItem.deleteAll(PendingOrderItem.class);
                        SaveOrder.deleteAll(SaveOrder.class);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Toast.makeText(baseActivity, "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
            }
        });
        //  CommonUtils.dismissProgress();

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


    @Override
    public void onDestroy() {
        hideDialog();
        super.onDestroy();

    }
}
