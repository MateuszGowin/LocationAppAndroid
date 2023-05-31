package com.example.myapplication.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private Context context;

    public AuthenticationInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.ACCESSTOKEN), Context.MODE_PRIVATE);

        String authToken = "Bearer " + sharedPreferences.getString("token","");

        Request.Builder builder = original.newBuilder()
                .header("Authorization", authToken);

        Request request = builder.build();
        Log.d("AUTH",authToken);
        return chain.proceed(request);
    }
}
