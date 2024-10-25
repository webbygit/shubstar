package com.salestrackmobileapp.android.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.ApplicationClass;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.SumProductAdapter;
import com.salestrackmobileapp.android.gson.SaveOrder;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ConfirmOrderFragment extends BaseFragment {

    @BindView(R.id.productsRv)
    RecyclerView productsRv;
    @BindView(R.id.place_order_btn)
    Button placeOrderBtn;

    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    SumProductAdapter sumProductAdapter;
    List<ProductInCart> cartList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);
        ButterKnife.bind(this, view);
        baseActivity.setTitle("Order Summary");

        if (getArguments() != null) {
            getArguments();
        }
        return view;
    }


    @OnClick(R.id.place_order_btn)
    public void placeOrder() {
        cartList = ProductInCart.listAll(ProductInCart.class);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                receiver = new NetworkChangeReceiver();
                if (ApplicationClass.getAppContext() != null)
                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).registerReceiver(receiver, filter);
            }
        }, 10000);
    }


    public void onBackPressed() {
        Intent intent = new Intent(baseActivity, GoalsActivities.class);
        intent.putExtra("nameActivity", "cart_fragment");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.v("Goals getting", "Receieved notification about network status");
            if (Connectivity.isNetworkAvailable(context)) {
                if (!isConnected) {
                    Log.v("is network Available", "Now you are connected to Internet!");
                    isConnected = true;
                    sharedPreference.saveBooleanValue("IsConfirmOrderFrag", true);
                    //    Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();

                    if (Select.from(SaveOrder.class).where(Condition.prop("send_to_server").eq("0")).list().size() != 0) {
                        // requestHttp(listSaveorderArray);
                    }
                    if (baseActivity != null)
                        LocalBroadcastManager.getInstance(baseActivity).unregisterReceiver(receiver);
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
                                Log.v("is network Available", "Now you are connected to Internet!");
                                isConnected = true;
                                sharedPreference.saveBooleanValue("IsConfirmOrderFrag", true);
                                //    Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();

                                if (Select.from(SaveOrder.class).where(Condition.prop("send_to_server").eq("0")).list().size() != 0) {
                                    // requestHttp(listSaveorderArray);
                                }
                                if (baseActivity != null)
                                    LocalBroadcastManager.getInstance(baseActivity).unregisterReceiver(receiver);
                                //do your processing here ---
                                //if you need to post any data to the server or get status
                                //update from the server

                            }
                            return true;
                        }
                    }
                }
            }
//            if (!sharedPreference.getBooleanValue("IsConfirmOrderFrag")){
//                Toast.makeText(getActivity(),"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//
//            }
            Log.v("not connected", "You are not connected to Internet!");
            //  Toast.makeText(context, "not Connected", Toast.LENGTH_SHORT).show();
            isConnected = false;
            return false;
        }
    }

    private void requestHttp(List<SaveOrder> listSaveorderArray) {
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        final List<SaveOrder> listSaveOrderListArray = listSaveorderArray;
        if (cartList.size() != 0) {
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
            CommonUtils.dismissProgress();
        }
    }
}
