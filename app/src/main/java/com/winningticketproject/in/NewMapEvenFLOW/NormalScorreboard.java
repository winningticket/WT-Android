package com.winningticketproject.in.NewMapEvenFLOW;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ImageView;
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
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.AuctionModel.NewLive_Auction;
import com.winningticketproject.in.ChatWithOther.User_List;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.IndividulaGameType.AddScoresToScorecard;
import com.winningticketproject.in.IndividulaGameType.Location_Services;
import com.winningticketproject.in.Live_Score_New_Flow.View_course_map;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import cz.msebera.android.httpclient.HttpStatus;
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

public class NormalScorreboard extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recycler_view;
    public Map<String, String> data1 = new HashMap<String, String>();
    public Map<String, String> data2 = new HashMap<String, String>();
    public Map<String, String> data3 = new HashMap<String, String>();
    public Map<String, String> data4 = new HashMap<String, String>();
    public Map<String, String> data5 = new HashMap<String, String>();
    ArrayList<String> hole_postionss = new ArrayList<>();

    Livescorecard livescorecard;
    int hole_selected_position = 0;
    LinearLayout ll_non_course;

    String home;

    String selected_hole_postion = "";

    String hand_hadicap_selected, player_name = "";

    String hand_hadicap_selecteds = "0";

    int total_holes = 0;

    String response_postions = "";

    String row_three_postion;

    String selected_gross_score = "0";

    JSONObject netscorecalculation;
    SwipeRefreshLayout swipeRefreshLayout;

    public String event_id;

    Button btn_gps;
    Button btn_leader_board;
    TextView tv_course_name;

    TextView tv_player_name;
    TextView tv_to_par_values;
    boolean isnotcourse;

    TextView tvpar, tvholes, tvdistance, tvgrassscore, tvnetscore, tvhandicap;


    TextView tv_scoring_legend,tv_eadge,tv_bride,tv_par,tv_bogey,tv_double_bogey;

    String to_par="gross";

    boolean cross_score_enabled;
    String Other_Team_name="";

    String cross_score_player_id="";

    ImageView image_view_leaderboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_scorreboard);

        tvpar = findViewById(R.id.tvpar);
        tvholes = findViewById(R.id.tvholes);
        tvdistance = findViewById(R.id.tvdistance);
        tvgrassscore = findViewById(R.id.tvgrassscore);
        tvnetscore = findViewById(R.id.tvnetscore);
        tvhandicap = findViewById(R.id.tvhandicap);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            home = extras.getString("home");
        }

        // and get whatever type user account id is

        tv_scoring_legend = findViewById(R.id.tv_scoring_legend);
        tv_eadge = findViewById(R.id.tv_eadge);
        tv_bride = findViewById(R.id.tv_bride);
        tv_par = findViewById(R.id.tv_par);
        tv_bogey = findViewById(R.id.tv_bogey);
        tv_double_bogey = findViewById(R.id.tv_double_bogey);

        tv_scoring_legend.setTypeface(medium);
        tv_eadge.setTypeface(regular);
        tv_bride.setTypeface(regular);
        tv_par.setTypeface(regular);
        tv_bogey.setTypeface(regular);
        tv_double_bogey.setTypeface(regular);

        tvpar.setTypeface(medium);
        tvholes.setTypeface(medium);
        tvdistance.setTypeface(medium);
        tvgrassscore.setTypeface(medium);
        tvnetscore.setTypeface(medium);
        tvhandicap.setTypeface(medium);

        event_id = get_auth_token("event_id");

        method_to_load_top_section();

        image_view_leaderboard  = findViewById(R.id.image_view_leaderboard);
        image_view_leaderboard.setOnClickListener(this);

        selected_hole_postion = get_auth_token("selected_hole_postion");
        Share_it.savestring("un_select", "-2");

        recycler_view = findViewById(R.id.recycler_view);

        ll_non_course = findViewById(R.id.ll_non_course);

        if (!Location_Services.game_name.equals("Course")){
            isnotcourse = false;
            ll_non_course.setVisibility(View.VISIBLE);
        }else {
            ll_non_course.setVisibility(View.GONE);
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No network", Toast.LENGTH_LONG).show();
        } else {
            methodforscorecard();
        }

        if (get_auth_token("CGB_event_code").equals(get_auth_token("current_event_code"))){
            image_view_leaderboard.setVisibility(View.VISIBLE);
            ll_non_course.setVisibility(View.GONE);
        }

    }

    private void method_to_load_top_section() {

        swipeRefreshLayout = findViewById(R.id.swipe_view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Define your work here .
                swipeRefreshLayout.setRefreshing(true);
                methodforscorecard();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
                ProgressDialog.getInstance().hideProgress();
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

        tv_player_name = findViewById(R.id.tv_player_name);
        tv_player_name.setTypeface(regular);

        tv_to_par_values = findViewById(R.id.tv_to_par_values);
        tv_to_par_values.setTypeface(regular);
        tv_to_par_values.setOnClickListener(this);

        if (!Location_Services.game_name.equals("Course")){
            btn_leader_board.setText("Event");
            btn_leader_board.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
        }

        ImageView image_auction = findViewById(R.id.iv_auction);
        ImageView image_chat = findViewById(R.id.iv_chat);
        ImageView image_super_ticket = findViewById(R.id.iv_super_ticket);
        ImageView image_leader_board = findViewById(R.id.iv_leader_board);

        image_auction.setOnClickListener(this);
        image_chat.setOnClickListener(this);
        image_super_ticket.setOnClickListener(this);
        image_leader_board.setOnClickListener(this);


        btn_leader_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Location_Services.game_name.equals("Course")) {
                    if (isnotcourse) {
                        isnotcourse = false;

                        if (get_auth_token("CGB_event_code").equals(get_auth_token("current_event_code"))){
                            image_view_leaderboard.setVisibility(View.VISIBLE);
                            ll_non_course.setVisibility(View.GONE);
                        }else {
                            ll_non_course.setVisibility(View.VISIBLE);
                            image_view_leaderboard.setVisibility(View.GONE);
                        }
                        btn_leader_board.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));

                    } else {
                        isnotcourse = true;
                            image_view_leaderboard.setVisibility(View.GONE);
                            ll_non_course.setVisibility(View.GONE);
                        btn_leader_board.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                    }
                } else {
                    Intent in = new Intent(NormalScorreboard.this, NormalLeaderoard.class);
                    in.putExtra("home",home);
                    startActivity(in);
                    finish();

                }
            }
        });

    }

    // method to call api to get records
    public void methodforscorecard() {
        JSONObject object = new JSONObject();

        ProgressDialog.getInstance().showProgress(this);
        String update_API = "";
        try {
            try {
                object.put("handicap", get_auth_token("handicap"));
                object.put("selected_tee", get_auth_token("selected_tee_postion"));
                object.put("selected_hole", get_auth_token("selected_hole_postion"));
            } catch (Exception e) {
            }

            if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")) {
                update_API = "api/v2/hole_info/player/select-course-info/" + get_auth_token("event_id");
            } else if (get_auth_token("play_type").equals("event")) {
                update_API = "api/v2/hole_info/select-course-info/" + get_auth_token("event_id");
            } else if (get_auth_token("play_type").equals("individual")) {
                update_API = "api/v2/hole_info/player/select-course-info/" + get_auth_token("event_id");
            }

            data1 = new HashMap<String, String>();
            data2 = new HashMap<String, String>();
            data3 = new HashMap<String, String>();
            data4 = new HashMap<String, String>();
            data5 = new HashMap<String, String>();
            hole_selected_position = 0;

            JsonObjectRequest myRequest_handicap = new JsonObjectRequest(
                    Request.Method.POST, Splash_screen.imageurl + update_API, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            ProgressDialog.getInstance().hideProgress();

                            try {
                                savestring("game_completed",response.getString("completed"));
                                System.out.println("----res--------" + response);
                                if (response.getString("status").equals("Success")) {
                                    try {

                                        try {
                                            cross_score_enabled = response.getBoolean("cross_scoring");
                                            Other_Team_name = response.getString("cross_score_team_name");
                                            cross_score_player_id = response.getString("cross_score_player_id");

                                        }catch (Exception e){ }

                                        player_name = response.getString("name");

                                        if (get_auth_token("handicap_score").equals("diseble")) {
                                            hand_hadicap_selected = "0";
                                            tv_player_name.setText(response.getString("team_name"));
                                        } else {
                                            if (game_name.equals("scramble")) {
                                                if (cross_score_enabled){
                                                    tv_player_name.setText(Html.fromHtml("<b>"+Other_Team_name+"</b>"));
                                                }else {
                                                    tv_player_name.setText(Html.fromHtml("<b>"+player_name+"</b>"));
                                                }
                                            }else {
                                                tv_player_name.setText(Html.fromHtml("<b>"+player_name+"</b>"+"<br>"+"Handicap <b>"+get_auth_token("handicap")+"<b>"));
                                            }
                                        }

                                        String net_score = response.getString("total_score");
                                        if (net_score == null || net_score.equals("") || net_score.equals("null") || net_score.equals(null)) {
                                            net_score = "0";
                                        }

                                        int to_par = Integer.parseInt(net_score);
                                        tv_to_par_values.setText(Html.fromHtml("<b><big>"+to_par+"</big></b>"+"<br> TO PAR"));

                                        String tot_par_valr = response.getString("total_net_score");
                                        if (tot_par_valr.equals("null") || tot_par_valr.equals(null)){
                                            tot_par_valr="0";
                                        }

                                        String tot_gross_score = response.getString("total_gross_score");
                                        if (tot_gross_score.equals("null") || tot_gross_score.equals(null)){
                                            tot_gross_score="0";
                                        }

                                        String game_type = "";
                                        if (game_name.equals("scramble")) {
                                            game_type = "Gross " + tot_gross_score + "/" + response.getString("total_par_value")+" - Net " + "</b>" + tot_par_valr + "/" + response.getString("total_par_value");
                                        } else {
                                            game_type = "Gross " + tot_gross_score + "/" + response.getString("total_par_value")+" - Net " + "</b>" + tot_par_valr + "/" + response.getString("total_par_value");
                                        }

                                        String couurse_data = "";
                                        if (game_name.equals("strokeplay")){
                                            couurse_data ="<b>"+get_auth_token("Event_name")+"</b>"+"<br>Starting "+ "Hole "+get_auth_token("selected_hole_postion")+"<b>" + " - "+ "</b>"+ get_auth_token("selected_tee_postion")+" "+get_auth_token("yards_value")+"<br>"+game_type;
                                        }else if (game_name.equals("Course")){
                                            couurse_data = "<b>"+get_auth_token("Event_name")+"</b>"+"<br>Starting "+ "Hole "+get_auth_token("starting_hole")+"<b>" + " - "+ "</b>"+ get_auth_token("selected_tee_postion")+" "+get_auth_token("yards_value")+"<br>"+game_type;
                                        }else {
                                            couurse_data = "<b>"+get_auth_token("Event_name")+"</b>"+"<br>Starting "+ "Hole "+get_auth_token("starting_hole")+"<b>" + " - "+ "</b>"+ get_auth_token("selected_tee_postion")+" "+get_auth_token("yards_value")+"<br>"+game_type;
                                        }

                                        savestring("leader_board_map_course",couurse_data);
                                        tv_course_name.setText(Html.fromHtml(couurse_data));

                                    } catch (Exception e) {
                                        //nothing
                                    }

                                    JSONArray hole_info = response.getJSONArray("hole_info");
                                    View_course_map.count_list = hole_info.length();
                                    total_holes = hole_info.length();
                                    try {
                                        String stating_hole = response.getString("starting_hole");
                                        String com_hole = response.getString("completed_hole");

                                        if (com_hole.equals(null) || com_hole.equals("null") || com_hole == null) {
                                            if (stating_hole.equals(null) || stating_hole.equals("null") || stating_hole == null) {
                                                stating_hole="0";
                                            }
                                            com_hole="0";
                                            hole_selected_position = Integer.parseInt(stating_hole);
                                            View_course_map.hole_number_from_all_corses = hole_selected_position ;

                                        } else {
                                            hole_selected_position = Integer.parseInt(com_hole);

                                            if (total_holes == 9) {
                                                if (hole_selected_position == 9) {
                                                    hole_selected_position = 1;
                                                    View_course_map.hole_number_from_all_corses = 1;
                                                } else {
                                                    View_course_map.hole_number_from_all_corses = hole_selected_position;
                                                }
                                            }

                                            if (total_holes == 18) {
                                                if (hole_selected_position > 8 && hole_selected_position < 18) {
                                                    hole_selected_position = hole_selected_position + 2;
                                                    View_course_map.hole_number_from_all_corses = hole_selected_position - 1 ;
                                                } else if (hole_selected_position == 18) {
                                                    hole_selected_position = 1;
                                                    View_course_map.hole_number_from_all_corses = 1;
                                                } else {
                                                    View_course_map.hole_number_from_all_corses = hole_selected_position ;
                                                }
                                            }

                                        }
                                    } catch (Exception e) {

                                        hole_selected_position = Integer.parseInt(selected_hole_postion) - 1;

                                        ProgressDialog.getInstance().hideProgress();
                                    }

                                    for (int i = 0; i < hole_info.length(); i++) {
                                        JSONObject hole_object = hole_info.getJSONObject(i);
                                        String hole_postion = hole_object.getString("position");
                                        String par = hole_object.getString("par");
                                        String yards = hole_object.getString("yards");
                                        String handicap = hole_object.getString("handicap");
                                        String gross_score = hole_object.getString("gross_score");
                                        String net_score = hole_object.getString("net_score");
                                        hole_postionss.add(hole_postion);
                                        if (gross_score.equals("null") || gross_score.equals("") || gross_score.equals(null)) {
                                            data3.put(hole_postion, "0");
                                            data4.put(hole_postion, "0");
                                        } else {
                                            data3.put(hole_postion, gross_score);
                                            data4.put(hole_postion, net_score);
                                        }
                                        if (handicap.equals("null") || handicap.equals("") || handicap.equals(null)) {
                                            data5.put(hole_postion, "0");
                                        } else {
                                            data5.put(hole_postion, handicap);
                                        }
                                        data1.put(hole_postion, par);
                                        if (yards.equals("null") || yards.equals("") || yards.equals(null)) {
                                            data2.put(hole_postion, "0");
                                        } else {
                                            data2.put(hole_postion, yards);
                                        }

                                        if (par.equals("null") || par.equals("") || par.equals(null)) {
                                            data1.put(hole_postion, "0");
                                        } else {
                                            data1.put(hole_postion, par);
                                        }

                                        if (yards.equals("null") || yards.equals("") || yards.equals(null)) {
                                            data2.put(hole_postion, "0");
                                        } else {
                                            data2.put(hole_postion, yards);
                                        }

                                    }



                                    if (data1.size()>10){
                                        hole_postionss.add("0");
                                        data1.put(hole_info.length() + 1 + "", "0");
                                        data2.put(hole_info.length() + 1 + "", "0");
                                        data3.put(hole_info.length() + 1 + "", "0");
                                        data4.put(hole_info.length() + 1 + "", "0");
                                        data5.put(hole_info.length() + 1 + "", "0");

                                        hole_postionss.add("0");
                                        data1.put(hole_info.length() + 2 + "", "0");
                                        data2.put(hole_info.length() + 2 + "", "0");
                                        data3.put(hole_info.length() + 2 + "", "0");
                                        data4.put(hole_info.length() + 2 + "", "0");
                                        data5.put(hole_info.length() + 2 + "", "0");

                                        hole_postionss.add("0");
                                        data1.put(hole_info.length() + 3 + "", "0");
                                        data2.put(hole_info.length() + 3 + "", "0");
                                        data3.put(hole_info.length() + 3 + "", "0");
                                        data4.put(hole_info.length() + 3 + "", "0");
                                        data5.put(hole_info.length() + 3 + "", "0");
                                    }else {
                                        hole_postionss.add(hole_info.length() + 1+"");
                                        data1.put(hole_info.length() + 1 + "", "0");
                                        data2.put(hole_info.length() + 1 + "", "0");
                                        data3.put(hole_info.length() + 1 + "", "0");
                                        data4.put(hole_info.length() + 1 + "", "0");
                                        data5.put(hole_info.length() + 1 + "", "0");
                                    }


                                    System.out.println("--------------"+data3);

                                    livescorecard = new Livescorecard(data1, data2, data3, data4, data5, hole_postionss, NormalScorreboard.this);
                                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(NormalScorreboard.this, LinearLayoutManager.VERTICAL, false);
                                    recycler_view.setLayoutManager(horizontalLayoutManager);
                                    recycler_view.setAdapter(livescorecard);
                                    recycler_view.smoothScrollToPosition(hole_selected_position);
                                    recycler_view.setNestedScrollingEnabled(false);

                                    ProgressDialog.getInstance().hideProgress();
                                } else {
                                    ProgressDialog.getInstance().hideProgress();

                                    Toast.makeText(NormalScorreboard.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                                ProgressDialog.getInstance().hideProgress();
                            } catch (Exception e) {
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
                                if (error.networkResponse.statusCode == 401) {
                                    Alert_Dailog.showNetworkAlert(NormalScorreboard.this);
                                } else {
                                    Toast.makeText(NormalScorreboard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(NormalScorreboard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
            myRequest_handicap.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(myRequest_handicap, "tag");

        } catch (Exception e) {
            //nothing
        }
    }

    @Override
    public void onClick(View view) {
        Intent in;
        switch (view.getId()){

//            case R.id.btn_leader_board:
//                if (!Location_Services.game_name.equals("Course")) {
//                    ll_non_course.setVisibility(View.VISIBLE);
//                }else {
//                    finish();
//                }
//                break;

            case R.id.btn_gps:

                if(home.equals("home")){
                    in = new Intent(NormalScorreboard.this , View_course_map.class);
                    savestring("id",event_id);
                    in.putExtra("home",home);
                    startActivity(in);
                    finish();
                }else {
                    finish();
                }
                break;

            case R.id.iv_super_ticket:
                Super_ticket_poup();
                break;

            case R.id.iv_auction:
                in=new Intent(NormalScorreboard.this, NewLive_Auction.class);
                savestring("id",event_id);
                startActivity(in);
                break;

            case R.id.iv_leader_board:
                in = new Intent(NormalScorreboard.this, NormalLeaderoard.class);
                in.putExtra("home",home);
                startActivity(in);
                finish();
                break;

            case R.id.iv_chat:
                in=new Intent(this, User_List.class);
                startActivity(in);
                break;

            case R.id.tv_to_par_values:

                if (to_par.equals("gross")){
                    to_par="net";
                    tvnetscore.setText("Net");
                }else {
                    to_par="gross";
                    tvnetscore.setText("Gross");
                }
                livescorecard.notifyDataSetChanged();
                break;

            case R.id.image_view_leaderboard:
                in = new Intent(NormalScorreboard.this, NormalLeaderoard.class);
                in.putExtra("home",home);
                startActivity(in);
                finish();
                break;
        }
    }


    public void Super_ticket_poup() {
        android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NormalScorreboard.this, R.style.CustomDialogTheme);
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

    public class Livescorecard extends RecyclerView.Adapter<Livescorecard.MyViewHolder> {
        Map<String, String> data2 = new HashMap<String, String>();
        Map<String, String> data3 = new HashMap<String, String>();
        Map<String, String> data4 = new HashMap<String, String>();
        Map<String, String> data5 = new HashMap<String, String>();
        Map<String, String> data6 = new HashMap<String, String>();
        ArrayList<String> postion_list = new ArrayList<>();
        View itemView;

        public Livescorecard(Map<String, String> data2, Map<String, String> data3, Map<String, String> data4, Map<String, String> data5, Map<String, String> data6, ArrayList<String> postion_list, Context context) {
            this.data2 = data2;
            this.data3 = data3;
            this.data4 = data4;
            this.data5 = data5;
            this.data6 = data6;
            this.postion_list = postion_list;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView serial_number, hole_number, distance, distance1, distance2, distance3;
            TextView edit_icon;
            String postiondata = "";

            public MyViewHolder(View view) {
                super(view);
                serial_number = view.findViewById(R.id.serial_number);
                hole_number = view.findViewById(R.id.hole_number);
                distance = view.findViewById(R.id.distance);
                distance1 = view.findViewById(R.id.distance1);
                distance2 = view.findViewById(R.id.distance2);
                distance3 = view.findViewById(R.id.distance3);
                edit_icon = view.findViewById(R.id.edit_icon);


                edit_icon.setText("\uf040");
                edit_icon.setTypeface(webfont);

                serial_number.setTypeface(medium);
                hole_number.setTypeface(medium);
                distance.setTypeface(medium);
                distance1.setTypeface(medium);
                distance2.setTypeface(medium);
                distance3.setTypeface(medium);
            }
        }

        @Override
        public Livescorecard.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_scorecard, parent, false);
            return new Livescorecard.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final Livescorecard.MyViewHolder holder, final int position) {

            final int half_data = (data1.size() - 3) / 2;
            holder.postiondata = postion_list.get(position);

            if (to_par.equals("gross")){
                holder.distance2.setVisibility(View.VISIBLE);
                holder.distance3.setVisibility(View.GONE);
            }else {
                holder.distance2.setVisibility(View.GONE);
                holder.distance3.setVisibility(View.VISIBLE);
            }

            String row_unselect = get_auth_token("un_select");

            if (row_unselect.equals(position + 1 + "")) {
            } else if (position % 2 == 1) {
                holder.serial_number.setBackgroundResource(R.color.second_new_row_scoreboard);
                holder.itemView.setBackgroundResource(R.color.colorwhite);
            } else {
                holder.serial_number.setBackgroundResource(R.color.new_row_scoreboard);
                holder.itemView.setBackgroundResource(R.color.new_gry_baground);
            }


            if (data1.size()==10){

                if (data3.size() == position + 1) {
                    holder.serial_number.setText("");
                    holder.hole_number.setText("");
                } else {
                    holder.serial_number.setText(holder.postiondata);
                    if (Integer.parseInt(data2.get(holder.postiondata)) == 0) {
                        holder.hole_number.setText("");
                    } else {
                        holder.hole_number.setText(data2.get(holder.postiondata) + "");
                    }
                }

                System.out.println(holder.postiondata+"-------------"+data4.get(holder.postiondata));

                if (data4.get(holder.postiondata).equals("0") || data4.get(holder.postiondata).equals("null") || data4.get(holder.postiondata) == null || data4.get(holder.postiondata).equals("") || data4.get(holder.postiondata) == "null") {
                    holder.distance2.setText("-");
                } else {
                    holder.distance2.setText(data4.get(holder.postiondata) + "");
                }

                if (Location_Services.game_name.equals("scramble") || Location_Services.game_name.equals("strokeplay")) {
                    holder.distance3.setText(" - ");
                } else {
                    if (data5.get(holder.postiondata).equals("0") || data5.get(holder.postiondata).equals("null") || data5.get(holder.postiondata).equals(null) || data5.get(holder.postiondata).equals("")) {
                        holder.distance3.setText("-");
                    } else {
                        holder.distance3.setText(data5.get(holder.postiondata) + "");
                    }
                }


//                if (Integer.parseInt(data6.get(holder.postiondata)) == 0) {
//                    holder.distance1.setText("");
//                } else {
                holder.distance1.setText(data6.get(holder.postiondata) + "");
//                }

                if (Integer.parseInt(data3.get(holder.postiondata)) == 0) {
                    holder.distance.setText("");
                } else {
                    holder.distance.setText(data3.get(holder.postiondata) + "");
                }

            }else {
                if (half_data == position) {
                    int parr = 0, yards = 0, gross = 0, net = 0;
                    for (int i = 0; i < half_data; i++) {
                        String pos = postion_list.get(i);
                        parr += Integer.parseInt(data2.get(pos));
                        yards += Integer.parseInt(data3.get(pos));
                        if (data4.get(pos).equals("") || data4.get(pos).equals("null") || data4.get(pos).equals(null) || data4.get(pos).equals("")) {
                            gross += 0;
                        } else {
                            gross += Integer.parseInt(data4.get(pos));
                        }
                        if (data4.get(pos).equals("") || data4.get(pos).equals("null") || data4.get(pos).equals(null) || data4.get(pos).equals("")) {
                            net += 0;
                        } else {
                            net += Integer.parseInt(data5.get(pos));
                        }
                    }
                    holder.serial_number.setText("OUT");

                    if (parr == 0) {
                        holder.hole_number.setText("");
                    } else {
                        holder.hole_number.setText(parr + "");
                    }

                    if (yards == 0) {
                        holder.distance.setText("");
                    } else {
                        holder.distance.setText(yards + "");
                    }

                    if (gross == 0) {
                        holder.distance2.setText(gross + "");
                    } else {
                        holder.distance2.setText(gross + "");
                    }

                    if (Location_Services.game_name.equals("scramble") || Location_Services.game_name.equals("strokeplay")) {
                        holder.distance3.setText(" - ");
                    } else {
                        if (net == 0) {
                            holder.distance3.setText(net + "");
                        } else {
                            holder.distance3.setText(net + "");
                        }
                    }

                    holder.distance1.setText("");

                } else if (position == data1.size() - 2) {

                    int parr = 0, yards = 0, gross = 0, net = 0;
                    for (int i = half_data; i < data1.size() - 3; i++) {
                        String pos = postion_list.get(i);
                        parr += Integer.parseInt(data2.get(pos));
                        yards += Integer.parseInt(data3.get(pos));
                        if (data4.get(pos).equals("0") || data4.get(pos).equals("null") || data4.get(pos).equals(null) || data4.get(pos).equals("")) {
                            gross += 0;
                        } else {
                            gross += Integer.parseInt(data4.get(pos));
                        }
                        if (data4.get(pos).equals("0") || data4.get(pos).equals("null") || data4.get(pos).equals(null) || data4.get(pos).equals("")) {
                            net += 0;
                        } else {
                            net += Integer.parseInt(data5.get(pos));
                        }
                    }

                    holder.serial_number.setText("IN");

                    if (parr == 0) {
                        holder.hole_number.setText("");
                    } else {
                        holder.hole_number.setText(parr + "");
                    }

                    if (yards == 0) {
                        holder.distance.setText("");
                    } else {
                        holder.distance.setText(yards + "");
                    }
                    if (gross == 0) {
                        holder.distance2.setText(gross + "");
                    } else {
                        holder.distance2.setText(gross + "");
                    }

                    if (Location_Services.game_name.equals("scramble") || Location_Services.game_name.equals("strokeplay")) {
                        holder.distance3.setText(" - ");
                    } else {
                        if (net == 0) {
                            holder.distance3.setText("-");
                        } else {
                            holder.distance3.setText(net + "");
                        }
                    }

                    holder.distance1.setText("");

                } else if (position < half_data) {

                    if (data3.size() == position + 1) {
                        holder.serial_number.setText("");
                        holder.hole_number.setText("");
                    } else {
                        holder.serial_number.setText(holder.postiondata);
                        if (Integer.parseInt(data2.get(holder.postiondata)) == 0) {
                            holder.hole_number.setText("");
                        } else {
                            holder.hole_number.setText(data2.get(holder.postiondata) + "");
                        }
                    }

                    if (data4.get(holder.postiondata).equals("0") || data4.get(holder.postiondata).equals("null") || data4.get(holder.postiondata).equals(null) || data4.get(holder.postiondata).equals("")) {
                        holder.distance2.setText("-");
                    } else {
                        holder.distance2.setText(data4.get(holder.postiondata) + "");
                    }

                    if (Location_Services.game_name.equals("scramble") || Location_Services.game_name.equals("strokeplay")) {
                        holder.distance3.setText(" - ");
                    } else {
                        if (data5.get(holder.postiondata).equals("0") || data5.get(holder.postiondata).equals("null") || data5.get(holder.postiondata).equals(null) || data5.get(holder.postiondata).equals("")) {
                            holder.distance3.setText("-");
                        } else {
                            holder.distance3.setText(data5.get(holder.postiondata) + "");
                        }
                    }


//                    if (Integer.parseInt(data6.get(holder.postiondata)) == 0) {
//                        holder.distance1.setText("");
//                    } else {
                    holder.distance1.setText(data6.get(holder.postiondata) + "");
//                    }

                    if (Integer.parseInt(data3.get(holder.postiondata)) == 0) {
                        holder.distance.setText("");
                    } else {
                        holder.distance.setText(data3.get(holder.postiondata) + "");
                    }

                } else if (position > half_data && position < data1.size() - 2) {

                    holder.postiondata = postion_list.get(position - 1);

                    if (data3.size() == position + 1) {
                        holder.serial_number.setText("");
                        holder.hole_number.setText("");
                    } else {
                        holder.serial_number.setText(holder.postiondata);
                        if (Integer.parseInt(data2.get(holder.postiondata)) == 0) {
                            holder.hole_number.setText("");
                        } else {
                            holder.hole_number.setText(data2.get(holder.postiondata) + "");
                        }
                    }

                    if (data4.get(holder.postiondata).equals("0") || data4.get(holder.postiondata).equals("null") || data4.get(holder.postiondata).equals(null) || data4.get(holder.postiondata).equals("")) {
                        holder.distance2.setText("-");
                    } else {
                        holder.distance2.setText(data4.get(holder.postiondata) + "");
                    }

                    if (Location_Services.game_name.equals("scramble") || Location_Services.game_name.equals("strokeplay")) {
                        holder.distance3.setText(" - ");
                    } else {
                        if (Integer.parseInt(data5.get(holder.postiondata)) == 0) {
                            holder.distance3.setText("-");
                        } else {
                            holder.distance3.setText(data5.get(holder.postiondata) + "");
                        }
                    }

//                    if (Integer.parseInt(data6.get(holder.postiondata)) == 0) {
//                        holder.distance1.setText("");
//                    } else {
                    holder.distance1.setText(data6.get(holder.postiondata) + "");
//                    }

                    if (Integer.parseInt(data3.get(holder.postiondata)) == 0) {
                        holder.distance.setText("");
                    } else {
                        holder.distance.setText(data3.get(holder.postiondata) + "");
                    }
                }
            }

            try {

                if (data1.size()>10){
                    if (half_data == position || position == data3.size() - 2) {
                        holder.edit_icon.setEnabled(false);
                        holder.edit_icon.setVisibility(View.INVISIBLE);
                    }
                }

                int remaining_par = 0, remaingin_gross = 0;
                String data4_values = data4.get(holder.postiondata);
                if (data4_values.equals("0") || data4_values.equals("") || data4_values.equals(null)) {
                    remaingin_gross = 0;
                    remaining_par = 0;
                } else {
                    remaingin_gross = Integer.parseInt(data4.get(holder.postiondata));
                    remaining_par = Integer.parseInt(data2.get(holder.postiondata));
                }
                int total_remaining = remaingin_gross - remaining_par;

                if (game_name.equals("scramble") || game_name.equals("Course") || Location_Services.game_name.equals("strokeplay")) {
                    if (total_remaining == 0) {
                        if (Integer.parseInt(data4.get(holder.postiondata)) != 0) {
                            holder.distance2.setBackgroundResource(R.drawable.par_color);
                        }
                    } else if (total_remaining == -1) {
                        holder.distance2.setBackgroundResource(R.drawable.bride);
                    } else if (total_remaining < -1) {
                        holder.distance2.setBackgroundResource(R.drawable.eagle);
                    } else if (total_remaining == 1) {
                        holder.distance2.setBackgroundResource(R.drawable.bogey);
                    } else if (total_remaining >= 1) {
                        holder.distance2.setBackgroundResource(R.drawable.double_bpgey);
                    }
                } else {
                    if (total_remaining == 0) {
                        if (Integer.parseInt(data4.get(holder.postiondata)) != 0) {
                            holder.distance3.setBackgroundResource(R.drawable.par_color);
                        }
                    } else if (total_remaining == -1) {
                        holder.distance3.setBackgroundResource(R.drawable.bride);
                    } else if (total_remaining < -1) {
                        holder.distance3.setBackgroundResource(R.drawable.eagle);
                    } else if (total_remaining == 1) {
                        holder.distance3.setBackgroundResource(R.drawable.bogey);
                    } else if (total_remaining >= 1) {
                        holder.distance3.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }

            } catch (Exception e) {
            }

            if (position == data3.size() - 1) {

                holder.edit_icon.setEnabled(false);
                holder.edit_icon.setVisibility(View.INVISIBLE);

                int parr = 0, yards = 0, gross = 0, net = 0;

                int last_position = 0;
                if (data1.size()==10) {
                    last_position = 1;
                }else {
                    last_position = 3;
                }

                for (int i = 0; i < data1.size() -last_position; i++) {

                    String pos = postion_list.get(i);
                    parr += Integer.parseInt(data2.get(pos));
                    yards += Integer.parseInt(data3.get(pos));
                    if (data4.get(pos).equals("0") || data4.get(pos).equals("null") || data4.get(pos).equals(null) || data4.get(pos).equals("")) {
                        gross += 0;
                    } else {
                        gross += Integer.parseInt(data4.get(pos));
                    }
                    if (data4.get(pos).equals("0") || data4.get(pos).equals("null") || data4.get(pos).equals(null) || data4.get(pos).equals("")) {
                        net += 0;
                    } else {
                        net += Integer.parseInt(data5.get(pos));
                    }
                }

                holder.itemView.setBackgroundResource(R.color.color_bottom_gry);
                holder.serial_number.setBackgroundResource(R.color.color_bottom_gry);

                holder.distance.setTextColor(Color.parseColor("#FFFFFF"));
                holder.serial_number.setTextColor(Color.parseColor("#FFFFFF"));
                holder.hole_number.setTextColor(Color.parseColor("#FFFFFF"));
                holder.distance1.setTextColor(Color.parseColor("#FFFFFF"));
                holder.distance2.setTextColor(Color.parseColor("#FFFFFF"));
                holder.distance3.setTextColor(Color.parseColor("#FFFFFF"));

                holder.serial_number.setText("TOT");
                holder.hole_number.setText(parr + "");
                holder.distance.setText(yards + "");
                holder.distance1.setText("");
                holder.distance2.setText(gross + "");

                holder.serial_number.setTypeface(semibold);

                if (game_name.equals("scramble") || Location_Services.game_name.equals("strokeplay")) {
                    holder.distance3.setText("-");
                } else {
                    holder.distance3.setText(net + "");
                }

            }

            if (NewEventDetailPage.event_type.equals("passed")) {
                holder.edit_icon.setClickable(false);
                holder.edit_icon.setEnabled(false);
                holder.edit_icon.setVisibility(View.INVISIBLE);
            } else {

                if (position == hole_selected_position - 1) {
                    holder.edit_icon.setEnabled(true);
                    holder.edit_icon.setTextColor(Color.parseColor("#09A5CE"));
                }
            }

            holder.edit_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data1.size()==10){
                        if (position == data3.size() - 1 ){

                        } else {

                            Share_it.savestring("response_postions", holder.postiondata);
                            Share_it.savestring("row_three_postion", position + 1 + "");
                            Share_it.savestring("un_select", "-2");
                            Share_it.savestring("row_gross_Score", data6.get(holder.postiondata));

                            savestring("selected_hole_postion", holder.postiondata + "");

                            Intent in = new Intent(NormalScorreboard.this, AddScoresToScorecard.class);
                            in.putExtra("player_name", player_name);
                            in.putExtra("hole", holder.postiondata);
                            in.putExtra("par", data2.get(holder.postiondata));
                            startActivityForResult(in, 2);

                        }
                    }else {

                        if (position == data3.size() - 1 || half_data == position || position == data3.size() - 2) {

                        } else {

                            Share_it.savestring("response_postions", holder.postiondata);
                            Share_it.savestring("row_three_postion", position + 1 + "");
                            Share_it.savestring("un_select", "-2");
                            Share_it.savestring("row_gross_Score", data6.get(holder.postiondata));

                            savestring("selected_hole_postion", holder.postiondata + "");

                            Intent in = new Intent(NormalScorreboard.this, AddScoresToScorecard.class);
                            in.putExtra("player_name", player_name);
                            in.putExtra("hole", holder.postiondata);
                            in.putExtra("par", data2.get(holder.postiondata));
                            startActivityForResult(in, 2);

                        }
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return data1.size();

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String message = data.getStringExtra("MESSAGE");
            if (!message.equals("-1")) {
                try {
                    response_postions = get_auth_token("response_postions");
                    hand_hadicap_selecteds = get_auth_token("handicap_score");
                    row_three_postion = get_auth_token("row_three_postion");
                    selected_gross_score = get_auth_token("row_gross_Score");
                    netscorecalculation = new JSONObject();
                    netscorecalculation.put("hole_number", response_postions);
                    netscorecalculation.put("handicap", selected_gross_score);
                    netscorecalculation.put("gross_score", message);
                    netscorecalculation.put("fairway_hit", AddScoresToScorecard.first_row);
                    netscorecalculation.put("green_in_regulation", AddScoresToScorecard.second_row);
                    netscorecalculation.put("putts", AddScoresToScorecard.third_row);
                    netscorecalculation.put("notes", AddScoresToScorecard.notes_values);

                    if (!isNetworkAvailable()) {
                        Toast.makeText(NormalScorreboard.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }else {
                        if (cross_score_enabled) {
                            method_to_call_cross_score(message,netscorecalculation,cross_score_player_id,response_postions);
                        }else {
                            methodtocallnetscoreApi(netscorecalculation, response_postions);
                        }
                    }
                } catch (Exception e) {
                    //nothing
                }
            }

        } else if (resultCode == RESULT_CANCELED) {
            row_three_postion = get_auth_token("row_three_postion");

            Share_it.savestring("un_select", row_three_postion);

            livescorecard.notifyDataSetChanged();
        }
    }

    // cross score api integration
    private void method_to_call_cross_score(String gross_value,JSONObject data,String player_id,String hole_number) {

        String  url_value = "hole_info/cross_score/gross_score/" + event_id+"/"+player_id;

        ProgressDialog.getInstance().showProgress(this);

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST, Splash_screen.url + url_value, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println("------ API ------"+response);

                            ProgressDialog.getInstance().hideProgress();

                            try {
                                hole_selected_position = Integer.parseInt(response.getString("completed_hole"));

                                if (total_holes == 9) {
                                    if (hole_selected_position == 9) {
                                        hole_selected_position = 1;
                                        View_course_map.hole_number_from_all_corses = 1;
                                        savestring("selected_hole_postion", "1");
                                    } else {
                                        hole_selected_position++;
                                        View_course_map.hole_number_from_all_corses = hole_selected_position ;
                                        savestring("selected_hole_postion", hole_selected_position + "");
                                    }
                                }

                                if (total_holes == 18) {
                                    if (hole_selected_position > 8 && hole_selected_position < 18) {
                                        hole_selected_position = hole_selected_position + 2;
                                        View_course_map.hole_number_from_all_corses = (hole_selected_position - 1);
                                        savestring("selected_hole_postion", hole_selected_position - 1 + "");
                                    } else if (hole_selected_position == 18) {
                                        hole_selected_position = 1;
                                        View_course_map.hole_number_from_all_corses = 1;
                                        savestring("selected_hole_postion", "1");
                                    } else {
                                        hole_selected_position++;
                                        View_course_map.hole_number_from_all_corses = hole_selected_position ;
                                        savestring("selected_hole_postion", hole_selected_position + "");
                                    }
                                }

                            } catch (Exception e) {
                                View_course_map.hole_number_from_all_corses = 1;
                                savestring("selected_hole_postion", "1");
                                hole_selected_position = Integer.parseInt(selected_hole_postion) - 1;
                            }

                            if (response.getString("status").equals("Success")) {
                                data3.put(response_postions, response.getString("gross_score"));
                                data4.put(response_postions, response.getString("net_score"));

                                String total_score = response.getString("total_score");
                                int to_par = Integer.parseInt(total_score);
                                tv_to_par_values.setText(Html.fromHtml("<b><big>"+to_par+"</big></b>"+"<br> TO PAR"));


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

                                livescorecard = new Livescorecard(data1, data2, data3, data4, data5, hole_postionss, NormalScorreboard.this);
                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(NormalScorreboard.this, LinearLayoutManager.VERTICAL, false);
                                recycler_view.setLayoutManager(horizontalLayoutManager);
                                recycler_view.setAdapter(livescorecard);
                                recycler_view.smoothScrollToPosition(hole_selected_position);
                            } else {
                                Toast.makeText(NormalScorreboard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }

                    } catch (Exception e) {
                            ProgressDialog.getInstance().hideProgress();
                        }

                        ProgressDialog.getInstance().hideProgress();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        ProgressDialog.getInstance().hideProgress();

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(NormalScorreboard.this);
                            } else {
                                Toast.makeText(NormalScorreboard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(NormalScorreboard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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


    public void methodtocallnetscoreApi(JSONObject data, final String response_postions) {

        String url_value = "";
        if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")) {
            url_value = "hole_info/player/gross_score/" + event_id;
        } else if (get_auth_token("play_type").equals("event")) {
            url_value = "hole_info/gross_score/" + event_id;
        }
        ProgressDialog.getInstance().showProgress(this);

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST, Splash_screen.url + url_value, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            ProgressDialog.getInstance().hideProgress();

                            savestring("game_completed",response.getString("completed"));

                            try {
                                hole_selected_position = Integer.parseInt(response.getString("completed_hole"));

                                if (total_holes == 9) {
                                    if (hole_selected_position == 9) {
                                        hole_selected_position = 1;
                                        View_course_map.hole_number_from_all_corses = 1;
                                        savestring("selected_hole_postion", "1");
                                    } else {
                                        hole_selected_position++;
                                        View_course_map.hole_number_from_all_corses = hole_selected_position ;
                                        savestring("selected_hole_postion", hole_selected_position + "");
                                    }
                                }

                                if (total_holes == 18) {
                                    if (hole_selected_position > 8 && hole_selected_position < 18) {
                                        hole_selected_position = hole_selected_position + 2;
                                        View_course_map.hole_number_from_all_corses = (hole_selected_position - 1);
                                        savestring("selected_hole_postion", hole_selected_position - 1 + "");
                                    } else if (hole_selected_position == 18) {
                                        hole_selected_position = 1;
                                        View_course_map.hole_number_from_all_corses = 1;
                                        savestring("selected_hole_postion", "1");
                                    } else {
                                        hole_selected_position++;
                                        View_course_map.hole_number_from_all_corses = hole_selected_position ;
                                        savestring("selected_hole_postion", hole_selected_position + "");
                                    }
                                }

                            } catch (Exception e) {
                                View_course_map.hole_number_from_all_corses = 1;
                                savestring("selected_hole_postion", "1");
                                hole_selected_position = Integer.parseInt(selected_hole_postion) - 1;
                            }

                            if (response.getString("status").equals("Success")) {
                                data3.put(response_postions, response.getString("gross_score"));
                                data4.put(response_postions, response.getString("net_score"));

                                String total_score = response.getString("total_score");
                                int to_par = Integer.parseInt(total_score);
                                tv_to_par_values.setText(Html.fromHtml("<b><big>"+to_par+"</big></b>"+"<br> TO PAR"));


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

                                livescorecard = new Livescorecard(data1, data2, data3, data4, data5, hole_postionss, NormalScorreboard.this);
                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(NormalScorreboard.this, LinearLayoutManager.VERTICAL, false);
                                recycler_view.setLayoutManager(horizontalLayoutManager);
                                recycler_view.setAdapter(livescorecard);
                                recycler_view.smoothScrollToPosition(hole_selected_position);
                            } else {
                                Toast.makeText(NormalScorreboard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            ProgressDialog.getInstance().hideProgress();
                        }

                        ProgressDialog.getInstance().hideProgress();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        ProgressDialog.getInstance().hideProgress();

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(NormalScorreboard.this);
                            } else {
                                Toast.makeText(NormalScorreboard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(NormalScorreboard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
}
