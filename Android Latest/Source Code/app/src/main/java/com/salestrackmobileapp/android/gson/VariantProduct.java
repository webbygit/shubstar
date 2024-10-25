package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by UserPC on 06-Mar-18.
 */

public class VariantProduct extends SugarRecord implements Serializable {
    @SerializedName("ProductID")
    @Expose
    private long productID;
    @SerializedName("ProductTitle")
    @Expose
    private String  productTitle;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("Description")
    @Expose
    private String  description;
    @SerializedName("Keywords")
    @Expose
    private String keywords;
    @SerializedName("Width")
    @Expose
    private String Width;
    @SerializedName("Height")
    @Expose
    private String height;
    @SerializedName("DimUnit")
    @Expose
    private String dimUnit;
    @SerializedName("Weight")
    @Expose
    private String weight;
    @SerializedName("WeightUnit")
    @Expose
    private String weightUnit;
    @SerializedName("UPCCode")
    @Expose
    private String upcCode;
    @SerializedName("ISBNNumber")
    @Expose
    private String isbnNumber;
    @SerializedName("DefaultPrice")
    @Expose
    private Double defaultPrice;

    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("CompanyCode")
    @Expose
    private String companyCode;
    @SerializedName("ImageName")
    @Expose
    private String imageName;
    @SerializedName("MultipleVariants")
    @Expose
    private Boolean multipleVariants;
    @SerializedName("SalesPrice")
    @Expose
    private Double salesPrice;
    @SerializedName("CGST")
    @Expose
    private Double CGST;
    @SerializedName("SGST")
    @Expose
    private Double SGST;

    @SerializedName("UOMMapping")
    @Expose
    private String UOMMapping;
    @SerializedName("DefaultUOMID")
    @Expose
    private long DefaultUOMID;
    @SerializedName("AllowPriceEdit")
    @Expose
    private Boolean AllowPriceEdit;
    @SerializedName("PurchasePrice")
    @Expose
    private Double PurchasePrice;
    @SerializedName("CreatedBy")
    @Expose
    private Integer CreatedBy;
    @SerializedName("CreatedDate")
    @Expose
    private String CreatedDate;
    @SerializedName("ModifiedBy")
    @Expose
    private Integer ModifiedBy;
    @SerializedName("ModifiedDate")
    @Expose
    private String ModifiedDate;

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getWidth() {
        return Width;
    }

    public void setWidth(String width) {
        Width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDimUnit() {
        return dimUnit;
    }

    public void setDimUnit(String dimUnit) {
        this.dimUnit = dimUnit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getUpcCode() {
        return upcCode;
    }

    public void setUpcCode(String upcCode) {
        this.upcCode = upcCode;
    }

    public String getIsbnNumber() {
        return isbnNumber;
    }

    public void setIsbnNumber(String isbnNumber) {
        this.isbnNumber = isbnNumber;
    }

    public Double getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(Double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Boolean getMultipleVariants() {
        return multipleVariants;
    }

    public void setMultipleVariants(Boolean multipleVariants) {
        this.multipleVariants = multipleVariants;
    }

    public Double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Double getCGST() {
        return CGST;
    }

    public void setCGST(Double CGST) {
        this.CGST = CGST;
    }

    public Double getSGST() {
        return SGST;
    }

    public void setSGST(Double SGST) {
        this.SGST = SGST;
    }

    public String getUOMMapping() {
        return UOMMapping;
    }

    public void setUOMMapping(String UOMMapping) {
        this.UOMMapping = UOMMapping;
    }

    public long getDefaultUOMID() {
        return DefaultUOMID;
    }

    public void setDefaultUOMID(long defaultUOMID) {
        DefaultUOMID = defaultUOMID;
    }

    public Boolean getAllowPriceEdit() {
        return AllowPriceEdit;
    }

    public void setAllowPriceEdit(Boolean allowPriceEdit) {
        AllowPriceEdit = allowPriceEdit;
    }

    public Double getPurchasePrice() {
        return PurchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        PurchasePrice = purchasePrice;
    }

    public Integer getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(Integer createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public Integer getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return ModifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        ModifiedDate = modifiedDate;
    }

    public long getOrgId() {
        return OrgId;
    }

    public void setOrgId(long orgId) {
        OrgId = orgId;
    }

    @SerializedName("OrgId")
    @Expose
    private long OrgId;
























}
