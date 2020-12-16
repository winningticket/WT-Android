package com.winningticketproject.in.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
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
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.IndividulaGameType.Grass_score_Update_from_Map;
import com.winningticketproject.in.AuctionModel.Live_Auction;
import com.winningticketproject.in.IndividulaGameType.Location_Services;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.GPSTracker;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.ChatWithOther.User_List;
import com.winningticketproject.in.R;
import com.winningticketproject.in.Shamble_flow.Shamble_Bestball_Map_GS_activity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.IndividulaGameType.Location_Services.game_name;
import static com.winningticketproject.in.EventTab.NewEventDetailPage.oragnization;
import static com.winningticketproject.in.EventTab.NewEventDetailPage.sponsor_logo;
import static com.winningticketproject.in.EventTab.Score_board_new.game_completed;
import static com.winningticketproject.in.SignInSingup.Splash_screen.black;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;
import static com.winningticketproject.in.Shamble_flow.Shamble_Bestball_Map_GS_activity.hole_data;
import static com.winningticketproject.in.Shamble_flow.Shamble_Bestball_Map_GS_activity.par_data;

public class View_course_map_drag extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener, LocationListener, View.OnClickListener {
    View layout_view;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    ArrayList<LatLng> points = new ArrayList<>();
    HashMap<Integer, Double> latlist = new HashMap<Integer, Double>();
    HashMap<Integer, Double> longlist = new HashMap<Integer, Double>();
    GPSTracker gps = null;
    TextView tv_total_distance, tv_total_yards, tv_total_distance_center, tv_total_yards_center, tv_total_distance_front, tv_total_yards_front, textviewdirection, textviewkmph;
    int total_merter = 0, meters = 0, meterss = 0, new_meter = 0, current_hole = 0, yards_1 = 0, yards_2 = 0, front_yards = 0, back_yards;
    ImageView preview_click, next_click, wind_direction;
    double current_latitude, current_longitudes;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    PolylineOptions options = null;
    MarkerOptions markerOptions;
    android.support.v7.app.AlertDialog alertDialog;
    String auth_token = "", event_id = "", hole_number = "", hole_par = "", hole_nume, hole_yards;
    public static String hole_number_from_all_corses = "";
    List<Polyline> polylines = new ArrayList<>();
    public static int count_list;
    ImageView img_totorial;
    RelativeLayout relative_layout;
    int image_pos;
    int[] tut_list = new int[]{R.drawable.tut0, R.drawable.tut01, R.drawable.tut02, R.drawable.tut04, R.drawable.tut05, R.drawable.tut06, R.drawable.tut07};

    SupportMapFragment mapFragment;
    float degrees;
    LinearLayout layout_map_hole_button;
    String back_lat, back_lng, front_lat, front_lng, handica_values = "";

    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    //Initialize to a non-valid zoom value
    private float previousZoomLevel = 13.0f;
    private boolean isZooming = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    Share_it share_it;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout_view = inflater.inflate(R.layout.fragment_view_course_map_drag, container, false);

        LinearLayout topbar3 = layout_view.findViewById(R.id.bottom_tab_layout);
        topbar3.setVisibility(View.VISIBLE);

        // GPS Enabled ot not
        gps = new GPSTracker(getActivity());
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        // total distance and yards
        tv_total_distance = layout_view.findViewById(R.id.tv_total_distance);
        tv_total_distance.setTypeface(black);

        tv_total_yards = layout_view.findViewById(R.id.tv_total_yards);
        tv_total_yards.setTypeface(semibold);

        tv_total_distance_center = layout_view.findViewById(R.id.tv_total_distance_center);
        tv_total_distance_center.setTypeface(semibold);

        tv_total_distance_front = layout_view.findViewById(R.id.tv_total_distance_front);
        tv_total_distance_front.setTypeface(semibold);

        tv_total_yards_center = layout_view.findViewById(R.id.tv_total_yards_center);
        tv_total_yards_center.setTypeface(semibold);

        tv_total_yards_front = layout_view.findViewById(R.id.tv_total_yards_front);
        tv_total_yards_front.setTypeface(semibold);

        // next previous button icons
        next_click = layout_view.findViewById(R.id.next_click);
        preview_click = layout_view.findViewById(R.id.preview_click);

        // wind direction and calculates KM

        wind_direction = layout_view.findViewById(R.id.wind_direction);

        textviewkmph = layout_view.findViewById(R.id.textviewkmph);
        textviewkmph.setTypeface(semibold);

        textviewdirection = layout_view.findViewById(R.id.textviewdirection);
        textviewdirection.setTypeface(semibold);

