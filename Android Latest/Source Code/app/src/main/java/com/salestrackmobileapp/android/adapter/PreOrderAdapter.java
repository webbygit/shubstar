package com.salestrackmobileapp.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.utils.RecyclerClick;

/**
 * Created by kanchan on 4/19/2017.
 */

public class PreOrderAdapter extends RecyclerView.Adapter<PreOrderAdapter.ViewHolder> {

    RecyclerClick rvClick;
    Context context;

    public PreOrderAdapter(Context context, RecyclerClick rvClick) {
        this.context = context;
        this.rvClick = rvClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.text_with_listview, parent, false);
        // set the view's size, margins, paddings and layout parameters
        PreOrderAdapter.ViewHolder vh = new PreOrderAdapter.ViewHolder(v, rvClick);
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


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public Custome_TextView dateOrderTv;
        public ListView listOrderView;
        RecyclerClick recyclerClick;

        public ViewHolder(View v, RecyclerClick recyclerClick) {
            super(v);
            dateOrderTv = (Custome_TextView) v.findViewById(R.id.date_order_tv);
            listOrderView = (ListView) v.findViewById(R.id.order_item_list);
            this.recyclerClick = recyclerClick;
        }

        @Override
        public void onClick(View v) {
            recyclerClick.productClick(v, getLayoutPosition());
        }
    }
}
