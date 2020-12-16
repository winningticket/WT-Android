package com.winningticketproject.in.CourseTab;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.Fragments.EventFragment;
import com.winningticketproject.in.Fragments.HomeFragment;
import com.winningticketproject.in.Fragments.My_account;
import com.winningticketproject.in.MyAccountTab.EditAccount_Details;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import cz.msebera.android.httpclient.HttpStatus;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

public class Courses_list_flow extends AppCompatActivity implements View.OnClickListener {
    LinearLayout event_layout, course_layout, user_layout, home_layout;
    public static TextView event_text, courses_text, user_text, home_text, noti_count;
    public static View event_view, courses_view, euser_view, home_view;
    public static ImageView imag_home, imag_event, imag_course, imag_my_account;
    String auth_token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        event_layout = findViewById(R.id.event_layout);
        course_layout = findViewById(R.id.course_layout);
        user_layout = findViewById(R.id.user_layout);
        home_layout = findViewById(R.id.home_layout);

        event_text = findViewById(R.id.event_text);
        courses_text = findViewById(R.id.courses_text);
        user_text = findViewById(R.id.user_text);
        home_text = findViewById(R.id.home_text);
        noti_count = findViewById(R.id.noti_count);
        noti_count.setTypeface(semibold);

        event_view = findViewById(R.id.event_view);
        courses_view = findViewById(R.id.courses_view);
        euser_view = findViewById(R.id.euser_view);
        home_view = findViewById(R.id.home_view);

        imag_home = findViewById(R.id.imag_home);
        imag_event = findViewById(R.id.imag_event);
        imag_course = findViewById(R.id.imag_course);
        imag_my_account = findViewById(R.id.imag_my_account);

        auth_token = get_auth_token("auth_token");

        event_text.setTypeface(regular);
        courses_text.setTypeface(regular);
        user_text.setTypeface(regular);

        home_text.setTextColor(getResources().getColor(R.color.colorbtn));

        Fragment profilefragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, profilefragment);
        fragmentTransaction.commit();

        event_layout.setOnClickListener(this);
        course_layout.setOnClickListener(this);
        user_layout.setOnClickListener(this);
        home_layout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.event_layout:

                event_view.setBackgroundColor(getResources().getColor(R.color.colorbtn));
                courses_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                euser_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                home_view.setBackgroundColor(getResources().getColor(R.color.colorblack));

                user_text.setTextColor(getResources().getColor(R.color.colorwhite));
                home_text.setTextColor(getResources().getColor(R.color.colorwhite));
                event_text.setTextColor(getResources().getColor(R.color.btn_color));
                courses_text.setTextColor(getResources().getColor(R.color.colorwhite));

                imag_home.setBackground(getResources().getDrawable(R.drawable.white_home_bottom));
                imag_event.setBackground(getResources().getDrawable(R.drawable.blue_home_event));
                imag_course.setBackground(getResources().getDrawable(R.drawable.home_corse));
                imag_my_account.setBackground(getResources().getDrawable(R.drawable.home_user));

                Fragment profilefragment = new EventFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, profilefragment);
                fragmentTransaction.commit();

//                method_to_load_user_popup();

                break;


            case R.id.course_layout:


                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }else{
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }else {

                    event_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                    courses_view.setBackgroundColor(getResources().getColor(R.color.colorbtn));
                    euser_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                    home_view.setBackgroundColor(getResources().getColor(R.color.colorblack));

                    user_text.setTextColor(getResources().getColor(R.color.colorwhite));
                    home_text.setTextColor(getResources().getColor(R.color.colorwhite));
                    event_text.setTextColor(getResources().getColor(R.color.colorwhite));
                    courses_text.setTextColor(getResources().getColor(R.color.colorbtn));

                    imag_home.setBackground(getResources().getDrawable(R.drawable.white_home_bottom));
                    imag_event.setBackground(getResources().getDrawable(R.drawable.home_event));
                    imag_course.setBackground(getResources().getDrawable(R.drawable.blue_home_course));
                    imag_my_account.setBackground(getResources().getDrawable(R.drawable.home_user));

                    Share_it.savestring("view", "map");

                    Fragment courses = new AllCourseOnMap();
                    FragmentTransaction coursetr = getSupportFragmentManager().beginTransaction();
                    coursetr.replace(R.id.content_frame, courses);
                    coursetr.commit();

//                    method_to_load_user_popup();
                }


                break;

            case R.id.user_layout:

                event_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                courses_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                euser_view.setBackgroundColor(getResources().getColor(R.color.colorbtn));
                home_view.setBackgroundColor(getResources().getColor(R.color.colorblack));

                user_text.setTextColor(getResources().getColor(R.color.colorbtn));
                home_text.setTextColor(getResources().getColor(R.color.colorwhite));
                event_text.setTextColor(getResources().getColor(R.color.colorwhite));
                courses_text.setTextColor(getResources().getColor(R.color.colorwhite));

                imag_home.setBackground(getResources().getDrawable(R.drawable.white_home_bottom));
                imag_event.setBackground(getResources().getDrawable(R.drawable.home_event));
                imag_course.setBackground(getResources().getDrawable(R.drawable.home_corse));
                imag_my_account.setBackground(getResources().getDrawable(R.drawable.blue_home_account));

                Fragment account = new My_account();
                FragmentTransaction accounttr = getSupportFragmentManager().beginTransaction();
                accounttr.replace(R.id.content_frame, account);
                accounttr.commit();

