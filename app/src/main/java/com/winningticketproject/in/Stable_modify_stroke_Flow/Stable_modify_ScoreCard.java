package com.winningticketproject.in.Stable_modify_stroke_Flow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.winningticketproject.in.NewMapEvenFLOW.NormalScorreboard;
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
import static com.winningticketproject.in.IndividulaGameType.Location_Services.team_name;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;

public class Stable_modify_ScoreCard extends AppCompatActivity implements View.OnClickListener {

    View dialogview;
    String selected_hole_postion = "";
    String row_three_postion;
    String hand_hadicap_selecteds = "0";
    String selected_gross_score = "0";
    String response_postions = "";
    int  postio_select = 0,hole_selected_position=0,total_holes=0,progress=0;
    RecyclerView recycler_view;
    TextView tvtotal_score, tvholes, tvdistance, tvgrassscore, tvnetscore, tvhandicap, tvpar;
    public static String event_id;
    public Map<String, String> data1 = new HashMap<String, String>();
    public Map<String, String> data2 = new HashMap<String, String>();
    public Map<String, String> data3 = new HashMap<String, String>();
    public Map<String, String> data4 = new HashMap<String, String>();
    public Map<String, String> data5 = new HashMap<String, String>();
    public Map<String, String> data6 = new HashMap<String, String>();
    ArrayList<String> hole_postionss = new ArrayList<>();
    Stable_Modify_adapter horizontalAdapter;
    JSONObject netscorecalculation;
    String hand_hadicap_selected,player_name,par_values="0";
    LinearLayout ll_non_course;
    boolean isnotcourse;
    Button btn_gps;
    Button btn_leader_board;
    TextView tv_course_name;

    TextView tv_player_name;
    TextView tv_to_par_values;
    String home;

