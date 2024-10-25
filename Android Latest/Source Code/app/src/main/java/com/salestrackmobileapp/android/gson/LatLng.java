
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

public class LatLng extends SugarRecord implements Serializable {

    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("TrackDate")
    @Expose
    private String trackDate;
    @SerializedName("SalesPersonName")
    @Expose
    private String salesPersonName;
    @SerializedName("slSalesperson")
    @Expose
    private List<SlSalesperson> slSalesperson = null;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTrackDate() {
        return trackDate;
    }

    public void setTrackDate(String trackDate) {
        this.trackDate = trackDate;
    }

    public String getSalesPersonName() {
        return salesPersonName;
    }

    public void setSalesPersonName(String salesPersonName) {
        this.salesPersonName = salesPersonName;
    }

    public List<SlSalesperson> getSlSalesperson() {
        return slSalesperson;
    }

    public void setSlSalesperson(List<SlSalesperson> slSalesperson) {
        this.slSalesperson = slSalesperson;
    }

}
