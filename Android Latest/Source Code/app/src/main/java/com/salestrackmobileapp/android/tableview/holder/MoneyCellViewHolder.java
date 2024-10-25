package com.salestrackmobileapp.android.tableview.holder;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.tableview.model.CellModel;

import org.fabiomsr.moneytextview.MoneyTextView;

/**
 * Created by evrencoskun on 22.12.2017.
 */

public class MoneyCellViewHolder extends AbstractViewHolder {
    public final MoneyTextView cell_textview;
    public final LinearLayout cell_container;

    public MoneyCellViewHolder(View itemView) {
        super(itemView);
        cell_textview = itemView.findViewById(R.id.money_cell_data);
        cell_container = itemView.findViewById(R.id.cell_container);
    }

    public void setCellModel(CellModel p_jModel) {

        // Set text
        cell_textview.setAmount((int) p_jModel.getData());

        // It is necessary to remeasure itself.
        cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        cell_textview.requestLayout();
    }

    @Override
    public void setSelected(SelectionState p_nSelectionState) {
        super.setSelected(p_nSelectionState);

        if (p_nSelectionState == SelectionState.SELECTED) {
            changeColorOfMoneyTextView(R.color.selected_text_color);
        } else {
            changeColorOfMoneyTextView(R.color.unselected_text_color);
        }
    }

    private void changeColorOfMoneyTextView(@ColorRes int id) {
        int color = ContextCompat.getColor(cell_textview.getContext(), id);

        cell_textview.setBaseColor(color);
        cell_textview.setDecimalsColor(color);
        cell_textview.setSymbolColor(color);
    }
}
