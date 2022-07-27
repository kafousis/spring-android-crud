package com.springcrud.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

import com.springcrud.android.R;
import com.springcrud.android.model.Book;
import com.springcrud.android.model.spring.CollectionResponse;
import com.springcrud.android.rest.BookService;
import com.springcrud.android.rest.RestClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookListActivity extends AppCompatActivity {

    private RecyclerView booksRecyclerView;
    private BookListAdapter bookListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        booksRecyclerView = findViewById(R.id.books_recycler_view);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookListAdapter = new BookListAdapter(new ArrayList<>());
        booksRecyclerView.setAdapter(bookListAdapter);

        BookService bookService = RestClient.createService(BookService.class);
        Call<CollectionResponse<Book>> readBooksCall = bookService.read();

        readBooksCall.enqueue(new Callback<CollectionResponse<Book>>() {
            @Override
            public void onResponse(Call<CollectionResponse<Book>> call, Response<CollectionResponse<Book>> response) {

                if (response.isSuccessful()) {
                    bookListAdapter.updateListData(response.body().getEmbedded().getCollection());
                } else {
                    Toasty.error(BookListActivity.this, R.string.load_books_failed, Toast.LENGTH_SHORT, true).show();

                }
            }

            @Override
            public void onFailure(Call<CollectionResponse<Book>> call, Throwable t) {
                Log.e(RestClient.LOG_TAG, t.getMessage());
                Toasty.error(BookListActivity.this, getString(R.string.request_failed), Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}