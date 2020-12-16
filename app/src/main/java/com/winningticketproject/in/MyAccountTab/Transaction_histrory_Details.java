package com.winningticketproject.in.MyAccountTab;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class Transaction_histrory_Details extends AppCompatActivity {

    TextView cancel_purchage, account_title,transation_id,amount,tv_transation_type,create_date,payment_type,pay_ment_type,donation_recipient,donation_values,event_name,name_of_event;
    String transaction_id="",auth_token="";
    RelativeLayout relativeLayout;
    LinearLayout add_funds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_histrory__details);


        cancel_purchage = findViewById(R.id.cancel_purchage);
        transation_id = findViewById(R.id.transation_id);
        amount = findViewById(R.id.amount);
        tv_transation_type = findViewById(R.id.tv_transation_type);
        create_date = findViewById(R.id.create_date);

        payment_type = findViewById(R.id.payment_type);
        pay_ment_type = findViewById(R.id.pay_ment_type);
        donation_recipient = findViewById(R.id.donation_recipient);
        donation_values = findViewById(R.id.donation_values);
        event_name = findViewById(R.id.event_name);
        name_of_event = findViewById(R.id.name_of_event);

        account_title = findViewById(R.id.account_title);
        account_title.setTypeface(medium);
        account_title.setText("Transaction History");

        Intent i = getIntent();
        transaction_id = i.getStringExtra("id");

        auth_token = get_auth_token("auth_token");


        add_funds = findViewById(R.id.layout_below);
        relativeLayout = findViewById(R.id.relativeLayout);


        transation_id.setTypeface(medium);
        amount.setTypeface(medium);
        tv_transation_type.setTypeface(regular);
        create_date.setTypeface(regular);
        payment_type.setTypeface(regular);
        pay_ment_type.setTypeface(medium);
        donation_recipient.setTypeface(regular);
        donation_values.setTypeface(medium);
        event_name.setTypeface(regular);
        name_of_event.setTypeface(medium);

        cancel_purchage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

        methodtogenrateclienttoken();
    }



    private void methodtogenrateclienttoken() {
        com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().showProgress(this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"payments/transaction_details?id="+transaction_id,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject transaction = response.getJSONObject("transaction");
                            String transation_type = transaction.getString("transaction_type").substring(0,1).toUpperCase() +transaction.getString("transaction_type").substring(1);
                            String transation_order_id = transaction.getString("id");
                            String transation_create_date = transaction.getString("created_at");
                            double transation_amount = Double.parseDouble(transaction.getString("amount"));

                            transation_id.setText("#"+transation_order_id);

                            amount.setText(String.format("$ %.2f", transation_amount));

                            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                            java.util.Date date = null;
                            try
                            {
                                date = format.parse(transation_create_date);
                            }
                            catch (ParseException e)
                            {

                                //nothing
                            }
                            java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("MMMM dd, yyyy");
                            java.text.SimpleDateFormat postFormater2 = new java.text.SimpleDateFormat("HH:mm aa");
                            create_date.setText(postFormater.format(date) +" - " +postFormater2.format(date).replace("am","AM").replace("pm","PM")+" EST");

                            JSONObject order=null;
                            if (!transation_type.equals("Withdrawal")){
                             order = transaction.getJSONObject("order");
                            String braintree_payment_type = order.getString("braintree_payment_type");
                            String card_type = order.getString("card_type");
                            String card_last_two = order.getString("card_last_two");
                            String paypal_email = order.getString("paypal_email");


                            if (braintree_payment_type.equals("CreditCard")){

                                if(card_type.equals("") || card_type.equals("null") || card_type.equals(null)){
                                    card_type="";
                                }

                                if(card_last_two.equals("") || card_last_two.equals("null") || card_last_two.equals(null)){
                                    card_last_two="Not Mentioned";
                                }else
                                {
                                    card_last_two=" - XXXXXXXXXX"+card_last_two;
                                }

                                pay_ment_type.setText(card_type+card_last_two);


                            }else {

                                if(paypal_email.equals("") || paypal_email.equals("null") || paypal_email.equals(null)){
                                    paypal_email="";
                                }else
                                {
                                    paypal_email=" - "+paypal_email;
                                }

                                pay_ment_type.setText("PayPal"+paypal_email);
                             }
                            }

                            if(transation_type.equals("Add_funds")){
                                relativeLayout.setBackgroundResource(R.drawable.add_fund_ring);
                                tv_transation_type.setText("Add Funds");
                            }else if(transation_type.equals("Withdrawal")){
                                relativeLayout.setBackgroundResource(R.drawable.donate_fund_ring);
                                add_funds.setVisibility(View.GONE);
                                tv_transation_type.setText("Withdrawal");
                            }else {
                                donation_recipient.setVisibility(View.VISIBLE);
                                donation_values.setVisibility(View.VISIBLE);
                                event_name.setVisibility(View.VISIBLE);
                                name_of_event.setVisibility(View.VISIBLE);
                                relativeLayout.setBackgroundResource(R.drawable.donate_fund_ring);
                                tv_transation_type.setText(transation_type);

                                String donation_recip = order.getString("organization_name");
                                String event_names = order.getString("event_name");

                                if(event_names.equals("") || event_names.equals("null") || event_names.equals(null)){
                                    event_names = "Not Mentioned";
                                }else {
                                    event_names = order.getString("event_name");
                                }



                                if(donation_recip.equals("") || donation_recip.equals("null") || donation_recip.equals(null)){
                                    donation_recip = "Not Mentioned";
                                }else {
                                    donation_recip = order.getString("organization_name");
                                }

                                name_of_event.setText(event_names.substring(0,1).toUpperCase() +event_names.substring(1));
                                donation_values.setText(donation_recip.substring(0,1).toUpperCase() +donation_recip.substring(1));
                            }

                            ProgressDialog.getInstance().hideProgress();

                        }catch (Exception E){
                            //nothing
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
                                Alert_Dailog.showNetworkAlert(Transaction_histrory_Details.this);
                            }else {
                                Toast.makeText(Transaction_histrory_Details.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Transaction_histrory_Details.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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

}
