package com.example.pollutionctrl.askclasses;

import android.net.Uri;

import java.time.LocalDate;
import java.util.Date;

public class AskMessage {

    private String id;
    private String date;
    private String imageUri;
    private String message;

    public AskMessage() {
    }

    public AskMessage(String id, String date, String imageUri, String message) {
        this.id = id;
        this.date = date;
        this.imageUri = imageUri;
        this.message = message;
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
}