    String to_par="gross";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stable_modify__score_card);

        event_id = get_auth_token("event_id");
        selected_hole_postion = get_auth_token("selected_hole_postion");
        Share_it.savestring("un_select", "-2");

        ll_non_course = findViewById(R.id.ll_non_course);
        ll_non_course.setVisibility(View.VISIBLE);
        isnotcourse = false;

        Bundle extras = getIntent().getExtras();
        home = extras.getString("home");

        tvtotal_score = findViewById(R.id.tvtotal_score);

        method_to_load_top_section();

        tvholes =findViewById(R.id.tvholes);
        tvdistance =findViewById(R.id.tvdistance);
        tvgrassscore = findViewById(R.id.tvgrassscore);
        tvnetscore = findViewById(R.id.tvnetscore);
        tvpar = findViewById(R.id.tvpar);
        tvnetscore.setOnClickListener(this);

        tvholes.setTypeface(medium);
        tvdistance.setTypeface(medium);
        tvgrassscore.setTypeface(medium);
        tvnetscore.setTypeface(medium);
        tvpar.setTypeface(medium);

        TextView tvstb_ford = findViewById(R.id.tvstb_ford);
        tvstb_ford.setTypeface(medium);
        tvstb_ford.setText("MSF");

        method_to_add_common_files();

        hand_hadicap_selected = get_auth_token("handicap");

        if (!isNetworkAvailable()) {
            Toast.makeText(this,"No Internet available",Toast.LENGTH_LONG).show();
        }    else {
            hand_hadicap_selected = "0";
            tv_player_name.setText(team_name);
            methodforscorecard();
        }

        tv_player_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (get_auth_token("handicap_score").equals("diseble")) {
                    Team_edit_popup();
                }
            }
        });

    }

    private void Team_edit_popup() {

        final android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Stable_modify_ScoreCard.this, R.style.CustomCastTheme);
        LayoutInflater inflater = getLayoutInflater();
        dialogview = inflater.inflate(R.layout.custome_team_change, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;

        final TextView pop_up_title = alertDialog.findViewById(R.id.pop_up_title);
        final EditText et_team_name = alertDialog.findViewById(R.id.et_team_name);
        Button continue_button = alertDialog.findViewById(R.id.save_team_name);

        et_team_name.setText(team_name);
        pop_up_title.setTypeface(medium);
        continue_button.setTypeface(medium);
        et_team_name.setTypeface(regular);

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isNetworkAvailable()) {
                    Toast.makeText(Stable_modify_ScoreCard.this,"No Internet Available",Toast.LENGTH_LONG).show();
                }
                else
                {
                    create_team_edit_method(et_team_name.getText().toString());
                }

                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    alertDialog.dismiss();
                }
            }
        });

    }


    private void create_team_edit_method(final String team_names) {

        JSONObject object = new JSONObject();
        try {
            object.put("team_name",team_names);
        }catch (Exception e){ }

        JsonObjectRequest myRequest_handicap = new JsonObjectRequest(
                Request.Method.POST, Splash_screen.url+"hole_info/team_name/"+event_id , object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("Success")){
                                tv_player_name.setText(team_names);
                                Toast.makeText(Stable_modify_ScoreCard.this,""+response.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            //nothing
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(Stable_modify_ScoreCard.this);
                            } else {
                                Toast.makeText(Stable_modify_ScoreCard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Stable_modify_ScoreCard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
    }



    public void methodforscorecard() {
        String url_value="";
        try {

            url_value="api/v2/hole_info/select-course-info/" + event_id;

            recycler_view = findViewById(R.id.stable_modify_ford_recycler_view);

            data1 = new HashMap<String, String>();
            data2 = new HashMap<String, String>();
            data3 = new HashMap<String, String>();
            data4 = new HashMap<String, String>();
            data5 = new HashMap<String, String>();
            data6 = new HashMap<String, String>();

            ProgressDialog.getInstance().showProgress(Stable_modify_ScoreCard.this);

            JSONObject params = new JSONObject();
            try{
                params.put("handicap",get_auth_token("handicap"));
                params.put("selected_tee",get_auth_token("selected_tee_postion"));
                params.put("selected_hole",get_auth_token("selected_hole_postion"));
            }catch (Exception e){ }


            JsonObjectRequest myRequest_handicap = new JsonObjectRequest(
                    Request.Method.POST, Splash_screen.imageurl +url_value , params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                                System.out.println("----res--------"+response);
                                if (response.getString("status").equals("Success")) {
                                    try {

                                        savestring("game_completed",response.getString("completed"));

                                        player_name = response.getString("name").replaceAll("\\s+"," ").substring(0,1).toUpperCase() +response.getString("name").substring(1);

                                        if (get_auth_token("handicap_score").equals("diseble")) {
                                            hand_hadicap_selected = "0";
                                            tv_player_name.setText(response.getString("team_name"));
                                        } else {
                                            tv_player_name.setText(Html.fromHtml("<b>"+player_name+"</b>"+"<br>"+"Handicap <b>"+get_auth_token("handicap")+"<b>"));
                                        }

                                        String net_score = response.getString("total_score");
                                        if(net_score==null || net_score.equals("") || net_score.equals("null") || net_score.equals(null)){
                                            net_score = "0";
                                        }

                                        int to_par = Integer.parseInt(net_score);
                                        tv_to_par_values.setText(Html.fromHtml("<b><big>"+to_par+"</big></b>"+"<br> MSF"));


                                        String tot_par_valr = response.getString("total_net_score");
                                        if (tot_par_valr.equals("null") || tot_par_valr.equals(null)){
                                            tot_par_valr="0";
                                        }

                                        String tot_gross_score = response.getString("total_gross_score");
                                        if (tot_gross_score.equals("null") || tot_gross_score.equals(null)){
                                            tot_gross_score="0";
                                        }

                                        String game_type = "";

                                        game_type = "Gross " + tot_gross_score + "/" + response.getString("total_par_value")+" - Net " + "</b>" + tot_par_valr+ "/" + response.getString("total_par_value");

                                        String couurse_data = "<b>"+get_auth_token("Event_name")+"</b>"+"<br>Starting "+ "Hole "+get_auth_token("selected_hole_postion")+"<b>" + " - "+ "</b>"+ get_auth_token("selected_tee_postion")+" "+get_auth_token("yards_value")+"<br>"+game_type;

                                        savestring("leader_board_map_course",couurse_data);
                                        tv_course_name.setText(Html.fromHtml(couurse_data));

                                    }catch (Exception e){
                                        //nothing

                                    }

                                    JSONArray hole_info = response.getJSONArray("hole_info");
                                    View_course_map.count_list = hole_info.length();
                                    total_holes = hole_info.length();


                                    try {
                                        String stating_hole = response.getString("starting_hole");
                                        String com_hole = response.getString("completed_hole");

                                        if (com_hole.equals(null) || com_hole.equals("null") || com_hole==null){
                                            if (stating_hole.equals(null) || stating_hole.equals("null") || stating_hole == null) {
                                                stating_hole="0";
                                            }
                                            com_hole="0";
                                            hole_selected_position = Integer.parseInt(stating_hole);
                                            View_course_map.hole_number_from_all_corses  = hole_selected_position;

                                        }else {
                                            hole_selected_position = Integer.parseInt(com_hole);

                                            if (total_holes==9){
                                                if (hole_selected_position==9){
                                                    hole_selected_position=1;
                                                    View_course_map.hole_number_from_all_corses  = 1;
                                                }else {
                                                    View_course_map.hole_number_from_all_corses  = hole_selected_position;
                                                }
                                            }

                                            if (total_holes==18){
                                                if (hole_selected_position>8 && hole_selected_position<18){
                                                    hole_selected_position = hole_selected_position+2;
                                                    View_course_map.hole_number_from_all_corses  = hole_selected_position-1;
                                                }else if (hole_selected_position==18){
                                                    hole_selected_position=1;
                                                    View_course_map.hole_number_from_all_corses  = 1;
                                                }else {
                                                    View_course_map.hole_number_from_all_corses  = hole_selected_position;
                                                }
                                            }

                                        }
                                    }catch (Exception e){
                                        hole_selected_position = Integer.parseInt(selected_hole_postion)-1;
                                    }


                                    for (int i = 0; i < hole_info.length(); i++) {
                                        JSONObject hole_object = hole_info.getJSONObject(i);
                                        String hole_postion = hole_object.getString("position");
                                        String par = hole_object.getString("par");
                                        String yards = hole_object.getString("yards");
                                        String handicap = hole_object.getString("handicap");
                                        String gross_score = hole_object.getString("gross_score");
                                        String net_score = hole_object.getString("net_score");
                                        String sf_msf_score = hole_object.getString("sf_msf_score");

                                        hole_postionss.add(hole_postion);
                                        if (gross_score.equals("null") || gross_score.equals("") || gross_score.equals(null)) {
                                            gross_score="0";
                                            data3.put(hole_postion, "0");
                                        } else {
                                            data3.put(hole_postion, gross_score);
                                        }

                                        if (net_score.equals("null") || net_score.equals("") || net_score.equals(null)) {
                                            net_score="0";
                                            data4.put(hole_postion, "0");
                                        } else {
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

                                        if (par.equals("null") || par.equals("") || par.equals(null))
                                        {
                                            par="0";
                                            data1.put(hole_postion, "0");
                                        }else {
                                            data1.put(hole_postion, par);
                                        }

                                        if (yards.equals("null") || yards.equals("") || yards.equals(null))
                                        {
                                            data2.put(hole_postion, "0");
                                        }else {
                                            data2.put(hole_postion, yards);
                                        }

                                        data6.put(hole_postion, sf_msf_score);

                                    }

                                    if (data1.size()>10){
                                        hole_postionss.add("0");
                                        data1.put(hole_info.length() + 1 + "", "0");
                                        data2.put(hole_info.length() + 1 + "", "0");
                                        data3.put(hole_info.length() + 1 + "", "0");
                                        data4.put(hole_info.length() + 1 + "", "0");
                                        data5.put(hole_info.length() + 1 + "", "0");
                                        data6.put(hole_info.length() + 1 + "", "0");

                                        hole_postionss.add("0");
                                        data1.put(hole_info.length() + 2 + "", "0");
                                        data2.put(hole_info.length() + 2 + "", "0");
                                        data3.put(hole_info.length() + 2 + "", "0");
                                        data4.put(hole_info.length() + 2 + "", "0");
                                        data5.put(hole_info.length() + 2 + "", "0");
                                        data6.put(hole_info.length() + 2 + "", "0");

                                        hole_postionss.add("0");
                                        data1.put(hole_info.length() + 3 + "", "0");
                                        data2.put(hole_info.length() + 3 + "", "0");
                                        data3.put(hole_info.length() + 3 + "", "0");
                                        data4.put(hole_info.length() + 3 + "", "0");
                                        data5.put(hole_info.length() + 3 + "", "0");
                                        data6.put(hole_info.length() + 3 + "", "0");
                                    }else {
                                        hole_postionss.add(hole_info.length() + 1+"");
                                        data1.put(hole_info.length() + 1 + "", "0");
                                        data2.put(hole_info.length() + 1 + "", "0");
                                        data3.put(hole_info.length() + 1 + "", "0");
                                        data4.put(hole_info.length() + 1 + "", "0");
                                        data5.put(hole_info.length() + 1 + "", "0");
                                        data6.put(hole_info.length() + 1 + "", "0");
                                    }

                                    horizontalAdapter = new Stable_Modify_adapter(data1, data2, data3, data4, data5,data6,hole_postionss, Stable_modify_ScoreCard.this);
                                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(Stable_modify_ScoreCard.this, LinearLayoutManager.VERTICAL, false);
                                    recycler_view.setLayoutManager(horizontalLayoutManager);
                                    recycler_view.setAdapter(horizontalAdapter);
                                    recycler_view.smoothScrollToPosition(hole_selected_position);
                                    recycler_view.setNestedScrollingEnabled(false);

                                    com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();

                                } else {
                                    com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                                    Toast.makeText(Stable_modify_ScoreCard.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                //nothing
                                com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse networkResponse = error.networkResponse;

                            com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                            if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                // HTTP Status Code: 401 Unauthorized
                                if (error.networkResponse.statusCode == 401) {
                                    Alert_Dailog.showNetworkAlert(Stable_modify_ScoreCard.this);
                                } else {
                                    Toast.makeText(Stable_modify_ScoreCard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(Stable_modify_ScoreCard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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

    public class Stable_Modify_adapter extends RecyclerView.Adapter<Stable_Modify_adapter.MyViewHolder> {
        Map<String, String> data2 = new HashMap<String, String>();
        Map<String, String> data3 = new HashMap<String, String>();
        Map<String, String> data4 = new HashMap<String, String>();
        Map<String, String> data5 = new HashMap<String, String>();
        Map<String, String> data6 = new HashMap<String, String>();
        Map<String, String> data7 = new HashMap<String, String>();
        ArrayList<String> postion_list = new ArrayList<>();
        View itemView;

        public Stable_Modify_adapter(Map<String, String> data2, Map<String, String> data3, Map<String, String> data4, Map<String, String> data5, Map<String, String> data6, Map<String,String> data7, ArrayList<String> postion_list, Context context) {
            this.data2 = data2;
            this.data3 = data3;
            this.data4 = data4;
            this.data5 = data5;
            this.data6 = data6;
            this.data7 = data7;
            this.postion_list = postion_list;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView serial_number,hole_number,distance, distance1, distance3,distance4,distance2;
            TextView edit_icon;
            String postiondata="";
            public MyViewHolder(View view) {
                super(view);
                serial_number = view.findViewById(R.id.serial_number);
                hole_number = view.findViewById(R.id.hole_number);
                distance = view.findViewById(R.id.distance);
                distance1 = view.findViewById(R.id.distance1);
                distance2 = view.findViewById(R.id.distance2);
                distance3 = view.findViewById(R.id.distance3);
                distance4 = view.findViewById(R.id.distance4);

                edit_icon = view.findViewById(R.id.edit_icon);
                edit_icon.setText("\uf040");
                edit_icon.setTypeface(webfont);

                serial_number.setTypeface(medium);
                hole_number.setTypeface(medium);
                distance.setTypeface(medium);
                distance1.setTypeface(medium);
                distance2.setTypeface(medium);
                distance3.setTypeface(medium);
                distance4.setTypeface(medium);
            }
        }

        @Override
        public Stable_Modify_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_stable_score_card, parent, false);
            return new Stable_Modify_adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final Stable_Modify_adapter.MyViewHolder holder, final int position) {

            final int half_data = (data1.size()-3)/2;
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
                    if (Integer.parseInt(data2.get(holder.postiondata))==0){
                        holder.hole_number.setText("");
                    }else {
                        holder.hole_number.setText(data2.get(holder.postiondata) +"");
                    } }

                if (Integer.parseInt(data4.get(holder.postiondata))==0){
                    holder.distance2.setText("");
                }else {
                    holder.distance2.setText(data4.get(holder.postiondata)+ "");
                    holder.distance2.setTypeface(medium);
                }

                if (Integer.parseInt(data5.get(holder.postiondata))==0){
                    holder.distance3.setText("");
                }else {
                    holder.distance3.setText(data5.get(holder.postiondata) + "");
                }

                if (data7.get(holder.postiondata).equals(null) || data7.get(holder.postiondata).equals("null")){
                    holder.distance4.setText("");
                }else {
                    holder.distance4.setText(data7.get(holder.postiondata));
                }

                if (Integer.parseInt(data6.get(holder.postiondata))==0){
                    holder.distance1.setText("");
                }else {
                    holder.distance1.setText(data6.get(holder.postiondata) + "");
                }

                if (Integer.parseInt(data3.get(holder.postiondata))==0){
                    holder.distance.setText("");
                }else {
                    holder.distance.setText(data3.get(holder.postiondata) + "");
                }

            }else {

                if (half_data == position) {
                    int parr = 0, yards = 0, gross = 0, net = 0, std_ford = 0;
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

                        if (data7.get(pos).equals("") || data7.get(pos).equals("null") || data7.get(pos).equals(null) || data7.get(pos).equals("")) {
                            std_ford += 0;
                        } else {
                            std_ford += Integer.parseInt(data7.get(pos));
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
                        holder.distance2.setText("");
                    } else {
                        holder.distance2.setText(gross + "");
                    }

                    if (net == 0) {
                        holder.distance3.setText("0");
                    } else {
                        holder.distance3.setText(net + "");
                    }

                    holder.distance4.setText(std_ford + "");


                    holder.distance1.setText("");

                } else if (position == data1.size() - 2) {

                    int parr = 0, yards = 0, gross = 0, net = 0, std_ford = 0;
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


                        if (data7.get(pos).equals("0") || data7.get(pos).equals("null") || data7.get(pos).equals(null) || data7.get(pos).equals("")) {
                            std_ford += 0;
                        } else {
                            std_ford += Integer.parseInt(data7.get(pos));
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
                        holder.distance2.setText("0");
                    } else {
                        holder.distance2.setText(gross + "");
                    }

                    if (net == 0) {
                        holder.distance3.setText("0");
                    } else {
                        holder.distance3.setText(net + "");
                    }

                    holder.distance4.setText(std_ford + "");

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
                        holder.distance2.setText("");
                    } else {
                        holder.distance2.setText(data4.get(holder.postiondata) + "");
                    }

                    if (data5.get(holder.postiondata).equals("0") || data5.get(holder.postiondata).equals("null") || data5.get(holder.postiondata).equals(null) || data5.get(holder.postiondata).equals("")) {
                        holder.distance3.setText("");
                    } else {
                        holder.distance3.setText(data5.get(holder.postiondata) + "");
                    }


                    if (data7.get(holder.postiondata).equals(null) || data7.get(holder.postiondata).equals("null")) {
                        holder.distance4.setText("");
                    } else {
                        holder.distance4.setText(data7.get(holder.postiondata));
                    }

                    if (Integer.parseInt(data6.get(holder.postiondata)) == 0) {
                        holder.distance1.setText("");
                    } else {
                        holder.distance1.setText(data6.get(holder.postiondata) + "");
                    }

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
                        holder.distance2.setText("");
                    } else {
                        holder.distance2.setText(data4.get(holder.postiondata) + "");
                    }

                    if (data5.get(holder.postiondata).equals("0") || data5.get(holder.postiondata).equals("null") || data5.get(holder.postiondata).equals(null) || data5.get(holder.postiondata).equals("")) {
                        holder.distance3.setText("-");
                    } else {
                        holder.distance3.setText(data5.get(holder.postiondata) + "");
                    }


                    if (data7.get(holder.postiondata).equals(null) || data7.get(holder.postiondata).equals("null")) {
                        holder.distance4.setText("");
                    } else {
                        holder.distance4.setText(data7.get(holder.postiondata));
                    }

                    if (Integer.parseInt(data6.get(holder.postiondata)) == 0) {
                        holder.distance1.setText("");
                    } else {
                        holder.distance1.setText(data6.get(holder.postiondata) + "");
                    }

                    if (Integer.parseInt(data3.get(holder.postiondata)) == 0) {
                        holder.distance.setText("");
                    } else {
                        holder.distance.setText(data3.get(holder.postiondata) + "");
                    }

                }
            }

            try {
                    if (!data7.get(holder.postiondata).equals(null) || !data7.get(holder.postiondata).equals("null")){
                        int total_remaining = Integer.parseInt(data7.get(holder.postiondata));
                        if (total_remaining == 0) {
                            holder.distance4.setBackgroundResource(R.drawable.double_bpgey);
                        } else if (total_remaining == 1) {
                            holder.distance4.setBackgroundResource(R.drawable.bogey);
                        } else if (total_remaining == 2) {
                            holder.distance4.setBackgroundResource(R.drawable.par_color);
                        } else if (total_remaining ==3) {
                            holder.distance4.setBackgroundResource(R.drawable.bride);
                        }else if (total_remaining == 4) {
                            holder.distance4.setBackgroundResource(R.drawable.eagle);
                        } else if (total_remaining == 5) {
                            holder.distance4.setTextColor(getResources().getColor(R.color.colorwhite));
                            holder.distance4.setBackgroundResource(R.drawable.albartoss);
                        }
                    }

                if (data1.size() > 10) {
                    if (position == data3.size() - 2 || half_data == position) {
                        holder.edit_icon.setEnabled(false);
                        holder.edit_icon.setVisibility(View.INVISIBLE);
                        holder.distance4.setBackground(null);
                    }
                }

                } catch (Exception e) { }

            if (position == data3.size() - 1) {

                holder.edit_icon.setEnabled(false);
                holder.edit_icon.setVisibility(View.INVISIBLE);

                int last_position = 0;
                if (data1.size()==10) {
                    last_position = 1;
                }else {
                    last_position = 3;
                }
                int parr = 0, yards = 0, gross = 0, net = 0,modify=0;

                for (int i = 0; i < data1.size() - last_position; i++) {
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

                    if (data7.get(pos).equals(null) || data7.get(pos).equals("null")){
                        modify+=0;
                    }else {
                        modify+= Integer.parseInt(data7.get(pos));
                    }
                }

                holder.itemView.setBackgroundResource(R.color.color_bottom_gry);
                holder.serial_number.setBackgroundResource(R.color.color_bottom_gry);

                holder.distance.setTextColor(Color.parseColor("#FFFFFF"));
                holder.serial_number.setTextColor(Color.parseColor("#FFFFFF"));
                holder.hole_number.setTextColor(Color.parseColor("#FFFFFF"));
                holder.distance1.setTextColor(Color.parseColor("#FFFFFF"));
                holder.distance3.setTextColor(Color.parseColor("#FFFFFF"));
                holder.distance4.setTextColor(Color.parseColor("#FFFFFF"));
                holder.distance2.setTextColor(Color.parseColor("#FFFFFF"));

                holder.distance2.setText(gross+"");
                holder.serial_number.setText("TOT");
                holder.hole_number.setText(parr + "");
                holder.distance.setText(yards + "");
                holder.distance1.setText("");
                holder.distance3.setText(net+"");
                holder.distance4.setText(modify+"");

            }





            if(NewEventDetailPage.event_type.equals("passed")){
                holder.edit_icon.setClickable(false);
                holder.edit_icon.setEnabled(false);
                holder.edit_icon.setVisibility(View.INVISIBLE);
            }else {

                if (position==hole_selected_position-1){
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
                            Share_it.savestring("response_postions",holder.postiondata);
                            Share_it.savestring("row_three_postion", position+1+"");
                            Share_it.savestring("un_select", "-2");
                            Share_it.savestring("row_gross_Score", data6.get(holder.postiondata));

                            Intent in = new Intent(Stable_modify_ScoreCard.this, AddScoresToScorecard.class);
                            in.putExtra("player_name", player_name);
                            in.putExtra("hole", holder.postiondata);
                            in.putExtra("par", data2.get(holder.postiondata));
                            startActivityForResult(in, 2);
                            par_values =  data2.get(holder.postiondata);
                        }
                    }else {
                        if(position==data3.size()-1 || half_data==position || position==data3.size()-2 || position==data3.size()-3){
                        }else {
                            Share_it.savestring("response_postions",holder.postiondata);
                            Share_it.savestring("row_three_postion", position+1+"");
                            Share_it.savestring("un_select", "-2");
                            Share_it.savestring("row_gross_Score", data6.get(holder.postiondata));

                            Intent in = new Intent(Stable_modify_ScoreCard.this, AddScoresToScorecard.class);
                            in.putExtra("player_name", player_name);
                            in.putExtra("hole", holder.postiondata);
                            in.putExtra("par", data2.get(holder.postiondata));
                            startActivityForResult(in, 2);
                            par_values =  data2.get(holder.postiondata);
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


    private void method_to_load_top_section() {
        btn_gps = findViewById(R.id.btn_gps);
        btn_gps.setTypeface(medium);
        btn_gps.setOnClickListener(this);

        btn_leader_board = findViewById(R.id.btn_leader_board);
        btn_leader_board.setTypeface(medium);
        btn_leader_board.setOnClickListener(this);

        tv_course_name = findViewById(R.id.tv_course_name);
        tv_course_name.setTypeface(regular);

        tv_player_name = findViewById(R.id.tv_player_name);
        tv_player_name.setTypeface(regular);

        tv_to_par_values = findViewById(R.id.tv_to_par_values);
        tv_to_par_values.setTypeface(regular);

        btn_leader_board.setText("Event");
        btn_leader_board.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));

        ImageView image_auction = findViewById(R.id.iv_auction);
        ImageView image_chat = findViewById(R.id.iv_chat);
        ImageView image_super_ticket = findViewById(R.id.iv_super_ticket);
        ImageView image_leader_board = findViewById(R.id.iv_leader_board);

        image_auction.setOnClickListener(this);
        image_chat.setOnClickListener(this);
        image_super_ticket.setOnClickListener(this);
        image_leader_board.setOnClickListener(this);
    }

    private void method_to_add_common_files() {
        TextView tv_scoring_legend = findViewById(R.id.tv_scoring_legend);
        tv_scoring_legend.setTypeface(medium);

        TextView tv_alboross = findViewById(R.id.tv_alboross);
        tv_alboross.setTypeface(regular);

        TextView tv_eagle = findViewById(R.id.tv_eagle);
        tv_eagle.setTypeface(regular);

        TextView tv_bride = findViewById(R.id.tv_bride);
        tv_bride.setTypeface(regular);

        TextView tv_par = findViewById(R.id.tv_par);
        tv_par.setTypeface(regular);

        TextView tv_bogey = findViewById(R.id.tv_bogey);
        tv_bogey.setTypeface(regular);

        TextView tv_double_bo = findViewById(R.id.tv_double_bogey);
        tv_double_bo.setTypeface(regular);

    }

    @Override
            public void onClick(View view) {
                Intent in;
                switch (view.getId()){
                    case R.id.btn_leader_board:
                        if (!Location_Services.game_name.equals("Course")) {
                            if (isnotcourse) {
                                isnotcourse = false;
                                ll_non_course.setVisibility(View.VISIBLE);
                                btn_leader_board.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));

                            } else {
                                isnotcourse = true;
                                ll_non_course.setVisibility(View.GONE);
                                btn_leader_board.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                            }
                        } else {
                            Intent in1 = new Intent(Stable_modify_ScoreCard.this, Stable_modify_leader_board.class);
                            startActivity(in1);
                            finish();

                        }
                        break;

                    case R.id.iv_super_ticket:
                        Super_ticket_poup();
                        break;

                    case R.id.btn_gps:
                        if(home.equals("home")){
                            in = new Intent(Stable_modify_ScoreCard.this , View_course_map.class);
                            in.putExtra("id",event_id);
                            startActivity(in);
                            finish();
                        }else {
                            finish();
                        }
                        break;

                    case R.id.iv_auction:
                        in=new Intent(Stable_modify_ScoreCard.this, NewLive_Auction.class);
                        savestring("id",event_id);
                        startActivity(in);
                        break;

                    case R.id.iv_leader_board:
                        in = new Intent(this, Stable_modify_leader_board.class);
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
                        horizontalAdapter.notifyDataSetChanged();
                        break;



                }
            }


    public void Super_ticket_poup() {
        android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.CustomDialogTheme);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String message = data.getStringExtra("MESSAGE");
            if (!message.equals("-1")){
                try {

                    com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().showProgress(this);
                    response_postions = get_auth_token("response_postions");
                    hand_hadicap_selecteds = get_auth_token("handicap_score");
                    row_three_postion = get_auth_token("row_three_postion");
                    selected_gross_score = get_auth_token("row_gross_Score");
                    netscorecalculation = new JSONObject();
                    netscorecalculation.put("hole_number", response_postions);
                    netscorecalculation.put("handicap", selected_gross_score);
                    netscorecalculation.put("gross_score", message);
                    netscorecalculation.put("fairway_hit",AddScoresToScorecard.first_row);
                    netscorecalculation.put("green_in_regulation", AddScoresToScorecard.second_row);
                    netscorecalculation.put("putts", AddScoresToScorecard.third_row);
                    netscorecalculation.put("par", par_values+"");
                    netscorecalculation.put("notes", AddScoresToScorecard.notes_values);

                    if (!isNetworkAvailable()) {
                        Toast.makeText(Stable_modify_ScoreCard.this,"No Internet Available",Toast.LENGTH_LONG).show();
                    } else {
                        methodtocallnetscoreApi(netscorecalculation, response_postions);
                    }


                } catch (Exception e) {
                    //nothing
                }

            }

        } else if (resultCode == RESULT_CANCELED) {
            row_three_postion = get_auth_token("row_three_postion");

            Share_it.savestring("un_select",row_three_postion);

            horizontalAdapter.notifyDataSetChanged();
        }
    }


    public void methodtocallnetscoreApi(JSONObject data, final String response_postions) {

        String url_value="";
        if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")){
            url_value = "hole_info/player/gross_score/"+event_id;
        }else if (get_auth_token("play_type").equals("event")){
            url_value = "hole_info/gross_score/" + event_id;
        }

        System.out.println(Splash_screen.url +url_value+"------------data----"+data);

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST, Splash_screen.url +url_value, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();

                            savestring("game_completed",response.getString("completed"));

                            try {
                                hole_selected_position = Integer.parseInt(response.getString("completed_hole"));

                                if (total_holes==9){
                                     if (hole_selected_position==9){
                                        hole_selected_position=1;
                                        View_course_map.hole_number_from_all_corses  = 1;
                                        savestring("selected_hole_postion","1");
                                    }else {
                                        hole_selected_position++;
                                        View_course_map.hole_number_from_all_corses  = hole_selected_position;
                                        savestring("selected_hole_postion",hole_selected_position+"");
                                    }
                                }

                                if (total_holes==18){
                                    if (hole_selected_position>8 && hole_selected_position<18){
                                        hole_selected_position = hole_selected_position+2;
                                        View_course_map.hole_number_from_all_corses  = (hole_selected_position-1);
                                        savestring("selected_hole_postion",hole_selected_position-1+"");
                                    }else if (hole_selected_position==18){
                                        hole_selected_position=1;
                                        View_course_map.hole_number_from_all_corses  = 1;
                                        savestring("selected_hole_postion","1");
                                    }else {
                                        hole_selected_position++;
                                        View_course_map.hole_number_from_all_corses  = hole_selected_position;
                                        savestring("selected_hole_postion",hole_selected_position+"");
                                    }
                                }

                            }catch (Exception e){
                                View_course_map.hole_number_from_all_corses  = 1;
                                savestring("selected_hole_postion","1");
                                hole_selected_position = Integer.parseInt(selected_hole_postion)-1;
                            }

                            postio_select = Integer.parseInt(get_auth_token("row_three_postion"));
                            //                                    {"gross_score":5,"net_score":5,"status":"Success"}
                            if (response.getString("status").equals("Success")) {

                                data3.put(response_postions, response.getString("gross_score"));
                                data4.put(response_postions, response.getString("net_score"));
                                data6.put(response_postions, response.getString("sf_msf_score"));

                                String total_score = response.getString("total_score");
                                int to_par = Integer.parseInt(total_score);
                                String game_type="";
                                game_type = "Gross " + response.getString("total_gross_score") + "/" + response.getString("total_par_value")+" - Net " + "</b>" + response.getString("total_net_score") + "/" + response.getString("total_par_value");
                                tv_to_par_values.setText(Html.fromHtml("<b><big>"+to_par+"</big></b>"+"<br> MSF"));

                                tv_course_name.setText(Html.fromHtml("Starting "+ "<b>Hole "+get_auth_token("starting_hole")+"</b>"+"<br>"+"<b>" + "Tee "+ "</b>"+ get_auth_token("selected_tee_postion")+"<br>"+game_type));


                                horizontalAdapter = new Stable_Modify_adapter(data1, data2, data3, data4, data5,data6,hole_postionss, Stable_modify_ScoreCard.this);
                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(Stable_modify_ScoreCard.this, LinearLayoutManager.VERTICAL, false);
                                recycler_view.setLayoutManager(horizontalLayoutManager);
                                recycler_view.setAdapter(horizontalAdapter);
                                recycler_view.smoothScrollToPosition(postio_select);
                            } else {
                                Toast.makeText(Stable_modify_ScoreCard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            //nothing
                            com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(Stable_modify_ScoreCard.this);
                            } else {
                                Toast.makeText(Stable_modify_ScoreCard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Stable_modify_ScoreCard.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
