package com.winningticketproject.in.AuctionModel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.Switch;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.HttpStatus;

import static com.winningticketproject.in.AppInfo.Share_it.ConvertDate;
import static com.winningticketproject.in.AppInfo.Share_it.getCurrentDate;
import static com.winningticketproject.in.AppInfo.Share_it.get_auth_token;
import static com.winningticketproject.in.AppInfo.Share_it.isNetworkAvailable;
import static com.winningticketproject.in.AppInfo.Share_it.setGridViewHeightBasedOnChildren;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;

public class Closed_Auction_items extends Fragment implements View.OnClickListener {

    View v;
    ArrayList<Auction_List_Data> auction_list_data = new ArrayList<>();
    ListView viewevent_listview;
    int position;
    boolean loadingMore=false;
    ProgressDialog pd;
    TextView Item_Number,Lowest_Price,Highest_Price,Time_End_Show,view_all;
    String  auth_token= "", id = "", selected_item="",url;
    SwipeRefreshLayout swipeRefreshLayout;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag1_layout, container, false);

        Share_it share_it = new Share_it(getContext());

        viewevent_listview = v.findViewById(R.id.live_auction);
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);

        auth_token = get_auth_token("auth_token");
        id = get_auth_token("event_id");

        Item_Number = v.findViewById(R.id.Item_Number);
        Lowest_Price = v.findViewById(R.id.Lowest_Price);
        Highest_Price = v.findViewById(R.id.Highest_Price);
        Time_End_Show = v.findViewById(R.id.Time_End_Show);
        Item_Number.setTypeface(medium);
        Lowest_Price.setTypeface(medium);
        Highest_Price.setTypeface(medium);
        Time_End_Show.setTypeface(medium);

        Item_Number.setOnClickListener(this);
        Lowest_Price.setOnClickListener(this);
        Highest_Price.setOnClickListener(this);
        Time_End_Show.setOnClickListener(this);

        view_all = v.findViewById(R.id.view_all);
        view_all.setTypeface(medium);

        method_to_upcomming_Auction_api(Splash_screen.imageurl + "api/v2/auction/upcoming-list/"+id+"?sort_by="+"item_number",2);

        swipeRefreshLayout =  v.findViewById(R.id.swipe_view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Define your work here .
                swipeRefreshLayout.setRefreshing(true);
                auction_list_data.clear();
                method_to_upcomming_Auction_api(Splash_screen.imageurl + "api/v2/auction/upcoming-list/"+id+"?sort_by="+"item_number",2);
                Item_Number.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Lowest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Highest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Time_End_Show.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                pd.hide();
            }
        });

        return v;
    }

    private void method_to_upcomming_Auction_api(String selected_item_type , int i ) {
        if (i==1){
            if (!pd.isShowing()) {
                pd.show();
            }
        }
        auction_list_data.clear();
        pd.show();
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, selected_item_type,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            pd.hide();
                            System.out.println("------response_upcoming------"+response);
                            JSONArray auction_items = response.getJSONArray("auction_items");

                            if (auction_items.length() == 0) {

                                viewevent_listview.setVisibility(View.GONE);
                                swipeRefreshLayout.setVisibility(View.GONE);
                                view_all.setVisibility(View.VISIBLE);

                            } else if (auction_items.length()>0) {

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


                                    auction_list_data.add(new Auction_List_Data(id,name,event_start_date,event_end_date,starting_bid,
                                            item_image,payment_status,bid_count,current_bid_amount,is_live,is_expired,buy_price,
                                            auction_item_number,winning_bid_amount,winning_paid_amount,event_short_time_zone,item_tags));

                                    Liveauction2 allevents = new Liveauction2(getContext(),auction_list_data);
                                    viewevent_listview.setAdapter(allevents);
                                    viewevent_listview.setSelectionFromTop(position, 0);
                                    allevents.notifyDataSetChanged();
                                    loadingMore = false;

                                }
                            }
                            JSONObject obj = response.getJSONObject("meta");
                            int current_page = obj.getInt("current_page");
                            int next_page = obj.getInt("next_page");
                            int prev_page = obj.getInt("prev_page");
                            int total_pages = obj.getInt("total_pages");
                            int total_count = obj.getInt("total_count");


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
//                                                            method_to_upcomming_Auction_api(Splash_screen.imageurl + "api/v2/auction/upcoming-list/" + id + "/?page=" + current_page + "?sort_by=" +selected_item, 1);
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
//                        ProgressDialog.getInstance().hideProgress();
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
                url = Splash_screen.imageurl+"api/v2/auction/upcoming-list/"+id;
                selected_item = "item_number";
                method_to_upcomming_Auction_api(url+"?sort_by="+selected_item,1);
                Lowest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Highest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Time_End_Show.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                break;

            case  R.id.Lowest_Price:
                Lowest_Price.setBackground(getResources().getDrawable(R.drawable.bg_hightlight_btn));
                pd.show();
                auction_list_data.clear();
                url = Splash_screen.imageurl+"api/v2/auction/upcoming-list/"+id;
                selected_item = "lowest_bid";
                method_to_upcomming_Auction_api(url+"?sort_by="+selected_item,1);
                Item_Number.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Highest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Time_End_Show.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                break;

            case  R.id.Highest_Price:
                Highest_Price.setBackground(getResources().getDrawable(R.drawable.bg_hightlight_btn));
                pd.show();
                auction_list_data.clear();
                url = Splash_screen.imageurl+"api/v2/auction/upcoming-list/"+id;
                selected_item = "highest_bid";
                method_to_upcomming_Auction_api(url+"?sort_by="+selected_item,1);
                Lowest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Item_Number.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Time_End_Show.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                break;

            case  R.id.Time_End_Show:
                Time_End_Show.setBackground(getResources().getDrawable(R.drawable.bg_hightlight_btn));
                pd.show();
                auction_list_data.clear();
                url = Splash_screen.imageurl+"api/v2/auction/upcoming-list/"+id;
                selected_item = "end_time";
                method_to_upcomming_Auction_api(url+"?sort_by="+selected_item,1);
                Lowest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Highest_Price.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                Item_Number.setBackground(getResources().getDrawable(R.drawable.background_dialog));
                break;

        }
    }


    static class Liveauction2  extends BaseAdapter {
        public LayoutInflater inflater;
        public Context context;
        String currentbid,date1;
        public long millisUntilFinished = 4000;

        ArrayList<Auction_List_Data> auction_list_data = new ArrayList<>();
        Liveauction2(Context context, ArrayList<Auction_List_Data> auction_list_data) {
            super();
            this.context = context;
            this.auction_list_data=auction_list_data;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return auction_list_data.size();
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
            TextView live_auction_title,live_auction_buy_amount,item_number,tv_item_date_time,live_auction_bid_amount;
            ImageView live_auction_image;
            LinearLayout coordinatorLayout;
            RecyclerView gv_tag_category;
            Typeface semibold = Typeface.createFromAsset(context.getAssets(), "Montserrat-SemiBold.ttf");
            Typeface regular = Typeface.createFromAsset(context.getAssets(), "Montserrat-Regular.ttf");
            Typeface medium = Typeface.createFromAsset(context.getAssets(),"Montserrat-Medium.ttf");

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            if(convertView==null)
            {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.fragment_upcoming_auction, null);

                holder.live_auction_image = convertView.findViewById(R.id.live_auction_image);
                holder.tv_item_date_time = convertView.findViewById(R.id.tv_item_date_time);
                holder.live_auction_title = convertView.findViewById(R.id.live_auction_title);
                holder.coordinatorLayout = convertView.findViewById(R.id.coordinatorLayout);
                holder.item_number = convertView.findViewById(R.id.tv_item_number);
                holder.live_auction_buy_amount = convertView.findViewById(R.id.live_auction_buy_amount);
                holder.live_auction_bid_amount = convertView.findViewById(R.id.live_auction_bid_amount);

                holder.gv_tag_category = convertView.findViewById(R.id.gv_tag_category);

                convertView.setTag(holder);
            }
            else
                holder=(ViewHolder)convertView.getTag();

                    long different = ConvertDate(auction_list_data.get(position).getAuctionenddate()).getTime() - ConvertDate(getCurrentDate()).getTime();
                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;
                    long elapsedDays = different / daysInMilli;
                    different = different % daysInMilli;
                    long elapsedHours = different / hoursInMilli;
                    different = different % hoursInMilli;
                    long elapsedMinutes = different / minutesInMilli;
                    different = different % minutesInMilli;
                    long elapsedSeconds = different / secondsInMilli;

                    if (auction_list_data.get(position).getAuction_bid_count().equals("0")){
                        date1 = String.format("%02d",elapsedDays) + "D : " + String.format("%02d",elapsedHours) + "H : "+elapsedMinutes + "M : "+elapsedSeconds + "S "+"LEFT";
                    }else {
                        date1 = String.format("%02d",elapsedDays) + "D : " + String.format("%02d",elapsedHours) + "H : "+elapsedMinutes + "M : "+elapsedSeconds + "S "+"left";

                    }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(context,NewLive_AuctionItem.class);
                    Share_it.savestring("Auction_id",auction_list_data.get(position).getAuctionid());
                    context.startActivity(in);
                }
            });
            holder.live_auction_bid_amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(context,NewLive_AuctionItem.class);
                    Share_it.savestring("Auction_id",auction_list_data.get(position).getAuctionid());
                    in.putExtra("date",date1);
                    context.startActivity(in);
                }
            });
            holder.live_auction_buy_amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(context,NewLive_AuctionItem.class);
                    Share_it.savestring("Auction_id",auction_list_data.get(position).getAuctionid());
                    in.putExtra("date",date1);
                    context.startActivity(in);
                }
            });

            holder.item_number.setText("#"+auction_list_data.get(position).getAuction_item_number());

            holder.tv_item_date_time.setTypeface(holder.regular);

            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = null;
            try
            {
                date = format.parse(auction_list_data.get(position).getAuctionstartdate());
            }
            catch (ParseException e)
            {
                //nothing
            }

            java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("MM-dd-yyyy  hh:mm aa");
            String newDateStr = postFormater.format(date);
            holder.tv_item_date_time.setText(""+newDateStr.replace("am","AM").replace("pm","PM")+" "+auction_list_data.get(position).getEvent_short_time_zone());

            holder.live_auction_title.setTypeface(holder.semibold);
            holder.live_auction_buy_amount.setTypeface(holder.regular);
            holder.live_auction_bid_amount.setTypeface(holder.regular);
            holder.item_number.setTypeface(holder.medium);

            String auction_title = auction_list_data.get(position).getAuctiontitle();
            String upperString = auction_title.substring(0, 1).toUpperCase() + auction_title.substring(1).toLowerCase();
            holder.live_auction_title.setText(upperString);

                Picasso.with(context)
                        .load(auction_list_data.get(position).getAuctionimage()).error(R.drawable.auto_image).placeholder(R.drawable.auction_event_empty)
                        .into(holder.live_auction_image);

            currentbid = auction_list_data.get(position).getAuction_current_bid_amount();
            if(currentbid.equals(null) || currentbid.equals("null") || currentbid.equals(" ") ) {

                String auctionstartbid = auction_list_data.get(position).getAuctionbidding();

                String s = String.format("%,d", Long.parseLong(auctionstartbid));

                String bid_amount_values = "<font color='#ffffff'><b><big>$" + s + ".00</big></b>" + "<small> Bid</small></font>";
                holder.live_auction_bid_amount.setText( Html.fromHtml(bid_amount_values));

            }else {
                currentbid = currentbid.substring(0, currentbid.length() - 2);

                String s = String.format("%,d", Long.parseLong(currentbid));

                String bid_amount_values = "<font color='#ffffff'><b><big>$ " + s + ".00</big></b>" + "<small> Bid</small></font>";
                holder.live_auction_bid_amount.setText( Html.fromHtml(bid_amount_values));
            }

            String  buyamount = auction_list_data.get(position).getAuction_buy_price();
            buyamount = buyamount.substring(0, buyamount.length()-2);

            String s = String.format("%,d", Long.parseLong(buyamount));

            String buy_amount_values = "<font color='#ffffff'><b><big>$ " + s + ".00</big></b>"+"<small> Buy</small></font>";
            holder.live_auction_buy_amount.setText( Html.fromHtml(buy_amount_values));

            ArrayList<String> tag_data = auction_list_data.get(position).getTag_list();

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
