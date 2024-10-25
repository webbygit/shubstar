
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class ProductList  extends SugarRecord implements Serializable {

    @SerializedName("ProductID")
    @Expose
    private Integer productID;
    @SerializedName("ProductTitle")
    @Expose
    private String productTitle;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ProductCategoryID")
    @Expose
    private String productCategoryID;
    @SerializedName("BrandID")
    @Expose
    private Integer brandID;
    @SerializedName("BrandName")
    @Expose
    private String brandName;
    @SerializedName("ProductCategoryName")
    @Expose
    private Object productCategoryName;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Keywords")
    @Expose
    private String keywords;
    @SerializedName("Width")
    @Expose
    private String width;
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
    private String uPCCode;
    @SerializedName("ISBNNumber")
    @Expose
    private String iSBNNumber;
    @SerializedName("MultipleVariants")
    @Expose
    private Boolean multipleVariants;
    @SerializedName("ImageName")
    @Expose
    private String imageName;
    @SerializedName("DefaultPrice")
    @Expose
    private Double defaultPrice;
    @SerializedName("PurchasePrice")
    @Expose
    private Integer purchasePrice;
    @SerializedName("SalePrice")
    @Expose
    private Integer salePrice;
    @SerializedName("AllowPriceEdit")
    @Expose
    private Boolean allowPriceEdit;



    private Integer dealID;



    private String dealType;



    public Boolean getAllowPriceEdit() {
        return allowPriceEdit;
    }

    public void setAllowPriceEdit(Boolean allowPriceEdit) {
        this.allowPriceEdit = allowPriceEdit;
    }

    public Integer getDealID() {
        return dealID;
    }

    public void setDealID(Integer dealID) {
        this.dealID = dealID;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }
    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
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

    public String getProductCategoryID() {
        return productCategoryID;
    }

    public void setProductCategoryID(String productCategoryID) {
        this.productCategoryID = productCategoryID;
    }

    public Integer getBrandID() {
        return brandID;
    }

    public void setBrandID(Integer brandID) {
        this.brandID = brandID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Object getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(Object productCategoryName) {
        this.productCategoryName = productCategoryName;
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
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
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

    public String getUPCCode() {
        return uPCCode;
    }

    public void setUPCCode(String uPCCode) {
        this.uPCCode = uPCCode;
    }

    public String getISBNNumber() {
        return iSBNNumber;
    }

    public void setISBNNumber(String iSBNNumber) {
        this.iSBNNumber = iSBNNumber;
    }

    public Boolean getMultipleVariants() {
        return multipleVariants;
    }

    public void setMultipleVariants(Boolean multipleVariants) {
        this.multipleVariants = multipleVariants;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

//    public Integer getDefaultPrice() {
//        return defaultPrice;
//    }
//
//    public void setDefaultPrice(Integer defaultPrice) {
//        this.defaultPrice = defaultPrice;
//    }

    public Double getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(Double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

}
