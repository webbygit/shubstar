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
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.solver.Goal;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.dsl.NotNull;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.ApplicationClass;
import com.salestrackmobileapp.android.activities.DashboardActivity;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.AllBusiAdapter;
import com.salestrackmobileapp.android.gson.AllBusiness;
import com.salestrackmobileapp.android.gson.CheckedDataHit;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.singleton.Singleton;
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

public class AllBusinessFragment extends BaseFragment implements RecyclerClick, SearchView.OnQueryTextListener {

    @BindView(R.id.buisness_rv)
    RecyclerView buisnessRV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private AllBusiAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private ProgressDialog mProgressDialog;

    String navigateToProductDetail;
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    IntentFilter filter;

    static Boolean checkedInStatus = false;


    @BindView(R.id.searchview)
    SearchView searchview;

    @BindView(R.id.state_spn)
    Spinner stateSpn;

    @BindView(R.id.city_spn)
    Spinner citySpn;

    static List<String> statesList = new ArrayList<String>();
    static List<String> cityList = new ArrayList<String>();

    static String cityNameSt = "Select City", stateNameSt = "Select State";
    GoalsActivities activity;

    public List<AllBusiness> listAllBusiness = new ArrayList<>();

    public AllBusinessFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_business, container, false);
        ButterKnife.bind(this, view);

        mLayoutManager = new LinearLayoutManager(baseActivity);
        mAdapter = new AllBusiAdapter(baseActivity, this);
        buisnessRV.setLayoutManager(mLayoutManager);
        buisnessRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = buisnessRV.getChildCount();
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
                    Log.i("Yaeye!", "end called");
                    loading = true;
                }
            }
        });

        setupSearchView();


        EditText searchEditText = (EditText) searchview.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.grey));

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        if (ApplicationClass.getAppContext() != null)
            LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).registerReceiver(receiver, filter);

        // getActivity().registerReceiver(receiver, filter);

        if (getArguments() != null) {
            navigateToProductDetail = getArguments().getString("navigateToProduct");
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ApplicationClass.getAppContext() != null)
                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).registerReceiver(receiver, filter);
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        if (Connectivity.isNetworkAvailable(baseActivity)) {
            if (!isAsyncTask) {
                if (!cityList.contains("Select City")) {
                    cityList.add("Select City");
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, cityList);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                citySpn.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
                citySpn.setSelection(0);
                getAllBusiness();
            }
        } else {

            if (!cityList.contains("Select City")) {
                cityList.add("Select City");
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, cityList);
            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            citySpn.setAdapter(dataAdapter);
            dataAdapter.notifyDataSetChanged();
            citySpn.setSelection(0);
//            if (!cityList.contains("Select City")) {
//                cityList.add("Select City");
//            }
            if (!statesList.contains("Select State")) {
                statesList.add("Select State");
            }


            listAllBusiness = AllBusiness.listAll(AllBusiness.class);
            Log.e("listAllBusiness", "::::size::::" + listAllBusiness.size());


            if (listAllBusiness != null && listAllBusiness.size() > 0) {


                for (int k = 0; k < listAllBusiness.size(); k++) {
                    AllBusiness business = listAllBusiness.get(k);
                    if (business.getCity() != null) {
                        if (!cityList.contains(business.getCity())) {
                            cityList.add(business.getCity() + "");
                        }
                    }

                    if (business.getState() != null) {
                        if (!statesList.contains(business.getState())) {
                            statesList.add(business.getState() + "");
                        }
                    }
                }

                mAdapter = new AllBusiAdapter(baseActivity, this);
                mAdapter.setAllBusinessList(listAllBusiness);
                buisnessRV.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
//
//                Log.e("listAllBusiness","::::mAdapter set::::"+listAllBusiness.size());

                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (statesList.size() != 0) {
                    dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, statesList);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    stateSpn.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();
                    stateSpn.setSelection(0);

                }


            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(baseActivity);
                builder1.setMessage("Sorry..Detail not available.Please contact your Administrator");
                builder1.setCancelable(false);
                builder1.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        //    Toast.makeText(getContext(), "Back is pressed", Toast.LENGTH_LONG).show();
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();

                    }
                });
                builder1.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        //  getAllBusiness();
                        getAllBusiness();
                    }
                });
                builder1.show();
            }
        }

        stateSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                showDialog();


                cityList.clear();
                cityList.add("Select City");
                if (position >= 1) {
                    stateNameSt = statesList.get(position);
                    List<AllBusiness> listProduct = Select.from(AllBusiness.class).where(Condition.prop("state").eq(stateNameSt)).list();
                    for (int k = 0; k < listProduct.size(); k++) {
                        if (!cityList.contains(listProduct.get(k).getCity())) {
                            cityList.add(listProduct.get(k).getCity());
                            hideDialog();

                        }
                        hideDialog();

                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, cityList);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    citySpn.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();
                    citySpn.setSelection(0);

                    cityNameSt = "";
                    if (stateNameSt.equals("Select State")) {
                        listAllBusiness = Select.from(AllBusiness.class).list();
                        mAdapter.setAllBusinessList(listAllBusiness);
                        buisnessRV.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        hideDialog();

                    } else {
                        viewAllBusiness();
                    }


                } else {
                    hideDialog();

                }
                hideDialog();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideDialog();

            }

        });

        citySpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cityNameSt = cityList.get(position);
