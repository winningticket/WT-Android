package com.winningticketproject.in.CourseTab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.IndividulaGameType.Location_Services;
import com.winningticketproject.in.Live_Score_New_Flow.View_course_map;
import com.winningticketproject.in.NewMapEvenFLOW.NormalScorreboard;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Course_Data;
import com.winningticketproject.in.AppInfo.GPSTracker;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.BuildConfig;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.EventTab.Score_board_new;
import com.winningticketproject.in.IGOLF.iGolfAuth;
import com.winningticketproject.in.MyAccountTab.SubcriptionInAppPurchase;
import com.winningticketproject.in.R;
import com.winningticketproject.in.TeeHandicapHole.Hole_Tee_Handicap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.winningticketproject.in.AppInfo.Share_it.remove_key;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.selected_handicap;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.selected_hole;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.selected_tee;
import static com.winningticketproject.in.EventTab.NewEventDetailPage.oragnization;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class Course_Detail_Page  extends FragmentActivity  implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    Button btn_play_round;
    SupportMapFragment mapFragment;
    CircleImageView courses_images;
    android.support.v7.app.AlertDialog alertDialog;
    String id_value = "";
    String auth_token = "", city;
    LatLng location = null;
    MarkerOptions marker = null;
    GPSTracker gps;
    double latitude, longitudes;
    View dialogview;
    String course_id;
    String course_image = "";
    String user_type = "";
    String name = "";
    String course_type = "";
    String address = "";
    String phone_number = "";
    String website_url = "";
    String zip_code = "";
    ArrayList<Course_Data> ar = new ArrayList<Course_Data>();
    int REQUEST_CODE=1;
    String player_id="";
    TextView account_title;
    TextView tv_course_name;
    TextView tv_course_location_name;
    TextView tv_course_distance_miles;
    TextView tv_course_type;

    ImageView image_loca_icon;

    TextView location_icon;
    TextView phone_icon;
    TextView browser_icon;

    TextView location_name;
    TextView phonenumber;
    TextView browser;


    TextView tv_tee_box_all_data;
    TextView tv_tee_box;

