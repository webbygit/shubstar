package com.salestrackmobileapp.android.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by kanchan on 3/23/2017.
 */

public class AlertDialogUtils {

    private AlertDialog.Builder dialogBuilder;
    public static AlertDialogUtils instance;

    public static AlertDialogUtils getInstance() {
        if (instance == null) {
            instance = new AlertDialogUtils();
        }
        return instance;
    }


    public void singleButtonDialog(Context context, String title, String positiveButtonTitle, DialogInterface.OnClickListener clickListener) {
        dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setPositiveButton(positiveButtonTitle, clickListener);
       // dialogBuilder.setCancelable(false);
        dialogBuilder.show();


    }

    public void doubleButtonDialog(Context context, String title, String positiveButtonTitle, DialogInterface.OnClickListener positiveButtonClickListener, String negetiveButtonTitle, DialogInterface.OnClickListener negetiveButtonClickListener) {
        dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setPositiveButton(positiveButtonTitle, positiveButtonClickListener);
        dialogBuilder.setNegativeButton(negetiveButtonTitle, negetiveButtonClickListener);
        dialogBuilder.show();


    }

}
