package com.salestrackmobileapp.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.ApplicationClass;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.custome_views.Custome_EditText;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.gson.AllBusiness;
import com.salestrackmobileapp.android.gson.NotesForBusiness;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.salestrackmobileapp.android.activities.GoalsActivities.actionBarTitle;

public class NotesFragment extends BaseFragment {

    @BindView(R.id.business_name_st)
    Custome_TextView businessNameSt;
    @BindView(R.id.data_notes_et)
    Custome_EditText dataNotesEt;
    @BindView(R.id.save_btn)
    Button saveBtn;
    AllBusiness businessObject;
    NotesForBusiness notesForBusiness;

    String backCheckGoal = "";
    int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        ButterKnife.bind(this, view);

        GoalsActivities.homeIconImg.setImageDrawable(getResources().getDrawable(R.drawable.back_arrow));
        if (getArguments() != null) {
            businessObject = (AllBusiness) getArguments().getSerializable("BusinessObject");
        }


        if (getArguments().containsKey("checkin")) {
            backCheckGoal = "backCheckingToPriFrag";
        }
        notesForBusiness = Select.from(NotesForBusiness.class).where(Condition.prop("business_id").eq(businessObject.getBusinessID())).first();
        if (notesForBusiness != null) {
            dataNotesEt.setText(notesForBusiness.getDataToSave() + "");
        }
        businessNameSt.setText(businessObject.getBusnessName() + "");
        dataNotesEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean handled = false;
                if (keyCode == EditorInfo.IME_ACTION_DONE) {
                    /* Write your logic here that will be executed when user taps next button */
                    if (notesForBusiness != null) {
                        if (dataNotesEt.getText().toString().equals("") || dataNotesEt.getText().toString().isEmpty()) {
                            Toast.makeText(baseActivity, "Notes detail empty", Toast.LENGTH_SHORT).show();
                        } else {
                            notesForBusiness.setDataToSave(dataNotesEt.getText().toString() + "");
                            notesForBusiness.save();
                            Toast.makeText(baseActivity, "Information save successfully", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (businessObject != null) {
                            if (dataNotesEt.getText().toString().equals("") || dataNotesEt.getText().toString().isEmpty()) {

                                Toast.makeText(baseActivity, "Notes detail empty", Toast.LENGTH_SHORT).show();
                            } else {
                                NotesForBusiness notesForBusinessNewObj = new NotesForBusiness();
                                notesForBusinessNewObj.setBusinessId(businessObject.getBusinessID() + "");
                                notesForBusinessNewObj.setDataToSave(dataNotesEt.getText().toString() + "");
                                notesForBusinessNewObj.save();
                            }
                        } else {
                            Toast.makeText(baseActivity, "business detail not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                    handled = true;
                }
                return handled;
            }
        });


        if (dataNotesEt.getText().toString().isEmpty() | dataNotesEt.getText().toString().equals("")) {
            dataNotesEt.setEnabled(true);
            dataNotesEt.setBackgroundColor(getResources().getColor(R.color.white));
            saveBtn.setBackground(getResources().getDrawable(R.drawable.selector_blue_fade_button));
            saveBtn.setText("Save");
        } else {
            i = 1;
            dataNotesEt.setEnabled(false);
            dataNotesEt.setBackgroundColor(getResources().getColor(R.color.back_white));
            dataNotesEt.setBackgroundColor(getResources().getColor(R.color.back_white));
            saveBtn.setBackground(getResources().getDrawable(R.drawable.selector_blue_button));
            saveBtn.setText("Update");
        }

        return view;
    }

    @OnClick(R.id.save_btn)
    public void saveNotes() {
        if (i == 0) {
            i = 1;
            saveBtn.setText("Update");
            saveBtn.setBackground(getResources().getDrawable(R.drawable.selector_blue_fade_button));
            dataNotesEt.setEnabled(false);
            dataNotesEt.setTextColor(getResources().getColor(R.color.grey));
            dataNotesEt.setBackgroundColor(getResources().getColor(R.color.back_white));

        } else {
            saveBtn.setText("Save");
            dataNotesEt.setEnabled(true);
            dataNotesEt.setBackgroundColor(getResources().getColor(R.color.white));
            saveBtn.setBackground(getResources().getDrawable(R.drawable.selector_blue_button));
            i = 0;
        }
        if (notesForBusiness != null) {
            if (dataNotesEt.getText().toString().equals("") || dataNotesEt.getText().toString().isEmpty()) {
                Toast.makeText(baseActivity, "Notes detail empty", Toast.LENGTH_SHORT).show();
            } else {
                notesForBusiness.setDataToSave(dataNotesEt.getText().toString() + "");
                notesForBusiness.save();
                Toast.makeText(baseActivity, "Information save successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (businessObject != null) {
                if (dataNotesEt.getText().toString().equals("") || dataNotesEt.getText().toString().isEmpty()) {

                    Toast.makeText(baseActivity, "Notes detail empty", Toast.LENGTH_SHORT).show();
                } else {
                    NotesForBusiness notesForBusinessNewObj = new NotesForBusiness();
                    notesForBusinessNewObj.setBusinessId(businessObject.getBusinessID() + "");
                    notesForBusinessNewObj.setDataToSave(dataNotesEt.getText().toString() + "");
                    notesForBusinessNewObj.save();
                }
            } else {
                Toast.makeText(baseActivity, "business detail not available", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void onBackPressed() {
        if (backCheckGoal.equals("backCheckingToPriFrag")) {
            actionBarTitle.setText("BUSINESS");

            FragmentTransaction ft = ((GoalsActivities) baseActivity).getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_enter, R.anim.slide_exit);
            ft.replace(R.id.container, new CheckInPriOrderFragment());
            ft.commit();
        } else {
            Intent intent = new Intent(baseActivity, GoalsActivities.class);
            intent.putExtra("nameActivity", "businessDetail");
            intent.putExtra("BusinessObject", businessObject);
            startActivity(intent);
        }
    }
}
