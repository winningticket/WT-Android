package com.winningticketproject.in.AuctionModel;

public class Bidding_histiry_data {

    public Bidding_histiry_data(String user_id, String bidding_current_bid, String bidding_created_bid, String bidding_bidder_name, String bidding_winning_bid, String bidding_winner_status, String bidding_status) {
        this.user_id = user_id;
        this.bidding_current_bid = bidding_current_bid;
        this.bidding_created_bid = bidding_created_bid;
        this.bidding_bidder_name = bidding_bidder_name;
        this.bidding_winning_bid = bidding_winning_bid;
        this.bidding_winner_status = bidding_winner_status;
        this.bidding_status = bidding_status;
    }

    String user_id;
    String bidding_current_bid;
    String bidding_created_bid;
    String bidding_bidder_name;
    String bidding_winning_bid;
    String bidding_winner_status;
    String bidding_status;

}
