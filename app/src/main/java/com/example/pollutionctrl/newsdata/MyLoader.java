package com.example.pollutionctrl.newsdata;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class MyLoader extends AsyncTaskLoader<AirData> {
    private String url;

    public MyLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public AirData loadInBackground() {
        if(url == null){
            return null;
        }

        return QueryUtils.fetchAirData(url);
    }
}
