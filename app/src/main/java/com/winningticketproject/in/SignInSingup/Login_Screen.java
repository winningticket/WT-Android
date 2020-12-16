package com.winningticketproject.in.SignInSingup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.winningticketproject.in.CourseTab.Course_Search_Page;
import com.winningticketproject.in.CourseTab.Courses_list_flow;
import com.winningticketproject.in.AffiliatorModel.Bottom_Tabs_For_afffiliate;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.MyFirebaseInstanceIdService;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.OrganizerFlow.EventListFlow;
import com.winningticketproject.in.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.winningticketproject.in.AppInfo.Share_it.remove_key;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;


public class Login_Screen extends AppCompatActivity implements View.OnClickListener {
    //updated
    Button btn_login;
    EditText et_email,et_password;
    TextView txt_forgotpassword,login_reminder;
    Switch switch_Button;
    android.support.v7.app.AlertDialog alertDialog;
    TextInputLayout text_usename,text_password;
    String auth_token="";
    private CoordinatorLayout coordinatorLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
            //If play service is available
        }

        // all ids initilizing
        id_intilizations();

        Boolean saveLogin = Boolean.valueOf(get_auth_token("checked"));
        if (saveLogin) {
            et_email.setText(get_auth_token("username"));
            switch_Button.setChecked(true);
        }else {
            switch_Button.setChecked(false);
        }

        txt_forgotpassword.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void id_intilizations() {

        TextView tv_sign_in = findViewById(R.id.tv_sign_in);
        tv_sign_in.setTypeface(medium);

        text_usename = findViewById(R.id.text_usename);
        text_password = findViewById(R.id.text_password);
        text_usename.setTypeface(regular);
        text_password.setTypeface(regular);

        switch_Button = findViewById(R.id.switch_keep_me_login);

        btn_login = findViewById(R.id.btn_login);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        txt_forgotpassword = findViewById(R.id.txt_forgotpassword);
        login_reminder = findViewById(R.id.tv_keep_me_login);

        btn_login.setTypeface(medium);
        et_email.setTypeface(regular);
        et_password.setTypeface(regular);
        txt_forgotpassword.setTypeface(regular);
        login_reminder.setTypeface(regular);

        // onclick sign in method
//        sign_in_on_clic_method();

    }

    private void sign_in_on_clic_method() {

//        TextView tv_login_text = findViewById(R.id.tv_login_text);
//        tv_login_text.setTypeface(medium);

        SpannableString myString = new SpannableString(Html.fromHtml("<font color=\"#000000\">Don’t have an account? </font>" +"<font color=\"#0bb1de\"><b>Sign up</b></font>"));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent in = new Intent(getApplicationContext(), Registration_form.class);
                startActivity(in);
                finish();
            }
        };

        myString.setSpan(clickableSpan,23,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        myString.setSpan(new ForegroundColorSpan(Color.parseColor("#0bb1de")),23, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        tv_login_text.setMovementMethod(LinkMovementMethod.getInstance());
//        tv_login_text.setText(myString);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {

        Intent in;
        switch (view.getId()){

            case R.id.txt_forgotpassword:
                 forgot_popup();
                break;

            case R.id.btn_login:

                if(et_email.getText().toString().isEmpty()){
                    hiddenInputMethod();
                    error("Please enter email");
                    et_email.requestFocus();
                    et_email.setFocusableInTouchMode(true);

                }else if(et_password.getText().toString().isEmpty()){
                    hiddenInputMethod();
                    error("Please enter password");
                    et_password.requestFocus();
                    et_password.setFocusableInTouchMode(true);

                }else if(et_email.getText().toString().equals("affiliate")){
                    hiddenInputMethod();
                    in = new Intent(getApplicationContext(), Bottom_Tabs_For_afffiliate.class);
                    startActivity(in);
                }else {

                    try {

                        if (!isNetworkAvailable()) {

                            alertdailogbox();

                        } else {
                            hiddenInputMethod();
                            JSONObject user = new JSONObject();
                            JSONObject parameter = new JSONObject();

                            parameter.put("email",et_email.getText().toString());
                            parameter.put("password",et_password.getText().toString());

                            user.put("user",parameter);
                            makeJsonObjectRequest(user, Splash_screen.url + "users/sign_in");
                        }
                    } catch (JSONException e) {
                        //nothing
                    }
                }

              break;
        }
    }

    public void alertdailogbox(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
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
                if (!isNetworkAvailable()) {

                    alertdailogbox();
                }
                else
                {
                    try {

                        JSONObject user = new JSONObject();
                        JSONObject parameter = new JSONObject();

                        parameter.put("email", et_email.getText().toString());
                        parameter.put("password", et_password.getText().toString());

                        user.put("user", parameter);

                        makeJsonObjectRequest(user, Splash_screen.url + "users/sign_in");
                    }
                    catch (Exception e)
                    {
                        //nothing
                    }
                }

            }
        });

        android.support.v7.app.AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    @SuppressLint("WrongConstant")
    private void error(String s) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_LONG);
        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
        View  view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtv.setTextSize(TypedValue
                .COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorsnakbar));
        snackbar.show();
    }


    public void hiddenInputMethod() {

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void makeJsonObjectRequest(JSONObject jsonBody2, String url) {

        System.out.println("-----sing in response--"+url);

        ProgressDialog.getInstance().showProgress(Login_Screen.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, url,jsonBody2,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("-----sing in response--"+response);

                        remove_key("auth_token");
                        try {
                            if (response.getString("status").equals("Success")){
                                    //Starting intent to register device

                                savestring("login_email", et_email.getText().toString());
                                savestring("email_show", "true");

                                if (switch_Button.isChecked()){
                                    savestring("checked", "true");
                                    savestring("username", et_email.getText().toString());
                                } else {

                                    remove_key("checked");
                                    remove_key("username");
                                }

                                savestring("user_player_type", "user");


                                JSONObject user_details = response.getJSONObject("user_details");
                                savestring("handicap",user_details.getString("user_handicap"));
                                savestring("user_profile_logo",user_details.getString("avatar_url"));
                                savestring("user_info",user_details.getString("first_name") +" "+ user_details.getString("last_name") );

                                savestring("chat_user_id", user_details.getString("id"));

                                if (response.getString("role").equals("contributor")){

                                    savestring("auth_token",response.getString("authentication_token"));
                                    savestring("role",response.getString("role"));

                                    MyFirebaseInstanceIdService firebase = new MyFirebaseInstanceIdService();
                                    firebase.onTokenRefresh();

                                    Intent in = new Intent(getApplicationContext(), Courses_list_flow.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                    startActivity(in);
                                    finishAffinity();

                                } else if (response.getString("role").equals("organizer")){

                                    savestring("auth_token",response.getString("authentication_token"));
                                    savestring("role",response.getString("role"));

                                    MyFirebaseInstanceIdService firebase = new MyFirebaseInstanceIdService();
                                    firebase.onTokenRefresh();

                                    Intent in = new Intent(getApplicationContext(), EventListFlow.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                    startActivity(in);
                                    finishAffinity();

                                }else {

                                    ProgressDialog.getInstance().hideProgress();

                                    savestring("auth_token",response.getString("authentication_token"));
                                    savestring("role",response.getString("role"));

                                    MyFirebaseInstanceIdService firebase = new MyFirebaseInstanceIdService();
                                    firebase.onTokenRefresh();

                                    Intent in = new Intent(getApplicationContext(),Bottom_Tabs_For_afffiliate.class);
                                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                    startActivity(in);
                                    finishAffinity();
                                }


                            }else {
                                ProgressDialog.getInstance().hideProgress();
                                hiddenInputMethod();
                                error(response.getString("message"));
                            }

                        } catch (JSONException e) {
                            //nothing
                            ProgressDialog.getInstance().hideProgress();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ProgressDialog.getInstance().hideProgress();
                    }
                });

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }

    public  void forgot_popup(){

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Login_Screen.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) Login_Screen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogview = inflater.inflate(R.layout.custome_forgot_popup, null);

        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;
        TextView pop_up_title = alertDialog.findViewById(R.id.pop_up_title);
        TextView pop_up_sub_title = alertDialog.findViewById(R.id.pop_up_sub_title);
        TextView cancel = alertDialog.findViewById(R.id.cancel);
        TextView submit = alertDialog.findViewById(R.id.submit);
        final EditText email_text = alertDialog.findViewById(R.id.email_edit_text);
        TextInputLayout text_fname = alertDialog.findViewById(R.id.text_fname);
        pop_up_title.setTypeface(webfont);
        pop_up_sub_title.setTypeface(medium);
        cancel.setTypeface(webfont);
        submit.setTypeface(webfont);
        email_text.setTypeface(medium);
        text_fname.setTypeface(medium);
        email_text.setFocusable(false);
        email_text.setFocusableInTouchMode(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        email_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                email_text.requestFocus();
                email_text.setFocusable(true);
                email_text.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(email_text, InputMethodManager.SHOW_FORCED);

                return true;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiddenInputMethod();
                if(email_text.getText().toString().isEmpty()) {
                    error("Please enter email");
                }else {
                    try {
                        if (!isNetworkAvailable()) {
                           Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();

                        } else {
                            hiddenInputMethod();
                            ProgressDialog.getInstance().showProgress(Login_Screen.this);
                            JSONObject user = new JSONObject();
                            JSONObject parameter = new JSONObject();
                            parameter.put("email", email_text.getText().toString());
                            user.put("user", parameter);
                            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url + "users/forgot_password", user,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            ProgressDialog.getInstance().hideProgress();
                                            try {
                                                if (response.getString("status").equals("Success")) {

                                                    Toast.makeText(getApplicationContext(),response.getString("password"),Toast.LENGTH_LONG).show();
                                                    alertDialog.dismiss();
//
                                                } else {
                                                    email_text.setFocusable(true);
                                                    email_text.setFocusableInTouchMode(true);
                                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                    imm.showSoftInput(email_text, InputMethodManager.HIDE_NOT_ALWAYS);

                                                    email_text.setText("");
                                                    Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                                                }

                                            } catch (JSONException e) {
                                                //nothing
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            ProgressDialog.getInstance().hideProgress();
                                        }
                                    });

                            int socketTimeout = 30000; // 30 seconds. You can change it
                            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            myRequest.setRetryPolicy(policy);
                            AppController.getInstance().addToRequestQueue(myRequest, "tag");
                        }
                        }catch(Exception e){

                    }
                }
                //alertDialog.dismiss();
            }
        });


        alertDialog.show();

    }
}
