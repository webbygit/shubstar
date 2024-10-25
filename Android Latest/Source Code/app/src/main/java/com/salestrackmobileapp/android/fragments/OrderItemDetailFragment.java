package com.salestrackmobileapp.android.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.ApplicationClass;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.OrderHistoryItemAdapter;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllProduct;
import com.salestrackmobileapp.android.gson.OrderHistory;
import com.salestrackmobileapp.android.gson.OrderItem;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.tableview.MyTableAdapter;
import com.salestrackmobileapp.android.tableview.model.CellModel;
import com.salestrackmobileapp.android.tableview.model.ColumnHeaderModel;
import com.salestrackmobileapp.android.tableview.model.RowHeaderModel;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.RecyclerClick;
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


public class OrderItemDetailFragment extends BaseFragment implements RecyclerClick, ITableViewListener {


    int orderId;

    List<OrderItem> pendingOrderItem;
    List<AllProduct> allProduct = new ArrayList<AllProduct>();
    @BindView(R.id.order_number_tv)
    Custome_TextView orderNumbertxt;
    @BindView(R.id.business_name)
    Custome_BoldTextView businessName;
    @BindView(R.id.total_amt)
    Custome_TextView totalAmount;
    @BindView(R.id.total_discount_txt)
    Custome_TextView totalDiscount;
    //total_discount_txt
    @BindView(R.id.orderDate)
    Custome_TextView orderDate;
    @BindView(R.id.order_status_btn)
    Button orderStatus;

    @BindView(R.id.rv_items_order)
    RecyclerView itemRV;


    OrderHistoryItemAdapter itemRVAdapter;
    LinearLayoutManager mLayoutManager;

    private ProgressDialog mProgressDialog;

    ServiceHandler serviceHandler;

    OrderHistory orderHistoryObject;
    int orderNumber;
    private TableView mTableView;
    private MyTableAdapter mTableAdapter;
    private List<List<CellModel>> mCellList;
    private List<ColumnHeaderModel> mColumnHeaderList;
    private List<RowHeaderModel> mRowHeaderList;

    private TextView taxTV;
    private TextView totalTV;
    private double taxamount;
    private double totalamount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        ButterKnife.bind(this, view);
        GoalsActivities.totalAmt.setVisibility(View.GONE);
        taxTV = (TextView)view.findViewById(R.id.taxTV);
        totalTV = (TextView)view.findViewById(R.id.totalTV);
        if (getArguments() != null) {
            orderId = getArguments().getInt("orderId");
            orderHistoryObject = (OrderHistory) getArguments().getSerializable("orderHistoryObject");//orderPosition
            orderNumber = getArguments().getInt("orderNumber");
        }
        mTableView = (TableView) view.findViewById(R.id.my_TableView);
        binDataForTable();


//        mLayoutManager = new LinearLayoutManager(getActivity());
//        itemRVAdapter = new OrderHistoryItemAdapter(getContext(), this);
//        itemRV.setLayoutManager(mLayoutManager);
//
//        orderNumbertxt.setText(" Order No. " + orderNumber + "");
//        orderNumbertxt.setVisibility(View.GONE);
//        String dateSt = orderHistoryObject.getOrderDate();
//        String dateArray[] = dateSt.split("T");
//
//        orderDate.setText("order on :" + dateArray[0] + "\n at " + dateArray[1]);
//        businessName.setText("Business :"+orderHistoryObject.getBusinessName() + "");
//        totalAmount.setText("Order Value " + orderHistoryObject.getTotalOrderValue() + "");
//        if (orderHistoryObject.getDiscountAmount().equals("0")){
//            totalDiscount.setVisibility(View.GONE);
//        }
//        else {
//            totalDiscount.setText("Total Discount" + orderHistoryObject.getDiscountAmount() + "");
//            totalDiscount.setVisibility(View.VISIBLE);
//        }
//
//
//        getOrderHistoryArray();

