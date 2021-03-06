package com.sirapp.RetrofitApi;

import com.sirapp.API.AllSirApi;
import com.sirapp.Utils.UnsafeOkHttpClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {


    private static Retrofit retrofit = null;
    private static final String BASE_URL = AllSirApi.BASE_URL;

    public static Retrofit getRetrofitInstance() {

        OkHttpClient okHttpClient= UnsafeOkHttpClient.getUnsafeOkHttpClient();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }


//    private static OkHttpClient getOkHttpClientTimeOut(){
//        return new OkHttpClient().newBuilder()
//                .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.SECONDS)
//                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
//                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
//                .build();
//
//    }









}
