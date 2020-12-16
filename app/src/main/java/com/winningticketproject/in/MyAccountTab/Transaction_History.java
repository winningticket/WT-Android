package com.winningticketproject.in.MyAccountTab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;


public class Transaction_History extends AppCompatActivity {

    ListView viewevent_listview;
    TextView cancel_purchage,account_title;
     Typeface tf;
    String auth_token="";
    ProgressDialog pd;
    int current_page=1,total_pages=0,position;
    boolean loadingMore=false;

    ArrayList<Transaction_data> transaction_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_transaction__history);



        tf = Typeface.createFromAsset(getApplicationContext().getAssets(),"Montserrat-Medium.ttf");
        pd = new ProgressDialog(Transaction_History.this);

        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        viewevent_listview = findViewById(R.id.transaction_listview);
        cancel_purchage = findViewById(R.id.cancel_purchage);
        cancel_purchage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
finish();            }
        });

        account_title = findViewById(R.id.account_title);
        account_title.setText("Transaction History");

        account_title.setTypeface(tf);
        cancel_purchage.setTypeface(tf);

        auth_token = get_auth_token("auth_token");

        if (!isNetworkAvailable()) {

            alertdailogbox();
        }
        else
        {
            makeJsonArrayRequest(Splash_screen.url+"payments/transaction_history");
        }

        current_page=1;
        viewevent_listview.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!pd.isShowing()) {

                    if (totalItemCount >= 20) {
                        if (firstVisibleItem + visibleItemCount == totalItemCount && !(loadingMore)) {
                            if (current_page <= total_pages) {
                                if (!isNetworkAvailable()) {
                                    Toast.makeText(getApplicationContext(), "You don't have internet connection.", Toast.LENGTH_LONG).show();
                                } else {
                                    try {
                                        makeJsonArrayRequest(Splash_screen.url +"payments/transaction_history?page=" + current_page);
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


        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

    }

    private void makeJsonArrayRequest(String url) {

        if(!pd.isShowing())
        {
            pd.show();
        }
        current_page++;
        position=transaction_data.size();
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.GET,url ,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            JSONArray arr = response.getJSONArray("transactions");
                            JSONObject objects = response.getJSONObject("meta");
                            total_pages = objects.getInt("total_pages");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject person = arr.getJSONObject(i);

                                transaction_data.add(new Transaction_data(person.getString("id"),
                                        person.getString("transaction_type"),
                                        person.getString("amount"),
                                        person.getString("created_at"),
                                        person.getString("id")));

                            }

                            Transactionhistory_Adapter allevents = new Transactionhistory_Adapter(Transaction_History.this,transaction_data);
                            viewevent_listview.setAdapter(allevents);
                            viewevent_listview.setSelectionFromTop(position, 0);
                            loadingMore = false;
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
                                Alert_Dailog.showNetworkAlert(Transaction_History.this);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Toast.makeText(Transaction_History.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(Transaction_History.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
        // Adding request to request que}


    public void alertdailogbox(){
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
                if (!isNetworkAvailable()) {

                    alertdailogbox();
                }
                else
                {
                    makeJsonArrayRequest(Splash_screen.url + "payments/transaction_history");
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

}
