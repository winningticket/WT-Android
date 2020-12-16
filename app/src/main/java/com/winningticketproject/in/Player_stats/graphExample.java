package com.winningticketproject.in.Player_stats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
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

import static android.graphics.Color.GRAY;
import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
public class graphExample extends DemoBase {
    PieChart chart, chart1,chart3;
    private BarChart chart4;
    Intent intent;
    ImageView btn_all_stats_back;
    LinearLayout fair_ways_hit_empty_lay,gree_in_regular_lay,scoring_lay;
    TextView txt_fair_way_title,green_way_data_empty,fair_way_data_empty,scorring_way_data_empty,navigate_put_details;

    android.support.v7.app.AlertDialog alertDialog;
    ArrayList<String> date_list = new ArrayList<>();
    SimpleDateFormat spf;
    String start_date="";

    String hit_per = "";
    String miss_per = "";

    public static String par_per, birdie_per, double_boggey_per, eagle_per, bogey_per;
    public static int flot_par_per, flot_birdie_per, flot_double_boggey_per, flot_eagle_per, float_bogey_per;
    public static String center_per = "", left_per = "", right_per = "";
    public static float roundOff, rightOff, leftOff;
    public static float hit_regular_value, miss_regular_values;

    public static ArrayList<BarEntry> puts_array_values = new ArrayList<>();
    public static ArrayList<Integer> puts_max_values = new ArrayList<>();
    public static ArrayList<PieEntry> total_score_values = new ArrayList<>();

    public static JSONObject fairwa_hit;

