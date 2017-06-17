package com.e2b.application;

import android.app.Application;
import android.content.Context;

import e2bmerchant.api.ApiClient;
import e2bmerchant.api.IApiRequest;
import e2bmerchant.utils.MerchantPreferenceKeeper;

/**
 * This is the application class.
 */
public class E2BApplication extends Application {

    public static Context context;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        MerchantPreferenceKeeper.setContext(getApplicationContext());
        ApiClient.init(IApiRequest.class);
    }
}
