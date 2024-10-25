
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class AllNotification extends SugarRecord implements Serializable {

    @SerializedName("QueueID")
    @Expose
    private Integer queueID;
    @SerializedName("QueueDate")
    @Expose
    private String queueDate;
    @SerializedName("DeviceID")
    @Expose
    private String deviceID;
    @SerializedName("DeviceType")
    @Expose
    private String deviceType;
    @SerializedName("ContentTitle")
    @Expose
    private String contentTitle;
    @SerializedName("contentBody")
    @Expose
    private String contentBody;
    @SerializedName("NotificationSent")
    @Expose
    private Boolean notificationSent;
    @SerializedName("SendDateTime")
    @Expose
    private String sendDateTime;
    @SerializedName("CompanyCode")
    @Expose
    private String companyCode;
    @SerializedName("UserProfileID")
    @Expose
    private Integer userProfileID;

    public Integer getQueueID() {
        return queueID;
    }

    public void setQueueID(Integer queueID) {
        this.queueID = queueID;
    }

    public String getQueueDate() {
        return queueDate;
    }

    public void setQueueDate(String queueDate) {
        this.queueDate = queueDate;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }

    public Boolean getNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(Boolean notificationSent) {
        this.notificationSent = notificationSent;
    }

    public String getSendDateTime() {
        return sendDateTime;
    }

    public void setSendDateTime(String sendDateTime) {
        this.sendDateTime = sendDateTime;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Integer getUserProfileID() {
        return userProfileID;
    }

    public void setUserProfileID(Integer userProfileID) {
        this.userProfileID = userProfileID;
    }

}
