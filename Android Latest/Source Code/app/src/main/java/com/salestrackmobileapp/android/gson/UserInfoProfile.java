
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class UserInfoProfile  extends SugarRecord implements Serializable {

    @SerializedName("$id")
    @Expose
    private String defaultId;
    @SerializedName("UserProfileID")
    @Expose
    private Integer userProfileID;
    @SerializedName("EmailID")
    @Expose
    private String emailID;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("AdminUserName")
    @Expose
    private Object adminUserName;
    @SerializedName("Password")
    @Expose
    private Object password;
    @SerializedName("companyName")
    @Expose
    private Object companyName;
    @SerializedName("Address1")
    @Expose
    private String address1;
    @SerializedName("Address2")
    @Expose
    private Object address2;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("Zip")
    @Expose
    private String zip;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("Latitude")
    @Expose
    private Object latitude;
    @SerializedName("Longitude")
    @Expose
    private Object longitude;
    @SerializedName("ContactPhoneNumber")
    @Expose
    private Object contactPhoneNumber;
    @SerializedName("IsActive")
    @Expose
    private Object isActive;
    @SerializedName("CompanyCode")
    @Expose
    private Object companyCode;
    @SerializedName("UserRole")
    @Expose
    private Object userRole;
    @SerializedName("MobileNo")
    @Expose
    private Object mobilePhoneNumber;

    public Object getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(Object mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    //Image
    @SerializedName("Image")
    @Expose
    private String imageUrl;

    public String getDefaultId() {
        return defaultId;
    }

    public void setDefaultId(String defaultId) {
        this.defaultId = defaultId;
    }

    public Integer getUserProfileID() {
        return userProfileID;
    }

    public void setUserProfileID(Integer userProfileID) {
        this.userProfileID = userProfileID;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Object getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(Object adminUserName) {
        this.adminUserName = adminUserName;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public Object getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Object companyName) {
        this.companyName = companyName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public Object getAddress2() {
        return address2;
    }

    public void setAddress2(Object address2) {
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public Object getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(Object contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public Object getIsActive() {
        return isActive;
    }

    public void setIsActive(Object isActive) {
        this.isActive = isActive;
    }

    public Object getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Object companyCode) {
        this.companyCode = companyCode;
    }

    public Object getUserRole() {
        return userRole;
    }

    public void setUserRole(Object userRole) {
        this.userRole = userRole;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
