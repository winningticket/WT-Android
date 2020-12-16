package com.winningticketproject.in.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.AppInfo.TeeData;
import com.winningticketproject.in.EventTab.Score_board_new;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.IndividulaGameType.Location_Services.selected_hole;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.setListViewHeightBasedOnChildren;

public class Select_tree extends AppCompatActivity implements View.OnClickListener {
    int backposition = -1;
    TextView player_name;
    TextView dummy_text;
    TextView hole_info;
    Button continu_button;
    String select_hole="",auth_token,event_id,url_value;
    ProgressDialog pd;
    LinearLayout select_hole_root;
    ListView tee_listview;
    ScrollView scroll_view;
    ArrayList<TeeData> teeData = new ArrayList<>();
    ImageButton back_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tree);

        select_hole_root = findViewById(R.id.select_hole_root);

        player_name = findViewById(R.id.player_name);
        dummy_text = findViewById(R.id.dummy_text);
        continu_button = findViewById(R.id.continu_button);

        player_name.setTypeface(regular);
        dummy_text.setTypeface(medium);
        continu_button.setTypeface(medium);

        scroll_view = findViewById(R.id.scroll_view);

        auth_token = get_auth_token("auth_token");
        event_id = get_auth_token("event_id");

        tee_listview = findViewById(R.id.tee_listview);

        player_name.setText(get_auth_token("Event_name"));

        hole_info = findViewById(R.id.hole_info);
        hole_info.setTypeface(regular);

