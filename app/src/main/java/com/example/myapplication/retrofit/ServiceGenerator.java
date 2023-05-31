package com.example.myapplication.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    public static final String API_BASE_URL = "http://192.168.0.134:8080/";

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();
    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.ACCESSTOKEN), Context.MODE_PRIVATE);

        String authToken = sharedPreferences.getString("token","");
        if(!authToken.equals("")) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(context);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);


                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(logging);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(serviceClass);
    }
}
