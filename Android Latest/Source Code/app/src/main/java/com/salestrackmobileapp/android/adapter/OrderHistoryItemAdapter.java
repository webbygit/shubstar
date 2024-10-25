package com.salestrackmobileapp.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orm.query.Condition;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllDeal;
import com.salestrackmobileapp.android.gson.AllProduct;
import com.salestrackmobileapp.android.gson.OrderItem;
import com.salestrackmobileapp.android.utils.RecyclerClick;

import java.util.List;


public class OrderHistoryItemAdapter extends RecyclerView.Adapter<OrderHistoryItemAdapter.ItemViewHolder> {

    Context ctxt;
    RecyclerClick rvClick;
    List<OrderItem> listItems;

    public OrderHistoryItemAdapter(Context ctxt, RecyclerClick rvClick) {
        this.ctxt = ctxt;
        this.rvClick = rvClick;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_list_item, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvClick.productClick(v, viewType);
            }
        });
        return viewHolder;
    }

    public void setItemArray(List<OrderItem> listItems) {
        this.listItems = listItems;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        AllDeal dealObj = Select.from(AllDeal.class).where(Condition.prop("deal_id").eq(listItems.get(position).getDeal())).first();
        AllProduct productObj = Select.from(AllProduct.class).where(Condition.prop("product_id").eq(listItems.get(position).getOrderItemID())).first();
        holder.itemTV.setText(listItems.get(position).getProductName() + "");
        holder.basePriceTv.setText(listItems.get(position).getCost() + "");
        if (listItems.get(position).getDiscount().equals(0.0) || listItems.get(position).getDiscount().equals("")) {
            //   holder.dsctTV.setText(listItems.get(position).getDiscount() + "Rs/-");
            holder.dsctTV.setVisibility(View.GONE);
            holder.dsct_tvtitle.setVisibility(View.GONE);
        } else {
            holder.dsctTV.setVisibility(View.VISIBLE);
            holder.dsct_tvtitle.setVisibility(View.VISIBLE);

            holder.dsctTV.setText(listItems.get(position).getDiscount() + "" + "Rs/-");
        }
        holder.qtyTV.setText(listItems.get(position).getQuantity() + "");

        if (listItems.get(position).getDiscount() != null) {
            holder.priceTv.setText(listItems.get(position).getCost() - listItems.get(position).getDiscount() + "");
            holder.subTotalTV.setText((Math.round(Double.parseDouble(String.valueOf(listItems.get(position).getCost() - listItems.get(position).getDiscount()))) * listItems.get(position).getQuantity()) + "");
        } else {
            holder.priceTv.setText(listItems.get(position).getCost() + "");
            holder.subTotalTV.setText((Math.round(Double.parseDouble(String.valueOf(listItems.get(position).getCost()))) * listItems.get(position).getQuantity()) + "");
        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        protected Custome_TextView itemTV, basePriceTv, priceTv, dsctTV, subTotalTV, qtyTV, dsct_tvtitle;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemTV = (Custome_TextView) itemView.findViewById(R.id.item_name_tv);
            basePriceTv = (Custome_TextView) itemView.findViewById(R.id.base_price_vl);
            dsctTV = (Custome_TextView) itemView.findViewById(R.id.dsct_vl);
            subTotalTV = (Custome_TextView) itemView.findViewById(R.id.sub_ttl_vl);
            priceTv = (Custome_TextView) itemView.findViewById(R.id.price_vl);
            qtyTV = (Custome_TextView) itemView.findViewById(R.id.qty_vl);
            dsct_tvtitle = (Custome_TextView) itemView.findViewById(R.id.dsct_tv);
        }
    }
}
