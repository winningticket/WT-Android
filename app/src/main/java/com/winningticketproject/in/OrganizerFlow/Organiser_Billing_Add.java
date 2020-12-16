package com.winningticketproject.in.OrganizerFlow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.BuildConfig;
import com.winningticketproject.in.EventTab.Order_Preview;
import com.winningticketproject.in.EventTab.Purchage_Winning_Ticket;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.Stripe_GetWay.Stripe_Main_Page;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.SignInSingup.Splash_screen.countrykeys;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class Organiser_Billing_Add extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout text_fname, text_lname, text_email, text_address1, text_address2, text_city, text_zipcode, text_pno;
    EditText profile_name;
    EditText profile_lname;
    EditText profile_email;
    EditText profile_address1;
    EditText profile_address2;
    EditText profile_city;
    EditText profile_zipcode;
    EditText profile_pno;
    Spinner country;
    Spinner statespinner;
    TextView citytxt,countrytxt;
    LinearLayout main_layout;
    int country_potion = 0, state_postion = 0;
    String country_values = "",state_values="";
    String states = "";
    ArrayList<String> statevalues = new ArrayList<String>();
    ArrayList<String> statekeys = new ArrayList<String>();
    private static final int REQUEST_SCAN = 100;
    Button btn_submit_form;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser__billing__add);

        billing_address_layout();

    }

    private void billing_address_layout() {

        main_layout = findViewById(R.id.main_layout);

        btn_submit_form = findViewById(R.id.btn_submit_form);
        btn_submit_form.setTypeface(medium);
        btn_submit_form.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.orgniser_tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Billing Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

            profile_name.setText(getIntent().getStringExtra("f_name"));
            profile_lname.setText(getIntent().getStringExtra("l_name"));
            profile_email.setText(getIntent().getStringExtra("user_email"));

            profile_name.setTypeface(regular);
            profile_lname.setTypeface(regular);
            profile_email.setTypeface(regular);
            profile_address1.setTypeface(regular);
            profile_address2.setTypeface(regular);
            profile_city.setTypeface(regular);
            profile_zipcode.setTypeface(regular);
            profile_pno.setTypeface(regular);
            text_city.setTypeface(regular);

        country = findViewById(R.id.country);
        statespinner = findViewById(R.id.state);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Organiser_Billing_Add.this, R.layout.custom_text, Splash_screen.countryvalues) {
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
                    Toast.makeText(Organiser_Billing_Add.this,"Internet Not available",Toast.LENGTH_LONG).show();
                } else {
                    MethodToCalStateApi(countrykeys.get(position));
                }
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
                state_values = statekeys.get(position);
                state_postion = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void MethodToCalStateApi(String county) {
        ProgressDialog.getInstance().showProgress(Organiser_Billing_Add.this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"city_states/states?country="+county, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                statevalues.clear();
                statekeys.clear();

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(Organiser_Billing_Add.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                statevalues.add("Select State");
                statekeys.add("");
                try {
                    Iterator<String> keys = response.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        statevalues.add(key);
                        statekeys.add(response.getString(key));

                    }

                    ArrayAdapter<String> adapter   = new ArrayAdapter<String>(Organiser_Billing_Add.this,R.layout.custom_text, statevalues) {

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


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_submit_form:
                method_to_upload();
                break;
        }
    }

    private void method_to_upload() {
        try {
            if (profile_name.getText().toString().isEmpty()) {
                error("First name is empty, please update your first name");
                profile_name.requestFocus();
            }else if (profile_lname.getText().toString().isEmpty()) {
                error("Last name is empty, please update your last name");
                profile_name.requestFocus();
            }else if (profile_address1.getText().toString().isEmpty()) {
                error("Address is empty, please update address");
                profile_address1.requestFocus();
            } else if (profile_city.getText().toString().isEmpty()) {
                error("City is empty, please update city");
                profile_city.requestFocus();
            } else if (country_values.equals("")){
                error("Please select country");
            } else if (state_values.equals("")){
                error("Please select state");
            }else if (profile_zipcode.getText().toString().isEmpty()) {
                error("Zip code is empty, please update zip code");
                profile_zipcode.requestFocus();
            } else if (profile_pno.getText().toString().isEmpty()) {
                error("Phone number is empty, please update phone number");
                profile_pno.requestFocus();
            } else {
                if (!isNetworkAvailable()) {
                    error("Internet not available");
                } else {

                    Intent intent = new Intent(Organiser_Billing_Add.this, CardIOActivity.class)
                            .putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, Color.GREEN)
                            .putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true)
                            .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
                            .putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true)
                            .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true)
                            .putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false)
                            .putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, false);
                            startActivityForResult(intent, REQUEST_SCAN);
                }
            }
        } catch(Exception e){
        }
    }

    public void method_cal_premium(JSONObject user,String token){
        System.out.println("------user-----"+user);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.imageurl+"api/v3/auction/billing_address/"+getIntent().getStringExtra("order_id")+"/create",user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("--------response--------"+response);
                                method_to_call_payment_flow(token);
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
                        ProgressDialog.getInstance().hideProgress();

                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(Organiser_Billing_Add.this);
                            }else {
                                Toast.makeText(Organiser_Billing_Add.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Organiser_Billing_Add.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("auth-token", get_auth_token("auth_token"));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void method_to_call_payment_flow(String token) {
        try {
            JSONObject object1 = new JSONObject();
            object1.put("nonce",token);
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.imageurl+"api/v3/payments/scan/"+getIntent().getStringExtra("order_id")+"/create",object1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println("-------------res----"+response);

                            ProgressDialog.getInstance().hideProgress();
                            Intent in = new Intent(Organiser_Billing_Add.this,OrgPurchaseConf.class);
                            in.putExtra("response", String.valueOf(response));
                            startActivity(in);
                            finish();
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
                        ProgressDialog.getInstance().hideProgress();
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(Organiser_Billing_Add.this);
                            }else {
                                Toast.makeText(Organiser_Billing_Add.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Organiser_Billing_Add.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("auth-token", get_auth_token("auth_token"));
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

    @SuppressLint("WrongConstant")
    private void error(String s) {
        Snackbar snackbar = Snackbar.make(main_layout, s, Snackbar.LENGTH_LONG);
        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
        View  view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtv.setTextSize(TypedValue
                .COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorsnakbar));
        snackbar.show();
    }

    private void getStripeToken(String card_number,int exp_month,int year,String cvv) {
        ProgressDialog.getInstance().showProgress(this);
        Card card = Card.create(card_number,exp_month,year,cvv);
        com.stripe.android.Stripe Stripe2 = null;
        Stripe2  = new Stripe(this, BuildConfig.STRIPE_PUBLIC_KEY);
        Stripe2.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        try {
                            JSONObject object1 = new JSONObject();
                            object1.put("first_name", profile_name.getText().toString());
                            object1.put("last_name", profile_lname.getText().toString());
                            object1.put("address_line1", profile_address1.getText().toString());
                            object1.put("address_line2", profile_address2.getText().toString());
                            object1.put("city", profile_city.getText().toString());
                            object1.put("country", country_values);
                            object1.put("zip_code", profile_zipcode.getText().toString());
                            object1.put("state", state_values);
                            object1.put("phone", profile_pno.getText().toString());
                            method_cal_premium(object1,token.getId());
                        }catch (Exception e){
                        }
                    }
                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(Organiser_Billing_Add.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_SCAN ) && data != null
                && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard result = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
            if (result != null) {
                System.out.println("---------------"+result.getFormattedCardNumber()+"----"+result.getRedactedCardNumber());
                getStripeToken(result.getFormattedCardNumber(),result.expiryMonth,result.expiryYear,result.cvv);
            }
        }
    }

}
