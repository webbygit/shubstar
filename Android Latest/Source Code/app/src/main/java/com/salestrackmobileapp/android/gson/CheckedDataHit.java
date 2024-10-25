
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckedDataHit {

    @SerializedName("UserCheckInId")
    @Expose
    private Integer userCheckInId;
    @SerializedName("UserProfileID")
    @Expose
    private Integer userProfileID;
    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
    @SerializedName("CheckInDate")
    @Expose
    private String checkInDate;
    @SerializedName("BusinessID")
    @Expose
    private Integer businessID;

    public Integer getUserCheckInId() {
        return userCheckInId;
    }

    public void setUserCheckInId(Integer userCheckInId) {
        this.userCheckInId = userCheckInId;
    }

    public Integer getUserProfileID() {
        return userProfileID;
    }

    public void setUserProfileID(Integer userProfileID) {
        this.userProfileID = userProfileID;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Integer getBusinessID() {
        return businessID;
    }

    public void setBusinessID(Integer businessID) {
        this.businessID = businessID;
    }

}