//        event_or_course_name = findViewById(R.id.event_or_course_name);
//        event_or_course_name.setTypeface(regular);

        if (!isNetworkAvailable()) {
            alertdailogbox("list_api");
        }
        else
        {
            listofholeandparfromapi();
        }


        back_text = findViewById(R.id.tee_btn_back);
        back_text.setOnClickListener(this);

        tee_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                final LinearLayout linear_row= view.findViewById(R.id.linear_row);
                linear_row.setBackgroundResource(R.drawable.select_hole_postion);
                LinearLayout BackSelectedItem = (LinearLayout) tee_listview.getChildAt(backposition);
                if (backposition != position)
                {
                    if (BackSelectedItem!=null){
                        BackSelectedItem.setSelected(false);
                        BackSelectedItem.setBackgroundResource(R.drawable.back);
                    }
                }
                scroll_view.smoothScrollTo(0,0);
                backposition = position;
                Share_it.savestring("tee_details",teeData.get(position).getName()+" "+teeData.get(position).getYards_total()+" yrds ("+teeData.get(position).getRating()+"/"+teeData.get(position).getSlope()+")");
                select_hole = teeData.get(position).getName();
                continu_button.setVisibility(View.VISIBLE);
                final Animation myAnim = AnimationUtils.loadAnimation(Select_tree.this, R.anim.button_zoom);
                linear_row.startAnimation(myAnim);
            }
        });

        continu_button.setOnClickListener(this);
    }

    private void listofholeandparfromapi() {
        try {
            pd = new ProgressDialog(Select_tree.this);
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();

        }catch (Exception E){
            //nothing
        }

        if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")){
            url_value = "hole_info/player/tee_list/"+event_id;
        }else if (get_auth_token("play_type").equals("event")){
            url_value = "hole_info/tee_list/"+event_id;
        }

        System.out.println("------url_value------"+url_value);


        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+url_value,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                                  if (pd.isShowing()){
                                      pd.dismiss();
                                   }
                            JSONArray hole_list = response.getJSONArray("tee_list");
                            for (int i = 0 ;i<hole_list.length();i++){
                                JSONObject hole_obj = hole_list.getJSONObject(i);

                                String rating =hole_obj.getString("rating");
                                String slope =hole_obj.getString("slope");
                                String name =hole_obj.getString("name");
                                String yards_total =hole_obj.getString("yards_total");

                                if (rating.equals("") || rating.equals(null) || rating.equals("null")) {
                                    rating = "0";
                                }

                                if (slope.equals("") || slope.equals(null) || slope.equals("null")) {
                                    slope = "0";
                                }

                                if (name.equals("") || name.equals(null) || name.equals("null")) {
                                    name = "Not Mentioned";
                                }

                                if (yards_total.equals("") || yards_total.equals(null) || yards_total.equals("null")) {
                                    yards_total = "0";
                                }

                                teeData.add(new TeeData(name,rating,slope,yards_total));

                            }
                            tee_listview.setAdapter(new GridAdapter(Select_tree.this,teeData));
                            setListViewHeightBasedOnChildren(tee_listview);
                            continu_button.setVisibility(View.GONE);
                            tee_listview.setFocusable(false);

                            if (teeData.isEmpty()) {
                                tee_listview.setVisibility(View.GONE);
                                hole_info.setVisibility(View.VISIBLE);
                            }
                            else {
                                tee_listview.setVisibility(View.VISIBLE);
                                hole_info.setVisibility(View.GONE);
                            }


                        }catch (Exception e){
                            //nothing

                            if (teeData.isEmpty()) {
                                tee_listview.setVisibility(View.GONE);
                                hole_info.setVisibility(View.VISIBLE);
                            }
                            else {
                                tee_listview.setVisibility(View.VISIBLE);
                                hole_info.setVisibility(View.GONE);
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
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Alert_Dailog.showNetworkAlert(Select_tree.this);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                error("Invalid code");
                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(Select_tree.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }



    private final class GridAdapter extends BaseAdapter {
        ArrayList<TeeData> teeData;
        private GridAdapter(Context context,ArrayList<TeeData> teeData) {
            this.teeData = teeData;
        }
        @Override
        public int getCount() {
            return teeData.size();
        }

        @Override
        public Object getItem(final int position) {
            return teeData.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_tee_selection, parent, false);
            }
            final TextView text = view.findViewById(R.id.tv_course_rating);
            final TextView text2 = view.findViewById(R.id.tv_total_yards);

            text.setTypeface(regular);
            text2.setTypeface(regular);

            String course_rating = "<b>" + teeData.get(position).getName() + "</b> " + "( Rating "+teeData.get(position).getRating()+" / slope "+ teeData.get(position).getSlope()+" )";
            String course_yards = "<b>" + "Total Yards:" + "</b> " + teeData.get(position).getYards_total();

            text.setText(Html.fromHtml(course_rating));
            text2.setText(Html.fromHtml(course_yards));

            return view;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_purchage:
                finish();
                break;
            case R.id.continu_button:
                if (select_hole.equals("") || select_hole.equals(null) || select_hole.equals("null")){
                    Toast.makeText(getApplicationContext(),"Please select tee",Toast.LENGTH_LONG).show();
                }else {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("update_hole");
                    }
                    else
                    {
                        Apicalforselecthole();
                    }
                }
                break;
            case R.id.tee_btn_back:
                finish();
                break;
        }
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
                if(value.equals("update_hole")) {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("update_hole");
                    } else {
                        Apicalforselecthole();
                    }
                }
                else if(value.equals("list_api"))
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("list_api");
                    } else {
                        listofholeandparfromapi();
                    }
                }

            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }





    private void Apicalforselecthole() {
        if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")){
            url_value = "hole_info/player/tee_list_select/"+event_id;
        }else if (get_auth_token("play_type").equals("event")){
            url_value = "hole_info/tee_list_select/"+event_id;
        }
        JSONObject user_code = new JSONObject();
        try {
            pd = new ProgressDialog(Select_tree.this);
            pd.setMessage("Please wait.");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
            user_code.put("selected_tee",select_hole);
        }catch (Exception E){
            //nothing
        }

        System.out.println(url_value+"-----user-------"+user_code);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+url_value,user_code,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            System.out.println("-----resp-----"+response);
                            pd.dismiss();
                            if (response.getString("status").equals("Success")){
                                if (selected_hole.equals("null") || selected_hole.equals(null)){
                                    Intent intent1 = new Intent(getApplicationContext(), Select_hole.class);
                                    startActivity(intent1);
                                    finish();
                                }else {
                                    Intent intent1 = new Intent(getApplicationContext(), Score_board_new.class);
                                    Share_it.savestring("selected_hole_postion",selected_hole);
                                    startActivity(intent1);
                                    finish();
                                }
                            }else {
                                error(response.getString("message"));
                            }

                        }catch (Exception e){
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
                            if (error.networkResponse.statusCode==401){
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Alert_Dailog.showNetworkAlert(Select_tree.this);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}

                                error("Some thing went wrong");
                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(Select_tree.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }

    @SuppressLint("WrongConstant")
    private void error(String message) {
        Snackbar snackbar = Snackbar.make(select_hole_root, message , Snackbar.LENGTH_LONG);
        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
        View  view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtv.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(Select_tree.this, R.color.colorsnakbar));
        snackbar.show();
    }

    }