        return view;
    }

    public void binDataForTable() {


        // Create TableView Adapter
        mTableAdapter = new MyTableAdapter(baseActivity);
        mTableView.setAdapter(mTableAdapter);
        mTableView.setTableViewListener(this);

        // Create listener
        //  mTableView.setTableViewListener(new MyTableViewListener(mTableView));
        // mTableView.setTableViewListener(this);

        // UserInfo data will be getting from a web server.
//        mWebServiceHandler = new WebServiceHandler(this);
//        mWebServiceHandler.loadUserInfoList();
        getOrderHistoryArrayTable();


    }

    public void populatedTableView(List<OrderItem> pendingOrderItem) {
        // create Models
        mColumnHeaderList = createColumnHeaderModelList();
        mCellList = loadCellModelList(pendingOrderItem);
        mRowHeaderList = createRowHeaderList();

        // Set all items to the TableView
        mTableAdapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);
    }


    private List<ColumnHeaderModel> createColumnHeaderModelList() {
        List<ColumnHeaderModel> list = new ArrayList<>();

        // Create Column Headers
//        list.add(new ColumnHeaderModel("No."));
        //list.add(new ColumnHeaderModel("No"));
        list.add(new ColumnHeaderModel("Item Name"));
        list.add(new ColumnHeaderModel("Qty"));
        list.add(new ColumnHeaderModel("Price"));
       /* list.add(new ColumnHeaderModel("Tax"));*/
        list.add(new ColumnHeaderModel("Discount"));
        /*list.add(new ColumnHeaderModel("Total"));*/
        list.add(new ColumnHeaderModel("Delete"));



        Log.e("list_size", "::::" + list.size());


        return list;
    }

    private List<List<CellModel>> loadCellModelList(List<OrderItem> orderItemList) {
        List<List<CellModel>> lists = new ArrayList<>();

        // Creating cell model list from UserInfo list for Cell Items
        // In this example, UserInfo list is populated from web service

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);

            List<CellModel> list = new ArrayList<>();

            // The order should be same with column header list;
            //  list.add(new CellModel("1-"+i,""+(i+1)));
            list.add(new CellModel("1-" + i, orderItem.getProductName()));       // "Id"
            list.add(new CellModel("2-" + i, orderItem.getQuantity()));     // "Name"

            if (orderItem.getDiscount() != null) {
                list.add(new CellModel("3-" + i, orderItem.getCost() - orderItem.getDiscount() + "")); // "Nickname"
            } else
                {
                list.add(new CellModel("3-" + i, orderItem.getCost() + "")); // "Nickname"
                }
           /* list.add(new CellModel("4-" + i, orderItem.getTaxAmount()+"")); */// "Nickname"
            if (orderItem.getDiscount() != null) {
                list.add(new CellModel("5-" + i, orderItem.getDiscount() + "")); // "Nickname"
            } else {
                list.add(new CellModel("5-" + i, "0.0")); // "Nickname"
            }
          /*  list.add(new CellModel("6-" + i, orderItem.getTotalOrderValue() + ""));*/    // "Email"



            list.add(new CellModel("7-" + i, ""));

            taxamount= orderItem.getTaxAmount();
            totalamount = orderItem.getTotalOrderValue();


            // Add
            lists.add(list);
        }
        taxTV.setText(String.valueOf(taxamount));
        totalTV.setText(String.valueOf(totalamount));
        return lists;

    }

    private List<RowHeaderModel> createRowHeaderList() {
        List<RowHeaderModel> list = new ArrayList<>();
        for (int i = 0; i < mCellList.size(); i++) {
            // In this example, Row headers just shows the index of the TableView List.
            list.add(new RowHeaderModel(String.valueOf(i + 1)));
        }
        return list;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(baseActivity);
            // mProgressDialog.setMessage("Get data, please wait...");
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {

        if ((mProgressDialog != null) && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    public void onBackPressed() {
        Intent intent = new Intent(baseActivity, GoalsActivities.class);
        intent.putExtra("isfrom", "dashboard");
        intent.putExtra("nameActivity", "orderhistory");
        startActivity(intent);
    }

    @Override
    public void productClick(View v, int position) {

    }

    List<OrderItem> orderItemList;

    public void getOrderHistoryArrayTable() {
        if (Connectivity.isNetworkAvailable(baseActivity)) {


            mProgressDialog = new ProgressDialog(baseActivity);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.show();
            //if(OrderItem.isSugarEntity(OrderItem.class))

            if (OrderItem.listAll(OrderItem.class).size() != 0) {
                OrderItem.deleteAll(OrderItem.class);
            }
            Log.e("ORDER_ID", "::::" + orderId);

            builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                    .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
            serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
            serviceHandler.getOrderItem(orderId, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    sharedPreference.saveBooleanValue("IsOrderItemDetail", true);
                    String arr = CommonUtils.getServerResponse(response);
                    Log.e("ORDER", ":::Response:::::" + arr);
                    try {
                        // String jsonString = arr.replace("null", "\"\"");
                        JSONArray jsonArr = new JSONArray(arr);
                        for (int i = 0; i < jsonArr.length(); i++) {
                            OrderItem orderItem = builder.fromJson(jsonArr.get(i).toString(), OrderItem.class);
                            orderItem.save();
                        }
                        orderItemList = new ArrayList<>();
                        orderItemList = OrderItem.listAll(OrderItem.class);
                        if (orderItemList.size() != 0) {
//                            pendingOrderItem = OrderItem.listAll(OrderItem.class);
//                            itemRVAdapter.setItemArray(pendingOrderItem);
//                            itemRV.setAdapter(itemRVAdapter);
//                            itemRVAdapter.notifyDataSetChanged();

                            populatedTableView(orderItemList);


                        } else {

                            Toast.makeText(baseActivity, "No data available", Toast.LENGTH_SHORT).show();
                            //  Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        hideDialog();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    try {
                        // Toast.makeText(getActivity(), "Goals not found", Toast.LENGTH_SHORT).show();
                        hideDialog();
                        orderItemList = new ArrayList<>();
                        orderItemList = OrderItem.listAll(OrderItem.class);

                        if (orderItemList.size() != 0) {
//                            pendingOrderItem = OrderItem.listAll(OrderItem.class);
//                            itemRVAdapter.setItemArray(pendingOrderItem);
//                            itemRV.setAdapter(itemRVAdapter);
//                            itemRVAdapter.notifyDataSetChanged();
                            populatedTableView(orderItemList);

                        } else {
                            Toast.makeText(baseActivity, "No data available", Toast.LENGTH_SHORT).show();
                            //  Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
                        }
//                        if (OrderItem.listAll(OrderItem.class).size() != 0) {
//                            pendingOrderItem = OrderItem.listAll(OrderItem.class);
//                            itemRVAdapter.setItemArray(pendingOrderItem);
//                            itemRV.setAdapter(itemRVAdapter);
//                            itemRVAdapter.notifyDataSetChanged();
//
//                        } else {
//                            Toast.makeText(getActivity(),"No data available",Toast.LENGTH_SHORT).show();
//                            //  Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
//                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } else {


            orderItemList = new ArrayList<>();
            orderItemList = OrderItem.listAll(OrderItem.class);

            if (orderItemList.size() != 0) {
//                            pendingOrderItem = OrderItem.listAll(OrderItem.class);
//                            itemRVAdapter.setItemArray(pendingOrderItem);
//                            itemRV.setAdapter(itemRVAdapter);
//                            itemRVAdapter.notifyDataSetChanged();
                populatedTableView(orderItemList);

            } else {
                Toast.makeText(baseActivity, "No data available", Toast.LENGTH_SHORT).show();
                //  Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
            }
            hideDialog();


        }
    }

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder p_jCellView, int p_nXPosition, int p_nYPosition) {
//        Log.e("X_position",":::"+p_nXPosition);

//        Log.e("Y_position",":::"+p_nYPosition);
        if (p_nXPosition == 6) {

            OrderItem orderItem = orderItemList.get(p_nYPosition);
            int Id = orderItem.getOrderItemID();
//            Log.e("OrderItemID",":::"+Id);
            getOrdereItemDelete(Id);


        }
    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder viewHolder, int i, int i1) {

    }

    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView, int p_nXPosition) {

    }

    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder p_jColumnHeaderView, int p_nXPosition) {

    }

    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int p_nYPosition) {

    }

    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder p_jRowHeaderView, int p_nYPosition) {

    }

    Gson builder;

    public void getOrdereItemDelete(int orderId) {


        if (Connectivity.isNetworkAvailable(ApplicationClass.getAppContext())) {

            showDialog();

            builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                    .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
            ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);


            serviceHandler.getOrderDeleteItem(orderId, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    String arr = CommonUtils.getServerResponse(response);


                    try {
                        JSONObject jsonObject = new JSONObject(arr);
                        String Message = jsonObject.getString("Message");
                        if (Message.equals("Success")) {
                            hideDialog();
                            binDataForTable();
                            Toast.makeText(baseActivity, "Order Item deleted successfully.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(baseActivity, "" + Message, Toast.LENGTH_SHORT).show();
                        }


                        hideDialog();


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
//                            getProductPriceList();
                        } else {


                        }
                        hideDialog();
//                    getAllGoalsAccDate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });


        } else {


        }
    }


    //
//    public void getOrderHistoryArray() {
//        if (Connectivity.isNetworkAvailable(getActivity())) {
//
//
//            if (mProgressDialog == null) {
//                mProgressDialog = new Dialog(getActivity());
//                mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                mProgressDialog.setContentView(R.layout.l_progress_view);
//                mProgressDialog.setCanceledOnTouchOutside(false);
//                mProgressDialog.show();
//            }
//            //if(OrderItem.isSugarEntity(OrderItem.class))
//
//            if (OrderItem.listAll(OrderItem.class).size() != 0) {
//                OrderItem.deleteAll(OrderItem.class);
//            }
//
//            builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
//                    .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
//            serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
//            serviceHandler.getOrderItem(orderId, new Callback<Response>() {
//                @Override
//                public void success(Response response, Response response2) {
//                    sharedPreference.saveBooleanValue("IsOrderItemDetail",true);
//                    String arr = CommonUtils.getServerResponse(response);
//                    try {
//                        // String jsonString = arr.replace("null", "\"\"");
//                        JSONArray jsonArr = new JSONArray(arr);
//                        for (int i = 0; i < jsonArr.length(); i++) {
//                            OrderItem orderItem = builder.fromJson(jsonArr.get(i).toString(), OrderItem.class);
//                            orderItem.save();
//                        }
//                        if (OrderItem.listAll(OrderItem.class).size() != 0) {
//                            pendingOrderItem = OrderItem.listAll(OrderItem.class);
//                            itemRVAdapter.setItemArray(pendingOrderItem);
//                            itemRV.setAdapter(itemRVAdapter);
//                            itemRVAdapter.notifyDataSetChanged();
//
//                        } else {
//                            Toast.makeText(getActivity(),"No data available",Toast.LENGTH_SHORT).show();
//                            //  Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
//                        }
//                        if (mProgressDialog != null && mProgressDialog.isShowing())
//                            mProgressDialog.dismiss();
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                        if (mProgressDialog != null && mProgressDialog.isShowing())
//                            mProgressDialog.dismiss();
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    error.printStackTrace();
//                    try {
//                       // Toast.makeText(getActivity(), "Goals not found", Toast.LENGTH_SHORT).show();
//                        if (mProgressDialog != null && mProgressDialog.isShowing())
//                            mProgressDialog.dismiss();
//                        if (OrderItem.listAll(OrderItem.class).size() != 0) {
//                            pendingOrderItem = OrderItem.listAll(OrderItem.class);
//                            itemRVAdapter.setItemArray(pendingOrderItem);
//                            itemRV.setAdapter(itemRVAdapter);
//                            itemRVAdapter.notifyDataSetChanged();
//
//                        } else {
//                            Toast.makeText(getActivity(),"No data available",Toast.LENGTH_SHORT).show();
//                            //  Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
//                        }
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            });
//        }
//        else{
//
//
//                if (OrderItem.listAll(OrderItem.class).size() != 0) {
//                    pendingOrderItem = OrderItem.listAll(OrderItem.class);
//                    itemRVAdapter.setItemArray(pendingOrderItem);
//                    itemRV.setAdapter(itemRVAdapter);
//                    itemRVAdapter.notifyDataSetChanged();
//
//                } else {
//                    Toast.makeText(getActivity(),"No data available",Toast.LENGTH_SHORT).show();
//                    //  Toast.makeText(getContext(),"",Toast.LENGTH_SHORT).show();
//                }
//            if (mProgressDialog != null && mProgressDialog.isShowing())
//                mProgressDialog.dismiss();
//
//
//        }
//    }
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

}
