package com.winningticketproject.in.OrganizerFlow;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.winningticketproject.in.AppInfo.AllEventGetterSetter;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Login_Screen;
import com.winningticketproject.in.SignInSingup.Splash_screen;
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

public class EventListFlow extends AppCompatActivity {

    Toolbar toolbar;
    ListView viewevent_listview;
    String auth_token="";
    private List<AllEventGetterSetter> viewallevents = new ArrayList<AllEventGetterSetter>();
    TextView view_all;
    OrganizerAllEvents allevents;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_flow);


        viewevent_listview = findViewById(R.id.orgniser_eventlist);
        auth_token = get_auth_token("auth_token");

        toolbar = findViewById(R.id.orgniser_tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("All Events");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewallevents.clear();

        view_all = findViewById(R.id.view_all);
        view_all.setTypeface(regular);

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
        } else {
            methodforcalleventlist(Splash_screen.imageurl + "api/v3/events");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_org_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout_method();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout_method() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EventListFlow.this);
        builder.setCancelable(false);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Share_it.remove_key("auth_token");
                Share_it.remove_key("role");
                Share_it.remove_key("saveLogin");
                Share_it.remove_key("checked");
                Toast.makeText(EventListFlow.this, "Successfully Logout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EventListFlow.this, Login_Screen.class));
                finish();

            }
        });
        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();

    }


    public void methodforcalleventlist(String url){
        ProgressDialog.getInstance().showProgress(this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("----respo---"+response);
                            JSONArray allevent = response.getJSONArray("events");
                            for (int i=0;i<allevent.length();i++){
                                JSONObject eventobject = allevent.getJSONObject(i);

                                viewallevents.add(new AllEventGetterSetter(eventobject.getString("id"),
                                        eventobject.getString("name").replaceAll("\\s+"," "),
                                        eventobject.getString("converted_start_date"),
                                        eventobject.getString("avatar_url"),
                                        "",
                                        eventobject.getString("event_status"),eventobject.getInt("push_notification_count")));
                            }

                            allevents = new OrganizerAllEvents(EventListFlow.this,viewallevents);
                            viewevent_listview.setAdapter(allevents);
                            if (viewallevents.isEmpty()) {
                                viewevent_listview.setVisibility(View.GONE);
                                view_all.setVisibility(View.VISIBLE);
                            }
                            else {
                                viewevent_listview.setVisibility(View.VISIBLE);
                                view_all.setVisibility(View.GONE);
                            }
                            ProgressDialog.getInstance().hideProgress();

                        }catch (Exception e){
                            ProgressDialog.getInstance().hideProgress();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        ProgressDialog.getInstance().hideProgress();
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(EventListFlow.this);
                            }else {
                                Toast.makeText(EventListFlow.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(EventListFlow.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(),"Please click BACK again to exit",Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }




}
