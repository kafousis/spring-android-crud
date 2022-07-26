package com.springcrud.android.rest;

import com.springcrud.android.model.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookService {

    // - CRUD

    @POST("/api/books")
    Call<Book> create(@Body Book book);

    @GET("/api/books")
    Call<List<Book>> read();

    @PUT("/api/books/{id}")
    Call<Book> update(@Body Book book, @Path("id") Long id);

    @DELETE("/api/books/{id}")
    Call<Void> delete(@Path("id") Long id);

    // ---

    @GET("/api/books/{id}")
    Call<Book> getBookById(@Path("id") Long id);
}
