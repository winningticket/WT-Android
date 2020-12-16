package com.winningticketproject.in.Fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.winningticketproject.in.EventTab.Event_Code_valid;
import com.winningticketproject.in.EventTab.NewEventDetailPage;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.EventTab.View_All_Events;
import com.winningticketproject.in.AppInfo.AllEventGetterSetter;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.google.android.gms.internal.zzail.runOnUiThread;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.savestring;


public class EventFragment extends Fragment implements Animation.AnimationListener, View.OnClickListener {

    RecyclerView upcomming_events,all_events,passed_event;
    String code;
    View view,test1View;
    Button cancel,eventcode,enter_event_code;
    EditText edittext1,edittext2,edittext3,edittext4,edittext5,edittext6;
    LinearLayout event_layout;
    Animation animFadein,sidedown;
    TextView upcomming_event_title,allevent_title,code_title,emptyView,upcomming_empty_view,all_passed_events_empty_view,upcoming_view_all,unpurchased_view_all,passed_view_all;
    String auth_token="";
    Intent in;
    private Recycler_Article_Adapter mAdapter;
    private List<AllEventGetterSetter> alleventlist_List = new ArrayList<AllEventGetterSetter>();
    private List<AllEventGetterSetter> upcomming_List = new ArrayList<AllEventGetterSetter>();
    private List<AllEventGetterSetter> passed_List = new ArrayList<AllEventGetterSetter>();
    TextView passed_title;
    AllEventGetterSetter alleventlist;
    AllEventGetterSetter upcommingeventlist ;
    AllEventGetterSetter passedventlist ;
    final Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_event, container, false);

        auth_token =  get_auth_token("auth_token");

        if (!isNetworkAvailable()) {

            alertdailogbox("eventlist");
        }
        else
        {
            methodforcalleventlist(2);

        }

        TextView tv_tournament = view.findViewById(R.id.tv_tournament);
        tv_tournament.setTypeface(semibold);

        TextView tv_charity = view.findViewById(R.id.tv_charity);
        tv_charity.setTypeface(medium);

        TextView upcomming_event_desciption = view.findViewById(R.id.upcomming_event_desciption);
        upcomming_event_desciption.setTypeface(regular);

        TextView passed_desciption = view.findViewById(R.id.passed_desciption);
        passed_desciption.setTypeface(regular);

        test1View =view.findViewById(R.id.code_enter);
        test1View.setVisibility(View.GONE);
        upcomming_events = view.findViewById(R.id.upcomming_events);
        all_events = view.findViewById(R.id.all_events);
        emptyView = view.findViewById(R.id.all_events_empty_view);

        passed_event = view.findViewById(R.id.passed_event);
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity());
        passed_event.setLayoutManager(mLayoutManager3);
        passed_event.setNestedScrollingEnabled(false);

        all_passed_events_empty_view = view.findViewById(R.id.all_passed_events_empty_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        all_events.setLayoutManager(mLayoutManager);

        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        upcomming_events.setLayoutManager(mLayoutManager2);
        upcomming_events.setNestedScrollingEnabled(false);

        upcomming_events.setFocusable(false);
        all_events.setFocusable(false);
        all_events.setNestedScrollingEnabled(false);

        upcomming_empty_view = view.findViewById(R.id.upcomming_empty_view);

        emptyView.setTypeface(medium);
        upcomming_empty_view.setTypeface(medium);
        all_passed_events_empty_view.setTypeface(medium);

        passed_title = view.findViewById(R.id.passed_title);
        passed_title.setTypeface(medium);

        animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.side_down);
        animFadein.setAnimationListener(this);

        sidedown = AnimationUtils.loadAnimation(getActivity(), R.anim.side_up);
        sidedown.setAnimationListener(this);

        edittext1 = view.findViewById(R.id.edittext1);
        code_title = view.findViewById(R.id.code_title);

        enter_event_code  = view.findViewById(R.id.enter_event_code);
        cancel  = view.findViewById(R.id.cancel);
        eventcode  = view.findViewById(R.id.eventcode);
        event_layout = view.findViewById(R.id.event_layout);

        upcoming_view_all = view.findViewById(R.id.upcoming_view_all);
        unpurchased_view_all = view.findViewById(R.id.unpurchased_view_all);
        passed_view_all = view.findViewById(R.id.passed_view_all);

        upcoming_view_all.setTypeface(medium);
        unpurchased_view_all.setTypeface(medium);
        passed_view_all.setTypeface(medium);

        upcoming_view_all.setOnClickListener(this);
        unpurchased_view_all.setOnClickListener(this);
        passed_view_all.setOnClickListener(this);

        eventcode.setOnClickListener(this);

        upcomming_event_title  = view.findViewById(R.id.upcomming_event_title);
        allevent_title  = view.findViewById(R.id.allevent_title);


        upcomming_event_title.setTypeface(medium);
        allevent_title.setTypeface(medium);
        enter_event_code.setTypeface(medium);
        cancel.setTypeface(medium);
        eventcode.setTypeface(medium);
        edittext1.setTypeface(medium);
        code_title.setTypeface(regular);

        enter_event_code.setVisibility(View.VISIBLE);
        enter_event_code.setOnClickListener(this);

        return  view;
    }

    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View view) {
        Intent in;
        switch (view.getId()){
            case R.id.upcoming_view_all:
                 in  = new Intent(getActivity(),View_All_Events.class);
                 in.putExtra("url","events/all_upcoming");
                 in.putExtra("auto_search","events/autocomplete/upcoming");
                 in.putExtra("event_type","upcoming_or_live");
                 startActivity(in);
                break;
            case R.id.unpurchased_view_all:
                 in  = new Intent(getActivity(),View_All_Events.class);
                in.putExtra("url","events/all_today");
                in.putExtra("auto_search","events/autocomplete/today");
                in.putExtra("event_type","unpurchased");
                startActivity(in);
                break;
            case R.id.passed_view_all:
                in  = new Intent(getActivity(),View_All_Events.class);
                in.putExtra("url","events/all_passed");
                in.putExtra("auto_search","events/autocomplete/passed");
                in.putExtra("event_type","passed");
                startActivity(in);
                break;
//            case R.id.eventcode:
////                code = edittext1.getText().toString();
////                hiddenInputMethod();
////                if(code.isEmpty()){
////                    Toast.makeText(getContext(),"Please enter event code",Toast.LENGTH_SHORT).show();
////                }else if (code.length()<=5){
////                    error("Please enter 6 digit code");
////                    }else {
////                    if (!isNetworkAvailable()) {
////                        alertdailogbox("eventcode");
////                    }
////                    else
////                    {
////                        methodforcalleventcode(code);
////                    }
////                }
//                break;

            case R.id.cancel:
                hiddenInputMethod();
                edittext1.requestFocus();
                test1View.startAnimation(sidedown);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enter_event_code.setVisibility(View.VISIBLE);
                        test1View.setVisibility(View.GONE);
                    }
                }, 500);
                break;
            case R.id.enter_event_code:
                method_to_open_event_code_popup();
                break;
        }
    }

    private void method_to_open_event_code_popup() {
        LayoutInflater inflater1 = LayoutInflater.from(getContext());
        View viewq = inflater1.inflate(R.layout.custome_code_enter , null);
        AlertDialog  alertDialog_event = new AlertDialog.Builder(getActivity()).setView(viewq).show();

        edittext1 = viewq.findViewById(R.id.edittext1);

        TextView code_title = viewq.findViewById(R.id.code_title);
        cancel  = viewq.findViewById(R.id.cancel);
        eventcode  = viewq.findViewById(R.id.eventcode);
        event_layout = viewq.findViewById(R.id.event_layout);

        edittext1.setTypeface(medium);

        code_title.setTypeface(regular);

        hiddenInputMethod();
        edittext1.getText().clear();
        edittext1.requestFocus();

        TextView cancel = viewq.findViewById(R.id.cancel);
        TextView eventcode = viewq.findViewById(R.id.eventcode);

        eventcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = edittext1.getText().toString() ;
                hiddenInputMethod();
                if(code.isEmpty()){
                    Toast.makeText(getContext(),"Please enter event code",Toast.LENGTH_SHORT).show();
                }else if (code.length() <= 5) {
                    Toast.makeText(getContext(),"Please enter 6 digit code",Toast.LENGTH_SHORT).show();
                } else {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("eventcode");
                    } else {
                        methodforcalleventcode(code);
                    }
                }
                alertDialog_event.dismiss();
                alertDialog_event.setCanceledOnTouchOutside(false);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_event.dismiss();
                alertDialog_event.setCanceledOnTouchOutside(false);
            }
        });
        alertDialog_event.setCanceledOnTouchOutside(false);
        alertDialog_event.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void error(String s) {
        Snackbar snackbar = Snackbar.make(event_layout, s, Snackbar.LENGTH_LONG);
        snackbar.getView().getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
        view = snackbar.getView();
        TextView txtv = view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtv.setTextSize(TypedValue
                .COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorsnakbar));
        snackbar.show();
    }

    public void methodforcalleventlist(int values){
        if (values==1){
            ProgressDialog.getInstance().showProgress(getContext());
        }

        JsonObjectRequest myRequest = new JsonObjectRequest(
                Request.Method.GET, Splash_screen.url+"events",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        upcomming_List.clear();
                        alleventlist_List.clear();
                        passed_List.clear();
                        try {
                            JSONArray allevent = response.getJSONArray("today_events");

                            for (int i=0;i<allevent.length();i++){
                                JSONObject eventobject = allevent.getJSONObject(i);
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        try {
                                            alleventlist = new AllEventGetterSetter(eventobject.getString("id"),eventobject.getString("name").replaceAll("\\s+"," "),eventobject.getString("converted_start_date"),eventobject.getString("avatar_url"),"unpurchased",eventobject.getString("event_status"),eventobject.getInt("push_notification_count"));
                                            alleventlist_List.add(alleventlist);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }

                            mAdapter = new Recycler_Article_Adapter(getActivity(),alleventlist_List);
                            all_events.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            //nothing
                        }


                        if (alleventlist_List.isEmpty()) {
                            all_events.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }
                        else {
                            all_events.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }

                        try {
                            JSONArray upcomming = response.getJSONArray("upcoming_and_live");
                            for (int i=0;i<upcomming.length();i++){
                                JSONObject upcommingobject = upcomming.getJSONObject(i);
                                upcommingeventlist = new AllEventGetterSetter(upcommingobject.getString("id"),upcommingobject.getString("name"),upcommingobject.getString("converted_start_date"),upcommingobject.getString("avatar_url"),upcommingobject.getString("upcoming_event_type"),upcommingobject.getString("event_status"),upcommingobject.getInt("push_notification_count"));
                                upcomming_List.add(upcommingeventlist);
                            }

                            mAdapter = new Recycler_Article_Adapter(getActivity(),upcomming_List);
                            upcomming_events.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            //nothing
                        }


                        if (upcomming_List.isEmpty()) {
                            upcomming_events.setVisibility(View.GONE);
                            upcomming_empty_view.setVisibility(View.VISIBLE);
                        }
                        else {
                            upcomming_events.setVisibility(View.VISIBLE);
                            upcomming_empty_view.setVisibility(View.GONE);
                        }

                        try {
                            JSONArray upcomming = response.getJSONArray("passed_events");
                            for (int i=0;i<upcomming.length();i++){
                                JSONObject upcommingobject = upcomming.getJSONObject(i);
                                passedventlist = new AllEventGetterSetter(upcommingobject.getString("id"),upcommingobject.getString("name"),upcommingobject.getString("converted_start_date"),upcommingobject.getString("avatar_url"),upcommingobject.getString("passed_event_type"),upcommingobject.getString("event_status"),0);
                                passed_List.add(passedventlist);
                            }

                            mAdapter = new Recycler_Article_Adapter(getActivity(),passed_List);
                            passed_event.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            //nothing
                        }

                        if (passed_List.isEmpty()) {
                            passed_event.setVisibility(View.GONE);
                            all_passed_events_empty_view.setVisibility(View.VISIBLE);
                        }
                        else {
                            passed_event.setVisibility(View.VISIBLE);
                            all_passed_events_empty_view.setVisibility(View.GONE);
                        }


                        if (upcomming_List.size()==15){
                            upcoming_view_all.setVisibility(View.VISIBLE);
                        }

                        if (alleventlist_List.size()==15){
                            unpurchased_view_all.setVisibility(View.VISIBLE);
                        }

                        if (passed_List.size()==15){
                            passed_view_all.setVisibility(View.VISIBLE);
                        }

                        if (values==1){ ProgressDialog.getInstance().hideProgress(); }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                if (values==1){ ProgressDialog.getInstance().hideProgress(); }
                                Alert_Dailog.showNetworkAlert(getActivity());
                            }else {
                                if (values==1){ ProgressDialog.getInstance().hideProgress(); }
                                Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (values==1){ ProgressDialog.getInstance().hideProgress(); }
                            Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();

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

    private void refresh(int i) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do updates for imageview
                methodforcalleventlist(2);
                ProgressDialog.getInstance().hideProgress();
            }
        };
        handler.postDelayed(runnable, i);
    }

    public void methodforcalleventcode(String code){
        JSONObject user_code = new JSONObject();
        try {
            user_code.put("code",code.toUpperCase());
            ProgressDialog.getInstance().showProgress(getActivity());
        }catch (Exception E){
            //nothing
        }

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, Splash_screen.url+"events/event_code",user_code,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("-----res------"+response);

                        try
                        {
                            ProgressDialog.getInstance().hideProgress();
                            if (response.getString("status").equals("Success")){

                                if (response.getString("purchase_type").equals("unpurchased")) {

                                    JSONObject obj = response.getJSONObject("event");
                                    double pricevalue=Double.parseDouble(response.getString("price"));
                                    in = new Intent(getActivity(), Event_Code_valid.class);
                                    Share_it.savestring("ticket_price", String.format("%.2f",pricevalue));
                                    Share_it.savestring("name", obj.getString("name"));
                                    Share_it.savestring("code", obj.getString("code"));
                                    Share_it.savestring("id", obj.getString("id"));
                                    Share_it.savestring("organization_name", obj.getString("organization_name"));
                                    Share_it.savestring("banner_image", obj.getString("banner_image"));
                                    Share_it.savestring("wallete", response.getString("wallet"));
                                    Share_it.savestring("aminities", response.getString("ticket_amenities"));
                                    Share_it.savestring("imge_url", obj.getString("avatar_url"));
                                    Share_it.savestring("start_date", obj.getString("start_date"));
                                    Share_it.savestring("location", obj.getString("get_golf_location"));
                                    Share_it.savestring("get_course_name", obj.getString("get_course_name"));
                                    savestring("play_type", "event");
                                    savestring("premium_type", "");
                                    startActivity(in);
                                    edittext1.getText().clear();
                                }

                                test1View.startAnimation(sidedown);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        enter_event_code.setVisibility(View.VISIBLE);
                                        test1View.setVisibility(View.GONE);
                                    }
                                }, 500);

                                if (response.getString("purchase_type").equals("purchased")){
                                    Intent in = new Intent(getActivity(), NewEventDetailPage.class);
                                    in.putExtra("id",response.getString("event_id"));
                                    in.putExtra("homepage","1");
                                    in.putExtra("event_type","upcoming_or_live");
                                    startActivity(in);
                                }
                            }else {
//                                error(response.getString("message"));
                                Toast.makeText(getContext(),response.getString("message"),Toast.LENGTH_SHORT).show();

                                edittext1.getText().clear();
                                edittext1.requestFocus();
                            }
                        }catch (Exception e){
                            //nothing
                        }
                    }
                },
                new Response.ErrorListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                ProgressDialog.getInstance().hideProgress();
                                Alert_Dailog.showNetworkAlert(getActivity());
                            }else {
                                ProgressDialog.getInstance().hideProgress();
//                                    error("Invalid code");
                                Toast.makeText(getContext(),"Invalid code",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            ProgressDialog.getInstance().hideProgress();
                            Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();

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
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }



    public void alertdailogbox(final String value){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("No Internet");
        builder.setMessage("Internet is required. Please Retry.");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                if(value.equals("eventlist")) {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("eventlist");
                    } else {
                        methodforcalleventlist(2);
                    }
                }
                else if(value.equals("eventlist"))
                {
                    if (!isNetworkAvailable()) {
                        alertdailogbox("eventcode");
                    } else {
                        methodforcalleventcode(code);
                    }
                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        methodforcalleventlist(1);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((mMessageReceiver), new IntentFilter("Notification_count"));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isNetworkAvailable()) {
                alertdailogbox("livescore");
            } else {
                methodforcalleventlist(2);
            }
        }
    };
}
