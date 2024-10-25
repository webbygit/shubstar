package com.salestrackmobileapp.android.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.constraint.solver.Goal;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.fragments.ProductDetailFragment;
import com.salestrackmobileapp.android.gson.AllDeal;
import com.salestrackmobileapp.android.gson.AllProduct;
import com.salestrackmobileapp.android.gson.AllVariants;
import com.salestrackmobileapp.android.gson.Product;
import com.salestrackmobileapp.android.gson.ProductList;
import com.salestrackmobileapp.android.gson.UOMArray;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.salestrackmobileapp.android.fragments.CartFragment.sumValues;

/**
 * Created by kanchan on 3/16/2017.
 */

public class ProductPagerAdapter extends PagerAdapter {

    Context context;
    List<AllProduct> allProductList;
    ProductInCart productInCart;
    String businessId;
    DealApplyAdapter mAdapter;
    public static String uomSt = "";
    public static Integer uomId = 0;
    List<AllDeal> listOfDeals = new ArrayList<AllDeal>();
    List<String> listUomArray = new ArrayList<String>();
    List<Integer> listUomIdArray = new ArrayList<Integer>();
    RecyclerView listDealOnProductRv;
    private static Dialog mProgressDialog;
    Spinner spinner;
    static int locationUMO = 0;
    int location = 0;

    ImageView addToCartImg;
    int i = 0;
    Double sum = 1.0;
    Button btn_no;
    SwitchCompat switch_instock;
    RecyclerView rv_variant;
    ProductVariantAdapter productVariantAdapter;
    List<AllVariants> allVariants = new ArrayList<>();
//    private Custome_TextView categoryTv,descProductTv,mrpAfterDiscount;
//    private Custome_BoldTextView nameProductTv;

    TextView alreadyInCart;
    ImageView productImg;
    AllProduct product;

    LinearLayout linearLayout;


    public ProductPagerAdapter(Context context, String businessId) {
        this.context = context;
        this.businessId = businessId;
    }

    public void setListOfProduct(List<AllProduct> allProductList, List<String> listUomArray, List<Integer> listUomIdArray, int location) {
        i = 0;
        this.allProductList = allProductList;
        this.listUomArray = listUomArray;
        this.listUomIdArray = listUomIdArray;
        this.location = location;
    }


    @Override
    public Object instantiateItem(final ViewGroup container,
                                  int position) {
        Log.e("###", "::::position" + position);

//        position=location;
//        if (i == 0) {
//            i++;
//            position = location;
//        }
        Log.e("###", "::::1 position" + position);

        if (allProductList.size() > position) {
            product = allProductList.get(position);
        } else {
            product = allProductList.get(0);

        }
        LayoutInflater inflater = LayoutInflater.from(context);
        sum = 1.0;
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.product_detail_item, container, false);

        listDealOnProductRv = (RecyclerView) layout.findViewById(R.id.list_deal_rv);
        listDealOnProductRv.setNestedScrollingEnabled(true);
        productInCart = Select.from(ProductInCart.class).where(Condition.prop("product_id").eq(product.getProductID())).first();


        productImg = (ImageView) layout.findViewById(R.id.product_img);
        productImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Custome_BoldTextView nameProductTv = (Custome_BoldTextView) layout.findViewById(R.id.name_product_tv);
        final Custome_TextView mrpProductTv = (Custome_TextView) layout.findViewById(R.id.mrp_product_tv);
        Custome_TextView categoryTv = (Custome_TextView) layout.findViewById(R.id.category_tv);
        mrpProductTv.setTag(product);
        Custome_TextView descProductTv = (Custome_TextView) layout.findViewById(R.id.desc_product_tv);
        Custome_TextView mrpAfterDiscount = (Custome_TextView) layout.findViewById(R.id.mrp_after_dicount);
        Custome_TextView mrp_product_tv_original = (Custome_TextView) layout.findViewById(R.id.mrp_product_tv_original);
        alreadyInCart = (TextView) layout.findViewById(R.id.already_in_cart);

