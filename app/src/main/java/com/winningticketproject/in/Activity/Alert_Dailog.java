package com.winningticketproject.in.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.SignInSingup.DeepLinkLogin;
import com.winningticketproject.in.SignInSingup.Login_Screen;

public class Alert_Dailog {

    public static void showNetworkAlert(final Context activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Session Timeout");

        final Share_it share_it = new Share_it(activity);

        alertDialog.setMessage("In some other device same user logged in. Please login again");
        alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent login_intent = new Intent(activity, Login_Screen.class);

                Share_it.remove_key("auth_token");
                Share_it.remove_key("role");

                Share_it.savestring("role","");

                activity.startActivity(login_intent);
                ((Activity) activity).finish();
                ((Activity) activity).finishAffinity();
            }
        });
        alertDialog.show();
      }

    public static void showNetworkAlert1(final Context activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Deeplink Login");

        final Share_it share_it = new Share_it(activity);

        alertDialog.setMessage("Plaese Login");
        alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent login_intent = new Intent(activity, DeepLinkLogin.class);

                Share_it.remove_key("auth_token");
                Share_it.remove_key("role");

                Share_it.savestring("role","");

                activity.startActivity(login_intent);
            }
        });
        alertDialog.show();
    }
    }
