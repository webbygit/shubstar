package com.salestrackmobileapp.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.orm.query.Condition;
import com.orm.query.Select;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.Business;
import com.salestrackmobileapp.android.gson.SaveOrder;
import com.salestrackmobileapp.android.utils.RecyclerClickInterface;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanchan on 3/18/2017.
 */

public class PendingOrderHistoryAdapter extends RecyclerView.Adapter<PendingOrderHistoryAdapter.ViewHolder> {

    RecyclerClickInterface rvClick;
    RecyclerView recyclerView;
    List<SaveOrder> saveOrderList;
    List<SaveOrder> saveOrderAll;
    Context context;

    public PendingOrderHistoryAdapter(Context context, RecyclerClickInterface rvClick, RecyclerView recyclerView) {
        this.context = context;
        this.rvClick = rvClick;
        this.recyclerView = recyclerView;
    }


    public void setSaveOrderList(List<SaveOrder> saveOrderList) {
        this.saveOrderAll = saveOrderList;
        this.saveOrderList = new ArrayList<>();
        this.saveOrderList.addAll(this.saveOrderAll);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.order_history_main, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, rvClick);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvClick.productClick(v, viewType, recyclerView);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String dateSt = saveOrderList.get(position).getOrderDate();
        String dateArray[] = dateSt.split("T");
        int loc = position + 1;

        holder.order_number.setText("Order Number: " + "STA000000" + loc + "");
//        holder.order_number.setText("Order Number :"+loc+ "");
        holder.mainGoalTitle.setText("Order on :" + dateArray[0] + "");

        holder.description.setText("Total Amount:" + saveOrderList.get(position).getTotalOrderValue() + "");

        // Log.e("ORDER__",":::::BUSINESS_NAME::::"+saveOrderList.get(position).getBusinessName());
        holder.business_name.setText("Business:" + saveOrderList.get(position).getBusinessName());
        if (saveOrderList.get(position).getSendToServer().equals("0")) {
            holder.status.setText("not sent to server ");
            holder.status.setBackgroundColor(context.getResources().getColor(R.color.red));

        } else {
            holder.status.setText("Processing");
            holder.status.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
        try {
            List<Business> businessList = Select.from(Business.class).where(Condition.prop("Business_id").eq(saveOrderList.get(position).getBusinessID())).list();
            if (businessList != null && businessList.size() > 0) {
                holder.business_type.setText("Business Type: " + businessList.get(0).getBusinessType());
            }

        } catch (Exception e) {
            Log.e("error", ":::" + e.toString());
        }

        //holder.status.setBackgroundColor(context.getResources().getColor(R.color.green));
    }

    @Override
    public int getItemCount() {
        return saveOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        Custome_BoldTextView business_name;
        public Custome_TextView description, status, mainGoalTitle, order_number, business_type;
        RecyclerClickInterface recyclerClick;

        public ViewHolder(View v, RecyclerClickInterface recyclerClick) {
            super(v);
            order_number = (Custome_TextView) v.findViewById(R.id.order_number);
            mainGoalTitle = (Custome_TextView) v.findViewById(R.id.main_goal_title);
            description = (Custome_TextView) v.findViewById(R.id.sub_goal_title);
            status = (Custome_TextView) v.findViewById(R.id.status_text);
            business_name = (Custome_BoldTextView) v.findViewById(R.id.business_name);
            business_type = v.findViewById(R.id.business_type);
            this.recyclerClick = recyclerClick;
        }

        @Override
        public void onClick(View v) {
            recyclerClick.productClick(v, getLayoutPosition(), recyclerView);
        }
    }

    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                saveOrderList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    saveOrderList.addAll(saveOrderAll);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (SaveOrder item : saveOrderAll) {
                        if (item.getBusinessName().toLowerCase().contains(text.toLowerCase())) {
                            // Adding Matched items
                            saveOrderList.add(item);
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
