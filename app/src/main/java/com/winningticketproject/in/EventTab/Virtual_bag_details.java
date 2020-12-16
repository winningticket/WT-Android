package com.winningticketproject.in.EventTab;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.R;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Virtual_bag_details extends AppCompatActivity implements View.OnClickListener {
    TextView virtual_bag_title,offere_expire;
    Intent in;
    String id = "", auth_token,gift_vocher_id;
    ImageView gift_bag_iamge;
    WebView offer_direction_detail;
    ImageButton btn_virtual_gift_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_bag_details);

        virtual_bag_title = findViewById(R.id.virtual_bag_title);
//        offer_expire_detail = (TextView) findViewById(R.id.offer_expire_detail);
        offer_direction_detail = findViewById(R.id.offer_direction_detail);
//        offer_code = (TextView) findViewById(R.id.offer_code);

        btn_virtual_gift_back = findViewById(R.id.btn_virtual_gift_back);

        gift_bag_iamge = findViewById(R.id.gift_bag_iamge);
        offere_expire = findViewById(R.id.offere_expire);

        auth_token = get_auth_token("auth_token");

        in = getIntent();
        id = in.getStringExtra("id");
        gift_vocher_id = in.getStringExtra("vocher_id");

        if (!isNetworkAvailable()) {

            alertdailogbox();
        }
        else
        {
            methodforcalleventlist();
        }

        offere_expire.setTypeface(regular);
        virtual_bag_title.setTypeface(semibold);

        btn_virtual_gift_back.setOnClickListener(this);

    }


    public void alertdailogbox() {
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

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (!isNetworkAvailable()) {

                    alertdailogbox();
                } else {
                    if (!isNetworkAvailable()) {

                        alertdailogbox();
                    } else {
                        methodforcalleventlist();
                    }
                }

            }


        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }


    private void methodforcalleventlist() {

        JSONObject event_id = new JSONObject();
        try {
            event_id.put("event_id",id);
            event_id.put("virtual_gift_bag_id",gift_vocher_id);

        }catch (Exception e){
            //nothing
        }

        ProgressDialog.getInstance().showProgress(this);

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST, Splash_screen.url+"virtual_gift_bag/details",event_id,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println(response);

                            JSONObject virtual_gift_bag_details = response.getJSONObject("virtual_gift_bag_details");

                            String item_name = virtual_gift_bag_details.getString("item_name");
                            String item_directions = virtual_gift_bag_details.getString("item_directions");
                            String offer_expires_date = virtual_gift_bag_details.getString("offer_expires_date");
                            String offer_does_not_expire = virtual_gift_bag_details.getString("offer_does_not_expire");
                            String image = virtual_gift_bag_details.getString("image");

                            virtual_bag_title.setText(item_name.substring(0, 1).toUpperCase() + item_name.substring(1));

                            offer_direction_detail.setBackgroundColor(0);
                            offer_direction_detail.loadData(item_directions, "text/html; charset=utf-8", "UTF-8");


                            if (offer_does_not_expire.equals("true")){
//                                offer_expire_detail.setVisibility(View.VISIBLE);
                               if (offer_expires_date.equals(null) || offer_does_not_expire.equals("null")){
                                   offere_expire.setText("This offer does not expire.");
                               }else {
                                   java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                   java.util.Date date = null;
                                   try {
                                       date = format.parse(offer_expires_date);
                                       java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("MMMM dd yyyy");
                                       String newDateStr = postFormater.format(date);
                                       offere_expire.setText(Html.fromHtml("<b>Offer Expires: </b>"+newDateStr));
                                   } catch (ParseException e) {//nothing
                                       offere_expire.setText("Not Mentioned");
                                   }
                               }

                                }else {
                                offere_expire.setText("Not Mentioned");
                            }

                            Picasso.with(Virtual_bag_details.this)
                                    .load(image).error(R.drawable.auction_event_empty).placeholder(R.drawable.auction_event_empty)
                                    .into( gift_bag_iamge);


                        }catch (Exception e){
                            //nothing
                        }
                        ProgressDialog.getInstance().hideProgress();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                ProgressDialog.getInstance().hideProgress();
                                Alert_Dailog.showNetworkAlert(Virtual_bag_details.this);
                            }else {
                                ProgressDialog.getInstance().hideProgress();
                                Toast.makeText(Virtual_bag_details.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            ProgressDialog.getInstance().hideProgress();
                            Toast.makeText(Virtual_bag_details.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_virtual_gift_back:
                finish();
                break;


        }
    }

}
