package com.salestrackmobileapp.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.salestrackmobileapp.android.gson.BusinessOrder;
import com.salestrackmobileapp.android.utils.RecyclerClick;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by kanchan on 7/21/2017.
 */

public class OrderHistoryAccDateAdapter extends RecyclerView.Adapter<OrderHistoryAccDateAdapter.ViewHolder> {

    RecyclerClick rvClick;
    List<BusinessOrder> saveOrderList;
    Context context;

    public OrderHistoryAccDateAdapter(Context context, RecyclerClick rvClick) {
        this.context = context;
        this.rvClick = rvClick;
    }

    public void setSaveOrderList(List<BusinessOrder> saveOrderList) {
        this.saveOrderList = saveOrderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.order_history_main, parent, false);
        // set the view's size, margins, paddings and layout parameters
        OrderHistoryAccDateAdapter.ViewHolder vh = new OrderHistoryAccDateAdapter.ViewHolder(v, rvClick);
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

        String dateSt = saveOrderList.get(position).getOrderDate();
        String dateArray[] = dateSt.split("T");
        int loc = position + 1;
        holder.business_name.setText("Business : " + saveOrderList.get(position).getBusinessName() + "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        try {
            dateFormat.parse(saveOrderList.get(position).getOrderDate());
            holder.mainGoalTitle.setText("Order on :" + dateFormat.parse(saveOrderList.get(position).getOrderDate() + "") + "");
        } catch (ParseException e) {
            e.printStackTrace();
            holder.mainGoalTitle.setText("Order on :" + saveOrderList.get(position).getOrderDate() + "");
        }
        try {
            List<Business> businessList = Select.from(Business.class).where(Condition.prop("Business_id").eq(saveOrderList.get(position).getBusinessID())).list();
            if (businessList != null && businessList.size() > 0) {
                holder.business_type.setText("Business Type: " + businessList.get(0).getBusinessType());
            }

        } catch (Exception e) {
            Log.e("error", ":::" + e.toString());
        }


        holder.description.setText("Total Amount:" + saveOrderList.get(position).getTotalOrderValue() + "");
        holder.status.setVisibility(View.GONE);
        holder.order_number.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return saveOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        //  Custome_BoldTextView  ;
        public Custome_TextView description, status, order_number, mainGoalTitle, business_type;
        Custome_BoldTextView business_name;
        RecyclerClick recyclerClick;

        public ViewHolder(View v, RecyclerClick recyclerClick) {
            super(v);
            order_number = (Custome_TextView) v.findViewById(R.id.order_number);
            mainGoalTitle = (v.findViewById(R.id.main_goal_title));
            description = (Custome_TextView) v.findViewById(R.id.sub_goal_title);
            status = (Custome_TextView) v.findViewById(R.id.status_text);
            this.recyclerClick = recyclerClick;
            business_name = v.findViewById(R.id.business_name);
            business_type = v.findViewById(R.id.business_type);
        }

        @Override
        public void onClick(View v) {
            recyclerClick.productClick(v, getLayoutPosition());
        }
    }

}
