package com.springcrud.android.rest;

import com.springcrud.android.model.Author;
import com.springcrud.android.model.spring.CollectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthorService {

    @GET("/api/authors?sort=fullName,asc")
    Call<CollectionResponse<Author>> get(@Query("size") int size);
}
