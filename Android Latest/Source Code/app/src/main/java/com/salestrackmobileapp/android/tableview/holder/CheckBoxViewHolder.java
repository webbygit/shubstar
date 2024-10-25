package com.salestrackmobileapp.android.tableview.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.tableview.model.CellModel;

/**
 * Created by UserPC on 13-Mar-18.
 */

public class CheckBoxViewHolder extends AbstractViewHolder {

    private final ImageView checkBox;




    public CheckBoxViewHolder(View itemView) {
        super(itemView);
        checkBox =  itemView.findViewById(R.id.checkbox);
//        checkBox=new ImageView(mcontext);
//
//            checkBox.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.delete));







        // Get vector drawables

    }

    public void setCellModel(CellModel p_jModel) {
//        String id = p_jModel.getId();
//        Log.e("ID_string","::::"+id);
//        if (id != null) {
//            checkBox.setId(Integer.parseInt(id));
//
//
//        }
    }



}
