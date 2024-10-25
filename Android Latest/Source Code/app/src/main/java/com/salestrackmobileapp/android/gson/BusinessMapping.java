
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class BusinessMapping extends SugarRecord implements Serializable {

    @SerializedName("$id")
    @Expose
    private String defaultBusinessID;
    @SerializedName("Business")
    @Expose
    private GoalBusiness business;

    public String getDefaultBusinessID() {
        return defaultBusinessID;
    }

    public void setDefaultBusinessID(String defaultBusinessID) {
        this.defaultBusinessID = defaultBusinessID;
    }

    public GoalBusiness getBusiness() {
        return business;
    }

    public void setBusiness(GoalBusiness business) {
        this.business = business;
    }

}
