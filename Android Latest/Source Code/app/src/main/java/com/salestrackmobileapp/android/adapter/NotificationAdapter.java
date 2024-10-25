package com.salestrackmobileapp.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllNotification;
import com.salestrackmobileapp.android.utils.RecyclerClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanchan on 7/26/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    RecyclerClick rvClick;
    List<AllNotification> allNotificationList, filterList;
    Context context;

    public NotificationAdapter(Context context, RecyclerClick rvClick) {
        this.context = context;
        this.rvClick = rvClick;
    }

    public void setAllNotification() {
        allNotificationList = AllNotification.listAll(AllNotification.class);
        this.filterList = new ArrayList<AllNotification>();
        this.filterList.addAll(this.allNotificationList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.deal_item, parent, false);
        NotificationAdapter.ViewHolder vh = new NotificationAdapter.ViewHolder(view, rvClick);
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
        AllNotification allNotification = filterList.get(position);
        holder.mainGoalTitle.setText(" " + allNotification.getContentTitle() + "");
        holder.subGoalTitle.setText(allNotification.getContentBody() + "");
        holder.startDateTv.setVisibility(View.VISIBLE);
        holder.startDateTv.setText(allNotification.getSendDateTime() + " ");
        holder.endDateTv.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
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

    // Do Search...
    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    filterList.addAll(allNotificationList);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (AllNotification item : allNotificationList) {
                        if (item.getContentTitle().toLowerCase().contains(text.toLowerCase())) {
                            // Adding Matched items
                            filterList.add(item);
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
