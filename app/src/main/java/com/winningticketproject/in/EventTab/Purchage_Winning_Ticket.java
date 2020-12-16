package com.winningticketproject.in.EventTab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;
import com.winningticketproject.in.Stripe_GetWay.Stripe_Main_Page;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.countrykeys;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.SignInSingup.Splash_screen.webfont;
import static com.winningticketproject.in.AppInfo.Share_it.decimal_amount;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;

public class Purchage_Winning_Ticket extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {
    EditText profile_name;
    EditText profile_lname;
    EditText profile_email;
    EditText profile_address1;
    EditText profile_address2;
    EditText profile_city;
    EditText profile_zipcode;
    EditText profile_pno;
    TextInputLayout text_fname, text_lname, text_email, text_address1, text_address2, text_city, text_zipcode, text_pno;
    Button preview_order;
    String  state_values = "";
    String country_values = "";
    String states = "";
    String output="";
    String pageliveauction="";
    String click2="0";
    Spinner country;
    Spinner statespinner;
    int profile_btn_value=0;
    LinearLayout edit_address_layout;
    LinearLayout address_layout;
    LinearLayout editpagelayout;
    LinearLayout succes_layout;
    LinearLayout applay_credit_balance;
    LinearLayout pay_not;
    String name,last_name,email,address1,address2,city,state_name,zipcode,country_key,pno="";
    TextView citytxt,countrytxt,success_status,Congrats_mesage,payment_downarrow,text_wallete_balance,account_balance,text_use_wallete_balance,view_hide;
    Intent in;
    String auth_token="";
    String order_item_id;
    String wallete_status;
    String strfirstname;
    String strlastname;
    String stradd1;
    String stradd2;
    String strcity;
    String strzipcode;
    String strphone;
    String full_add1;
    String fulladd2;
    String full_city;
    String statename;
    String full_state;
    String countryname;
    String full_country;
    String full_zip;
    String finalprice="";
    String order_id="";
    Animation animFadein;
    ImageView remove_icon;
    DecimalFormat df;
    double discount_pric=0,wallete_amount=0,remainng_amount=0,total_price;
    ToggleButton toggleButton;
    String page_type="";
    int country_potion = 0, state_postion = 0;

