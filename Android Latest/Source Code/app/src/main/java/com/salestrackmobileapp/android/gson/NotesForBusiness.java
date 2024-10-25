package com.salestrackmobileapp.android.gson;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by kanchan on 6/8/2017.
 */

public class NotesForBusiness extends SugarRecord implements Serializable {

    String businessId;

    String dataToSave;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }


    public String getDataToSave() {
        return dataToSave;
    }

    public void setDataToSave(String dataToSave) {
        this.dataToSave = dataToSave;
    }

}
