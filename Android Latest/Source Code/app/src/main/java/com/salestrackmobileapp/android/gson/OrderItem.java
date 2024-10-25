
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class OrderItem extends SugarRecord implements Serializable {

    @SerializedName("OrderItemID")
    @Expose
    private Integer orderItemID;
    @SerializedName("Quantity")
    @Expose
    private Double quantity;
    @SerializedName("Cost")
    @Expose
    private Double cost;
    @SerializedName("Discount")
    @Expose
    private Double discount;
    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("TaxAmount")
    @Expose
    private Double TaxAmount;


    @SerializedName("order")
    @Expose
    private Integer orderTb;
    @SerializedName("deal")
    @Expose
    private Integer deal;
    @SerializedName("ProductID")
    @Expose
    private Integer productID;
    @SerializedName("product")
    @Expose
    private Object product;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("TotalOrderValue")
    @Expose
    private Double TotalOrderValue;

    public Double getTotalOrderValue() {
        return TotalOrderValue;
    }

    public void setTotalOrderValue(Double totalOrderValue) {
        TotalOrderValue = totalOrderValue;
    }

    public Integer getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(Integer orderItemID) {
        this.orderItemID = orderItemID;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getOrder() {
        return orderTb;
    }

    public void setOrder(Integer orderTb) {
        this.orderTb = orderTb;
    }

    public Integer getDeal() {
        return deal;
    }

    public void setDeal(Integer deal) {
        this.deal = deal;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Object getProduct() {
        return product;
    }

    public void setProduct(Object product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        TaxAmount = taxAmount;
    }
}
