package com.shareware.saregama;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashScreen extends AppCompatActivity {

    ImageView img;
    Animation top,fade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        img = (ImageView)findViewById(R.id.bgimg);
        top = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        fade = AnimationUtils.loadAnimation(this,R.anim.faldeanim);

        img.setAnimation(top);
        //img.setAnimation(fade);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, LoginPage.class);
                startActivity(i);
                finish();
            }
        }, 4000);
    }
}
