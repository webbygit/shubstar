
package com.salestrackmobileapp.android.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SlProductCategory {

    @SerializedName("Disabled")
    @Expose
    private Boolean disabled;
    @SerializedName("Group")
    @Expose
    private Group group;
    @SerializedName("Selected")
    @Expose
    private Boolean selected;
    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("Value")
    @Expose
    private String value;

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
