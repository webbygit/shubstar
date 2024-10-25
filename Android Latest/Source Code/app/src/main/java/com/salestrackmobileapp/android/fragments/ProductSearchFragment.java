package com.salestrackmobileapp.android.fragments;

import android.app.Activity;
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
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.salestrackmobileapp.android.adapter.AllProductAdapter;
import com.salestrackmobileapp.android.custome_views.Custome_EditText;
import com.salestrackmobileapp.android.gson.AllProduct;
import com.salestrackmobileapp.android.gson.AllVariants;
import com.salestrackmobileapp.android.gson.UOMArray;
import com.salestrackmobileapp.android.gson.VariantProduct;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.ProductPriceList;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.salestrackmobileapp.android.activities.GoalsActivities.actionBarTitle;


public class ProductSearchFragment extends BaseFragment implements RecyclerClick, SearchView.OnQueryTextListener {

    @BindView(R.id.search_spn_ll)
    LinearLayout searchSpinnerLayout;
    @BindView(R.id.product_rv)
    RecyclerView productRv;
    @BindView(R.id.search_cat_spn)
    Spinner searchCatSpn;
    @BindView(R.id.search_subcat_spn)
    Spinner searchSubcatSpn;
    @BindView(R.id.search_et)
    Custome_EditText searchEt;
    SearchView mSearchView;
    @BindView(R.id.view_item_btn)
    Button viewItemBtn;
//    @BindView(R.id.nested_scroll_view)
//    NestedScrollView nestedScrollView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private AllProductAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    static List<String> categories = new ArrayList<String>();
    static List<String> categoriesids = new ArrayList<String>();
    static List<String> brandArrayList = new ArrayList<String>();
    static List<Integer> brandIdList = new ArrayList<Integer>();
    List<AllProduct> listAllProduct;
    List<ProductPriceList> listProductPriceList = new ArrayList<>();
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    Integer categoryID = 0, brandId = 0;

    String brandName = "SelectBrand";

    static  int  catPosition = 0;
   static int subcatePosition = 0;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    String searchSt;
    @BindView(R.id.brand_spinner_layout)
    LinearLayout brandSpinnerLayout;
    CoordinatorLayout coordinateLayout;

    List<AllProduct> listProduct = new ArrayList<AllProduct>();
    private ProgressDialog mProgressDialog;


    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    IntentFilter filter;
    EditText searchEditText;


    List<String> listUomArray = new ArrayList<String>();
    List<Integer> listUomIdArray = new ArrayList<Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragments
        View view = inflater.inflate(R.layout.fragment_product_search, container, false);
        ButterKnife.bind(this, view);
        coordinateLayout = (CoordinatorLayout) view.findViewById(R.id.coordinateLayout);


