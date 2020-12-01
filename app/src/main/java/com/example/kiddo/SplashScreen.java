package com.example.kiddo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(Login.class)
                .withSplashTimeOut(1000)
                .withBackgroundColor(Color.parseColor("#FFFFFF"))
                .withFooterText("made with ♡ by HITAM")
                .withAfterLogoText("KidDo")
                .withLogo(R.mipmap.ic_kiddo_foreground);

        //config.getHeaderTextView().setTextColor(Color.WHITE);
        config.getFooterTextView().setTextColor(Color.BLACK);
        config.getAfterLogoTextView().setTextColor(Color.BLACK);

        View easySplashScreen = config.create();
        setContentView(easySplashScreen);
//        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
//                .withFullScreen()
//                .withTargetActivity(MainActivity.class)
//                .withSplashTimeOut(3000)
//                .withBackgroundColor(Color.parseColor("#000"))
//                //.withHeaderText("Rekayasa Sistem")
//                .withFooterText("made with ❤ by HITAM")
//                .withBeforeLogoText("CRUD App")
//                .withAfterLogoText("KidDo")
//                .withLogo(R.mipmap.ic_kiddo);
//
//        //config.getHeaderTextView().setTextColor(Color.WHITE);
//        config.getFooterTextView().setTextColor(Color.WHITE);
//        config.getAfterLogoTextView().setTextColor(Color.WHITE);
//        config.getBeforeLogoTextView().setTextColor(Color.WHITE);
//
//        View easySplashScreen = config.create();
//        setContentView(easySplashScreen);
    }
}