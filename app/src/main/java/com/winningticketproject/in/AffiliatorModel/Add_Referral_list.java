package com.winningticketproject.in.AffiliatorModel;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
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
import com.winningticketproject.in.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Add_Referral_list extends AppCompatActivity implements View.OnClickListener {

    TextView cancel_purchage;
    TextView toolbar_title;
    TextView user_role;
    Typeface textfont;
    Typeface book;
    EditText referral_name,referral_email,referral_phone_number;
    Button sponser,event_orgranizer,participant,affiliate,add_referral;
    LinearLayout  adreferal;
    String auth_token="",reffereal ="";
    ProgressDialog pd;
    TextInputLayout text_fname,text_emailadress,text_pno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__referral_list);
        pd = new ProgressDialog(Add_Referral_list.this);
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        book = Typeface.createFromAsset(getAssets(),"Montserrat-Regular.ttf");
        textfont = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");

        auth_token = get_auth_token("auth_token");


        cancel_purchage = findViewById(R.id.cancel_purchage);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Add Referral");
        cancel_purchage.setOnClickListener(this);

        text_fname = findViewById(R.id.text_fname);
        text_emailadress = findViewById(R.id.text_emailadress);
        text_pno = findViewById(R.id.text_pno);
        user_role = findViewById(R.id.user_role);
        referral_name = findViewById(R.id.referral_name);
        referral_email = findViewById(R.id.referral_email);
        referral_phone_number = findViewById(R.id.referral_phone_number);

        sponser = findViewById(R.id.sponser);
        event_orgranizer = findViewById(R.id.event_orgranizer);
        participant = findViewById(R.id.participant);
        add_referral = findViewById(R.id.add_referral);
        affiliate = findViewById(R.id.affiliate);
        adreferal = findViewById(R.id.adreferal);

        user_role.setTypeface(textfont);
        referral_name.setTypeface(book);
        referral_email.setTypeface(book);
        referral_phone_number.setTypeface(book);
        sponser.setTypeface(textfont);
        event_orgranizer.setTypeface(textfont);
        participant.setTypeface(textfont);
        affiliate.setTypeface(textfont);
        add_referral.setTypeface(textfont);
        cancel_purchage.setTypeface(textfont);
        add_referral.setTypeface(textfont);
        toolbar_title.setTypeface(textfont);
        text_fname.setTypeface(book);
        text_emailadress.setTypeface(book);
        text_pno.setTypeface(book);

        sponser.setOnClickListener(this);
        event_orgranizer.setOnClickListener(this);
        participant.setOnClickListener(this);
        add_referral.setOnClickListener(this);
        affiliate.setOnClickListener(this);

        toolbar_title.setOnClickListener(this);

        referral_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                referral_name.setFocusable(true);
                referral_name.setFocusableInTouchMode(true);
                return false;
            }
        });
        referral_email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                referral_email.setFocusable(true);
                referral_email.setFocusableInTouchMode(true);
                return false;
            }
        });

        referral_phone_number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                referral_phone_number.setFocusable(true);
                referral_phone_number.setFocusableInTouchMode(true);
                return false;
            }
        });



        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
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

            case R.id.sponser:

                sponser.setBackgroundResource(R.drawable.add_refre_button_baground);
                sponser.setTextColor(getResources().getColor(R.color.colorwhite));

                event_orgranizer.setBackgroundResource(R.drawable.add_referral);
                event_orgranizer.setTextColor(getResources().getColor(R.color.coloraddreferal));

                affiliate.setBackgroundResource(R.drawable.add_referral);
                affiliate.setTextColor(getResources().getColor(R.color.coloraddreferal));

                participant.setBackgroundResource(R.drawable.add_referral);
                participant.setTextColor(getResources().getColor(R.color.coloraddreferal));
                reffereal="sponsor";

                break;

            case R.id.event_orgranizer:

                sponser.setBackgroundResource(R.drawable.add_referral);
                sponser.setTextColor(getResources().getColor(R.color.coloraddreferal));

                event_orgranizer.setBackgroundResource(R.drawable.add_refre_button_baground);
                event_orgranizer.setTextColor(getResources().getColor(R.color.colorwhite));

                affiliate.setBackgroundResource(R.drawable.add_referral);
                affiliate.setTextColor(getResources().getColor(R.color.coloraddreferal));

                participant.setBackgroundResource(R.drawable.add_referral);
                participant.setTextColor(getResources().getColor(R.color.coloraddreferal));
                reffereal="organizer";


                break;

            case R.id.participant:

                sponser.setBackgroundResource(R.drawable.add_referral);
                sponser.setTextColor(getResources().getColor(R.color.coloraddreferal));

                event_orgranizer.setBackgroundResource(R.drawable.add_referral);
                event_orgranizer.setTextColor(getResources().getColor(R.color.coloraddreferal));

                affiliate.setBackgroundResource(R.drawable.add_referral);
                affiliate.setTextColor(getResources().getColor(R.color.coloraddreferal));

                participant.setBackgroundResource(R.drawable.add_refre_button_baground);
                participant.setTextColor(getResources().getColor(R.color.colorwhite));
                reffereal="contributor";


                break;


            case R.id.affiliate:

                sponser.setBackgroundResource(R.drawable.add_referral);
                sponser.setTextColor(getResources().getColor(R.color.coloraddreferal));

                event_orgranizer.setBackgroundResource(R.drawable.add_referral);
                event_orgranizer.setTextColor(getResources().getColor(R.color.coloraddreferal));

                participant.setBackgroundResource(R.drawable.add_referral);
                participant.setTextColor(getResources().getColor(R.color.coloraddreferal));

                affiliate.setBackgroundResource(R.drawable.add_refre_button_baground);
                affiliate.setTextColor(getResources().getColor(R.color.colorwhite));

                reffereal="affiliate";


                break;


            case R.id.cancel_purchage:
                finish();
                break;

            case R.id.add_referral:


                hiddenInputMethod();

                if (referral_name.getText().toString().isEmpty()){
                    error("Please enter referral name");
                    referral_name.requestFocus();
                }else if (referral_name.getText().toString().length()<2){
                    error("Referral name minimum 2 characters");
                    referral_name.requestFocus();
                } else if (referral_email.getText().toString().isEmpty()){
                    error("Please enter email address");
                    referral_email.requestFocus();
                }else if(referral_email.getText().toString().contains(" ")){
                    error("Space is not allowed in email field");
                    referral_email.requestFocus();
                }else if (!isValidEmail(referral_email.getText().toString())){
                    error("Please enter valid email address");
                    referral_email.requestFocus();
                }else if (referral_phone_number.getText().toString().isEmpty()){
                    error("Please enter phone number");
                    referral_phone_number.requestFocus();
                }else if (referral_phone_number.getText().toString().length()<5) {
                    error("Phone number cannot be less than 5 digit");
                    referral_phone_number.requestFocus();
                }else if (reffereal.equals("")){
                    error("Please select any role");
                }else {

//                    {"referral": {"first_name": "test", "email": "abbc@gmail.com", "phone": "3432423423", "role": "sponsor"}}
                    if(isNetworkAvailable()) {
                        addReferalApi();
                    }
                    else
                    {
                        alertdailogbox();
                    }


                }

                break;

        }
    }

    private void addReferalApi() {
        try {

            pd.show();

            JSONObject object = new JSONObject();
            final JSONObject refferal = new JSONObject();
            object.put("first_name",referral_name.getText().toString());
            object.put("email",referral_email.getText().toString());
            object.put("phone",referral_phone_number.getText().toString());
            object.put("role",reffereal);
            refferal.put("referral",object);

            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"referrals/create",refferal,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                pd.dismiss();

                                if(response.getString("status").equals("Success")){
                                    // error(response.getString("message"));
                                    Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                                    finish();

                                }else {
                                    error(response.getString("message"));
                                }
                            }catch (Exception E){
                                //nothing
                                pd.dismiss();
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
                                    Alert_Dailog.showNetworkAlert(Add_Referral_list.this);
                                }else {
                                    if (pd.isShowing()){
                                        pd.dismiss();}
                                    Toast.makeText(Add_Referral_list.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                                }
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Toast.makeText(Add_Referral_list.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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

        }catch (Exception e){

        }
    }


    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @SuppressLint("WrongConstant")
    private void error(String s) {

        Snackbar snackbar = Snackbar.make(adreferal, s, Snackbar.LENGTH_LONG);
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
                    addReferalApi();
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }
}
