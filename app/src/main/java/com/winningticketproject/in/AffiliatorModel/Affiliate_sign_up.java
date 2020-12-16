package com.winningticketproject.in.AffiliatorModel;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.text.TextUtils;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.winningticketproject.in.SignInSingup.Login_Screen;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.url;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Affiliate_sign_up extends AppCompatActivity implements View.OnClickListener {
    EditText et_firsrname;
    EditText et_lastname;
    EditText et_email_address;
    EditText et_address1;
    EditText et_address2;
    EditText et_city;
    EditText et_phone_number;
    EditText colof_course_name;
    EditText et_contact_title;
    EditText et_zip_code;
    Button btn_signup_success;
    TextView back_text,countrytxt,statetxt,sub_title;
    RelativeLayout backlayout,registation_page;
    ArrayList<String> statevalues = new ArrayList<String>();
    ArrayList<String> statekeys = new ArrayList<String>();
    String state_values="",country_values = "";
    int country_potion = 0;
    Spinner statespinner,country;
    TextInputLayout colof_course_nam,text_ufname,contact_lastname,in_contact_title,in_address1,in_address2,in_city,in_number,in_zipcode,in_contact_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple__signup);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        colof_course_name = findViewById(R.id.colof_course_name);
        et_contact_title = findViewById(R.id.et_contact_title);
        et_zip_code = findViewById(R.id.et_zip_code);
        countrytxt = findViewById(R.id.countrytxt);
        statetxt = findViewById(R.id.statetxt);

        et_firsrname = findViewById(R.id.et_firsrname);
        et_lastname = findViewById(R.id.et_lastname);
        et_email_address = findViewById(R.id.et_email_address);
        et_address1 = findViewById(R.id.et_address1);
        et_address2 = findViewById(R.id.et_address2);
        et_city = findViewById(R.id.et_city);
        et_phone_number = findViewById(R.id.et_phone_number);


        statespinner= findViewById(R.id.statespinner);
        country= findViewById(R.id.country);


        colof_course_nam= findViewById(R.id.colof_course_nam);

        text_ufname = findViewById(R.id.text_ufname);
        contact_lastname = findViewById(R.id.contact_lastname);
        in_contact_title = findViewById(R.id.in_contact_title);
        in_address1 = findViewById(R.id.in_address1);
        in_address2 = findViewById(R.id.in_address2);
        in_city = findViewById(R.id.in_city);
        in_number = findViewById(R.id.in_number);
        in_zipcode = findViewById(R.id.in_zipcode);
        in_contact_email = findViewById(R.id.in_contact_email);

        registation_page = findViewById(R.id.registation_page);


        sub_title = findViewById(R.id.sub_title);
        sub_title.setTypeface(medium);


        btn_signup_success = findViewById(R.id.btn_signup_success);
        btn_signup_success.setOnClickListener(this);
        backlayout = findViewById(R.id.backlayout);
        backlayout.setOnClickListener(this);
        back_text = findViewById(R.id.back_text);
        TextView tv = findViewById(R.id.title);
        tv.setTypeface(medium);
        colof_course_name.setTypeface(regular);
        et_firsrname.setTypeface(regular);
        et_lastname.setTypeface(regular);
        et_email_address.setTypeface(regular);
        btn_signup_success.setTypeface(regular);
        et_address1.setTypeface(regular);
        et_address2.setTypeface(regular);
        et_city.setTypeface(regular);
        et_phone_number.setTypeface(regular);
        colof_course_name.setTypeface(regular);
        et_contact_title.setTypeface(regular);
        et_zip_code.setTypeface(regular);
        countrytxt.setTypeface(regular);


        colof_course_nam.setTypeface(regular);

        text_ufname.setTypeface(regular);
        contact_lastname.setTypeface(regular);
        in_contact_title.setTypeface(regular);
        in_address1.setTypeface(regular);
        in_address2.setTypeface(regular);
        in_city.setTypeface(regular);
        in_number.setTypeface(regular);
        in_zipcode.setTypeface(regular);
        in_contact_email.setTypeface(regular);

        back_text.setOnClickListener(this);
        statetxt.setTypeface(regular);
        countrytxt.setTypeface(regular);

        colof_course_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                colof_course_name.setFocusable(true);
                colof_course_name.setFocusableInTouchMode(true);

                return false;
            }
        });



        ArrayAdapter<String> adapter   = new ArrayAdapter<String>(Affiliate_sign_up.this,R.layout.custom_country_text, Splash_screen.countryvalues) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTypeface(regular);
                ((TextView) v).setTextColor(getResources().getColorStateList(R.color.colorwhite));


                return v;
            }


            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                ((TextView) v).setTypeface(regular);
                if(((TextView) v).getText().toString().equals("Select Country")){
                    // Set the hint text color gray

                    ((TextView) v).setTextColor(Color.GRAY);
                }
                else
                {
                    ((TextView) v).setTextColor(Color.BLACK);
                }
                return v;
            }
        };


        adapter.setDropDownViewResource( R.layout.custom_country_text);
        country.setAdapter(adapter);
