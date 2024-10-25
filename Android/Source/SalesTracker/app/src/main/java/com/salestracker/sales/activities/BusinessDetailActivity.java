package com.salestracker.sales.activities;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.salestracker.sales.R;
import com.salestracker.sales.adapters.GoalAdapter;
import com.salestracker.sales.adapters.HistoryOrderAdapter;
import com.salestracker.sales.modals.HistoryOrder;

import java.util.ArrayList;

public class BusinessDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<HistoryOrder> mItems;
    private HistoryOrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Business");

        mRecyclerView = (RecyclerView) findViewById(R.id.rvBusinessOrders);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(BusinessDetailActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mItems = new ArrayList<>();

        /*getBusinesses(url, true);*/

        mAdapter = new HistoryOrderAdapter(BusinessDetailActivity.this, mItems, true);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
