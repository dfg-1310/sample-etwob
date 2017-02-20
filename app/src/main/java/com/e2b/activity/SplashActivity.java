package com.e2b.activity;

import android.os.Bundle;
import android.os.Handler;

import com.e2b.R;

import e2b.activity.AuthActivity;
import e2b.activity.HomeActivity;
import e2b.utils.ConsumerPreferenceKeeper;

/**
 * A login screen that offers login via email/password.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // check user loggedin or not in app
                if(ConsumerPreferenceKeeper.getInstance().getIsLogin()){
                    launchActivityMain(HomeActivity.class);
                }else{
                    launchActivityMain(AuthActivity.class);
                }
                finish();
            }
        }, 1500);
    }

}

