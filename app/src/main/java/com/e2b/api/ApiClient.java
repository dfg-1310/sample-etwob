package com.e2b.api;


import com.e2b.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static IApiRequest apiRequest;

    public static void init(Class<IApiRequest> requestClass) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(new ApiInterceptor()).addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                .build();
        apiRequest = retrofit.create(requestClass);
    }

    public static IApiRequest getRequest() {
        return apiRequest;
    }
}
