package com.salestrackmobileapp.android.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.query.Condition;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.fragments.AllBusinessFragment;
import com.salestrackmobileapp.android.fragments.BusinessDetailFragment;
import com.salestrackmobileapp.android.fragments.BusinessListDateFragment;
import com.salestrackmobileapp.android.fragments.BusinessOrderDetailFragment;
import com.salestrackmobileapp.android.fragments.CartFragment;
import com.salestrackmobileapp.android.fragments.CheckInPriOrderFragment;
import com.salestrackmobileapp.android.fragments.ConfirmOrderFragment;
import com.salestrackmobileapp.android.fragments.DealsFragment;
import com.salestrackmobileapp.android.fragments.ListOfDealsProductFragment;
import com.salestrackmobileapp.android.fragments.MyGoalFragment;
import com.salestrackmobileapp.android.fragments.NotesFragment;
import com.salestrackmobileapp.android.fragments.OrderHistoryFragment;
import com.salestrackmobileapp.android.fragments.OrderItemDetailFragment;
import com.salestrackmobileapp.android.fragments.PendingOrderDetailFragment;
import com.salestrackmobileapp.android.fragments.ProductDetailFragment;
import com.salestrackmobileapp.android.fragments.ProductSearchFragment;
import com.salestrackmobileapp.android.fragments.VisitedGoalsFragment;
import com.salestrackmobileapp.android.gson.AllBusiness;
import com.salestrackmobileapp.android.gson.AllProduct;
import com.salestrackmobileapp.android.gson.GoalBusiness;
import com.salestrackmobileapp.android.gson.PendingOrderItem;
import com.salestrackmobileapp.android.gson.SaveOrder;
import com.salestrackmobileapp.android.gson.VisitedGoals;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;
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

public class GoalsActivities extends BaseActivity {


    public static TextView actionBarTitle;
    @BindView(R.id.coordinateLayout)
    CoordinatorLayout coordinateLayout;

    public static ImageView homeIconImg;
    @BindView(R.id.setting_dots_img)
    ImageView threeDotsImg;

    public  static  ImageView cartImg;



    public static ImageView addProduct;

    public static Custome_TextView totalAmt;
    public  static Custome_TextView total_items_bracket;

    public static boolean backToCheckIn = false;
    public static boolean businesCheckIn = false;

    List<AllProduct> listProduct = new ArrayList<AllProduct>();
    private static Dialog mProgressDialog;

    static int i = 0;
    static int serachProducthomeBack = 0;
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    boolean isGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals_activities);
        ButterKnife.bind(this);
        actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        homeIconImg = (ImageView) findViewById(R.id.home_icon_img);
        totalAmt = (Custome_TextView) findViewById(R.id.total_items);
        total_items_bracket=(Custome_TextView) findViewById(R.id.total_items_bracket);

        cartImg=(ImageView) findViewById(R.id.cart_img);
        cartImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePageCalling();
            }
        });


        addProduct=(ImageView)findViewById(R.id.add_product);
        total_items_bracket.setVisibility(View.GONE);
        cartImg.setVisibility(View.VISIBLE);
        totalAmt.setVisibility(View.VISIBLE);
