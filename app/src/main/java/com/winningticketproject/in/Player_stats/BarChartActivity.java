package com.winningticketproject.in.Player_stats;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;

public class BarChartActivity extends DemoBase {

    com.github.mikephil.charting.charts.BarChart chart;
    ListView list;
    SimpleDateFormat spf;
    String start_date="";
    android.support.v7.app.AlertDialog alertDialog;
    ArrayList<Bar> barArrayList = new ArrayList<>();
    HashMap<Integer,ArrayList<Bar>> putts_main_list = new HashMap<>();
    HashMap<Integer,ArrayList<bar>> putts_sub_list = new HashMap<>();
    ImageView btn_back_puts_score,stats_date_filter_btn;
    ArrayList<String> date_list = new ArrayList<>();

    private ArrayList<String> new_putts_bottom_valkues = new ArrayList<>();
    private ArrayList<Integer> new_putts_colors = new ArrayList<>();
    private ArrayList<BarEntry> new_puts_array_values = new ArrayList<>();
    private ArrayList<Integer> new_puts_max_values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        Date c = Calendar.getInstance().getTime();
        spf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa");
        spf= new SimpleDateFormat("dd-MM-yyyy");
        start_date = spf.format(c);

        method_for_static_data();

        method_to_cal_api(start_date);

