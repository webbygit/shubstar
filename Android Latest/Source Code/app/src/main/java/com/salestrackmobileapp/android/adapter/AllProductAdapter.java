package com.salestrackmobileapp.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllProduct;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.salestrackmobileapp.android.utils.RecyclerViewAnimator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanchan on 3/16/2017.
 */

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.ViewHolder> {

    RecyclerClick rvClick;
    List<AllProduct> allProductList = new ArrayList<>();
    public static List<AllProduct> filterList = new ArrayList<>();
    RecyclerView recyclerView;
    Context context;
    private RecyclerViewAnimator mAnimator;
    ProductInCart productInCart;

    List<String> listUomArray = new ArrayList<String>();
    List<Integer> listUomIdArray = new ArrayList<Integer>();
    boolean isAdded;
    SharedPreferences preferences;


    public AllProductAdapter(Context context, RecyclerClick rvClick, RecyclerView recyclerView) {
        this.context = context;
        this.rvClick = rvClick;
        this.recyclerView = recyclerView;
        mAnimator = new RecyclerViewAnimator(this.recyclerView);
        preferences = context.getSharedPreferences("MyPref", 0);

    }

    public void setListProduct(List<AllProduct> allProductList, List<String> listUomArray, List<Integer> listUomIdArray) {
        this.allProductList = allProductList;
        this.listUomArray = listUomArray;
        this.listUomIdArray = listUomIdArray;
        this.filterList = new ArrayList<AllProduct>();
        this.filterList.addAll(this.allProductList);
    }


    @Override
    public AllProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, rvClick);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvClick.productClick(v, viewType);

            }
        });
        //   mAnimator.onCreateViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AllProduct product = filterList.get(position);
        Picasso.with(context).load(filterList.get(position).getImageName()).error(R.drawable.calendar_icon).into(holder.productImg);
        holder.productNameTv.setText(filterList.get(position).getProductName() + "");
        holder.mrpTv.setText("₹" + " " + filterList.get(position).getSalePrice());
        holder.mrp_product_tv_original.setText("₹" + " " + filterList.get(position).getDefaultPrice());
        holder.mrp_product_tv_original.setPaintFlags(holder.mrp_product_tv_original.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.categoryTv.setText(filterList.get(position).getDescription() + "");
        Log.e("InStock", ":::" + filterList.get(position).getInStock());
        if (filterList.get(position).getInStock().equals(true)) {
            holder.switch_instock.setChecked(true);
            holder.switch_instock.setClickable(false);
            holder.switch_instock.setText("In stock");
            holder.switch_instock.setTextColor(context.getResources().getColor(R.color.green));

        } else {
            holder.switch_instock.setChecked(false);
            holder.switch_instock.setClickable(false);
            holder.switch_instock.setText("N.A.");
            holder.switch_instock.setTextColor(context.getResources().getColor(R.color.red));
        }

//        if (position==0){
//            holder.switch_instock.setChecked(true);
//            holder.switch_instock.setClickable(false);
//        }
//        else if (position==1){
//            holder.switch_instock.setChecked(false);
//            holder.switch_instock.setClickable(false);
//        }
//        else{
//            holder.switch_instock.setChecked(true);
//            holder.switch_instock.setClickable(false);
//        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.single_text_item, listUomArray);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        holder.spinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        holder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int proQty = 0;

                if (!preferences.getBoolean("" + product.getProductID(), false)) {


                    //  Toast.makeText(context, "Add to Cart Click", Toast.LENGTH_SHORT).show();

                    productInCart = Select.from(ProductInCart.class).where(Condition.prop("product_id").eq(product.getProductID())).first();
                    isAdded = true;
                    Log.e("***ProductID", "::::::" + product.getProductID());

                    if (productInCart == null) {

//                        editor.putBoolean("")
                        productInCart = new ProductInCart();
                        productInCart.productID = product.getProductID();
                        productInCart.productCategoryId = String.valueOf(product.getProductCategoryID());
                        productInCart.brandId = String.valueOf(product.getBrandID());
                        productInCart.uomID = product.getDefaultUOMID();
                        //     Log.e("SET_UOM_default",":::"+product.getDefaultUOMID());
                        productInCart.uomST = product.getUOMMapping();
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("" + product.getProductID(), true);
                        editor.commit();


                        productInCart.cGSTPercentage = product.getCGSTPercentage();


                        productInCart.sGSTPercentage = product.getSGSTPercentage();
                        productInCart.variantID = product.getVariantID();
                        product.getBrandName();

                        //  productInCart.uomID = /*product.getUOMMapping();*/1;
                        productInCart.uomST = product.getUOMMapping();
                        productInCart.productName = product.getProductName();
                        if (productInCart.uomID == 364) {

                            productInCart.baseQty = 1.0;


                        } else if (productInCart.uomID == 384) {
                            productInCart.baseQty = 12.0;

                        } else if (productInCart.uomID == 614) {
                            productInCart.baseQty = 48.0;

                        } else {
                            productInCart.baseQty = 1.0;

                        }
                        if (!product.getDefaultPrice().equals(null)) {
                            productInCart.priceOriginal = product.getDefaultPrice() * productInCart.baseQty;
                        } else {
                            productInCart.priceOriginal = 0.0;
                        }


                        if (!product.getSalePrice().equals(null)) {
                            productInCart.price = product.getSalePrice() * productInCart.baseQty;
                        } else {
                            productInCart.price = 0.0;
                        }
                        productInCart.imageUrl = String.valueOf(product.getImageName());
                        productInCart.qty = 1;
                        productInCart.CartQty = 1;


                        productInCart.dealCategory = "";
                        productInCart.dealId = "0";
                        productInCart.dealAmount = "0";
                        productInCart.dealType = "";
                        Log.e("AllowPriceEdit", "::::" + product.getAllowPriceEdit());
                        productInCart.allowPriceEdit = product.getAllowPriceEdit();


                        productInCart.save();
                        proQty = proQty + productInCart.qty;


//                        Animation animation = new AlphaAnimation(1, 0);
//                        animation.setDuration(500);
//                        animation.setInterpolator(new LinearInterpolator());
//                        animation.setRepeatCount(0);
//                        animation.setRepeatMode(Animation.REVERSE);
//                        GoalsActivities.cartImg.startAnimation(animation);


                    } else {
                        productInCart.uomST = product.getUOMMapping();
                        productInCart.uomID = product.getDefaultUOMID();
                        if (productInCart.uomID == 364) {

                            productInCart.baseQty = 1.0;


                        } else if (productInCart.uomID == 384) {
                            productInCart.baseQty = 12.0;

                        } else if (productInCart.uomID == 614) {
                            productInCart.baseQty = 48.0;

                        } else {
                            productInCart.baseQty = 1.0;

                        }
                        productInCart.save();
                        proQty = proQty + productInCart.qty;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("" + product.getProductID(), true);
                        editor.commit();


//                        Animation animation = new AlphaAnimation(1, 0);
//                        animation.setDuration(500);
//                        animation.setInterpolator(new LinearInterpolator());
//                        animation.setRepeatCount(0);
//                        animation.setRepeatMode(Animation.REVERSE);
//                        GoalsActivities.cartImg.startAnimation(animation);


                    }


//                if (ProductInCart.listAll(ProductInCart.class).size() != 0) {


                    //   }

                    //
                } else {
                    Toast.makeText(context, "Alredy added in cart", Toast.LENGTH_SHORT).show();
                }
                Log.e("###", ":::productIncartSize:::" + ProductInCart.listAll(ProductInCart.class).size());

                if (ProductInCart.listAll(ProductInCart.class).size() != 0) {
                    GoalsActivities.totalAmt.setVisibility(View.VISIBLE);
                    int sum = 0;
                    for (ProductInCart products : ProductInCart.listAll(ProductInCart.class)) {
                        sum += products.qty;
                    }
                    GoalsActivities.totalAmt.setText(sum + "");
                    GoalsActivities.total_items_bracket.setText("(" + sum + "" + ")");
                }


            }

        });

        //mAnimator.onBindViewHolder(holder.itemView, position);
        // holder.description.setText(allProductList.get(position).getDefaultPrice() + "");
    }

    @Override
    public int getItemCount() {
        /* return allProductList.size();*/
        return (null != filterList ? filterList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public Custome_TextView productNameTv, mrpTv, categoryTv, mrp_product_tv_original;
        public ImageView productImg;
        Button addtocart;
        RecyclerClick recyclerClick;
        Spinner spinner;
        SwitchCompat switch_instock;

        public ViewHolder(View v, RecyclerClick recyclerClick) {
            super(v);
            productNameTv = (Custome_TextView) v.findViewById(R.id.product_title_tv);
            mrpTv = (Custome_TextView) v.findViewById(R.id.mrp_product_tv);
            categoryTv = (Custome_TextView) v.findViewById(R.id.category_tv);
            productImg = (ImageView) v.findViewById(R.id.product_item_img);
            addtocart = (Button) v.findViewById(R.id.addtocart);
            spinner = (Spinner) v.findViewById(R.id.measurmentSpn);
            switch_instock = v.findViewById(R.id.switch_instock);
            mrp_product_tv_original = v.findViewById(R.id.mrp_product_tv_original);
            addtocart.setVisibility(View.VISIBLE);
            this.recyclerClick = recyclerClick;
        }

        @Override
        public void onClick(View v) {
            recyclerClick.productClick(v, getLayoutPosition());
        }
    }

    // Do Search...
    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {
                    if (filterList != null) {

                        filterList.addAll(allProductList);
                    }

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (AllProduct item : allProductList) {
                        if (item.getProductName().toLowerCase().contains(text.toLowerCase())) {
                            // Adding Matched items
                            filterList.add(item);
                        }
                    }
                }

                // Set on UI Thread
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }
}
