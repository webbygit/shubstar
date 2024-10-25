package com.salestrackmobileapp.android.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orm.query.Condition;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllProduct;
import com.salestrackmobileapp.android.gson.PendingOrderItem;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kanchan on 6/21/2017.
 */

public class SumProductAdapter extends RecyclerView.Adapter<SumProductAdapter.ViewHolder> {

    List<PendingOrderItem> saveOrderList;
    RecyclerView recyclerView;
    Context context;
    PrefsHelper prefsHelper;

    public SumProductAdapter(Context context, List<PendingOrderItem> saveOrderList, PrefsHelper sharedpreference) {
        this.context = context;
        this.saveOrderList = saveOrderList;
        prefsHelper = sharedpreference;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_sum_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PendingOrderItem pendingOrderItem = saveOrderList.get(position);

        Integer sumAmount;
        if (Select.from(AllProduct.class).where(Condition.prop("product_id").eq(pendingOrderItem.getOrderItemID())).first() != null) {
            AllProduct productObj = Select.from(AllProduct.class).where(Condition.prop("product_id").eq(pendingOrderItem.getOrderItemID())).first();
            holder.productNameTv.setText(productObj.getProductName() + "");
            Picasso.with(context).load(productObj.getImageName()).placeholder(context.getResources().getDrawable(R.drawable.calendar_icon)).error(R.drawable.calendar_icon).into(holder.productImg);
            holder.qtyTv.setText("Total Quantity: " + pendingOrderItem.getQuantity() + "");

            if (pendingOrderItem.getDiscount() != null) {

                holder.discount.setText("Discount:" + pendingOrderItem.getDiscount() + "");

                holder.mrpTv.setPaintFlags(holder.mrpTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.discount.setVisibility(View.VISIBLE);
                holder.actualAmt.setVisibility(View.VISIBLE);
                sumAmount = pendingOrderItem.getQuantity() * pendingOrderItem.getCost() + pendingOrderItem.getQuantity() * pendingOrderItem.getDiscount();
                holder.actualAmt.setText("amount after discount " + sumAmount);
            } else {
                sumAmount = pendingOrderItem.getCost();
                holder.discount.setVisibility(View.GONE);
            }
            if (pendingOrderItem.getDiscount() != null) {
                if (pendingOrderItem.getDiscount().equals("") || pendingOrderItem.getDiscount().equals("0")) {
                    holder.discount.setVisibility(View.GONE);
                }
            } else {
                holder.discount.setVisibility(View.GONE);
            }

            holder.mrpTv.setText("Rs. " + pendingOrderItem.getCost() * pendingOrderItem.getQuantity() + "");
        }


    }

    @Override
    public int getItemCount() {
        return saveOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Custome_TextView productNameTv, mrpTv, qtyTv, discount;
        Custome_BoldTextView actualAmt;
        public ImageView productImg;

        public ViewHolder(View v) {
            super(v);
            productNameTv = (Custome_TextView) v.findViewById(R.id.product_title_tv);
            actualAmt = (Custome_BoldTextView) v.findViewById(R.id.actualAmount);
            mrpTv = (Custome_TextView) v.findViewById(R.id.mrp_product_tv);
            qtyTv = (Custome_TextView) v.findViewById(R.id.category_tv);
            productImg = (ImageView) v.findViewById(R.id.product_item_img);
            discount = (Custome_TextView) v.findViewById(R.id.discount);
        }

    }
}

