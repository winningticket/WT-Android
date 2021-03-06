package com.winningticketproject.in.Shamble_flow;

import android.content.Context;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.BestBallFlow.Bestball_Player_list;
import com.winningticketproject.in.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.IndividulaGameType.Location_Services.game_name;
import static com.winningticketproject.in.SignInSingup.Splash_screen.italic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.mediumitalic;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.setGridViewHeightBasedOnChildren;
import static com.winningticketproject.in.AppInfo.Share_it.setListViewHeightBasedOnChildren;

public class Shamble_Player_list_activity extends AppCompatActivity implements View.OnClickListener {

    TextView tvholes,crossTxt,info_gross_score;
    LinearLayout gross_score_layout,lay_player_name,layout_gross_score;
    Button continu_button;
    ListView lv_player_list;
    int backposition = -1;
    ScrollView scroll_view;
    String player_name,player_id,hole="",par="";
    String Eagle="",Birdie="",par_postion="",Bogey="",double_Bogey;
    RelativeLayout player_name_layout;
    public Map<String, String> score;
    public Map<String, String> par_position ;

    android.support.v7.app.AlertDialog alertDialog_2;

    public static String notes_values="";

    ImageView tvleft,tv_right,tv_center,gross_Score_btn_back;
    ImageView two_right,two_left;

    Button two_one,two_two,two_three,two_four,two_five;

    TextView tv_fairway_hit,tv_green_regu,tv_putts,tv_selected_user,tv_gs_info,gross_Score_course_name;
    Button save_grsoss_info;
    static public String first_row="",second_row="",third_row="";
    public static ArrayList<Bestball_Player_list> player_in = new ArrayList<>();

    int stats_value = 0;

    GridView gridview;
    View fair_way_hits,green_in_regular,gross_putts;

    TextView tv_selected_score,add_stats_text,add_stats_icon;

    LinearLayout et_layout;
    Button btn_edit_note,btn_delete_note,btn_add_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shamble__player_list_activity);


        Intent in = getIntent();
        hole = in.getStringExtra("hole");
        par = in.getStringExtra("par");
        player_name = in.getStringExtra("player_name");

        info_gross_score = findViewById(R.id.info_gross_score);
        info_gross_score.setTypeface(italic);

        if(game_name.equals("shamble")){
            info_gross_score.setText("During a shamble, all team members have the \n ability to edit the team score from their device");
        }else if (game_name.equals("bestball")){
            info_gross_score.setText("During a best ball, all team members have the \n ability to edit the team score from their device");
        }

//        all_players = findViewById(R.id.all_players);
//        all_players.setTypeface(medium);

        if (get_auth_token("Game_type").equals("shamble") || game_name.equals("bestball")){
//            all_players.setText(Location_Services.Plyaer_name);
        }else {
//            all_players.setVisibility(View.GONE);
            info_gross_score.setVisibility(View.GONE);
        }

        gross_Score_course_name = findViewById(R.id.gross_Score_course_name);
        gross_Score_course_name.setTypeface(medium);
        gross_Score_course_name.setText(get_auth_token("Event_name"));

        tvholes = findViewById(R.id.tvholes);

        tvholes.setText("Hole "+hole+" - "+"Par "+par);

        crossTxt = findViewById(R.id.crossTxt);
        crossTxt.setTypeface(semibold);
        crossTxt.setVisibility(View.GONE);

