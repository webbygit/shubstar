package com.salestrackmobileapp.android.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.solver.Goal;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.orm.query.Condition;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.ApplicationClass;
import com.salestrackmobileapp.android.activities.DashboardActivity;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.CartAdapter;
import com.salestrackmobileapp.android.adapter.OffersDealAdapter;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllBusiness;
import com.salestrackmobileapp.android.gson.AllDeal;
import com.salestrackmobileapp.android.gson.GoalBusiness;
import com.salestrackmobileapp.android.gson.PendingOrderItem;
import com.salestrackmobileapp.android.gson.SaveOrder;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.services.LocationService;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


public class CartFragment extends BaseFragment implements RecyclerClick {

    @BindView(R.id.item_in_cart)
    RecyclerView itemCartRv;

    static
    Custome_TextView rowTotalAmount;
    public static
    Custome_TextView totalDiscountTv;
    public static
    Custome_BoldTextView totalAmountTv;

    @BindView(R.id.updated_ampunt_tv)
    Custome_BoldTextView updated_ampunt_tv;

    @BindView(R.id.business_name_txt)
    Custome_BoldTextView businessNameTxt;
    @BindView(R.id.business_contactperson_txt)
    Custome_TextView businessContactTxt;
    @BindView(R.id.business_contactnumber_txt)
    Custome_TextView businessContactNumberTxt;
    @BindView(R.id.btnApplyDeals)
    Button btnApplyDeals;
    NestedScrollView nested_scroll_view;
    Custome_TextView business_type_tv;

    //business_contactnumber_txt

    private LinearLayoutManager mLayoutManager, dealLayoutManager;
    CartAdapter cardAdapter;
    List<ProductInCart> cartList;
    private int year;
    private int month;
    private int day;
    SaveOrder saveOrderObject;
    static float subTotal = 0;
    public static double amount = 0;
    public static double discount = 0;
    int location = 0, productId = 0;
    Integer categoryID = 0;
    String brandName = "";
    static double rawAmount = -1;
    static float sumVat = -1;
    public static double rowAmountTax = -1;

    GoalBusiness goalBusiness;
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;

    static int orderIdInt = 0;
    String businessLoc = "";
    static String moveTo = "";
    List<SaveOrder> listSaveorderArray;
    @BindView(R.id.offers_deals)
    RecyclerView offersDeal;

    OffersDealAdapter offersAdapter;
    static Custome_TextView tvtax, tvAmountTax;
    public static String STATE = "";
    public static Custome_TextView total_discount_head;
    private ProgressDialog mProgressDialog;
    ImageView ivbusiness;
    LinearLayout llupdate_business;
    Custome_TextView tvDealavailable;
    ImageView ivPlusDeal, ivMinusDeal;
    boolean isdeal = false;
    List<AllDeal> alldeals = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        ivbusiness = (ImageView) view.findViewById(R.id.ivbusiness);
        llupdate_business = view.findViewById(R.id.llupdate_business);
        business_type_tv = view.findViewById(R.id.business_type_tv);
        // getActivity().setTitle("Cart");
        GoalsActivities.actionBarTitle.setText("CART");
        GoalsActivities.addProduct.setVisibility(View.VISIBLE);
        GoalsActivities.total_items_bracket.setVisibility(View.VISIBLE);
        GoalsActivities.totalAmt.setVisibility(View.GONE);
        GoalsActivities.cartImg.setVisibility(View.GONE);
        //  GoalsActivities.homeIconImg.setBackground(getResources().getDrawable(R.drawable.home_icon));
        rowTotalAmount = (Custome_TextView) view.findViewById(R.id.raw_amount_tv);
        totalDiscountTv = (Custome_TextView) view.findViewById(R.id.total_discount_txt);
        tvtax = (Custome_TextView) view.findViewById(R.id.tvtax);
        tvAmountTax = (Custome_TextView) view.findViewById(R.id.tvAmountTax);
        totalAmountTv = (Custome_BoldTextView) view.findViewById(R.id.total_amount_tv);
        nested_scroll_view = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
        total_discount_head = view.findViewById(R.id.total_discount_head);
        tvDealavailable = view.findViewById(R.id.tvDealavailable);
        ivPlusDeal = view.findViewById(R.id.ivPlusDeal);
        ivMinusDeal = view.findViewById(R.id.ivMinusDeal);

        STATE = sharedPreference.getStringValue("STATE_PRODUCT");
        Log.e("STATE_cart", ":::" + STATE);


