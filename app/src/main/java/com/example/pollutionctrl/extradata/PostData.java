package com.example.pollutionctrl.extradata;

public class PostData {
    private String desp;
    private String _id;
    private String img;
    private String name;
    private String article_name;
    private String Date;

    public PostData(String data, String userUID, String imageUri, String name, String article_name, String date) {
        this.desp = data;
        this._id = userUID;
        this.img = imageUri;
        this.name = name;
        this.article_name = article_name;
        Date = date;
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
        return Date;
    }
}
