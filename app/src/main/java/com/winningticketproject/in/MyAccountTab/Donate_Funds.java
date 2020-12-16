package com.winningticketproject.in.MyAccountTab;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextWatcher;
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
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.EventTab.Order_Preview;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;
import com.winningticketproject.in.Stripe_GetWay.Stripe_Main_Page;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;


public class Donate_Funds extends AppCompatActivity {
    EditText editext_amount,profile_name, profile_lname, profile_email, profile_address1, profile_address2, profile_city, profile_zipcode, profile_pno;
    TextInputLayout text_fname, text_lname, text_email, text_address1, text_address2, text_city, text_zipcode, text_pno;
    Button preview_order;
    int events_position;
    String auth_token = "";
    String state_values = "";
    String country_values = "";
    String output="";
    String client_tokens="";
    ArrayList<String> statevalues = new ArrayList<String>();
    ArrayList<String> statekeys = new ArrayList<String>();
    int country_potion = 0, state_postion = 0;
    Spinner events,country, statespinner;
    TextView cancel_purchage, account_title,full_address,billing_edit,oraganization,oragnization_amount,billing_address_title,countrytxt,citytxt,downaarow,text_wallete_balance,account_balance,text_use_wallete_balance,thank_you_content;
    int profile_btn_value=0;
    LinearLayout frofile_layout,address_layout,editpagelayout,applay_credit_balance,pay_not;
    String name;
    String last_name;
    String email;
    String address1;
    String address2;
    String city;
    String state_name;
    String zipcode;
    String country_key;
    String pno;
    String full_add1;
    String fulladd2;
    String full_city;
    String full_state;
    String full_country;
    String full_zip;
    String countryname;
    String statename;
    String click="0";
    ToggleButton toggleButton;
    ArrayList<String> eventsname= new ArrayList<>();
    ArrayList<Integer> eventsid= new ArrayList<>();
    ArrayAdapter oragnization_adapter;
    double wallete_amount=0,remainng_amount=0,coupon_amoun=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_donate__funds);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        auth_token = get_auth_token("auth_token");

        frofile_layout= findViewById(R.id.frofile_layout);
        address_layout= findViewById(R.id.address_layout);
        editpagelayout= findViewById(R.id.editpagelayout);

        events= findViewById(R.id.events);
        country = findViewById(R.id.country);

        statespinner = findViewById(R.id.state);
        preview_order = findViewById(R.id.preview_order);
        cancel_purchage = findViewById(R.id.cancel_purchage);
        cancel_purchage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });
        account_title = findViewById(R.id.account_title);
        full_address = findViewById(R.id.full_address);
        billing_edit= findViewById(R.id.billing_edit);
        billing_edit.setText("\uf040");
        billing_edit.setTypeface(webfont);
        billing_edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                frofile_layout.setVisibility(View.VISIBLE);
                address_layout.setVisibility(View.GONE);
                profile_btn_value=1;
                return false;
            }
        });
        account_title.setText("Donate");
        text_fname = findViewById(R.id.text_fname);
        text_lname = findViewById(R.id.text_lname);
        text_email = findViewById(R.id.text_email);
        text_address1 = findViewById(R.id.text_address1);
        text_address2 = findViewById(R.id.text_address2);
        text_city = findViewById(R.id.text_city);
        text_zipcode = findViewById(R.id.text_zipcode);
        text_pno = findViewById(R.id.text_pno);
        text_fname.setTypeface(regular);
        text_lname.setTypeface(regular);
        text_email.setTypeface(regular);
        text_address1.setTypeface(regular);
        text_address2.setTypeface(regular);
        text_zipcode.setTypeface(regular);
        text_pno.setTypeface(regular);

        thank_you_content = findViewById(R.id.thank_you_content);
        thank_you_content.setTypeface(regular);
        profile_name = findViewById(R.id.profile_name);
        profile_lname = findViewById(R.id.profile_lname);
        profile_email = findViewById(R.id.profile_email);
        profile_address1 = findViewById(R.id.profile_address1);
        profile_address2 = findViewById(R.id.profile_address2);
        profile_city = findViewById(R.id.profile_city);
        profile_zipcode = findViewById(R.id.profile_zipcode);
        profile_pno = findViewById(R.id.profile_pno);
        editext_amount = findViewById(R.id.editext_amount);

        account_balance = findViewById(R.id.account_balance);
        account_balance.setTypeface(medium);

        text_use_wallete_balance = findViewById(R.id.text_use_wallete_balance);
        text_use_wallete_balance.setTypeface(regular);


        editext_amount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>0){
                if(!s.toString().matches("(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})"))
                {
                    String userInput= ""+s.toString().replaceAll("[^\\d]", "");
                    StringBuilder cashAmountBuilder = new StringBuilder(userInput);

                    while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                        cashAmountBuilder.deleteCharAt(0);
                    }
                    while (cashAmountBuilder.length() < 3) {
                        cashAmountBuilder.insert(0, '0');
                    }
                    cashAmountBuilder.insert(cashAmountBuilder.length()-2, '.');
                    cashAmountBuilder.insert(0, "");

                    editext_amount.setText(cashAmountBuilder.toString());
                    // keeps the cursor always to the right
                    Selection.setSelection(editext_amount.getText(), cashAmountBuilder.toString().length());

                }
                }

            }
        });
