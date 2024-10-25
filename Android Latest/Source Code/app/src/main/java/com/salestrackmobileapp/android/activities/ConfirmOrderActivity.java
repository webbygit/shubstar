package com.salestrackmobileapp.android.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.adapter.SumProductAdapter;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllBusiness;
import com.salestrackmobileapp.android.gson.PendingOrderItem;
import com.salestrackmobileapp.android.gson.SaveOrder;
import com.salestrackmobileapp.android.gson.VisitedGoals;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ConfirmOrderActivity extends BaseActivity {


    @BindView(R.id.productsRv)
    RecyclerView productsRv;
    @BindView(R.id.place_order_btn)
    Button placeOrderBtn;
    @BindView(R.id.cur_date)
    Custome_BoldTextView currentDate;
    @BindView(R.id.business_name)
    Custome_TextView businessName;
    @BindView(R.id.business_address1)
    Custome_TextView businessAddress1;
    @BindView(R.id.business_address2)
    Custome_TextView businessAddress2;
    @BindView(R.id.business_email)
    Custome_TextView businessEmail;

    @BindView(R.id.total_amount_tv)
    Custome_TextView totalAmount;
    @BindView(R.id.total_discount_tv)
    Custome_TextView totalDiscount;
    @BindView(R.id.total_base_amount_tv)
    Custome_TextView totalbaseAmount;
//total_base_amount_tv


    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    SumProductAdapter sumProductAdapter;
    List<ProductInCart> cartList;
    List<PendingOrderItem> saveOrderList;
    SaveOrder saveObj;
    List<SaveOrder> listSaveorderArray;

    private LinearLayoutManager mLayoutManager;
    private int year;
    private int month;
    private int day;
    SaveOrder newObje;
    VisitedGoals visitedGoals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        setTitle("Order Summary");
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        productsRv.setLayoutManager(mLayoutManager);

        if (getIntent().getSerializableExtra("saveOrderItem") != null) {

            saveObj = (SaveOrder) getIntent().getSerializableExtra("saveObj");

            newObje = Select.from(SaveOrder.class).where(Condition.prop("order_no").eq(saveObj.getOrderNo())).first();
            saveOrderList = (List<PendingOrderItem>) getIntent().getSerializableExtra("saveOrderItem");
            totalAmount.setText(/*saveObj.getTotalOrderValue() */ sharedPreference.getStringValue(PrefsHelper.TOTAL_AMOUNT) + "");
            totalDiscount.setText(/*saveObj.getDiscountAmount()*/sharedPreference.getStringValue(PrefsHelper.TOTAL_DISCOUNT) + "");
            totalbaseAmount.setText(/*saveObj.getTotalOrderValue() + saveObj.getDiscountAmount()*/sharedPreference.getStringValue(PrefsHelper.RAW_AMOUNT) + "");
            listSaveorderArray = (List<SaveOrder>) getIntent().getSerializableExtra("saveOrdersArray");
            if (saveOrderList != null) {
                sumProductAdapter = new SumProductAdapter(getApplicationContext(), saveOrderList,sharedPreference);
                productsRv.setAdapter(sumProductAdapter);
                sumProductAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "no items found", Toast.LENGTH_SHORT).show();
            }


            AllBusiness allBusiness = Select.from(AllBusiness.class).where(Condition.prop("business_id").eq(saveObj.getBusinessID())).first();
            if (allBusiness != null) {
                businessName.setText(allBusiness.getBusnessName() + "");
                businessAddress1.setText(allBusiness.getAddress1() + "  ");
                businessAddress2.setText(allBusiness.getAddress2() + "  ");// + allBusiness.getAddress2()
                businessEmail.setText(allBusiness.getContactPersonEmail() + "");
            } else {
                Toast.makeText(this, "no business detail found", Toast.LENGTH_SHORT).show();
            }

            totalAmount.setText(/*saveObj.getTotalOrderValue() */ sharedPreference.getStringValue(PrefsHelper.TOTAL_AMOUNT) + "");
            totalDiscount.setText(/*saveObj.getDiscountAmount()*/sharedPreference.getStringValue(PrefsHelper.TOTAL_DISCOUNT) + "");
            totalbaseAmount.setText(/*saveObj.getTotalOrderValue() + saveObj.getDiscountAmount()*/sharedPreference.getStringValue(PrefsHelper.RAW_AMOUNT) + "");

            String currentDateTimeString = getDateTime();
            String dateSt[] = currentDateTimeString.split("T");

            newObje.setOrderDate(currentDateTimeString);
            newObje.save();
            currentDate.setText(dateSt[0] + " at " + dateSt[1]);

        }
    }


    private String getDateTime() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);

        String format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS").format(cal.getTime());
        return format;
    }


    @OnClick(R.id.place_order_btn)
    public void placeOrder() {
        cartList = ProductInCart.listAll(ProductInCart.class);
        AllBusiness goalBusiness = Select.from(AllBusiness.class).where(Condition.prop("business_id").eq(sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID))).first();
        if (goalBusiness != null) {
            visitedGoals = new VisitedGoals();
            visitedGoals.setBusinessID(goalBusiness.getBusinessID());
            visitedGoals.setBusnessName(goalBusiness.getBusnessName());
            visitedGoals.setAddress1(goalBusiness.getAddress1());
            visitedGoals.setAddress2(goalBusiness.getAddress2());
            visitedGoals.setZipCode(goalBusiness.getZipCode());
            visitedGoals.setWebsiteName(goalBusiness.getWebsiteName());
            visitedGoals.setCheckedIN(goalBusiness.getCheckedIN());
            visitedGoals.setCity(goalBusiness.getCity());
            visitedGoals.setContactPersonEmail(goalBusiness.getContactPersonEmail());
            visitedGoals.setContactPersonName(goalBusiness.getContactPersonName());
            visitedGoals.setContactPersonPhone(goalBusiness.getContactPersonPhone());
            visitedGoals.setCountry(goalBusiness.getCountry());
            visitedGoals.setDefaultGoalbusinessID(goalBusiness.getDefault_id());
            visitedGoals.setState(goalBusiness.getState());
            visitedGoals.setImageName(goalBusiness.getImageName());
            visitedGoals.save();
        }

        if (listSaveorderArray.size() != 0) {
            /*new Handler().postDelayed(new Runnable() {
                public void run() {*/
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            receiver = new NetworkChangeReceiver();
            registerReceiver(receiver, filter);
              /*  }
            }, 10000);*/
        } else {
            Toast.makeText(getApplicationContext(), "no data to place order ", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
        intent.putExtra("nameActivity", "cart_fragment");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.v("Goals getting", "Receieved notification about network status");
            if(Connectivity.isNetworkAvailable(context)){
                if (!isConnected) {
                    //  Toast.makeText(context, "Internet Connected with cart ", Toast.LENGTH_SHORT).show();
                    if (Select.from(SaveOrder.class).where(Condition.prop("send_to_server").eq("0")).list().size() != 0) {
                        requestHttp(listSaveorderArray);
                        sharedPreference.saveBooleanValue("IsConfirmOrder",true);
                    }
                    unregisterReceiver(receiver);
                }
            }
            else {

                isConnected = false;
            }

        }
        boolean isConfirmOrder;

        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if (!isConnected) {
                              //  Toast.makeText(context, "Internet Connected with cart ", Toast.LENGTH_SHORT).show();
                                if (Select.from(SaveOrder.class).where(Condition.prop("send_to_server").eq("0")).list().size() != 0) {
                                    requestHttp(listSaveorderArray);
                                    sharedPreference.saveBooleanValue("IsConfirmOrder",true);
                                }
                                unregisterReceiver(receiver);
                            }
                            return true;
                        }
                    }
                }
            }
            else{
                if (!sharedPreference.getBooleanValue("IsConfirmOrder")){
                  //  Toast.makeText(ConfirmOrderActivity.this,"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();

                }

            }
            Log.v("not connected", "You are not connected to Internet!");
           // Toast.makeText(context, "not Connected", Toast.LENGTH_SHORT).show();


            isConnected = false;
            return false;
        }
    }

    private void requestHttp(final List<SaveOrder> listSaveorderArray) {
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(this, ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
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
                                SaveOrder newSaveOrder = Select.from(SaveOrder.class).where(Condition.prop("order_no").eq(saveOrder.getOrderNo())).first();
                                newSaveOrder.setSendToServer("1");
                                newSaveOrder.save();
                            }
                           /* PendingOrderItem.deleteAll(PendingOrderItem.class);
                            SaveOrder.deleteAll(SaveOrder.class);*/
                        }
                        ProductInCart.deleteAll(ProductInCart.class);
                     //   sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, "");
                        Toast.makeText(getApplicationContext(), "Your order has been successfully placed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), GoalsActivities.class);
                        intent.putExtra("nameActivity", "orderhistory");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        finish();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if (error.getMessage().equals("timeout")) {
                        requestHttp(listSaveOrderListArray);
                    }
                    error.printStackTrace();
                  //  Toast.makeText(getBaseContext(), "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
                }
            });
            CommonUtils.dismissProgress();
        }
    }
}
