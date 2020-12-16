package com.winningticketproject.in.Player_stats;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.MPPointF;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.ProgressDialog;
import com.winningticketproject.in.R;
import com.wisnu.datetimerangepickerandroid.CalendarPickerView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static android.graphics.Color.WHITE;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;

public class pieChart1 extends DemoBase {
    PieChart chart;
    ListView list;
    ImageButton back_firway_btn,stats_date_filter_btn;
    ArrayList<fairway_getter_setter> fairway_getter_setters = new ArrayList<>();
    android.support.v7.app.AlertDialog alertDialog;
    ArrayList<String> date_list = new ArrayList<>();
    SimpleDateFormat spf;
    String start_date="";
    public  float roundOff, rightOff, leftOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart1);

        Date c = Calendar.getInstance().getTime();
        spf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa");
        spf= new SimpleDateFormat("dd-MM-yyyy");
        start_date = spf.format(c);

        method_to_load_fair_way_data();

        pieChart();

        setDatap();

        TextView you_round = findViewById(R.id.you_round);
        you_round.setTypeface(medium);

        method_to_call_fairway_hits(start_date);

        back_firway_btn = findViewById(R.id.back_stats_top_buttons);
        back_firway_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });


        stats_date_filter_btn = findViewById(R.id.stats_date_filter_btn);
        stats_date_filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Show_custome_dailogs_date_picker();
                showDialog(999);
            }
        });

    }

    private void Show_custome_dailogs_date_picker() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(pieChart1.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) pieChart1.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogview = inflater.inflate(R.layout.custome_date_picker_popup, null);

        final android.support.v7.app.AlertDialog popupDia = builder.create();
        popupDia.setView(dialogview, 0, 0, 0, 0);
        popupDia.setCanceledOnTouchOutside(false);
        popupDia.setCancelable(false);
        popupDia.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        popupDia.show();
        alertDialog = popupDia;


        TextView filter_by_state = dialogview.findViewById(R.id.filter_by_state);
        filter_by_state.setTypeface(medium);

        TextView filter_description = dialogview.findViewById(R.id.filter_description);
        filter_description.setTypeface(regular);


        Button btn_show_dates = dialogview.findViewById(R.id.btn_show_dates);
        btn_show_dates.setTypeface(medium);

        Button btn_cancel = dialogview.findViewById(R.id.btn_cancel);
        btn_cancel.setTypeface(medium);

        CalendarPickerView cal = dialogview.findViewById(R.id.calendar_view);
        cal.init(DateTime.now(DateTimeZone.UTC).minusYears(1).toDate(),
                DateTime.now(DateTimeZone.UTC).plusDays(1).toDate())
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDate(DateTime.now(DateTimeZone.UTC).minusDays(1).plusDays(1).toDate());

        final TextView selected_dates = dialogview.findViewById(R.id.selected_dates);
        selected_dates.setTypeface(regular);

        btn_show_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (date_list.size() > 0) {
                    method_to_call_fairway_hits(date_list.get(0));
                }else if (date_list.size()==0){
                    method_to_call_fairway_hits(start_date);
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        selected_dates.setText(Html.fromHtml("<small><b>Week Selected : </b></small><small>"+start_date+"</small>"));

        date_list.clear();
        cal.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {

                String dates = spf.format(date);

                date_list.add(dates + "");

                if (date_list.size() > 1) {
                    selected_dates.setText(Html.fromHtml("<small><b>Week Selected : </b></big><small>"+date_list.get(0) + " - " + date_list.get(1)+"</small>"));
                }

                if (date_list.size() == 1) {
                    selected_dates.setText(Html.fromHtml("<small><b>Week Selected : </b></big><small>"+date_list.get(0)+"</small>"));
                }
            }

            @Override
            public void onDateUnselected(Date date) {
                date_list.clear();
                selected_dates.setText("Week date not selected ");

            }
        });

    }

    private void method_to_call_fairway_hits(String datess) {

        fairway_getter_setters.clear();
        System.out.println(Splash_screen.url+"users/fairway-details?start_date="+datess+"----------");
        ProgressDialog.getInstance().showProgress(pieChart1.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"users/fairway-details?start_date="+datess,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("fairway-----" + response);
                        try {
                            ProgressDialog.getInstance().hideProgress();


                            JSONObject fairway_array = response.getJSONObject("fairway_array");
                            String fair_way_left_per = fairway_array.getString("left_per");
                            if (fair_way_left_per.equals(null) || fair_way_left_per.equals("") || fair_way_left_per.equals("null") || fair_way_left_per==null){
                                fair_way_left_per="0";
                            }

                            String fair_way_center_per = fairway_array.getString("center_per");
                            if (fair_way_center_per.equals(null) || fair_way_center_per.equals("") || fair_way_center_per.equals("null") || fair_way_center_per==null){
                                fair_way_center_per="0";
                            }

                            String fair_way_right_per = fairway_array.getString("right_per");
                            if (fair_way_right_per.equals(null) || fair_way_right_per.equals("") || fair_way_right_per.equals("null") || fair_way_right_per==null){
                                fair_way_right_per="0";
                            }

                            double fair_way_center_per_round_up = Double.parseDouble(fair_way_center_per);
                            roundOff = (float) (Math.round(fair_way_center_per_round_up * 10.0) / 10.0);

                            double fair_way_left_per_round_up = Double.parseDouble(fair_way_left_per);
                            leftOff = (float) (Math.round(fair_way_left_per_round_up * 10.0) / 10.0);

                            double fair_way_right_per_round_up = Double.parseDouble(fair_way_right_per);
                            rightOff = (float) (Math.round(fair_way_right_per_round_up * 10.0) / 10.0);

                            method_to_load_fair_way_data();

                            pieChart();

                            setDatap();

                            if (fair_way_right_per.equals("0") && fair_way_center_per.equals("0") &&  fair_way_left_per.equals("0")){
                                System.out.println(fair_way_right_per + "=======i========" + fair_way_center_per + "=========i========" + fair_way_left_per);
                                chart.clear();
                            }

                            fairway_getter_setters.clear();
                            JSONArray get_fairway_details = response.getJSONArray("get_fairway_details");

                            for (int i=0;i<get_fairway_details.length();i++){
                                JSONObject object =  get_fairway_details.getJSONObject(i);
                                String  total_score = object.getString("total_score");
                                String  score_date_details = object.getString("score_date_details");
                                String  left_per = object.getString("left_per");
                                String  right_per = object.getString("right_per");
                                String  center_per = object.getString("center_per");
                                String  event_name = object.getString("event_name");

                                if (left_per.equals(null) || left_per.equals("") || left_per.equals("null") || left_per==null){
                                    left_per="0";
                                }

                                if (center_per.equals(null) || center_per.equals("") || center_per.equals("null") || center_per==null){
                                    center_per="0";
                                }

                                if (right_per.equals(null) || right_per.equals("") || right_per.equals("null") || right_per==null){
                                    right_per="0";
                                }

                                double center_per_round_up = Double.parseDouble(center_per);
                                float center_hit = (float) (Math.round(center_per_round_up * 10.0) / 10.0);

                                double left_per_round_up = Double.parseDouble(left_per);
                                float leftOff = (float) (Math.round(left_per_round_up * 10.0) / 10.0);

                                double right_per_round_up = Double.parseDouble(right_per);
                                float rightOff = (float) (Math.round(right_per_round_up * 10.0) / 10.0);

                                fairway_getter_setters.add(new fairway_getter_setter(total_score,score_date_details,leftOff+"",rightOff+"",center_hit+"",event_name));

                            }

                            pieChartAdapter1 adapter=new pieChartAdapter1(pieChart1.this,fairway_getter_setters);
                            list=findViewById(R.id.list);
                            list.setAdapter(adapter);
                            list.setFocusable(false);
//                            setListViewHeightBasedOnChildren(list);

                            ProgressDialog.getInstance().hideProgress();

                            if(get_fairway_details.length()==0){
                                Toast.makeText(getApplicationContext(),"No Records Found",Toast.LENGTH_LONG).show();
                            }


                        }catch (Exception e){
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
                                Alert_Dailog.showNetworkAlert(pieChart1.this);
                            }else {
                                Toast.makeText(pieChart1.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(pieChart1.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }){
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

    private void method_to_load_fair_way_data() {

        TextView circle_hit = findViewById(R.id.circle_hit);
        TextView circle_left = findViewById(R.id.d_fair_left);
        TextView circle_Right = findViewById(R.id.circle_right);

        TextView d_fair_title = findViewById(R.id.stats_top_title);
        d_fair_title.setTypeface(medium);
        d_fair_title.setText("Fairways Hits");

        TextView stats_based_title = findViewById(R.id.stats_sub_title);
        stats_based_title.setTypeface(regular);

        TextView d_f_hits = findViewById(R.id.d_f_hits);
        d_f_hits.setTypeface(medium);
        d_f_hits.setText(roundOff+"%");

        TextView d_fair_left = findViewById(R.id.d_fair_left);
        d_fair_left.setTypeface(medium);
        d_fair_left.setText(leftOff+"%");

        TextView d_fai_right = findViewById(R.id.d_fai_right);
        d_fai_right.setTypeface(medium);
        d_fai_right.setText(rightOff+"%");

        TextView d_fair_hint_center = findViewById(R.id.d_fair_hint_center);
        d_fair_hint_center.setTypeface(regular);

        TextView d_fair_hint_left = findViewById(R.id.d_fair_hint_left);
        d_fair_hint_left.setTypeface(regular);

        TextView d_fair_hint_right = findViewById(R.id.d_fair_hint_right);
        d_fair_hint_right.setTypeface(regular);

        if (roundOff > rightOff && roundOff > leftOff){
            circle_hit.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.big_text_size));
            d_f_hits.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.big_text_size));
        }else if (rightOff > roundOff && rightOff > leftOff){
            circle_Right.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.big_text_size));
            d_fai_right.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.big_text_size));
        }else if (leftOff> rightOff && leftOff > roundOff){
            circle_left.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.big_text_size));
            d_fair_left.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.big_text_size));
        }


    }


    private void pieChart() {
        chart = findViewById(R.id.details_pie_chart);
        chart.clear();
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(WHITE);
        chart.setCenterTextTypeface(tfLight);
        chart.setTransparentCircleColor(WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        chart.getLegend().setEnabled(false);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);
//        chart.setOnChartValueSelectedListener(this);
        Legend l3 = chart.getLegend();
        l3.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l3.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l3.setOrientation(Legend.LegendOrientation.VERTICAL);
        l3.setDrawInside(false);
        l3.setXEntrySpace(7);
        l3.setYEntrySpace(0);
        l3.setYOffset(0f);
        chart.setEntryLabelTypeface(tfFont);

        setDatap();
    }

    private void setDatap() {

        System.out.println(roundOff+"--------------"+leftOff+"---------"+rightOff);

//        if (roundOff== 0 && leftOff == 0 && rightOff == 0){
//            chart.clear();
//        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.clear();
        ArrayList<Integer> fairwa_colors = new ArrayList<>();
        fairwa_colors.clear();

        for (int i = 0; i < 3; i++) {
            if(i==0){
                fairwa_colors.add(Color.parseColor("#5CB768"));
                entries.add(new PieEntry(roundOff));
            }else if(i==1){
                fairwa_colors.add(Color.parseColor("#8AB6FF"));
                if (leftOff<1){
                    entries.add(new PieEntry(leftOff, parties[i % parties.length], getResources()));
                }else {
                    entries.add(new PieEntry(leftOff, parties[i % parties.length], getResources()));
                }
            }else if(i==2){
                fairwa_colors.add(Color.parseColor("#FF3030"));
                if (rightOff<1){
                    entries.add(new PieEntry(rightOff));
                }else {
                    entries.add(new PieEntry(rightOff, parties[i % parties.length], getResources()));
                }

            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 0));
        dataSet.setSelectionShift(10);
        // add a lot of colors

        dataSet.setColors(fairwa_colors);
//        dataSet.setColors(Color.GRAY, Color.LTGRAY,Color.RED);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(0);
        chart.setData(data);

        chart.notifyDataSetChanged();
        chart.invalidate();


    }
    @Override
    protected void saveToGallery() {

    }
}
