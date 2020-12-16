package com.winningticketproject.in.AppInfo;

public class TeeData {
    public TeeData(String name, String rating, String slope, String yards_total) {
        this.name = name;
        this.rating = rating;
        this.slope = slope;
        this.yards_total = yards_total;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getSlope() {
        return slope;
    }

    public String getYards_total() {
        return yards_total;
    }

    String name,rating,slope,yards_total;



}
