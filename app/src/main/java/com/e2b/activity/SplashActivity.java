package com.e2b.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.e2b.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import e2b.activity.AuthActivity;
import e2b.activity.MapActivity;
import e2b.utils.ConsumerPreferenceKeeper;


/**
 * A login screen that offers login via email/password.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initializeFCMToken();
        checkForUpdates();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // check profile loggedin or not in app
                    if(!TextUtils.isEmpty(ConsumerPreferenceKeeper.getInstance().getAccessToken())){
                        launchActivityMain(MapActivity.class);
                    }else{
                        launchActivityMain(AuthActivity.class);
                    }
                finish();
            }
        }, 1500);
    }

    private void initializeFCMToken() {

        FirebaseApp.initializeApp(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null) {
            Log.d(TAG, "FCM TOKEN "+token);
            ConsumerPreferenceKeeper.getInstance().setFCMToken(token);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // ... your own onResume implementation
        checkForCrashes();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }
}

