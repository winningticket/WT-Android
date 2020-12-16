package com.winningticketproject.in.MyAccountTab;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Login_Screen;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Change_Password extends AppCompatActivity implements View.OnClickListener {
    TextView cancel_purchage,account_title,status;
    Button change_password;
    EditText current_password,new_ppasword,confirm_password;
    ProgressDialog pd;
    Typeface textfont,book;
    LinearLayout llchange_password;
    String auth_token="";
    TextInputLayout text_oldpassword,text_newpassword,text_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);


        book = Typeface.createFromAsset(getAssets(),"Montserrat-Regular.ttf");
        textfont = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");

        llchange_password = findViewById(R.id.llchange_password);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        cancel_purchage = findViewById(R.id.cancel_purchage);
        cancel_purchage.setOnClickListener(this);

        account_title = findViewById(R.id.account_title);
        account_title.setText("Change Password");

        change_password = findViewById(R.id.change_password);
        change_password.setOnClickListener(this);

        status = findViewById(R.id.status);

        auth_token = get_auth_token("auth_token");

        text_oldpassword = findViewById(R.id.text_oldpassword);
        text_newpassword = findViewById(R.id.text_newpassword);
        text_confirm_password = findViewById(R.id.text_confirm_password);

        current_password = findViewById(R.id.current_password);
        new_ppasword = findViewById(R.id.new_ppasword);
        confirm_password = findViewById(R.id.confirm_password);

        cancel_purchage.setTypeface(textfont);
        account_title.setTypeface(textfont);
        change_password.setTypeface(textfont);
        current_password.setTypeface(textfont);
        new_ppasword.setTypeface(textfont);
        confirm_password.setTypeface(textfont);
        text_oldpassword.setTypeface(book);
        text_newpassword.setTypeface(book);
        text_confirm_password.setTypeface(book);

        status.setTypeface(textfont);


        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

        current_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                current_password.setFocusable(true);
                current_password.setFocusableInTouchMode(true);
                return false;
            }
        });

        new_ppasword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                new_ppasword.setFocusable(true);
                new_ppasword.setFocusableInTouchMode(true);
                return false;
            }
        });

        confirm_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                confirm_password.setFocusable(true);
                confirm_password.setFocusableInTouchMode(true);
                return false;
            }
        });


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int[] scrcoords = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                view.setFocusable(false);
            }else
            {
                view.setFocusable(true);
            }
        }
        return ret;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_purchage:
                finish();
                break;

            case R.id.change_password:
               // makeJsonObjectRequest();
//                InputMethodManager inputManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                View view1 = this.getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
                if (current_password.getText().toString().isEmpty()){
                    error("Please enter current password");
                    current_password.requestFocus();
                } else if(current_password.getText().toString().length()<8) {
                    error("Current password contains at least 8 characters");
                    current_password.requestFocus();

                }
                else if(new_ppasword.getText().toString().isEmpty()){
                    error("Please enter new password");
                    new_ppasword.requestFocus();

                }
                else if(new_ppasword.getText().toString().length()<8){
                    error("New password contains at least 8 characters ");
                    new_ppasword.requestFocus();

                }
                else if(confirm_password.getText().toString().isEmpty()){
                    error("Please enter confirm password");
                    confirm_password.requestFocus();

                }
                else if(confirm_password.getText().toString().length()<8){
                    error("Confirm password contains at least 8 characters ");
                    confirm_password.requestFocus();


                }
            else if(!confirm_password.getText().toString().equals(new_ppasword.getText().toString())){
                    error("Confirm password mismatch");
                    confirm_password.requestFocus();


                }else {


                    if (!isNetworkAvailable()) {

                        alertdailogbox();
                    }
                    else
                    {
                        try {
                            pd = new ProgressDialog(Change_Password.this);
                            pd.setMessage("Please wait.");
                            pd.setCancelable(false);
                            pd.setIndeterminate(true);
                            pd.show();

                            JSONObject user = new JSONObject();
                            JSONObject parameter = new JSONObject();

                            parameter.put("current_password", current_password.getText().toString());
                            parameter.put("password", new_ppasword.getText().toString());
                            parameter.put("password_confirmation", confirm_password.getText().toString());

                            user.put("user", parameter);
                            makeJsonObjectRequest(user);


                        } catch (JSONException e) {
                            //nothing
                        }
                    }







                }

                break;
        }
    }

    @SuppressLint("WrongConstant")
    private void error(String s) {

        Snackbar snackbar = Snackbar.make(llchange_password, s, Snackbar.LENGTH_LONG);
        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
        View  view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtv.setTextSize(TypedValue
                .COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorsnakbar));
        snackbar.show();
    }


    public void makeJsonObjectRequest(JSONObject object) {

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"users/change_password",object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("status").equals("Success")){
                                status.setText(response.getString("message"));
                                status.setVisibility(View.VISIBLE);
                                pd.dismiss();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        status.setVisibility(View.GONE);
                                        current_password.getText().clear();
                                        new_ppasword.getText().clear();
                                        confirm_password.getText().clear();
                                        pd.dismiss();
                                        Share_it.remove_key("auth_token");
                                        Share_it.remove_key("role");
                                        startActivity(new Intent(getApplicationContext(), Login_Screen.class));
                                        finishAffinity();
                                    }
                                }, 3000);





                            }else {

                                status.setText(response.getString("message"));
                                status.setVisibility(View.VISIBLE);
                                status.setBackgroundResource(R.color.colorsnakbar);

                                current_password.clearFocus();
                                new_ppasword.clearFocus();
                                confirm_password.clearFocus();

                                current_password.setText("");
                                new_ppasword.setText("");
                                confirm_password.setText("");


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        status.setVisibility(View.GONE);

                                    }
                                }, 3000);
                                pd.dismiss();
                            }

                        }catch (Exception E){
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
                                Alert_Dailog.showNetworkAlert(Change_Password.this);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Toast.makeText(Change_Password.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(Change_Password.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
    public void alertdailogbox(){
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
                if (!isNetworkAvailable()) {

                    alertdailogbox();
                }
                else
                {
                    try {
                        pd = new ProgressDialog(Change_Password.this);
                        pd.setMessage("Please wait.");
                        pd.setCancelable(false);
                        pd.setIndeterminate(true);
                        pd.show();

                        JSONObject user = new JSONObject();
                        JSONObject parameter = new JSONObject();

                        parameter.put("current_password", current_password.getText().toString());
                        parameter.put("password", new_ppasword.getText().toString());
                        parameter.put("password_confirmation", confirm_password.getText().toString());

                        user.put("user", parameter);
                        makeJsonObjectRequest(user);


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
