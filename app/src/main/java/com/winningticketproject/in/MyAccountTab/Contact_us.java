package com.winningticketproject.in.MyAccountTab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.R;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Contact_us extends AppCompatActivity implements View.OnClickListener {
    TextView facebook,twitter,account_title,cancel_purchage,contact_title,instagram_logo;
    EditText contact_fname,contact_lname,contact_email,contact_pno,contact_subject,contact_message;
    Button preview_order;
    LinearLayout fulllayout;
    TextInputLayout text_ufname,text_ulname,text_uemail,text_uphno,text_usubject,text_umessage;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_us);

        hiddenInputMethod();
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        fulllayout= findViewById(R.id.fulllayout);

        facebook = findViewById(R.id.facebook);
        twitter = findViewById(R.id.twitter);

        instagram_logo = findViewById(R.id.instagram_logo);

        facebook.setText("\uf082");
        facebook.setTypeface(webfont);

        twitter.setText("\uf099");
        twitter.setTypeface(webfont);

        instagram_logo.setText("\uf16d");
        instagram_logo.setTypeface(webfont);

        contact_title = findViewById(R.id.contact_title);

        account_title = findViewById(R.id.account_title);
        account_title.setText("Contact Us");

        cancel_purchage = findViewById(R.id.cancel_purchage);
        cancel_purchage.setOnClickListener(this);


        contact_fname = findViewById(R.id.contact_fname);
        contact_lname = findViewById(R.id.contact_lname);
        contact_email = findViewById(R.id.contact_email);
        contact_pno = findViewById(R.id.contact_pno);
        contact_subject = findViewById(R.id.contact_subject);
        contact_message = findViewById(R.id.contact_message);

        text_ufname = findViewById(R.id.text_ufname);
        text_ulname = findViewById(R.id.text_ulname);
        text_uemail = findViewById(R.id.text_uemail);
        text_uphno = findViewById(R.id.text_uphno);
        text_usubject = findViewById(R.id.text_usubject);
        text_umessage = findViewById(R.id.text_umessage);


        preview_order = findViewById(R.id.preview_order);
        preview_order.setOnClickListener(this);
        preview_order.setTypeface(medium);
        contact_fname.setTypeface(regular);
        contact_lname.setTypeface(regular);
        contact_email.setTypeface(regular);
        contact_pno.setTypeface(regular);
        contact_subject.setTypeface(regular);
        contact_message.setTypeface(regular);
        cancel_purchage.setTypeface(medium);
        account_title.setTypeface(medium);
        contact_title.setTypeface(medium);



        text_ufname.setTypeface(regular);
        text_ulname.setTypeface(regular);
        text_uemail.setTypeface(regular);
        text_uphno.setTypeface(regular);
        text_usubject.setTypeface(regular);
        text_umessage.setTypeface(regular);

        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        instagram_logo.setOnClickListener(this);

        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

        contact_fname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                contact_fname.setFocusable(true);
                contact_fname.setFocusableInTouchMode(true);
                return false;
            }
        });

        contact_lname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                contact_lname.setFocusable(true);
                contact_lname.setFocusableInTouchMode(true);
                return false;
            }
        });

        contact_email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                contact_email.setFocusable(true);
                contact_email.setFocusableInTouchMode(true);
                return false;
            }
        });

        contact_pno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                contact_pno.setFocusable(true);
                contact_pno.setFocusableInTouchMode(true);
                return false;
            }
        });


        contact_subject.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                contact_subject.setFocusable(true);
                contact_subject.setFocusableInTouchMode(true);
                return false;
            }
        });

        contact_message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                contact_message.setFocusable(true);
                contact_message.setFocusableInTouchMode(true);
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
        Intent In;
        switch (view.getId()) {

            case R.id.cancel_purchage:
                finish();
                break;

            case R.id.facebook:
                In = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com"));
                startActivity(In);
                break;

            case R.id.twitter:
                In = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet"));
                startActivity(In);
                break;

            case R.id.instagram_logo:
                In = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gobloggerslive.com"));
                startActivity(In);
                break;

            case R.id.preview_order:



                hiddenInputMethod();

                if(contact_fname.getText().toString().equals(""))
                {
                    error("Please enter contact first name");
                    contact_fname.requestFocus();
                }
                else if(contact_fname.getText().toString().length()<2)
                {
                    error("Contact first name minimum 2 characters");
                    contact_fname.requestFocus();

                }
                else if(contact_email.getText().toString().equals(""))
                {
                    error("Please enter contact email address");
                    contact_email.requestFocus();
                }
                else if(!isValidEmail(contact_email.getText().toString().trim()))
                {
                    error("Invalid email address");
                    contact_email.requestFocus();

                }
                else if(contact_email.getText().toString().contains(" "))
                {
                    error("Space not allowed in email");
                    contact_email.requestFocus();

                }
                else if(contact_pno.getText().toString().equals(""))
                {
                    error("Please enter contact phone number");
                    contact_pno.requestFocus();

                }
                else if(contact_pno.getText().toString().length()<5)
                {
                    error("Contact phone number minimum 5 digit");
                    contact_pno.requestFocus();

                }else if(contact_subject.getText().toString().equals(""))
                {
                    error("Please enter subject");
                    contact_subject.requestFocus();
                }
                else if(contact_subject.getText().toString().length()<2)
                {
                    error("Subject minimum 2 characters");
                    contact_subject.requestFocus();
                }
                else if(contact_message.getText().toString().equals(""))
                {
                    error("Please enter message");
                    contact_message.requestFocus();
                }
                else if(contact_message.getText().toString().length()<2)
                {
                    error("Message minimum 2 characters");
                    contact_message.requestFocus();

                }
                else {

                    if (!isNetworkAvailable()) {

                        alertdailogbox();
                    }
                    else
                    {
                        try {
                            JSONObject parameter = new JSONObject();
                            parameter.put("first_name", contact_fname.getText().toString());
                            parameter.put("last_name", contact_lname.getText().toString());
                            parameter.put("email", contact_email.getText().toString());
                            parameter.put("subject", contact_subject.getText().toString());
                            parameter.put("message", contact_message.getText().toString());
                            parameter.put("phone", contact_pno.getText().toString());
                            makeJsonObjectRequest(parameter, Splash_screen.url + "home/contact_us");
                        } catch (Exception e) {
                            //nothing
                        }
                    }
                }
                break;
        }
        }
    @SuppressLint("WrongConstant")
    private void error(String s) {

        Snackbar snackbar = Snackbar.make(fulllayout, s, Snackbar.LENGTH_LONG);
        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
        View  view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtv.setTextSize(TypedValue
                .COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorsnakbar));
        snackbar.show();

    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void makeJsonObjectRequest(JSONObject jsonBody2, String url) {

        ProgressDialog.getInstance().showProgress(Contact_us.this);

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, url,jsonBody2,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialog.getInstance().hideProgress();
                        try {
                            if (response.getString("status").equals("Success")){
                                Toast.makeText(getApplicationContext(), "Thank you. We aim to respond to your request within 48 hours." +"",Toast.LENGTH_LONG).show();
                             finish();
                            }else {
                                hiddenInputMethod();
                                Snackbar snackbar = Snackbar.make(fulllayout, response.getString("message"), Snackbar.LENGTH_LONG);
                                snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
                                View  view = snackbar.getView();
                                TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
                                txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                txtv.setTextSize(TypedValue
                                        .COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size));
                                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorsnakbar));
                                snackbar.show();
                            }
                        } catch (JSONException e) {
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
                                ProgressDialog.getInstance().hideProgress();
                                Alert_Dailog.showNetworkAlert(Contact_us.this);
                            }else {
                                ProgressDialog.getInstance().hideProgress();
                                Toast.makeText(Contact_us.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            ProgressDialog.getInstance().hideProgress();
                            Toast.makeText(Contact_us.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }


    public void hiddenInputMethod() {

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
                        JSONObject parameter = new JSONObject();
                        parameter.put("first_name", contact_fname.getText().toString());
                        parameter.put("last_name", contact_lname.getText().toString());
                        parameter.put("email", contact_email.getText().toString());
                        parameter.put("subject", contact_subject.getText().toString());
                        parameter.put("message", contact_message.getText().toString());
                        parameter.put("phone", contact_pno.getText().toString());
                        makeJsonObjectRequest(parameter, Splash_screen.url + "home/contact_us");
                    } catch (Exception e) {
                        //nothing
                    }
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }
}