//                Log.e("cityNameSt",":::"+cityNameSt);
//                Log.e("stateNameSt","::::"+stateNameSt);
                if (cityNameSt.equalsIgnoreCase("Select City")) {
                    if (stateNameSt.equalsIgnoreCase("Select State")) {
                        listAllBusiness = Select.from(AllBusiness.class).list();
//                        Log.e("listAllBusiness if","::::"+listAllBusiness.size());
                    } else {
                        listAllBusiness = Select.from(AllBusiness.class).where(Condition.prop("state").eq(stateNameSt)).list();
//                        Log.e("listAllBusiness else","::::"+listAllBusiness.size());

                    }
                    mAdapter.setAllBusinessList(listAllBusiness);
                    buisnessRV.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    hideDialog();
                    viewAllBusiness();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideDialog();
            }
        });

        return view;

    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }


    public void viewAllBusiness() {
        hideDialog();
        if (stateNameSt.equals("") || cityNameSt.equals("")) {
            //listAllBusiness
        } else {
            if (cityNameSt.equals("") || cityNameSt.equals("Select City")) {
                listAllBusiness = Select.from(AllBusiness.class).where(Condition.prop("state").eq(stateNameSt)).list();
            } else {
                listAllBusiness = Select.from(AllBusiness.class).where(Condition.prop("state").eq(stateNameSt), Condition.prop("city").eq(cityNameSt)).list();
            }
            mAdapter.setAllBusinessList(listAllBusiness);
            buisnessRV.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }

    }

    private void setupSearchView() {
        searchview.setIconifiedByDefault(false);
        searchview.setOnQueryTextListener(this);
        searchview.setSubmitButtonEnabled(true);
        searchview.setFocusableInTouchMode(true);
        searchview.setQueryHint("Search by business..");
    }


    private void getAllBusiness() {
        isAsyncTask = true;
        AllBusiness.deleteAll(AllBusiness.class);
        if (!cityList.contains("Select City")) {
            cityList.add("Select City");
        }
        if (!statesList.contains("Select State")) {
            statesList.add("Select State");
        }

        mProgressAsynctAsk = new ProgressDialog(baseActivity);
        mProgressAsynctAsk.setMessage("Loading data...");
        mProgressAsynctAsk.setCanceledOnTouchOutside(false);
        mProgressAsynctAsk.show();


        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();


        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(baseActivity, ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);

        serviceHandler.getAllBusinessArray(new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                String arr = CommonUtils.getServerResponse(response);
                try {


//                    Log.e("getAllBusiness", "getAllBusinessArray");

                    JSONArray jsonArr = new JSONArray(arr);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        AllBusiness allBusiness = builder.fromJson(jsonArr.get(i).toString(), AllBusiness.class);

                        if (allBusiness.getCity() != null) {
                            if (!cityList.contains(allBusiness.getCity())) {
                                cityList.add(allBusiness.getCity() + "");
                            }
                        }

                        if (allBusiness.getState() != null) {
                            if (!statesList.contains(allBusiness.getState())) {
                                statesList.add(allBusiness.getState() + "");
                            }
                        }

                        allBusiness.save();
                    }
                    listAllBusiness = AllBusiness.listAll(AllBusiness.class);

//                    Log.e("getAllBusiness", "list true");

                    if (listAllBusiness.size() != 0) {
                        mAdapter.setAllBusinessList(listAllBusiness);
                        buisnessRV.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    }
                    hideDialog();




                   /* if (cityList.size() != 0) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.single_text_item, cityList);
                        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        citySpn.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                        citySpn.setSelection(0);
                    }*/

                    if (statesList.size() != 0) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, statesList);
                        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        stateSpn.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                        stateSpn.setSelection(0);
                    }


                    if (listAllBusiness.size() == 0) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(baseActivity);
                        builder1.setMessage("No goals are available");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                hideDialog();

                            }
                        });
                        builder1.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                // getAllBusiness();
                                getAllBusiness();
                            }
                        });
                        builder1.show();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