        listUomArray.add("Select..");
        mProgressDialog = new ProgressDialog(baseActivity);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();

        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        try {
            if (ApplicationClass.getAppContext() != null)
                LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).registerReceiver(receiver, filter);
        } catch (Exception e) {
//            Log.e("EXCEPTION:", ":::::::::" + e.toString());
        }


        mSearchView = (SearchView) view.findViewById(R.id.searchview);
        EditText searchEditText = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.grey));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    if (ApplicationClass.getAppContext() != null)
                        LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).registerReceiver(receiver, filter);
                } catch (Exception e) {
                }
               /// searchCatSpn.setSelection(0);
              ///  searchSubcatSpn.setSelection(0);
                searchEt.setText("");
                swipeRefreshLayout.setRefreshing(false);
                getAllProduct();
                getAllvariants();
            }
        });
        if (!brandArrayList.contains("SelectBrand")) {
            brandArrayList.add("SelectBrand");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, brandArrayList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        searchSubcatSpn.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
      ///  searchSubcatSpn.setSelection(0);
       /// searchCatSpn.setSelection(0);


        searchCatSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("position","::::"+position);
//                Log.e("parent.position",":::::"+parent.getSelectedItemPosition());

                catPosition = position;

//                showDialog();


                if (position >= 1) {
                    categoryID = Integer.valueOf(categoriesids.get(position - 1).toString());
                    viewProductItems();
//                    brandSpinnerLayout.setVisibility(View.VISIBLE);
//
//                    brandArrayList.clear();
//                    brandIdList.clear();
//                    brandName = "SelectBrand";
//                    brandArrayList.add("SelectBrand");
//                    List<AllProduct> listProduct = Select.from(AllProduct.class).where(Condition.prop("product_category_id").eq(categoryID)).list();
//                    for (int k = 0; k < listProduct.size(); k++) {
//                        if (!brandArrayList.contains(listProduct.get(k).getBrandName())) {
//                            brandArrayList.add(listProduct.get(k).getBrandName());
//
//                            try {
//                                brandIdList.add(listProduct.get(k).getBrandID());
//
//
//                                hideDialog();
//
//
//                            } catch (Exception ec) {
//                                ec.printStackTrace();
//
//
//                                hideDialog();
//
//                            }
//                        }
//
//
//                        hideDialog();
//                    }
//                    if (brandIdList.size() != 0) {
//                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.single_text_item, brandArrayList);
//                        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//                        searchSubcatSpn.setAdapter(dataAdapter);
//                        dataAdapter.notifyDataSetChanged();
//                        searchSubcatSpn.setSelection(0);
//                        viewProductItems();
//                    }


//                    hideDialog();
                } else {
                    categoryID = 0;


                    viewProductItems();

//                    hideDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Log.e("onNothingSelected()","1");


                hideDialog();
            }

        });


        searchSubcatSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 1) {


//                    hideDialog();
                    subcatePosition = position;
                    // brandId = brandIdList.get(position - 1);
                    brandName = brandArrayList.get(position);
                    viewProductItems();


                } else {
                    brandName = "SelectBrand";
                    viewProductItems();
//                    hideDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


                hideDialog();

            }
        });
        mAdapter = new AllProductAdapter(baseActivity, this, productRv);
        mLayoutManager = new LinearLayoutManager(baseActivity);
        productRv.setLayoutManager(mLayoutManager);


        setupSearchView();

        productRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = productRv.getChildCount();
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
        if (!isAllProduct) {
            searchCatSpn.setSelection(catPosition);
            searchCatSpn.setSelection(catPosition);
            getAllProduct();
        }
