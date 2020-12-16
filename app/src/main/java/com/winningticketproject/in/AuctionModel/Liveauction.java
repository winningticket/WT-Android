package com.winningticketproject.in.AuctionModel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.winningticketproject.in.AppInfo.Auction_List_Data;
import com.winningticketproject.in.AppInfo.Share_it;
import com.winningticketproject.in.R;

import java.text.ParseException;
import java.util.ArrayList;

public class Liveauction  extends BaseAdapter
{
    public LayoutInflater inflater;
    public Context context;
    ArrayList<Auction_List_Data> auction_list_data = new ArrayList<>();
    Liveauction(Context context, ArrayList<Auction_List_Data> auction_list_data) {
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
        TextView live_auction_title,live_auction_amount,live_auction_bidding,item_number;
        ImageView live_auction_image;
        TextView tv_item_date_time;
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
            convertView = inflater.inflate(R.layout.custome_live_auction, null);

            holder.live_auction_image = convertView.findViewById(R.id.live_auction_image);
            holder.live_auction_title = convertView.findViewById(R.id.live_auction_title);
            holder.live_auction_amount = convertView.findViewById(R.id.live_auction_amount);
            holder.live_auction_bidding = convertView.findViewById(R.id.live_auction_bidding);
            holder.item_number = convertView.findViewById(R.id.tv_item_number);
            holder.tv_item_date_time = convertView.findViewById(R.id.tv_item_date_time);

            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context,LiveAction_Items.class);
                Share_it.savestring("Auction_id",auction_list_data.get(position).getAuctionid());
                context.startActivity(in);
            }
        });

        holder.item_number.setText("#"+auction_list_data.get(position).getAuction_item_number());

        holder.tv_item_date_time.setTypeface(holder.regular);

        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        java.util.Date date = null;
        try
        {
            date = format.parse(auction_list_data.get(position).getAuctionenddate());
        }
        catch (ParseException e)
        {
            //nothing
        }
        java.text.SimpleDateFormat postFormater = new java.text.SimpleDateFormat("MM/dd/yy @ hh:mm aa");
        String newDateStr = postFormater.format(date);
        holder.tv_item_date_time.setText("Ends "+newDateStr.replace("am","AM").replace("pm","PM"));

        holder.live_auction_title.setTypeface(holder.semibold);
        holder.live_auction_amount.setTypeface(holder.regular);
        holder.live_auction_bidding.setTypeface(holder.regular);
        holder.item_number.setTypeface(holder.medium);

        holder.live_auction_title.setText(auction_list_data.get(position).getAuctiontitle());
        Picasso.with(context)
                .load(auction_list_data.get(position).getAuctionimage()).error(R.drawable.auction_event_empty).placeholder(R.drawable.auction_event_empty)
                .into(holder.live_auction_image);


        String winning_bid_amount = auction_list_data.get(position).getWinning_bid_amount();

        String winning_pad_amount = auction_list_data.get(position).getWinning_paid_amount();
        String winning_amount;

        if (winning_pad_amount.equals(null) || winning_pad_amount.equals("null")){
            winning_amount=winning_bid_amount;
        }else {
            winning_amount = winning_pad_amount;
        }

        if (winning_pad_amount.equals(null) || winning_pad_amount.equals("null") || winning_pad_amount.equals("")){
            winning_amount = auction_list_data.get(position).getAuctionbidding();
        }

        double pricevalue1 = Double.parseDouble(winning_amount);

        double pricevalue = Double.parseDouble(auction_list_data.get(position).getAuctionbidding());

        String bid_amount_values="";
        if (auction_list_data.get(position).getAuction_is_live().equals("false") && auction_list_data.get(position).getAuction_is_expired().equals("false")){

            bid_amount_values = "<b><big>$" +String.format("%.2f", pricevalue)+"</big></b><br>"+"Starting bid";
            holder.live_auction_bidding.setVisibility(View.VISIBLE);

        }else if (auction_list_data.get(position).getAuction_is_live().equals("true")){

            if (auction_list_data.get(position).getAuction_payment_status().equals("paid")){

                bid_amount_values = "<font color='#FD2726'><b><big>$" +String.format("%.2f", pricevalue1)+"</big></b><br>"+"Sold</font>";
                holder.live_auction_bidding.setVisibility(View.GONE);

            }else {
                if (auction_list_data.get(position).getAuction_bid_count().equals("0")) {

                    bid_amount_values = "<font color='#0bb1de'><b><big>$" + String.format("%.2f", pricevalue) + "</big></b><br>" + "Starting bid</font>";

                } else {

                    double currentbid = Double.parseDouble(auction_list_data.get(position).getAuction_current_bid_amount());

                    bid_amount_values = "<font color='#0bb1de'><b><big>$" + String.format("%.2f", currentbid) + "</big></b><br>" + "Current Bid</font>";

                }
                holder.live_auction_bidding.setVisibility(View.VISIBLE);
            }

        }else if (auction_list_data.get(position).getAuction_is_expired().equals("true")){
            holder.live_auction_bidding.setVisibility(View.GONE);
            if (auction_list_data.get(position).getAuction_payment_status().equals("paid")){
                bid_amount_values = "<font color='#FD2726'><b><big>$" +String.format("%.2f", pricevalue1)+"</big></b><br>"+"Sold</font>";
                holder.live_auction_bidding.setVisibility(View.GONE);

            }else {
                bid_amount_values = "<font color='#FD2726'><b>Auction Closed</b></font>";

            }

            holder.live_auction_bidding.setVisibility(View.GONE);
        }

        holder.live_auction_amount.setText(Html.fromHtml(bid_amount_values));

        String auction_bid_now = "<b><big>$"+auction_list_data.get(position).getAuction_buy_price()+"</big></b><br>Buy it now";
        holder.live_auction_bidding.setText(Html.fromHtml(auction_bid_now));

        return convertView;
    }
}