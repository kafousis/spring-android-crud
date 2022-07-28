package com.springcrud.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.springcrud.android.R;
import com.springcrud.android.model.Book;
import com.springcrud.android.rest.BookService;
import com.springcrud.android.rest.RestClient;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookViewActivity extends AppCompatActivity {

    private LinearLayout viewLayout;
    private ProgressBar progressBar;
    private TextView titleTextView;
    private TextView authorsTextView;
    private TextView genreTextView;
    private TextView totalPagesTextView;
    private TextView isbnTextView;
    private TextView publisherNameTextView;
    private TextView publishedYearTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);

        viewLayout = findViewById(R.id.view_layout);
        progressBar = findViewById(R.id.progress_bar);
        titleTextView = findViewById(R.id.title_text_view);
        authorsTextView = findViewById(R.id.authors_text_view);
        genreTextView = findViewById(R.id.genre_text_view);
        totalPagesTextView = findViewById(R.id.total_pages_text_view);
        isbnTextView = findViewById(R.id.isbn_text_view);
        publisherNameTextView = findViewById(R.id.publisher_name_text_view);
        publishedYearTextView = findViewById(R.id.published_year_text_view);

        setTitle(getIntent().getStringExtra("BOOK_TITLE"));

        Long bookId = getIntent().getLongExtra("BOOK_ID", 0L);
        BookService bookService = RestClient.createService(BookService.class);
        Call<Book> bookById = bookService.getBookById(bookId);

        progressBar.setVisibility(View.VISIBLE);
        viewLayout.setVisibility(View.GONE);

        bookById.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {

                progressBar.setVisibility(View.GONE);
                viewLayout.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    String successMsg = getString(R.string.load_book_success, bookId);
                    Toasty.success(BookViewActivity.this, successMsg, Toast.LENGTH_LONG, true).show();
                    showBookDetails(response.body());
                } else {
                    Toasty.error(BookViewActivity.this, R.string.load_book_fail, Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                viewLayout.setVisibility(View.VISIBLE);
                Log.e(RestClient.LOG_TAG, t.getMessage());
                Toasty.error(BookViewActivity.this, getString(R.string.request_fail), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    private void showBookDetails(Book book) {
        titleTextView.setText(book.getTitle());
        authorsTextView.setText(book.getAllAuthors());
        genreTextView.setText(book.getGenre().getName());
        totalPagesTextView.setText(String.valueOf(book.getTotalPages()));
        isbnTextView.setText(book.getIsbn());;
        publisherNameTextView.setText(book.getPublisher().getName());
        publishedYearTextView.setText(String.valueOf(book.getPublishedYear()));
    }
}