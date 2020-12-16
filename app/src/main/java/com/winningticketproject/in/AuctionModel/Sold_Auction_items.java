package com.winningticketproject.in.AuctionModel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.squareup.picasso.Picasso;
import com.winningticketproject.in.Activity.Alert_Dailog;
import com.winningticketproject.in.AppInfo.AppController;
import com.winningticketproject.in.AppInfo.Auction_List_Data;
import com.winningticketproject.in.AppInfo.ExpandableGridview;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;
import com.winningticketproject.in.SignInSingup.Splash_screen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.setGridViewHeightBasedOnChildren;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;

public class Sold_Auction_items extends Fragment implements View.OnClickListener{
    View v;
    ArrayList<Auction_List_Data> auction_list_data = new ArrayList<>();
    ListView viewevent_listview;
    int position;
    ProgressDialog pd;
    boolean loadingMore=false;
    TextView Item_Number,Lowest_Price,Highest_Price,Time_End_Show,view_all;
    String auth_token = "", id = "",url,selected_item1;
    SwipeRefreshLayout swipeRefreshLayout;
    int current_page=1;
    int total_page=0;
    private boolean loading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag1_layout, container, false);

        Share_it share_it = new Share_it(getContext());
        auth_token = get_auth_token("auth_token");
        id = get_auth_token("event_id");

        viewevent_listview = v.findViewById(R.id.live_auction);
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        Item_Number = v.findViewById(R.id.Item_Number);
        Lowest_Price = v.findViewById(R.id.Lowest_Price);
        Highest_Price = v.findViewById(R.id.Highest_Price);
        Time_End_Show = v.findViewById(R.id.Time_End_Show);
        Item_Number.setTypeface(Splash_screen.medium);
        Lowest_Price.setTypeface(Splash_screen.medium);
        Highest_Price.setTypeface(Splash_screen.medium);
        Time_End_Show.setTypeface(Splash_screen.medium);

        Item_Number.setOnClickListener(this);
        Lowest_Price.setOnClickListener(this);
        Highest_Price.setOnClickListener(this);
        Time_End_Show.setOnClickListener(this);

        view_all = v.findViewById(R.id.view_all);
        view_all.setTypeface(medium);

        method_to_sold_Auction_api(Splash_screen.imageurl + "api/v2/auction/sold-list/"+id+"?sort_by="+"item_number",2);

        swipeRefreshLayout =  v.findViewById(R.id.swipe_view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                auction_list_data.clear();
                method_to_sold_Auction_api(Splash_screen.imageurl + "api/v2/auction/sold-list/"+id+"?sort_by="+"item_number",2);
                Item_Number.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Lowest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Highest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Time_End_Show.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                pd.hide();
            }
        });

        return v;
    }

    private void method_to_sold_Auction_api(String selected_item , int i ) {
        auction_list_data.clear();
        if (i==1){
            if (!pd.isShowing()) {
                pd.show();
            }
        }
        pd.show();
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, selected_item,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            pd.hide();
                            System.out.println("------response_sold------"+response);
                            JSONArray auction_items = response.getJSONArray("auction_items");

                            System.out.println("-----------view_All---------"+auction_items.length());

                            if (auction_items.length() == 0) {
                                viewevent_listview.setVisibility(View.GONE);
                                swipeRefreshLayout.setVisibility(View.GONE);
                                view_all.setVisibility(View.VISIBLE);
                            }
                            else if (auction_items.length()>0) {

                                view_all.setVisibility(View.GONE);
                                viewevent_listview.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setVisibility(View.VISIBLE);

                                for (int k = 0; k < auction_items.length(); k++) {
                                    JSONObject auction_items_obj = auction_items.getJSONObject(k);

                                    String id = auction_items_obj.getString("id");
                                    String name = auction_items_obj.getString("name");
                                    String starting_bid = auction_items_obj.getString("starting_bid");
                                    String payment_status = auction_items_obj.getString("payment_status");
                                    String buy_price = auction_items_obj.getString("buy_price");
                                    String event_start_date = auction_items_obj.getString("event_start_date");
                                    String event_end_date = auction_items_obj.getString("event_end_date");
                                    String item_image = auction_items_obj.getString("item_image");
                                    String bid_count = auction_items_obj.getString("bid_count");
                                    String event_short_time_zone = auction_items_obj.getString("event_short_time_zone");
                                    String current_bid_amount = auction_items_obj.getString("current_bid_amount");
                                    String is_live = auction_items_obj.getString("is_live?");
                                    String auction_item_number = auction_items_obj.getString("auction_item_number");
                                    String winning_bid_amount = auction_items_obj.getString("winning_bid_amount");
                                    String winning_paid_amount = auction_items_obj.getString("winning_paid_amount");
                                    String is_expired = auction_items_obj.getString("is_expired?");

                                    ArrayList item_tags = new ArrayList();
                                    item_tags.add("#"+auction_item_number);

                                    JSONArray tags = auction_items_obj.getJSONArray("auction_tags_display");
                                    for (int j = 0;j<tags.length();j++){
                                        item_tags.add(tags.get(j));
                                    }

                                    if (i==2){
                                        swipeRefreshLayout.setRefreshing(false);
                                    }else {
                                        if (pd.isShowing()){
                                            pd.dismiss();}
                                    }

                                    auction_list_data.add(new Auction_List_Data(id, name, event_start_date, event_end_date, starting_bid,
                                            item_image, payment_status, bid_count, current_bid_amount, is_live, is_expired, buy_price,
                                            auction_item_number, winning_bid_amount, winning_paid_amount, event_short_time_zone,item_tags));

                                    Liveauctionq allevents = new Liveauctionq(getContext(),auction_list_data);
                                    viewevent_listview.setAdapter(allevents);
                                    viewevent_listview.setSelectionFromTop(position, 0);
                                    allevents.notifyDataSetChanged();
                                    loadingMore = false;
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

                                }
                            }
                            JSONObject obj = response.getJSONObject("meta");
                            int current_page = obj.getInt("current_page");
                            int next_page = obj.getInt("next_page");
                            int prev_page = obj.getInt("prev_page");
                            int total_pages = obj.getInt("total_pages");
                            int total_count = obj.getInt("total_count");

//                            System.out.println("------current-------"+current_page+"------------"+total_pages+"--------"+total_count);
//
//                            viewevent_listview.setOnScrollListener(new AbsListView.OnScrollListener(){
//                                @Override
//                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
//                                {
//                                    if(!pd.isShowing()) {
//                                        if (totalItemCount >= 20) {
//                                            if (firstVisibleItem + visibleItemCount == totalItemCount && !(loadingMore)) {
//                                                if (current_page <= total_pages) {
//                                                    if (!isNetworkAvailable()) {
//                                                        Toast.makeText(getActivity(), "You don't have internet connection.", Toast.LENGTH_LONG).show();
//                                                    } else {
//                                                        try {
//                                                            loadingMore = true;
//                                                            auction_list_data.clear();
//                                                            System.out.println("---------------------"+selected_item1);
//                                                            method_to_sold_Auction_api(Splash_screen.imageurl + "api/v2/auction/sold-list/"+id+"/?page="+current_page+ "?sort_by=" +selected_item1,2);
//                                                        } catch (Exception e) {
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onScrollStateChanged(AbsListView view, int scrollState){
//
//                                }
//                            });


                        }catch (Exception e){
                            //nothing
                            if (i==2){
                                swipeRefreshLayout.setRefreshing(false);
                            }else {
                                if (pd.isShowing()){
                                    pd.dismiss();}
                            }
                            swipeRefreshLayout.setVisibility(View.GONE);
                            viewevent_listview.setVisibility(View.GONE);
                            view_all.setVisibility(View.VISIBLE);
                        }
                        if (i==2){
                            swipeRefreshLayout.setRefreshing(false);
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (i==2){
                            swipeRefreshLayout.setRefreshing(false);
                        }else {
                            if (pd.isShowing()){
                                pd.dismiss();}
                        }
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized
                            if (error.networkResponse.statusCode==401){
                                Alert_Dailog.showNetworkAlert(getContext());
                            }
                        }else {
                            Toast.makeText(getContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
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
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);myRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Item_Number:
                Item_Number.setBackground(getResources().getDrawable(R.drawable.bg_hightlight_btn));
                pd.show();
                auction_list_data.clear();
                url = Splash_screen.imageurl +"api/v2/auction/sold-list/"+id;
                selected_item1 = "item_number";
                method_to_sold_Auction_api(url+"?sort_by="+selected_item1,1);
                Lowest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Highest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Time_End_Show.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                break;

            case  R.id.Lowest_Price:
                Lowest_Price.setBackground(getResources().getDrawable(R.drawable.bg_hightlight_btn));
                pd.show();
                url = Splash_screen.imageurl +"api/v2/auction/sold-list/"+id;
                auction_list_data.clear();
                selected_item1 = "lowest_bid";
                method_to_sold_Auction_api(url+"?sort_by="+selected_item1,1);
                Item_Number.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Highest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Time_End_Show.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                break;

            case  R.id.Highest_Price:
                Highest_Price.setBackground(getResources().getDrawable(R.drawable.bg_hightlight_btn));
                pd.show();
                auction_list_data.clear();
                url = Splash_screen.imageurl +"api/v2/auction/sold-list/"+id;
                selected_item1 = "highest_bid";
                method_to_sold_Auction_api(url+"?sort_by="+selected_item1,1);
                Lowest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Item_Number.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Time_End_Show.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                break;

            case  R.id.Time_End_Show:
                Time_End_Show.setBackground(getResources().getDrawable(R.drawable.bg_hightlight_btn));
                pd.show();
                auction_list_data.clear();
                url = Splash_screen.imageurl +"api/v2/auction/sold-list/"+id;
                selected_item1 = "end_time";
                method_to_sold_Auction_api(url+"?sort_by="+selected_item1,1);
                auction_list_data.clear();
                Lowest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Highest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Item_Number.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                break;

        }
    }


    static class Liveauctionq  extends BaseAdapter {
        public LayoutInflater inflater;
        public Context context;
        String date1;
        ArrayList<Auction_List_Data> auction_list_data1 = new ArrayList<>();
        Liveauctionq(Context context, ArrayList<Auction_List_Data> auction_list_data1) {
            super();
            this.context = context;
            this.auction_list_data1=auction_list_data1;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return auction_list_data1.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public class ViewHolder
        {
            TextView live_auction_title,live_auction_amount,item_number,tv_item_date_time,live_auction_amount1;
            ImageView live_auction_image;
            LinearLayout coordinatorLayout;
            RecyclerView gv_tag_category ;
            Typeface semibold = Typeface.createFromAsset(context.getAssets(), "Montserrat-SemiBold.ttf");
            Typeface regular = Typeface.createFromAsset(context.getAssets(), "Montserrat-Regular.ttf");
            Typeface medium = Typeface.createFromAsset(context.getAssets(),"Montserrat-Medium.ttf");

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView==null)
            {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.fragment_sold_auction, null);

                holder.live_auction_image = convertView.findViewById(R.id.live_auction_image);
                holder.tv_item_date_time = convertView.findViewById(R.id.tv_item_date_time);
                holder.live_auction_amount1 = convertView.findViewById(R.id.live_auction_amount1);
                holder.live_auction_title = convertView.findViewById(R.id.live_auction_title);
                holder.coordinatorLayout = convertView.findViewById(R.id.coordinatorLayout);
                holder.item_number = convertView.findViewById(R.id.tv_item_number);
                holder.live_auction_amount = convertView.findViewById(R.id.live_auction_amount);

                holder.gv_tag_category = convertView.findViewById(R.id.gv_tag_category);

                convertView.setTag(holder);
            }
            else
                holder=(ViewHolder)convertView.getTag();

            holder.item_number.setText("#"+auction_list_data1.get(position).getAuction_item_number());

            holder.tv_item_date_time.setTypeface(holder.regular);
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            java.util.Date date = null;
            try
            {
                date = format.parse(auction_list_data1.get(position).getAuctionenddate());
            }
            catch (ParseException e) {
            }

            java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("MM-dd-yyyy   hh:mm aa");
            String newDateStr = postFormater.format(date);
            holder.tv_item_date_time.setText(""+newDateStr.replace("am","AM").replace("pm","PM")+" "+auction_list_data1.get(position).getEvent_short_time_zone());
            date1 = newDateStr.replace("am","AM").replace("pm","PM")+" "+auction_list_data1.get(position).getEvent_short_time_zone();

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(context,NewLive_AuctionItem.class);
                    Share_it.savestring("Auction_id",auction_list_data1.get(position).getAuctionid());
                    in.putExtra("date",date1);
                    context.startActivity(in);
                }
            });

            holder.live_auction_amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(context,NewLive_AuctionItem.class);
                    Share_it.savestring("Auction_id",auction_list_data1.get(position).getAuctionid());
                    in.putExtra("date",date1);
                    context.startActivity(in);
                }
            });


            holder.live_auction_title.setTypeface(holder.semibold);
            holder.live_auction_amount.setTypeface(holder.regular);
            holder.item_number.setTypeface(holder.medium);
            holder.live_auction_amount1.setTypeface(holder.regular);


            String auction_title = auction_list_data1.get(position).getAuctiontitle();
            String upperString = auction_title.substring(0, 1).toUpperCase() + auction_title.substring(1).toLowerCase();
            holder.live_auction_title.setText(upperString);

                Picasso.with(context)
                        .load(auction_list_data1.get(position).getAuctionimage()).error(R.drawable.auto_image).placeholder(R.drawable.auction_event_empty)
                        .into(holder.live_auction_image);

            String  buyamount = auction_list_data1.get(position).getWinning_paid_amount();

            if (buyamount.equals("null")) {
                holder.live_auction_amount1.setVisibility(View.VISIBLE);
                holder.live_auction_amount1.setText("Auction Closed");
                holder.live_auction_amount.setVisibility(View.GONE);
            }else {

                holder.live_auction_amount1.setVisibility(View.GONE);
                holder.live_auction_amount.setVisibility(View.VISIBLE);
                buyamount = buyamount.substring(0, buyamount.length() - 2);

                String s = String.format("%,d", Long.parseLong(buyamount));

                String buy_amount_values = "<font color='#ffffff'><b><big>$" + s + ".00</big></b>" + "<small> Sold</small></font>";
                holder.live_auction_amount.setText( Html.fromHtml(buy_amount_values));
            }

            ArrayList<String> tag_data = auction_list_data1.get(position).getTag_list();

            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.FLEX_START);
            holder.gv_tag_category.setLayoutManager(layoutManager);
            FlexboxAdapter adapter = new FlexboxAdapter(context, tag_data);
            holder.gv_tag_category.setAdapter(adapter);

            return convertView;
        }

    }

}
