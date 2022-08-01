package com.springcrud.android.rest;

import com.springcrud.android.model.Book;
import com.springcrud.android.model.spring.CollectionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookService {

    @POST("/api/books")
    Call<Book> create(@Body Book book);

    @GET("/api/books?projection=bookDetail&sort=title,asc")
    Call<CollectionResponse<Book>> get(@Query("size") int size);

    @GET("/api/books/{id}?projection=bookDetail")
    Call<Book> getBookById(@Path("id") Long id);

    // https://github.com/spring-projects/spring-data-rest/issues/1374
    // currently PUT does not update associations, but PATCH does
    @PATCH("/api/books/{id}")
    Call<Book> update(@Body Book book, @Path("id") Long id);

    @DELETE("/api/books/{id}")
    Call<Void> delete(@Path("id") Long id);
}
