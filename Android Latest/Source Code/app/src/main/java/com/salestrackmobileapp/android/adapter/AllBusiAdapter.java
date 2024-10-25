package com.salestrackmobileapp.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllBusiness;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AllBusiAdapter extends RecyclerView.Adapter<AllBusiAdapter.ViewHolder> {

    RecyclerClick rvClick;
    List<AllBusiness> allBusiness = new ArrayList<>();
    public static List<AllBusiness> filterList = new ArrayList<>();
    Context context;

    public AllBusiAdapter(Context context, RecyclerClick rvClick) {
        this.context = context;
        this.rvClick = rvClick;
    }

    public void setAllBusinessList(List<AllBusiness> listAllBusiness) {
        this.allBusiness = listAllBusiness;
        this.filterList = new ArrayList<AllBusiness>();
        this.filterList.addAll(this.allBusiness);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.business_item_layout, parent, false);
        AllBusiAdapter.ViewHolder vh = new AllBusiAdapter.ViewHolder(view, rvClick);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvClick.productClick(v, viewType);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AllBusiness allBusinessItem = filterList.get(position);

        Log.e("getBusnessName ", "::::" + allBusinessItem.getBusnessName());


        Picasso.with(context).load(allBusinessItem.getImageName()).error(R.drawable.calendar_icon).into(holder.productImg);
        holder.busiNameTv.setText(allBusinessItem.getBusnessName() + "");
        holder.addressTv.setText(allBusinessItem.getAddress1() + " ");
        holder.cityTv.setText(allBusinessItem.getCity() + "(" + allBusinessItem.getState() + " )");
        holder.switch_instock.setVisibility(View.GONE);
        holder.mrp_product_tv_original.setVisibility(View.GONE);
        holder.businesstype_tv.setText(allBusinessItem.getBusinesstype());

    }

    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Custome_TextView busiNameTv, addressTv, cityTv, mrp_product_tv_original, businesstype_tv;
        public ImageView productImg;
        RecyclerClick recyclerClick;
        SwitchCompat switch_instock;

        public ViewHolder(View v, RecyclerClick recyclerClick) {
            super(v);
            productImg = (ImageView) v.findViewById(R.id.product_item_img);
            busiNameTv = (Custome_TextView) v.findViewById(R.id.product_title_tv);
            addressTv = (Custome_TextView) v.findViewById(R.id.mrp_product_tv);
            cityTv = (Custome_TextView) v.findViewById(R.id.category_tv);
            switch_instock = v.findViewById(R.id.switch_instock);
            mrp_product_tv_original = v.findViewById(R.id.mrp_product_tv_original);
            businesstype_tv = v.findViewById(R.id.businesstype_tv);
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
                if (filterList.size() > 0 || filterList != null) {
                    filterList.clear();
                }

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    filterList.addAll(allBusiness);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (AllBusiness item : allBusiness) {
                        if (item.getBusnessName().toLowerCase().contains(text.toLowerCase())) {
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
