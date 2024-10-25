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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.ApplicationClass;
import com.salestrackmobileapp.android.activities.DashboardActivity;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.ProductPagerAdapter;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.gson.AllProduct;
import com.salestrackmobileapp.android.gson.UOMArray;
import com.salestrackmobileapp.android.gson.VariantByProduct;
import com.salestrackmobileapp.android.gson.VariantProduct;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
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
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProductDetailFragment extends BaseFragment implements RecyclerClick {

    @BindView(R.id.product_view_pager)
    ViewPager productViewPager;
    @BindView(R.id.left_nav)
    ImageView leftNavImg;
    @BindView(R.id.right_nav)
    ImageView rightNavImg;

    public static Custome_BoldTextView continueTxt;

    ProductPagerAdapter pagerAdapter;
    List<AllProduct> allProductList;
    int location = 0, productId = 0;
    Integer categoryID = 0, dealId = 0;
    String brandName = "", dealType = "";
    String businessId;
    RecyclerView listDealOnProductRv;
    private ProgressDialog mProgressDialog;

    List<String> listUomArray = new ArrayList<String>();
    List<Integer> listUomIdArray = new ArrayList<Integer>();
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    IntentFilter filter;

    String dealFragment = "";
    boolean isMeasurmentValue = false;

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
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        ButterKnife.bind(this, view);
        GoalsActivities.totalAmt.setVisibility(View.VISIBLE);
        CommonUtils.variantID = 0;


        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        if (getContext() != null)
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
        listUomArray.add("Select..");
        //getMeasurementValues();
        listDealOnProductRv = (RecyclerView) view.findViewById(R.id.list_deal_rv);
        allProductList = AllProduct.listAll(AllProduct.class);
        System.out.println("size"+allProductList.size());
        continueTxt = (Custome_BoldTextView) view.findViewById(R.id.continue_txt);


        if (getArguments() != null) {


            location = getArguments().getInt("ProductLocation");
            productId = getArguments().getInt("product_id");
            categoryID = getArguments().getInt("category_id");
            brandName = getArguments().getString("brandname");

            if (getArguments().containsKey("listofdeals")) {
                dealFragment = getArguments().getString("listofdeals");
            }
            if (getArguments().containsKey("dealID")) {

            }
            if (productId == 0) {
                location = sharedPreference.getIntValue(PrefsHelper.PRODUCT_LOC);
                categoryID = sharedPreference.getIntValue(PrefsHelper.CATEGORYID);
                brandName = sharedPreference.getStringValue(PrefsHelper.BRAND_NAME);
                productId = sharedPreference.getIntValue(PrefsHelper.PRODUCT_ID_FOR_DETAIL);
            } else {
                sharedPreference.saveIntData(PrefsHelper.CATEGORYID, categoryID);
                sharedPreference.saveStringData(PrefsHelper.BRAND_NAME, brandName);
                sharedPreference.saveIntData(PrefsHelper.PRODUCT_ID_FOR_DETAIL, productId);
                sharedPreference.saveIntData(PrefsHelper.PRODUCT_LOC, location);
            }

            if (categoryID != 0) {
                allProductList.clear();
                if (brandName == null || brandName.equals("null")) {
                    brandName = "";
                    allProductList = Select.from(AllProduct.class).where(Condition.prop("product_category_id").eq(categoryID)).list();
                } else {
                    allProductList = Select.from(AllProduct.class).where(Condition.prop("product_category_id").eq(categoryID)).list();
                    System.out.println("brandName"+brandName);

                   // allProductList = Select.from(AllProduct.class).where(Condition.prop("product_category_id").eq(categoryID), Condition.prop("brand_name").eq(brandName)).list();
                }
            } else {
                if (getArguments().getString("product_name") != null) {
                    allProductList = Select.from(AllProduct.class).where(Condition.prop("product_name").eq(getArguments().getString("product_name"))).list();
                } else {
                    allProductList = Select.from(AllProduct.class).list();
                }
            }
        } else {

            allProductList = Select.from(AllProduct.class).where(Condition.prop("product_category_id").eq(sharedPreference.getIntValue(PrefsHelper.CATEGORYID)), Condition.prop("brand_name").eq(sharedPreference.getStringValue(PrefsHelper.BRAND_NAME))).list();
            System.out.println("size"+allProductList.size());
           // allProductList.get(0).getBrandName();
        }
        //getVariantsByProductId();
        if (allProductList.size()!= 0) {
            Toast.makeText(getContext(), " available", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "no detail available", Toast.LENGTH_SHORT).show();
            hideDialog();
            System.out.println("size"+allProductList.size());
            Intent inten = new Intent(getContext(), DashboardActivity.class);
            startActivity(inten);
            baseActivity.finish();
        }

        businessId = sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID);
        pagerAdapter = new ProductPagerAdapter(baseActivity, sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID));
        if (Connectivity.isNetworkAvailable(baseActivity)) {
            if (!isMeasurmentValue) {
                getMeasurementValues();
            }
        } else {
            List<UOMArray> uomArray = Select.from(UOMArray.class).list();
            for (UOMArray uomArray1 : uomArray) {
                if (listUomArray.contains(uomArray1.getUOM())) {

                } else {
                    listUomArray.add(uomArray1.getUOM() + "");
                    listUomIdArray.add(uomArray1.getUOMID());
                }
            }
            pagerAdapter.setListOfProduct(allProductList, listUomArray, listUomIdArray, location);
            productViewPager.setAdapter(pagerAdapter);

            productViewPager.setCurrentItem(location);

            pagerAdapter.notifyDataSetChanged();

        }

        leftNavImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = productViewPager.getCurrentItem();

                if (tab > 0) {
                    tab--;

                    productViewPager.setCurrentItem(tab);
                    pagerAdapter.notifyDataSetChanged();
                } else if (tab == 0) {


                    productViewPager.setCurrentItem(tab);
                    pagerAdapter.notifyDataSetChanged();
                }
            }

        });

        // Images right navigation
        rightNavImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = productViewPager.getCurrentItem();

                tab++;

                productViewPager.setCurrentItem(tab);
                pagerAdapter.notifyDataSetChanged();
            }
        });

        if (ProductInCart.listAll(ProductInCart.class).size() != 0) {
            // continueTxt.setVisibility(View.VISIBLE);
        } else {
            continueTxt.setVisibility(View.GONE);
        }
        return view;
    }

    public void onBackPressed() {
        if (!dealFragment.equals("")) {
            baseActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, new ListOfDealsProductFragment()).commit();
        } else {
            hideDialog();
            Intent intent = new Intent(baseActivity, GoalsActivities.class);
            intent.putExtra("nameActivity", "AllProduct");
            startActivity(intent);
        }
    }

    @OnClick(R.id.continue_txt)
    public void continueShopping() {

        if (!ProductPagerAdapter.uomSt.equals("")) {

            if (sharedPreference.getStringValue(PrefsHelper.BUSINESS_CHECKED).equals("true")) {

                if (businessId == null || businessId.equals("")) {


                    if (!(sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID).equals("") || sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID) == null)) {
                        Bundle data = new Bundle();
                        data.putString("product_location", String.valueOf(location));
                        data.putString("product_id", String.valueOf(productId));
                        data.putString("brand_name", brandName);
                        data.putString("category_id", String.valueOf(categoryID));
                        data.putString("nameActivity", "cart_fragment");
                        data.putString("moveto", "product");
                        FragmentTransaction ft = ((GoalsActivities) baseActivity).getSupportFragmentManager().beginTransaction();
                        ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                        CartFragment fragment = new CartFragment();
                        fragment.setArguments(data);
                        ft.replace(R.id.container, fragment);

                    } else {
                        hideDialog();
                        Intent intent = new Intent(baseActivity, GoalsActivities.class);
                        intent.putExtra("nameActivity", "AllBusiness");
                        intent.putExtra("navigateToProduct", "productDetail");
                        startActivity(intent);
                    }
                } else {


                    Bundle data = new Bundle();
                    data.putString("product_location", String.valueOf(location));
                    data.putString("product_id", String.valueOf(productId));
                    data.putString("brand_name", brandName);
                    data.putString("category_id", String.valueOf(categoryID));
                    data.putString("nameActivity", "cart_fragment");
                    data.putString("moveto", "product");
                    FragmentTransaction ft = ((GoalsActivities) baseActivity).getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                    CartFragment fragment = new CartFragment();
                    fragment.setArguments(data);
                    ft.replace(R.id.container, fragment);
                    ft.commit();
                }


            } else {
                hideDialog();
                Intent intent = new Intent(baseActivity, GoalsActivities.class);
                intent.putExtra("nameActivity", "AllBusiness");
                intent.putExtra("navigateToProduct", "productDetail");
                startActivity(intent);


//
//                Bundle data = new Bundle();
//                data.putString("product_location", String.valueOf(location));
//                data.putString("product_id", String.valueOf(productId));
//                data.putString("brand_name", brandName);
//                data.putString("category_id", String.valueOf(categoryID));
//                data.putString("nameActivity", "cart_fragment");
//                data.putString("moveto", "product");
//                FragmentTransaction ft = ((GoalsActivities) getContext()).getSupportFragmentManager().beginTransaction();
//                ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
//                CartFragment fragment = new CartFragment();
//                fragment.setArguments(data);
//                ft.replace(R.id.container, fragment);
//                ft.commit();
            }
        } else {
            Toast.makeText(baseActivity, "Please select quantity", Toast.LENGTH_SHORT).show();
        }


    }

    public void getMeasurementValues() {
        isMeasurmentValue = true;
        mProgressDialog = new ProgressDialog(baseActivity);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();
        UOMArray.deleteAll(UOMArray.class);
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getActivity(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.getAllMeasurement(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                UOMArray.deleteAll(UOMArray.class);
                String arr = CommonUtils.getServerResponse(response);

                try {
                    JSONArray jsonArr = new JSONArray(arr);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        String stringJson = jsonArr.get(i).toString();
                        UOMArray uomArray = builder.fromJson(stringJson, UOMArray.class);
                        uomArray.save();
                    }

                    if (listUomArray.size() != 0) {
                        listUomArray.clear();
                        listUomIdArray.clear();
                    }

                    listUomIdArray.add(0);
                    listUomArray.add("Select..");

                    List<UOMArray> uomArray = Select.from(UOMArray.class).list();
                    for (UOMArray uomArray1 : uomArray) {
                        if (listUomArray.contains(uomArray1.getUOM())) {

                        } else {
                            listUomArray.add(uomArray1.getUOM() + "");
                            listUomIdArray.add(uomArray1.getUOMID());
                        }
                    }
//                    Log.e("---pagerAdapter",":::::pagerAdapter POSITION:::"+location);

                    pagerAdapter.setListOfProduct(allProductList, listUomArray, listUomIdArray, location);


                    productViewPager.setOffscreenPageLimit(3);
                    productViewPager.setAdapter(pagerAdapter);
                    productViewPager.setCurrentItem(location);

                    //  pagerAdapter.notifyDataSetChanged();
                    hideDialog();

                } catch (Exception ex) {
//                    Log.e("getMeasurementValues",":::::"+ex.toString());

                    ex.printStackTrace();
                    hideDialog();
                }
            }


            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                List<UOMArray> uomArray = Select.from(UOMArray.class).list();
                for (UOMArray uomArray1 : uomArray) {
                    if (listUomArray.contains(uomArray1.getUOM())) {

                    } else {
                        listUomArray.add(uomArray1.getUOM() + "");
                        listUomIdArray.add(uomArray1.getUOMID());
                    }
                }
                hideDialog();
                pagerAdapter.setListOfProduct(allProductList, listUomArray, listUomIdArray, location);
                productViewPager.setAdapter(pagerAdapter);

                productViewPager.setCurrentItem(location);
                pagerAdapter.notifyDataSetChanged();
                //   Toast.makeText(getActivity(), "Please check your Internet Connectivity", Toast.LENGTH_SHORT).show();
                isConnected = false;

            }
        });

    }

    Gson builder;


    @Override
    public void productClick(View v, int position) {

    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (Connectivity.isNetworkAvailable(context)) {
                if (!isConnected) {

                    isConnected = true;
                    sharedPreference.saveBooleanValue("IsProductDetailFrag", true);
                    // Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();

                    if (!isMeasurmentValue) {
                        getMeasurementValues();
                    }
                    if (ApplicationClass.getAppContext() != null)
                        LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);                                //do your processing here ---
                    //if you need to post any data to the server or get status
                    //update from the server

                } else {
                    if (ApplicationClass.getAppContext() != null)
                        LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);                                //do your processing here ---


                }
            } else {
                List<UOMArray> uomArray = Select.from(UOMArray.class).list();
                for (UOMArray uomArray1 : uomArray) {
                    if (listUomArray.contains(uomArray1.getUOM())) {

                    } else {
                        listUomArray.add(uomArray1.getUOM() + "");
                        listUomIdArray.add(uomArray1.getUOMID());
                    }
                }
                pagerAdapter.setListOfProduct(allProductList, listUomArray, listUomIdArray, location);
                productViewPager.setAdapter(pagerAdapter);
                productViewPager.setCurrentItem(location);
                pagerAdapter.notifyDataSetChanged();
                //  Toast.makeText(context, "Please check your Internet Connectivity", Toast.LENGTH_SHORT).show();
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
                                sharedPreference.saveBooleanValue("IsProductDetailFrag", true);
                                // Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();

                                if (!isMeasurmentValue) {
                                    getMeasurementValues();
                                }
                                if (ApplicationClass.getAppContext() != null)
                                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);                                //do your processing here ---
                                //if you need to post any data to the server or get status
                                //update from the server

                            } else {
                                if (ApplicationClass.getAppContext() != null)
                                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).unregisterReceiver(receiver);                                //do your processing here ---


                            }
                            return true;
                        }
                    }
                }
            } else {

            }

//            if (!sharedPreference.getBooleanValue("IsProductDetailFrag")){
//                Toast.makeText(getActivity(),"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//
//            }


            List<UOMArray> uomArray = Select.from(UOMArray.class).list();
            for (UOMArray uomArray1 : uomArray) {
                if (listUomArray.contains(uomArray1.getUOM())) {

                } else {
                    listUomArray.add(uomArray1.getUOM() + "");
                    listUomIdArray.add(uomArray1.getUOMID());
                }
            }
            pagerAdapter.setListOfProduct(allProductList, listUomArray, listUomIdArray, location);
            productViewPager.setAdapter(pagerAdapter);
            productViewPager.setCurrentItem(location);
            pagerAdapter.notifyDataSetChanged();
            //  Toast.makeText(context, "Please check your Internet Connectivity", Toast.LENGTH_SHORT).show();
            isConnected = false;
            return false;
        }
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

}