//                method_to_load_user_popup();

                break;

            case R.id.home_layout:

                event_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                courses_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                euser_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                home_view.setBackgroundColor(getResources().getColor(R.color.colorbtn));

                home_text.setTextColor(getResources().getColor(R.color.colorbtn));
                user_text.setTextColor(getResources().getColor(R.color.colorwhite));
                event_text.setTextColor(getResources().getColor(R.color.colorwhite));
                courses_text.setTextColor(getResources().getColor(R.color.colorwhite));

                imag_home.setBackground(getResources().getDrawable(R.drawable.home_bottom_logo));
                imag_event.setBackground(getResources().getDrawable(R.drawable.home_event));
                imag_course.setBackground(getResources().getDrawable(R.drawable.home_corse));
                imag_my_account.setBackground(getResources().getDrawable(R.drawable.home_user));

                Fragment home = new HomeFragment();
                FragmentTransaction home_frag = getSupportFragmentManager().beginTransaction();
                home_frag.replace(R.id.content_frame, home);
                home_frag.commit();

                break;
        }
    }

    private void method_to_load_user_popup() {
        if(get_auth_token("user_player_type").equals("player")) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view1 = inflater.inflate(R.layout.custom_alert_pop , null);
            AlertDialog  alertDialog = new AlertDialog.Builder(this).setView(view1).show();
            TextView textView1 = view1.findViewById(R.id.title);
            textView1.setTypeface(semibold);
            TextView textView2 = view1.findViewById(R.id.text_msg);
            textView2.setTypeface(regular);
            TextView textView = view1.findViewById(R.id.btn_alert_ok);
            textView.setTypeface(regular);
            TextView textView3 = view1.findViewById(R.id.btn_alert_cancel);
            textView3.setTypeface(regular);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Courses_list_flow.this , EditAccount_Details.class));
                    alertDialog.dismiss();
                    alertDialog.setCanceledOnTouchOutside(false);
                }
            });
            textView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    alertDialog.setCanceledOnTouchOutside(false);
                }
            });
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNetworkAvailable()) {
                alertdailogbox("livescore");
            } else {
                method_to_cal_notification_count_API();
            }
    }

    private void method_to_cal_notification_count_API() {

        String Url = Splash_screen.imageurl+"api/v2/users/notification_count";
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            ProgressDialog.getInstance().hideProgress();
                            System.out.println("------response1------"+response);
                            if (response.getString("status").equals("Success")) {

                                String notification_count = response.getString("notification_count");

                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if (Integer.parseInt(notification_count) > 0) {
                                            noti_count.setVisibility(View.VISIBLE);
                                            noti_count.setText(notification_count);
                                        } else {
                                            noti_count.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                            ProgressDialog.getInstance().hideProgress();
                        }catch (Exception e){
                            //nothing
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ProgressDialog.getInstance().hideProgress();
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(Courses_list_flow.this);
                            }
                        }else {
                            Toast.makeText(Courses_list_flow.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("auth-token", get_auth_token("auth_token"));
                return headers;
            }
        };

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    public void alertdailogbox(final String value) {
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
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (value.equals("livescore")) {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("livescore");
                    } else {
                        method_to_cal_notification_count_API();
                    }
                }
            }
        });
        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver), new IntentFilter("Notification_count"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            method_to_cal_notification_count_API();
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                        event_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                        courses_view.setBackgroundColor(getResources().getColor(R.color.colorbtn));
                        euser_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                        home_view.setBackgroundColor(getResources().getColor(R.color.colorblack));

                        user_text.setTextColor(getResources().getColor(R.color.colorwhite));
                        home_text.setTextColor(getResources().getColor(R.color.colorwhite));
                        event_text.setTextColor(getResources().getColor(R.color.colorwhite));
                        courses_text.setTextColor(getResources().getColor(R.color.colorbtn));

                        imag_home.setBackground(getResources().getDrawable(R.drawable.white_home_bottom));
                        imag_event.setBackground(getResources().getDrawable(R.drawable.home_event));
                        imag_course.setBackground(getResources().getDrawable(R.drawable.blue_home_course));
                        imag_my_account.setBackground(getResources().getDrawable(R.drawable.home_user));

                        Share_it.savestring("view", "map");

                        Fragment courses = new AllCourseOnMap();
                        FragmentTransaction coursetr = getSupportFragmentManager().beginTransaction();
                        coursetr.replace(R.id.content_frame, courses);
                        coursetr.commitAllowingStateLoss();

                        method_to_load_user_popup();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
