package com.winningticketproject.in.Shamble_flow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.parent_getter_setter;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.setListViewHeightBasedOnChildren;
import static com.winningticketproject.in.Shamble_flow.Shamble_score_card.scores_counted;


public class Shamble_Leader_Board extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    ExpandableListView explist;
    ProgressDialog pd;

    public Map<String, String> par_values = new HashMap<String, String>();
    public Map<String, String> Score_values = new HashMap<String, String>();
    public Map<String, String> net_score_values = new HashMap<String, String>();
    public Map<String, String> hole_values = new HashMap<String, String>();
    public Map<String, ArrayList<Shamble_player_info>> user_name_values = new HashMap<>();

    public HashMap<String, List<New_Leader_Board_Child>> new_childArrayList_list = new HashMap<>();
    ListAdapterExpandible   adapterz;
    private List<parent_getter_setter> alleventlist_List = new ArrayList<parent_getter_setter>();

    String Server_url = "ws://18.204.221.185/cable";
    ArrayList<String>  flight_list = new ArrayList<>();
    ArrayList<String> all_users = new ArrayList<>();

    public static int devider=0,eneble=0;
    int group = 0;
    int expand_colapse=0;
    parent_getter_setter parent_list = null;
    String player_name_in_team="",team_name="";
    View v;
    int par_total=0;
    int gross_score_total=0;
    String auth_token = "";
    String event_id = "";
    String user_id="";
    String handicap_score="0";
    String user_current_postion;
    private int lastExpandedPosition = -1;
    TextView tv_par_score,tv_gross_score,tv_handicap,tv_pos_t,text_1,text_2,text_3,text_4,text_champin_ship;
    int gross_score_temp = 0;
    int net_score_temp = 0;
    public OkHttpClient client;
    WebSocket ws;

    public Shamble_Leader_Board() {
    }
    int row_postion=-1,player_postion=0;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        v= inflater.inflate(R.layout.fragment_leader_board, container, false);
        explist= v.findViewById(R.id.explist);
        // add headers values

        MobileAds.initialize(getContext(), String.valueOf(R.string.ads_app_id));
        AdView mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("35E23B2B248A9549E2E9FEF359980EF8").build();
        mAdView.loadAd(adRequest);

