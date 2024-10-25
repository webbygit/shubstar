package com.salestrackmobileapp.android.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.salestrackmobileapp.android.fragments.MyGoalFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by kanchan on 4/6/2017.
 */

public class SelectDateFragment extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener {

    TextView textView;
    int mm, yy, dd;
    MyGoalFragment myGoalFragment;

    public SelectDateFragment(TextView textView,MyGoalFragment myGoalFragment) {
        this.textView = textView;
        this.myGoalFragment=myGoalFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm , dd);

    }

    public void populateSetDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);

        MyGoalFragment.yy = year;
        MyGoalFragment.mm = month;
        MyGoalFragment.dd = day;

        String format = new SimpleDateFormat("MMM d, yyyy").format(cal.getTime());
        String formatforapi = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());
        String formatforoffline = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        MyGoalFragment.dateForApiST = format;
        MyGoalFragment.dateForApi=formatforapi;
        MyGoalFragment.dateForOffline=formatforoffline;
       // MyGoalFragment myGoalFragment=getGoalFragmentInstance();
      //  myGoalFragment.getOrderHistoryArray();
        textView.setText(format);
        myGoalFragment.getAllGoalsAccDate();


    }


    public static void incDateByOne() {
        MyGoalFragment.dd++;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, MyGoalFragment.yy);
        cal.set(Calendar.DAY_OF_MONTH, MyGoalFragment.dd);
        cal.set(Calendar.MONTH, MyGoalFragment.mm);

        String format = new SimpleDateFormat("MMM d, yyyy").format(cal.getTime());
        String formatforapi = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());
        String formatforoffline = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        MyGoalFragment.dateForApiST = /*String.valueOf(new StringBuilder().append(MyGoalFragment.yy).append("-").append(MyGoalFragment.mm).append("-").append(MyGoalFragment.dd).append(" "))*/format;
        MyGoalFragment.dateForApi = /*String.valueOf(new StringBuilder().append(MyGoalFragment.yy).append("-").append(MyGoalFragment.mm).append("-").append(MyGoalFragment.dd).append(" "))*/formatforapi;
        MyGoalFragment.dateForOffline = formatforoffline;
    }

    public static void decDateByOne() {

        MyGoalFragment.dd--;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, MyGoalFragment.yy);
        cal.set(Calendar.DAY_OF_MONTH, MyGoalFragment.dd);
        cal.set(Calendar.MONTH, MyGoalFragment.mm);

        String format = new SimpleDateFormat("MMM d, yyyy").format(cal.getTime());
        String formatforapi = new SimpleDateFormat("MM-dd-yyyy").format(cal.getTime());
        String formatforoffline = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        MyGoalFragment.dateForApiST = /*String.valueOf(new StringBuilder().append(MyGoalFragment.yy).append("-").append(MyGoalFragment.mm).append("-").append(MyGoalFragment.dd).append(" "))*/format;
        MyGoalFragment.dateForApi = /*String.valueOf(new StringBuilder().append(MyGoalFragment.yy).append("-").append(MyGoalFragment.mm).append("-").append(MyGoalFragment.dd).append(" "))*/formatforapi;
        MyGoalFragment.dateForOffline = formatforoffline;
    }
}