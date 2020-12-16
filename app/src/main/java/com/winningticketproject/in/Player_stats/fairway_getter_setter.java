package com.winningticketproject.in.Player_stats;

public class fairway_getter_setter {

    public fairway_getter_setter(String total_score, String score_date_details, String left_per, String right_per, String center_per, String event_name) {
        this.total_score = total_score;
        this.score_date_details = score_date_details;
        this.left_per = left_per;
        this.right_per = right_per;
        this.center_per = center_per;
        this.event_name = event_name;
    }

    public String getScore_date_details() {
        return score_date_details;
    }

    public String getEvent_name() {
        return event_name;
    }

    String total_score;
    String score_date_details;
    String left_per;
    String right_per;
    String center_per;
    String event_name;

}
