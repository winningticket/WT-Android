package com.winningticketproject.in.MyAccountTab;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class WIthdrawal extends AppCompatActivity {
    TextView cancel_purchage,account_title,status,bank_satus,amounttxt,amounttxts,status_pay,current_balence,chose_amount;
    Button banck_transfer,submit;
    EditText et_email_address,amount,amount_bt,et_name,et_bank_routing_number,et_bank_number;
    Typeface tf,book,semi_bold;
    String auth_token="";
    LinearLayout paypal_layout,bank_layout,bank_transper_lay,paypal_lay;
    int value=0;
    ProgressDialog pd;
    TextInputLayout text_email,text_name,text_bank_routing_number,text_bank_number;
    ImageView paypal;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        paypal_layout= findViewById(R.id.paypal_layout);
        bank_layout= findViewById(R.id.bank_layout);
        pd = new ProgressDialog(WIthdrawal.this);

        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        tf = Typeface.createFromAsset(getApplicationContext().getAssets(),"Montserrat-Medium.ttf");
        book = Typeface.createFromAsset(getAssets(),"Montserrat-Regular.ttf");
        semi_bold = Typeface.createFromAsset(getAssets(),"Montserrat-SemiBold.ttf");

        et_email_address= findViewById(R.id.et_email_address);
        amount= findViewById(R.id.amount);

        amounttxts = findViewById(R.id.amounttxts);

        amount_bt= findViewById(R.id.amount_bt);
        et_name= findViewById(R.id.et_name);
        et_bank_routing_number= findViewById(R.id.et_bank_routing_number);
        et_bank_number= findViewById(R.id.et_bank_number);

        auth_token = get_auth_token("auth_token");


        paypal_lay = findViewById(R.id.paypal_lay);
        bank_transper_lay = findViewById(R.id.bank_transper_lay);

        current_balence = findViewById(R.id.current_balence);
        current_balence.setTypeface(semi_bold);

        chose_amount = findViewById(R.id.chose_amount);
        chose_amount.setTypeface(book);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            String j =(String) b.get("balance");
            current_balence.setText(j);
        }

        banck_transfer = findViewById(R.id.banck_transfer);
        paypal = findViewById(R.id.paypal);
        submit = findViewById(R.id.submit);
        submit.setTypeface(tf);

        paypal_lay.setBackground(null);
        bank_transper_lay.setBackground(null);

        banck_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paypal_lay.setBackground(null);
                bank_transper_lay.setBackgroundResource(R.drawable.with_drwa_color);

                paypal_layout.setVisibility(View.GONE);
                bank_layout.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                value=1;
                et_email_address.setText("");
                amount.setText("");
            }
        });
        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank_transper_lay.setBackground(null);
                paypal_lay.setBackgroundResource(R.drawable.with_drwa_color);

                paypal_layout.setVisibility(View.VISIBLE);
                bank_layout.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                value=2;
                amount_bt.setText("");
                et_name.setText("");
                et_bank_routing_number.setText("");
                et_bank_number.setText("");

            }
        });

        amounttxts.setTypeface(book);
        amount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
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

                    amount.setText(cashAmountBuilder.toString());
                    // keeps the cursor always to the right
                    Selection.setSelection(amount.getText(), cashAmountBuilder.toString().length());

                }

            }
        });

        amount_bt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
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

                    amount_bt.setText(cashAmountBuilder.toString());
                    // keeps the cursor always to the right
                    Selection.setSelection(amount_bt.getText(), cashAmountBuilder.toString().length());

                }

            }
        });


        status = findViewById(R.id.status);
        bank_satus = findViewById(R.id.bank_satus);
        amounttxt = findViewById(R.id.amounttxt);
        status_pay = findViewById(R.id.status_pay);


        text_email = findViewById(R.id.text_email);
        text_name = findViewById(R.id.text_name);
        text_bank_routing_number = findViewById(R.id.text_bank_routing_number);
        text_bank_number = findViewById(R.id.text_bank_number);

        text_email.setTypeface(book);
        text_name.setTypeface(book);
        text_bank_routing_number.setTypeface(book);
        text_bank_number.setTypeface(book);


        bank_satus.setTypeface(tf);
        amounttxt.setTypeface(book);
        status_pay.setTypeface(tf);

        et_email_address.setTypeface(book);
        amount.setTypeface(book);
        amount_bt.setTypeface(book);
        et_name.setTypeface(book);
        et_bank_routing_number.setTypeface(book);
        et_bank_number.setTypeface(book);






        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hiddenInputMethod();

                if(value==1)
            {
                if(amount_bt.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Amount is mandatory",Toast.LENGTH_LONG).show();
                    amount_bt.requestFocus();
                }else if (amount_bt.getText().toString().equals("0.00") || amount_bt.getText().toString().equals("00.00") || amount_bt.getText().toString().equals("000.00") || amount_bt.getText().toString().equals("0000.00") || amount_bt.getText().toString().equals("00000.00") || amount_bt.getText().toString().equals("000000.00")) {
                    Toast.makeText(getApplicationContext(),"Please enter greater than Zero",Toast.LENGTH_LONG).show();
                    amount_bt.requestFocus();
                }
                else  if(et_name.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Account holder name is mandatory",Toast.LENGTH_LONG).show();
                    et_name.requestFocus();
                }
                else  if(et_name.getText().toString().length()<3)
                {
                    Toast.makeText(getApplicationContext(),"Account holder name should have minimum 3 charecter",Toast.LENGTH_LONG).show();
                    et_name.requestFocus();

                }
                else  if(et_bank_routing_number.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Bank routing number is mandatory",Toast.LENGTH_LONG).show();
                    et_bank_routing_number.requestFocus();

                }
                else  if(et_bank_routing_number.getText().toString().length()<5)
                {
                    Toast.makeText(getApplicationContext(),"Bank routing number should have minimum 5 charecter",Toast.LENGTH_LONG).show();
                    et_bank_routing_number.requestFocus();

                }
                else  if(et_bank_number.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Bank account number is mandatory",Toast.LENGTH_LONG).show();
                    et_bank_number.requestFocus();

                }
                else  if(et_bank_number.getText().toString().length()<5)
                {
                    Toast.makeText(getApplicationContext(),"Bank account number should have minimum 5 charecter",Toast.LENGTH_LONG).show();
                    et_bank_number.requestFocus();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Coming soon",Toast.LENGTH_LONG).show();

                    finish();


                }

            }
            else if(value==2)
            {
                if(et_email_address.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter email",Toast.LENGTH_LONG).show();
                    et_email_address.requestFocus();


                }else if(!isValidEmail(et_email_address.getText().toString()))
               {
                   Toast.makeText(getApplicationContext(),"Please enter valid email",Toast.LENGTH_LONG).show();
                   et_email_address.requestFocus();

               }
               else if(amount.getText().toString().equals(""))
               {
                   Toast.makeText(getApplicationContext(),"Please enter amount",Toast.LENGTH_LONG).show();
                   amount.requestFocus();

               }else if (amount.getText().toString().equals("0.00") || amount.getText().toString().equals("00.00") || amount.getText().toString().equals("000.00") || amount.getText().toString().equals("0000.00") || amount.getText().toString().equals("00000.00") || amount.getText().toString().equals("000000.00")) {
                    Toast.makeText(getApplicationContext(),"Please enter greater than Zero",Toast.LENGTH_LONG).show();
                    amount.requestFocus();

                }
               else
               {

                   if (!isNetworkAvailable()) {

                       alertdailogbox();
                   }
                   else
                   {
                       try {
                           JSONObject object1 = new JSONObject();
                           object1.put("email", et_email_address.getText().toString());
                           object1.put("amount", amount.getText().toString());
                           methodforuploadprofiledata(object1);
                       }
                       catch (Exception e)
                       {
                           //nothing
                       }
                   }
               }
            }

            }
        });

        account_title = findViewById(R.id.account_title);
        cancel_purchage = findViewById(R.id.cancel_purchage);
        account_title.setText("Withdraw");


        cancel_purchage.setTypeface(tf);
        account_title.setTypeface(tf);
