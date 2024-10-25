package com.salestracker.sales.listeners;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Rohit Arya on 12/4/15.
 * B.Tech, IIT Delhi
 * http://rohitarya.com
 */
public abstract class EndlessRecyclerGridOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerGridOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 3; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private GridLayoutManager mLinearLayoutManager;

    public EndlessRecyclerGridOnScrollListener(GridLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        /*System.out.println("visibleItemCount : " + visibleItemCount);
        System.out.println("totalItemCount : " + totalItemCount);
        System.out.println("firstVisibleItem : " + firstVisibleItem);
        System.out.println("previousTotal : " + previousTotal);

        System.out.println("loading : " + loading);*/
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            current_page++;

            onLoadMore(current_page);

            loading = true;
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
    }

    public abstract void onLoadMore(int current_page);
}
