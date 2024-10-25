package com.salestrackmobileapp.android.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllDeal;
import com.salestrackmobileapp.android.gson.ProductList;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.utils.RecyclerClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanchan on 5/31/2017.
 */

//deal_apply_for_prodescription
public class DealApplyAdapter extends RecyclerView.Adapter<DealApplyAdapter.ViewHolder> {

    RecyclerClick rvClick;
    Context context;
    List<AllDeal> allDeals = new ArrayList<AllDeal>();
    List<AllDeal> listOfDeals = new ArrayList<AllDeal>();
    int proId;
    static int i = 0;
    Custome_TextView mrpTextPagerAdapter, mrpAfterDiscount;

    public DealApplyAdapter(Context context, Integer productID, Custome_TextView mrpText, Custome_TextView mrpAfterDiscount) {
        this.context = context;
        proId = productID;
        mrpTextPagerAdapter = mrpText;
        this.mrpAfterDiscount = mrpAfterDiscount;
        if (proId != 0) {
            if (allDeals.size() != 0) {
                allDeals.clear();
            }
            List<ProductList> dealProduct = Select.from(ProductList.class).where(Condition.prop("product_id").eq(proId), Condition.prop("deal_type").eq("Product")).list();
            for (ProductList productObj : dealProduct) {
                allDeals.add(Select.from(AllDeal.class).where(Condition.prop("deal_id").eq(productObj.getDealID()), Condition.prop("deal_type").eq("Product")).first());
            }
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.deal_apply_for_prodescription, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (proId != 0) {
            if (allDeals.size() != 0) {
                allDeals.clear();
            }
            List<ProductList> dealProduct = Select.from(ProductList.class).where(Condition.prop("product_id").eq(proId), Condition.prop("deal_type").eq("Product")).list();
            for (ProductList productObj : dealProduct) {
                allDeals.add(Select.from(AllDeal.class).where(Condition.prop("deal_id").eq(productObj.getDealID()), Condition.prop("deal_type").eq("Product")).first());
            }
        }
        final AllDeal allDealItem = allDeals.get(position);
        if (allDealItem.getDealApplicableAs().equals("Percentage")) {
            if (allDealItem.getAmount() != null) {
                holder.mainTitle.setText(allDealItem.getAmount() + "% off ");
            } else {
                holder.mainTitle.setText("");
            }
        }
        if (allDealItem.getDealApplicableAs().equals("Amount")) {
            if (allDealItem.getAmount() != null) {
                holder.mainTitle.setText("Rs/-" + allDealItem.getAmount() + " OFF");
            } else {
                holder.mainTitle.setText("");
            }
        }

        String dateSt = allDealItem.getStartDate();
        String dateArray[] = dateSt.split("T");

        String dateEndSt = allDealItem.getStartDate();
        String endDateArray[] = dateEndSt.split("T");

        ProductInCart productInCart = Select.from(ProductInCart.class).where(Condition.prop("product_id").eq(proId)).first();
        if (productInCart != null) {
            if (productInCart.dealId.equals("0")) {
                mrpAfterDiscount.setVisibility(View.GONE);
            } else {
                Double amount = 0.0;
                if (productInCart.dealType.equals("Amount")) {
                    amount = Double.parseDouble(productInCart.dealAmount);
                } else {
                    amount = (Integer.parseInt(productInCart.dealAmount) * productInCart.price) / 100;
                }
                Double mrp = Double.parseDouble(mrpTextPagerAdapter.getText().toString());
                Double amountafterdicount = mrp - amount;
                mrpAfterDiscount.setVisibility(View.VISIBLE);
                mrpAfterDiscount.setText(amountafterdicount + "");
                mrpTextPagerAdapter.setPaintFlags(mrpTextPagerAdapter.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.applyDealOnProduct.setBackgroundColor(context.getResources().getColor(R.color.fade_color));
                holder.applyDealOnProduct.setText("Applied");
            }
        }

        if (dateSt.equals(dateEndSt)) {
            holder.startDateTv.setText("valid for today only ");
            holder.endDateTv.setVisibility(View.GONE);
        } else {
            holder.startDateTv.setText("start from" + dateArray[0] + "");
            holder.endDateTv.setText("till " + endDateArray[0] + "");
        }
        //  if (productInCart1.dealId.equals("") | productInCart1.dealId.equals(null)) {


        holder.applyDealOnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(context, "apply", Toast.LENGTH_SHORT).show();
                final ProductInCart productInCart = Select.from(ProductInCart.class).where(Condition.prop("product_id").eq(proId)).first();
                if (productInCart != null) {
                    if (productInCart.dealId.equals(null) || productInCart.dealId.equals("0")) {
                        if (productInCart != null) {
                            holder.applyDealOnProduct.setBackgroundColor(context.getResources().getColor(R.color.fade_color));
                            holder.applyDealOnProduct.setText("Applied");
                            productInCart.dealId = String.valueOf(allDealItem.getDealID() + "");
                            productInCart.dealCategory = "product";
                            productInCart.dealType = String.valueOf(allDealItem.getDealApplicableAs() + "");
                            productInCart.dealAmount = String.valueOf(allDealItem.getAmount() + "");
                            productInCart.save();
                            Double amount = 0.0;
                            if (productInCart.dealType.equals("Amount")) {
                                amount = Double.parseDouble(productInCart.dealAmount);
                            } else {
                                amount = (Integer.parseInt(productInCart.dealAmount) * productInCart.price) / 100;
                            }

                            Double mrp = Double.parseDouble(mrpTextPagerAdapter.getText().toString());
                            Double amountafterdicount = mrp - amount;
                            mrpAfterDiscount.setVisibility(View.VISIBLE);
                            mrpAfterDiscount.setText(amountafterdicount + "");
                            mrpTextPagerAdapter.setPaintFlags(mrpTextPagerAdapter.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                            notifyDataSetChanged();

                        } else {
                            Toast.makeText(context, "Please add item into cart..", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (productInCart.dealId.equals(String.valueOf(allDealItem.getDealID()))) {
                            Toast.makeText(context, "you have already this deal on product", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                            builder1.setTitle("You have already applied another deal.");
                            builder1.setMessage("Do you want to change deal value?");
                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override

                                public void onClick(DialogInterface dialog, int which) {
                                    productInCart.dealId = String.valueOf(allDealItem.getDealID() + "");
                                    productInCart.dealType = String.valueOf(allDealItem.getDealApplicableAs() + "");
                                    productInCart.dealAmount = String.valueOf(allDealItem.getAmount() + "");
                                    productInCart.dealCategory = "product";
                                    productInCart.save();
                                    notifyDataSetChanged();

                                    mrpTextPagerAdapter.setPaintFlags(mrpTextPagerAdapter.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                    Toast.makeText(context, "you have successfully applied change deals", Toast.LENGTH_SHORT).show();

                                }
                            });
                            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder1.show();
                        }
                    }
                } else {
                    Toast.makeText(context, "Please add product in cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allDeals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Custome_BoldTextView mainTitle;
        public Custome_TextView startDateTv, endDateTv;
        Button applyDealOnProduct;

        public ViewHolder(View v) {
            super(v);
            mainTitle = (Custome_BoldTextView) v.findViewById(R.id.main_title);
            applyDealOnProduct = (Button) v.findViewById(R.id.apply_deal_btn);
            startDateTv = (Custome_TextView) v.findViewById(R.id.start_date);
            endDateTv = (Custome_TextView) v.findViewById(R.id.end_date);

        }

    }
}