//    ImageView image_loca_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_detail_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        id_value = getIntent().getExtras().getString("id");

        auth_token = get_auth_token("auth_token");

        gps = new GPSTracker(Course_Detail_Page.this);

        savestring("CGB_event_code","1");
        savestring("current_event_code","2");

        initiliza_all_ids();


    }

    private void initiliza_all_ids() {

        account_title = findViewById(R.id.account_title);
        account_title.setText("Courses");
        account_title.setTypeface(medium);


        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

        image_loca_icon = findViewById(R.id.image_loca_icon);
        tv_course_type = findViewById(R.id.tv_course_type);

        courses_images = findViewById(R.id.courses_image);

        tv_course_name = findViewById(R.id.tv_course_name);
        tv_course_name.setTypeface(medium);

        tv_course_location_name = findViewById(R.id.tv_course_location_name);
        tv_course_location_name.setTypeface(regular);

        tv_course_distance_miles = findViewById(R.id.tv_course_distance_miles);
        tv_course_distance_miles.setTypeface(medium);
        tv_course_distance_miles.setText(getIntent().getExtras().getString("distance")+" miles");

        location_icon = findViewById(R.id.location_icon);
        phone_icon = findViewById(R.id.phone_icon);
        browser_icon = findViewById(R.id.browser_icon);

        browser = findViewById(R.id.browser);
        phonenumber = findViewById(R.id.phonenumber);

        location_icon.setText("\uf041");
        location_icon.setTypeface(webfont);

        phone_icon.setText("\uf095");
        phone_icon.setTypeface(webfont);

        browser_icon.setText("\uf0ac");
        browser_icon.setTypeface(webfont);

        browser.setOnClickListener(this);

        browser.setTypeface(medium);
        phonenumber.setTypeface(medium);

        location_name = findViewById(R.id.location_name);
        location_name.setTypeface(medium);
        location_name.setOnClickListener(this);

        btn_play_round = findViewById(R.id.btn_play_round);
        btn_play_round.setTypeface(medium);
        btn_play_round.setOnClickListener(this);

        // Tee Boxs
        tv_tee_box = findViewById(R.id.tv_tee_box);
        tv_tee_box.setTypeface(medium);


        tv_tee_box_all_data = findViewById(R.id.tv_tee_box_all_data);
        tv_tee_box_all_data.setTypeface(regular);


    }


    private void start_playing() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Course_Detail_Page.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) Course_Detail_Page.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogview = inflater.inflate(R.layout.play_round_popup, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;
        TextView location_services = alertDialog.findViewById(R.id.location_services);
        TextView nearby_courses = alertDialog.findViewById(R.id.st_round_nearby_courses);
        location_services.setTypeface(medium);
        nearby_courses.setTypeface(regular);
        nearby_courses.setText(name);

        Button enable = alertDialog.findViewById(R.id.enable);
        Button not_now = alertDialog.findViewById(R.id.not_now);
        enable.setTypeface(medium);
        not_now.setTypeface(medium);

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                method_to_cal_free_play_api();

            }
        });

        not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }


    private void start_playing_premium(final String player_id) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Course_Detail_Page.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) Course_Detail_Page.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogview = inflater.inflate(R.layout.play_round_popup_permium, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;
        TextView location_services = alertDialog.findViewById(R.id.location_services);
        TextView nearby_courses = alertDialog.findViewById(R.id.nearby_courses);
        location_services.setTypeface(semibold);
        nearby_courses.setTypeface(regular);
        Button enable = alertDialog.findViewById(R.id.enable);
        Button not_now = alertDialog.findViewById(R.id.not_now);
        enable.setTypeface(semibold);
        not_now.setTypeface(semibold);

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    alertdailogbox("dropin");
                } else {
                    Intent in = new Intent(getApplicationContext(), SubcriptionInAppPurchase.class);
                    startActivityForResult(in, REQUEST_CODE);

                }

                alertDialog.dismiss();
            }
        });

        not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                method_to_cal_free_play_api_cal(player_id);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){

                user_type = "premium";

                method_to_cal_free_play_api_cal(player_id);

            }
        }
    }


    private void method_to_cal_free_play_api_cal(final String player_id) {
        JSONObject object = new JSONObject();
        try {
            object.put("player_id", player_id);
//            object.put("player_type", player_type);

            System.out.println(object);

            ProgressDialog.getInstance().showProgress(Course_Detail_Page.this);
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url + "hole_info/player_type", object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println("-------free user------"+response);
                                ProgressDialog.getInstance().hideProgress();
                                if (response.getString("status").equals("Success")) {

                                    Share_it.savestring("play_type", "free");
                                    Share_it.savestring("event_id", player_id);
                                    Share_it.savestring("first_time", "yes");
                                    Share_it.savestring("Event_name", name);
                                    Share_it.savestring("Game_type","Course");
                                    Share_it.savestring("handicap_score","0");
                                    Location_Services.game_name="Course";

                                    selected_tee = response.getString("selected_tee");
                                    selected_hole = response.getString("selected_hole");
                                    selected_handicap = response.getString("selected_handicap");

                                    savestring("starting_hole",selected_hole);
                                    savestring("cross_scoring","false");
                                    savestring("CGB_event_code","course");
                                    if (response.getString("selected_tee").equals(null) || response.getString("selected_tee") == null || response.getString("selected_tee").equals("null")){
                                        NewEventDetailPage.event_type ="";
                                        remove_key("page_type");
                                        Intent intent1 = new Intent(getApplicationContext(), Hole_Tee_Handicap.class);
                                        startActivity(intent1);
                                    }else {
                                        savestring("handicap",selected_handicap);
                                        savestring("selected_tee_postion",selected_tee);
                                        savestring("selected_hole_postion",selected_hole);
                                        Intent intent1 = new Intent(getApplicationContext(), View_course_map.class);
                                        startActivity(intent1);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "" + response.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception E) {
                                //nothing
                                ProgressDialog.getInstance().hideProgress();

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
                                if (error.networkResponse.statusCode == 401) {
                                    Alert_Dailog.showNetworkAlert(Course_Detail_Page.this);
                                } else {
                                    Toast.makeText(Course_Detail_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Course_Detail_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("auth-token", auth_token);
                    return headers;
                }
            };
            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            myRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(myRequest, "tag");
        } catch (Exception e) {
            //nothing
        }
    }

    private void method_to_cal_free_play_api() {
        try {
            ProgressDialog.getInstance().showProgress(Course_Detail_Page.this);
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url + "hole_info/add-player/" + course_id, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println("-------player------"+response);
                                ProgressDialog.getInstance().hideProgress();
                                if (response.getString("status").equals("Success")) {
                                    player_id = response.getString("player_id");
                                    Share_it.savestring("play_type", "free");
                                    Share_it.savestring("event_id", player_id);
                                    Share_it.savestring("first_time", "yes");
                                    Share_it.savestring("Event_name", name);
                                    Share_it.savestring("Game_type","Course");
                                    Location_Services.game_name="Course";

                                    selected_tee = response.getString("selected_tee");
                                    selected_hole = response.getString("selected_hole");
                                    selected_handicap = response.getString("selected_handicap");
                                    savestring("CGB_event_code","course");

                                    if (user_type.equals("free")) {
                                        start_playing_premium(player_id);
                                    } else {
                                        remove_key("page_type");
                                        Intent intent1 = new Intent(getApplicationContext(), Hole_Tee_Handicap.class);
                                        startActivity(intent1);
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "" + response.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception E) {
                                //nothing
                                ProgressDialog.getInstance().hideProgress();

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
                                if (error.networkResponse.statusCode == 401) {
                                    Alert_Dailog.showNetworkAlert(Course_Detail_Page.this);
                                } else {
                                    Toast.makeText(Course_Detail_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Course_Detail_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("auth-token", auth_token);
                    return headers;
                }
            };
            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            myRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(myRequest, "tag");
        } catch (Exception e) {
            //nothing
        }
    }


    public void alertdailogbox(final String value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Course_Detail_Page.this);
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
                if (value.equals("eventlist")) {

                    if (!isNetworkAvailable()) {
                        alertdailogbox("eventlist");
                    } else if (value.equals("dropin")) {
                        if (!isNetworkAvailable()) {

                            alertdailogbox("dropin");
                        }

                    } else {
                        methodtocall_course_detaisl_api();
                    }
                }
            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    public void method_for_check_wallete_balance() {
        try {
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url + "hole_info/player/player_type", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println("-----player_type-------" + response);
                                if (response.getString("status").equals("Success")) {
                                    Share_it.savestring("handicap_score", response.getString("handicap"));
                                    user_type = response.getString("user_type");
                                } else {
                                    Toast.makeText(getApplicationContext(), "Something wen't wrong" + response.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception E) {

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                // HTTP Status Code: 401 Unauthorized
                                if (error.networkResponse.statusCode == 401) {
                                    Alert_Dailog.showNetworkAlert(Course_Detail_Page.this);
                                } else {
                                    Toast.makeText(Course_Detail_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Course_Detail_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("auth-token", auth_token);
                    return headers;
                }
            };
            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            myRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(myRequest, "tag");
        } catch (Exception e) {
            //nothing
        }
    }

    private void methodtocall_course_detaisl_api() {
        ar.clear();
        ProgressDialog.getInstance().showProgress(Course_Detail_Page.this);
        try {
            JSONObject object = new JSONObject();
            object.put("id_course", id_value);
            object.put("detailLevel","2");
            String Course_Detaisl_Url = BuildConfig.REST_ENDPOINT + iGolfAuth.getUrlForAction("CourseDetails");

            System.out.println(Course_Detaisl_Url);

            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Course_Detaisl_Url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                System.out.println("-----response-----" + response);
                                course_id = response.getString("id_course");

                                name = response.getString("courseName");

                                tv_course_name.setText(response.getString("courseName"));

                                try {
                                    course_type = response.getString("class");
                                    if (course_type.equals("") || course_type.equals(null) || course_type.equals("null")) {
                                        course_type = "";
                                    }
                                } catch (Exception e) {
                                    course_type = "";
                                    //nothing
                                }

                                try {
                                    zip_code = response.getString("zipCode");
                                } catch (Exception e) {
                                    zip_code = "";
                                    //nothing
                                }

                                try {
                                    address = response.getString("address1");
                                    if (address.equals("") || address.equals(null) || address.equals("null")) {
                                        address = "Not Mentioned";
                                    }
                                } catch (Exception e) {
                                    address = "Not Mentioned";
                                    //nothing
                                }

                                try {
                                    phone_number = response.getString("phone");
                                    if (phone_number.equals("") || phone_number.equals(null) || phone_number.equals("null")) {
                                        phone_number = "Not Mentioned";
                                    }
                                } catch (Exception e) {
                                    phone_number = "Not Mentioned";
                                    //nothing
                                }
                                try {
                                    website_url = response.getString("url");
                                    if (website_url.equals("") || website_url.equals(null) || website_url.equals("null")) {
                                        website_url = "Not Mentioned";
                                        browser.setText(website_url);
                                    } else {
                                        browser.setText(website_url);
                                    }

                                } catch (Exception e) {
                                    //nothing

                                }

                                try {
                                    course_image = response.getString("image");
                                    savestring("imge_url",course_image);
                                    if (course_image.length()>4){
                                        Picasso.with(Course_Detail_Page.this)
                                                .load(course_image).placeholder(R.drawable.courses_empty).error(R.drawable.courses_empty)
                                                .into(courses_images);
                                    }
                                } catch (Exception e) {
                                    savestring("imge_url","null");
                                    //nothing
                                }

                                 double course_lat = response.getDouble("latitude");
                                 double course_long = response.getDouble("longitude");

                                try {

                                    location = new LatLng(course_lat, course_long);
                                    marker = new MarkerOptions().position(new LatLng(course_lat, course_long)).icon(BitmapDescriptorFactory.fromResource(R.drawable.golf_icon)).anchor(0.5f,0.5f);
                                    marker.title(name + "");
                                    mMap.addMarker(marker);
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
                                } catch (Exception e) {
                                    //nothing
                                }

                                phonenumber.setText(phone_number);
                                city = getIntent().getExtras().getString("city_state");
                                location_name.setText(address + ", "+city + " "+zip_code);


                                if (course_type.equals("private")) {
                                    image_loca_icon.setVisibility(View.VISIBLE);
                                    tv_course_type.setText("Private");
                                    tv_course_type.setTextColor(Color.parseColor("#FD2726"));
                                }else if (course_type.equals("semi-private")) {
                                    tv_course_type.setText("semi-private");
                                    image_loca_icon.setVisibility(View.VISIBLE);
                                    tv_course_type.setTextColor(Color.parseColor("#FD2726"));
                                }else if (course_type.equals("public")) {
                                    image_loca_icon.setVisibility(View.GONE);
                                    tv_course_type.setText("Public");
                                    tv_course_type.setTextColor(Color.parseColor("#00AC2C"));
                                }

                                tv_course_location_name.setText(getIntent().getStringExtra("city_state"));


                                JSONObject object = new JSONObject();
                                object.put("id_course", id_value);
                                object.put("detailLevel","2");
                                Near_by_course_list(object);

                            } catch (Exception E) {
                                ProgressDialog.getInstance().hideProgress();
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
                                if (error.networkResponse.statusCode == 401) {
                                    Alert_Dailog.showNetworkAlert(Course_Detail_Page.this);
                                } else {
                                    Toast.makeText(Course_Detail_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Course_Detail_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("auth-token", auth_token);
                    return headers;
                }

            };
            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            myRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(myRequest, "tag");
        } catch (Exception e) {
            //nothing
        }
    }

    private void Near_by_course_list(JSONObject object) {
        String url = BuildConfig.REST_ENDPOINT + iGolfAuth.getUrlForAction("CourseTeeDetails");
        System.out.println("----"+url);

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            System.out.println("-----tee----"+response);
                            JSONArray teesList = response.getJSONArray("teesList");

                            String htmlstring="";

                            for (int i=0;i<teesList.length();i++){

                                JSONObject teeobject = teesList.getJSONObject(i);
                                if (teeobject.getString("gender").equals("men")){
                                    htmlstring+= "<b>"+teeobject.getString("teeName")+"</b> (Rating "+teeobject.getString("ratingMen")+"/ Slope "+teeobject.getString("slopeMen")+")<br>"+teeobject.getString("ydsTotal")+" yds<br /><br />";
                                }else {
                                    htmlstring+= "<b>"+teeobject.getString("teeName")+"</b> (Rating "+teeobject.getString("ratingWomen")+"/ Slope "+teeobject.getString("slopeWomen")+")<br>"+teeobject.getString("ydsTotal")+" yds<br /><br />";
                                }
                            }

                            tv_tee_box_all_data.setText(Html.fromHtml(htmlstring));

                            ProgressDialog.getInstance().hideProgress();

                        } catch (Exception E) {
                            //nothing
                            ProgressDialog.getInstance().hideProgress();
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
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(Course_Detail_Page.this);
                            } else {
                                Toast.makeText(Course_Detail_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("auth-token", auth_token);
                return headers;
            }
        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
        //nothing


}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (Location_Services.isLocationEnabled(Course_Detail_Page.this)){
            latitude = gps.getLatitude();
            longitudes = gps.getLongitude();
            if (!isNetworkAvailable()) {
                alertdailogbox("eventlist");
            } else {
                method_for_check_wallete_balance();
                methodtocall_course_detaisl_api();
            }
            }

}


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_play_round:
                oragnization ="Purchase Premium User";
                savestring("live_aminties","This course does not have any amenities");
                start_playing();
                break;
            case R.id.browser:
                browser.setMovementMethod(LinkMovementMethod.getInstance());
                break;

            case R.id.location_name:
                String urlAddress = "http://maps.google.com/maps?q="+ latitude + ","+ longitudes + "(" + name + ")&iwloc=A&hl=es";
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(urlAddress));
                startActivity(intent);

                break;
        }
    }

}