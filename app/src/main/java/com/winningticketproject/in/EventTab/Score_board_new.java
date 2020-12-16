package com.winningticketproject.in.EventTab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.winningticketproject.in.BestBallFlow.BestBall_Scorecard;
import com.winningticketproject.in.IndividulaGameType.Location_Services;
import com.winningticketproject.in.AppInfo.GPSTracker;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.NewMapEvenFLOW.NormalScorreboard;
import com.winningticketproject.in.R;
import com.winningticketproject.in.Stable_modify_stroke_Flow.StableFord_Score_Card;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_modify_ScoreCard;

import static com.winningticketproject.in.IndividulaGameType.Location_Services.game_name;
import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;

public class Score_board_new extends AppCompatActivity implements View.OnClickListener {
    TextView tvleave_game, tvscrore_card, textView7, tvlive;
    String event_name = "", auth_token;
    Button score_board, leader_board;
    View score_board_view, leader_board_view;
    View dialogview;
    android.support.v7.app.AlertDialog alertDialog;
    GPSTracker gps;
    public static String game_completed = "";
    ImageView view_course_map;
    Button btn_single_line;
    ImageView score_board_menu;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_page_new);

        gps = new GPSTracker(getApplicationContext());

        auth_token = get_auth_token("auth_token");
        event_name = get_auth_token("Event_name");


        btn_single_line = findViewById(R.id.btn_single_line);

        score_board = findViewById(R.id.score_board);
        leader_board = findViewById(R.id.leader_board);
        view_course_map = findViewById(R.id.view_course_map);

        view_course_map.setOnClickListener(this);

        score_board_view = findViewById(R.id.score_board_view);
        leader_board_view = findViewById(R.id.leader_board_view);

        score_board.setTextColor(getApplication().getResources().getColor(R.color.colorbtn));
        score_board_view.setBackgroundResource(R.color.colorbtn);

        leader_board.setTextColor(getApplication().getResources().getColor(R.color.colorwhite));
        leader_board_view.setBackgroundResource(R.color.colorblack);


        if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")) {
            view_course_map.setVisibility(View.VISIBLE);
        } else if (get_auth_token("play_type").equals("event")) {
            view_course_map.setVisibility(View.VISIBLE);
        } else if (get_auth_token("play_type").equals("individual")) {
            view_course_map.setVisibility(View.INVISIBLE);
        }

        tvscrore_card = findViewById(R.id.tvscrore_card);
        textView7 = findViewById(R.id.textView7);
        tvscrore_card.setText("Your Scorecard");
        tvleave_game = findViewById(R.id.tvleave_game);
        tvleave_game.setTypeface(medium);
        tvscrore_card.setTypeface(medium);
        textView7.setTypeface(medium);
        score_board.setTypeface(medium);
        leader_board.setTypeface(medium);

        tvlive = findViewById(R.id.tvlive);
        tvlive.setTypeface(italic);

        score_board_menu = findViewById(R.id.score_board_menu);
        score_board_menu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               if (get_auth_token("Game_type").equals("stableford") || get_auth_token("Game_type").equals("modifiedstableford")) {
                    Stabble_ford_modify();
                } else {
                    Stroke_scramble_individulplay();
                }
                return false;
            }
        });

         if (get_auth_token("Game_type").equals("shamble") || get_auth_token("Game_type").equals("bestball")) {
             Intent intent1 = new Intent(Score_board_new.this, BestBall_Scorecard.class);
             startActivity(intent1);
             finish();
              game_name="bestball";
        } else if (get_auth_token("Game_type").equals("stableford")) {
             Intent inten = new Intent(Score_board_new.this, StableFord_Score_Card.class);
             startActivity(inten);
        } else if (get_auth_token("Game_type").equals("modifiedstableford")) {
             Intent intent1 = new Intent(Score_board_new.this, Stable_modify_ScoreCard.class);
             startActivity(intent1);
        } else {
             Intent intent1 = new Intent(getApplicationContext(), NormalScorreboard.class);
             startActivity(intent1);
             finish();
        }


        score_board.setOnClickListener(this);
        leader_board.setOnClickListener(this);


        textView7.setText(event_name);
        tvleave_game.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pop_up_open();
                return false;
            }
        });

    }

    private void Stabble_ford_modify() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Score_board_new.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) Score_board_new.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogview = inflater.inflate(R.layout.stable_modify_stable_ford, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;

        ImageView cross_close = alertDialog.findViewById(R.id.cross_close);
        cross_close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                alertDialog.dismiss();
                return false;
            }
        });

        TextView tv_lengend_leader = alertDialog.findViewById(R.id.tv_lengend_leader);
        tv_lengend_leader.setTypeface(medium);

        TextView tv_alboross = alertDialog.findViewById(R.id.tv_alboross);
        tv_alboross.setTypeface(regular);

        TextView tv_eadge = alertDialog.findViewById(R.id.tv_eagle);
        tv_eadge.setTypeface(regular);

        TextView tv_bride = alertDialog.findViewById(R.id.tv_bride);
        tv_bride.setTypeface(regular);

        TextView tv_par = alertDialog.findViewById(R.id.tv_par);
        tv_par.setTypeface(regular);

        TextView tv_bogey = alertDialog.findViewById(R.id.tv_bogey);
        tv_bogey.setTypeface(regular);

        TextView tv_double_bogey = alertDialog.findViewById(R.id.tv_double_bogey);
        tv_double_bogey.setTypeface(regular);
