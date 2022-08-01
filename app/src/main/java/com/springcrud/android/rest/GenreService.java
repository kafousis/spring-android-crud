package com.springcrud.android.rest;

import com.springcrud.android.model.Book;
import com.springcrud.android.model.Genre;
import com.springcrud.android.model.spring.CollectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenreService {

    @GET("/api/genres?sort=name,asc")
    Call<CollectionResponse<Genre>> get(@Query("size") int size);

}
