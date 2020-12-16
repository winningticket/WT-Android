package com.winningticketproject.in.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.MyFirebaseInstanceIdService;
import com.winningticketproject.in.AuctionModel.NewLive_Auction;
import com.winningticketproject.in.BestBallFlow.BestBall_Scorecard;
import com.winningticketproject.in.BestBallFlow.Bestbal_Leader_Board;
import com.winningticketproject.in.CourseTab.AllCourseOnMap;
import com.winningticketproject.in.CourseTab.Courses_list_flow;
import com.winningticketproject.in.EventTab.Event_Code_valid;
import com.winningticketproject.in.IndividulaGameType.Location_Services;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.Live_Score_New_Flow.View_course_map;
import com.winningticketproject.in.MyAccountTab.EditAccount_Details;
import com.winningticketproject.in.NewMapEvenFLOW.NormalLeaderoard;
import com.winningticketproject.in.NewMapEvenFLOW.NormalScorreboard;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.EventTab.View_All_Events;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;
import com.winningticketproject.in.Stable_modify_stroke_Flow.StableFord_Score_Card;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_ford_Leaderboard;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_modify_ScoreCard;
import com.winningticketproject.in.Stable_modify_stroke_Flow.Stable_modify_leader_board;
import com.winningticketproject.in.TeeHandicapHole.Hole_Tee_Handicap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.protocol.HTTP;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.winningticketproject.in.AppInfo.Share_it.setGridViewHeightBasedOnChildren;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.courses_text;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.courses_view;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.euser_view;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.event_text;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.event_view;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.home_text;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.home_view;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.imag_course;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.imag_event;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.imag_home;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.imag_my_account;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.user_text;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.event_id;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.game_name;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.isLocationEnabled;
import static com.winningticketproject.in.EventTab.NewEventDetailPage.oragnization;
import static com.winningticketproject.in.IndividulaGameType.Location_Services.selected_tee;
import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class HomeFragment extends Fragment implements Animation.AnimationListener, View.OnClickListener {
    View view,test1View;
    LinearLayout event_layout;
    Animation sidedown;
    Button enter_event_code,cancel,eventcode;
    String code , total;
    Intent in;
    android.support.v7.app.AlertDialog alertDialog;
    String  live_aminties="";
    public HomeFragment() {
        // Required empty public constructoredittext1
    }
    TextView event_empty_text ,course_name ;
    EditText edittext1;
    LinearLayout ll_your_round,ll_rournaments,ll_courses,event_empty;
    ImageView home_page_event_logo;
    LinearLayout home_page_coordinator,layout_two_button , home_page_layout ,event_code_btn;
    String round_info_tee_seleect="";
    Button continue_event;
    String event_type="";

    TextView tv_code_sub_title,play_now ,play_game_name ;
    String round_info_hole_seleect="";
    String slected_yards_tee="";

    ImageView iv_event_auction;

    LinearLayout button_continue_event;

    View dialogview;

    TextView live_event_text;
    String event_start_date;


    ImageView  tv_gps ;
    ImageView  iv_score_board ;
    ImageView iv_leader_board ;
    ImageView  iv_event_home ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);

        ll_your_round = view.findViewById(R.id.ll_your_round);
        ll_rournaments = view.findViewById(R.id.ll_rournaments);
        ll_courses = view.findViewById(R.id.ll_courses);

        ll_your_round.setOnClickListener(this);
        ll_rournaments.setOnClickListener(this);
        ll_courses.setOnClickListener(this);

        course_name = view.findViewById(R.id.course_name);
        course_name.setTypeface(semibold);

        play_now = view.findViewById(R.id.play_now);
        play_now.setTypeface(semibold);

        home_page_layout = view.findViewById(R.id.home_page_layout);
        event_code_btn = view.findViewById(R.id.event_code_btn);

        play_game_name = view.findViewById(R.id.play_game_name);
        play_game_name.setTypeface(regular);

        event_empty_text = view.findViewById(R.id.event_empty_text);
        event_empty_text.setTypeface(medium);

        continue_event = view.findViewById(R.id.continue_event);
        continue_event.setTypeface(semibold);

        event_empty = view.findViewById(R.id.event_empty);
        home_page_coordinator = view.findViewById(R.id.home_page_coordinator);

        button_continue_event = view.findViewById(R.id.button_continue_event);

        live_event_text = view.findViewById(R.id.live_event_text);
        live_event_text.setTypeface(semibold);

        tv_gps = view.findViewById(R.id.tv_gps);
        iv_score_board = view.findViewById(R.id.iv_score_board);
        iv_leader_board = view.findViewById(R.id.iv_leader_board);
        iv_event_auction = view.findViewById(R.id.iv_event_auction);
        iv_event_home = view.findViewById(R.id.iv_event_home);



        if (!isLocationEnabled(getContext())){
            show_location_enable();
        }

        method_to_load_top_section();

        method_to_play_now();

        if (!isNetworkAvailable()) {
            alertdailogbox("eventlist");
        }
        else
        {
            methodforcalleventlist();
        }
        return view;
    }

    private void show_location_enable() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


    private void method_to_declare_course_card(final String Course_name, String Thur, String Stroke, String totla_values, final String course_date, final String player_id) {

        TextView tv_event_name_course_name = view.findViewById(R.id.tv_event_name_course_name);
        tv_event_name_course_name.setTypeface(medium);
        tv_event_name_course_name.setText(Course_name);

        TextView tv_course_date = view.findViewById(R.id.tv_course_date);
        tv_course_date.setTypeface(regular);
        tv_course_date.setText(course_date);

        TextView tv_thur = view.findViewById(R.id.tv_thur);
        tv_thur.setTypeface(regular);
        tv_thur.setText(Html.fromHtml("Thur<b><big>"+Thur+"</big></b>"));

        TextView tv_stroke = view.findViewById(R.id.tv_stroke);
        tv_stroke.setTypeface(regular);
        tv_stroke.setText(Html.fromHtml("Stroke<b><big>"+Stroke+"</big></b>"));

        TextView tv_total_par = view.findViewById(R.id.tv_total_par);
        tv_total_par.setTypeface(regular);
        tv_total_par.setText(Html.fromHtml("To Par<b><big>"+totla_values+"</big></b>"));

        Button button_start_round = view.findViewById(R.id.button_start_round);
        button_start_round.setTypeface(medium);
        button_start_round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Share_it.savestring("id", player_id);
                Share_it.savestring("play_type", "paid");
                Share_it.savestring("event_id", player_id);
                Share_it.savestring("first_time", "yes");
                Share_it.savestring("Event_name", Course_name);
                Share_it.savestring("Game_type","Course");
                Share_it.savestring("handicap_score","0");
                Location_Services.game_name="Course";
                savestring("live_aminties","");
                oragnization = Course_name;
                NewEventDetailPage.event_type ="";

                round_info_tee_seleect = get_auth_token("selected_tee_postion");

                if (round_info_tee_seleect == null || round_info_tee_seleect.equals(null) || round_info_tee_seleect.equals("null") || round_info_tee_seleect.equals("")){
                    Intent intent1 = new Intent(getActivity(), Hole_Tee_Handicap.class);
                    startActivity(intent1);
                }else {
                    Intent intent1 = new Intent(getActivity(), View_course_map.class);
                    intent1.putExtra("event_type",event_type);
                    startActivity(intent1);
                }
            }
        });


    }

    private void method_to_declare_event_cards(String event_name, String event_course_date, final String Event_id, String event_logo, final String game_type,String event_status) {


        savestring("play_type", "event");
        savestring("event_id", Event_id);
        savestring("first_time", "No");
        savestring("Event_name", event_name);
        savestring("Game_type",game_type);
        savestring("handicap_score","0");
        Share_it.savestring("live_aminties",live_aminties);
        Location_Services.game_name=game_type;
        oragnization = event_name;

        round_info_tee_seleect = get_auth_token("selected_tee_postion");
        round_info_hole_seleect = get_auth_token("starting_hole");


        TextView evnt_name = view.findViewById(R.id.evnt_name);
        evnt_name.setTypeface(medium);
        evnt_name.setText(event_name);

        savestring("event_id", Event_id);

        TextView event_date = view.findViewById(R.id.event_date);
        event_date.setTypeface(regular);

        event_date.setText(event_course_date);

        tv_gps.setBackgroundResource(R.drawable.new_gray_button_bacgound);
        iv_score_board.setBackgroundResource(R.drawable.new_gray_button_bacgound);
        iv_leader_board.setBackgroundResource(R.drawable.new_gray_button_bacgound);
        iv_event_auction.setBackgroundResource(R.drawable.new_gray_button_bacgound);
        iv_event_home.setBackgroundResource(R.drawable.home_item_select);


        button_continue_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!event_status.equals("Live")){
                    show_alert_popup_for_home();
                }else {

                    if (round_info_hole_seleect == null || round_info_hole_seleect.equals(null) || round_info_hole_seleect.equals("null") || round_info_hole_seleect.equals("")) {
                        Intent intent1 = new Intent(getActivity(), Location_Services.class);
                        intent1.putExtra("id", Event_id);
                        intent1.putExtra("event_type", game_type);
                        savestring("page_type","map");
                        startActivity(intent1);
                    }else if (round_info_tee_seleect == null || round_info_tee_seleect.equals(null) || round_info_tee_seleect.equals("null") || round_info_tee_seleect.equals("")){
                        Intent intent1 = new Intent(getActivity(), Location_Services.class);
                        intent1.putExtra("id", Event_id);
                        intent1.putExtra("event_type", game_type);
                        savestring("page_type","map");
                        startActivity(intent1);
                    }else {
                        Intent intent1 = new Intent(getActivity(), View_course_map.class);
                        intent1.putExtra("home","home");
                        startActivity(intent1);
                    }
                }

            }
        });

        tv_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!event_status.equals("Live")){
                    show_alert_popup_for_home();
                }else {
                    NewEventDetailPage.event_type = "";
                    if (round_info_hole_seleect == null || round_info_hole_seleect.equals(null) || round_info_hole_seleect.equals("null") || round_info_hole_seleect.equals("")) {
                        Intent intent1 = new Intent(getActivity(), Location_Services.class);
                        intent1.putExtra("id", Event_id);
                        intent1.putExtra("event_type", game_type);
                        savestring("page_type", "map");
                        startActivity(intent1);
                    } else if (round_info_tee_seleect == null || round_info_tee_seleect.equals(null) || round_info_tee_seleect.equals("null") || round_info_tee_seleect.equals("")) {
                        Intent intent1 = new Intent(getActivity(), Location_Services.class);
                        intent1.putExtra("id", Event_id);
                        intent1.putExtra("event_type", game_type);
                        savestring("page_type", "map");
                        startActivity(intent1);
                    } else {
                        Intent intent1 = new Intent(getActivity(), View_course_map.class);
                        intent1.putExtra("home", "home");
                        startActivity(intent1);
                    }
                }

            }
        });

        iv_event_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!event_status.equals("Live")) {
                    show_alert_popup_for_home();
                } else {
                    Intent in = new Intent(getActivity(), NewEventDetailPage.class);
                    in.putExtra("id", Event_id);
                    in.putExtra("homepage", "1");
                    in.putExtra("event_type", "upcoming_or_live");
                    startActivity(in);
                }
            }
        });

        iv_score_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!event_status.equals("Live")) {
                    show_alert_popup_for_home();
                } else {
                    if (round_info_hole_seleect == null || round_info_hole_seleect.equals(null) || round_info_hole_seleect.equals("null") || round_info_hole_seleect.equals("")) {
                        Intent intent1 = new Intent(getActivity(), Location_Services.class);
                        intent1.putExtra("id", Event_id);
                        intent1.putExtra("event_type", game_type);
                        savestring("page_type", "score_card");
                        startActivity(intent1);
                    } else if (round_info_tee_seleect == null || round_info_tee_seleect.equals(null) || round_info_tee_seleect.equals("null") || round_info_tee_seleect.equals("")) {
                        Intent intent1 = new Intent(getActivity(), Location_Services.class);
                        intent1.putExtra("id", Event_id);
                        intent1.putExtra("event_type", game_type);
                        savestring("page_type", "score_card");
                        startActivity(intent1);
                    } else if (get_auth_token("Game_type").equals("shamble") || get_auth_token("Game_type").equals("bestball")) {
                        game_name = "bestball";
                        Intent intent1 = new Intent(getActivity(), BestBall_Scorecard.class);
                        intent1.putExtra("home", "home");
                        startActivity(intent1);
                    } else if (get_auth_token("Game_type").equals("stableford")) {
                        Intent intent1 = new Intent(getActivity(), StableFord_Score_Card.class);
                        intent1.putExtra("home", "home");
                        startActivity(intent1);
                    } else if (get_auth_token("Game_type").equals("modifiedstableford")) {
                        Intent intent1 = new Intent(getActivity(), Stable_modify_ScoreCard.class);
                        intent1.putExtra("home", "home");
                        startActivity(intent1);
                    } else {
                        Intent intent1 = new Intent(getActivity(), NormalScorreboard.class);
                        intent1.putExtra("home", "home");
                        startActivity(intent1);
                    }
                }
            }
        });

        iv_leader_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!event_status.equals("Live")) {
                    show_alert_popup_for_home();
                } else {
                    if (round_info_hole_seleect == null || round_info_hole_seleect.equals(null) || round_info_hole_seleect.equals("null") || round_info_hole_seleect.equals("")) {
                        Intent intent1 = new Intent(getActivity(), Location_Services.class);
                        intent1.putExtra("id", Event_id);
                        intent1.putExtra("event_type", game_type);
                        savestring("page_type", "leaderboard");
                        startActivity(intent1);
                    } else if (round_info_tee_seleect == null || round_info_tee_seleect.equals(null) || round_info_tee_seleect.equals("null") || round_info_tee_seleect.equals("")) {
                        Intent intent1 = new Intent(getActivity(), Location_Services.class);
                        intent1.putExtra("id", Event_id);
                        intent1.putExtra("event_type", game_type);
                        savestring("page_type", "leaderboard");
                    } else if (get_auth_token("Game_type").equals("shamble") || get_auth_token("Game_type").equals("bestball")) {
                        Intent intent1 = new Intent(getContext(), Bestbal_Leader_Board.class);
                        intent1.putExtra("home", "home");
                        startActivity(intent1);
                    } else if (get_auth_token("Game_type").equals("stableford")) {
                        Intent intent1 = new Intent(getContext(), Stable_ford_Leaderboard.class);
                        intent1.putExtra("home", "home");
                        startActivity(intent1);
                    } else if (get_auth_token("Game_type").equals("modifiedstableford")) {
                        Intent intent1 = new Intent(getContext(), Stable_modify_leader_board.class);
                        intent1.putExtra("home", "home");
                        startActivity(intent1);
                    } else {
                        Intent intent1 = new Intent(getContext(), NormalLeaderoard.class);
                        intent1.putExtra("home", "home");
                        startActivity(intent1);
                    }
                }
            }
        });

        iv_event_auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!event_status.equals("Live")){
                    show_alert_popup_for_home();
                }else {
                    in = new Intent(getContext(), NewLive_Auction.class);
                    savestring("play_type", "event");
                    savestring("event_id", get_auth_token("event_id"));
                    savestring("first_time", "No");
                    savestring("Event_name", event_name);
                    savestring("Game_type", game_type);
                    savestring("handicap_score", "0");
                    startActivity(in);
                }
            }
        });

        home_page_event_logo = view.findViewById(R.id.home_page_event_logo);
        if ( event_logo!= null) {
            Glide.with(getContext())
                    .load(event_logo).skipMemoryCache(false)
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(home_page_event_logo);
        }

    }

    private void show_alert_popup_for_home() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dialogview = inflater.inflate(R.layout.home_page_play_now_popup, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;

        TextView txt_reminder_title = alertDialog.findViewById(R.id.txt_home_page_title);
        txt_reminder_title.setTypeface(regular);

        ImageView img_cross = alertDialog.findViewById(R.id.img_cross);

        txt_reminder_title.setText(Html.fromHtml("<b>Event hasn't started This event opens</b><br>"+event_start_date));

        Button enable= alertDialog.findViewById(R.id.btn_home_page_countinue);
        enable.setTypeface(medium);

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        img_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void method_to_play_now() {
        TextView tv_start_round = view.findViewById(R.id.tv_start_round);
        tv_start_round.setTypeface(regular);
        tv_start_round.setText(Html.fromHtml("<b><big>Start Round</big></b> <br>Single Play"));

        TextView tv_tournament = view.findViewById(R.id.tv_tournament);
        tv_tournament.setTypeface(regular);
        tv_tournament.setText(Html.fromHtml("<b><big>Tournaments</big></b> <br>Participate in an event"));

        TextView tv_locator = view.findViewById(R.id.tv_locator);
        tv_locator.setTypeface(regular);
        tv_locator.setText(Html.fromHtml("<b><big>Course Locator</big></b> <br>GPS data on 38,000+ courses"));

    }

    private void method_to_load_top_section() {

        // home page in progress

        TextView tv_code_title = view.findViewById(R.id.tv_code_title);
        tv_code_title.setTypeface(medium);
        tv_code_title.setVisibility(View.VISIBLE);

        tv_code_sub_title = view.findViewById(R.id.tv_code_sub_title);
        tv_code_sub_title.setTypeface(regular);

        Button  btn_player_code = view.findViewById(R.id.btn_player_code);
        btn_player_code.setTypeface(regular);
        btn_player_code.setText(Html.fromHtml("7-digit <br><b><big>player code</big></b>"));
        btn_player_code.setOnClickListener(this);

        Button  btn_event_code = view.findViewById(R.id.btn_event_code);
        btn_event_code.setTypeface(regular);
        btn_event_code.setText(Html.fromHtml("6-digit <br><b><big>event code</big></b>"));
        btn_event_code.setOnClickListener(this);


        enter_event_code = view.findViewById(R.id.enter_event_code);
        tv_code_sub_title.setTypeface(regular);
        enter_event_code.setOnClickListener(this);


        layout_two_button = view.findViewById(R.id.layout_two_button);
    }

    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.cancel:
                hiddenInputMethod();
                edittext1.requestFocus();
                test1View.startAnimation(sidedown);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        test1View.setVisibility(View.GONE);
                    }
                }, 500);
                break;

            case R.id.ll_your_round:

                    if (isLocationEnabled(getContext())) {
                        start_playing();
                    } else {

                        Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent1);
                    }

                break;

            case R.id.ll_rournaments:
                    event_view.setBackgroundColor(getResources().getColor(R.color.colorbtn));
                    courses_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                    euser_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                    home_view.setBackgroundColor(getResources().getColor(R.color.colorblack));

                    user_text.setTextColor(getResources().getColor(R.color.colorwhite));
                    home_text.setTextColor(getResources().getColor(R.color.colorwhite));
                    event_text.setTextColor(getResources().getColor(R.color.btn_color));
                    courses_text.setTextColor(getResources().getColor(R.color.colorwhite));

                    imag_home.setBackground(getResources().getDrawable(R.drawable.white_home_bottom));
                    imag_event.setBackground(getResources().getDrawable(R.drawable.blue_home_event));
                    imag_course.setBackground(getResources().getDrawable(R.drawable.home_corse));
                    imag_my_account.setBackground(getResources().getDrawable(R.drawable.home_user));

                    Fragment profilefragment = new EventFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, profilefragment);
                    fragmentTransaction.commit();

                break;

            case R.id.ll_courses:

                    if (isLocationEnabled(getContext())) {
                        event_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                        courses_view.setBackgroundColor(getResources().getColor(R.color.colorbtn));
                        euser_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                        home_view.setBackgroundColor(getResources().getColor(R.color.colorblack));

                        user_text.setTextColor(getResources().getColor(R.color.colorwhite));
                        home_text.setTextColor(getResources().getColor(R.color.colorwhite));
                        event_text.setTextColor(getResources().getColor(R.color.colorwhite));
                        courses_text.setTextColor(getResources().getColor(R.color.colorbtn));

                        imag_home.setBackground(getResources().getDrawable(R.drawable.white_home_bottom));
                        imag_event.setBackground(getResources().getDrawable(R.drawable.home_event));
                        imag_course.setBackground(getResources().getDrawable(R.drawable.blue_home_course));
                        imag_my_account.setBackground(getResources().getDrawable(R.drawable.home_user));

                        Share_it.savestring("view", "map");

                        Fragment courses = new AllCourseOnMap();
                        FragmentTransaction coursetr = getFragmentManager().beginTransaction();
                        coursetr.replace(R.id.content_frame, courses);
                        coursetr.commit();

                    } else {

                        Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent1);
                    }

                break;

            case R.id.enter_event_code:

                method_to_open_event_code_popup();

                break;
        }
    }

    private void start_playing() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogview = inflater.inflate(R.layout.start_your_round, null);
        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;
        TextView txt_round_title = alertDialog.findViewById(R.id.txt_round_title);
        TextView txt_info = alertDialog.findViewById(R.id.txt_info);
        txt_round_title.setTypeface(semibold);
        txt_info.setTypeface(regular);
        Button enable= alertDialog.findViewById(R.id.btn_countinue);
        enable.setTypeface(medium);
        TextView disable = alertDialog.findViewById(R.id.btn_cancel);
        disable.setTypeface(semibold);

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                event_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                courses_view.setBackgroundColor(getResources().getColor(R.color.colorbtn));
                euser_view.setBackgroundColor(getResources().getColor(R.color.colorblack));
                home_view.setBackgroundColor(getResources().getColor(R.color.colorblack));

                user_text.setTextColor(getResources().getColor(R.color.colorwhite));
                home_text.setTextColor(getResources().getColor(R.color.colorwhite));
                event_text.setTextColor(getResources().getColor(R.color.colorwhite));
                courses_text.setTextColor(getResources().getColor(R.color.colorbtn));

                imag_home.setBackground(getResources().getDrawable(R.drawable.white_home_bottom));
                imag_event.setBackground(getResources().getDrawable(R.drawable.home_event));
                imag_course.setBackground(getResources().getDrawable(R.drawable.blue_home_course));
                imag_my_account.setBackground(getResources().getDrawable(R.drawable.home_user));

                Share_it.savestring("view","map");

                Fragment courses = new AllCourseOnMap();
                FragmentTransaction coursetr = getFragmentManager().beginTransaction();
                coursetr.replace(R.id.content_frame, courses);
                coursetr.commit();

            }
        });
        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void error(String s) {
        Snackbar snackbar = Snackbar.make(event_layout, s, Snackbar.LENGTH_LONG);
        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
        view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtv.setTextSize(TypedValue
                .COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorsnakbar));
        snackbar.show();
    }

    public void alertdailogbox(final String value){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                if(value.equals("eventlist")) {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("eventlist");
                    } else {
                        methodforcalleventlist();
                    }
                }
                else if(value.equals("eventlist"))
                {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("eventcode");
                    } else {
                        methodforcalleventcode(code);
                    }
                }

            }
        });
        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    public void methodforcalleventcode(String code){
        JSONObject user_code = new JSONObject();
        try {
            user_code.put("code",code.toUpperCase());
            ProgressDialog.getInstance().showProgress(getActivity());
        }catch (Exception E){
            //nothing
        }
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"events/event_code",user_code,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("-----res------"+response);
                        try
                        {
                            ProgressDialog.getInstance().hideProgress();
                            if (response.getString("status").equals("Success")){
                                if (response.getString("purchase_type").equals("unpurchased")) {
                                    JSONObject obj = response.getJSONObject("event");
                                    double pricevalue=  Double.parseDouble(response.getString("price"));
                                    in = new Intent(getActivity(), Event_Code_valid.class);
                                    Share_it.savestring("ticket_price", String.format("%.2f",pricevalue));
                                    Share_it.savestring("name", obj.getString("name"));
                                    Share_it.savestring("code", obj.getString("code"));
                                    Share_it.savestring("id", obj.getString("id"));
                                    Share_it.savestring("organization_name", obj.getString("organization_name"));
                                    Share_it.savestring("banner_image", obj.getString("banner_image"));
                                    Share_it.savestring("wallete", response.getString("wallet"));
                                    Share_it.savestring("aminities", response.getString("ticket_amenities"));
                                    Share_it.savestring("imge_url", obj.getString("avatar_url"));
                                    Share_it.savestring("start_date", obj.getString("start_date"));
                                    Share_it.savestring("location", obj.getString("get_golf_location"));
                                    Share_it.savestring("get_course_name", obj.getString("get_course_name"));
                                    savestring("play_type", "event");
                                    savestring("premium_type", "");
                                    startActivity(in);
                                    edittext1.getText().clear();

                                }

                                test1View.startAnimation(sidedown);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        test1View.setVisibility(View.GONE);
                                    }
                                }, 500);


                                if (response.getString("purchase_type").equals("purchased")){
                                    Intent in = new Intent(getActivity(), NewEventDetailPage.class);
                                    in.putExtra("id",response.getString("event_id"));
                                    in.putExtra("homepage","1");
                                    in.putExtra("event_type","upcoming_or_live");
                                    startActivity(in);
                                }

                            }else {
                                Toast.makeText(getContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                                edittext1.getText().clear();
                                edittext1.requestFocus();

                            }

                        }catch (Exception e){
                            //nothing
                        }
                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                ProgressDialog.getInstance().hideProgress();
                                Alert_Dailog.showNetworkAlert(getActivity());
                            }else {
                                ProgressDialog.getInstance().hideProgress();
                                error("Invalid code");
                            }
                        }else {
                            ProgressDialog.getInstance().hideProgress();
                            Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

    public void methodforcalleventlist(){
        ProgressDialog.getInstance().showProgress(getActivity());
        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.GET, Splash_screen.imageurl+"api/v2/hole_info/continue-round",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("-------round note---"+response);
                            JSONObject round_info = response.getJSONObject("round_info");
                            savestring("handicap",round_info.getString("handicap"));
                            savestring("selected_tee_postion",round_info.getString("selected_tee"));
                            savestring("selected_hole_postion",round_info.getString("selected_hole"));
                            savestring("starting_hole",round_info.getString("selected_hole"));
                            round_info_tee_seleect  = round_info.getString("selected_tee");

                            savestring("cross_scoring",round_info.getString("cross_scoring"));

                            String  selected_tee = round_info.getString("selected_tee");
                            if (selected_tee.equals(null) || selected_tee.equals("") || selected_tee==null || selected_tee.equals("null") ){
                                selected_tee="";
                            }

                            savestring("Game_type",round_info.getString("game_type"));

                            savestring("CGB_event_code",round_info.getString("cgb_code"));
                            savestring("current_event_code",round_info.getString("event_code"));

                            if (!round_info.getString("cgb_code").equals(round_info.getString("event_code"))){
                                iv_event_auction.setVisibility(View.VISIBLE);
                            }

                            Location_Services.game_name=round_info.getString("game_type");

                            Location_Services.game_type=round_info.getString("game_type");

                            play_game_name.setText(round_info.getString("game_detail"));
                            course_name.setText(round_info.getString("course_name"));

                            Location_Services.selected_tee=selected_tee;

                            event_type = round_info.getString("type");

                            if (round_info.getString("type").equals("course")) {
                                savestring("event_name",round_info.getString("course_name"));
                                LinearLayout course_flow = view.findViewById(R.id.course_flow);
                                course_flow.setVisibility(View.VISIBLE);


                                home_page_coordinator.setVisibility(View.VISIBLE);
                                home_page_layout.setVisibility(View.VISIBLE);
                                event_code_btn.setVisibility(View.VISIBLE);
                                savestring("event_id",round_info.getString("player_id"));

                                method_to_declare_course_card(round_info.getString("course_name"), round_info.getString("thru"), round_info.getString("gross_score_stroke"), round_info.getString("total_score"), round_info.getString("event_course_date"), round_info.getString("player_id"));

                                Handicap_hole_tee_api_Integration(round_info.getString("player_id"));

                            } else {

                                event_start_date = round_info.getString("event_course_date");

                                live_event_text.setText(round_info.getString("event_status"));

                                home_page_coordinator.setVisibility(View.VISIBLE);
                                home_page_layout.setVisibility(View.GONE);
                                event_code_btn.setVisibility(View.GONE);

                                LinearLayout event_flow = view.findViewById(R.id.event_flow);
                                event_flow.setVisibility(View.VISIBLE);

                                savestring("event_name",round_info.getString("event_name"));
                                method_to_declare_event_cards(round_info.getString("event_name"), round_info.getString("event_course_date"), round_info.getString("event_id"), round_info.getString("event_image"), round_info.getString("game_type"),round_info.getString("event_status"));
                                savestring("event_id",round_info.getString("event_id"));

                                Handicap_hole_tee_api_Integration(round_info.getString("event_id"));

                                String ticket_amenities = round_info.getString("ticket_amenities");
                                if (ticket_amenities != "null") {
                                    JSONObject ticketamenities = new JSONObject(ticket_amenities);
                                   Iterator<String> keys = ticketamenities.keys();
                                    while (keys.hasNext()) {
                                        String key = keys.next();
                                        String value = ticketamenities.getString(key);
                                        live_aminties += value + " " + key + "\n";
                                    }
                                }
                            }

                            savestring("aminities",live_aminties);
                            savestring("live_aminties",live_aminties);

                            // else user_type = player new tab
                            ProgressDialog.getInstance().hideProgress();
                        }catch (Exception e){

                            ProgressDialog.getInstance().hideProgress();

                            if((get_auth_token("user_player_type").equals("user"))) {
                                home_page_coordinator.setVisibility(View.GONE);
                                home_page_layout.setVisibility(View.VISIBLE);
                                event_code_btn.setVisibility(View.VISIBLE);
                            }else {
                                home_page_layout.setVisibility(View.VISIBLE);
                                event_code_btn.setVisibility(View.VISIBLE);
                            }

                        }

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
                                Alert_Dailog.showNetworkAlert(getActivity());
                            }else {
                                ProgressDialog.getInstance().hideProgress();
                                Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            home_page_coordinator.setVisibility(View.GONE);
                            home_page_layout.setVisibility(View.VISIBLE);
                            event_code_btn.setVisibility(View.VISIBLE);
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


    public void method_to_open_event_code_popup(){
        LayoutInflater inflater1 = LayoutInflater.from(getContext());
        View viewq = inflater1.inflate(R.layout.custome_code_enter , null);
        AlertDialog  alertDialog_event = new AlertDialog.Builder(getActivity()).setView(viewq).show();

        edittext1 = viewq.findViewById(R.id.edittext1);

        TextView code_title = viewq.findViewById(R.id.code_title);
        cancel  = viewq.findViewById(R.id.cancel);
        eventcode  = viewq.findViewById(R.id.eventcode);
        event_layout = viewq.findViewById(R.id.event_layout);

        edittext1.setTypeface(medium);

        code_title.setTypeface(regular);

        hiddenInputMethod();
        edittext1.getText().clear();
        edittext1.requestFocus();

        TextView cancel = viewq.findViewById(R.id.cancel);
        TextView eventcode = viewq.findViewById(R.id.eventcode);

        eventcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = edittext1.getText().toString() ;
                hiddenInputMethod();
                if(code.isEmpty()){
                    Toast.makeText(getContext(),"Please enter event code",Toast.LENGTH_SHORT).show();
                }else if (code.length() <= 5) {
                    Toast.makeText(getContext(),"Please enter 6 digit code",Toast.LENGTH_SHORT).show();
                } else {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("eventcode");
                    } else {
                        methodforcalleventcode(code);
                    }
                }
                        alertDialog_event.dismiss();
                        alertDialog_event.setCanceledOnTouchOutside(false);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_event.dismiss();
                alertDialog_event.setCanceledOnTouchOutside(false);
            }
        });
        alertDialog_event.setCanceledOnTouchOutside(false);
        alertDialog_event.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


    private void Handicap_hole_tee_api_Integration(String event_id) {

        String update_API="api/v2/hole_info/course-info/" + event_id;

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.imageurl+update_API, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("------hole-------"+response);
                        try {
                            ArrayList<String> tee_selected = new ArrayList<>();
                            ArrayList<String> tee_selecte_value = new ArrayList<>();
                            // tee array value get
                            JSONArray tee_list = response.getJSONArray("tee_list");
                            for (int i=0;i<tee_list.length();i++){
                                JSONObject jsonObject = tee_list.getJSONObject(i);
                                tee_selecte_value.add(jsonObject.getString("name"));
                                String selected_tee_yards = jsonObject.getString("yards_total")+" yds ("+jsonObject.getString("rating")+"/"+jsonObject.getString("slope")+")";
                                tee_selected.add(selected_tee_yards);
                            }

                            if (round_info_tee_seleect.equals(null) || round_info_tee_seleect.equals("") || round_info_tee_seleect==null || round_info_tee_seleect.equals("null") ){
                            }else {
                                int index = tee_selecte_value.indexOf(round_info_tee_seleect);
                                slected_yards_tee = tee_selected.get(index);
                            }

                            String course_data = "<b>"+get_auth_token("event_name")+"</b>"+"<br>Starting "+ "Hole "+get_auth_token("starting_hole")+"<b>" + " - "+ "</b>"+ get_auth_token("selected_tee_postion")+" "+slected_yards_tee+"<br>"+get_auth_token("Game_type");

                            savestring("leader_board_map_course",course_data);
                            savestring("yards_value",slected_yards_tee);

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

}