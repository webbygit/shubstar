package com.salestrackmobileapp.android.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllDeal;
import com.salestrackmobileapp.android.gson.ProductList;
import com.salestrackmobileapp.android.gson.UOMArray;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DealProductDetailFragment extends BaseFragment {


    Custome_TextView nameProductTv, mrpProductTv, categoryTv, descProductTv;
    ImageView productImg;
    Spinner spinner;
    ProductList productList;

    CheckBox checkToApplyDeal;
    Button addToCart;

    List<String> listUomArray = new ArrayList<String>();
    List<Integer> listUomIdArray = new ArrayList<Integer>();
    private static Dialog mProgressDialog;
    public static String uomSt = "";
    public static Integer uomId = 0;

    ProductInCart productInCart;
    AllDeal allDeals;
    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deal_product_detail, container, false);
        preferences = baseActivity.getSharedPreferences("MyPref", 0);

        if (getArguments() != null) {
           /* location = getArguments().getInt("ProductLocation");
            productId = getArguments().getInt("product_id");
            categoryID = getArguments().getInt("category_id");
            brandName = getArguments().getString("brandname");
            dealId=getArguments().getInt("dealId");
            if (brandName != null || brandName.equals("null")) {
                brandName = "";

            } else {
            }*/
            productImg = (ImageView) view.findViewById(R.id.product_img);
            productImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
            productList = (ProductList) getArguments().getSerializable("productObj");
            nameProductTv = (Custome_TextView) view.findViewById(R.id.name_product_tv);
            mrpProductTv = (Custome_TextView) view.findViewById(R.id.mrp_product_tv);
            categoryTv = (Custome_TextView) view.findViewById(R.id.category_tv);
            descProductTv = (Custome_TextView) view.findViewById(R.id.desc_product_tv);
            spinner = (Spinner) view.findViewById(R.id.measurmentSpn);
            checkToApplyDeal = (CheckBox) view.findViewById(R.id.apply_deal);
            allDeals = Select.from(AllDeal.class).where(Condition.prop("deal_id").eq(productList.getDealID()), Condition.prop("deal_type").eq("Product")).first();
            addToCart = (Button) view.findViewById(R.id.add_cart_btn);

            if (checkToApplyDeal.isChecked()) {
                addToCart.setEnabled(true);
            } else {
                addToCart.setEnabled(false);
            }

            if (productList.getImageName().equals("")) {
                productImg.setImageResource(R.drawable.calendar_icon);
            } else {
                Picasso.with(baseActivity).load(productList.getImageName()).placeholder(getResources().getDrawable(R.drawable.calendar_icon)).error(R.drawable.calendar_icon).into(productImg);
            }

            nameProductTv.setText(productList.getProductName() + "");
            mrpProductTv.setText(productList.getDefaultPrice() + "");

            if (productList.getProductCategoryName() == null) {
                categoryTv.setVisibility(View.GONE);
            } else {
                categoryTv.setText(productList.getProductCategoryName() + "");
            }
            descProductTv.setText(productList.getDescription() + "");


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= 1) {
                        uomSt = listUomArray.get(position);
                        uomId = listUomIdArray.get(position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            getMeasurementValues();

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkToApplyDeal.isChecked()) {

                        productInCart = Select.from(ProductInCart.class).where(Condition.prop("product_id").eq(productList.getProductID())).first();
                        if (productInCart == null) {
                            productInCart = new ProductInCart();
                            productInCart.productID = productList.getProductID();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("" + productList.getProductID(), true);
                            editor.commit();

                            if (productList.getProductCategoryID() == null) {
                                productInCart.productCategoryId = "";
                            } else {
                                productInCart.productCategoryId = String.valueOf(productList.getProductCategoryID());
                            }
                            productInCart.brandId = String.valueOf(productList.getBrandID());
                            productList.getBrandName();

                            productInCart.uomID = uomId;
                            productInCart.uomST = uomSt;
                            productInCart.productName = productList.getProductName();

                            if (!productList.getDefaultPrice().equals(null)) {
                                productInCart.priceOriginal = Double.valueOf(productList.getDefaultPrice());
                            } else {
                                productInCart.priceOriginal = 0.0;
                            }
                            if (!productList.getSalePrice().equals(null)) {
                                productInCart.price = Double.valueOf(productList.getSalePrice());
                            } else {
                                productInCart.price = 0.0;
                            }
                            productInCart.imageUrl = String.valueOf(productList.getImageName());
                            productInCart.qty = 1;
                            productInCart.CartQty = 1;
                            productInCart.dealId = String.valueOf(productList.getDealID());
                            productInCart.dealCategory = "product";
                            productInCart.dealType = String.valueOf(allDeals.getDealApplicableAs() + "");
                            productInCart.dealAmount = String.valueOf(allDeals.getAmount() + "");
                            productInCart.allowPriceEdit = productList.getAllowPriceEdit();
                            productInCart.save();
                            Toast.makeText(baseActivity, "you have successfully apply deal on the product", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(baseActivity, GoalsActivities.class);
                            intent.putExtra("nameActivity", "AllDeals");
                            startActivity(intent);
                            baseActivity.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

                        } else {
                            productInCart.dealId = String.valueOf(productList.getDealID());
                            productInCart.dealCategory = "product";
                            productInCart.dealType = String.valueOf(allDeals.getDealApplicableAs() + "");
                            productInCart.dealAmount = String.valueOf(allDeals.getAmount() + "");
                            productInCart.save();
                            Toast.makeText(baseActivity, "you have successfully apply deal on the product", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(baseActivity, GoalsActivities.class);
                            intent.putExtra("nameActivity", "AllDeals");
                            startActivity(intent);
                            baseActivity.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
                        }
                    } else {
                        Toast.makeText(baseActivity, "Please checked to apply this deal", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        return view;
    }

    @OnClick(R.id.apply_deal)
    public void applyDeal() {
        if (checkToApplyDeal.isChecked()) {
            addToCart.setEnabled(true);
        } else {
            addToCart.setEnabled(false);
        }
    }

    public void getMeasurementValues() {

        if (mProgressDialog == null) {
            mProgressDialog = new Dialog(baseActivity);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setContentView(R.layout.l_progress_view);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }

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
                        String stringJson = jsonArr.get(i).toString();
                        UOMArray uomArray = builder.fromJson(stringJson, UOMArray.class);
                        uomArray.save();
                    }
                    if (listUomArray.size() != 0) {
                        listUomArray.clear();
                    }


                    listUomIdArray.add(0);
                    List<UOMArray> uomArray = Select.from(UOMArray.class).list();
                    for (UOMArray uomArray1 : uomArray) {
                        if (listUomArray.contains(uomArray1.getUOM())) {

                        } else {
                            listUomArray.add(uomArray1.getUOM() + "");
                            listUomIdArray.add(uomArray1.getUOMID());
                        }
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(baseActivity, R.layout.single_text_item, listUomArray);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                    dataAdapter.notifyDataSetChanged();
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


}
