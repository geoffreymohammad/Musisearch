package com.project.android.musisearch.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project.android.musisearch.R;
import com.project.android.musisearch.ui.login.LoginActivity;
import com.project.android.musisearch.ui.main.MainActivity;
import com.project.android.musisearch.ui.register.Step1Activity;
import com.project.android.musisearch.ui.register.Step2Activity;

import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_GOOGLE;
import static com.project.android.musisearch.utils.Sessions.PREFS_LOGIN_STEP2;

public class SplashActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        prepareLaunchApp();
    }

    void prepareLaunchApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences getPrefsSplash = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        boolean isLoginGoogle = getPrefsSplash.getBoolean(PREFS_LOGIN_GOOGLE, false);
                        boolean isLoginStep2 = getPrefsSplash.getBoolean(PREFS_LOGIN_STEP2, false);
                        if (!isLoginGoogle) {
                            intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (isLoginStep2){
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            intent = new Intent(SplashActivity.this, Step2Activity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                t.start();
            }
        }, 3000);
    }
}