        btn_back_puts_score = findViewById(R.id.back_stats_top_buttons);
        btn_back_puts_score.setOnTouchListener(new View.OnTouchListener() {
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

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BarChartActivity.this, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) BarChartActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    method_to_cal_api(date_list.get(0));
                }else if (date_list.size()==0){
                    method_to_cal_api(start_date);
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



    private void method_to_cal_api(String datess) {

        ProgressDialog.getInstance().showProgress(BarChartActivity.this);
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, Splash_screen.url+"users/putts-details?start_date="+datess,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("-----------puts--------" + response);
                        try {

                            ProgressDialog.getInstance().hideProgress();

                            //Puts Graph
                            JSONObject putts_array = response.getJSONObject("putts_array");
                            JSONArray putts_count  = putts_array.getJSONArray("putts_count");

                            new_puts_array_values.clear();
                            new_putts_colors.clear();
                            new_putts_bottom_valkues.clear();
                            new_puts_max_values.clear();

                            if (putts_array.length()>1){
                                for(int i=0;i<2;i++){
                                    JSONObject puts_object = putts_count.getJSONObject(i);
                                    new_puts_array_values.add(new BarEntry(i,puts_object.getInt("putts"),getResources()));
                                    new_puts_max_values.add(puts_object.getInt("putts"));
                                    new_putts_bottom_valkues.add(puts_object.getString("date"));
                                    new_putts_colors.add(Color.parseColor("#FF8B38"));
                                }

                            }else {
                                for(int i=0;i<putts_count.length();i++){
                                    JSONObject puts_object = putts_count.getJSONObject(i);
                                    new_puts_array_values.add(new BarEntry(i,puts_object.getInt("putts"),getResources()));
                                    new_putts_bottom_valkues.add(puts_object.getString("date"));
                                    new_putts_colors.add(Color.parseColor("#FF8B38"));
                                    new_puts_max_values.add(puts_object.getInt("putts"));
                                }

                            }

                            Barchart();

                            barArrayList.clear();
                            putts_main_list.clear();
                            putts_sub_list.clear();

                            JSONArray get_putts_details = response.getJSONArray("get_putts_details");
                            for (int i=0;i<get_putts_details.length();i++){
                                JSONObject  putts_details = get_putts_details.getJSONObject(i);
                                String total_score = putts_details.getString("get_putts_total");
                                String score_date_details = putts_details.getString("score_date_details");
                                String event_name = putts_details.getString("event_name");

                                if(total_score==null || total_score.equals(null) || total_score.equals("null") || total_score.equals("")){
                                    total_score="0";
                                }

                                barArrayList.add(new Bar(total_score, score_date_details, event_name));
                                putts_main_list.put(i, barArrayList);

                                ArrayList<bar> bars = new ArrayList<>();
                                JSONObject get_putts_values = putts_details.getJSONObject("get_putts_values");
                                Iterator<String> keys = get_putts_values.keys();
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    String values_s =get_putts_values.getString(key);
                                    if (values_s.equals("null") || values_s.equals("")){
                                        values_s = "0";
                                    }
                                    bars.add(new bar(values_s, "Hole"+key));
                                 }

                                putts_sub_list.put(i, bars);

                            }

                            BarChartAdapter adapter = new BarChartAdapter(BarChartActivity.this, putts_main_list,putts_sub_list);
                            list = findViewById(R.id.puts_list);
                            list.setAdapter(adapter);
                            list.setFocusable(false);
//                            Share_it.setListViewHeightBasedOnChildren(list);
                            ProgressDialog.getInstance().hideProgress();

                            if(get_putts_details.length()==0){
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
                                Alert_Dailog.showNetworkAlert(BarChartActivity.this);
                            }else {
                                Toast.makeText(BarChartActivity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(BarChartActivity.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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


    private void method_for_static_data() {

        TextView d_fair_title = findViewById(R.id.stats_top_title);
        d_fair_title.setTypeface(medium);
        d_fair_title.setText("Totals Putts Per Round");

        TextView stats_based_title = findViewById(R.id.stats_sub_title);
        stats_based_title.setTypeface(regular);

        TextView puts_your_round = findViewById(R.id.puts_your_round);
        puts_your_round.setTypeface(medium);

    }

    private void Barchart() {

        chart = findViewById(R.id.line_chart);
        chart.clear();
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(100);
        chart.setPinchZoom(false);

        YAxis leftAxis = chart.getAxisRight();

        leftAxis.setLabelCount(6, true);
        chart.getAxisLeft().setAxisMinimum(0);

        if(new_puts_max_values.size()>0){
            chart.getAxisLeft().setAxisMaximum(Collections.max(new_puts_max_values)+10);
        }else {
            chart.getAxisLeft().setAxisMaximum(10);
        }

        leftAxis.setGranularity(1.0f);
        leftAxis.setGranularityEnabled(true); // Required to enable granularity
        leftAxis.setLabelCount(5);
        leftAxis.setEnabled(false);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(false);
        chart.getLegend().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return new_putts_bottom_valkues.get((int) value);
            }

        });

        chart.getAxisLeft().setDrawGridLines(true);
        chart.animateY(2000);
        setData();
    }

    private void setData() {

        BarDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(new_puts_array_values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(new_puts_array_values, "");
            set1.setDrawIcons(false);
            set1.setColors(new_putts_colors);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setBarWidth(0.4f); // set custom bar width
            chart.setData(data);
            data.setValueTextSize(0);
            chart.setFitBars(true); // make the x-axis fit exactly all bars
            chart.invalidate(); // refresh
        }
    }


    @Override
    protected void saveToGallery() {
    }

    public static class Puts_BarChartAdapter extends BaseAdapter {
        ArrayList<bar> hashMap1 = new ArrayList<bar>();
        Activity context;
        TextView text;
        TextView mode1;
        public Puts_BarChartAdapter(Activity context, ArrayList<bar> hashMap1) {
            this.context = context;
            this.hashMap1=hashMap1;
        }

        @Override
        public int getCount() {
            return hashMap1.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View view = inflater.inflate(R.layout.barchart_activity, null, true);

            text = view.findViewById(R.id.model_number);
            mode1=view.findViewById(R.id.mode1);

            text.setTypeface(regular);
            mode1.setTypeface(regular);

            text.setText(hashMap1.get(position).getNum());
            mode1.setText(hashMap1.get(position).getMode());

            return view;
        }
    }

}
