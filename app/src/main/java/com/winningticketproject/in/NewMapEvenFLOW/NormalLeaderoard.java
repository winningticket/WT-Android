package com.winningticketproject.in.NewMapEvenFLOW;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Child_getter_setter;
import com.winningticketproject.in.AppInfo.parent_getter_setter;
import com.winningticketproject.in.AuctionModel.NewLive_Auction;
import com.winningticketproject.in.ChatWithOther.User_List;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.IndividulaGameType.Location_Services;
import com.winningticketproject.in.Live_Score_New_Flow.View_course_map;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;
import static com.winningticketproject.in.EventTab.NewEventDetailPage.oragnization;
import static com.winningticketproject.in.EventTab.NewEventDetailPage.sponsor_logo;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.game_name;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;

public class NormalLeaderoard extends AppCompatActivity implements View.OnClickListener {

    ExpandableListView explist;
    ProgressDialog pd;
    public Map<String, String> par_values = new HashMap<String, String>();
    public Map<String, String> Score_values = new HashMap<String, String>();
    public Map<String, String> net_score_values = new HashMap<String, String>();
    public Map<String, String> hole_values = new HashMap<String, String>();

    SwipeRefreshLayout swipeRefreshLayout;

    ListAdapterExpandible adapterz;
    private List<parent_getter_setter> alleventlist_List = new ArrayList<parent_getter_setter>();
    HashMap<String, List<Child_getter_setter>> childArrayList = new HashMap<String, List<Child_getter_setter>>();
    String Server_url = "ws://18.204.221.185/cable";
    ArrayList<String>  flight_list = new ArrayList<>();
    boolean isnotcourse;

    public static int devider=0;
    public static int eneble=0;
    int alpha_bets=0,expand_colapse=0;
    parent_getter_setter parent_list = null;
    Child_getter_setter child_list= null;
    String player_name_in_team="",team_name;
    int parent_postion=0,par_total=0,gross_score_total=0,net_score_total=0;
    int par_postion_1=0,net_score_postion_1=0,gross_score_postion_1=0,par_postion_2=0,net_score_postion_2=0,gross_score_postion_2=0;
    String auth_token = "";
    String event_id = "";
    String user_id="";
    String handicap_score="0";
    String user_current_postion="";
    private int lastExpandedPosition = -1;
    TextView tv_par_score,tv_gross_score,tv_handicap,tv_pos_t,text_1,text_2,text_3,text_4,text_champin_ship;
    public OkHttpClient client;
    WebSocket ws;
    int group = 0;
    TextView tv_live_leaderboard;
    TextView tv_course_name;
    LinearLayout ll_non_course;
    AdView mAdView;
    LinearLayout ll_addsense ;
    Button btn_leader_board;
    String home ;

