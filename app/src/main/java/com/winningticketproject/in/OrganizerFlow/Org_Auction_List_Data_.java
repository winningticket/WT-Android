package com.winningticketproject.in.OrganizerFlow;

public class Org_Auction_List_Data_ {

    public String getWinning_bid_amount() {
        return winning_bid_amount;
    }

    public Org_Auction_List_Data_(String auctionid, String auctiontitle, String auctionstartdate, String auctionenddate, String auctionbidding, String auctionimage, String auction_payment_status, String auction_bid_count, String auction_current_bid_amount, String auction_is_live, String auction_is_expired, String auction_buy_price, String auction_item_number, String winning_bid_amount, String winning_paid_amount,String user_id,String user_name) {
        this.auctionid = auctionid;
        this.auctiontitle = auctiontitle;
        this.auctionstartdate = auctionstartdate;
        this.auctionenddate = auctionenddate;
        this.auctionbidding = auctionbidding;
        this.auctionimage = auctionimage;
        this.auction_payment_status = auction_payment_status;
        this.auction_bid_count = auction_bid_count;
        this.auction_current_bid_amount = auction_current_bid_amount;
        this.auction_is_live = auction_is_live;
        this.auction_is_expired = auction_is_expired;
        this.auction_buy_price = auction_buy_price;
        this.auction_item_number=auction_item_number;
        this.winning_bid_amount = winning_bid_amount;
        this.winning_paid_amount = winning_paid_amount;

        this.user_id = user_id;
        this.user_name = user_name;
    }


    public String getAuctionid() {
        return auctionid;
    }

    public String getAuctiontitle() {
        return auctiontitle;
    }

    public String getAuctionbidding() {
        return auctionbidding;
    }

    public String getAuctionimage() {
        return auctionimage;
    }

    public String getAuction_payment_status() {
        return auction_payment_status;
    }

    public String getAuction_bid_count() {
        return auction_bid_count;
    }

    public String getAuction_current_bid_amount() {
        return auction_current_bid_amount;
    }

    public String getAuction_is_live() {
        return auction_is_live;
    }

    public String getAuction_is_expired() {
        return auction_is_expired;
    }

    String auctionid;
    String auctiontitle;
    String auctionstartdate;

    public String getAuctionstartdate() {
        return auctionstartdate;
    }

    public String getAuctionenddate() {
        return auctionenddate;
    }

    String auctionenddate;
    String auctionbidding;
    String auctionimage;
    String auction_payment_status;
    String auction_bid_count;
    String auction_current_bid_amount;
    String auction_is_live;
    String auction_is_expired;

    public String getAuction_item_number() {
        return auction_item_number;
    }

    public String getAuction_buy_price() {
        return auction_buy_price;
    }

    String auction_item_number;
    String auction_buy_price;

    String winning_bid_amount;

    public String getWinning_paid_amount() {
        return winning_paid_amount;
    }

    String winning_paid_amount;

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    String user_id;
    String user_name;
}
