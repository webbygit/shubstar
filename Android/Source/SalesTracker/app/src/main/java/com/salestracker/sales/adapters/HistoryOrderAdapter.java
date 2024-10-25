package com.salestracker.sales.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salestracker.sales.R;
import com.salestracker.sales.modals.HistoryOrder;

import java.util.ArrayList;

/**
 * Created by Tarun Kumar
 */
public class HistoryOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;

    private ArrayList<HistoryOrder> mDataset;

    private final boolean hasHeader;
    private int TYPE_HEADER = 0;
    private int TYPE_ORDER = 1;

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private final View cardView;

        public OrderViewHolder(View v) {
            super(v);

            cardView = v.findViewById(R.id.card_view);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View v) {
            super(v);
        }
    }

    public void add(int position, HistoryOrder item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public HistoryOrderAdapter(Context context, ArrayList<HistoryOrder> myDataset, boolean hasHeader) {
        mContext = context;
        mDataset = myDataset;

        this.hasHeader = hasHeader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_details_header, parent, false);
            HeaderViewHolder vh = new HeaderViewHolder(v);
            return vh;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_list_item, parent, false);
            OrderViewHolder vh = new OrderViewHolder(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        // Transparent overlay code

        /*imageLoader.get(mDataset.get(position).getIcon_325_200(), new ImageLoader.ImageListener() {

            public void onErrorResponse(VolleyError error) {
                //imageView.setImageResource(R.drawable.icon_error); // set an error image if the download fails
            }

            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    //nivChallengeImage.setImageBitmap(response.getBitmap());

                    Palette.generateAsync(response.getBitmap(), new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            // Do something with colors...

                            //You can select any of the colors.
                            Palette.Swatch swatch = palette.getVibrantSwatch();
                            if (swatch != null) { // mind it, it can be null sometime.
                                int color = swatch.getRgb();
                                holder.viewTransparentOverlay.findViewById(R.id.viewTransparentOverlay).setBackgroundColor(color);
                            }
                        }
                    });

                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return /*mDataset.size()*/8 + (hasHeader ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && hasHeader) {
            return TYPE_HEADER;
        } else {
            return TYPE_ORDER;
        }
    }
}
