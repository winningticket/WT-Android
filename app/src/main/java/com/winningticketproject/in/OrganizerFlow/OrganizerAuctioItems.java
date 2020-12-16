package com.winningticketproject.in.OrganizerFlow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.BuildConfig;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

public class OrganizerAuctioItems extends AppCompatActivity implements View.OnClickListener {

    Spinner auction_itmes;
    ArrayList<Org_Auction_List_Data_> auction_list_data = new ArrayList<>();
    ArrayList<String>  auction_name = new ArrayList<>();
    TextView auction_status;
    Button buy_it_now;
    RelativeLayout relative_layout;
    android.support.v7.app.AlertDialog alertDialog;
    String auction_id;
    String pay_amount;
    ArrayList<String> user_name = new ArrayList<>();
    ArrayList<String> user_id = new ArrayList<>();
    String user_id_create="",amount_type;
    private static final int REQUEST_SCAN = 100;
    TextView title_text;
    String current_user_winner_name="";
    String stripe_token;
    ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_auctio_items);

        auction_status = findViewById(R.id.auction_status);
        auction_status.setTypeface(regular);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        buy_it_now = findViewById(R.id.buy_it_now);
        buy_it_now.setTypeface(medium);
        buy_it_now.setOnClickListener(this);

        relative_layout = findViewById(R.id.relative_layout);

        title_text = findViewById(R.id.title_text);
        title_text.setTypeface(medium);

        method_to_call_api();

        load_user_data();

        auction_itmes = findViewById(R.id.auction_itmes);

        auction_itmes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                auction_id = auction_list_data.get(position).getAuctionid();
                if (auction_list_data.get(position).getAuction_is_live().equals("false") && auction_list_data.get(position).getAuction_is_expired().equals("false")){
                    String all_date = "Item Status: <b>Upcoming</b><br><br>"+"Buy It Now: <b>$"+
                            String.format("%.2f",Double.parseDouble(auction_list_data.get(position).getAuction_buy_price()));
                    auction_status.setText(Html.fromHtml(all_date));
                    buy_it_now.setVisibility(View.GONE);

                } else if (auction_list_data.get(position).getAuction_is_live().equals("true")){
                    if (auction_list_data.get(position).getAuction_payment_status().equals("not_paid")){
                        String current_bid;
                        if (Integer.parseInt(auction_list_data.get(position).getAuction_bid_count())>0){
                            current_bid = Double.parseDouble(auction_list_data.get(position).getAuction_current_bid_amount())+"";
                        }else{
                            current_bid="0";
                        }

                        if (!auction_list_data.get(position).getAuction_current_bid_amount().equals("null")){

                            if (Double.parseDouble(auction_list_data.get(position).getAuction_current_bid_amount())>Double.parseDouble(auction_list_data.get(position).getAuction_buy_price())){
                                String all_date = "Item Status: <b>Live</b><br><br>Current Bid: <b>$"+
                                String.format("%.2f",Double.parseDouble(current_bid))
                                        +"</b><br><br>Bid Increment: <b>$"+
                                        String.format("%.2f",Double.parseDouble(auction_list_data.get(position).getAuction_item_number()))+"</b><br><br>Buy It Now: <b>$"+
                                        String.format("%.2f",Double.parseDouble(auction_list_data.get(position).getAuction_buy_price()));

                                auction_status.setText(Html.fromHtml(all_date));
                                buy_it_now.setText("Buy it now");
                                buy_it_now.setVisibility(View.VISIBLE);
                                pay_amount = auction_list_data.get(position).getAuction_buy_price();
                                amount_type = "1";

                            }else{

                                String all_date = "Item Status: <b>Live</b><br><br>Current Bid: <b>$"+
                                        String.format("%.2f",Double.parseDouble(current_bid))
                                        +"</b><br><br>Bid Increment: <b>$"+
                                        String.format("%.2f",Double.parseDouble(auction_list_data.get(position).getAuction_item_number()))+"</b><br><br>Buy It Now: <b>$"+
                                        String.format("%.2f",Double.parseDouble(auction_list_data.get(position).getAuction_buy_price()));

                                auction_status.setText(Html.fromHtml(all_date));
                                buy_it_now.setText("Buy it now");
                                buy_it_now.setVisibility(View.VISIBLE);
                                pay_amount = auction_list_data.get(position).getAuction_buy_price();
                                amount_type = "1";

                            }
                        }else {

                            String all_date = "Item Status: <b>Live</b><br><br>Current Bid: <b>$"+
                                    String.format("%.2f",Double.parseDouble(current_bid))
                                    +"</b><br><br>Bid Increment: <b>$"+
                                    String.format("%.2f",Double.parseDouble(auction_list_data.get(position).getAuction_item_number()))+"</b><br><br>Buy It Now: <b>$"+
                                    String.format("%.2f",Double.parseDouble(auction_list_data.get(position).getAuction_buy_price()));

                            auction_status.setText(Html.fromHtml(all_date));
                            buy_it_now.setText("Buy it now");
                            buy_it_now.setVisibility(View.VISIBLE);
                            pay_amount = auction_list_data.get(position).getAuction_buy_price();
                            amount_type = "1";
                        }
                    }else {
                        String all_date = "Item Status: <b>Purchased</b><br><br>Winning Bid: <b>$"+
                                String.format("%.2f",Double.parseDouble(auction_list_data.get(position).getWinning_paid_amount()));

                        auction_status.setText(Html.fromHtml(all_date));
                        buy_it_now.setVisibility(View.GONE);

                    }
                }else if (auction_list_data.get(position).getAuction_is_expired().equals("true")){

                    if (auction_list_data.get(position).getAuction_payment_status().equals("not_paid")){

                        if (Integer.parseInt(auction_list_data.get(position).getAuction_bid_count())>0){

                            String all_date = "Item Status: <b>Unpurchased</b><br><br>Winning Bid: <b>$"+
                            String.format("%.2f",Double.parseDouble(auction_list_data.get(position).getAuction_current_bid_amount()));

                            auction_status.setText(Html.fromHtml(all_date));
                            pay_amount = auction_list_data.get(position).getAuction_current_bid_amount();
                            buy_it_now.setVisibility(View.VISIBLE);
                            buy_it_now.setText("Take payment from winner");
                            amount_type = "0";

                            current_user_winner_name = auction_list_data.get(position).getUser_name();
                            user_id_create = auction_list_data.get(position).getUser_id();

                        }else {
                            String all_date = "Item Status: <b>Auction Closed</b><br><br>" + "No one bidded for this item";
                            auction_status.setText(Html.fromHtml(all_date));
                            buy_it_now.setVisibility(View.GONE);
                        }

                    }else {

                        String all_date = "Item Status: <b>Purchased</b><br><br>Winning Bid: <b>$"+
                        String.format("%.2f",Double.parseDouble(auction_list_data.get(position).getWinning_paid_amount()));
                        auction_status.setText(Html.fromHtml(all_date));
                        buy_it_now.setVisibility(View.GONE);

                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    private void method_to_call_api() {
        auction_name.clear();
        ProgressDialog.getInstance().showProgress(this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.imageurl+"api/v3/auction/list/"+getIntent().getStringExtra("id"),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("----respo---"+response);

                    ProgressDialog.getInstance().hideProgress();

                        JSONArray auction_items = response.getJSONArray("auction_items");
                        for (int i = 0; i < auction_items.length(); i++) {
                            JSONObject eventobject = auction_items.getJSONObject(i);
                            String name = eventobject.getString("name");

                            if (name.equals("") || name.equals("null") || name.equals(null)) {
                                name = "Not Mentioned";
                            } else {
                                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                            }
                            auction_name.add("#"+eventobject.getString("auction_item_number")+" - "+name);
                            auction_list_data.add(
                                    new Org_Auction_List_Data_(eventobject.getString("id"),
                                            name,
                                            eventobject.getString("event_start_date"),
                                            eventobject.getString("event_end_date"),
                                            eventobject.getString("starting_bid"),
                                            eventobject.getString("item_image"),
                                            eventobject.getString("payment_status"),
                                            eventobject.getString("bid_count"),
                                            eventobject.getString("current_bid_amount"),
                                            String.valueOf(eventobject.getBoolean("is_live?")),
                                            String.valueOf(eventobject.getBoolean("is_expired?")),
                                            eventobject.getInt("buy_price") + "",
                                            eventobject.getString("minimum_bid_increment"),
                                            eventobject.getString("winning_bid_amount"),
                                            eventobject.getString("winning_paid_amount"),
                                            eventobject.getString("current_bid_user_id"),
                                            eventobject.getString("current_bid_user_name")));
                        }

                        if (auction_list_data.size()==0){
                            relative_layout.setVisibility(View.GONE);
                            auction_status.setText("No auction available for this event");
                        }else {

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrganizerAuctioItems.this, R.layout.organizer_dropdown,auction_name ) {
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View v = super.getView(position, convertView, parent);
                                    ((TextView) v).setTypeface(regular);
                                    return v;
                                }

                                public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                                    View v =super.getDropDownView(position, convertView, parent);
                                    ((TextView) v).setTypeface(regular);
                                    ((TextView) v).setTextColor(Color.BLACK);
                                    return v;
                                }
                            };

                    adapter.setDropDownViewResource(R.layout.custom_text);
                    auction_itmes.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                    }


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
                                Alert_Dailog.showNetworkAlert(OrganizerAuctioItems.this);
                            }else {
                                Toast.makeText(OrganizerAuctioItems.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(OrganizerAuctioItems.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

    }

    private void load_user_data() {
        auction_name.clear();
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.imageurl+"api/v3/auction/users_list/"+getIntent().getStringExtra("id"),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("----users---"+response);
                    JSONArray auction_items = response.getJSONArray("users");
                    for (int i = 0; i < auction_items.length(); i++) {
                        JSONObject user_data = auction_items.getJSONObject(i);
                        user_id.add(user_data.getString("id"));
                        user_name.add(user_data.getString("first_name") +" "+user_data.getString("last_name"));
                    }
                }catch (Exception e){
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
                                Alert_Dailog.showNetworkAlert(OrganizerAuctioItems.this);
                            }else {
                                Toast.makeText(OrganizerAuctioItems.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(OrganizerAuctioItems.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.buy_it_now:
                open_popup_payment();
                break;

            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void open_popup_payment() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(OrganizerAuctioItems.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) OrganizerAuctioItems.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogview = inflater.inflate(R.layout.custome_enter_email, null);

        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;
        TextView pop_up_title = alertDialog.findViewById(R.id.pop_up_title);
        TextView pop_up_sub_title = alertDialog.findViewById(R.id.pop_up_sub_title);

        Button  submit =  alertDialog.findViewById(R.id.submit);

        Button  new_user =  alertDialog.findViewById(R.id.new_user);
        new_user.setTypeface(medium);


        TextView cancel = alertDialog.findViewById(R.id.cancel);
        pop_up_title.setTypeface(regular);
        pop_up_sub_title.setTypeface(medium);

        AutoCompleteTextView email_text =  alertDialog.findViewById(R.id.et_eamil_type);
        cancel.setTypeface(medium);
        submit.setTypeface(medium);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent intent = new Intent(OrganizerAuctioItems.this, ScanAndPay.class);
                intent.putExtra("auction_id",auction_id);
                intent.putExtra("amount",pay_amount);
                intent.putExtra("event_id",getIntent().getStringExtra("id"));
                intent.putExtra("amount_type",amount_type);
                startActivity(intent);
            }
        });

        if (user_id_create.equals("")){
            email_text.setEnabled(true);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,user_name);
            //Getting the instance of AutoCompleteTextView
            email_text.setTypeface(medium);
            email_text.setThreshold(1);//will start working from first character
            email_text.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
            email_text.setTextColor(Color.BLACK);
        }else {
            new_user.setVisibility(View.GONE);
            email_text.setText(current_user_winner_name);
            email_text.setEnabled(false);
            pop_up_sub_title.setText("User");
            email_text.setHint("Search Users");
        }

        email_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                String selection = (String)parent.getItemAtPosition(pos);
                int postion_index = user_name.indexOf(selection);
                user_id_create = user_id.get(postion_index);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiddenInputMethod();
                if(email_text.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_SHORT).show();
                }else {
                    alertDialog.dismiss();
                    hiddenInputMethod();
                    try {
                        JSONObject object = new JSONObject();
                        object.put("amount",pay_amount);
                        object.put("event_id",getIntent().getStringExtra("id"));
                        object.put("auction_item_id",auction_id);
                        object.put("user_id",user_id_create);
                        object.put("auction_order_type",Integer.parseInt(amount_type));
                        method_to_call_token(object);
                    }catch (Exception e){ }
                }
            }
        });
        alertDialog.show();
    }


    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @Override
    protected void onResume() {
        super.onResume();
        user_id_create="";
    }

    // create token
    private void getStripeToken(String card_number,int exp_month,int year,String cvv) {
        ProgressDialog.getInstance().showProgress(this);
        Card card = Card.create(card_number,exp_month,year,cvv);
        com.stripe.android.Stripe Stripe2 = null;
        Stripe2  = new Stripe(this, BuildConfig.STRIPE_PUBLIC_KEY);
        Stripe2.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("amount",pay_amount);
                            stripe_token = token.getId();
                            object.put("event_id",getIntent().getStringExtra("id"));
                            object.put("auction_item_id",auction_id);
                            object.put("user_id",user_id_create);
                            object.put("auction_order_type",Integer.parseInt(amount_type));
                            method_to_call_token(object);
                            System.out.println("----------"+object);
                        }catch (Exception e){
                        }
                    }
                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(OrganizerAuctioItems.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void method_to_call_token(JSONObject object ) {
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.imageurl+"api/v3/auction/order/create",object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                ProgressDialog.getInstance().hideProgress();
                                Intent in = new Intent(OrganizerAuctioItems.this,Organiser_Billing_Add.class);
                                in.putExtra("order_id", response.getString("order_id"));
                                in.putExtra("f_name",response.getString("user_first_name"));
                                in.putExtra("l_name",response.getString("user_last_name"));
                                in.putExtra("user_email",response.getString("user_email"));
                                startActivity(in);
                        }catch (Exception E){
                            //nothing
                            ProgressDialog.getInstance().hideProgress();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ProgressDialog.getInstance().hideProgress();
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                        }catch (Exception e){

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

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_SCAN ) && data != null
                && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard result = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
            if (result != null) {
                getStripeToken(result.getFormattedCardNumber(),result.expiryMonth,result.expiryYear,result.cvv);
            }
        }
    }

}
