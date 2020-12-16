package com.winningticketproject.in.Fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.winningticketproject.in.IndividulaGameType.AddScoresToScorecard;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.IndividulaGameType.Location_Services;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.EventTab.Score_board_new;
import com.winningticketproject.in.Live_Score_New_Flow.View_course_map;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.game_name;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class ScoreboardFragment extends Fragment {
    View dialogview, convertView;
    String auth_token = "";
    String first_time = "";
    String selected_hole_postion = "";
    String row_three_postion;
    String hand_hadicap_selecteds = "0";
    String selected_gross_score = "0";
    String response_postions = "";
    int hole_selected_position=0;
    int total_holes=0;
    RecyclerView recycler_view;
    TextView tvtotal_score, tvholes, tvdistance, tvgrassscore, tvnetscore, tvhandicap, tvpar;
    public static String event_id;
    public Map<String, String> data1 = new HashMap<String, String>();
    public Map<String, String> data2 = new HashMap<String, String>();
    public Map<String, String> data3 = new HashMap<String, String>();
    public Map<String, String> data4 = new HashMap<String, String>();
    public Map<String, String> data5 = new HashMap<String, String>();
    ArrayList<String> hole_postionss = new ArrayList<>();
    Livescorecard horizontalAdapter;
    JSONObject netscorecalculation;
    String hand_hadicap_selected,player_name="";
    android.app.ProgressDialog pd;
    public ScoreboardFragment() {
        // Required empty public constructor
    }

    TextView tv_tee_hole_info;
    TextView tvteam;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        convertView = inflater.inflate(R.layout.activity_live_scorecard, container, false);

        auth_token = get_auth_token("auth_token");

        event_id = get_auth_token("event_id");
        selected_hole_postion = get_auth_token("selected_hole_postion");
        Share_it.savestring("un_select", "-2");

        tvtotal_score = convertView.findViewById(R.id.tvtotal_score);
        tvholes = convertView.findViewById(R.id.tvholes);
        tvdistance = convertView.findViewById(R.id.tvdistance);
        tvgrassscore = convertView.findViewById(R.id.tvgrassscore);
        tvnetscore = convertView.findViewById(R.id.tvnetscore);
        tvhandicap = convertView.findViewById(R.id.tvhandicap);

        tv_tee_hole_info = convertView.findViewById(R.id.tv_tee_hole_info);
        tv_tee_hole_info.setTypeface(regular);

        tvpar = convertView.findViewById(R.id.tvpar);

        pd = new android.app.ProgressDialog(getActivity());
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        tvtotal_score.setTypeface(medium);
//        tvtotal.setTypeface(regular);
        tvholes.setTypeface(medium);
        tvdistance.setTypeface(medium);
        tvgrassscore.setTypeface(medium);
        tvnetscore.setTypeface(medium);
        tvhandicap.setTypeface(medium);
        tvpar.setTypeface(medium);

        first_time = get_auth_token("first_time");
        hand_hadicap_selected = get_auth_token("handicap");

        tvteam = convertView.findViewById(R.id.team_name_edit);
        tvteam.setTypeface(medium);

        if (!isNetworkAvailable()) {
            alertdailogbox("scoreboardapi");
        } else {
            methodforscorecard();
        }

        tvteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (get_auth_token("handicap_score").equals("diseble")) {
                    Team_edit_popup();
                }
            }
        });

        return convertView;

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
                        if (pd.isShowing()) { pd.dismiss(); }
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(getActivity());
                            } else {
                               Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
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
                        methodtocallnetscoreApi(netscorecalculation, row_three_postion);
                    }

                }

            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    public void methodforscorecard() {
        JSONObject object = new JSONObject();

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        String update_API="";
        try {
            try{
                object.put("handicap",get_auth_token("handicap"));
                object.put("selected_tee",get_auth_token("selected_tee_postion"));
                object.put("selected_hole",get_auth_token("selected_hole_postion"));
            }catch (Exception e){ }

            if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")){
                update_API = "api/v2/hole_info/player/select-course-info/"+get_auth_token("event_id");
            }else if (get_auth_token("play_type").equals("event")){
                update_API = "api/v2/hole_info/select-course-info/" + get_auth_token("event_id");
            }else if (get_auth_token("play_type").equals("individual")) {
                update_API = "api/v2/hole_info/player/select-course-info/" + get_auth_token("event_id");
            }

            recycler_view = convertView.findViewById(R.id.recycler_view);

            data1 = new HashMap<String, String>();
            data2 = new HashMap<String, String>();
            data3 = new HashMap<String, String>();
            data4 = new HashMap<String, String>();
            data5 = new HashMap<String, String>();
            hole_selected_position=0;

            JsonObjectRequest myRequest_handicap = new JsonObjectRequest(
                    Request.Method.POST, Splash_screen.imageurl +update_API , object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (pd.isShowing()) { pd.dismiss(); }
                            try {
                                Score_board_new.game_completed =   response.getString("completed");
                                System.out.println("----res--------"+response);
                                if (response.getString("status").equals("Success")) {
                                    try {

                                        player_name = response.getString("name");

                                        if (get_auth_token("handicap_score").equals("diseble")) {
                                            hand_hadicap_selected = "0";
                                            tvteam.setText(response.getString("team_name"));

                                        } else {
                                            tvteam.setText(player_name);
                                        }

                                        String net_score = response.getString("total_score");
                                        if(net_score==null || net_score.equals("") || net_score.equals("null") || net_score.equals(null)){
                                            net_score = "0";
                                        }

//                                        int to_par = Integer.parseInt(total_net_score)-Integer.parseInt(total_par_score);
                                        int to_par = Integer.parseInt(net_score);
                                        tvtotal_score.setText(Html.fromHtml("<b><big>"+to_par+"</big></b>"+"<br> SCORE"));

                                        String game_type="";
                                        if (game_name.equals("scramble")){
                                            game_type = "<b>" + "Score "+ "</b>"+response.getString("total_net_score")+"/"+response.getString("total_par_value");
                                        }else {
                                            game_type = "<b>" + "Net Score "+ "</b>"+response.getString("total_net_score")+"/"+response.getString("total_par_value");
                                        }

                                        if (game_name.equals("strokeplay")){
                                            tv_tee_hole_info.setText(Html.fromHtml("Starting "+ "<b>Hole "+get_auth_token("selected_hole_postion")+"</b>"+"<br>"+"<b>" + "Tee "+ "</b>"+ get_auth_token("selected_tee_postion")+"<br>"+game_type));
                                        }else {
                                            tv_tee_hole_info.setText(Html.fromHtml("Starting "+ "<b>Hole "+get_auth_token("starting_hole")+"</b>"+"<br>"+"<b>" + "Tee "+ "</b>"+ get_auth_token("selected_tee_postion")+"<br>"+game_type+"<br>"+"Handicap <b>"+get_auth_token("handicap")+"<b>"));
                                        }

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
                                                    View_course_map.hole_number_from_all_corses  = hole_selected_position-1 ;
                                                }else if (hole_selected_position==9){
                                                    hole_selected_position=1;
                                                    View_course_map.hole_number_from_all_corses  = 1;
                                                }else {
                                                    View_course_map.hole_number_from_all_corses  = hole_selected_position ;
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
                                        if (pd.isShowing()) { pd.dismiss(); }                                    }

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

                                    }

                                    hole_postionss.add("0");
                                    data1.put(hole_info.length()+1+"", "0");
                                    data2.put(hole_info.length()+1+"", "0");
                                    data3.put(hole_info.length()+1+"", "0");
                                    data4.put(hole_info.length()+1+"", "0");
                                    data5.put(hole_info.length()+1+"", "0");

                                    hole_postionss.add("0");
                                    data1.put(hole_info.length()+2+"", "0");
                                    data2.put(hole_info.length()+2+"", "0");
                                    data3.put(hole_info.length()+2+"", "0");
                                    data4.put(hole_info.length()+2+"", "0");
                                    data5.put(hole_info.length()+2+"", "0");

                                    hole_postionss.add("0");
                                    data1.put(hole_info.length()+3+"", "0");
                                    data2.put(hole_info.length()+3+"", "0");
                                    data3.put(hole_info.length()+3+"", "0");
                                    data4.put(hole_info.length()+3+"", "0");
                                    data5.put(hole_info.length()+3+"", "0");

                                    hole_postionss.add("0");
                                    data1.put(hole_info.length()+4+"", "0");
                                    data2.put(hole_info.length()+4+"", "0");
                                    data3.put(hole_info.length()+4+"", "0");
                                    data4.put(hole_info.length()+4+"", "0");
                                    data5.put(hole_info.length()+3+"", "0");

                                    horizontalAdapter = new Livescorecard(data1, data2, data3, data4, data5,hole_postionss, getActivity());
                                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    recycler_view.setLayoutManager(horizontalLayoutManager);
                                    recycler_view.setAdapter(horizontalAdapter);
                                    recycler_view.smoothScrollToPosition(hole_selected_position);

                                    if (pd.isShowing()) { pd.dismiss(); }
                                } else {
                                    if (pd.isShowing()) { pd.dismiss(); }

                                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                                if (pd.isShowing()) { pd.dismiss(); }
                            } catch (Exception e) {
                                //nothing
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (pd.isShowing()) { pd.dismiss(); }
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                // HTTP Status Code: 401 Unauthorized
                                if (error.networkResponse.statusCode == 401) {
                                    Alert_Dailog.showNetworkAlert(getActivity());
                                } else {
                                    Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
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
        Map<String, String> data2 = new HashMap<String, String>();
        Map<String, String> data3 = new HashMap<String, String>();
        Map<String, String> data4 = new HashMap<String, String>();
        Map<String, String> data5 = new HashMap<String, String>();
        Map<String, String> data6 = new HashMap<String, String>();
        ArrayList<String> postion_list = new ArrayList<>();
        View itemView;

        public Livescorecard(Map<String, String> data2, Map<String, String> data3, Map<String, String> data4, Map<String, String> data5, Map<String, String> data6,ArrayList<String> postion_list,Context context) {
            this.data2 = data2;
            this.data3 = data3;
            this.data4 = data4;
            this.data5 = data5;
            this.data6 = data6;
            this.postion_list = postion_list;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView serial_number, hole_number,distance, distance1, distance2, distance3;
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
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_scorecard, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final int half_data = (data1.size()-3)/2;
            holder.postiondata = postion_list.get(position);

            String row_unselect = get_auth_token("un_select");

            if (row_unselect.equals(position + 1 + "")){
            }else  if(position %2 == 1)
            {
                holder.serial_number.setBackgroundResource(R.color.second_row);
                holder.itemView.setBackgroundResource(R.color.colorwhite);
//                holder.color_text.setBackgroundResource(R.color.second_row);
            }
            else
            {
                holder.serial_number.setBackgroundResource(R.color.first_row_color);
//                holder.color_text.setBackgroundResource(R.color.first_row_color);
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
                    holder.distance2.setText(gross + "");
                }else {
                    holder.distance2.setText(gross + "");
                }

                if (Location_Services.game_name.equals("scramble") || Location_Services.game_name.equals("strokeplay")){
                    holder.distance3.setText(" - ");
                }else {
                    if (net==0){
                        holder.distance3.setText(net + "");
                    }else {
                        holder.distance3.setText(net + "");
                    }
                }

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
                    holder.distance2.setText(gross +"");
                }else {
                    holder.distance2.setText(gross + "");
                }

                if (Location_Services.game_name.equals("scramble") || Location_Services.game_name.equals("strokeplay")){
                    holder.distance3.setText(" - ");
                }else {
                    if (net==0){
                        holder.distance3.setText("-");
                    }else {
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
                    if (Integer.parseInt(data2.get(holder.postiondata))==0){
                        holder.hole_number.setText("");
                    }else {
                        holder.hole_number.setText(data2.get(holder.postiondata) +"");
                    } }

                if (data4.get(holder.postiondata).equals("0") || data4.get(holder.postiondata).equals("null") || data4.get(holder.postiondata).equals(null) || data4.get(holder.postiondata).equals("")) {
                    holder.distance2.setText("-");
                }else {
                    holder.distance2.setText(data4.get(holder.postiondata)+ "");
                }

                if (Location_Services.game_name.equals("scramble") || Location_Services.game_name.equals("strokeplay")){
                    holder.distance3.setText(" - ");
                }else {
                    if (data5.get(holder.postiondata).equals("0") || data5.get(holder.postiondata).equals("null") || data5.get(holder.postiondata).equals(null) || data5.get(holder.postiondata).equals("")) {
                        holder.distance3.setText("-");
                    }else {
                        holder.distance3.setText(data5.get(holder.postiondata)+ "");
                    }
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
                    holder.distance2.setText("-");
                }else {
                    holder.distance2.setText(data4.get(holder.postiondata)+ "");
                }

                if (Location_Services.game_name.equals("scramble") || Location_Services.game_name.equals("strokeplay")){
                    holder.distance3.setText(" - ");
                }else {
                    if (Integer.parseInt(data5.get(holder.postiondata))==0){
                        holder.distance3.setText("-");
                    }else {
                        holder.distance3.setText(data5.get(holder.postiondata) + "");
                    }
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
            }

            try {

                if (position == half_data || position == data3.size()-1 || position == data3.size()-2 || position==data3.size()-3){
                    holder.edit_icon.setEnabled(false);
                    holder.edit_icon.setVisibility(View.INVISIBLE);
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
                    int total_remaining = remaingin_gross - remaining_par;

                    if (game_name.equals("scramble") || game_name.equals("Course") || Location_Services.game_name.equals("strokeplay")){
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
                    }else {
                        if (total_remaining == 0) {
                            if (Integer.parseInt(data4.get(holder.postiondata))!=0){
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

                }
            }catch (Exception e){
            }

            if (position==data3.size()-1){
                holder.distance2.setBackgroundResource(R.color.colorwhite);
//                holder.layout_edit.setVisibility(View.INVISIBLE);
                holder.serial_number.setBackgroundResource(R.color.colorwhite);
                holder.itemView.setBackgroundResource(R.color.colorwhite);
                holder.hole_number.setText("");
                holder.distance.setText("");
                holder.distance1.setText("");
                holder.distance2.setText("");
                holder.distance3.setText("");
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

                if (game_name.equals("scramble") || Location_Services.game_name.equals("strokeplay")){
                    holder.distance3.setText("-");
                }else {
                    holder.distance3.setText(net+"");
                }


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
                    if(position==data3.size()-1 || half_data==position || position==data3.size()-2 || position==data3.size()-3){
                    }else {
                        Share_it.savestring("response_postions",holder.postiondata);
                        Share_it.savestring("row_three_postion", position+1+"");
                        Share_it.savestring("un_select", "-2");
                        Share_it.savestring("row_gross_Score", data6.get(holder.postiondata));

                        savestring("selected_hole_postion",holder.postiondata+"");

                        Intent in = new Intent(getActivity(), AddScoresToScorecard.class);
                        in.putExtra("player_name", player_name);
                        in.putExtra("hole", holder.postiondata);
                        in.putExtra("par", data2.get(holder.postiondata));
                        startActivityForResult(in, 2);
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
            if (!message.equals("-1")){
                try {

                    pd = new ProgressDialog(getActivity());
                    pd.setMessage("Please wait.");
                    pd.setCancelable(false);
                    pd.setIndeterminate(true);
                    pd.show();

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
                    netscorecalculation.put("notes", AddScoresToScorecard.notes_values);

                    if (!isNetworkAvailable()) {
                        alertdailogbox("update_score");
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

                            if (pd.isShowing()) { pd.dismiss(); }

                            Score_board_new.game_completed =   response.getString("completed");

                            try {
                                hole_selected_position = Integer.parseInt(response.getString("completed_hole"));

                                if (total_holes==9){
                                    if (hole_selected_position>3 && hole_selected_position<9){
                                        hole_selected_position = hole_selected_position+2;
                                        View_course_map.hole_number_from_all_corses  = hole_selected_position-1;
                                        savestring("selected_hole_postion",hole_selected_position-1+"");
                                    }else if (hole_selected_position==9){
                                        hole_selected_position=1;
                                        View_course_map.hole_number_from_all_corses  = 1;
                                        savestring("selected_hole_postion","1");
                                    }else {
                                        hole_selected_position++;
                                        View_course_map.hole_number_from_all_corses  = hole_selected_position ;
                                        savestring("selected_hole_postion",hole_selected_position+"");
                                    }
                                }

                                if (total_holes==18){
                                    if (hole_selected_position>8 && hole_selected_position<18){
                                            hole_selected_position = hole_selected_position+2;
                                            View_course_map.hole_number_from_all_corses  = hole_selected_position-1;
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

                            if (response.getString("status").equals("Success")) {
                                data3.put(response_postions, response.getString("gross_score"));
                                data4.put(response_postions, response.getString("net_score"));

                                String total_score = response.getString("total_score");
                                tvtotal_score.setText(Html.fromHtml("<b><big>"+total_score+"</big></b><br> SCORE"));

                                String game_type="";
                                if (game_name.equals("scramble")){
                                    game_type = "<b>" + "Score "+ "</b>"+response.getString("total_net_score")+"/"+response.getString("total_par_value");
                                }else {
                                    game_type = "<b>" + "Net Score "+ "</b>"+response.getString("total_net_score")+"/"+response.getString("total_par_value");
                                }

                                if (game_name.equals("strokeplay")){
                                    tv_tee_hole_info.setText(Html.fromHtml("Starting "+ "<b>Hole "+get_auth_token("selected_hole_postion")+"</b>"+"<br>"+"<b>" + "Tee "+ "</b>"+ get_auth_token("selected_tee_postion")+"<br>"+game_type));
                                }else {
                                    tv_tee_hole_info.setText(Html.fromHtml("Starting "+ "<b>Hole "+get_auth_token("starting_hole")+"</b>"+"<br>"+"<b>" + "Tee "+ "</b>"+ get_auth_token("selected_tee_postion")+"<br>"+game_type+"<br>"+"Handicap <b>"+get_auth_token("handicap")+"<b>"));
                                }

                                horizontalAdapter = new Livescorecard(data1, data2, data3, data4, data5,hole_postionss, getActivity());
                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                recycler_view.setLayoutManager(horizontalLayoutManager);
                                recycler_view.setAdapter(horizontalAdapter);
                                recycler_view.smoothScrollToPosition(hole_selected_position);
                            } else {
                                Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            //nothing
                            if (pd.isShowing()) { pd.dismiss(); }
                        }

                        if (pd.isShowing()) { pd.dismiss(); }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (pd.isShowing()) { pd.dismiss(); }
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(getActivity());
                            } else {
                                Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
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