    public static ArrayList<String> putts_bottom_valkues = new ArrayList<>();
    public static ArrayList<Integer> putts_colors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_example);

        Date c = Calendar.getInstance().getTime();
        spf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa");
        spf= new SimpleDateFormat("dd-MM-yyyy");
        start_date = spf.format(c);


        TextView id_totali_puts = findViewById(R.id.id_totali_puts);
        id_totali_puts.setTypeface(medium);

        TextView all_stats_title = findViewById(R.id.all_stats_title);
        all_stats_title.setTypeface(medium);

        btn_all_stats_back = findViewById(R.id.btn_all_stats_back);
        btn_all_stats_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        fair_ways_hit_empty_lay = findViewById(R.id.fair_ways_hit_empty_lay);
        gree_in_regular_lay = findViewById(R.id.gree_in_regular_lay);
        scoring_lay = findViewById(R.id.scoring_lay);

        fair_way_data_empty = findViewById(R.id.fair_way_data_empty);
        fair_way_data_empty.setTypeface(medium);


        txt_fair_way_title = findViewById(R.id.txt_fair_way_title);
        txt_fair_way_title.setTypeface(semibold);

        green_way_data_empty = findViewById(R.id.green_way_data_empty);
        green_way_data_empty.setTypeface(medium);

        scorring_way_data_empty = findViewById(R.id.scorring_way_data_empty);
        scorring_way_data_empty.setTypeface(medium);

        navigate_put_details = findViewById(R.id.navigate_put_details);

       ImageButton stats_date_filter_btn = findViewById(R.id.all_stats_date_filter_btn);
        stats_date_filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Show_custome_dailogs_date_picker();
                showDialog(999);
            }
        });


        method_to_call_all_methods();
        Load_Graph_Section_data1();

    }

    private void method_to_call_all_methods() {

        method_to_cal_update_regular_in_green();
        method_update_firway_hits();
        method_for_scoring();

        if (fairwa_hit==null){

            fair_way_data_empty.setVisibility(View.VISIBLE);
            fair_ways_hit_empty_lay.setVisibility(View.GONE);
            txt_fair_way_title.setVisibility(View.GONE);

            green_way_data_empty.setVisibility(View.VISIBLE);
            gree_in_regular_lay.setVisibility(View.GONE);

            scorring_way_data_empty.setVisibility(View.VISIBLE);
            scoring_lay.setVisibility(View.GONE);

            navigate_put_details.setVisibility(View.GONE);
        }else {
            pieChart();
            fair_way_data_empty.setVisibility(View.GONE);
            fair_ways_hit_empty_lay.setVisibility(View.VISIBLE);
            txt_fair_way_title.setVisibility(View.VISIBLE);


            pieChart1();
            gree_in_regular_lay.setVisibility(View.VISIBLE);
            green_way_data_empty.setVisibility(View.GONE);

            pieChart2();
            scorring_way_data_empty.setVisibility(View.GONE);
            scoring_lay.setVisibility(View.VISIBLE);

            BarChart();
            navigate_put_details.setVisibility(View.VISIBLE);
        }

    }

    private void Load_Graph_Section_data1() {
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url + "users/player-stats", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("----response---re(graph)--" + response);
                        try {
                            total_score_values.clear();
                            puts_array_values.clear();
                            puts_max_values.clear();
//                            clear all array else it will add twice thrice
//                             Scorecard Graph
                            JSONObject par_per_object = response.getJSONObject("score_card_details");
                            par_per = par_per_object.getString("par_per");
                            if (par_per.equals(null) || par_per.equals("") || par_per.equals("null") || par_per == null) {
                                par_per = "0";
                            }

                            double center_per_par_per = Double.parseDouble(par_per);
                            flot_par_per = (int) (Math.round(center_per_par_per));

                            birdie_per = par_per_object.getString("birdie_per");
                            if (birdie_per.equals(null) || birdie_per.equals("") || birdie_per.equals("null") || birdie_per == null) {
                                birdie_per = "0";
                            }
                            double center_birdie_per = Double.parseDouble(birdie_per);
                            flot_birdie_per = (int) (Math.round(center_birdie_per));

                            double_boggey_per = par_per_object.getString("double_boggey_per");
                            if (double_boggey_per.equals(null) || double_boggey_per.equals("") || double_boggey_per.equals("null") || double_boggey_per == null) {
                                double_boggey_per = "0";
                            }
                            double center_double_boggey_per = Double.parseDouble(double_boggey_per);
                            flot_double_boggey_per = (int) (Math.round(center_double_boggey_per));

                            eagle_per = par_per_object.getString("eagle_per");
                            if (eagle_per.equals(null) || eagle_per.equals("") || eagle_per.equals("null") || eagle_per == null) {
                                eagle_per = "0";
                            }
                            double center_eagle_per = Double.parseDouble(eagle_per);
                            flot_eagle_per = (int) (Math.round(center_eagle_per));

                            bogey_per = par_per_object.getString("bogey_per");
                            if (bogey_per.equals(null) || bogey_per.equals("") || bogey_per.equals("null") || bogey_per == null) {
                                bogey_per = "0";
                            }
                            double center_bogey_per = Double.parseDouble(bogey_per);
                            float_bogey_per = (int) (Math.round(center_bogey_per));

                            // Fairways Array Graph
                            fairwa_hit = response.getJSONObject("fairway_array");
                            left_per = fairwa_hit.getString("left_per");
                            if (left_per.equals(null) || left_per.equals("") || left_per.equals("null") || left_per == null) {
                                left_per = "0";
                            }

                            center_per = fairwa_hit.getString("center_per");
                            if (center_per.equals(null) || center_per.equals("") || center_per.equals("null") || center_per == null) {
                                center_per = "0";
                            }

                            right_per = fairwa_hit.getString("right_per");
                            if (right_per.equals(null) || right_per.equals("") || right_per.equals("null") || right_per == null) {
                                right_per = "0";
                            }

                            if(left_per.equals("0") && center_per.equals("0") && right_per.equals("0")){
                                System.out.println(left_per+"==========s========="+center_per+"=========s======="+right_per);
                                chart.clear();
                            }

                            double center_per_round_up = Double.parseDouble(center_per);
                            roundOff = (float) (Math.round(center_per_round_up * 10.0) / 10.0);

                            double left_per_round_up = Double.parseDouble(left_per);
                            leftOff = (float) (Math.round(left_per_round_up * 10.0) / 10.0);

                            double right_per_round_up = Double.parseDouble(right_per);
                            rightOff = (float) (Math.round(right_per_round_up * 10.0) / 10.0);

                            // gir array graph
                            JSONObject gir_object = response.getJSONObject("gir_array");

                            miss_per = gir_object.getString("miss_per");
                            if (miss_per.equals(null) || miss_per.equals("") || miss_per.equals("null") || miss_per == null) {
                                miss_per = "0";
                            }

                            hit_per = gir_object.getString("hit_per");
                            if (hit_per.equals(null) || hit_per.equals("") || hit_per.equals("null") || hit_per == null) {
                                hit_per = "0";
                            }

                            if(miss_per.equals("0") &&  hit_per.equals("0")){
                                chart1.clear();
                            }

                            double his_regular = Double.parseDouble(hit_per);
                            hit_regular_value = (float) (Math.round(his_regular * 10.0) / 10.0);

                            double miss_regular = Double.parseDouble(miss_per);
                            miss_regular_values = (float) (Math.round(miss_regular * 10.0) / 10.0);

                            //Puts Graph
                            JSONObject putts_array = response.getJSONObject("putts_array");
                            JSONArray putts_count = putts_array.getJSONArray("putts_count");

                            if (putts_array.length() > 1) {
                                for (int i = 0; i < 2; i++) {
                                    JSONObject puts_object = putts_count.getJSONObject(i);
                                    puts_array_values.add(new BarEntry(i, puts_object.getInt("putts"), getResources()));
                                    puts_max_values.add(puts_object.getInt("putts"));
                                    putts_bottom_valkues.add(puts_object.getString("date"));
                                    putts_colors.add(Color.parseColor("#FF8B38"));
                                }

                            } else {
                                for (int i = 0; i < putts_count.length(); i++) {
                                    JSONObject puts_object = putts_count.getJSONObject(i);
                                    puts_array_values.add(new BarEntry(i, puts_object.getInt("putts"), getResources()));
                                    putts_bottom_valkues.add(puts_object.getString("date"));
                                    putts_colors.add(Color.parseColor("#FF8B38"));
                                    puts_max_values.add(puts_object.getInt("putts"));
                                }

                            }
                        } catch (Exception e) {
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }


    private void method_for_scoring() {

        TextView scoring_title = findViewById(R.id.scoring_title);
        scoring_title.setTypeface(medium);

        TextView score_1 = findViewById(R.id.score_1);
        score_1.setTypeface(regular);
        score_1.setText(flot_eagle_per+"%");

        TextView score_2 = findViewById(R.id.score_2);
        score_2.setTypeface(regular);
        score_2.setText(flot_birdie_per+"%");

        TextView score_3 = findViewById(R.id.score_3);
        score_3.setTypeface(regular);
        score_3.setText(flot_par_per+"%");

        TextView score_4 = findViewById(R.id.score_4);
        score_4.setTypeface(regular);
        score_4.setText(float_bogey_per+"%");

        TextView score_5 = findViewById(R.id.score_5);
        score_5.setTypeface(regular);
        score_5.setText(flot_double_boggey_per+"%");

    }

    // update regular in green ui
    private void method_to_cal_update_regular_in_green() {
        TextView txt_regular_title = findViewById(R.id.txt_regular_title);
        txt_regular_title.setTypeface(semibold);

        TextView txt_regular_hit = findViewById(R.id.txt_regular_hit);
        txt_regular_hit.setTypeface(regular);
        txt_regular_hit.setText(hit_regular_value+"%");
        TextView txt_regular_hit_hint = findViewById(R.id.txt_regular_hit_hint);
        txt_regular_hit_hint.setTypeface(regular);

        TextView txt_reular_miss = findViewById(R.id.txt_reular_miss);
        txt_reular_miss.setTypeface(regular);
        txt_reular_miss.setText(miss_regular_values+"%");
        TextView txt_regular_miss_hint = findViewById(R.id.txt_regular_miss_hint);
        txt_regular_miss_hint.setTypeface(regular);
    }


    // to update fairway hits UI

    private void method_update_firway_hits(){

        TextView txt_fairway_hit = findViewById(R.id.txt_fairway_hit);
        txt_fairway_hit.setTypeface(regular);
        txt_fairway_hit.setText(roundOff+"%");

        TextView hint_hit = findViewById(R.id.hint_hit);
        hint_hit.setTypeface(regular);

        TextView txt_fairway_left = findViewById(R.id.txt_fairway_left);
        txt_fairway_left.setTypeface(regular);
        txt_fairway_left.setText(leftOff+"%");

        TextView hint_left = findViewById(R.id.hint_left);
        hint_left.setTypeface(regular);


        TextView txt_fairway_right = findViewById(R.id.txt_fairway_right);
        txt_fairway_right.setTypeface(regular);
        txt_fairway_right.setText(rightOff+"%");


        TextView hint_right = findViewById(R.id.hint_right);
        hint_right.setTypeface(regular);
    }

    private void pieChart() {

        chart = findViewById(R.id.firway_pie_chart);
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

        chart.setTouchEnabled(false);

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


    private void pieChart1() {

        chart1 = findViewById(R.id.regular_pie_chart);
        chart1.clear();
        chart1.setUsePercentValues(true);
        chart1.getDescription().setEnabled(false);
        chart1.setExtraOffsets(5, 10, 5, 5);
        chart1.setDragDecelerationFrictionCoef(0.95f);
        chart1.setDrawHoleEnabled(true);
        chart1.setHoleColor(WHITE);
        chart1.setTransparentCircleColor(WHITE);
        chart1.setTransparentCircleAlpha(110);
        chart1.setHoleRadius(58f);
        chart1.setTransparentCircleRadius(61f);
        chart1.setDrawCenterText(true);
        chart1.setRotationAngle(0);
        chart1.getLegend().setEnabled(false);
        chart1.setTouchEnabled(false);
        // enable rotation of the chart by touch
        chart1.setRotationEnabled(false);
        chart1.setHighlightPerTapEnabled(true);
//        chart1.setOnChartValueSelectedListener(this);
        Legend l1 = chart1.getLegend();
        l1.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l1.setOrientation(Legend.LegendOrientation.VERTICAL);
        l1.setDrawInside(false);
        l1.setXEntrySpace(7f);
        l1.setYEntrySpace(0f);
        l1.setYOffset(0f);

        // entry label styling
        chart1.setEntryLabelColor(WHITE);

        ArrayList<Integer> regular_green_color = new ArrayList<>();
        ArrayList<PieEntry> entries_regular = new ArrayList<>();
            entries_regular.add(new PieEntry(hit_regular_value));
            entries_regular.add(new PieEntry(miss_regular_values));

            regular_green_color.add(Color.parseColor("#5CB768"));
            regular_green_color.add(Color.parseColor("#8AB6FF"));


            PieDataSet dataSet = new PieDataSet(entries_regular, "");
            dataSet.setDrawIcons(false);
            dataSet.setSliceSpace(3f);
            dataSet.setIconsOffset(new MPPointF(0, 0));
            dataSet.setSelectionShift(10f);

            // add a lot of colors
            dataSet.setColors(regular_green_color);
            PieData data = new PieData(dataSet);
            data.setValueTextSize(0);
            data.setValueTextColor(WHITE);
            chart1.setData(data);

    }

    private void pieChart2() {

        chart3 = findViewById(R.id.scoring_pie_chart_list);
        chart3.clear();
        chart3.setUsePercentValues(true);
        chart3.getDescription().setEnabled(false);
        chart3.setExtraOffsets(5, 10, 5, 5);
        chart3.setDragDecelerationFrictionCoef(0.95f);
        chart3.setDrawHoleEnabled(true);
        chart3.setHoleColor(WHITE);
        chart3.setTransparentCircleColor(WHITE);
        chart3.setTransparentCircleAlpha(110);
        chart3.setHoleRadius(58f);
        chart3.setTransparentCircleRadius(61f);
        chart3.setDrawCenterText(true);
        chart3.setRotationAngle(0);
        chart3.getLegend().setEnabled(false);

        // enable rotation of the chart by touch
        chart3.setRotationEnabled(false);
        chart3.setHighlightPerTapEnabled(true);
        chart3.setTouchEnabled(false);
//        chart1.setOnChartValueSelectedListener(this);
        Legend l1 = chart3.getLegend();
        l1.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l1.setOrientation(Legend.LegendOrientation.VERTICAL);
        l1.setDrawInside(false);
        l1.setXEntrySpace(7f);
        l1.setYEntrySpace(0f);
        l1.setYOffset(0f);

        // entry label styling
        chart3.setEntryLabelColor(WHITE);

        set_Score_Datap();
    }

    private void setDatap() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> fairwa_colors = new ArrayList<>();

        System.out.println(entries.size()+"size ---"+fairwa_colors.size());

            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    fairwa_colors.add(Color.parseColor("#5CB768"));
                    entries.add(new PieEntry(roundOff));
                } else if (i == 1) {
                    fairwa_colors.add(Color.parseColor("#8AB6FF"));
                    if (leftOff < 1) {
                        entries.add(new PieEntry(leftOff, parties[i % parties.length], getResources()));
                    } else {
                        entries.add(new PieEntry(leftOff, parties[i % parties.length], getResources()));
                    }
                } else if (i == 2) {
                    fairwa_colors.add(Color.parseColor("#FF3030"));
                    if (rightOff < 1) {
                        entries.add(new PieEntry(rightOff));
                    } else {
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
    }


    private void set_Score_Datap() {

        ArrayList<PieEntry> scoring_data = new ArrayList<>();
        ArrayList<Integer>  scoring_color = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            if(i==0){
                scoring_color.add(Color.parseColor("#FF8B38"));
                scoring_data.add(new PieEntry(flot_eagle_per));
            }else if(i==1){
                scoring_color.add(Color.parseColor("#FF3030"));
                scoring_data.add(new PieEntry(flot_birdie_per));
            }else if(i==2){
                scoring_color.add(Color.parseColor("#5CB768"));
                scoring_data.add(new PieEntry(flot_par_per));
            }else if(i==3){
                scoring_color.add(Color.parseColor("#8AB6FF"));
                scoring_data.add(new PieEntry(float_bogey_per));
            }else if(i==4){
                scoring_color.add(Color.parseColor("#C400FF"));
                scoring_data.add(new PieEntry(flot_double_boggey_per));
            }
        }

        PieDataSet dataSet = new PieDataSet(scoring_data, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 0));
        dataSet.setSelectionShift(10);
        // add a lot of colors

        dataSet.setColors(scoring_color);
//        dataSet.setColors(Color.GRAY, Color.LTGRAY,Color.RED);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(0);
        chart3.setData(data);
    }


    private void BarChart() {

        chart4 = findViewById(R.id.list_linechart1);
        chart4.clear();
        chart4.getAxisLeft().setDrawLabels(false);
        chart4.getAxisRight().setDrawLabels(false);
        chart4.getXAxis().setDrawLabels(false);
        chart4.getAxisRight().setEnabled(false);

        chart4.getLegend().setEnabled(false);
        chart4.getDescription().setEnabled(false);
        chart4.getDescription().setEnabled(false);
        chart4.setMaxVisibleValueCount(60);
        chart4.setPinchZoom(false);
        chart4.setTouchEnabled(false);

        YAxis leftAxis = chart4.getAxisRight();
        chart4.getAxisRight().setEnabled(false); // hides horizontal grid lines with below line
        leftAxis.setEnabled(false);

        chart4.setDrawBarShadow(false);
        chart4.setDrawGridBackground(false);
        XAxis xAxis = chart4.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        chart4.getAxisLeft().setDrawGridLines(false);
        chart4.animateY(1500);
        xAxis.setAxisLineColor(getResources().getColor(R.color.colorblack));
        leftAxis.setAxisLineColor(getResources().getColor(R.color.colorblack));
        setData();
    }

    private void setData() {

        BarDataSet set1;

        if (chart4.getData() != null && chart4.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart4.getData().getDataSetByIndex(0);
            set1.setValues(puts_array_values);
            chart4.getData().notifyDataChanged();
            chart4.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(puts_array_values, "");

            set1.setDrawIcons(false);

            set1.setColors(RED, GRAY);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(0);
            data.setBarWidth(0.3f);

            chart4.setData(data);
        }
    }

    @Override
    protected void saveToGallery() {

    }

    public void click1(View view) {
        intent =new Intent(this,pieChart1.class);
        startActivity(intent);
    }

    public void click2(View view) {
        intent=new Intent(getApplicationContext(),pieChart2.class);
        startActivity(intent);
    }

    public void click3(View view) {
        intent=new Intent(getApplicationContext(),pieChart3.class);
        startActivity(intent);
    }

    public void click4(View view) {
        intent=new Intent(getApplicationContext(), BarChartActivity.class);
        startActivity(intent);
    }


    private void Show_custome_dailogs_date_picker() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(graphExample.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) graphExample.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    Load_Graph_Section_data(date_list.get(0));
                }else if (date_list.size()==0){
                    Load_Graph_Section_data(start_date);
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


    private void Load_Graph_Section_data(String dates) {

        ProgressDialog.getInstance().showProgress(graphExample.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url + "users/player-stats?start_date="+dates,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("----response---re--" + response);
                        try {
                            total_score_values.clear();
                            puts_array_values.clear();
                            puts_max_values.clear();
//                            clear all array else it will add twice thrice
//                             Scorecard Graph
                            JSONObject par_per_object = response.getJSONObject("score_card_details");
                            par_per = par_per_object.getString("par_per");
                            if (par_per.equals(null) || par_per.equals("") || par_per.equals("null") || par_per==null){
                                par_per="0";
                            }

                            double center_per_par_per = Double.parseDouble(par_per);
                            flot_par_per = (int) (Math.round(center_per_par_per));

                            birdie_per = par_per_object.getString("birdie_per");
                            if (birdie_per.equals(null) || birdie_per.equals("") || birdie_per.equals("null") || birdie_per==null){
                                birdie_per="0";
                            }
                            double center_birdie_per = Double.parseDouble(birdie_per);
                            flot_birdie_per = (int) (Math.round(center_birdie_per ));

                            double_boggey_per = par_per_object.getString("double_boggey_per");
                            if (double_boggey_per.equals(null) || double_boggey_per.equals("") || double_boggey_per.equals("null") || double_boggey_per==null){
                                double_boggey_per="0";
                            }
                            double center_double_boggey_per = Double.parseDouble(double_boggey_per);
                            flot_double_boggey_per = (int) (Math.round(center_double_boggey_per));

                            eagle_per = par_per_object.getString("eagle_per");
                            if (eagle_per.equals(null) || eagle_per.equals("") || eagle_per.equals("null") || eagle_per==null){
                                eagle_per="0";
                            }
                            double center_eagle_per = Double.parseDouble(eagle_per);
                            flot_eagle_per = (int) (Math.round(center_eagle_per));

                            bogey_per = par_per_object.getString("bogey_per");
                            if (bogey_per.equals(null) || bogey_per.equals("") || bogey_per.equals("null") || bogey_per==null){
                                bogey_per="0";
                            }
                            double center_bogey_per = Double.parseDouble(bogey_per);
                            float_bogey_per = (int) (Math.round(center_bogey_per));

                            // Fairways Array Graph
                            fairwa_hit = response.getJSONObject("fairway_array");
                            left_per = fairwa_hit.getString("left_per");
                            if (left_per.equals(null) || left_per.equals("") || left_per.equals("null") || left_per==null){
                                left_per="0";
                            }

                            center_per = fairwa_hit.getString("center_per");
                            if (center_per.equals(null) || center_per.equals("") || center_per.equals("null") || center_per==null){
                                center_per="0";
                            }

                            right_per = fairwa_hit.getString("right_per");
                            if (right_per.equals(null) || right_per.equals("") || right_per.equals("null") || right_per==null){
                                right_per="0";
                            }


                            double center_per_round_up = Double.parseDouble(center_per);
                            roundOff = (float) (Math.round(center_per_round_up * 10.0) / 10.0);

                            double left_per_round_up = Double.parseDouble(left_per);
                            leftOff = (float) (Math.round(left_per_round_up * 10.0) / 10.0);

                            double right_per_round_up = Double.parseDouble(right_per);
                            rightOff = (float) (Math.round(right_per_round_up * 10.0) / 10.0);

                            // gir array graph
                            JSONObject gir_object = response.getJSONObject("gir_array");

                            String miss_per = gir_object.getString("miss_per");
                            if (miss_per.equals(null) || miss_per.equals("") || miss_per.equals("null") || miss_per==null){
                                miss_per="0";
                            }
                            String hit_per = gir_object.getString("hit_per");
                            if (hit_per.equals(null) || hit_per.equals("") || hit_per.equals("null") || hit_per==null){
                                hit_per="0";
                            }

                            double his_regular = Double.parseDouble(hit_per);
                            hit_regular_value = (float) (Math.round(his_regular * 10.0) / 10.0);

                            double miss_regular = Double.parseDouble(miss_per);
                            miss_regular_values = (float) (Math.round(miss_regular * 10.0) / 10.0);

                            //Puts Graph
                            JSONObject putts_array = response.getJSONObject("putts_array");
                            JSONArray putts_count  = putts_array.getJSONArray("putts_count");

                            if (putts_array.length()>1){
                                for(int i=0;i<2;i++){
                                    JSONObject puts_object = putts_count.getJSONObject(i);
                                    puts_array_values.add(new BarEntry(i,puts_object.getInt("putts"),getResources()));
                                    puts_max_values.add(puts_object.getInt("putts"));
                                    putts_bottom_valkues.add(puts_object.getString("date"));
                                    putts_colors.add(Color.parseColor("#FF8B38"));
                                }

                            }else {
                                for(int i=0;i<putts_count.length();i++){
                                    JSONObject puts_object = putts_count.getJSONObject(i);
                                    puts_array_values.add(new BarEntry(i,puts_object.getInt("putts"),getResources()));
                                    putts_bottom_valkues.add(puts_object.getString("date"));
                                    putts_colors.add(Color.parseColor("#FF8B38"));
                                    puts_max_values.add(puts_object.getInt("putts"));
                                }

                            }

                            ProgressDialog.getInstance().hideProgress();

                            method_to_call_all_methods();

                        }catch (Exception e){
                            ProgressDialog.getInstance().hideProgress();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
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

}