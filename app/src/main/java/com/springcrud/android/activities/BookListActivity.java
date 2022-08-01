package com.springcrud.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

import com.springcrud.android.R;
import com.springcrud.android.adapters.BookListAdapter;
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
    private ProgressBar progressBar;

    // TODO RecyclerView Pagination
    private static final int BOOK_SIZE = 180;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        //Log.i("BookListActivity", "onCreate()");

        setTitle(R.string.book_list);

        progressBar = findViewById(R.id.progress_bar);
        booksRecyclerView = findViewById(R.id.books_recycler_view);

        bookListAdapter = new BookListAdapter(this, new ArrayList<>());
        booksRecyclerView.setAdapter(bookListAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        booksRecyclerView.setLayoutManager(layoutManager);
        booksRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.i("BookListActivity", "onResume()");
        loadData();
    }

    private void loadData() {

        BookService bookService = RestClient.createService(BookService.class);
        Call<CollectionResponse<Book>> getBooks = bookService.get(BOOK_SIZE);
        progressBar.setVisibility(View.VISIBLE);

        getBooks.enqueue(new Callback<CollectionResponse<Book>>() {
            @Override
            public void onResponse(Call<CollectionResponse<Book>> call, Response<CollectionResponse<Book>> response) {

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    bookListAdapter.updateListData(response.body().getEmbedded().getCollection());
                    String successMsg = getString(R.string.load_books_success, response.body().getEmbedded().getCollection().size());
                    Toasty.success(BookListActivity.this, successMsg, Toast.LENGTH_SHORT, true).show();
                } else {
                    Toasty.error(BookListActivity.this, R.string.load_books_fail, Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<CollectionResponse<Book>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(RestClient.LOG_TAG, t.getMessage());
                Toasty.error(BookListActivity.this, getString(R.string.request_fail), Toast.LENGTH_LONG, true).show();
            }
        });
    }
}