package com.madushanka.imotorist.network;

import com.madushanka.imotorist.entities.AccessToken;
import com.madushanka.imotorist.entities.Ticket;
import com.madushanka.imotorist.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

    @GET("logged-in-user")
    Call<User> user();

    @GET("get-motorist-ticket")
    Call<List<Ticket>> getAllTickets();


    @POST("register-firebase")
    @FormUrlEncoded
    Call<String> firebase(@Field("firebase_token") String firebase_token);


    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken> refresh(@Field("refresh_token") String refreshToken);


}
