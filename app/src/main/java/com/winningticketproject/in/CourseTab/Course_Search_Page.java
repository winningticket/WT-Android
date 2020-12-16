package com.winningticketproject.in.CourseTab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.games.Game;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Course_Data;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.EventTab.Score_board_new;
import com.winningticketproject.in.IndividulaGameType.Location_Services;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.TeeHandicapHole.Hole_Tee_Handicap;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.distFrom;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;
import static com.winningticketproject.in.CourseTab.AllCourseOnMap.latitude;
import static com.winningticketproject.in.CourseTab.AllCourseOnMap.longitudes;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.selected_handicap;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.selected_hole;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.selected_tee;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class Course_Search_Page extends AppCompatActivity implements View.OnClickListener {
    EditText select_course;
    Button appaly_button;
    Spinner state_search_course;
    Spinner spinner_country;
    ArrayList<String> contry_id = new ArrayList<>();
    ArrayList<String> contry_values = new ArrayList<>();
    EditText tv_city_by_name;
    ArrayList<String> states_id = new ArrayList<>();
    ArrayList<String> state_values = new ArrayList<>();
    ImageButton back_to_course_page;
    RelativeLayout relative_state;
    int selected_country_id=0;
    int State_id=0;
    public static ArrayList<Course_Data> all_search_data= new ArrayList<Course_Data>();
    public static ArrayList<Course_Data> public_search_data= new ArrayList<Course_Data>();
    public static ArrayList<Course_Data> private_search_data= new ArrayList<Course_Data>();
    public static ArrayList<Course_Data> semi_private_search_data= new ArrayList<Course_Data>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_search_page);

        appaly_button= findViewById(R.id.appaly_button);


        select_course = findViewById(R.id.tv_search_by_name);

        TextView tv_or = findViewById(R.id.tv_or);
        tv_or.setTypeface(regular);

        select_course.setTypeface(regular);
        appaly_button.setTypeface(regular);

        select_course = findViewById(R.id.tv_search_by_name);

        state_search_course = findViewById(R.id.state_search_course);

        tv_city_by_name = findViewById(R.id.tv_city_by_name);
        tv_city_by_name.setTypeface(regular);

        relative_state = findViewById(R.id.relative_state);

        method_to_call_contry_api();

        appaly_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiddenInputMethod();
                if (select_course.getText().toString().isEmpty() && selected_country_id==0){
                    Toast.makeText(Course_Search_Page.this,"Please search by course name or country",Toast.LENGTH_LONG).show();
                }else {
                    if (select_course.getText().toString().length()>0){
                        selected_country_id=0;
                        State_id=0;
                    }
                    method_to_call_search_result_api();
                }
            }
        });

        back_to_course_page = findViewById(R.id.back_to_search_page);
        back_to_course_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void method_to_call_search_result_api() {

        JSONObject map_search = new JSONObject();
        try {
            map_search.put("country",selected_country_id);
            map_search.put("state_id",State_id);
            map_search.put("course_name",select_course.getText().toString());
            map_search.put("city",tv_city_by_name.getText().toString());
        }catch (Exception e) { }

        System.out.println("-------------"+map_search);

        all_search_data.clear();
        public_search_data.clear();
        private_search_data.clear();
        semi_private_search_data.clear();

        ProgressDialog.getInstance().showProgress(Course_Search_Page.this);

//        String url = Splash_screen.imageurl + "api/v2/golfcourse/search_courses_list?country="+Integer.parseInt(selected_country_id)+"&state_id="+Integer.parseInt(State_id)+"&city=&course_name=";

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.imageurl + "api/v2/golfcourse/search_courses_list", map_search,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println("------data------" + response);
                            //all data from igolf
                            JSONObject courses = response.getJSONObject("courses");
                            JSONArray all = courses.getJSONArray("all");
                            for (int i = 0; i < all.length(); i++) {

                                JSONObject all_data = all.getJSONObject(i);

                                String city = "", state_or_province = "";

                                try {
                                    city = all_data.getString("city");
                                    if (city.equals("") || city.equals(null) || city.equals("null")) {
                                        city = "";
                                    } else {
                                        city = city + ", ";
                                    }
                                } catch (Exception e) {
                                    city = "";
                                    //nothing
                                }
                                try {

                                    state_or_province = all_data.getString("stateShort");

                                    if (state_or_province.equals("") || state_or_province.equals(null) || state_or_province.equals("null")) {
                                        state_or_province = "";
                                    } else {
                                        state_or_province = state_or_province + ", ";
                                    }
                                } catch (Exception e) {
                                    state_or_province = "";
                                    //nothing
                                }

                                String course_classification;
                                try {
                                    course_classification = all_data.getString("classification");
                                    if (course_classification.equals("") || course_classification.equals(null) || course_classification.equals("null")) {
                                        course_classification = "Not Mentioned";
                                    }
                                } catch (Exception e) {
                                    course_classification = "Not Mentioned";
                                    //nothing
                                }

                                String mm_values = String.valueOf(distFrom(latitude,longitudes,all_data.getDouble("latitude"),all_data.getDouble("longitude")));

                                if (course_classification.equals("semi-private")){
                                    semi_private_search_data.add(new Course_Data(all_data.getString("id_course"), "", "", all_data.getString("courseName"), "", course_classification, city + "" + state_or_province, mm_values));
                                }
                                    all_search_data.add(new Course_Data(all_data.getString("id_course"), "", "", all_data.getString("courseName"), "", course_classification, city + "" + state_or_province, mm_values));

                                 }

                            // public data from igolf
                            JSONArray public_data = courses.getJSONArray("public");
                            for (int j = 0; j < public_data.length(); j++) {

                                JSONObject public_search = public_data.getJSONObject(j);

                                String city = "", state_or_province = "";

                                try {
                                    city = public_search.getString("city");
                                    if (city.equals("") || city.equals(null) || city.equals("null")) {
                                        city = "";
                                    } else {
                                        city = city + ", ";
                                    }
                                } catch (Exception e) {
                                    city = "";
                                    //nothing
                                }
                                try {

                                    state_or_province = public_search.getString("stateShort");

                                    if (state_or_province.equals("") || state_or_province.equals(null) || state_or_province.equals("null")) {
                                        state_or_province = "";
                                    } else {
                                        state_or_province = state_or_province + ", ";
                                    }
                                } catch (Exception e) {
                                    state_or_province = "";
                                    //nothing
                                }

                                String course_classification;
                                try {
                                    course_classification = public_search.getString("classification");
                                    if (course_classification.equals("") || course_classification.equals(null) || course_classification.equals("null")) {
                                        course_classification = "Not Mentioned";
                                    }
                                } catch (Exception e) {
                                    course_classification = "Not Mentioned";
                                    //nothing
                                }

                                String mm_values = String.valueOf(distFrom(latitude,longitudes,public_search.getDouble("latitude"),public_search.getDouble("longitude")));

                                public_search_data.add(new Course_Data(public_search.getString("id_course"), "", "", public_search.getString("courseName"), "", course_classification, city + "" + state_or_province, mm_values));


                            }
                            // private data from iGOlf
                            JSONArray private_data = courses.getJSONArray("private");
                            for (int k = 0; k < private_data.length(); k++) {

                                JSONObject private_search = private_data.getJSONObject(k);

                                String city = "", state_or_province = "";

                                try {
                                    city = private_search.getString("city");
                                    if (city.equals("") || city.equals(null) || city.equals("null")) {
                                        city = "";
                                    } else {
                                        city = city + ", ";
                                    }
                                } catch (Exception e) {
                                    city = "";
                                    //nothing
                                }
                                try {

                                    state_or_province = private_search.getString("stateShort");

                                    if (state_or_province.equals("") || state_or_province.equals(null) || state_or_province.equals("null")) {
                                        state_or_province = "";
                                    } else {
                                        state_or_province = state_or_province + ", ";
                                    }
                                } catch (Exception e) {
                                    state_or_province = "";
                                    //nothing
                                }

                                String course_classification;
                                try {
                                    course_classification = private_search.getString("classification");
                                    if (course_classification.equals("") || course_classification.equals(null) || course_classification.equals("null")) {
                                        course_classification = "Not Mentioned";
                                    }
                                } catch (Exception e) {
                                    course_classification = "Not Mentioned";
                                    //nothing
                                }

                                String mm_values = String.valueOf(distFrom(latitude,longitudes,private_search.getDouble("latitude"),private_search.getDouble("longitude")));

                                private_search_data.add(new Course_Data(private_search.getString("id_course"), "", "", private_search.getString("courseName"), "", course_classification, city + "" + state_or_province, mm_values));


                            }

                            ProgressDialog.getInstance().hideProgress();

                            Intent in = new Intent(Course_Search_Page.this, Search_Course_Result.class);
                            startActivity(in);

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
                                Alert_Dailog.showNetworkAlert(Course_Search_Page.this);
                            } else {
                                Toast.makeText(Course_Search_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Course_Search_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

    }

    private void method_to_call_contry_api() {
        spinner_country = findViewById(R.id.spinner_country);
        contry_id.clear();
        contry_values.clear();
        ProgressDialog.getInstance().showProgress(Course_Search_Page.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.imageurl + "api/v2/golfcourse/country_list", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            contry_id.add("0");
                            contry_values.add("Select a country");

                            ProgressDialog.getInstance().hideProgress();
                            JSONArray country_hash = response.getJSONArray("country_hash");
                            for (int i=0;i<country_hash.length();i++){

                                JSONObject country_hash_obeject = country_hash.getJSONObject(i);

                                contry_id.add(country_hash_obeject.getString("country_id"));
                                contry_values.add(country_hash_obeject.getString("country_name"));

                            }
                            ArrayAdapter<String> adapter   = new ArrayAdapter<String>(Course_Search_Page.this,R.layout.custom_text,  contry_values) {
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View v = super.getView(position, convertView, parent);
                                    ((TextView) v).setTypeface(regular);
                                    return v;
                                }
                                public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                                    View v =super.getDropDownView(position, convertView, parent);
                                    ((TextView) v).setTypeface(regular);
                                    if(((TextView) v).getText().toString().equals("Select a country")){
                                        // Set the hint text color gray
                                        ((TextView) v).setTextColor(Color.GRAY);
                                    }
                                    else
                                    {
                                        ((TextView) v).setTextColor(Color.BLACK);
                                    }
                                    return v;
                                }
                            };

                            adapter.setDropDownViewResource( R.layout.custom_country_text);
                            spinner_country.setAdapter(adapter);

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
                                Alert_Dailog.showNetworkAlert(Course_Search_Page.this);
                            } else {
                                Toast.makeText(Course_Search_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Course_Search_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    relative_state.setVisibility(View.VISIBLE);
                    tv_city_by_name.setVisibility(View.VISIBLE);
                    selected_country_id = Integer.parseInt(contry_id.get(position));
                    method_to_call_state_list_api(contry_id.get(position));
                    select_course.setText("");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void method_to_call_state_list_api(String contry_ids) {
        JSONObject object = new JSONObject();
        try {
            object.put("country_id", contry_ids);
        }catch (Exception e) { }

        states_id.clear();
        state_values.clear();

        ProgressDialog.getInstance().showProgress(Course_Search_Page.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.imageurl+"/api/v2/golfcourse/state_list", object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            states_id.add("0");
                            state_values.add("Select a state");

                            ProgressDialog.getInstance().hideProgress();
                            JSONArray state_list = response.getJSONArray("state_list");
                            for (int i=0;i<state_list.length();i++){
                                JSONObject state_list_object = state_list.getJSONObject(i);
                                states_id.add(state_list_object.getString("id_state"));
                                state_values.add(state_list_object.getString("stateFull"));
                            }

                            method_to_load_state();

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
                                Alert_Dailog.showNetworkAlert(Course_Search_Page.this);
                            } else {
                                Toast.makeText(Course_Search_Page.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }if(networkResponse == null){
                            states_id.add("0");
                            state_values.add("Select a state");
                            method_to_load_state();
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

    }

    private void method_to_load_state() {
        ArrayAdapter<String> adapter1   = new ArrayAdapter<String>(Course_Search_Page.this,R.layout.custom_text,  state_values) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(regular);
                return v;
            }
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(regular);
                if(((TextView) v).getText().toString().equals("Select a state")){
                    // Set the hint text color gray
                    ((TextView) v).setTextColor(Color.GRAY);
                }
                else
                {
                    ((TextView) v).setTextColor(Color.BLACK);
                }
                return v;
            }
        };
        adapter1.setDropDownViewResource( R.layout.custom_country_text);
        state_search_course.setAdapter(adapter1);

        state_search_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position>0) {
                    State_id = Integer.parseInt(states_id.get(position));
                }else {
                    State_id=0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.cancel_purchage:
                hiddenInputMethod();
                finish();
                break;
        }

    }
}
