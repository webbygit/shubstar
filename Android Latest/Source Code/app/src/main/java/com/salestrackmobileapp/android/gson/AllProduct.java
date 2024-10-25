
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class AllProduct extends SugarRecord implements Serializable{

    @SerializedName("$id")
    @Expose
    private String db_id;
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
    private Integer productCategoryID;
    @SerializedName("BrandID")
    @Expose
    private Integer brandID;
    @SerializedName("BrandName")
    @Expose
    private String brandName;
    @SerializedName("ProductCategoryName")
    @Expose
    private String productCategoryName;
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
    @SerializedName("CGSTPercentage")
    @Expose
    private Double cGSTPercentage;
    @SerializedName("SGSTPercentage")
    @Expose
    private Double sGSTPercentage;
    @SerializedName("PurchasePrice")
    @Expose
    private Double purchasePrice;
    @SerializedName("SalePrice")
    @Expose
    private Double salePrice;
    @SerializedName("blnUpdatePriceList")
    @Expose
    private Boolean blnUpdatePriceList;
    @SerializedName("UOMMapping")
    @Expose
    private String uOMMapping;
    @SerializedName("DefaultUOMID")
    @Expose
    private Integer defaultUOMID;
    @SerializedName("InStock")
    @Expose
    private Boolean inStock;

    public Boolean getInStock() {
        return inStock;
    }
    @SerializedName("VariantID")
    @Expose
    private Integer variantID;

    @SerializedName("AllowPriceEdit")
    @Expose
    private Boolean allowPriceEdit;

    public Boolean getAllowPriceEdit() {
        return allowPriceEdit;
    }

    public void setAllowPriceEdit(Boolean allowPriceEdit) {
        this.allowPriceEdit = allowPriceEdit;
    }

    public Integer getVariantID() {
        return variantID;
    }

    public void setVariantID(Integer variantID) {
        this.variantID = variantID;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    /*  @SerializedName("PurchasePrice")
        @Expose
        private Double purchasePrice;
        @SerializedName("SalePrice")
        @Expose
        private Double salePrice;
    */
    public String getDb_id() {
        return db_id;
    }

    public void setDb_id(String db_id) {
        this.db_id = db_id;
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

    public Integer getProductCategoryID() {
        return productCategoryID;
    }

    public void setProductCategoryID(Integer productCategoryID) {
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

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
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

    public Double getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(Double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public Double getCGSTPercentage() {
        return cGSTPercentage;
    }

    public void setCGSTPercentage(Double cGSTPercentage) {
        this.cGSTPercentage = cGSTPercentage;
    }

    public Double getSGSTPercentage() {
        return sGSTPercentage;
    }

    public void setSGSTPercentage(Double sGSTPercentage) {
        this.sGSTPercentage = sGSTPercentage;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Boolean getBlnUpdatePriceList() {
        return blnUpdatePriceList;
    }

    public void setBlnUpdatePriceList(Boolean blnUpdatePriceList) {
        this.blnUpdatePriceList = blnUpdatePriceList;
    }

    public String getUOMMapping() {
        return uOMMapping;
    }

    public void setUOMMapping(String uOMMapping) {
        this.uOMMapping = uOMMapping;
    }

    public Integer getDefaultUOMID() {
        return defaultUOMID;
    }

    public void setDefaultUOMID(Integer defaultUOMID) {
        this.defaultUOMID = defaultUOMID;
    }
}