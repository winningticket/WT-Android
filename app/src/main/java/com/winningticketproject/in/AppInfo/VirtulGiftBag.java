package com.winningticketproject.in.AppInfo;

public class VirtulGiftBag {

    public VirtulGiftBag(String id, String name, String date, String image,String expires) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.image = image;
        this.expires = expires;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    String id;
    String name;
    String date;
    String image;

    String expires;

}
