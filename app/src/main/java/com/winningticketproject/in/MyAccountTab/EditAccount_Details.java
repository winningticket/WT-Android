package com.winningticketproject.in.MyAccountTab;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.CourseTab.Courses_list_flow;
import com.winningticketproject.in.Fragments.HomeFragment;
import com.winningticketproject.in.Fragments.My_account;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.savestring;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.courses_text;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.courses_view;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.euser_view;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.event_text;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.event_view;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.home_text;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.home_view;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.imag_course;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.imag_event;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.imag_home;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.imag_my_account;
import static com.winningticketproject.in.CourseTab.Courses_list_flow.user_text;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class EditAccount_Details extends AppCompatActivity {
    String strfirstname,strlastname,stradd1,stradd2,strcity,strzipcode,strphone;
    TextView cancel_purchage, account_title, countrytxt,citytxt;
    Spinner country, statespinner;
    EditText profile_name, profile_lname, profile_email, profile_address1, profile_address2, profile_city, profile_zipcode, profile_pno , password, confirm_password;
    Button preview_order;
    LinearLayout editpagelayout;
    String auth_token = "", state_values = "", country_values = "", states = "";
    ProgressDialog pd;
    ArrayList<String> statevalues = new ArrayList<String>();
    ArrayList<String> statekeys = new ArrayList<String>();
    int country_potion = 0, state_postion = 0;
    TextInputLayout text_fname, text_lname, text_email, text_address1, text_address2, text_city, text_zipcode, text_pno, text_password ,text_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_account__details);
        pd = new ProgressDialog(EditAccount_Details.this);

        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        country = findViewById(R.id.country);

        statespinner = findViewById(R.id.state);
        preview_order = findViewById(R.id.preview_order);
        cancel_purchage = findViewById(R.id.cancel_purchage);
        editpagelayout= findViewById(R.id.editpagelayout);
        cancel_purchage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

        account_title = findViewById(R.id.account_title);
        countrytxt = findViewById(R.id.countrytxt);
        account_title.setText("Edit Account Information");
        citytxt = findViewById(R.id.citytxt);

        text_fname = findViewById(R.id.text_fname);
        text_lname = findViewById(R.id.text_lname);
        text_email = findViewById(R.id.text_email);
        text_address1 = findViewById(R.id.text_address1);
        text_address2 = findViewById(R.id.text_address2);
        text_city = findViewById(R.id.text_city);
        text_zipcode = findViewById(R.id.text_zipcode);
        text_pno = findViewById(R.id.text_pno);
        text_password = findViewById(R.id.text_password);
        text_confirm_password = findViewById(R.id.text_confirm_password);

        text_fname.setTypeface(regular);
        text_lname.setTypeface(regular);
        text_email.setTypeface(regular);
        text_address1.setTypeface(regular);
        text_address2.setTypeface(regular);
        text_zipcode.setTypeface(regular);
        text_pno.setTypeface(regular);
        countrytxt.setTypeface(regular);
        citytxt.setTypeface(regular);
        text_city.setTypeface(regular);
        text_password.setTypeface(regular);
        text_confirm_password.setTypeface(regular);

        profile_name = findViewById(R.id.profile_name);
        profile_lname = findViewById(R.id.profile_lname);
        profile_email = findViewById(R.id.profile_email);
        profile_address1 = findViewById(R.id.profile_address1);
        profile_address2 = findViewById(R.id.profile_address2);
        profile_city = findViewById(R.id.profile_city);
        profile_zipcode = findViewById(R.id.profile_zipcode);
        profile_pno = findViewById(R.id.profile_pno);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);

        profile_name.setTypeface(regular);
        profile_lname.setTypeface(regular);
        profile_email.setTypeface(regular);
        profile_address1.setTypeface(regular);
        profile_address2.setTypeface(regular);
        profile_city.setTypeface(regular);
        profile_zipcode.setTypeface(regular);
        profile_pno.setTypeface(regular);
        password.setTypeface(regular);
        confirm_password.setTypeface(regular);

        auth_token = get_auth_token("auth_token");


        ArrayAdapter<String> adapter   = new ArrayAdapter<String>(EditAccount_Details.this,R.layout.custom_text, Splash_screen.countryvalues) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTypeface(regular);

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


        adapter.setDropDownViewResource( R.layout.custom_text);
        country.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        if (!isNetworkAvailable()) {

            alertdailogbox("profile");
        }
        else
        {
            makeJsonObjectRequest();
        }

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_values=Splash_screen.countrykeys.get(position);
                state_postion=0;
                country_potion=position;
