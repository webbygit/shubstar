package com.salestrackmobileapp.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.ProductList;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kanchan on 6/2/2017.
 */

public class DealsProductAdapter extends RecyclerView.Adapter<DealsProductAdapter.ViewHolder> {

    Context context;
    RecyclerClick rvClick;
    List<ProductList> allDealProductList;

    public DealsProductAdapter(Context context, RecyclerClick rvClick) {
        this.context = context;
        this.rvClick = rvClick;
    }

    public void setListDealProduct(List<ProductList> allProductList) {
        allDealProductList = allProductList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, rvClick);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvClick.productClick(v, viewType);

            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProductList productList = allDealProductList.get(position);
        Log.e("ImageName", ":::" + productList.getImageName());
        Log.e("Description", "::::" + productList.getDescription());
        Log.e("CategoryNAme", "::::" + productList.getProductCategoryName());


        Picasso.with(context).load(allDealProductList.get(position).getImageName()).placeholder(context.getResources().getDrawable(R.drawable.calendar_icon)).error(R.drawable.calendar_icon).into(holder.productImg);
        holder.productNameTv.setText(allDealProductList.get(position).getProductName() + "");
        holder.mrpTv.setText(allDealProductList.get(position).getDefaultPrice() + "");
        holder.categoryTv.setText(allDealProductList.get(position).getDescription() + "");
        holder.switch_instock.setVisibility(View.GONE);
        holder.mrp_product_tv_original.setVisibility(View.GONE);
      /*  Picasso.with(context).load(filterList.get(position).getImageName()).placeholder(context.getResources().getDrawable(R.drawable.calendar_icon)).error(R.drawable.calendar_icon).into(holder.productImg);
        holder.mainGoalTitle.setText(filterList.get(position).getProductName() + "");
        holder.description.setText(filterList.get(position).getDefaultPrice() + "");
        holder.qtyTv.setText(filterList.get(position).getDescription() + "");
        mAnimator.onBindViewHolder(holder.itemView, position);*/
    }

    @Override
    public int getItemCount() {
        return allDealProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public Custome_TextView productNameTv, mrpTv, categoryTv, mrp_product_tv_original;
        public ImageView productImg;
        RecyclerClick recyclerClick;
        SwitchCompat switch_instock;

        public ViewHolder(View v, RecyclerClick recyclerClick) {
            super(v);
            productNameTv = (Custome_TextView) v.findViewById(R.id.product_title_tv);
            mrpTv = (Custome_TextView) v.findViewById(R.id.mrp_product_tv);
            categoryTv = (Custome_TextView) v.findViewById(R.id.category_tv);
            productImg = (ImageView) v.findViewById(R.id.product_item_img);
            switch_instock = v.findViewById(R.id.switch_instock);
            mrp_product_tv_original = v.findViewById(R.id.mrp_product_tv_original);
            this.recyclerClick = recyclerClick;
        }

        @Override
        public void onClick(View v) {
            recyclerClick.productClick(v, getLayoutPosition());
        }
    }

}
