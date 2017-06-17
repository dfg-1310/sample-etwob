package com.e2b.api;

import android.text.TextUtils;
import android.util.Log;

import com.e2b.utils.AppConstant;
import com.e2b.utils.AppUtils;

import java.io.IOException;

import e2bmerchant.utils.MerchantPreferenceKeeper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class is used to passing profile token at central level.
 */
public class ApiInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        originalRequest = originalRequest.newBuilder()
                .addHeader("deviceId", "56247")
                .addHeader("deviceOS", AppConstant.DEVICE_OS).build();
        String token = MerchantPreferenceKeeper.getInstance().getAccessToken();

        if (TextUtils.isEmpty(token)) {
            return chain.proceed(originalRequest);
        }

        token = AppUtils.encodeToBase64("x:"+token);
        Request newRequest = originalRequest.newBuilder().
                addHeader("Authorization", "Basic " +token).build();


        Log.d("Request token ", token);
        Log.d("Request URL ", newRequest.url().toString());

        Response response = chain.proceed(newRequest);
        Log.d("Response ", response.body().source().toString());

        return response;
    }

}