//        if (i == 0) {
//            i++;
//            getAllProduct();
//        }
//
//        sessionWorkingWithServer();
//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        receiver = new NetworkChangeReceiver();
//        try {
//            registerReceiver(receiver, filter);
//        }
//        catch (Exception e){}


        if (getIntent() != null) {
            try {
                if (getIntent().getStringExtra("backoncheckin") != null) {
                    if (getIntent().getStringExtra("backoncheckin").equals("true")) {
                        backToCheckIn = true;
                    } else {
                        backToCheckIn = false;
                    }
                } else if (getIntent().getStringExtra("businesCheckIn") != null) {
                    if (getIntent().getStringExtra("businesCheckIn").equals("true")) {
                        businesCheckIn = true;
                    } else {
                        businesCheckIn = false;
                    }
                }
                if (getIntent().getBooleanExtra("Slider_cart", false)) {
                    boolean isFromSliderCart = getIntent().getBooleanExtra("Slider_cart", false);
                    if (isFromSliderCart) {
                        profilePageCalling();
                    }
                }
                if (getIntent().getExtras() != null) {

                    if (getIntent().getExtras().getString("nameActivity").equals("MyGoals")) {
                        cartImg.setVisibility(View.VISIBLE);
                        totalAmt.setVisibility(View.VISIBLE);
                        total_items_bracket.setVisibility(View.GONE);
                        actionBarTitle.setText("MY GOALS");
                        MyGoalFragment fragment = new MyGoalFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    } else if (getIntent().getExtras().getString("nameActivity").equals("AllProduct")) {
                        Log.e("***Replace", "ALlProduct");
                        actionBarTitle.setText("PRODUCTS");
                        cartImg.setVisibility(View.VISIBLE);
                        totalAmt.setVisibility(View.VISIBLE);
                        total_items_bracket.setVisibility(View.GONE);
                        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
                        ProductSearchFragment fragment = new ProductSearchFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();


                    } else if (getIntent().getExtras().getString("nameActivity").equals("AllProductHome")) {
                        Log.e("***Replace", "AllProductHome");
                        actionBarTitle.setText("PRODUCTS");
                        cartImg.setVisibility(View.VISIBLE);
                        totalAmt.setVisibility(View.VISIBLE);
                        total_items_bracket.setVisibility(View.GONE);
                        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.home_icon));

                        ProductSearchFragment fragment = new ProductSearchFragment();

                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();


                    } else if (getIntent().getExtras().getString("nameActivity").equals("businessListFragment")) {

                        BusinessListDateFragment fragment = new BusinessListDateFragment();
                        Bundle args = new Bundle();
                        args.putString("checkOrder", getIntent().getExtras().getString("checkOrder"));
                        fragment.setArguments(args);
                        actionBarTitle.setText("Salestrack");

                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    } else if (getIntent().getExtras().getString("nameActivity").equals("check_privous_order")) {
                        actionBarTitle.setText("BUSINESS");
                        backToCheckIn = false;
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CheckInPriOrderFragment()).commit();
                    } else if (getIntent().getExtras().getString("nameActivity").equals("visitsfragment")) {
                        actionBarTitle.setText("GOALS");
                        backToCheckIn = false;
                        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new VisitedGoalsFragment()).commit();
                    } else if (getIntent().getExtras().getString("nameActivity").equals("cart_fragment")) {
                        actionBarTitle.setText("CART");
                        addProduct.setVisibility(View.GONE);
                        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
                        cartImg.setVisibility(View.GONE);
                        addProduct.setVisibility(View.GONE);
                        total_items_bracket.setVisibility(View.VISIBLE);
                        CartFragment fragment = new CartFragment();

                        if (getIntent().getStringExtra("navigateToProduct") != null) {
                            Bundle args = new Bundle();
                            args.putString("moveto", getIntent().getExtras().getString("moveto"));
                            fragment.setArguments(args);
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();//businessOrderhistroryDetail
                    } else if (getIntent().getExtras().getString("nameActivity").equals("AllBusiness")) {
                        actionBarTitle.setText("BUSINESS");
                        cartImg.setVisibility(View.VISIBLE);
                        totalAmt.setVisibility(View.VISIBLE);
                        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
                        total_items_bracket.setVisibility(View.GONE);
                        AllBusinessFragment fragment = new AllBusinessFragment();
                        if (getIntent().getStringExtra("navigateToProduct") != null) {
                            //.equals("productDetail")
                            Bundle args = new Bundle();
                            args.putString("navigateToProduct", "productDetail");
                            fragment.setArguments(args);
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    } else if (getIntent().getExtras().getString("nameActivity").equals("AllBusinessDashBoard")) {
                        actionBarTitle.setText("BUSINESS");
                        cartImg.setVisibility(View.VISIBLE);
                        totalAmt.setVisibility(View.VISIBLE);
                        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.home_icon));
                        total_items_bracket.setVisibility(View.GONE);
                        AllBusinessFragment fragment = new AllBusinessFragment();
                        if (getIntent().getStringExtra("navigateToProduct") != null) {
                            //.equals("productDetail")
                            Bundle args = new Bundle();
                            args.putString("navigateToProduct", "productDetail");
                            fragment.setArguments(args);
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    } else if (getIntent().getExtras().getString("nameActivity").equals("businessOrderhistroryDetail")) {
                        actionBarTitle.setText("BUSINESS");
                        BusinessOrderDetailFragment fragment = new BusinessOrderDetailFragment();
                        if (getIntent().getIntExtra("orderId", 0) != 0) {
                            //.equals("productDetail")
                            Bundle args = new Bundle();
                            args.putInt("orderId", getIntent().getIntExtra("orderId", 0));
                            args.putInt("totalAmount", getIntent().getIntExtra("totalAmount", 0));
                            args.putString("businessName", getIntent().getStringExtra("businessName"));
                            args.putString("orderDate", getIntent().getStringExtra("orderDate"));
                            fragment.setArguments(args);
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    } else if (getIntent().getExtras().getString("nameActivity").equals("AllDeals")) {
                        actionBarTitle.setText("DEALS");
                        totalAmt.setVisibility(View.GONE);
                        cartImg.setVisibility(View.GONE);
                        total_items_bracket.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new DealsFragment()).commit();
                    } else if (getIntent().getExtras().getString("nameActivity").equals("businessDetail")) {
                        actionBarTitle.setText("Salestrack");
                        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
                        /*businesCheckIn = false;*/
                        BusinessDetailFragment pdtFragment = new BusinessDetailFragment();
                        if (getIntent().getSerializableExtra("BusinessObject") != null) {
                            Bundle args = new Bundle();
                            args.putSerializable("BusinessObject", getIntent().getSerializableExtra("BusinessObject"));
                            pdtFragment.setArguments(args);
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, pdtFragment).commit();
                    } else if (getIntent().getExtras().getString("nameActivity").equals("ProductDetail")) {

                        Log.e("+++PRODUCT_frag", "::::::true");
                        actionBarTitle.setText("PRODUCT DETAILS");
                        totalAmt.setVisibility(View.VISIBLE);
                        cartImg.setVisibility(View.VISIBLE);
                        total_items_bracket.setVisibility(View.GONE);
                        ProductDetailFragment pdtFragment = new ProductDetailFragment();
                        Bundle args = new Bundle();
                        args.putInt("ProductLocation", getIntent().getExtras().getInt("product_location"));
                        args.putInt("product_id", getIntent().getExtras().getInt("product_id"));
                        int cat_id = 0;
                        if (getIntent().getExtras().getString("category_id") != null) {
                            cat_id = Integer.parseInt(getIntent().getExtras().getString("category_id"));
                        }
                        args.putInt("category_id", cat_id);
                        args.putString("brandname", getIntent().getExtras().getString("brand_name"));
                        if (getIntent().hasExtra("product_name")) {
                            args.putString("product_name", getIntent().getExtras().getString("product_name"));
                        }
                        if (getIntent().hasExtra("listofdeals")) {
                            args.putString("listofdeals", "dealfragment");
                        }
                        pdtFragment.setArguments(args);
                        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, pdtFragment).commit();

                        }
                        else if (getIntent().getExtras().getString("nameActivity").equals("orderhistory")) {

                        actionBarTitle.setText("ORDERS");
                        if (getIntent().getExtras().getString("isfrom").equals("cartfrag")) {
                            isFromCart = true;
                            homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.home_icon));
                            OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();

                            getSupportFragmentManager().beginTransaction().replace(R.id.container, orderHistoryFragment).commit();
                        } else if (getIntent().getExtras().getString("isfrom").equals("dashboard")) {
                            Log.e("ISOrder", ":::");
                            isFromCart = false;

                            homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.home_icon));
                            OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();

                            getSupportFragmentManager().beginTransaction().replace(R.id.container, orderHistoryFragment).commit();
                        }
                    } else if (getIntent().getExtras().getString("nameActivity").equals("orderDetailFragment")) {
                        actionBarTitle.setText("ORDER DETAILS");
                        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
                        totalAmt.setVisibility(View.GONE);
                        cartImg.setVisibility(View.GONE);
                        total_items_bracket.setVisibility(View.GONE);
                        OrderItemDetailFragment orderItemDetailFragment = new OrderItemDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("orderId", getIntent().getIntExtra("orderId", 0));
                        bundle.putSerializable("orderHistoryObject", getIntent().getSerializableExtra("orderhistoryObject"));
                        bundle.putInt("orderPosition", getIntent().getIntExtra("orderPosition", 0));
                        orderItemDetailFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, orderItemDetailFragment).commit();
                    } else if (getIntent().getExtras().getString("nameActivity").equals("orderPendingDetailFragment")) {
                        actionBarTitle.setText("ORDER DETAILS");
                        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
                        PendingOrderDetailFragment pendingOrderDetailFragment = new PendingOrderDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("orderNumber", getIntent().getIntExtra("lorderNumber", 0));
                        bundle.putInt("orderPosition", getIntent().getIntExtra("orderPosition", 0));
                        bundle.putSerializable("orderObj", getIntent().getSerializableExtra("saveorderObj"));
                        pendingOrderDetailFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, pendingOrderDetailFragment).commit();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (Select.from(ProductInCart.class).first() != null) {
                    if (ProductInCart.listAll(ProductInCart.class).size() != 0) {
                        totalAmt.setVisibility(View.VISIBLE);
                        int sum = 0;
                        for (ProductInCart products : ProductInCart.listAll(ProductInCart.class)) {
                            sum += products.CartQty;
                        }
                        totalAmt.setText(sum + "");
                        total_items_bracket.setText("(" + sum + "" + ")");
                    } else {
                        totalAmt.setVisibility(View.GONE);
                    }

                } else {
                    totalAmt.setVisibility(View.GONE);
                }
            }catch (Exception e){
                System.out.println("error"+e.getMessage());
            }
        }



    }


