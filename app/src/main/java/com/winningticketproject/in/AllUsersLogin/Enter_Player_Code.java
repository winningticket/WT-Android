package com.winningticketproject.in.AllUsersLogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.MyFirebaseInstanceIdService;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.EventTab.Event_Code_valid;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

public class Enter_Player_Code extends AppCompatActivity implements View.OnClickListener{

    TextView tv_player_code,tv_text_player;
    Button btn_continue;
    EditText edit1;
    String total;
    String auth_token = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_player_code);

        initilize_all_fields();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    private void initilize_all_fields() {

        tv_player_code = findViewById(R.id.tv_player_code);
        tv_player_code.setTypeface(semibold);

        tv_text_player = findViewById(R.id.tv_text_player);
        tv_text_player.setTypeface(regular);

        btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setTypeface(semibold);
        btn_continue.setOnClickListener(this);

        edit1 = findViewById(R.id.edit_player_code);
        edit1.setTypeface(medium);
        edit1.setCursorVisible(true);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_continue :
                total = edit1.getText().toString();
                if(total.isEmpty()){
                    Toast.makeText(this ,"Please enter player code ",Toast.LENGTH_LONG).show();
                }else if (total.length()<=6){
                    Toast.makeText(this ,"Please enter 7 digit code",Toast.LENGTH_LONG).show();
                }else if(total.length() == 7){
                        methodtocallplayercode(total);
                    }
                break;
                }
        }

    private void methodtocallplayercode(final String total) {

        JSONObject user_code = new JSONObject();
        try {
            user_code.put("player_code",total);
            ProgressDialog.getInstance().showProgress(Enter_Player_Code.this);
        }catch (Exception E){
        }

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.imageurl+"api/v2/users/player_code",user_code,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("-----res_player_code------"+response);

                        ProgressDialog.getInstance().hideProgress();

                        try {
                            if (response.getString("status").equals("Success")) {

                                JSONObject obj = response.getJSONObject("user_details");

                                savestring("auth_token",response.getString("authentication_token"));
                                savestring("role",response.getString("role"));

                                savestring("chat_user_id", obj.getString("id"));

                                savestring("first_name",obj.getString("first_name"));
                                savestring("last_name", obj.getString("last_name"));
                                savestring("handicap","0");
                                JSONArray data = response.getJSONArray("partner_name");

                                String partner_name="";
                                for (int i = 0; i < data.length(); i++) {
                                    partner_name += data.getString(i)+" ";
                                }
                                savestring("partner_name",partner_name);

                                savestring("starting_hole", response.getString("starting_hole"));
                                savestring("user_player_type", obj.getString("user_player_type"));
                                savestring("user_profile_logo", obj.getString("avatar_url"));
                                savestring("user_info",obj.getString("first_name") +" "+ obj.getString("last_name") );
                                savestring("team_name",response.getString("team_name"));
                                savestring("email_show",response.getString("email_show"));
                                savestring("cross_score_popup","false");

                                Toast.makeText(Enter_Player_Code.this , response.getString("message"),Toast.LENGTH_LONG).show();

                                MyFirebaseInstanceIdService firebase = new MyFirebaseInstanceIdService();
                                firebase.onTokenRefresh();

                                Intent in = new Intent(Enter_Player_Code.this , VerifyGuestUser.class);
                                in.putExtra("player_code",total);
                                startActivity(in);

                            }else{
                                Toast.makeText(Enter_Player_Code.this , response.getString("message"),Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        ProgressDialog.getInstance().hideProgress();
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {

                            if (error.networkResponse.statusCode == 401){
                                Alert_Dailog.showNetworkAlert(Enter_Player_Code.this);
                            }else {
                                Toast.makeText(Enter_Player_Code.this , "Invalid code" , Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(Enter_Player_Code.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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

}