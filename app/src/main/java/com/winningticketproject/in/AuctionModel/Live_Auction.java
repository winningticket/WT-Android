package com.winningticketproject.in.AuctionModel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.winningticketproject.in.SignInSingup.Login_Screen;
import com.winningticketproject.in.SignInSingup.Splash_screen;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Auction_List_Data;
import com.winningticketproject.in.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.winningticketproject.in.AppInfo.Share_it;
import cz.msebera.android.httpclient.HttpStatus;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;

public class Live_Auction extends AppCompatActivity implements View.OnClickListener {
    TextView cancel_purchage,toolbar_title,view_all;
    ListView viewevent_listview;
    ProgressDialog pd;
    String auth_token="",id="";
    ArrayList<Auction_List_Data> auction_list_data = new ArrayList<>();
    JSONObject eventobject;
    int current_page=1;
    int total_page=0;
    int position;
    boolean loadingMore=false;
    String selected_item="";
    Typeface regular;
    Typeface medium;
    String from="";
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live__auction);

        Share_it share_it = new Share_it(this);

        regular = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
        medium = Typeface.createFromAsset(getAssets(),"Montserrat-Medium.ttf");

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        pd = new ProgressDialog(Live_Auction.this);
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        cancel_purchage = findViewById(R.id.cancel_purchage);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Silent Auction");
        cancel_purchage.setOnClickListener(this);

        auth_token = get_auth_token("auth_token");
        if( auth_token == null ){
            Intent in = new Intent(getApplicationContext(), Splash_screen.class);
            startActivity(in);
            finish();
        }

        cancel_purchage.setTypeface(medium);
        toolbar_title.setTypeface(medium);

        viewevent_listview = findViewById(R.id.live_auction);

        view_all = findViewById(R.id.view_all);
        view_all.setTypeface(medium);


        LinearLayout backtouch = findViewById(R.id.backtouch);
        backtouch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (from.equals("app")) {
                    finish();
                } else {
                    Intent in = new Intent(getApplicationContext(), Splash_screen.class);
                    startActivity(in);
                    finish();
                }
                return false;
            }
        });


        swipeRefreshLayout =  findViewById(R.id.swipe_view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Define your work here .
                swipeRefreshLayout.setRefreshing(true);
                current_page=1;
                auction_list_data.clear();
                methodforLiveauction(Splash_screen.url + "auction/list/"+id+"?sort_by="+selected_item,2);

            }
        });

        handleIntent(getIntent());

        viewevent_listview.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!pd.isShowing()) {

                    if (totalItemCount >= 20) {
                        if (firstVisibleItem + visibleItemCount == totalItemCount && !(loadingMore)) {
                            if (current_page <= total_page) {
                                if (!isNetworkAvailable()) {
                                    Toast.makeText(getApplicationContext(), "You don't have internet connection.", Toast.LENGTH_LONG).show();
                                } else {
                                    try {

                                        methodforLiveauction(Splash_screen.url + "auction/list/"+id+"/?page="+ current_page+"?sort_by="+selected_item,1);

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


    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        current_page=1;
        auction_list_data.clear();
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            from = "link";
            String recipeId = appLinkData.getLastPathSegment();
            Share_it.savestring ("Auction_id", recipeId);
            id = get_auth_token("Auction_id");
            load_filter_sort();
        }else {
            if (!isNetworkAvailable()) {
                Toast.makeText(this,"Internet not available",Toast.LENGTH_LONG).show();
            } else {
                id = get_auth_token("id");
                from = "app";
                current_page=1;
                auction_list_data.clear();
                load_filter_sort();

            }
        }
    }

    @Override
    public void onBackPressed() {
        if (from.equals("app")) {
            finish();
        } else {
            Intent in = new Intent(getApplicationContext(), Splash_screen.class);
            startActivity(in);
            finish();
        }
    }

    private void load_filter_sort() {

        TextView sort_by_text = findViewById(R.id.sort_by_text);
        sort_by_text.setTypeface(medium);

        final String[] sort_by = {"Item Number","Lowest Price","Highest Price","Time: ending soonest"};

        Spinner spinner_sort_by = findViewById(R.id.auction_filter_by);

        ArrayAdapter<String> adapter   = new ArrayAdapter<String>(Live_Auction.this,R.layout.custom_country_text, sort_by) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(regular);
                ((TextView) v).setTextColor(getResources().getColorStateList(R.color.colorblack));
                return v;
            }
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(regular);
                ((TextView) v).setTextColor(Color.BLACK);
                return v;
            }
        };
        adapter.setDropDownViewResource( R.layout.custom_country_text);
        spinner_sort_by.setAdapter(adapter);

        spinner_sort_by.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                current_page=1;
                auction_list_data.clear();
                String url = Splash_screen.url + "auction/list/"+id;
                try {
                    if (i==0){
                        selected_item = "item_number";
                    }else if (i==1){
                        selected_item = "lowest_bid";
                    }else if (i==2){
                        selected_item ="highest_bid";
                    }else {
                        selected_item ="end_time";
                    }
                    methodforLiveauction(url+"?sort_by="+selected_item,1);
                }catch (Exception e){
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void hiddenInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void methodforLiveauction(String selection_type,int type) {
        current_page++;
        position=auction_list_data.size();

       if (type==1){
           if (!pd.isShowing()) {
               pd.show();
           }
       }

        System.out.println("------res----"+selection_type);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, selection_type, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {

                System.out.println("------res----"+response);
                try {
                    JSONArray auction_items = response.getJSONArray("auction_items");
                    for (int i=0;i<auction_items.length();i++) {
                        eventobject = auction_items.getJSONObject(i);
                        String name=eventobject.getString("name");
                        if(name.equals("") || name.equals("null") || name.equals(null)){
                            name = "Not Mentioned";
                        }else {
                            name  = name.substring(0,1).toUpperCase() +name.substring(1);
                        }
                        ArrayList item_tags = new ArrayList();


                        JSONObject metaobject = response.getJSONObject("meta");
                        total_page = metaobject.getInt("total_pages");

                        auction_list_data.add(
                                new Auction_List_Data(eventobject.getString("id"),
                                        name,
                                        eventobject.getString("event_start_date"),
                                        eventobject.getString("event_end_date"),
                                        eventobject.getString("starting_bid"),
                                        eventobject.getString("item_image"),
                                        eventobject.getString("payment_status"),
                                        eventobject.getString("bid_count"),
                                        eventobject.getString("current_bid_amount"),
                                        String.valueOf(eventobject.getBoolean("is_live?")),
                                        String.valueOf(eventobject.getBoolean("is_expired?")),
                                        eventobject.getInt("buy_price")+"",
                                        eventobject.getString("auction_item_number"),
                                        eventobject.getString("winning_bid_amount"),
                                        eventobject.getString("winning_paid_amount"),"",item_tags));
                    }


                    if (type==2){
                        swipeRefreshLayout.setRefreshing(false);
                    }else {
                        if (pd.isShowing()){
                            pd.dismiss();}
                    }


                    Liveauction allevents = new Liveauction(Live_Auction.this,auction_list_data);
                    viewevent_listview.setAdapter(allevents);
                    viewevent_listview.setSelectionFromTop(position, 0);
                    loadingMore = false;
                    allevents.notifyDataSetChanged();
                    if (auction_list_data.isEmpty()) {
                        viewevent_listview.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        view_all.setVisibility(View.VISIBLE);
                    }
                    else {
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        viewevent_listview.setVisibility(View.VISIBLE);
                        view_all.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                    //nothing

                    if (type==2){
                        swipeRefreshLayout.setRefreshing(false);
                    }else {
                        if (pd.isShowing()){
                            pd.dismiss();}
                    }
                    swipeRefreshLayout.setVisibility(View.GONE);
                    viewevent_listview.setVisibility(View.GONE);
                    view_all.setVisibility(View.VISIBLE);

                }
                if (type==2){
                    swipeRefreshLayout.setRefreshing(false);
                }else {
                    if (pd.isShowing()){
                        pd.dismiss();}
                }

            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (type==2){
                    swipeRefreshLayout.setRefreshing(false);
                }else {
                    if (pd.isShowing()){
                        pd.dismiss();}
                }

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    if (error.networkResponse.statusCode==401){
                        Alert_Dailog.showNetworkAlert(Live_Auction.this);
                    }else {
                        Toast.makeText(Live_Auction.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Live_Auction.this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
//                    Alert_Dailog.showNetworkAlert1(Live_Auction.this);
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


    @Override
    protected void onRestart(){
        super.onRestart();//call to restart after onStop
        current_page=1;
        auction_list_data.clear();
        handleIntent(getIntent());
    }

}
