package com.salestracker.sales.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salestracker.sales.R;
import com.salestracker.sales.activities.BusinessDetailActivity;
import com.salestracker.sales.modals.BusinessEntry;

import java.util.ArrayList;

/**
 * Created by Tarun Kumar
 */
public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.ViewHolder> {
    private final Context mContext;
    private ArrayList<BusinessEntry> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final View viewCallAction;

        public ViewHolder(View v) {
            super(v);

            rootView = v.findViewById(R.id.rootView);
            viewCallAction = v.findViewById(R.id.viewCallAction);
        }
    }

    public void add(int position, BusinessEntry item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public BusinessAdapter(Context context, ArrayList<BusinessEntry> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    @Override
    public BusinessAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

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

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, BusinessDetailActivity.class));
            }
        });

        holder.viewCallAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9999999999"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mContext.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    } else
                        mContext.startActivity(intent);
                } else
                    mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return /*mDataset.size()*/8;
    }

}
