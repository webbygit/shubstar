package com.salestracker.sales.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salestracker.sales.R;
import com.salestracker.sales.activities.BusinessDetailActivity;
import com.salestracker.sales.activities.ProductDetailActivity;
import com.salestracker.sales.modals.Goal;
import com.salestracker.sales.modals.Product;

import java.util.ArrayList;

/**
 * Created by Tarun Kumar
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<Product> mDataset;

    private int color1;
    private int color2;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;

        public ViewHolder(View v) {
            super(v);

            rootView = v.findViewById(R.id.rootView);
        }
    }

    public void add(int position, Product item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public ProductAdapter(Context context, ArrayList<Product> myDataset) {
        mContext = context;
        mDataset = myDataset;

        color1 = context.getResources().getColor(R.color.colorPrimaryLight);
        color2 = context.getResources().getColor(R.color.colorPrimaryLightExtra);
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item_new, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ProductDetailActivity.class));
            }
        });

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
        return /*mDataset.size()*/8;
    }

}
