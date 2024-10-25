package com.salestrackmobileapp.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;


import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.GoalBusiness;
import com.salestrackmobileapp.android.gson.GoalsAccDate;
import com.salestrackmobileapp.android.utils.RecyclerClick;

import java.util.List;

/**
 * Created by kanchan on 3/12/2017.
 */

public class MyGoalsAdapter extends RecyclerView.Adapter<MyGoalsAdapter.ViewHolder> {

    RecyclerClick rvClick;
    Context context;
    List<GoalBusiness> allGoals;
    List<GoalsAccDate> allGoalsDates;


    public MyGoalsAdapter(Context context, RecyclerClick rvClick) {
        this.context = context;
        this.rvClick = rvClick;
    }

    public void setListGoals() {
        this.allGoals = GoalBusiness.listAll(GoalBusiness.class);
        this.allGoalsDates = GoalsAccDate.listAll(GoalsAccDate.class);
    }

    @Override
    public MyGoalsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_goals_item_layout, parent, false);
        MyGoalsAdapter.ViewHolder vh = new MyGoalsAdapter.ViewHolder(view, rvClick);
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
        if (allGoals.size() > position) {
            GoalBusiness goalsAccDate = allGoals.get(position);
            holder.mainGoalTitle.setText(goalsAccDate.getBusnessName() + "");
            holder.subGoalTitle.setText(goalsAccDate.getContactPersonName() + "");
            if (!(goalsAccDate.getAddress2().equals("") || goalsAccDate.getAddress2().equals(null))) {
                holder.addressTv.setText(goalsAccDate.getAddress1() + "" + goalsAccDate.getAddress2());
            } else {
                holder.addressTv.setText(goalsAccDate.getAddress1());
            }

            if (goalsAccDate.getCheckedIN()) {
                //  holder.visitedCheck.setVisibility(View.VISIBLE);
                holder.visitedCheck.setChecked(true);
            } else {
                // holder.visitedCheck.setVisibility(View.GONE);
                holder.visitedCheck.setChecked(false);
            }
        }

        if (allGoalsDates.size() > 0) {
            GoalsAccDate goalsAccDate1 = allGoalsDates.get(position);
            holder.tvGoalTitle.setText(goalsAccDate1.getGoalTitle());
            holder.tvGoalAmount.setText("₹ " + "" + goalsAccDate1.getAmountID());
            holder.tvGoalQuantity.setText("Qty " + goalsAccDate1.getQuantityID());
        }


    }

    @Override
    public int getItemCount() {
        return allGoalsDates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public Custome_TextView mainGoalTitle, subGoalTitle, addressTv, tvGoalTitle, tvGoalAmount, tvGoalQuantity;
        public ImageView proceedImg;
        RecyclerClick recyclerClick;
        CheckBox visitedCheck;

        public ViewHolder(View v, RecyclerClick recyclerClick) {
            super(v);
            mainGoalTitle = (Custome_TextView) v.findViewById(R.id.main_goal_title);
            subGoalTitle = (Custome_TextView) v.findViewById(R.id.sub_goal_title);
            addressTv = (Custome_TextView) v.findViewById(R.id.start_date);
            visitedCheck = (CheckBox) v.findViewById(R.id.visited_ck);
            tvGoalTitle = (Custome_TextView) v.findViewById(R.id.tvGoalTitle);
            tvGoalAmount = (Custome_TextView) v.findViewById(R.id.tvGoalAmount);
            tvGoalQuantity = (Custome_TextView) v.findViewById(R.id.tvGoalQuantity);


            this.recyclerClick = recyclerClick;
        }

        @Override
        public void onClick(View v) {
            recyclerClick.productClick(v, getLayoutPosition());
        }
    }
}