    ImageView  image_view_scorecard;
    int hole_size=0;
    Button btn_score_card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_leaderoard);

        explist= findViewById(R.id.explist);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            home = extras.getString("home");
        }

        ll_non_course = findViewById(R.id.ll_non_course);

        MobileAds.initialize(this, String.valueOf(R.string.ads_app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("35E23B2B248A9549E2E9FEF359980EF8").build();
        mAdView.loadAd(adRequest);
        ll_addsense = findViewById(R.id.ll_addsense);

        method_to_load_top_section();


        methodtocalllivestreaming();

        image_view_scorecard = findViewById(R.id.image_view_scorecard);
        image_view_scorecard.setOnClickListener(this);

        if (get_auth_token("CGB_event_code").equals(get_auth_token("current_event_code"))){
            image_view_scorecard.setVisibility(View.VISIBLE);
            ll_non_course.setVisibility(View.GONE);
        }


        auth_token = get_auth_token("auth_token");

        event_id = get_auth_token("event_id");

        tv_live_leaderboard = findViewById(R.id.tv_live_leaderboard);
        tv_live_leaderboard.setTypeface(semibold);
        tv_live_leaderboard.setOnClickListener(this);

        TextView tv_course_name = findViewById(R.id.tv_course_name);
        tv_course_name.setTypeface(regular);

        text_1 = findViewById(R.id.text_1);
        text_2 = findViewById(R.id.text_2);
        text_3 = findViewById(R.id.text_3);
        text_4 = findViewById(R.id.text_4);

        text_1.setTypeface(medium);
        text_2.setTypeface(medium);
        text_3.setTypeface(medium);
        text_4.setTypeface(medium);

        text_champin_ship = findViewById(R.id.text_champin_ship);
        text_champin_ship.setTypeface(Splash_screen.regular);

        if (!isNetworkAvailable()) {
            Toast.makeText(this,"No Internet",Toast.LENGTH_LONG).show();
        }
        else
        {
            devider=0;
            eneble=0;
            alpha_bets=0;
            methodforscorecard(1);
        }



        explist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent1, View v, int groupPosition, long id) {
                pd.show();
                par_total = 0;
                gross_score_total = 0;
                net_score_total = 0;

                par_postion_1 = 0;
                net_score_postion_1 = 0;
                gross_score_postion_1 = 0;
                par_postion_2 = 0;
                net_score_postion_2 = 0;
                gross_score_postion_2 = 0;

                par_values = new HashMap<String, String>();
                Score_values = new HashMap<String, String>();
                net_score_values = new HashMap<String, String>();
                hole_values = new HashMap<String, String>();

                for (int i = 1; i <= 18; i++) {
                    par_values.put(i + "", "0");
                    Score_values.put(i + "", "0");
                    net_score_values.put(i + "", "0");
                }

                parent_postion = groupPosition;

                List<Child_getter_setter> ll = new ArrayList<>();
                child_list = new Child_getter_setter("", "", "", "");
                ll.add(child_list);
                childArrayList.put(alleventlist_List.get(parent_postion).getId(), ll);
                adapterz.notifyDataSetChanged();
                user_id = alleventlist_List.get(groupPosition).getId();

                if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")) {
                    event_id = alleventlist_List.get(groupPosition).getIndividula_player();
                }

                System.out.println("--------event--------"+event_id);

                try {
                    if (!isNetworkAvailable()) {
                        Toast.makeText(NormalLeaderoard.this, "Internet is required.", Toast.LENGTH_LONG).show();
                    } else {
                        String url_value = "";
                        if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")) {
                            url_value = "hole_info/player/leader_board/" + event_id + "/" + user_id;
                        } else if (get_auth_token("play_type").equals("event")) {
                            url_value = "hole_info/leader_board/" + event_id + "/" + user_id;
                        }

                        System.out.print("------- position ----"+url_value);

                        JsonObjectRequest myRequest_handicap = new JsonObjectRequest(
                                com.android.volley.Request.Method.POST, Splash_screen.url + url_value, null,
                                new com.android.volley.Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            System.out.println("------sys-----" + response);
                                            JSONObject score_details = response.getJSONObject("score_details");

                                            player_name_in_team = "";
                                            // Inside team all Player
                                            try {
                                                JSONArray array = response.getJSONArray("user_names");
                                                for (int i = 0; i < array.length(); i++) {
                                                    player_name_in_team += array.getString(i) + " - ";
                                                }
                                                player_name_in_team = player_name_in_team.substring(0, player_name_in_team.length() - 2);
                                            } catch (Exception e) {
                                            }

                                            try {
                                                team_name =  score_details.getString("team_name");
                                            }catch (Exception e){

                                            }

                                            user_current_postion = score_details.getString("user_position");
                                            handicap_score = score_details.getString("user_handicap");

                                            String total_net_scoress = score_details.getString("total_net_score");
                                            String total_gross_scoress = score_details.getString("total_gross_score");

                                            if (total_net_scoress.equals(null) || total_net_scoress.equals("") || total_net_scoress==null || total_net_scoress.equals("null") ){

                                                net_score_total=0;
                                            }else {
                                                net_score_total = Integer.parseInt(total_net_scoress);
                                            }

                                            if (total_gross_scoress.equals(null) || total_gross_scoress.equals("") || total_gross_scoress==null || total_gross_scoress.equals("null") ){
                                                gross_score_total=0;
                                            }else {
                                                gross_score_total = Integer.parseInt(total_gross_scoress);
                                            }

                                            JSONArray leader_board = response.getJSONArray("hole_array");
                                            hole_size = leader_board.length();
                                            for (int i = 0; i < leader_board.length(); i++) {

                                                JSONObject object_hole = leader_board.getJSONObject(i);
                                                String row_postion = object_hole.getString("position");
                                                String gross_score = object_hole.getString("gross_score");
                                                String net_score = object_hole.getString("net_score");
                                                String par_valuess = object_hole.getString("par");

                                                if (par_valuess.equals("null") || par_valuess.equals("") || par_valuess.equals(null)) {
                                                    par_valuess = "0";
                                                }

                                                if (gross_score.equals("") || gross_score.equals(null) || gross_score.equals("null")) {
                                                    gross_score = "";
                                                }

                                                if (game_name.equals("scramble") || game_name.equals("strokeplay")){
                                                    net_score = "-";

                                                }else if (net_score.equals("") || net_score.equals(null) || net_score.equals("null")) {
                                                    net_score = "";
                                                }

                                                par_values.put(i + 1 + "", par_valuess + "");
                                                Score_values.put(i + 1 + "", gross_score + "");
                                                net_score_values.put(i + 1 + "", net_score + "");
                                                hole_values.put(i + 1 + "", row_postion + "");

                                                par_total += Integer.parseInt(par_valuess);
                                            }
                                            for (int j = 1; j < 10; j++) {
                                                String par_result = par_values.get(j + "");
                                                String gross_result = Score_values.get(j + "");
                                                String net_result = net_score_values.get(j + "");


                                                if (par_result.equals("null") || par_result.equals("") || par_result.equals(null)) {
                                                    par_result = "0";
                                                }
                                                if (gross_result.equals("null") || gross_result.equals("") || gross_result.equals(null)) {
                                                    gross_result = "0";
                                                }
                                                if (net_result.equals("null") || net_result.equals("") || net_result.equals(null) || net_result.equals("-") || net_result=="-") {
                                                    net_result = "0";
                                                }
                                                par_postion_1 += Integer.parseInt(par_result);
                                                net_score_postion_1 += Integer.parseInt(net_result);
                                                gross_score_postion_1 += Integer.parseInt(gross_result);
                                            }

                                            for (int k = 10; k < 19; k++) {
                                                String par_result2 = par_values.get(k + "");
                                                String gross_result2 = Score_values.get(k + "");
                                                String net_result2 = net_score_values.get(k + "");
                                                if (par_result2.equals("null") || par_result2.equals("") || par_result2.equals(null)) {
                                                    par_result2 = "0";
                                                }
                                                if (gross_result2.equals("null") || gross_result2.equals("") || gross_result2.equals(null)) {
                                                    gross_result2 = "0";
                                                }
                                                if (net_result2.equals("null") || net_result2.equals("") ||  net_result2.equals(null) || net_result2.equals("-") || net_result2=="-") {
                                                    net_result2 = "0";
                                                }
                                                par_postion_2 += Integer.parseInt(par_result2);
                                                net_score_postion_2 += Integer.parseInt(net_result2);
                                                gross_score_postion_2 += Integer.parseInt(gross_result2);
                                            }

                                            List<Child_getter_setter> ll = new ArrayList<>();
                                            child_list = new Child_getter_setter("", "", "", "");
                                            ll.add(child_list);
                                            childArrayList.put(alleventlist_List.get(parent_postion).getId(), ll);
                                            adapterz.notifyDataSetChanged();

                                            if (pd.isShowing()) {
                                                pd.dismiss();
                                            }

                                        } catch (Exception e) { }

                                        if (pd.isShowing()) {
                                            pd.dismiss();
                                        }

                                    }
                                },
                                new com.android.volley.Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        NetworkResponse networkResponse = error.networkResponse;
                                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                            // HTTP Status Code: 401 Unauthorized
                                            if (error.networkResponse.statusCode == 401) {
                                                if (pd.isShowing()) {
                                                    pd.dismiss();
                                                }
                                                Alert_Dailog.showNetworkAlert(NormalLeaderoard.this);
                                            } else {

                                                if (pd.isShowing()) {
                                                    pd.dismiss();
                                                }
                                                Toast.makeText(NormalLeaderoard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                                            }
                                        } else {
                                            if (pd.isShowing()) {
                                                pd.dismiss();
                                            }
                                            Toast.makeText(NormalLeaderoard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
                        myRequest_handicap.setRetryPolicy(policy);
                        AppController.getInstance().addToRequestQueue(myRequest_handicap, "tag");
                    }


                } catch (Exception e) {
                    //nothing
                }
                return false;
            }

        });

        explist.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    explist.collapseGroup(lastExpandedPosition);
                }else {
                }

                expand_colapse = 1;
                lastExpandedPosition = groupPosition;
            }
        });
        explist.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                expand_colapse=0;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        methodtocalllivestreaming();
    }

    private void method_to_load_top_section() {

        Button btn_gps;

        swipeRefreshLayout = findViewById(R.id.swipe_view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Define your work here .
                swipeRefreshLayout.setRefreshing(true);
                devider=0;
                eneble=0;
                alpha_bets=0;
                alleventlist_List.clear();
                childArrayList.clear();
                methodforscorecard(2);
            }
        });


            btn_gps = findViewById(R.id.btn_gps);
            btn_gps.setTypeface(medium);
            btn_gps.setOnClickListener(this);

            btn_leader_board = findViewById(R.id.btn_leader_board);
            btn_leader_board.setTypeface(medium);
            btn_leader_board.setOnClickListener(this);

            btn_leader_board.setOnClickListener(this);

            tv_course_name = findViewById(R.id.tv_course_name);
            tv_course_name.setTypeface(regular);

            btn_score_card = findViewById(R.id.btn_score_card);

            if (!Location_Services.game_name.equals("Course")){
                ll_non_course.setVisibility(View.VISIBLE);
                btn_score_card.setVisibility(View.GONE);
                btn_leader_board.setVisibility(View.VISIBLE);
                btn_leader_board.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
            }else {
                ll_non_course.setVisibility(View.GONE);
                btn_score_card.setVisibility(View.VISIBLE);
                btn_score_card.setTypeface(semibold);
                btn_score_card.setOnClickListener(this);
                btn_leader_board.setVisibility(View.GONE);
                image_view_scorecard.setVisibility(View.VISIBLE);
            }

            ImageView image_auction = findViewById(R.id.iv_auction);
            ImageView image_chat = findViewById(R.id.iv_chat);
            ImageView image_super_ticket = findViewById(R.id.iv_super_ticket);
            ImageView image_leader_board = findViewById(R.id.iv_leader_board);
            image_leader_board.setBackgroundResource(R.drawable.new_scorecard);

            image_auction.setOnClickListener(this);
            image_chat.setOnClickListener(this);
            image_super_ticket.setOnClickListener(this);
            image_leader_board.setOnClickListener(this);

        }

    @Override
    public void onClick(View view) {
        Intent in;
        switch (view.getId()) {
            case R.id.btn_score_card:
                in = new Intent(NormalLeaderoard.this, NormalScorreboard.class);
                in.putExtra("home",home);
                startActivity(in);
                finish();
                break;


            case R.id.btn_leader_board:
                    if (isnotcourse) {
                        isnotcourse = false;
                        if (get_auth_token("CGB_event_code").equals(get_auth_token("current_event_code"))){
                            image_view_scorecard.setVisibility(View.VISIBLE);
                            ll_non_course.setVisibility(View.GONE);
                        }else {
                            ll_non_course.setVisibility(View.VISIBLE);
                            image_view_scorecard.setVisibility(View.GONE);
                        }
                        btn_leader_board.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                    } else {
                        isnotcourse = true;
                         image_view_scorecard.setVisibility(View.GONE);
                         ll_non_course.setVisibility(View.GONE);
                        btn_leader_board.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                    }

                break;

            case R.id.iv_super_ticket:
                Super_ticket_poup();
                break;

            case R.id.iv_auction:
                in=new Intent(this, NewLive_Auction.class);
                savestring("id",event_id);
                startActivity(in);
                break;

            case R.id.iv_leader_board:
                in = new Intent(NormalLeaderoard.this, NormalScorreboard.class);
                in.putExtra("home",home);
                startActivity(in);
                finish();
                break;

            case R.id.iv_chat:
                in=new Intent(this, User_List.class);
                startActivity(in);
                break;

            case R.id.btn_gps:
                if (home.equals("home")){
                    in = new Intent(NormalLeaderoard.this , View_course_map.class);
                    savestring("id",event_id);
                    in.putExtra("home",home);
                    startActivity(in);
                    finish();
                }else {
                    finish();
                }
                break;

            case R.id.tv_live_leaderboard:
                if (Location_Services.game_name.equals("Course")){
                    ll_addsense.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.image_view_scorecard:
                in = new Intent(NormalLeaderoard.this, NormalScorreboard.class);
                in.putExtra("home",home);
                startActivity(in);
                finish();
                break;

        }

    }

    public void Super_ticket_poup() {
        android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NormalLeaderoard.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogview = inflater.inflate(R.layout.pop_up_super_ticket, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;

        TextView tv_super_content = alertDialog.findViewById(R.id.pop_super_ticket);
        tv_super_content.setTypeface(medium);

        TextView pop_super_content = alertDialog.findViewById(R.id.pop_super_content);
        pop_super_content.setTypeface(medium);


        TextView super_ticket_values = alertDialog.findViewById(R.id.pop_super_ticket_values);
        super_ticket_values.setTypeface(regular);
        super_ticket_values.setText(get_auth_token("live_aminties"));

        TextView tv_super_ticket_content = alertDialog.findViewById(R.id.pop_super_ticket_content);
        tv_super_ticket_content.setTypeface(regular);

        String locations = oragnization.substring(0,1).toUpperCase() +oragnization.substring(1);
        tv_super_ticket_content.setText("All super ticket proceeds go to "+locations+" Thank you for your generous donation.");


        TextView tv_sponsor_title = alertDialog.findViewById(R.id.pop_sponsor_title);
        tv_sponsor_title.setTypeface(medium);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recler_sponsor = alertDialog.findViewById(R.id.pop_recler_sponsor);
        recler_sponsor.setLayoutManager(layoutManager2);

        NewEventDetailPage.SponsorLogo allevents_hori = new NewEventDetailPage.SponsorLogo(this, sponsor_logo);
        recler_sponsor.setAdapter(allevents_hori);

        TextView empty_sponsor = alertDialog.findViewById(R.id.pop_empty_sponsor);
        empty_sponsor.setTypeface(regular);

        if (sponsor_logo.size()>0){
            recler_sponsor.setVisibility(View.VISIBLE);
        }else {
            empty_sponsor.setVisibility(View.VISIBLE);
        }

        ImageView img_cross = alertDialog.findViewById(R.id.img_cross);
        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void methodtocalllivestreaming() {
        client = new OkHttpClient();
        Request request = new Request.Builder().url(Server_url).addHeader("auth-token",auth_token).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }


    private void output(final String txt) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject response = new JSONObject(txt);
                    try {
                        JSONObject message = response.getJSONObject("message");
                        JSONObject json = message.getJSONObject("json");
                        JSONArray leader_board = json.getJSONArray("user_leader_board");
                        alleventlist_List.clear();
                        for (int b=0;b<leader_board.length();b++) {
                            JSONObject leaderboard_object = leader_board.getJSONObject(b);
                            String user_ids = leaderboard_object.getString("user_id");
                            String total_net_scores = leaderboard_object.getString("total_net_score");
                            String total_scores = leaderboard_object.getString("total_score");
                            String user_postions = leaderboard_object.getString("user_position");
                            String user_names = leaderboard_object.getString("user_name").substring(0, 1).toUpperCase() + leaderboard_object.getString("user_name").substring(1);

                            String user_thru = leaderboard_object.getString("thru");

                            if (total_net_scores.equals("null") || total_net_scores.equals("") || total_net_scores.equals(null)) {
                                total_net_scores = "";
                            }
                            if (total_scores.equals("null") || total_scores.equals("") || total_scores.equals(null)) {
                                total_scores = "";
                            }
                            parent_list = new parent_getter_setter(user_postions, user_ids, user_names, total_net_scores, total_scores,"","",user_thru);
                            alleventlist_List.add(parent_list);

                        }
                        adapterz = new ListAdapterExpandible(NormalLeaderoard.this, alleventlist_List,childArrayList);
                        explist.setAdapter(adapterz);
                        adapterz.notifyDataSetChanged();

                    }catch (Exception e){
                    }

                    try {
                        JSONObject message = response.getJSONObject("message");
                        JSONObject json = message.getJSONObject("json");
                        String gross_score = json.getString("gross_score");
                        String net_score = json.getString("net_score");
                        String total_score = json.getString("total_score");
                        String hole_number = json.getString("hole_number");
                        String user_idss = json.getString("user_id");
                        String sender_auth_token = json.getString("sender_auth_token");
                        String total_net_scoress = json.getString("total_net_score");
                        String total_gross_score = json.getString("total_gross_score");
                        user_current_postion = json.getString("position");
                        int postion = Integer.parseInt(hole_number);
                        if (!sender_auth_token.equals(auth_token)){

                            alleventlist_List.clear();
                            JSONArray leader_board = json.getJSONArray("leader_board");
                            for (int b=0;b<leader_board.length();b++){
                                JSONObject leaderboard_object = leader_board.getJSONObject(b);
                                String user_ids = leaderboard_object.getString("user_id");
                                String total_net_scores = leaderboard_object.getString("total_net_score");
                                String total_scores = leaderboard_object.getString("total_score");
                                String user_postions = leaderboard_object.getString("user_position");

                                String user_thru = leaderboard_object.getString("thru");

                                String user_names = leaderboard_object.getString("user_name").substring(0,1).toUpperCase()+leaderboard_object.getString("user_name").substring(1);
                                if (total_net_scores.equals("null") || total_net_scores.equals("") ||total_net_scores.equals(null)){
                                    total_net_scores="";
                                }
                                if (total_scores.equals("null") || total_scores.equals("") ||total_scores.equals(null)){
                                    total_scores="";
                                }
                                parent_list = new parent_getter_setter(user_postions,user_ids,user_names,total_net_scores,total_scores,"","",user_thru);
                                alleventlist_List.add(parent_list);
                                if (user_id.equals(user_idss)){
                                    gross_score_total = Integer.parseInt(total_gross_score);
                                    net_score_total = Integer.parseInt(total_net_scoress);
                                    Score_values.put(hole_number,gross_score+"");
                                    net_score_values.put(hole_number,net_score+"");
                                    if (postion<10) {
                                        net_score_postion_1 =0;
                                        gross_score_postion_1 = 0;
                                        for (int j = 1; j< 10 ;j++){
                                            String gross_result = Score_values.get(j+"");
                                            String net_result = net_score_values.get(j+"");
                                            if (gross_result.equals("null") || gross_result.equals("") || gross_result.equals(null)){
                                                gross_result = "0";
                                            }
                                            if (net_result.equals("null") || net_result.equals("") || net_result.equals(null)){
                                                net_result = "0";
                                            }
                                            net_score_postion_1 += Integer.parseInt(net_result);
                                            gross_score_postion_1 += Integer.parseInt(gross_result);
                                        }
                                    }else {
                                        net_score_postion_2 = 0;
                                        gross_score_postion_2 = 0;
                                        for (int k = 10; k < 19 ; k++){
                                            String gross_result2 = Score_values.get(k+"");
                                            String net_result2 = net_score_values.get(k+"");
                                            if (gross_result2.equals("null") || gross_result2.equals("") || gross_result2.equals(null)){
                                                gross_result2 = "0";
                                            }
                                            if (net_result2.equals("null") || net_result2.equals("") || net_result2.equals(null)){
                                                net_result2 = "0";}
                                            net_score_postion_2 += Integer.parseInt(net_result2);
                                            gross_score_postion_2 += Integer.parseInt(gross_result2);
                                        }
                                    }
                                }
                            }

                            adapterz = new ListAdapterExpandible(NormalLeaderoard.this, alleventlist_List,childArrayList);
                            explist.setAdapter(adapterz);
                            adapterz.notifyDataSetChanged();

                        }
                    }catch (Exception e){
                        //nothing
                    }

                    try {
                        JSONObject message = response.getJSONObject("message");
                        JSONObject json = message.getJSONObject("json");
                        String user_idss = json.getString("user_id");
                        String sender_auth_token = json.getString("sender_auth_token");
                        JSONObject score_card = json.getJSONObject("score_card");
                        String total_score = score_card.getString("total_score");
                        String total_net_scoress = score_card.getString("total_net_score");
                        String total_gross_scoress = score_card.getString("total_gross_score");
                        String user_postion = score_card.getString("user_position");

                        String user_thru = score_card.getString("thru");

                        if (total_net_scoress.equals(null) || total_net_scoress.equals("") || total_net_scoress==null){
                            net_score_total=0;
                        }else {
                            net_score_total = Integer.parseInt(total_net_scoress);
                        }

                        if (total_gross_scoress.equals(null) || total_gross_scoress.equals("") || total_gross_scoress==null){
                            gross_score_total=0;
                        }else {
                            gross_score_total = Integer.parseInt(total_gross_scoress);
                        }

                        if (!sender_auth_token.equals(auth_token)){
                            par_total=0;
                            par_postion_1=0;
                            net_score_postion_1=0;
                            gross_score_postion_1=0;
                            par_postion_2=0;
                            net_score_postion_2=0;
                            gross_score_postion_2=0;

                            try {
                                for (int i = 0 ; i<alleventlist_List.size(); i++) {

                                    String list_user_id = alleventlist_List.get(i).getId();
                                    if (list_user_id.equals(user_idss)){
                                        String user_name = alleventlist_List.get(i).getName();
                                        if (list_user_id.equals(user_idss)) {
                                            parent_list = new parent_getter_setter(user_postion,user_idss, user_name, total_net_scoress, total_score,"","",user_thru);
                                            alleventlist_List.set(i, parent_list);
                                            adapterz = new ListAdapterExpandible(NormalLeaderoard.this, alleventlist_List,childArrayList);
                                            explist.setAdapter(adapterz);
                                            adapterz.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }catch (Exception e){

                            }
                            try {
                                if (user_id.equals(user_idss)){
                                    try {
                                        for (int i = 0 ; i<alleventlist_List.size(); i++) {

                                            String list_user_id = alleventlist_List.get(i).getId();
                                            String user_name = alleventlist_List.get(i).getName();
                                            if (list_user_id.equals(user_idss)) {
                                                parent_list = new parent_getter_setter(user_postion,user_idss, user_name, total_net_scoress, total_score,"","",user_thru);
                                                alleventlist_List.set(i, parent_list);
                                                adapterz = new ListAdapterExpandible(NormalLeaderoard.this, alleventlist_List,childArrayList);
                                                explist.setAdapter(adapterz);
                                                adapterz.notifyDataSetChanged();
                                            }
                                        }
                                        handicap_score = json.getString("handicap");
                                        JSONArray leader_board = json.getJSONArray("hole_array");
                                        for (int i = 0; i < leader_board.length(); i++) {
                                            JSONObject object_hole = leader_board.getJSONObject(i);
                                            String row_postion = object_hole.getString("position");
                                            String gross_score = object_hole.getString("gross_score");
                                            String net_score = object_hole.getString("net_score");
                                            String par_valuess = object_hole.getString("par");
                                            if (gross_score.equals("") || gross_score.equals(null) || gross_score.equals("null")) {
                                                gross_score = "";
                                            }

                                            if (net_score.equals("") || net_score.equals(null) || net_score.equals("null")) {
                                                net_score = "";
                                            }

                                            if (par_valuess.equals("") || par_valuess.equals(null) || par_valuess.equals("null")) {
                                                par_valuess = "";
                                            }

                                            par_values.put(i+1+"", par_valuess + "");
                                            Score_values.put(i+1+"", gross_score + "");
                                            net_score_values.put(i+1+"", net_score + "");
                                            hole_values.put(i+1+"", row_postion + "");

                                            par_total += Integer.parseInt(par_valuess);
                                        }
                                        alleventlist_List.clear();
                                        JSONArray leader_boards = json.getJSONArray("leader_board");
                                        for (int b=0;b<leader_boards.length();b++) {
                                            JSONObject leaderboard_objects = leader_boards.getJSONObject(b);
                                            String new_user_idss = leaderboard_objects.getString("user_id");
                                            String new_total_net_scoress = leaderboard_objects.getString("total_net_score");
                                            String new_total_scoress = leaderboard_objects.getString("total_score");
                                            String new_user_postionss = leaderboard_objects.getString("user_position");
                                            String new_user_namess = leaderboard_objects.getString("user_name").substring(0, 1).toUpperCase() + leaderboard_objects.getString("user_name").substring(1);

                                            String user_thrus = leaderboard_objects.getString("user_position");


                                            if (new_total_net_scoress.equals("null") || new_total_net_scoress.equals("") || new_total_net_scoress.equals(null)) {
                                                new_total_net_scoress = "";
                                            }
                                            if (new_total_scoress.equals("null") || new_total_scoress.equals("") || new_total_scoress.equals(null)) {
                                                new_total_scoress = "";
                                            }
                                            parent_list = new parent_getter_setter(new_user_postionss, user_idss, new_user_namess, new_total_net_scoress, new_total_scoress,"","",user_thrus);
                                            alleventlist_List.add(parent_list);
                                        }
                                        for (int j = 1; j < 10; j++) {
                                            String par_result = par_values.get(j + "");
                                            String gross_result = Score_values.get(j + "");
                                            String net_result = net_score_values.get(j + "");

                                            if (par_result.equals("null") || par_result.equals("") || par_result.equals(null)) {
                                                par_result = "0";
                                            }
                                            if (gross_result.equals("null") || gross_result.equals("") || gross_result.equals(null)) {
                                                gross_result = "0";
                                            }
                                            if (net_result.equals("null") || net_result.equals("") || net_result.equals(null)) {
                                                net_result = "0";
                                            }
                                            par_postion_1 += Integer.parseInt(par_result);
                                            net_score_postion_1 += Integer.parseInt(net_result);
                                            gross_score_postion_1 += Integer.parseInt(gross_result);
                                        }
                                        for (int k = 10; k < 19; k++) {
                                            String par_result2 = par_values.get(k + "");
                                            String gross_result2 = Score_values.get(k + "");
                                            String net_result2 = net_score_values.get(k + "");
                                            if (par_result2.equals("null") || par_result2.equals("") || par_result2.equals(null)) {
                                                par_result2 = "0";
                                            }
                                            if (gross_result2.equals("null") || gross_result2.equals("") || gross_result2.equals(null)) {
                                                gross_result2 = "0";
                                            }
                                            if (net_result2.equals("null") || net_result2.equals("") || net_result2.equals(null)) {
                                                net_result2 = "0";
                                            }
                                            par_postion_2 += Integer.parseInt(par_result2);
                                            net_score_postion_2 += Integer.parseInt(net_result2);
                                            gross_score_postion_2 += Integer.parseInt(gross_result2);
                                        }
                                        adapterz = new ListAdapterExpandible(NormalLeaderoard.this, alleventlist_List,childArrayList);
                                        explist.setAdapter(adapterz);
                                        adapterz.notifyDataSetChanged();

                                        if (pd.isShowing()) {
                                            pd.dismiss();
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }catch (Exception e){
                            }
                        }

                    }catch (Exception e){
                    }

                }catch (Exception  e){
                    //nothing
                }
            }
        });
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 4000;
        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            webSocket.send("{\"command\":\"subscribe\", \"identifier\":\"{\\\"channel\\\":\\\"LiveScoringsChannel\\\"}\"}");
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output(text);
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
            output("Error : " + t.getMessage());
        }
    }



    public void methodforscorecard(int progress) {
        String url_value="";
        if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")){
            url_value = "hole_info/player/leader_board/"+event_id;
        }else if (get_auth_token("play_type").equals("event")){
            url_value = "hole_info/leader_board/" + event_id;
        }else if (get_auth_token("play_type").equals("individual")) {
            url_value = "hole_info/leader_board/" + event_id;
        }

        try {
            if (progress==1){
                pd = new ProgressDialog(NormalLeaderoard.this);
                pd.setMessage("Please wait.");
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                pd.show();
            }

            System.out.println("--------res-----"+Splash_screen.url +url_value);
            JsonObjectRequest myRequest_handicap = new JsonObjectRequest(
                    com.android.volley.Request.Method.POST, Splash_screen.url +url_value, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println("--------res-----"+response);

                                String game_type = "";
                                if (game_name.equals("scramble")) {
                                    game_type = "Gross " + response.getString("total_gross_score") + "/" + response.getString("total_par_value")+" - Net " + "</b>" + response.getString("total_net_score") + "/" + response.getString("total_par_value");
                                } else {
                                    game_type = "Gross " + response.getString("total_gross_score") + "/" + response.getString("total_par_value")+" - Net " + "</b>" + response.getString("total_net_score") + "/" + response.getString("total_par_value");
                                }

                                if (game_name.equals("strokeplay")){
                                    tv_course_name.setText(Html.fromHtml("<b>"+get_auth_token("Event_name")+"</b>"+"<br>Starting "+ "Hole "+get_auth_token("selected_hole_postion")+"<b>" + " - "+ "</b>"+ get_auth_token("selected_tee_postion")+" "+get_auth_token("yards_value")+"<br>"+game_type));
                                }else if (game_name.equals("Course")){
                                    tv_course_name.setText(Html.fromHtml("<b>"+get_auth_token("Event_name")+"</b>"+"<br>Starting "+ "Hole "+get_auth_token("starting_hole")+"<b>" + " - "+ "</b>"+ get_auth_token("selected_tee_postion")+" "+get_auth_token("yards_value")+"<br>"+game_type));
                                }else {
                                    tv_course_name.setText(Html.fromHtml("<b>"+get_auth_token("Event_name")+"</b>"+"<br>Starting "+ "Hole "+get_auth_token("starting_hole")+"<b>" + " - "+ "</b>"+ get_auth_token("selected_tee_postion")+" "+get_auth_token("yards_value")+"<br>"+game_type));
                                }

                                JSONArray leader_board = response.getJSONArray("leader_board");

                                try {
                                    if (response.getString("flight_type").equals("enabled")){
                                        eneble=1;

                                        JSONObject flight_names = response.getJSONObject("flight_names");
                                        Iterator keys = flight_names.keys();
                                        while(keys.hasNext()) {
                                            // loop to get the dynamic key
                                            String flight_vales = (String) keys.next();
                                            flight_list.add(flight_names.getString(flight_vales));
                                        }

                                        Double devider_ss = Double.parseDouble(String.valueOf(leader_board.length()))/Double.parseDouble(response.getString("no_of_flights"));
                                        devider = (int) Math.round(Math.ceil(devider_ss));
                                    }else {
                                        text_champin_ship.setVisibility(View.GONE);
                                    }

                                }catch (Exception e){
                                    text_champin_ship.setVisibility(View.GONE);
                                }

                                for (int i=0;i<leader_board.length();i++){
                                    JSONObject object =leader_board.getJSONObject(i);

                                    String user_id = object.getString("user_id");
                                    String total_net_score = object.getString("total_net_score");
                                    String total_score = object.getString("total_score");
                                    String user_position = object.getString("user_position");
                                    String individula_player = object.getString("individual_player_id");
                                    String user_thrus = object.getString("thru");

                                    String user_name="";

                                    if (total_score.equals("null") || total_score.equals("") ||total_score.equals(null)){
                                        total_score="";
                                    }

                                    if (game_name.equals("scramble")){
                                        user_name= object.getString("team_name").substring(0,1).toUpperCase()+object.getString("team_name").substring(1);
                                        if (total_net_score.equals("null") || total_net_score.equals("") ||total_net_score.equals(null)){
                                            total_net_score="";
                                        }
                                    }else {
                                        user_name= object.getString("user_name").substring(0,1).toUpperCase()+object.getString("user_name").substring(1);
                                        if (total_net_score.equals("null") || total_net_score.equals("") ||total_net_score.equals(null)){
                                            total_net_score="";
                                        }
                                    }

                                    if (eneble==1){
                                        if (i%devider==0){
                                            parent_list = new parent_getter_setter(user_position,user_id,user_name,total_net_score,total_score,flight_list.get(alpha_bets),individula_player,user_thrus);
                                            alpha_bets++;
                                        }else {
                                            parent_list = new parent_getter_setter(user_position,user_id,user_name,total_net_score,total_score,"0",individula_player,user_thrus);
                                        }

                                    }else {

                                        parent_list = new parent_getter_setter(user_position,user_id,user_name,total_net_score,total_score,"0",individula_player,user_thrus);

                                    }

                                    alleventlist_List.add(parent_list);
                                }

                                // if number flights enabled go to this flow
                                adapterz = new ListAdapterExpandible(NormalLeaderoard.this, alleventlist_List,childArrayList);
                                explist.setAdapter(adapterz);

                                if (progress==1){
                                    if (pd.isShowing()) {
                                        pd.dismiss();
                                    }
                                }else {
                                    swipeRefreshLayout.setRefreshing(false);
                                }

                            }catch (Exception e){

                            }
                            if (progress==1){
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }else {
                                swipeRefreshLayout.setRefreshing(false);
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                // HTTP Status Code: 401 Unauthorized
                                if (error.networkResponse.statusCode == 401) {
                                    if (pd.isShowing()) {
                                        pd.dismiss();
                                    }
                                    Alert_Dailog.showNetworkAlert(NormalLeaderoard.this);
                                } else {

                                    if (pd.isShowing()) {
                                        pd.dismiss();
                                    }
                                    Toast.makeText(NormalLeaderoard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                                Toast.makeText(NormalLeaderoard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
            myRequest_handicap.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(myRequest_handicap, "tag");

        } catch (Exception e) {
            //nothing
        }

    }


    public class ListAdapterExpandible extends BaseExpandableListAdapter {

        private Context context;
        private List<parent_getter_setter> headerArray;
        private HashMap<String, List<Child_getter_setter>> childArray;
        private LayoutInflater infalInflater;

        // Initialize constructor for array list
        public ListAdapterExpandible(Context context, List<parent_getter_setter> headerArray, HashMap<String, List<Child_getter_setter>> listChildData) {
            this.context = context;
            this.headerArray = headerArray;
            this.childArray = listChildData;
            infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this.childArray.get(this.headerArray.get(groupPosition).getId())
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public int getChildTypeCount() {
            return super.getChildTypeCount();
        }
        // Inflate child view


        @Override
        public int getChildType(int groupPosition, int childPosition) {
            return super.getChildType(groupPosition, childPosition);
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = infalInflater.inflate(R.layout.childrow, null);
            }

            if(groupPosition %2 == 1)
            {
                convertView.setBackgroundResource(R.color.colorwhite);
            }
            else
            {
                convertView.setBackgroundResource(R.color.leader_board_row_color);
            }



            TextView tv_eadge = convertView.findViewById(R.id.tv_eadge);
            TextView tv_bride = convertView.findViewById(R.id.tv_bride);
            TextView tv_bogey = convertView.findViewById(R.id.tv_bogey);
            TextView tv_double_bogey = convertView.findViewById(R.id.tv_double_bogey);

            tv_eadge.setTypeface(regular);
            tv_bride.setTypeface(regular);
            tv_bogey.setTypeface(regular);
            tv_double_bogey.setTypeface(regular);

            TextView tv_colunm_1 = convertView.findViewById(R.id.tv_colunm_1);
            TextView tv_colunm_2 = convertView.findViewById(R.id.tv_colunm_2);
            TextView tv_colunm_3 = convertView.findViewById(R.id.tv_colunm_3);
            TextView tv_colunm_4 = convertView.findViewById(R.id.tv_colunm_4);
            TextView tv_colunm_5 = convertView.findViewById(R.id.tv_colunm_5);
            TextView tv_colunm_6 = convertView.findViewById(R.id.tv_colunm_6);
            TextView tv_colunm_7 = convertView.findViewById(R.id.tv_colunm_7);
            TextView tv_colunm_8 = convertView.findViewById(R.id.tv_colunm_8);
            TextView tv_colunm_9 = convertView.findViewById(R.id.tv_colunm_9);
            TextView tv_colunm_10 = convertView.findViewById(R.id.tv_colunm_10);
            TextView tv_colunm_11 = convertView.findViewById(R.id.tv_colunm_11);
            TextView tv_colunm_12 = convertView.findViewById(R.id.tv_colunm_12);
            TextView tv_colunm_13 = convertView.findViewById(R.id.tv_colunm_13);
            TextView tv_colunm_14 = convertView.findViewById(R.id.tv_colunm_14);
            TextView tv_colunm_15 = convertView.findViewById(R.id.tv_colunm_15);
            TextView tv_colunm_16 = convertView.findViewById(R.id.tv_colunm_16);
            TextView tv_colunm_17 = convertView.findViewById(R.id.tv_colunm_17);
            TextView tv_colunm_18 = convertView.findViewById(R.id.tv_colunm_18);
            TextView tv_colunm_19 = convertView.findViewById(R.id.tv_colunm_19);
            TextView tv_colunm_20 = convertView.findViewById(R.id.tv_colunm_20);
            TextView tv_colunm_21 = convertView.findViewById(R.id.tv_colunm_21);
            TextView tv_colunm_22 = convertView.findViewById(R.id.tv_colunm_22);

            tv_colunm_1.setTypeface(medium);
            tv_colunm_2.setTypeface(medium);
            tv_colunm_3.setTypeface(medium);
            tv_colunm_4.setTypeface(medium);
            tv_colunm_5.setTypeface(medium);
            tv_colunm_6.setTypeface(medium);
            tv_colunm_7.setTypeface(medium);
            tv_colunm_8.setTypeface(medium);
            tv_colunm_9.setTypeface(medium);
            tv_colunm_10.setTypeface(medium);
            tv_colunm_11.setTypeface(medium);
            tv_colunm_12.setTypeface(medium);
            tv_colunm_13.setTypeface(medium);
            tv_colunm_14.setTypeface(medium);
            tv_colunm_15.setTypeface(medium);
            tv_colunm_16.setTypeface(medium);
            tv_colunm_17.setTypeface(medium);
            tv_colunm_18.setTypeface(medium);
            tv_colunm_19.setTypeface(medium);
            tv_colunm_20.setTypeface(medium);
            tv_colunm_21.setTypeface(medium);
            tv_colunm_22.setTypeface(medium);

            View second_row;
            second_row = convertView.findViewById(R.id.second_row);

            if (hole_size>9){
                tv_colunm_11.setText("OUT");
                second_row.setVisibility(View.VISIBLE);

                TextView tv_colunm_tot = convertView.findViewById(R.id.tv_colunm_tot);
                tv_colunm_tot.setTypeface(medium);

                TextView tv_par_colunm_tot = convertView.findViewById(R.id.tv_par_colunm_tot);
                tv_par_colunm_tot.setTypeface(medium);

                TextView tv_colunm_score = convertView.findViewById(R.id.tv_colunm_score);
                tv_colunm_score.setTypeface(medium);

                int total_par = par_postion_1+par_postion_2;
                tv_par_colunm_tot.setText(total_par+"");

                int tot_score = gross_score_postion_1+gross_score_postion_2;
                tv_colunm_score.setText(tot_score+"");

            }else {
                tv_colunm_11.setText("TOT");
                second_row.setVisibility(View.GONE);
            }

            tv_par_score = convertView.findViewById(R.id.tv_par_score);
            tv_gross_score = convertView.findViewById(R.id.tv_gross_score);
            tv_handicap = convertView.findViewById(R.id.tv_handicap);
            tv_pos_t = convertView.findViewById(R.id.tv_pos_t);

            tv_par_score.setTypeface(regular);
            tv_gross_score.setTypeface(regular);
            tv_handicap.setTypeface(regular);
            tv_pos_t.setTypeface(regular);

            if (!Location_Services.game_name.equals("Course")) {
                tv_par_score.setVisibility(View.VISIBLE);
                tv_gross_score.setVisibility(View.VISIBLE);
                tv_handicap.setVisibility(View.VISIBLE);
                tv_pos_t.setVisibility(View.VISIBLE);

            }else {
                tv_par_score.setVisibility(View.GONE);
                tv_gross_score.setVisibility(View.GONE);
                tv_handicap.setVisibility(View.GONE);
                tv_pos_t.setVisibility(View.GONE);
            }

            TextView leadrbord_all_team = convertView.findViewById(R.id.leadrbord_all_team);
            leadrbord_all_team.setTypeface(Splash_screen.regular);

            tv_par_score.setText(Html.fromHtml("Par: "+"<b>"+par_total+"</b>"));

            tv_pos_t.setText(Html.fromHtml("Position: "+"<b>"+user_current_postion+"</b>"));

            if (Integer.parseInt(handicap_score)>0){
                if (get_auth_token("handicap_score").equals("diseble")){
                    tv_gross_score.setText(Html.fromHtml("Score: "+"<b>"+gross_score_total+"</b>"));
                    tv_handicap.setText(Html.fromHtml("Handicap: "+"<b>Disabled</b>"));
                    leadrbord_all_team.setText(Html.fromHtml("<b>"+team_name+"</b>"+": " +player_name_in_team));
                }else {
                    leadrbord_all_team.setText(Html.fromHtml("<b>"+team_name+"</b>"+": " +player_name_in_team));
                    tv_gross_score.setText(Html.fromHtml("Score: "+"<b>"+net_score_total+"</b>"));
                    tv_handicap.setText(Html.fromHtml("Handicap: +"+"<b>"+handicap_score+"</b>"));
                }

            }else {
                if (get_auth_token("handicap_score").equals("diseble")){
                    tv_handicap.setText(Html.fromHtml("Handicap: "+"<b>Disabled</b>"));
                    tv_gross_score.setText(Html.fromHtml("Score: "+"<b>"+net_score_total+"</b>"));
                    leadrbord_all_team.setText(Html.fromHtml("<b>"+team_name+"</b>"+": " +player_name_in_team));
                }else {
                    tv_gross_score.setText(Html.fromHtml("Score: "+"<b>"+net_score_total+"</b>"));
                    leadrbord_all_team.setText(Html.fromHtml("<b>"+team_name+"</b>"+": " +player_name_in_team));
                    tv_handicap.setText(Html.fromHtml("Handicap: "+"<b>"+handicap_score+"</b>"));
                }

            }


            // set all hole position
            tv_colunm_2.setText(hole_values.get(1+""));
            tv_colunm_3.setText(hole_values.get(2+""));
            tv_colunm_4.setText(hole_values.get(3+""));
            tv_colunm_5.setText(hole_values.get(4+""));
            tv_colunm_6.setText(hole_values.get(5+""));
            tv_colunm_7.setText(hole_values.get(6+""));
            tv_colunm_8.setText(hole_values.get(7+""));
            tv_colunm_9.setText(hole_values.get(8+""));
            tv_colunm_10.setText(hole_values.get(9+""));

            tv_colunm_13.setText(hole_values.get(10+""));
            tv_colunm_14.setText(hole_values.get(11+""));
            tv_colunm_15.setText(hole_values.get(12+""));
            tv_colunm_16.setText(hole_values.get(13+""));
            tv_colunm_17.setText(hole_values.get(14+""));
            tv_colunm_18.setText(hole_values.get(15+""));
            tv_colunm_19.setText(hole_values.get(16+""));
            tv_colunm_20.setText(hole_values.get(17+""));
            tv_colunm_21.setText(hole_values.get(18+""));


            TextView roq_ii_1 = convertView.findViewById(R.id.roq_ii_1);
            TextView roq_iii_1 = convertView.findViewById(R.id.roq_iii_1);
            TextView roq_iiii_1 = convertView.findViewById(R.id.roq_iiii_1);

            roq_ii_1.setTypeface(regular);
            roq_iii_1.setTypeface(regular);
            roq_iiii_1.setTypeface(regular);
//
//  1 coloum
//
            TextView roq_ii_2 = convertView.findViewById(R.id.roq_ii_2);
            TextView roq_iii_2 = convertView.findViewById(R.id.roq_iii_2);
            TextView roq_iiii_2 = convertView.findViewById(R.id.roq_iiii_2);

            roq_ii_2.setTypeface(regular);
            roq_iii_2.setTypeface(regular);
            roq_iiii_2.setTypeface(regular);

            String row_1__par_1  = par_values.get(1+"")+"";
            String row_2_score_1 = Score_values.get(1+"")+"";

            int row_1_result_1 = 0;
            if (row_1__par_1=="0" || row_1__par_1=="null" || row_1__par_1=="" || row_1__par_1==null){
            }else {
                if (row_2_score_1=="0" || row_2_score_1=="null" || row_2_score_1=="" || row_2_score_1==null){
                    roq_iii_2.setBackground(null);
                }else{
                    row_1_result_1 = (Integer.parseInt(row_2_score_1)) - Integer.parseInt(row_1__par_1);
                    if (row_1_result_1 == 0) {
                        roq_iii_2.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_1 == -1) {
                        roq_iii_2.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_1 < -1) {
                        roq_iii_2.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_1 == 1) {
                        roq_iii_2.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_1 >= 1) {
                        roq_iii_2.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }

            roq_ii_2.setText(par_values.get(1+"")+"");
            roq_iii_2.setText(Score_values.get(1+"")+"");
            if (game_name.equals("scramble")){
                roq_iiii_2.setText("-");
            }else {
                roq_iiii_2.setText(net_score_values.get(1+"")+"");
            }

            //2 coloum

            TextView roq_ii_3 = convertView.findViewById(R.id.roq_ii_3);
            TextView roq_iii_3 = convertView.findViewById(R.id.roq_iii_3);
            TextView roq_iiii_3 = convertView.findViewById(R.id.roq_iiii_3);

            roq_ii_3.setTypeface(regular);
            roq_iii_3.setTypeface(regular);
            roq_iiii_3.setTypeface(regular);

            String row_1__par_2  = par_values.get(2+"")+"";
            String row_2_score_2 = Score_values.get(2+"")+"";
            int row_1_result_2 = 0;
            if (row_1__par_2=="0" || row_1__par_2=="null" || row_1__par_2=="" || row_1__par_2==null){
            }else {
                if (row_2_score_2=="0" || row_2_score_2=="null" || row_2_score_2=="" || row_2_score_2==null){
                    roq_iii_3.setBackground(null);
                }else{
                    row_1_result_2 = (Integer.parseInt(row_2_score_2)) - Integer.parseInt(row_1__par_2);
                    if (row_1_result_2 == 0) {
                        roq_iii_3.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_2 == -1) {
                        roq_iii_3.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_2 < -1) {
                        roq_iii_3.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_2 == 1) {
                        roq_iii_3.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_2 >= 1) {
                        roq_iii_3.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }

            roq_ii_3.setText(par_values.get(2+"")+"");
            roq_iii_3.setText(Score_values.get(2+"")+"");
            if (game_name.equals("scramble")){
                roq_iiii_3.setText("-");
            }else {
                roq_iiii_3.setText(net_score_values.get(3+"")+"");
            }

            //3 coloum

            TextView roq_ii_4 = convertView.findViewById(R.id.roq_ii_4);
            TextView roq_iii_4 = convertView.findViewById(R.id.roq_iii_4);
            TextView roq_iiii_4 = convertView.findViewById(R.id.roq_iiii_4);

            roq_ii_4.setTypeface(regular);
            roq_iii_4.setTypeface(regular);
            roq_iiii_4.setTypeface(regular);

            String row_1__par_3  = par_values.get(3+"")+"";
            String row_2_score_3 = Score_values.get(3+"")+"";
            int row_1_result_3 = 0;
            if (row_1__par_3=="0" || row_1__par_3=="null" || row_1__par_3=="" || row_1__par_3==null){
            }else {
                if (row_2_score_3=="0" || row_2_score_3=="null" || row_2_score_3=="" || row_2_score_3==null){
                    roq_iii_4.setBackground(null);
                }else{
                    row_1_result_3 = (Integer.parseInt(row_2_score_3)) - Integer.parseInt(row_1__par_3);
                    if (row_1_result_3 == 0) {
                      roq_iii_4.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_3 == -1) {
                        roq_iii_4.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_3 < -1) {
                        roq_iii_4.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_3 == 1) {
                        roq_iii_4.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_3 >= 1) {
                        roq_iii_4.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }

            roq_ii_4.setText(par_values.get(3+"")+"");
            roq_iii_4.setText(Score_values.get(3+"")+"");
            if (game_name.equals("scramble")){
                roq_iiii_4.setText("-");
            }else {
                roq_iiii_4.setText(net_score_values.get(3+"")+"");
            }
            //4 coloum

            TextView roq_ii_5 = convertView.findViewById(R.id.roq_ii_5);
            TextView roq_iii_5 = convertView.findViewById(R.id.roq_iii_5);
            TextView roq_iiii_5 = convertView.findViewById(R.id.roq_iiii_5);

            roq_ii_5.setTypeface(regular);
            roq_iii_5.setTypeface(regular);
            roq_iiii_5.setTypeface(regular);

            String row_1__par_4  = par_values.get(4+"")+"";
            String row_2_score_4 = Score_values.get(4+"")+"";
            int row_1_result_4 = 0;
            if (row_1__par_4=="0" || row_1__par_4=="null" || row_1__par_4=="" || row_1__par_4==null){
            }else {
                if (row_2_score_4=="0" || row_2_score_4=="null" || row_2_score_4=="" || row_2_score_4==null){
                    roq_iii_5.setBackground(null);
                }else{
                    row_1_result_4 = (Integer.parseInt(row_2_score_4)) - Integer.parseInt(row_1__par_4);
                    if (row_1_result_4 == 0) {
                        roq_iii_5.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_4 == -1) {
                        roq_iii_5.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_4 < -1) {
                        roq_iii_5.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_4 == 1) {
                        roq_iii_5.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_4 >= 1) {
                        roq_iii_5.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }
            roq_ii_5.setText(par_values.get(4+"")+"");
            roq_iii_5.setText(Score_values.get(4+"")+"");
            if (game_name.equals("scramble")){
                roq_iiii_5.setText("-");
            }else {
                roq_iiii_5.setText(net_score_values.get(4+"")+"");
            }
            //5 coloum

            TextView roq_ii_6 = convertView.findViewById(R.id.roq_ii_6);
            TextView roq_iii_6 = convertView.findViewById(R.id.roq_iii_6);
            TextView roq_iiii_6 = convertView.findViewById(R.id.roq_iiii_6);

            roq_ii_6.setTypeface(regular);
            roq_iii_6.setTypeface(regular);
            roq_iiii_6.setTypeface(regular);

            String row_1__par_5  = par_values.get(5+"")+"";
            String row_2_score_5 = Score_values.get(5+"")+"";
            int row_1_result_5 = 0;
            if (row_1__par_5=="0" || row_1__par_5=="null" || row_1__par_5=="" || row_1__par_5==null){
            }else {
                if (row_2_score_5=="0" || row_2_score_5=="null" || row_2_score_5=="" || row_2_score_5==null){
                    roq_iii_6.setBackground(null);
                }else{
                    row_1_result_5 = (Integer.parseInt(row_2_score_5)) - Integer.parseInt(row_1__par_5);
                    if (row_1_result_5 == 0) {
                        roq_iii_6.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_5 == -1) {
                        roq_iii_6.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_5 < -1) {
                        roq_iii_6.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_5 == 1) {
                        roq_iii_6.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_5 >= 1) {
                        roq_iii_6.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }
            roq_ii_6.setText(par_values.get(5+"")+"");
            roq_iii_6.setText(Score_values.get(5+"")+"");
            if (game_name.equals("scramble")){
                roq_iiii_6.setText("-");
            }else {
                roq_iiii_6.setText(net_score_values.get(5+"")+"");
            }
            //6 coloum

            TextView roq_ii_7 = convertView.findViewById(R.id.roq_ii_7);
            TextView roq_iii_7 = convertView.findViewById(R.id.roq_iii_7);
            TextView roq_iiii_7 = convertView.findViewById(R.id.roq_iiii_7);

            roq_ii_7.setTypeface(regular);
            roq_iii_7.setTypeface(regular);
            roq_iiii_7.setTypeface(regular);

            String row_1__par_6  = par_values.get(6+"")+"";
            String row_2_score_6 = Score_values.get(6+"")+"";
            int row_1_result_6 = 0;
            if (row_1__par_6=="0" || row_1__par_6=="null" || row_1__par_6=="" || row_1__par_6==null){
            }else {
                if (row_2_score_6=="0" || row_2_score_6=="null" || row_2_score_6=="" || row_2_score_6==null){
                    roq_iii_7.setBackground(null);
                }else{
                    row_1_result_6 = (Integer.parseInt(row_2_score_6)) - Integer.parseInt(row_1__par_6);
                    if (row_1_result_6 == 0) {
                       roq_iii_7.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_6 == -1) {
                        roq_iii_7.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_6 < -1) {
                        roq_iii_7.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_6 == 1) {
                        roq_iii_7.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_6 >= 1) {
                        roq_iii_7.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }
            roq_ii_7.setText(par_values.get(6+"")+"");
            roq_iii_7.setText(Score_values.get(6+"")+"");
            if (game_name.equals("scramble")){
                roq_iiii_7.setText("-");
            }else {
                roq_iiii_7.setText(net_score_values.get(6+"")+"");
            }
            //7 coloum

            TextView roq_ii_8 = convertView.findViewById(R.id.roq_ii_8);
            TextView roq_iii_8 = convertView.findViewById(R.id.roq_iii_8);
            TextView roq_iiii_8 = convertView.findViewById(R.id.roq_iiii_8);

            roq_ii_8.setTypeface(regular);
            roq_iii_8.setTypeface(regular);
            roq_iiii_8.setTypeface(regular);

            String row_1__par_7  = par_values.get(7+"")+"";
            String row_2_score_7 = Score_values.get(7+"")+"";
            int row_1_result_7 = 0;
            if (row_1__par_7=="0" || row_1__par_7=="null" || row_1__par_7=="" || row_1__par_7==null){
            }else {
                if (row_2_score_7=="0" || row_2_score_7=="null" || row_2_score_7=="" || row_2_score_7==null){
                    roq_iii_8.setBackground(null);
                }else{
                    row_1_result_7 = (Integer.parseInt(row_2_score_7)) - Integer.parseInt(row_1__par_7);
                    if (row_1_result_7 == 0) {
                        roq_iii_8.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_7 == -1) {
                        roq_iii_8.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_7 < -1) {
                        roq_iii_8.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_7 == 1) {
                        roq_iii_8.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_7 >= 1) {
                        roq_iii_8.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }

            roq_ii_8.setText(par_values.get(7+"")+"");
            roq_iii_8.setText(Score_values.get(7+"")+"");
            if (game_name.equals("scramble")){
                roq_iiii_8.setText("-");
            }else {
                roq_iiii_8.setText(net_score_values.get(7+"")+"");
            }
            //8 coloum

            TextView roq_ii_9 = convertView.findViewById(R.id.roq_ii_9);
            TextView roq_iii_9 = convertView.findViewById(R.id.roq_iii_9);
            TextView roq_iiii_9 = convertView.findViewById(R.id.roq_iiii_9);

            roq_ii_9.setTypeface(regular);
            roq_iii_9.setTypeface(regular);
            roq_iiii_9.setTypeface(regular);

            String row_1__par_8  = par_values.get(8+"")+"";
            String row_2_score_8 = Score_values.get(8+"")+"";
            int row_1_result_8 = 0;
            if (row_1__par_8=="0" || row_1__par_8=="null" || row_1__par_8=="" || row_1__par_8==null){
            }else {
                if (row_2_score_8=="0" || row_2_score_8=="null" || row_2_score_8=="" || row_2_score_8==null){
                    roq_iii_9.setBackground(null);
                }else{
                    row_1_result_8 = (Integer.parseInt(row_2_score_8)) - Integer.parseInt(row_1__par_8);
                    if (row_1_result_8 == 0) {
                        roq_iii_9.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_8 == -1) {
                        roq_iii_9.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_8 < -1) {
                        roq_iii_9.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_8 == 1) {
                        roq_iii_9.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_8 >= 1) {
                        roq_iii_9.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }
            roq_ii_9.setText(par_values.get(8+"")+"");
            roq_iii_9.setText(Score_values.get(8+"")+"");
            if (game_name.equals("scramble")){
                roq_iiii_9.setText("-");
            }else {
                roq_iiii_9.setText(net_score_values.get(8+"")+"");
            }
            //9 coloum

            TextView roq_ii_10 = convertView.findViewById(R.id.roq_ii_10);
            TextView roq_iii_10 = convertView.findViewById(R.id.roq_iii_10);
            TextView roq_iiii_10 = convertView.findViewById(R.id.roq_iiii_10);

            roq_ii_10.setTypeface(regular);
            roq_iii_10.setTypeface(regular);
            roq_iiii_10.setTypeface(regular);

            roq_ii_10.setText(par_values.get(9+"")+"");
            roq_iii_10.setText(Score_values.get(9+"")+"");
            if (game_name.equals("scramble")){
                roq_iiii_10.setText("-");
            }else {
                roq_iiii_10.setText(net_score_values.get(9+"")+"");
            }

            String row_1__par_9  = par_values.get(9+"")+"";
            String row_2_score_9 = Score_values.get(9+"")+"";
            int row_1_result_9 = 0;
            if (row_1__par_9=="0" || row_1__par_9=="null" || row_1__par_9=="" || row_1__par_9==null){
            }else {
                if (row_2_score_9=="0" || row_2_score_9=="null" || row_2_score_9=="" || row_2_score_9==null){
                    roq_iii_10.setBackground(null);
                }else{
                    row_1_result_9 = (Integer.parseInt(row_2_score_9)) - Integer.parseInt(row_1__par_9);
                    if (row_1_result_9 == 0) {
                        roq_iii_10.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_9 == -1) {
                        roq_iii_10.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_9 < -1) {
                        roq_iii_10.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_9 == 1) {
                        roq_iii_10.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_9 >= 1) {
                        roq_iii_10.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }

            //9 coloum

            TextView roq_ii_11 = convertView.findViewById(R.id.roq_ii_11);
            TextView roq_iii_11 = convertView.findViewById(R.id.roq_iii_11);
            TextView roq_iiii_11 = convertView.findViewById(R.id.roq_iiii_11);

            roq_ii_11.setTypeface(regular);
            roq_iii_11.setTypeface(regular);
            roq_iiii_11.setTypeface(regular);

            roq_ii_11.setText(par_postion_1+"");
            roq_iii_11.setText(gross_score_postion_1+"");

            if (game_name.equals("scramble") || game_name.equals("strokeplay")){
                roq_iiii_11.setText("-");
            }else {
                roq_iiii_11.setText(net_score_postion_1+"");
            }


            TextView row_2_roq_ii_1 = convertView.findViewById(R.id.row_2_roq_ii_1);
            TextView row_2_roq_iii_1 = convertView.findViewById(R.id.row_2_roq_iii_1);
            TextView row_2_roq_iiii_1 = convertView.findViewById(R.id.row_2_roq_iiii_1);

            row_2_roq_ii_1.setTypeface(regular);
            row_2_roq_iii_1.setTypeface(regular);
            row_2_roq_iiii_1.setTypeface(regular);

            //10 coloum

            TextView row_2_roq_ii_2 = convertView.findViewById(R.id.row_2_roq_ii_2);
            TextView row_2_roq_iii_2 = convertView.findViewById(R.id.row_2_roq_iii_2);
            TextView row_2_roq_iiii_2 = convertView.findViewById(R.id.row_2_roq_iiii_2);


            row_2_roq_ii_2.setTypeface(regular);
            row_2_roq_iii_2.setTypeface(regular);
            row_2_roq_iiii_2.setTypeface(regular);

            String row_1__par_10  = par_values.get(10+"")+"";
            String row_2_score_10 = Score_values.get(10+"")+"";
            int row_1_result_10 = 0;
            if (row_1__par_10=="0" || row_1__par_10=="null" || row_1__par_10=="" || row_1__par_10==null){
            }else {
                if (row_2_score_10=="0" || row_2_score_10=="null" || row_2_score_10=="" || row_2_score_10==null){
                    row_2_roq_iii_2.setBackground(null);
                }else{
                    row_1_result_10 = (Integer.parseInt(row_2_score_10)) - Integer.parseInt(row_1__par_10);
                    if (row_1_result_10 == 0) {
                        row_2_roq_iii_2.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_10 == -1) {
                        row_2_roq_iii_2.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_10 < -1) {
                        row_2_roq_iii_2.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_10 == 1) {
                        row_2_roq_iii_2.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_10 >= 1) {
                        row_2_roq_iii_2.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }

            row_2_roq_ii_2.setText(par_values.get(10+"")+"");
            row_2_roq_iii_2.setText(Score_values.get(10+"")+"");
            if (game_name.equals("scramble")){
                row_2_roq_iiii_2.setText("-");
            }else {
                row_2_roq_iiii_2.setText(net_score_values.get(10+"")+"");
            }
            //11 coloum

            TextView row_2_roq_ii_3 = convertView.findViewById(R.id.row_2_roq_ii_3);
            TextView row_2_roq_iii_3 = convertView.findViewById(R.id.row_2_roq_iii_3);
            TextView row_2_roq_iiii_3 = convertView.findViewById(R.id.row_2_roq_iiii_3);

            row_2_roq_ii_3.setTypeface(regular);
            row_2_roq_iii_3.setTypeface(regular);
            row_2_roq_iiii_3.setTypeface(regular);

            String row_1__par_11  = par_values.get(11+"")+"";
            String row_2_score_11 = Score_values.get(11+"")+"";
            int row_1_result_11 = 0;
            if (row_1__par_11=="0" || row_1__par_11=="null" || row_1__par_11=="" || row_1__par_11==null){
            }else {
                if (row_2_score_11=="0" || row_2_score_11=="null" || row_2_score_11=="" || row_2_score_11==null){
                    row_2_roq_iii_3.setBackground(null);
                }else{
                    row_1_result_11 = (Integer.parseInt(row_2_score_11)) - Integer.parseInt(row_1__par_11);
                    if (row_1_result_11 == 0) {
                        row_2_roq_iii_3.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_11 == -1) {
                        row_2_roq_iii_3.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_11 < -1) {
                        row_2_roq_iii_3.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_11 == 1) {
                        row_2_roq_iii_3.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_11 >= 1) {
                        row_2_roq_iii_3.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }
            row_2_roq_ii_3.setText(par_values.get(11+"")+"");
            row_2_roq_iii_3.setText(Score_values.get(11+"")+"");
            if (game_name.equals("scramble")){
                row_2_roq_iiii_3.setText("-");
            }else {
                row_2_roq_iiii_3.setText(net_score_values.get(11+"")+"");
            }
            //12 coloum

            TextView row_2_roq_ii_4 = convertView.findViewById(R.id.row_2_roq_ii_4);
            TextView row_2_roq_iii_4 = convertView.findViewById(R.id.row_2_roq_iii_4);
            TextView row_2_roq_iiii_4 = convertView.findViewById(R.id.row_2_roq_iiii_4);

            row_2_roq_ii_4.setTypeface(regular);
            row_2_roq_iii_4.setTypeface(regular);
            row_2_roq_iiii_4.setTypeface(regular);

            String row_1__par_12  = par_values.get(12+"")+"";
            String row_2_score_12 = Score_values.get(12+"")+"";
            int row_1_result_12 = 0;
            if (row_1__par_12=="0" || row_1__par_12=="null" || row_1__par_12=="" || row_1__par_12==null){
            }else {
                if (row_2_score_12=="0" || row_2_score_12=="null" || row_2_score_12=="" || row_2_score_12==null){
                    row_2_roq_iii_4.setBackground(null);
                }else{
                    row_1_result_12 = (Integer.parseInt(row_2_score_12)) - Integer.parseInt(row_1__par_12);
                    if (row_1_result_12 == 0) {
                        row_2_roq_iii_4.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_12 == -1) {
                        row_2_roq_iii_4.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_12 < -1) {
                        row_2_roq_iii_4.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_12 == 1) {
                        row_2_roq_iii_4.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_12 >= 1) {
                        row_2_roq_iii_4.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }

            row_2_roq_ii_4.setText(par_values.get(12+"")+"");
            row_2_roq_iii_4.setText(Score_values.get(12+"")+"");
            if (game_name.equals("scramble")){
                row_2_roq_iiii_4.setText("-");
            }else {
                row_2_roq_iiii_4.setText(net_score_values.get(12+"")+"");
            }
            //13 coloum

            TextView row_2_roq_ii_5 = convertView.findViewById(R.id.row_2_roq_ii_5);
            TextView row_2_roq_iii_5 = convertView.findViewById(R.id.row_2_roq_iii_5);
            TextView row_2_roq_iiii_5 = convertView.findViewById(R.id.row_2_roq_iiii_5);

            row_2_roq_ii_5.setTypeface(regular);
            row_2_roq_iii_5.setTypeface(regular);
            row_2_roq_iiii_5.setTypeface(regular);

            String row_1__par_13  = par_values.get(13+"")+"";
            String row_2_score_13 = Score_values.get(13+"")+"";
            int row_1_result_13 = 0;
            if (row_1__par_13=="0" || row_1__par_13=="null" || row_1__par_13=="" || row_1__par_13==null){
            }else {
                if (row_2_score_13=="0" || row_2_score_13=="null" || row_2_score_13=="" || row_2_score_13==null){
                    row_2_roq_iii_5.setBackground(null);
                }else{
                    row_1_result_13 = (Integer.parseInt(row_2_score_13)) - Integer.parseInt(row_1__par_13);
                    if (row_1_result_13 == 0) {
                        row_2_roq_iii_5.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_13 == -1) {
                        row_2_roq_iii_5.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_13 < -1) {
                        row_2_roq_iii_5.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_13 == 1) {
                        row_2_roq_iii_5.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_13 >= 1) {
                        row_2_roq_iii_5.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }
            row_2_roq_ii_5.setText(par_values.get(13+"")+"");
            row_2_roq_iii_5.setText(Score_values.get(13+"")+"");
            if (game_name.equals("scramble")){
                row_2_roq_iiii_5.setText("-");
            }else {
                row_2_roq_iiii_5.setText(net_score_values.get(13+"")+"");
            }
            //14 coloum

            TextView row_2_roq_ii_6 = convertView.findViewById(R.id.row_2_roq_ii_6);
            TextView row_2_roq_iii_6 = convertView.findViewById(R.id.row_2_roq_iii_6);
            TextView row_2_roq_iiii_6 = convertView.findViewById(R.id.row_2_roq_iiii_6);

            row_2_roq_ii_6.setTypeface(regular);
            row_2_roq_iii_6.setTypeface(regular);
            row_2_roq_iiii_6.setTypeface(regular);


            String row_1__par_14  = par_values.get(14+"")+"";
            String row_2_score_14 = Score_values.get(14+"")+"";
            int row_1_result_14 = 0;
            if (row_1__par_14=="0" || row_1__par_14=="null" || row_1__par_14=="" || row_1__par_14==null){
            }else {
                if (row_2_score_14=="0" || row_2_score_14=="null" || row_2_score_14=="" || row_2_score_14==null){
                    row_2_roq_iii_6.setBackground(null);
                }else{
                    row_1_result_14 = (Integer.parseInt(row_2_score_14)) - Integer.parseInt(row_1__par_14);

                    if (row_1_result_14 == 0) {
                        row_2_roq_iii_6.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_14 == -1) {
                        row_2_roq_iii_6.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_14 < -1) {
                        row_2_roq_iii_6.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_14 == 1) {
                        row_2_roq_iii_6.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_14 >= 1) {
                        row_2_roq_iii_6.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }
            row_2_roq_ii_6.setText(par_values.get(14+"")+"");
            row_2_roq_iii_6.setText(Score_values.get(14+"")+"");
            if (game_name.equals("scramble")){
                row_2_roq_iiii_6.setText("-");
            }else {
                row_2_roq_iiii_6.setText(net_score_values.get(14+"")+"");
            }
            //15 coloum

            TextView row_2_roq_ii_7 = convertView.findViewById(R.id.row_2_roq_ii_7);
            TextView row_2_roq_iii_7 = convertView.findViewById(R.id.row_2_roq_iii_7);
            TextView row_2_roq_iiii_7 = convertView.findViewById(R.id.row_2_roq_iiii_7);

            row_2_roq_ii_7.setTypeface(regular);
            row_2_roq_iii_7.setTypeface(regular);
            row_2_roq_iiii_7.setTypeface(regular);

            String row_1__par_15  = par_values.get(15+"")+"";
            String row_2_score_15 = Score_values.get(15+"")+"";
            int row_1_result_15 = 0;
            if (row_1__par_15=="0" || row_1__par_15=="null" || row_1__par_15=="" || row_1__par_15==null){
            }else {
                if (row_2_score_15=="0" || row_2_score_15=="null" || row_2_score_15=="" || row_2_score_15==null){
                    row_2_roq_iii_7.setBackground(null);
                }else{
                    row_1_result_15 = (Integer.parseInt(row_2_score_15)) - Integer.parseInt(row_1__par_15);

                    if (row_1_result_15 == 0) {
                        row_2_roq_iii_7.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_15 == -1) {
                        row_2_roq_iii_7.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_15 < -1) {
                        row_2_roq_iii_7.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_15 == 1) {
                        row_2_roq_iii_7.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_15 >= 1) {
                        row_2_roq_iii_7.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }
            row_2_roq_ii_7.setText(par_values.get(15+"")+"");
            row_2_roq_iii_7.setText(Score_values.get(15+"")+"");
            if (game_name.equals("scramble")){
                row_2_roq_iiii_7.setText("-");
            }else {
                row_2_roq_iiii_7.setText(net_score_values.get(15 + "") + "");
            }
            //16 coloum

            TextView row_2_roq_ii_8 = convertView.findViewById(R.id.row_2_roq_ii_8);
            TextView row_2_roq_iii_8 = convertView.findViewById(R.id.row_2_roq_iii_8);
            TextView row_2_roq_iiii_8 = convertView.findViewById(R.id.row_2_roq_iiii_8);

            row_2_roq_ii_8.setTypeface(regular);
            row_2_roq_iii_8.setTypeface(regular);
            row_2_roq_iiii_8.setTypeface(regular);

            String row_1__par_16  = par_values.get(16+"")+"";
            String row_2_score_16 = Score_values.get(16+"")+"";
            int row_1_result_16 = 0;
            if (row_1__par_16=="0" || row_1__par_16=="null" || row_1__par_16=="" || row_1__par_16==null){
            }else {
                if (row_2_score_16=="0" || row_2_score_16=="null" || row_2_score_16=="" || row_2_score_16==null){
                    row_2_roq_iii_8.setBackground(null);
                }else{
                    row_1_result_16 = (Integer.parseInt(row_2_score_16)) - Integer.parseInt(row_1__par_16);

                    if (row_1_result_16 == 0) {
                        row_2_roq_iii_8.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_16 == -1) {
                        row_2_roq_iii_8.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_16 < -1) {
                        row_2_roq_iii_8.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_16 == 1) {
                        row_2_roq_iii_8.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_16 >= 1) {
                        row_2_roq_iii_8.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }
            row_2_roq_ii_8.setText(par_values.get(16+"")+"");
            row_2_roq_iii_8.setText(Score_values.get(16+"")+"");
            if (game_name.equals("scramble")){
                row_2_roq_iiii_8.setText("-");
            }else {
                row_2_roq_iiii_8.setText(net_score_values.get(16+"")+"");
            }


            //17 coloum

            TextView row_2_roq_ii_9 = convertView.findViewById(R.id.row_2_roq_ii_9);
            TextView row_2_roq_iii_9 = convertView.findViewById(R.id.row_2_roq_iii_9);
            TextView row_2_roq_iiii_9 = convertView.findViewById(R.id.row_2_roq_iiii_9);

            row_2_roq_ii_9.setTypeface(regular);
            row_2_roq_iii_9.setTypeface(regular);
            row_2_roq_iiii_9.setTypeface(regular);

            String row_1__par_17  = par_values.get(17+"")+"";
            String row_2_score_17 = Score_values.get(17+"")+"";
            int row_1_result_17 = 0;
            if (row_1__par_17=="0" || row_1__par_17=="null" || row_1__par_17=="" || row_1__par_17==null){
            }else {
                if (row_2_score_17=="0" || row_2_score_17=="null" || row_2_score_17=="" || row_2_score_17==null){
                    row_2_roq_iii_9.setBackground(null);
                }else{
                    row_1_result_17 = (Integer.parseInt(row_2_score_17)) - Integer.parseInt(row_1__par_17);

                    if (row_1_result_17 == 0) {
                        row_2_roq_iii_9.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_17 == -1) {
                        row_2_roq_iii_9.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_17 < -1) {
                        row_2_roq_iii_9.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_17 == 1) {
                        row_2_roq_iii_9.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_17 >= 1) {
                        row_2_roq_iii_9.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }
            row_2_roq_ii_9.setText(par_values.get(17+"")+"");
            row_2_roq_iii_9.setText(Score_values.get(17+"")+"");
            if (game_name.equals("scramble")){
                row_2_roq_iiii_9.setText("-");
            }else {
                row_2_roq_iiii_9.setText(net_score_values.get(17+"")+"");
            }

            //18 coloum

            TextView row_2_roq_ii_10 = convertView.findViewById(R.id.row_2_roq_ii_10);
            TextView row_2_roq_iii_10 = convertView.findViewById(R.id.row_2_roq_iii_10);
            TextView row_2_roq_iiii_10 = convertView.findViewById(R.id.row_2_roq_iiii_10);

            row_2_roq_ii_10.setTypeface(regular);
            row_2_roq_iii_10.setTypeface(regular);
            row_2_roq_iiii_10.setTypeface(regular);

            String row_1__par_18  = par_values.get(18+"")+"";
            String row_2_score_18 = Score_values.get(18+"")+"";
            int row_1_result_18 = 0;
            if (row_1__par_18=="0" || row_1__par_18=="null" || row_1__par_18=="" || row_1__par_18==null){
            }else {
                if (row_2_score_18=="0" || row_2_score_18=="null" || row_2_score_18=="" || row_2_score_18==null){
                    row_2_roq_iii_10.setBackground(null);
                }else{
                    row_1_result_18 = (Integer.parseInt(row_2_score_18)) - Integer.parseInt(row_1__par_18);
                    if (row_1_result_18 == 0) {
                        row_2_roq_iii_10.setBackgroundResource(R.drawable.par_color);
                    } else if (row_1_result_18 == -1) {
                        row_2_roq_iii_10.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_18 < -1) {
                        row_2_roq_iii_10.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_18 == 1) {
                        row_2_roq_iii_10.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_18 >= 1) {
                        row_2_roq_iii_10.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }
            row_2_roq_ii_10.setText(par_values.get(18+"")+"");
            row_2_roq_iii_10.setText(Score_values.get(18+"")+"");
            if (game_name.equals("scramble")){
                row_2_roq_iiii_10.setText("-");
            }else {
                row_2_roq_iiii_10.setText(net_score_values.get(18+"")+"");
            }


            //19 coloum

            TextView row_2_roq_ii_11 = convertView.findViewById(R.id.row_2_roq_ii_11);
            TextView row_2_roq_iii_11 = convertView.findViewById(R.id.row_2_roq_iii_11);
            TextView row_2_roq_iiii_11 = convertView.findViewById(R.id.row_2_roq_iiii_11);

            row_2_roq_ii_11.setTypeface(regular);
            row_2_roq_iii_11.setTypeface(regular);
            row_2_roq_iiii_11.setTypeface(regular);

            row_2_roq_ii_11.setText(par_postion_2+"");
            row_2_roq_iii_11.setText(gross_score_postion_2+"");

            if (game_name.equals("scramble") || game_name.equals("strokeplay")){
                row_2_roq_iiii_11.setText("-");
            }else {
                row_2_roq_iiii_11.setText(net_score_postion_2+"");
            }

            return convertView;
        }

        // return number of headers in list
        @Override
        public int getChildrenCount(int groupPosition) {
            return this.childArray.get(this.headerArray.get(groupPosition).getId()).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.headerArray.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this.headerArray.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        // inflate header view

        @Override
        public void notifyDataSetChanged()
        {
            // Refresh List rows
            super.notifyDataSetChanged();
        }
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


            parent_getter_setter parent_postion = headerArray.get(groupPosition);

            if (convertView == null) {
                convertView = infalInflater.inflate(R.layout.grouprow, null);
            }

            TextView id_champion_ship = convertView.findViewById(R.id.id_champion_ship);
            id_champion_ship.setTypeface(regular);

            TextView text_1s = convertView.findViewById(R.id.text_1);
            TextView tvplayer_name = convertView.findViewById(R.id.tvplayer_name);
            TextView tv_score = convertView.findViewById(R.id.tv_score);
            TextView tv_par_score = convertView.findViewById(R.id.tv_par_score);
            TextView tvtrophy = convertView.findViewById(R.id.tvtrophy);

            text_1s.setTypeface(regular);
            tvplayer_name.setTypeface(regular);
            tv_score.setTypeface(regular);
            tv_par_score.setTypeface(medium);


            if(parent_postion.getUser_postion().equals("1") || parent_postion.getUser_postion().equals("T1")) {
                tvtrophy.setVisibility(View.VISIBLE);
                tvtrophy.setText("\uf091");
                tvtrophy.setTypeface(webfont);
            }else{
                tvtrophy.setVisibility(View.GONE);
            }

            TextView tv_thru = convertView.findViewById(R.id.tv_thru);
            tv_thru.setTypeface(regular);
            tv_thru.setText(parent_postion.THRU);

            if(groupPosition %2 == 1)
            {
                convertView.setBackgroundResource(R.color.colorwhite);
            }
            else
            {
                convertView.setBackgroundResource(R.color.leader_board_row_color);
            }

            if (expand_colapse==0){
                if (!parent_postion.getEnebled().equals("0")){
                    id_champion_ship.setVisibility(View.VISIBLE);
                    id_champion_ship.setText(parent_postion.getEnebled());
                    group++;
                }else {
                    id_champion_ship.setVisibility(View.GONE);
                }
            }

            text_1s.setText(parent_postion.getUser_postion());

            tvplayer_name.setText(parent_postion.getName());
            tv_score.setText(parent_postion.getTotal_score());

            int total_scoress=0;
            if (parent_postion.getPar_score().equals("0")){
                tv_par_score.setText("E");
            }else {
                if (parent_postion.getPar_score().equals(null) || parent_postion.getPar_score().equals("null") || parent_postion.getPar_score().equals("")){
                    total_scoress=0;
                }else {
                    total_scoress = Integer.parseInt(parent_postion.getPar_score());
                }

                if (total_scoress>0){
                    tv_par_score.setText("+"+parent_postion.getPar_score());
                }else {
                    tv_par_score.setText(parent_postion.getPar_score());
                }
            }


            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

}
