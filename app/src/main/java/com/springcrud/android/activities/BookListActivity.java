package com.springcrud.android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

import com.springcrud.android.R;
import com.springcrud.android.model.Book;
import com.springcrud.android.rest.BookService;
import com.springcrud.android.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

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
        bookListAdapter = new BookListAdapter(new ArrayList<>());
        booksRecyclerView.setAdapter(bookListAdapter);

        BookService bookService = RestClient.createService(BookService.class);
        Call<List<Book>> readBooksCall = bookService.read();

        readBooksCall.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                if (response.isSuccessful()){

                    List<Book> books = response.body();
                    bookListAdapter.updateBookList(books);

                }else{
                    Toasty.error(BookListActivity.this, R.string.load_books_failed, Toast.LENGTH_SHORT, true).show();

                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.e(RestClient.LOG_TAG, t.getMessage());
                Toasty.error(BookListActivity.this, getString(R.string.request_failed), Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}