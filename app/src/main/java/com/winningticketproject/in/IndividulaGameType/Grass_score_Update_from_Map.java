package com.winningticketproject.in.IndividulaGameType;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
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
import com.winningticketproject.in.Live_Score_New_Flow.View_course_map;
import com.winningticketproject.in.NewMapEvenFLOW.NormalScorreboard;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.savestring;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.game_name;
import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.setGridViewHeightBasedOnChildren;

public class Grass_score_Update_from_Map extends AppCompatActivity implements View.OnClickListener {
    int backposition = -1;
    ImageButton gross_Score_btn_back;
    String Eagle = "", Birdie = "", par_postion = "", Bogey = "", double_Bogey;
    TextView tvholes,crossTxt;
    public Map<String, String> score;
    public Map<String, String> par_position;

    ImageView tvleft,tv_right,tv_center;
    ImageView two_right,two_left;

    Button two_one,two_two,two_three,two_four,two_five;

    TextView tv_fairway_hit,tv_green_regu,tv_putts;
    Button save_grsoss_info;
    String first_row = "", second_row = "", third_row = "",auth_token="";
    JSONObject netscorecalculation;
    TextView info_gross_score;
    public static String par_data_gr;
    public static String hole_data;
    TextView tv_selected_score,add_stats_text,add_stats_icon;
    int stats_value = 0;
    GridView gridview;
    android.support.v7.app.AlertDialog alertDialog_2;
    View fair_way_hits,green_in_regular,gross_putts;

    LinearLayout et_layout,linear_layout;
    Button btn_add_note,btn_edit_note,btn_delete_note;
    String map_notes_values="";

    Button btn_cross_scoring_enabled;
    boolean cross_score_enabled;

    String cross_score_player_id;

