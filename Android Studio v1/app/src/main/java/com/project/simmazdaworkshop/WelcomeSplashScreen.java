package com.project.simmazdaworkshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeSplashScreen extends AppCompatActivity {

    private  int loadtime = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(WelcomeSplashScreen.this, Loginpage.class);
                startActivity(i);
                finish();
            }
        },loadtime);
    }
}
