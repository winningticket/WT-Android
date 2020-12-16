package com.winningticketproject.in.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
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
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.EventTab.Score_board_new;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.setGridViewHeightBasedOnChildren;

public class Select_hole extends AppCompatActivity implements View.OnClickListener {
    int backposition = -1;
    public Map<String, String> score;
    public Map<String, String> par_position ;
    TextView player_name,dummy_text,hole_info;
    Button continu_button;
    String selected_hole="",auth_token,event_id,url_value;
    LinearLayout select_hole_root;
    GridView gridview;
    ImageButton back_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hole);

        score = new HashMap<String, String>();
        par_position = new HashMap<String, String>();

        select_hole_root = findViewById(R.id.select_hole_root);

        player_name = findViewById(R.id.player_name);
        dummy_text = findViewById(R.id.dummy_text);
        continu_button = findViewById(R.id.continu_button);

        player_name.setTypeface(regular);
        dummy_text.setTypeface(medium);
        continu_button.setTypeface(medium);

        auth_token = get_auth_token("auth_token");
        event_id = get_auth_token("event_id");

        gridview = findViewById(R.id.gridview);

        player_name.setText(get_auth_token("Event_name"));

        hole_info = findViewById(R.id.hole_info);
        hole_info.setTypeface(regular);

        if (!isNetworkAvailable()) {
            alertdailogbox("list_api");
        }
        else
        {
            listofholeandparfromapi();
        }


        back_text = findViewById(R.id.hole_btn_back);
        back_text.setOnClickListener(this);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                final LinearLayout linear_row= view.findViewById(R.id.linear_row);
                linear_row.setBackgroundResource(R.drawable.select_hole_postion);
                LinearLayout BackSelectedItem = (LinearLayout) gridview.getChildAt(backposition);
                if (backposition != position)
                {
                    if (BackSelectedItem!=null){
                        BackSelectedItem.setSelected(false);
                        BackSelectedItem.setBackgroundResource(R.drawable.new_back);
                    }
                }
//
                backposition = position;
                selected_hole = score.get(position+1+"")+"";
                continu_button.setVisibility(View.VISIBLE);
                    final Animation myAnim = AnimationUtils.loadAnimation(Select_hole.this, R.anim.button_zoom);
                   linear_row.startAnimation(myAnim);
            }
        });

        continu_button.setOnClickListener(this);
    }

    private void listofholeandparfromapi() {
        try {
            com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().showProgress(Select_hole.this);
        }catch (Exception E){
            //nothing
        }

        if (get_auth_token("play_type").equals("free") || get_auth_token("play_type").equals("paid")){
            url_value = "hole_info/player/hole_list/"+event_id;
        }else if (get_auth_token("play_type").equals("event")){
            url_value = "hole_info/hole_list/"+event_id;
        }

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+url_value,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();

                            JSONArray hole_list = response.getJSONArray("hole_list");
                            for (int i = 0 ;i<hole_list.length();i++){
                                JSONObject hole_obj = hole_list.getJSONObject(i);
                                String hole_num =hole_obj.getString("hole_num");
                                String par =hole_obj.getString("par");
                                score.put(i+1+"",hole_num);
                                par_position.put(i+1+"",par);
                            }
                            gridview.setAdapter(new GridAdapter(Select_hole.this,score,par_position));
                            setGridViewHeightBasedOnChildren(gridview,3);
                            continu_button.setVisibility(View.GONE);

                            if (score.isEmpty()) {
                                gridview.setVisibility(View.GONE);
                                hole_info.setVisibility(View.VISIBLE);
                            }
                            else {
                                gridview.setVisibility(View.VISIBLE);
                                hole_info.setVisibility(View.GONE);
                            }


                        }catch (Exception e){
                            //nothing

                            if (score.isEmpty()) {
                                gridview.setVisibility(View.GONE);
                                hole_info.setVisibility(View.VISIBLE);
                            }
                            else {
                                gridview.setVisibility(View.VISIBLE);
                                hole_info.setVisibility(View.GONE);
                            }

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
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(Select_hole.this);
                            }else {
                                error("Some thing went wrong");
                            }
                        }else {
                            Toast.makeText(Select_hole.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
        Map<String, String> par_score= new HashMap<String, String>();
        Map<String, String> par_postion= new HashMap<String, String>();
        private GridAdapter(Context context,Map<String, String> par_score, Map<String, String> par_postion) {
            this.par_score = par_score;
            this.par_postion = par_postion;
        }
        @Override
        public int getCount() {
            return par_score.size();
        }

        @Override
        public Object getItem(final int position) {
            return par_score.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple, parent, false);
            }
            final TextView text = view.findViewById(R.id.text1);
            final TextView text2 = view.findViewById(R.id.text2);

            text.setTypeface(medium);
            text2.setTypeface(regular);

            text.setText(par_score.get(position+1+"")+"");

            String valuess = par_postion.get(position+1+"");
            if (valuess==null){
                text2.setText("");
            }else {
                text2.setText("Par "+valuess);
            }
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
                if (selected_hole.equals("") || selected_hole.equals(null) || selected_hole.equals("null")){
                    Toast.makeText(getApplicationContext(),"Please select hole",Toast.LENGTH_LONG).show();
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
            case R.id.hole_btn_back:
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
            url_value = "hole_info/player/select_hole_info/"+event_id;
        }else if (get_auth_token("play_type").equals("event")){
            url_value = "hole_info/select_hole_info/"+event_id;
        }
            JSONObject user_code = new JSONObject();
            try {
                com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().showProgress(Select_hole.this);
                user_code.put("selected_hole",selected_hole);
                }catch (Exception E){
                //nothing
            }

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+url_value,user_code,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try
                            {
                                com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();

                                if (response.getString("status").equals("Success")){
                                    Intent intent1 = new Intent(getApplicationContext(), Score_board_new.class);
                                    Share_it.savestring("selected_hole_postion",selected_hole);
                                    startActivity(intent1);
                                    finish();
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
                            com.winningticketproject.in.AppInfo.ProgressDialog.getInstance().hideProgress();
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                // HTTP Status Code: 401 Unauthorized
                                if (error.networkResponse.statusCode==401){
                                    Alert_Dailog.showNetworkAlert(Select_hole.this);
                                }else {
                                        error("Some thing went wrong");
                                }
                            }else {
                                Toast.makeText(Select_hole.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(Select_hole.this, R.color.colorsnakbar));
        snackbar.show();
    }
}
