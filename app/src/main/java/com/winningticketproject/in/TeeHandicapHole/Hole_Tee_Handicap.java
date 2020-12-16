package com.winningticketproject.in.TeeHandicapHole;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.winningticketproject.in.BestBallFlow.BestBall_Scorecard;
import com.winningticketproject.in.BestBallFlow.Bestbal_Leader_Board;
import com.winningticketproject.in.IndividulaGameType.AddScoresToScorecard;
import com.winningticketproject.in.Live_Score_New_Flow.View_course_map;
import com.winningticketproject.in.NewMapEvenFLOW.NormalLeaderoard;
import com.winningticketproject.in.NewMapEvenFLOW.NormalScorreboard;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.R;
import com.winningticketproject.in.Stable_modify_stroke_Flow.StableFord_Score_Card;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_ford_Leaderboard;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_modify_ScoreCard;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_modify_leader_board;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.setGridViewHeightBasedOnChildren;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.game_name;
import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class Hole_Tee_Handicap extends AppCompatActivity implements View.OnClickListener {

    Spinner select_tee;
    Spinner select_hole;
    Spinner spinner_handicap;

    TextView tv_event_name;
    TextView tv_tee_text;
    TextView tv_select_hole_text;
    TextView tv_handicap_text;

    TextView tv_selected_nadicap;
    TextView tv_cancel;

    ArrayList<String> tee_selecteds = new ArrayList<>();
    ArrayList<String> tee_selecteds_values = new ArrayList<>();
    ArrayList<String> tee_yards = new ArrayList<>();

    ArrayList<String> hole_selected = new ArrayList<>();
    ArrayList<String> hole_selected_values = new ArrayList<>();

    String[] handicap_values={"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","25","27","28","29","30","31","32","33","34","35","36","-1","-2","-3","-4","-5","-6","-7","-8","-9","-10","-11","-12","-13","-14","-15","-16","-17","-18","-19","-20","-21","-22","-23","-24","-25","-25","-27","-28","-29","-30","-31","-32","-33","-34","-35","-36"};

    Button button_start_round;


    String Tee_selected_str="",Hole_selected_str,Handicap_selected="";

    GridView hole_gridview;

    int backposition = -1;

    LinearLayout ll_non_scramble;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hole__tee__handicap);


        Intilization_ids();

        hole_gridview = findViewById(R.id.hole_gridview);

        ll_non_scramble = findViewById(R.id.ll_non_scramble);

        Handicap_hole_tee_api_Integration();

        button_start_round_method();


        if (get_auth_token("Game_type").equals("strokeplay") || get_auth_token("Game_type").equals("scramble")){
            tv_handicap_text.setVisibility(View.GONE);
            LinearLayout handicap_layout = findViewById(R.id.handicap_layout);
            handicap_layout.setVisibility(View.GONE);
            Handicap_selected = "0";
            savestring("handicap","0");
            savestring("selected_tee_postion","");
        }

        hole_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                final LinearLayout linear_row = view.findViewById(R.id.linear_row);
                linear_row.setBackgroundResource(R.drawable.selected_button);
                LinearLayout BackSelectedItem = (LinearLayout) hole_gridview.getChildAt(backposition);
                if (backposition != position) {
                    if (BackSelectedItem != null) {
                        BackSelectedItem.setSelected(false);
                        BackSelectedItem.setBackgroundResource(R.drawable.new_back);
                    }
                }
                Hole_selected_str = hole_selected_values.get(position);
                backposition = position;
            }
        });
    }

    private void button_start_round_method() {

        button_start_round = findViewById(R.id.button_start_round);
        button_start_round.setTypeface(medium);

        tv_cancel = findViewById(R.id.tv_cancel_);
        tv_cancel.setTypeface(medium);


        tv_cancel.setOnClickListener(this);
        button_start_round.setOnClickListener(this);

        tv_event_name = findViewById(R.id.tv_event_name);
        tv_event_name.setTypeface(medium);
        tv_event_name.setText(get_auth_token("Event_name"));
    }

    private void addItemsOnSpinnerHole() {

        select_hole =  findViewById(R.id.spinner_hole);
        CustomTeeAdapter customAdapter=new CustomTeeAdapter(getApplicationContext(),hole_selected);
        select_hole.setAdapter(customAdapter);

        int hole_pos = hole_selected_values.indexOf(Hole_selected_str);
        if (hole_pos>=0){
            select_hole.setSelection(hole_pos);
        }

        select_hole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Hole_selected_str = hole_selected_values.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Intilization_ids() {

        tv_event_name  = findViewById(R.id.tv_event_name);
        tv_event_name.setTypeface(medium);

        tv_tee_text = findViewById(R.id.tv_tee_text);
        tv_tee_text.setTypeface(italic);

        tv_select_hole_text = findViewById(R.id.tv_select_hole_text);
        tv_select_hole_text.setTypeface(italic);

        tv_handicap_text = findViewById(R.id.tv_handicap_text);
        tv_handicap_text.setTypeface(italic);

        tv_cancel = findViewById(R.id.tv_cancel_);
        tv_cancel.setTypeface(medium);

        tv_selected_nadicap = findViewById(R.id.tv_selected_nadicap);
        tv_selected_nadicap.setTypeface(regular);

    }

    private void handicap_spinner() {

        tv_selected_nadicap = findViewById(R.id.tv_selected_nadicap);
        tv_selected_nadicap.setTypeface(regular);

        spinner_handicap =  findViewById(R.id.spinner_handicap);
        ArrayAdapter<String> adapter_handicap   = new ArrayAdapter<String>(Hole_Tee_Handicap.this,R.layout.custom_select_hole_handicap,handicap_values) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTypeface(medium);
                ((TextView) v).setTextColor(getResources().getColorStateList(R.color.colorblack));
                return v;
            }
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(medium);
                    ((TextView) v).setTextColor(Color.BLACK);
                return v;
            }
        };
        adapter_handicap.setDropDownViewResource( R.layout.custom_select_hole_handicap);
        spinner_handicap.setAdapter(adapter_handicap);

        int index = -1;
        for (int i=0;i<handicap_values.length;i++) {
            if (handicap_values[i].equals(Handicap_selected)) {
                index = i;
                break;
            }
        }

        if (index>=0){
            spinner_handicap.setSelection(index);
        }

        spinner_handicap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int total  = Integer.parseInt(handicap_values[position]);
                Handicap_selected = handicap_values[position];

                if (total == 0) {
                    tv_selected_nadicap.setText("Scratch golfer. You will not have any strokes taken off your gross score.");
                } else if (total == -1) {
                    tv_selected_nadicap.setText("You will have a stroke TAKEN OFF the hardest hole on the golf course.");
                } else if (total < -1) {
                    tv_selected_nadicap.setText("You will have a stroke TAKEN OFF the " + String.valueOf(total).replace("-", "") + " hardest holes on the golf course.");
                } else if (total == 1) {
                    tv_selected_nadicap.setText("You will have a stroke ADDED to the easiest hole on the golf course.");
                } else if (total > 1) {
                    tv_selected_nadicap.setText("You will have a stroke ADDED to the " + total + " easiest holes on the golf course.");
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void Handicap_hole_tee_api_Integration() {

        ProgressDialog.getInstance().showProgress(this);
        String update_API="";
        if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")){
            update_API = "api/v2/hole_info/player/course-info/"+get_auth_token("event_id");
        }else if (get_auth_token("play_type").equals("event")){
            update_API = "api/v2/hole_info/course-info/" + get_auth_token("event_id");
        }else if (get_auth_token("play_type").equals("individual")) {
            update_API = "api/v2/hole_info/player/course-info/"+get_auth_token("event_id");
        }

        System.out.println("---------------"+update_API);

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.imageurl+update_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            System.out.println("-----res-- hole-----"+response);

                            Hole_selected_str = response.getString("selected_hole");
                            Tee_selected_str = response.getString("selected_tee");
                            Handicap_selected = response.getString("selected_handicap");

                            savestring("handicap",Handicap_selected);

                            handicap_spinner();

                            if (response.getString("status").equals("Failure")){
                                Toast.makeText(getApplicationContext(),"Hole Information not exist for this course",Toast.LENGTH_LONG).show();
                                tee_selecteds.add("No Tee");
                                tee_selecteds_values.add("No Tee");
                                tee_yards.add("");

                                hole_selected.add("No Hole");
                                hole_selected_values.add("No Hole");
                            }

                            // tee array value get
                            JSONArray tee_list = response.getJSONArray("tee_list");
                            for (int i=0;i<tee_list.length();i++){
                                JSONObject jsonObject = tee_list.getJSONObject(i);
                                String selected_tee = "<b>"+jsonObject.getString("name")+"</b> (Rating "+jsonObject.getString("rating")+" / Slope "+jsonObject.getString("slope")+") "+jsonObject.getString("yards_total")+" yards";

                                String selected_tee_yards = jsonObject.getString("yards_total")+" yds ("+jsonObject.getString("rating")+"/"+jsonObject.getString("slope")+")";
                                tee_yards.add(selected_tee_yards);
                                tee_selecteds.add(selected_tee);
                                tee_selecteds_values.add(jsonObject.getString("name"));
                            }

                            savestring("yards_value",tee_yards.get(0));

                            // hole array values get
                            JSONArray  hole_list = response.getJSONArray("hole_list");
                            for (int j=0;j<hole_list.length();j++){
                                JSONObject hole_object = hole_list.getJSONObject(j);
                                String selected_tee = "<b>Hole "+hole_object.getString("hole_num")+"</b> - Par "+hole_object.getString("par");

                                String scamble_hole = "<b>"+hole_object.getString("hole_num")+"</b> <br> Par "+hole_object.getString("par");

                                if(get_auth_token("Game_type").equals("scramble")){
                                    hole_selected.add(scamble_hole);
                                }else {
                                    hole_selected.add(selected_tee);
                                }
                                hole_selected_values.add(hole_object.getString("hole_num"));
                            }


                            if(get_auth_token("cross_scoring").equals("true")){

                                tv_tee_text.setText("Select Hole");
                                Tee_selected_str = tee_selecteds_values.get(0);
                                savestring("yards_value",tee_yards.get(0));

                                   hole_gridview.setAdapter(new GridAdapter2(Hole_Tee_Handicap.this, hole_selected, hole_selected_values));
                                setGridViewHeightBasedOnChildren(hole_gridview,3);

                            }else {
                                ll_non_scramble.setVisibility(View.VISIBLE);

                                addItemsOnSpinner2();
                                addItemsOnSpinnerHole();
                            }

                            ProgressDialog.getInstance().hideProgress();

                        } catch (Exception E) {
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
                                Alert_Dailog.showNetworkAlert(Hole_Tee_Handicap.this);
                            } else {
                                Toast.makeText(Hole_Tee_Handicap.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
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
        //nothing
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        select_tee =  findViewById(R.id.spinner_tee);
        CustomTeeAdapter customAdapter=new CustomTeeAdapter(getApplicationContext(),tee_selecteds);
        select_tee.setAdapter(customAdapter);

        int tee_pos = tee_selecteds_values.indexOf(Tee_selected_str);
        if (tee_pos>=0){
            select_tee.setSelection(tee_pos);
        }

        select_tee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               Tee_selected_str = tee_selecteds_values.get(position);
                savestring("yards_value",tee_yards.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.button_start_round:

                savestring("handicap",Handicap_selected);
                savestring("selected_tee_postion",Tee_selected_str);
                savestring("selected_hole_postion",Hole_selected_str);
                savestring("starting_hole",Hole_selected_str);

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
                break ;
            case R.id.tv_cancel_:
                finish();
                break;

        }

    }

    private final class GridAdapter2 extends BaseAdapter {
        ArrayList<String> items = new ArrayList<>();
        ArrayList<String> itemss = new ArrayList<>();

        private GridAdapter2(Context context, ArrayList<String> items, ArrayList<String> itemss) {
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

            text.setVisibility(View.VISIBLE);

            text.setText(Html.fromHtml(items.get(position)));

            text.setTypeface(regular);

            return view;
        }
    }

}