//        if (!isAllVariant) {
//            getAllvariants();
//
//        }

        //  getAllvariants();
     /*   searchCatSpn.setSelection(catPosition);
        searchSubcatSpn.setSelection(subcatePosition);*/

        return view;
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setFocusableInTouchMode(true);
        mSearchView.setQueryHint("Search product by name..");
        //   mSearchView.requestFocusFromTouch();
    }

    @OnClick(R.id.view_item_btn)
    public void viewProductItems() {
        searchSt = searchEt.getText().toString();
//        showDialog();


//        Log.e("viewproductItems","::0");
//        Log.e("CategoryID","::::"+categoryID);
        if (categoryID == 0 && brandName.equals("SelectBrand")) {

            listAllProduct = AllProduct.listAll(AllProduct.class);
//            Log.e("viewproductItems","::1");


//            hideDialog();
        } else {
//            Log.e("viewproductItems","::2");


            listAllProduct = new ArrayList<AllProduct>();
            if (searchSt.equals("") || searchSt.isEmpty()) {
                if (brandName.equals("SelectBrand")) {
//                    Log.e("viewproductItems","::3");
                    listProduct = Select.from(AllProduct.class).where(Condition.prop("product_category_id").eq(categoryID)).list();
//                    hideDialog();
                } else {
                    if (categoryID != 0) {
//                        Log.e("viewproductItems","::4");
                        if (brandName.equals("SelectBrand")) {
                            listProduct = Select.from(AllProduct.class).where(Condition.prop("product_category_id").eq(categoryID)).list();

                        } else {

                            listProduct = Select.from(AllProduct.class).where(Condition.prop("product_category_id").eq(categoryID), Condition.prop("brand_name").eq(brandName)).list();
                        }
//                        hideDialog();
                    } else {
//                        Log.e("viewproductItems","::5");
                        listProduct = Select.from(AllProduct.class).where(Condition.prop("brand_name").eq(brandName)).list();
//                        hideDialog();
                    }
                }
            } else {
                if (brandName.equals("SelectBrand")) {
//                    Log.e("viewproductItems","::6");
                    listProduct = Select.from(AllProduct.class).where(Condition.prop("product_category_id").eq(categoryID)).list();
//                    hideDialog();
                } else {
                    if (categoryID != 0) {
//                        Log.e("viewproductItems","::7");
                        if (brandName.equals("SelectBrand")) {
                            listProduct = Select.from(AllProduct.class).where(Condition.prop("product_category_id").eq(categoryID)).list();

                        } else {
                            listProduct = Select.from(AllProduct.class).where(Condition.prop("product_category_id").eq(categoryID), Condition.prop("brand_name").eq(brandName), Condition.prop("product_name").eq(searchSt)).list();
                        }
//                        hideDialog();
                    } else {
//                        Log.e("viewproductItems","::8");
                        listProduct = Select.from(AllProduct.class).where(Condition.prop("brand_name").eq(brandName), Condition.prop("product_name").eq(searchSt)).list();
//                        hideDialog();

                    }
                }
            }

            if (listProduct.size() != 0) {
                if (!searchSt.equals("")) {

                    for (AllProduct allProduct : listProduct) {
                        if (allProduct.getProductName().contains(searchSt)) {
                            listAllProduct.add(allProduct);
                        } else {
                            Toast.makeText(baseActivity, "search item not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    listAllProduct = listProduct;

                }


                productRv.setVisibility(View.VISIBLE);
                mSearchView.setVisibility(View.VISIBLE);
//                hideDialog();

            } else {
                Toast.makeText(baseActivity, "Item not found.", Toast.LENGTH_SHORT).show();
            }


//            hideDialog();
        }


//        hideDialog();
        mAdapter.setListProduct(listAllProduct, listUomArray, listUomIdArray);
        productRv.setAdapter(mAdapter);
    }

    boolean isProductData = false;

    public void getAllProduct() {

        isAllProduct = true;
        brandSpinnerLayout.setVisibility(View.VISIBLE);
        brandName = "SelectBrand";


        if (!categories.contains("SelectCategory")) {
            categories.add("SelectCategory");
        }
        if (!brandArrayList.contains("SelectBrand")) {
            brandArrayList.add("SelectBrand");
        }
        if (Connectivity.isNetworkAvailable(baseActivity)) {
            showDialog();
//            Log.e("showDialog()","2");


            AllProduct.deleteAll(AllProduct.class);

            final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                    .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
            ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
            serviceHandler.getAllProduct(new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    String arr = CommonUtils.getServerResponse(response);
                    AllProduct.deleteAll(AllProduct.class);
                    sharedPreference.saveBooleanValue("IsProductSearchFrag", true);
                    try {
                        JSONArray jsonArr = new JSONArray(arr);
                        for (int i = 0; i < jsonArr.length(); i++) {
                            AllProduct alproduct = builder.fromJson(jsonArr.get(i).toString(), AllProduct.class);
                            if (alproduct.getDefaultPrice() == null) {
                                alproduct.setDefaultPrice(0.0);
                            }
                            if (alproduct.getSalePrice() == null) {
                                alproduct.setSalePrice(0.0);
                            }
                            if (alproduct.getPurchasePrice() == null) {
                                alproduct.setPurchasePrice(0.0);
                            }

                            if (alproduct.getProductCategoryID() != 0) {
                                for (int j = 0; j < categories.size(); j++) {
                                    String categoryName = categories.get(j);
                                    categories.contains(categoryName);
                                    if (!categories.contains(alproduct.getProductCategoryName())) {
                                        categoriesids.add(alproduct.getProductCategoryID() + "");
                                        categories.add(alproduct.getProductCategoryName() + "");
                                    }
                                }
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, categories);
                                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                searchCatSpn.setAdapter(dataAdapter);
                                dataAdapter.notifyDataSetChanged();

                            }


                            if (alproduct.getBrandID() != 0) {

                                for (int k = 0; k < brandArrayList.size(); k++) {
                                    if (!brandArrayList.contains(alproduct.getBrandName())) {
                                        brandArrayList.add(alproduct.getBrandName());

                                        try {
                                            brandIdList.add(alproduct.getBrandID());


                                        } catch (Exception ec) {
                                            ec.printStackTrace();


                                        }
                                    }


                                }

                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, brandArrayList);
                                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                searchSubcatSpn.setAdapter(dataAdapter);
                                dataAdapter.notifyDataSetChanged();


                            }


                            int productId = alproduct.getProductID();


                            alproduct.save();
                            //getProductPriceList(productId);

                        }
                        listAllProduct = AllProduct.listAll(AllProduct.class);
                        List<UOMArray> uomArray = Select.from(UOMArray.class).list();
                        for (UOMArray uomArray1 : uomArray) {
                            if (listUomArray.contains(uomArray1.getUOM())) {

                            } else {
                                listUomArray.add(uomArray1.getUOM() + "");
                                listUomIdArray.add(uomArray1.getUOMID());
                            }
                        }

                        mAdapter.setListProduct(listAllProduct, listUomArray, listUomIdArray);
                        productRv.setAdapter(mAdapter);
                        searchCatSpn.setSelection(catPosition);
                       searchSubcatSpn.setSelection(subcatePosition);

                        hideDialog();

                    } catch (Exception ex) {
                        ex.printStackTrace();


                        hideDialog();
                    }

                    if (!isAllVariant) {
                        getAllvariants();

                    }
                }


                @Override
                public void failure(RetrofitError error) {

                    if (error.getMessage().equals("timeout")) {
                        getAllProduct();
                    } else {


                        listAllProduct = AllProduct.listAll(AllProduct.class);
                        if (listAllProduct != null && listAllProduct.size() > 0) {
                            for (AllProduct allproduct : listAllProduct) {
                                if (!categories.contains(allproduct.getProductCategoryName())) {
                                    categories.add(allproduct.getProductCategoryName());
                                    categoriesids.add(allproduct.getProductCategoryID() + "");
                                }
                            }


                            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, categories);
                            dataAdapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            searchCatSpn.setAdapter(dataAdapter1);
                            dataAdapter1.notifyDataSetChanged();
                           /// searchCatSpn.setSelection(0);


                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, brandArrayList);
                            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            searchSubcatSpn.setAdapter(dataAdapter);
                            dataAdapter.notifyDataSetChanged();
                            searchSubcatSpn.setSelection(subcatePosition);


                            List<UOMArray> uomArray = Select.from(UOMArray.class).list();
//                            Log.e("uomArray", ":::::" + uomArray.size());
                            if (uomArray != null && uomArray.size() > 0) {
                                for (UOMArray uomArray1 : uomArray) {
//                                    Log.e("uomArray", ":::::for");

                                    if (listUomArray.contains(uomArray1.getUOM())) {
//                                        Log.e("uomArray", ":::::if");

                                    } else {

//                                        Log.e("uomArray", ":::::else");


                                        listUomArray.add(uomArray1.getUOM() + "");
                                        listUomIdArray.add(uomArray1.getUOMID());
                                    }
                                }
                            }
                            mAdapter.setListProduct(listAllProduct, listUomArray, listUomIdArray);
                            productRv.setAdapter(mAdapter);


                        } else {
                            if (baseActivity != null) {
                                Toast.makeText(baseActivity, "No Product data available", Toast.LENGTH_SHORT).show();
                            }
                        }


                        hideDialog();
                    }


                    hideDialog();
                }

            });

        } else {
            hideDialog();

            listAllProduct = AllProduct.listAll(AllProduct.class);
            if (listAllProduct != null && listAllProduct.size() > 0) {
                for (AllProduct allproduct : listAllProduct) {
                    if (!categories.contains(allproduct.getProductCategoryName())) {
                        categories.add(allproduct.getProductCategoryName());
                        categoriesids.add(allproduct.getProductCategoryID() + "");
                    }
                }


                ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, categories);
                dataAdapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                searchCatSpn.setAdapter(dataAdapter1);
                dataAdapter1.notifyDataSetChanged();
              ///  searchCatSpn.setSelection(0);


                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, brandArrayList);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                searchSubcatSpn.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
                searchSubcatSpn.setSelection(subcatePosition);

                List<UOMArray> uomArray = Select.from(UOMArray.class).list();
