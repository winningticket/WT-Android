package com.winningticketproject.in.AuctionModel;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.EventTab.ListViewForEmbeddingInScrollView;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;

public class Bidding_History extends AppCompatActivity implements View.OnClickListener {
    ListViewForEmbeddingInScrollView bidding_listview;
    TextView cancel_purchage,toolbar_title;
    Typeface italic;
    ProgressDialog pd;
    ArrayList<Bidding_histiry_data> bidding_histiry_data = new ArrayList<Bidding_histiry_data>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding__history);

        cancel_purchage = findViewById(R.id.cancel_purchage);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Bidding History");
        cancel_purchage.setOnClickListener(this);

        pd = new ProgressDialog(Bidding_History.this);
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        bidding_listview = findViewById(R.id.bidding_listview);

        cancel_purchage.setTypeface(Splash_screen.regular);
        toolbar_title.setTypeface(Splash_screen.regular);
        bidding_history_api(Splash_screen.url + "auction/bidding_history/" + getIntent().getStringExtra("id"));

    }

    private void bidding_history_api(String url) {
        if (!pd.isShowing()){ pd.show();}
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {

                    System.out.println("-------------"+response);

                    String winning_bid_amount = response.getString("winning_bid_amount");
                    boolean winner_statuss = response.getBoolean("winner_status");

                    if (winner_statuss){
                        bidding_histiry_data.add(new Bidding_histiry_data("",winning_bid_amount,"","Winning Bid","","","true"));
                    }

                    JSONArray biddings=response.getJSONArray("biddings");
                    for (int i=0;i<biddings.length();i++) {
                        String bidder_name_values="";
                        JSONObject bidding_object = biddings.getJSONObject(i);

                        String user_id = bidding_object.getString("user_id");
                        String current_user_id = response.getString("current_user_id");
                        String current_bid = bidding_object.getString("current_bid");
                        String created_at = bidding_object.getString("created_at");
                        String bidder_name = bidding_object.getString("bidder_name").trim();
                        String winner_status = response.getString("winner_status");
                        String status = response.getString("status");

                        String starcarecter = "";
                        for (int j = 0; j < bidder_name.length() - 2; j++) {
                            starcarecter += "*";
                        }

                        if (user_id.equals(current_user_id)) {
                            bidder_name_values = "Your bid";
                        } else {
                            bidder_name_values = bidder_name.charAt(0) + starcarecter + bidder_name.charAt(bidder_name.length() - 1);
                        }

                        System.out.println("----------------"+bidder_name_values);

                        java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("MMMM dd, yyyy");
                        java.text.SimpleDateFormat postFormater2 = new java.text.SimpleDateFormat("HH:mm aa");
                        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        java.util.Date date = null;
                        try
                        {
                            date = format.parse(created_at);
                        }
                        catch (ParseException e)
                        {
                            //nothing
                        }

                        String dates=postFormater.format(date) +" - " +postFormater2.format(date)+" EST";
                        String created_date = dates.replace("am","AM").replace("pm","PM");

                        bidding_histiry_data.add(new Bidding_histiry_data(user_id,current_bid,created_date,bidder_name_values,bidder_name_values,"",status));

                    }

                    String starting_bid=response.getString("starting_bid");
                    bidding_histiry_data.add(new Bidding_histiry_data("",starting_bid,"","Starting Bid","","",""));

                    if (pd.isShowing()){ pd.dismiss();}

                    Bidding_history bidevent=new Bidding_history(Bidding_History.this,bidding_histiry_data);
                    bidding_listview.setAdapter(bidevent);

                } catch (Exception e) {
                    //nothing

                }
                if (pd.isShowing()){
                    pd.dismiss();}


            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (pd.isShowing()){ pd.dismiss();}
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    if (error.networkResponse.statusCode==401){
                        Alert_Dailog.showNetworkAlert(Bidding_History.this);
                    }else {
                        Toast.makeText(Bidding_History.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Bidding_History.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjReq, "");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_purchage:
                finish();
                break;
        }
    }

    public class Bidding_history extends BaseAdapter
    {
        public ArrayList<Bidding_histiry_data> bidding_histiry_data = new ArrayList<>();
        public LayoutInflater inflater;

        public Bidding_history(Context context, ArrayList<Bidding_histiry_data> bidding_histiry_data) {
            super();
             this.bidding_histiry_data = bidding_histiry_data;
             this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return bidding_histiry_data.size();
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
            TextView amount_text,bidding_name,bidding_date,bidding_type;
            double bidding_amount=0;
            String amount;
            LinearLayout linear_layout;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            final ViewHolder holder;
            if(convertView==null)
            {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.custome_bidding_history, null);


                holder.amount_text = convertView.findViewById(R.id.amount_text);

                holder.bidding_name = convertView.findViewById(R.id.bidding_name);
                holder.bidding_date = convertView.findViewById(R.id.bidding_date);
                holder.bidding_type = convertView.findViewById(R.id.bidding_type);
                holder.linear_layout = convertView.findViewById(R.id.linear_layout);

                convertView.setTag(holder);
            }
            else
                holder=(ViewHolder)convertView.getTag();

            String Bidding = bidding_histiry_data.get(position).bidding_bidder_name;

            holder.bidding_name.setTypeface(Splash_screen.regular);
            holder.bidding_date.setTypeface(Splash_screen.regular);
            holder.bidding_type.setTypeface(Splash_screen.regular);
            holder.amount_text.setTypeface(Splash_screen.regular);

            holder.bidding_name.setText(Bidding);

            holder.bidding_date.setText(bidding_histiry_data.get(position).bidding_created_bid);

            holder.bidding_amount = Double.parseDouble(bidding_histiry_data.get(position).bidding_current_bid);
            DecimalFormat df = new DecimalFormat("0.00");
            holder.amount = df.format(holder.bidding_amount);

            holder.linear_layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorwhite));

            if (bidding_histiry_data.get(position).bidding_winner_status.equals("Winning Bid")){
                holder.bidding_type.setVisibility(View.VISIBLE);
                holder.bidding_type.setText(bidding_histiry_data.get(position).bidding_winner_status);
                holder.amount_text.setTextColor(getResources().getColor(R.color.colorgreen));
            }else {
                holder.bidding_type.setVisibility(View.GONE);
                holder.amount_text.setTextColor(getResources().getColor(R.color.colorblack));
            }

            if (Bidding.equals("Your bid")){
                holder.bidding_name.setTextColor(getResources().getColor(R.color.colorblack));
            }else if (Bidding.equals("Starting Bid")){
                holder.bidding_name.setTypeface(Splash_screen.italic);
                holder.bidding_name.setTextColor(getResources().getColor(R.color.colorblack));
                holder.amount_text.setTextColor(getResources().getColor(R.color.colorblack));
            }else {
                holder.bidding_name.setTextColor(getResources().getColor(R.color.colorblack));
            }


            if (bidding_histiry_data.get(position).bidding_winner_status.equals("")){
                holder.amount_text.setTextColor(getResources().getColor(R.color.colorblack));
            }

            if (bidding_histiry_data.get(position).bidding_winner_status.equals("") && Bidding.equals("Starting Bid")){


                holder.amount_text.setTextColor(getResources().getColor(R.color.colorlightkgray));
            }

            holder.amount_text.setText("$"+ holder.amount);
            return convertView;
        }
    }


}