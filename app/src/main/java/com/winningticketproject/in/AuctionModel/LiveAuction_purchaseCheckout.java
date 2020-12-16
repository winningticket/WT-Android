package com.winningticketproject.in.AuctionModel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.EventTab.Purchage_Winning_Ticket;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class LiveAuction_purchaseCheckout extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {
    TextView cancel_purchage,wining_title,winning_code,winning_amount,winnig_sub_total,subtotal,wining_amount,totla,toolbar_title;
    Button preview_order;
    Animation animFadein;
    Intent in;
    String id;
    String ticket_amount;
    String name;
    String order_item_id;
    String auth_token="";
    String locations;
    String code;
    double ticket_amounts=0;
    DecimalFormat df;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liveauction_checkout__page);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        auth_token = get_auth_token("auth_token");


        in = getIntent();
        df = new DecimalFormat("0.00");
        id = in.getStringExtra("id");

        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(),"Internet not available, Please check your internet connection",Toast.LENGTH_LONG).show();
        }
        else {
            methodtocalorderpreview();
        }

        wining_title = findViewById(R.id.wining_title);
        winning_code = findViewById(R.id.winning_code);
        winning_amount = findViewById(R.id.winning_amount);
        winnig_sub_total = findViewById(R.id.winnig_sub_total);
        subtotal = findViewById(R.id.subtotal);
        wining_amount = findViewById(R.id.wining_amount);
        totla = findViewById(R.id.totla);

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Silent Auction");

        preview_order = findViewById(R.id.preview_order);
        preview_order.setOnClickListener(this);


        cancel_purchage = findViewById(R.id.cancel_purchage);
        cancel_purchage.setOnClickListener(this);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.side_down);
        animFadein.setAnimationListener(this);

        wining_title.setTypeface(medium);
        winning_code.setTypeface(regular);
        winning_amount.setTypeface(medium);
        winnig_sub_total.setTypeface(medium);
        subtotal.setTypeface(medium);
        wining_amount.setTypeface(medium);
        totla.setTypeface(medium);
        preview_order.setTypeface(medium);
        cancel_purchage.setTypeface(medium);
        toolbar_title.setTypeface(medium);

//        LinearLayout backtouch = findViewById(R.id.backtouch);
//        backtouch.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                finish();
//                return false;
//            }
//        });

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.preview_order:
                in = new Intent(getApplicationContext(), Purchage_Winning_Ticket.class);
                in.putExtra("order_item_id", order_item_id);
                in.putExtra("wallet_status","0");
                in.putExtra("pageliveauction","1");
                in.putExtra("name",name);
                in.putExtra("location",locations);
                in.putExtra("price",ticket_amount+"");
                in.putExtra("code",code);
                savestring("name",name+"");
                savestring("ticket_price",ticket_amount+"");
                savestring("play_type","free");
                savestring("quantity",1+"");
                in.putExtra("order_id",order_item_id);
                in.putExtra("wallete",get_auth_token("wallete")+"");
                startActivity(in);
                finish();
                 break;
            case R.id.cancel_purchage:
                finish();
                break;
        }
    }



    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    public void methodtocalorderpreview(){
        try {
            com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().showProgress(this);
        } catch (Exception E) {
            //nothing
        }
        final String url;

        if (in.getStringExtra("type").equals("buyitnow")){
            url = "auction/buy_now_auction_order/"+id;
        }else {
            url = "auction/auction_order/"+id;
        }

        System.out.println("-------url--------"+url);

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url + url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println(url+"----wallete----"+response);

                            if (response.getString("status").equals("Success")) {
                                order_item_id = response.getString("order_id");
                                name = response.getString("organization_name").replaceAll("\\s+", " ");
                                locations = response.getString("item_name").replaceAll("\\s+", " ");
                                code = response.getString("code");

                                if (locations.equals("null") || locations.equals("") || locations.equals(null)) {
                                    winning_code.setText("Not Mentioned");
                                } else {
                                    winning_code.setText(code+" - "+locations.substring(0, 1).toUpperCase() + locations.substring(1).trim());
                                }

                                if (name.equals("null") || name.equals("") || name.equals(null)) {
                                    wining_title.setText("Not Mentioned");

                                } else {
                                    wining_title.setText(name.substring(0, 1).toUpperCase() + name.substring(1).trim());

                                }

                                ticket_amounts = Double.parseDouble(response.getString("price"));
                                ticket_amount = df.format(ticket_amounts);


                                winning_amount.setText("$" + ticket_amount);
                                subtotal.setText("$" + ticket_amount);
                                totla.setText("$" + ticket_amount);

                            }else {
                                Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                                finish();
                            }

                        } catch (Exception e) {

                            //nothing
                        }

                        com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(LiveAuction_purchaseCheckout.this);
                            }else {
                                Toast.makeText(LiveAuction_purchaseCheckout.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(LiveAuction_purchaseCheckout.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
        myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");



    }
}
