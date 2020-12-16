package com.winningticketproject.in.IndividulaGameType;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
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
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Individual_course_data;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Individul_play_course_list extends AppCompatActivity implements View.OnClickListener {
    ListView course_list;
    ArrayList<Individual_course_data> individual_course_data = new ArrayList<Individual_course_data>();
    View play_store;
    TextView cancel_purchage, account_title,player_hitory;
    String auth_token="";
    ProgressDialog pd;
    int current_page=1;
    int total_count;
    boolean loadingMore=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individul_play_course_list);

        play_store = findViewById(R.id.play_store);

        course_list = findViewById(R.id.individual_play);
        cancel_purchage = play_store.findViewById(R.id.cancel_purchage);
        cancel_purchage.setOnClickListener(this);
        account_title = findViewById(R.id.account_title);
        account_title.setText("Individual Play Score");

        pd = new ProgressDialog(this);
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        player_hitory = findViewById(R.id.player_hitory);
        cancel_purchage.setTypeface(medium);
        account_title.setTypeface(medium);
        player_hitory.setTypeface(regular);

        auth_token = get_auth_token("auth_token");

        if (!isNetworkAvailable()) {
            alertdailogbox();
        }
        else
        {
            try {
                JSONObject parms1 = new JSONObject();
                parms1.put("page",current_page);
                parms1.put("per_page","20");
                method_to_cal_player_history(Splash_screen.url+"users/player-history",parms1);
            }catch (Exception e){

            }
             }

        current_page=1;
        course_list.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!pd.isShowing()) {
                    if (totalItemCount >= 20) {
                        if (firstVisibleItem + visibleItemCount == totalItemCount && !(loadingMore)) {
                            if (total_count == 20) {
                                if (!isNetworkAvailable()) {
                                    Toast.makeText(getApplicationContext(), "You don't have internet connection.", Toast.LENGTH_LONG).show();
                                } else {
                                    try {
                                        JSONObject parms = new JSONObject();
                                        parms.put("page",current_page);
                                        parms.put("per_page","20");
                                        method_to_cal_player_history(Splash_screen.url +"users/player-history",parms);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){

            }
        });


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
                    try {

                        JSONObject parms = new JSONObject();
                        parms.put("page","1");
                        parms.put("per_page","20");
                        method_to_cal_player_history(Splash_screen.url +"users/player-history",parms);
                    }catch (Exception e){

                    }

                }

            }
        });

        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
        dialog.show();
    }


    private void method_to_cal_player_history(String url,JSONObject data) {
        if(!pd.isShowing())
        { pd.show(); }

        System.out.println(data);
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("--res----"+response);
                            if (response.getString("status").equals("Success")) {

                                current_page++;
                                JSONArray history = response.getJSONArray("history");
                                    for (int i = 0; i < history.length(); i++) {
                                        JSONObject object = history.getJSONObject(i);

                                        String name = object.getString("event_name");
                                        String date_value = object.getString("event_date");
                                        String total_par = object.getString("total_par_value");

                                        String total_score = object.getString("total_score");
                                        if (total_score.equals(null) || total_score.equals("null") || total_score.equals("") || total_score == null) {
                                            total_score = "0";
                                        }

                                        String total_net_values = object.getString("total_net_score");
                                        if (total_net_values.equals(null) || total_net_values.equals("null") || total_net_values.equals("") || total_net_values == null) {
                                            total_net_values = "0";
                                        }
                                        String total_grass_values = object.getString("total_gross_score");
                                        if (total_grass_values.equals(null) || total_grass_values.equals("null") || total_grass_values.equals("") || total_grass_values == null) {
                                            total_grass_values = "0";
                                        }

                                        individual_course_data.add(new Individual_course_data(name,total_par,date_value,total_net_values,total_grass_values,total_score));
                                        total_count = individual_course_data.size();
                                    }
                                }
                                pd.dismiss();
                            Individual_play_course allevents_hori = new Individual_play_course(Individul_play_course_list.this,individual_course_data);
                            course_list.setAdapter(allevents_hori);


                            if (individual_course_data.size()>0){
                                course_list.setVisibility(View.VISIBLE);
                                player_hitory.setVisibility(View.GONE);
                            }else {
                                course_list.setVisibility(View.GONE);
                                player_hitory.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            //nothing
                            pd.dismiss();
                            if (individual_course_data.size()>0){
                                course_list.setVisibility(View.VISIBLE);
                                player_hitory.setVisibility(View.GONE);
                            }else {
                                course_list.setVisibility(View.GONE);
                                player_hitory.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode == 401) {
                                Alert_Dailog.showNetworkAlert(Individul_play_course_list.this);
                            } else {
                                Toast.makeText(Individul_play_course_list.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Individul_play_course_list.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
        req.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(req, "tag");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_purchage:
                finish();
                break;
        }

    }



    public class Individual_play_course extends BaseAdapter
    {
        public Context context;
        public ArrayList<Individual_course_data> indivdual_play_data = new ArrayList<>();

        public Individual_play_course(Context context,ArrayList<Individual_course_data> indivdual_play_data) {
            super();
            this.context = context;
            this.indivdual_play_data = indivdual_play_data;
        }


        @Override
        public int getCount() {
            return indivdual_play_data.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public class ViewHolder
        {
            TextView tv_course_names,course_date,course_score,to_par_values,to_par_text;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            // TODO Auto-generated method stub

            final Individual_play_course.ViewHolder holder;
            if(view==null)
            {
                holder = new Individual_play_course.ViewHolder();
                LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view= inflater.inflate(R.layout.custome_course_score_list,null);

                holder.tv_course_names = view.findViewById(R.id.course_name);
                holder.course_date = view.findViewById(R.id.course_date);
                holder.course_score = view.findViewById(R.id.course_score);

                holder.to_par_values = view.findViewById(R.id.to_par_values);
                holder.to_par_text = view.findViewById(R.id.to_par_text);

                holder.tv_course_names.setTypeface(regular);
                holder.course_date.setTypeface(medium);
                holder.course_score.setTypeface(medium);

                holder.to_par_values.setTypeface(medium);
                holder.to_par_text.setTypeface(regular);

                view.setTag(holder);
            }
            else
                holder=(Individual_play_course.ViewHolder)view.getTag();

            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            java.util.Date date = null;
            try
            {
                date = format.parse(indivdual_play_data.get(position).getDate_value());
            }
            catch (ParseException e)
            {//nothing
            }
            try {

                java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("MMM dd, yyyy HH:mm aa");
                String newDateStr = postFormater.format(date);
                holder.course_date.setText(newDateStr.replace("am","AM").replace("pm","PM") +" EST");

            }catch (Exception e){
            }

            holder.tv_course_names.setText(indivdual_play_data.get(position).getName());
            holder.to_par_values.setText(indivdual_play_data.get(position).getTotal_gross());

            if (Integer.parseInt(indivdual_play_data.get(position).getTotal_scores())>0){
                holder.to_par_values.setTextColor(getResources().getColor(R.color.fdfdf));
            }else if (Integer.parseInt(indivdual_play_data.get(position).getTotal_scores())<0){
                holder.to_par_values.setTextColor(getResources().getColor(R.color.colorgreen));
            }else if (Integer.parseInt(indivdual_play_data.get(position).getTotal_scores())==0){
                holder.to_par_values.setTextColor(getResources().getColor(R.color.colorblack));
            }

            holder.course_score.setText("Net Score: "+indivdual_play_data.get(position).getTotal_net()+"/"+indivdual_play_data.get(position).getTotal_par());

            return view;
        }
    }
}
