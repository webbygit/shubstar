package com.salestrackmobileapp.android.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by UserPC on 12-Mar-18.
 */

public class ProductPriceList extends SugarRecord implements Serializable {
    @SerializedName("ProductPriceID")
    @Expose
    private long productpriceId;
    @SerializedName("ProductID")
    @Expose
    private int productId;

    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("StateID")
    @Expose
    private int stateID;
    @SerializedName("StateName")
    @Expose
    private String stateName;
    @SerializedName("CGSTTaxValue")
    @Expose
    private double cgstTaxValue;
    @SerializedName("SGSTTaxValue")
    @Expose
    private double sgstTaxValue;
    @SerializedName("Price")
    @Expose
    private double price;

    public long getProductpriceId() {
        return productpriceId;
    }

    public void setProductpriceId(long productpriceId) {
        this.productpriceId = productpriceId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStateID() {
        return stateID;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public double getCgstTaxValue() {
        return cgstTaxValue;
    }

    public void setCgstTaxValue(double cgstTaxValue) {
        this.cgstTaxValue = cgstTaxValue;
    }

    public double getSgstTaxValue() {
        return sgstTaxValue;
    }

    public void setSgstTaxValue(double sgstTaxValue) {
        this.sgstTaxValue = sgstTaxValue;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
