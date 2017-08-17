package com.madushanka.imotorist.network;

import com.madushanka.imotorist.entities.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface ApiService {

    @POST("register")
    @FormUrlEncoded
    Call<AccessToken> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<AccessToken> login(@Field("username") String username, @Field("password") String password);

    @POST("logout")
    Call<AccessToken> logout();

    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken> refresh(@Field("refresh_token") String refreshToken);


}
