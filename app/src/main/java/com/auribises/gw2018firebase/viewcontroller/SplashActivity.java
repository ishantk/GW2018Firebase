package com.auribises.gw2018firebase.viewcontroller;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.auribises.gw2018firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    FirebaseUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // One Time Registration or Login !!
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        if(fbUser != null){
            handler.sendEmptyMessageDelayed(101,2500);
        }else{
            handler.sendEmptyMessageDelayed(201,2500);
        }

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 101){
                Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(SplashActivity.this,RegisterUserActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
}
