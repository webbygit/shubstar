
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class UOMArray extends SugarRecord implements Serializable {

    @SerializedName("UOMID")
    @Expose
    private Integer uOMID;
    @SerializedName("UOM")
    @Expose
    private String uOM;
    @SerializedName("UOMDescription")
    @Expose
    private String uOMDescription;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("BaseQty")
    @Expose
    private Double baseQty;
    @SerializedName("BaseUOMID")
    @Expose
    private Integer baseUOMID;
    @SerializedName("BaseUOMName")
    @Expose
    private String baseUOMName;
    @SerializedName("SortOrder")
    @Expose
    private Integer sortOrder;

    public Integer getUOMID() {
        return uOMID;
    }

    public void setUOMID(Integer uOMID) {
        this.uOMID = uOMID;
    }

    public String getUOM() {
        return uOM;
    }

    public void setUOM(String uOM) {
        this.uOM = uOM;
    }

    public String getUOMDescription() {
        return uOMDescription;
    }

    public void setUOMDescription(String uOMDescription) {
        this.uOMDescription = uOMDescription;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Double getBaseQty() {
        return baseQty;
    }

    public void setBaseQty(Double baseQty) {
        this.baseQty = baseQty;
    }

    public Integer getBaseUOMID() {
        return baseUOMID;
    }

    public void setBaseUOMID(Integer baseUOMID) {
        this.baseUOMID = baseUOMID;
    }

    public String getBaseUOMName() {
        return baseUOMName;
    }

    public void setBaseUOMName(String baseUOMName) {
        this.baseUOMName = baseUOMName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

}