    android.support.v7.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addscorestoscorecard);

        auth_token = Share_it.get_auth_token("auth_token");

        tv_fairway_hit  = findViewById(R.id.tv_fairway_hit);
        tvleft  = findViewById(R.id.tvleft);
        tv_center  = findViewById(R.id.tv_center);
        tv_right  = findViewById(R.id.tv_right);

        tvleft.setOnClickListener(this);
        tv_center.setOnClickListener(this);
        tv_right.setOnClickListener(this);

        tv_fairway_hit.setTypeface(regular);

        linear_layout = findViewById(R.id.linear_layout);

        info_gross_score = findViewById(R.id.info_gross_score);
        info_gross_score.setTypeface(italic);

        tv_green_regu  = findViewById(R.id.tv_green_regu);
        two_left  = findViewById(R.id.two_left);
        two_right  = findViewById(R.id.two_right);

        tv_green_regu.setTypeface(regular);

        two_left.setOnClickListener(this);
        two_right.setOnClickListener(this);

        tv_putts  = findViewById(R.id.tv_putts);
        two_one  = findViewById(R.id.two_one);
        two_two  = findViewById(R.id.two_two);
        two_three  = findViewById(R.id.two_three);
        two_four  = findViewById(R.id.two_four);
        two_five  = findViewById(R.id.two_five);

        two_one.setOnClickListener(this);
        two_two.setOnClickListener(this);
        two_three.setOnClickListener(this);
        two_four.setOnClickListener(this);
        two_five.setOnClickListener(this);

        tv_putts.setTypeface(regular);
        two_one.setTypeface(italic);
        two_two.setTypeface(italic);
        two_three.setTypeface(italic);
        two_four.setTypeface(italic);
        two_five.setTypeface(italic);

        //cross scoring button
        btn_cross_scoring_enabled = findViewById(R.id.btn_cross_scoring_enabled);
        btn_cross_scoring_enabled.setTypeface(regular);

        save_grsoss_info = findViewById(R.id.save_grsoss_info);
        save_grsoss_info.setTypeface(medium);

        save_grsoss_info.setOnClickListener(this);

        tvholes = findViewById(R.id.tvholes);
        tvholes.setTypeface(medium);

        crossTxt = findViewById(R.id.crossTxt);
        crossTxt.setTypeface(semibold);

        score = new HashMap<String, String>();
        par_position = new HashMap<String, String>();

        tvholes.setText("Hole "+hole_data+" - "+"Par "+par_data_gr);

        Eagle = Integer.parseInt(par_data_gr) - 2 + "";
        Birdie = Integer.parseInt(par_data_gr) - 1 + "";
        par_postion = Integer.parseInt(par_data_gr) - 0 + "";
        Bogey = Integer.parseInt(par_data_gr) + 1 + "";
        double_Bogey = Integer.parseInt(par_data_gr) + 2 + "";

        par_position.put(Eagle,"Eagle");
        par_position.put(Birdie,"Birdie");
        par_position.put(par_postion,"Par");
        par_position.put(Bogey,"Bogey");
        par_position.put(double_Bogey,"Double Bogey");

        gridview = findViewById(R.id.gridview);

        tv_selected_score = findViewById(R.id.tv_selected_score);
        tv_selected_score.setTypeface(medium);

        add_stats_text = findViewById(R.id.add_stats_text);
        add_stats_text.setTypeface(medium);

        add_stats_icon = findViewById(R.id.add_stats_icon);
        add_stats_icon.setText("\uf0d7");
        add_stats_icon.setTypeface(webfont);

        // three layout for
        fair_way_hits = findViewById(R.id.fair_way_hits);
        green_in_regular = findViewById(R.id.green_in_regular);
        gross_putts = findViewById(R.id.gross_putts);

        if (Integer.parseInt(par_data_gr)==3){
            fair_way_hits.setVisibility(View.GONE);
            first_row="";
        }

        gross_putts.setVisibility(View.GONE);
        green_in_regular.setVisibility(View.GONE);


        et_layout = findViewById(R.id.et_layout);
        et_layout.setVisibility(View.GONE);

        btn_add_note = findViewById(R.id.btn_add_note);
        btn_add_note.setTypeface(medium);
        btn_add_note.setOnClickListener(this);

        btn_delete_note = findViewById(R.id.btn_delete_note);
        btn_delete_note.setTypeface(medium);

        btn_delete_note.setOnClickListener(this);

        btn_edit_note = findViewById(R.id.btn_edit_note);
        btn_edit_note.setTypeface(medium);

        btn_edit_note.setOnClickListener(this);

        et_layout.setVisibility(View.GONE);
        linear_layout.setVisibility(View.VISIBLE);

        if(game_name.equals("scramble")){
            info_gross_score.setVisibility(View.VISIBLE);
        }else {
            info_gross_score.setVisibility(View.GONE);
        }

        add_stats_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (stats_value==0){
                    add_stats_text.setText("Close Stats");
                    stats_value=1;
                    add_stats_icon.setText("\uf0d8");
                    add_stats_icon.setTypeface(webfont);

                    if (Integer.parseInt(par_data_gr)==3){
                        fair_way_hits.setVisibility(View.GONE);
                        first_row="";
                    }else {
                        fair_way_hits.setVisibility(View.VISIBLE);
                    }

                    gross_putts.setVisibility(View.VISIBLE);
                    green_in_regular.setVisibility(View.VISIBLE);

                }else {

                    fair_way_hits.setVisibility(View.GONE);
                    gross_putts.setVisibility(View.GONE);
                    green_in_regular.setVisibility(View.GONE);

                    add_stats_icon.setText("\uf0d7");
                    add_stats_icon.setTypeface(webfont);
                    add_stats_text.setText("Add your stats");
                    stats_value=0;
                }
                return false;
            }
        });

        gross_Score_btn_back = findViewById(R.id.gross_Score_btn_back);
        gross_Score_btn_back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent=new Intent();
                setResult(RESULT_CANCELED,intent);
                intent.putExtra("MESSAGE","-1");

                finish();//finishing activity
                return false;
            }
        });

        TextView gross_Score_course_name = findViewById(R.id.gross_Score_course_name);
        gross_Score_course_name.setTypeface(regular);
        gross_Score_course_name.setText(get_auth_token("Event_name"));

        score.clear();
        for (int i=1;i<=26;i++){
            score.put(i+"",i+"");
        }

        gridview.setAdapter(new GridAdapter(Grass_score_Update_from_Map.this, score, par_position));
        setGridViewHeightBasedOnChildren(gridview,3);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final LinearLayout linear_row = view.findViewById(R.id.linear_row);
                LinearLayout BackSelectedItem = (LinearLayout) gridview.getChildAt(backposition);

                if (position<8) {
                    // TODO Auto-generated method stub
                    linear_row.setBackgroundResource(R.drawable.selected_button);
                    if (backposition != position) {
                        if (BackSelectedItem != null) {
                            BackSelectedItem.setSelected(false);
                            BackSelectedItem.setBackgroundResource(R.drawable.new_back);
                        }
                    }
                    backposition = position;

                    tv_selected_score.setVisibility(View.VISIBLE);
                    String text = "<font color=#000000>Selected Score :</font> <font color=#0bb1de>"+(backposition+1)+"</font>";
                    tv_selected_score.setText(Html.fromHtml(text));

                }else {

                    if (BackSelectedItem != null) {
                        BackSelectedItem.setSelected(false);
                        BackSelectedItem.setBackgroundResource(R.drawable.new_back);
                    }

                    Show_more_grid_view_popup();
                }

            }
        });

        try {
            JSONObject objects = new JSONObject();
            objects.put("hole_number",hole_data+"");
            method_to_load_api(objects);
        }catch (Exception e){
        }

    }

    private void Show_more_grid_view_popup() {

        Map<String, String> score2 = new HashMap<>();

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View premium_purchase_view = inflater.inflate(R.layout.more_gross_score, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(premium_purchase_view, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(true);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog_2 = popupDia;
        alertDialog_2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        int poup_posion=0;
        for (int i=9;i<=26;i++){
            score2.put(poup_posion+"",i+"");
            poup_posion++;
        }

        Button popup_select_score = premium_purchase_view.findViewById(R.id.popup_select_score);
        Button popup_cancel = premium_purchase_view.findViewById(R.id.popup_cancel);

        popup_cancel.setTypeface(medium);
        popup_select_score.setTypeface(medium);


        popup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backposition=-1;
                tv_selected_score.setVisibility(View.GONE);
                alertDialog_2.dismiss();
            }
        });

        popup_select_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backposition==-1){
                    Toast.makeText(getApplicationContext(),"Please select gross score",Toast.LENGTH_LONG).show();
                }else {

                    backposition = backposition+8;
                    alertDialog_2.dismiss();

                    tv_selected_score.setVisibility(View.VISIBLE);
                    String text = "<font color=#000000>Selected Score :</font> <font color=#0bb1de>"+(backposition+1)+"</font>";
                    tv_selected_score.setText(Html.fromHtml(text));

                }
            }
        });

        final GridView gross_score_poup_gridview = premium_purchase_view.findViewById(R.id.gross_score_poup_gridview);

        gross_score_poup_gridview.setAdapter(new GridAdapter2(Grass_score_Update_from_Map.this, score2, par_position));
        Share_it.setGridViewHeightBasedOnChildren(gross_score_poup_gridview,3);

        gross_score_poup_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                final LinearLayout linear_row = view.findViewById(R.id.linear_row);
                linear_row.setBackgroundResource(R.drawable.selected_button);
                LinearLayout BackSelectedItem = (LinearLayout) gross_score_poup_gridview.getChildAt(backposition);
                if (backposition != position) {
                    if (BackSelectedItem != null) {
                        BackSelectedItem.setSelected(false);
                        BackSelectedItem.setBackgroundResource(R.drawable.new_back);
                    }
                }
                backposition = position;
            }
        });

    }

    public void method_to_load_api(JSONObject objects) {
        try {
            String url = "";
            if (Share_it.get_auth_token("play_type").equals("free") || Share_it.get_auth_token("play_type").equals("paid")){
                url = Splash_screen.url + "hole_info/view_score/"+ get_auth_token("event_id");
            }else if (Share_it.get_auth_token("play_type").equals("event")){
                url =  Splash_screen.url + "hole_info/view_score/event/"+ get_auth_token("event_id");
            }

           ProgressDialog.getInstance().showProgress(this);
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, url, objects,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                ProgressDialog.getInstance().hideProgress();

                                // cross scoring funcanality
                                try {
                                    cross_score_enabled = response.getBoolean("cross_scoring");
                                    cross_score_player_id = response.getString("cross_score_player_id");
                                    if (cross_score_enabled){

                                        btn_cross_scoring_enabled.setVisibility(View.VISIBLE);
                                        et_layout.setVisibility(View.GONE);
                                        info_gross_score.setVisibility(View.GONE);
                                        linear_layout.setVisibility(View.GONE);
                                        crossTxt.setText("Input the gross score for " + response.getString("cross_score_team_name")+":");

                                        btn_cross_scoring_enabled.setText(Html.fromHtml("<font color=#000000><b>CROSS SCORING ENABLED:</b></font><br> You are entering the score for <br><b>"+response.getString("cross_score_team_name")+"<b>"));

                                        if (get_auth_token("cross_score_popup").equals("false")){
                                            show_reminder_popup();
                                        }
                                    }
                                }catch (Exception e){
                                    btn_cross_scoring_enabled.setVisibility(View.GONE);
                                    et_layout.setVisibility(View.GONE);
                                    linear_layout.setVisibility(View.VISIBLE);

                                }

                                if (response.getString("user_type").equals("premium")) {
                                    map_notes_values = response.getString("notes");
                                    if (map_notes_values.equals("null") || map_notes_values.equals(null)) {
                                        map_notes_values="";
                                        btn_add_note.setVisibility(View.VISIBLE);
                                    } else {
                                        et_layout.setVisibility(View.VISIBLE);
                                    }
                                }

                                String gross_score = response.getString("gross_score");
                                String fairway = response.getString("fairway");
                                String green_in_regulation = response.getString("green_in_regulation");
                                String putts = response.getString("putts");

                                if (gross_score.equals("null") || gross_score.equals("") || gross_score.equals(null)){
                                }else {
                                    backposition = Integer.parseInt(gross_score)-1;

                                    if (backposition>9){
                                        tv_selected_score.setVisibility(View.VISIBLE);
                                        String text = "<font color=#000000>Selected Score :</font> <font color=#0bb1de>"+gross_score+"</font>";
                                        tv_selected_score.setText(Html.fromHtml(text));
                                    }else {

                                        LinearLayout BackSelectedItem = (LinearLayout) gridview.getChildAt(backposition);
                                        BackSelectedItem.setSelected(true);
                                        BackSelectedItem.setBackgroundResource(R.drawable.selected_button);

                                    }
                                }

                                if (fairway.equals("left")){
                                    first_row = "left";

                                    tvleft.setBackgroundResource(R.drawable.blue_circle);
                                    tvleft.setImageResource(R.drawable.stats_left_sel);


                                }else if (fairway.equals("center")){
                                    first_row = "center";

                                    tv_center.setBackgroundResource(R.drawable.blue_circle);
                                    tv_center.setImageResource(R.drawable.stats_ok_sel);

                                }else if (fairway.equals("right")){
                                    first_row = "right";

                                    tv_center.setBackgroundResource(R.drawable.blue_circle);
                                    tv_center.setImageResource(R.drawable.stats_right_sel);

                                }

                                if (green_in_regulation.equals("hit")){
                                    second_row = "hit";

                                    two_left.setBackgroundResource(R.drawable.blue_circle);
                                    two_left.setImageResource(R.drawable.stats_left_sel);

                                }else if (green_in_regulation.equals("miss")){
                                    second_row = "miss";

                                    two_right.setBackgroundResource(R.drawable.blue_circle);
                                    two_right.setImageResource(R.drawable.stats_close_sel);

                                }

                                if (putts.equals("0")){
                                    third_row = "0";
                                    two_one.setBackgroundResource(R.drawable.blue_circle);
                                    two_one.setTextColor(getResources().getColor(R.color.colorwhite));
                                }else if (putts.equals("1")){
                                    third_row = "1";
                                    two_two.setBackgroundResource(R.drawable.blue_circle);
                                    two_two.setTextColor(getResources().getColor(R.color.colorwhite));
                                }else if (putts.equals("2")){
                                    third_row = "2";
                                    two_three.setBackgroundResource(R.drawable.blue_circle);
                                    two_three.setTextColor(getResources().getColor(R.color.colorwhite));
                                }else if (putts.equals("3")){
                                    third_row = "3";
                                    two_four.setBackgroundResource(R.drawable.blue_circle);
                                    two_four.setTextColor(getResources().getColor(R.color.colorwhite));
                                }else if (putts.equals("4")){
                                    third_row = "4";
                                    two_five.setBackgroundResource(R.drawable.blue_circle);
                                    two_five.setTextColor(getResources().getColor(R.color.colorwhite));
                                }
                            } catch (Exception E) {
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
                                    Alert_Dailog.showNetworkAlert(Grass_score_Update_from_Map.this);
                                } else {
                                    Toast.makeText(Grass_score_Update_from_Map.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Grass_score_Update_from_Map.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("auth-token", Share_it.get_auth_token("auth_token"));
                    return headers;
                }
            };
            int socketTimeout = 30000; // 30 seconds. You can change it
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            myRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(myRequest, "tag");
        } catch (Exception e) {
            //nothing
        }
    }


    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.save_grsoss_info:

               if (backposition==-1){
                   Toast.makeText(getApplicationContext(),"Please select gross score",Toast.LENGTH_LONG).show();
               }else{
                try {

                    com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().showProgress(this);
                    String hand_hadicap_selecteds = Share_it.get_auth_token("handicap_score");
                    netscorecalculation = new JSONObject();
                    netscorecalculation.put("hole_number", hole_data);
                    netscorecalculation.put("handicap", hand_hadicap_selecteds);
                    netscorecalculation.put("gross_score", score.get(backposition + 1 + ""));
                    netscorecalculation.put("fairway_hit",first_row);
                    netscorecalculation.put("green_in_regulation", second_row);
                    netscorecalculation.put("putts", third_row);
                    netscorecalculation.put("notes",map_notes_values);

                    if (!isNetworkAvailable()) {
                        Toast.makeText(Grass_score_Update_from_Map.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }else {
                        if (cross_score_enabled) {
                            method_to_call_cross_score(netscorecalculation,cross_score_player_id);
                        }else {
                            methodtocallnetscoreApi(netscorecalculation);
                        }
                    }
                } catch (Exception e) {
                    //nothing
                }

            }
                break;


            case R.id.tvleft:

                first_row = "left";

                tvleft.setBackgroundResource(R.drawable.blue_circle);
                tvleft.setImageResource(R.drawable.stats_left_sel);

                tv_center.setBackgroundResource(R.drawable.gross_score_selecter);
                tv_center.setImageResource(R.drawable.stats_ok);

                tv_right.setBackgroundResource(R.drawable.gross_score_selecter);
                tv_right.setImageResource(R.drawable.stats_right);

                break;


            case R.id.tv_center:

                first_row = "center";

                tvleft.setBackgroundResource(R.drawable.gross_score_selecter);
                tvleft.setImageResource(R.drawable.stats_left);

                tv_center.setBackgroundResource(R.drawable.blue_circle);
                tv_center.setImageResource(R.drawable.stats_ok_sel);

                tv_right.setBackgroundResource(R.drawable.gross_score_selecter);
                tv_right.setImageResource(R.drawable.stats_right);

                break;


            case R.id.tv_right:

                first_row = "right";

                tvleft.setBackgroundResource(R.drawable.gross_score_selecter);
                tvleft.setImageResource(R.drawable.stats_left);

                tv_center.setBackgroundResource(R.drawable.gross_score_selecter);
                tv_center.setImageResource(R.drawable.stats_ok);

                tv_right.setBackgroundResource(R.drawable.blue_circle);
                tv_right.setImageResource(R.drawable.stats_right_sel);

                break;


            case R.id.two_left:

                second_row = "hit";

                two_left.setBackgroundResource(R.drawable.blue_circle);
                two_left.setImageResource(R.drawable.stats_left_sel);

                two_right.setBackgroundResource(R.drawable.gross_score_selecter);
                two_right.setImageResource(R.drawable.stats_close);
                break;


            case R.id.two_right:

                second_row = "miss";

                two_left.setBackgroundResource(R.drawable.gross_score_selecter);
                two_left.setImageResource(R.drawable.stats_left);

                two_right.setBackgroundResource(R.drawable.blue_circle);
                two_right.setImageResource(R.drawable.stats_close_sel);

                break;

            case R.id.two_one:

                third_row = "0";

                two_one.setBackgroundResource(R.drawable.blue_circle);
                two_one.setTextColor(getResources().getColor(R.color.colorwhite));

                two_two.setBackgroundResource(R.drawable.gross_score_selecter);
                two_two.setTextColor(getResources().getColor(R.color.colorblack));

                two_three.setBackgroundResource(R.drawable.gross_score_selecter);
                two_three.setTextColor(getResources().getColor(R.color.colorblack));

                two_four.setBackgroundResource(R.drawable.gross_score_selecter);
                two_four.setTextColor(getResources().getColor(R.color.colorblack));

                two_five.setBackgroundResource(R.drawable.gross_score_selecter);
                two_five.setTextColor(getResources().getColor(R.color.colorblack));

                break;


            case R.id.two_two:

                third_row = "1";

                two_one.setBackgroundResource(R.drawable.gross_score_selecter);
                two_one.setTextColor(getResources().getColor(R.color.colorblack));

                two_two.setBackgroundResource(R.drawable.blue_circle);
                two_two.setTextColor(getResources().getColor(R.color.colorwhite));

                two_three.setBackgroundResource(R.drawable.gross_score_selecter);
                two_three.setTextColor(getResources().getColor(R.color.colorblack));

                two_four.setBackgroundResource(R.drawable.gross_score_selecter);
                two_four.setTextColor(getResources().getColor(R.color.colorblack));

                two_five.setBackgroundResource(R.drawable.gross_score_selecter);
                two_five.setTextColor(getResources().getColor(R.color.colorblack));

                break;


            case R.id.two_three:

                third_row = "2";

                two_one.setBackgroundResource(R.drawable.gross_score_selecter);
                two_one.setTextColor(getResources().getColor(R.color.colorblack));

                two_two.setBackgroundResource(R.drawable.gross_score_selecter);
                two_two.setTextColor(getResources().getColor(R.color.colorblack));

                two_three.setBackgroundResource(R.drawable.blue_circle);
                two_three.setTextColor(getResources().getColor(R.color.colorwhite));

                two_four.setBackgroundResource(R.drawable.gross_score_selecter);
                two_four.setTextColor(getResources().getColor(R.color.colorblack));

                two_five.setBackgroundResource(R.drawable.gross_score_selecter);
                two_five.setTextColor(getResources().getColor(R.color.colorblack));

                break;


            case R.id.two_four:

                third_row = "3";

                two_one.setBackgroundResource(R.drawable.gross_score_selecter);
                two_one.setTextColor(getResources().getColor(R.color.colorblack));

                two_two.setBackgroundResource(R.drawable.gross_score_selecter);
                two_two.setTextColor(getResources().getColor(R.color.colorblack));

                two_three.setBackgroundResource(R.drawable.gross_score_selecter);
                two_three.setTextColor(getResources().getColor(R.color.colorblack));

                two_four.setBackgroundResource(R.drawable.blue_circle);
                two_four.setTextColor(getResources().getColor(R.color.colorwhite));

                two_five.setBackgroundResource(R.drawable.gross_score_selecter);
                two_five.setTextColor(getResources().getColor(R.color.colorblack));

                break;


            case R.id.two_five:

                third_row = "4";

                two_one.setBackgroundResource(R.drawable.gross_score_selecter);
                two_one.setTextColor(getResources().getColor(R.color.colorblack));

                two_two.setBackgroundResource(R.drawable.gross_score_selecter);
                two_two.setTextColor(getResources().getColor(R.color.colorblack));

                two_three.setBackgroundResource(R.drawable.gross_score_selecter);
                two_three.setTextColor(getResources().getColor(R.color.colorblack));

                two_four.setBackgroundResource(R.drawable.gross_score_selecter);
                two_four.setTextColor(getResources().getColor(R.color.colorblack));

                two_five.setBackgroundResource(R.drawable.blue_circle);
                two_five.setTextColor(getResources().getColor(R.color.colorwhite));

                break;

            // delete note
            case R.id.btn_delete_note:

                Delete_note_method();

                break;

            // Edit Note
            case R.id.btn_edit_note:
                Show_add_note();
                break;

            // add new note
            case R.id.btn_add_note:

                Show_add_note();

                break;

        }

    }


    private void method_to_call_cross_score(JSONObject data,String player_id) {

        String  url_value = "hole_info/cross_score/gross_score/" + get_auth_token("event_id")+"/"+player_id;

        ProgressDialog.getInstance().showProgress(this);
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST, Splash_screen.url + url_value, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            ProgressDialog.getInstance().hideProgress();
                            Toast.makeText(getApplicationContext(), "Score Updated Successfully", Toast.LENGTH_SHORT).show();

                            View_course_map.hole_number_from_all_corses = Integer.parseInt(hole_data)+1;
                            finish();

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
                                Alert_Dailog.showNetworkAlert(Grass_score_Update_from_Map.this);
                            } else {
                                Toast.makeText(Grass_score_Update_from_Map.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Grass_score_Update_from_Map.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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



    private void Delete_note_method() {
        String url = "";
        if (Share_it.get_auth_token("play_type").equals("free") || Share_it.get_auth_token("play_type").equals("paid")){
            url = Splash_screen.url + "hole_info/player/delete-note/"+ get_auth_token("event_id");
        }else if (Share_it.get_auth_token("play_type").equals("event")){
            url =  Splash_screen.url + "hole_info/delete-note/"+ get_auth_token("event_id");
        }
        JSONObject object = new JSONObject();
        try {
            object.put("hole_number",hole_data);
        }catch (Exception e){ }

        com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().showProgress(this);

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                            if (response.getString("status").equals("Success")){
                                map_notes_values="";
                                btn_add_note.setVisibility(View.VISIBLE);
                                et_layout.setVisibility(View.GONE);
                            }
                            Toast.makeText(Grass_score_Update_from_Map.this,response.getString("message"),Toast.LENGTH_LONG).show();
                        } catch (Exception E) {
                            //nothing
                            com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(Grass_score_Update_from_Map.this);
                            } else {
                                Toast.makeText(Grass_score_Update_from_Map.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Grass_score_Update_from_Map.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

    private void Show_add_note() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View premium_purchase_view = inflater.inflate(R.layout.layout_add_notes, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(premium_purchase_view, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(true);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog_2 = popupDia;
        alertDialog_2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tv_notes = premium_purchase_view.findViewById(R.id.tv_notes);
        tv_notes.setTypeface(medium);

        TextView tv_hole_par = premium_purchase_view.findViewById(R.id.tv_hole_par);
        tv_hole_par.setTypeface(medium);
        tv_hole_par.setText("Hole "+hole_data+" - "+"Par "+par_data_gr);

        final EditText et_add_note = premium_purchase_view.findViewById(R.id.et_add_note);
        et_add_note.setTypeface(regular);
        et_add_note.setText(map_notes_values+"");

        Button btn_save_notes = premium_purchase_view.findViewById(R.id.btn_save_notes);
        Button btn_note_cancel = premium_purchase_view.findViewById(R.id.btn_note_cancel);

        btn_note_cancel.setTypeface(medium);
        btn_save_notes.setTypeface(medium);


        btn_note_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog_2.dismiss();
            }
        });

        btn_save_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_add_note.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please add note",Toast.LENGTH_LONG).show();
                }else {
                    map_notes_values = et_add_note.getText().toString();
                    alertDialog_2.dismiss();

                }

            }
        });
    }


    public void alertdailogbox(final String value){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
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
                if(value.equals("update_score"))
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("update_score");
                    } else {
                        methodtocallnetscoreApi(netscorecalculation);
                    }

                }

            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    public void methodtocallnetscoreApi(JSONObject data) {

        System.out.println("--------object-------"+data);

        String url_value="";
        if (Share_it.get_auth_token("play_type").equals("free") || Share_it.get_auth_token("play_type").equals("paid")){
            url_value = "hole_info/player/gross_score/"+ Share_it.get_auth_token("event_id");
        }else if (Share_it.get_auth_token("play_type").equals("event")){
            url_value = "hole_info/gross_score/" + Share_it.get_auth_token("event_id");
        }

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST, Splash_screen.url +url_value, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getString("status").equals("Success")) {
                                com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                                View_course_map.hole_number_from_all_corses = Integer.parseInt(hole_data)+1;

                                finish();
                                Toast.makeText(getApplicationContext(), "Score Updated Successfully", Toast.LENGTH_SHORT).show();

                            } else {

                                com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();

                                Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            //nothing
                            com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
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
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(getApplicationContext());
                            } else {
                                Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
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


    private final class GridAdapter extends BaseAdapter {

        Map<String, String> items = new HashMap<String, String>();
        Map<String, String> itemss = new HashMap<String, String>();

        /**
         * Default constructor
         *
         * @param items to fill data to
         */
        private GridAdapter(Context context, Map<String, String> items, Map<String, String> itemss) {
            this.items = items;
            this.itemss = itemss;
        }

        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(final int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shamble_gross_score, parent, false);
            }

            final TextView text = view.findViewById(R.id.text1);
            final TextView text2 = view.findViewById(R.id.text2);
            LinearLayout linearLayout = view.findViewById(R.id.linear_row);
            ImageView bottom_button = view.findViewById(R.id.bottom_button);

            if (position==8) {

                bottom_button.setVisibility(View.VISIBLE);
                text.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                linearLayout.setBackgroundResource(R.color.colorwhite);
//                linearLayout.addView(addButton());
            }else {

                text.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);
                bottom_button.setVisibility(View.GONE);

                text.setText(items.get(position + 1 + "") + "");
                String valuess = itemss.get(position + 1 + "");

                text.setTypeface(italic);
                text2.setTypeface(regular);

                if (valuess == null) {
                    text2.setText("");
                } else {
                    text2.setText(valuess);
                }
            }

                return view;
        }
    }

    private final class GridAdapter2 extends BaseAdapter {
        Map<String, String> items = new HashMap<String, String>();
        Map<String, String> itemss = new HashMap<String, String>();
        int row_postion = 9;
        /**
         * Default constructor
         *
         * @param items to fill data to
         */
        private GridAdapter2(Context context, Map<String, String> items, Map<String, String> itemss) {
            this.items = items;
            this.itemss = itemss;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(final int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shamble_gross_score, parent, false);
            }

            final TextView text = view.findViewById(R.id.text1);
            final TextView text2 = view.findViewById(R.id.text2);

            text.setVisibility(View.VISIBLE);
            text2.setVisibility(View.VISIBLE);

                text.setText(items.get(position+"") + "");
                String valuess = itemss.get(row_postion+"");

                text.setTypeface(italic);
                text2.setTypeface(regular);

                if (valuess == null) {
                    text2.setText("");
                } else {
                    text2.setText(valuess);
                }

            return view;
        }
    }

    private void show_reminder_popup() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dialogview = inflater.inflate(R.layout.rimnder_popup, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;

        TextView txt_reminder_title = alertDialog.findViewById(R.id.txt_reminder_title);
        TextView reminder_opponent_description = alertDialog.findViewById(R.id.reminder_opponent_description);

        txt_reminder_title.setTypeface(semibold);
        reminder_opponent_description.setTypeface(regular);

        ImageView img_cross = alertDialog.findViewById(R.id.img_cross);

        txt_reminder_title.setText(Html.fromHtml("</font> <font color=#FD2726> REMINDER</font><br>Cross Scoring Enabled"));
        Button enable= alertDialog.findViewById(R.id.btn_reminder_countinue);
        enable.setTypeface(medium);

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                savestring("cross_score_popup","true");
            }
        });

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}
