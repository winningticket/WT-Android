package com.winningticketproject.in.IndividulaGameType;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.BestBallFlow.BestBall_Scorecard;
import com.winningticketproject.in.BestBallFlow.Bestbal_Leader_Board;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.EventTab.Score_board_new;
import com.winningticketproject.in.Live_Score_New_Flow.View_course_map;
import com.winningticketproject.in.NewMapEvenFLOW.NormalLeaderoard;
import com.winningticketproject.in.NewMapEvenFLOW.NormalScorreboard;
import com.winningticketproject.in.R;
import com.winningticketproject.in.Shamble_flow.Shamble_Player_info;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.Stable_modify_stroke_Flow.StableFord_Score_Card;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_ford_Leaderboard;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_modify_ScoreCard;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_modify_leader_board;
import com.winningticketproject.in.TeeHandicapHole.Hole_Tee_Handicap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.remove_key;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;
import static com.winningticketproject.in.EventTab.NewEventDetailPage.location;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

public class Location_Services extends AppCompatActivity implements View.OnClickListener {
    TextView cancel_purchage;
    View dialogview;
    android.support.v7.app.AlertDialog alertDialog;
    LinearLayout scramble_type;
    public static String  game_name="";
    public static String game_type;
    public static String address1;
    public static String description;
    public static String team_name="";
    public static String id,event_type="",event_id;
    public static ArrayList<Shamble_Player_info> shamble_player_infos = new ArrayList<>();
    public static String selected_tee,selected_hole,selected_handicap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_scorecard);

        scramble_type = findViewById(R.id.scramble_type);

        cancel_purchage = findViewById(R.id.cancel_purchage);
        cancel_purchage.setOnClickListener(this);

        Intent in = getIntent();
        id = in.getStringExtra("id");
        event_type = in.getStringExtra("event_type");
        event_id = get_auth_token(id);

        Handicap_hole_tee_api_Integration(id);

        methotocal_hole_info();
    }

    private void Game_type_scramble() {

        scramble_type.setVisibility(View.VISIBLE);

        TextView tv_scoring_gps = findViewById(R.id.tv_scoring_gps);
        tv_scoring_gps.setTypeface(semibold);

        TextView tv_score_card_leaderboard = findViewById(R.id.tv_score_card_leaderboard);
        tv_score_card_leaderboard.setTypeface(regular);

        TextView tv_event_name = findViewById(R.id.tv_event_name);
        tv_event_name.setTypeface(medium);


        TextView tv_event_values = findViewById(R.id.tv_event_values);
        tv_event_values.setTypeface(regular);

        TextView tv_event_location = findViewById(R.id.tv_event_location);
        tv_event_location.setTypeface(medium);

        TextView tv_event_location_values = findViewById(R.id.tv_event_location_values);
        tv_event_location_values.setTypeface(regular);

        TextView tv_event_type = findViewById(R.id.tv_event_type);
        tv_event_type.setTypeface(medium);

        TextView tv_event_type_values = findViewById(R.id.tv_event_type_values);
        tv_event_type_values.setTypeface(regular);

        TextView tv_event_decript = findViewById(R.id.tv_event_decript);
        tv_event_decript.setTypeface(medium);

        TextView tv_event_dcpt_values = findViewById(R.id.tv_event_dcpt_values);
        tv_event_dcpt_values.setTypeface(regular);

        Button btn_start_play = findViewById(R.id.btn_start_play);
        btn_start_play.setTypeface(medium);
        btn_start_play.setOnClickListener(this);

        //set values
        tv_event_values.setText(get_auth_token("Event_name"));
        tv_event_location_values.setText(address1);
        tv_event_type_values.setText(game_type);
        tv_event_dcpt_values.setText(description);

    }

    private void start_playing() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Location_Services.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) Location_Services.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogview = inflater.inflate(R.layout.location_services, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;
        TextView location_services = alertDialog.findViewById(R.id.location_services);
        TextView nearby_courses = alertDialog.findViewById(R.id.nearby_courses);
        location_services.setTypeface(semibold);
        nearby_courses.setTypeface(regular);
        Button enable= alertDialog.findViewById(R.id.enable);
        Button not_now= alertDialog.findViewById(R.id.not_now);
        enable.setTypeface(regular);
        not_now.setTypeface(regular);

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent1);
            }
        });
        not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void Handicap_hole_tee_api_Integration(String event_id) {

        String update_API="api/v2/hole_info/course-info/" + event_id;

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.imageurl+update_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            ArrayList<String> tee_selected = new ArrayList<>();
                            // tee array value get
                            JSONArray tee_list = response.getJSONArray("tee_list");
                            for (int i=0;i<tee_list.length();i++){
                                JSONObject jsonObject = tee_list.getJSONObject(i);
                                String selected_tee_yards = jsonObject.getString("yards_total")+" yds ("+jsonObject.getString("rating")+"/"+jsonObject.getString("slope")+")";
                                tee_selected.add(selected_tee_yards);
                            }

                            String course_data = "<b>"+get_auth_token("event_name")+"</b>"+"<br>Starting "+ "Hole "+get_auth_token("starting_hole")+"<b>" + " - "+ "</b>"+ get_auth_token("selected_tee_postion")+" "+tee_selected.get(0)+"<br>"+get_auth_token("Game_type");

                            savestring("leader_board_map_course",course_data);
                            savestring("yards_value",tee_selected.get(0));


                        } catch (Exception E) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) { }
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
        //nothing
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_play:

                savestring("first_time","yes");
                savestring("play_type","event");
                savestring("Game_type",game_name);

                if (isLocationEnabled(Location_Services.this)){
                    savestring("selected_hole_postion",selected_hole);
                    savestring("handicap_score",selected_handicap);
                    savestring("selected_tee_postion",selected_tee);

                    if (selected_tee == null || selected_tee.equals(null) || selected_tee.equals("null") || selected_tee.equals("")) {
                        Intent intent1 = new Intent(Location_Services.this, Hole_Tee_Handicap.class);
                        startActivity(intent1);
                        finish();
                    }else if (selected_hole == null || selected_hole.equals(null) || selected_hole.equals("null") || selected_hole.equals("")){
                        Intent intent1 = new Intent(Location_Services.this, Hole_Tee_Handicap.class);
                        startActivity(intent1);
                        finish();
                    }else {

                        if (get_auth_token("page_type").equals("score_card")){

                            if (get_auth_token("Game_type").equals("shamble") || get_auth_token("Game_type").equals("bestball")) {
                                game_name="bestball";
                                Intent intent1 = new Intent(this, BestBall_Scorecard.class);
                                intent1.putExtra("home","home");
                                startActivity(intent1);
                                finish();
                            } else if (get_auth_token("Game_type").equals("stableford")) {
                                Intent intent1 = new Intent(this, StableFord_Score_Card.class);
                                intent1.putExtra("home","home");
                                startActivity(intent1);
                                finish();
                            } else if (get_auth_token("Game_type").equals("modifiedstableford")) {
                                Intent intent1 = new Intent(this, Stable_modify_ScoreCard.class);
                                intent1.putExtra("home","home");
                                startActivity(intent1);
                                finish();
                            } else {
                                Intent intent1 = new Intent(this, NormalScorreboard.class);
                                intent1.putExtra("home","home");
                                startActivity(intent1);
                                finish();
                            }

                        }else if (get_auth_token("page_type").equals("leaderboard")){

                            if (get_auth_token("Game_type").equals("shamble") || get_auth_token("Game_type").equals("bestball")) {
                                Intent intent1 = new Intent(this, Bestbal_Leader_Board.class);
                                intent1.putExtra("home","home");
                                startActivity(intent1);
                                finish();
                            } else if (get_auth_token("Game_type").equals("stableford")) {
                                Intent intent1 = new Intent(this, Stable_ford_Leaderboard.class);
                                intent1.putExtra("home","home");
                                startActivity(intent1);
                                finish();
                            } else if (get_auth_token("Game_type").equals("modifiedstableford")) {
                                Intent intent1 = new Intent(this, Stable_modify_leader_board.class);
                                intent1.putExtra("home","home");
                                startActivity(intent1);
                                finish();
                            } else {
                                Intent intent1 = new Intent(this, NormalLeaderoard.class);
                                intent1.putExtra("home","home");
                                startActivity(intent1);
                                finish();
                            }
                        }else {
                            Intent in = new Intent(this,View_course_map.class);
                            in.putExtra("home","");
                            startActivity(in);
                            finish();
                        }
                    }
                }else{
                    start_playing();
                }

                break;

            case R.id.cancel_purchage:
                finish();
                break;
        }
    }


    //is live or not api
     private void methotocal_hole_info(){
        ProgressDialog.getInstance().showProgress(Location_Services.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST, Splash_screen.url+"hole_info/check_hole_info/"+id,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getString("status").equals("Success")){

                                System.out.println("--------------"+response);

                                ProgressDialog.getInstance().hideProgress();

                                JSONObject object = response.getJSONObject("game_details");

                                Location_Services.game_name = object.getString("game_name");
                                Location_Services.game_type = object.getString("game_type");

                                savestring("Game_type",object.getString("game_name"));
                                savestring("play_type","event");

                                selected_tee = response.getString("selected_tee");
                                selected_hole = response.getString("selected_hole");
                                selected_handicap = response.getString("selected_handicap");

                                savestring("handicap",response.getString("selected_handicap"));
                                savestring("selected_tee_postion",response.getString("selected_tee"));
                                savestring("selected_hole_postion",response.getString("selected_hole"));


                                try {
                                    try {
                                        try {
                                            String handicap = response.getString("handicap");
                                            if (handicap.equals("null") || handicap.equals(null) || handicap.equals("")){
                                                handicap = "0";
                                            }
                                            Share_it.savestring("handicap_score",handicap);
                                            Game_type_scramble();
                                        }catch (Exception e){ }

                                    }catch (Exception e){

                                    }

                                    try {
                                        Share_it.savestring("handicap_score",response.getString("handicap"));
                                    }catch (Exception e){
                                        Share_it.savestring("handicap_score","0");
                                    }

                                    JSONObject game_details = response.getJSONObject("game_details");
                                    if (game_details!=null){
                                        game_name = game_details.getString("game_name");
                                        game_type = game_details.getString("game_type");
                                        address1 = game_details.getString("address1")+",\n"+game_details.getString("city")+", "+game_details.getString("state")+", "+game_details.getString("zip_code");
                                        description = game_details.getString("description");

                                        if (game_name.equals("scramble") || game_name.equals("shamble") ){
                                            Share_it.savestring("handicap_score","diseble");
                                            Game_type_scramble();
                                            team_name = response.getString("team_name");

                                        }else if (game_name.equals("bestball")){
                                            if (Share_it.get_auth_token("handicap_score").equals("diseble")){
                                                Share_it.savestring("handicap_score","0");
                                            }
                                            Game_type_scramble();
                                            team_name = response.getString("team_name");

                                        } else if (game_name.equals("stableford") || game_name.equals("strokeplay") || game_name.equals("modifiedstableford")){
                                            if (Share_it.get_auth_token("handicap_score").equals("diseble")){
                                                Share_it.savestring("handicap_score","0");
                                            }
                                            Game_type_scramble();
                                            team_name = "";

                                        }else {

                                            Game_type_scramble();

                                        }

                                        if (game_name.equals("scramble") || game_name.equals("shamble") || game_name.equals("bestball") ){

                                            String player_nanes="";
                                            JSONArray array = response.getJSONArray("player_names");
                                            for (int i = 0; i < array.length(); i++) {
                                                player_nanes+= array.getString(i)+" - ";
                                            }

                                            shamble_player_infos.clear();

                                            JSONObject player_info = response.getJSONObject("player_info");
                                            Iterator keys = player_info.keys();
                                            while(keys.hasNext()) {
                                                // loop to get the dynamic key
                                                String id_values = (String) keys.next();
                                                shamble_player_infos.add(new Shamble_Player_info(id_values,player_info.getString(id_values)));
                                            }

                                        }
                                    }else {
                                        Game_type_scramble();
                                    }

                                }catch (Exception e){
                                    Game_type_scramble();
                                }
                            }else {
                                ProgressDialog.getInstance().hideProgress();
                                Toast.makeText(Location_Services.this,"There is no course information available for this event",Toast.LENGTH_LONG).show();
                            }
                            ProgressDialog.getInstance().hideProgress();

                        }catch (Exception e){
                            //nothing
                        }

                        ProgressDialog.getInstance().hideProgress();
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
                                Alert_Dailog.showNetworkAlert(Location_Services.this);
                            }else {

                                ProgressDialog.getInstance().hideProgress();
                            }
                        }else {
                            ProgressDialog.getInstance().hideProgress();
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


    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }

    }

}