//        methodtocalllivestreaming();

        auth_token = get_auth_token("auth_token");

        event_id = get_auth_token("event_id");


        text_1 = v.findViewById(R.id.text_1);
        text_2 = v.findViewById(R.id.text_2);
        text_3 = v.findViewById(R.id.text_3);
        text_4 = v.findViewById(R.id.text_4);

        text_1.setTypeface(medium);
        text_2.setTypeface(medium);
        text_3.setTypeface(medium);
        text_4.setTypeface(medium);

        text_champin_ship = v.findViewById(R.id.text_champin_ship);
        text_champin_ship.setTypeface(Splash_screen.regular);

        if (!isNetworkAvailable()) {
            alertdailogbox("scoreboardapi");
        }
        else
        {
            devider=0;
            eneble=0;
            methodforscorecard();
        }

        explist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent1, View v, int groupPosition, long id) {

                pd.show();
                par_total = 0;
                gross_score_total = 0;

                par_values = new HashMap<String, String>();
                Score_values = new HashMap<String, String>();
                net_score_values = new HashMap<String, String>();
                hole_values = new HashMap<String, String>();

                user_id = alleventlist_List.get(groupPosition).getId();

                try {

                    if (!isNetworkAvailable()) {
                        Toast.makeText(getActivity(), "Internet is required.", Toast.LENGTH_LONG).show();
                    } else {

                        String url_value = "";
                        if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")) {
                            url_value = "hole_info/player/leader_board/" + event_id + "/" + user_id;
                        } else if (get_auth_token("play_type").equals("event")) {
                            url_value = "hole_info/leader_board/" + event_id + "/" + user_id;
                        }

                        System.out.println(url_value);
                        JsonObjectRequest myRequest_handicap = new JsonObjectRequest(
                                com.android.volley.Request.Method.POST, Splash_screen.url + url_value, null,
                                new com.android.volley.Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            System.out.println("------sys-----" + response);
                                            JSONObject score_details = response.getJSONObject("score_details");
                                            JSONArray leader_board = response.getJSONArray("hole_array");

                                            player_name_in_team = "";
                                            all_users.clear();
                                            // Inside team all Player
                                            try {
                                                JSONArray array = response.getJSONArray("user_names");
                                                for (int i = 0; i < array.length(); i++) {
                                                    player_name_in_team += array.getString(i) + " - ";
                                                    all_users.add(array.getString(i));
                                                }
                                                player_name_in_team = player_name_in_team.substring(0, player_name_in_team.length() - 2);
                                            } catch (Exception e) {
                                            }

                                            user_current_postion = score_details.getString("user_position");
                                            handicap_score = score_details.getString("user_handicap");
                                            team_name =  score_details.getString("team_name");

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
                                                    gross_score = "0";
                                                    gross_score_temp = 0;
                                                } else {
                                                    gross_score_temp = Integer.parseInt(gross_score);
                                                }

                                                if (net_score.equals("") || net_score.equals(null) || net_score.equals("null")) {
                                                    net_score = "0";
                                                    net_score_temp = 0;
                                                } else {
                                                    net_score_temp = Integer.parseInt(net_score);
                                                }


                                                par_values.put(row_postion, par_valuess + "");
                                                Score_values.put(row_postion, gross_score + "");
                                                net_score_values.put(row_postion, net_score + "");
                                                hole_values.put(row_postion, row_postion + "");

                                                JSONArray player_details = object_hole.getJSONArray("player_details");
                                                ArrayList<Shamble_player_info> shamble_player_infos = new ArrayList<>();

                                                System.out.println("-------------player"+player_details);

                                                if (player_details.length()==0){

                                                    for (int l=0;l<all_users.size();l++){
                                                        shamble_player_infos.add(new Shamble_player_info(all_users.get(l),"0","no"));
                                                    }

                                                    user_name_values.put(row_postion,shamble_player_infos);

                                                }else {

                                                    for (int j=0;j<player_details.length();j++){

                                                        JSONObject object = player_details.getJSONObject(j);

                                                        String selected="no";
                                                        String player_gross_score = object.getString("player_gross_score");

                                                        if (player_gross_score.equals("") || player_gross_score.equals(null) || player_gross_score.equals("null") || player_gross_score=="") {
                                                            player_gross_score = "0";
                                                        }else {
                                                            selected = object.getString("selected");
                                                        }

                                                        shamble_player_infos.add(new Shamble_player_info(object.getString("player_name"),player_gross_score,selected));
                                                    }

                                                    user_name_values.put(row_postion,shamble_player_infos);
                                                }

                                                par_total += Integer.parseInt(par_valuess);
                                                gross_score_total += gross_score_temp;
                                            }

                                            String best_together = "Best "+ scores_counted +" scores \n added together";

                                            if (leader_board.length()<=10){


                                                ArrayList<New_Leader_Board_Child> new_leader_board_children = new ArrayList<>();
                                                new_leader_board_children.add(new New_Leader_Board_Child("Hole","1","2","3","4","5","6","7","8","9","OUT","","","","","","","","","",""));
                                                new_leader_board_children.add(new New_Leader_Board_Child("Par",par_values.get("1"),par_values.get("2"),par_values.get("3"),par_values.get("4"),par_values.get("5"),par_values.get("6"),par_values.get("7"),par_values.get("8"),par_values.get("9"),par_total+"","","","","","","","","","",""));
                                                new_leader_board_children.add(new New_Leader_Board_Child(best_together,Score_values.get("1"),Score_values.get("2"),Score_values.get("3"),Score_values.get("4"),Score_values.get("5"),Score_values.get("6"),Score_values.get("7"),Score_values.get("8"),Score_values.get("9"),gross_score_total+"","","","","","","","","","",""));

                                                int value_featch=0;
                                                for (int k=0;k<all_users.size();k++){


                                                    ArrayList<Shamble_player_info> playe_list = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list2 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list3 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list4 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list5 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list6 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list7 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list8 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list9 = new ArrayList<>();

                                                    playe_list =  user_name_values.get(1+"");
                                                    playe_list2 =  user_name_values.get(2+"");
                                                    playe_list3 =  user_name_values.get(3+"");
                                                    playe_list4 =  user_name_values.get(4+"");
                                                    playe_list5 =  user_name_values.get(5+"");
                                                    playe_list6 =  user_name_values.get(6+"");
                                                    playe_list7 =  user_name_values.get(7+"");
                                                    playe_list8 =  user_name_values.get(8+"");
                                                    playe_list9 =  user_name_values.get(9+"");

                                                    System.out.println(playe_list.get(value_featch).getName()+"------values-------"+all_users.get(k));

                                                    String hole1 =  playe_list.get(value_featch).getScore();
                                                    String hole2 =  playe_list2.get(value_featch).getScore();
                                                    String hole3 =  playe_list3.get(value_featch).getScore();
                                                    String hole4 =  playe_list4.get(value_featch).getScore();
                                                    String hole5 =  playe_list5.get(value_featch).getScore();
                                                    String hole6 =  playe_list6.get(value_featch).getScore();
                                                    String hole7 =  playe_list7.get(value_featch).getScore();
                                                    String hole8 =  playe_list8.get(value_featch).getScore();
                                                    String hole9 =  playe_list9.get(value_featch).getScore();

                                                    String select1 =  playe_list.get(value_featch).getSelected();
                                                    String select2 =  playe_list2.get(value_featch).getSelected();
                                                    String select3 =  playe_list3.get(value_featch).getSelected();
                                                    String select4 =  playe_list4.get(value_featch).getSelected();
                                                    String select5 =  playe_list5.get(value_featch).getSelected();
                                                    String select6 =  playe_list6.get(value_featch).getSelected();
                                                    String select7 =  playe_list7.get(value_featch).getSelected();
                                                    String select8 =  playe_list8.get(value_featch).getSelected();
                                                    String select9 =  playe_list9.get(value_featch).getSelected();

                                                    int total_score = Integer.parseInt(hole1)+Integer.parseInt(hole2)+Integer.parseInt(hole3)+Integer.parseInt(hole4)+Integer.parseInt(hole5)+Integer.parseInt(hole6)+Integer.parseInt(hole7)+Integer.parseInt(hole8)+Integer.parseInt(hole9);

                                                    new_leader_board_children.add(new New_Leader_Board_Child(playe_list.get(k).getName(),hole1,hole2,hole3,hole4,hole5,hole6,hole7,hole8,hole9,total_score+"",select1,select2,select3,select4,select5,select6,select7,select8,select9,""));

                                                    new_childArrayList_list.put(0+"",new_leader_board_children);

                                                    value_featch++;

                                                }

                                            }else {


                                                int par_total =0,best_to_gether=0;
                                                for (int l=1;l<10;l++){
                                                    par_total+= Integer.parseInt(par_values.get(l+""));
                                                    best_to_gether+= Integer.parseInt(Score_values.get(l+""));
                                                }

                                                ArrayList<New_Leader_Board_Child> new_leader_board_children = new ArrayList<>();

                                                new_leader_board_children.add(new New_Leader_Board_Child("Hole","1","2","3","4","5","6","7","8","9","OUT","","","","","","","","","",""));
                                                new_leader_board_children.add(new New_Leader_Board_Child("Par",par_values.get("1"),par_values.get("2"),par_values.get("3"),par_values.get("4"),par_values.get("5"),par_values.get("6"),par_values.get("7"),par_values.get("8"),par_values.get("9"),par_total+"","","","","","","","","","",""));
                                                new_leader_board_children.add(new New_Leader_Board_Child(best_together,Score_values.get("1"),Score_values.get("2"),Score_values.get("3"),Score_values.get("4"),Score_values.get("5"),Score_values.get("6"),Score_values.get("7"),Score_values.get("8"),Score_values.get("9"),best_to_gether+"","","","","","","","","","",""));

                                                int value_featch=0;
                                                for (int k=0;k<all_users.size();k++) {

                                                    ArrayList<Shamble_player_info> playe_list = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list2 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list3 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list4 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list5 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list6 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list7 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list8 = new ArrayList<>();
                                                    ArrayList<Shamble_player_info> playe_list9 = new ArrayList<>();

                                                    playe_list = user_name_values.get(1 + "");
                                                    playe_list2 = user_name_values.get(2 + "");
                                                    playe_list3 = user_name_values.get(3 + "");
                                                    playe_list4 = user_name_values.get(4 + "");
                                                    playe_list5 = user_name_values.get(5 + "");
                                                    playe_list6 = user_name_values.get(6 + "");
                                                    playe_list7 = user_name_values.get(7 + "");
                                                    playe_list8 = user_name_values.get(8 + "");
                                                    playe_list9 = user_name_values.get(9 + "");


                                                    String hole1 = playe_list.get(value_featch).getScore();
                                                    String hole2 = playe_list2.get(value_featch).getScore();
                                                    String hole3 = playe_list3.get(value_featch).getScore();
                                                    String hole4 = playe_list4.get(value_featch).getScore();
                                                    String hole5 = playe_list5.get(value_featch).getScore();
                                                    String hole6 = playe_list6.get(value_featch).getScore();
                                                    String hole7 = playe_list7.get(value_featch).getScore();
                                                    String hole8 = playe_list8.get(value_featch).getScore();
                                                    String hole9 = playe_list9.get(value_featch).getScore();

                                                    String select1 =  playe_list.get(value_featch).getSelected();
                                                    String select2 =  playe_list2.get(value_featch).getSelected();
                                                    String select3 =  playe_list3.get(value_featch).getSelected();
                                                    String select4 =  playe_list4.get(value_featch).getSelected();
                                                    String select5 =  playe_list5.get(value_featch).getSelected();
                                                    String select6 =  playe_list6.get(value_featch).getSelected();
                                                    String select7 =  playe_list7.get(value_featch).getSelected();
                                                    String select8 =  playe_list8.get(value_featch).getSelected();
                                                    String select9 =  playe_list9.get(value_featch).getSelected();


                                                    int total_score = Integer.parseInt(hole1)+Integer.parseInt(hole2)+Integer.parseInt(hole3)+Integer.parseInt(hole4)+Integer.parseInt(hole5)+Integer.parseInt(hole6)+Integer.parseInt(hole7)+Integer.parseInt(hole8)+Integer.parseInt(hole9);

                                                    new_leader_board_children.add(new New_Leader_Board_Child(playe_list.get(k).getName(), hole1, hole2, hole3, hole4, hole5, hole6, hole7, hole8, hole9, total_score+"",select1,select2,select3,select4,select5,select6,select7,select8,select9,""));

                                                    value_featch++;


                                                }

                                                    new_leader_board_children.add(new New_Leader_Board_Child("","","","","","","","","","","","","","","","","","","","",""));

                                                int par_total_9 =0,best_to_gether_9=0;

                                                for (int l=10;l<=18;l++){
                                                    par_total_9+= Integer.parseInt(par_values.get(l+""));
                                                    best_to_gether_9+= Integer.parseInt(Score_values.get(l+""));
                                                }

                                                    new_leader_board_children.add(new New_Leader_Board_Child("Hole","10","11","12","13","14","15","16","17","18","IN","","","","","","","","","",""));
                                                    new_leader_board_children.add(new New_Leader_Board_Child("Par",par_values.get("10"),par_values.get("11"),par_values.get("12"),par_values.get("13"),par_values.get("14"),par_values.get("15"),par_values.get("16"),par_values.get("17"),par_values.get("18"),par_total_9+"","","","","","","","","","",""));
                                                    new_leader_board_children.add(new New_Leader_Board_Child(best_together,Score_values.get("10"),Score_values.get("11"),Score_values.get("12"),Score_values.get("13"),Score_values.get("14"),Score_values.get("15"),Score_values.get("16"),Score_values.get("17"),Score_values.get("18"),best_to_gether_9+"","","","","","","","","","",""));

                                                    int value_featch2=0;
                                                    for (int k2=0;k2<all_users.size();k2++) {

                                                        ArrayList<Shamble_player_info> playe_list10 = new ArrayList<>();
                                                        ArrayList<Shamble_player_info> playe_list11 = new ArrayList<>();
                                                        ArrayList<Shamble_player_info> playe_list12 = new ArrayList<>();
                                                        ArrayList<Shamble_player_info> playe_list13 = new ArrayList<>();
                                                        ArrayList<Shamble_player_info> playe_list14 = new ArrayList<>();
                                                        ArrayList<Shamble_player_info> playe_list15 = new ArrayList<>();
                                                        ArrayList<Shamble_player_info> playe_list16 = new ArrayList<>();
                                                        ArrayList<Shamble_player_info> playe_list17 = new ArrayList<>();
                                                        ArrayList<Shamble_player_info> playe_list18 = new ArrayList<>();

                                                        playe_list10 = user_name_values.get(10 + "");
                                                        playe_list11 = user_name_values.get(11 + "");
                                                        playe_list12 = user_name_values.get(12 + "");
                                                        playe_list13 = user_name_values.get(13 + "");
                                                        playe_list14 = user_name_values.get(14 + "");
                                                        playe_list15 = user_name_values.get(15 + "");
                                                        playe_list16 = user_name_values.get(16 + "");
                                                        playe_list17 = user_name_values.get(17 + "");
                                                        playe_list18 = user_name_values.get(18 + "");


                                                        String hole10 = playe_list10.get(value_featch2).getScore();
                                                        String hole11 = playe_list11.get(value_featch2).getScore();
                                                        String hole12 = playe_list12.get(value_featch2).getScore();
                                                        String hole13 = playe_list13.get(value_featch2).getScore();
                                                        String hole14 = playe_list14.get(value_featch2).getScore();
                                                        String hole15 = playe_list15.get(value_featch2).getScore();
                                                        String hole16 = playe_list16.get(value_featch2).getScore();
                                                        String hole17 = playe_list17.get(value_featch2).getScore();
                                                        String hole18 = playe_list18.get(value_featch2).getScore();


                                                        String select10 = playe_list10.get(value_featch2).getSelected();
                                                        String select11 = playe_list11.get(value_featch2).getSelected();
                                                        String select12 = playe_list12.get(value_featch2).getSelected();
                                                        String select13 = playe_list13.get(value_featch2).getSelected();
                                                        String select14 = playe_list14.get(value_featch2).getSelected();
                                                        String select15 = playe_list15.get(value_featch2).getSelected();
                                                        String select16 = playe_list16.get(value_featch2).getSelected();
                                                        String select17 = playe_list17.get(value_featch2).getSelected();
                                                        String select18 = playe_list18.get(value_featch2).getSelected();

                                                        int total_score2 = Integer.parseInt(hole10)+Integer.parseInt(hole11)+Integer.parseInt(hole12)+Integer.parseInt(hole13)+Integer.parseInt(hole14)+Integer.parseInt(hole15)+Integer.parseInt(hole16)+Integer.parseInt(hole17)+Integer.parseInt(hole18);

                                                        new_leader_board_children.add(new New_Leader_Board_Child(playe_list10.get(value_featch2).getName(), hole10, hole11, hole12, hole13, hole14, hole15, hole16, hole17, hole18, total_score2+"",select10,select11,select12,select13,select14,select15,select16,select17,select18,""));

                                                        value_featch2++;

                                                    }

                                                new_childArrayList_list.put(0 + "", new_leader_board_children);
                                            }

                                            adapterz.notifyDataSetChanged();
//
                                            if (pd.isShowing()) {
                                                pd.dismiss();
                                            }
                                        } catch (Exception e) {

                                        }

                                        if (pd.isShowing()) {
                                            pd.dismiss();
                                        }
