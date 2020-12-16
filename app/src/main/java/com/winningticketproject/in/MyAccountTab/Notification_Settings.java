package com.winningticketproject.in.MyAccountTab;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
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
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.protocol.HTTP;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class Notification_Settings extends AppCompatActivity implements View.OnClickListener {
    TextView cancel_purchage,account_title,notificatio_title,notificatio_title1,notificatio_event,notificatio_title2,notificatio_title3,notificatio_action,notificatio_title4,notificatio_title5,tc_text_mobile,tc_text_title,tc_email_title;
    Typeface tf,book;
    ToggleButton togal_button_1,togal_button_2,togal_button_3,togal_button_4,togal_button_5,togal_button_6,togal_button_7,togal_button_8,togal_button_9,togal_button_10,push_togal_button_1,push_togal_button_2,push_togal_button_3,push_togal_button_4,push_togal_button_5;
    boolean button_1=true,button_2=true,button_3=true,button_4=true,button_5=true,button_6=true,button_7=true,button_8=true,button_9=true,button_10=true,push_button1=true,push_button2=true,push_button3=true,push_button4=true,push_button5=true;
    String auth_token="";
    ToggleButton toggle_sms,toggle_email;
    boolean email,sms;
    TextView text_push_sms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification__settings);


        book = Typeface.createFromAsset(getAssets(),"Montserrat-Regular.ttf");
        tf = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");

        cancel_purchage = findViewById(R.id.cancel_purchage);
        account_title = findViewById(R.id.account_title);
        account_title.setText("Notification Settings");
        cancel_purchage.setOnClickListener(this);

        auth_token = get_auth_token("auth_token");


        old_method_code();


        new_design_flow();

        if (!isNetworkAvailable()) {
            Toast.makeText(this,"Internet not available",Toast.LENGTH_LONG).show();
        } else {
            methodtocalnotificationsetting();
        }

        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

    }

    private void new_design_flow() {


        TextView text_title =  findViewById(R.id.text_title);
        text_title.setTypeface(regular);


        text_push_sms = findViewById(R.id.text_push_sms);
        text_push_sms.setTypeface(medium);


        TextView sms_notificatio_title1 = findViewById(R.id.sms_notificatio_title1);
        sms_notificatio_title1.setTypeface(medium);


        toggle_sms = findViewById(R.id.toggle_sms);
        toggle_sms.setOnClickListener(this);

        toggle_email = findViewById(R.id.toggle_email);
        toggle_email.setOnClickListener(this);

    }

    private void old_method_code() {


        tc_text_mobile = findViewById(R.id.tc_text_mobile);
        tc_text_title = findViewById(R.id.tc_text_title);
        tc_email_title = findViewById(R.id.tc_email_title);

        notificatio_title = findViewById(R.id.notificatio_title);
        notificatio_title1 = findViewById(R.id.notificatio_title1);
        notificatio_event = findViewById(R.id.notificatio_event);
        notificatio_action = findViewById(R.id.notificatio_action);

        tc_text_title = findViewById(R.id.tc_text_title);
        tc_email_title = findViewById(R.id.tc_email_title);
        notificatio_title2 = findViewById(R.id.notificatio_title2);
        notificatio_title3 = findViewById(R.id.notificatio_title3);
        notificatio_title4 = findViewById(R.id.notificatio_title4);
        notificatio_title5 = findViewById(R.id.notificatio_title5);
        cancel_purchage.setOnClickListener(this);

        togal_button_1 = findViewById(R.id.togal_button_1);
        togal_button_2 = findViewById(R.id.togal_button_2);
        togal_button_3 = findViewById(R.id.togal_button_3);
        togal_button_4 = findViewById(R.id.togal_button_4);
        togal_button_5 = findViewById(R.id.togal_button_5);
        togal_button_6 = findViewById(R.id.togal_button_6);
        togal_button_7 = findViewById(R.id.togal_button_7);
        togal_button_8 = findViewById(R.id.togal_button_8);
        togal_button_9 = findViewById(R.id.togal_button_9);
        togal_button_10 = findViewById(R.id.togal_button_10);

        push_togal_button_1 = findViewById(R.id.push_togal_button_1);
        push_togal_button_2 = findViewById(R.id.push_togal_button_2);
        push_togal_button_3 = findViewById(R.id.push_togal_button_3);
        push_togal_button_4 = findViewById(R.id.push_togal_button_4);
        push_togal_button_5 = findViewById(R.id.push_togal_button_5);

        togal_button_1.setOnClickListener(this);
        togal_button_2.setOnClickListener(this);
        togal_button_3.setOnClickListener(this);
        togal_button_4.setOnClickListener(this);
        togal_button_5.setOnClickListener(this);
        togal_button_6.setOnClickListener(this);
        togal_button_7.setOnClickListener(this);
        togal_button_8.setOnClickListener(this);
        togal_button_9.setOnClickListener(this);
        togal_button_10.setOnClickListener(this);

        push_togal_button_1.setOnClickListener(this);
        push_togal_button_2.setOnClickListener(this);
        push_togal_button_3.setOnClickListener(this);
        push_togal_button_4.setOnClickListener(this);
        push_togal_button_5.setOnClickListener(this);


        cancel_purchage.setTypeface(tf);
        account_title.setTypeface(tf);
        notificatio_title.setTypeface(tf);
        tc_text_title.setTypeface(tf);
        tc_email_title.setTypeface(tf);
        tc_text_mobile.setTypeface(tf);
        notificatio_event.setTypeface(tf);
        notificatio_action.setTypeface(tf);

        notificatio_title1.setTypeface(book);
        notificatio_title2.setTypeface(book);
        notificatio_title3.setTypeface(book);
        notificatio_title4.setTypeface(book);
        notificatio_title5.setTypeface(book);

    }

