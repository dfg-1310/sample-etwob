package com.e2b.api;

import com.e2b.model.response.BaseResponse;
import com.e2b.model.response.CommonApiResponse;
import com.e2b.model.response.PlaceOrder;
import com.google.gson.JsonObject;

import e2b.model.response.MerchantResponse;
import e2b.model.response.Orders;
import e2b.model.response.UserResponse;
import e2b.model.response.VerifiedOTPResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IApiRequest {

    @POST("consumer")
    Call<BaseResponse<UserResponse>> consumer(@Body JsonObject mobileJsonObject);

    @POST("resend/otp")
    Call<BaseResponse<UserResponse>> resendOtp(@Body JsonObject mobileJsonObject);

    @POST("verify/otp")
    Call<BaseResponse<VerifiedOTPResponse>> verifyotp(@Body JsonObject jsonObject);

    @PUT("consumer/{id}")
    Call<BaseResponse<UserResponse>> profileSetup(@Path("id") String userId, @Body JsonObject jsonObject);

    @GET("consumer/{id}")
    Call<BaseResponse<UserResponse>> getProfile(@Path("id") String userId);

    @GET("merchant")
    Call<BaseResponse<MerchantResponse>> getMerchants();

    @FormUrlEncoded
    @POST("profile/forgotPassword")
    Call<BaseResponse<CommonApiResponse>> forgotPasswordAPI(@Field(FIELD.EMAIL) String email);

    @GET("profile/logout")
    Call<BaseResponse<CommonApiResponse>> logout();

    @GET("order")
    Call<BaseResponse<Orders>> getOrders();

    @POST("users")
    Call<BaseResponse<UserResponse>> updateProfileAPI(String mobileNumber, String fullName, String password, String address);

    Call<BaseResponse<UserResponse>> sendOTP(String email);

    @POST("order")
    Call<BaseResponse<PlaceOrder>> placeOrder(@Body JsonObject jsonObject);

    @GET("order/{id}")
    Call<BaseResponse<PlaceOrder>> getOrder(@Path("id") String orderId);


    interface FIELD {
        String ACCESS_TOKEN = "accessToken";
        String NAME = "name";
        String EMAIL = "email";
        String PASSWORD = "password";
        String PASSCODE = "passcode";
        String MOBILE = "mobile";

    }

}