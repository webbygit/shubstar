package com.salestrackmobileapp.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.adapter.DealsProductAdapter;
import com.salestrackmobileapp.android.gson.ProductList;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.RecyclerClick;

import java.util.List;


public class ListOfDealsProductFragment extends BaseFragment implements RecyclerClick {


    RecyclerView dealsProductListRV;

    Integer dealId;
    DealsProductAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    List<ProductList> productList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_of_deals_product, container, false);
        dealsProductListRV = (RecyclerView) view.findViewById(R.id.deals_product_list);

        if (getArguments() != null) {
            dealId = getArguments().getInt("dealId");
            sharedPreference.saveIntData(PrefsHelper.DEAL, dealId);
        } else {
            if (dealId == null) {
                dealId = sharedPreference.getIntValue(PrefsHelper.DEAL);
            }
        }
        productList = Select.from(ProductList.class).where(Condition.prop("deal_id").eq(dealId)).list();
        //can be removed
        //  sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID,null);
        mAdapter = new DealsProductAdapter(baseActivity, this);
        mAdapter.setListDealProduct(productList);
        mLayoutManager = new LinearLayoutManager(baseActivity);
        dealsProductListRV.setLayoutManager(mLayoutManager);
        dealsProductListRV.setAdapter(mAdapter);
        return view;
    }


    @Override
    public void productClick(View v, int position) {

        int itemPosition = dealsProductListRV.getChildPosition(v);
        ProductList productListObj = productList.get(itemPosition);
       /* Intent intent = new Intent(getActivity(), GoalsActivities.class);
        intent.putExtra("product_location", itemPosition);
        intent.putExtra("product_id", productListObj.getProductID());
        intent.putExtra("brand_name", productListObj.getBrandName() + "");
        if (productListObj.getProductCategoryID() != null) {
            intent.putExtra("category_id", Integer.parseInt(productListObj.getProductCategoryID()));
        } else {
            intent.putExtra("category_id", 0);
        }
        intent.putExtra("dealID", dealId);
        intent.putExtra("listofdeals", "dealfragment");
        intent.putExtra("nameActivity", "ProductDetail");
        startActivity(intent);*/

        DealProductDetailFragment fragment = new DealProductDetailFragment();
        Bundle args = new Bundle();
       /* args.putInt("product_location", itemPosition);
        args.putInt("product_id", productListObj.getProductID());
        args.putString("brand_name", productListObj.getBrandName() + "");
        if (productListObj.getProductCategoryID() != null) {
            args.putInt("category_id", Integer.parseInt(productListObj.getProductCategoryID()));
        } else {
            args.putInt("category_id", 0);
        }
        args.putInt("dealID", dealId);
        args.putString("listofdeals", "dealfragment");*/
        args.putSerializable("productObj", productListObj);
        fragment.setArguments(args);
        baseActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void onBackPressed() {
        Intent intent = new Intent(baseActivity, GoalsActivities.class);
        intent.putExtra("nameActivity", "AllDeals");
        startActivity(intent);
        baseActivity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
