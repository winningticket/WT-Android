package com.winningticketproject.in.Shamble_flow;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.EventTab.Score_board_new;
import com.winningticketproject.in.Live_Score_New_Flow.View_course_map;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.BestBallFlow.Bestball_Player_list;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class Shamble_score_card extends Fragment {
    View dialogview, convertView;
    String auth_token = "";
    String selected_hole_postion="";
    String row_three_postion;
    String selected_gross_score="0";
    String response_postions="";
    int postio_select=0;
    int hole_selected_position=0;
    int total_holes=0;
    int progress=0;
    RecyclerView recycler_view;
    TextView   tvteam, tvtotal_score, tvholes, tvdistance, tvgrassscore, tvnetscore, tvhandicap,tvpar;
    ProgressDialog pd;
    public static  String player_name="",scores_counted;
    public static  String event_id;
    public Map<String, String> data1 = new HashMap<String, String>();
    public Map<String, String> data2 = new HashMap<String, String>();
    public Map<String, String> data3 = new HashMap<String, String>();
    public Map<String, String> data4 = new HashMap<String, String>();
    public Map<String, String> data5 = new HashMap<String, String>();
    ArrayList<String> hole_postionss = new ArrayList<>();
    public Map<String, String> is_expand = new HashMap<String, String>();
    Livescorecard horizontalAdapter;
    JSONObject netscorecalculation;
    public HashMap <String,ArrayList<Bestball_Player_list>> No_of_player = new HashMap<>();
    public Shamble_score_card() {
        // Required empty public constructor
    }

    TextView tv_tee_hole_info;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        convertView = inflater.inflate(R.layout.shamble_score_card, container, false);

//        tv_pull_down = convertView.findViewById(R.id.tv_pull_down);
//        tv_pull_down.setTypeface(regular);
//        tv_pull_down.postDelayed(new Runnable() {
//            public void run() {
//                tv_pull_down.setVisibility(View.GONE);
//            }
//        }, 5000);
        auth_token = get_auth_token("auth_token");

        event_id = get_auth_token("event_id");
        selected_hole_postion = get_auth_token("selected_hole_postion");
        savestring("un_select","-2");

        tvtotal_score = convertView.findViewById(R.id.tvtotal_score);
        tvholes = convertView.findViewById(R.id.tvholes);
        tvdistance = convertView.findViewById(R.id.tvdistance);
        tvgrassscore = convertView.findViewById(R.id.tvgrassscore);
        tvnetscore = convertView.findViewById(R.id.tvnetscore);
        tvhandicap = convertView.findViewById(R.id.tvhandicap);

        tvpar = convertView .findViewById(R.id.tvpar);

        tvteam = convertView.findViewById(R.id.team_name_edit);
        tvteam.setTypeface(italic);

        tvtotal_score.setTypeface(medium);
        tvholes.setTypeface(medium);
        tvdistance.setTypeface(medium);
        tvgrassscore.setTypeface(medium);
        tvnetscore.setTypeface(medium);
        tvhandicap.setTypeface(medium);
        tvpar.setTypeface(medium);

        tv_tee_hole_info = convertView.findViewById(R.id.tv_tee_hole_info);
        tv_tee_hole_info.setTypeface(regular);

        if (!isNetworkAvailable()) {
            alertdailogbox("scoreboardapi");
        }else {
            methodforscorecard();
        }

        tvteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Team_edit_popup();

            }
        });

        return convertView;
    }

    private void Team_edit_popup() {

        final android.support.v7.app.AlertDialog alertDialog;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
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

        et_team_name.setText(tvteam.getText().toString());
        pop_up_title.setTypeface(medium);
        continue_button.setTypeface(medium);
        et_team_name.setTypeface(regular);

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isNetworkAvailable()) {
                    alertdailogbox("scoreboardapi");
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
                                tvteam.setText(team_names);
                                Toast.makeText(getActivity(),""+response.getString("message"),Toast.LENGTH_LONG).show();
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
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                                Alert_Dailog.showNetworkAlert(getActivity());
                            } else {

                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                                Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                            Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();

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

    public void alertdailogbox(final String value){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("No Internet");
        builder.setMessage("Internet is required. Please Retry.");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                if(value.equals("scoreboardapi")) {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("scoreboardapi");
                    } else {
                        methodforscorecard();
                    }
                }
                else if(value.equals("update_score"))
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("update_score");
                    } else {
                        methodtocallnetscoreApi(netscorecalculation, row_three_postion,"");
                    }

                }

            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    public void methodforscorecard() {
        String url_value="api/v2/hole_info/select-course-info/" + event_id;
        try {
            recycler_view = convertView.findViewById(R.id.recycler_view);
            data1 = new HashMap<String, String>();
            data2 = new HashMap<String, String>();
            data3 = new HashMap<String, String>();
            data4 = new HashMap<String, String>();
            data5 = new HashMap<String, String>();
            is_expand = new HashMap<String, String>();

            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            if (progress==0){ pd.show(); }

            JSONObject params = new JSONObject();
            try{
                params.put("handicap",get_auth_token("handicap"));
                params.put("selected_tee",get_auth_token("selected_tee_postion"));
                params.put("selected_hole",get_auth_token("selected_hole_postion"));
            }catch (Exception e){ }

            System.out.println("--------parm---"+params);
            JsonObjectRequest myRequest_handicap = new JsonObjectRequest(
                    Request.Method.POST, Splash_screen.imageurl +url_value , params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                if (pd.isShowing()) { pd.dismiss(); }

                                System.out.println("--------response-----"+response);
                                if (response.getString("status").equals("Success")) {
                                    try {

                                        Score_board_new.game_completed =   response.getString("completed");

                                        player_name = response.getString("name").replaceAll("\\s+"," ").substring(0,1).toUpperCase() +response.getString("name").substring(1);

                                        scores_counted = response.getString("scores_counted");

                                        tvteam.setText(response.getString("team_name"));

                                        String net_score = response.getString("total_score");
                                        if(net_score==null || net_score.equals("") || net_score.equals("null") || net_score.equals(null)){
                                            net_score = "0";
                                        }

                                        int to_par = Integer.parseInt(net_score);
                                        String game_type="";
                                        game_type = "<b>" + "Score: "+ "</b>"+response.getString("total_net_score")+"/"+(Integer.parseInt(response.getString("total_par_value")) * Integer.parseInt(scores_counted));
                                        tvtotal_score.setText(Html.fromHtml("<b><big>"+to_par+"</big></b>"+"<br> SCORE<br>"+"<small>TO PAR X"+scores_counted+"</small>"));

                                        tv_tee_hole_info.setText(Html.fromHtml("Starting "+ "<b>Hole "+response.getString("starting_hole")+"</b><br><b>Tee </b>"+ get_auth_token("selected_tee_postion")+"<br>"+game_type+"<br>"+"Handicap <b>"+get_auth_token("handicap")+"<b>"));

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

                                            hole_selected_position = Integer.parseInt(stating_hole);
                                            View_course_map.hole_number_from_all_corses  = hole_selected_position;

                                        }else {
                                            hole_selected_position = Integer.parseInt(com_hole);

                                            if (total_holes==9){
                                                if (hole_selected_position>3 && hole_selected_position<9){
                                                    hole_selected_position = hole_selected_position+2;
                                                    View_course_map.hole_number_from_all_corses  = hole_selected_position-1;
                                                }else if (hole_selected_position==9){
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
                                        hole_postionss.add(hole_postion);

                                        is_expand.put(hole_postion,"false");


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

                                        if (par.equals("null") || par.equals("") || par.equals(null))
                                        {
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


                                        // No of players who updated gross score
                                        ArrayList<Bestball_Player_list> shamble_score_cards = new ArrayList<>();
                                        JSONArray player_details = hole_object.getJSONArray("player_details");
                                        for (int j=0;j<player_details.length();j++){

                                            JSONObject player_details_obj = player_details.getJSONObject(j);
                                            String player_name = player_details_obj.getString("player_name");
                                            String player_gross_score = player_details_obj.getString("player_gross_score");
                                            String selecte_unselect="";

                                            String player_id = player_details_obj.getString("player_id");

                                            if (player_gross_score.equals(null) || player_gross_score.equals("null") || player_gross_score.equals("")){
                                                player_gross_score="";
                                            }else {
                                                selecte_unselect = player_details_obj.getString("selected");
                                                if (selecte_unselect.equals("no")){
                                                    selecte_unselect="";
                                                }else {
                                                    selecte_unselect="Selected";
                                                }
                                            }
                                            shamble_score_cards.add(new Bestball_Player_list(player_name,player_gross_score,selecte_unselect,player_id,""));
                                        }
                                        No_of_player.put(hole_postion+"",shamble_score_cards);
                                    }


                                    hole_postionss.add("0");
                                    data1.put(hole_info.length()+1+"", "0");
                                    data2.put(hole_info.length()+1+"", "0");
                                    data3.put(hole_info.length()+1+"", "0");
                                    data4.put(hole_info.length()+1+"", "0");
                                    data5.put(hole_info.length()+1+"", "0");
                                    is_expand.put(hole_info.length()+1+"","false");

                                    ArrayList<Bestball_Player_list> main_shamble_score_cards = new ArrayList<>();
                                    main_shamble_score_cards.add(new Bestball_Player_list("","","","",""));
                                    No_of_player.put(hole_info.length()+1+"",main_shamble_score_cards);

                                    hole_postionss.add("0");
                                    data1.put(hole_info.length()+2+"", "0");
                                    data2.put(hole_info.length()+2+"", "0");
                                    data3.put(hole_info.length()+2+"", "0");
                                    data4.put(hole_info.length()+2+"", "0");
                                    data5.put(hole_info.length()+2+"", "0");
                                    is_expand.put(hole_info.length()+2+"","false");


                                    ArrayList<Bestball_Player_list> mid_shamble_score_cards = new ArrayList<>();
                                    mid_shamble_score_cards.add(new Bestball_Player_list("","","","",""));
                                    No_of_player.put(hole_info.length()+2+"",mid_shamble_score_cards);


                                    hole_postionss.add("0");
                                    data1.put(hole_info.length()+3+"", "0");
                                    data2.put(hole_info.length()+3+"", "0");
                                    data3.put(hole_info.length()+3+"", "0");
                                    data4.put(hole_info.length()+3+"", "0");
                                    data5.put(hole_info.length()+3+"", "0");
                                    is_expand.put(hole_info.length()+3+"","false");

                                    ArrayList<Bestball_Player_list> last_shamble_score_cards = new ArrayList<>();
                                    last_shamble_score_cards.add(new Bestball_Player_list("","","","",""));
                                    No_of_player.put(hole_info.length()+3+"",last_shamble_score_cards);

                                    hole_postionss.add("0");
                                    data1.put(hole_info.length()+4+"", "0");
                                    data2.put(hole_info.length()+4+"", "0");
                                    data3.put(hole_info.length()+4+"", "0");
                                    data4.put(hole_info.length()+4+"", "0");
                                    data5.put(hole_info.length()+4+"", "0");
                                    is_expand.put(hole_info.length()+4+"","false");

                                    ArrayList<Bestball_Player_list> last_shamble_score_cards1 = new ArrayList<>();
                                    last_shamble_score_cards1.add(new Bestball_Player_list("","","","",""));
                                    No_of_player.put(hole_info.length()+4+"",last_shamble_score_cards1);


                                    horizontalAdapter = new Livescorecard(data1, data2, data3, data4, data5,hole_postionss,No_of_player, getActivity(),is_expand);
                                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    recycler_view.setLayoutManager(horizontalLayoutManager);
                                    recycler_view.setAdapter(horizontalAdapter);
                                    recycler_view.smoothScrollToPosition(hole_selected_position);

                                    Shamble_Bestball_Map_GS_activity.player_in = No_of_player.get(selected_hole_postion);
                                    Shamble_Bestball_Map_GS_activity.hole_data = hole_postionss.get(Integer.parseInt(selected_hole_postion));
                                    Shamble_Bestball_Map_GS_activity.par_data = data1.get(selected_hole_postion);
                                    Share_it.savestring("selected_hole_postion",selected_hole_postion);

                                    if (progress==0){ if (pd.isShowing()) {
                                        pd.dismiss();
                                    } }

                                    progress=1;


                                } else {
                                    if (progress==0){ if (pd.isShowing()) { pd.dismiss(); } }
                                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
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
                            if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                // HTTP Status Code: 401 Unauthorized
                                if (error.networkResponse.statusCode == 401) {
                                    if (progress==0){ if (pd.isShowing()) { pd.dismiss(); } }
                                    Alert_Dailog.showNetworkAlert(getActivity());
                                } else {
                                    if (progress==0){ if (pd.isShowing()) { pd.dismiss(); } }
                                    Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                if (progress==0){ if (pd.isShowing()) { pd.dismiss(); } }
                                Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();

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


    public class Livescorecard extends RecyclerView.Adapter<Livescorecard.MyViewHolder> {
        Map<String, String> data2 = new HashMap<>();
        Map<String, String> data3 = new HashMap<>();
        Map<String, String> data4 = new HashMap<>();
        Map<String, String> data5 = new HashMap<>();
        Map<String, String> data6 = new HashMap<>();
        ArrayList<String> postion_list = new ArrayList<>();
        HashMap <String,ArrayList<Bestball_Player_list>> No_of_player  = new HashMap<>();
        Map<String,String> is_expand_values = new HashMap<>();
        View itemView;

        public Livescorecard(Map<String, String> data2, Map<String, String> data3, Map<String, String> data4, Map<String, String> data5, Map<String, String> data6,ArrayList<String> postion_list,HashMap <String,ArrayList<Bestball_Player_list>> No_of_player,Context context,Map<String,String> is_expand_values) {
            this.data2 = data2;
            this.data3 = data3;
            this.data4 = data4;
            this.data5 = data5;
            this.data6 = data6;
            this.postion_list = postion_list;
            this.No_of_player = No_of_player;
            this.is_expand_values = is_expand_values;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView serial_number,hole_number, distance, distance1, distance2, distance3,all_ply_text,lowest_score;
            LinearLayout all_player_user;
            String postiondata="";
            RecyclerView team_list;
            TextView edit_icon;
            public MyViewHolder(View view) {
                super(view);
                serial_number = view.findViewById(R.id.serial_number);
                hole_number = view.findViewById(R.id.hole_number);
                distance = view.findViewById(R.id.distance);
                distance1 = view.findViewById(R.id.distance1);
                distance2 = view.findViewById(R.id.distance2);
                distance3 = view.findViewById(R.id.distance3);
                all_ply_text = view.findViewById(R.id.all_ply_text);
                lowest_score = view.findViewById(R.id.lowest_score);
                lowest_score.setTypeface(medium);
                lowest_score.setText("Lowest "+scores_counted+" scores selected and added together");
                team_list = view.findViewById(R.id.team_list);

                edit_icon = view.findViewById(R.id.edit_icon);
                edit_icon.setText("\uf040");
                edit_icon.setTypeface(webfont);

                serial_number.setTypeface(medium);
                hole_number.setTypeface(medium);
                distance.setTypeface(medium);
                distance1.setTypeface(medium);
                distance2.setTypeface(medium);
                distance3.setTypeface(medium);

                all_player_user = view.findViewById(R.id.all_player_user);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_shamble_score_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final int half_data = (data1.size()-3)/2;
            holder.postiondata = postion_list.get(position);

            String row_unselect = Share_it.get_auth_token("un_select");

            if (row_unselect.equals(position + 1 + "")){
            }else  if(position %2 == 1)
            {
                holder.serial_number.setBackgroundResource(R.color.second_row);
                holder.all_ply_text.setBackgroundResource(R.color.second_row);
                holder.itemView.setBackgroundResource(R.color.colorwhite);
            }
            else
            {
                holder.serial_number.setBackgroundResource(R.color.first_row_color);
                holder.all_ply_text.setBackgroundResource(R.color.first_row_color);
                holder.itemView.setBackgroundResource(R.color.row_background_color);
            }


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

                if (parr==0){
                    holder.hole_number.setText("");
                }else {
                    holder.hole_number.setText(parr +"");
                }

                if (yards==0){
                    holder.distance.setText("");
                }else {
                    holder.distance.setText(yards + "");
                }

                if (gross==0){
                    holder.distance2.setText("0");
                }else {
                    holder.distance2.setText(gross + "");
                }

                holder.distance3.setText("-");

                holder.distance1.setText("");

            } else if (position == data1.size() - 3) {

                int parr = 0, yards = 0, gross = 0, net = 0;

                for (int i = half_data; i < data1.size() - 4; i++) {
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
                if (parr==0){
                    holder.hole_number.setText("");
                }else {
                    holder.hole_number.setText(parr +"");
                }

                if (yards==0){
                    holder.distance.setText("");
                }else {
                    holder.distance.setText(yards + "");
                }
                if (gross==0){
                    holder.distance2.setText("0");
                }else {
                    holder.distance2.setText(gross + "");
                }

                holder.distance3.setText("-");

                holder.distance1.setText("");


            } else if (position < half_data) {

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

                if (data4.get(holder.postiondata).equals("0") || data4.get(holder.postiondata).equals("null") || data4.get(holder.postiondata).equals(null) || data4.get(holder.postiondata).equals("")) {
                    holder.distance2.setText("");
                }else {
                    holder.distance2.setText(data4.get(holder.postiondata)+ "");
                    holder.distance2.setTypeface(medium);
                }

                holder.distance3.setText("-");

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

            } else if (position > half_data && position < data1.size() - 3) {

                holder.postiondata = postion_list.get(position - 1);


                if (data3.size() == position + 1) {
                    holder.serial_number.setText("");
                    holder.hole_number.setText("");
                } else {
                    holder.serial_number.setText(holder.postiondata);
                    if (Integer.parseInt(data2.get(holder.postiondata))==0){
                        holder.hole_number.setText("");
                    }else {
                        holder.hole_number.setText(data2.get(holder.postiondata) +"");
                    }
                }

                if (data4.get(holder.postiondata).equals("0") || data4.get(holder.postiondata).equals("null") || data4.get(holder.postiondata).equals(null) || data4.get(holder.postiondata).equals("")) {
                    holder.distance2.setText("");
                }else {
                    holder.distance2.setText(data4.get(holder.postiondata)+ "");
                    holder.distance2.setTypeface(medium);
                }

                holder.distance3.setText("-");

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
            }

            try {

                if (position == data3.size()-1 ){
                    holder.distance2.setBackgroundResource(R.color.colorwhite);
                }else {
                    int remaining_par = 0, remaingin_gross = 0;
                    String data4_values = data4.get(holder.postiondata);
                    if (data4_values.equals("0") || data4_values.equals("") || data4_values.equals(null)) {
                        remaingin_gross = 0;
                        remaining_par = 0;
                    } else {
                        remaingin_gross = Integer.parseInt(data4.get(holder.postiondata));
                        remaining_par = Integer.parseInt(data2.get(holder.postiondata));
                    }

                    int total_remaining = remaingin_gross - remaining_par*Integer.parseInt(scores_counted);
                    if (total_remaining == 0) {
                        if (Integer.parseInt(data4.get(holder.postiondata))!=0){
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
                }
            }catch (Exception e){
            }


            if (position==data3.size()-2){
                int parr = 0, yards = 0, gross = 0, net = 0;
                for (int i = 0; i < data1.size() - 4; i++) {
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
                holder.itemView.setBackgroundResource(R.color.colorwhite);
                holder.serial_number.setText("Total");
                holder.hole_number.setText(parr+"");
                holder.distance.setText(yards+"");
                holder.distance1.setText("");
                holder.distance2.setText(gross+"");
                holder.distance3.setText("-");
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (position == data3.size() - 1 || half_data == position || position == data3.size() - 2 || position == data3.size() - 3) {
                        holder.edit_icon.setEnabled(false);
                        holder.edit_icon.setVisibility(View.INVISIBLE);
                    } else{

                        if (is_expand_values.get(holder.postiondata).equals("true")){
                            holder.all_player_user.setVisibility(View.GONE);
                            is_expand_values.put(holder.postiondata,"false");
                        } else {

                            Player_ListAdapter player = new Player_ListAdapter(No_of_player.get(holder.postiondata+""), getActivity());
                            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            holder.team_list.setLayoutManager(horizontalLayoutManager);
                            holder.team_list.setAdapter(player);
                            player.notifyDataSetChanged();
                            holder.all_player_user.setVisibility(View.VISIBLE);

                            is_expand_values.put(holder.postiondata,"true");

                        }
                    }
                }
            });

            if (position==data3.size()-1){
                holder.distance2.setBackgroundResource(R.color.colorwhite);
                holder.serial_number.setBackgroundResource(R.color.colorwhite);
                holder.all_ply_text.setBackgroundResource(R.color.colorwhite);
                holder.itemView.setBackgroundResource(R.color.colorwhite);
                holder.hole_number.setText("");
                holder.distance.setText("");
                holder.distance1.setText("");
                holder.distance2.setText("");
                holder.distance3.setText("");
            }

            if(position==data3.size()-1 || half_data==position || position==data3.size()-2 || position==data3.size()-3) {
                holder.edit_icon.setEnabled(true);
                holder.edit_icon.setVisibility(View.INVISIBLE);
            }

            if(NewEventDetailPage.event_type.equals("passed")){

                holder.edit_icon.setClickable(false);
                holder.edit_icon.setEnabled(false);

                }else {

                if (position==hole_selected_position-1){

                    holder.team_list.setFocusable(false);
                    Player_ListAdapter player = new Player_ListAdapter(No_of_player.get(holder.postiondata+""), getActivity());
                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    holder.team_list.setLayoutManager(horizontalLayoutManager);
                    holder.team_list.setAdapter(player);
                    player.notifyDataSetChanged();

                    Shamble_Bestball_Map_GS_activity.player_in = No_of_player.get(holder.postiondata+"");
                    Shamble_Bestball_Map_GS_activity.hole_data = holder.postiondata;
                    Shamble_Bestball_Map_GS_activity.par_data = data2.get(holder.postiondata);
                    savestring("selected_hole_postion",holder.postiondata);

                    holder.edit_icon.setEnabled(true);
                    holder.all_player_user.setVisibility(View.VISIBLE);
                    holder.edit_icon.setTextColor(Color.parseColor("#09A5CE"));

                    is_expand_values.put(holder.postiondata,"true");

                }
            }




            holder.edit_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==data3.size()-1 || half_data==position || position==data3.size()-2|| position==data3.size()-3 ){
                    }else {

                        View_course_map.hole_number_from_all_corses  = Integer.parseInt(holder.postiondata)-1;

                        Share_it.savestring("response_postions",holder.postiondata);
                        Share_it.savestring("row_three_postion", position+1+"");
                        Share_it.savestring("un_select", "-2");
                        Share_it.savestring("row_gross_Score", data6.get(holder.postiondata));

                        savestring("selected_hole_postion",holder.postiondata+"");

                        Shamble_Player_list_activity.player_in = No_of_player.get(holder.postiondata+"");
                        Intent in = new Intent(getActivity(), Shamble_Player_list_activity.class);
                        in.putExtra("player_name", player_name);
                        in.putExtra("hole", holder.postiondata);
                        in.putExtra("par", data2.get(holder.postiondata));
                        startActivityForResult(in, 2);
                    }

                }
            });


//            holder.layout_edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if(position==data3.size()-1 || half_data==position || position==data3.size()-2){
//
//                    }else {
//                        //data = fill_with_data();
//                        holder.team_list.setFocusable(false);
//                        Player_ListAdapter player = new Player_ListAdapter(No_of_player.get(holder.postiondata+""), getActivity());
//                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//                        holder.team_list.setLayoutManager(horizontalLayoutManager);
//                        holder.team_list.setAdapter(player);
//                        player.notifyDataSetChanged();
//
//                        Shamble_Bestball_Map_GS_activity.player_in = No_of_player.get(holder.postiondata+"");
//                        Shamble_Bestball_Map_GS_activity.hole_data = holder.postiondata;
//                        Shamble_Bestball_Map_GS_activity.par_data = data2.get(holder.postiondata);
//                        Share_it.savestring("selected_hole_postion",holder.postiondata);
//
//                        View_course_map_drag.hole_number_from_all_corses  = Integer.parseInt(holder.postiondata)-1+"";
//
//                        holder.distance2.setEnabled(true);
//                        holder.all_player_user.setVisibility(View.VISIBLE);
//                        holder.box_text_layout.setVisibility(View.VISIBLE);
//                        holder.distance2.setBackgroundResource(R.drawable.editext_baground);
//                    }
//
//                }
//            });
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


    public class Player_ListAdapter extends RecyclerView.Adapter<Player_ListAdapter.MyViewHolder> {
        ArrayList<Bestball_Player_list> shamble_player_lists;

        public Player_ListAdapter(ArrayList<Bestball_Player_list> shamble_player_lists, Context context) {
            this.shamble_player_lists = shamble_player_lists;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView player_name,player_score,player_select_unselt;
            public MyViewHolder(View view) {
                super(view);

                player_name = view.findViewById(R.id.player_name);
                player_score = view.findViewById(R.id.player_score);
                player_select_unselt = view.findViewById(R.id.player_select_unselt);

                player_name.setTypeface(Splash_screen.regular);
                player_score.setTypeface(Splash_screen.regular);
                player_select_unselt.setTypeface(Splash_screen.regular);

            }
        }

        @Override
        public Player_ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shamble_sub_list_player_name, parent, false);

            return new Player_ListAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final Player_ListAdapter.MyViewHolder holder, final int position) {

            holder.player_name.setText(shamble_player_lists.get(position).getPlayer_name());
            holder.player_score.setText(shamble_player_lists.get(position).getScore());
            holder.player_select_unselt.setText(shamble_player_lists.get(position).getSelect_unselect());

        }
        @Override
        public int getItemCount() {
            return shamble_player_lists.size();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String message = data.getStringExtra("MESSAGE");
            String player_id = data.getStringExtra("user_id");
            if (!message.equals("-1")){
                try {
                    pd = new ProgressDialog(getActivity());
                    pd.setMessage("Please wait.");
                    pd.setCancelable(false);
                    pd.setIndeterminate(true);
                    pd.show();

                    response_postions = get_auth_token("response_postions");
                    row_three_postion = get_auth_token("row_three_postion");
                    selected_gross_score = get_auth_token("row_gross_Score");
                    netscorecalculation = new JSONObject();
                    netscorecalculation.put("hole_number", response_postions);
                    netscorecalculation.put("handicap", selected_gross_score);
                    netscorecalculation.put("gross_score", message);
                    netscorecalculation.put("fairway_hit",Shamble_Player_list_activity.first_row);
                    netscorecalculation.put("green_in_regulation", Shamble_Player_list_activity.second_row);
                    netscorecalculation.put("putts", Shamble_Player_list_activity.third_row);
                    netscorecalculation.put("notes", Shamble_Player_list_activity.notes_values);

                    if (!isNetworkAvailable()) {
                        alertdailogbox("update_score");
                    } else {
                        methodtocallnetscoreApi(netscorecalculation, response_postions,"/"+player_id);
                    }


                } catch (Exception e) {
                    //nothing
                }

            }

        } else if (resultCode == RESULT_CANCELED) {
            row_three_postion = Share_it.get_auth_token("row_three_postion");

            Share_it.savestring("un_select",row_three_postion);

            horizontalAdapter.notifyDataSetChanged();
        }
    }

    public void methodtocallnetscoreApi(JSONObject data, final String response_postions,String user__id) {
        String url_value="";
            url_value = "hole_info/gross_score/" + event_id+user__id;
        System.out.println("------------data----"+data);
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.POST, Splash_screen.url +url_value, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Score_board_new.game_completed =   response.getString("completed");
                            try {
                                hole_selected_position = Integer.parseInt(response.getString("completed_hole"));
                                if (total_holes==9){
                                    if (hole_selected_position>3 && hole_selected_position<9){
                                        hole_selected_position = hole_selected_position+2;
                                        View_course_map.hole_number_from_all_corses  = (hole_selected_position-1);
                                        savestring("selected_hole_postion",hole_selected_position-1+"");
                                    }else if (hole_selected_position==9){
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

                            postio_select = Integer.parseInt(Share_it.get_auth_token("row_three_postion"));
                            //                                    {"gross_score":5,"net_score":5,"status":"Success"}
                            if (response.getString("status").equals("Success")) {

                                String gross_score = response.getString("gross_score");
                                String net_score = response.getString("net_score");

                                if(gross_score==null || gross_score.equals("") || gross_score.equals("null") || gross_score.equals(null)){
                                    gross_score = "0";
                                }


                                if(net_score==null || net_score.equals("") || net_score.equals("null") || net_score.equals(null)){
                                    net_score = "0";
                                }


                                data3.put(response_postions,gross_score );
                                data4.put(response_postions,net_score );

                                String total_score = response.getString("total_score");
                                tvtotal_score.setText(Html.fromHtml("<b><big>"+total_score+"</big></b>"+"<br> SCORE<br>"+"<small>TO PAR X"+scores_counted+"</small>"));

                                int to_par = Integer.parseInt(net_score);
                                String game_type="";
                                game_type = "<b>" + "Score: "+ "</b>"+response.getString("total_net_score")+"/"+(Integer.parseInt(response.getString("total_par_value")) * Integer.parseInt(scores_counted));
                                tvtotal_score.setText(Html.fromHtml("<b><big>"+to_par+"</big></b>"+"<br> SCORE<br>"+"<small>TO PAR X"+scores_counted+"</small>"));

                                tv_tee_hole_info.setText(Html.fromHtml("Starting "+ "<b>Hole "+response_postions+"</b><br><b>Tee </b>"+ get_auth_token("selected_tee_postion")+"<br>"+game_type+"<br>"+"Handicap <b>"+get_auth_token("handicap")+"<b>"));


                                // No of players who updated gross score
                                ArrayList<Bestball_Player_list> shamble_score_cards = new ArrayList<>();
                                JSONArray player_details = response.getJSONArray("player_array");
                                for (int j=0;j<player_details.length();j++){

                                    JSONObject player_details_obj = player_details.getJSONObject(j);
                                    String player_name = player_details_obj.getString("player_name");
                                    String player_gross_score = player_details_obj.getString("player_gross_score");
                                    String selecte_unselect="";

                                    String player_id = player_details_obj.getString("player_id");

                                    if (player_gross_score.equals(null) || player_gross_score.equals("null") || player_gross_score.equals("")){
                                        player_gross_score="";
                                    }else {
                                        selecte_unselect = player_details_obj.getString("selected");

                                        if (selecte_unselect.equals("no") || selecte_unselect.equals(null) || selecte_unselect.equals("null") || selecte_unselect.equals("")){
                                            selecte_unselect="";
                                        }else {
                                            selecte_unselect="Selected";
                                        }
                                    }

                                    shamble_score_cards.add(new Bestball_Player_list(player_name,player_gross_score,selecte_unselect,player_id,""));
                                }
                                No_of_player.put(response_postions+"",shamble_score_cards);


                                horizontalAdapter = new Livescorecard(data1, data2, data3, data4, data5,hole_postionss,No_of_player, getActivity(),is_expand);
                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                recycler_view.setLayoutManager(horizontalLayoutManager);
                                recycler_view.setAdapter(horizontalAdapter);
                                recycler_view.smoothScrollToPosition(postio_select);
//                                recycler_view.scrollToPosition(postio_select-1);

                            } else {
                                Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            //nothing
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                                Alert_Dailog.showNetworkAlert(getActivity());
                            } else {

                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                                Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                            Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

