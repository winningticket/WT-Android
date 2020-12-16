package com.winningticketproject.in.AppInfo;

/**
 * Created by carmatec on 2/5/17.
 */
public class AllEventGetterSetter {

    String id;
    String name;
    String start_date;
    String event_logo;
    String event_type;
    String event_status;
    int push_notification_count;

    public AllEventGetterSetter(String id, String name, String start_date,  String event_logo,String event_type, String event_status, int push_notification_count) {
        this.id = id;
        this.name = name;
        this.start_date = start_date;
        this.event_logo = event_logo;
        this.event_type = event_type;
        this.event_status = event_status;
        this.push_notification_count = push_notification_count;
    }

    public String getEvent_status() {
        return event_status;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEvent_type() {
        return event_type;
    }

    public String getEvent_logo() {
        return event_logo;
    }

    public int getPush_notification_count() {
        return push_notification_count;
    }

    public void setPush_notification_count(int push_notification_count) {
        this.push_notification_count = push_notification_count;
    }
}