//        plaer_name = findViewById(R.id.plaer_name);
//        plaer_name.setTypeface(medium);
//        plaer_name.setText(player_name);

        continu_button = findViewById(R.id.continu_button);
        continu_button.setTypeface(medium);

        continu_button.setOnClickListener(this);

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


        player_name_layout = findViewById(R.id.player_name_layout);

        gross_score_layout = findViewById(R.id.gross_score_layout);

        layout_gross_score = findViewById(R.id.layout_gross_score);
        lay_player_name = findViewById(R.id.lay_player_name);


        scroll_view = findViewById(R.id.scroll_view);

        lv_player_list = findViewById(R.id.lv_player_list);

        lv_player_list.setAdapter(new Player_list_adapter(Shamble_Player_list_activity.this,player_in));
        setListViewHeightBasedOnChildren(lv_player_list);
        continu_button.setVisibility(View.GONE);
        lv_player_list.setFocusable(false);

        lv_player_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                final LinearLayout linear_row= view.findViewById(R.id.linear_row);
                linear_row.setBackgroundResource(R.drawable.select_hole_postion);
                LinearLayout BackSelectedItem = (LinearLayout) lv_player_list.getChildAt(backposition);
                if (backposition != position)
                {
                    if (BackSelectedItem!=null){
                        BackSelectedItem.setSelected(false);
                        BackSelectedItem.setBackgroundResource(R.drawable.back);
                    }
                }
                scroll_view.smoothScrollTo(0,0);
                backposition = position;
                player_name = player_in.get(position).getPlayer_name();
                player_id = player_in.get(position).getId();
                continu_button.setVisibility(View.VISIBLE);
                final Animation myAnim = AnimationUtils.loadAnimation(Shamble_Player_list_activity.this, R.anim.button_zoom);
                linear_row.startAnimation(myAnim);
            }
        });
    }

    // adapter declaration

    private final class Player_list_adapter extends BaseAdapter {
        ArrayList<Bestball_Player_list> shamble_player_infos;
        private Player_list_adapter(Context context,ArrayList<Bestball_Player_list> shamble_player_infos) {
            this.shamble_player_infos = shamble_player_infos;
        }
        @Override
        public int getCount() {
            return shamble_player_infos.size();
        }

        @Override
        public Object getItem(final int position) {
            return shamble_player_infos.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shamble_custome_player_info, parent, false);
            }
            final TextView tv_player_name = view.findViewById(R.id.tv_player_name);

            tv_player_name.setTypeface(regular);
            tv_player_name.setText(shamble_player_infos.get(position).getPlayer_name());

            return view;
        }
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case R.id.save_grsoss_info :

                if (backposition==-1){
                    Toast.makeText(getApplicationContext(),"Please select gross score",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE",score.get(backposition+1+"")+"");
                    intent.putExtra("user_id",player_id);
                    setResult(RESULT_OK,intent);
                    finish();//finishing activity
                }


                break;

            case R.id.continu_button:

                player_name_layout.setVisibility(View.VISIBLE);
                gross_score_layout.setVisibility(View.VISIBLE);

                lv_player_list.setVisibility(View.GONE);

                layout_gross_score.setVisibility(View.GONE);

                lay_player_name.setVisibility(View.VISIBLE);

                method_to_show_player_GS();

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
        tv_hole_par.setText("Hole "+hole+" - "+"Par "+par);

        final EditText et_add_note = premium_purchase_view.findViewById(R.id.et_add_note);
        et_add_note.setTypeface(regular);
        et_add_note.setText(notes_values+"");

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
                    notes_values=et_add_note.getText().toString();
                    alertDialog_2.dismiss();

                }

            }
        });
    }


    private void method_to_show_player_GS() {

        tv_fairway_hit  = findViewById(R.id.tv_fairway_hit);
        tvleft  = findViewById(R.id.tvleft);
        tv_center  = findViewById(R.id.tv_center);
        tv_right  = findViewById(R.id.tv_right);

        tvleft.setOnClickListener(this);
        tv_center.setOnClickListener(this);
        tv_right.setOnClickListener(this);

        tv_fairway_hit.setTypeface(regular);

        tv_green_regu  = findViewById(R.id.tv_green_regu);
        two_left  = findViewById(R.id.two_left);
        two_right  = findViewById(R.id.two_right);

        tv_green_regu.setTypeface(regular);

        two_left.setOnClickListener(this);
        two_right.setOnClickListener(this);

        tv_gs_info = findViewById(R.id.tv_gs_info);
        tv_gs_info.setTypeface(mediumitalic);
        tv_gs_info.setVisibility(View.VISIBLE);

        tv_selected_user = findViewById(R.id.tv_selected_user);
        tv_selected_user.setTypeface(mediumitalic);
        tv_selected_user.setText(player_name);
        tv_selected_user.setVisibility(View.VISIBLE);

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


        // add notes
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


        if (Integer.parseInt(par)==3){
            fair_way_hits.setVisibility(View.GONE);
            first_row="";
        }

        gross_putts.setVisibility(View.GONE);
        green_in_regular.setVisibility(View.GONE);

        add_stats_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (stats_value==0){
                    add_stats_text.setText("Close Stats");
                    stats_value=1;
                    add_stats_icon.setText("\uf0d8");
                    add_stats_icon.setTypeface(webfont);

                    if (Integer.parseInt(par)==3){
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

        save_grsoss_info = findViewById(R.id.save_grsoss_info);
        save_grsoss_info.setOnClickListener(this);

        score = new HashMap<String, String>();
        par_position = new HashMap<String, String>();


        Eagle = Integer.parseInt(par)-2 +"";
        Birdie = Integer.parseInt(par)-1 +"";
        par_postion = Integer.parseInt(par)-0 +"";
        Bogey = Integer.parseInt(par)+1 +"";
        double_Bogey = Integer.parseInt(par)+2 +"";


        par_position.put(Eagle,"Eagle");
        par_position.put(Birdie,"Birdie");
        par_position.put(par_postion,"Par");
        par_position.put(Bogey,"Bogey");
        par_position.put(double_Bogey,"Double Bogey");

        gridview = findViewById(R.id.gridview);

        score.clear();
        for (int i=1;i<=26;i++){
            score.put(i+"",i+"");
        }

        gridview.setAdapter(new Shamble_GS(Shamble_Player_list_activity.this,score,par_position));
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
            objects.put("hole_number",hole+"");
            objects.put("user_id",player_id);
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
//
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


//        final LinearLayout linear_row = premium_purchase_view.findViewById(R.id.linear_row);
//        linear_row.setBackgroundResource(R.drawable.selected_button);

        System.out.println("----postion--  "+backposition);

        gross_score_poup_gridview.setAdapter(new GridAdapter2(Shamble_Player_list_activity.this, score2, par_position));
        Share_it.setGridViewHeightBasedOnChildren(gross_score_poup_gridview,3);

        gross_score_poup_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                final LinearLayout linear_row = view.findViewById(R.id.linear_row);
                linear_row.setBackgroundResource(R.drawable.selected_button);
                LinearLayout BackSelectedItem = (LinearLayout) gross_score_poup_gridview.getChildAt(backposition);
                if (backposition != position) {
                    if (BackSelectedItem != null)
                    {
                        BackSelectedItem.setSelected(false);
                        BackSelectedItem.setBackgroundResource(R.drawable.new_back);
                    }
                }
                backposition = position;
            }
        });

    }


    private void Delete_note_method() {
        String url = "";
        if (Share_it.get_auth_token("play_type").equals("free") || Share_it.get_auth_token("play_type").equals("paid")){
            url = Splash_screen.url + "hole_info/delete-note/"+ get_auth_token("event_id");
        }else if (Share_it.get_auth_token("play_type").equals("event")){
            url =  Splash_screen.url + "hole_info/delete-note/"+ get_auth_token("event_id");
        }

        JSONObject object = new JSONObject();
        try {
            object.put("hole_number",hole);
            object.put("user_id",player_id);
        }catch (Exception e){ }

        com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().showProgress(this);

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                            if (response.getString("status").equals("Success")){
                                notes_values="";
                                btn_add_note.setVisibility(View.VISIBLE);
                                et_layout.setVisibility(View.GONE);
                            }
                            Toast.makeText(Shamble_Player_list_activity.this,response.getString("message"),Toast.LENGTH_LONG).show();

                            System.out.println("----response----------"+response);


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
                                Alert_Dailog.showNetworkAlert(Shamble_Player_list_activity.this);
                            } else {
                                Toast.makeText(Shamble_Player_list_activity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Shamble_Player_list_activity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

    public void method_to_load_api(JSONObject objects) {
        try {
            com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().showProgress(Shamble_Player_list_activity.this);
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url + "hole_info/view_score/event/"+ get_auth_token("event_id"), objects,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                System.out.println("---------"+response);

                                continu_button.setVisibility(View.GONE);

                                ProgressDialog.getInstance().hideProgress();

                                if (response.getString("user_type").equals("premium")) {
                                    notes_values = response.getString("notes");
                                    if (notes_values.equals("null") || notes_values.equals(null)) {
                                        notes_values="";
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
                                    backposition =-1;

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
                                    Alert_Dailog.showNetworkAlert(Shamble_Player_list_activity.this);
                                } else {
                                    Toast.makeText(Shamble_Player_list_activity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Shamble_Player_list_activity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            //nothing
        }
    }

    private final class Shamble_GS extends BaseAdapter {

        Map<String, String> items= new HashMap<String, String>();
        Map<String, String> itemss= new HashMap<String, String>();

        /**
         * Default constructor
         * @param items to fill data to
         */
        private Shamble_GS(Context context,Map<String, String> items, Map<String, String> itemss) {
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

                text.setText(items.get(position+1+"")+"");
                String valuess = itemss.get(position+1+"");

                text.setTypeface(italic);
                text2.setTypeface(regular);

                if (valuess==null){
                    text2.setText("");
                }else {
                    text2.setText(valuess);
                }

            }


            return view;
        }
    }

    @Override
    public void onBackPressed() {

    }

}