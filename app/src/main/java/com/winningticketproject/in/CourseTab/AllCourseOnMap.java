package com.winningticketproject.in.CourseTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.Adapter.Allcourse_horizontal;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Course_Data;
import com.winningticketproject.in.AppInfo.GPSTracker;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.BuildConfig;
import com.winningticketproject.in.IndividulaGameType.Location_Services;
import com.winningticketproject.in.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import cz.msebera.android.httpclient.HttpStatus;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

public class AllCourseOnMap extends Fragment implements OnMapReadyCallback {

    GoogleMap gMap;
    SupportMapFragment mapFragment;
    GPSTracker gps;
    public static double latitude,longitudes;
    public static ArrayList<Course_Data> public_data= new ArrayList<Course_Data>();
    public static ArrayList<Course_Data> private_data= new ArrayList<Course_Data>();
    public static ArrayList<Course_Data> all_data= new ArrayList<Course_Data>();
    MarkerInfo markerInfo;
    RecyclerView recylerview_public,recylerview_privte,recylerview_all;
    public AllCourseOnMap() {
        // Required empty public constructor
    }
    Map<Marker, MarkerInfo> mMarkerMap = new HashMap<>();
    Marker marker;

    TextView tv_near_by_course;
    TextView tv_public_course;
    TextView tv_all_course;
    LinearLayout linear_layout_all_data;

