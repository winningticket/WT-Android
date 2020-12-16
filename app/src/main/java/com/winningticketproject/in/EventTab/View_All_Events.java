package com.winningticketproject.in.EventTab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.winningticketproject.in.AppInfo.AllEventGetterSetter;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.Adapter.All_Event_List;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cz.msebera.android.httpclient.HttpStatus;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;


public class View_All_Events extends AppCompatActivity{

    ListView viewevent_listview;
    ProgressDialog pd;
    String auth_token="",url_parse,auto_complete,event_type;
    int current_page=1;
    int total_page=0;
    int position;
    boolean loadingMore=false;
    private List<AllEventGetterSetter> viewallevents = new ArrayList<AllEventGetterSetter>();
    TextView view_all;
    All_Event_List allevents;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view__all__events);

        viewevent_listview = findViewById(R.id.viewevent_listview);
        auth_token = get_auth_token("auth_token");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("All Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pd = new ProgressDialog(View_All_Events.this);
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        Intent in = getIntent();
        url_parse = in.getStringExtra("url");
        auto_complete = in.getStringExtra("auto_search");
        event_type = in.getStringExtra("event_type");

        viewallevents.clear();

        view_all = findViewById(R.id.view_all);
        view_all.setTypeface(regular);

         if (!isNetworkAvailable()) {
            Toast.makeText(this,"No Internet",Toast.LENGTH_LONG).show();
        }
        else
        {
            methodforcalleventlist(Splash_screen.url+url_parse);
        }

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
                                        methodforcalleventlist(Splash_screen.url +url_parse+"?page=" + current_page);
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


    }

    public void methodforcalleventlist(String url){
        if(!pd.isShowing())
        {
            pd.show();
        }
            current_page++;
         position=viewallevents.size();
         System.out.println("------url-----"+url);
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                try {
                    System.out.println("----respo---"+response);
                    if (pd.isShowing()){
                        pd.dismiss();

                    }
                    JSONObject meta = response.getJSONObject("meta");
                    total_page = meta.getInt("total_pages");

                    JSONArray allevent = response.getJSONArray("events");

                    for (int i=0;i<allevent.length();i++){
                        JSONObject eventobject = allevent.getJSONObject(i);

                        viewallevents.add(new AllEventGetterSetter(eventobject.getString("id"),
                                eventobject.getString("name").replaceAll("\\s+"," "),
                                eventobject.getString("converted_start_date"),
                                eventobject.getString("avatar_url"),
                                event_type,
                                eventobject.getString("event_status"),0));
                    }

                    allevents = new All_Event_List(View_All_Events.this,viewallevents);
                    viewevent_listview.setAdapter(allevents);
                    viewevent_listview.setSelectionFromTop(position, 0);
                    loadingMore = false;

                    if (viewallevents.isEmpty()) {
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
                        if (pd.isShowing()){ pd.dismiss();}
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(View_All_Events.this);
                            }else {
                                Toast.makeText(View_All_Events.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(View_All_Events.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_search, menu);
        MenuItem mSearch = menu.findItem(R.id.app_bar_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                String final_url = Splash_screen.url+url_parse+"?query="+newText+"&page=1";
                metho_to_search_event(final_url);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void metho_to_search_event(String url){
        viewallevents.clear();
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.GET, url.replaceAll(" ","%20"),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (pd.isShowing()){ pd.dismiss(); }
                            viewallevents.clear();
                            JSONObject meta = response.getJSONObject("meta");
                            total_page = meta.getInt("total_pages");
                            JSONArray allevent = response.getJSONArray("events");
                            for (int i=0;i<allevent.length();i++){
                                JSONObject eventobject = allevent.getJSONObject(i);
                                viewallevents.add(new AllEventGetterSetter(eventobject.getString("id"),eventobject.getString("name").replaceAll("\\s+"," "),eventobject.getString("converted_start_date"),eventobject.getString("avatar_url"),event_type,eventobject.getString("event_status"),eventobject.getInt("push_notification_count")));
                            }
                            All_Event_List allevents = new All_Event_List(View_All_Events.this,viewallevents);
                            viewevent_listview.setAdapter(allevents);
                            if (viewallevents.isEmpty()) {
                                viewevent_listview.setVisibility(View.GONE);
                                view_all.setVisibility(View.VISIBLE);
                            }
                            else {
                                viewevent_listview.setVisibility(View.VISIBLE);
                                view_all.setVisibility(View.GONE);
                            }

                        }catch (Exception e){
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
                                 Alert_Dailog.showNetworkAlert(View_All_Events.this);
                            }else {
                                Toast.makeText(View_All_Events.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(View_All_Events.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

}