package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

/**
 * Created by UserPC on 05-Mar-18.
 */

public class VariantByProduct extends SugarRecord implements Serializable {
    @SerializedName("VariantID")
    @Expose
    private long variantID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("IsActive")
    @Expose
    private boolean isActive;
    @SerializedName("IsDeafult")
    @Expose
    private Boolean isDeafult;
    @SerializedName("ProductID")
    @Expose
    private long productID;
    @SerializedName("product")
    @Expose
    private List<VariantProduct> variantProductList;

    public List<VariantProduct> getVariantProductList() {
        return variantProductList;
    }

    public void setVariantProductList(List<VariantProduct> variantProductList) {
        this.variantProductList = variantProductList;
    }

    @SerializedName("PurchasePrice")
    @Expose
    private Double purchasePrice;
    @SerializedName("SalePrice")
    @Expose
    private Double salePrice;
    @SerializedName("ImageName")
    @Expose
    private String imageName;

    public long getVariantID() {
        return variantID;
    }

    public void setVariantID(long variantID) {
        this.variantID = variantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Boolean getDeafult() {
        return isDeafult;
    }

    public void setDeafult(Boolean deafult) {
        isDeafult = deafult;
    }


    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
