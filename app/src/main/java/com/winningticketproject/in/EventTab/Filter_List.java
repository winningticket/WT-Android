package com.winningticketproject.in.EventTab;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.AllEventGetterSetter;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.Adapter.All_Event_List;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;


public class Filter_List extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    TextView cancel_button,choose_from_date,choose_to_date,felter_text,by_state,by_date,view_all;
    ListView viewevent_listview;
    int startenddate;
    Calendar now,now1 ;
    Spinner select_state;
    Typeface tf;
    Typeface book;
    ProgressDialog pd;
    String auth_token="",url="",state_key="",from_date="",to_date="";

    ArrayList<String> statevalues = new ArrayList<>();
    ArrayList<String> statekeys = new ArrayList<>();

    ArrayList<AllEventGetterSetter> filter_event = new ArrayList<>();

    int current_page=1,total_page=0,position;
    boolean loadingMore=false;
    Button appaly_button;
    com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd,dpd1;
    String url_parse,event_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_filter__list);


        tf = Typeface.createFromAsset(getApplicationContext().getAssets(),"Montserrat-Medium.ttf");
        book = Typeface.createFromAsset(getApplicationContext().getAssets(),"Montserrat-Regular.ttf");

        auth_token = get_auth_token("auth_token");

        pd = new ProgressDialog(Filter_List.this);

        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        viewevent_listview = findViewById(R.id.filter_viewevent_listview);
        view_all = findViewById(R.id.view_all);

        filter_event.clear();

        Intent in = getIntent();
        url_parse = in.getStringExtra("url");
        event_type = in.getStringExtra("event_type");

        if (!isNetworkAvailable()) {

            alertdailogbox("nofilter");
        }
        else
        {
            url = Splash_screen.url+url_parse+"?event_state="+""+"&start_date="+""+"&end_date="+"" ;
            MethodToCalStateApi();
            methodforcalleventlist(url);
        }

        appaly_button = findViewById(R.id.appaly_button);
        appaly_button.setOnClickListener(this);

        select_state  = findViewById(R.id.select_state);
        viewevent_listview.setOnScrollListener(new AbsListView.OnScrollListener(){

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {

                if(!pd.isShowing()) {
                    if (totalItemCount >= 20) {
                        if (firstVisibleItem + visibleItemCount == totalItemCount && !(loadingMore)) {
                            if (current_page <= total_page) {

                                if (!isNetworkAvailable()) {
                                    Toast.makeText(getApplicationContext(), "You don't have internet connection.", Toast.LENGTH_LONG).show();
                                } else {
                                    try {
                                        loadingMore = true;
                                        methodforcalleventlist(url);

                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){

            }
        });

        select_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                // TODO Auto-generated method stub

                state_key=statekeys.get(postion);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });



        cancel_button = findViewById(R.id.cancel_button);
        choose_from_date = findViewById(R.id.choose_from_date);
        choose_to_date = findViewById(R.id.choose_to_date);


        felter_text = findViewById(R.id.felter_text);
        by_state = findViewById(R.id.by_state);
        by_date = findViewById(R.id.by_date);


        cancel_button.setTypeface(tf);
        choose_from_date.setTypeface(tf);
        choose_to_date.setTypeface(tf);
        felter_text.setTypeface(tf);
        by_state.setTypeface(tf);
        by_date.setTypeface(tf);


        cancel_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                        finish();
                return false;
            }
        });
        choose_from_date.setOnClickListener(this);
        choose_to_date.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.choose_from_date:

                startenddate=1;


                now = Calendar.getInstance();
              dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(Filter_List.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                choose_to_date.setText("To");
                choose_to_date.setTextColor(getResources().getColor(R.color.colorgray));
                from_date="1";
                dpd.show(getFragmentManager(), "Datepickerdialog");


                break;

            case R.id.choose_to_date:
                    startenddate = 2;
                    now1 = Calendar.getInstance();
                 dpd1 = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                            Filter_List.this,
                            now1.get(Calendar.YEAR),
                            now1.get(Calendar.MONTH),
                            now1.get(Calendar.DAY_OF_MONTH)
                    );
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        Date date = format.parse(choose_from_date.getText().toString());
                        now1.setTime(date);
                        dpd1.setMinDate(now1);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        //nothing
                    }

                  choose_to_date.setTextColor(getResources().getColor(R.color.colorblack));
                    to_date="2";
                    dpd1.show(getFragmentManager(), "Datepickerdialog");
//                }
                break;


            case R.id.appaly_button:

                to_date=choose_to_date.getText().toString();
                from_date=choose_from_date.getText().toString();

                if(from_date.equals("1")){
                    from_date="";
                }

                if(to_date.equals("2")){
                    to_date="";
                }

                    current_page=1;
                filter_event.clear();

                    if (!isNetworkAvailable()) {

                        alertdailogbox("filter");
                    }
                    else
                    {
                        url=Splash_screen.url+url_parse+"?event_state="+state_key+"&start_date="+from_date+"&end_date="+to_date;
                        methodforcalleventlist(url);
                    }

