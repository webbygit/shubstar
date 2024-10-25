
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class Business extends SugarRecord implements Serializable {


    @SerializedName("$id")
    @Expose
    private String businessDefaultId;
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
    private Object address2;
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
    @SerializedName("CompanyCode")
    @Expose
    private String companyCode;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("ImageName")
    @Expose
    private String imageName;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("ModifiedBy")
    @Expose
    private Object modifiedBy;
    @SerializedName("ModifiedDate")
    @Expose
    private Object modifiedDate;
    @SerializedName("OrgId")
    @Expose
    private Integer orgId;
    @SerializedName("$ref")
    @Expose
    private String ref;
    @SerializedName("BusinessType")
     @Expose
    private  String BusinessType;

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public String getBusinessDefaultId() {
        return businessDefaultId;
    }

    public void setBusinessDefaultId(String businessDefaultId) {
        this.businessDefaultId = businessDefaultId;
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

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Object modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Object getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Object modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

}
