package com.winningticketproject.in.AppInfo;

public class Individual_course_data {
    public Individual_course_data(String name, String total_par, String date_value, String total_net, String total_gross, String total_scores) {
        this.name = name;
        this.total_par = total_par;
        this.date_value = date_value;
        this.total_net = total_net;
        this.total_gross = total_gross;
        this.total_scores = total_scores;
    }

    public String getName() {
        return name;
    }

    public String getTotal_par() {
        return total_par;
    }

    public String getDate_value() {
        return date_value;
    }

    public String getTotal_net() {
        return total_net;
    }

    public String getTotal_gross() {
        return total_gross;
    }

    public String getTotal_scores() {
        return total_scores;
    }

    String name ;
    String total_par ;
    String date_value;
    String total_net ;
    String total_gross ;
    String total_scores ;
}
