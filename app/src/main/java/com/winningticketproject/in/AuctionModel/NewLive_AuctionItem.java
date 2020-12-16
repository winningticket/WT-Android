package com.winningticketproject.in.AuctionModel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.Adapter.SlidingImage_Adapter;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.AppInfo.item_Data;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.viewpagerindicator.CirclePageIndicator;
import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.ConvertDate;
import static com.winningticketproject.in.AppInfo.Share_it.getCurrentDate;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class NewLive_AuctionItem extends AppCompatActivity implements View.OnClickListener{

    TextView cancel_purchage, toolbar_title,menu_item,tv_item_number ,items_titles ,item_amount,details_bid_counts
            ,watch_icon ,clock_img,watch_bidcout,bidding_msg ,bid_type ,item_description ,bidding_row  ,similar_item ,view_all,watch_firstlayout;
    ViewPager pager;
    ReadMoreTextView description_content;
    CirclePageIndicator indicator ;
    RelativeLayout menu_icon,viewpager_id , relative;
    ScrollView items_scroll;
    Button palcebid ,checkout_btn ,button_buy_it_now ;
    RecyclerView horizontal_recycler_view;
    public static Typeface semibold ,regular, medium, black, italic, light, mediumitalic, webfont;
    String auth_token="", from="" , time = "";
    String URL = Splash_screen.url+"auction/item_detail/";
    String is_expired ,user_watching_status ,name , order_id,description,starting_bid,minimum_bid_increment,payment_status,bid_count,current_bid_amount,watchers_count ,is_live,event_start_date,event_end_date,winner_status,user_paid_status,unique_url_key;
    String winning_bid_amount,winning_paid_amount,buy_price_final ,winning_amount ;
    Integer bid_counts,watchers_counts ;
    AlertDialog alertDialog;
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    private ArrayList<String> image_size = new ArrayList<String>();
    Similar_ItemList_for_auction horizontalAdapter;
    SlidingImage_Adapter sliding_adpter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    BeforeLiveCountdown beforeLiveCountdown;
    AfterLiveCountDown afterLiveCountDown;
    final Handler handler = new Handler();
    Intent in;
    String date;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_live__auction_item);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        semibold = Typeface.createFromAsset(getAssets(), "Montserrat-SemiBold.ttf");
        regular = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
        medium = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");
        black = Typeface.createFromAsset(getAssets(),"Montserrat-Black.ttf");
        italic = Typeface.createFromAsset(getAssets(),"Montserrat-Italic.ttf");
        light = Typeface.createFromAsset(getAssets(),"Montserrat-Light.ttf");
        mediumitalic = Typeface.createFromAsset(getAssets(),"MontserratAlternates-MediumItalic.ttf");
        webfont = Typeface.createFromAsset(getAssets(),"fontawesome-webfont.ttf");

        Share_it share_it = new Share_it(this);

        pd = new ProgressDialog(NewLive_AuctionItem.this);
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            date = bundle.getString("date");
        }

    }

    private void method_to_declare_all_id() {

        cancel_purchage = findViewById(R.id.cancel_purchage);
        toolbar_title = findViewById(R.id.toolbar_title);
        menu_item = findViewById(R.id.menu_item);
        tv_item_number = findViewById(R.id.tv_item_number);
        items_titles = findViewById(R.id.items_titles);
        clock_img = findViewById(R.id.clock_img);
        item_amount = findViewById(R.id.item_amount);
        watch_icon = findViewById(R.id.watch_icon);
        bidding_msg = findViewById(R.id.bidding_msg);
        item_description = findViewById(R.id.item_description);

        description_content = findViewById(R.id.description_content);
        description_content.setTypeface(regular);

        similar_item = findViewById(R.id.similar_item);
        view_all = findViewById(R.id.tv_auction_empty);
        watch_firstlayout = findViewById(R.id.watch_firstlayout);
        details_bid_counts = findViewById(R.id.details_bid_counts);
        details_bid_counts.setTypeface(regular);

        relative = findViewById(R.id.relative);
        relative.setVisibility(View.VISIBLE);

        bidding_row = findViewById(R.id.bidding_row);
        bidding_row.setTypeface(regular);

        watch_bidcout = findViewById(R.id.watch_bidcoutn);
        bid_type = findViewById(R.id.bid_type);

        cancel_purchage.setTypeface(medium);
        toolbar_title.setTypeface(medium);
        menu_item.setTypeface(regular);
        tv_item_number.setTypeface(regular);
        items_titles.setTypeface(semibold);
        item_amount.setTypeface(regular);
        bidding_msg.setTypeface(regular);
        item_description.setTypeface(medium);
        similar_item.setTypeface(medium);
        view_all.setTypeface(regular);

        watch_firstlayout.setTypeface(regular);
        watch_firstlayout.setOnClickListener(this);

        button_buy_it_now =  findViewById(R.id.button_buy_it_now);
        button_buy_it_now.setTypeface(semibold);
        button_buy_it_now.setOnClickListener(this);

        items_scroll = findViewById(R.id.items_scroll);
        items_scroll.fullScroll(View.SCROLL_INDICATOR_TOP);

        pager = findViewById(R.id.pager);
        viewpager_id = findViewById(R.id.viewpager_id);
        viewpager_id.setVisibility(View.GONE);

        checkout_btn = findViewById(R.id.checkout_btn);
        checkout_btn.setOnClickListener(this);
        checkout_btn.setTypeface(semibold);

        palcebid = findViewById(R.id.palcebid);
        palcebid.setTypeface(semibold);

        cancel_purchage.setOnClickListener(this);
        menu_item.setOnClickListener(this);
        palcebid.setOnClickListener(this);

        handleIntent(getIntent());

        watch_icon = findViewById(R.id.watch_icon);
        watch_icon.setTypeface(webfont);

        cancel_purchage.setOnTouchListener(new View.OnTouchListener() {
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

            if(Share_it.get_auth_token("auth_token").isEmpty()){
                Alert_Dailog.showNetworkAlert1(NewLive_AuctionItem.this);
            }else {
                methodtocalAPI(1);
            }

        }else {
            if (!isNetworkAvailable()) {
                alertdailogbox("withoutname");
            } else {
                from = "app";
                if(Share_it.get_auth_token("auth_token").isEmpty()){
                    Alert_Dailog.showNetworkAlert1(NewLive_AuctionItem.this);
                }else {
                    methodtocalAPI(1);
                }
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
                        methodtocalAPI(1);
                    }
                }
                else
                {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("withoutname");
                    }
                    else {
                        methodtocalAPI(1);
                    }
                }
            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    private void methodtocalAPI(int values) {
        if (values == 1){
            if (!pd.isShowing()) {
                pd.show();
            }
        }

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL+get_auth_token("Auction_id"), null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                System.out.println("-------"+response);
                pd.hide();
                image_size.clear();
                try {
                    pd.hide();
                    JSONObject auction_item = response.getJSONObject("auction_item");

                    toolbar_title.setText("#"+auction_item.getString("auction_item_number"));
                    tv_item_number.setText("#"+auction_item.getString("auction_item_number"));

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
                    watch_bidcout.setText(watchers_counts+"");

                    savestring("wallete",response.getString("wallet_amount")+"");

                    winning_bid_amount = auction_item.getString("winning_bid_amount");
                    winning_paid_amount = auction_item.getString("winning_paid_amount");

                    buy_price_final = auction_item.getString("buy_price");

                    String buy_amount_values = "<font color='#ffffff'>Buy it now: $ <b><big>" + buy_price_final.substring(0, buy_price_final.length() - 2) + ".00</big></b></font>";
                    button_buy_it_now.setText( Html.fromHtml(buy_amount_values));

                    button_buy_it_now.setVisibility(View.VISIBLE);

                    if (values==1) {
                        JSONArray auction_item_images = auction_item.getJSONArray("auction_item_images");
                        if (auction_item_images.length() == 0) {
                            image_size.add("R.drawable.auto_image");
                        } else {
                            for (int j = 0; j < auction_item_images.length(); j++) {
                                JSONObject image_url = auction_item_images.getJSONObject(j);
                                image_size.add(image_url.getString("image_url"));
                            }
                        }
                        if (image_size.size() > 0){
                            init();
                            viewpager_id.setVisibility(View.VISIBLE);
                        }else {
                            viewpager_id.setVisibility(View.GONE);
                        }

                        JSONArray similar_items = response.getJSONArray("similar_items");
                        List<item_Data> data = new ArrayList<>();
                        for (int j=0;j<similar_items.length();j++){
                            JSONObject items = similar_items.getJSONObject(j);
                            String id = items.getString("id");
                            String images = items.getString("item_image");
                            String title = items.getString("name");
                            String bid_count=items.getString("bid_count");
                            String buy_price = items.getString("buy_price");
                            String starting_bid=items.getString("starting_bid");
                            String auction_item_number = items.getString("auction_item_number");
                            String current_bid_amount=items.getString("current_bid_amount");
                            String payment_status = items.getString("payment_status");
                            String is_live = items.getString("is_live?");
                            String event_end_date = items.getString("event_end_date");
                            String is_expired = items.getString("is_expired?");
                            String event_short_time_zone = items.getString("event_short_time_zone");
                            String event_start_date =items.getString("event_start_date");

                            ArrayList item_tags = new ArrayList();
                            item_tags.add("#"+auction_item_number);

                            JSONArray tags = items.getJSONArray("auction_tags_display");
                            for (int k = 0;k<tags.length();k++){
                                item_tags.add(tags.get(k));
                            }

                            data.add(new item_Data(id,images,title,bid_count,is_live,is_expired,current_bid_amount,starting_bid,payment_status,auction_item_number,buy_price,event_end_date,event_short_time_zone,event_start_date,item_tags));
                        }

                        horizontal_recycler_view = findViewById(R.id.horizontal_recycler_view);
                        horizontal_recycler_view.setFocusable(false);

                        horizontalAdapter = new Similar_ItemList_for_auction(data, getApplicationContext());
                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(NewLive_AuctionItem.this, LinearLayoutManager.VERTICAL, false);
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

                    if (user_watching_status.equals("true")){
                        watch_firstlayout.setText("Viewed");
                        watch_icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_remove_red_eye_blue_24dp));
                    }else {
                        watch_firstlayout.setText("Click to Watch");
                        watch_icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_remove_red_eye_black_24dp));
                    }

                    if (is_live.equals("false") && is_expired.equals("false")){
                        if (bid_counts > 1){
                            details_bid_counts.setText(bid_count+"  BIDS");
                        }else if (bid_counts == 1){
                            details_bid_counts.setText(bid_count+"  BID ");
                        } else {
                            details_bid_counts.setText("No Bids");
                        }

                        button_buy_it_now.setVisibility(View.GONE);

                        palcebid.setVisibility(View.GONE);
                        relative.setVisibility(View.GONE);
                        bidding_msg.setVisibility(View.VISIBLE);
                        bidding_msg.setText(Html.fromHtml("<b><big><font color='black'>Bidding not yet started</font>.</big><b>"));
                        bidding_row.setVisibility(View.VISIBLE);
                        bidding_row.setText(date);

                        item_amount.setText("$ "+starting_bid+".00");
//                        item_amount.setText(Html.fromHtml("<big><b>"+"$"+"</b></big><b><small><b>"+starting_bid+"</b></small><b>"));

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

                                String pricevalue   = "<font color='#0bb1de'><b><big>" + buy_price_final.substring(0,buy_price_final.length()-2) + "</big></b></font>";
                                item_amount.setText("$ "+Html.fromHtml(pricevalue)+".00");

                                button_buy_it_now.setVisibility(View.GONE);
                                bidding_msg.setVisibility(View.VISIBLE);
                                watch_firstlayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(NewLive_AuctionItem.this,"Auction Closed, you can't able to access",Toast.LENGTH_LONG).show();
                                    }
                                });
                                watch_icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_remove_red_eye_blue_24dp));
                                bidding_msg.setText(Html.fromHtml("<b><big><font color='black'>Congratulations</font>.</big><b><br> <small>You won this item!</small>"));
                                bidding_row.setVisibility(View.VISIBLE);
                                bidding_row.setText(date);
                                bid_type.setVisibility(View.GONE);
                                watch_firstlayout.setVisibility(View.VISIBLE);

                                checkout_btn.setText("You’ve already checked out!");
                                checkout_btn.setClickable(false);
                                checkout_btn.setBackgroundResource(R.drawable.new_gray_button_bacgound);
                                checkout_btn.setVisibility(View.VISIBLE);

                            }else {

                                item_amount.setText("$ "+winning_paid_amount.substring(0,winning_paid_amount.length()-2)+".00");

                                palcebid.setVisibility(View.GONE);
                                watch_firstlayout.setVisibility(View.VISIBLE);
                                button_buy_it_now.setVisibility(View.GONE);

                                bidding_msg.setText("This item is sold and is no\n" + "longer available.");
                                bidding_row.setVisibility(View.VISIBLE);
                                bidding_row.setText(date);
                                bid_type.setVisibility(View.GONE);

                                checkout_btn.setText("Sold Price: $ "+winning_paid_amount.substring(0,winning_paid_amount.length()-2)+".00");
                                checkout_btn.setClickable(false);
                                checkout_btn.setBackgroundResource(R.drawable.new_gray_button_bacgound);
                                checkout_btn.setVisibility(View.VISIBLE);

                            }

                        }else {

                            methodenddate();

                            bidding_msg.setVisibility(View.GONE);
                            bidding_row.setVisibility(View.VISIBLE);
                            bid_type.setVisibility(View.GONE);
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
                                item_amount.setText("$ "+winning_amount.substring(0, winning_amount.length() - 2)+".00");

                                bidding_row.setVisibility(View.VISIBLE);
                                bidding_row.setText(date);
                                watch_firstlayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(NewLive_AuctionItem.this,"Auction Closed, you can't able to access",Toast.LENGTH_LONG).show();
                                    }
                                });
                                watch_icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_remove_red_eye_blue_24dp));
                                bidding_msg.setText(Html.fromHtml("<b><big><font color='black'>Congratulations</font>.</big><b><br> <small>You won this item!</small>"));
                                bidding_msg.setVisibility(View.VISIBLE);
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

                                bidding_row.setText(date);
                                bidding_row.setVisibility(View.VISIBLE);
                                item_amount.setText("$ "+winning_amount.substring(0, winning_amount.length() - 2)+".00");
                                bidding_msg.setText("This item is sold and is no\n" + "longer available.");
                                bid_type.setVisibility(View.GONE);
                                button_buy_it_now.setVisibility(View.GONE);

                                checkout_btn.setText("Sold Price: $ "+winning_amount.substring(0, winning_amount.length() - 2)+".00");
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
                                details_bid_counts.setText("No Bid");
                            }
                            if (winner_status.equals("true") && user_paid_status.equals("false")) {

                                String  pricevalue = "0";
                                String pricevalue1 = "0";
                                if (current_bid_amount.equals("") || current_bid_amount.equals(null) || current_bid_amount.equals("null")) {
                                    pricevalue = (starting_bid);
                                } else {
                                    pricevalue1 = (current_bid_amount);
                                }

                                item_amount.setText("$ "+pricevalue+".00");
                                item_amount.setText("$ "+pricevalue1.substring(0, pricevalue1.length() - 2)+".00");

                                if (winning_paid_amount.equals(null) || winning_paid_amount.equals("null")) {
                                    winning_amount = winning_bid_amount;
                                }else if(winning_bid_amount.equals(null) || winning_bid_amount.equals("null")) {
                                    winning_amount = winning_paid_amount;
                                }else {
                                    winning_amount = starting_bid;
                                }

                                String sold_price = winning_amount;

//                                bidding_msg.setText(Html.fromHtml("<b><big><font color='black'>Congratulations</font>.</big><b><br> <small>You won this item!</small>"));
                                bidding_msg.setVisibility(View.GONE);
                                bidding_row.setText(date);
                                bidding_row.setVisibility(View.VISIBLE);
                                bid_type.setVisibility(View.GONE);
                                button_buy_it_now.setVisibility(View.GONE);
                                watch_icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_remove_red_eye_blue_24dp));
                                watch_firstlayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(NewLive_AuctionItem.this,"Auction Closed, you can't able to access",Toast.LENGTH_LONG).show();
                                    }
                                });
                                checkout_btn.setText("CHECKOUT NOW: $ "+ sold_price.substring(0,sold_price.length()-2)+".00");
                                checkout_btn.setVisibility(View.VISIBLE);

                            } else {

                                String  pricevalue = "0";
                                String pricevalue1 = "0";
                                if (current_bid_amount.equals("") || current_bid_amount.equals(null) || current_bid_amount.equals("null")) {
                                    pricevalue =  starting_bid;
                                } else {
                                    pricevalue1 =  current_bid_amount;
                                }

                                bidding_row.setVisibility(View.VISIBLE);
                                bidding_row.setText(date);
                                watch_firstlayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(NewLive_AuctionItem.this,"Auction Closed, you can't able to access",Toast.LENGTH_LONG).show();
                                    }
                                });
                                watch_icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_remove_red_eye_blue_24dp));
                                bidding_msg.setText(Html.fromHtml("<b><big><font color='black'>Auction Closed</font></big><b><br> <small>Item not purchased</small>"));
                                item_amount.setText("$ "+pricevalue+".00");
                                item_amount.setText("$ "+pricevalue1.substring(0, pricevalue1.length() - 2)+".00");
                                bid_type.setVisibility(View.GONE);
                                button_buy_it_now.setVisibility(View.GONE);
                                checkout_btn.setVisibility(View.GONE);

                            }

                        }
                    }
                    pd.hide();
                } catch (Exception e) {
                    //nothing
                    pd.hide();
                }
                pd.hide();
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                pd.hide();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    pd.hide();
                    if (error.networkResponse.statusCode==401){
                        Alert_Dailog.showNetworkAlert(NewLive_AuctionItem.this);
                    }else {
                        Toast.makeText(NewLive_AuctionItem.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    pd.hide();
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
                bid_type.setVisibility(View.GONE);

                String times = "<b>" + "Starting bid | " + "</b>"+ String.format("%02d", elapsedDays) + "D : " + String.format("%02d", elapsedHours) + "H : " + elapsedMinutes + "M : " + elapsedSeconds + "S " + "remaining ";

//                System.out.println("------time------"+times);

                bidding_row.setText(Html.fromHtml(times));

            }catch (Exception e){ }
        }

        @Override
        public void onFinish() {
//            mHandler.obtainMessage(1).sendToTarget();
            relative.setVisibility(View.VISIBLE);
            refresh(10);

        }
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            if (payment_status.equals("paid")) {

                if (user_paid_status.equals("true")){

                    item_amount.setText("$ "+starting_bid+".00");

                    button_buy_it_now.setVisibility(View.GONE);
                    clock_img.setVisibility(View.VISIBLE);
                    watch_firstlayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(NewLive_AuctionItem.this,"Auction Closed, you can't able to access",Toast.LENGTH_LONG).show();
                        }
                    });
                    watch_icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_remove_red_eye_blue_24dp));
                    bidding_msg.setText(Html.fromHtml("<b><big><font color='black'>Congratulations</font>.</big><b><br> <small>You won this item!</small>"));
                    bidding_msg.setVisibility(View.VISIBLE);
                    bidding_row.setVisibility(View.VISIBLE);
                    palcebid.setVisibility(View.GONE);
                    bid_type.setVisibility(View.GONE);
                    watch_firstlayout.setVisibility(View.VISIBLE);

                    checkout_btn.setText("You’ve already checked out!");
                    checkout_btn.setClickable(false);
                    checkout_btn.setBackgroundResource(R.drawable.new_gray_button_bacgound);
                    checkout_btn.setVisibility(View.VISIBLE);


                }else {

                    item_amount.setText("$ "+winning_paid_amount.substring(0, winning_paid_amount.length() - 2)+".00");

                    palcebid.setVisibility(View.GONE);
                    watch_firstlayout.setVisibility(View.VISIBLE);
                    button_buy_it_now.setVisibility(View.GONE);

                    bidding_msg.setText("This item is sold and is no\n" + "longer available.");
                    bidding_row.setVisibility(View.VISIBLE);
                    bid_type.setVisibility(View.GONE);

                    checkout_btn.setText("Sold Price: $ "+winning_paid_amount.substring(0, winning_paid_amount.length() - 2)+".00");
                    checkout_btn.setClickable(false);
                    checkout_btn.setBackgroundResource(R.drawable.new_gray_button_bacgound);
                    checkout_btn.setVisibility(View.VISIBLE);

                }

            }else {

                methodenddate();

                bidding_msg.setVisibility(View.GONE);
                bidding_row.setVisibility(View.VISIBLE);
                bid_type.setVisibility(View.GONE);
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
            watch_firstlayout.setEnabled(true);
//            time = "ongoing";
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

                if (bid_count.equals("0")){
                    bidding_row.setText(String.format("%02d",elapsedDays) + "D : " + String.format("%02d",elapsedHours) + "H : "+elapsedMinutes + "M : "+elapsedSeconds + "S "+"END");
                }else {
                    bidding_row.setText(String.format("%02d",elapsedDays) + "D : " + String.format("%02d",elapsedHours) + "H : "+elapsedMinutes + "M : "+elapsedSeconds + "S "+"end");

                }
            } catch (Exception e) {
                //nothing
            }
        }

        @Override
        public void onFinish() {
            afterLiveCountDown.cancel();
            finish();
        }
    }

    public void methodenddate(){
        long differents=0;
        differents = ConvertDate(event_end_date).getTime() - ConvertDate(getCurrentDate()).getTime();

        if (bid_count.equals("0")){
            item_amount.setText("$ "+starting_bid+".00");
            watch_icon.setVisibility(View.VISIBLE);
            bid_type.setVisibility(View.GONE);
            bid_type.setText("Starting bid");

        }else {
            item_amount.setText("$ "+current_bid_amount.substring(0, current_bid_amount.length() - 2)+".00");
            watch_icon.setVisibility(View.VISIBLE);
            bid_type.setVisibility(View.GONE);
            bid_type.setText("Current bid");
        }


        if(differents>0) {
            afterLiveCountDown = new AfterLiveCountDown(differents, 1000);
            afterLiveCountDown.start();
        }

    }

    private void refresh(int i) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    methodtocalAPI(2);
                    pd.hide();
                } catch (WindowManager.BadTokenException e) {
                }
            }
        };
        handler.postDelayed(runnable,i);
    }

    private void share() {

        final Button watch,share,cancel,view_bidding_histry;
        final LinearLayout layout_2;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogview = inflater.inflate(R.layout.watch_popup, null);
        final AlertDialog popupDia = builder.create();
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

        if (is_expired.equals("true") || is_live.equals("false")){
            layout_2.setVisibility(View.GONE);
        }else{
            layout_2.setVisibility(View.VISIBLE);
        }

        if (user_watching_status.equals("true")){
            watch.setText("Viewed");
            watch_icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_remove_red_eye_blue_24dp));
        }else {
            watch.setText("Click to watch");
            watch_icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_remove_red_eye_black_24dp));
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
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT,"https://staging.winningticket.com/auction_item/"+get_auth_token("Auction_id"));
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
        if (user_watching_status.equals("true")) {
            watch_firstlayout.setText("Click to Watch");
            watch_icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_remove_red_eye_black_24dp));
            methodtocalawatchapi(Splash_screen.url+"auction/remove_watch/"+order_id);
            user_watching_status="false";
            watchers_counts--;
            watch_bidcout.setText(watchers_counts+"");
        }else if(user_watching_status.equals("false")) {
            watch_firstlayout.setText("Viewed");
            watch_icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_remove_red_eye_blue_24dp));
            methodtocalawatchapi(Splash_screen.url+"auction/add_watch/"+order_id);
            user_watching_status="true";
            watchers_counts++;
            watch_bidcout.setText(watchers_counts+"");
        }
    }

    private void methodtocalawatchapi(String s) {
        System.out.println(s);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST,s,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("-----------"+response);
                            pd.hide();
                            if (response.getString("status").equals("Success")){
                                pd.hide();
                            }else {
                                Toast.makeText(getApplicationContext(),response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception E){
                            //nothing
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
                                pd.hide();
                                Alert_Dailog.showNetworkAlert(NewLive_AuctionItem.this);
                            }else {
                                Toast.makeText(NewLive_AuctionItem.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(NewLive_AuctionItem.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogview = inflater.inflate(R.layout.custome_placebid, null);
        final AlertDialog popupDia = builder.create();
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
        DecimalFormat df = new DecimalFormat("0");
        String formate = df.format(pricevalue);
        default_amoutn.setText("Must bid in increments of $"+formate);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bit_amount.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter amount",Toast.LENGTH_LONG).show();
                }else{
                    try {
                        pd.show();
                        JSONObject curent_bid = new JSONObject();
                        curent_bid.put("current_bid", bit_amount.getText().toString());
                        System.out.println("-----res------"+curent_bid);
                        alertDialog.dismiss();

                        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"auction/place_bid/"+order_id,curent_bid,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        System.out.println("-------response.bid------------"+response);
                                        try {
                                            if (response.getString("status").equals("Success")){
                                                item_amount.setText("");
//                                                double pricevalue = Double.parseDouble(bit_amount.getText().toString());

//                                                publishProgress(pricevalue);
                                                Toast.makeText(getApplicationContext(),"Bidding has been done successfully", Toast.LENGTH_SHORT).show();
                                                methodtocalAPI(1);

                                            }else {
                                                pd.hide();
                                                hiddenInputMethod();
                                                bit_amount.getText().clear();
                                                Toast.makeText(getApplicationContext(),response.getString("message"), Toast.LENGTH_SHORT).show();
                                            }

                                        }catch (Exception E){
                                            //nothing
                                            pd.hide();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        pd.hide();
                                        NetworkResponse networkResponse = error.networkResponse;
                                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                            // HTTP Status Code: 401 Unauthorized

                                            if (error.networkResponse.statusCode==401){
                                                pd.hide();
                                                Alert_Dailog.showNetworkAlert(NewLive_AuctionItem.this);
                                            }else {
                                                Toast.makeText(NewLive_AuctionItem.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(NewLive_AuctionItem.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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

//    private void publishProgress(final double randNum) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                pd.hide();
//                String bidvalue = String.valueOf(randNum);
//                item_amount.setText("$"+bidvalue.substring(0,bidvalue.length()-2));
//            }
//        });
//    }

    public void hiddenInputMethod() {

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void init() {
        currentPage = 0;
        ImagesArray.clear();

        for (int i = 0; i < image_size.size(); i++) {
            ImagesArray.add((image_size.get(i)));

            sliding_adpter = new SlidingImage_Adapter(NewLive_AuctionItem.this, ImagesArray);
            sliding_adpter.notifyDataSetChanged();
            pager.setAdapter(sliding_adpter);

            indicator = findViewById(R.id.indicator);
            indicator.setViewPager(pager);

            if(ImagesArray.size() >1){
                indicator.setViewPager(pager);
            }

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
                pager.setCurrentItem(currentPage++, true);
            }
        };

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
                currentPage = pos;
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

        if ( is_live != null && is_live.equals("true")){
            try {
                afterLiveCountDown.cancel();
            }catch (Exception e){ }

        }else if (is_live != null && is_live.equals("true") && is_expired.equals("false")){
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

    @Override
    protected void onStop() {
        super.onStop();
        methodtocalAPI(1);
        pd.hide();
    }
}