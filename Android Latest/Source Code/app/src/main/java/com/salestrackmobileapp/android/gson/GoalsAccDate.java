
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

public class GoalsAccDate extends SugarRecord implements Serializable {

    @SerializedName("$id")
    @Expose
    private String defaultId;
    @SerializedName("DailyGoalID")
    @Expose
    private Integer dailyGoalID;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("GoalTitle")
    @Expose
    private String goalTitle;
    @SerializedName("GoalDetails")
    @Expose
    private Object goalDetails;
    @SerializedName("GoalNote")
    @Expose
    private Object goalNote;
    @SerializedName("Quantity")
    @Expose
    private Integer quantityID;
    @SerializedName("Amount")
    @Expose
    private Integer amountID;
    @SerializedName("ProductID")
    @Expose
    private Integer productID;
    @SerializedName("product")
    @Expose
    private Object product;
    @SerializedName("SalesPersonId")
    @Expose
    private Integer salesPersonId;
    @SerializedName("salesPerson")
    @Expose
    private SalesPerson salesPerson;
    @SerializedName("BusinessId")
    @Expose
    private String businessId;
    @SerializedName("business")
    @Expose
    private Business business;
    @SerializedName("BusinessMapping")
    @Expose
    private List<BusinessMapping> businessMapping;

    public String getDefaultId() {
        return defaultId;
    }

    public void setDefaultId(String defaultId) {
        this.defaultId = defaultId;
    }

    public Integer getDailyGoalID() {
        return dailyGoalID;
    }

    public void setDailyGoalID(Integer dailyGoalID) {
        this.dailyGoalID = dailyGoalID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGoalTitle() {
        return goalTitle;
    }

    public void setGoalTitle(String goalTitle) {
        this.goalTitle = goalTitle;
    }

    public Object getGoalDetails() {
        return goalDetails;
    }

    public void setGoalDetails(Object goalDetails) {
        this.goalDetails = goalDetails;
    }

    public Object getGoalNote() {
        return goalNote;
    }

    public void setGoalNote(Object goalNote) {
        this.goalNote = goalNote;
    }

    public Integer getQuantityID() {
        return quantityID;
    }

    public void setQuantityID(Integer quantityID) {
        this.quantityID = quantityID;
    }

    public Integer getAmountID() {
        return amountID;
    }

    public void setAmountID(Integer amountID) {
        this.amountID = amountID;
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

    public Integer getSalesPersonId() {
        return salesPersonId;
    }

    public void setSalesPersonId(Integer salesPersonId) {
        this.salesPersonId = salesPersonId;
    }

    public SalesPerson getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(SalesPerson salesPerson) {
        this.salesPerson = salesPerson;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public List<BusinessMapping> getBusinessMapping() {
        return businessMapping;
    }

    public void setBusinessMapping(List<BusinessMapping> businessMapping) {
        this.businessMapping = businessMapping;
    }

}