        // map initialization
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.view_course_map);

        // points array declaration
        points.clear();
        for (int i=0;i<5;i++){
            points.add(new LatLng(0, 0));
        }

        // location manager initialization
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, (android.location.LocationListener) getContext()); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
            return null;
        }

        share_it = new Share_it(getContext());

        boolean ranBefore = Boolean.parseBoolean(get_auth_token("RanBefore"));
        if (ranBefore==false) {
            tutorilas_method();
        }

        ImageView img_help = layout_view.findViewById(R.id.img_help);
        img_help.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                tutorilas_method();
                return false;
            }
        });

        auth_token = get_auth_token("auth_token");
        event_id = get_auth_token("event_id");
        hole_number = get_auth_token("selected_hole_postion");

        current_hole = Integer.parseInt(hole_number_from_all_corses);

        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();


        method_for_load_top_sectopn();

        return layout_view;
    }


    // for tutorials view
    private void tutorilas_method() {
        Share_it.savestring("RanBefore", "true");
        image_pos = 1;
        relative_layout = layout_view.findViewById(R.id.relative_layout);
        img_totorial = layout_view.findViewById(R.id.img_totorial);
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

    private void method_for_load_top_sectopn() {

        LinearLayout bottom_tab_layout = layout_view.findViewById(R.id.bottom_tab_layout);
        layout_map_hole_button = layout_view.findViewById(R.id.layout_map_hole_button);

        if (NewEventDetailPage.event_type.equals("passed")){
            bottom_tab_layout.setVisibility(View.GONE);
            layout_map_hole_button.setVisibility(View.GONE);
        }

        LinearLayout layout_silent_auction = layout_view.findViewById(R.id.layout_silent_auction);
        TextView tv_silent_auction = layout_view.findViewById(R.id.tv_silent_auction);
        tv_silent_auction.setTypeface(regular);

        LinearLayout layout_scoring_gps = layout_view.findViewById(R.id.layout_scoring_gps);
        TextView tv_scoring_gps = layout_view.findViewById(R.id.tv_scoring_gps);
        tv_scoring_gps.setTypeface(regular);

        LinearLayout layout_live_chat = layout_view.findViewById(R.id.layout_live_chat);
        TextView tv_live_chat = layout_view.findViewById(R.id.tv_live_chat);
        tv_live_chat.setTypeface(regular);

        LinearLayout layout_aline_center = layout_view.findViewById(R.id.layout_aline_center);
        TextView tv_aling_center = layout_view.findViewById(R.id.tv_aling_center);
        tv_aling_center.setTypeface(regular);

        LinearLayout layout_enter_score = layout_view.findViewById(R.id.layout_enter_score);
        TextView tv_enter_score = layout_view.findViewById(R.id.tv_enter_score);
        tv_enter_score.setTypeface(regular);

        TextView text_1 = layout_view.findViewById(R.id.text_1);
        TextView text_2 = layout_view.findViewById(R.id.text_2);
        TextView text_3 = layout_view.findViewById(R.id.text_3);

        if (Location_Services.game_name.equals("Course")){
            layout_silent_auction.setVisibility(View.GONE);
            text_1.setVisibility(View.GONE);

            layout_scoring_gps.setVisibility(View.GONE);
            text_2.setVisibility(View.GONE);

            layout_live_chat.setVisibility(View.GONE);
            text_3.setVisibility(View.GONE);

        }

        layout_silent_auction.setOnClickListener(this);
        layout_scoring_gps.setOnClickListener(this);
        layout_live_chat.setOnClickListener(this);
        layout_aline_center.setOnClickListener(this);
        layout_enter_score.setOnClickListener(this);
    }


    @Override
    public void onStart() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        current_hole = Integer.parseInt(hole_number_from_all_corses);

        if (current_hole>count_list){
            current_hole = 1;
            methodtocalgetholepostion(current_hole+"");
        }else {
            methodtocalgetholepostion(current_hole+"");
        }

        super.onStart();
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

        double old_lat = latlist.get(0);
        double old_long = longlist.get(0);

        mMap.setOnCameraChangeListener(getCameraChangeListener());

        if (latlist.size() > 1) {

            ConnectivityManager cn = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

//        mCurrLocationMarker.remove();

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

        method_top_section();

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


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setPadding(10, 150, 10, 0);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        //        mMap.setMyLocationEnabled(true);

        // check location enabled or not
        if (Location_Services.isLocationEnabled(getActivity())){
            current_latitude = gps.getLatitude();
            current_longitudes = gps.getLongitude();
            latlist.put(0, current_latitude);
            longlist.put(0, current_longitudes);
//            googleMap.setMyLocationEnabled(true);
//            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);

        }

//        mMap.setOnCameraChangeListener(getCameraChangeListener());

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setOnMarkerDragListener(this);

        preview_click.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                current_hole = current_hole-1;
                if (current_hole<1){
                    current_hole = count_list;
                    methodtocalgetholepostion(current_hole+"");
                }else {
                    methodtocalgetholepostion(current_hole+"");
                }
                return false;
            }
        });

        next_click.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                current_hole = current_hole+1;
                if (current_hole>count_list){
                    current_hole = 1;
                    methodtocalgetholepostion(current_hole+"");
                }else {
                    methodtocalgetholepostion(current_hole+"");
                }
                return false;
            }
        });


    }

    private void method_top_section() {

        TextView tvleave_game = layout_view.findViewById(R.id.tvleave_game);
        tvleave_game.setTypeface(semibold);
        tvleave_game.setOnClickListener(this);

        TextView tv_course_name = layout_view.findViewById(R.id.tv_course_name);
        tv_course_name.setTypeface(medium);
        tv_course_name.setText(get_auth_token("Event_name"));

        TextView tv_hole_number = layout_view.findViewById(R.id.tv_hole_number);
        tv_hole_number.setTypeface(semibold);
        tv_hole_number.setText("Hole "+hole_nume);

        TextView tv_par_yards_handicap = layout_view.findViewById(R.id.tv_par_yards_handicap);
        tv_par_yards_handicap.setTypeface(regular);
        tv_par_yards_handicap.setText("Par "+hole_par +" - "+hole_yards+" Yards - Handicap "+handica_values);

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


    private void methodtocalgetholepostion(final String hole_postion) {
        ProgressDialog.getInstance().showProgress(getActivity());
        try {
            String url_value="";
            if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")){
                url_value = "hole_info/player/course_map/"+event_id+"/"+hole_postion;
            }else if (get_auth_token("play_type").equals("event")){
                url_value = "hole_info/course_map/"+event_id+"/"+hole_postion;
            }else if (get_auth_token("play_type").equals("individual")) {
                url_value = "hole_info/course_map/"+event_id+"/"+hole_postion;
            }

            System.out.println("--------hole---"+hole_postion);


            JsonObjectRequest myRequest_handicap = new JsonObjectRequest(
                    com.android.volley.Request.Method.POST, Splash_screen.url + url_value, null,
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
                                    layout_map_hole_button.setVisibility(View.GONE);
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
                                    Alert_Dailog.showNetworkAlert(getActivity());
                                } else {
                                    Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        if ( bitmapConfig == null ) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
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
        if (ContextCompat.checkSelfPermission(getActivity(),
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

        method_top_section();

    }

    @Override
    public void onClick(View view) {
        Intent in;
        switch (view.getId()){

            case R.id.layout_silent_auction:
                in=new Intent(getActivity(), Live_Auction.class);
                savestring("id",event_id);
                startActivity(in);
                break;

            case R.id.layout_scoring_gps:

                Super_ticket_poup();

                break;


            case R.id.layout_live_chat:
                in=new Intent(getActivity(), User_List.class);
                startActivity(in);
                break;

            case R.id.layout_aline_center:
                updateUI_to_Center();
                break;

            case R.id.layout_enter_score:
                if(game_name.equals("shamble") || game_name.equals("bestball")){
                    hole_data = hole_nume;
                    par_data = hole_par;
                    in = new Intent(getActivity(), Shamble_Bestball_Map_GS_activity.class);
                    startActivity(in);
                }else{
                    in = new Intent(getActivity(), Grass_score_Update_from_Map.class);
                    Grass_score_Update_from_Map.hole_data = hole_nume;
                    Grass_score_Update_from_Map.par_data_gr = hole_par;
                    startActivity(in);
                }

                break;

            case R.id.tvleave_game:
                pop_up_open();
                break;
        }
    }


    public void Super_ticket_poup() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recler_sponsor = alertDialog.findViewById(R.id.pop_recler_sponsor);
        recler_sponsor.setLayoutManager(layoutManager2);

        NewEventDetailPage.SponsorLogo allevents_hori = new NewEventDetailPage.SponsorLogo(getActivity(), sponsor_logo);
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

    public void pop_up_open() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogview = inflater.inflate(R.layout.pop_up_finish_game, null);
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
        nearby_courses.setTypeface(medium);
        Button enable= alertDialog.findViewById(R.id.enable);
        Button not_now= alertDialog.findViewById(R.id.not_now);
        enable.setTypeface(medium);
        not_now.setTypeface(medium);


        Button btn_be_right_back = alertDialog.findViewById(R.id.btn_be_right_back);
        btn_be_right_back.setTypeface(medium);

        if (Location_Services.game_name.equals("Course")){
            if (game_completed.equals("No")) {
                btn_be_right_back.setVisibility(View.VISIBLE);
                enable.setVisibility(View.GONE);
                nearby_courses.setVisibility(View.GONE);
                location_services.setText(Html.fromHtml("You haven't completed your \n game yet"));
            } else {
                btn_be_right_back.setVisibility(View.GONE);
                nearby_courses.setText(Html.fromHtml("Click " + "&ldquo;" + "Finish Game" + "&rdquo;" + " to compare your \nscores on our leaderboard "));
                location_services.setText(Html.fromHtml("You've completed your game!"));
            }
        }else {

            btn_be_right_back.setVisibility(View.GONE);
            enable.setVisibility(View.VISIBLE);

            if (game_completed.equals("No")) {
                nearby_courses.setVisibility(View.GONE);
                location_services.setText(Html.fromHtml("You haven't completed your \n game yet"));
            } else {
                nearby_courses.setText(Html.fromHtml("Click " + "&ldquo;" + "Finish Game" + "&rdquo;" + " to compare your \nscores on our leaderboard "));
                location_services.setText(Html.fromHtml("You've completed your game!"));
            }

        }



        btn_be_right_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                getActivity().finish();
            }
        });

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share_it.remove_key("first_time");
                alertDialog.dismiss();
                getActivity().finish();
            }
        });
        not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    private void updateUI_to_Center() {
//        points.clear();
//        polylines.clear();
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
}
