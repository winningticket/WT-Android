package com.winningticketproject.in.Live_Score_New_Flow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.GPSTracker;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.AuctionModel.NewLive_Auction;
import com.winningticketproject.in.BestBallFlow.BestBall_Scorecard;
import com.winningticketproject.in.BestBallFlow.Bestbal_Leader_Board;
import com.winningticketproject.in.ChatWithOther.User_List;
import com.winningticketproject.in.CourseTab.Courses_list_flow;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.Fragments.HomeFragment;
import com.winningticketproject.in.IndividulaGameType.Grass_score_Update_from_Map;
import com.winningticketproject.in.IndividulaGameType.Location_Services;
import com.winningticketproject.in.NewMapEvenFLOW.NormalLeaderoard;
import com.winningticketproject.in.NewMapEvenFLOW.NormalScorreboard;
import com.winningticketproject.in.R;
import com.winningticketproject.in.Shamble_flow.Shamble_Bestball_Map_GS_activity;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import com.winningticketproject.in.Stable_modify_stroke_Flow.StableFord_Score_Card;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_ford_Leaderboard;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_modify_ScoreCard;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_modify_leader_board;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cz.msebera.android.httpclient.HttpStatus;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;
import static com.winningticketproject.in.EventTab.NewEventDetailPage.oragnization;
import static com.winningticketproject.in.EventTab.NewEventDetailPage.sponsor_logo;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.game_name;
import static com.winningticketproject.in.Shamble_flow.Shamble_Bestball_Map_GS_activity.hole_data;
import static com.winningticketproject.in.Shamble_flow.Shamble_Bestball_Map_GS_activity.par_data;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