//

        downaarow = findViewById(R.id.downarrow);
        downaarow.setText("\uf107");
        downaarow.setTypeface(webfont);



        text_wallete_balance = findViewById(R.id.text_wallete_balance);
        text_wallete_balance.setTypeface(regular);

        pay_not = findViewById(R.id.pay_not);
        toggleButton = findViewById(R.id.togal_button);
        applay_credit_balance = findViewById(R.id.applay_credit_balance);

        applay_credit_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click=="1"){
                    click="0";
                    downaarow.setText("\uf107");
                    downaarow.setTypeface(webfont);
                    pay_not.setVisibility(View.GONE);
                    text_wallete_balance.setVisibility(View.GONE);
//                    toggleButton.setChecked(false);
                }else {
                    click="1";
                    downaarow.setText("\uf106");
                    downaarow.setTypeface(webfont);
                    pay_not.setVisibility(View.VISIBLE);
                    text_wallete_balance.setVisibility(View.VISIBLE);

                }

            }
        });


        oraganization = findViewById(R.id.oraganization);
        oragnization_amount = findViewById(R.id.oragnization_amount);
        billing_address_title = findViewById(R.id.billing_address_title);
        countrytxt = findViewById(R.id.countrytxt);
        citytxt = findViewById(R.id.citytxt);
        countrytxt.setTypeface(regular);
        citytxt.setTypeface(regular);

        editext_amount.setTypeface(medium);
        profile_name.setTypeface(regular);
        profile_lname.setTypeface(regular);
        profile_email.setTypeface(regular);
        profile_address1.setTypeface(regular);
        profile_address2.setTypeface(regular);
        profile_city.setTypeface(regular);
        profile_zipcode.setTypeface(regular);
        profile_pno.setTypeface(regular);
        text_city.setTypeface(regular);
        cancel_purchage.setTypeface(medium);
        account_title.setTypeface(medium);
        preview_order.setTypeface(medium);

        oraganization.setTypeface(medium);
        oragnization_amount.setTypeface(medium);
        billing_address_title.setTypeface(medium);
        full_address.setTypeface(regular);

        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

        ArrayAdapter<String> adapter   = new ArrayAdapter<String>(Donate_Funds.this,R.layout.custom_text, Splash_screen.countryvalues) {

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



        if (!isNetworkAvailable()) {

            alertdailogbox("token");
        }
        else
        {

            makeJsonObjectRequestEvents(Splash_screen.url+"contributors/events");
            methodtogenrateclienttoken();
        }


        adapter.setDropDownViewResource( R.layout.custom_text);
        country.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

//        makeJsonObjectRequest();
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_values=Splash_screen.countrykeys.get(position);
                state_postion=0;
                country_potion=position;
//                if(position!=0){

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

                editext_amount.setFocusable(true);
                editext_amount.setFocusableInTouchMode(true);

                if(position>0) {
                    citytxt.setVisibility(View.VISIBLE);
                }
                else
                {
                    citytxt.setVisibility(View.GONE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        editext_amount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                editext_amount.setFocusable(true);
                editext_amount.setFocusableInTouchMode(true);

                return false;
            }
        });

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

        events.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                events_position=position;
                editext_amount.setFocusable(false);
                hiddenInputMethod();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

                editext_amount.setFocusable(false);
                hiddenInputMethod();

                // your code here
            }

        });





        preview_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hiddenInputMethod();

    if(profile_btn_value==0)
    {
    try {

        if (events_position == 0) {
            error("Please select event");
            editext_amount.setFocusable(false);
            editext_amount.setFocusableInTouchMode(false);
        } else if (editext_amount.getText().toString().equals("")||(editext_amount.getText().toString().equals(null)||editext_amount.getText().toString().equals("0"))) {
            error("Please enter amount");
            editext_amount.requestFocus();
            editext_amount.setFocusable(true);
            editext_amount.setFocusableInTouchMode(true);
        }else if (editext_amount.getText().toString().equals("")) {
            error("Amount should not be empty");
            editext_amount.requestFocus();
            editext_amount.setFocusable(true);
            editext_amount.setFocusableInTouchMode(true);
        } else if (editext_amount.getText().toString().equals("0.00") || editext_amount.getText().toString().equals("00.00") || editext_amount.getText().toString().equals("000.00") || editext_amount.getText().toString().equals("0000.00") || editext_amount.getText().toString().equals("00000.00") || editext_amount.getText().toString().equals("000000.00")) {
            error("Please enter greater than Zero");
            editext_amount.requestFocus();
            editext_amount.setFocusable(true);
            editext_amount.setFocusableInTouchMode(true);
        } else if (name.equals("")) {
            error("First name is empty, please update your first name");
        } else if (address1.equals("")) {
            error("Address is empty, please update address");
        }  else if (city.equals("")) {
            error("City is empty, please update city");
//        }else if (country_key .equals("")) {
//            error("Country is empty, please update country");
//        } else if (state_name .equals("")) {
//            error("State is empty, please update state");
        } else if (zipcode.equals("")) {
            error("Zip code is empty, please update zip code");
        } else if (pno.equals("")) {
            error("Phone number is empty, please update phone number");
        }
        else {


            if (!isNetworkAvailable()) {

                alertdailogbox("p1");
            } else {
                JSONObject object1 = new JSONObject();
                JSONObject object2 = new JSONObject();
                object1.put("first_name", name);
                object1.put("last_name", last_name);
                object1.put("phone", pno);
                object1.put("address_line1", address1);
                object1.put("address_line2", address2);
                object1.put("state", state_name);
                object1.put("city", city);
                object1.put("zip_code", zipcode);
                object1.put("country", country_key);
                object2.put("billing_address", object1);
                object2.put("event_id", eventsid.get(events_position));
                object2.put("price", editext_amount.getText().toString());

                if(toggleButton.isChecked())
                {
                    object2.put("fund_amount", "yes");
                }
                methodforuploadprofiledata(object2);
            }

        }
    }
    catch (Exception e)
    {

    }
}
else {

    try {

        if (events_position == 0) {
            error("Please select event");
            editext_amount.setFocusable(false);
            editext_amount.setFocusableInTouchMode(false);
        } else if (editext_amount.getText().toString().equals("0.00")) {
            error("Please enter amount");
            editext_amount.requestFocus();
        } else if (editext_amount.getText().toString().equals("")) {
            error("Please enter amount");
            editext_amount.requestFocus();
        } else if (editext_amount.getText().toString().equals("0.00")) {
            error("Please enter greater than Zero");
            editext_amount.requestFocus();
        }else  if (profile_name.getText().toString().equals("")) {
            error("Please enter first name");
            profile_name.requestFocus();
        } else if (profile_name.getText().toString().length() < 2) {
            error("First name minimum 2 characters");
            profile_name.requestFocus();
        } else if (profile_address1.getText().toString().equals("")) {
            error("Please enter address line 1");
            profile_address1.requestFocus();

        } else if (profile_address1.getText().toString().length() < 2) {
            error("Address line 1 minimum 2 characters ");
            profile_address1.requestFocus();

        } else if (profile_address2.getText().toString().length()==1) {
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

                alertdailogbox("p2");
            } else {
                String strfirstname, strlastname, stradd1, stradd2, strcity, strzipcode, strphone;
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
                object1.put("address_line1", stradd1);
                object1.put("address_line2", stradd2);
                object1.put("state", state_values);
                object1.put("city", strcity);
                object1.put("zip_code", strzipcode);
                object1.put("country", country_values);
                object2.put("billing_address", object1);
                object2.put("event_id", eventsid.get(events_position));
                object2.put("price", editext_amount.getText().toString());
                if(toggleButton.isChecked())
                {
                    object2.put("fund_amount", "yes");
                }
                methodforuploadprofiledata(object2);

            }


        }

                    } catch (Exception e) {
                        //nothing
                    }
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
            }else {

                view.setFocusableInTouchMode(true);
                view.setFocusable(true);
            }
        }
        return ret;
    }


    public void makeJsonObjectRequestEvents(String url) {
        try {
            ProgressDialog.getInstance().showProgress(Donate_Funds.this);
        }catch (Exception e){
            //nothing
        }
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        eventsname.clear();
                        eventsid.clear();

                        eventsname.add("Select Event");
                        eventsid.add(0);


//                        {"events":[[153,"abcdabcdabcdabcdabcdabcdabcd"]],"status":"Success"}
                        try {
                            if (response.getString("status").equals("Success")) {
                                JSONArray eventarray=response.getJSONArray("events");
                               for(int i=0;i<eventarray.length();i++)
                               {
                                   JSONArray evntarrayresponse=eventarray.getJSONArray(i);
                                   eventsname.add(evntarrayresponse.getString(1).substring(0,1).toUpperCase() +evntarrayresponse.getString(1).substring(1));
                                   eventsid.add(evntarrayresponse.getInt(0));

                               }
                                oragnization_adapter  = new ArrayAdapter<String>(Donate_Funds.this,R.layout.custom, eventsname) {

                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        View v = super.getView(position, convertView, parent);
                                        ((TextView) v).setTypeface(regular);

                                        return v;
                                    }


                                    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                                        View v =super.getDropDownView(position, convertView, parent);

                                        editext_amount.setFocusable(false);
                                        hiddenInputMethod();

                                        ((TextView) v).setTypeface(regular);
                                        if(((TextView) v).getText().toString().equals("Select Event")){
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
                                oragnization_adapter.setDropDownViewResource(R.layout.custom_text);
                                events.setAdapter(oragnization_adapter);




                                makeJsonObjectRequest();
                            } else {
                                error("No events");
                            }
                        }catch (Exception e)
                        {
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
                                Alert_Dailog.showNetworkAlert(Donate_Funds.this);
                            }else {
                                ProgressDialog.getInstance().hideProgress();
                                Toast.makeText(Donate_Funds.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            ProgressDialog.getInstance().hideProgress();
                            Toast.makeText(Donate_Funds.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                }){
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
    public void makeJsonObjectRequest() {


        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"users/view_profile",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responses) {
                        try {
                            JSONObject response=responses.getJSONObject("user");


                             name = response.getString("first_name").replaceAll("\\s+"," ");
                             last_name = response.getString("last_name").replaceAll("\\s+"," ");
                             email = response.getString("email");
                             address1 = response.getString("address1").replaceAll("\\s+"," ");
                             address2 = response.getString("address2").replaceAll("\\s+"," ");
                             city = response.getString("city").replaceAll("\\s+"," ");
                             state_name = response.getString("state");
                             zipcode = response.getString("zipcode");
                             country_key = response.getString("country");
                             pno = response.getString("phone");
                             countryname=responses.getString("country");
                             statename=responses.getString("state");


                            try {
                                wallete_amount = responses.getDouble("wallet");
                                DecimalFormat df = new DecimalFormat("0.00");
                                String formate = df.format(wallete_amount);
                                text_wallete_balance.setText(Html.fromHtml("Current Balance: "+"<medium><b>" + "$"+formate+ "</b></medium>" ));
                                if (wallete_amount>0){
                                    downaarow.setText("\uf106");
                                    downaarow.setTypeface(webfont);
                                    click = "1";
                                    toggleButton.setChecked(true);
                                    pay_not.setVisibility(View.VISIBLE);
                                    text_wallete_balance.setVisibility(View.VISIBLE);

                                }else {

                                    downaarow.setText("\uf107");
                                    downaarow.setTypeface(webfont);

                                    click = "0";
                                    pay_not.setVisibility(View.GONE);
                                    toggleButton.setChecked(false);
                                    text_wallete_balance.setVisibility(View.GONE);

                                }

                            }
                            catch (Exception e)
                            {
                                //nothing
                            }


                            try{
                                name=name.substring(0,1).toUpperCase() +name.substring(1);
                            }
                            catch (Exception e)
                            {

                            }

                            try{
                                last_name=last_name.substring(0,1).toUpperCase() +last_name.substring(1);
                            }
                            catch (Exception e)
                            {

                            }

                            try{
                                address1=address1.substring(0,1).toUpperCase() +address1.substring(1);
                            }
                            catch (Exception e)
                            {

                            }

                            try{
                                address2=address2.substring(0,1).toUpperCase() +address2.substring(1).trim();
                            }
                            catch (Exception e)
                            {

                            }

                            try{
                                city=city.substring(0,1).toUpperCase() +city.substring(1);
                            }
                            catch (Exception e)
                            {

                            }

                            if(name.equals("null") || name.equals("") || name.equals(null)){
                                profile_name.setText("");
                                name="";
                            }else {
                                profile_name.setText(name.trim().replaceAll("\\s+"," "));
                            }

                            if(last_name.equals("null") || last_name.equals("") || last_name.equals(null)){
                                profile_lname.setText("");
                                last_name="";
                            }else {
                                profile_lname.setText( last_name.trim().replaceAll("\\s+"," "));
                            }

                            if(email.equals("null") || email.equals("") || email.equals(null)){
                                profile_email.setText("");
                                email="";
                            }else {
                                profile_email.setText(email);
                            }


                            if(address1.equals("null") || address1.equals("") || address1.equals(null)){
                                profile_address1.setText("");
                                address1="";
                                full_add1="";
                            }else {
                                profile_address1.setText(address1.trim().replaceAll("\\s+"," "));
                                full_add1=address1.replaceAll("\\s+"," ")+", ";
                            }

                            if(address2.equals("null") || address2.equals("") || address2.equals(null)){
                                profile_address2.setText("");
                                address2="";
                                fulladd2="";
                            }else {
                                    profile_address2.setText(address2.trim().replaceAll("\\s+"," "));
                                fulladd2=address2.replaceAll("\\s+"," ")+", ";
                            }

                            if(city.equals("null") || city.equals("") || city.equals(null)){
                                profile_city.setText("");
                                city="";
                                full_city="";
                            }else {
                                profile_city.setText( city.trim().replaceAll("\\s+"," "));
                                full_city= city.replaceAll("\\s+"," ")+", ";
                            }

                            if(statename.equals("null") || statename.equals("") || statename.equals(null)){
                                full_state="";
                            }else {
                                full_state=statename+", ";
                            }

                            if(countryname.equals("null") || countryname.equals("") || countryname.equals(null)){
                                full_country="";
                            }else {
                                full_country=countryname+", ";
                            }


                            if(state_name.equals("null") || state_name.equals("") || state_name.equals(null)){
                                state_name="";
                            }



                            if(country_key.equals("null") || country_key.equals("") || country_key.equals(null)){
//                                edt.setText("Not Mentioned");
                                country_potion =0;
                                country.setSelection(country_potion);
                                MethodToCalStateApi(country_key);
                            }else {
                                country_potion = Splash_screen.countrykeys.indexOf(country_key);
                                country.setSelection(country_potion);
                            }



                            if(zipcode.equals("null") || zipcode.equals("") || zipcode.equals(null)){
                                profile_zipcode.setText("");
                                zipcode="";
                                full_zip="";
                            }else {
                                profile_zipcode.setText(zipcode.replaceAll("\\s+"," "));
                                full_zip=zipcode+", ";
                            }


                            if(pno.equals("null") || pno.equals("") || pno.equals(null)){
                                profile_pno.setText("");
                                pno="";
                            }else {
                                profile_pno.setText(pno);
                            }

                            String full_addresss=name.trim()+" "+last_name.trim()+", "+"\n"+full_add1+fulladd2+"\n"+full_city+""+full_state+""+full_zip+"\n"+full_country+"\nPhone: "+pno;
                            full_address.setText(full_addresss);
                            ProgressDialog.getInstance().hideProgress();
                        }catch (Exception E){
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
    private void MethodToCalStateApi(String county) {
        ProgressDialog.getInstance().showProgress(Donate_Funds.this);
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

                    ArrayAdapter<String> adapter   = new ArrayAdapter<String>(Donate_Funds.this,R.layout.custom_text, statevalues) {

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
                        state_postion = statekeys.indexOf(state_name);
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
                ProgressDialog.getInstance().hideProgress();
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

    public void methodforuploadprofiledata(JSONObject user){
        ProgressDialog.getInstance().showProgress(Donate_Funds.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"contributors/donation_order",user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ProgressDialog.getInstance().hideProgress();
                            if(response.getString("status").equals("Success")){

                                remainng_amount = Double.parseDouble(editext_amount.getText().toString());

                                output=response+"";
                                client_tokens = client_tokens.replaceAll("\"", "");


                                if(toggleButton.isChecked())
                                {
                                    if(remainng_amount>wallete_amount){

                                        Intent in = new Intent(getApplicationContext(), Stripe_Main_Page.class);
                                        startActivityForResult(in,1);

                                    }else {
                                    Intent in = new Intent(getApplicationContext(), Order_Preview.class);
                                    in.putExtra("respose", output);
                                    Share_it.savestring("user_type", "paid");
                                    in.putExtra("page", "donate_fund");
                                    in.putExtra("pageliveauction","2");
                                    in.putExtra("wallet", "1");
                                    in.putExtra("pat_ment_type","Wallet");
                                    in.putExtra("discount_pric",coupon_amoun);
                                    in.putExtra("wallete_balance",wallete_amount) ;
                                    savestring("quantity","1");
                                    savestring("ticket_price",editext_amount.getText().toString());
                                    savestring("name",events.getSelectedItem().toString());
                                    savestring("event_name",events.getSelectedItem().toString());

                                        startActivity(in);
                                    finish();
                                    }
                                }
                                else
                                {
                                    Intent in = new Intent(getApplicationContext(),Stripe_Main_Page.class);
                                    startActivityForResult(in,1);
                                }
                                
                            }else {
                                Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception E){
                            //nothing
                            ProgressDialog.getInstance().hideProgress();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ProgressDialog.getInstance().hideProgress();
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(Donate_Funds.this);
                            }else {
                                Toast.makeText(Donate_Funds.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Donate_Funds.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
    private void methodtogenrateclienttoken() {

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"contributors/client_token",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            client_tokens = response.getString("client_token")+"";
                        }catch (Exception E){
                            //nothing
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (RESULT_OK == resultCode){

                final String result=data.getStringExtra("Payment_token");
                final String source_id =data.getStringExtra("source_id");

                Intent in = new Intent(getApplicationContext(),Order_Preview.class);
                in.putExtra("respose", output);
                in.putExtra("page", "donate_fund");
                in.putExtra("payment-source", source_id);
                in.putExtra("nonce", result);

                in.putExtra("pageliveauction","2");
                in.putExtra("discount_pric",coupon_amoun);
                in.putExtra("wallete_balance",wallete_amount);
                if(toggleButton.isChecked())
                {
                    in.putExtra("wallet", "2");
                    in.putExtra("pat_ment_type","Wallet & Visa");
                }
                else
                {
                    in.putExtra("wallet", "0");
                    in.putExtra("pat_ment_type","Visa");

                }

                savestring("quantity","1");
                savestring("ticket_price",editext_amount.getText().toString());

                startActivity(in);
                finish();
                //send to your server
            }else if(resultCode == Activity.RESULT_CANCELED){
            }else {
//                Exception error = (Exception)data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
        }
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
                dialog.dismiss();
                if(value.equals("token")) {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("token");
                    } else {
                        methodtogenrateclienttoken();
                        makeJsonObjectRequestEvents(Splash_screen.url+"contributors/events");
                    }
                }
                else if(value.equals("country"))
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("country");
                    } else {
                        MethodToCalStateApi(Splash_screen.countrykeys.get(country_potion));
                    }

                }
                else if(value.equals("p1"))
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("p1");
                    } else {
                        try {
                            JSONObject object1 = new JSONObject();
                            JSONObject object2 = new JSONObject();
                            object1.put("first_name", name);
                            object1.put("last_name", last_name);
                            object1.put("phone", pno);
                            object1.put("address_line1", address1);
                            object1.put("address_line2", address2);
                            object1.put("state", state_name);
                            object1.put("city", city);
                            object1.put("zip_code", zipcode);
                            object1.put("country", country_key);
                            object2.put("billing_address", object1);
                            object2.put("event_id", eventsid.get(events_position));
                            object2.put("price", editext_amount.getText().toString());
                            if(toggleButton.isChecked())
                            {
                                object2.put("fund_amount", "yes");
                            }
                            methodforuploadprofiledata(object2);
                        }
                        catch (Exception e)
                        {
                            //nothing
                        }
                    }

                }
                else if(value.equals("p2"))
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("p2");
                    } else {
                        try {
                            String strfirstname, strlastname, stradd1, stradd2, strcity, strzipcode, strphone;
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
                            object1.put("address_line1", stradd1);
                            object1.put("address_line2", stradd2);
                            object1.put("state", state_values);
                            object1.put("city", strcity);
                            object1.put("zip_code", strzipcode);
                            object1.put("country", country_values);
                            object2.put("billing_address", object1);
                            object2.put("event_id", eventsid.get(events_position));
                            object2.put("price", editext_amount.getText().toString());
                            if(toggleButton.isChecked())
                            {
                                object2.put("fund_amount", "yes");
                            }
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
    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
