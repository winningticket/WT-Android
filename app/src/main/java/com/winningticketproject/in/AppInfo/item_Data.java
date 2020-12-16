package com.winningticketproject.in.AppInfo;

import java.util.ArrayList;

public class item_Data {

    public String id,txt,imageId,bid_count,is_live,is_expired,current_bid_amount,starting_bid,payment_status,item_number,buy_price,event_end_date,event_short_time_zone,event_start_date;
    public ArrayList<String> tag_list;

    public item_Data(String id, String imageId, String text, String bid_count, String is_live, String is_expired, String current_bid_amount, String starting_bid, String payment_status, String item_number, String buy_price, String event_end_date, String event_short_time_zone, String event_start_date, ArrayList<String> tag_list) {

        this.id=id;
        this.imageId = imageId;
        this.txt=text;
        this.bid_count=bid_count;
        this.is_live=is_live;
        this.is_expired=is_expired;
        this.current_bid_amount=current_bid_amount;
        this.starting_bid=starting_bid;
        this.payment_status = payment_status;
        this.item_number = item_number;
        this.buy_price = buy_price;
        this.event_end_date = event_end_date;
        this.event_short_time_zone = event_short_time_zone;
        this.event_start_date = event_start_date;
        this.tag_list = tag_list;
    }
}