//    @OnClick(R.id.cart_img)
    public void profilePageCalling() {


        if (ProductInCart.listAll(ProductInCart.class).size() != 0) {


            if (!sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID).equals("business_id")) {
                GoalBusiness goalBusiness = Select.from(GoalBusiness.class).where(Condition.prop("business_id").eq(sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID))).first();

                if (goalBusiness == null) {
                    AllBusiness allBusiness = Select.from(AllBusiness.class).where(Condition.prop("business_id").
                            eq(sharedPreference.getStringValue(PrefsHelper.BUSINESS_ID))).first();
                    if (allBusiness != null) {


                        actionBarTitle.setText("CART ");
                        backToCheckIn = false;
                        cartImg.setVisibility(View.GONE);
                        totalAmt.setVisibility(View.GONE);
                        total_items_bracket.setVisibility(View.VISIBLE);
                        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));

                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CartFragment()).commit();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please checkin to business to place order", Toast.LENGTH_SHORT).show();


                        actionBarTitle.setText("BUSINESS");
                        cartImg.setVisibility(View.VISIBLE);
                        totalAmt.setVisibility(View.VISIBLE);
                        total_items_bracket.setVisibility(View.GONE);
                        homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));

                        AllBusinessFragment fragment = new AllBusinessFragment();
                        if (getIntent().getStringExtra("navigateToProduct") != null) {
                            //.equals("productDetail")
                            Bundle args = new Bundle();
                            args.putString("navigateToProduct", "productDetail");
                            fragment.setArguments(args);
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    }
                }
                else{
                    actionBarTitle.setText("CART ");
                    backToCheckIn = false;
                    cartImg.setVisibility(View.GONE);
                    homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
                    totalAmt.setVisibility(View.GONE);
                    total_items_bracket.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new CartFragment()).commit();
                }


            } else {
                Toast.makeText(getApplicationContext(), "Please checkin to business to place order", Toast.LENGTH_SHORT).show();
                actionBarTitle.setText("BUSINESS");
                cartImg.setVisibility(View.VISIBLE);
                totalAmt.setVisibility(View.VISIBLE);
                total_items_bracket.setVisibility(View.GONE);
                homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));

                AllBusinessFragment fragment = new AllBusinessFragment();
                if (getIntent().getStringExtra("navigateToProduct") != null) {
                    //.equals("productDetail")
                    Bundle args = new Bundle();
                    args.putString("navigateToProduct", "productDetail");
                    fragment.setArguments(args);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }

            /*Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);*/
        }
        else{
            Log.e("In_else",":::");
        }
    }
