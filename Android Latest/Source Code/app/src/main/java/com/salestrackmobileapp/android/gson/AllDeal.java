
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

public class AllDeal  extends SugarRecord implements Serializable {

    @SerializedName("DealID")
    @Expose
    private Integer dealID;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("DealType")
    @Expose
    private String dealType;
    @SerializedName("DealApplicableAs")
    @Expose
    private String dealApplicableAs;
    @SerializedName("DealDescription")
    @Expose
    private Object dealDescription;
    @SerializedName("Amount")
    @Expose
    private String amount;
    @SerializedName("BussinessID")
    @Expose
    private String bussinessID;
    @SerializedName("SalesPersonID")
    @Expose
    private Object salesPersonID;
    @SerializedName("ProductID")
    @Expose
    private String productID;
    @SerializedName("CouponCode")
    @Expose
    private Object couponCode;
    @SerializedName("productList")
    @Expose
    private List<ProductList> productList = null;
    @SerializedName("BussinessList")
    @Expose
    private Object bussinessList;
    @SerializedName("SalesPersonList")
    @Expose
    private Object salesPersonList;

    public Integer getDealID() {
        return dealID;
    }

    public void setDealID(Integer dealID) {
        this.dealID = dealID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDealApplicableAs() {
        return dealApplicableAs;
    }

    public void setDealApplicableAs(String dealApplicableAs) {
        this.dealApplicableAs = dealApplicableAs;
    }

    public Object getDealDescription() {
        return dealDescription;
    }

    public void setDealDescription(Object dealDescription) {
        this.dealDescription = dealDescription;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBussinessID() {
        return bussinessID;
    }

    public void setBussinessID(String bussinessID) {
        this.bussinessID = bussinessID;
    }

    public Object getSalesPersonID() {
        return salesPersonID;
    }

    public void setSalesPersonID(Object salesPersonID) {
        this.salesPersonID = salesPersonID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Object getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(Object couponCode) {
        this.couponCode = couponCode;
    }

    public List<ProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductList> productList) {
        this.productList = productList;
    }

    public Object getBussinessList() {
        return bussinessList;
    }

    public void setBussinessList(Object bussinessList) {
        this.bussinessList = bussinessList;
    }

    public Object getSalesPersonList() {
        return salesPersonList;
    }

    public void setSalesPersonList(Object salesPersonList) {
        this.salesPersonList = salesPersonList;
    }

}