//                    Log.e("getAllBusiness", "catch");

                    hideDialog();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
//                Log.e("getAllBusiness", "failure");

                if (listAllBusiness != null && listAllBusiness.size() > 0) {

                    if (!cityList.contains("Select City")) {
                        cityList.add("Select City");
                    }
                    if (!statesList.contains("Select State")) {
                        statesList.add("Select State");
                    }

                    for (int k = 0; k < listAllBusiness.size(); k++) {
                        AllBusiness business = listAllBusiness.get(k);
                        if (business.getCity() != null) {
                            if (!cityList.contains(business.getCity())) {
                                cityList.add(business.getCity() + "");
                            }
                        }

                        if (business.getState() != null) {
                            if (!statesList.contains(business.getState())) {
                                statesList.add(business.getState() + "");
                            }
                        }
                    }

                    mAdapter.setAllBusinessList(listAllBusiness);
                    buisnessRV.setAdapter(mAdapter);

                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    if (statesList.size() != 0) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, statesList);
                        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        stateSpn.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                        stateSpn.setSelection(0);
                    }
                    hideDialog();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(baseActivity);
                    builder1.setMessage("Sorry..Detail not available.Please contact your Administrator");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            hideDialog();

                        }
                    });
                    builder1.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            // getAllBusiness();
                            getAllBusiness();
                        }
                    });
                    builder1.show();
                }
            }
        });


    }

    AllBusiness businessObject;
    int businessLocation;

    @Override
    public void productClick(View v, int position) {
        int itemPosition = buisnessRV.getChildPosition(v);
        showDialog();

        sharedPreference.saveStringData(PrefsHelper.BUSINESS_LOCATION, itemPosition + "");
        businessLocation = itemPosition;
//        Log.e("Business_loc", "::::::" + itemPosition);
//        Log.e("STATE_click:", "--->" + listAllBusiness.get(itemPosition).getState());
        sharedPreference.saveStringData("STATE_PRODUCT", listAllBusiness.get(itemPosition).getState());

        if (stateNameSt.equals("Select State")) {
            sharedPreference.saveStringData(PrefsHelper.CITY, "");
        } else {
            sharedPreference.saveStringData(PrefsHelper.STATE, stateNameSt);
        }
        if (cityNameSt.equals("Select City")) {
            sharedPreference.saveStringData(PrefsHelper.CITY, "");
        } else {
            sharedPreference.saveStringData(PrefsHelper.CITY, cityNameSt);
        }
//
//        if (searchview.getQuery().toString().equals("")) {
//            args.putSerializable("BusinessObject", listAllBusiness.get(itemPosition));
//            sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, String.valueOf(listAllBusiness.get(itemPosition).getBusinessID()));
//
//        } else {
//            args.putSerializable("BusinessObject", mAdapter.filterList.get(itemPosition));
//            sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, String.valueOf(mAdapter.filterList.get(itemPosition).getBusinessID()));
//        }
//
//        fragment.setArguments(args);
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

        if (searchview.getQuery().toString().equals("")) {
            businessObject = listAllBusiness.get(itemPosition);
            sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, String.valueOf(listAllBusiness.get(itemPosition).getBusinessID()));

        } else {
            businessObject = mAdapter.filterList.get(itemPosition);
            sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, String.valueOf(mAdapter.filterList.get(itemPosition).getBusinessID()));
        }


        try {
            if (stateNameSt == null) {
                if (!sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID).equals("business_location")) {
                    List<AllBusiness> listGoal = AllBusiness.listAll(AllBusiness.class);
                    if (listGoal.size() != 0) {
                        int location = Integer.parseInt(sharedPreference.getStringValue(PrefsHelper.BUSINESS_LOCATION));
                        businessObject = listGoal.get(location);
                        checkedInStatus = businessObject.getCheckedIN();
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (!sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID).equals("business_location")) {
                List<AllBusiness> listGoal = AllBusiness.listAll(AllBusiness.class);

                // List<AllBusiness> listGoal = Select.from(AllBusiness.class).where(Condition.prop("business_id").eq(sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID))).list();
                if (listGoal.size() != 0) {
                    int location = Integer.parseInt(sharedPreference.getStringValue(PrefsHelper.BUSINESS_LOCATION));
                    businessObject = listGoal.get(location);
                    checkedInStatus = businessObject.getCheckedIN();
                }

            }
        }
        if (businessObject != null) {

            checkedInStatus = businessObject.getCheckedIN();
        } else {
            Toast.makeText(ApplicationClass.getAppContext(), "No detail available", Toast.LENGTH_SHORT).show();
        }
        hideDialog();
        setCheckInBtn(itemPosition);


    }


    public void onBackPressed() {
//        Log.e("OnBAck", ":::+business true");

        if (navigateToProductDetail != null) {
            if (navigateToProductDetail.equals("productDetail")) {
//                Intent intent = new Intent(getContext(), GoalsActivities.class);
//                intent.putExtra("product_location", sharedPreference.getIntValue(PrefsHelper.PRODUCT_LOC));
//                intent.putExtra("product_id", sharedPreference.getIntValue(PrefsHelper.PRODUCT_ID_FOR_DETAIL));
//                intent.putExtra("brand_name", "" + sharedPreference.getStringValue(PrefsHelper.BRAND_NAME));
//                intent.putExtra("category_id", sharedPreference.getIntValue(PrefsHelper.CATEGORYID) + "");
//                intent.putExtra("nameActivity", "ProductDetail");
//                startActivity(intent);
                Intent intent = new Intent(baseActivity, GoalsActivities.class);
                intent.putExtra("nameActivity", "AllProduct");
                startActivity(intent);
            }
        } else if (sharedPreference.getStringValue(PrefsHelper.NAVI_PRODUCT_BUSINESS).equals("product")) {
//            Intent intent = new Intent(getContext(), GoalsActivities.class);
//            intent.putExtra("product_location", sharedPreference.getIntValue(PrefsHelper.PRODUCT_LOC));
//            intent.putExtra("product_id", sharedPreference.getIntValue(PrefsHelper.PRODUCT_ID_FOR_DETAIL));
//            intent.putExtra("brand_name", "" + sharedPreference.getStringValue(PrefsHelper.BRAND_NAME));
//            intent.putExtra("category_id", sharedPreference.getIntValue(PrefsHelper.CATEGORYID) + "");
//            intent.putExtra("nameActivity", "ProductDetail");
//            startActivity(intent);
            Intent intent = new Intent(baseActivity, GoalsActivities.class);
            intent.putExtra("nameActivity", "AllProduct");
            startActivity(intent);
        } else if (sharedPreference.getStringValue(PrefsHelper.NAVI_PRODUCT_BUSINESS).equals("cartFragment")) {
            Intent intent = new Intent(baseActivity, GoalsActivities.class);
            intent.putExtra("nameActivity", "cart_fragment");
            startActivity(intent);
        } else {
            Intent intent1 = new Intent(baseActivity, DashboardActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
            baseActivity.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }


    public void setCheckInBtn(int position) {
        if (checkedInStatus) {
            ProductInCart.businessID = String.valueOf(mAdapter.filterList.get(position).getBusinessID());
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
                    GoalsActivities.homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));

                    Bundle data = new Bundle();
                    data.putString("business_loc", String.valueOf(position));
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

            if (Connectivity.isNetworkAvailable(baseActivity)) {
//                Log.e("NO_conne", "::::::");
//                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//                receiver = new NetworkChangeReceiver();
//                getActivity().registerReceiver(receiver, filter);
                if (checkedInStatus) {
//                    Log.e("checkedInStatus", "::::::true");
                    Toast.makeText(ApplicationClass.getAppContext(), "You have already checkin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(baseActivity, GoalsActivities.class);
                    intent.putExtra("nameActivity", "AllProduct");
                    intent.putExtra("backoncheckin", "true");
                    Log.d("successfully checkin", "checkined");
                    startActivity(intent);
                    baseActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } else {
                    checkInApiCall(position);
                }
            } else {

//                Log.e("checkedInStatus", "::::::false_else case");
                if (checkedInStatus) {
                    Toast.makeText(ApplicationClass.getAppContext(), "You have already checkin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(baseActivity, GoalsActivities.class);
                    intent.putExtra("nameActivity", "AllProduct");
                    intent.putExtra("backoncheckin", "true");
                    Log.d("successfully checkin", "checkined");
                    startActivity(intent);
                    baseActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } else {
//                    Log.e("checkedInStatus", "::::::false");
                    Toast.makeText(ApplicationClass.getAppContext(), "You are not checked in,you need Internet connection for check in ", Toast.LENGTH_SHORT).show();


                }

            }
        }
    }

    Gson builder;
    ServiceHandler serviceHandler;
    CheckedDataHit checkedDataHit;


    private void checkInApiCall(final int itemposition) {
        if (Connectivity.isNetworkAvailable(ApplicationClass.getAppContext())) {
            builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                    .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
            serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
            try {
                JSONObject object = new JSONObject();
                object.put("UserCheckInId", 0);
                object.put("UserProfileID", sharedPreference.getStringValue(PrefsHelper.USER_ID));
                object.put("Longitude", (Singleton.instance.SLNG));
                object.put("Latitude", (Singleton.instance.SLAT));
                object.put("CheckInDate", "2017-04-13T06:54:04.543Z");
                object.put("BusinessID", businessObject.getBusinessID());

//                Log.e("OBJECT", "::::" + object.toString());

                checkedDataHit = builder.fromJson(object.toString(), CheckedDataHit.class);
                serviceHandler.checkedInSave(checkedDataHit, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        String arr = CommonUtils.getServerResponse(response);
                        sharedPreference.saveBooleanValue("IsBusinessDetailFrag", true);

                        try {
                            JSONObject jsonArr = new JSONObject(arr);
                            if (jsonArr.getString("Message").equals("Success")) {
                                ProductInCart.businessID = String.valueOf(businessObject.getBusinessID());

                                if (ProductInCart.listAll(ProductInCart.class).size() != 0) {

                                    if (sharedPreference.getStringValue(PrefsHelper.NAVI_PRODUCT_BUSINESS).equals("business")) {
                                        Toast.makeText(baseActivity, "You have already checked in ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(baseActivity, GoalsActivities.class);
                                        intent.putExtra("nameActivity", "AllProduct");
                                        intent.putExtra("businesscheckin", "true");
//                                        Log.e("ALLBUSINESS", "1");
                                        startActivity(intent);

                                    } else {
//                                        Log.e("ALLBUSINESS", "2");

                                        FragmentTransaction ft = ((GoalsActivities) baseActivity).getSupportFragmentManager().beginTransaction();
                                        ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                                        Bundle data = new Bundle();
                                        data.putString("moveto", "business");
                                        data.putString("business_loc", String.valueOf(itemposition));
                                        GoalsActivities.homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));

                                        // sharedPreference.saveStringData(PrefsHelper.NAVI_PRODUCT_BUSINESS, "business");
                                        CartFragment fragment = new CartFragment();
                                        fragment.setArguments(data);
                                        ft.replace(R.id.container, fragment);
                                        ft.commit();
                                    }

                                } else {
//                                    Log.e("ALLBUSINESS", "3");


                                    Intent intent = new Intent(baseActivity, GoalsActivities.class);
                                    intent.putExtra("nameActivity", "AllProduct");
                                    intent.putExtra("businesCheckIn", "true");
                                    Log.d("successfully checkin", "checkined");
                                    startActivity(intent);
                                    if (baseActivity != null)
                                        baseActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                                }
                                checkedInStatus = true;
                                businessObject.setCheckedIN(true);
                                businessObject.save();
                            } else {
                                Log.d("unsuccessful", "data not found in response of api ");
                                Toast.makeText(baseActivity, "No data found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(baseActivity, "You need Internet connection for check-in", Toast.LENGTH_SHORT).show();
                        CommonUtils.dismissProgress();
                        //Log.d("checkInApiCall Retrofit", "checkin error");
                        //  Toast.makeText(getContext(), "checkInApiCall Retrofit", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(baseActivity, "Please do login again", Toast.LENGTH_SHORT).show();
            }
            CommonUtils.dismissProgress();
        } else {
            Toast.makeText(baseActivity, "You need Internet connection for check-in", Toast.LENGTH_SHORT).show();
            CommonUtils.dismissProgress();

        }

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.filter(newText);
        return true;
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
                        if (!isAsyncTask) {
//                          Log.e("###", "::::::Async calling...");
                            getAllBusiness();


                        }
                    }
                    if (ApplicationClass.getAppContext() != null)
                        LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);


                }
            } else {
                listAllBusiness = AllBusiness.listAll(AllBusiness.class);


                if (listAllBusiness != null && listAllBusiness.size() > 0) {
                    if (!cityList.contains("Select City")) {
                        cityList.add("Select City");
                    }
                    if (!statesList.contains("Select State")) {
                        statesList.add("Select State");
                    }
                    for (int k = 0; k < listAllBusiness.size(); k++) {
                        AllBusiness business = listAllBusiness.get(k);
                        if (business.getCity() != null) {
                            if (!cityList.contains(business.getCity())) {
                                cityList.add(business.getCity() + "");
                            }
                        }

                        if (business.getState() != null) {
                            if (!statesList.contains(business.getState())) {
                                statesList.add(business.getState() + "");
                            }
                        }
                    }

                    mAdapter.setAllBusinessList(listAllBusiness);
                    buisnessRV.setAdapter(mAdapter);

                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    if (statesList.size() != 0) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, statesList);
                        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        stateSpn.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                        stateSpn.setSelection(0);
                    }

                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(baseActivity);
                    builder1.setMessage("Sorry..Detail not available.Please contact your Administrator");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            //    Toast.makeText(getContext(), "Back is pressed", Toast.LENGTH_LONG).show();
                            if (mProgressDialog != null && mProgressDialog.isShowing())
                                mProgressDialog.dismiss();

                        }
                    });
                    builder1.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            //  getAllBusiness();
                            getAllBusiness();
                        }
                    });
                    builder1.show();
                }
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
                                sharedPreference.saveBooleanValue("IsAllBusinessfrag", true);
                                //Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                                if (isNetworkAvailable(ApplicationClass.getAppContext())) {
                                    if (!isAsyncTask) {
//                                        Log.e("###", "::::::Async calling...");
                                        getAllBusiness();


                                    }
                                }
                                if (ApplicationClass.getAppContext() != null)
                                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);


                            }
                            return true;
                        }
                    }
                }
            }
            listAllBusiness = AllBusiness.listAll(AllBusiness.class);


            if (listAllBusiness != null && listAllBusiness.size() > 0) {
                if (!cityList.contains("Select City")) {
                    cityList.add("Select City");
                }
                if (!statesList.contains("Select State")) {
                    statesList.add("Select State");
                }
                for (int k = 0; k < listAllBusiness.size(); k++) {
                    AllBusiness business = listAllBusiness.get(k);
                    if (business.getCity() != null) {
                        if (!cityList.contains(business.getCity())) {
                            cityList.add(business.getCity() + "");
                        }
                    }

                    if (business.getState() != null) {
                        if (!statesList.contains(business.getState())) {
                            statesList.add(business.getState() + "");
                        }
                    }
                }

                mAdapter.setAllBusinessList(listAllBusiness);
                buisnessRV.setAdapter(mAdapter);

                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (statesList.size() != 0) {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, statesList);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    stateSpn.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();
                    stateSpn.setSelection(0);
                }

            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(baseActivity);
                builder1.setMessage("Sorry..Detail not available.Please contact your Administrator");
                builder1.setCancelable(false);
                builder1.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        //    Toast.makeText(getContext(), "Back is pressed", Toast.LENGTH_LONG).show();
                        if (mProgressDialog != null && mProgressDialog.isShowing())
                            mProgressDialog.dismiss();

                    }
                });
                builder1.setNegativeButton("Reload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        //  getAllBusiness();
                        getAllBusiness();
                    }
                });
                builder1.show();
            }
            isConnected = false;
            return false;
        }
    }


    boolean isAsyncTask = false;
    private ProgressDialog mProgressAsynctAsk;

    public void showDialog() {


        if (mProgressAsynctAsk != null && !mProgressAsynctAsk.isShowing()) {
            mProgressAsynctAsk.show();
            Log.e("showDialog", "true");


        }
    }

    public void hideDialog() {


        if (mProgressAsynctAsk != null && mProgressAsynctAsk.isShowing()) {
            mProgressAsynctAsk.dismiss();
//            Log.e("hideDialog", "true");

        }
    }


}
