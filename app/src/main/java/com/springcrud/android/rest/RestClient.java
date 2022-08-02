package com.springcrud.android.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.springcrud.android.model.spring.LinksDeserializer;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    // https://square.github.io/retrofit/

    public static final int AUTHORS_SIZE = 100;
    public static final int GENRES_SIZE = 30;
    public static final int PUBLISHERS_SIZE = 50;

    // TODO RecyclerView Pagination
    public static final int BOOK_SIZE = 200;

    public static final String LOG_TAG = "RestClient";
    public static final String BASE_URL = "http://192.168.1.10:8080";

    private static Retrofit.Builder builder
            = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            //.addConverterFactory(GsonConverterFactory.create()
            .addConverterFactory(buildGsonConverter());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Adding custom deserializers
        gsonBuilder.registerTypeAdapter(HashMap.class, new LinksDeserializer());
        Gson myGson = gsonBuilder.create();

        return GsonConverterFactory.create(myGson);
    }
}
