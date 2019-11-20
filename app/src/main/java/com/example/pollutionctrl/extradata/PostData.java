package com.example.pollutionctrl.extradata;

import com.google.firebase.Timestamp;

import java.util.Date;

public class PostData {
    private String desp;
    private String _id;
    private String img;
    private String name;
    private String article_name;
    private Timestamp date;

    public PostData() {
    }

    public PostData(Timestamp Date, String _id, String article_name, String desp, String img, String name) {
        this.desp = desp;
        this._id = _id;
        this.img = img;
        this.name = name;
        this.article_name = article_name;
        this.date = Date;
    }

    public String getData() {
        return desp;
    }

    public String getUserUID() {
        return _id;
    }

    public String getImageUri() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getArticle_name() {
        return article_name;
    }

    public String getDate() {
        return date.toDate().toString();
    }
}
