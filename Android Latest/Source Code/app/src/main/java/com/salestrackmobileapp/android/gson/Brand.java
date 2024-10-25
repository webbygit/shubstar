
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class Brand  extends SugarRecord implements Serializable {

    @SerializedName("$id")
    @Expose
    private String brand_id;
    @SerializedName("BrandID")
    @Expose
    private Integer brandID;
    @SerializedName("BrandName")
    @Expose
    private String brandName;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Category")
    @Expose
    private String category;
    @SerializedName("ImageName")
    @Expose
    private String imageName;

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

}
