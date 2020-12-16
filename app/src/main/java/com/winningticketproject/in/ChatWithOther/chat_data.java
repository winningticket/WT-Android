package com.winningticketproject.in.ChatWithOther;

public class chat_data {

    public chat_data(String message, String user_type, String attachment_utl, String mime_type,String timestamp) {
        this.message = message;
        this.user_type = user_type;
        this.attachment_utl = attachment_utl;
        this.mime_type = mime_type;
        this.timestamp = timestamp;
    }

    String message;
    String user_type;
    String attachment_utl;
    String mime_type;
    String timestamp;

}
