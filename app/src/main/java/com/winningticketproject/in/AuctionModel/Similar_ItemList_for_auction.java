package com.winningticketproject.in.AuctionModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.squareup.picasso.Picasso;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.AppInfo.item_Data;
import com.winningticketproject.in.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.winningticketproject.in.AppInfo.Share_it.ConvertDate;
import static com.winningticketproject.in.AppInfo.Share_it.getCurrentDate;
import static com.winningticketproject.in.SignInSingup.Splash_screen.medium;
import static com.winningticketproject.in.SignInSingup.Splash_screen.regular;
import static com.winningticketproject.in.SignInSingup.Splash_screen.semibold;

public class Similar_ItemList_for_auction extends RecyclerView.Adapter<Similar_ItemList_for_auction.MyViewHolder> {
    List<item_Data> horizontalList = Collections.emptyList();
    String bidding_type,date1;
    Context context;
    String s;

    public Similar_ItemList_for_auction(List<item_Data> horizontalList, Context context) {
        this.horizontalList = horizontalList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        CountDownTimer timer;
        LinearLayout liner_layout ;
        RecyclerView gv_tag_category;
        TextView txtview, txtviewamount,tv_item_number,txtamount,sold,bidding_row;
        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageview);
            txtview = view.findViewById(R.id.txtview);
            txtviewamount = view.findViewById(R.id.txtviewamount);
            tv_item_number = view.findViewById(R.id.tv_item_number);
            liner_layout = view.findViewById(R.id.liner_layout);
            sold = view.findViewById(R.id.sold);
            txtamount = view.findViewById(R.id.txtamount);
            bidding_row = view.findViewById(R.id.bidding_row);

            gv_tag_category = view.findViewById(R.id.gv_tag_category);

            txtview.setTypeface(semibold);
            txtviewamount.setTypeface(regular);
            sold.setTypeface(regular);
            txtamount.setTypeface(regular);
            tv_item_number.setTypeface(regular);
            bidding_row.setTypeface(regular);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.similar_data_liveaucton, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtview.setText(horizontalList.get(position).txt);

        Picasso.with(context)
                .load(horizontalList.get(position).imageId).error(R.drawable.auto_image).placeholder(R.drawable.auction_event_empty)
                .into(holder.imageView);

        holder.tv_item_number.setText("#"+horizontalList.get(position).item_number);

        String buy_amount = horizontalList.get(position).buy_price;

        buy_amount = buy_amount.substring(0, buy_amount.length()-2);

        s = String.format("%,d", Long.parseLong(buy_amount));

        String buy_amount_values = "<font color='#ffffff'><b><big>$" + s + "</big></b><small>"+".00 Buy</small></font>";
        holder.txtamount.setText(Html.fromHtml(buy_amount_values));

        long timer = ConvertDate(horizontalList.get(position).event_start_date).getTime();
        Date today = new Date();
        final long currentTime = today.getTime();
        long expiryTime = timer - currentTime;

        if (holder.timer != null) {
            holder.timer.cancel();
        }

