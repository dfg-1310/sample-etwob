package com.e2b.api;

import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.CommonApiResponse;

import net.hockeyapp.android.metrics.model.User;

import e2b.model.response.UserResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface IApiRequest {

    @FormUrlEncoded
    @POST("signup")
    Call<BaseResponse<UserResponse>> profileSetup(@Field(FIELD.NAME) String name, @Field(FIELD.PASSWORD) String address);

    @FormUrlEncoded
    @POST("authenticate")
    Call<BaseResponse<UserResponse>> signInAPI(@Field(FIELD.EMAIL) String mobile, @Field(FIELD.PASSWORD) String password);

    @FormUrlEncoded
    @POST("user/forgotPassword")
    Call<BaseResponse<CommonApiResponse>> forgotPasswordAPI(@Field(FIELD.EMAIL) String email);

    @GET("user/logout")
    Call<BaseResponse<CommonApiResponse>> logout();

    @POST("users")
    Call<BaseResponse<UserResponse>> updateProfileAPI(String mobileNumber, String fullName, String password, String address);

    Call<BaseResponse<UserResponse>> sendOTP(String email);

    interface FIELD {
        String ACCESS_TOKEN = "accessToken";
        String NAME = "name";
        String EMAIL = "email";
        String PASSWORD = "password";
        String PASSCODE = "passcode";
        String MOBILE = "mobile";

    }

}