    ArrayList<String> statevalues = new ArrayList<String>();
    ArrayList<String> statekeys = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_purchage__winning__ticket);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editpagelayout = findViewById(R.id.editpagelayout);
        address_layout = findViewById(R.id.address_layout);

        auth_token = get_auth_token("auth_token");

        in = getIntent();
        df = new DecimalFormat("0.00");
        pageliveauction = in.getStringExtra("pageliveauction");

        if (pageliveauction.equals("3")){
            wallete_status = in.getStringExtra("wallet_status");
            wallete_amount  = Double.parseDouble(get_auth_token("wallete"));
        }else {
            order_item_id = in.getStringExtra("order_item_id");
            wallete_status = in.getStringExtra("wallet_status");
            order_id =  in.getStringExtra("order_id");
            wallete_amount  = Double.parseDouble(get_auth_token("wallete"));
        }

        country = findViewById(R.id.country);

        statespinner = findViewById(R.id.state);
        preview_order = findViewById(R.id.preview_order);

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.side_down);
        animFadein.setAnimationListener(this);

        billing_address();

        if (!isNetworkAvailable()) {

            alertdailogbox("main");
        }
        else
        {
            makeJsonObjectRequest();
        }

        section_one();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Purchage_Winning_Ticket.this, R.layout.custom_text, Splash_screen.countryvalues) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(regular);
                return v;
            }


            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
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

        payment_downarrow = findViewById(R.id.payment_downarrow);
        payment_downarrow.setText("\uf107");
        payment_downarrow.setTypeface(webfont);

        text_wallete_balance = findViewById(R.id.text_wallete_balance);
        text_wallete_balance.setTypeface(regular);


        account_balance = findViewById(R.id.account_balance);
        account_balance.setTypeface(medium);

        text_use_wallete_balance = findViewById(R.id.text_use_wallete_balance);
        text_use_wallete_balance.setTypeface(regular);

        toggleButton = findViewById(R.id.togal_button);
        pay_not = findViewById(R.id.pay_not);
        applay_credit_balance = findViewById(R.id.applay_credit_balance);

        text_wallete_balance.setTypeface(regular);
        DecimalFormat df = new DecimalFormat("0.00");
        String formate = df.format(wallete_amount);
        text_wallete_balance.setText(Html.fromHtml("Current Balance: "+"<medium><b>" + "$"+formate+ "</b></medium>" ));

        if (wallete_amount>0){

            payment_downarrow.setText("\uf106");
            payment_downarrow.setTypeface(webfont);

            toggleButton.setChecked(true);
            pay_not.setVisibility(View.VISIBLE);
            text_wallete_balance.setVisibility(View.VISIBLE);

        }else {

            payment_downarrow.setText("\uf107");
            payment_downarrow.setTypeface(webfont);

            pay_not.setVisibility(View.GONE);
            toggleButton.setChecked(false);
            text_wallete_balance.setVisibility(View.GONE);
        }

        applay_credit_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(click2=="1"){
                    click2="0";
                    payment_downarrow.setText("\uf107");
                    payment_downarrow.setTypeface(webfont);
                    pay_not.setVisibility(View.GONE);
                    text_wallete_balance.setVisibility(View.GONE);
//                    toggleButton.setChecked(false);
                }else {
                    click2="1";
                    payment_downarrow.setText("\uf106");
                    payment_downarrow.setTypeface(webfont);
                    pay_not.setVisibility(View.VISIBLE);
                    text_wallete_balance.setVisibility(View.VISIBLE);
                }

            }
        });


        adapter.setDropDownViewResource(R.layout.custom_text);
        country.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_values = countrykeys.get(position);
                state_postion = 0;
                country_potion = position;
                if(position>0) {
                    countrytxt.setVisibility(View.VISIBLE);
                }
                else
                {
                    countrytxt.setVisibility(View.GONE);
                }

                if (!isNetworkAvailable()) {
                    alertdailogbox("country");
                } else {

                    MethodToCalStateApi(countrykeys.get(position));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                state_values = statekeys.get(position);
                state_postion = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        preview_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remainng_amount = Double.parseDouble(finalprice);
                if (toggleButton.isChecked()) {
                    if (remainng_amount > wallete_amount) {
                        wallete_status = "2";
                    } else {
                        wallete_status = "1";
                    }
                } else {
                    wallete_status = "0";
                }

                hiddenInputMethod();

                if (profile_btn_value == 0) {
                    try {
                        if (name.equals("")) {
                            error("First name is empty, please update your first name");
                        } else if (address1.equals("")) {
                            error("Address is empty, please update address");
                        } else if (city.equals("")) {
                            error("City is empty, please update city");
                        } else if (zipcode.equals("")) {
                            error("Zip code is empty, please update zip code");
                        } else if (pno.equals("")) {
                            error("Phone number is empty, please update phone number");
                        } else {

                            if (!isNetworkAvailable()) {

                                alertdailogbox("p1");
                            } else {
                                if (Share_it.get_auth_token("play_type").equals("paid")){

                                    strfirstname = profile_name.getText().toString();
                                    strzipcode = profile_zipcode.getText().toString();
                                    strlastname = profile_lname.getText().toString();
                                    strphone = profile_pno.getText().toString();
                                    stradd1 = profile_address1.getText().toString();
                                    stradd2 = profile_address2.getText().toString();
                                    strcity = profile_city.getText().toString();
                                    JSONObject object1 = new JSONObject();
                                    JSONObject object2 = new JSONObject();
                                    object1.put("first_name", strfirstname);
                                    object1.put("last_name", strlastname);
                                    object1.put("address_line1", stradd1);
                                    object1.put("address_line2", stradd2);
                                    object1.put("city", strcity);
                                    object1.put("country", country_values);
                                    object1.put("zip_code", strzipcode);
                                    object1.put("state", state_values);
                                    object1.put("phone", strphone);
                                    object2.put("price", get_auth_token("price"));
                                    object2.put("billing_address", object1);
                                    object2.put("premium_type", get_auth_token("premium_type"));
                                    if (wallete_status.equals("1") || wallete_status.equals("2")) {
                                        object2.put("fund_amount", "yes");
                                    }
                                    System.out.println("--------object---" + object2);
                                    method_cal_premium(object2);

                                }else {

                                    strfirstname = profile_name.getText().toString();
                                    strzipcode = profile_zipcode.getText().toString();
                                    strlastname = profile_lname.getText().toString();
                                    strphone = profile_pno.getText().toString();
                                    stradd1 = profile_address1.getText().toString();
                                    stradd2 = profile_address2.getText().toString();
                                    strcity = profile_city.getText().toString();
                                    JSONObject object1 = new JSONObject();
                                    JSONObject object2 = new JSONObject();
                                    object1.put("first_name", strfirstname);
                                    object1.put("last_name", strlastname);
                                    object1.put("address_line1", stradd1);
                                    object1.put("address_line2", stradd2);
                                    object1.put("city", strcity);
                                    object1.put("country", country_values);
                                    object1.put("zip_code", strzipcode);
                                    object1.put("state", state_values);
                                    object1.put("phone", strphone);
                                    object1.put("order_id", order_item_id);
                                    object2.put("billing_address", object1);
                                    if (wallete_status.equals("1") || wallete_status.equals("2")) {
                                        object2.put("fund_amount", "yes");
                                    }
                                    System.out.println("--------object---" + object2);

                                    methodforuploadprofiledata(object2);
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                } else {

                    try {

                        if (profile_name.getText().toString().equals("")) {
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

                                if (Share_it.get_auth_token("play_type").equals("paid")) {
                                    strfirstname = profile_name.getText().toString();
                                    strzipcode = profile_zipcode.getText().toString();
                                    strlastname = profile_lname.getText().toString();
                                    strphone = profile_pno.getText().toString();
                                    stradd1 = profile_address1.getText().toString();
                                    stradd2 = profile_address2.getText().toString();
                                    strcity = profile_city.getText().toString();
                                    JSONObject object1 = new JSONObject();
                                    JSONObject object2 = new JSONObject();
                                    object1.put("first_name", strfirstname);
                                    object1.put("last_name", strlastname);
                                    object1.put("address_line1", stradd1);
                                    object1.put("address_line2", stradd2);
                                    object1.put("city", strcity);
                                    object1.put("country", country_values);
                                    object1.put("zip_code", strzipcode);
                                    object1.put("state", state_values);
                                    object1.put("phone", strphone);
                                    object2.put("price", get_auth_token("price"));
                                    object2.put("billing_address", object1);
                                    object2.put("premium_type", get_auth_token("premium_type"));

                                    if (wallete_status.equals("1") || wallete_status.equals("2")) {
                                        object2.put("fund_amount", "yes");
                                    }
                                    System.out.println("--------object---" + object2);
                                    method_cal_premium(object2);
                                }else {

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
                                        object1.put("address_line1", stradd1);
                                        object1.put("address_line2", stradd2);
                                        object1.put("city", strcity);
                                        object1.put("country", country_values);
                                        object1.put("zip_code", strzipcode);
                                        object1.put("state", state_values);
                                        object1.put("phone", strphone);
                                        object1.put("order_id", order_item_id);
                                        object2.put("billing_address", object1);

                                        if (wallete_status.equals("1") || wallete_status.equals("2")) {
                                            object2.put("fund_amount", "yes");
                                        }
                                        methodforuploadprofiledata(object2);
                                    } catch (Exception e) {
                                        //nothing
                                    }
                                }
                            }

                        }


                    } catch (Exception e) {
                        //nothing
                    }

                }

            }
        });

    }

    private void billing_address() {

        countrytxt= findViewById(R.id.countrytxt);
        citytxt= findViewById(R.id.citytxt);

        countrytxt.setTypeface(regular);
        citytxt.setTypeface(regular);

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
        profile_name = findViewById(R.id.profile_name);
        profile_lname = findViewById(R.id.profile_lname);
        profile_email = findViewById(R.id.email_address);
        profile_address1 = findViewById(R.id.profile_address1);
        profile_address2 = findViewById(R.id.profile_address2);
        profile_city = findViewById(R.id.profile_city);
        profile_zipcode = findViewById(R.id.profile_zipcode);
        profile_pno = findViewById(R.id.profile_pno);

        profile_name.setTypeface(regular);
        profile_lname.setTypeface(regular);
        profile_email.setTypeface(regular);
        profile_address1.setTypeface(regular);
        profile_address2.setTypeface(regular);
        profile_city.setTypeface(regular);
        profile_zipcode.setTypeface(regular);
        profile_pno.setTypeface(regular);
        text_city.setTypeface(regular);
        preview_order.setTypeface(medium);

        view_hide = findViewById(R.id.view_hide);

        succes_layout = findViewById(R.id.succes_layout);
        success_status = findViewById(R.id.success_status);
        remove_icon = findViewById(R.id.remove_icon);
        Congrats_mesage = findViewById(R.id.Congrats_mesage);
        remove_icon.setOnClickListener(this);

        success_status.setTypeface(semibold);
        Congrats_mesage.setTypeface(medium);


       TextView home_icon = findViewById(R.id.home_icon);
        home_icon.setTypeface(webfont);

        TextView billing_address = findViewById(R.id.billing_address);
        billing_address.setTypeface(medium);


        edit_address_layout = findViewById(R.id.edit_address_layout);
        TextView full_address_payment = findViewById(R.id.full_address_payment);
        full_address_payment.setTypeface(regular);

        TextView edit_icon = findViewById(R.id.edit_icon);
        edit_icon.setTypeface(regular);
        edit_icon.setPaintFlags(edit_icon.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        edit_icon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                address_layout.setVisibility(View.VISIBLE);
                edit_address_layout.setVisibility(View.GONE);
                profile_btn_value = 1;
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode){

            final String result=data.getStringExtra("Payment_token");
            final String source_id =data.getStringExtra("source_id");


                Intent in = new Intent(getApplicationContext(),Order_Preview.class);
                in.putExtra("respose", output);
                in.putExtra("page", page_type);
                in.putExtra("pageliveauction",pageliveauction);
                in.putExtra("payment-source", source_id);
                in.putExtra("nonce", result);
                in.putExtra("pat_ment_type","Visa");
                in.putExtra("discount_pric",discount_pric);
                in.putExtra("wallete_balance",wallete_amount) ;

                if(wallete_status.equals("2"))
                {
                    in.putExtra("wallet", "2");
                    in.putExtra("pat_ment_type","Wallet & "+"Visa");

                    if (discount_pric>0){
                        in.putExtra("pat_ment_type","Wallet & "+"Visa"+ " & VIP Payment");
                    }

                }
                else if(wallete_status.equals("0"))
                {
                    in.putExtra("wallet", "0");
                    in.putExtra("pat_ment_type","Visa");

                    if (discount_pric>0){
                        in.putExtra("pat_ment_type","Visa"+ " & VIP Payment");
                    }
                }
                startActivity(in);
                finish();

                //send to your server
            }else if(resultCode == Activity.RESULT_CANCELED){

            }else {

//                Exception error = (Exception)data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            }

    }


    public void section_one() {

        TextView title_page = findViewById(R.id.title_page);
        title_page.setTypeface(medium);

        TextView title_desciption = findViewById(R.id.title_desciption);
        title_desciption.setTypeface(regular);


        ImageView event_logo = findViewById(R.id.event_logo);
        if (get_auth_token("imge_url").equals("") || get_auth_token("imge_url").equals("null") || get_auth_token("imge_url").equals(null)){
            savestring("imge_url","null");
        }
        try {
            Picasso.with(Purchage_Winning_Ticket.this)
                    .load(get_auth_token("imge_url")).error(R.drawable.auto_image).placeholder(R.drawable.logo)
                    .into(event_logo);
        }catch (Exception e){
        }

        TextView event_access_name = findViewById(R.id.payment_access_name);
        event_access_name.setTypeface(medium);

        TextView payment_event_name = findViewById(R.id.payment_event_name);
        payment_event_name.setTypeface(regular);
        payment_event_name.setText(get_auth_token("name"));

        TextView include_title = findViewById(R.id.payment_include_title);
        include_title.setTypeface(medium);
        include_title.setPaintFlags(include_title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView payment_include_data = findViewById(R.id.payment_include_data);
        payment_include_data.setTypeface(regular);

        if (get_auth_token("premium_type").equals("month") || get_auth_token("premium_type").equals("year")){
            event_access_name.setText("Premium Membersip");
            payment_event_name.setVisibility(View.GONE);
            include_title.setVisibility(View.GONE);
            payment_include_data.setVisibility(View.GONE);
        }

        payment_include_data.setText(get_auth_token("aminities"));

        method_to_calculate();

    }

    public void method_to_calculate(){

        TextView payment_price_title = findViewById(R.id.payment_price_title);
        payment_price_title.setTypeface(regular);
        int qty = Integer.parseInt(get_auth_token("quantity"));
        if (qty>1){
            payment_price_title.setText("Purchase Price (X"+qty+")");
        }

        String final_total_amount = String.valueOf((Double.parseDouble(get_auth_token("ticket_price")) * Integer.parseInt(get_auth_token("quantity"))));

        TextView payment_amount = findViewById(R.id.payment_amount);
        payment_amount.setTypeface(regular);
        payment_amount.setText("$"+decimal_amount(Double.parseDouble(final_total_amount)));

        TextView payment_processing_title = findViewById(R.id.payment_processing_title);
        payment_processing_title.setTypeface(regular);

        TextView payment_total_title = findViewById(R.id.payment_total_title);
        payment_total_title.setTypeface(medium);

        TextView payment_total_amount = findViewById(R.id.payment_total_amount);
        payment_total_amount.setTypeface(medium);
        payment_total_amount.setText("$"+decimal_amount(Double.parseDouble(final_total_amount)));

        total_price = Double.parseDouble(decimal_amount(Double.parseDouble(final_total_amount)));

        if (pageliveauction.equals("3")){
            finalprice = df.format(total_price);
        }else if (pageliveauction.equals("1")) {
            finalprice = df.format(total_price);
        }else {
            finalprice = df.format(total_price);
        }

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

    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public void method_cal_premium(JSONObject user){

        System.out.println("------user-----"+user);

        ProgressDialog.getInstance().showProgress(Purchage_Winning_Ticket.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"contributors/premium_order",user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("--------response--------"+response);
                            ProgressDialog.getInstance().hideProgress();
                            if(response.getString("status").equals("Success")){
                                remainng_amount = Double.parseDouble(get_auth_token("price"));
                                output=response+"";

                                page_type="individual_play";
                                if(toggleButton.isChecked())
                                {

                                    if(remainng_amount>wallete_amount){

                                        Intent in = new Intent(getApplicationContext(), Stripe_Main_Page.class);
                                        startActivityForResult(in,1);

                                    }else {
                                        Intent in = new Intent(getApplicationContext(),Order_Preview.class);
                                        in.putExtra("respose", output);
                                        in.putExtra("page", page_type);
                                        in.putExtra("pageliveauction","3");
                                        in.putExtra("wallet", "1");
                                        in.putExtra("pat_ment_type","Wallet");
                                        in.putExtra("discount_pric", discount_pric);
                                        in.putExtra("wallete_balance",wallete_amount) ;
                                        startActivity(in);
                                        finish();
                                    }
                                }
                                else
                                {
                                    Intent in = new Intent(getApplicationContext(), Stripe_Main_Page.class);
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
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                ProgressDialog.getInstance().hideProgress();
                                Alert_Dailog.showNetworkAlert(Purchage_Winning_Ticket.this);
                            }else {
                                ProgressDialog.getInstance().hideProgress();
                                Toast.makeText(Purchage_Winning_Ticket.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            ProgressDialog.getInstance().hideProgress();
                            Toast.makeText(Purchage_Winning_Ticket.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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



    public void methodforuploadprofiledata(JSONObject user){
        ProgressDialog.getInstance().showProgress(Purchage_Winning_Ticket.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"events/billing_address",user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            output=response+"";
                            page_type="purchase_wt";
                            if (wallete_status.equals("2")) {
                                double remaining_amount  = total_price - wallete_amount - discount_pric;

                                if(remaining_amount>0) {

                                    Intent in = new Intent(getApplicationContext(), Stripe_Main_Page.class);
                                    startActivityForResult(in,1);

                               }else {
                                    Intent in = new Intent(getApplicationContext(), Order_Preview.class);
                                    in.putExtra("respose", output);
                                    in.putExtra("page", page_type);
                                    in.putExtra("wallet", "1");
                                    in.putExtra("pat_ment_type", "Wallet");
                                    if (discount_pric>0){
                                        in.putExtra("pat_ment_type", "Wallet & VIP Payment");
                                    }
                                    in.putExtra("pageliveauction", pageliveauction);
                                    in.putExtra("discount_pric", discount_pric);
                                    in.putExtra("wallete_balance", wallete_amount);
                                    startActivity(in);
                                    finish();
                                }

                            } else if (wallete_status.equals("1")) {

                                Intent in = new Intent(getApplicationContext(), Order_Preview.class);
                                in.putExtra("respose", output);
                                in.putExtra("page", page_type);
                                in.putExtra("wallet", "1");
                                in.putExtra("pat_ment_type", "Wallet");

                                if (discount_pric>0){
                                    in.putExtra("pat_ment_type", "Wallet & VIP Payment");
                                }
                                in.putExtra("pageliveauction", pageliveauction);
                                in.putExtra("discount_pric", discount_pric);
                                in.putExtra("wallete_balance", wallete_amount);
                                startActivity(in);
                                finish();


                            } else if (wallete_status.equals("0")) {

                                double remaining_amount  = total_price - discount_pric;
                                if(remaining_amount>0) {

                                    Intent in = new Intent(getApplicationContext(), Stripe_Main_Page.class);
                                    startActivityForResult(in,1);

                                }else {
                                    Intent in = new Intent(getApplicationContext(), Order_Preview.class);
                                    in.putExtra("respose", output);
                                    in.putExtra("page", page_type);
                                    in.putExtra("wallet", "1");
                                    in.putExtra("pat_ment_type", "Wallet");
                                    if (discount_pric>0){
                                        in.putExtra("pat_ment_type", "Wallet & VIP Payment");
                                    }
                                    in.putExtra("pageliveauction", pageliveauction);
                                    in.putExtra("discount_pric", discount_pric);
                                    in.putExtra("wallete_balance", wallete_amount);
                                    startActivity(in);
                                    finish();
                                }


                            }
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
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(Purchage_Winning_Ticket.this);
                            }else {
                                Toast.makeText(Purchage_Winning_Ticket.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Purchage_Winning_Ticket.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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


    private void MethodToCalStateApi(String county) {
        ProgressDialog.getInstance().showProgress(Purchage_Winning_Ticket.this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"city_states/states?country="+county, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
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

                    ArrayAdapter<String> adapter   = new ArrayAdapter<String>(Purchage_Winning_Ticket.this,R.layout.custom_text, statevalues) {

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
                        state_postion = statevalues.indexOf(states);
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

    public void makeJsonObjectRequest() {

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"users/view_profile",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responses) {

                        System.out.println("------resp------"+responses);

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

                            states = statename;

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
                                country_potion = countrykeys.indexOf(country_key);
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
                            TextView full_address_payment = findViewById(R.id.full_address_payment);
                            full_address_payment.setText(full_addresss);

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
                                ProgressDialog.getInstance().hideProgress();
                                Alert_Dailog.showNetworkAlert(Purchage_Winning_Ticket.this);
                            }else {
                                ProgressDialog.getInstance().hideProgress();
                                Toast.makeText(Purchage_Winning_Ticket.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            ProgressDialog.getInstance().hideProgress();
                            Toast.makeText(Purchage_Winning_Ticket.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
                if(value.equals("main")) {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("main");
                    } else {
                        makeJsonObjectRequest();
                    }
                }
                else   if(value.equals("country"))
                {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("country");
                    } else {
                        MethodToCalStateApi(countrykeys.get(country_potion));
                    }

                }
                else if(value.equals("p1"))
                {

                    if (!isNetworkAvailable()) {

                        alertdailogbox("p1");
                    } else {
                        try {
                            strfirstname = profile_name.getText().toString();
                            strzipcode = profile_zipcode.getText().toString();
                            strlastname = profile_lname.getText().toString();
                            strphone = profile_pno.getText().toString();
                            stradd1 = profile_address1.getText().toString();
                            stradd2 = profile_address2.getText().toString();
                            strcity = profile_city.getText().toString();


                            JSONObject object1 = new JSONObject();
                            JSONObject object2 = new JSONObject();

                            object1.put("first_name", strfirstname);
                            object1.put("last_name", strlastname);
                            object1.put("address_line1", stradd1);
                            object1.put("address_line2", stradd2);
                            object1.put("city", strcity);
                            object1.put("country", country_values);
                            object1.put("zip_code", strzipcode);
                            object1.put("state", state_values);
                            object1.put("phone", strphone);
                            object1.put("order_id", order_item_id);
                            object2.put("billing_address", object1);

                            if(wallete_status.equals("1") || wallete_status.equals("2"))
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
                            object1.put("address_line1", stradd1);
                            object1.put("address_line2", stradd2);
                            object1.put("city", strcity);
                            object1.put("country", country_values);
                            object1.put("zip_code", strzipcode);
                            object1.put("state", state_values);
                            object1.put("phone", strphone);
                            object1.put("order_id", order_item_id);
                            object2.put("billing_address", object1);
                            if(wallete_status.equals("1") || wallete_status.equals("2"))
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

    @Override
    public void onClick(final View view) {

        switch (view.getId()) {

            case R.id.btn_back:
                finish();
                break;

            case R.id.remove_icon:

                try {
                    ProgressDialog.getInstance().showProgress(Purchage_Winning_Ticket.this);

                    JSONObject promocode = new JSONObject();
                    promocode.put("order_id", order_id);
                    JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url + "events/remove_promo_code", promocode,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        if (response.getString("status").equals("Success")) {
                                            discount_pric = 0;
                                            wallete_status = in.getStringExtra("wallet_status");
                                            succes_layout.setVisibility(View.GONE);
                                            if (wallete_amount > 0) {
                                                toggleButton.setChecked(true);
                                                pay_not.setVisibility(View.VISIBLE);
                                            } else {
                                                pay_not.setVisibility(View.GONE);
                                                toggleButton.setChecked(false);
                                            }
                                            view_hide.setVisibility(View.GONE);
                                            applay_credit_balance.setVisibility(View.VISIBLE);
                                            text_wallete_balance.setVisibility(View.VISIBLE);
                                        } else {
                                            Toast.makeText(Purchage_Winning_Ticket.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                        }
                                        ProgressDialog.getInstance().hideProgress();
                                    } catch (Exception e) {

                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    NetworkResponse networkResponse = error.networkResponse;
                                    if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                                        // HTTP Status Code: 401 Unauthorized
                                        if (error.networkResponse.statusCode == 401) {
                                            ProgressDialog.getInstance().hideProgress();
                                            Alert_Dailog.showNetworkAlert(Purchage_Winning_Ticket.this);
                                        } else {
                                            ProgressDialog.getInstance().hideProgress();
                                            Toast.makeText(Purchage_Winning_Ticket.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        ProgressDialog.getInstance().hideProgress();
                                        Toast.makeText(Purchage_Winning_Ticket.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();


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
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    myRequest.setRetryPolicy(policy);
                    AppController.getInstance().addToRequestQueue(myRequest, "tag");


                } catch (Exception E) {
                    //nothing
                }

                break;

        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
