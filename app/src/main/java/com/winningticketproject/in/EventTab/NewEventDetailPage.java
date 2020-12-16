package com.winningticketproject.in.EventTab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AuctionModel.NewLive_Auction;
import com.winningticketproject.in.EventPurchase.Checkout_Page;
import com.winningticketproject.in.IndividulaGameType.Location_Services;
import com.winningticketproject.in.Live_Score_New_Flow.View_course_map;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.AppInfo.Sponsor_logo_data;
import com.winningticketproject.in.AppInfo.VirtulGiftBag;
import com.winningticketproject.in.ChatWithOther.User_List;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.IndividulaGameType.Location_Services.game_type;
import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.AppInfo.Share_it.decimal_amount;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class NewEventDetailPage extends AppCompatActivity implements Animation.AnimationListener,View.OnClickListener {
    TextView ticket_details, text_event_name, event_code,arrow, ticket_time, event_course_name, access_code, need_process, include_title, include_data , event_expand;
    LinearLayout main_layout;
    ImageView imageid, live_event , image_event_bg;
    View onExpand_text;
    Button btn_purchase_ticket;
    CardView card_not_purchased,card_auction_purchased;
    RecyclerView myList,recler_sponsor;
    public static ArrayList<Sponsor_logo_data> sponsor_logo = new ArrayList<>();
    TextView tv_silent_auction, tv_scoring_gps, tv_live_chat,virtual_gift_viewall,virtual_gift_tit,empty_text;
    ArrayList<VirtulGiftBag> virtulGiftBags = new ArrayList<>();
    TextView tv_super_ticket,tv_super_ticket_content,super_ticket_values,tv_sponsor_title,empty_sponsor,text_chart_count;
    LinearLayout layout_silent_auction,layout_scoring_gps,layout_live_chat,card_live ,linear_text ;
    public static String id;
    public static String name;
    public static String start_date="";
    public static String location;
    public static String ticket_price;
    public static String code;
    public static String oragnization;
    public static String event_type="";
    public static String wallete="10";
    public static String get_course_name;
    Animation animFadein,sidedown;
    boolean expand;
    ImageButton btn_back;
    TextView closed_content,event_ticket_all_values;
    String amenities = "",image_url, live_aminties="" , banner_image_url;
    String purchase_statuss="";
    int push_notification_count = 0;
    String event_status="",event_id;
    LinearLayout is_live_layout,chart_count;
    CardView cardview_silent_auction;
    String event_game_type="";
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_detail_page);

        event_expand = findViewById(R.id.event_expand);
        event_expand.setTypeface(medium);

        linear_text = findViewById(R.id.linear_text);
        linear_text.setOnClickListener(this);

        onExpand_text = findViewById(R.id.onExpand_text);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.side_down);
        animFadein.setAnimationListener(this);

        sidedown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.side_up);
        sidedown.setAnimationListener(this);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        ticket_details = findViewById(R.id.ticket_details);
        ticket_details.setTypeface(medium);

        text_event_name = findViewById(R.id.text_event_name);
        text_event_name.setTypeface(italic);

        event_code = findViewById(R.id.event_code);
        event_code.setTypeface(regular);

        imageid = findViewById(R.id.imageid);

        main_layout = findViewById(R.id.main_layout);

        text_chart_count = findViewById(R.id.text_chart_count);
        text_chart_count.setTypeface(semibold);
        chart_count = findViewById(R.id.chart_count);

        need_process = findViewById(R.id.need_process);
        need_process.setTypeface(regular);

        image_event_bg = findViewById(R.id.image_event_bg);

        include_data = findViewById(R.id.include_data);
        include_data.setTypeface(regular);

        card_live = findViewById(R.id.card_live);
        Intent in = getIntent();
        id = in.getStringExtra("id");
        event_type = in.getStringExtra("event_type");
        savestring(id , id);
        event_id = get_auth_token(id);

        live_event = findViewById(R.id.live_event);

        arrow = findViewById(R.id.arrow);
        arrow.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_blue_24dp));

        include_title = findViewById(R.id.include_title);
        include_title.setTypeface(medium);
        include_title.setPaintFlags(include_title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ticket_time = findViewById(R.id.ticket_time);
        ticket_time.setTypeface(regular);

        event_course_name = findViewById(R.id.event_course_name);
        event_course_name.setTypeface(regular);

        access_code = findViewById(R.id.access_code);
        access_code.setTypeface(medium);

        btn_purchase_ticket = findViewById(R.id.btn_purchase_ticket);
        btn_purchase_ticket.setTypeface(regular);

        btn_purchase_ticket.setOnClickListener(this);

        is_live_layout = findViewById(R.id.is_live_layout);
        cardview_silent_auction = findViewById(R.id.cardview_silent_auction);

        card_not_purchased = findViewById(R.id.card_not_purchased);
        card_auction_purchased = findViewById(R.id.card_auction_purchased);
        card_auction_purchased.setOnClickListener(this);

        TextView silent_auction = findViewById(R.id.silent_auction);
        silent_auction.setTypeface(medium);

        tv_live_chat = findViewById(R.id.tv_live_chat);
        tv_live_chat.setTypeface(medium);

        tv_scoring_gps = findViewById(R.id.tv_scoring_gps);
        tv_scoring_gps.setTypeface(medium);

        tv_silent_auction = findViewById(R.id.tv_silent_auction);
        tv_silent_auction.setTypeface(medium);


        layout_live_chat = findViewById(R.id.layout_live_chat);
        layout_scoring_gps = findViewById(R.id.layout_scoring_gps);
        layout_silent_auction = findViewById(R.id.layout_silent_auction);

        layout_live_chat.setOnClickListener(this);
        layout_scoring_gps.setOnClickListener(this);
        layout_silent_auction.setOnClickListener(this);

        // virtual gift bag

        virtual_gift_viewall = findViewById(R.id.virtual_gift_viewall);
        virtual_gift_viewall.setTypeface(medium);

        virtual_gift_tit = findViewById(R.id.virtual_gift_tit);
        virtual_gift_tit.setTypeface(medium);

        empty_text = findViewById(R.id.empty_text);
        empty_text.setTypeface(regular);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        myList = findViewById(R.id.recler_virtual_gift_bag);
        myList.setLayoutManager(layoutManager);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recler_sponsor = findViewById(R.id.recler_sponsor);
        recler_sponsor.setLayoutManager(layoutManager2);


        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(myList);

        // super ticket
        tv_super_ticket = findViewById(R.id.tv_super_ticket);
        tv_super_ticket.setTypeface(medium);

        tv_super_ticket_content = findViewById(R.id.tv_super_ticket_content);
        tv_super_ticket_content.setTypeface(regular);

        super_ticket_values = findViewById(R.id.super_ticket_values);
        super_ticket_values.setTypeface(regular);

        TextView tv_super_content = findViewById(R.id.tv_super_content);
        tv_super_content.setTypeface(medium);


        // sponsor
        tv_sponsor_title = findViewById(R.id.tv_sponsor_title);
        tv_sponsor_title.setTypeface(medium);

        empty_sponsor = findViewById(R.id.empty_sponsor);
        empty_sponsor.setTypeface(regular);

        closed_content = findViewById(R.id.closed_content);
        closed_content.setTypeface(medium);


        // sponsor event extra ticket
        TextView tv_event_access = findViewById(R.id.tv_event_access);
        tv_event_access.setTypeface(medium);

        TextView tv_event_ticket_content = findViewById(R.id.tv_event_ticket_content);
        tv_event_ticket_content.setTypeface(medium);

        event_ticket_all_values = findViewById(R.id.event_ticket_all_values);
        event_ticket_all_values.setTypeface(medium);

        virtual_gift_viewall.setOnClickListener(this);

        if (get_auth_token("CGB_event_code").equals(get_auth_token("current_event_code"))){
            layout_silent_auction.setVisibility(View.GONE);
            layout_live_chat.setVisibility(View.GONE);
        }

    }

    public void alertdailogbox(final String value) {
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
                if (value.equals("livescore")) {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("livescore");
                    } else {
                        methodforcalleventlist(2);
                    }
                } else if (value.equals("livestreming")) {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("livestreming");
                    } else {
                        methodforcallivestreming();
                    }
                }
            }
        });
        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    private void methodforcallivestreming() {

        ProgressDialog.getInstance().showProgress(NewEventDetailPage.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST, Splash_screen.url+"hole_info/check_event_live/"+id,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialog.getInstance().hideProgress();
                        try {
                            if (response.getString("event_status").equals("live")){
                                live_event.setVisibility(View.VISIBLE);
                                Intent intent1 = new Intent(NewEventDetailPage.this, Location_Services.class);
                                intent1.putExtra("id", id);
                                intent1.putExtra("event_type", event_game_type);
                                savestring("page_type","event");
                                startActivity(intent1);

                            }else if (response.getString("event_status").equals("closed")){
                                Intent intent1 = new Intent(NewEventDetailPage.this, Location_Services.class);
                                intent1.putExtra("id", id);
                                intent1.putExtra("event_type", event_game_type);
                                savestring("page_type","event");
                                startActivity(intent1);
                                live_event.setVisibility(View.GONE);

                            } else if (response.getString("event_status").equals("upcoming")){
                                live_event.setVisibility(View.GONE);
                                error("Upcoming Event");
                            }
                        }catch (Exception e){
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
                                ProgressDialog.getInstance().hideProgress();
                                Alert_Dailog.showNetworkAlert(NewEventDetailPage.this);
                            }else {
                                ProgressDialog.getInstance().hideProgress();
                                error("Some thing went wrong");
                            }
                        }else {
                            ProgressDialog.getInstance().hideProgress();
                            error("Some thing went wrong");
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



    public void methodforcalleventlist(int values) {
        virtulGiftBags.clear();
        amenities="";
        live_aminties="";

        if (values==1){
            ProgressDialog.getInstance().showProgress(NewEventDetailPage.this);
        }
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.GET, Splash_screen.url + "events/event_detail?id=" + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println("-----response---" + response);

                            String extra_ticket ="";

                            purchase_statuss = response.getString("purchase_status");
                            push_notification_count = response.getInt("push_notification_count");

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if(push_notification_count > 0){
                                        chart_count.setVisibility(View.VISIBLE);
                                        text_chart_count.setText(push_notification_count+"");
                                    }else {
                                        chart_count.setVisibility(View.GONE);
                                    }
                                }
                            });

                            try {
                                JSONArray recipients = response.getJSONArray("recipients");
                                if (recipients.length()>0){
                                    for (int k=0;k<recipients.length();k++)
                                    {
                                        JSONObject recipients_obj = recipients.getJSONObject(k);
                                        extra_ticket+=k+1+" "+recipients_obj.getString("full_name")+"\n";
                                    }
                                    event_ticket_all_values.setText(extra_ticket);
                                }else {
                                    event_ticket_all_values.setText("No Extra Ticket");
                                }

                            }catch (Exception e){
                                event_ticket_all_values.setText("No Extra Ticket");
                            }

                            JSONObject obj = response.getJSONObject("event");

                            savestring("CGB_event_code",response.getString("cgb_code"));
                            savestring("current_event_code",obj.getString("code"));

                            name = obj.getString("name").replaceAll("\\s+", " ");
                            start_date = obj.getString("detail_converted_date");
                            location = obj.getString("get_golf_location");
                            oragnization = obj.getString("organization_name");
                            ticket_price = response.getString("price");
                            code = obj.getString("code");
                            get_course_name = obj.getString("get_course_name");
                            wallete = response.getString("wallet");
                            event_game_type = obj.getString("game_type");

                            banner_image_url = obj.getString("banner_image");

                            Picasso.with(NewEventDetailPage.this)
                                    .load(obj.getString("banner_image") + "").error(R.drawable.auto_image).placeholder(R.drawable.auto_image)
                                    .into(image_event_bg);


                            savestring("cross_scoring",obj.getString("cross_scoring"));

                            if(obj.getString("event_golf_type").equals("non-golf")){
                                layout_scoring_gps.setVisibility(View.GONE);
                            }else {
                                layout_scoring_gps.setVisibility(View.VISIBLE);
                            }

                            event_status = obj.getString("event_status");
                            savestring("Event_name",name);
                            savestring("event_name",name);
                            ticket_price = decimal_amount(Double.parseDouble(ticket_price));
                            String text = "<font color=#000000>Event Access: </font> <font color=#00AC2C>"+"$"+ticket_price+"</font>";

                            access_code.setText(Html.fromHtml(text));

                            try {
                                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                            } catch (Exception e) { }

                            ticket_details.setText(name.trim());

                            String event_code1 = "<b>" + "Event code: " + "</b>"+ code;

                            event_code.setText(Html.fromHtml(event_code1));

                            String newString = start_date.replace("@", "-");
                            String asubstring;

                            String[] str1 = newString.split("-");
                            String String = str1[0];
                            String[] str = String.split(",");
                            asubstring  = str[1] + ", " + Calendar.getInstance().get(Calendar.YEAR) + " -" + str1[1];

                            ticket_time.setText(Html.fromHtml("<b>" + "Event Date: " + "</b>"+asubstring));

                            if (location.equals("null") || location.equals("") || location.equals(null)) {
                                event_course_name.setText("Not Mentioned");
                            } else {
                                String[] aray = location.split(",");
                                String split_data="";
                                if (aray.length>2) {
                                    split_data = aray[0] + "<br>" + aray[1] + "," + aray[2];
                                }else if (aray.length==2){
                                    split_data = aray[0] + "<br>" + aray[1];
                                }else {
                                    split_data = location;
                                }
                                if (obj.getString("event_golf_type").equals("non-golf")){
                                    event_course_name.setText(Html.fromHtml("<big>" + "<b>" + obj.getString("get_first_line_address")+ "</b>" + "</big>" + "<br>" + split_data));
                                }else {
                                    event_course_name.setText(Html.fromHtml("<big>" + "<b>" + get_course_name + "</b>" + "</big>" + "<br>" + split_data));
                                }

                            }


                            if (oragnization.equals("null") || oragnization.equals("") || oragnization.equals(null)){
                                text_event_name.setText("Not Mentioned");
                            }else {
                                String locations = oragnization.substring(0,1).toUpperCase() +oragnization.substring(1);
                                text_event_name.setText(locations.replaceAll("\\s+"," "));
                                tv_super_ticket_content.setText("All super ticket proceeds go to "+locations+" Thank you for your generous donation.");
                            }

                            try {
                                amenities="Live Scoring & GPS \nSilent Auction Entry \nEvent Live Chat \nVirtual Gift Bag\n";
                                // amenities response
                                JSONObject ticket_amenities = response.getJSONObject("ticket_amenities");
                                Iterator<String> keys = ticket_amenities.keys();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    String value = ticket_amenities.getString(key);
                                    amenities += value + " " + key + "\n";
                                    live_aminties+=value + " " + key + "\n";
                                }

                                include_data.setText(amenities);
                                super_ticket_values.setText(live_aminties);

                            }catch (org.json.JSONException exception){
                                include_data.setText(amenities);
                                live_aminties = "This super ticket does not have any amenities";
                                super_ticket_values.setText(live_aminties);

                            }

                            savestring("event_id",event_id);
                            savestring("live_aminties",live_aminties);

                            // virtual gift bag

                            try {
                                JSONArray virtual_gift_bags = response.getJSONArray("virtual_gift_bags");
                                for (int i = 0; i < virtual_gift_bags.length(); i++) {
                                    JSONObject object = virtual_gift_bags.getJSONObject(i);

                                    String id = object.getString("id");
                                    String item_name = object.getString("item_name");
                                    String offer_expires_date = object.getString("offer_does_not_expire");
                                    String datess = object.getString("offer_expires_date");
                                    String image = object.getString("image");
                                    virtulGiftBags.add(new VirtulGiftBag(id, item_name, datess, image,offer_expires_date));
                                }

                                Virtula_Gift_Bag allevents_hori = new Virtula_Gift_Bag(NewEventDetailPage.this, virtulGiftBags);
                                myList.setAdapter(allevents_hori);

                                if (virtulGiftBags.size()>0){
                                    myList.setVisibility(View.VISIBLE);
                                }else {
                                    empty_text.setVisibility(View.VISIBLE);
                                    virtual_gift_viewall.setVisibility(View.GONE);
                                }

                            } catch (org.json.JSONException exception ) {
                                empty_text.setVisibility(View.VISIBLE);
                                virtual_gift_viewall.setVisibility(View.GONE);
                            }

                            sponsor_logo.clear();
                            try {
                                JSONArray sponsor_users = response.getJSONArray("sponsor_users");
                                for (int i = 0; i < sponsor_users.length(); i++) {
                                    JSONObject object = sponsor_users.getJSONObject(i);
                                    sponsor_logo.add(new Sponsor_logo_data(object.getString("sponsor_logo_url"),object.getString("website_url")));

                                }

                                SponsorLogo allevents_hori = new SponsorLogo(NewEventDetailPage.this, sponsor_logo);
                                recler_sponsor.setAdapter(allevents_hori);

                                if (sponsor_logo.size()>0){
                                    recler_sponsor.setVisibility(View.VISIBLE);
                                }else {
                                    empty_sponsor.setVisibility(View.VISIBLE);
                                }
                            }catch (org.json.JSONException e){
                                empty_sponsor.setVisibility(View.VISIBLE);
                            }

                            Picasso.with(NewEventDetailPage.this)
                                    .load(obj.getString("avatar_url") + "").error(R.drawable.auction_event_empty).placeholder(R.drawable.auction_event_empty)
                                    .into(imageid);

                            image_url =  obj.getString("avatar_url");

                        } catch (Exception e) {
                            //nothing
                        }

                        ImageView grey_logo = findViewById(R.id.grey_logo);
                        if (event_status.equals("Live")) {
                            if (purchase_statuss.equals("Purchased")){
                                card_auction_purchased.setVisibility(View.GONE);
                                card_not_purchased.setVisibility(View.GONE);
                                is_live_layout.setVisibility(View.GONE);
                                card_live.setVisibility(View.VISIBLE);
                                live_event.setVisibility(View.VISIBLE);
                            }else {
                                grey_logo.setVisibility(View.VISIBLE);
                                is_live_layout.setVisibility(View.VISIBLE);
                                live_event.setVisibility(View.GONE);
                                card_live.setVisibility(View.GONE);
                                cardview_silent_auction.setVisibility(View.GONE);
                            }

                        } else if (event_status.equals("Upcoming")) {

                            if (purchase_statuss.equals("Purchased")){
                                card_auction_purchased.setVisibility(View.GONE);
                                card_not_purchased.setVisibility(View.GONE);
                                is_live_layout.setVisibility(View.GONE);
                                card_live.setVisibility(View.VISIBLE);
                            }else {
                                grey_logo.setVisibility(View.VISIBLE);
                                card_live.setVisibility(View.GONE);
                                live_event.setVisibility(View.GONE);
                                is_live_layout.setVisibility(View.VISIBLE);
                                cardview_silent_auction.setVisibility(View.GONE);
                            }

                        } else if (event_status.equals("Closed")) {
                            card_live.setVisibility(View.VISIBLE);
                            String htmls = "<big>"+"This event is over"+"</big>"+"<br> You can still view all event information but you will not be able to place bids, enter scores, or chat other members.";
                            closed_content.setText(Html.fromHtml(htmls));
                            closed_content.setVisibility(View.VISIBLE);
                            card_auction_purchased.setVisibility(View.GONE);
                            card_not_purchased.setVisibility(View.GONE);
                            is_live_layout.setVisibility(View.GONE);
                        }else {
                            live_event.setVisibility(View.GONE);
                        }

                        if (values==1){ ProgressDialog.getInstance().hideProgress(); }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (values==1){ ProgressDialog.getInstance().hideProgress(); }
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(NewEventDetailPage.this);
                            } else {
                                error("Some thing went wrong");
                            }
                        } else {
                            error("Some thing went wrong");
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
        myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    // error messagres
    @SuppressLint("WrongConstant")
    private void error(String s) {
        Snackbar snackbar = Snackbar.make(main_layout, s, Snackbar.LENGTH_LONG);
        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
        View view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtv.setTextSize(TypedValue
                .COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_size));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorsnakbar));
        snackbar.show();

    }

    @Override
    public void onClick(View view) {

        Intent in;
        switch (view.getId()) {

            case R.id.btn_purchase_ticket:
                in = new Intent(getApplicationContext(), Checkout_Page.class);
                Share_it.savestring("ticket_price",ticket_price);
                Share_it.savestring("name",name);
                Share_it.savestring("code",code);
                Share_it.savestring("id",id);
                Share_it.savestring("organization_name",oragnization);
                Share_it.savestring("wallete",wallete);
                Share_it.savestring("aminities",amenities);
                Share_it.savestring("live_aminties",live_aminties);
                Share_it.savestring("imge_url",image_url);
                savestring("play_type","event");
                startActivity(in);
                break;

            case R.id.linear_text :
                if(expand) {
                    expand = false;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onExpand_text.startAnimation(animFadein);
                            event_expand.setText("click for event details");
                            arrow.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_blue_24dp));

                            onExpand_text.setVisibility(View.GONE);
                        }
                    }, 500);

                }else {
                    expand = true;
                    onExpand_text.setVisibility(View.VISIBLE);

                    event_expand.setText("Hide event details");
                    arrow.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_blue_24dp));
                    onExpand_text.startAnimation(animFadein);

                }
                break;

            case R.id.layout_silent_auction:
                in=new Intent(getApplicationContext(), NewLive_Auction.class);
                Share_it.savestring("id",event_id);
                startActivity(in);
                break;
            case R.id.card_auction_purchased:
                in=new Intent(getApplicationContext(), NewLive_Auction.class);
                Share_it.savestring("id",event_id);
                startActivity(in);
                break;
            case R.id.layout_scoring_gps:

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                savestring("page_type","event");
                if (!isNetworkAvailable()) {
                    alertdailogbox("livestreming");
                }
                else
                {
                    methodforcallivestreming();
                }
                break;

            case R.id.layout_live_chat:
                in=new Intent(getApplicationContext(), User_List.class);
                Share_it.savestring("event_id",id);
                startActivity(in);
                break;

            case R.id.virtual_gift_viewall:
                in=new Intent(getApplicationContext(), Virtual_gift_bag.class);
                in.putExtra("id",id);
                startActivity(in);
                break;


            case R.id.btn_back:
                this.finish();
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


    private class Virtula_Gift_Bag extends RecyclerView.Adapter<Virtula_Gift_Bag.MyViewHolder> implements View.OnClickListener {

        public Activity context;
        public ArrayList<VirtulGiftBag> course_list;

        @Override
        public void onClick(View view) {

            int itemPosition = myList.getChildLayoutPosition(view);
            String item = course_list.get(itemPosition).getId();
            Intent in = new Intent(context,Virtual_bag_details.class);
            in.putExtra("id",id);
            in.putExtra("vocher_id",item);
            context.startActivity(in);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView vg_image ;
            TextView text_course_name,text_date;

            public MyViewHolder(View view) {
                super(view);

                vg_image = view.findViewById(R.id.vg_image);
                text_course_name = view.findViewById(R.id.text_course_name);
                text_date = view.findViewById(R.id.text_date);

                text_course_name.setTypeface(medium);
                text_date.setTypeface(regular);

            }
        }

        public Virtula_Gift_Bag(Activity context, ArrayList<VirtulGiftBag> course_list) {
            super();
            this.context = context;
            this.course_list = course_list;
        }

        @Override
        public Virtula_Gift_Bag.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_virtual_gift_bag, parent, false);
            itemView.setOnClickListener(this);
            return new Virtula_Gift_Bag.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(Virtula_Gift_Bag.MyViewHolder holder, final int position) {

            holder.text_course_name.setText(course_list.get(position).getName());

            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            java.util.Date date = null;
            try
            {
                date = format.parse(course_list.get(position).getDate());
                java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("dd/MM/yyyy");
                String newDateStr = postFormater.format(date);
                holder.text_date.setText(Html.fromHtml("<b>" +"Offer expires: " +"</b>"+newDateStr));
            }
            catch (ParseException e)
            {//nothing
                holder.text_date.setText(Html.fromHtml("<b>" +"Offer expires: " +"</b>"+"Not Mentioned"));
            }

            Picasso.with(NewEventDetailPage.this)
                    .load(course_list.get(position).getImage()).error(R.drawable.auction_event_empty).placeholder(R.drawable.auction_event_empty)
                    .into( holder.vg_image);

        }

        @Override
        public int getItemCount() {
            return course_list.size();
        }

    }

    public static class SponsorLogo extends RecyclerView.Adapter<SponsorLogo.MyViewHolder> implements View.OnClickListener {
        public Activity context;
        public ArrayList<Sponsor_logo_data> sponsor_logos;

        @Override
        public void onClick(View view) {

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView sponsor_logos ;
            public MyViewHolder(View view) {
                super(view);

                sponsor_logos = view.findViewById(R.id.sponsor_logo);

            }
        }

        public SponsorLogo(Activity context, ArrayList<Sponsor_logo_data> course_list) {
            super();
            this.context = context;
            this.sponsor_logos = course_list;
        }

        @Override
        public SponsorLogo.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_sponsor_logo, parent, false);
            itemView.setOnClickListener(this);
            return new SponsorLogo.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SponsorLogo.MyViewHolder holder, final int position) {

            Picasso.with(context)
                    .load(sponsor_logos.get(position).sponsor_log).error(R.drawable.auction_event_empty).placeholder(R.drawable.auction_event_empty)
                    .into( holder.sponsor_logos);

            holder.sponsor_logos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!sponsor_logos.get(position).sponsor_web_site.equals("null") || !sponsor_logos.get(position).sponsor_web_site.equals("") || !sponsor_logos.get(position).sponsor_web_site.equals(null)){

                        Uri webpage = Uri.parse(sponsor_logos.get(position).sponsor_web_site);

                        if (!sponsor_logos.get(position).sponsor_web_site.startsWith("http://") && !sponsor_logos.get(position).sponsor_web_site.startsWith("https://")) {
                            webpage = Uri.parse("http://" + sponsor_logos.get(position).sponsor_web_site);
                        }

                        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return sponsor_logos.size();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver), new IntentFilter("Notification_count"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isNetworkAvailable()) {
                alertdailogbox("livescore");
            } else {
                methodforcalleventlist(2);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // call api
        if (!isNetworkAvailable()) {
            alertdailogbox("livescore");
        } else {
            methodforcalleventlist(1);
        }
    }

}