
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class AllBusiness extends SugarRecord implements Serializable {

    Boolean checkedIN = false;

    @SerializedName("$id")
    @Expose
    private String default_id;
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
    private String imageName;
    @SerializedName("BusinessType")
    @Expose
    private String businesstype;

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public Boolean getCheckedIN() {
        return checkedIN;
    }

    public void setCheckedIN(Boolean checkedIN) {
        this.checkedIN = checkedIN;
    }

    public String getDefault_id() {
        return default_id;
    }

    public void setDefault_id(String default_id) {
        this.default_id = default_id;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

}