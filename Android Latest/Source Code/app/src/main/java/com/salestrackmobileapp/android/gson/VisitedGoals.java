package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by kanchan on 7/26/2017.
 */

public class VisitedGoals extends SugarRecord implements Serializable {
    //
    @SerializedName("CheckedIn")
    @Expose
    boolean checkedin;
    @SerializedName("$id")
    @Expose
    private String defaultGoalbusinessID;
    @SerializedName("BusinessID")
    @Expose
    private Integer businessID;
    @SerializedName("BusnessName")
    @Expose
    private String busnessName;
    @SerializedName("Address1")
    @Expose
    private String address1;
    @SerializedName("Address2")
    @Expose
    private String address2;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("ZipCode")
    @Expose
    private String zipCode;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("WebsiteName")
    @Expose
    private String websiteName;
    @SerializedName("ContactPersonName")
    @Expose
    private String contactPersonName;
    @SerializedName("ContactPersonPhone")
    @Expose
    private String contactPersonPhone;
    @SerializedName("ContactPersonEmail")
    @Expose
    private String contactPersonEmail;
    @SerializedName("ImageName")
    @Expose
    private Object imageName;

    public boolean getCheckedIN() {
        return checkedin;
    }

    public void setCheckedIN(boolean checkedIN) {
        this.checkedin = checkedIN;
    }

    public String getDefaultGoalbusinessID() {
        return defaultGoalbusinessID;
    }

    public void setDefaultGoalbusinessID(String defaultGoalbusinessID) {
        this.defaultGoalbusinessID = defaultGoalbusinessID;
    }

    public Integer getBusinessID() {
        return businessID;
    }

    public void setBusinessID(Integer businessID) {
        this.businessID = businessID;
    }

    public String getBusnessName() {
        return busnessName;
    }

    public void setBusnessName(String busnessName) {
        this.busnessName = busnessName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }

    public void setContactPersonEmail(String contactPersonEmail) {
        this.contactPersonEmail = contactPersonEmail;
    }

    public Object getImageName() {
        return imageName;
    }

    public void setImageName(Object imageName) {
        this.imageName = imageName;
    }

}
