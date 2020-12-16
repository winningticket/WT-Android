package com.winningticketproject.in.SignInSingup;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.MyAccountTab.About_Us;
import com.winningticketproject.in.CourseTab.Course_Search_Page;
import com.winningticketproject.in.CourseTab.Courses_list_flow;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.MyFirebaseInstanceIdService;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.R;

import org.json.JSONException;
import org.json.JSONObject;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class Registration_form extends AppCompatActivity implements View.OnClickListener {

    EditText sign_up_f_name;
    EditText sign_up_l_name;
    EditText et_sign_up_email;
    EditText sign_up_password;
    EditText sign_up_conform_password;
    EditText et_sign_up_phone_number;
    private LinearLayout coordinatorLayout;
    Button btn_create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initilize_all_fields();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initilize_all_fields() {

        TextInputLayout text_ufname = findViewById(R.id.text_fname);
        TextInputLayout text_ulname = findViewById(R.id.text_lname);
        TextInputLayout text_email = findViewById(R.id.tv_up_email);
        TextInputLayout text_password = findViewById(R.id.text_password);
        TextInputLayout text_re_password = findViewById(R.id.text_confirm_password);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        TextInputLayout tvxtinput_phone_number = findViewById(R.id.tvxtinput_phone_number);

        text_ufname.setTypeface(regular);
        text_ulname.setTypeface(regular);
        text_email.setTypeface(regular);
        text_password.setTypeface(regular);
        text_re_password.setTypeface(regular);
        tvxtinput_phone_number.setTypeface(regular);

        TextView tv_sign_up = findViewById(R.id.tv_sign_up);
        tv_sign_up.setTypeface(medium);

        TextView tv_wt_top_info = findViewById(R.id.tv_wt_top_info);
        tv_wt_top_info.setTypeface(regular);

        sign_up_f_name = findViewById(R.id.sign_up_f_name);
        sign_up_l_name = findViewById(R.id.sign_up_l_name);
        et_sign_up_email = findViewById(R.id.et_sign_up_email);
        sign_up_password = findViewById(R.id.sign_up_password);
        sign_up_conform_password = findViewById(R.id.sign_up_conform_password);
        et_sign_up_phone_number = findViewById(R.id.et_sign_up_phone_number);

        et_sign_up_phone_number.setTypeface(regular);
        sign_up_f_name.setTypeface(regular);
        sign_up_l_name.setTypeface(regular);
        et_sign_up_email.setTypeface(regular);
        sign_up_password.setTypeface(regular);
        sign_up_conform_password.setTypeface(regular);


        btn_create_account = findViewById(R.id.btn_create_account);
        btn_create_account.setTypeface(medium);
        btn_create_account.setOnClickListener(this);

        sing_in_click_term_of_user();




    }

    private void sing_in_click_term_of_user() {

        TextView privacy_policy = findViewById(R.id.privacy_policy);
        privacy_policy.setTypeface(regular);
        SpannableString privacy_policy_string = new SpannableString("By signing up, you are agree to Winning Ticket’s \nTerms of Service and Privacy Policy");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent in = new Intent(getApplicationContext(), About_Us.class);
                in.putExtra("page_type", "terms_of_use");
                startActivity(in);
            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent in = new Intent(getApplicationContext(), About_Us.class);
                in.putExtra("page_type", "privacy_policy");
                startActivity(in);
            }
        };

        privacy_policy_string.setSpan(clickableSpan1,49,66,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        privacy_policy_string.setSpan(new ForegroundColorSpan(Color.parseColor("#757575")),49, 66, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        privacy_policy_string.setSpan(clickableSpan2,71,85,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        privacy_policy_string.setSpan(new ForegroundColorSpan(Color.parseColor("#757575")),71, 85, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        privacy_policy.setMovementMethod(LinkMovementMethod.getInstance());
        privacy_policy.setText(privacy_policy_string);

    }

    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_create_account:
                hiddenInputMethod();
               if (sign_up_f_name.getText().toString().isEmpty()){
                    error("Please enter first name");
                   sign_up_f_name.requestFocus();
                }else if(sign_up_f_name.getText().toString().length()<2) {
                   error("First name minimum 2 characters");
                   sign_up_f_name.requestFocus();
               }else if(sign_up_l_name.getText().toString().isEmpty()) {
                   error("Please enter last name");
                   sign_up_l_name.requestFocus();
               }else if(sign_up_l_name.getText().toString().length()<2) {
                   error("Last name minimum 2 characters");
                   sign_up_l_name.requestFocus();
               } else if (et_sign_up_email.getText().toString().isEmpty()){
                    error("Please enter email address");
                   et_sign_up_email.requestFocus();
                } else if ((!isValidEmail(et_sign_up_email.getText().toString()))){
                      error("Please enter valid email");
                   et_sign_up_email.requestFocus();
               }else if (et_sign_up_phone_number.getText().toString().isEmpty()){
                   error("Please enter phone number");
                   et_sign_up_phone_number.requestFocus();
               } else if (et_sign_up_phone_number.getText().toString().length()<5){
                   error("Last name minimum 5 numbers");
                   et_sign_up_phone_number.requestFocus();
               } else if (sign_up_password.getText().toString().isEmpty()){
                   error("Please enter password");
                   sign_up_password.requestFocus();
               }else if (sign_up_password.getText().toString().length()<6){
                   error("Please enter minimum 6 digit password");
                   sign_up_password.requestFocus();
                   }else if (sign_up_conform_password.getText().toString().isEmpty()){
                   error("Please enter confirm password");
                   sign_up_conform_password.requestFocus();
               }else if (!sign_up_conform_password.getText().toString().equals(sign_up_password.getText().toString())){
                   error("Confirm password miss match");
                   sign_up_conform_password.requestFocus();

                  }else {

                   if (!isNetworkAvailable()) {
                       alertdailogbox("register");
                   } else {
                       try {
                           JSONObject user = new JSONObject();
                           JSONObject parameter = new JSONObject();
                           parameter.put("first_name", sign_up_f_name.getText().toString());
                           parameter.put("last_name", sign_up_l_name.getText().toString());
                           parameter.put("email", et_sign_up_email.getText().toString());
                           parameter.put("password", sign_up_password.getText().toString());
                           parameter.put("phone",et_sign_up_phone_number.getText().toString());
                           parameter.put("password_confirmation", sign_up_conform_password.getText().toString());
                           user.put("user_type", "contributor");
                           user.put("user", parameter);

                           savestring("user_info",sign_up_f_name.getText().toString()+" "+sign_up_l_name.getText().toString());

                           makeJsonObjectRequest(user, Splash_screen.url + "users");


                       } catch (JSONException e) {
                           //nothing
                       }
                   }
                }

                break;
            case R.id.backlayout:
                finish();
                break;
        }


    }

    private void error(String s) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, s, Snackbar.LENGTH_LONG);
        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
        View  view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextAlignment(android.view.View.TEXT_ALIGNMENT_CENTER);
        txtv.setTextSize(TypedValue
                .COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorsnakbar));
        snackbar.show();

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void makeJsonObjectRequest(JSONObject jsonBody2, String url) {
        System.out.println("--------------jsob----"+jsonBody2);
        ProgressDialog.getInstance().showProgress(Registration_form.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, url,jsonBody2,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                        if (response.getString("status").equals("Success")){

                            System.out.println("-----success----"+response);

                            savestring("handicap","0");
                            savestring("user_profile_logo","");

                            JSONObject object = response.getJSONObject("user_details");

                            savestring("login_email", et_sign_up_email.getText().toString());
                            savestring("chat_user_id", object.getString("id"));

                            Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();

                            savestring("auth_token",response.getString("authentication_token"));
                            savestring("role",response.getString("role"));
                            savestring("user_player_type", "user");

                            MyFirebaseInstanceIdService firebase = new MyFirebaseInstanceIdService();
                            firebase.onTokenRefresh();

                            savestring("email_show", "true");

                            Intent in = new Intent(getApplicationContext(), Courses_list_flow.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
                            startActivity(in);
                            finishAffinity();

                            ProgressDialog.getInstance().hideProgress();

                        }else {
                            error(response.getString("message"));
                            ProgressDialog.getInstance().hideProgress();

                        }
                    }catch (Exception E){
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

    public void alertdailogbox(final String apivalue){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

                if(apivalue.equals("stateapi"))
                {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("stateapi");
                    }
                    else
                        try {
                            JSONObject user = new JSONObject();
                            JSONObject parameter = new JSONObject();
                            parameter.put("first_name", sign_up_f_name.getText().toString());
                            parameter.put("last_name", sign_up_l_name.getText().toString());
                            parameter.put("email", et_sign_up_email.getText().toString());
                            parameter.put("password", sign_up_password.getText().toString());
                            parameter.put("password_confirmation", sign_up_conform_password.getText().toString());
                            user.put("user_type", "contributor");
                            user.put("user", parameter);
                            makeJsonObjectRequest(user, Splash_screen.url + "users");

                        } catch (JSONException e) {
                            //nothing
                        }
                    }
            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

}
