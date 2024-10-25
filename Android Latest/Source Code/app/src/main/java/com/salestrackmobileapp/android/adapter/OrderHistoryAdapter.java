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
import com.salestrackmobileapp.android.gson.GoalBusinessBackup;
import com.salestrackmobileapp.android.gson.OrderHistory;
import com.salestrackmobileapp.android.utils.RecyclerClickInterface;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanchan on 3/18/2017.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    RecyclerClickInterface rvClick;
    List<OrderHistory> saveOrderList = new ArrayList<>();
    List<OrderHistory> saveOrderAll = new ArrayList<>();

    Context context;


    RecyclerView recyclerView;

    public OrderHistoryAdapter(Context context, RecyclerClickInterface rvClick, RecyclerView recyclerView) {
        this.context = context;
        this.rvClick = rvClick;
        this.recyclerView = recyclerView;
    }

    public void setSaveOrderList(List<OrderHistory> saveOrderList) {
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
        int loc = saveOrderList.get(position).getOrderPositions();

        Log.e("OrderNUM", ":::::" + loc);

        holder.order_number.setText("Order Number: " + "STA000000" + loc + "");

        holder.mainGoalTitle.setText("Order on :" + dateArray[0] + "");
        Log.e("TOAL_AMT", ":::::" + saveOrderList.get(position).getTotalOrderValue());
//        Double discount=saveOrderList.get(position).getDiscountAmount();
//        Double taxAmount=saveOrderList.get(position).getTaxAmount();
//        Double totalAmount=(saveOrderList.get(position).getTotalOrderValue()+taxAmount)-discount;
//       Double parsedAmount= Double.parseDouble(new DecimalFormat("##.##").format((totalAmount)));
//        Log.e("totalAmount",":::::"+parsedAmount);
        holder.description.setText("Total Amount:" + saveOrderList.get(position).getTotalOrderValue() + "");
        holder.business_name.setText("Business:" + saveOrderList.get(position).getBusinessName());
        try {
            List<Business> businessList = Select.from(Business.class).where(Condition.prop("Business_id").eq(saveOrderList.get(position).getBusinessID())).list();
            if (businessList != null && businessList.size() > 0) {
                holder.business_type.setText("Business Type: " + businessList.get(0).getBusinessType());
            }

        } catch (Exception e) {
            Log.e("error", ":::" + e.toString());
        }


        // Log.e("BUSINESS",":::"+saveOrderList.get(position).getBusiness());
        //  holder.business_type.setText("Business Type:"+saveOrderList.get(position).getBusiness().getBusinessType());
        holder.status.setVisibility(View.GONE);

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
                    for (OrderHistory item : saveOrderAll) {
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
