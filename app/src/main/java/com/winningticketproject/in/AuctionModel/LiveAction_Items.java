package com.winningticketproject.in.AuctionModel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.viewpagerindicator.CirclePageIndicator;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.Auction_List_Data;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.SignInSingup.Login_Screen;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.Adapter.SlidingImage_Adapter;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.item_Data;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;
import static com.winningticketproject.in.AppInfo.Share_it.ConvertDate;
import static com.winningticketproject.in.AppInfo.Share_it.getCurrentDate;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class LiveAction_Items extends AppCompatActivity implements View.OnClickListener {
    TextView noofpostion, cancel_purchage, toolbar_title, items_titles, item_amount, bidding_row,bidding_msg,item_description, description_content, similar_item,watch_bidcoutn,menu_item,view_all,details_bid_counts,bid_type,watch_icon;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    SlidingImage_Adapter sliding_adpter;
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    private ArrayList<String> image_size = new ArrayList<String>();
    RecyclerView horizontal_recycler_view;
    Similar_ItemList_for_auction horizontalAdapter;
    Button watch_firstlayout, palcebid,checkout_btn;
    ScrollView items_scroll;
    String order_id;
    String auth_token="",id="";
    String name;
    String description;
    String starting_bid;
    String minimum_bid_increment;
    String payment_status;
    String event_start_date;
    String event_end_date;
    String bid_count;
    String winning_amount;
    String current_bid_amount;
    String watchers_count;
    String is_live = "";
    CirclePageIndicator indicator;
    String is_expired;
    String user_watching_status;
    String winner_status;
    String user_paid_status;
    String unique_url_key;
    Intent in;
    public static Typeface semibold;
    public static Typeface regular;
    public static Typeface medium;
    public static Typeface black;
    public static Typeface italic;
    public static Typeface light;
    public static Typeface mediumitalic;
    public static Typeface webfont;
    RelativeLayout viewpager_id,menu_icon;
    android.support.v7.app.AlertDialog alertDialog;
    Integer bid_counts,watchers_counts;
    String URL = Splash_screen.url+"auction/item_detail/";
    Double double_amount=0.0;
    Button button_buy_it_now;
    String buy_price_final="";
    String winning_paid_amount;
    String winning_bid_amount;
    BeforeLiveCountdown beforeLiveCountdown;
    final Handler handler = new Handler();
    String from="";
    AfterLiveCountDown afterLiveCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_action__items);

        Share_it share_it = new Share_it(this);

        semibold = Typeface.createFromAsset(getAssets(), "Montserrat-SemiBold.ttf");
        regular = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
        medium = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");
        black = Typeface.createFromAsset(getAssets(),"Montserrat-Black.ttf");
        italic = Typeface.createFromAsset(getAssets(),"Montserrat-Italic.ttf");
        light = Typeface.createFromAsset(getAssets(),"Montserrat-Light.ttf");
        mediumitalic = Typeface.createFromAsset(getAssets(),"MontserratAlternates-MediumItalic.ttf");
        webfont = Typeface.createFromAsset(getAssets(),"fontawesome-webfont.ttf");

        cancel_purchage = findViewById(R.id.cancel_purchage);
        toolbar_title = findViewById(R.id.toolbar_title);
        cancel_purchage.setOnClickListener(this);

        auth_token = Share_it.get_auth_token("auth_token");

        method_to_declare_all_id();

        menu_icon = findViewById(R.id.menu_icon);
        menu_icon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                share();
                return false;
            }
        });

        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (from.equals("app")) {
                    finish();
                } else {
                    Intent in = new Intent(getApplicationContext(), Splash_screen.class);
                    startActivity(in);
                    finish();
                }
                return false;
            }
        });
    }

    private void method_to_declare_all_id() {

        button_buy_it_now =  findViewById(R.id.button_buy_it_now);
        button_buy_it_now.setTypeface(medium);
        button_buy_it_now.setOnClickListener(this);

        noofpostion = findViewById(R.id.noofpostion);

        menu_item = findViewById(R.id.menu_item);

        items_scroll = findViewById(R.id.items_scroll);
        items_scroll.fullScroll(View.SCROLL_INDICATOR_TOP);

        mPager = findViewById(R.id.pager);
        viewpager_id = findViewById(R.id.viewpager_id);
        viewpager_id.setVisibility(View.GONE);

//        item_title = (TextView) findViewById(R.id.item_title);
        items_titles = findViewById(R.id.items_titles);
        item_amount = findViewById(R.id.item_amount);
        bidding_row = findViewById(R.id.bidding_row);
        item_description = findViewById(R.id.item_description);
        description_content = findViewById(R.id.description_content);
        similar_item = findViewById(R.id.similar_item);
        watch_bidcoutn = findViewById(R.id.watch_bidcoutn);
        bidding_msg = findViewById(R.id.bidding_msg);
        view_all = findViewById(R.id.tv_auction_empty);
        view_all.setTypeface(regular);

        details_bid_counts = findViewById(R.id.details_bid_counts);
        bid_type = findViewById(R.id.bid_type);

        noofpostion.setTypeface(medium);
        // item_title.setTypeface(textfont);
        items_titles.setTypeface(regular);
        item_amount.setTypeface(medium);
        bidding_row.setTypeface(regular);
        bid_type.setTypeface(italic);
        item_description.setTypeface(medium);
        description_content.setTypeface(regular);
        similar_item.setTypeface(medium);
        cancel_purchage.setTypeface(medium);
        toolbar_title.setTypeface(medium);
        bidding_msg.setTypeface(regular);
        details_bid_counts.setTypeface(italic);

        checkout_btn = findViewById(R.id.checkout_btn);
        checkout_btn.setOnClickListener(this);
        checkout_btn.setTypeface(semibold);

        watch_firstlayout = findViewById(R.id.watch_firstlayout);
        watch_firstlayout.setOnClickListener(this);

        palcebid = findViewById(R.id.palcebid);
        watch_firstlayout.setTypeface(medium);
        palcebid.setTypeface(medium);

        cancel_purchage.setOnClickListener(this);
        menu_item.setOnClickListener(this);
        palcebid.setOnClickListener(this);

        handleIntent(getIntent());

        watch_icon = findViewById(R.id.watch_icon);
        watch_icon.setText("\uf06e");
        watch_icon.setTypeface(webfont);

    }


    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            from = "link";
            String recipeId = appLinkData.getLastPathSegment();
            Share_it.savestring ("Auction_id", recipeId);
            methodtocalAPI(1);
            System.out.println(recipeId+"------------"+appLinkData);

        }else {
            if (!isNetworkAvailable()) {
                alertdailogbox("withoutname");
            } else {
                from = "app";
                methodtocalAPI(1);
            }
        }
    }

    public void alertdailogbox(final String name){
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
                if(name.equals("withname"))
                {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("withname");
                    }
                    else
                    {
                        methodtocalAPI(1);                    }
                }
                else
                {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("withoutname");
                    }
                    else {
                        methodtocalAPI(1);                    }
                }
            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }


    private void methodtocalAPI(int values) {
        if (values==1){
            ProgressDialog.getInstance().showProgress(LiveAction_Items.this);
        }
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL+get_auth_token("Auction_id"), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                System.out.println("-------"+response);
                image_size.clear();
                ProgressDialog.getInstance().hideProgress();
                try {
                    JSONObject auction_item = response.getJSONObject("auction_item");

                    toolbar_title.setText("#"+auction_item.getString("auction_item_number"));

                    order_id = auction_item.getString("id");
                    name = auction_item.getString("name");
                    description = auction_item.getString("description");
                    starting_bid = auction_item.getString("starting_bid");
                    minimum_bid_increment = auction_item.getString("minimum_bid_increment");
                    payment_status = auction_item.getString("payment_status");
                    bid_count = auction_item.getString("bid_count");
                    current_bid_amount = auction_item.getString("current_bid_amount");
                    watchers_count = auction_item.getString("watchers_count");
                    is_live = String.valueOf(auction_item.getBoolean("is_live?"));
                    is_expired = String.valueOf(auction_item.getBoolean("is_expired?"));
                    event_start_date = auction_item.getString("event_start_date");
                    event_end_date = auction_item.getString("event_end_date");
                    user_watching_status = String.valueOf(response.getBoolean("user_watching_status"));
                    winner_status = String.valueOf(response.getBoolean("winner_status"));
                    user_paid_status = String.valueOf(response.getBoolean("user_paid_status"));
                    unique_url_key = auction_item.getString("unique_url_key");
                    bid_counts = Integer.parseInt(bid_count);
                    watchers_counts=Integer.parseInt(watchers_count);
                    watch_bidcoutn.setText(watchers_counts+"");

                    savestring("wallete",response.getString("wallet_amount")+"");

                    winning_bid_amount = auction_item.getString("winning_bid_amount");
                    winning_paid_amount = auction_item.getString("winning_paid_amount");

                    buy_price_final = auction_item.getString("buy_price");

                    double buy_it_double = Double.parseDouble(buy_price_final);
                    button_buy_it_now.setText(String.format("Buy it now: $ %.2f", buy_it_double));

                    button_buy_it_now.setVisibility(View.VISIBLE);

                    JSONArray auction_item_images = auction_item.getJSONArray("auction_item_images");
                    for (int j=0;j<auction_item_images.length();j++){
                        JSONObject  image_url = auction_item_images.getJSONObject(j);
                        image_size.add(image_url.getString("image_url"));
                    }

                    if (image_size.size()>0){
                        init();
                        viewpager_id.setVisibility(View.VISIBLE);
                    }else {
                        viewpager_id.setVisibility(View.GONE);
                    }

                    if (name.equals("") || name.equals("null") || name.equals(null)){
                        items_titles.setText("Not Mentioned");
                    }else {
                        items_titles.setText(name.substring(0,1).toUpperCase() +name.substring(1));
                    }


                    if (description.equals("") || description.equals("null") || description.equals(null)){
                        description_content.setText("Not Mentioned");
                    }else {
                        description_content.setText(description.substring(0,1).toUpperCase() +description.substring(1));
                    }

                    JSONArray similar_items = response.getJSONArray("similar_items");
                    List<item_Data> data = new ArrayList<>();
                    for (int j=0;j<similar_items.length();j++){
                        JSONObject items = similar_items.getJSONObject(j);
                        String id = items.getString("id");
                        String images = items.getString("item_image");
                        String title = items.getString("name");
                        String bid_count=items.getString("bid_count");
                        String starting_bid=items.getString("starting_bid");
                        String current_bid_amount=items.getString("current_bid_amount");
                        String payment_status = items.getString("payment_status");
                        String is_live = items.getString("is_live?");
                        String is_expired = items.getString("is_expired?");

                        data.add(new item_Data(id,images,title,bid_count,is_live,is_expired,current_bid_amount,starting_bid,payment_status,"","","","","",ImagesArray));

                    }
                    //data = fill_with_data();
                    horizontal_recycler_view = findViewById(R.id.horizontal_recycler_view);
                    horizontal_recycler_view.setFocusable(false);

                    horizontalAdapter = new Similar_ItemList_for_auction(data, getApplicationContext());
                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(LiveAction_Items.this, LinearLayoutManager.HORIZONTAL, false);
                    horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
                    horizontal_recycler_view.setAdapter(horizontalAdapter);
                    horizontalAdapter.notifyDataSetChanged();

                    if (similar_items.length()<=0) {
                        view_all.setVisibility(View.VISIBLE);
                        horizontal_recycler_view.setVisibility(View.GONE);
                    }
                    else {
                        view_all.setVisibility(View.GONE);
                        horizontal_recycler_view.setVisibility(View.VISIBLE);
                    }

                    if (user_watching_status.equals("true")){
                        watch_firstlayout.setText("WATCHING");
                    }else {
                        watch_firstlayout.setText("WATCH");
                    }

                    if (is_live.equals("false") && is_expired.equals("false")){

                        refresh(10000);

                        if (bid_counts > 1){
                            details_bid_counts.setText(bid_count+"  BIDS");
                        }else if (bid_counts == 1){
                            details_bid_counts.setText(bid_count+"  BID ");
                        } else {
                            details_bid_counts.setText("NO BIDS");
                        }

                        button_buy_it_now.setVisibility(View.GONE);

                        palcebid.setVisibility(View.GONE);
                        watch_firstlayout.setVisibility(View.GONE);
                        bidding_msg.setVisibility(View.VISIBLE);
                        bidding_msg.setText(Html.fromHtml("<b><big><font color='black'>Bidding not yet started</font>.</big><b>"));
                        bidding_row.setVisibility(View.VISIBLE);

                        double pricevalue = Double.parseDouble(starting_bid);
                        DecimalFormat df = new DecimalFormat("0.00");
                        double_amount = Double.parseDouble(df.format(pricevalue));
                        item_amount.setText(String.format("$ %.2f", pricevalue));

                        long different=0;
                        try {
                            different = ConvertDate(event_start_date).getTime() - ConvertDate(getCurrentDate()).getTime();
                        }catch (Exception e){ }

                        if(different>0) {
                            beforeLiveCountdown = new BeforeLiveCountdown(different, 1000);
                            beforeLiveCountdown.start();
                        }

                    }else if (is_live.equals("true") && is_expired.equals("false")){
                        refresh(10000);
                        if (bid_counts > 1){
                            details_bid_counts.setText(bid_count+"  BIDS");
                        }else if (bid_counts == 1){
                            details_bid_counts.setText(bid_count+"  BID ");
                        } else {
                            details_bid_counts.setText("NO BIDS");
                        }

                        if (payment_status.equals("paid")) {

                            details_bid_counts.setText("Sold Price");

                            if (user_paid_status.equals("true")){

                                double pricevalue = Double.parseDouble(buy_price_final);

                                item_amount.setText(String.format("$ %.2f", pricevalue));


                                button_buy_it_now.setVisibility(View.GONE);
                                bidding_msg.setText(Html.fromHtml("<b><big><font color='black'>Congratulations</font>.</big><b><br> <small>You won this item!</small>"));
                                bidding_row.setVisibility(View.GONE);
                                bid_type.setVisibility(View.GONE);
                                watch_firstlayout.setVisibility(View.GONE);

                                checkout_btn.setText("You’ve already checked out!");
                                checkout_btn.setClickable(false);
                                checkout_btn.setBackgroundResource(R.drawable.new_gray_button_bacgound);
                                checkout_btn.setVisibility(View.VISIBLE);


                            }else {

                                double pricevalue = Double.parseDouble(winning_paid_amount);

                                item_amount.setText(String.format("$ %.2f", pricevalue));

                                palcebid.setVisibility(View.GONE);
                                watch_firstlayout.setVisibility(View.GONE);
                                button_buy_it_now.setVisibility(View.GONE);

                                bidding_msg.setText("This item is sold and is no\n" + "longer available.");
                                bidding_row.setVisibility(View.GONE);
                                bid_type.setVisibility(View.GONE);

                                checkout_btn.setText("Sold Price: "+String.format("$ %.2f", pricevalue));
                                checkout_btn.setClickable(false);
                                checkout_btn.setBackgroundResource(R.drawable.new_gray_button_bacgound);
                                checkout_btn.setVisibility(View.VISIBLE);

                            }

                        }else {

                            methodenddate();

                            bidding_msg.setVisibility(View.GONE);
                            bidding_row.setVisibility(View.VISIBLE);
                            bid_type.setVisibility(View.VISIBLE);
                            checkout_btn.setVisibility(View.GONE);

                            palcebid.setVisibility(View.VISIBLE);
                            watch_firstlayout.setVisibility(View.VISIBLE);
                            button_buy_it_now.setVisibility(View.VISIBLE);

                        }

                    }else if (is_expired.equals("true")) {

                        button_buy_it_now.setVisibility(View.GONE);
                        palcebid.setVisibility(View.GONE);

                        if (payment_status.equals("paid")) {
                            details_bid_counts.setText("Sold Price");

                            if (user_paid_status.equals("true") || winner_status.equals("true")) {

                                if (winning_paid_amount.equals(null) || winning_paid_amount.equals("null")) {
                                    winning_amount = winning_bid_amount;
                                } else {
                                    winning_amount = winning_paid_amount;
                                }

                                item_amount.setText(String.format("$ %.2f", Double.parseDouble(winning_amount)));

                                bidding_msg.setText(Html.fromHtml("<b><big><font color='black'>Congratulations</font>.</big><b><br> <small>You won this item!</small>"));
                                bidding_row.setVisibility(View.GONE);
                                bid_type.setVisibility(View.GONE);
                                button_buy_it_now.setVisibility(View.GONE);
                                checkout_btn.setText("You’ve already checked out!");
                                checkout_btn.setClickable(false);
                                checkout_btn.setBackgroundResource(R.drawable.new_gray_button_bacgound);
                                checkout_btn.setVisibility(View.VISIBLE);

                            } else {

                                if (winning_paid_amount.equals(null) || winning_paid_amount.equals("null")) {
                                    winning_amount = winning_bid_amount;
                                } else {
                                    winning_amount = winning_paid_amount;
                                }

                                double pricevalue = Double.parseDouble(winning_amount);

                                item_amount.setText(String.format("$ %.2f", pricevalue));
                                bidding_msg.setText("This item is sold and is no\n" + "longer available.");
                                bidding_row.setVisibility(View.GONE);
                                bid_type.setVisibility(View.GONE);
                                button_buy_it_now.setVisibility(View.GONE);

                                double sold_price = Double.parseDouble(winning_amount);

                                checkout_btn.setText(String.format("Sold Price: $ %.2f", sold_price));
                                checkout_btn.setClickable(false);
                                checkout_btn.setBackgroundResource(R.drawable.new_gray_button_bacgound);
                                checkout_btn.setVisibility(View.VISIBLE);

                            }

                        } else {

                            if (bid_counts > 1){
                                details_bid_counts.setText(bid_count+"  BIDS");
                            }else if (bid_counts == 1){
                                details_bid_counts.setText(bid_count+"  BID ");
                            } else {
                                details_bid_counts.setText("NO BIDS");
                            }
                            if (winner_status.equals("true") && user_paid_status.equals("false")) {

                                double pricevalue = 0;
                                if (current_bid_amount.equals("") || current_bid_amount.equals(null) || current_bid_amount.equals("null")) {
                                    pricevalue = Double.parseDouble(starting_bid);
                                } else {
                                    pricevalue = Double.parseDouble(current_bid_amount);
                                }

                                item_amount.setText(String.format("$ %.2f", pricevalue));

                                if (winning_paid_amount.equals(null) || winning_paid_amount.equals("null")) {
                                    winning_amount = winning_bid_amount;
                                }else if(winning_bid_amount.equals(null) || winning_bid_amount.equals("null")) {
                                    winning_amount = winning_paid_amount;
                                }else {
                                    winning_amount = starting_bid;
                                }

                                double sold_price = Double.parseDouble(winning_amount);

                                bidding_msg.setText(Html.fromHtml("<b><big><font color='black'>Congratulations</font>.</big><b><br> <small>You won this item!</small>"));
                                bidding_row.setVisibility(View.GONE);
                                bid_type.setVisibility(View.GONE);
                                button_buy_it_now.setVisibility(View.GONE);
                                checkout_btn.setText(String.format("CHECKOUT NOW: $ %.2f", sold_price));
                                checkout_btn.setVisibility(View.VISIBLE);

                            } else {

                                double pricevalue = 0;
                                if (current_bid_amount.equals("") || current_bid_amount.equals(null) || current_bid_amount.equals("null")) {
                                    pricevalue = Double.parseDouble(starting_bid);
                                } else {
                                    pricevalue = Double.parseDouble(current_bid_amount);
                                }

                                item_amount.setText(String.format("$ %.2f", pricevalue));
                                bidding_msg.setText(Html.fromHtml("<b><big><font color='black'>Auction Closed</font></big><b><br> <small>Item not purchased</small>"));
                                bidding_row.setVisibility(View.GONE);
                                bid_type.setVisibility(View.GONE);
                                button_buy_it_now.setVisibility(View.GONE);

                                checkout_btn.setVisibility(View.GONE);

                            }

                        }

                    }

                    ProgressDialog.getInstance().hideProgress();
                } catch (Exception e) {
                    //nothing
                    ProgressDialog.getInstance().hideProgress();
                }
                ProgressDialog.getInstance().hideProgress();

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                ProgressDialog.getInstance().hideProgress();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    if (error.networkResponse.statusCode==401){
                        Alert_Dailog.showNetworkAlert(LiveAction_Items.this);
                    }else {
                        Toast.makeText(LiveAction_Items.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    }
                }else {
//                    Toast.makeText(LiveAction_Items.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    if(auth_token.isEmpty()){
                        Alert_Dailog.showNetworkAlert1(LiveAction_Items.this);
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjReq, "");
    }

    public class BeforeLiveCountdown extends CountDownTimer {

        public BeforeLiveCountdown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            try {
                long different = ConvertDate(event_start_date).getTime() - ConvertDate(getCurrentDate()).getTime();
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;
                long elapsedDays = different / daysInMilli;
                different = different % daysInMilli;
                long elapsedHours = different / hoursInMilli;
                different = different % hoursInMilli;
                long elapsedMinutes = different / minutesInMilli;
                different = different % minutesInMilli;
                long elapsedSeconds = different / secondsInMilli;
                bid_type.setText("Starting bid");
                bidding_row.setText(" | " + String.format("%02d", elapsedDays) + "D : " + String.format("%02d", elapsedHours) + "H : " + elapsedMinutes + "M : " + elapsedSeconds + "S " + "remaining");

            }catch (Exception e){ }
        }

        @Override
        public void onFinish() {

            mHandler.obtainMessage(1).sendToTarget();

        }
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            if (payment_status.equals("paid")) {

                if (user_paid_status.equals("true")){

                    double pricevalue = Double.parseDouble(starting_bid);

                    item_amount.setText(String.format("$ %.2f", pricevalue));


                    button_buy_it_now.setVisibility(View.GONE);
                    bidding_msg.setText(Html.fromHtml("<b><big><font color='black'>Congratulations</font>.</big><b><br> <small>You won this item!</small>"));
                    bidding_row.setVisibility(View.GONE);
                    bid_type.setVisibility(View.GONE);
                    watch_firstlayout.setVisibility(View.GONE);

                    checkout_btn.setText("You’ve already checked out!");
                    checkout_btn.setClickable(false);
                    checkout_btn.setBackgroundResource(R.drawable.new_gray_button_bacgound);
                    checkout_btn.setVisibility(View.VISIBLE);


                }else {

                    double pricevalue = Double.parseDouble(winning_paid_amount);

                    item_amount.setText(String.format("$ %.2f", pricevalue));

                    palcebid.setVisibility(View.GONE);
                    watch_firstlayout.setVisibility(View.GONE);
                    button_buy_it_now.setVisibility(View.GONE);

                    bidding_msg.setText("This item is sold and is no\n" + "longer available.");
                    bidding_row.setVisibility(View.GONE);
                    bid_type.setVisibility(View.GONE);

                    checkout_btn.setText("Sold Price: "+String.format("$ %.2f", pricevalue));
                    checkout_btn.setClickable(false);
                    checkout_btn.setBackgroundResource(R.drawable.new_gray_button_bacgound);
                    checkout_btn.setVisibility(View.VISIBLE);

                }

            }else {

                methodenddate();

                bidding_msg.setVisibility(View.GONE);
                bidding_row.setVisibility(View.VISIBLE);
                bid_type.setVisibility(View.VISIBLE);
                checkout_btn.setVisibility(View.GONE);

                palcebid.setVisibility(View.VISIBLE);
                watch_firstlayout.setVisibility(View.VISIBLE);
                button_buy_it_now.setVisibility(View.VISIBLE);

            }



        }


    };


    public Handler mHandler2 = new Handler() {
        public void handleMessage(Message msg) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    finish();
                    startActivity(getIntent());

                    // Actions to do after 10 seconds
                }
            }, 10000);
        }
    };


    public class AfterLiveCountDown extends CountDownTimer {

        public AfterLiveCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            try {
                long different = ConvertDate(event_end_date).getTime() - ConvertDate(getCurrentDate()).getTime();
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;
                long elapsedDays = different / daysInMilli;
                different = different % daysInMilli;
                long elapsedHours = different / hoursInMilli;
                different = different % hoursInMilli;
                long elapsedMinutes = different / minutesInMilli;
                different = different % minutesInMilli;
                long elapsedSeconds = different / secondsInMilli;
//
                if (bid_count.equals("0")){
                    bidding_row.setText(" | " +String.format("%02d",elapsedDays) + "D : " + String.format("%02d",elapsedHours) + "H : "+elapsedMinutes + "M : "+elapsedSeconds + "S "+"LEFT");
                }else {
                    bidding_row.setText(" | " +String.format("%02d",elapsedDays) + "D : " + String.format("%02d",elapsedHours) + "H : "+elapsedMinutes + "M : "+elapsedSeconds + "S "+"left");

                }
            } catch (Exception e) {
                //nothing
            }
        }

        @Override
        public void onFinish() {

//            mHandler2.obtainMessage(1).sendToTarget();
            afterLiveCountDown.cancel();
            finish();

        }

    }

    public void methodenddate(){
        long differents=0;
        differents = ConvertDate(event_end_date).getTime() - ConvertDate(getCurrentDate()).getTime();

        if (bid_count.equals("0")){
            double pricevalue = Double.parseDouble(starting_bid);
            item_amount.setText(String.format("$ %.2f", pricevalue));
            watch_icon.setVisibility(View.VISIBLE);
            bid_type.setText("Starting bid");

        }else {
            double pricevalue = Double.parseDouble(current_bid_amount);
            item_amount.setText(String.format("$ %.2f", pricevalue));
            watch_icon.setVisibility(View.VISIBLE);
            bid_type.setText("Current bid");
        }


        if(differents>0) {
            afterLiveCountDown = new AfterLiveCountDown(differents+10000, 1000);
            afterLiveCountDown.start();
        }

    }


    private void refresh(int i) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    methodtocalAPI(2);
                    ProgressDialog.getInstance().hideProgress();
                } catch (WindowManager.BadTokenException e) {
                }
            }
        };
        handler.postDelayed(runnable,i);
    }

    private void share() {

        final Button watch,share,cancel,view_bidding_histry;
        final LinearLayout layout_2;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogview = inflater.inflate(R.layout.watch_popup, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(true);
        popupDia.setCancelable(true);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;

        TextView popup_title = alertDialog.findViewById(R.id.popup_title);
        watch = alertDialog.findViewById(R.id.watch);
        share = alertDialog.findViewById(R.id.share);
        cancel = alertDialog.findViewById(R.id.cancel);
        view_bidding_histry = alertDialog.findViewById(R.id.view_bidding_histry);
        layout_2= alertDialog.findViewById(R.id.layout_2);

        if (is_expired.equals("true")){

            layout_2.setVisibility(View.GONE);
        }else {
            layout_2.setVisibility(View.VISIBLE);
        }

        if (user_watching_status.equals("true")){
            watch.setText("Watching");
        }else {
            watch.setText("Watch");
        }

        popup_title.setTypeface(medium);
        watch.setTypeface(regular);
        share.setTypeface(regular);
        cancel.setTypeface(regular);
        view_bidding_histry.setTypeface(regular);


        view_bidding_histry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent in = new Intent(getApplicationContext(), Bidding_History.class);
                in.putExtra("id",get_auth_token("Auction_id"));
                startActivity(in);
            }
        });


        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_to_call_watch_condition();
                alertDialog.dismiss();

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.length()>99){
                    name = name.substring(0,99);
                }
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT,"https://www.winningticket.com/auction_item/"+get_auth_token("Auction_id"));
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                alertDialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void method_to_call_watch_condition() {
        if (user_watching_status.equals("true")){
            watch_firstlayout.setText("WATCH");
            methodtocalawatchapi(Splash_screen.url+"auction/remove_watch/"+order_id);
            watchers_counts--;
            user_watching_status="false";
            watch_bidcoutn.setText(watchers_counts+"");
        }else {
            watch_firstlayout.setText("WATCHING");
            methodtocalawatchapi(Splash_screen.url+"auction/add_watch/"+order_id);
            watchers_counts++;
            user_watching_status="true";
            watch_bidcoutn.setText(watchers_counts+"");
        }
    }


    private void methodtocalawatchapi(String s) {
        System.out.println(s);

        ProgressDialog.getInstance().showProgress(LiveAction_Items.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST,s,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ProgressDialog.getInstance().hideProgress();
                            if (response.getString("status").equals("Success")){
                                ProgressDialog.getInstance().hideProgress();
                            }else {
                                Toast.makeText(getApplicationContext(),response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
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
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(LiveAction_Items.this);
                            }else {
                                Toast.makeText(LiveAction_Items.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(LiveAction_Items.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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


    private void pacebid() {
        final android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogview = inflater.inflate(R.layout.custome_placebid, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(true);
        popupDia.setCancelable(true);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;

        TextView popup_title = alertDialog.findViewById(R.id.pop_up_title);
        TextView default_amoutn = alertDialog.findViewById(R.id.default_amoutn);

        Button submit = alertDialog.findViewById(R.id.submit);
        Button cancel = alertDialog.findViewById(R.id.cancel);
        final EditText bit_amount = alertDialog.findViewById(R.id.bit_amount);


        bit_amount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().matches("(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})"))
                {
                    String userInput= ""+s.toString().replaceAll("[^\\d]", "");
                    StringBuilder cashAmountBuilder = new StringBuilder(userInput);

                    while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                        cashAmountBuilder.deleteCharAt(0);
                    }
                    while (cashAmountBuilder.length() < 3) {
                        cashAmountBuilder.insert(0, '0');
                    }
                    cashAmountBuilder.insert(cashAmountBuilder.length()-2, '.');
                    cashAmountBuilder.insert(0, "");

                    bit_amount.setText(cashAmountBuilder.toString());
                    // keeps the cursor always to the right
                    Selection.setSelection(bit_amount.getText(), cashAmountBuilder.toString().length());

                }

            }
        });


        popup_title.setTypeface(medium);
        submit.setTypeface(medium);
        cancel.setTypeface(medium);
        bit_amount.setTypeface(medium);
        default_amoutn.setTypeface(medium);

        double pricevalue = Double.parseDouble(minimum_bid_increment);
        DecimalFormat df = new DecimalFormat("0.00");
        String formate = df.format(pricevalue);
        default_amoutn.setText("Must bid in increments of $"+formate);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bit_amount.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter amount",Toast.LENGTH_LONG).show();
                }else{
                    try {

                        ProgressDialog.getInstance().showProgress(LiveAction_Items.this);

                        JSONObject curent_bid = new JSONObject();

                        curent_bid.put("current_bid", bit_amount.getText().toString());
                        System.out.println("-----res------"+curent_bid);

                        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"auction/place_bid/"+order_id,curent_bid,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {

                                            if (response.getString("status").equals("Success")){

                                                ProgressDialog.getInstance().hideProgress();

                                                item_amount.setText("");

                                                double pricevalue = Double.parseDouble(bit_amount.getText().toString());
                                                alertDialog.dismiss();

                                                publishProgress(pricevalue);
                                                Toast.makeText(getApplicationContext(),"Bidding has been done successfully", Toast.LENGTH_SHORT).show();

                                            }else {
                                                ProgressDialog.getInstance().hideProgress();
                                                hiddenInputMethod();
                                                bit_amount.getText().clear();
                                                Toast.makeText(getApplicationContext(),response.getString("message"), Toast.LENGTH_SHORT).show();
                                            }

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
                                        NetworkResponse networkResponse = error.networkResponse;
                                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                            // HTTP Status Code: 401 Unauthorized

                                            if (error.networkResponse.statusCode==401){
                                                Alert_Dailog.showNetworkAlert(LiveAction_Items.this);
                                            }else {
                                                Toast.makeText(LiveAction_Items.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(LiveAction_Items.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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


                    } catch (JSONException e) {
                        //nothing
                    }
                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });
    }


    private void publishProgress(final double randNum) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressDialog.getInstance().hideProgress();
                item_amount.setText(String.format("$ %.2f", randNum));

            }
        });
    }

    public void hiddenInputMethod() {

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void init() {
        currentPage=0;
        ImagesArray.clear();

        // Auto start of viewpager
        noofpostion.setText("1 of " + image_size.size() + "");

        for (int i = 0; i < image_size.size(); i++) {
            ImagesArray.clear();
            ImagesArray.add((image_size.get(i)));

            sliding_adpter = new SlidingImage_Adapter(LiveAction_Items.this, ImagesArray);
            sliding_adpter.notifyDataSetChanged();
            mPager.setAdapter(sliding_adpter);

            indicator = findViewById(R.id.indicator);
            indicator.setViewPager(mPager);

            final float density = getResources().getDisplayMetrics().density;
            indicator.setRadius(5 * density);
            NUM_PAGES = image_size.size();
        }

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                System.out.println("-----size----"+image_size.size());
                noofpostion.setText(position + 1 + " of " + image_size.size() + "");
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_purchage:
                finish();
                break;

            case R.id.watch_firstlayout:
                method_to_call_watch_condition();
                break;

            case R.id.palcebid:
                pacebid();
                break;

            case R.id.checkout_btn:
                in = new Intent(getApplicationContext(), LiveAuction_purchaseCheckout.class);
                in.putExtra("id",get_auth_token("Auction_id"));
                in.putExtra("type","checkout");
                if (image_size.size()>0){ savestring("imge_url",image_size.get(0)); }
                startActivity(in);
                finish();
                break;

            case R.id.button_buy_it_now:
                Intent in = new Intent(getApplicationContext(), LiveAuction_purchaseCheckout.class);
                in.putExtra("id",order_id);
                in.putExtra("type","buyitnow");
                if (image_size.size()>0){ savestring("imge_url",image_size.get(0)); }
                startActivity(in);
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (from.equals("app")) {
            finish();
        } else {
            Intent in = new Intent(getApplicationContext(), Splash_screen.class);
            startActivity(in);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (is_live.equals("true")){
            try {
                afterLiveCountDown.cancel();
            }catch (Exception e){ }

        }else if (is_live.equals("true") && is_expired.equals("false")){
            try {
                beforeLiveCountdown.cancel();
            }catch (Exception e){ }
        }

        handler.removeCallbacksAndMessages(null);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        methodtocalAPI(1);
    }
}

