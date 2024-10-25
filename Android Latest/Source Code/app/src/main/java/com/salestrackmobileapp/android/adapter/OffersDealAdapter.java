package com.salestrackmobileapp.android.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.salestrackmobileapp.android.fragments.CartFragment;
import com.salestrackmobileapp.android.gson.AllDeal;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.RecyclerClick;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanchan on 8/6/2017.
 */

public class OffersDealAdapter extends RecyclerView.Adapter<OffersDealAdapter.ViewHolder> {
    RecyclerClick rvClick;
    Context context;
    List<AllDeal> allDeals = new ArrayList<AllDeal>();
    List<AllDeal> listOfDeals = new ArrayList<AllDeal>();
    int proId;
    static int i = 0;
    Custome_TextView mrpTextPagerAdapter;
    Custome_BoldTextView mrpAfterDiscount;
    List<ProductInCart> listProductInCart;

    ProductInCart productInCart;
    PrefsHelper prefsHelper;
    String selectedbusinessID = "";

    public OffersDealAdapter(Context context, Custome_TextView dicountTextView, Custome_BoldTextView mrpAfterDiscount, PrefsHelper prefsHelper, RecyclerView offersDeal, String selectedbusinessID) {
        this.context = context;
        mrpTextPagerAdapter = dicountTextView;
        this.mrpAfterDiscount = mrpAfterDiscount;
        this.prefsHelper = prefsHelper;
        this.selectedbusinessID = selectedbusinessID;
        Log.e("SelectedBusienssId", "::::" + selectedbusinessID);
        allDeals = Select.from(AllDeal.class).where(Condition.prop("deal_type").eq("Order")).list();
        listProductInCart = Select.from(ProductInCart.class).where(Condition.prop("deal_id").eq("")).list();

        Log.e("ALL DEALS", "::::" + allDeals.size());
//        if(allDeals== null || allDeals.size() == 0 ){
//            offersDeal.setVisibility(View.GONE);
//        }else {
//            offersDeal.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.deal_apply_for_prodescription, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AllDeal allDealItem = allDeals.get(position);
        Log.e("ALl_deals", ":::::" + allDeals.size());
        String businessID = allDealItem.getBussinessID();
        Log.e("###BusinessID", "::::" + businessID);
        String dateSt = allDealItem.getStartDate();
        String dateArray[] = dateSt.split("T");
        Log.e("DEAL_Amount", "::::" + allDealItem.getAmount());
        Log.e("DEAL_app", "::::::applicable as:::" + allDealItem.getDealApplicableAs());


        String dateEndSt = allDealItem.getEndDate();
        String endDateArray[] = dateEndSt.split("T");
        Log.e("DATE_St", "::::" + dateSt);
        Log.e("DATE_end", "::::" + dateEndSt);
        holder.endDateTv.setTypeface(holder.endDateTv.getTypeface(), Typeface.ITALIC);

        if (dateSt.equals(dateEndSt)) {
            holder.endDateTv.setText("Deal: valid for today only ");
            holder.startDateTv.setVisibility(View.GONE);
        } else {
            holder.startDateTv.setText("Deal starts" + dateArray[0] + "");
            holder.endDateTv.setText("Deal ends: " + endDateArray[0] + "");
        }
        if (allDealItem.getDealApplicableAs().equals("Percentage")) {
            if (allDealItem.getAmount() != null) {
                holder.mainTitle.setText(allDealItem.getAmount() + "% OFF ");
            } else {
                holder.mainTitle.setText("");
            }
        }
        if (allDealItem.getDealApplicableAs().equals("Amount")) {
            if (allDealItem.getAmount() != null) {
                holder.mainTitle.setText("₹" + " " + allDealItem.getAmount() + " OFF");
            } else {
                holder.mainTitle.setText("");
            }
        }
        productInCart = Select.from(ProductInCart.class).where(Condition.prop("deal_id").eq(allDealItem.getDealID())).first();

        if (productInCart != null) {
            //   holder.applyDealOnProduct.setBackgroundColor(context.getResources().getColor(R.color.fade_color));
            holder.applyDealOnProduct.setText("Applied");


        }
        holder.applyDealOnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listProductInCart.size() != 0) {
                    Toast.makeText(context, "You have already apply another deal..", Toast.LENGTH_SHORT).show();
                } else {
                    if (productInCart != null) {
                        Toast.makeText(context, "A deal is already applied ", Toast.LENGTH_SHORT).show();
                    } else {
                        //  holder.applyDealOnProduct.setBackgroundColor(context.getResources().getColor(R.color.fade_color));
                        holder.applyDealOnProduct.setText("Applied");
                        holder.applyDealOnProduct.setVisibility(View.GONE);
                        Toast.makeText(context, "Deal Applied successfully.", Toast.LENGTH_SHORT).show();


                        if (CartFragment.discount == 0.0) {
                            List<ProductInCart> productInCartList = ProductInCart.listAll(ProductInCart.class);
                            for (ProductInCart cartProduct : productInCartList) {
                                cartProduct.dealCategory = "Order";
                                cartProduct.dealId = String.valueOf(allDealItem.getDealID());
                                cartProduct.dealAmount = String.valueOf(allDealItem.getAmount());
                                cartProduct.dealType = String.valueOf(allDealItem.getDealApplicableAs());
                                cartProduct.save();
                            }

                            if (allDealItem.getDealApplicableAs().equals("Amount")) {
                                Toast.makeText(context, "you have applied " + allDealItem.getAmount(), Toast.LENGTH_SHORT).show();
                                CartFragment.discount = getFloatFromString(allDealItem.getAmount());

                                Double DTotalDiscount = Double.parseDouble(new DecimalFormat("##.##").format((CartFragment.discount)));
                                if (DTotalDiscount.equals(0.0)) {
                                    CartFragment.totalDiscountTv.setVisibility(View.GONE);
                                    CartFragment.total_discount_head.setVisibility(View.GONE);

                                } else {
                                    CartFragment.totalDiscountTv.setVisibility(View.VISIBLE);
                                    CartFragment.total_discount_head.setVisibility(View.VISIBLE);


                                }
                                Log.e("%%%", ":::cartD" + DTotalDiscount);

                                CartFragment.totalDiscountTv.setText("₹" + " " + DTotalDiscount);
                                Double amount = CartFragment.amount - CartFragment.discount;
                                Log.e("amount", ":::::" + amount);
                                Double DTotalAmount = Double.parseDouble(new DecimalFormat("##.##").format((amount + CartFragment.rowAmountTax)));
                                CartFragment.totalAmountTv.setText("₹" + " " + DTotalAmount);
                                notifyDataSetChanged();
                                notifyDataSetChanged();
                            } else {
                                Double sumAmt = CartFragment.amount;
                                Log.e("sumAmt", ":::::" + sumAmt);
                                Log.e("allDealItem.getAmount()", ":::::" + allDealItem.getAmount());


                                Double disAmount = ((getDoubleFromString(allDealItem.getAmount())) * sumAmt) / 100;
                                Log.e("disAmount", ":::::" + disAmount);
                                CartFragment.discount = disAmount;
                                Double DTotalDiscount = Double.parseDouble(new DecimalFormat("##.##").format((CartFragment.discount)));
                                if (DTotalDiscount.equals(0.0)) {
                                    CartFragment.totalDiscountTv.setVisibility(View.GONE);
                                    CartFragment.total_discount_head.setVisibility(View.GONE);

                                } else {
                                    CartFragment.totalDiscountTv.setVisibility(View.VISIBLE);
                                    CartFragment.total_discount_head.setVisibility(View.VISIBLE);


                                }
                                Log.e("%%%", ":::cartD" + DTotalDiscount);

                                CartFragment.totalDiscountTv.setText("₹" + " " + DTotalDiscount);
                                Double amount = CartFragment.amount - CartFragment.discount;
                                Log.e("amount", ":::::" + amount);
                                Double DTotalAmount = Double.parseDouble(new DecimalFormat("##.##").format((amount + CartFragment.rowAmountTax)));
                                CartFragment.totalAmountTv.setText("₹" + " " + DTotalAmount);
                                notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(context, "You have already apply deal on product...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        Log.e("DEAL_COUNT", ":::" + allDeals.size());

        return allDeals.size();
    }

    public static Double getDoubleFromString(String amount) {
        double totalPrice = Double.parseDouble(amount.replaceAll("[^0-9\\.]+", ""));
        return totalPrice;
    }

    public static Float getFloatFromString(String amount) {
        Float totalPrice = Float.parseFloat(amount.replaceAll("[^0-9\\.]+", ""));
        return totalPrice;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Custome_BoldTextView mainTitle;
        public Custome_TextView startDateTv, endDateTv;
        Button applyDealOnProduct, removeBtn;

        public ViewHolder(View v) {
            super(v);
            mainTitle = (Custome_BoldTextView) v.findViewById(R.id.main_title);
            applyDealOnProduct = (Button) v.findViewById(R.id.apply_deal_btn);
            startDateTv = (Custome_TextView) v.findViewById(R.id.start_date);
            endDateTv = (Custome_TextView) v.findViewById(R.id.end_date);
            removeBtn = (Button) v.findViewById(R.id.remove_deal_btn);

        }

    }
}