        mProgressDialog = new ProgressDialog(baseActivity);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.show();
        Log.e("ProgressDialog", ":::True");

        if (!sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID).equals("business_id")) {
            goalBusiness = Select.from(GoalBusiness.class).where(Condition.prop("business_id").eq(sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID))).first();
        }
        if (goalBusiness == null) {
            AllBusiness allBusiness = Select.from(AllBusiness.class).where(Condition.prop("business_id").eq(sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID))).first();

            if (allBusiness != null) {
                goalBusiness = new GoalBusiness();
                goalBusiness.setBusinessID(allBusiness.getBusinessID());
                goalBusiness.setBusnessName(allBusiness.getBusnessName());
                goalBusiness.setCheckedIN(true);
                goalBusiness.setAddress1(allBusiness.getAddress1());
                goalBusiness.setAddress2(allBusiness.getAddress2());
                goalBusiness.setCity(allBusiness.getCity());
                goalBusiness.setContactPersonEmail(allBusiness.getContactPersonEmail());
                goalBusiness.setContactPersonPhone(allBusiness.getContactPersonPhone());
                goalBusiness.setContactPersonName(allBusiness.getContactPersonName());
                goalBusiness.setCountry(allBusiness.getCountry());
                goalBusiness.setImageName(allBusiness.getImageName());
                goalBusiness.setState(allBusiness.getState());
                goalBusiness.setWebsiteName(allBusiness.getWebsiteName());
                goalBusiness.setZipCode(allBusiness.getZipCode());
                goalBusiness.setId(allBusiness.getId());
                goalBusiness.setBusinesstype(allBusiness.getBusinesstype());
            }
        }


        if (goalBusiness != null) {
            if (goalBusiness.getImageName() != null && !goalBusiness.getImageName().isEmpty()) {
                Log.e("getImageName", ":::not null");
                if (getContext() != null)
                    Picasso.with(baseActivity).load(goalBusiness.getImageName()).placeholder(getContext().getResources().getDrawable(R.drawable.calendar_icon)).error(R.drawable.calendar_icon).into(ivbusiness);
            } else {
                Log.e("getImageName", ":::null  null");

                ivbusiness.setImageDrawable(null);
            }

            Log.e("BusinessType", "::" + goalBusiness.getBusinesstype());
            businessContactTxt.setTypeface(businessContactTxt.getTypeface(), Typeface.ITALIC);
            business_type_tv.setTypeface(business_type_tv.getTypeface(), Typeface.ITALIC);


            businessNameTxt.setText(goalBusiness.getBusnessName());
            if (goalBusiness.getBusinesstype() != null && goalBusiness.getBusinesstype().length() > 0) {
                business_type_tv.setText("(" + goalBusiness.getBusinesstype() + ")");
            }

            if (goalBusiness.getContactPersonName() != null) {

                businessContactTxt.setText(goalBusiness.getContactPersonName() + " ");


            } else {

                businessContactTxt.setVisibility(View.GONE);

            }

            if (goalBusiness.getContactPersonPhone() != null) {
                businessContactNumberTxt.setText(goalBusiness.getContactPersonEmail());
            } else {
                businessContactNumberTxt.setVisibility(View.GONE);
            }

        }
        String selectedBusinessID = String.valueOf(goalBusiness.getBusinessID());


        offersAdapter = new OffersDealAdapter(baseActivity, totalDiscountTv, totalAmountTv, sharedPreference, offersDeal, selectedBusinessID);
        if (getArguments() != null) {

            if (getArguments().containsKey("product_location")) {
                location = getArguments().getInt("product_location");
                productId = getArguments().getInt("product_id");
                categoryID = getArguments().getInt("category_id");
                brandName = getArguments().getString("brand_name");
            } else if (getArguments().containsKey("business_loc")) {
                businessLoc = getArguments().getString("business_loc");
            }

            if (getArguments().containsKey("moveto")) {
                moveTo = getArguments().getString("moveto");
            }
            //Select.from(AllProduct.class).where(Condition.prop("product_category_id").eq(categoryID), Condition.prop("brand_name").eq(brandName)).list();
        } else {

        }

