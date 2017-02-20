package com.e2b.api;

import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.CommonApiResponse;
import com.google.gson.JsonObject;

import e2b.model.response.UserResponse;
import e2b.model.response.VerifiedOTPResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IApiRequest {

    @POST("consumer")
    Call<BaseResponse<UserResponse>> consumer(@Body JsonObject mobileJsonObject);

    @POST("consumer/verify/otp")
    Call<BaseResponse<VerifiedOTPResponse>> verifyotp(@Body JsonObject jsonObject);

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