//
    }

    private void Stroke_scramble_individulplay() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Score_board_new.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) Score_board_new.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogview = inflater.inflate(R.layout.scramble_stroke_individual, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;

        ImageView cross_close = alertDialog.findViewById(R.id.cross_close);
        cross_close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                alertDialog.dismiss();
                return false;
            }
        });

        TextView tv_lengend_leader = alertDialog.findViewById(R.id.tv_lengend_leader);
        tv_lengend_leader.setTypeface(medium);

        TextView tv_eadge = alertDialog.findViewById(R.id.tv_eadge);
        tv_eadge.setTypeface(regular);

        TextView tv_bride = alertDialog.findViewById(R.id.tv_bride);
        tv_bride.setTypeface(regular);

        TextView tv_par = alertDialog.findViewById(R.id.tv_par);
        tv_par.setTypeface(regular);

        TextView tv_bogey = alertDialog.findViewById(R.id.tv_bogey);
        tv_bogey.setTypeface(regular);

        TextView tv_double_bogey = alertDialog.findViewById(R.id.tv_double_bogey);
        tv_double_bogey.setTypeface(regular);
    }

    public void pop_up_open() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Score_board_new.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) Score_board_new.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogview = inflater.inflate(R.layout.pop_up_finish_game, null);
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
        nearby_courses.setTypeface(medium);
        Button enable = alertDialog.findViewById(R.id.enable);
        Button not_now = alertDialog.findViewById(R.id.not_now);
        enable.setTypeface(medium);
        not_now.setTypeface(medium);


        Button btn_be_right_back = alertDialog.findViewById(R.id.btn_be_right_back);
        btn_be_right_back.setTypeface(medium);

        if (Location_Services.game_name.equals("Course")) {
            if (game_completed.equals("No")) {
                btn_be_right_back.setVisibility(View.VISIBLE);
                enable.setVisibility(View.GONE);
                nearby_courses.setVisibility(View.GONE);
                location_services.setText(Html.fromHtml("You haven't completed your \n game yet"));
            } else {
                btn_be_right_back.setVisibility(View.GONE);
                nearby_courses.setText(Html.fromHtml("Click " + "&ldquo;" + "Finish Game" + "&rdquo;" + " to compare your \nscores on our leaderboard "));
                location_services.setText(Html.fromHtml("You've completed your game!"));
            }
        } else {

            btn_be_right_back.setVisibility(View.GONE);
            enable.setVisibility(View.VISIBLE);

            if (game_completed.equals("No")) {
                nearby_courses.setVisibility(View.GONE);
                location_services.setText(Html.fromHtml("You haven't completed your \n game yet"));
            } else {
                nearby_courses.setText(Html.fromHtml("Click " + "&ldquo;" + "Finish Game" + "&rdquo;" + " to compare your \nscores on our leaderboard "));
                location_services.setText(Html.fromHtml("You've completed your game!"));
            }
        }


        btn_be_right_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share_it.remove_key("first_time");
                alertDialog.dismiss();
                finish();
            }
        });
        not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
