package com.salestrackmobileapp.android.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.orm.query.Condition;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllProduct;
import com.salestrackmobileapp.android.gson.AllVariants;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.salestrackmobileapp.android.utils.RecyclerViewAnimator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UserPC on 16-Mar-18.
 */

public class ProductVariantAdapter extends RecyclerView.Adapter<ProductVariantAdapter.ViewHolder> {

    RecyclerClick rvClick;
    public List<AllVariants> allVariantsList = new ArrayList<>();
    RecyclerView recyclerView;
    Context context;
    private RecyclerViewAnimator mAnimator;
    AllVariants allVariants;

    SharedPreferences preferences;
    ProductPagerAdapter productPagerAdapter;

    int productPosition;
    List<AllProduct> allProductList;
    List<String> listUomArray = new ArrayList<String>();
    List<Integer> listUomIdArray = new ArrayList<Integer>();
    int location;
    int selectedVariantID;
    ProgressDialog mProgressDialog;
    public int row_index = -1;

    public ProductVariantAdapter(Context context, RecyclerView recyclerView, ProductPagerAdapter productPagerAdapter, List<AllVariants> allVariantList, int position, List<AllProduct> listAllProduct, List<String> listUomArray, List<Integer> listUomIdArray, int location, Integer variantID) {
        this.context = context;

        this.recyclerView = recyclerView;
        mAnimator = new RecyclerViewAnimator(this.recyclerView);
        preferences = context.getSharedPreferences("MyPref", 0);
        this.productPagerAdapter = productPagerAdapter;
        this.allVariantsList = allVariantList;
        this.productPosition = position;
        this.allProductList = listAllProduct;
        this.listUomArray = listUomArray;
        this.listUomIdArray = listUomIdArray;
        this.location = location;
        this.selectedVariantID = variantID;


    }

//    public void setVariants(List<AllVariants> allVariantList) {
//        this.allVariantsList = allVariantList;
//    }


    @Override
    public ProductVariantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_variant, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ProductVariantAdapter.ViewHolder vh = new ProductVariantAdapter.ViewHolder(v);


        //   mAnimator.onCreateViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ProductVariantAdapter.ViewHolder holder, final int position) {
        final AllVariants allVariants = allVariantsList.get(position);

        Picasso.with(context).load(allVariants.getImageName()).placeholder(context.getResources().getDrawable(R.drawable.calendar_icon)).error(R.drawable.calendar_icon).into(holder.ivVariant);
        holder.tvVariant.setText(allVariants.getName());
        if (selectedVariantID == allVariants.getVariantID()) {
            CommonUtils.variantID = allVariants.getVariantID();
        }

        if (allVariants.getVariantID() == CommonUtils.variantID) {
            holder.linearLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.all_border_blue));
            holder.tvVariant.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

        } else {
            holder.linearLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.all_border_grey));
            holder.tvVariant.setTextColor(context.getResources().getColor(R.color.grey));
        }
//
//        if (allVariants.getVariantID() == selectedVariantID) {
//            holder.linearLayout.setSelected(true);
//
//        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            //
            @Override
            public void onClick(View view) {
                Log.e("***", ":::purchasePrice:::" + allVariants.getPurchasePrice());
                Log.e("***", ":::SalePrice:::" + allVariants.getSalePrice());

                if (position > 2) {
                    Log.e("***", ":::position:::" + position);


                    recyclerView.scrollToPosition(position);
                }

//
                if (context != null) {
                    mProgressDialog = new ProgressDialog(context);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.setCanceledOnTouchOutside(true);
                    mProgressDialog.show();
                    Log.e("dialog", "true");
                }


                try {
                    CommonUtils.variantID = allVariants.getVariantID();
                    Integer productID = new Integer("" + allVariants.getProductID());
                    Log.e("$$$", "::::variant product ID::" + productID);
                    AllProduct allProduct = Select.from(AllProduct.class).where(Condition.prop("product_id").eq(productID)).first();
                    allProduct.setDefaultPrice(allVariants.getPurchasePrice());
                    allProduct.setSalePrice(allVariants.getSalePrice());
                    allProduct.setDescription(allVariants.getDescription());
                    allProduct.setImageName(allVariants.getImageName());
                    allProduct.setProductID(productID);
                    allProduct.setInStock(allVariants.getInStock());
                    Integer variantId = new Integer("" + allVariants.getVariantID());
                    allProduct.setVariantID(variantId);
                    allProduct.setProductName(allVariants.getName());
                    allProduct.save();
                    if (allProductList.size() > productPosition) {

                        allProductList.get(productPosition).setDefaultPrice(allVariants.getPurchasePrice());
                        allProductList.get(productPosition).setSalePrice(allVariants.getSalePrice());
                        allProductList.get(productPosition).setDescription(allVariants.getDescription());
                        allProductList.get(productPosition).setImageName(allVariants.getImageName());
                        allProductList.get(productPosition).setProductID(productID);
                        allProductList.get(productPosition).setInStock(allVariants.getInStock());
                        allProductList.get(productPosition).setProductName(allVariants.getName());
                        allProductList.get(productPosition).setVariantID(variantId);

                        productPagerAdapter.setListOfProduct(allProductList, listUomArray, listUomIdArray, productPosition);

                        productPagerAdapter.notifyDataSetChanged();
                        hideDialog();
                    } else {
                        hideDialog();
                    }
                } catch (Exception e) {
                    Log.e("exception", "::::" + e.toString());
                    hideDialog();
                }
//
//
            }

        });


    }

    @Override
    public int getItemCount() {
        /* return allProductList.size();*/
        return allVariantsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Custome_TextView tvVariant;
        public ImageView ivVariant;
        LinearLayout linearLayout, llVariantImage;


        public ViewHolder(View v) {
            super(v);
            tvVariant = (Custome_TextView) v.findViewById(R.id.tvVariant);

            ivVariant = (ImageView) v.findViewById(R.id.ivVariant);
            linearLayout = v.findViewById(R.id.linearLayout);
            llVariantImage = v.findViewById(R.id.llVariantImage);


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
            Log.e("hideDialog", "true");


        }
    }

    // Do Search...

}

