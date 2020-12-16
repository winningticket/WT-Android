package com.winningticketproject.in.AppInfo;
import android.app.AlertDialog;
import android.content.Context;

public class ProgressDialog {

    public static ProgressDialog customProgress = null;
    android.app.ProgressDialog progressDialogs;


    public static ProgressDialog getInstance() {
        if (customProgress == null) {
            customProgress = new ProgressDialog();
        }
        return customProgress;
    }

    public void showProgress(Context context) {
        progressDialogs = new android.app.ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialogs.setMessage("Please wait...");
        progressDialogs.setCancelable(false);
        progressDialogs.setIndeterminate(true);
        progressDialogs.show();
    }

    public void hideProgress() {
        if (progressDialogs != null) {
            progressDialogs.dismiss();
            progressDialogs = null;
        }
    }
}