package com.springcrud.android.rest;

import com.springcrud.android.model.Book;
import com.springcrud.android.model.Genre;
import com.springcrud.android.model.spring.CollectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GenreService {

    @GET("/api/genres")
    Call<CollectionResponse<Genre>> read();

}
