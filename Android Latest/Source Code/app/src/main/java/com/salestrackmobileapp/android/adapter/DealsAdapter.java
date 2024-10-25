package com.salestrackmobileapp.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllDeal;
import com.salestrackmobileapp.android.utils.RecyclerClick;

import java.util.List;

/**
 * Created by kanchan on 4/12/2017.
 */

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> {

    //AllDeal
    RecyclerClick rvClick;
    Context context;
    List<AllDeal> allDeals;

    public DealsAdapter(Context context, RecyclerClick rvClick) {
        this.context = context;
        this.rvClick = rvClick;
    }

    public void setListDeal() {
        this.allDeals = AllDeal.listAll(AllDeal.class);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.deal_item, parent, false);
        DealsAdapter.ViewHolder vh = new DealsAdapter.ViewHolder(view, rvClick);
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
        AllDeal allDealItem = allDeals.get(position);
        if (allDealItem.getAmount() != null) {

            Log.e("DEAL+TYPE", ":::" + allDealItem.getDealType());
            if (allDealItem.getDealApplicableAs().contains("Percentage")) {
                holder.mainGoalTitle.setText(" " + allDealItem.getAmount() + "% off/-");
            } else {
                holder.mainGoalTitle.setText(" Rs/-" + allDealItem.getAmount() + " OFF");
            }
        }


        //  holder.subGoalTitle.setText("applicable as"+allDealItem.getDealApplicableAs()+"");
        String dateSt = allDealItem.getStartDate();
        String dateArray[] = dateSt.split("T");

        String dateSt2 = allDealItem.getEndDate();
        String dateArray1[] = dateSt2.split("T");

        holder.startDateTv.setText("valid from:- " + dateArray[0] + "");
        holder.endDateTv.setText("end date:- " + dateArray1[0] + "");
        holder.subGoalTitle.setText("applicable on " + allDealItem.getDealApplicableAs() + "");
    }

    @Override
    public int getItemCount() {
        return allDeals.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public Custome_BoldTextView mainGoalTitle;
        public Custome_TextView subGoalTitle, startDateTv, endDateTv;
        public ImageView proceedImg;
        RecyclerClick recyclerClick;

        public ViewHolder(View v, RecyclerClick recyclerClick) {
            super(v);
            mainGoalTitle = (Custome_BoldTextView) v.findViewById(R.id.main_goal_title);
            subGoalTitle = (Custome_TextView) v.findViewById(R.id.sub_goal_title);
            startDateTv = (Custome_TextView) v.findViewById(R.id.start_date);
            endDateTv = (Custome_TextView) v.findViewById(R.id.end_date);


            this.recyclerClick = recyclerClick;
        }

        @Override
        public void onClick(View v) {
            recyclerClick.productClick(v, getLayoutPosition());
        }
    }
}
