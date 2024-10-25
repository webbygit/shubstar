
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

public class DashboardArray extends SugarRecord implements Serializable {

    @SerializedName("$id")
    @Expose
    private String dashboard_id;
    @SerializedName("monthlyGoals")
    @Expose
    private MonthlyGoals monthlyGoals;
    @SerializedName("CheckIn")
    @Expose
    private List<CheckIn> checkIn = null;

    public String getDashboard_id() {
        return dashboard_id;
    }

    public void setDashboard_id(String dashboard_id) {
        this.dashboard_id = dashboard_id;
    }

    public MonthlyGoals getMonthlyGoals() {
        return monthlyGoals;
    }

    public void setMonthlyGoals(MonthlyGoals monthlyGoals) {
        this.monthlyGoals = monthlyGoals;
    }

    public List<CheckIn> getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(List<CheckIn> checkIn) {
        this.checkIn = checkIn;
    }

}