//                Log.e("uomArray", ":::::" + uomArray.size());
                if (uomArray != null && uomArray.size() > 0) {
                    for (UOMArray uomArray1 : uomArray) {
//                    Log.e("uomArray", ":::::for");

                        if (listUomArray.contains(uomArray1.getUOM())) {
//                        Log.e("uomArray", ":::::if");

                        } else {

//                        Log.e("uomArray", ":::::else");


                            listUomArray.add(uomArray1.getUOM() + "");
                            listUomIdArray.add(uomArray1.getUOMID());
                        }
                    }
                }

                mAdapter.setListProduct(listAllProduct, listUomArray, listUomIdArray);
                productRv.setAdapter(mAdapter);


            } else {
                Toast.makeText(baseActivity, "No Product data available", Toast.LENGTH_SHORT).show();
            }

//            if (!isAllVariant) {
//                getAllvariants();
//            }

        }


    }


    public void onBackPressed() {
//        Log.e("onBackPressed","::true");


        if (sharedPreference.getStringValue(PrefsHelper.NAVI_PRODUCT_BUSINESS).equals("business")) {
//            Log.e("onBackPressed","::1");


            Intent intent = new Intent(baseActivity, GoalsActivities.class);
            intent.putExtra("nameActivity", "AllBusinessDashBoard");
            startActivity(intent);
            baseActivity.overridePendingTransition(R.anim.slide_enter, R.anim.slide_enter);
        } else if (sharedPreference.getStringValue(PrefsHelper.NAVI_PRODUCT_BUSINESS).equals("CheckinBusiness")) {

//            this change is asked by sunil for ticket 270.
//            actionBarTitle.setText("BUSINESS");
//
//            CheckInPriOrderFragment fragment = new CheckInPriOrderFragment();
//            FragmentTransaction ft = ((GoalsActivities) getContext()).getSupportFragmentManager().beginTransaction();
//            ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
//            ft.replace(R.id.container, fragment);
//            ft.commit();
//            Log.e("onBackPressed","::2");
            Intent intent = new Intent(getContext(), GoalsActivities.class);
            intent.putExtra("nameActivity", "AllBusinessDashBoard");
            startActivity(intent);
            baseActivity.overridePendingTransition(R.anim.slide_enter, R.anim.slide_enter);
        } else {
//            Log.e("onBackPressed","::3");
            Intent intent = new Intent(baseActivity, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            baseActivity.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }


    }

    @Override
    public void productClick(View v, int position) {

        //getMeasurementValues();
        int itemPosition = productRv.getChildPosition(v);
        listAllProduct.get(itemPosition);
        Intent intent = new Intent(baseActivity, GoalsActivities.class);

        if (mSearchView.getQuery().toString().equals("")) {

        } else {
            mAdapter.filterList.get(itemPosition).getProductName();
            intent.putExtra("product_name", mAdapter.filterList.get(itemPosition).getProductName());
        }
        intent.putExtra("product_location", itemPosition);
        intent.putExtra("product_id", listAllProduct.get(itemPosition).getProductID());

//        Log.e("UOM_ID", "::on click product" + listAllProduct.get(itemPosition).getDefaultUOMID());
        if (searchSubcatSpn.getSelectedItem() != null) {
            if (searchSubcatSpn.getSelectedItem().equals("SelectBrand")) {
                intent.putExtra("brand_name", "");
            } else {
                intent.putExtra("brand_name", "" + brandName);
            }
        } else {
            intent.putExtra("brand_name", "");
        }
        if (searchCatSpn.getSelectedItem() != null) {
            if (searchCatSpn.getSelectedItem().equals("SelectCategory")) {
                intent.putExtra("category_id", "0");
                mAdapter.filterList.get(itemPosition).getProductName();
                if (!searchSubcatSpn.getSelectedItem().equals("SelectBrand")) {
                    intent.putExtra("product_name", mAdapter.filterList.get(itemPosition).getProductName());
                }
            } else {
                intent.putExtra("category_id", categoryID + "");
            }
        } else {
            intent.putExtra("category_id", categoryID + "");
        }
        intent.putExtra("nameActivity", "ProductDetail");
        startActivity(intent);
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

    public void getMeasurementValues() {

        showDialog();
//        Log.e("showDialog()","3");

        UOMArray.deleteAll(UOMArray.class);
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(baseActivity, ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.getAllMeasurement(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String arr = CommonUtils.getServerResponse(response);
                try {
                    JSONArray jsonArr = new JSONArray(arr);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        UOMArray uomArray = builder.fromJson(jsonArr.get(i).toString(), UOMArray.class);
                        uomArray.save();
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

                hideDialog();
//                if (AllProduct.listAll(AllProduct.class).size() != 0) {
//                    listAllProduct = AllProduct.listAll(AllProduct.class);
//                    mAdapter.setListProduct(listAllProduct, listUomArray, listUomIdArray);
//                    productRv.setAdapter(mAdapter);
//
//                    for (AllProduct allproduct : listAllProduct) {
//                        if (!categories.contains(allproduct.getProductCategoryName())) {
//                            categories.add(allproduct.getProductCategoryName());
//                            categoriesids.add(allproduct.getProductCategoryID()+"");
//                        }
//                    }
//
//
//                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.single_text_item, categories);
//                    dataAdapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
//                    searchCatSpn.setAdapter(dataAdapter1);
//                    dataAdapter1.notifyDataSetChanged();
//                    searchCatSpn.setSelection(0);
//
//
//                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.single_text_item, brandArrayList);
//                    dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
//                    searchSubcatSpn.setAdapter(dataAdapter);
//                    dataAdapter.notifyDataSetChanged();
//                    searchSubcatSpn.setSelection(0);
//
//                } else {
//                    Toast.makeText(getContext(), "No data avilable", Toast.LENGTH_SHORT).show();
//                }
            }
        });


//
//        CommonUtils.dismissProgress();
    }

    public void getAllvariants() {
        isAllVariant = true;

        showDialog();
//        Log.e("showDialog()","4");
        if (Connectivity.isNetworkAvailable(baseActivity)) {

            AllVariants.deleteAll(AllVariants.class);
            VariantProduct.deleteAll(VariantProduct.class);


            final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                    .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
            ServiceHandler serviceHandler = NetworkManager.createRetrofitService(baseActivity, ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
            serviceHandler.getAllVariants(new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    String arr = CommonUtils.getServerResponse(response);


                    AllVariants.deleteAll(AllVariants.class);
                    VariantProduct.deleteAll(VariantProduct.class);

                    try {
                        JSONArray jsonArr = new JSONArray(arr);
                        for (int i = 0; i < jsonArr.length(); i++) {

                            AllVariants allVariants = builder.fromJson(jsonArr.get(i).toString(), AllVariants.class);
                            JSONObject jsonObject = (JSONObject) jsonArr.get(i);

                            if (jsonObject.has("product")) {
                                if (!jsonObject.isNull("product")) {


                                    VariantProduct variantProduct = builder.fromJson(jsonObject.getJSONObject("product").toString(), VariantProduct.class);
                                    variantProduct.save();
                                }

                            }
                            allVariants.save();

                        }


                        hideDialog();

                    } catch (Exception ex) {
                        ex.printStackTrace();


                        hideDialog();
                    }
                }


                @Override
                public void failure(RetrofitError error) {

                    if (error.getMessage().equals("timeout")) {
                        getAllvariants();
                    } else {


                        hideDialog();
                    }


                    hideDialog();
                }

            });

        } else {


            hideDialog();

        }


    }


    @Override
    public void onResume() {
        super.onResume();
//        listAllProduct = AllProduct.listAll(AllProduct.class);
//        if (listAllProduct.size() != 0) {
//            mAdapter.setListProduct(listAllProduct, listUomArray, listUomIdArray);
//            productRv.setAdapter(mAdapter);
//
//        }
//        searchCatSpn.setSelection(0);
//        searchSubcatSpn.setSelection(0);


    }

    boolean isAllProduct = false, isAllVariant = false;


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (Connectivity.isNetworkAvailable(context)) {
                if (!isConnected) {
                    isConnected = true;
                    sharedPreference.saveBooleanValue("IsProductSearchFrag", true);
                    // Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();

                    getMeasurementValues();
                    if (!isAllProduct) {
                        getAllProduct();

                    }
                    if (!isAllVariant) {
                        getAllvariants();
                    }
                    if (ApplicationClass.getAppContext() != null)
                        LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);

                    //do your processing here ---
                    //if you need to post any data to the server or get status
                    //update from the server

                } else {
                    if (ApplicationClass.getAppContext() != null)
                        LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);

                }
            } else {
                if (AllProduct.listAll(AllProduct.class).size() != 0) {
                    listAllProduct = AllProduct.listAll(AllProduct.class);
                    mAdapter.setListProduct(listAllProduct, listUomArray, listUomIdArray);
                    productRv.setAdapter(mAdapter);

                    for (AllProduct allproduct : listAllProduct) {
                        if (!categories.contains(allproduct.getProductCategoryName())) {
                            categories.add(allproduct.getProductCategoryName());
                            categoriesids.add(allproduct.getProductCategoryID() + "");
                        }
                    }


                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, categories);
                    dataAdapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    searchCatSpn.setAdapter(dataAdapter1);
                    dataAdapter1.notifyDataSetChanged();
                    //searchCatSpn.setSelection(0);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, brandArrayList);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    searchSubcatSpn.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();
                   searchSubcatSpn.setSelection(subcatePosition);

                } else {
                    Toast.makeText(baseActivity, "No data available", Toast.LENGTH_SHORT).show();
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
                                isConnected = true;
                                sharedPreference.saveBooleanValue("IsProductSearchFrag", true);
                                // Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();

                                getMeasurementValues();
                                if (!isAllProduct) {
                                    getAllProduct();

                                }
                                if (!isAllVariant) {
                                    getAllvariants();
                                }
                                if (ApplicationClass.getAppContext() != null)
                                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);

                                //do your processing here ---
                                //if you need to post any data to the server or get status
                                //update from the server

                            } else {
                                if (ApplicationClass.getAppContext() != null)
                                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);

                            }
                            return true;
                        }
                    }
                }
            } else {


//                if (!sharedPreference.getBooleanValue("IsProductSearchFrag")) {
//                    Toast.makeText(getActivity(), "You must visit every screen once after first time login with internet connected.", Toast.LENGTH_SHORT).show();
//
//                }
                if (AllProduct.listAll(AllProduct.class).size() != 0) {
                    listAllProduct = AllProduct.listAll(AllProduct.class);
                    mAdapter.setListProduct(listAllProduct, listUomArray, listUomIdArray);
                    productRv.setAdapter(mAdapter);

                    for (AllProduct allproduct : listAllProduct) {
                        if (!categories.contains(allproduct.getProductCategoryName())) {
                            categories.add(allproduct.getProductCategoryName());
                            categoriesids.add(allproduct.getProductCategoryID() + "");
                        }
                    }


                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, categories);
                    dataAdapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    searchCatSpn.setAdapter(dataAdapter1);
                    dataAdapter1.notifyDataSetChanged();
                    ///searchCatSpn.setSelection(0);


                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, brandArrayList);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    searchSubcatSpn.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();
                    searchSubcatSpn.setSelection(subcatePosition);

                } else {
                    Toast.makeText(context, "No data available", Toast.LENGTH_SHORT).show();
                }

            }
            isConnected = false;
            return false;

        }
    }

    public void showDialog() {

        {
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {

                if (!((Activity) baseActivity).isFinishing())
                    mProgressDialog.show();
//                    Log.e("showDialog","true");
            }

        }
    }

    public void hideDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
//            Log.e("hideDialog","true");


        }
    }

}
