package com.winningticketproject.in.MyAccountTab;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
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
import com.winningticketproject.in.Fragments.My_account;
import com.winningticketproject.in.R;
import com.winningticketproject.in.Stripe_GetWay.Stripe_Main_Page;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;


public class Add_Funds extends AppCompatActivity implements View.OnClickListener {
    TextView cancel_purchage, account_title, chose_amount, customer_amount, dowler_symbol,no_amoount,current_balence,no_title_test;
    Button deposite_fund;
    EditText etittext_amount;
    View add_funds;
    String amount_value="";
    String auth_token = "";
    ArrayList<Integer> denominationsList = new ArrayList<Integer>();
    Button button_1,button_2,button_3,button_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add__funds);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        no_amoount = findViewById(R.id.no_amoount);
        no_amoount.setVisibility(View.GONE);

        button_1 = findViewById(R.id.button_1);
        button_2 = findViewById(R.id.button_2);
        button_3 = findViewById(R.id.button_3);
        button_4= findViewById(R.id.button_4);


        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        button_3.setOnClickListener(this);
        button_4.setOnClickListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        add_funds = findViewById(R.id.add_funds);

        button_1.setTypeface(medium);
        button_2.setTypeface(medium);
        button_3.setTypeface(medium);
        button_4.setTypeface(medium);
        no_amoount.setTypeface(medium);

        button_1.setBackgroundResource(R.drawable.dark_button_background);
        button_1.setTextColor(getResources().getColor(R.color.colorwhite));

        cancel_purchage = add_funds.findViewById(R.id.cancel_purchage);


        chose_amount = findViewById(R.id.chose_amount);
        customer_amount = findViewById(R.id.customer_amount);
        dowler_symbol = findViewById(R.id.dowler_symbol);

        etittext_amount = findViewById(R.id.etittext_amount);
        cancel_purchage.setOnClickListener(this);

        auth_token = get_auth_token("auth_token");

        account_title = findViewById(R.id.account_title);
        account_title.setText("Add Funds");

        current_balence = findViewById(R.id.current_balence);
        current_balence.setTypeface(semibold);

        current_balence.setText(String.format("$ %.2f",My_account.wallete_balance));
        if (!isNetworkAvailable()) {
            alertdailogbox("denominations");
        }
        else
        {
            jsonapiDenominations();
        }

        no_title_test = findViewById(R.id.no_title_test);
        no_title_test.setTypeface(regular);
        cancel_purchage.setTypeface(medium);
        account_title.setTypeface(medium);
        chose_amount.setTypeface(medium);
        customer_amount.setTypeface(regular);
        dowler_symbol.setTypeface(medium);
        etittext_amount.setTypeface(medium);

        deposite_fund = findViewById(R.id.deposite_fund);

        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

        etittext_amount.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(9) });

        etittext_amount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {



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

                    etittext_amount.setText(cashAmountBuilder.toString());
                    // keeps the cursor always to the right
                    Selection.setSelection(etittext_amount.getText(), cashAmountBuilder.toString().length());

                }

            }
        });

            etittext_amount.setFocusableInTouchMode(false);
            etittext_amount.setFocusable(false);

            etittext_amount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                etittext_amount.setFocusableInTouchMode(true);
                etittext_amount.setFocusable(true);

                amount_value ="";

                button_1.setBackgroundResource(R.drawable.editext_baground);
                button_1.setTextColor(getResources().getColor(R.color.colorlightkgray));

                button_2.setBackgroundResource(R.drawable.editext_baground);
                button_2.setTextColor(getResources().getColor(R.color.colorlightkgray));

                button_3.setBackgroundResource(R.drawable.editext_baground);
                button_3.setTextColor(getResources().getColor(R.color.colorlightkgray));

                button_4.setBackgroundResource(R.drawable.editext_baground);
                button_4.setTextColor(getResources().getColor(R.color.colorlightkgray));

                return false;
            }
        });

        cancel_purchage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        deposite_fund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiddenInputMethod();
                if (amount_value.equals("") && etittext_amount.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                } else if (!amount_value.equals("")) {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("dropin");
                    } else {

                        Intent in = new Intent(getApplicationContext(), Stripe_Main_Page.class);
                        startActivityForResult(in,1);

                    }
                } else if (etittext_amount.getText().toString().equals("0.00")) {
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                } else if (etittext_amount.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();

                } else if (etittext_amount.getText().toString().equals("00.00")) {
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                }else if (!etittext_amount.getText().toString().isEmpty() || !etittext_amount.getText().toString().equals("")) {
                    amount_value = "";

                        amount_value = etittext_amount.getText().toString();
                        if (!isNetworkAvailable()) {
                            alertdailogbox("dropin");
                        } else {
                            Intent in = new Intent(getApplicationContext(),Stripe_Main_Page.class);
                            startActivityForResult(in,1);
                    }
                }

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
            }
        }
        return ret;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_purchage:
                finish();
                break;

            case R.id.button_1:
                hiddenInputMethod();

                etittext_amount.setFocusableInTouchMode(false);
                etittext_amount.setFocusable(false);
                etittext_amount.setText("");
                amount_value = button_1.getText().toString().replace("$","");

                button_1.setBackgroundResource(R.drawable.dark_button_background);
                button_1.setTextColor(getResources().getColor(R.color.colorwhite));

                button_2.setBackgroundResource(R.drawable.editext_baground);
                button_2.setTextColor(getResources().getColor(R.color.colorlightkgray));

                button_3.setBackgroundResource(R.drawable.editext_baground);
                button_3.setTextColor(getResources().getColor(R.color.colorlightkgray));

                button_4.setBackgroundResource(R.drawable.editext_baground);
                button_4.setTextColor(getResources().getColor(R.color.colorlightkgray));

                break;

            case R.id.button_2:

                hiddenInputMethod();

                etittext_amount.setFocusableInTouchMode(false);
                etittext_amount.setFocusable(false);

                etittext_amount.setText("");
                amount_value = button_2.getText().toString().replace("$","");

                button_2.setBackgroundResource(R.drawable.dark_button_background);
                button_2.setTextColor(getResources().getColor(R.color.colorwhite));


                button_1.setBackgroundResource(R.drawable.editext_baground);
                button_1.setTextColor(getResources().getColor(R.color.colorlightkgray));

                button_3.setBackgroundResource(R.drawable.editext_baground);
                button_3.setTextColor(getResources().getColor(R.color.colorlightkgray));

                button_4.setBackgroundResource(R.drawable.editext_baground);
                button_4.setTextColor(getResources().getColor(R.color.colorlightkgray));


                break;

            case R.id.button_3:
                hiddenInputMethod();

                amount_value = button_3.getText().toString().replace("$","");
                etittext_amount.setFocusableInTouchMode(false);
                etittext_amount.setFocusable(false);
                etittext_amount.setText("");

                button_1.setBackgroundResource(R.drawable.editext_baground);
                button_1.setTextColor(getResources().getColor(R.color.colorlightkgray));

                button_2.setBackgroundResource(R.drawable.editext_baground);
                button_2.setTextColor(getResources().getColor(R.color.colorlightkgray));

                button_4.setBackgroundResource(R.drawable.editext_baground);
                button_4.setTextColor(getResources().getColor(R.color.colorlightkgray));


                button_3.setBackgroundResource(R.drawable.dark_button_background);
                button_3.setTextColor(getResources().getColor(R.color.colorwhite));

                break;

            case R.id.button_4:
                hiddenInputMethod();
                amount_value = button_4.getText().toString().replace("$","");
                etittext_amount.setFocusableInTouchMode(false);
                etittext_amount.setFocusable(false);
                etittext_amount.setText("");
                button_4.setBackgroundResource(R.drawable.dark_button_background);
                button_4.setTextColor(getResources().getColor(R.color.colorwhite));


                button_1.setBackgroundResource(R.drawable.editext_baground);
                button_1.setTextColor(getResources().getColor(R.color.colorlightkgray));

                button_3.setBackgroundResource(R.drawable.editext_baground);
                button_3.setTextColor(getResources().getColor(R.color.colorlightkgray));

                button_2.setBackgroundResource(R.drawable.editext_baground);
                button_2.setTextColor(getResources().getColor(R.color.colorlightkgray));


                break;

        }
    }


    public void jsonapiDenominations() {
        ProgressDialog.getInstance().showProgress(Add_Funds.this);
        String tag_json_obj = "";
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Splash_screen.url + "contributors/denominations", null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {

                try {
                    JSONArray denominations = response.getJSONArray("denominations");
                    for (int i = 0; i < denominations.length(); i++) {
                        denominationsList.add(Integer.parseInt(denominations.getString(i)));
                    }
                    Collections.sort(denominationsList);
                    if (denominationsList.size()>0){

                        no_amoount.setVisibility(View.GONE);
//                        horizontalScrollView1.setVisibility(View.VISIBLE);

                    if (denominationsList.size()==4){
                        amount_value = denominationsList.get(0)+"";
                        button_1.setText("$"+denominationsList.get(0)+"");
                        button_2.setText("$"+denominationsList.get(1)+"");
                        button_3.setText("$"+denominationsList.get(2)+"");
                        button_4.setText("$"+denominationsList.get(3)+"");
                    }else  if (denominationsList.size()==3){
                        button_4.setVisibility(View.GONE);
                        amount_value = denominationsList.get(0)+"";
                        button_1.setText("$"+denominationsList.get(0)+"");
                        button_2.setText("$"+denominationsList.get(1)+"");
                        button_3.setText("$"+denominationsList.get(2)+"");
                    }else  if (denominationsList.size()==2){


                        button_4.setVisibility(View.GONE);
                        button_3.setVisibility(View.GONE);

                        amount_value = denominationsList.get(0)+"";
                        button_1.setText("$"+denominationsList.get(0)+"");
                        button_2.setText("$"+denominationsList.get(1)+"");
                    }else  if (denominationsList.size()==1){

                        button_4.setVisibility(View.GONE);
                        button_3.setVisibility(View.GONE);
                        button_2.setVisibility(View.GONE);

                        amount_value = denominationsList.get(0)+"";
                        button_1.setText("$"+denominationsList.get(0)+"");
                    }
                    }else {

                        no_amoount.setVisibility(View.VISIBLE);
//                        horizontalScrollView1.setVisibility(View.GONE);
                    }

                    ProgressDialog.getInstance().hideProgress();

                } catch (Exception e) {
                    //nothing
                    ProgressDialog.getInstance().hideProgress();
                }


            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                ProgressDialog.getInstance().hideProgress();
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    if (error.networkResponse.statusCode==401){
                        Alert_Dailog.showNetworkAlert(Add_Funds.this);
                    }else {
                        Toast.makeText(Add_Funds.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Add_Funds.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
        jsonObjReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            final String result=data.getStringExtra("Payment_token");
            final String source_id =data.getStringExtra("source_id");

            System.out.println(source_id+"--------token--------- "+result);
            if (!isNetworkAvailable()) {
                   Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        if (source_id.length()>0){
                            jsonObject.put("payment-source", source_id);
                        }else {
                            jsonObject.put("nonce", result);
                        }
                        jsonObject.put("transaction_type", "add_funds");
                        jsonObject.put("price", amount_value);

                        ProgressDialog.getInstance().showProgress(Add_Funds.this);
                    }catch (Exception e){
                        //nothing
                    }

                    System.out.println("-----jsonObject------"+jsonObject);

                    JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"payments/create",jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    ProgressDialog.getInstance().hideProgress();
                                    System.out.println("-------response-----"+response);
                                    try {
                                        ProgressDialog.getInstance().hideProgress();
                                        if (response.getString("status").equals("Success")){
                                            Toast.makeText(getApplicationContext(), "" + response.getString("message"), Toast.LENGTH_LONG).show();
                                            finish();

                                        }else {
                                            Toast.makeText(getApplicationContext(),""+response.getString("message"),Toast.LENGTH_LONG).show();
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
                                            Alert_Dailog.showNetworkAlert(Add_Funds.this);
                                        }else {
                                            Toast.makeText(Add_Funds.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(Add_Funds.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

                if(value.equals("denominations")) {
                    if (!isNetworkAvailable()) {

                        alertdailogbox("denominations");
                    } else {
                        jsonapiDenominations();
                    }
                }
                else if(value.equals("dropin"))
                {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("dropin");
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