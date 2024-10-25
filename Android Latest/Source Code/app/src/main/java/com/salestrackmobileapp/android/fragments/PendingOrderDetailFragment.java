package com.salestrackmobileapp.android.fragments;

import android.app.Dialog;
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
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.ITableViewListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.ItemRVAdapter;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllBusiness;
import com.salestrackmobileapp.android.gson.AllProduct;
import com.salestrackmobileapp.android.gson.PendingOrderItem;
import com.salestrackmobileapp.android.gson.SaveOrder;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PendingOrderDetailFragment extends BaseFragment implements RecyclerClick, ITableViewListener {

    int orderNumber;
    int orderLocation;

    List<PendingOrderItem> pendingOrderItem = new ArrayList<PendingOrderItem>();
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

    SaveOrder saveOrder;
    AllBusiness business;

    ItemRVAdapter itemRVAdapter;
    private LinearLayoutManager mLayoutManager;
    private TableView mTableView;
    private MyTableAdapter mTableAdapter;
    private List<List<CellModel>> mCellList;
    private List<ColumnHeaderModel> mColumnHeaderList;
    private List<RowHeaderModel> mRowHeaderList;


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
        mTableView = (TableView) view.findViewById(R.id.my_TableView);
        if (getArguments() != null) {
            orderLocation = getArguments().getInt("orderPosition");
            orderNumber = getArguments().getInt("orderNumber");
            saveOrder = (SaveOrder) getArguments().getSerializable("orderObj");//orderNumberID
            pendingOrderItem = Select.from(PendingOrderItem.class).where(Condition.prop("order_number_id").eq(orderNumber)).list();
            business = Select.from(AllBusiness.class).where(Condition.prop("business_id").eq(saveOrder.getBusinessID())).first();

        }
        binDataForTable();

//            orderNumbertxt.setText(" Order No. "+orderNumber + "");
//            String dateSt = saveOrder.getOrderDate();
//            String dateArray[] = dateSt.split("T");
//
//            orderDate.setText("order on :"+dateArray[0]+ "\n at "+dateArray[1]);
//            businessName.setText(business.getBusnessName() + "");
//            totalAmount.setText("Order Value" + saveOrder.getTotalOrderValue() + "");
//            totalDiscount.setText("Total Discount" + saveOrder.getDiscountAmount() + "");
//
//            if (saveOrder.getSendToServer().equals("0")) {
//                orderStatus.setText("pending");
//            } else {
//                orderStatus.setText("deleivered");
//            }
//
//        }
//        mLayoutManager = new LinearLayoutManager(getActivity());
//
//        itemRVAdapter = new ItemRVAdapter(getContext(), this);
//        itemRVAdapter.setItemArray(pendingOrderItem);
//        itemRV.setLayoutManager(mLayoutManager);
//        itemRV.setAdapter(itemRVAdapter);
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

    public void getOrderHistoryArrayTable() {
        for (PendingOrderItem pendingOrderItemobj : pendingOrderItem) {
            allProduct.add(Select.from(AllProduct.class).where(Condition.prop("product_id").eq(pendingOrderItemobj.getOrderItemID())).first());
        }
        populatedTableView(pendingOrderItem);

    }

    public void populatedTableView(List<PendingOrderItem> pendingOrderItem) {
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
        list.add(new ColumnHeaderModel("Item Name"));
        list.add(new ColumnHeaderModel("Qty"));
        list.add(new ColumnHeaderModel("Price"));
        list.add(new ColumnHeaderModel("Total"));
        list.add(new ColumnHeaderModel("Delete"));

//        Log.e("list_size","::::"+list.size());


        return list;
    }

    private List<List<CellModel>> loadCellModelList(List<PendingOrderItem> orderItemList) {
        List<List<CellModel>> lists = new ArrayList<>();

        // Creating cell model list from UserInfo list for Cell Items
        // In this example, UserInfo list is populated from web service

        for (int i = 0; i < orderItemList.size(); i++) {
            PendingOrderItem orderItem = orderItemList.get(i);

            List<CellModel> list = new ArrayList<>();

            // The order should be same with column header list;
            list.add(new CellModel("1-" + i, orderItem.getProductName()));       // "Id"
            list.add(new CellModel("2-" + i, orderItem.getQuantity()));     // "Name"

            if (orderItem.getDiscount() != null) {

                list.add(new CellModel("3-" + i, orderItem.getCost() - orderItem.getDiscount() + "")); // "Nickname"

                list.add(new CellModel("4-" + i, (Math.round(Double.parseDouble(String.valueOf(orderItem.getCost() - orderItem.getDiscount()))) * orderItem.getQuantity()) + ""));    // "Email"


            } else {

                list.add(new CellModel("3-" + i, orderItem.getCost() + "")); // "Nickname"
                list.add(new CellModel("4-" + i, (Math.round(Double.parseDouble(String.valueOf(orderItem.getCost()))) * orderItem.getQuantity()) + ""));    // "Email"

            }
//            CheckBox checkBox = new CheckBox(getActivity());
//            checkBox.setOnCheckedChangeListener(this);
//            checkBox.setId(i);
//            checkBox.setText("");
            list.add(new CellModel("" + i, ""));


            // Add
            lists.add(list);
        }

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

    public void onBackPressed() {
        Intent intent = new Intent(baseActivity, GoalsActivities.class);
        intent.putExtra("isfrom", "dashboard");
        intent.putExtra("nameActivity", "orderhistory");
        startActivity(intent);
    }

    @Override
    public void productClick(View v, int position) {

    }

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder p_jCellView, int p_nXPosition, int p_nYPosition) {
//        Log.e("X_position",":::"+p_nXPosition);

//        Log.e("Y_position",":::"+p_nYPosition);
        if (p_nXPosition == 4) {

            PendingOrderItem orderItem = pendingOrderItem.get(p_nYPosition);
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
    private static Dialog mProgressDialog;

    public void getOrdereItemDelete(int orderId) {


        if (Connectivity.isNetworkAvailable(baseActivity)) {


            if (mProgressDialog == null) {
                mProgressDialog = new Dialog(baseActivity);
                mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mProgressDialog.setContentView(
                        R.layout.l_progress_view);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
            }

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
                            binDataForTable();
                            Toast.makeText(baseActivity, "Order Item deleted successfully.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(baseActivity, "" + Message, Toast.LENGTH_SHORT).show();
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
                    try {
                        if (error.getMessage().equals("timeout")) {
//                            getProductPriceList();
                        } else {


                        }
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
//                    getAllGoalsAccDate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });


        } else {


        }
    }
}
