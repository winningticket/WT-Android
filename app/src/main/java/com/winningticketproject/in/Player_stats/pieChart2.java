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
public class pieChart2 extends DemoBase {

    ImageButton stats_date_filter_btn,back_reular_btn;
    PieChart chart;
    ListView list;
    SimpleDateFormat spf;
    String start_date="";
    android.support.v7.app.AlertDialog alertDialog;
    ArrayList<String> date_list = new ArrayList<>();
    public  float hit_regular_value, miss_regular_values;
    String hit_per = "";
    String miss_per = "";
    ArrayList<fairway_getter_setter> green_in_reulation_getter_setters = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart2);

        Date c = Calendar.getInstance().getTime();
        spf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa");
        spf= new SimpleDateFormat("dd-MM-yyyy");
        start_date = spf.format(c);

        method_to_call_fairway_hits(start_date);

        method_to_update_regular_graph();

        back_reular_btn = findViewById(R.id.back_stats_top_buttons);
        back_reular_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });
        pieChart();

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

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(pieChart2.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) pieChart2.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    private void method_to_update_regular_graph() {

        TextView regulation_your_round = findViewById(R.id.regulation_your_round);
        regulation_your_round.setTypeface(regular);
        TextView green_in_regulation_title = findViewById(R.id.stats_top_title);
        green_in_regulation_title.setText("Greens In Regulations");
        green_in_regulation_title.setTypeface(medium);

        TextView green_in_reg_sub_title = findViewById(R.id.stats_sub_title);
        green_in_reg_sub_title.setTypeface(regular);


        TextView regulation_hit = findViewById(R.id.regulation_hit);
        regulation_hit.setTypeface(medium);
        regulation_hit.setText(hit_regular_value+"%");

        TextView regulation_hit_hint = findViewById(R.id.regulation_hit_hint);
        regulation_hit_hint.setTypeface(regular);


        TextView regulation_miss = findViewById(R.id.regulation_miss);
        regulation_miss.setTypeface(medium);
        regulation_miss.setText(miss_regular_values+"%");

        TextView regulation_miss_hit = findViewById(R.id.regulation_miss_hit);
        regulation_miss_hit.setTypeface(regular);


        if (miss_regular_values > hit_regular_value){
            regulation_miss.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.big_text_size));
        }else if (hit_regular_value > miss_regular_values){
            regulation_hit.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.big_text_size));
        }

    }


    private void method_to_call_fairway_hits(String datess) {
        green_in_reulation_getter_setters.clear();
        ProgressDialog.getInstance().showProgress(pieChart2.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"users/gir-details?start_date="+datess,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("gair_way---------" + response);
                        try {

                            ProgressDialog.getInstance().hideProgress();

                            // gir array graph
                            JSONObject gir_object = response.getJSONObject("gir_array");

                            String arr_miss_per = gir_object.getString("miss_per");
                            if (arr_miss_per.equals(null) || arr_miss_per.equals("") || arr_miss_per.equals("null") || arr_miss_per==null){
                                arr_miss_per="0";
                            }
                            String arr_hit_per = gir_object.getString("hit_per");
                            if (arr_hit_per.equals(null) || arr_hit_per.equals("") || arr_hit_per.equals("null") || arr_hit_per==null){
                                arr_hit_per="0";
                            }


                            double his_regular = Double.parseDouble(arr_hit_per);
                            hit_regular_value = (float) (Math.round(his_regular * 10.0) / 10.0);

                            double miss_regular = Double.parseDouble(arr_miss_per);
                            miss_regular_values = (float) (Math.round(miss_regular * 10.0) / 10.0);

                            method_to_update_regular_graph();

                            pieChart();

                            setDatap();

                            JSONArray get_fairway_details = response.getJSONArray("get_gir_details");

                            for (int i=0;i<get_fairway_details.length();i++){
                                JSONObject object =  get_fairway_details.getJSONObject(i);
                                String  total_score = object.getString("total_score");
                                String  score_date_details = object.getString("score_date_details");
                                String  hit_per = object.getString("hit_per");
                                String  miss_per = object.getString("miss_per");
                                String  event_name = object.getString("event_name");

                                if (hit_per.equals(null) || hit_per.equals("") || hit_per.equals("null") || hit_per==null){
                                    hit_per="0";
                                }

                                if (miss_per.equals(null) || miss_per.equals("") || miss_per.equals("null") || miss_per==null){
                                    miss_per="0";
                                }


                                double hit_per_round_up = Double.parseDouble(hit_per);
                                float center_hit = (float) (Math.round(hit_per_round_up * 10.0) / 10.0);

                                double miss_per_round_up = Double.parseDouble(miss_per);
                                float center_miss_ = (float) (Math.round(miss_per_round_up * 10.0) / 10.0);

                                green_in_reulation_getter_setters.add(new fairway_getter_setter(total_score,score_date_details,center_hit+"",center_miss_+"",""+"",event_name));

                            }

                            if (hit_per.equals("0") && miss_per.equals("0")){
                                chart.clear();
                            }

                            pieChartAdapter2 adapter=new pieChartAdapter2(pieChart2.this,green_in_reulation_getter_setters);
                            list= findViewById(R.id.list);
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
                                Alert_Dailog.showNetworkAlert(pieChart2.this);
                            }else {
                                Toast.makeText(pieChart2.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(pieChart2.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

    private void pieChart() {

        chart = findViewById(R.id.details_regular_pie_chart);
        chart.clear();
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(WHITE);
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
//        chart1.setOnChartValueSelectedListener(this);
        Legend l1 = chart.getLegend();
        l1.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l1.setOrientation(Legend.LegendOrientation.VERTICAL);
        l1.setDrawInside(false);
        l1.setXEntrySpace(7f);
        l1.setYEntrySpace(0f);
        l1.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(WHITE);

        setDatap();
    }

    private void setDatap() {

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> fairwa_colors = new ArrayList<>();

        fairwa_colors.add(Color.parseColor("#5CB768"));
        entries.add(new PieEntry(hit_regular_value));

        fairwa_colors.add(Color.parseColor("#8AB6FF"));
        entries.add(new PieEntry(miss_regular_values));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 0));
        dataSet.setSelectionShift(10f);
        // add a lot of colors

        dataSet.setColors( fairwa_colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(0);
        data.setValueTextColor(WHITE);
        chart.setData(data);
    }
    @Override
    protected void saveToGallery() {

    }
}
