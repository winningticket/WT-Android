package com.winningticketproject.in.AppInfo;

public class Your_round_data {

    public Your_round_data(String r_id,String r_image, String r_course_name, String r_address, String r_km, String r_last_played, String r_tee, String r_starting_hole, String r_current_score,String r_handicap_score) {
        this.r_id = r_id;
        this.r_image = r_image;
        this.r_course_name = r_course_name;
        this.r_address = r_address;
        this.r_km = r_km;
        this.r_last_played = r_last_played;
        this.r_tee = r_tee;
        this.r_starting_hole = r_starting_hole;
        this.r_current_score = r_current_score;
        this.r_handicap_score = r_handicap_score;
    }

    public String getR_image() {
        return r_image;
    }

    public String getR_course_name() {
        return r_course_name;
    }

    public String getR_address() {
        return r_address;
    }

    public String getR_km() {
        return r_km;
    }

    public String getR_last_played() {
        return r_last_played;
    }

    public String getR_tee() {
        return r_tee;
    }

    public String getR_starting_hole() {
        return r_starting_hole;
    }

    public String getR_current_score() {
        return r_current_score;
    }

    public String getR_id() {
        return r_id;
    }

    String r_id;
    String r_image;
    String r_course_name;
    String r_address;
    String r_km;
    String r_last_played;
    String r_tee;
    String r_starting_hole;
    String r_current_score;

    public String getR_handicap_score() {
        return r_handicap_score;
    }

    String r_handicap_score;


}
