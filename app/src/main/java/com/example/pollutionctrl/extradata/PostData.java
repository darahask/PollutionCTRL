package com.example.pollutionctrl.extradata;

public class PostData {
    private String data;
    private String userUID;
    private String imageUri;

    public PostData(String data, String userUID, String imageUri) {
        this.data = data;
        this.userUID = userUID;
        this.imageUri = imageUri;
    }

    public String getData() {
        return data;
    }

    public String getUserUID() {
        return userUID;
    }

    public String getImageUri() {
        return imageUri;
    }
}
