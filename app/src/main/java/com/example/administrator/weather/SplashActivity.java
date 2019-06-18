package com.example.administrator.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean firstLogin;
                    SharedPreferences sp=getSharedPreferences("sp",MODE_PRIVATE);
                    firstLogin=sp.getBoolean("state",true);
                    Thread.sleep(3000);
                    if(firstLogin){ //第一次登录，跳转到Login页面，且修改登录状态、写入登录状态
                        firstLogin=false;
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putBoolean("state",firstLogin);
                        editor.commit();
                        Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }else{//不是第一次登录，则跳转到主界面
                        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
