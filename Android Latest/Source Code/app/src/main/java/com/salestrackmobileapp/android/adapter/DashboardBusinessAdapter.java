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
import com.salestrackmobileapp.android.utils.RecyclerClick;

import java.util.List;

/**
 * Created by kanchan on 5/19/2017.
 */

public class DashboardBusinessAdapter extends RecyclerView.Adapter<DashboardBusinessAdapter.ViewHolder> {

    RecyclerClick rvClick;
    Context context;
    List<GoalBusiness> allGoals;


    public DashboardBusinessAdapter(Context context, RecyclerClick rvClick) {
        this.context = context;
        this.rvClick = rvClick;
    }

    public void setListGoals() {
        this.allGoals = GoalBusiness.listAll(GoalBusiness.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_goals_item_layout, parent, false);
        DashboardBusinessAdapter.ViewHolder vh = new DashboardBusinessAdapter.ViewHolder(view, rvClick);
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
        GoalBusiness goalsAccDate = allGoals.get(position);

        holder.mainGoalTitle.setText(goalsAccDate.getBusnessName() + "");
        holder.subGoalTitle.setText(goalsAccDate.getContactPersonName() + "");
        if (goalsAccDate.getCheckedIN()) {
            holder.visitedCheck.setVisibility(View.VISIBLE);
            holder.visitedCheck.setChecked(true);
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return allGoals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public Custome_TextView mainGoalTitle, subGoalTitle;
        public ImageView proceedImg;
        RecyclerClick recyclerClick;
        CheckBox visitedCheck;

        public ViewHolder(View v, RecyclerClick recyclerClick) {
            super(v);
            mainGoalTitle = (Custome_TextView) v.findViewById(R.id.main_goal_title);
            subGoalTitle = (Custome_TextView) v.findViewById(R.id.sub_goal_title);
            visitedCheck = (CheckBox) v.findViewById(R.id.visited_ck);
            this.recyclerClick = recyclerClick;
        }

        @Override
        public void onClick(View v) {
            recyclerClick.productClick(v, getLayoutPosition());
        }
    }
}
