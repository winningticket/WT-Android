package com.winningticketproject.in.ChatWithOther;

public class User_list_Model {

    public User_list_Model(String user_id, String name, String avtar_imge, String email_id,String key , String value , String status , String last_msg,String timing) {
        this.user_id = user_id;
        this.name = name;
        this.avtar_imge = avtar_imge;
        this.email_id = email_id;
        this.key = key;
        this.value = value;
        this.status = status;
        this.last_msg = last_msg;
        this.timing = timing;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getAvtar_imge() {
        return avtar_imge;
    }

    public String getEmail_id() {
        return email_id;
    }

    String user_id;
    String name;
    String avtar_imge;
    String email_id;
    String key;
    String value;
    String status;
    String last_msg;

    public String getTiming() {
        return timing;
    }

    String timing;
}