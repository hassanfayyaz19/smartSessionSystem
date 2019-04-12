package com.example.hassan.smartsession.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

   /* public static final String Base_URL = "http://hassan105.000webhostapp.com/smartSession/";*/
  /*  public static final String Base_URL="http://192.168.43.251/php/smart/";*/
    public static final String Base_URL="http://smartsessionqr.com/mobile/";


    public static Retrofit retrofit = null;

    public static Retrofit getApiClient() {

        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