        rv_variant = layout.findViewById(R.id.rv_variant);
        rv_variant.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        Long longId = 0L;
        int seletedVariantID = 0;
        if (product.getProductCategoryID() != null) {
            try {
                longId = new Long("" + product.getProductID());
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        if (product.getVariantID() != null)
            try {
                seletedVariantID = new Integer("" + product.getVariantID());

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


        allVariants = new ArrayList<>();
        allVariants = Select.from(AllVariants.class).where(Condition.prop("product_id").eq(longId)).list();

        if (allVariants.size() > 0 && allVariants != null) {

            productVariantAdapter = new ProductVariantAdapter(context, rv_variant, ProductPagerAdapter.this, allVariants, position, allProductList, listUomArray, listUomIdArray, location, product.getVariantID());
            rv_variant.setAdapter(productVariantAdapter);
            for (int i = 0; i < allVariants.size(); i++) {
                if (allVariants.get(i).getVariantID() == CommonUtils.variantID) {
                    if (i > 2)
                        rv_variant.scrollToPosition(i);
                }
            }
            productVariantAdapter.notifyDataSetChanged();
        }


        final ImageView plusImg = (ImageView) layout.findViewById(R.id.plus_img);
        final ImageView minusImg = (ImageView) layout.findViewById(R.id.minus_img);
        addToCartImg = (ImageView) layout.findViewById(R.id.addtocart_img);
        switch_instock = layout.findViewById(R.id.switch_instock);


        Custome_BoldTextView isInCart = (Custome_BoldTextView) layout.findViewById(R.id.is_add_tv);
        final EditText qtyTv = (EditText) layout.findViewById(R.id.qty_tv);
        qtyTv.setTag(product);
        //  qtyTv.setText("1");

        uomId = product.getDefaultUOMID();


        //   mrpProductTv.setText(product.getDefaultPrice() + "");
        mrpProductTv.setText(((AllProduct) mrpProductTv.getTag()).getSalePrice() + "");
        mrp_product_tv_original.setText("₹" + product.getDefaultPrice() + "");
        mrp_product_tv_original.setPaintFlags(mrp_product_tv_original.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        Custome_BoldTextView titleExcitingOffer = (Custome_BoldTextView) layout.findViewById(R.id.title_exciting_offer);
        linearLayout = (LinearLayout) layout.findViewById(R.id.plus_minus_layout);
        final Button addCartBtn = (Button) layout.findViewById(R.id.add_cart_btn);
        addCartBtn.setTag(product);
        if (productInCart != null) {
            if ((productInCart.variantID).equals(product.getVariantID())) {
                //   alreadyInCart.setVisibility(View.VISIBLE);
                addCartBtn.setText("Update Order.");
            } else {
                addCartBtn.setText("Add to Cart .");

            }
        } else {
            addCartBtn.setText("Add to Cart");
            alreadyInCart.setVisibility(View.GONE);
        }
        addCartBtn.setVisibility(View.VISIBLE);
        final List<UOMArray> uomArrayList = Select.from(UOMArray.class).list();

        if (productInCart == null) {
            linearLayout.setVisibility(View.VISIBLE);


        } else {
            linearLayout.setVisibility(View.VISIBLE);
            // addCartBtn.setVisibility(View.GONE);
        }
        Spinner spinner = (Spinner) layout.findViewById(R.id.measurmentSpn);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.single_text_item, listUomArray);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        dataAdapter.notifyDataSetChanged();

        if (productInCart != null) {
            linearLayout.setVisibility(View.VISIBLE);
            isInCart.setVisibility(View.VISIBLE);

            linearLayout.setEnabled(true);
            qtyTv.setText(productInCart.qty + "");
            uomId = productInCart.uomID;
            uomSt = productInCart.uomST;


            if (!uomSt.equals(null)) {
                locationUMO = dataAdapter.getPosition(uomSt);
            }

           /* for (int i = 0; i <= listUomIdArray.size(); i++) {
                if (listUomIdArray.equals(productInCart.uomID)) {
                    locationUMO = i;
                }
            }*/
            //   spinner.setSelection(locationUMO);
        }

        if (uomId == 364) {
            spinner.setSelection(1);
        } else if (uomId == 384) {
            spinner.setSelection(2);

        } else if (uomId == 614) {
            spinner.setSelection(3);

        } else {
            spinner.setSelection(0);

        }

        if (product.getProductID() != 0) {
            if (listOfDeals.size() != 0) {
                listOfDeals.clear();
            }
            List<ProductList> dealProduct = Select.from(ProductList.class).where(Condition.prop("product_id").eq(product.getProductID()), Condition.prop("deal_type").eq("Product")).list();
            for (ProductList productObj : dealProduct) {
                listOfDeals.add(Select.from(AllDeal.class).where(Condition.prop("deal_id").eq(productObj.getDealID()), Condition.prop("deal_type").eq("Product")).first());
            }
        }
        if (listOfDeals.size() != 0) {
            titleExcitingOffer.setVisibility(View.GONE);
        }
        mAdapter = new DealApplyAdapter(context, product.getProductID(), mrpProductTv, mrpAfterDiscount);
        listDealOnProductRv.setAdapter(mAdapter);
        listDealOnProductRv.setLayoutManager(new LinearLayoutManager(context));
        mAdapter.notifyDataSetChanged();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 1) {
                    try {
                        if (position < listUomArray.size() && position < listUomIdArray.get(position)) {

                            uomSt = listUomArray.get(position);
                            uomId = listUomIdArray.get(position);


                            UOMArray uomArray = Select.from(UOMArray.class).where(Condition.prop("u_omid").eq(uomId)).first();
                            if (uomArray != null) {
                                if (uomArray.getBaseQty() == 1) {
                                    //   mrpProductTv.setText(product.getDefaultPrice() + "");
                                    mrpProductTv.setText(((AllProduct) mrpProductTv.getTag()).getSalePrice() + "");
                                    Log.e("$$$", ":::1" + product.getSalePrice() + "");
                                } else {
                                    sum = uomArray.getBaseQty();
                                    Double baseqyt = 0.0;
                                    String uomBaseName = uomArray.getBaseUOMName();
                                    int i = 1;
                                    while (baseqyt == 0.0) {//baseUOMID
                                        UOMArray uomArray1 = Select.from(UOMArray.class).where(Condition.prop("base_uom_name").eq(uomBaseName)).first();
                                        baseqyt = uomArray1.getBaseQty();
                                        uomBaseName = uomArray1.getBaseUOMName();
                                        if (i == 1) {

                                        } else {
                                            if (baseqyt != 0.0) {
                                                sum *= baseqyt;

                                            }
                                            i = 0;
                                        }
                                        if (sum > 1.0) {
                                            Log.e("$$$", ":::2:::" + product.getDefaultPrice() * sum + "");
                                            mrpProductTv.setText(((AllProduct) mrpProductTv.getTag()).getSalePrice() * sum + "");

                                            //   mrpProductTv.setText(product.getDefaultPrice() * sum + "");
                                        } else {
                                            Log.e("$$$", ":::3:::" + product.getDefaultPrice() + "");
                                            mrpProductTv.setText(((AllProduct) mrpProductTv.getTag()).getSalePrice() + "");


                                            //  mrpProductTv.setText(product.getDefaultPrice() + "");
                                        }
                                    }
                                }
                            }
                        }

                        //////
                    } catch (Exception e) {
                        Log.e("EXCEPTION", ":::::" + e.toString());
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uomSt.equals("") || uomSt.isEmpty()) {
                    Toast.makeText(context, "Please select quantity type", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("###", ":::productID::" + (addCartBtn.getTag()));
                    product = (AllProduct) addCartBtn.getTag();
                    Log.e("###", ":::product variantID::" + (product.getVariantID()));

                    // product_id
                    linearLayout.setVisibility(View.VISIBLE);
                    productInCart = Select.from(ProductInCart.class).where(Condition.prop("variant_id").eq(((AllProduct) addCartBtn.getTag()).getVariantID())).first();
                    if (productInCart == null) {
                        product = (AllProduct) addCartBtn.getTag();
                        Log.e("$$$", "::::" + product.getProductID());


                        UOMArray uomArray = Select.from(UOMArray.class).where(Condition.prop("u_omid").eq(uomId)).first();


                        if (uomArray.getBaseQty() != null && uomArray.getBaseQty() == 1) {

                        } else {
                            sum = uomArray.getBaseQty();
                            Double baseqyt = 0.0;
                            String uomBaseName = uomArray.getBaseUOMName();
                            int i = 1;
                            while (baseqyt == 0.0) {//baseUOMID
                                UOMArray uomArray1 = Select.from(UOMArray.class).where(Condition.prop("base_uom_name").eq(uomBaseName)).first();
                                baseqyt = uomArray1.getBaseQty();
                                uomBaseName = uomArray1.getBaseUOMName();
                                if (i == 1) {

                                } else {
                                    if (baseqyt != 0.0) {
                                        sum *= baseqyt;

                                    }
                                    i = 0;
                                }
                            }

                            //qtyTv.setText("1");
                        }
                        int qty = 1;
                        if (qtyTv.getText() != null && qtyTv.getText().length() > 0 && !qtyTv.getText().equals("")) {
                            try {
                                qty = Integer.parseInt(qtyTv.getText().toString());
                            } catch (Exception e) {
                            }
                        }


                        addToCartImg.setImageResource(R.drawable.bluecart);
                        productInCart = new ProductInCart();
                        productInCart.sGSTPercentage = product.getSGSTPercentage();
                        productInCart.cGSTPercentage = product.getCGSTPercentage();
                        productInCart.productID = product.getProductID();
                        productInCart.productCategoryId = String.valueOf(product.getProductCategoryID());
                        productInCart.brandId = String.valueOf(product.getBrandID());
                        product.getBrandName();

                        productInCart.uomID = uomId;
                        productInCart.uomST = uomSt;
                        productInCart.productName = product.getProductName();
                        if (!product.getDefaultPrice().equals(null)) {
                            productInCart.priceOriginal = product.getDefaultPrice() * sum;
                            Log.e("$$$", "::getDefaultPrice::" + product.getDefaultPrice());
                            Log.e("$$$", "::sum::" + sum);

                        } else {
                            productInCart.priceOriginal = 0.0;
                        }

                        if (!product.getSalePrice().equals(null)) {
                            productInCart.price = product.getSalePrice() * sum;
                            Log.e("$$$", "::getDefaultPrice::" + product.getSalePrice());
                            Log.e("$$$", "::sum::" + sum);

                            Log.e("$$$", "::productInCart.price::" + productInCart.price);

                            mrpProductTv.setText(productInCart.price + "");
                        } else {
                            productInCart.price = 0.0;
                        }
                        productInCart.imageUrl = String.valueOf(product.getImageName());
                        productInCart.qty = qty;
                        productInCart.CartQty = 1;
                        productInCart.dealCategory = "";
                        productInCart.dealId = "0";
                        productInCart.dealAmount = "0";
                        productInCart.dealType = "";
                        productInCart.variantID = product.getVariantID();
                        productInCart.allowPriceEdit = product.getAllowPriceEdit();

                        productInCart.save();
                        addCartBtn.setText("Update Order");

                        // ProductDetailFragment.continueTxt.setVisibility(View.VISIBLE);

                    } else if ((productInCart != null) && ((productInCart.variantID).equals(product.getVariantID()))) {
                        product = (AllProduct) addCartBtn.getTag();
                        Log.e("###", ":::product cart variantID1::" + productInCart.variantID);


                        productInCart.uomST = uomSt;
                        productInCart.uomID = uomId;
                        productInCart.variantID = product.getVariantID();
                        productInCart.productName = product.getProductName();
                        productInCart.imageUrl = product.getImageName();
                        if (qtyTv.getText().length() > 0 && qtyTv.getText() != null && !qtyTv.getText().equals("")) {
                            try {
                                productInCart.qty = Integer.parseInt(qtyTv.getText().toString());
                            } catch (Exception e) {
                            }
                        }
                        productInCart.save();
                        Toast.makeText(context, "Order is updated succesfully", Toast.LENGTH_SHORT).show();
                        addCartBtn.setText("Update Order");

                    } else if ((productInCart != null) && (!productInCart.variantID.equals(product.getVariantID()))) {
                        product = (AllProduct) addCartBtn.getTag();
                        Log.e("$$$", "::::" + product.getProductID());
                        Log.e("###", ":::product cart variantID2::" + productInCart.variantID);


                        UOMArray uomArray = Select.from(UOMArray.class).where(Condition.prop("u_omid").eq(uomId)).first();


                        if (uomArray.getBaseQty() != null && uomArray.getBaseQty() == 1) {

                        } else {
                            sum = uomArray.getBaseQty();
                            Double baseqyt = 0.0;
                            String uomBaseName = uomArray.getBaseUOMName();
                            int i = 1;
                            while (baseqyt == 0.0) {//baseUOMID
                                UOMArray uomArray1 = Select.from(UOMArray.class).where(Condition.prop("base_uom_name").eq(uomBaseName)).first();
                                baseqyt = uomArray1.getBaseQty();
                                uomBaseName = uomArray1.getBaseUOMName();
                                if (i == 1) {

                                } else {
                                    if (baseqyt != 0.0) {
                                        sum *= baseqyt;

                                    }
                                    i = 0;
                                }
                            }

                            //qtyTv.setText("1");
                        }
                        int qty = 1;
                        if (qtyTv.getText() != null && qtyTv.getText().length() > 0 && !qtyTv.getText().equals("")) {
                            try {
                                qty = Integer.parseInt(qtyTv.getText().toString());
                            } catch (Exception e) {
                            }
                        }


                        addToCartImg.setImageResource(R.drawable.bluecart);
                        productInCart = new ProductInCart();
                        productInCart.sGSTPercentage = product.getSGSTPercentage();
                        productInCart.cGSTPercentage = product.getCGSTPercentage();
                        productInCart.productID = product.getProductID();
                        productInCart.productCategoryId = String.valueOf(product.getProductCategoryID());
                        productInCart.brandId = String.valueOf(product.getBrandID());
                        product.getBrandName();

                        productInCart.uomID = uomId;
                        productInCart.uomST = uomSt;
                        productInCart.productName = product.getProductName();
                        if (!product.getDefaultPrice().equals(null)) {
                            productInCart.priceOriginal = product.getDefaultPrice() * sum;
                            Log.e("$$$", "::getDefaultPrice::" + product.getDefaultPrice());
                            Log.e("$$$", "::sum::" + sum);

                        } else {
                            productInCart.priceOriginal = 0.0;
                        }

                        if (!product.getSalePrice().equals(null)) {
                            productInCart.price = product.getSalePrice() * sum;
                            Log.e("$$$", "::getDefaultPrice::" + product.getSalePrice());
                            Log.e("$$$", "::sum::" + sum);

                            Log.e("$$$", "::productInCart.price::" + productInCart.price);

                            mrpProductTv.setText(productInCart.price + "");
                        } else {
                            productInCart.price = 0.0;
                        }
                        productInCart.imageUrl = String.valueOf(product.getImageName());
                        productInCart.qty = qty;
                        productInCart.dealCategory = "";
                        productInCart.dealId = "0";
                        productInCart.dealAmount = "0";
                        productInCart.dealType = "";
                        productInCart.variantID = product.getVariantID();
                        productInCart.allowPriceEdit = product.getAllowPriceEdit();

                        productInCart.save();
                        Toast.makeText(context, "Order is updated succesfully", Toast.LENGTH_SHORT).show();
                        addCartBtn.setText("Update Order");

                    }
                }


                if (ProductInCart.listAll(ProductInCart.class).size() != 0) {
                    Log.e("cart_size", "::::" + ProductInCart.listAll(ProductInCart.class).size());

                    int sum = 0;
                    for (ProductInCart products : ProductInCart.listAll(ProductInCart.class)) {
                        sum += products.CartQty;
                    }
                    GoalsActivities.totalAmt.setText(sum + "");
                    GoalsActivities.total_items_bracket.setText("(" + sum + "" + ")");
                }


                //  productInCart = Select.from(ProductInCart.class).where(Condition.prop("product_id").eq(((AllProduct)addCartBtn.getTag()).getProductID())).first();


                // Toast.makeText(context, "" + position, Toast.LENGTH_LONG).show();
//                if (ProductInCart.listAll(ProductInCart.class).size() != 0) {
//                    GoalsActivities.totalAmt.setText(ProductInCart.listAll(ProductInCart.class).size() + "");
//                    GoalsActivities.total_items_bracket.setText("("+ProductInCart.listAll(ProductInCart.class).size() + ""+")");
//                }

              /*  if ((!(businessId == null || (businessId.equals(""))))) {
                    Bundle data = new Bundle();
                    data.putString("product_location", String.valueOf(position));
                    data.putString("product_id", String.valueOf(product.getProductID()));
                    data.putString("brand_name", product.getBrandName());
                    data.putString("category_id", String.valueOf(product.getProductCategoryID()));
                    data.putString("nameActivity", "cart_fragment");
                    FragmentTransaction ft = ((GoalsActivities) context).getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                    CartFragment fragment = new CartFragment();
                    fragment.setArguments(data);
                    ft.replace(R.id.container, fragment);
                    ft.commit();
                } else if (businessId == null || businessId.equals("")) {
                    Intent intent = new Intent(context, GoalsActivities.class);
                    intent.putExtra("nameActivity", "AllBusiness");
                    context.startActivity(intent);
                }*/
            }
        });
        productInCart = Select.from(ProductInCart.class).where(Condition.prop("variant_id").eq(((AllProduct) qtyTv.getTag()).getVariantID())).first();
        if (productInCart != null) {
            qtyTv.setText(productInCart.qty + "");
        } else {
            qtyTv.setText("1");
        }
        String sourceString = "<b>" + "Description:" + "</b> " + product.getDescription();
//        descProductTv.setText(Html.fromHtml(sourceString));
        descProductTv.setText(product.getDescription() + "");


        nameProductTv.setText(product.getProductName() + "");
        categoryTv.setText(product.getProductCategoryName() + "");

        if (product.getInStock().equals(true)) {
            switch_instock.setChecked(true);
            switch_instock.setText("In stock");
            switch_instock.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            switch_instock.setChecked(false);
            switch_instock.setText("Not Available");
            switch_instock.setTextColor(context.getResources().getColor(R.color.red));
        }
        switch_instock.setClickable(false);


        Picasso.with(context).load(product.getImageName()).placeholder(context.getResources().getDrawable(R.drawable.calendar_icon)).error(R.drawable.calendar_icon).into(productImg);


        plusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("***", "plus click");
                product = ((AllProduct) mrpProductTv.getTag());
                productInCart = Select.from(ProductInCart.class).where(Condition.prop("variant_id").eq(((AllProduct) mrpProductTv.getTag()).getVariantID())).first();
                if ((productInCart != null) && ((productInCart.variantID).equals(product.getVariantID()))) {
                    Log.e("***", "plus click productInCart not null");

                    String currentQtyString = String.valueOf(productInCart.qty);
                    int currentQtyInt = Integer.parseInt(currentQtyString);
                    if (currentQtyInt >= 0) {
                        currentQtyInt = currentQtyInt + 1;
                    } else {
                        currentQtyInt = 1;
                    }
                    qtyTv.setText(currentQtyInt + "");
                    if (productInCart != null) {
                        productInCart.qty = currentQtyInt;
                        productInCart.save();
                    }
                    Double pri = productInCart.price * productInCart.qty;

                    mrpProductTv.setText(pri + "");

                } else if ((productInCart != null) && (!(productInCart.variantID).equals(product.getVariantID()))) {
                    String currentQtyString = qtyTv.getText().toString();
                    if (currentQtyString != null && currentQtyString.length() > 0 && !currentQtyString.equals("")) {
                        try {
                            int currentQtyInt = Integer.parseInt(currentQtyString);
                            if (currentQtyInt >= 0) {
                                currentQtyInt = currentQtyInt + 1;
                            } else {
                                currentQtyInt = 1;
                            }
                            qtyTv.setText(currentQtyInt + "");
                            Double d = Double.valueOf(mrpProductTv.getText().toString());
                            Double d2 = Double.valueOf(currentQtyString);
                            Double pri = d * d2;
                            mrpProductTv.setText(pri + "");
                        } catch (Exception e) {
                        }
                    }
                } else {


                    String currentQtyString = qtyTv.getText().toString();
                    if (currentQtyString != null && currentQtyString.length() > 0 && !currentQtyString.equals("")) {
                        try {
                            int currentQtyInt = Integer.parseInt(currentQtyString);
                            if (currentQtyInt >= 0) {
                                currentQtyInt = currentQtyInt + 1;
                            } else {
                                currentQtyInt = 1;
                            }
                            qtyTv.setText(currentQtyInt + "");
                            Double d = Double.valueOf(mrpProductTv.getText().toString());
                            Double d2 = Double.valueOf(currentQtyString);
                            Double pri = d * d2;
                            mrpProductTv.setText(pri + "");
                        } catch (Exception e) {
                        }
                    }
                }
            }
        });

        minusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productInCart = Select.from(ProductInCart.class).where(Condition.prop("variant_id").eq(((AllProduct) mrpProductTv.getTag()).getVariantID())).first();
                if ((productInCart != null) && ((productInCart.variantID).equals(product.getVariantID()))) {
                    GoalsActivities.totalAmt.setText(ProductInCart.listAll(ProductInCart.class).size() + "");
                    GoalsActivities.total_items_bracket.setText("(" + ProductInCart.listAll(ProductInCart.class).size() + "" + ")");
                    String currentQtyString = String.valueOf(productInCart.qty);
                    int currentQtyInt = Integer.parseInt(currentQtyString);
                    if (currentQtyInt > 0) {
                        if (currentQtyInt == 0) {
                            currentQtyInt = 0;
                            if (productInCart != null) {
                                productInCart.delete();
                            }

                        } else {
                            currentQtyInt = currentQtyInt - 1;
                        }
                    } else {
                        currentQtyInt = 1;
                    }
                    if (currentQtyInt >= 1) {
                        qtyTv.setText(currentQtyInt + "");
                    } else {
                        qtyTv.setText(1 + "");
                        if (productInCart != null) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                            // Setting Dialog Title
                            alertDialog.setTitle("Confirm delete product...");
                            // Setting Dialog Message
                            alertDialog.setMessage("Do you want to delete this product?");
                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke YES event
                                    productInCart.delete();
                                    //  Toast.makeText(context, "You clicked on YES", Toast.LENGTH_SHORT).show();
                                }
                            });

                            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    //  Toast.makeText(context, "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                            // Showing Alert Message
                            alertDialog.show();

                        }
                    }
                    if (productInCart != null) {
                        if (currentQtyInt != 0) {
                            productInCart.qty = currentQtyInt;
                        } else {
                            productInCart.qty = 1;
                        }
                        productInCart.save();
                    }
                    if (productInCart.qty >= 1) {
                        Double pri = productInCart.price * productInCart.qty;
                        mrpProductTv.setText(pri + "");
                    } else {
                        mrpProductTv.setText(productInCart.price + "");
                    }
                    GoalsActivities.totalAmt.setText(ProductInCart.listAll(ProductInCart.class).size() + "");
                    GoalsActivities.total_items_bracket.setText("(" + ProductInCart.listAll(ProductInCart.class).size() + "" + ")");
                } else if ((productInCart != null) && (!(productInCart.variantID).equals(product.getVariantID()))) {

                    String currentQtyString = qtyTv.getText().toString();
                    if (currentQtyString != null && currentQtyString.length() > 0 && !currentQtyString.equals("")) {
                        try {
                            int currentQtyInt = Integer.parseInt(currentQtyString);
                            if (currentQtyInt > 0) {
                                if (currentQtyInt == 0) {
                                    currentQtyInt = 0;


                                } else {
                                    currentQtyInt = currentQtyInt - 1;
                                }
                            } else {
                                currentQtyInt = 1;
                            }
                            if (currentQtyInt >= 1) {
                                qtyTv.setText(currentQtyInt + "");
                            } else {
                                qtyTv.setText(1 + "");

                            }


                            qtyTv.setText(currentQtyInt + "");

                            Double d = Double.valueOf(mrpProductTv.getText().toString());
                            Double d2 = Double.valueOf(qtyTv.getText().toString());
                            Double pri = d * d2;
                            mrpProductTv.setText(pri + "");
                        } catch (Exception e) {
                        }
                    }
                } else {


                    String currentQtyString = qtyTv.getText().toString();
                    if (currentQtyString != null && currentQtyString.length() > 0 && !currentQtyString.equals("")) {
                        try {
                            int currentQtyInt = Integer.parseInt(currentQtyString);
                            if (currentQtyInt > 0) {
                                if (currentQtyInt == 0) {
                                    currentQtyInt = 0;


                                } else {
                                    currentQtyInt = currentQtyInt - 1;
                                }
                            } else {
                                currentQtyInt = 1;
                            }
                            if (currentQtyInt >= 1) {
                                qtyTv.setText(currentQtyInt + "");
                            } else {
                                qtyTv.setText(1 + "");

                            }


                            qtyTv.setText(currentQtyInt + "");

                            Double d = Double.valueOf(mrpProductTv.getText().toString());
                            Double d2 = Double.valueOf(qtyTv.getText().toString());
                            Double pri = d * d2;
                            mrpProductTv.setText(pri + "");
                        } catch (Exception e) {
                        }
                    }

                }

            }
        });


        TextView change_qty_tv = (TextView) layout.findViewById(R.id.change_qty_tv);
        change_qty_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.custome_edit_dialog);

                final EditText text = (EditText) dialog.findViewById(R.id.txt_dia);
                text.setText(qtyTv.getText() + "");
                Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
                btn_no = (Button) dialog.findViewById(R.id.btn_no);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qtyTv.setText("" + text.getText());
                        productInCart = Select.from(ProductInCart.class).where(Condition.prop("variant_id").eq(product.getVariantID())).first();
                        if (productInCart != null) {
                            productInCart.qty = Integer.parseInt(text.getText().toString());
                            productInCart.save();
                        }
                        dialog.dismiss();
                    }
                });
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        qtyTv.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.e("***", "on text changed");
                if (s.length() != 0) {
                    Log.e("***", "not nul length");


                    String currentQtyString = qtyTv.getText().toString().trim();
                    int currentQtyInt = Integer.parseInt(currentQtyString);
                    productInCart = Select.from(ProductInCart.class).where(Condition.prop("variant_id").eq(((AllProduct) qtyTv.getTag()).getVariantID())).first();
                    if ((productInCart != null) && ((productInCart.variantID).equals(product.getVariantID()))) {
                        productInCart.qty = currentQtyInt;
                        productInCart.save();
                        Double pri = productInCart.price * productInCart.qty;
                        mrpProductTv.setText(pri + "");
                    } else if ((productInCart != null) && (!(productInCart.variantID).equals(product.getVariantID()))) {
                        Double d = Double.valueOf(mrpProductTv.getText().toString());
                        Double d2 = Double.valueOf(currentQtyString);
                        Double pri = d * d2;
                        mrpProductTv.setText(pri + "");
                    } else {
                        Double d = Double.valueOf(mrpProductTv.getText().toString());
                        Double d2 = Double.valueOf(currentQtyString);
                        Double pri = d * d2;
                        mrpProductTv.setText(pri + "");
                    }



                   /* if (productInCartItem.dealType!=null) {

                        if(productInCartItem.dealType.equals("Amount")) {
                            discountAmt = discountAmt * currentQtyInt;
                            priceAmt = priceAmt * currentQtyInt;
                            holder.mrpPrice.setText(priceAmt + "");
                          //  holder.sizeOfProduct.setText(discountAmt + "");
                        }
                    }*/

                }
            }


        });


        container.addView(layout);
        return layout;


    }


    @Override
    public int getCount() {
        return allProductList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        // notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {


        return POSITION_NONE;
    }

 /*   public void getMeasurementValues() {
        UOMArray.deleteAll(UOMArray.class);
        final Gson builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
        ServiceHandler serviceHandler = NetworkManager.createRetrofitService(context, ServiceHandler.class, ((BaseActivity) context).sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);
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





                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
//
//        CommonUtils.dismissProgress();
    }*/
}


