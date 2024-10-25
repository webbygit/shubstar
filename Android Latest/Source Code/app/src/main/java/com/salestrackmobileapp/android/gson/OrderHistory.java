
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

public class OrderHistory  extends SugarRecord implements Serializable {

    @SerializedName("OrderItems")
    @Expose
    private List<OrderItem> pendingOrderItems = null;
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
    private Double  subTotal;
    @SerializedName("TaxPercentage")
    @Expose
    private Double taxPercentage;
    @SerializedName("TaxAmount")
    @Expose
    private Double taxAmount;
    @SerializedName("DeliveryAmount")
    @Expose
    private Integer deliveryAmount;
    @SerializedName("DiscountAmount")
    @Expose
    private Double  discountAmount;

    public Double getTotalOrderValue() {
        return totalOrderValue;
    }

    public void setTotalOrderValue(Double totalOrderValue) {
        this.totalOrderValue = totalOrderValue;
    }

    @SerializedName("TotalOrderValue")
    @Expose
    private Double totalOrderValue;
    @SerializedName("dealID")
    @Expose
    private Integer dealID;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("BusinessName")
    @Expose
    private String businessName;
    @SerializedName("BusinessAddress1")
    @Expose
    private String businessAddress1;
    @SerializedName("BusinessAddress2")
    @Expose
    private String businessAddress2;
    @SerializedName("BusinessCity")
    @Expose
    private String businessCity;
    @SerializedName("BusinessState")
    @Expose
    private String businessState;


    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @SerializedName("SalesPersonName")

    @Expose
    private String salesPersonName;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("SalesPersonID")
    @Expose
    private Integer salesPersonID;
    @SerializedName("Business")
    @Expose
    private Business business;
    @SerializedName("slProductCategory")
    @Expose
    private String slProductCategory ;
    @SerializedName("slProduct")
    @Expose
    private String slProduct = null;
    @SerializedName("ProductList")
    @Expose
    private String productList = null;
    private Integer orderPositions=0;

    public Integer getOrderPositions() {
        return orderPositions;
    }

    public void setOrderPositions(Integer orderPositions) {
        this.orderPositions = orderPositions;
    }

    public List<OrderItem> getPendingOrderItems() {
        return pendingOrderItems;
    }

    public void setPendingOrderItems(List<OrderItem> pendingOrderItems) {
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


    public Double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Integer getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(Integer deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }





    public Integer getDealID() {
        return dealID;
    }

    public void setDealID(Integer dealID) {
        this.dealID = dealID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessAddress1() {
        return businessAddress1;
    }

    public void setBusinessAddress1(String businessAddress1) {
        this.businessAddress1 = businessAddress1;
    }

    public String getBusinessAddress2() {
        return businessAddress2;
    }

    public void setBusinessAddress2(String businessAddress2) {
        this.businessAddress2 = businessAddress2;
    }

    public String getBusinessCity() {
        return businessCity;
    }

    public void setBusinessCity(String businessCity) {
        this.businessCity = businessCity;
    }

    public String getBusinessState() {
        return businessState;
    }

    public void setBusinessState(String businessState) {
        this.businessState = businessState;
    }

    public String getSalesPersonName() {
        return salesPersonName;
    }

    public void setSalesPersonName(String salesPersonName) {
        this.salesPersonName = salesPersonName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSalesPersonID() {
        return salesPersonID;
    }

    public void setSalesPersonID(Integer salesPersonID) {
        this.salesPersonID = salesPersonID;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public String getSlProductCategory() {
        return slProductCategory;
    }

    public void setSlProductCategory(String slProductCategory) {
        this.slProductCategory = slProductCategory;
    }

    public String getSlProduct() {
        return slProduct;
    }

    public void setSlProduct(String slProduct) {
        this.slProduct = slProduct;
    }

    public String getProductList() {
        return productList;
    }

    public void setProductList(String productList) {
        this.productList = productList;
    }

}