//        if (!isNetworkAvailable()) {
//
//            alertdailogbox();
//
//        } else {
//
//        }
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                country_values=Splash_screen.countrykeys.get(position);
                country_potion=position;
                if(position>0) {
                    countrytxt.setVisibility(View.VISIBLE);
                }
                else
                {
                    countrytxt.setVisibility(View.GONE);
                }
//                if(position!=0) {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("stateapi");
                    } else {
                        MethodToCalStateApi(Splash_screen.countrykeys.get(country_potion));
                    }
//                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        et_firsrname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                et_firsrname.setFocusable(true);
                et_firsrname.setFocusableInTouchMode(true);
                return false;
            }
        });
        et_lastname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                et_lastname.setFocusable(true);
                et_lastname.setFocusableInTouchMode(true);
                return false;
            }
        });

        et_email_address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                et_email_address.setFocusable(true);
                et_email_address.setFocusableInTouchMode(true);
                return false;
            }
        });

        et_address1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                et_address1.setFocusable(true);
                et_address1.setFocusableInTouchMode(true);
                return false;
            }
        });

        et_address2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                et_address2.setFocusable(true);
                et_address2.setFocusableInTouchMode(true);
                return false;
            }
        });

        et_city.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                et_city.setFocusable(true);
                et_city.setFocusableInTouchMode(true);
                return false;
            }
        });

        et_phone_number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                et_phone_number.setFocusable(true);
                et_phone_number.setFocusableInTouchMode(true);
                return false;
            }
        });


        colof_course_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                colof_course_name.setFocusable(true);
                colof_course_name.setFocusableInTouchMode(true);
                return false;
            }
        });

        et_contact_title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                et_contact_title.setFocusable(true);
                et_contact_title.setFocusableInTouchMode(true);
                return false;
            }
        });

        et_zip_code.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                et_zip_code.setFocusable(true);
                et_zip_code.setFocusableInTouchMode(true);
                return false;
            }
        });

        statespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    statetxt.setVisibility(View.VISIBLE);
                }
                else
                {
                    statetxt.setVisibility(View.GONE);
                }


                state_values=statekeys.get(position);
                //statetxt.setText(state_values);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        backlayout.setOnTouchListener(new View.OnTouchListener() {
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

    private void MethodToCalStateApi(String county) {
        ProgressDialog.getInstance().showProgress(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url+"city_states/states?country="+county, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                statevalues.clear();
                statekeys.clear();
                statevalues.add("Select State");
                statekeys.add("");
                try {
                    Iterator<String> keys = response.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        statevalues.add(key);
                        statekeys.add(response.getString(key));
                    }
                    ArrayAdapter<String> adapter   = new ArrayAdapter<String>(Affiliate_sign_up.this,R.layout.custom_country_text, statevalues) {

                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);

                            ((TextView) v).setTypeface(regular);
                            ((TextView) v).setTextColor(getResources().getColorStateList(R.color.colorwhite));


                            return v;
                        }


                        public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                            View v =super.getDropDownView(position, convertView, parent);

                            ((TextView) v).setTypeface(regular);

                            if(((TextView) v).getText().toString().equals("Select State")){
                                // Set the hint text color gray

                                ((TextView) v).setTextColor(Color.GRAY);
                            }
                            else
                            {
                                ((TextView) v).setTextColor(Color.BLACK);
                            }

                            return v;
                        }
                    };
                    adapter.setDropDownViewResource( R.layout.custom_country_text);
                    statespinner.setAdapter(adapter);
                    ProgressDialog.getInstance().hideProgress();


                }
                catch (Exception e)
                {
                    ProgressDialog.getInstance().hideProgress();
                    //nothing
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                ProgressDialog.getInstance().hideProgress();
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy( 20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    @SuppressLint("WrongConstant")
    private void error(String s) {
        Snackbar snackbar = Snackbar.make(registation_page, s, Snackbar.LENGTH_LONG);
        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
        View  view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtv.setTextSize(TypedValue
                .COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorsnakbar));
        snackbar.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_signup_success:
                if (colof_course_name.getText().toString().isEmpty()) {
                    error("Please enter course name");
                    colof_course_name.requestFocus();
                }else if (colof_course_name.getText().toString().length()<2) {
                    error("Course name minimum 2 character");
                    colof_course_name.requestFocus();
                }
                else if (et_firsrname.getText().toString().isEmpty()){
                    error("Please enter first name");
                    et_firsrname.requestFocus();
                }else if(et_firsrname.getText().toString().length()<2){
                    error("First name minimum 2 character");
                    et_firsrname.requestFocus();
                }else if (et_contact_title.getText().toString().isEmpty()){
                    error("Please enter contact title");
                    et_contact_title.requestFocus();
                }else if(et_contact_title.getText().toString().length()<2){
                    error("Contact title minimum 2 character");
                    et_contact_title.requestFocus();
                }
                else if (et_address1.getText().toString().isEmpty()){
                    error("Please enter address1");
                    et_address1.requestFocus();
                }else if(et_address1.getText().toString().length()<2){
                    error("Address 1 minimum 2 character");
                    et_address1.requestFocus();
                }else if(et_address2.getText().toString().length()==1){
                    error("Address 2 minimum 2 character");
                    et_address2.requestFocus();
                }else if (et_city.getText().toString().isEmpty()) {
                    error("Please enter city");
                    et_city.requestFocus();
                }
                else if (et_city.getText().toString().length()<2) {
                    error("City minimum 2 character");
                    et_city.requestFocus();
                }else if (et_phone_number.getText().toString().isEmpty()){
                    error("Please enter phone number");
                    et_phone_number.requestFocus();
                }else if(et_phone_number.getText().toString().length()<5){
                    error("Phone number minimum 5 character");
                    et_phone_number.requestFocus();
                }else if (et_zip_code.getText().toString().isEmpty()){
                    error("Please enter zip code");
                    et_zip_code.requestFocus();
                }else if(et_zip_code.getText().toString().length()<4){
                    error("Zip code minimum 4 character");
                    et_zip_code.requestFocus();
                }
                else if (et_email_address.getText().toString().isEmpty()){

                    error("Please enter email address");
                    et_email_address.requestFocus();

                }else if(et_email_address.getText().toString().contains(" ")){

                    error("Space is not allowed in email address");
                    et_email_address.requestFocus();
                }
                else if ((isValidEmail(et_email_address.getText().toString())))
                {


                    if (!isNetworkAvailable()) {

                        alertdailogbox("register");
                    }
                    else
                    {
//                    addReferalApi();
                        try {

                            JSONObject user = new JSONObject();
                            JSONObject parameter = new JSONObject();

                            parameter.put("company_name",colof_course_name.getText().toString());
                            parameter.put("first_name",et_firsrname.getText().toString());
                            parameter.put("last_name",et_lastname.getText().toString());
                            parameter.put("contact_affiliation",et_contact_title.getText().toString());
                            parameter.put("address1",et_address1.getText().toString());
                            parameter.put("address2",et_address2.getText().toString());
                            parameter.put("city",et_city.getText().toString());
                            parameter.put("phone",et_phone_number.getText().toString());
                            parameter.put("country",country_values);
                            parameter.put("state",state_values);

                            parameter.put("email",et_email_address.getText().toString());
                            parameter.put("zipcode",et_zip_code.getText().toString());

                            user.put("user_type","affiliate");
                            user.put("user",parameter);
                            makeJsonObjectRequest(user, url+"users");

                        } catch (JSONException e) {
                            //nothing
                        }
                    }




                }
                else
                {
                    error("Please enter valid email address");
                    et_email_address.requestFocus();
                }
                break;
            case R.id.backlayout:
                finish();
                break;
            case R.id.back_text:
                finish();
                break;
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void makeJsonObjectRequest(JSONObject jsonBody2, String url) {
        ProgressDialog.getInstance().showProgress(this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, url,jsonBody2,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialog.getInstance().hideProgress();
                        try {

                            if (response.getString("status").equals("Success")) {




                                Intent in = new Intent(getApplicationContext(), Login_Screen.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                startActivity(in);
                                finishAffinity();

                                Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();

                            }else {
                                error(response.getString("message"));
                            }

                        }catch (Exception e){
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
                }) {
        };
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
                    {
                        MethodToCalStateApi(Splash_screen.countrykeys.get(country_potion));
                    }
                }
                else
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("register");
                    }
                    else
                    {
//                    addReferalApi();
                        try {

                            JSONObject user = new JSONObject();
                            JSONObject parameter = new JSONObject();

                            parameter.put("company_name",colof_course_name.getText().toString());
                            parameter.put("first_name",et_firsrname.getText().toString());
                            parameter.put("last_name",et_lastname.getText().toString());
                            parameter.put("contact_affiliation",et_contact_title.getText().toString());
                            parameter.put("address1",et_address1.getText().toString());
                            parameter.put("address2",et_address2.getText().toString());
                            parameter.put("city",et_city.getText().toString());
                            parameter.put("phone",et_phone_number.getText().toString());
                            parameter.put("country",country_values);
                            parameter.put("state",state_values);

                            parameter.put("email",et_email_address.getText().toString());
                            parameter.put("zipcode",et_zip_code.getText().toString());

                            user.put("user_type","affiliate");
                            user.put("user",parameter);
                            makeJsonObjectRequest(user, url+"users");

                        } catch (JSONException e) {
                            //nothing
                        }
                    }
                }



            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }
}