//
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
                lastExpandedPosition = groupPosition;
            }
        });
        explist.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
            }
        });
        return  v;

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
                        methodforscorecard();                    }
                }
            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
//        methodtocalllivestreaming();
    }

    @Override
    public void onPause() {
        super.onPause();
//        ws.close(1000, null);

    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        ws.close(1000, null);
//
//    }


//
//    private void methodtocalllivestreaming() {
//
//        client = new OkHttpClient();
//
//        Request request = new Request.Builder().url(Server_url).addHeader("auth-token",auth_token).build();
//        EchoWebSocketListener listener = new EchoWebSocketListener();
//        ws = client.newWebSocket(request, listener);
//        client.dispatcher().executorService().shutdown();
//    }
//
//    private void output(final String txt) {
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    JSONObject response = new JSONObject(txt);
//                    try {
//                        JSONObject message = response.getJSONObject("message");
//                        JSONObject json = message.getJSONObject("json");
//                        JSONArray leader_board = json.getJSONArray("user_leader_board");
//                        alleventlist_List.clear();
//                        for (int b=0;b<leader_board.length();b++) {
//                            JSONObject leaderboard_object = leader_board.getJSONObject(b);
//                            String user_ids = leaderboard_object.getString("user_id");
//                            String total_net_scores = leaderboard_object.getString("total_net_score");
//                            String total_scores = leaderboard_object.getString("total_score");
//                            String user_postions = leaderboard_object.getString("user_position");
//                            String user_names = leaderboard_object.getString("user_name").substring(0, 1).toUpperCase() + leaderboard_object.getString("user_name").substring(1);
//                            if (total_net_scores.equals("null") || total_net_scores.equals("") || total_net_scores.equals(null)) {
//                                total_net_scores = "";
//                            }
//                            if (total_scores.equals("null") || total_scores.equals("") || total_scores.equals(null)) {
//                                total_scores = "";
//                            }
//                            parent_list = new parent_getter_setter(user_postions, user_ids, user_names, total_net_scores, total_scores,"");
//                            alleventlist_List.add(parent_list);
//
//                        }
//                        adapterz = new  ListAdapterExpandible(getActivity(), alleventlist_List,new_childArrayList_list);
//                        explist.setAdapter(adapterz);
//                        adapterz.notifyDataSetChanged();
//
//                    }catch (Exception e){
//                    }
//
//                    try {
//                        JSONObject message = response.getJSONObject("message");
//                        JSONObject json = message.getJSONObject("json");
//                        String gross_score = json.getString("gross_score");
//                        String net_score = json.getString("net_score");
//                        String total_score = json.getString("total_score");
//                        String hole_number = json.getString("hole_number");
//                        String user_idss = json.getString("user_id");
//                        String sender_auth_token = json.getString("sender_auth_token");
//                        String total_net_scoress = json.getString("total_net_score");
//                        String total_gross_score = json.getString("total_gross_score");
//                        user_current_postion = json.getString("position");
//                        int postion = Integer.parseInt(hole_number);
//                        if (!sender_auth_token.equals(auth_token)){
//
//                            alleventlist_List.clear();
//                            JSONArray leader_board = json.getJSONArray("leader_board");
//                            for (int b=0;b<leader_board.length();b++){
//                                JSONObject leaderboard_object = leader_board.getJSONObject(b);
//                                String user_ids = leaderboard_object.getString("user_id");
//                                String total_net_scores = leaderboard_object.getString("total_net_score");
//                                String total_scores = leaderboard_object.getString("total_score");
//                                String user_postions = leaderboard_object.getString("user_position");
//                                String user_names = leaderboard_object.getString("user_name").substring(0,1).toUpperCase()+leaderboard_object.getString("user_name").substring(1);
//                                if (total_net_scores.equals("null") || total_net_scores.equals("") ||total_net_scores.equals(null)){
//                                    total_net_scores="";
//                                }
//                                if (total_scores.equals("null") || total_scores.equals("") ||total_scores.equals(null)){
//                                    total_scores="";
//                                }
//                                parent_list = new parent_getter_setter(user_postions,user_ids,user_names,total_net_scores,total_scores,"");
//                                alleventlist_List.add(parent_list);
//                                if (user_id.equals(user_idss)){
//                                    gross_score_total = Integer.parseInt(total_gross_score);
//                                    Score_values.put(hole_number,gross_score+"");
//                                    net_score_values.put(hole_number,net_score+"");
//                                    if (postion<10) {
//                                        for (int j = 1; j< 10 ;j++){
//                                            String gross_result = Score_values.get(j+"");
//                                            String net_result = net_score_values.get(j+"");
//                                            if (gross_result.equals("null") || gross_result.equals("") || gross_result.equals(null)){
//                                                gross_result = "0";
//                                            }
//                                            if (net_result.equals("null") || net_result.equals("") || net_result.equals(null)){
//                                                net_result = "0";
//                                            }
//                                        }
//                                    }else {
//                                        for (int k = 10; k < 19 ; k++){
//                                            String gross_result2 = Score_values.get(k+"");
//                                            String net_result2 = net_score_values.get(k+"");
//                                            if (gross_result2.equals("null") || gross_result2.equals("") || gross_result2.equals(null)){
//                                                gross_result2 = "0";
//                                            }
//                                            if (net_result2.equals("null") || net_result2.equals("") || net_result2.equals(null)){
//                                                net_result2 = "0";}
//                                        }
//                                    }
//                                }
//                            }
//
//                            adapterz = new  ListAdapterExpandible(getActivity(), alleventlist_List,new_childArrayList_list);
//                            explist.setAdapter(adapterz);
//                            adapterz.notifyDataSetChanged();
//
//                        }
//                    }catch (Exception e){
//                        //nothing
//                    }
//
//                    try {
//                        JSONObject message = response.getJSONObject("message");
//                        JSONObject json = message.getJSONObject("json");
//                        String user_idss = json.getString("user_id");
//                        String sender_auth_token = json.getString("sender_auth_token");
//                        JSONObject score_card = json.getJSONObject("score_card");
//                        String total_score = score_card.getString("total_score");
//                        String total_net_scoress = score_card.getString("total_net_score");
//                        String user_postion = score_card.getString("user_position");
//                        if (!sender_auth_token.equals(auth_token)){
//                            par_total=0;
//                            gross_score_total=0;
//
//                            try {
//                                for (int i = 0 ; i<alleventlist_List.size(); i++) {
//
//                                    String list_user_id = alleventlist_List.get(i).getId();
//                                    if (list_user_id.equals(user_idss)){
//                                        String user_name = alleventlist_List.get(i).getName();
//                                        if (list_user_id.equals(user_idss)) {
//                                            parent_list = new parent_getter_setter(user_postion,user_idss, user_name, total_net_scoress, total_score,"");
//                                            alleventlist_List.set(i, parent_list);
//                                            adapterz = new  ListAdapterExpandible(getActivity(), alleventlist_List,new_childArrayList_list);
//                                            explist.setAdapter(adapterz);
//                                            adapterz.notifyDataSetChanged();
//                                        }
//                                    }
//                                }
//                            }catch (Exception e){
//
//                            }
//                            try {
//                                if (user_id.equals(user_idss)){
//                                    try {
//                                        for (int i = 0 ; i<alleventlist_List.size(); i++) {
//
//                                            String list_user_id = alleventlist_List.get(i).getId();
//                                            String user_name = alleventlist_List.get(i).getName();
//                                            if (list_user_id.equals(user_idss)) {
//                                                parent_list = new parent_getter_setter(user_postion,user_idss, user_name, total_net_scoress, total_score,"");
//                                                alleventlist_List.set(i, parent_list);
//                                                adapterz = new  ListAdapterExpandible(getActivity(), alleventlist_List,new_childArrayList_list);
//                                                explist.setAdapter(adapterz);
//                                                adapterz.notifyDataSetChanged();
//                                            }
//                                        }
//                                        handicap_score = json.getString("handicap");
//                                        JSONArray leader_board = json.getJSONArray("hole_array");
//                                        for (int i = 0; i < leader_board.length(); i++) {
//                                            JSONObject object_hole = leader_board.getJSONObject(i);
//                                            String row_postion = object_hole.getString("position");
//                                            String gross_score = object_hole.getString("gross_score");
//                                            String net_score = object_hole.getString("net_score");
//                                            String par_valuess = object_hole.getString("par");
//                                            if (gross_score.equals("") || gross_score.equals(null) || gross_score.equals("null")) {
//                                                gross_score = "";
//                                                gross_score_temp = 0;
//                                            } else {
//                                                gross_score_temp = Integer.parseInt(gross_score);
//                                            }
//
//                                            if (net_score.equals("") || net_score.equals(null) || net_score.equals("null")) {
//                                                net_score = "";
//                                                net_score_temp = 0;
//                                            } else {
//                                                net_score_temp = Integer.parseInt(net_score);
//                                            }
//
//                                            if (par_valuess.equals("") || par_valuess.equals(null) || par_valuess.equals("null")) {
//                                                par_valuess = "";
//                                            }
//
//                                            par_values.put(i+1+"", par_valuess + "");
//                                            Score_values.put(i+1+"", gross_score + "");
//                                            net_score_values.put(i+1+"", net_score + "");
//                                            hole_values.put(i+1+"", row_postion + "");
//
//                                            par_total += Integer.parseInt(par_valuess);
//                                            gross_score_total += gross_score_temp;
//                                        }
//                                        alleventlist_List.clear();
//                                        JSONArray leader_boards = json.getJSONArray("leader_board");
//                                        for (int b=0;b<leader_boards.length();b++) {
//                                            JSONObject leaderboard_objects = leader_boards.getJSONObject(b);
//                                            String new_user_idss = leaderboard_objects.getString("user_id");
//                                            String new_total_net_scoress = leaderboard_objects.getString("total_net_score");
//                                            String new_total_scoress = leaderboard_objects.getString("total_score");
//                                            String new_user_postionss = leaderboard_objects.getString("user_position");
//                                            String new_user_namess = leaderboard_objects.getString("user_name").substring(0, 1).toUpperCase() + leaderboard_objects.getString("user_name").substring(1);
//                                            if (new_total_net_scoress.equals("null") || new_total_net_scoress.equals("") || new_total_net_scoress.equals(null)) {
//                                                new_total_net_scoress = "";
//                                            }
//                                            if (new_total_scoress.equals("null") || new_total_scoress.equals("") || new_total_scoress.equals(null)) {
//                                                new_total_scoress = "";
//                                            }
//                                            parent_list = new parent_getter_setter(new_user_postionss, user_idss, new_user_namess, new_total_net_scoress, new_total_scoress,"");
//                                            alleventlist_List.add(parent_list);
//                                        }
//                                        for (int j = 1; j < 10; j++) {
//                                            String par_result = par_values.get(j + "");
//                                            String gross_result = Score_values.get(j + "");
//                                            String net_result = net_score_values.get(j + "");
//
//                                            if (par_result.equals("null") || par_result.equals("") || par_result.equals(null)) {
//                                                par_result = "0";
//                                            }
//                                            if (gross_result.equals("null") || gross_result.equals("") || gross_result.equals(null)) {
//                                                gross_result = "0";
//                                            }
//                                            if (net_result.equals("null") || net_result.equals("") || net_result.equals(null)) {
//                                                net_result = "0";
//                                            }
//                                          }
//                                        for (int k = 10; k < 19; k++) {
//                                            String par_result2 = par_values.get(k + "");
//                                            String gross_result2 = Score_values.get(k + "");
//                                            String net_result2 = net_score_values.get(k + "");
//                                            if (par_result2.equals("null") || par_result2.equals("") || par_result2.equals(null)) {
//                                                par_result2 = "0";
//                                            }
//                                            if (gross_result2.equals("null") || gross_result2.equals("") || gross_result2.equals(null)) {
//                                                gross_result2 = "0";
//                                            }
//                                            if (net_result2.equals("null") || net_result2.equals("") || net_result2.equals(null)) {
//                                                net_result2 = "0";
//                                            }
//                                           }
//
//                                        ArrayList<New_Leader_Board_Child> new_shamble = new ArrayList<>();
//                                        new_shamble.add(new New_Leader_Board_Child("","","","","","","","","","","","","","","","","","","",""));
//                                        new_childArrayList_list.put(0+"",new_shamble);
//
//                                        adapterz = new  ListAdapterExpandible(getActivity(), alleventlist_List,new_childArrayList_list);
//                                        explist.setAdapter(adapterz);
//                                        adapterz.notifyDataSetChanged();
//
//                                        if (pd.isShowing()) {
//                                            pd.dismiss();
//                                        }
//                                    } catch (Exception e) {
//                                    }
//                                }
//                            }catch (Exception e){
//                            }
//                        }
//
//                    }catch (Exception e){
//                    }
//
//                }catch (Exception  e){
//                    //nothing
//                }
//            }
//        });
//    }
//

//    private final class EchoWebSocketListener extends WebSocketListener {
//        private static final int NORMAL_CLOSURE_STATUS = 4000;
//        @Override
//        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
//            webSocket.send("{\"command\":\"subscribe\", \"identifier\":\"{\\\"channel\\\":\\\"LiveScoringsChannel\\\"}\"}");
//        }
//        @Override
//        public void onMessage(WebSocket webSocket, String text) {
//            output(text);
//        }
//        @Override
//        public void onMessage(WebSocket webSocket, ByteString bytes) {
//            output("Receiving bytes : " + bytes.hex());
//        }
//        @Override
//        public void onClosing(WebSocket webSocket, int code, String reason) {
//            webSocket.close(NORMAL_CLOSURE_STATUS, null);
//            output("Closing : " + code + " / " + reason);
//        }
//        @Override
//        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
//            output("Error : " + t.getMessage());
//        }
//    }



    public void methodforscorecard() {
        String url_value="";
        if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")){
            url_value = "hole_info/player/leader_board/"+event_id;
        }else if (get_auth_token("play_type").equals("event")){
            url_value = "hole_info/leader_board/" + event_id;
        }else if (get_auth_token("play_type").equals("individual")) {
            url_value = "hole_info/leader_board/" + event_id;
        }

        try {
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
            JsonObjectRequest myRequest_handicap = new JsonObjectRequest(
                    com.android.volley.Request.Method.POST, Splash_screen.url +url_value, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println("------Shamble response-----"+response);

                                JSONArray leader_board = response.getJSONArray("leader_board");

                                flight_list.clear();

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
                                    }
                                }catch (Exception e){ }


                                for (int i=0;i<leader_board.length();i++){

                                    JSONObject object =leader_board.getJSONObject(i);

                                    String user_id = object.getString("user_id");
                                    String total_net_score = object.getString("total_net_score");
                                    String total_score = object.getString("total_score");
                                    String user_position = object.getString("user_position");

                                    String user_name = object.getString("team_name").substring(0,1).toUpperCase()+object.getString("team_name").substring(1);
                                    if (total_net_score.equals("null") || total_net_score.equals("") ||total_net_score.equals(null)){
                                        total_net_score="";
                                    }

                                    if (total_score.equals("null") || total_score.equals("") ||total_score.equals(null)){
                                        total_score="";
                                    }

                                    if (eneble==1){
                                        if (i%devider==0){
                                            parent_list = new parent_getter_setter(user_position,user_id,user_name,total_net_score,total_score,flight_list.get(group),"","");
                                            group++;
                                        }else {
                                            parent_list = new parent_getter_setter(user_position,user_id,user_name,total_net_score,total_score,"0","","");
                                        }
                                    }else {
                                        parent_list = new parent_getter_setter(user_position,user_id,user_name,total_net_score,total_score,"0","","");
                                    }

                                    alleventlist_List.add(parent_list);
                                }

                                // if number flights enabled go to this flow

                                ArrayList<New_Leader_Board_Child> new_shamble = new ArrayList<>();
                                new_shamble.add(new New_Leader_Board_Child("","","","","","","","","","","","","","","","","","","","",""));
                                new_childArrayList_list.put(0+"",new_shamble);



                                adapterz = new  ListAdapterExpandible(getActivity(), alleventlist_List,new_childArrayList_list);
                                explist.setAdapter(adapterz);
                                adapterz.notifyDataSetChanged();

                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }catch (Exception e){

                            }
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

        } catch (Exception e) {
            //nothing
        }

    }

    //Updated by rajan


    public class ListAdapterExpandible extends BaseExpandableListAdapter{

        private Context context;
        private List<parent_getter_setter> headerArray;
        private HashMap<String, List<New_Leader_Board_Child>> new_childArray;
        private LayoutInflater infalInflater;

        // Initialize constructor for array list
        public ListAdapterExpandible(Context context, List<parent_getter_setter> headerArray, HashMap<String, List<New_Leader_Board_Child>> new_childArray) {
            this.context = context;
            this.headerArray = headerArray;
            this.new_childArray = new_childArray;
            infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this.new_childArray.get(this.headerArray.get(groupPosition).getId())
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
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

//            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                convertView = infalInflater.inflate(R.layout.shamble_leader_board_child, null);
            }


            List<New_Leader_Board_Child> leader_boar = new_childArrayList_list.get("0");

            ListView shamble_plaer_score = convertView.findViewById(R.id.shamble_plaer_score);

            Shamble_player_score_card sub_list_adapter = new Shamble_player_score_card(getActivity(),leader_boar);
            shamble_plaer_score.setAdapter(sub_list_adapter);
            setListViewHeightBasedOnChildren(shamble_plaer_score);
            sub_list_adapter.notifyDataSetChanged();

            tv_par_score = convertView.findViewById(R.id.tv_par_score);
            tv_gross_score = convertView.findViewById(R.id.tv_gross_score);
            tv_handicap = convertView.findViewById(R.id.tv_handicap);
            tv_pos_t = convertView.findViewById(R.id.tv_pos_t);

            tv_par_score.setTypeface(regular);
            tv_gross_score.setTypeface(regular);
            tv_handicap.setTypeface(regular);
            tv_pos_t.setTypeface(regular);

            TextView tv_choosen_color = convertView.findViewById(R.id.tv_choosen_color);
            TextView tv_double_bogey = convertView.findViewById(R.id.tv_double_bogey);
            TextView tv_bogey = convertView.findViewById(R.id.tv_bogey);
            TextView tv_bride = convertView.findViewById(R.id.tv_bride);
            TextView tv_eglae = convertView.findViewById(R.id.tv_eglae);

            tv_choosen_color.setTypeface(regular);
            tv_double_bogey.setTypeface(regular);
            tv_bogey.setTypeface(regular);
            tv_bride.setTypeface(regular);
            tv_eglae.setTypeface(regular);

            TextView leadrbord_all_team = convertView.findViewById(R.id.leadrbord_all_team);
            leadrbord_all_team.setTypeface(Splash_screen.regular);
//
            tv_par_score.setText(Html.fromHtml("Team Par: "+"<b>"+par_total*Integer.parseInt(scores_counted)+"</b>"));
//
            tv_gross_score.setText(
                    Html.fromHtml("Gross: "+"<b>"+gross_score_total+"</b>"));
            tv_pos_t.setText(Html.fromHtml("Position: "+"<b>"+user_current_postion+"</b>"));

            if (Integer.parseInt(handicap_score)>0){
                if (get_auth_token("handicap_score").equals("diseble")){
                    tv_handicap.setText(Html.fromHtml("Handicap: "+"<b>Disabled</b>"));
                    leadrbord_all_team.setText(Html.fromHtml("<b>"+team_name+"</b>"+": " +player_name_in_team));
                }else {
                    leadrbord_all_team.setVisibility(View.GONE);
                    tv_handicap.setText(Html.fromHtml("Handicap: +"+"<b>"+player_name_in_team+"</b>"));
                }

            }else {
                if (get_auth_token("handicap_score").equals("diseble")){
                    tv_handicap.setText(Html.fromHtml("Handicap: "+"<b>Disabled</b>"));
                    leadrbord_all_team.setText(Html.fromHtml("<b>"+team_name+"</b>"+": " +player_name_in_team));
                }else {
                    leadrbord_all_team.setVisibility(View.GONE);
                    tv_handicap.setText(Html.fromHtml("Handicap: "+"<b>"+handicap_score+"</b>"));
                }

            }

            return convertView;
        }

        // return number of headers in list
        @Override
        public int getChildrenCount(int groupPosition) {
            int size=0;
                size = new_childArrayList_list.size();
            return size;

//            return this.new_childArray.get(this.headerArray.get(groupPosition).getId()).size();
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
                convertView = infalInflater.inflate(R.layout.shamble_leaderboard_parent_, null);
            }


            TextView text_1s = convertView.findViewById(R.id.text_1);
            TextView tvplayer_name = convertView.findViewById(R.id.tvplayer_name);
            TextView tv_score = convertView.findViewById(R.id.tv_score);
            TextView tv_par_score = convertView.findViewById(R.id.tv_par_score);
            TextView tvtrophy = convertView.findViewById(R.id.tvtrophy);

            TextView id_champion_ship = convertView.findViewById(R.id.id_champion_ship);
            id_champion_ship.setTypeface(regular);

            text_1s.setTypeface(regular);
            tvplayer_name.setTypeface(regular);
            tv_score.setTypeface(regular);
            tv_par_score.setTypeface(medium);

            System.out.println("Group postion"+groupPosition);

            if(groupPosition == 0) {
                tv_par_score.setTextColor(getResources().getColor(R.color.public_color));
                tvtrophy.setVisibility(View.VISIBLE);
                tvtrophy.setText("\uf091");
                tvtrophy.setTypeface(webfont);
            }

            if(groupPosition %2 == 1)
            {
                text_1s.setBackgroundResource(R.color.second_row);
                convertView.setBackgroundResource(R.color.colorwhite);
            }
            else
            {
                text_1s.setBackgroundResource(R.color.first_row_color);
                convertView.setBackgroundResource(R.color.row_background_color);
            }

            text_1s.setText(parent_postion.getUser_postion());


            if (expand_colapse==0){
                if (!parent_postion.getEnebled().equals("0")){
                    id_champion_ship.setVisibility(View.VISIBLE);
                    id_champion_ship.setText(parent_postion.getEnebled());
                    group++;
                }else {
                    id_champion_ship.setVisibility(View.GONE);
                }
            }


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

    public class Shamble_player_score_card extends BaseAdapter {
        public LayoutInflater inflater;
        List<New_Leader_Board_Child> leader_board_data;
        public Shamble_player_score_card(Activity context,List<New_Leader_Board_Child> leader_board_data){
            super();
            this.leader_board_data = leader_board_data;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return leader_board_data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public class ViewHolder {
            TextView tv_colunm_1,tv_colunm_2,tv_colunm_3,tv_colunm_4,tv_colunm_5,tv_colunm_6,tv_colunm_7,tv_colunm_8,tv_colunm_9,tv_colunm_10,tv_colunm_11;
            LinearLayout top_layout;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final Shamble_player_score_card.ViewHolder holder;
            if (convertView == null) {

                holder = new Shamble_player_score_card.ViewHolder();
                convertView = inflater.inflate(R.layout.shamble_score_player_list, null);


             holder.tv_colunm_1 = convertView.findViewById(R.id.tv_colunm_1);
             holder.tv_colunm_2 = convertView.findViewById(R.id.tv_colunm_2);
             holder.tv_colunm_3 = convertView.findViewById(R.id.tv_colunm_3);
             holder.tv_colunm_4 = convertView.findViewById(R.id.tv_colunm_4);
             holder.tv_colunm_5 = convertView.findViewById(R.id.tv_colunm_5);
             holder.tv_colunm_6 = convertView.findViewById(R.id.tv_colunm_6);
             holder.tv_colunm_7 = convertView.findViewById(R.id.tv_colunm_7);
             holder.tv_colunm_8 = convertView.findViewById(R.id.tv_colunm_8);
             holder.tv_colunm_9 = convertView.findViewById(R.id.tv_colunm_9);
             holder.tv_colunm_10 = convertView.findViewById(R.id.tv_colunm_10);
             holder.tv_colunm_11 = convertView.findViewById(R.id.tv_colunm_11);


             holder.top_layout = convertView.findViewById(R.id.top_layout);

             convertView.setTag(holder);

            } else

                holder = (Shamble_player_score_card.ViewHolder) convertView.getTag();

            if (leader_board_data.get(position).equals("null") || leader_board_data.get(position).equals(null)) {

                if (leader_board_data.get(position).getName().equals("Hole") || leader_board_data.get(position).getName().equals("") || leader_board_data.get(position).getName().equals("Par") || leader_board_data.get(position).getName().contains("Best")) {
                    player_postion = 1;
                } else {
                    player_postion = 0;
                }

            }else {

                if (leader_board_data.get(position).getName().equals("")) {

                    holder.top_layout.setVisibility(View.INVISIBLE);
                    holder.top_layout.setBackgroundColor(getResources().getColor(R.color.colorwhite));

                } else if (leader_board_data.get(position).getName().equals("Hole")) {

                    row_postion = position;

                    holder.tv_colunm_1.setTypeface(medium);
                    holder.tv_colunm_2.setTypeface(medium);
                    holder.tv_colunm_3.setTypeface(medium);
                    holder.tv_colunm_4.setTypeface(medium);
                    holder.tv_colunm_5.setTypeface(medium);
                    holder.tv_colunm_6.setTypeface(medium);
                    holder.tv_colunm_7.setTypeface(medium);
                    holder.tv_colunm_8.setTypeface(medium);
                    holder.tv_colunm_9.setTypeface(medium);
                    holder.tv_colunm_10.setTypeface(medium);
                    holder.tv_colunm_11.setTypeface(medium);

                    holder.top_layout.setBackgroundResource(R.drawable.top_line);

                } else {

                    holder.top_layout.setBackgroundResource(R.drawable.top_white);

                    holder.tv_colunm_1.setTypeface(medium);
                    holder.tv_colunm_2.setTypeface(regular);
                    holder.tv_colunm_3.setTypeface(regular);
                    holder.tv_colunm_4.setTypeface(regular);
                    holder.tv_colunm_5.setTypeface(regular);
                    holder.tv_colunm_6.setTypeface(regular);
                    holder.tv_colunm_7.setTypeface(regular);
                    holder.tv_colunm_8.setTypeface(regular);
                    holder.tv_colunm_9.setTypeface(regular);
                    holder.tv_colunm_10.setTypeface(regular);
                    holder.tv_colunm_11.setTypeface(medium);

                }
            }


            holder.tv_colunm_1.setText(leader_board_data.get(position).getName());
            holder.tv_colunm_11.setText(leader_board_data.get(position).getLast());

            String str_hole_1= leader_board_data.get(position).getHole();
            String str_hole_2= leader_board_data.get(position).getHole2();
            String str_hole_3= leader_board_data.get(position).getHole3();
            String str_hole_4= leader_board_data.get(position).getHole4();
            String str_hole_5= leader_board_data.get(position).getHole5();
            String str_hole_6= leader_board_data.get(position).getHole6();
            String str_hole_7= leader_board_data.get(position).getHole7();
            String str_hole_8= leader_board_data.get(position).getHole8();
            String str_hole_9= leader_board_data.get(position).getHole9();

            if (str_hole_1=="0" || str_hole_1.equals("0")){
                holder.tv_colunm_2.setText("");
            }else { holder.tv_colunm_2.setText(str_hole_1); }

            if (str_hole_2=="0" || str_hole_2.equals("0")){
                holder.tv_colunm_3.setText("");
            }else {
                holder.tv_colunm_3.setText(str_hole_2);
            }

            if (str_hole_3=="0" || str_hole_3.equals("0")){
                holder.tv_colunm_4.setText("");
            }else {
                holder.tv_colunm_4.setText(str_hole_3);
            }


            if (str_hole_4=="0" || str_hole_4.equals("0")){
                holder.tv_colunm_5.setText("");
            }else {
                holder.tv_colunm_5.setText(str_hole_4);
            }

            if (str_hole_5=="0" || str_hole_5.equals("0")){ holder.tv_colunm_6.setText("");
            }else { holder.tv_colunm_6.setText(str_hole_5); }

            if (str_hole_6=="0" || str_hole_6.equals("0")){ holder.tv_colunm_7.setText("");
            }else { holder.tv_colunm_7.setText(str_hole_6); }

            if (str_hole_7=="0" || str_hole_7.equals("0")){ holder.tv_colunm_8.setText("");
            }else { holder.tv_colunm_8.setText(str_hole_7); }

            if (str_hole_8=="0" || str_hole_8.equals("0")){ holder.tv_colunm_9.setText("");
            }else { holder.tv_colunm_9.setText(str_hole_8); }

            if (str_hole_9=="0" || str_hole_9.equals("0")){holder.tv_colunm_10.setText("");
            }else { holder.tv_colunm_10.setText(str_hole_9); }



            if (position==2){

            String row_1__par_1  = leader_board_data.get(1).getHole();
            String row_2_score_1 = leader_board_data.get(position).getHole();
                int row_1_result_1 = 0;
            if (row_1__par_1=="0" || row_1__par_1=="null" || row_1__par_1=="" || row_1__par_1==null || row_1__par_1.equals("0")){

            }else {
                if (row_2_score_1=="0" || row_2_score_1=="null" || row_2_score_1=="" || row_2_score_1==null || row_2_score_1.equals("0")){

                }else{
                    row_1_result_1 = (Integer.parseInt(row_2_score_1)) - Integer.parseInt(row_1__par_1)*Integer.parseInt(scores_counted);

                    if (row_1_result_1 == 0) {
                    } else if (row_1_result_1 == -1) {
                        holder.tv_colunm_2.setBackgroundResource(R.drawable.bride);
                    } else if (row_1_result_1 < -1) {
                        holder.tv_colunm_2.setBackgroundResource(R.drawable.eagle);
                    } else if (row_1_result_1 == 1) {
                        holder.tv_colunm_2.setBackgroundResource(R.drawable.bogey);
                    } else if (row_1_result_1 >= 1) {
                        holder.tv_colunm_2.setBackgroundResource(R.drawable.double_bpgey);
                    }
                }
            }


                String row_1__par_2  = leader_board_data.get(1).getHole2();
                String row_2_score_2 = leader_board_data.get(position).getHole2();

                int row_1_result_2 = 0;
                if (row_1__par_2=="0" || row_1__par_2=="null" || row_1__par_2=="" || row_1__par_2==null || row_1__par_2.equals("0")){
                }else {
                    if (row_2_score_2=="0" || row_2_score_2=="null" || row_2_score_2=="" || row_2_score_2==null ||  row_2_score_2.equals("0")){
                    }else{
                        row_1_result_2 = (Integer.parseInt(row_2_score_2)) - Integer.parseInt(row_1__par_2)*Integer.parseInt(scores_counted);
                        if (row_1_result_2 == 0) {
                        } else if (row_1_result_2 == -1) {
                            holder.tv_colunm_3.setBackgroundResource(R.drawable.bride);
                        } else if (row_1_result_2 < -1) {
                            holder.tv_colunm_3.setBackgroundResource(R.drawable.eagle);
                        } else if (row_1_result_2 == 1) {
                            holder.tv_colunm_3.setBackgroundResource(R.drawable.bogey);
                        } else if (row_1_result_2 >= 1) {
                            holder.tv_colunm_3.setBackgroundResource(R.drawable.double_bpgey);
                        }
                    }
                }

                // 3 colom
                String row_1__par_3  = leader_board_data.get(1).getHole3();
                String row_2_score_3 = leader_board_data.get(position).getHole3();

                int row_1_result_3 = 0;
                if (row_1__par_3=="0" || row_1__par_3=="null" || row_1__par_3=="" || row_1__par_3==null || row_1__par_3.equals("0")){
                }else {
                    if (row_2_score_3=="0" || row_2_score_3=="null" || row_2_score_3=="" || row_2_score_3==null || row_2_score_3.equals("0")){
                    }else{
                        row_1_result_3 = (Integer.parseInt(row_2_score_3)) - Integer.parseInt(row_1__par_3)*Integer.parseInt(scores_counted);
                        if (row_1_result_3 == 0) {
                        } else if (row_1_result_3 == -1) {
                            holder.tv_colunm_4.setBackgroundResource(R.drawable.bride);
                        } else if (row_1_result_3 < -1) {
                            holder.tv_colunm_4.setBackgroundResource(R.drawable.eagle);
                        } else if (row_1_result_3 == 1) {
                            holder.tv_colunm_4.setBackgroundResource(R.drawable.bogey);
                        } else if (row_1_result_3 >= 1) {
                            holder.tv_colunm_4.setBackgroundResource(R.drawable.double_bpgey);
                        }
                    }
                }


                // 4 colom
                String row_1__par_4  = leader_board_data.get(1).getHole4();
                String row_2_score_4 = leader_board_data.get(position).getHole4();

                int row_1_result_4 = 0;
                if (row_1__par_4=="0" || row_1__par_4=="null" || row_1__par_4=="" || row_1__par_4==null || row_1__par_4.equals("0")){
                }else {
                    if (row_2_score_4=="0" || row_2_score_4=="null" || row_2_score_4=="" || row_2_score_4==null || row_2_score_4.equals("0")){
                    }else{
                        row_1_result_4 = (Integer.parseInt(row_2_score_4)) - Integer.parseInt(row_1__par_4)*Integer.parseInt(scores_counted);
                        if (row_1_result_4 == 0) {
                        } else if (row_1_result_4 == -1) {
                            holder.tv_colunm_5.setBackgroundResource(R.drawable.bride);
                        } else if (row_1_result_4 < -1) {
                            holder.tv_colunm_5.setBackgroundResource(R.drawable.eagle);
                        } else if (row_1_result_4 == 1) {
                            holder.tv_colunm_5.setBackgroundResource(R.drawable.bogey);
                        } else if (row_1_result_4 >= 1) {
                            holder.tv_colunm_5.setBackgroundResource(R.drawable.double_bpgey);
                        }
                    }
                }

                // 5 colom
                String row_1__par_5  = leader_board_data.get(1).getHole5();
                String row_2_score_5 = leader_board_data.get(position).getHole5();

                int row_1_result_5 = 0;
                if (row_1__par_5=="0" || row_1__par_5=="null" || row_1__par_5=="" || row_1__par_5==null || row_1__par_5.equals("0")){
                }else {
                    if (row_2_score_5=="0" || row_2_score_5=="null" || row_2_score_5=="" || row_2_score_5==null || row_2_score_5.equals("0")){
                    }else{
                        row_1_result_5 = (Integer.parseInt(row_2_score_5)) - Integer.parseInt(row_1__par_5)*Integer.parseInt(scores_counted);
                        if (row_1_result_5 == 0) {
                        } else if (row_1_result_5 == -1) {
                            holder.tv_colunm_6.setBackgroundResource(R.drawable.bride);
                        } else if (row_1_result_5 < -1) {
                            holder.tv_colunm_6.setBackgroundResource(R.drawable.eagle);
                        } else if (row_1_result_5 == 1) {
                            holder.tv_colunm_6.setBackgroundResource(R.drawable.bogey);
                        } else if (row_1_result_5 >= 1) {
                            holder.tv_colunm_6.setBackgroundResource(R.drawable.double_bpgey);
                        }
                    }
                }


                // 6 colom
                String row_1__par_6  = leader_board_data.get(1).getHole6();
                String row_2_score_6 = leader_board_data.get(position).getHole6();

                int row_1_result_6 = 0;
                if (row_1__par_6=="0" || row_1__par_6=="null" || row_1__par_6=="" || row_1__par_6==null || row_1__par_6.equals("0")){
                }else {
                    if (row_2_score_6=="0" || row_2_score_6=="null" || row_2_score_6=="" || row_2_score_6==null  || row_2_score_6.equals("0")){
                    }else{
                        row_1_result_6 = (Integer.parseInt(row_2_score_6)) - Integer.parseInt(row_1__par_6)*Integer.parseInt(scores_counted);
                        if (row_1_result_5 == 0) {
                        } else if (row_1_result_6 == -1) {
                            holder.tv_colunm_7.setBackgroundResource(R.drawable.bride);
                        } else if (row_1_result_6 < -1) {
                            holder.tv_colunm_7.setBackgroundResource(R.drawable.eagle);
                        } else if (row_1_result_6 == 1) {
                            holder.tv_colunm_7.setBackgroundResource(R.drawable.bogey);
                        } else if (row_1_result_6 >= 1) {
                            holder.tv_colunm_7.setBackgroundResource(R.drawable.double_bpgey);
                        }
                    }
                }



                // 7 colom
                String row_1__par_7  = leader_board_data.get(1).getHole7();
                String row_2_score_7 = leader_board_data.get(position).getHole7();

                int row_1_result_7 = 0;
                if (row_1__par_7=="0" || row_1__par_7=="null" || row_1__par_7=="" || row_1__par_7==null || row_1__par_7.equals("0")){
                }else {
                    if (row_2_score_7=="0" || row_2_score_7=="null" || row_2_score_7=="" || row_2_score_7==null || row_2_score_7.equals("0")){
                    }else{
                        row_1_result_7 = (Integer.parseInt(row_2_score_7)) - Integer.parseInt(row_1__par_7)*Integer.parseInt(scores_counted);
                        if (row_1_result_7 == 0) {
                        } else if (row_1_result_7 == -1) {
                            holder.tv_colunm_8.setBackgroundResource(R.drawable.bride);
                        } else if (row_1_result_7 < -1) {
                            holder.tv_colunm_8.setBackgroundResource(R.drawable.eagle);
                        } else if (row_1_result_7 == 1) {
                            holder.tv_colunm_8.setBackgroundResource(R.drawable.bogey);
                        } else if (row_1_result_7 >= 1) {
                            holder.tv_colunm_8.setBackgroundResource(R.drawable.double_bpgey);
                        }
                    }
                }

                // 8 colom
                String row_1__par_8  = leader_board_data.get(1).getHole8();
                String row_2_score_8 = leader_board_data.get(position).getHole8();

                int row_1_result_8 = 0;
                if (row_1__par_8=="0" || row_1__par_8=="null" || row_1__par_8=="" || row_1__par_8==null || row_1__par_8.equals("0")){
                }else {
                    if (row_2_score_8=="0" || row_2_score_8=="null" || row_2_score_8=="" || row_2_score_8==null || row_2_score_8.equals("0")){
                    }else{
                        row_1_result_8 = (Integer.parseInt(row_2_score_8)) - Integer.parseInt(row_1__par_8)*Integer.parseInt(scores_counted);
                        if (row_1_result_8 == 0) {
                        } else if (row_1_result_8 == -1) {
                            holder.tv_colunm_9.setBackgroundResource(R.drawable.bride);
                        } else if (row_1_result_8 < -1) {
                            holder.tv_colunm_9.setBackgroundResource(R.drawable.eagle);
                        } else if (row_1_result_8 == 1) {
                            holder.tv_colunm_9.setBackgroundResource(R.drawable.bogey);
                        } else if (row_1_result_8 >= 1) {
                            holder.tv_colunm_9.setBackgroundResource(R.drawable.double_bpgey);
                        }
                    }
                }
                // 9 colom
                String row_1__par_9  = leader_board_data.get(1).getHole9();
                String row_2_score_9 = leader_board_data.get(position).getHole9();

                int row_1_result_9 = 0;
                if (row_1__par_9=="0" || row_1__par_9=="null" || row_1__par_9=="" || row_1__par_9==null || row_1__par_9.equals("0")){
                }else {
                    if (row_2_score_9=="0" || row_2_score_9=="null" || row_2_score_9=="" || row_2_score_9==null || row_2_score_9.equals("0")){
                    }else{
                        row_1_result_9 = (Integer.parseInt(row_2_score_9)) - Integer.parseInt(row_1__par_9)*Integer.parseInt(scores_counted);
                        if (row_1_result_9 == 0) {
                        } else if (row_1_result_9 == -1) {
                            holder.tv_colunm_10.setBackgroundResource(R.drawable.bride);
                        } else if (row_1_result_9 < -1) {
                            holder.tv_colunm_10.setBackgroundResource(R.drawable.eagle);
                        } else if (row_1_result_9 == 1) {
                            holder.tv_colunm_10.setBackgroundResource(R.drawable.bogey);
                        } else if (row_1_result_9 >= 1) {
                            holder.tv_colunm_10.setBackgroundResource(R.drawable.double_bpgey);
                        }
                    }
                }

            }


                if (position>3) {

                    if (row_postion + 2 == position) {

                        String row_1__par_1 = leader_board_data.get(row_postion).getHole();
                        String row_2_score_1 = leader_board_data.get(position).getHole();
                        int row_1_result_1 = 0;
                        if (row_1__par_1 == "0" || row_1__par_1 == "null" || row_1__par_1 == "" || row_1__par_1 == null || row_1__par_1.equals("0")) {

                        } else {
                            if (row_2_score_1 == "0" || row_2_score_1 == "null" || row_2_score_1 == "" || row_2_score_1 == null || row_2_score_1.equals("0")) {

                            } else {
                                row_1_result_1 = (Integer.parseInt(row_2_score_1)) - Integer.parseInt(row_1__par_1)*Integer.parseInt(scores_counted);

                                if (row_1_result_1 == 0) {
                                } else if (row_1_result_1 == -1) {
                                    holder.tv_colunm_2.setBackgroundResource(R.drawable.bride);
                                } else if (row_1_result_1 < -1) {
                                    holder.tv_colunm_2.setBackgroundResource(R.drawable.eagle);
                                } else if (row_1_result_1 == 1) {
                                    holder.tv_colunm_2.setBackgroundResource(R.drawable.bogey);
                                } else if (row_1_result_1 >= 1) {
                                    holder.tv_colunm_2.setBackgroundResource(R.drawable.double_bpgey);
                                }
                            }
                        }


                        String row_1__par_2 = leader_board_data.get(row_postion).getHole2();
                        String row_2_score_2 = leader_board_data.get(position).getHole2();

                        int row_1_result_2 = 0;
                        if (row_1__par_2 == "0" || row_1__par_2 == "null" || row_1__par_2 == "" || row_1__par_2 == null || row_1__par_2.equals("0")) {
                        } else {
                            if (row_2_score_2 == "0" || row_2_score_2 == "null" || row_2_score_2 == "" || row_2_score_2 == null || row_2_score_2.equals("0")) {
                            } else {
                                row_1_result_2 = (Integer.parseInt(row_2_score_2)) - Integer.parseInt(row_1__par_2)*Integer.parseInt(scores_counted);
                                if (row_1_result_2 == 0) {
                                } else if (row_1_result_2 == -1) {
                                    holder.tv_colunm_3.setBackgroundResource(R.drawable.bride);
                                } else if (row_1_result_2 < -1) {
                                    holder.tv_colunm_3.setBackgroundResource(R.drawable.eagle);
                                } else if (row_1_result_2 == 1) {
                                    holder.tv_colunm_3.setBackgroundResource(R.drawable.bogey);
                                } else if (row_1_result_2 >= 1) {
                                    holder.tv_colunm_3.setBackgroundResource(R.drawable.double_bpgey);
                                }
                            }
                        }

                        //  3 colom
                        String row_1__par_3 = leader_board_data.get(row_postion).getHole3();
                        String row_2_score_3 = leader_board_data.get(position).getHole3();

                        int row_1_result_3 = 0;
                        if (row_1__par_3 == "0" || row_1__par_3 == "null" || row_1__par_3 == "" || row_1__par_3 == null || row_1__par_3.equals("0")) {
                        } else {
                            if (row_2_score_3 == "0" || row_2_score_3 == "null" || row_2_score_3 == "" || row_2_score_3 == null || row_2_score_3.equals("0")) {
                            } else {
                                row_1_result_3 = (Integer.parseInt(row_2_score_3)) - Integer.parseInt(row_1__par_3)*Integer.parseInt(scores_counted);
                                if (row_1_result_3 == 0) {
                                } else if (row_1_result_3 == -1) {
                                    holder.tv_colunm_4.setBackgroundResource(R.drawable.bride);
                                } else if (row_1_result_3 < -1) {
                                    holder.tv_colunm_4.setBackgroundResource(R.drawable.eagle);
                                } else if (row_1_result_3 == 1) {
                                    holder.tv_colunm_4.setBackgroundResource(R.drawable.bogey);
                                } else if (row_1_result_3 >= 1) {
                                    holder.tv_colunm_4.setBackgroundResource(R.drawable.double_bpgey);
                                }
                            }
                        }


                        // 4 colom
                        String row_1__par_4 = leader_board_data.get(row_postion).getHole4();
                        String row_2_score_4 = leader_board_data.get(position).getHole4();

                        int row_1_result_4 = 0;
                        if (row_1__par_4 == "0" || row_1__par_4 == "null" || row_1__par_4 == "" || row_1__par_4 == null || row_1__par_4.equals("0")) {
                        } else {
                            if (row_2_score_4 == "0" || row_2_score_4 == "null" || row_2_score_4 == "" || row_2_score_4 == null || row_2_score_4.equals("0")) {
                            } else {
                                row_1_result_4 = (Integer.parseInt(row_2_score_4)) - Integer.parseInt(row_1__par_4)*Integer.parseInt(scores_counted);
                                if (row_1_result_4 == 0) {
                                } else if (row_1_result_4 == -1) {
                                    holder.tv_colunm_5.setBackgroundResource(R.drawable.bride);
                                } else if (row_1_result_4 < -1) {
                                    holder.tv_colunm_5.setBackgroundResource(R.drawable.eagle);
                                } else if (row_1_result_4 == 1) {
                                    holder.tv_colunm_5.setBackgroundResource(R.drawable.bogey);
                                } else if (row_1_result_4 >= 1) {
                                    holder.tv_colunm_5.setBackgroundResource(R.drawable.double_bpgey);
                                }
                            }
                        }

                        // 5 colom
                        String row_1__par_5 = leader_board_data.get(row_postion).getHole5();
                        String row_2_score_5 = leader_board_data.get(position).getHole5();

                        int row_1_result_5 = 0;
                        if (row_1__par_5 == "0" || row_1__par_5 == "null" || row_1__par_5 == "" || row_1__par_5 == null || row_1__par_5.equals("0")) {
                        } else {
                            if (row_2_score_5 == "0" || row_2_score_5 == "null" || row_2_score_5 == "" || row_2_score_5 == null || row_2_score_5.equals("0")) {
                            } else {
                                row_1_result_5 = (Integer.parseInt(row_2_score_5)) - Integer.parseInt(row_1__par_5)*Integer.parseInt(scores_counted);
                                if (row_1_result_5 == 0) {
                                } else if (row_1_result_5 == -1) {
                                    holder.tv_colunm_6.setBackgroundResource(R.drawable.bride);
                                } else if (row_1_result_5 < -1) {
                                    holder.tv_colunm_6.setBackgroundResource(R.drawable.eagle);
                                } else if (row_1_result_5 == 1) {
                                    holder.tv_colunm_6.setBackgroundResource(R.drawable.bogey);
                                } else if (row_1_result_5 >= 1) {
                                    holder.tv_colunm_6.setBackgroundResource(R.drawable.double_bpgey);
                                }
                            }
                        }


                        // 6 colom
                        String row_1__par_6 = leader_board_data.get(row_postion).getHole6();
                        String row_2_score_6 = leader_board_data.get(position).getHole6();

                        int row_1_result_6 = 0;
                        if (row_1__par_6 == "0" || row_1__par_6 == "null" || row_1__par_6 == "" || row_1__par_6 == null || row_1__par_6.equals("0")) {
                        } else {
                            if (row_2_score_6 == "0" || row_2_score_6 == "null" || row_2_score_6 == "" || row_2_score_6 == null || row_2_score_6.equals("0")) {
                            } else {
                                row_1_result_6 = (Integer.parseInt(row_2_score_6)) - Integer.parseInt(row_1__par_6)*Integer.parseInt(scores_counted);
                                if (row_1_result_5 == 0) {
                                } else if (row_1_result_6 == -1) {
                                    holder.tv_colunm_7.setBackgroundResource(R.drawable.bride);
                                } else if (row_1_result_6 < -1) {
                                    holder.tv_colunm_7.setBackgroundResource(R.drawable.eagle);
                                } else if (row_1_result_6 == 1) {
                                    holder.tv_colunm_7.setBackgroundResource(R.drawable.bogey);
                                } else if (row_1_result_6 >= 1) {
                                    holder.tv_colunm_7.setBackgroundResource(R.drawable.double_bpgey);
                                }
                            }
                        }

                        // 7 colom
                        String row_1__par_7 = leader_board_data.get(row_postion).getHole7();
                        String row_2_score_7 = leader_board_data.get(position).getHole7();

                        int row_1_result_7 = 0;
                        if (row_1__par_7 == "0" || row_1__par_7 == "null" || row_1__par_7 == "" || row_1__par_7 == null || row_1__par_7.equals("0")) {
                        } else {
                            if (row_2_score_7 == "0" || row_2_score_7 == "null" || row_2_score_7 == "" || row_2_score_7 == null || row_2_score_7.equals("0")) {
                            } else {
                                row_1_result_7 = (Integer.parseInt(row_2_score_7)) - Integer.parseInt(row_1__par_7)*Integer.parseInt(scores_counted);
                                if (row_1_result_7 == 0) {
                                } else if (row_1_result_7 == -1) {
                                    holder.tv_colunm_8.setBackgroundResource(R.drawable.bride);
                                } else if (row_1_result_7 < -1) {
                                    holder.tv_colunm_8.setBackgroundResource(R.drawable.eagle);
                                } else if (row_1_result_7 == 1) {
                                    holder.tv_colunm_8.setBackgroundResource(R.drawable.bogey);
                                } else if (row_1_result_7 >= 1) {
                                    holder.tv_colunm_8.setBackgroundResource(R.drawable.double_bpgey);
                                }
                            }
                        }

                        // 8 colom
                        String row_1__par_8 = leader_board_data.get(row_postion).getHole8();
                        String row_2_score_8 = leader_board_data.get(position).getHole8();

                        int row_1_result_8 = 0;
                        if (row_1__par_8 == "0" || row_1__par_8 == "null" || row_1__par_8 == "" || row_1__par_8 == null || row_1__par_8.equals("0")) {
                        } else {
                            if (row_2_score_8 == "0" || row_2_score_8 == "null" || row_2_score_8 == "" || row_2_score_8 == null || row_2_score_8.equals("0")) {
                            } else {
                                row_1_result_8 = (Integer.parseInt(row_2_score_8)) - Integer.parseInt(row_1__par_8)*Integer.parseInt(scores_counted);
                                if (row_1_result_8 == 0) {
                                } else if (row_1_result_8 == -1) {
                                    holder.tv_colunm_9.setBackgroundResource(R.drawable.bride);
                                } else if (row_1_result_8 < -1) {
                                    holder.tv_colunm_9.setBackgroundResource(R.drawable.eagle);
                                } else if (row_1_result_8 == 1) {
                                    holder.tv_colunm_9.setBackgroundResource(R.drawable.bogey);
                                } else if (row_1_result_8 >= 1) {
                                    holder.tv_colunm_9.setBackgroundResource(R.drawable.double_bpgey);
                                }
                            }
                        }

                        // 9 colom
                        String row_1__par_9 = leader_board_data.get(row_postion).getHole9();
                        String row_2_score_9 = leader_board_data.get(position).getHole9();

                        int row_1_result_9 = 0;
                        if (row_1__par_9 == "0" || row_1__par_9 == "null" || row_1__par_9 == "" || row_1__par_9 == null || row_1__par_9.equals("0")) {
                        } else {
                            if (row_2_score_9 == "0" || row_2_score_9 == "null" || row_2_score_9 == "" || row_2_score_9 == null || row_2_score_9.equals("0")) {
                            } else {
                                row_1_result_9 = (Integer.parseInt(row_2_score_9)) - Integer.parseInt(row_1__par_9)*Integer.parseInt(scores_counted);
                                if (row_1_result_9 == 0) {
                                } else if (row_1_result_9 == -1) {
                                    holder.tv_colunm_10.setBackgroundResource(R.drawable.bride);
                                } else if (row_1_result_9 < -1) {
                                    holder.tv_colunm_10.setBackgroundResource(R.drawable.eagle);
                                } else if (row_1_result_9 == 1) {
                                    holder.tv_colunm_10.setBackgroundResource(R.drawable.bogey);
                                } else if (row_1_result_9 >= 1) {
                                    holder.tv_colunm_10.setBackgroundResource(R.drawable.double_bpgey);
                                }
                            }
                        }


                    }

                }


                if (player_postion==0) {

                    String row_1_score_1 = leader_board_data.get(position).getHole();
                    String row_selected_1 = leader_board_data.get(position).getSelected1();

                    if (row_1_score_1 == "0" || row_1_score_1 == "null" || row_1_score_1 == "" || row_1_score_1 == null || row_1_score_1.equals("0")) {
                    } else {
                        if (row_selected_1.equals("yes")) {
                            holder.tv_colunm_2.setBackgroundResource(R.drawable.selected);
                            holder.tv_colunm_2.setTextColor(getResources().getColor(R.color.colorwhite));
                        }
                    }

                    //colom 2

                    String row_1_score_2 = leader_board_data.get(position).getHole2();
                    String row_selected_2 = leader_board_data.get(position).getSelected2();

                    if (row_1_score_2 == "0" || row_1_score_2 == "null" || row_1_score_2 == "" || row_1_score_2 == null || row_1_score_2.equals("0")) {
                    } else {
                        if (row_selected_2.equals("yes")) {
                            holder.tv_colunm_3.setBackgroundResource(R.drawable.selected);
                            holder.tv_colunm_3.setTextColor(getResources().getColor(R.color.colorwhite));
                        }
                    }


                    //colom 3

                    String row_1_score_3 = leader_board_data.get(position).getHole3();
                    String row_selected_3 = leader_board_data.get(position).getSelected3();

                    if (row_1_score_3 == "0" || row_1_score_3 == "null" || row_1_score_3 == "" || row_1_score_3 == null || row_1_score_3.equals("0") || row_1_score_3.equals("") || row_1_score_3.equals("null") || row_1_score_3.equals(null)) {
                    } else {
                        if (row_selected_3.equals("yes")) {
                            holder.tv_colunm_4.setBackgroundResource(R.drawable.selected);
                            holder.tv_colunm_4.setTextColor(getResources().getColor(R.color.colorwhite));
                        }
                    }

                    //colom 4
                    String row_1_score_4 = leader_board_data.get(position).getHole4();
                    String row_selected_4 = leader_board_data.get(position).getSelected4();

                    if (row_1_score_4 == "0" || row_1_score_4 == "null" || row_1_score_4 == "" || row_1_score_4 == null || row_1_score_4.equals("0")) {
                    } else {
                        if (row_selected_4.equals("yes")) {
                            holder.tv_colunm_5.setBackgroundResource(R.drawable.selected);
                            holder.tv_colunm_5.setTextColor(getResources().getColor(R.color.colorwhite));
                        }
                    }

                    //colom 5

                    String row_1_score_5 = leader_board_data.get(position).getHole5();
                    String row_selected_5 = leader_board_data.get(position).getSelected5();

                    if (row_1_score_5 == "0" || row_1_score_5 == "null" || row_1_score_5 == "" || row_1_score_5 == null || row_1_score_5.equals("0")) {
                    } else {
                        if (row_selected_5.equals("yes")) {
                            holder.tv_colunm_6.setBackgroundResource(R.drawable.selected);
                            holder.tv_colunm_6.setTextColor(getResources().getColor(R.color.colorwhite));
                        }
                    }

                    //colom 6
                    String row_1_score_6 = leader_board_data.get(position).getHole6();
                    String row_selected_6 = leader_board_data.get(position).getSelected6();

                    if (row_1_score_6=="0" || row_1_score_6=="null" || row_1_score_6=="" || row_1_score_6==null || row_1_score_6.equals("0")){
                    }else {
                        if (row_selected_6.equals("yes")){
                            holder.tv_colunm_7.setBackgroundResource(R.drawable.selected);
                            holder.tv_colunm_7.setTextColor(getResources().getColor(R.color.colorwhite));
                        }
                    }

                    //colom 7
                    String row_1_score_7 = leader_board_data.get(position).getHole7();
                    String row_selected_7 = leader_board_data.get(position).getSelected7();

                    if (row_1_score_7=="0" || row_1_score_7=="null" || row_1_score_7=="" || row_1_score_7==null || row_1_score_7.equals("0")){
                    }else {
                        if (row_selected_7.equals("yes")){
                            holder.tv_colunm_8.setBackgroundResource(R.drawable.selected);
                            holder.tv_colunm_8.setTextColor(getResources().getColor(R.color.colorwhite));
                        }
                    }

                    //colom 8

                    String row_1_score_8 = leader_board_data.get(position).getHole8();
                    String row_selected_8 = leader_board_data.get(position).getSelected8();

                    if (row_1_score_8=="0" || row_1_score_8=="null" || row_1_score_8=="" || row_1_score_8==null || row_1_score_8.equals("0")){
                    }else {
                        if (row_selected_8.equals("yes")){
                            holder.tv_colunm_9.setBackgroundResource(R.drawable.selected);
                            holder.tv_colunm_9.setTextColor(getResources().getColor(R.color.colorwhite));
                        }
                    }

                    //colom 9
                    String row_1_score_9 = leader_board_data.get(position).getHole9();
                    String row_selected_9 = leader_board_data.get(position).getSelected9();

                    if (row_1_score_9=="0" || row_1_score_9=="null" || row_1_score_9=="" || row_1_score_9==null || row_1_score_9.equals("0")){
                    }else {
                        if (row_selected_9.equals("yes")){
                            holder.tv_colunm_10.setBackgroundResource(R.drawable.selected);
                            holder.tv_colunm_10.setTextColor(getResources().getColor(R.color.colorwhite));
                        }
                    }
                }

            return convertView;
        }

    }

}
