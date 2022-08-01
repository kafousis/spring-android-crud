package com.springcrud.android.rest;

import com.springcrud.android.model.Publisher;
import com.springcrud.android.model.spring.CollectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PublisherService {

    @GET("/api/publishers?sort=name,asc")
    Call<CollectionResponse<Publisher>> get(@Query("size") int size);

}
