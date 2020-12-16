package com.winningticketproject.in.AppInfo;
public class Course_Data {

    public String getId() {
        return id;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getNames() {
        return names;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getAddres() {
        return addres;
    }

    String id;
    String lat;
    String lon;
    String names;
    String image;
    String type;
    String addres;

    public Course_Data(String id, String lat, String lon, String names, String image, String type, String addres, String distance_mils) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.names = names;
        this.image = image;
        this.type = type;
        this.addres = addres;
        this.distance_mils = distance_mils;
    }

    public String getDistance_mils() {
        return distance_mils;
    }

    String distance_mils;
}
