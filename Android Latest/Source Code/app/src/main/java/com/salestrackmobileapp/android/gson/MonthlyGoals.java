
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

public class MonthlyGoals  extends SugarRecord implements Serializable {

    @SerializedName("MonthlyTarget")
    @Expose
    private Integer monthlyTarget;
    @SerializedName("AchievedTarget")
    @Expose
    private Integer achievedTarget;

    public Integer getMonthlyTarget() {
        return monthlyTarget;
    }

    public void setMonthlyTarget(Integer monthlyTarget) {
        this.monthlyTarget = monthlyTarget;
    }

    public Integer getAchievedTarget() {
        return achievedTarget;
    }

    public void setAchievedTarget(Integer achievedTarget) {
        this.achievedTarget = achievedTarget;
    }

}
