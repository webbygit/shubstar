
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class CheckIn  extends SugarRecord implements Serializable {

    @SerializedName("SalesImage")
    @Expose
    private String salesImage;
    @SerializedName("SalesPersonName")
    @Expose
    private String salesPersonName;
    @SerializedName("UserCheckInId")
    @Expose
    private Integer userCheckInId;
    @SerializedName("CheckInDate")
    @Expose
    private String checkInDate;
    @SerializedName("SalesPerson")
    @Expose
    private SalesPerson salesPerson;
    @SerializedName("UserProfile")
    @Expose
    private UserInfoProfile userProfile;
    @SerializedName("Business")
    @Expose
    private GoalBusiness business;
    @SerializedName("BusinessName")
    @Expose
    private String businessName;
    @SerializedName("SalesPersonID")
    @Expose
    private Integer salesPersonID;

    public String getSalesImage() {
        return salesImage;
    }

    public void setSalesImage(String salesImage) {
        this.salesImage = salesImage;
    }

    public String getSalesPersonName() {
        return salesPersonName;
    }

    public void setSalesPersonName(String salesPersonName) {
        this.salesPersonName = salesPersonName;
    }

    public Integer getUserCheckInId() {
        return userCheckInId;
    }

    public void setUserCheckInId(Integer userCheckInId) {
        this.userCheckInId = userCheckInId;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public SalesPerson getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(SalesPerson salesPerson) {
        this.salesPerson = salesPerson;
    }

    public UserInfoProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserInfoProfile userProfile) {
        this.userProfile = userProfile;
    }

    public GoalBusiness getBusiness() {
        return business;
    }

    public void setBusiness(GoalBusiness business) {
        this.business = business;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getSalesPersonID() {
        return salesPersonID;
    }

    public void setSalesPersonID(Integer salesPersonID) {
        this.salesPersonID = salesPersonID;
    }

}
