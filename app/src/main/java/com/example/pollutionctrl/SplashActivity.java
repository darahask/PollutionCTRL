package com.example.pollutionctrl;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_TIME_OUT =3500;
    TextView t1,t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        t1 = findViewById(R.id.splash_text);
        t2 = findViewById(R.id.splash_text2);
        t2.setAlpha(0f);
        t1.setAlpha(0f);

        final long duration = 1000;
        t1.animate().alpha(1f).setDuration(1500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                t1.animate().alpha(0f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        t2.animate().alpha(1f).setDuration(duration);
                    }
                });
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,LauncherActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_SCREEN_TIME_OUT);
    }
}