boolean isFromCart=false;
    @OnClick(R.id.add_product)
    public void addMoreProduct() {
        GoalsActivities.addProduct.setVisibility(View.GONE);
        actionBarTitle.setText("PRODUCTS");
        cartImg.setVisibility(View.VISIBLE);
        totalAmt.setVisibility(View.VISIBLE);
        total_items_bracket.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProductSearchFragment()).commit();
    }

    @OnClick(R.id.setting_dots_img)
    public void showMenu() {
        final PopupMenu menu = new PopupMenu(getApplicationContext(), threeDotsImg);
        menu.inflate(R.menu.profile_menu);

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.logout_menu) {
                    Toast.makeText(getApplicationContext(), "logout is clicked", Toast.LENGTH_SHORT).show();

                    SharedPreferences preferences = getSharedPreferences("SalesTrack", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                   /* SugarContext.terminate();
                    SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
                    schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
                    SugarContext.init(getApplicationContext());
                    schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());
                    finish();*/

                    if (Select.from(SaveOrder.class).first() != null) {
                        SaveOrder.deleteAll(SaveOrder.class);
                        if (Select.from(PendingOrderItem.class).first() != null) {
                            PendingOrderItem.deleteAll(PendingOrderItem.class);
                        }
                    }

                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.edit_profile) {
                  //  Toast.makeText(getApplicationContext(), "Edit Profile is clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
                return false;
            }
        });

        menu.show();
    }

    @OnClick(R.id.home_icon_img)
    public void backHomeButtonClick() {
        int count = getFragmentManager().getBackStackEntryCount();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
//onBackPressed
        if (f instanceof ProductDetailFragment) {
            Log.e("PRODUCT_HOME","::::::productHomeclick)");
            ((ProductDetailFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof OrderItemDetailFragment) {
            ((OrderItemDetailFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof BusinessDetailFragment) {
            ((BusinessDetailFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof VisitedGoalsFragment) {
            ((VisitedGoalsFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof CheckInPriOrderFragment) {
            ((CheckInPriOrderFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof OrderHistoryFragment) {
//            if (isFromCart){
//                actionBarTitle.setText("CART");
//                addProduct.setVisibility(View.VISIBLE);
//                homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.home_icon));
//                cartImg.setVisibility(View.GONE);
//                addProduct.setVisibility(View.GONE);
//                total_items_bracket.setVisibility(View.VISIBLE);
//                CartFragment fragment = new CartFragment();
//                if (getIntent().getStringExtra("navigateToProduct") != null) {
//                    Bundle args = new Bundle();
//                    args.putString("moveto", getIntent().getExtras().getString("moveto"));
//                    fragment.setArguments(args);
//                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
//
//            }
//            else {

            ((OrderHistoryFragment) f).onBackPressed();
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
//            }
        } else if (f instanceof PendingOrderDetailFragment) {
            ((PendingOrderDetailFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof ProductSearchFragment) {

//            if (serachProducthomeBack == 0) {
//                serachProducthomeBack++;
//                ((ProductSearchFragment) f).onBackPressed();
//                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
//            } else {
//                serachProducthomeBack = 0;
//                Intent inten = new Intent(getApplicationContext(), DashboardActivity.class);
//                inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(inten);
//            }
//            Intent inten = new Intent(getApplicationContext(), DashboardActivity.class);
//                inten.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(inten);
            ((ProductSearchFragment) f).onBackPressed();
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

        } else if (f instanceof CheckInPriOrderFragment) {
            ((CheckInPriOrderFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof ConfirmOrderFragment) {
            ((ConfirmOrderFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof NotesFragment) {
            ((NotesFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof CartFragment) {
//            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
            ((CartFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof BusinessOrderDetailFragment) {
            ((BusinessOrderDetailFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof BusinessListDateFragment) {
            ((BusinessListDateFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
        else  if (f instanceof AllBusinessFragment){
            ((AllBusinessFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
        else if (f instanceof ProductSearchFragment){
            ((ProductSearchFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent objEvent) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, objEvent);
    }

    @Override
    public void onBackPressed() {

        Log.e("onback","::::true");

        int count = getFragmentManager().getBackStackEntryCount();
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);

        if (f instanceof ProductDetailFragment) {
            ((ProductDetailFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof VisitedGoalsFragment) {
            ((VisitedGoalsFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof OrderHistoryFragment) {
            ((OrderHistoryFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof OrderItemDetailFragment) {
            ((OrderItemDetailFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof CheckInPriOrderFragment) {
            ((CheckInPriOrderFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof ListOfDealsProductFragment) {
            ((ListOfDealsProductFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof BusinessDetailFragment) {
            ((BusinessDetailFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof BusinessListDateFragment) {
            ((BusinessListDateFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof PendingOrderDetailFragment) {
            ((PendingOrderDetailFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof ProductSearchFragment) {
            Log.e("***GOAL_BACK",":::::ProductSearchFragment");

//            if (sharedPreference.getStringValue(PrefsHelper.NAVI_PRODUCT_BUSINESS).equals("business")) {
//                Log.e("***GOAL_BACK",":::::businessDetail");
//                Intent intent = new Intent(this, GoalsActivities.class);
//                intent.putExtra("nameActivity", "businessDetail");
//                startActivity(intent);
//                //  overridePendingTransition(R.anim.slide_enter, R.anim.slide_enter);
//            } else if (sharedPreference.getStringValue(PrefsHelper.NAVI_PRODUCT_BUSINESS).equals("CheckinBusiness")) {
//                Log.e("***GOAL_BACK",":::::CheckInPriOrderFragment");
//                actionBarTitle.setText("BUSINESS");
//
//                CheckInPriOrderFragment fragment = new CheckInPriOrderFragment();
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.container, fragment);
//                ft.commit();
//            } else {
//                Log.e("***GOAL_BACK",":::::ELSE");
//
//                Intent intent = new Intent(this, DashboardActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
//            }
            /*}*/
            ((ProductSearchFragment) f).onBackPressed();

            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof CheckInPriOrderFragment) {
            ((CheckInPriOrderFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof ConfirmOrderFragment) {
            ((ConfirmOrderFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof NotesFragment) {
            ((NotesFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof AllBusinessFragment) {
            ((AllBusinessFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof CartFragment) {
            ((CartFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else if (f instanceof BusinessOrderDetailFragment) {
            ((BusinessOrderDetailFragment) f).onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else {
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            System.out.println("Dashboard");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }


    public void getAllProduct() {

        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(this);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setContentView(R.layout.l_progress_view);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

        try {
            AllProduct.deleteAll(AllProduct.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.getAllProduct(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String arr = CommonUtils.getServerResponse(response);
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
                        alproduct.save();
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
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
//
//        CommonUtils.dismissProgress();
    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            Log.v("Goals getting", "Receieved notification about network status");
            if (Connectivity.isNetworkAvailable(context)){
                if (!isConnected) {
                    Log.v("is network Available", "Now you are connected to Internet!");
                    isConnected = true;
                    sessionWorkingWithServer();
                    sharedPreference.saveBooleanValue("IsGoals",true);
                    //   Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();

                    unregisterReceiver(receiver);
                    //do your processing here ---
                    //if you need to post any data to the server or get status
                    //update from the server
                }
            }
            else {
                unregisterReceiver(receiver);

                Log.v("not connected", "You are not connected to Internet!");
                //  Toast.makeText(context, "Please Connected you internet connectivity", Toast.LENGTH_SHORT).show();
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
                                sessionWorkingWithServer();
                                sharedPreference.saveBooleanValue("IsGoals",true);
                             //   Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();

                                unregisterReceiver(receiver);
                                //do your processing here ---
                                //if you need to post any data to the server or get status
                                //update from the server
                            }
                            return true;
                        }
                    }
                }
            }
            unregisterReceiver(receiver);

            Log.v("not connected", "You are not connected to Internet!");
          //  Toast.makeText(context, "Please Connected you internet connectivity", Toast.LENGTH_SHORT).show();
            isConnected = false;
            return false;
        }
    }

    public void sessionWorkingWithServer() {
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(getApplicationContext(), ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
        serviceHandler.checkSession(CommonUtils.getDeviceID(getApplicationContext()), sharedPreference.getStringValue(PrefsHelper.REGID), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String arr = CommonUtils.getServerResponse(response);
                try {
                    JSONObject jsonObject = new JSONObject(arr);
                    if (jsonObject.getBoolean("Status")) {
                        Log.d("continue ", "session connected");
                    } else {

                        SharedPreferences preferences = getSharedPreferences("SalesTrack", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                   /* SugarContext.terminate();
                    SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
                    schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
                    SugarContext.init(getApplicationContext());
                    schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());
                    finish();*/

                        if (Select.from(SaveOrder.class).first() != null) {
                            SaveOrder.deleteAll(SaveOrder.class);
                            if (Select.from(PendingOrderItem.class).first() != null) {
                                PendingOrderItem.deleteAll(PendingOrderItem.class);
                            }

                        }

                        if (Select.from(VisitedGoals.class).first() != null) {
                            VisitedGoals.deleteAll(VisitedGoals.class);
                        }
                        Toast.makeText(getApplicationContext(), "Your session has been expired.please login again...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        finish();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
