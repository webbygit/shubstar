package com.salestrackmobileapp.android.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.adapter.MyGoalsAdapter;

public class TermAndConditionActivity extends BaseActivity {


    String[] title = {"User Account,Password,Security", "Services Offered", "Privacy Policies", "User Conduct and Rules", "Exactness Not Guaranteed"};
    RecyclerView listRV;
    private MyGoalsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);
        // getActionBar().setHomeButtonEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("TERM OF SERVICES");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        // toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back_arrow));
        listRV = (RecyclerView) findViewById(R.id.list);

        mLayoutManager = new LinearLayoutManager(this);
        listRV.setLayoutManager(mLayoutManager);

        CustomAdapter adapter = new CustomAdapter(title);
        listRV.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

   /* @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }*/


    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

        String arr[];

        CustomAdapter(String arr[]) {
            this.arr = arr;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_term_and_condition, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.numberRow.setText(position + "");
            holder.titleTermTV.setText(arr[position]);
        }


        @Override
        public int getItemCount() {

            return arr.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView titleTermTV, numberRow;

            public ViewHolder(View v) {
                super(v);
                numberRow = (TextView) v.findViewById(R.id.number_row);
                titleTermTV = (TextView) v.findViewById(R.id.textView1);
            }
        }
    }


}