public class View_course_map extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener
        , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener, LocationListener {

    SupportMapFragment mapFragment;
    TextView tvleave_game, hole_information, tv_total_distance, yards_to_back, tv_total_distance_center, yards_to_center, tv_total_distance_front, yards_to_front, tv_help, tv_left_holes_score, tv_right_holes_score;
    TextView next_click, preview_click , wther_info , wther_place_info ,temperture_text ,temperature_info ,Wind_txt ,Wind_info ,Precipitation , humidity ,cancle_btn ;
    Button btn_enter_score;
    ImageView tvWhether_info, tvalign_center;
    private GoogleApiClient mGoogleApiClient;
    Intent in;
    public static int count_list ;
    GPSTracker gps;
    private GoogleMap mMap;
    Location mLastLocation;
    String back_lat, back_lng, front_lat, front_lng, handica_values = "";
    int total_merter = 0, meters = 0, meterss = 0 , current_hole = 0, yards_1 = 0, yards_2 = 0, front_yards = 0, back_yards;
    String auth_token = "", event_id = "", hole_number = "", hole_par = "", hole_nume, hole_yards;
    private LocationManager locationManager;
    double current_latitude, current_longitudes;
    Marker mCurrLocationMarker;
    int image_pos;
    PolylineOptions options = null;
    ImageView img_totorial;
    RelativeLayout relative_layout ,relative_layout1;
    int[] tut_list = new int[]{R.drawable.tutoinst,R.drawable.tut0, R.drawable.tut01, R.drawable.tut02, R.drawable.tut04, R.drawable.tut05, R.drawable.tut06, R.drawable.tut07};

    String completed;
    AlertDialog alertDialog;
    ArrayList<LatLng> points = new ArrayList<>();
    HashMap<Integer, Double> latlist = new HashMap<Integer, Double>();
    HashMap<Integer, Double> longlist = new HashMap<Integer, Double>();
    List<Polyline> polylines = new ArrayList<>();
    MarkerOptions markerOptions;
    public static int  hole_number_from_all_corses = 1;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    float degrees;
    //Initialize to a non-valid zoom value
    private float previousZoomLevel = 10.0f;
    private boolean isZooming = false;

    LinearLayout btn_back;
    LinearLayout btn_next;

    private String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
    private String APPID = "8264d0d1e507766337835a8143449487";
    ImageView iv_super_ticket;
    ImageView iv_auction;
    ImageView iv_chat;
    ImageView iv_map_leader_board;
    ImageView iv_map_score_card;
    String home;

    boolean cross_scoring_enable ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course_map);

        Share_it share_it = new Share_it(View_course_map.this);

        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            home = extras.getString("home");
        }

        // GPS Enabled ot not
        gps = new GPSTracker(this);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        iv_auction = findViewById(R.id.iv_auction);
        iv_chat = findViewById(R.id.iv_chat);
        iv_super_ticket = findViewById(R.id.iv_super_ticket);
        iv_map_leader_board = findViewById(R.id.iv_map_leader_board);

        iv_map_score_card = findViewById(R.id.iv_map_score_card);
        iv_map_score_card.setOnClickListener(this);


        iv_map_leader_board.setOnClickListener(this);

        if (!Location_Services.game_name.equals("Course")) {
            iv_auction.setVisibility(View.VISIBLE);
            iv_chat.setVisibility(View.VISIBLE);
            iv_super_ticket.setVisibility(View.VISIBLE);
            iv_map_leader_board.setVisibility(View.VISIBLE);

            iv_auction.setOnClickListener(this);
            iv_chat.setOnClickListener(this);
            iv_super_ticket.setOnClickListener(this);
        }


        tv_total_distance = findViewById(R.id.tv_total_distance);
        tv_total_distance.setTypeface(semibold);

        yards_to_back = findViewById(R.id.yards_to_back);
        yards_to_back.setTypeface(semibold);

        tv_total_distance_center = findViewById(R.id.tv_total_distance_center);
        tv_total_distance_center.setTypeface(semibold);

        yards_to_center = findViewById(R.id.yards_to_center);
        yards_to_center.setTypeface(semibold);

        tv_total_distance_front = findViewById(R.id.tv_total_distance_front);
        tv_total_distance_front.setTypeface(semibold);

        yards_to_front = findViewById(R.id.yards_to_front);
        yards_to_front.setTypeface(semibold);

        tv_help = findViewById(R.id.tv_help);
        tv_help.setTypeface(regular);
        tv_help.setOnClickListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.view_course_map);

        // points array declaration
        points.clear();
        for (int i = 0; i < 5; i++) {
            points.add(new LatLng(0, 0));
        }

        // location manager initialization
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, (android.location.LocationListener) getApplicationContext()); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
            return;
        }

        boolean ranBefore = Boolean.parseBoolean(get_auth_token("RanBefore"));
        if (ranBefore==false) {
            tutorilas_method();
        }

        auth_token = get_auth_token("auth_token");
        event_id = get_auth_token("event_id");
        hole_number = get_auth_token("selected_hole_postion");

        current_hole = hole_number_from_all_corses;

        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    // for tutorials view
    private void tutorilas_method() {
        Share_it.savestring("RanBefore", "true");
        image_pos = 1;
        relative_layout = findViewById(R.id.relative_layout);
        img_totorial = findViewById(R.id.img_totorial);
        img_totorial.setImageResource(tut_list[0]);
        relative_layout.setVisibility(View.VISIBLE);
        img_totorial.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                if (image_pos<=tut_list.length-1){
                    img_totorial.setImageResource(tut_list[image_pos]);
                    image_pos++;
                }else {
                    relative_layout.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    private void method_for_load_top_section() {

        tvleave_game = findViewById(R.id.tvleave_game);
        tvleave_game.setTypeface(semibold);
        tvleave_game.setOnClickListener(this);

        tvWhether_info = findViewById(R.id.tvWhether_info);
        tvWhether_info.setOnClickListener(this);

        tvalign_center = findViewById(R.id.tvalign_center);
        tvalign_center.setOnClickListener(this);

        hole_information  = findViewById(R.id.hole_information);
        hole_information.setTypeface(regular);

    }

    private void method_for_load_bottom_section() {

        preview_click = findViewById(R.id.preview_click);
        next_click = findViewById(R.id.next_click);

        tv_left_holes_score = findViewById(R.id.tv_left_holes_score);
        tv_left_holes_score.setTypeface(regular);

        tv_right_holes_score = findViewById(R.id.tv_right_holes_score);
        tv_right_holes_score.setTypeface(regular);

        btn_enter_score = findViewById(R.id.btn_enter_score);
        btn_enter_score.setTypeface(regular);

        btn_enter_score.setOnClickListener(this);

    }

    @Override
    public void onStart() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        current_hole = hole_number_from_all_corses;

        System.out.println("---------------"+current_hole);
        method_to_call_api_for_update_hole_handicap();

        super.onStart();
    }

    private void method_to_call_api_for_update_hole_handicap() {
        ProgressDialog.getInstance().showProgress(this);
        String update_API="";
        JSONObject object = new JSONObject();

        try {
            object.put("handicap", get_auth_token("handicap"));
            object.put("selected_tee", get_auth_token("selected_tee_postion"));
            object.put("selected_hole", get_auth_token("selected_hole_postion"));
        } catch (Exception e) {
        }


        if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")) {
            update_API = "api/v2/hole_info/player/select-course-info/" + get_auth_token("event_id");
        } else if (get_auth_token("play_type").equals("event")) {
            update_API = "api/v2/hole_info/select-course-info/" + get_auth_token("event_id");
        } else if (get_auth_token("play_type").equals("individual")) {
            update_API = "api/v2/hole_info/player/select-course-info/" + get_auth_token("event_id");
        }

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.imageurl+update_API, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("------------"+response);
                            // cross scoring funcanality
                            completed = response.getString("completed");

                            cross_scoring_enable = response.getBoolean("cross_scoring");

                            JSONArray hole_info = response.getJSONArray("hole_info");
                            count_list = hole_info.length();

                            method_for_load_top_section();

                            method_for_load_bottom_section();

                            try {
                                if (response.getBoolean("cross_scoring")){
                                    if(response.getString("cross_score_team_name").equals("null") || response.getString("cross_score_team_name")=="null") {
                                        btn_enter_score.setVisibility(View.GONE);
                                    }
                                }
                            }catch (Exception e){ }

                            if (get_auth_token("CGB_event_code").equals(get_auth_token("current_event_code"))){
                                iv_chat.setVisibility(View.GONE);
                                iv_auction.setVisibility(View.GONE);
                                iv_super_ticket.setVisibility(View.GONE);
                            }

                            try {
                                String stating_hole = response.getString("starting_hole");
                                String com_hole = response.getString("completed_hole");

                                if (com_hole.equals(null) || com_hole.equals("null") || com_hole==null){
                                    if (stating_hole.equals(null) || stating_hole.equals("null") || stating_hole==null){
                                        current_hole = Integer.parseInt("1");
                                    }else {
                                        current_hole = Integer.parseInt(stating_hole);
                                    }
                                }else {
                                    current_hole = Integer.parseInt(com_hole)+1;
                                }

                            }catch (Exception e){
                                current_hole =current_hole-1;
                            }

                            hole_number_from_all_corses  = current_hole;

                            if (current_hole==1){
                                tv_left_holes_score.setText(Html.fromHtml("Hole<br><b><big>"+ count_list +"</big></b>"));
                                tv_right_holes_score.setText(Html.fromHtml("Hole<br><b><big>"+(current_hole+1)+"</big></b>"));
                            }else if (current_hole==count_list){
                                tv_left_holes_score.setText(Html.fromHtml("Hole<br><b><big>"+ (current_hole -1) +"</big></b>"));
                                tv_right_holes_score.setText(Html.fromHtml("Hole<br><b><big> 1"+"</big></b>"));
                            }else if (current_hole>=count_list){
                                tv_left_holes_score.setText(Html.fromHtml("Hole<br><b><big>"+ (current_hole -1) +"</big></b>"));
                                tv_right_holes_score.setText(Html.fromHtml("Hole<br><b><big> 2"+"</big></b>"));
                            }else {
                                tv_left_holes_score.setText(Html.fromHtml("Hole<br><b><big>"+ (current_hole -1) +"</big></b>"));
                                tv_right_holes_score.setText(Html.fromHtml("Hole<br><b><big>"+(current_hole+1)+"</big></b>"));
                            }

                            if (current_hole>count_list){
                                current_hole = 1;
                                methodtocalgetholepostion(current_hole+"",1);
                            }else {
                                methodtocalgetholepostion(current_hole+"",1);
                            }


                            btn_enter_score.setText(Html.fromHtml("<b>Enter Score</b><br> Hole "+current_hole));

                        } catch (Exception E) {
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
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(View_course_map.this);
                            } else {
                                Toast.makeText(View_course_map.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
        //nothing
    }

    @Override
    public void onResume() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {

        meters = 0;
        meterss = 0;
        mLastLocation = location;
        double new_lat = mLastLocation.getLatitude();
        double new_long = mLastLocation.getLongitude();

        RequestParams requestParams = new RequestParams();
        requestParams.put("lat", new_lat);
        requestParams.put("lon", new_long);
        requestParams.put("appid", APPID);
        networking(requestParams);


        double old_lat = latlist.get(0);
        double old_long = longlist.get(0);

        mMap.setOnCameraChangeListener(getCameraChangeListener());

        if (latlist.size() > 1) {

            ConnectivityManager cn = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = cn.getActiveNetworkInfo();
            if (nf != null && nf.isConnected()) {
                // lat1 and lng1 are the values of a previously stored location
                if (distance(old_lat, old_long, new_lat, new_long) > 5 || distance(old_lat, old_long, new_lat, new_long) < -5) { // if distance < 0.1 miles we take locations as equal
                    //do what you want to do...
                    mMap.clear();

                    latlist.put(0, mLastLocation.getLatitude());
                    longlist.put(0, mLastLocation.getLongitude());

                    points.set(0,new LatLng(latlist.get(0), longlist.get(0)));

                    update_polyline_with_current_location();

                }
            }
        }
    }

    private void update_polyline_with_current_location(){

//        mMap.clear();
        markerOptions = new MarkerOptions();

        for(Polyline line : polylines)
        {
            line.remove();
        }
        polylines.clear();

        for (int i =  0; i<latlist.size();i++){
            int background = R.drawable.center_with_text;
            if (i==0){
                mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromResource(R.drawable.start_point)).title(i+"").anchor(0.5f,0.5f).draggable(false).rotation(degrees));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(i), longlist.get(i)), previousZoomLevel));
                points.set(0,new LatLng(latlist.get(i), longlist.get(i)));
            }else if(i==2){
                mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromResource(R.drawable.center_point)).title(i+"").anchor(0.5f,0.5f).draggable(true));
                points.set(2,new LatLng(latlist.get(i), longlist.get(i)));
            }else if (i==4){
                mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromResource(R.drawable.end_point)).title(i+"").anchor(0.5f,0.5f).draggable(true));
                points.set(4,new LatLng(latlist.get(i), longlist.get(i)));
            }else if (i==1){
                meters = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),latlist.get(2),longlist.get(2)));
                yards_1 = (int) Math.round(meters*1.0936);
                Bitmap icons = drawTextToBitmap(background,String.valueOf(yards_1));
                mCurrLocationMarker =  mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromBitmap(icons)).title(i+"").anchor(0.5f,0.5f).draggable(false));
                points.set(1,new LatLng(latlist.get(i), longlist.get(i)));
            }else if (i==3){
                meterss = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),latlist.get(4),longlist.get(4)));
                yards_2 = (int) Math.round(meterss*1.0936);
                Bitmap icons = drawTextToBitmap(background,String.valueOf(yards_2));
                mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromBitmap(icons)).title(i+"").anchor(0.5f,0.5f).draggable(false));
                points.set(3,new LatLng(latlist.get(i), longlist.get(i)));
            }
        }

        yards_update();

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        options = new PolylineOptions().width(7).color(Color.WHITE).geodesic(true);
        options.addAll(points);
        polylines.add(mMap.addPolyline(options));

    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        double yards =(dist*1760);

        System.out.println(yards+"-------------");
        return yards; // output distance, in yards
    }


    private void yards_update() {
        //front calculation
        int center_for_three = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),latlist.get(2),longlist.get(2)));

        int front_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),Double.parseDouble(front_lat),Double.parseDouble(front_lng)));
        int front_yards = (int) Math.round((center_for_three+front_meters)*1.0936);
        tv_total_distance_front.setText(front_yards+"");

        //back calculation
        int back_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),Double.parseDouble(back_lat),Double.parseDouble(back_lng)));
        int back_yards = (int) Math.round((center_for_three+back_meters)*1.0936);
        tv_total_distance.setText(back_yards+"");

        // center calculation
        int center_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),latlist.get(4),longlist.get(4)));
        int center_yards = (int) Math.round((center_for_three+center_meters)*1.0936);
        tv_total_distance_center.setText(center_yards+"");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setPadding(10, 150, 10, 0);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        //        mMap.setMyLocationEnabled(true);

        // check location enabled or not
        if (Location_Services.isLocationEnabled(getApplicationContext())){
            current_latitude = gps.getLatitude();
            current_longitudes = gps.getLongitude();
            latlist.put(0, current_latitude);
            longlist.put(0, current_longitudes);
            googleMap.getUiSettings().setCompassEnabled(true);

        }

        mMap.setOnCameraChangeListener(getCameraChangeListener());

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setOnMarkerDragListener(this);

    }

    public GoogleMap.OnCameraChangeListener getCameraChangeListener()
    {
        return new GoogleMap.OnCameraChangeListener()
        {
            @Override
            public void onCameraChange(CameraPosition position)
            {

                if(previousZoomLevel != position.zoom)
                {
                    isZooming = true;
                }
                previousZoomLevel = position.zoom;
            }
        };
    }

    private void methodtocalgetholepostion(final String hole_postion,int type) {

        if (type==2){
            ProgressDialog.getInstance().showProgress(View_course_map.this);
        }

        try {
            String url_value="";
            if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")){
                url_value = "hole_info/player/course_map/"+event_id+"/"+hole_postion;
            }else if (get_auth_token("play_type").equals("event")){
                url_value = "hole_info/course_map/"+event_id+"/"+hole_postion;
            }else if (get_auth_token("play_type").equals("individual")) {
                url_value = "hole_info/course_map/"+event_id+"/"+hole_postion;
            }

            JsonObjectRequest myRequest_handicap = new JsonObjectRequest(Request.Method.POST, Splash_screen.url + url_value, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("--------response---"+response);
                            try {

                                JSONObject hole = response.getJSONObject("hole");
                                hole_nume = hole.getString("hole_num");
                                double hole_lat=0,hole_lng=0;
                                String lat_string = hole.getString("lat");
                                String long_string = hole.getString("lng");

                                back_lat = hole.getString("back_lat");
                                back_lng = hole.getString("back_lng");

                                front_lat = hole.getString("front_lat");
                                front_lng = hole.getString("front_lng");

                                handica_values = hole.getString("hcp");


                                if (lat_string.equals("null") || lat_string.equals("") || lat_string.equals(null)){
                                    preview_click.setVisibility(View.VISIBLE);
                                    next_click.setVisibility(View.VISIBLE);
                                }else {

                                    preview_click.setVisibility(View.VISIBLE);
                                    next_click.setVisibility(View.VISIBLE);

                                    hole_lat = Double.parseDouble(lat_string);
                                    hole_lng = Double.parseDouble(long_string);

                                    hole_par = hole.getString("par");
                                    hole_yards = hole.getString("yards");

                                    if (hole_par.equals("null") || hole_par.equals("") || hole_par.equals(null)){
                                        hole_nume="";
                                    }

                                    if (hole_yards.equals("null") || hole_yards.equals("") || hole_yards.equals(null)){
                                        hole_yards="";
                                    }

                                    hole_information.setText(Html.fromHtml("<big><b>Hole "+hole_nume+"</b> </big><b><br>"+get_auth_token("Event_name")+"<br>Par "+hole_par+" - "+hole_yards +" yards - Handicap "+handica_values +"</b>"));


                                    // putt hole latlong to 4th position
                                    latlist.put(4,hole_lat);
                                    longlist.put(4,hole_lng);

                                    // calculate center point and putt in 2nd position
                                    double CenterLat = (latlist.get(0) + latlist.get(4)) / 2;
                                    double CenterLon = (longlist.get(0) + longlist.get(4) ) / 2;
                                    latlist.put(2,CenterLat);
                                    longlist.put(2,CenterLon);

                                    // calculate 1st position
                                    double CenterLat_first = (latlist.get(0) + latlist.get(2)) / 2;
                                    double CenterLon_second = (longlist.get(0) + longlist.get(2) ) / 2;
                                    latlist.put(1,CenterLat_first);
                                    longlist.put(1,CenterLon_second);

                                    // calculate 3rd position
                                    double CenterLat_third = (latlist.get(2) + latlist.get(4)) / 2;
                                    double CenterLon_third = (longlist.get(2) + longlist.get(4) ) / 2;
                                    latlist.put(3,CenterLat_third);
                                    longlist.put(3,CenterLon_third);

                                    ProgressDialog.getInstance().hideProgress();

                                    updateUI();

                                }

                                ProgressDialog.getInstance().hideProgress();
                            }catch (Exception e){
                                //nothing
                                ProgressDialog.getInstance().hideProgress();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ProgressDialog.getInstance().hideProgress();
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                // HTTP Status Code: 401 Unauthorized
                                if (error.networkResponse.statusCode == 401) {
                                    Alert_Dailog.showNetworkAlert(View_course_map.this);
                                } else {
                                    Toast.makeText(View_course_map.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(View_course_map.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
            myRequest_handicap.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(myRequest_handicap, "tag");

        } catch (Exception e) {
            //nothing
        }

    }
    private float getDistancBetweenTwoPoints(double lat1,double lon1,double lat2,double lon2) {
        float[] distance = new float[2];
        Location.distanceBetween( lat1, lon1, lat2, lon2, distance);
        return distance[0];
    }

    public Bitmap drawTextToBitmap(int gResId, String gText) {
        Resources resources = getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);
        Bitmap.Config bitmapConfig = bitmap.getConfig();
        if ( bitmapConfig == null ) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        /* SET FONT COLOR (e.g. WHITE -> rgb(255,255,255)) */
        paint.setColor(Color.rgb(0, 0, 0));
        /* SET FONT SIZE (e.g. 15) */
        paint.setTextSize((int) (15 * scale));
        /* SET SHADOW WIDTH, POSITION AND COLOR (e.g. BLACK) */
        paint.setShadowLayer(1f, 0f, 1f, Color.BLACK);
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;
        canvas.drawText(gText, x, y, paint);
        return bitmap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    @Override
    public void onMarkerDragStart(Marker marker) {
    }
    @Override
    public void onMarkerDrag(Marker marker) {

        meterss=0;
        meters=0;

        String marker_title = marker.getTitle();

        method_to_draging_marker(marker_title,marker);

    }

    private void method_to_draging_marker(String marker_title, Marker marker) {

        if (marker_title.equals("0")){

            latlist.put(0,marker.getPosition().latitude);
            longlist.put(0,marker.getPosition().longitude);

        }else if (marker_title.equals("2")){

            latlist.put(2,marker.getPosition().latitude);
            longlist.put(2,marker.getPosition().longitude);

            double CenterLat_first = (latlist.get(0) + latlist.get(2)) / 2;
            double CenterLon_second = (longlist.get(0) + longlist.get(2) ) / 2;

            latlist.put(1,CenterLat_first);
            longlist.put(1,CenterLon_second);

            double CenterLat_third = (latlist.get(2) + latlist.get(4)) / 2;
            double CenterLon_third = (longlist.get(2) + longlist.get(4) ) / 2;

            latlist.put(3,CenterLat_third);
            longlist.put(3,CenterLon_third);

            meterss = Math.round(getDistancBetweenTwoPoints(latlist.get(0), longlist.get(0), latlist.get(2), longlist.get(2)));
            meters = Math.round(getDistancBetweenTwoPoints(latlist.get(2), longlist.get(2), latlist.get(4), longlist.get(4)));

            total_merter = (int) ((meterss*1.0936)+(meters*1.0936));

        }else if (marker_title.equals("4")){
            latlist.put(4,marker.getPosition().latitude);
            longlist.put(4,marker.getPosition().longitude);

            double CenterLat_third = (latlist.get(2) + latlist.get(4)) / 2;
            double CenterLon_third = (longlist.get(2) + longlist.get(4) ) / 2;

            latlist.put(3,CenterLat_third);
            longlist.put(3,CenterLon_third);

            meterss = Math.round(getDistancBetweenTwoPoints(latlist.get(2), longlist.get(2), latlist.get(4), longlist.get(4)));
            meters = Math.round(getDistancBetweenTwoPoints(latlist.get(0), longlist.get(0), latlist.get(2), longlist.get(2)));
            total_merter = (int) ((meterss*1.0936)+(meters*1.0936));

        }

        //front calculation
        int center_for_three = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),latlist.get(2),longlist.get(2)));

        int front_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),Double.parseDouble(front_lat),Double.parseDouble(front_lng)));
        int front_yards = (int) Math.round((center_for_three+front_meters)*1.0936);
        tv_total_distance_front.setText(front_yards+"");

        //back calculation
        int back_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),Double.parseDouble(back_lat),Double.parseDouble(back_lng)));
        int back_yards = (int) Math.round((center_for_three+back_meters)*1.0936);
        tv_total_distance.setText(back_yards+"");

        // center calculation
        int center_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),latlist.get(4),longlist.get(4)));
        int center_yards = (int) Math.round((center_for_three+center_meters)*1.0936);
        tv_total_distance_center.setText(center_yards+"");

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
//        points.clear();
        mMap.clear();

        for(Polyline line : polylines)
        {
            line.remove();
        }

        polylines.clear();

        meterss=0;
        meters=0;

        String marker_title = marker.getTitle();

        method_to_drag_marker(marker_title,marker);

        System.out.println(marker.getPosition().latitude+"------lat long-----"+marker.getPosition().longitude);

    }

    private void method_to_drag_marker(String marker_title,Marker marker){

        if (marker_title.equals("0")){

            latlist.put(0,marker.getPosition().latitude);
            longlist.put(0,marker.getPosition().longitude);

        }else if (marker_title.equals("2")){

            latlist.put(2,marker.getPosition().latitude);
            longlist.put(2,marker.getPosition().longitude);


            double CenterLat_first = (latlist.get(0) + latlist.get(2)) / 2;
            double CenterLon_second = (longlist.get(0) + longlist.get(2) ) / 2;

            latlist.put(1,CenterLat_first);
            longlist.put(1,CenterLon_second);

            double CenterLat_third = (latlist.get(2) + latlist.get(4)) / 2;
            double CenterLon_third = (longlist.get(2) + longlist.get(4) ) / 2;

            latlist.put(3,CenterLat_third);
            longlist.put(3,CenterLon_third);

            meterss = Math.round(getDistancBetweenTwoPoints(latlist.get(0), longlist.get(0), latlist.get(2), longlist.get(2)));
            meters = Math.round(getDistancBetweenTwoPoints(latlist.get(2), longlist.get(2), latlist.get(4), longlist.get(4)));

            total_merter = (int) ((meterss*1.0936)+(meters*1.0936));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(2), longlist.get(2)), previousZoomLevel));
            points.set(2,new LatLng(latlist.get(2), longlist.get(2)));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(2), longlist.get(2)), previousZoomLevel);
            mMap.animateCamera(cameraUpdate);

        }else if (marker_title.equals("4")){
            latlist.put(4,marker.getPosition().latitude);
            longlist.put(4,marker.getPosition().longitude);

            double CenterLat_third = (latlist.get(2) + latlist.get(4)) / 2;
            double CenterLon_third = (longlist.get(2) + longlist.get(4) ) / 2;

            latlist.put(3,CenterLat_third);
            longlist.put(3,CenterLon_third);

            meterss = Math.round(getDistancBetweenTwoPoints(latlist.get(2), longlist.get(2), latlist.get(4), longlist.get(4)));
            meters = Math.round(getDistancBetweenTwoPoints(latlist.get(0), longlist.get(0), latlist.get(2), longlist.get(2)));
            total_merter = (int) ((meterss*1.0936)+(meters*1.0936));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(4), longlist.get(4)), previousZoomLevel));
            points.set(4,new LatLng(latlist.get(4), longlist.get(4)));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(4), longlist.get(4)), previousZoomLevel);
            mMap.animateCamera(cameraUpdate);
        }


        mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(0), longlist.get(0))).icon(BitmapDescriptorFactory.fromResource(R.drawable.start_point)).title(0+"").anchor(0.5f,0.5f).draggable(false).rotation(degrees));
        mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(2), longlist.get(2))).icon(BitmapDescriptorFactory.fromResource(R.drawable.center_point)).title(2+"").anchor(0.5f,0.5f).draggable(true));
        mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(4), longlist.get(4))).icon(BitmapDescriptorFactory.fromResource(R.drawable.end_point)).title(4+"").anchor(0.5f,0.5f).draggable(true));

        int background = R.drawable.center_with_text;
        int meters1 = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),latlist.get(2),longlist.get(2)));
        int yards_11 = (int) Math.round(meters1*1.0936);
        Bitmap icons = drawTextToBitmap(background,String.valueOf(yards_11));
        mCurrLocationMarker =  mMap.addMarker(markerOptions.position(new LatLng(latlist.get(1), longlist.get(1))).icon(BitmapDescriptorFactory.fromBitmap(icons)).title(1+"").anchor(0.5f,0.5f).draggable(false));
        points.set(1,new LatLng(latlist.get(1), longlist.get(1)));


        int meterss2 = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),latlist.get(4),longlist.get(4)));
        int yards_22 = (int) Math.round(meterss2*1.0936);
        Bitmap iconss = drawTextToBitmap(background,String.valueOf(yards_22));

        mCurrLocationMarker =  mMap.addMarker(markerOptions.position(new LatLng(latlist.get(3), longlist.get(3))).icon(BitmapDescriptorFactory.fromBitmap(iconss)).title(3+"").anchor(0.5f,0.5f).draggable(false));
        points.set(3,new LatLng(latlist.get(3), longlist.get(3)));


        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        options = new PolylineOptions().width(7).color(Color.WHITE).geodesic(true);
        options.addAll(points);
        polylines.add(this.mMap.addPolyline(options));

        //front calculation
        int center_for_three = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),latlist.get(2),longlist.get(2)));

        int front_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),Double.parseDouble(front_lat),Double.parseDouble(front_lng)));
        int front_yards = (int) Math.round((center_for_three+front_meters)*1.0936);
        tv_total_distance_front.setText(front_yards+"");

        //back calculation
        int back_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),Double.parseDouble(back_lat),Double.parseDouble(back_lng)));
        int back_yards = (int) Math.round((center_for_three+back_meters)*1.0936);
        tv_total_distance.setText(back_yards+"");

        // center calculation
        int center_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),latlist.get(4),longlist.get(4)));
        int center_yards = (int) Math.round((center_for_three+center_meters)*1.0936);
        tv_total_distance_center.setText(center_yards+"");

    }

    public void updateUI(){
        markerOptions = new MarkerOptions();
        mMap.clear();
        //map boundry fit in screen
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i =  0; i<latlist.size();i++){
            int background = R.drawable.center_with_text;
            if (i==0){
                mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromResource(R.drawable.start_point)).title(i+"").anchor(0.5f,0.5f).draggable(false).rotation(degrees));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latlist.get(i), longlist.get(i))));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(i), longlist.get(i)), 0));
                points.set(0,new LatLng(latlist.get(i), longlist.get(i)));
            }else if(i==2){
                mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromResource(R.drawable.center_point)).title(i+"").anchor(0.5f,0.5f).draggable(true));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(i), longlist.get(i)), previousZoomLevel));
                points.set(2,new LatLng(latlist.get(i), longlist.get(i)));
            }else if (i==4){
                mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromResource(R.drawable.end_point)).title(i+"").anchor(0.5f,0.5f).draggable(true));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(i), longlist.get(i)), previousZoomLevel));
                points.set(4,new LatLng(latlist.get(i), longlist.get(i)));
            }else if (i==1){
                meters = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),latlist.get(2),longlist.get(2)));
                yards_1 = (int) Math.round(meters*1.0936);
                Bitmap icons = drawTextToBitmap(background,String.valueOf(yards_1));
                mCurrLocationMarker =  mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromBitmap(icons)).title(i+"").anchor(0.5f,0.5f).draggable(false));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(i), longlist.get(i)), 0));
                points.set(1,new LatLng(latlist.get(i), longlist.get(i)));
            }else if (i==3){
                meterss = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),latlist.get(4),longlist.get(4)));
                yards_2 = (int) Math.round(meterss*1.0936);
                Bitmap icons = drawTextToBitmap(background,String.valueOf(yards_2));
                mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromBitmap(icons)).title(i+"").anchor(0.5f,0.5f).draggable(false));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(i), longlist.get(i)), 0));
                points.set(3,new LatLng(latlist.get(i), longlist.get(i)));
            }

            builder.include(markerOptions.getPosition());

        }

        LatLngBounds bounds = builder.build();

        int padding = 250; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.moveCamera(cu);
        mMap.animateCamera(cu);

        int front_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),Double.parseDouble(front_lat),Double.parseDouble(front_lng)));
        front_yards = (int) Math.round(front_meters*1.0936);
        tv_total_distance_front.setText(front_yards+"");


        int back_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),Double.parseDouble(back_lat),Double.parseDouble(back_lng)));
        back_yards = (int) Math.round(back_meters*1.0936);
        tv_total_distance.setText(back_yards+"");


        int center_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),latlist.get(4),longlist.get(4)));
        int center_yards = (int) Math.round(center_meters*1.0936);
        tv_total_distance_center.setText(center_yards+"");

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        options = new PolylineOptions().width(7).color(Color.WHITE).geodesic(true);
        options.addAll(points);
        polylines.add(mMap.addPolyline(options));

        method_for_load_top_section();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvleave_game :
                pop_up_open();
                break;

            case R.id.tvalign_center :
                updateUI_to_Center();
                break;

            case R.id.tvWhether_info :

                tvleave_game.setEnabled(false);
                tvalign_center.setEnabled(false);
                tv_help.setEnabled(false);
                iv_super_ticket.setEnabled(false);
                iv_auction.setEnabled(false);
                iv_chat.setEnabled(false);
                btn_next.setEnabled(false);
                btn_back.setEnabled(false);
                btn_next.setEnabled(false);
                btn_enter_score.setEnabled(false);

                relative_layout1 = findViewById(R.id.relative_layout1);
                relative_layout1.setVisibility(View.VISIBLE);
                method_to_call_whether_info();
                break;

            case R.id.tv_help :
                tutorilas_method();
                break;

            case R.id.iv_super_ticket:
                Super_ticket_poup();
                break;

            case R.id.iv_map_leader_board:
                if (get_auth_token("Game_type").equals("shamble") || get_auth_token("Game_type").equals("bestball")) {
                    Intent intent1 = new Intent(View_course_map.this, Bestbal_Leader_Board.class);
                    intent1.putExtra("home","");
                    startActivity(intent1);
                } else if (get_auth_token("Game_type").equals("stableford")) {
                    Intent intent1 = new Intent(View_course_map.this, Stable_ford_Leaderboard.class);
                    intent1.putExtra("home","");
                    startActivity(intent1);
                } else if (get_auth_token("Game_type").equals("modifiedstableford")) {
                    Intent intent1 = new Intent(View_course_map.this, Stable_modify_leader_board.class);
                    intent1.putExtra("home","");
                    startActivity(intent1);
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), NormalLeaderoard.class);
                    intent1.putExtra("home","");
                    startActivity(intent1);
                }
                break;

            case R.id.iv_auction:
                in=new Intent(this, NewLive_Auction.class);
                savestring("id",event_id);
                startActivity(in);
                break;

            case R.id.iv_chat:
                in=new Intent(this, User_List.class);
                startActivity(in);
                break;


            case R.id.btn_next:

                current_hole = current_hole+1;

                if (current_hole==count_list){
                    methodtocalgetholepostion(count_list + "",2);
                    tv_left_holes_score.setText(Html.fromHtml("Hole<br><b><big>" +(count_list-1)+ "</big></b>"));
                    tv_right_holes_score.setText(Html.fromHtml("Hole<br><b> <big> 1"+"</big></b>"));
                    current_hole=count_list;
                    btn_enter_score.setText(Html.fromHtml("<b>Enter Score</b><br> Hole "+count_list));

                } else if (current_hole>count_list){
                    methodtocalgetholepostion("1" + "",2);
                    tv_left_holes_score.setText(Html.fromHtml("Hole<br><b><big>" +(count_list)+ "</big></b>"));
                    tv_right_holes_score.setText(Html.fromHtml("Hole<br><b> <big> 2"+"</big></b>"));
                    current_hole=1;
                    btn_enter_score.setText(Html.fromHtml("<b>Enter Score</b><br> Hole 1"));

                }else {
                    if (current_hole < 1) {
                        current_hole = 1;
                        methodtocalgetholepostion(current_hole + "",2);
                        tv_left_holes_score.setText(Html.fromHtml("Hole<br><b><big> 1" + "</big></b>"));
                        tv_right_holes_score.setText(Html.fromHtml("Hole<br><b><big> " + (current_hole + 1) + "</big></b>"));
                    } else {
                        methodtocalgetholepostion(current_hole + "",2);
                        if (current_hole == 1) {
                            tv_left_holes_score.setText(Html.fromHtml("Hole<br><b> <big>" + (current_hole) + "</big></b>"));
                            tv_right_holes_score.setText(Html.fromHtml("Hole<br><b><big> " + (count_list) + "</big></b>"));
                        } else {
                            tv_left_holes_score.setText(Html.fromHtml("Hole<br><b><big> " + (current_hole - 1) + "</big></b>"));
                            tv_right_holes_score.setText(Html.fromHtml("Hole<br><b><big> " + (current_hole + 1) + "</big></b>"));
                        }
                    }
                    btn_enter_score.setText(Html.fromHtml("<b>Enter Score</b><br> Hole "+current_hole));
                }

                break;

            case R.id.btn_back:

                current_hole = current_hole-1;
                if (current_hole<1){
                    current_hole = count_list;
                    methodtocalgetholepostion(current_hole + "",2);
                    tv_left_holes_score.setText(Html.fromHtml("Hole<br><b> <big>"+(count_list-1)+"</big></b>"));
                    tv_right_holes_score.setText(Html.fromHtml("Hole<br><b> <big> 1"+"</big></b>"));
                    btn_enter_score.setText(Html.fromHtml("<b>Enter Score</b><br> Hole "+current_hole));
                }else {
                    methodtocalgetholepostion(current_hole + "",2);
                    if (current_hole==1){
                        tv_left_holes_score.setText(Html.fromHtml("Hole<br><b><big> "+(count_list)+"</big></b>"));
                        tv_right_holes_score.setText(Html.fromHtml("Hole<br><b><big> "+(current_hole+1)+"</big></b>"));
                    }else {
                        tv_left_holes_score.setText(Html.fromHtml("Hole<br><b><big>"+(current_hole-1)+"</big></b>"));
                        tv_right_holes_score.setText(Html.fromHtml("Hole<br><b><big> "+(current_hole+1)+"</big></b>"));
                    }
                    btn_enter_score.setText(Html.fromHtml("<b>Enter Score</b><br> Hole "+current_hole));
                }

                break;

            case R.id.btn_enter_score :
                if(game_name.equals("shamble") || game_name.equals("bestball")){
                    hole_data = hole_nume;
                    par_data = hole_par;
                    in = new Intent(View_course_map.this, Shamble_Bestball_Map_GS_activity.class);
                    startActivity(in);
                }else{
                    in = new Intent(View_course_map.this, Grass_score_Update_from_Map.class);
                    Grass_score_Update_from_Map.hole_data = hole_nume;
                    Grass_score_Update_from_Map.par_data_gr = hole_par;
                    startActivity(in);
                }
                break;

            case R.id.iv_map_score_card:
                if (get_auth_token("Game_type").equals("shamble") || get_auth_token("Game_type").equals("bestball")) {
                    Intent intent1 = new Intent(View_course_map.this, BestBall_Scorecard.class);
                    intent1.putExtra("home","");
                    startActivity(intent1);
                } else if (get_auth_token("Game_type").equals("stableford")) {
                    Intent intent1 = new Intent(View_course_map.this, StableFord_Score_Card.class);
                    intent1.putExtra("home","");
                    startActivity(intent1);
                } else if (get_auth_token("Game_type").equals("modifiedstableford")) {
                    Intent intent1 = new Intent(View_course_map.this, Stable_modify_ScoreCard.class);
                    intent1.putExtra("home","");
                    startActivity(intent1);
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), NormalScorreboard.class);
                    intent1.putExtra("home","");
                    startActivity(intent1);
                }

                break;
        }
    }

    public void Super_ticket_poup() {
        android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogview = inflater.inflate(R.layout.pop_up_super_ticket, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;

        TextView tv_super_content = alertDialog.findViewById(R.id.pop_super_ticket);
        tv_super_content.setTypeface(medium);

        TextView pop_super_content = alertDialog.findViewById(R.id.pop_super_content);
        pop_super_content.setTypeface(medium);


        TextView super_ticket_values = alertDialog.findViewById(R.id.pop_super_ticket_values);
        super_ticket_values.setTypeface(regular);

        super_ticket_values.setText(get_auth_token("live_aminties"));

        TextView tv_super_ticket_content = alertDialog.findViewById(R.id.pop_super_ticket_content);
        tv_super_ticket_content.setTypeface(regular);

        String locations = oragnization.substring(0,1).toUpperCase() +oragnization.substring(1);
        tv_super_ticket_content.setText("All super ticket proceeds go to "+locations+" Thank you for your generous donation.");


        TextView tv_sponsor_title = alertDialog.findViewById(R.id.pop_sponsor_title);
        tv_sponsor_title.setTypeface(medium);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recler_sponsor = alertDialog.findViewById(R.id.pop_recler_sponsor);
        recler_sponsor.setLayoutManager(layoutManager2);

        NewEventDetailPage.SponsorLogo allevents_hori = new NewEventDetailPage.SponsorLogo(this, sponsor_logo);
        recler_sponsor.setAdapter(allevents_hori);

        TextView empty_sponsor = alertDialog.findViewById(R.id.pop_empty_sponsor);
        empty_sponsor.setTypeface(regular);

        if (sponsor_logo.size()>0){
            recler_sponsor.setVisibility(View.VISIBLE);
        }else {
            empty_sponsor.setVisibility(View.VISIBLE);
        }

        ImageView img_cross = alertDialog.findViewById(R.id.img_cross);
        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    public void method_to_call_whether_info(){
         wther_info = findViewById(R.id.wther_info);
         wther_place_info = findViewById(R.id.wther_place_info);
         temperture_text = findViewById(R.id.temperture_text);
         temperature_info = findViewById(R.id.temperature_info);
         Wind_txt = findViewById(R.id.Wind_txt);
         Wind_info = findViewById(R.id.Wind_info);
         Precipitation = findViewById(R.id.precipitation);
         humidity = findViewById(R.id.humidity);
         cancle_btn = findViewById(R.id.cancle_btn);

        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relative_layout1.setVisibility(View.GONE);

                tvleave_game.setEnabled(true);
                tvalign_center.setEnabled(true);
                tv_help.setEnabled(true);
                iv_super_ticket.setEnabled(true);
                iv_auction.setEnabled(true);
                iv_chat.setEnabled(true);
                btn_next.setEnabled(true);
                btn_back.setEnabled(true);
                btn_next.setEnabled(true);
                btn_enter_score.setEnabled(true);

            }
        });

    }

    public void pop_up_open() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogview = inflater.inflate(R.layout.pop_up_finish_game, null);
        final AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;

        Button btn_reminder_countinue = alertDialog.findViewById(R.id.btn_reminder_countinue);
        btn_reminder_countinue.setTypeface(regular);

        ImageView img_cross = alertDialog.findViewById(R.id.img_cross);

        TextView location_services = alertDialog.findViewById(R.id.location_services);
        TextView nearby_courses = alertDialog.findViewById(R.id.nearby_courses);
        location_services.setTypeface(semibold);
        nearby_courses.setTypeface(medium);

        Button enable= alertDialog.findViewById(R.id.enable);
        enable.setTypeface(medium);

        Button btn_be_right_back = alertDialog.findViewById(R.id.btn_be_right_back);
        btn_be_right_back.setTypeface(medium);

        if (Location_Services.game_name.equals("Course")){
            if (completed.equals("Yes")) {
                btn_be_right_back.setVisibility(View.GONE);
                nearby_courses.setText(Html.fromHtml("Click " + "&ldquo;" + "Finish Game" + "&rdquo;" + " to compare your \nscores on our leaderboard "));
                location_services.setText(Html.fromHtml("You've completed your game!"));
            } else {
                btn_be_right_back.setVisibility(View.VISIBLE);
                enable.setVisibility(View.GONE);
                nearby_courses.setVisibility(View.GONE);
                location_services.setText(Html.fromHtml("You haven't completed your \n game yet"));
                }
        }else {
            if (completed.equals("Yes")) {

                if (cross_scoring_enable){
                    nearby_courses.setText(Html.fromHtml("Confirm all scores with <br>your opponent"));
                    location_services.setText(Html.fromHtml("You've completed your round"));
                    enable.setVisibility(View.VISIBLE);
                }else {
                    nearby_courses.setVisibility(View.GONE);
                    location_services.setText(Html.fromHtml("You've completed your round"));
                    enable.setVisibility(View.VISIBLE);
                }

              } else {
                if (cross_scoring_enable){
                    nearby_courses.setVisibility(View.GONE);
                    location_services.setText(Html.fromHtml("You haven't completed your \n round yet"));
                }else {
                    nearby_courses.setVisibility(View.GONE);
                    location_services.setText(Html.fromHtml("You haven't completed your \n game yet"));
                }
            }

        }

        btn_be_right_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

        btn_reminder_countinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share_it.remove_key("first_time");
                alertDialog.dismiss();
                finish();
            }
        });

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share_it.remove_key("first_time");
                alertDialog.dismiss();
                finish();
            }
        });

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    private void updateUI_to_Center() {
        markerOptions = new MarkerOptions();
        mMap.clear();

        if (latlist.size()>1){
            double CenterLat = (latlist.get(0) + latlist.get(4)) / 2;
            double CenterLon = (longlist.get(0) + longlist.get(4) ) / 2;
            latlist.put(2,CenterLat);
            longlist.put(2,CenterLon);
        }

        double CenterLat_first = (latlist.get(0) + latlist.get(2)) / 2;
        double CenterLon_second = (longlist.get(0) + longlist.get(2) ) / 2;
        latlist.put(1,CenterLat_first);
        longlist.put(1,CenterLon_second);

        double CenterLat_third = (latlist.get(2) + latlist.get(4)) / 2;
        double CenterLon_third = (longlist.get(2) + longlist.get(4) ) / 2;
        latlist.put(3,CenterLat_third);
        longlist.put(3,CenterLon_third);


        for (int i =  0; i<latlist.size();i++){
            int background = R.drawable.center_with_text;
            if (i==0){
                mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromResource(R.drawable.start_point)).title(i+"").anchor(0.5f,0.5f).draggable(false).rotation(degrees));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(i), longlist.get(i)), previousZoomLevel));
                points.set(0,new LatLng(latlist.get(i), longlist.get(i)));
            }else if(i==2){
                mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromResource(R.drawable.center_point)).title(i+"").anchor(0.5f,0.5f).draggable(true));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(i), longlist.get(i)), previousZoomLevel));
                points.set(2,new LatLng(latlist.get(i), longlist.get(i)));
            }else if (i==4){
                mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromResource(R.drawable.end_point)).title(i+"").anchor(0.5f,0.5f).draggable(true));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(i), longlist.get(i)), previousZoomLevel));
                points.set(4,new LatLng(latlist.get(i), longlist.get(i)));
            }else if (i==1){
                meters = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),latlist.get(2),longlist.get(2)));
                yards_1 = (int) Math.round(meters*1.0936);
                Bitmap icons = drawTextToBitmap(background,String.valueOf(yards_1));
                mCurrLocationMarker =  mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromBitmap(icons)).title(i+"").anchor(0.5f,0.5f).draggable(false));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(i), longlist.get(i)), previousZoomLevel));
                points.set(1,new LatLng(latlist.get(i), longlist.get(i)));
            }else if (i==3){
                meterss = Math.round(getDistancBetweenTwoPoints(latlist.get(2),longlist.get(2),latlist.get(4),longlist.get(4)));
                yards_2 = (int) Math.round(meterss*1.0936);
                Bitmap icons = drawTextToBitmap(background,String.valueOf(yards_2));
                mCurrLocationMarker = mMap.addMarker(markerOptions.position(new LatLng(latlist.get(i), longlist.get(i))).icon(BitmapDescriptorFactory.fromBitmap(icons)).title(i+"").anchor(0.5f,0.5f).draggable(false));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(i), longlist.get(i)), previousZoomLevel));
                points.set(3,new LatLng(latlist.get(i), longlist.get(i)));

            }
        }

        int front_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),Double.parseDouble(front_lat),Double.parseDouble(front_lng)));
        front_yards = (int) Math.round(front_meters*1.0936);
        tv_total_distance_front.setText(front_yards+"");

        int back_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),Double.parseDouble(back_lat),Double.parseDouble(back_lng)));
        back_yards = (int) Math.round(back_meters*1.0936);
        tv_total_distance.setText(back_yards+"");

        int center_meters = Math.round(getDistancBetweenTwoPoints(latlist.get(0),longlist.get(0),latlist.get(4),longlist.get(4)));
        int center_yards = (int) Math.round(center_meters*1.0936);
        tv_total_distance_center.setText(center_yards+"");

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        options = new PolylineOptions().width(7).color(Color.WHITE).geodesic(true);
        options.addAll(points);
        polylines.add(this.mMap.addPolyline(options));
    }


    private void networking(RequestParams requestParams) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(BASE_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                try {

                    WeatherDataModel weatherDataModel = WeatherDataModel.fromJson(response);

                    String tem = weatherDataModel.mTemperature;

                    int temperature = ((Integer.parseInt(tem) * 9/5) + 32);

                    Double humidity1 = weatherDataModel.humidity;

                    String humidy = humidity1.toString();

                    int wind_data = Math.round(Float.parseFloat(weatherDataModel.wind_speed));

                    method_to_call_whether_info();
                    wther_place_info.setText(weatherDataModel.getmCity() + "," + weatherDataModel.country);
                    humidity.setText("Humidity ......................" + humidy.substring(0, humidy.length() - 2) + "%");
//                    Precipitation.setText("Precipitation ................"+ weatherDataModel.rain + "%");
                    Wind_txt.setText(Html.fromHtml("<b><big>"+ wind_data+"" +"</big></b>"+ "<b> <small> MPH ESE </small></b>"));
                    temperture_text.setText(Html.fromHtml("<b><big>"+ temperature  +"</big></b>"+  "<b> <small> °F </small></b>"));

                }catch (Exception e){ }

            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable e, JSONObject response) {
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

}