//
//            case R.id.score_board:
//
//                RelativeLayout topbar = findViewById(R.id.topbar);
//                topbar.setVisibility(View.VISIBLE);
//
//                btn_single_line.setVisibility(View.GONE);
////                view_course_map.setTextColor(getResources().getColor(R.color.colorblack));
//                view_course_map.setBackgroundResource(R.drawable.ovel_shape_scoreview);
//
//                score_board.setTextColor(getApplication().getResources().getColor(R.color.colorbtn));
//                score_board_view.setBackgroundResource(R.color.colorbtn);
//                leader_board.setTextColor(getApplication().getResources().getColor(R.color.colorwhite));
//                leader_board_view.setBackgroundResource(R.color.colorblack);
//
//                Share_it.savestring("first_time", "no");
//
//                tvlive.setVisibility(View.GONE);
//                tvscrore_card.setText("Your Scorecard");
//
////                if (get_auth_token("Game_type").equals("shamble") || get_auth_token("Game_type").equals("bestball")) {
////                    Shamble_score_card shamble_score_card = new Shamble_score_card();
////                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.replace(R.id.score_board_frame_layout, shamble_score_card);
////                    fragmentTransaction.commit();
//
////                    if (get_auth_token("Game_type").equals("shamble") || get_auth_token("Game_type").equals("bestball")) {
////                    game_name="bestball";
////                    BestBall_score_card profilefragment = new BestBall_score_card();
////                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.replace(R.id.score_board_frame_layout, profilefragment);
////                    fragmentTransaction.commit();
//
//                } else if (get_auth_token("Game_type").equals("stableford")) {
//
//                    Stable_modify_store_score_card profilefragment3 = new Stable_modify_store_score_card();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.score_board_frame_layout, profilefragment3);
//                    fragmentTransaction.commit();
//
//                } else if (get_auth_token("Game_type").equals("modifiedstableford")) {
//                    Stable_modify_score_card profilefragment = new Stable_modify_score_card();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.score_board_frame_layout, profilefragment);
//                    fragmentTransaction.commit();
//                } else {
//                    ScoreboardFragment profilefragment = new ScoreboardFragment();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.score_board_frame_layout, profilefragment);
//                    fragmentTransaction.commit();
//                }
//
//                break;
//
//            case R.id.leader_board:
//
//                RelativeLayout topbar2 = findViewById(R.id.topbar);
//                topbar2.setVisibility(View.VISIBLE);
//                btn_single_line.setVisibility(View.GONE);
////                view_course_map.setTextColor(getResources().getColor(R.color.colorblack));
//                view_course_map.setBackgroundResource(R.drawable.ovel_shape_scoreview);
//
//                score_board.setTextColor(getApplication().getResources().getColor(R.color.colorwhite));
//                score_board_view.setBackgroundResource(R.color.colorblack);
//                leader_board.setTextColor(getApplication().getResources().getColor(R.color.colorbtn));
//                leader_board_view.setBackgroundResource(R.color.colorbtn);
//
//                Share_it.remove_key("row_three_postion");
//
//                tvlive.setVisibility(View.VISIBLE);
//                tvlive.setText("LIVE");
//                tvscrore_card.setText("LeaderBoard");
//
////                if (get_auth_token("Game_type").equals("shamble")) {
////
////                    Shamble_Leader_Board profilefragment2 = new Shamble_Leader_Board();
////                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction2.replace(R.id.score_board_frame_layout, profilefragment2);
////                    fragmentTransaction2.commit();
//
//                    if (get_auth_token("Game_type").equals("shamble") || get_auth_token("Game_type").equals("bestball")) {
////                    game_name="bestball";
////                    Bestbal_Leader_Board profilefragment = new Bestbal_Leader_Board();
////                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.replace(R.id.score_board_frame_layout, profilefragment);
////                    fragmentTransaction.commit();
//
//                   } else if (get_auth_token("Game_type").equals("stableford")) {
//
//                    stable_ford_leader_board profilefragment3 = new stable_ford_leader_board();
//                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction2.replace(R.id.score_board_frame_layout, profilefragment3);
//                    fragmentTransaction2.commit();
//
//                } else if (get_auth_token("Game_type").equals("modifiedstableford")) {
//                    stable_modify_leader_board profilefragment = new stable_modify_leader_board();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.score_board_frame_layout, profilefragment);
//                    fragmentTransaction.commit();
//                } else {
//                    LeaderBoard profilefragment2 = new LeaderBoard();
//                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction2.replace(R.id.score_board_frame_layout, profilefragment2);
//                    fragmentTransaction2.commit();
//                }
//
//                break;
//
//            case R.id.view_course_map:
//
//                RelativeLayout topbar3 = findViewById(R.id.topbar);
//                topbar3.setVisibility(View.GONE);
//
//                btn_single_line.setVisibility(View.VISIBLE);
//                if (isLocationEnabled(Score_board_new.this)) {
//                    score_board.setTextColor(getApplication().getResources().getColor(R.color.colorwhite));
//                    score_board_view.setBackgroundResource(R.color.colorblack);
//                    leader_board.setTextColor(getApplication().getResources().getColor(R.color.colorwhite));
//                    leader_board_view.setBackgroundResource(R.color.colorblack);
//
//                    Share_it.remove_key("row_three_postion");
//
//                    tvlive.setVisibility(View.GONE);
//                    tvscrore_card.setText("VIEW COURSE MAP");
//                    view_course_map.setBackgroundResource(R.drawable.after_select_color);
//
//
//                    View_course_map_drag profilefragment3 = new View_course_map_drag();
//                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction3.replace(R.id.score_board_frame_layout, profilefragment3);
//                    fragmentTransaction3.commit();
//                } else {
//
//                    Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(intent1);
//                }
//
//                break;

        }

    }
}