//        paypal.setTypeface(tf);
        banck_transfer.setTypeface(tf);

        status.setTypeface(book);

        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
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
        amount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                amount.setFocusable(true);
                amount.setFocusableInTouchMode(true);
                return false;
            }
        });

        amount_bt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                amount_bt.setFocusable(true);
                amount_bt.setFocusableInTouchMode(true);
                return false;
            }
        });

        et_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                et_name.setFocusable(true);
                et_name.setFocusableInTouchMode(true);
                return false;
            }
        });

        et_bank_routing_number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                et_bank_routing_number.setFocusable(true);
                et_bank_routing_number.setFocusableInTouchMode(true);
                return false;
            }
        });


        et_bank_number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                et_bank_number.setFocusable(true);
                et_bank_number.setFocusableInTouchMode(true);
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
            }
        }
        return ret;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public void methodforuploadprofiledata(JSONObject user){
        pd.show();
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"withdrawal/paypal_funds",user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pd.dismiss();
                            if(response.getString("status").equals("Success")){

                                Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_LONG).show();
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(),response.getString("errors"),Toast.LENGTH_LONG).show();
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
                                Alert_Dailog.showNetworkAlert(WIthdrawal.this);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                                Toast.makeText(WIthdrawal.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                            Toast.makeText(WIthdrawal.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
                    if (!isNetworkAvailable()) {

                        alertdailogbox();
                    }
                    else
                    {
                        try {
                            JSONObject object1 = new JSONObject();
                            object1.put("email", et_email_address.getText().toString());
                            object1.put("amount", amount.getText().toString());
                            methodforuploadprofiledata(object1);
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

}
