
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class PendingOrderItem extends SugarRecord implements Serializable {

    static public int orderNumber = 0;


    public String getOrderNumberID() {
        return orderNumberID;
    }

    public void setOrderNumberID(String orderNumberID) {
        this.orderNumberID = orderNumberID;
    }



    String orderNumberID;
    @SerializedName("OrderItemID")
    @Expose
    private Integer orderItemID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("Cost")
    @Expose
    private Integer cost;
    @SerializedName("Discount")
    @Expose
    private Integer discount;
    @SerializedName("Amount")
    @Expose
    private Integer amount;
    @SerializedName("deal")
    @Expose
    private Integer deal;

    @SerializedName("UOM")
    @Expose
    private String umoSt;

   /* public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @SerializedName("order")
    @Expose
    private int order;*/


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @SerializedName("ProductName")
    @Expose
    private String productName;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @SerializedName("ProductID")
    @Expose
    private int productId;

    public String getUmoSt() {
        return umoSt;
    }

    public void setUmoSt(String umoSt) {
        this.umoSt = umoSt;
    }

    public Integer getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(Integer orderItemID) {
        this.orderItemID = orderItemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

/*
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
*/

    public Integer getDeal() {
        return deal;
    }

    public void setDeal(Integer deal) {
        this.deal = deal;
    }

}
