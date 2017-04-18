package com.e2b.application;

import android.app.Application;
import android.content.Context;

import com.e2b.api.ApiClient;
import com.e2b.api.IApiRequest;
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
