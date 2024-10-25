
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

public class SaveOrder extends SugarRecord implements Serializable {

    static public int LOrderNumber = 0;

    public Integer getLOrderNumber() {
        return LOrderNumber;
    }

    public void setLOrderNumber(Integer LOrderNumber) {
        this.LOrderNumber = LOrderNumber;
    }

  /*  @SerializedName("LOrderNumber")
    @Expose
    Integer LOrderNumber;*/

    @SerializedName("OrderItems")
    @Expose
    private List<PendingOrderItem> pendingOrderItems;
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
    private Double subTotal;
    @SerializedName("TaxPercentage")
    @Expose
    private Integer taxPercentage;
    @SerializedName("TaxAmount")
    @Expose
    private Double taxAmount;
    @SerializedName("DeliveryAmount")
    @Expose
    private Integer deliveryAmount;
    @SerializedName("DiscountAmount")
    @Expose
    private Double discountAmount;
    @SerializedName("TotalOrderValue")
    @Expose
    private Double totalOrderValue;
    @SerializedName("dealID")
    @Expose
    private Integer dealID;


    @SerializedName("Business")
    @Expose
    private Business business;



    @SerializedName("BusinessName")
    @Expose
    private String businessName;

    public String getSendToServer() {
        return sendToServer;
    }

    public void setSendToServer(String sendToServer) {
        this.sendToServer = sendToServer;
    }

    @SerializedName("sentToServer")
    @Expose
    String sendToServer;
    public  int orderPositions;

    public int getOrderPositions() {
        return orderPositions;
    }

    public void setOrderPositions(int orderPositions) {
        this.orderPositions = orderPositions;
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


    public Integer getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Integer taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public Integer getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(Integer deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public static void setLOrderNumber(int LOrderNumber) {
        SaveOrder.LOrderNumber = LOrderNumber;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getTotalOrderValue() {
        return totalOrderValue;
    }

    public void setTotalOrderValue(Double totalOrderValue) {
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
    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

}