//                }

                break;
        }



    }


    public void methodforcalleventlist(String url){
        if(!pd.isShowing())
        {
            pd.show();
        }

        System.out.println("----url--"+url);
        position=filter_event.size();
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.GET, url+"&page="+current_page,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("------response-----"+response);

                            if (pd.isShowing()){
                                pd.dismiss();

                            }
                            JSONObject meta = response.getJSONObject("meta");

                            total_page = meta.getInt("total_pages");

                            JSONArray allevent = response.getJSONArray("events");

                            for (int i=0;i<allevent.length();i++){
                                JSONObject eventobject = allevent.getJSONObject(i);
                                filter_event.add(new AllEventGetterSetter(eventobject.getString("id"),eventobject.getString("name").replaceAll("\\s+"," "),eventobject.getString("start_date"),eventobject.getString("avatar_url"),event_type,eventobject.getString("event_status"),eventobject.getInt("push_notification_count")));
                            }

                            All_Event_List allevents = new All_Event_List(Filter_List.this,filter_event);
                            viewevent_listview.setAdapter(allevents);
                            viewevent_listview.setSelectionFromTop(position, 0);
                            loadingMore = false;
                            current_page++;
                            if (filter_event.isEmpty()) {
                                viewevent_listview.setVisibility(View.GONE);
                                view_all.setVisibility(View.VISIBLE);
                            }
                            else {
                                viewevent_listview.setVisibility(View.VISIBLE);
                                view_all.setVisibility(View.GONE);
                            }


                            if (pd.isShowing()){
                                pd.dismiss();
                            }

                        }catch (Exception e){
                            if (pd.isShowing()){
                                pd.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Alert_Dailog.showNetworkAlert(Filter_List.this);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Toast.makeText(Filter_List.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(Filter_List.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            if(startenddate==1)
            {
                String monthof=(monthOfYear+1)+"";
                String dayof=dayOfMonth+"";
                if(monthof.length()==1)
                {
                    monthof="0"+monthof;
                }
                if(dayof.length()==1)
                {
                    dayof="0"+dayof;
                }
                String date = ""+dayof+"-"+monthof+"-"+year;
                choose_from_date.setText(date);
//            start_dates=monthof+"/"+dayof+"/"+year;

            }else {
                    String monthof=(monthOfYear+1)+"";
                    String dayof=dayOfMonth+"";
                    if(monthof.length()==1)
                    {
                        monthof="0"+monthof;
                    }
                    if(dayof.length()==1)
                    {
                        dayof="0"+dayof;
                    }
                    String date = ""+dayof+"-"+monthof+"-"+year;
                    choose_to_date.setText(date);
//            start_dates=monthof+"/"+dayof+"/"+year;

                }
    }

    public void alertdailogbox(final String value){
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
                if(value.equals("nofilter")) {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("nofilter");
                    } else {
                        methodforcalleventlist(Splash_screen.url+"events/all_events");
                    }
                }
                else  if(value.equals("filter"))
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("filter");
                    }
                    else
                    {

                        methodforcalleventlist(Splash_screen.url+url_parse+"?start_date="+choose_from_date.getText().toString()+"&end_date="+choose_to_date.getText().toString());
                    }
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }
    private void MethodToCalStateApi() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"city_states/states?country=US", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                statevalues.clear();
                statekeys.clear();

                statevalues.add("Choose a state");
                statekeys.add("");
                try {
                    Iterator<String> keys = response.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        statevalues.add(key);
                        statekeys.add(response.getString(key));
                    }

                    ArrayAdapter<String> adapter  = new ArrayAdapter<String>(Filter_List.this,R.layout.custom, statevalues) {

                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);
                            ((TextView) v).setTypeface(book);

                            return v;
                        }


                        public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                            View v =super.getDropDownView(position, convertView, parent);

                            ((TextView) v).setTypeface(book);
                            if(((TextView) v).getText().toString().equals("Choose a state")){
                                ((TextView) v).setTextColor(Color.GRAY);
                            }
                            else
                            {
                                ((TextView) v).setTextColor(Color.BLACK);
                            }
                            return v;
                        }
                    };
                    adapter.setDropDownViewResource(R.layout.custom_text);
                    select_state.setAdapter(adapter);
                }
                catch (Exception e)
                {
                    //nothing
                }}
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    if (error.networkResponse.statusCode==401){
                        if (pd.isShowing()){
                            pd.dismiss();}
                        Alert_Dailog.showNetworkAlert(Filter_List.this);
                    }else {
                        if (pd.isShowing()){
                            pd.dismiss();}
                        Toast.makeText(Filter_List.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    if (pd.isShowing()){
                        pd.dismiss();}
                    Toast.makeText(Filter_List.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                }
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy( 20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

}
