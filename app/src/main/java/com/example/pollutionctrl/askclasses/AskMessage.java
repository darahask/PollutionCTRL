package com.example.pollutionctrl.askclasses;

import android.net.Uri;

import java.time.LocalDate;
import java.util.Date;

public class AskMessage {

    private String id;
    private String date;
    private String imageUri;
    private String message;
    private String replyMessage;
    private String replyImageUri;
    private String status;
    private String key;

    public AskMessage() {
    }

    public AskMessage(String id, String date, String imageUri, String message, String replyImageUri, String replyMessage, String key, String status) {
        this.id = id;
        this.date = date;
        this.imageUri = imageUri;
        this.message = message;
        this.status = status;
        this.replyImageUri = replyImageUri;
        this.replyMessage = replyMessage;
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getMessage() {
        return message;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public String getReplyImageUri() {
        return replyImageUri;
    }

    public String getStatus() {
        return status;
    }

    public String getKey() {
        return key;
    }
}