//                if(position!=0) {

                if(position>0) {
                    countrytxt.setVisibility(View.VISIBLE);
                }
                else
                {
                    countrytxt.setVisibility(View.GONE);
                }

                if (!isNetworkAvailable()) {

                    alertdailogbox("country");
                }
                else
                {
                    MethodToCalStateApi(Splash_screen.countrykeys.get(position));
                }

//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        statespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    citytxt.setVisibility(View.VISIBLE);
                }
                else
                {
                    citytxt.setVisibility(View.GONE);
                }
                state_values=statekeys.get(position);
                state_postion=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        cancel_purchage.setTypeface(medium);
        account_title.setTypeface(medium);
        preview_order.setTypeface(medium);


        profile_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                profile_name.setFocusable(true);
                profile_name.setFocusableInTouchMode(true);
                return false;
            }
        });
        profile_lname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                profile_lname.setFocusable(true);
                profile_lname.setFocusableInTouchMode(true);
                return false;
            }
        });

        profile_email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                profile_email.setFocusable(true);
                profile_email.setFocusableInTouchMode(true);
                return false;
            }
        });

        profile_address1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                profile_address1.setFocusable(true);
                profile_address1.setFocusableInTouchMode(true);
                return false;
            }
        });

        profile_address2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                profile_address2.setFocusable(true);
                profile_address2.setFocusableInTouchMode(true);
                return false;
            }
        });

        profile_city.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                profile_city.setFocusable(true);
                profile_city.setFocusableInTouchMode(true);
                return false;
            }
        });

        profile_zipcode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                profile_zipcode.setFocusable(true);
                profile_zipcode.setFocusableInTouchMode(true);
                return false;
            }
        });

        profile_pno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                profile_pno.setFocusable(true);
                profile_pno.setFocusableInTouchMode(true);
                return false;
            }
        });
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                password.setFocusable(true);
                password.setFocusableInTouchMode(true);
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


        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

        preview_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                hiddenInputMethod();

                try {
                        if (profile_name.getText().toString().equals("")) {
                            error("Please enter first name");
                            profile_name.requestFocus();
                        } else if (profile_name.getText().toString().length() < 2) {
                            error("First name minimum 2 characters");
                            profile_name.requestFocus();
                        }
                        else if (profile_address1.getText().toString().equals("")) {
                            error("Please enter address line 1");
                            profile_address1.requestFocus();
                        } else if (profile_address1.getText().toString().length() < 2) {
                            error("Address line 1 minimum 2 characters ");
                            profile_address1.requestFocus();
                        } else if (profile_address2.getText().toString().length() == 1) {
                            error("Address line 2 minimum 2 characters ");
                            profile_address2.requestFocus();
                        } else if (profile_city.getText().toString().equals("")) {
                            error("Please enter city");
                            profile_city.requestFocus();
                        } else if (profile_city.getText().toString().length() < 2) {
                            error("City minimum 2 characters");
                            profile_city.requestFocus();
                        } else if (profile_zipcode.getText().toString().equals("")) {
                            error("Please enter zip code");
                            profile_zipcode.requestFocus();
                        } else if (profile_zipcode.getText().toString().length() < 4) {
                            error("Zip code minimum 4 characters");
                            profile_zipcode.requestFocus();
                        } else if (profile_pno.getText().toString().equals("")) {
                            error("Please enter phone number");
                            profile_pno.requestFocus();
                        } else if (profile_pno.getText().toString().length() < 5) {
                            error("Phone number minimum 5 numbers");
                            profile_pno.requestFocus();
                        } else {

                            if (!isNetworkAvailable()) {

                                alertdailogbox("update");
                            } else {
                                JSONObject object1 = new JSONObject();
                                JSONObject object2 = new JSONObject();
                                strfirstname = profile_name.getText().toString();
                                strzipcode = profile_zipcode.getText().toString();
                                strlastname = profile_lname.getText().toString();
                                strphone = profile_pno.getText().toString();
                                stradd1 = profile_address1.getText().toString();
                                stradd2 = profile_address2.getText().toString();
                                strcity = profile_city.getText().toString();
                                object1.put("first_name", strfirstname);
                                object1.put("last_name", strlastname);
                                object1.put("phone", strphone);
                                object1.put("address1", stradd1);
                                object1.put("address2", stradd2);
                                object1.put("state", state_values);
                                object1.put("city", strcity);
                                object1.put("zipcode", strzipcode);
                                object1.put("country", country_values);
                                object2.put("user", object1);

                                if(get_auth_token("user_player_type").equals("player")){
                                    if(profile_email.getText().toString().equals("")){
                                        error("please enter Email address");
                                        profile_email.requestFocus();
                                    }else
                                    if(password.getText().toString().equals("")){
                                        error("Please enter Password");
                                        password.requestFocus();
                                    }else if(password.getText().toString().length() < 8){
                                        error("password contains at least 8 characters");
                                        password.requestFocus();
                                    }else if(confirm_password.getText().toString().equals("")){
                                        error("Please enter confirm password");
                                        confirm_password.requestFocus();
                                    }else if(confirm_password.getText().toString().length() < 8){
                                        error("Confirm password contains at least 8 characters");
                                        confirm_password.requestFocus();
                                    } else if(!(confirm_password.getText().toString()).equals(password.getText().toString())){
                                        error("Confirm password mismatch");
                                        confirm_password.requestFocus();
                                        text_confirm_password.requestFocus();
                                    }else {
                                        object1.put("email",profile_email.getText().toString());
                                        object1.put("password", password.getText().toString());
                                        object1.put("password_confirmation", confirm_password.getText().toString());
                                        object2.put("user", object1);
                                        System.out.println("---object2-----" + object2);

                                        methodforuploadprofiledata(object2);
                                    }
                                }else {

                                    methodforuploadprofiledata(object2);

                                }

                            }

                        }



                }catch (Exception e){
                    //nothing
                }



            }
        });
    }
    @SuppressLint("WrongConstant")
    private void error(String s) {
        Snackbar snackbar = Snackbar.make(editpagelayout, s, Snackbar.LENGTH_LONG);
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

    public void methodforuploadprofiledata(JSONObject user){
        if (!pd.isShowing()){
            pd.show();
        }
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"users/edit_profile",user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (pd.isShowing()){
                                pd.dismiss();
                            }
                            if(response.getString("status").equals("Success")){
                                Toast.makeText(getApplicationContext(),"Profile updated sucessfully",Toast.LENGTH_LONG).show();
                                savestring("login_email", profile_email.getText().toString());
                                if (get_auth_token("user_player_type").equals("player")){
                                    Intent in = new Intent(EditAccount_Details.this, Courses_list_flow.class);
                                    savestring("user_player_type", "user");
                                    savestring("player_type","1");
                                    startActivity(in);
                                    finish();
                                    finishAffinity();
                                }else {
                                    finish();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception E){
                            //nothing
                            if (pd.isShowing()){
                                pd.dismiss();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (pd.isShowing()){
                            pd.dismiss();}
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(EditAccount_Details.this);
                            }else {
                                Toast.makeText(EditAccount_Details.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(EditAccount_Details.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
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




    public void makeJsonObjectRequest() {


        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"users/view_profile",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responses) {

                        try {

                                JSONObject response = responses.getJSONObject("user");
                                String name = response.getString("first_name").replaceAll("\\s+", " ");
                                String last_name = response.getString("last_name").replaceAll("\\s+", " ");
                                String email = response.getString("email").replaceAll("\\s+", " ");
                                String address1 = response.getString("address1").replaceAll("\\s+", " ");
                                String address2 = response.getString("address2").replaceAll("\\s+", " ");
                                String city = response.getString("city").replaceAll("\\s+", " ");
                                states = response.getString("state");
                                String zipcode = response.getString("zipcode").replaceAll("\\s+", " ");
                                String country_key = response.getString("country");
                                String pno = response.getString("phone").replaceAll("\\s+", " ");

                                try {
                                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                                } catch (Exception e) {

                                }

                                try {
                                    last_name = last_name.substring(0, 1).toUpperCase() + last_name.substring(1);
                                } catch (Exception e) {

                                }

                                try {
                                    address1 = address1.substring(0, 1).toUpperCase() + address1.substring(1);
                                } catch (Exception e) {

                                }

                                try {
                                    address2 = address2.substring(0, 1).toUpperCase() + address2.substring(1).trim();
                                } catch (Exception e) {

                                }

                                try {
                                    city = city.substring(0, 1).toUpperCase() + city.substring(1);
                                } catch (Exception e) {

                                }

                                if (name.equals("null") || name.equals("") || name.equals(null)) {
                                    profile_name.setText("");
                                } else {

                                    profile_name.setText(name.trim());
                                }

                                if (last_name.equals("null") || last_name.equals("") || last_name.equals(null)) {
                                    profile_lname.setText("");
                                } else {
                                    profile_lname.setText(last_name.trim());
                                }


                            if(get_auth_token("user_player_type").equals("player")) {
                                profile_email.setText("");
                                text_password.setVisibility(View.VISIBLE);
                                text_confirm_password.setVisibility(View.VISIBLE);
                                if(get_auth_token("email_show").equals("false")){
                                    profile_email.setText("");
                                    profile_email.setEnabled(true);
                                    profile_email.setFocusable(true);
                                    profile_email.setClickable(true);
                                    profile_email.setFocusableInTouchMode(true);
                                }else {
                                    profile_email.setEnabled(false);
                                    profile_email.setFocusable(false);
                                    profile_email.setClickable(false);
                                    profile_email.setFocusableInTouchMode(false);
                                    profile_email.setText(email);
                                }
                            }else{
                                profile_email.setEnabled(false);
                                profile_email.setFocusable(false);
                                profile_email.setClickable(false);
                                profile_email.setFocusableInTouchMode(false);
                                text_password.setVisibility(View.GONE);
                                text_confirm_password.setVisibility(View.GONE);
                                if (email.equals("null") || email.equals("") || email.equals(null)) {
                                    profile_email.setText("");
                                } else {
                                    profile_email.setText(email);
                                }

                            }


                                if (address1.equals("null") || address1.equals("") || address1.equals(null)) {
                                    profile_address1.setText("");
                                } else {
                                    profile_address1.setText(address1.trim());
                                }
                                if (address2.equals("null") || address2.equals("") || address2.equals(null)) {
                                    profile_address2.setText("");
                                } else {
                                    profile_address2.setText(address2);
                                }

                                if (city.equals("null") || city.equals("") || city.equals(null)) {
                                    profile_city.setText("");
                                } else {
                                    profile_city.setText(city.trim());
                                }

                                if (country_key.equals("null") || country_key.equals("") || country_key.equals(null)) {
//                                edt.setText("Not Mentioned");
                                    country_potion = 0;
                                    country.setSelection(country_potion);
                                    MethodToCalStateApi("");
                                } else {
                                    country_potion = Splash_screen.countrykeys.indexOf(country_key);
                                    country.setSelection(country_potion);
                                    MethodToCalStateApi(country_key);
                                }


                                if (zipcode.equals("null") || zipcode.equals("") || zipcode.equals(null)) {
                                    profile_zipcode.setText("");
                                } else {
                                    profile_zipcode.setText(zipcode);
                                }
                                if (pno.equals("null") || pno.equals("") || pno.equals(null)) {
                                    profile_pno.setText("");
                                } else {
                                    profile_pno.setText(pno);
                                }
                            pd.dismiss();
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
                                Alert_Dailog.showNetworkAlert(EditAccount_Details.this);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Toast.makeText(EditAccount_Details.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(EditAccount_Details.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
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


    private void MethodToCalStateApi(String county) {
        if(!pd.isShowing()) {
            pd.show();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"city_states/states?country="+county, null, new Response.Listener<JSONObject>() {
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

                    ArrayAdapter<String> adapter   = new ArrayAdapter<String>(EditAccount_Details.this,R.layout.custom_text, statevalues) {

                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);

                            ((TextView) v).setTypeface(regular);

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

                    //Getting the instance of AutoCompleteTextView
                    adapter.setDropDownViewResource( R.layout.custom_text);
                    statespinner.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                    try {
                        state_postion = statekeys.indexOf(states);
                    }catch (Exception e)
                    {
                        //nothing
                    }
                    try {
                        statespinner.setSelection(state_postion);
                    }catch (Exception e)
                    {
                        //nothing
                    }


                }
                catch (Exception e)
                {
                    //nothing
                }
                if(pd.isShowing())
                {
                    pd.dismiss();
                }}
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
                if(pd.isShowing())
                {
                    pd.dismiss();
                }

            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy( 20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void alertdailogbox(final String value){
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
                if(value.equals("profile")) {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("profile");
                    } else {
                        makeJsonObjectRequest();
                    }
                }
                else   if(value.equals("country"))
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("country");
                    }
                    else
                    {
                        MethodToCalStateApi(Splash_screen.countrykeys.get(country_potion));
                    }
                }
                else   if(value.equals("update"))
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("update");
                    }
                    else
                    {
                        try {
                            JSONObject object1 = new JSONObject();
                            JSONObject object2 = new JSONObject();
                            strfirstname = profile_name.getText().toString();
                            strzipcode = profile_zipcode.getText().toString();
                            strlastname = profile_lname.getText().toString();
                            strphone = profile_pno.getText().toString();
                            stradd1 = profile_address1.getText().toString();
                            stradd2 = profile_address2.getText().toString();
                            strcity = profile_city.getText().toString();
                            object1.put("first_name", strfirstname);
                            object1.put("last_name", strlastname);
                            object1.put("phone", strphone);
                            object1.put("address1", stradd1);
                            object1.put("address2", stradd2);
                            object1.put("state", state_values);
                            object1.put("city", strcity);
                            object1.put("zipcode", strzipcode);
                            object1.put("country", country_values);
                            object2.put("user", object1);
                            methodforuploadprofiledata(object2);
                        }
                        catch (Exception e)
                        {
                            //nothing
                        }
                    }
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
