package com.winningticketproject.in.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.Adapter.Your_Round;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Your_round_data;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.distFrom;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;

public class Your_round extends Fragment {
    View view;
    TextView course_content;
    ListView your_round_listview;
    ArrayList<Your_round_data> your_round_data = new ArrayList<>();
    int progress=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_your_round, container, false);

        course_content = view.findViewById(R.id.course_content);
        course_content.setTypeface(regular);

        your_round_listview = view.findViewById(R.id.your_round);

        method_to_cal_rounded_api();

        return view;


    }

    private void method_to_cal_rounded_api() {
        try {
            if (progress==0){
                ProgressDialog.getInstance().showProgress(getContext());
            }
            your_round_data.clear();
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET,Splash_screen.url+"hole_info/recent_played_uncompleted",null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                System.out.println("-------respo----"+response);

                                JSONArray allevent = response.getJSONArray("uncompleted_courses");
                                for (int i=0;i<allevent.length();i++){
                                    JSONObject eventobject = allevent.getJSONObject(i);
                                    String city="",state_or_province="";
                                    try {
                                        city = eventobject.getString("city");
                                        if (city.equals("") || city.equals(null) || city.equals("null")) {
                                            city = "Not Mentioned";
                                        }
                                    } catch (Exception e) {
                                        city = "Not Mentioned";
                                        //nothing
                                    }
                                    try {
                                        state_or_province = eventobject.getString("stateShort");
                                        if (state_or_province.equals("") || state_or_province.equals(null) || state_or_province.equals("null")) {
                                            state_or_province = "Not Mentioned";
                                        }
                                    } catch (Exception e) {
                                        state_or_province = "Not Mentioned";
                                        //nothing
                                    }

                                    String course_image;
                                    try {
                                        course_image = eventobject.getString("image");
                                    }catch (Exception e){
                                        course_image="null";
                                    }

//                                    String mm_values = String.valueOf(distFrom(Testfragment.latitude,Testfragment.longitudes,eventobject.getDouble("latitude"),eventobject.getDouble("longitude")));

                                    your_round_data.add(new Your_round_data(eventobject.getString("player_id"),course_image,eventobject.getString("courseName"),city+", "+state_or_province,"",eventobject.getString("last_played"),eventobject.getString("selected_tee"),eventobject.getString("starting_hole"),eventobject.getString("current_score"),eventobject.getString("handicap")));

                                }

                                Your_Round your_round = new Your_Round(getActivity(),your_round_data);
                                your_round_listview.setAdapter(your_round);

                                if (your_round_data.size()>0){
                                    your_round_listview.setVisibility(View.VISIBLE);
                                }else {
                                    course_content.setVisibility(View.VISIBLE);
                                }

                                ProgressDialog.getInstance().hideProgress();

                            }catch (Exception E){
                                //nothing
                                if (progress==0){
                                    ProgressDialog.getInstance().hideProgress();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (progress==0){
                                ProgressDialog.getInstance().hideProgress();
                            }
                            NetworkResponse networkResponse = error.networkResponse;
                            System.out.println(networkResponse.data);

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
                    headers.put("auth-token", get_auth_token("auth_token"));
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
}
