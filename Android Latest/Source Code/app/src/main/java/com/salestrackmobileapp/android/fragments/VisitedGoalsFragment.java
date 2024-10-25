package com.salestrackmobileapp.android.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orm.query.Select;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.VisitedBusinessAdapter;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.GoalBusiness;
import com.salestrackmobileapp.android.gson.VisitedGoals;
import com.salestrackmobileapp.android.utils.RecyclerClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VisitedGoalsFragment extends BaseFragment implements RecyclerClick {

    @BindView(R.id.visitesRv)
    RecyclerView visitedRv;
    @BindView(R.id.no_item)
    Custome_TextView noItems;
    private LinearLayoutManager mLayoutManager;
    List<GoalBusiness> goals;
    VisitedGoals visitedGoals;
    VisitedBusinessAdapter businessAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visited_goals, container, false);
        ButterKnife.bind(this, view);
        //  visitedGoals = new VisitedGoals();
        mLayoutManager = new LinearLayoutManager(baseActivity);
        //  VisitedGoals.deleteAll(VisitedGoals.class);
        goals = GoalBusiness.find(GoalBusiness.class, "checkedin = ?", "1");
        if (goals != null) {
            for (int i = 0; i < goals.size(); i++) {
                visitedGoals = new VisitedGoals();
                GoalBusiness goalBusiness = goals.get(i);
                visitedGoals.setBusinessID(goalBusiness.getBusinessID());
                visitedGoals.setBusnessName(goalBusiness.getBusnessName());
                visitedGoals.setAddress1(goalBusiness.getAddress1());
                visitedGoals.setAddress2(goalBusiness.getAddress2());
                visitedGoals.setZipCode(goalBusiness.getZipCode());
                visitedGoals.setWebsiteName(goalBusiness.getWebsiteName());
                visitedGoals.setCheckedIN(goalBusiness.getCheckedIN());
                visitedGoals.setCity(goalBusiness.getCity());
                visitedGoals.setContactPersonEmail(goalBusiness.getContactPersonEmail());
                visitedGoals.setContactPersonName(goalBusiness.getContactPersonName());
                visitedGoals.setContactPersonPhone(goalBusiness.getContactPersonPhone());
                visitedGoals.setCountry(goalBusiness.getCountry());
                visitedGoals.setDefaultGoalbusinessID(goalBusiness.getDefaultGoalbusinessID());
                visitedGoals.setState(goalBusiness.getState());
                visitedGoals.setImageName(goalBusiness.getImageName());
                visitedGoals.save();
            }
        } else {
            noItems.setVisibility(View.VISIBLE);
            Toast.makeText(baseActivity, "no visited business available..", Toast.LENGTH_SHORT).show();
        }


        visitedRv.setLayoutManager(mLayoutManager);
        businessAdapter = new VisitedBusinessAdapter(baseActivity, this);
        businessAdapter.setListGoals();
        visitedRv.setAdapter(businessAdapter);
        businessAdapter.notifyDataSetChanged();
        if (Select.from(VisitedGoals.class).list().size() == 0) {
            noItems.setVisibility(View.VISIBLE);
            visitedRv.setVisibility(View.GONE);
        }
        return view;
    }

    public void onBackPressed() {
        Intent intent = new Intent(baseActivity, GoalsActivities.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("nameActivity", "check_privous_order");
        startActivity(intent);
        baseActivity.overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

    }

    @Override
    public void productClick(View v, int position) {

    }
}
