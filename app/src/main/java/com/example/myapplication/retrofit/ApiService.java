package com.example.myapplication.retrofit;


import com.example.myapplication.model.AddOpinionRequest;
import com.example.myapplication.model.AddPlaceRequest;
import com.example.myapplication.model.JwtResponse;
import com.example.myapplication.model.LoginRequest;
import com.example.myapplication.model.RegisterRequest;
import com.example.myapplication.model.Opinion;
import com.example.myapplication.model.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/place/allNearbyPlaces/{latitude}/{longitude}/{radius}")
    Call<List<Place>> getAllPlaces(@Path("latitude") double latitude,@Path("longitude") double longitude,@Path("radius") int radius);

    @GET("/place/allNearbyPlaces/{latitude}/{longitude}/{radius}/{types}")
    Call<List<Place>> getAllPlaces(@Path("latitude") double latitude,@Path("longitude") double longitude,@Path("radius") int radius,@Path("types")String types);

    @POST("/api/auth/signin")
    Call<JwtResponse> authenticateUser(@Body LoginRequest loginRequest);

    @POST("/opinion")
    Call<Opinion> addOpinion(@Query("placeId") Long placeId, @Query("userId") Long userId, @Body AddOpinionRequest addOpinionRequest);

    @POST("/user")
    Call<Opinion> createUser(@Body RegisterRequest registerRequest);

    @GET("/place")
    Call<Place> getPlaceById(@Query("placeId") Long placeId);

    @POST("/place")
    Call<Place> createPlace(@Body AddPlaceRequest addPlaceRequest);

    @POST("/api/auth/forgotPassword")
    Call<Void> recoverPassword(@Query("email") String email);
}
