package com.winningticketproject.in.SignInSingup;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.firebase.perf.metrics.Trace;
import com.winningticketproject.in.AllUsersLogin.AllUserLogin;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.CourseTab.Course_Search_Page;
import com.winningticketproject.in.CourseTab.Courses_list_flow;
import com.winningticketproject.in.OrganizerFlow.EventListFlow;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimeZone;

import io.fabric.sdk.android.Fabric;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Splash_screen extends AppCompatActivity {
//    updated
//    public static String url="https://www.winningticket.com/api/v1/";
//    public static String imageurl="https://www.winningticket.com/";

//    public static String url="https://staging.winningticket.com/api/v1/";
//    public static String imageurl="https://staging.winningticket.com/";

    public static String url="https://www.uat.winningticket.com/api/v1/";
    public static String imageurl="https://www.uat.winningticket.com/";

    String role="",versionCode="";

    public static ArrayList<String> countryvalues = new ArrayList<String>();
    public static ArrayList<String> countrykeys = new ArrayList<String>();

    public static Typeface semibold;
    public static Typeface regular;
    public static Typeface medium;
    public static Typeface black;
    public static Typeface italic;
    public static Typeface light;
    public static Typeface mediumitalic;
    public static Typeface webfont;

    @Override
    @AddTrace(name = "onCreateTrace", enabled = true /* optional */)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);

//        FirebaseCrash.log("Activity created");

        try {
            TimeZone tz = TimeZone.getDefault();
            System.out.println("TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" Timezon id :: " +tz.getID());
        }catch (Exception e){}

        semibold = Typeface.createFromAsset(getAssets(), "Montserrat-SemiBold.ttf");
        regular = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
        medium = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");
        black = Typeface.createFromAsset(getAssets(),"Montserrat-Black.ttf");
        italic = Typeface.createFromAsset(getAssets(),"Montserrat-Italic.ttf");
        light = Typeface.createFromAsset(getAssets(),"Montserrat-Light.ttf");
        mediumitalic = Typeface.createFromAsset(getAssets(),"MontserratAlternates-MediumItalic.ttf");
        webfont = Typeface.createFromAsset(getAssets(),"fontawesome-webfont.ttf");

        Share_it share_it = new Share_it(this);

        //        Fabric.with(this, new Crashlytics());

        role = get_auth_token("role");

        getVersionInfo();

        if(isNetworkAvailable()) {
            callToCountyAPI();
        }
        else
        {
            alertdailogbox();
        }
        System.out.println("-----auth-------- "+get_auth_token("auth_token"));
    }


    private void getVersionInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
             versionCode = packageInfo.versionName;
            System.out.println("-----versionCode-------- "+versionCode);

            method_to_call_version_api();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void alertdailogbox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("No Internet");
        builder.setMessage("Internet is required. Please Retry.");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                if (!isNetworkAvailable()) {

                    alertdailogbox();
                }
                else
                {
                    callToCountyAPI();
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }


    private void show_popup() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue reposting.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore("https://play.google.com/store/apps/details?id=" + Splash_screen.this.getPackageName());
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (role.equals("contributor")){
                                    Intent in = new Intent(getApplicationContext(), Courses_list_flow.class);
                                    startActivity(in);
                                    finish();
                                } else if(role.equals("organizer")) {
                                    Intent i = new Intent(Splash_screen.this, EventListFlow.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Intent i = new Intent(getApplicationContext(), AllUserLogin.class);
                                    startActivity(i);
                                    finish();
                                }

                            }
                        }).create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void callToCountyAPI() {
        countrykeys.clear();
        countryvalues.clear();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"city_states/countries", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    countrykeys.add("");
                    countryvalues.add("Select Country");
                    Iterator<String> keys = response.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        countrykeys.add(response.optString(key));
                        countryvalues.add(key);
                    }
                }
                catch (Exception e)
                {
                    //nothing
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy( 30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);

}

    private void method_to_call_version_api() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"users/get_version_number", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                        if (Float.valueOf(versionCode) < Float.valueOf(response.getString("version_number"))) {
                            //show anything
                            show_popup();

                        } else {

                            if (role.equals("contributor")) {
                                Intent in = new Intent(getApplicationContext(), Courses_list_flow.class);
                                startActivity(in);
                                finish();
                            }  else if(role.equals("organizer")) {
                                Intent i = new Intent(Splash_screen.this, EventListFlow.class);
                                startActivity(i);
                                finish();
                            }  else {
                                Intent i = new Intent(Splash_screen.this, AllUserLogin.class);
                                startActivity(i);
                                finish();
                            }
                        }

                }
                catch (Exception e)
                {
                    //nothing
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy( 30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    }