//    public void alertdailogbox(final String value){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
//        builder.setTitle("No Internet");
//        builder.setMessage("Internet is required. Please Retry.");
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                finish();
//            }
//        });
//
//        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                dialog.dismiss();
//                if(value.equals("livescore")) {
//                    if (!isNetworkAvailable()) {
//
//                        alertdailogbox("livescore");
//                    } else {
//                        methodtocalnotificationsetting();
//                    }
//                }else if(value.equals("updatenotification")) {
//                    if (!isNetworkAvailable()) {
//
//                        alertdailogbox("updatenotification");
//                    } else {
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }
//                }
//
//            }
//        });
//
//        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
//        dialog.show();
//    }

    private void methodtocalnotificationsetting() {
        ProgressDialog.getInstance().showProgress(this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"notification_settings/all_settings",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println("----------res--"+response);
                            sms = response.getBoolean("text_message");
                            email = response.getBoolean("email");
                            if (sms){
                                toggle_sms.setChecked(true);
                            }else {
                                toggle_sms.setChecked(false);
                            }

                            if (email){
                                toggle_email.setChecked(true);
                            }else {
                                toggle_email.setChecked(false);
                            }

                            String user_number = response.getString("user_number");
                            if (user_number.equals(null) || user_number.equals("null")){
                                user_number = "";
                            }
                            text_push_sms.setText(Html.fromHtml("SMS<br><small>"+user_number+"</small>"));

                            //news_and_offers
                          JSONObject news_and_offers = response.getJSONObject("news_and_offers");
                            push_button1 = news_and_offers.getBoolean("push");
                            button_1 = news_and_offers.getBoolean("text_message");
                            button_2 = news_and_offers.getBoolean("email");

                            if (push_button1){
                                push_togal_button_1.setChecked(true);
                            }else {
                                push_togal_button_1.setChecked(false);
                            }

                            if (button_1){
                                togal_button_1.setChecked(true);
                            }else {
                                togal_button_1.setChecked(false);
                            }

                            if (button_2){
                                togal_button_2.setChecked(true);
                            }else {
                                togal_button_2.setChecked(false);
                            }

                            //upcoming_event
                            JSONObject upcoming_event = response.getJSONObject("upcoming_event");
                            push_button2 = upcoming_event.getBoolean("push");
                            button_3 = upcoming_event.getBoolean("text_message");
                            button_4 = upcoming_event.getBoolean("email");
                            if (push_button2){
                                push_togal_button_2.setChecked(true);
                            }else {
                                push_togal_button_2.setChecked(false);
                            }

                            if (button_3){
                                togal_button_3.setChecked(true);
                            }else {
                                togal_button_3.setChecked(false);
                            }

                            if (button_4){
                                togal_button_4.setChecked(true);
                            }else {
                                togal_button_4.setChecked(false);
                            }
                            //one_day_before
                            JSONObject one_day_before = response.getJSONObject("one_day_before");
                            push_button3 = one_day_before.getBoolean("push");
                            button_5 = one_day_before.getBoolean("text_message");
                            button_6 = one_day_before.getBoolean("email");
                            if (push_button3){
                                push_togal_button_3.setChecked(true);
                            }else {
                                push_togal_button_3.setChecked(false);
                            }

                            if (button_5){
                                togal_button_5.setChecked(true);
                            }else {
                                togal_button_5.setChecked(false);
                            }

                            if (button_6){
                                togal_button_6.setChecked(true);
                            }else {
                                togal_button_6.setChecked(false);
                            }


                            //before_an_event_begin
                            JSONObject before_an_event_begin = response.getJSONObject("before_an_event_begin");
                            push_button4 = before_an_event_begin.getBoolean("push");
                            button_7 = before_an_event_begin.getBoolean("text_message");
                            button_8 = before_an_event_begin.getBoolean("email");
                            if (push_button4){
                                push_togal_button_4.setChecked(true);
                            }else {
                                push_togal_button_4.setChecked(false);
                            }

                            if (button_7){
                                togal_button_7.setChecked(true);
                            }else {
                                togal_button_7.setChecked(false);
                            }

                            if (button_8){
                                togal_button_8.setChecked(true);
                            }else {
                                togal_button_8.setChecked(false);
                            }


                            //auction_item_sold
                            JSONObject auction_item_sold = response.getJSONObject("auction_item_sold");
                            push_button5 = auction_item_sold.getBoolean("push");
                            button_9 = auction_item_sold.getBoolean("text_message");
                            button_10 = auction_item_sold.getBoolean("email");
                            if (push_button5){
                                push_togal_button_5.setChecked(true);
                            }else {
                                push_togal_button_5.setChecked(false);
                            }

                            if (button_9){
                                togal_button_9.setChecked(true);
                            }else {
                                togal_button_9.setChecked(false);
                            }

                            if (button_10){
                                togal_button_10.setChecked(true);
                            }else {
                                togal_button_10.setChecked(false);
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
                        NetworkResponse networkResponse = error.networkResponse;
                        ProgressDialog.getInstance().hideProgress();
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(Notification_Settings.this);
                            }else {
                                Toast.makeText(Notification_Settings.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Notification_Settings.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onClick(View view) {

        JSONObject parameter_updation = new JSONObject();

        switch (view.getId()){
            case R.id.toggle_sms:
                if(toggle_sms.isChecked()){
                    try
                    {
                        parameter_updation.put("notification_type","text_message");
                        parameter_updation.put("turn_off_sms",false);
                        methodaforupdationnotificationsetting(parameter_updation);
                    }catch (Exception e){}
                }
                else{
                    try
                    {
                        parameter_updation.put("notification_type","text_message");
                        parameter_updation.put("turn_off_sms",true);
                        methodaforupdationnotificationsetting(parameter_updation);
                    }catch (Exception e){
                        //nothing
                    }
                }
                break;


            case R.id.toggle_email:
                if(toggle_email.isChecked()){
                    try
                    {
                        parameter_updation.put("notification_type","email");
                        parameter_updation.put("turn_off_email",false);
                        methodaforupdationnotificationsetting(parameter_updation);
                    }catch (Exception e){}
                }
                else{
                    try
                    {
                        parameter_updation.put("notification_type","email");
                        parameter_updation.put("turn_off_email",true);
                        methodaforupdationnotificationsetting(parameter_updation);
                    }catch (Exception e){
                        //nothing
                    }
                }
                break;

            case R.id.cancel_purchage:
                finish();
                break;

//            case R.id.push_togal_button_1:
//                if(push_togal_button_1.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","push");
//                        parameter_updation.put("news_and_offers",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else{
//                    try
//                    {
//                        parameter_updation.put("notification_type","push");
//                        parameter_updation.put("news_and_offers",false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                break;
//
//            case R.id.push_togal_button_2:
//                if(push_togal_button_2.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","push");
//                        parameter_updation.put("upcoming_event",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else{
//                    try
//                    {
//                        parameter_updation.put("notification_type","push");
//                        parameter_updation.put("upcoming_event",false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                break;
//
//            case R.id.push_togal_button_3:
//                if(push_togal_button_3.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","push");
//                        parameter_updation.put("one_day_before",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "push");
//                        parameter_updation.put("one_day_before", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//                break;
//
//            case R.id.push_togal_button_4:
//                if(push_togal_button_4.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","push");
//                        parameter_updation.put("before_an_event_begin",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "push");
//                        parameter_updation.put("before_an_event_begin", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//                break;
//
//            case R.id.push_togal_button_5:
//                if(push_togal_button_5.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","push");
//                        parameter_updation.put("auction_item_sold",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "push");
//                        parameter_updation.put("auction_item_sold", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//                break;
//
//            case R.id.togal_button_1:
//                if(togal_button_1.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","text_message");
//                        parameter_updation.put("news_and_offers",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "text_message");
//                        parameter_updation.put("news_and_offers", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//             break;
//            case R.id.togal_button_2:
//
//                if(togal_button_2.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","email");
//                        parameter_updation.put("news_and_offers",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "email");
//                        parameter_updation.put("news_and_offers", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//                break;
//
//            case R.id.togal_button_3:
//
//                if(togal_button_3.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","text_message");
//                        parameter_updation.put("upcoming_event",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "text_message");
//                        parameter_updation.put("upcoming_event", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//                break;
//
//            case R.id.togal_button_4:
//
//                if(togal_button_4.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","email");
//                        parameter_updation.put("upcoming_event",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "email");
//                        parameter_updation.put("upcoming_event", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//                break;
//
//            case R.id.togal_button_5:
//
//                if(togal_button_5.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","text_message");
//                        parameter_updation.put("one_day_before",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "text_message");
//                        parameter_updation.put("one_day_before", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//                break;
//
//            case R.id.togal_button_6:
//
//                if(togal_button_6.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","email");
//                        parameter_updation.put("one_day_before",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "email");
//                        parameter_updation.put("one_day_before", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//                break;
//
//            case R.id.togal_button_7:
//
//                if(togal_button_7.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","text_message");
//                        parameter_updation.put("before_an_event_begin",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "text_message");
//                        parameter_updation.put("before_an_event_begin", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//                break;
//
//
//            case R.id.togal_button_8:
//
//                if(togal_button_8.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","email");
//                        parameter_updation.put("before_an_event_begin",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "email");
//                        parameter_updation.put("before_an_event_begin", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//                break;
//
//            case R.id.togal_button_9:
//                if(togal_button_9.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","text_message");
//                        parameter_updation.put("auction_item_sold",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "text_message");
//                        parameter_updation.put("auction_item_sold", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//                break;
//
//            case R.id.togal_button_10:
//
//                if(togal_button_10.isChecked()){
//                    try
//                    {
//                        parameter_updation.put("notification_type","email");
//                        parameter_updation.put("auction_item_sold",true);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    }catch (Exception e){
//                        //nothing
//                    }
//                }
//                else {
//                    try {
//                        parameter_updation.put("notification_type", "email");
//                        parameter_updation.put("auction_item_sold", false);
//                        methodaforupdationnotificationsetting(parameter_updation);
//                    } catch (Exception e) {
//                        //nothing
//                    }
//                }
//                break;
        }

    }

    private void methodaforupdationnotificationsetting(JSONObject parameter_updation) {
        System.out.println("----------"+parameter_updation);
        if (!isNetworkAvailable()) {
            Toast.makeText(this,"Internet not available",Toast.LENGTH_LONG).show();
        } else {
        ProgressDialog.getInstance().showProgress(this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"notification_settings/update_setting",parameter_updation,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("----------"+response);
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
                        NetworkResponse networkResponse = error.networkResponse;
                        ProgressDialog.getInstance().hideProgress();
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(Notification_Settings.this);
                            }else {
                                Toast.makeText(Notification_Settings.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Notification_Settings.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
    }
}