    ImageButton image_btn_search;
    TextView tv_no_records_found;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_all_course_on_map, container, false);


        linear_layout_all_data = view.findViewById(R.id.linear_layout_all_data);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.near_by_map);
        mapFragment.getMapAsync(this);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recylerview_public = view.findViewById(R.id.recylerview_public);
        recylerview_public.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recylerview_privte = view.findViewById(R.id.recylerview_privte);
        recylerview_privte.setLayoutManager(layoutManager2);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recylerview_all = view.findViewById(R.id.recylerview_all);
        recylerview_all.setLayoutManager(layoutManager3);

        //top textview id declaration
        tv_all_course = view.findViewById(R.id.tv_all_course);
        tv_public_course = view.findViewById(R.id.tv_public_course);
        tv_near_by_course = view.findViewById(R.id.tv_private_course);

        tv_no_records_found = view.findViewById(R.id.tv_no_records_found);
        tv_no_records_found.setTypeface(regular);

        tv_near_by_course.setTypeface(semibold);
        tv_public_course.setTypeface(semibold);
        tv_all_course.setTypeface(semibold);


        image_btn_search = view.findViewById(R.id.image_btn_search);
        image_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Course_Search_Page.class);
                startActivity(intent);
            }
        });
        return view;
    }

    // call igolf api to get near by courses
    private void methodtocalallcourses() {
        try {

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            JSONObject object = new JSONObject();
            object.put("referenceLatitude",latitude);
            object.put("referenceLongitude",longitudes);
            object.put("radius",10);
            object.put("active",1);
            object.put("page",1);
            object.put("resultsPerPage",99);
            System.out.println("-------post---"+object);
            String url = BuildConfig.REST_ENDPOINT+ com.winningticketproject.in.IGOLF.iGolfAuth.getUrlForAction("CourseList");

            System.out.println("-------------"+url);
            ProgressDialog.getInstance().showProgress(getContext());
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, url,object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                ProgressDialog.getInstance().hideProgress();
                                System.out.println("--------print---------"+response);
                                private_data.clear();
                                public_data.clear();
                                all_data.clear();

                                if (response.getInt("totalCourses")==0){
                                    linear_layout_all_data.setVisibility(View.GONE);
                                    tv_no_records_found.setVisibility(View.VISIBLE);
                                }else {
                                    tv_no_records_found.setVisibility(View.GONE);
                                    linear_layout_all_data.setVisibility(View.VISIBLE);
                                }

                                JSONArray allevent = response.getJSONArray("courseList");

                                for (int i=0;i<allevent.length();i++){

                                    JSONObject eventobject = allevent.getJSONObject(i);

                                    String city="",state_or_province="";

                                    try {
                                        city = eventobject.getString("city");
                                        if (city.equals("") || city.equals(null) || city.equals("null")) {
                                            city = "";
                                        }else {
                                            city = city+", ";
                                        }
                                    } catch (Exception e) {
                                        city = "";
                                        //nothing
                                    }
                                    try {

                                        state_or_province = eventobject.getString("stateShort");

                                        if (state_or_province.equals("") || state_or_province.equals(null) || state_or_province.equals("null")) {
                                            state_or_province = "";
                                        }else {
                                            state_or_province = state_or_province+", ";
                                        }
                                    } catch (Exception e) {
                                        state_or_province = "";
                                        //nothing
                                    }

                                    String course_classification;
                                    try {
                                        course_classification = eventobject.getString("classification");
                                        if (course_classification.equals("") || course_classification.equals(null) || course_classification.equals("null")) {
                                            course_classification = "Not Mentioned";
                                        }
                                    } catch (Exception e) {
                                        course_classification = "Not Mentioned";
                                        //nothing
                                    }

                                    if (course_classification.equals("public")){
                                        public_data.add(new Course_Data(eventobject.getString("id_course"), eventobject.getString("latitude"), eventobject.getString("longitude"),eventobject.getString("courseName"),"",course_classification,city+""+state_or_province,eventobject.getString("distance")));
                                    }else if (course_classification.equals("private") || course_classification.equals("semi-private") ){
                                        private_data.add(new Course_Data(eventobject.getString("id_course"), eventobject.getString("latitude"), eventobject.getString("longitude"),eventobject.getString("courseName"),"",course_classification,city+""+state_or_province,eventobject.getString("distance")));
                                    }else {
                                       all_data.add(new Course_Data(eventobject.getString("id_course"), eventobject.getString("latitude"), eventobject.getString("longitude"),eventobject.getString("courseName"),"",course_classification,city+""+state_or_province,eventobject.getString("distance")));
                                    }

                                    marker = gMap.addMarker(new MarkerOptions().position(new LatLng(eventobject.getDouble("latitude"), eventobject.getDouble("longitude"))).icon(BitmapDescriptorFactory.fromResource(R.drawable.golf_icon)).title(eventobject.getString("courseName")));

                                    builder.include(marker.getPosition());

                                    markerInfo = new MarkerInfo(eventobject.getString("id_course"),eventobject.getString("courseName"),eventobject.getString("distance"),city);
                                    mMarkerMap.put(marker, markerInfo);
                                }

                                ProgressDialog.getInstance().hideProgress();

                                LatLngBounds bounds = builder.build();
                                int padding = 250; // offset from edges of the map in pixels
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                gMap.moveCamera(cu);
                                gMap.animateCamera(cu);

                                // Setting a custom info window adapter for the google map
                                MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getContext(),mMarkerMap);
                                gMap.setInfoWindowAdapter(markerInfoWindowAdapter);

                                // call horizontal_adapter for public
                                final Allcourse_horizontal public_courses = new Allcourse_horizontal(getActivity(),public_data);
                                recylerview_public.setAdapter(public_courses);

                                if (public_data.isEmpty()){
                                    recylerview_public.setVisibility(View.GONE);
                                    tv_public_course.setVisibility(View.GONE);
                                }

                                if (private_data.isEmpty()){
                                    recylerview_privte.setVisibility(View.GONE);
                                    tv_near_by_course.setVisibility(View.GONE);
                                }


                                if (all_data.isEmpty()){
                                    recylerview_all.setVisibility(View.GONE);
                                    tv_all_course.setVisibility(View.GONE);
                                }

                                // call horizontal_adapter for priate and semi-private
                                final Allcourse_horizontal private_courss = new Allcourse_horizontal(getActivity(),private_data);
                                recylerview_privte.setAdapter(private_courss);

                                // call horizontal_adapter for priate and semi-private
                                final Allcourse_horizontal all_courses = new Allcourse_horizontal(getActivity(),all_data);
                                recylerview_all.setAdapter(all_courses);


                                // Adding and showing marker when the map is touched
                                gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng arg0) {
                                        marker.hideInfoWindow();
                                    }
                                });


                                gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        MarkerInfo markerInfo = mMarkerMap.get(marker);
                                        Intent viewIntent = new Intent(getContext(), Course_Detail_Page.class);
                                        viewIntent.putExtra("id", markerInfo.Course_id);
                                        viewIntent.putExtra("distance", markerInfo.Distance);
                                        viewIntent.putExtra("city_state", markerInfo.Course_city);
                                        startActivity(viewIntent);
                                    }
                                });

                            }catch (Exception E){
                                //nothing
                                ProgressDialog.getInstance().hideProgress();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            linear_layout_all_data.setVisibility(View.GONE);
                            ProgressDialog.getInstance().hideProgress();
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                // HTTP Status Code: 401 Unauthorized
                                if (error.networkResponse.statusCode==401){
                                    Alert_Dailog.showNetworkAlert(getActivity());
                                }else {
                                    Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);myRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(myRequest, "tag");
        } catch (Exception e) {
            //nothing
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gps = new GPSTracker(getActivity());

        if (Location_Services.isLocationEnabled(getActivity())){
            latitude = gps.getLatitude();
                longitudes = gps.getLongitude();
                methodtocalallcourses();
        }else {
                gps.showSettingsAlert();
        }
    }
}
