package com.winningticketproject.in.Player_stats;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
public class pieChart3 extends DemoBase implements View.OnClickListener {

    public  int flot_par_per, flot_birdie_per, flot_double_boggey_per, flot_eagle_per, float_bogey_per;
    PieChart chart;
    ListView list;
    ArrayList<scoring_getter_setter> scoring_getter_setters = new ArrayList<>();
    ImageView back_reular_btn,stats_date_filter_btn;
    SimpleDateFormat spf;
    String start_date="";
    android.support.v7.app.AlertDialog alertDialog;
    ArrayList<String> date_list = new ArrayList<>();
    Button btn_gross,btn_net;
    String button_type = "net";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart3);


        Date c = Calendar.getInstance().getTime();
        spf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa");
        spf= new SimpleDateFormat("dd-MM-yyyy");
        start_date = spf.format(c);


        TextView scoring_your_round= findViewById(R.id.scoring_your_round);
        scoring_your_round.setTypeface(medium);

        pieChart();

        method_to_load_top_serction();

        method_call_scoring("users/score-details?gross_net=net&start_date="+start_date);

        back_reular_btn = findViewById(R.id.back_stats_top_buttons);
        back_reular_btn.setOnTouchListener(new View.OnTouchListener() {
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


        btn_gross = findViewById(R.id.btn_gross);
        btn_net = findViewById(R.id.btn_net);

        btn_gross.setTypeface(regular);
        btn_net.setTypeface(regular);

        btn_gross.setOnClickListener(this);
        btn_net.setOnClickListener(this);


    }


    private void Show_custome_dailogs_date_picker() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(pieChart3.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) pieChart3.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                    start_date  = date_list.get(0);

                    if (button_type.equals("net")){
                        method_call_scoring("users/score-details?gross_net=net&start_date="+date_list.get(0));
                    }else {
                        method_call_scoring("users/score-details?gross_net=gross&start_date="+date_list.get(0));
                    }

                }else if (date_list.size()==0){

                    if (button_type.equals("net")){
                        method_call_scoring("users/score-details?gross_net=net&start_date="+start_date);
                    }else {
                        method_call_scoring("users/score-details?gross_net=gross&start_date="+start_date);
                    }
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


    private void method_call_scoring(String datess) {
        ProgressDialog.getInstance().showProgress(pieChart3.this);

        scoring_getter_setters.clear();

        System.out.println("--------"+Splash_screen.url+datess);

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+datess,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("scoring" + response);

                        try {
                            ProgressDialog.getInstance().hideProgress();

                            try {

                                JSONObject par_per_object = response.getJSONObject("score_card_details");
                                String par_per = par_per_object.getString("par_per");
                                if (par_per.equals(null) || par_per.equals("") || par_per.equals("null") || par_per==null){
                                    par_per="0";
                                }

                                double stat_center_per_par_per = Double.parseDouble(par_per);
                                flot_par_per = (int) (Math.round(stat_center_per_par_per));

                                String birdie_per = par_per_object.getString("birdie_per");
                                if (birdie_per.equals(null) || birdie_per.equals("") || birdie_per.equals("null") || birdie_per==null){
                                    birdie_per="0";
                                }
                                double stat_center_birdie_per = Double.parseDouble(birdie_per);
                                flot_birdie_per = (int) (Math.round(stat_center_birdie_per ));

                                String double_boggey_per = par_per_object.getString("double_boggey_per");
                                if (double_boggey_per.equals(null) || double_boggey_per.equals("") || double_boggey_per.equals("null") || double_boggey_per==null){
                                    double_boggey_per="0";
                                }
                                double stat_center_double_boggey_per = Double.parseDouble(double_boggey_per);
                                flot_double_boggey_per = (int) (Math.round(stat_center_double_boggey_per));

                                String eagle_per = par_per_object.getString("eagle_per");
                                if (eagle_per.equals(null) || eagle_per.equals("") || eagle_per.equals("null") || eagle_per==null){
                                    eagle_per="0";
                                }
                                double stat_center_eagle_per = Double.parseDouble(eagle_per);
                                flot_eagle_per = (int) (Math.round(stat_center_eagle_per));

                                String bogey_per = par_per_object.getString("bogey_per");
                                if (bogey_per.equals(null) || bogey_per.equals("") || bogey_per.equals("null") || bogey_per==null){
                                    bogey_per="0";
                                }

                                System.out.println(par_per+"---"+birdie_per+"---"+double_boggey_per+"---"+eagle_per+"---"+bogey_per);

                                if( par_per.equals("0") && birdie_per.equals("0") && double_boggey_per.equals("0") && eagle_per.equals("0") && bogey_per.equals("0")){
                                   System.out.println(par_per+"---"+birdie_per+"---"+double_boggey_per+"---"+eagle_per+"---"+bogey_per);
                                    chart.clear();
                                }


                                double stat_center_bogey_per = Double.parseDouble(bogey_per);
                                float_bogey_per = (int) (Math.round(stat_center_bogey_per));

                                pieChart();

                                method_to_load_top_serction();

                            }catch (Exception e){

                            }


                            JSONArray get_fairway_details = response.getJSONArray("score_card_array");

                            for (int i=0;i<get_fairway_details.length();i++){

                                JSONObject score_card_array =  get_fairway_details.getJSONObject(i);
                                String score_date_details = score_card_array.getString("score_date_details");
                                String event_name = score_card_array.getString("event_name");
                                String total_score="";

                                int flot_par_per=0,flot_birdie_per=0,flot_double_boggey_per=0,flot_eagle_per=0,float_bogey_per=0;

                                JSONObject get_score_details= null;
                                if (button_type.equals("gross")){
                                    get_score_details =  score_card_array.getJSONObject("get_score_details_gross");

                                    total_score = score_card_array.getString("total_gross_score");

                                }else {
                                    get_score_details =  score_card_array.getJSONObject("get_score_details");

                                    total_score = score_card_array.getString("total_net_score");

                                }

                                if(total_score==null || total_score.equals(null) || total_score.equals("null") || total_score.equals("")){
                                    total_score="0";
                                }

                                if (get_score_details.length()>0){
                                    String score_par_per = get_score_details.getString("par_per");
                                    if (score_par_per.equals(null) || score_par_per.equals("") || score_par_per.equals("null") || score_par_per==null){
                                        score_par_per="0";
                                    }
                                    double center_per_par_per = Double.parseDouble(score_par_per);
                                    flot_par_per = (int) (Math.round(center_per_par_per));

                                    String score_birdie_per = get_score_details.getString("birdie_per");
                                    if (score_birdie_per.equals(null) || score_birdie_per.equals("") || score_birdie_per.equals("null") || score_birdie_per==null){
                                        score_birdie_per="0";
                                    }

                                    double center_birdie_per = Double.parseDouble(score_birdie_per);
                                    flot_birdie_per = (int) (Math.round(center_birdie_per ));

                                    String Score_center_double_boggey_per = get_score_details.getString("double_boggey_per");
                                    if (Score_center_double_boggey_per.equals(null) || Score_center_double_boggey_per.equals("") || Score_center_double_boggey_per.equals("null") || Score_center_double_boggey_per==null){
                                        Score_center_double_boggey_per="0";
                                    }

                                    double center_double_boggey_per = Double.parseDouble(Score_center_double_boggey_per);
                                    flot_double_boggey_per = (int) (Math.round(center_double_boggey_per));

                                    String score_eagle_per = get_score_details.getString("eagle_per");
                                    if (score_eagle_per.equals(null) || score_eagle_per.equals("") || score_eagle_per.equals("null") || score_eagle_per==null){
                                        score_eagle_per="0";
                                    }

                                    double center_eagle_per = Double.parseDouble(score_eagle_per);
                                    flot_eagle_per = (int) (Math.round(center_eagle_per));

                                    String score_bogey_per = get_score_details.getString("bogey_per");
                                    if (score_bogey_per.equals(null) || score_bogey_per.equals("") || score_bogey_per.equals("null") || score_bogey_per==null){
                                        score_bogey_per="0";
                                    }

                                    double center_bogey_per = Double.parseDouble(score_bogey_per);
                                    float_bogey_per = (int) (Math.round(center_bogey_per));
                                }

                                scoring_getter_setters.add(new scoring_getter_setter(event_name,score_date_details,total_score,flot_eagle_per+"",flot_birdie_per+"",flot_par_per+"",float_bogey_per+"",flot_double_boggey_per+""));


                            }

                            pieChartAdapter3 adapter=new pieChartAdapter3(pieChart3.this,scoring_getter_setters);
                            list=findViewById(R.id.scoring_list);
                            list.setAdapter(adapter);
                            list.setFocusable(false);
//                            setListViewHeightBasedOnChildren(list);

                            ProgressDialog.getInstance().hideProgress();

                            if(get_fairway_details.length()==0){
                                Toast.makeText(getApplicationContext(),"No Records Found",Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
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
                                Alert_Dailog.showNetworkAlert(pieChart3.this);
                            }else {
                                Toast.makeText(pieChart3.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(pieChart3.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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

    private void method_to_load_top_serction() {

        TextView d_fair_title = findViewById(R.id.stats_top_title);
        d_fair_title.setTypeface(medium);
        d_fair_title.setText("Scoring");

        TextView stats_based_title = findViewById(R.id.stats_sub_title);
        stats_based_title.setTypeface(regular);

        TextView scoring_eagle_hint = findViewById(R.id.scoring_eagle_hint);
        scoring_eagle_hint.setTypeface(regular);

        TextView scoring_bride_hint = findViewById(R.id.scoring_bride_hint);
        scoring_bride_hint.setTypeface(regular);

        TextView scoring_par_hint = findViewById(R.id.scoring_par_hint);
        scoring_par_hint.setTypeface(regular);

        TextView scoring_bogey_hint = findViewById(R.id.scoring_bogey_hint);
        scoring_bogey_hint.setTypeface(regular);

        TextView scoring_double_bogey_hint = findViewById(R.id.scoring_double_bogey_hint);
        scoring_double_bogey_hint.setTypeface(regular);


        TextView scoring_eagle_data = findViewById(R.id.scoring_eagle_data);
        scoring_eagle_data.setTypeface(medium);
        scoring_eagle_data.setText(flot_eagle_per+"%");

        TextView scoring_bride_data = findViewById(R.id.scoring_bride_data);
        scoring_bride_data.setTypeface(medium);
        scoring_bride_data.setText(flot_birdie_per+"%");

        TextView scoring_par_data = findViewById(R.id.scoring_par_data);
        scoring_par_data.setTypeface(medium);
        scoring_par_data.setText(flot_par_per+"%");

        TextView scoring_bogey_data = findViewById(R.id.scoring_bogey_data);
        scoring_bogey_data.setTypeface(medium);
        scoring_bogey_data.setText(float_bogey_per+"%");

        TextView scoring_double_bogey_data = findViewById(R.id.scoring_double_bogey_data);
        scoring_double_bogey_data.setTypeface(medium);
        scoring_double_bogey_data.setText(flot_double_boggey_per+"%");


    }


    private void pieChart() {

        chart = findViewById(R.id.regular_pie_chart);
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
//        chart3.setOnChartValueSelectedListener(this);
        Legend l3 = chart.getLegend();
        l3.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l3.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l3.setOrientation(Legend.LegendOrientation.VERTICAL);
        l3.setDrawInside(false);
        l3.setXEntrySpace(7f);
        l3.setYEntrySpace(0f);
        l3.setYOffset(0f);
        // entry label styling
        chart.setEntryLabelColor(WHITE);

        setDatap();
    }

    private void setDatap() {

        ArrayList<PieEntry> regular_entries = new ArrayList<>();
        ArrayList<Integer> regular_colors = new ArrayList<>();

        regular_colors.add(Color.parseColor("#FF8B38"));
        regular_entries.add(new PieEntry(flot_eagle_per));

        regular_colors.add(Color.parseColor("#FF3030"));
        regular_entries.add(new PieEntry(flot_birdie_per));

        regular_colors.add(Color.parseColor("#5CB768"));
        regular_entries.add(new PieEntry(flot_par_per));

        regular_colors.add(Color.parseColor("#8AB6FF"));
        regular_entries.add(new PieEntry(float_bogey_per));

        regular_colors.add(Color.parseColor("#C400FF"));
        regular_entries.add(new PieEntry(flot_double_boggey_per));

        PieDataSet dataSet = new PieDataSet(regular_entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 0));
        dataSet.setSelectionShift(10f);
        // add a lot of colors

        dataSet.setColors(regular_colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(0);
        data.setValueTextColor(WHITE);
        chart.setData(data);
        chart.setData(data);
    }
    @Override
    protected void saveToGallery() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_gross:

                button_type = "gross";
                btn_gross.setBackgroundResource(R.drawable.gross_score_selection);
                btn_net.setBackgroundResource(R.drawable.neft_baground);

                btn_net.setTextColor(getResources().getColor(R.color.btn_color));
                btn_gross.setTextColor(getResources().getColor(R.color.colorwhite));

                method_call_scoring("users/score-details?gross_net=gross&start_date="+start_date);

                break;

            case R.id.btn_net:

                btn_net.setBackgroundResource(R.drawable.gross_score_selection);
                btn_gross.setBackgroundResource(R.drawable.neft_baground);

                btn_gross.setTextColor(getResources().getColor(R.color.btn_color));
                btn_net.setTextColor(getResources().getColor(R.color.colorwhite));

                button_type = "net";

                method_call_scoring("users/score-details?gross_net=net&start_date="+start_date);

                break;
        }
    }
}