        holder.timer = new CountDownTimer(expiryTime, 1000) {
            public void onTick(long millisUntilFinished) {

                long different = ConvertDate(horizontalList.get(position).event_start_date).getTime() - ConvertDate(getCurrentDate()).getTime();
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

                holder.bidding_row.setText(String.format("%02d", elapsedDays) + "D : " + String.format("%02d", elapsedHours) + "H : " + elapsedMinutes + "M : " + elapsedSeconds + "S " + "remaining");

            }

            public void onFinish() {
                long timer = ConvertDate(horizontalList.get(position).event_end_date).getTime();
                Date today = new Date();
                final long currentTime = today.getTime();
                long expiryTime = timer - currentTime;

                if (holder.timer != null) {
                    holder.timer.cancel();
                }

                holder.timer = new CountDownTimer(expiryTime, 1000) {
                    public void onTick(long millisUntilFinished) {

                        long different = ConvertDate(horizontalList.get(position).event_end_date).getTime() - ConvertDate(getCurrentDate()).getTime();
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

                        if (horizontalList.get(position).bid_count.equals("0")){
                            holder.bidding_row.setText(String.format("%02d",elapsedDays) + "D : " + String.format("%02d",elapsedHours) + "H : "+elapsedMinutes + "M : "+elapsedSeconds + "S "+"LEFT");
                        }else {
                            holder.bidding_row.setText(String.format("%02d",elapsedDays) + "D : " + String.format("%02d",elapsedHours) + "H : "+elapsedMinutes + "M : "+elapsedSeconds + "S "+"left");
                        }
                    }

                    public void onFinish() {
                        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        java.util.Date date = null;
                        try
                        {
                            date = format.parse(horizontalList.get(position).event_end_date);
                        }
                        catch (ParseException e)
                        {
                            //nothing
                        }

                        java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("MM-dd-yyyy hh:mm aa");
                        String newDateStr = postFormater.format(date);
                        date1 =newDateStr.replace("am","AM").replace("pm","PM")+" "+horizontalList.get(position).event_short_time_zone;
                        holder.bidding_row.setText(""+newDateStr.replace("am","AM").replace("pm","PM")+" "+horizontalList.get(position).event_short_time_zone);
                    }
                }.start();
            }
        }.start();


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context,NewLive_AuctionItem.class);
                Share_it.savestring ("Auction_id", horizontalList.get(position).id);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("date",date1);
                context.startActivity(in);
                ((Activity)v.getContext()).finish();
            }
        });

        String pricevalue = horizontalList.get(position).starting_bid;

        if (horizontalList.get(position).is_live.equals("false") && horizontalList.get(position).is_expired.equals("false")){

            s = String.format("%,d", Long.parseLong(pricevalue));

            bidding_type   = "<font color='#FFFFFF'><b><big>$" + s + "</big></b><small>" + ".00 bid</small></font>";

        }else if (horizontalList.get(position).is_live.equals("true")){

            if (horizontalList.get(position).bid_count.equals("0")){

                s = String.format("%,d", Long.parseLong(pricevalue));

                bidding_type   = "<font color='#FFFFFF'><b><big>$" + s + "</big></b><small>"+".00 Bid</small></font>";
            }
            else {

                String currentbid = horizontalList.get(position).current_bid_amount;
                currentbid = currentbid.substring(0, currentbid.length()-2);

                s = String.format("%,d", Long.parseLong(currentbid));

                bidding_type   = "<font color='#FFFFFF'><b><big>$" + s + "</big></b><small>"+".00 Bid</small></font>";
            }

            if (horizontalList.get(position).payment_status.equals("paid")){
                bidding_type   = "<font color='#ef4363'><b><big>" + "Sold</big><b></font>";
                holder.sold.setVisibility(View.VISIBLE);
                holder.sold.setText(Html.fromHtml(bidding_type));
                holder.txtviewamount.setVisibility(View.GONE);
                holder.txtamount.setVisibility(View.GONE);
            }

        }else if (horizontalList.get(position).is_expired.equals("true")){

            if (horizontalList.get(position).payment_status.equals("paid")){

                bidding_type   = "<font color='#ef4363'><b><big>" + "Sold</big><b></font>";
                holder.sold.setVisibility(View.VISIBLE);
                holder.sold.setText(Html.fromHtml(bidding_type));
                holder.txtviewamount.setVisibility(View.GONE);
                holder.txtamount.setVisibility(View.GONE);

            }else {
                bidding_type   = "<font color='#ef4363'><b><big>" + "Auction Closed</big><b></font>";
                holder.sold.setVisibility(View.VISIBLE);
                holder.sold.setText(Html.fromHtml(bidding_type));
                holder.txtviewamount.setVisibility(View.GONE);
                holder.txtamount.setVisibility(View.GONE);
            }
        }

        holder.txtviewamount.setText(Html.fromHtml(bidding_type));

        holder.txtviewamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context,NewLive_AuctionItem.class);
                Share_it.savestring ("Auction_id", horizontalList.get(position).id);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("date",date1);
                context.startActivity(in);
                ((Activity)v.getContext()).finish();
            }
        });
        holder.txtamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context,NewLive_AuctionItem.class);
                Share_it.savestring ("Auction_id", horizontalList.get(position).id);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("date",date1);
                context.startActivity(in);
                ((Activity)v.getContext()).finish();
            }
        });
        holder.sold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context,NewLive_AuctionItem.class);
                Share_it.savestring ("Auction_id", horizontalList.get(position).id);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("date",date1);
                context.startActivity(in);
                ((Activity)v.getContext()).finish();
            }
        });

        ArrayList<String> tag_data = horizontalList.get(position).tag_list;

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager();
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.FLEX_START);
        holder.gv_tag_category.setLayoutManager(layoutManager);
        FlexboxAdapter adapter = new FlexboxAdapter(context, tag_data);
        holder.gv_tag_category.setAdapter(adapter);

    }
    @Override
    public int getItemCount() {
        return horizontalList.size();
    }
}