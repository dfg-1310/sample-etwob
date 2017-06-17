package com.e2b.api;


import android.text.TextUtils;
import android.util.Log;

import com.e2b.BuildConfig;
import com.e2b.utils.AppUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import e2bmerchant.api.IApiRequest;
import e2bmerchant.utils.MerchantPreferenceKeeper;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class ApiClient {

    private static IApiRequest apiRequest;

    private static final String TAG = ApiClient.class.getSimpleName();
    public static void init(Class<IApiRequest> requestClass) {

        final MerchantPreferenceKeeper preference = MerchantPreferenceKeeper.getInstance();
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (request.url() == null || request.url().toString().trim().isEmpty()) {
                    return chain.proceed(request);
                }

                Log.d(TAG, "intercept: "+ preference.getAccessToken());
                if (!TextUtils.isEmpty(preference.getAccessToken()))
                    request = request.newBuilder()
                            .addHeader("Content-type", "application/json")
                            .addHeader("Authorization", "Basic "+ AppUtils.encodeToBase64("x:"+preference.getAccessToken())).build();
                return chain.proceed(request);
            }
        };
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).
                addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        apiRequest = retrofit.create(requestClass);
    }

    public static IApiRequest getRequest() {
        return apiRequest;
    }
}


