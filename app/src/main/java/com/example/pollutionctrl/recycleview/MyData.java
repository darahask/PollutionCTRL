package com.example.pollutionctrl.recycleview;

import android.net.Uri;

public class MyData {

    private String title;
    private String imgUri;
    private String link;

    public MyData() {
    }

    public MyData(String title, String imgUri, String link) {
        this.title = title;
        this.imgUri = imgUri;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getImgUri() {
        return imgUri;
    }

    public String getLink() {
        return link;
    }
}