//
//            Log.e("Cart_click",":::::cart fragment::Select Business ");
//            Toast.makeText(getContext(), "Please select business ", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getContext(), GoalsActivities.class);
//            intent.putExtra("nameActivity", "AllBusiness");
//            intent.putExtra("navigateToProduct", "productDetail");
//            startActivity(intent);
//        }
        mLayoutManager = new LinearLayoutManager(baseActivity);
        itemCartRv.setLayoutManager(mLayoutManager);


        dealLayoutManager = new LinearLayoutManager(baseActivity);

        offersDeal.setLayoutManager(dealLayoutManager);
        offersDeal.setAdapter(offersAdapter);
        offersAdapter.notifyDataSetChanged();
        offersDeal.setVisibility(View.GONE);
        hideDialog();


        cardAdapter = new CartAdapter(baseActivity, this);

        itemCartRv.setAdapter(cardAdapter);
        cardAdapter.notifyDataSetChanged();

        sumValues();
        llupdate_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBusiness();
            }
        });
        Log.e("Goal BusinessID", ":::" + goalBusiness.getBusinessID());

       alldeals = Select.from(AllDeal.class).where(Condition.prop("deal_type").eq("Order")).list();
       /// alldeals = Select.from(AllDeal.class).where(Condition.prop("Product_iD").eq("Order")).list();
        /// List<AllDeal> alldeals=Select.from(AllDeal.class).where(Condition.prop("Product_iD")).eq("Order")).l;
        // List<AllDeal> alldealBusiness=Select.from(AllDeal.class).where(Condition.prop("Product_iD")).equals()
        Log.e("ALDEALS", ":::" + alldeals.size());
        if (alldeals == null || alldeals.size() == 0) {
            offersDeal.setVisibility(View.GONE);
            tvDealavailable.setText("No Deals available.");
            ivPlusDeal.setVisibility(View.GONE);
            ivMinusDeal.setVisibility(View.GONE);
        } else {
            ivPlusDeal.setVisibility(View.VISIBLE);
            ivMinusDeal.setVisibility(View.GONE);
            if (alldeals.size() == 1) {
                tvDealavailable.setText("" + alldeals.size() + " Deal available");
            } else {
                tvDealavailable.setText("" + alldeals.size() + " Deals available");
            }
        }


        ivPlusDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isdeal) {
                    ivMinusDeal.setVisibility(View.VISIBLE);
                    ivPlusDeal.setVisibility(View.GONE);
                    offersDeal.setVisibility(View.VISIBLE);
                    isdeal = true;
                    focusOnView();


//                    alldeals = Select.from(AllDeal.class).where(Condition.prop("deal_type").eq("Order")).list();
//                    if (alldeals == null || alldeals.size() == 0) {
//                        offersDeal.setVisibility(View.GONE);
//                        tvDealavailable.setText("No Deals available.");
//                        ivPlusDeal.setVisibility(View.GONE);
//                        ivMinusDeal.setVisibility(View.GONE);
//                        // Toast.makeText(getActivity(), "No Deals found.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        offersDeal.setVisibility(View.VISIBLE);
//
//                    }
//                    btnApplyDeals.setText("DONE");
//                    isdeal = true;
//                    focusOnView();
                }
            }
        });
        ivMinusDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isdeal) {
                    ivMinusDeal.setVisibility(View.GONE);
                    ivPlusDeal.setVisibility(View.VISIBLE);
                    offersDeal.setVisibility(View.GONE);
                    isdeal = false;
                }
            }
        });


        return view;
    }


    public static void sumValues() {

        sumVat = 0;
        Field f = null;
        rawAmount = 0;
        discount = 0;
        subTotal = 0;
        amount = 0;
        rowAmountTax = 0;
        int sum = 0;
        double cgstRate = 0.0;
        double sgstRate = 0.0;
        try {
            f = SugarContext.getSugarContext().getClass().getDeclaredField("sugarDb");
            f.setAccessible(true);
            SugarDb db = (SugarDb) f.get(SugarContext.getSugarContext());
            int proQty = 0;
            List<ProductInCart> cartItemList = Select.from(ProductInCart.class).list();
            try {
                String disAmount = "0";
                for (ProductInCart productsInCart : cartItemList) {

                    Double baseprice = productsInCart.price;
//                    Log.e("BASE_PRICE", ":::::" + baseprice);
//                    Log.e("CGST%", "::::::::%%%%" + productsInCart.cGSTPercentage);
//                    Log.e("SGST%", "::::::::%%%%" +  productsInCart.sGSTPercentage);
                    if (productsInCart.cGSTPercentage != null) {

                        cgstRate = (baseprice * productsInCart.cGSTPercentage) / 100;
                    }
                    if (productsInCart.sGSTPercentage != null) {
                        sgstRate = (baseprice * productsInCart.sGSTPercentage) / 100;
                    }
//                    Log.e("CGST", "::::::::cgstRatePrice" + cgstRate);
//                    Log.e("SGST", "::::::::sgstRatePrice" + sgstRate);

                    rowAmountTax = rowAmountTax + (cgstRate + sgstRate) * (productsInCart.qty);

//                    Log.e("QUANTITY", "::::;" + productsInCart.qty);
//                    Log.e("rowAmountTax", "::::::::rowAmountTax" + rowAmountTax);
                    rawAmount += baseprice * (productsInCart.qty);


//                    Log.e("ROW_amount", ":::::" + rawAmount);
                    String dealAmount = productsInCart.dealAmount;
//                    Log.e("DEAL_amount", ":::::" + dealAmount);


                    if (productsInCart.dealType.equals("Amount")) {
                        disAmount = productsInCart.dealAmount;
                        discount += getFloatFromString(productsInCart.dealAmount);
                        Log.e("discount", ":::::" + discount);
                        Double priceAfterDiscountAmount = productsInCart.price - getFloatFromString(productsInCart.dealAmount);
                        subTotal += priceAfterDiscountAmount;
                        Log.e("AMOunt", "::::" + amount);

                        amount += (priceAfterDiscountAmount) * (productsInCart.qty);
                        Log.e("AMOUNT_", "::::" + amount);


                    } else {
                        Double discountAmount = productsInCart.price * (getDoubleFromString(dealAmount)) / 100;
                        discount += discountAmount * productsInCart.qty;
                        disAmount = String.valueOf(dealAmount);
                        Double discAmt = productsInCart.price - discountAmount;
                        subTotal += discAmt;
                        amount += (discAmt) * (productsInCart.qty);
//                        Log.e("AMOUNT_2", "::::" + amount);


                    }

                    proQty += productsInCart.CartQty;
                }
                tvtax.setText("₹" + " " + Double.parseDouble(new DecimalFormat("##.##").format(rowAmountTax)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

//30-4-18
            Log.e("ProQTy", ":::" + proQty);


            if (GoalsActivities.total_items_bracket != null) {
                GoalsActivities.total_items_bracket.setText("(" + "" + proQty + ")");
            }


//                    Log.e("proQty","::::::"+proQty);
            //  GoalsActivities.totalAmt.setText(proQty);
//            GoalsActivities.totalAmt.setText(proQty);


            Double DrowTotalAmount = Double.parseDouble(new DecimalFormat("##.##").format(rawAmount));
            Double DTotalAmount = Double.parseDouble(new DecimalFormat("##.##").format((amount + rowAmountTax)));
            rowTotalAmount.setText("₹" + " " + DrowTotalAmount);
            totalAmountTv.setText("₹" + " " + DTotalAmount);


            ProductInCart productInCart = Select.from(ProductInCart.class).where(Condition.prop("deal_category").eq("Order")).first();
            if (productInCart != null) {
                if (productInCart.dealType.equals("Amount")) {

                    if (productInCart.dealAmount != null && productInCart.dealAmount.equals(0.0)) {
                        totalDiscountTv.setVisibility(View.GONE);
                        total_discount_head.setVisibility(View.GONE);
                    } else {
                        totalDiscountTv.setVisibility(View.VISIBLE);
                        total_discount_head.setVisibility(View.VISIBLE);
                    }
                    Log.e("%%%", ":::deal" + productInCart.dealAmount);

                    totalDiscountTv.setText("₹" + " " + productInCart.dealAmount);
                    double discount1 = getDoubleFromString(productInCart.dealAmount);
                    double amount1 = rawAmount - discount1;

                    amount = amount1;
                    discount = discount1;
                    Double dTotalAmount = Double.parseDouble(new DecimalFormat("##.##").format((amount + rowAmountTax)));

                    totalAmountTv.setText("₹" + " " + dTotalAmount);

                } else {

//                    Log.e("DEAL_amnount",":::"+productInCart.dealAmount);
//                    Log.e("RAWAMOUNT",":::"+rawAmount);
                    Double disAmount = (getDoubleFromString(productInCart.dealAmount) / 100) * rawAmount;
                    Double amount1 = rawAmount - disAmount;

                    discount = disAmount;
                    amount = amount1;
                    Double DTotalDiscount = Double.parseDouble(new DecimalFormat("##.##").format((disAmount)));
                    DTotalAmount = Double.parseDouble(new DecimalFormat("##.##").format((amount1 + rowAmountTax)));


                    if (DTotalDiscount.equals(0.0)) {
                        totalDiscountTv.setVisibility(View.GONE);
                        total_discount_head.setVisibility(View.GONE);
                    } else {
                        totalDiscountTv.setVisibility(View.VISIBLE);
                        total_discount_head.setVisibility(View.VISIBLE);
                    }
                    Log.e("%%%", ":::total_disc0" + DTotalDiscount);

                    totalDiscountTv.setText("₹" + " " + DTotalDiscount);
                    totalAmountTv.setText("₹" + " " + DTotalAmount);


                }
            } else {
//                Log.e("%%%",":::discount"+discount);

                Double DTotalDiscount = Double.parseDouble(new DecimalFormat("##.##").format((discount)));
                if (DTotalDiscount.equals(0.0)) {
                    totalDiscountTv.setVisibility(View.GONE);
                    total_discount_head.setVisibility(View.GONE);
                } else {
                    totalDiscountTv.setVisibility(View.VISIBLE);
                    total_discount_head.setVisibility(View.VISIBLE);
                }
                Log.e("%%%", ":::total_discount_txt" + DTotalDiscount);
                totalDiscountTv.setText("₹" + " " + (DTotalDiscount));


            }


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Double getDoubleFromString(String amount) {
        double totalPrice = Double.parseDouble(amount.replaceAll("[^0-9\\.]+", ""));
        return totalPrice;
    }

    public static Float getFloatFromString(String amount) {
        Float totalPrice = Float.parseFloat(amount.replaceAll("[^0-9\\.]+", ""));
        return totalPrice;
    }

    @OnClick(R.id.btnAddProducts)
    public void addMoreProduct() {
        GoalsActivities.addProduct.setVisibility(View.GONE);
        GoalsActivities.actionBarTitle.setText("PRODUCTS");
        Intent intent = new Intent(baseActivity, GoalsActivities.class);
        sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, null);
        intent.putExtra("nameActivity", "AllProduct");
        startActivity(intent);
//        getContext().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    @OnClick(R.id.confirm_order_btn)
    public void placeOrder() {
        SharedPreferences preferences = baseActivity.getSharedPreferences("MyPref", 0);

        if (ProductInCart.listAll(ProductInCart.class).size() != 0) {
            Log.e("***ProductIncart", ":::::::Not empty");
            SharedPreferences.Editor editor = preferences.edit();


            for (ProductInCart products : ProductInCart.listAll(ProductInCart.class)) {
                Log.e("***ProductID", "::::::" + products.productID);
                editor.putBoolean("" + products.productID, false);
                editor.commit();
            }


        }

        getPlaceOrder();
    }


    @OnClick(R.id.btnApplyDeals)
    public void applyDeals() {

        if (!isdeal) {
            alldeals = Select.from(AllDeal.class).where(Condition.prop("deal_type").eq("Order")).list();
            if (alldeals == null || alldeals.size() == 0) {
                offersDeal.setVisibility(View.GONE);
                tvDealavailable.setText("No Deals available.");
                ivPlusDeal.setVisibility(View.GONE);
                ivMinusDeal.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No Deals found.", Toast.LENGTH_SHORT).show();
            } else {
                offersDeal.setVisibility(View.VISIBLE);

            }
            btnApplyDeals.setText("DONE");
            isdeal = true;
            focusOnView();
        } else {
            offersDeal.setVisibility(View.GONE);
            btnApplyDeals.setText("APPLY DEALS");
            isdeal = false;
        }

    }

    private void focusOnView() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                nested_scroll_view.scrollTo(0, offersDeal.getTop());
            }
        });
    }

    public void onBackPressed() {
        hideDialog();
        if (moveTo.equals("business")) {
            sharedPreference.saveStringData(PrefsHelper.NAVI_PRODUCT_BUSINESS, "product");


            Intent intent = new Intent(baseActivity, GoalsActivities.class);
            intent.putExtra("nameActivity", "AllBusiness");
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (moveTo.equals("product")) {
            Intent intent = new Intent(baseActivity, GoalsActivities.class);
            intent.putExtra("nameActivity", "ProductDetail");
            intent.putExtra("product_location", location);
            intent.putExtra("product_id", productId);
            intent.putExtra("brand_name", brandName);
            intent.putExtra("category_id", categoryID);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            GoalsActivities.total_items_bracket.setVisibility(View.GONE);
            Intent intent = new Intent(baseActivity, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

//    @Override
//    public void productClick(View v, int position) {
//    }
//    }

    public void getPlaceOrder() {
        //  CommonUtils.showProgress(getActivity());
        if (Connectivity.isNetworkAvailable(ApplicationClass.getAppContext())) {
            mProgressDialog = new ProgressDialog(baseActivity);
            mProgressDialog.setMessage("Loading data...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
            {
                Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                        .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
                cartList = ProductInCart.listAll(ProductInCart.class);
                if (SaveOrder.listAll(SaveOrder.class).size() != 0) {
                    SaveOrder.listAll(SaveOrder.class).clear();
                }
                SaveOrder.LOrderNumber++;
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONArray array = new JSONArray();
                    orderIdInt++;
                    for (ProductInCart productsInCart : cartList) {
                        JSONObject object = new JSONObject();
                        String disAmount = "0";
                        if (productsInCart.qty != 0) {
                            object.put("OrderItemID", productsInCart.productID);
                            object.put("Quantity", productsInCart.qty);
                            object.put("VariantID", productsInCart.variantID);
                            Double d = new Double(productsInCart.price);
                            int price = d.intValue();

                            if (productsInCart.dealCategory.equals("product")) {


                                if (productsInCart.dealId.equals("0")) {
                                    object.put("deal", productsInCart.dealId);
                                    object.put("Cost", price);
                                    subTotal += 0;
                                    amount += 0;
                                    discount += 0;
                                    Double d2 = new Double((productsInCart.price) * (productsInCart.qty));
                                    int amount = d2.intValue();

                                    object.put("Amount", amount);
                                } else {
                                    String dealType = productsInCart.dealType;
                                    String dealAmount = productsInCart.dealAmount;
                                    if (dealType.equals("Amount")) {
                                        object.put("deal", productsInCart.dealId);
                                        object.put("Cost", price);
                                        disAmount = productsInCart.dealAmount;
                                        object.put("Discount", disAmount);
                                        Double priceAfterDiscountAmount = productsInCart.price - Double.parseDouble(productsInCart.dealAmount);

                                        Double d3 = new Double(priceAfterDiscountAmount * (productsInCart.qty));
                                        int amount2 = d3.intValue();

                                        object.put("Amount", amount2);

                                    } else {
                                        Double discountAmount = productsInCart.price * Double.parseDouble(dealAmount) / 100;
                                        disAmount = String.valueOf(dealAmount);

                                        Double discAmt = productsInCart.price - discountAmount;
                                        object.put("Discount", Math.round(discountAmount));
                                        object.put("deal", productsInCart.dealId);
                                        object.put("Cost", price);
                                        Double d4 = new Double(discAmt * (productsInCart.qty));
                                        int amount3 = d4.intValue();

                                        object.put("Amount", amount3);
                                    }
                                }

                            } else {
                                object.put("deal", productsInCart.dealId);
                                object.put("Cost", price);
                                subTotal += 0;
                                amount += 0;
                                discount += 0;
                                Double d5 = new Double((productsInCart.price) * (productsInCart.qty));
                                int amount4 = d5.intValue();

                                object.put("Amount", amount4);
                            }

                            object.put("UOM", productsInCart.uomST + "");
                            object.put("orderId", 0);

                            Log.e("***Array", "::::_object:::" + object.toString());


                            array.put(object);
                        }
                    }


                    final Calendar c = Calendar.getInstance();
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.DAY_OF_MONTH, day);
                    cal.set(Calendar.MONTH, month);

                    String format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS").format(cal.getTime());
                    Long Totalvalue = (Math.round(subTotal) + Math.round(rowAmountTax)) - (Math.round(discount));
                    Double TotalOrderValue = 0.0;
                    try {
                        TotalOrderValue = Totalvalue.doubleValue();
                    } catch (Exception e) {
                    }

                    jsonObject.put("OrderItems", array);
                    jsonObject.put("OrderID", 0);
                    jsonObject.put("LOrderNumber", SaveOrder.LOrderNumber);
                    jsonObject.put("OrderDate", format + "Z");
                    jsonObject.put("BusinessID", sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID));
                    jsonObject.put("OrderNo", SaveOrder.LOrderNumber);
                    jsonObject.put("SubTotal", Math.round(subTotal));
                    jsonObject.put("TaxPercentage", 0);
                    jsonObject.put("TaxAmount", Math.round(rowAmountTax));
                    jsonObject.put("DeliveryAmount", 0);
                    jsonObject.put("DiscountAmount", Math.round(discount));//DiscountAmount
                    jsonObject.put("TotalOrderValue", TotalOrderValue);
                    jsonObject.put("dealID", 0);
                    // jsonObject.put("VariantID",)
                    Log.e("***Array", "::::jsonObject::::" + jsonObject.toString());
                    Log.d("***Array", jsonObject.toString());

                    saveOrderObject = builder.fromJson(jsonObject.toString(), SaveOrder.class);

                    List<PendingOrderItem> itemList = saveOrderObject.getPendingOrderItems();

                    //   saveOrderObject.setOrderNo(SaveOrder.LOrderNumber + "");
                    for (int i = 0; i < itemList.size(); i++) {
                        PendingOrderItem pendingOrderItem = itemList.get(i);
                        pendingOrderItem.setOrderNumberID(SaveOrder.LOrderNumber + "");


                        //itemList.get(i).save();
                        pendingOrderItem.save();

                        saveOrderObject.setSendToServer("0");
                    }

                    saveOrderObject.save();
                    listSaveorderArray = new ArrayList<SaveOrder>();
                    listSaveorderArray.add(saveOrderObject);


                    if (saveOrderObject.getPendingOrderItems().size() != 0) {
                        if (Select.from(SaveOrder.class).where(Condition.prop("send_to_server").eq("0")).list().size() != 0) {
                            if (Connectivity.isNetworkAvailable(baseActivity)) {

                                requestHttp(listSaveorderArray);
                            } else {
                                Toast.makeText(ApplicationClass.getAppContext(), "you need internet connection for placing orders.", Toast.LENGTH_SHORT).show();

                            }
                        }
              /*  Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
                intent.putExtra("saveObj", saveOrderObject);
                intent.putExtra("saveOrderItem", (Serializable) saveOrderObject.getPendingOrderItems());
                intent.putExtra("saveOrdersArray", (Serializable) listSaveorderArray);


                sharedPreference.saveStringData(PrefsHelper.TOTAL_DISCOUNT, String.valueOf(discount));

                sharedPreference.saveStringData(PrefsHelper.RAW_AMOUNT, String.valueOf(rawAmount));
                sharedPreference.saveStringData(PrefsHelper.TOTAL_AMOUNT, String.valueOf(rawAmount-discount));
                startActivity(intent);*/
                        if (!isOrderPlaced) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                                    receiver = new NetworkChangeReceiver();

                                    LocalBroadcastManager.getInstance(ApplicationClass.getAppContext()).registerReceiver(receiver, filter);
                                }
                            }, 1000);
                        }
                    } else {
                        Toast.makeText(baseActivity, "no value found to order", Toast.LENGTH_SHORT).show();
                    }
                    hideDialog();


                } catch (Exception ex) {
                    hideDialog();
                    ex.printStackTrace();
                    //   Toast.makeText(getActivity(), "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
                }
                //  Toast.makeText(getContext(), "You has confirm you order ", Toast.LENGTH_LONG).show();
                Log.d("orderplace", "successfully");
                /*ProductInCart.deleteAll(ProductInCart.class);*/

      /*  Intent intent = new Intent(getContext(), DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
            }

        } else {

            Toast.makeText(ApplicationClass.getAppContext(), "you must have internet connection for placing orders.", Toast.LENGTH_SHORT).show();
        }
    }

    boolean isOrderPlaced = false;

    private void requestHttp(List<SaveOrder> listSaveorderArray) {
        if (Connectivity.isNetworkAvailable(ApplicationClass.getAppContext())) {
            isOrderPlaced = true;
            showDialog();

            ServiceHandler serviceHandler = NetworkManager.createRetrofitService(baseActivity, ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
            final List<SaveOrder> listSaveOrderListArray = listSaveorderArray;
            Log.e("listSAvrORder","::::"+listSaveOrderListArray.size());
            if (cartList.size() != 0) {
                serviceHandler.placeOrder(listSaveorderArray, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        String serverResponse = CommonUtils.getServerResponse(response);
//                        Log.e("ServerRes", ":::" + serverResponse);
                        try {
                            JSONObject jsonObject1 = new JSONObject(serverResponse);
                            if (jsonObject1.getString("Message").equals("Success")) {
                                for (SaveOrder saveOrder : listSaveOrderListArray) {
                                    saveOrder.setSendToServer("1");
                                    saveOrder.save();
                                }
                            }

                            ProductInCart.deleteAll(ProductInCart.class);
                            //  sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, "");
                            Toast.makeText(baseActivity, "Your order has been successfully placed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(baseActivity, GoalsActivities.class);
                            intent.putExtra("nameActivity", "orderhistory");
                            intent.putExtra("isfrom", "cartfrag");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            baseActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            baseActivity.finish();
                            hideDialog();
                        } catch (Exception ex) {
                            hideDialog();
                            Log.e("EXCEPTION","::::"+ex.toString());

//                            Log.e("Response:::", response.toString());
//                            Log.e("responseExc", "::::" + ex.toString());
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getMessage().equals("timeout")) {
                            hideDialog();
                            requestHttp(listSaveOrderListArray);
                        } else {

                            Toast.makeText(ApplicationClass.getAppContext(), "Error occured,Please try again.", Toast.LENGTH_SHORT).show();

//                            String responseError = CommonUtils.getServerResponse(error.getResponse());
//                            try {
//                                JSONObject jsonErrorObj = new JSONObject(responseError);
//                                error.printStackTrace();
//                                Log.e("ERROR_OBJ",":::"+jsonErrorObj.toString());
//
//                                Toast.makeText(baseActivity, jsonErrorObj.getString("Message") + "", Toast.LENGTH_SHORT).show();
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//
//                            }

                        }


                        //  Toast.makeText(baseActivity, "Please check your internet connectivity", Toast.LENGTH_SHORT).show();
                    }
                });
                // CommonUtils.dismissProgress();
            }
        } else {
            Toast.makeText(ApplicationClass.getAppContext(), "you must have internet connection for placing orders.", Toast.LENGTH_SHORT).show();

        }

    }

    //    @OnClick(R.id.update_business)
    public void updateBusiness() {
        sharedPreference.saveStringData(PrefsHelper.NAVI_PRODUCT_BUSINESS, "cartFragment");
        Intent intent = new Intent(baseActivity, GoalsActivities.class);
        intent.putExtra("nameActivity", "AllBusiness");
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        baseActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    @OnClick(R.id.update_amount)
    public void updateAmount() {
        final Dialog dialog = new Dialog(baseActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custome_edit_dialog);

        final EditText text = (EditText) dialog.findViewById(R.id.txt_dia);
        text.setText(amount + "");

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float updateAmt = Float.parseFloat(text.getText().toString());
               /* if (amount < updateAmt) {
                    Toast.makeText(getActivity(), "update amount ", Toast.LENGTH_SHORT).show();
                }
                {*/

                if (!totalAmountTv.getText().equals(text.getText())) {
                    updated_ampunt_tv.setVisibility(View.VISIBLE);
                    updated_ampunt_tv.setText("" + text.getText());
                    amount = Float.parseFloat(text.getText().toString());
//                    totalAmountTv.setPaintFlags(totalAmountTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                    totalAmountTv.setTextColor(getActivity().getResources().getColor(R.color.dark_dim_gray));
                } else {
                    updated_ampunt_tv.setVisibility(View.GONE);
                }
                /* }*/
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void productClick(View v, int position) {

    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.v("Goals getting", "Receieved notification about network status");
            if (Connectivity.isNetworkAvailable(context)) {
                if (!isConnected) {
                    Log.v("is network Available", "Now you are connected to Internet!");
                    isConnected = true;
                    //  Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();

                    if (Select.from(SaveOrder.class).where(Condition.prop("send_to_server").eq("0")).list().size() != 0) {
                        requestHttp(listSaveorderArray);
                    }

                    sharedPreference.saveBooleanValue("IsCartFragment", true);
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
                                //  Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();

                                if (Select.from(SaveOrder.class).where(Condition.prop("send_to_server").eq("0")).list().size() != 0) {
                                    requestHttp(listSaveorderArray);
                                }

                                sharedPreference.saveBooleanValue("IsCartFragment", true);
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
            }
//            if (!sharedPreference.getBooleanValue("IsCartFragment")){
//                Toast.makeText(getActivity(),"You must visit every screen once after first time login with internet connected.",Toast.LENGTH_SHORT).show();
//
//            }
            Log.v("not connected", "You are not connected to Internet!");
            //   Toast.makeText(context, "not Connected", Toast.LENGTH_SHORT).show();
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
//            Log.e("hideDialog", "::::::true");
            mProgressDialog.dismiss();

        }
    }


}
