package com.e2b.application;

import android.app.Application;
import android.content.Context;

import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;

import e2b.utils.ConsumerPreferenceKeeper;


/**
 * This is the application class.
 */
public class E2BConsumerApplication extends com.e2b.application.E2BApplication {

    public static Context context;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ConsumerPreferenceKeeper.setContext(getApplicationContext());

        ApiClient.init(IApiRequest.class);
    }
}
