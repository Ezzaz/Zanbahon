package com.orionitinc.uber;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.jaeger.library.StatusBarUtil;

public class SplashScreen extends AppCompatActivity {

    private LinearLayout upLL , downLL;
    private Animation uptodown,downtoup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // transparent status bar
        StatusBarUtil.setTransparent(SplashScreen.this);

        //find view by id
        upLL = findViewById(R.id.up);
        downLL = findViewById(R.id.down);

        // up down animation
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        upLL.setAnimation(uptodown);
        downLL.setAnimation(downtoup);

        new Handler ().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashScreen.this,
                        MainActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, 1000);


    }

}
