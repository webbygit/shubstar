
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

public class PrevOrder extends SugarRecord implements Serializable {

    @SerializedName("$id")
    @Expose
    private String preOrderDefaultId;
    @SerializedName("OrderItems")
    @Expose
    private List<PendingOrderItem> pendingOrderItems = null;
    @SerializedName("OrderID")
    @Expose
    private Integer orderID;
    @SerializedName("OrderDate")
    @Expose
    private String orderDate;
    @SerializedName("BusinessID")
    @Expose
    private Integer businessID;
    @SerializedName("OrderNo")
    @Expose
    private String orderNo;
    @SerializedName("SubTotal")
    @Expose
    private Integer subTotal;
    @SerializedName("TaxPercentage")
    @Expose
    private Integer taxPercentage;
    @SerializedName("TaxAmount")
    @Expose
    private Integer taxAmount;
    @SerializedName("DeliveryAmount")
    @Expose
    private Integer deliveryAmount;
    @SerializedName("DiscountAmount")
    @Expose
    private Integer discountAmount;
    @SerializedName("TotalOrderValue")
    @Expose
    private Integer totalOrderValue;
    @SerializedName("dealID")
    @Expose
    private Integer dealID;
    @SerializedName("Business")
    @Expose
    private Business business=new Business();

    public String getPreOrderDefaultId() {
        return preOrderDefaultId;
    }

    public void setPreOrderDefaultId(String preOrderDefaultId) {
        this.preOrderDefaultId = preOrderDefaultId;
    }

    public List<PendingOrderItem> getPendingOrderItems() {
        return pendingOrderItems;
    }

    public void setPendingOrderItems(List<PendingOrderItem> pendingOrderItems) {
        this.pendingOrderItems = pendingOrderItems;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getBusinessID() {
        return businessID;
    }

    public void setBusinessID(Integer businessID) {
        this.businessID = businessID;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Integer subTotal) {
        this.subTotal = subTotal;
    }

    public Integer getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Integer taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public Integer getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Integer taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Integer getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(Integer deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getTotalOrderValue() {
        return totalOrderValue;
    }

    public void setTotalOrderValue(Integer totalOrderValue) {
        this.totalOrderValue = totalOrderValue;
    }

    public Integer getDealID() {
        return dealID;
    }

    public void setDealID(Integer dealID) {
        this.dealID = dealID;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

}
