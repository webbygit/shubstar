package com.salestrackmobileapp.android.my_cart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by kanchan on 4/14/2017.
 */

public class ProductInCart extends SugarRecord {
    public Integer productID;
    public String productName;
    public int qty;
    public int CartQty;
    public Double price;
    public String brandId;
    public String productCategoryId;
    public String imageUrl;
    static public String businessID;
    public String dealId;
    public String dealAmount;
    public String dealType;
    public Integer uomID;
    public String uomST;
    public String uomDefault;
    public String dealCategory;
    public  Double cGSTPercentage;
    public Double sGSTPercentage;
    public Double baseQty;
    public Integer variantID;
    public Boolean allowPriceEdit;
    public  Double priceOriginal;


    //either product or order
}
