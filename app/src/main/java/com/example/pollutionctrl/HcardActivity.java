package com.example.pollutionctrl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;

public class HcardActivity extends AppCompatActivity {

    ImageView img;
    TextView txt,txt3;
    WebView txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hcard);

        img = findViewById(R.id.hcard_img);
        txt = findViewById(R.id.hcard_txt);
        txt2 = findViewById(R.id.hcard_txt2);
        txt3 = findViewById(R.id.hcard_txt3);

        Intent intent = getIntent();
        HashMap<String,String > map = (HashMap<String, String>) intent.getSerializableExtra("map");

        txt.setText(map.get("article_name"));
        txt2.loadData(map.get("desp"),"text/html","UTF-8");
        Glide.with(this).load(map.get("img")).into(img);

        String s = "Posted by -------" + map.get("name") + "------- on " + map.get("date");
        txt3.setText(s);

    }
}
