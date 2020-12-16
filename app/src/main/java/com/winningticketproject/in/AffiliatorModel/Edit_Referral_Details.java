package com.winningticketproject.in.AffiliatorModel;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
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
import com.winningticketproject.in.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Edit_Referral_Details extends AppCompatActivity implements View.OnClickListener {

    TextView cancel_purchage;
    TextView account_title;
    String id,name,role,email,pno;
    Typeface textfont,book;
    TextInputLayout text_referral_name,text_Referral_email,text_refferal_phone_numbr,text_Referral_role_type;
    EditText refferal_name,text_refferal_mail,text_Referral_phone_no,text_Referral_role;
    Button canacel_button,update_referral;
    LinearLayout referral_layout;
    String auth_token="";
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__referral__details);

        book = Typeface.createFromAsset(getAssets(),"Montserrat-Regular.ttf");
        textfont = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");

        cancel_purchage = findViewById(R.id.cancel_purchage);

        account_title = findViewById(R.id.account_title);
        account_title.setText("Edit Referral");

        refferal_name = findViewById(R.id.refferal_name);
        text_refferal_mail = findViewById(R.id.text_refferal_mail);
        text_Referral_phone_no = findViewById(R.id.text_Referral_phone_no);
        text_Referral_role = findViewById(R.id.text_refferal_role);

        text_referral_name = findViewById(R.id.text_referral_name);
        text_Referral_email = findViewById(R.id.text_Referral_email);
        text_refferal_phone_numbr = findViewById(R.id.text_refferal_phone_numbr);
        text_Referral_role_type = findViewById(R.id.text_Referral_role_type);

        canacel_button = findViewById(R.id.canacel_button);
        update_referral = findViewById(R.id.update_referral);

        canacel_button.setOnClickListener(this);
        update_referral.setOnClickListener(this);

        referral_layout = findViewById(R.id.referral_layout);

        auth_token = get_auth_token("auth_token");

        pd = new ProgressDialog(Edit_Referral_Details.this);
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        Intent i = getIntent();
        id = i.getStringExtra("id");
        name = i.getStringExtra("name").replaceAll("\\s+"," ");
        role = i.getStringExtra("role").replaceAll("\\s+"," ");

        email = i.getStringExtra("email");
        pno = i.getStringExtra("p_no");

        if(name.equals("Not mentioned"))
        {
            name="";
        }
        if(role.equals("Not mentioned"))
        {
            role="";
        }
        if(email.equals("Not mentioned"))
        {
            email="";
        }
        if(pno.equals("Not mentioned"))
        {
            pno="";
        }

            String namereferal="";
        try{
            namereferal= name.substring(0,1).toUpperCase() +name.substring(1);
        }
        catch (Exception e)
        {

        }
        String rolereferal="";
        try{
            rolereferal= role.substring(0,1).toUpperCase() +role.substring(1);
        }
        catch (Exception e)
        {

        }

        refferal_name.setText(namereferal.trim());
        text_Referral_role.setText(rolereferal.trim());
        text_refferal_mail.setText(email);
        text_Referral_phone_no.setText(pno);

        cancel_purchage.setTypeface(textfont);
        account_title.setTypeface(textfont);

        refferal_name.setTypeface(book);
        text_refferal_mail.setTypeface(book);
        text_Referral_phone_no.setTypeface(book);
        text_Referral_role.setTypeface(book);
        text_referral_name.setTypeface(book);
        text_Referral_email.setTypeface(book);
        text_refferal_phone_numbr.setTypeface(book);
        text_Referral_role_type.setTypeface(book);
        canacel_button.setTypeface(textfont);
        update_referral.setTypeface(textfont);


        cancel_purchage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                finish();

                return false;
            }
        });


        refferal_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                refferal_name.setFocusable(true);
                refferal_name.setFocusableInTouchMode(true);
                return false;
            }
        });

        text_refferal_mail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                text_refferal_mail.setFocusable(true);
                text_refferal_mail.setFocusableInTouchMode(true);
                return false;
            }
        });

        text_Referral_phone_no.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                text_Referral_phone_no.setFocusable(true);
                text_Referral_phone_no.setFocusableInTouchMode(true);
                return false;
            }
        });


        text_Referral_role.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                text_Referral_role.setFocusable(true);
                text_Referral_role.setFocusableInTouchMode(true);
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
            case R.id.canacel_button:
                finish();
                break;
            case R.id.update_referral:

                hiddenInputMethod();

                if(refferal_name.getText().toString().isEmpty()){
                    error("Please enter referral name");
                    refferal_name.requestFocus();
                }else if(refferal_name.getText().toString().length()<2){
                    error("Referral name minimum 2 characters");
                    refferal_name.requestFocus();

                }else if(text_Referral_phone_no.getText().toString().isEmpty()) {
                    error("Please enter referral phone number");
                    text_Referral_phone_no.requestFocus();

                }else if (text_Referral_phone_no.getText().toString().length()<5){
                    error("Phone number cannot be less than 5 digit");
                    text_Referral_phone_no.requestFocus();

                }else {

                    if (!isNetworkAvailable()) {

                        alertdailogbox();
                    }
                    else
                    {
                        try {
                            pd.show();
                            JSONObject object = new JSONObject();
                            JSONObject referral = new JSONObject();
                            object.put("first_name",refferal_name.getText().toString());
                            object.put("phone",text_Referral_phone_no.getText().toString());
                            referral.put("referral",object);
                            methodforcallupdateapi(referral, Splash_screen.url+"referrals/update/"+id);


                        }catch (Exception e){
                        }
                    }



                }
                break;
        }


    }


    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @SuppressLint("WrongConstant")
    private void error(String s) {
        Snackbar snackbar = Snackbar.make(referral_layout, s, Snackbar.LENGTH_LONG);
        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
        View  view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtv.setTextSize(TypedValue
                .COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorsnakbar));
        snackbar.show();
    }


    public void methodforcallupdateapi(JSONObject jsonBody2, String url) {
        if (!pd.isShowing()){
            pd.show();
        }
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.PUT, url,jsonBody2,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (pd.isShowing()){
                                pd.dismiss();

                                if(response.getString("status").equals("Success")){
                                    Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();
//
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();

                                }


                            }
                        }catch (Exception E){
                            //nothing
                            if (pd.isShowing()){
                                pd.dismiss();
                            }                        }
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
                                Alert_Dailog.showNetworkAlert(Edit_Referral_Details.this);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Toast.makeText(Edit_Referral_Details.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(Edit_Referral_Details.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
                        pd.show();
                        JSONObject object = new JSONObject();
                        JSONObject referral = new JSONObject();
                        object.put("first_name",refferal_name.getText().toString());
                        object.put("phone",text_Referral_phone_no.getText().toString());
                        referral.put("referral",object);
                        methodforcallupdateapi(referral,Splash_screen.url+"referrals/update/"+id);
                    }catch (Exception e){
                    }
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }


}
