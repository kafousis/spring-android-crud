package com.springcrud.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
    private Button updateBookBtn;
    private Button deleteBookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);
        //Log.i("BookViewActivity", "onCreate()");

        viewLayout = findViewById(R.id.view_layout);
        progressBar = findViewById(R.id.progress_bar);
        titleTextView = findViewById(R.id.title_text_view);
        authorsTextView = findViewById(R.id.authors_text_view);
        genreTextView = findViewById(R.id.genre_text_view);
        totalPagesTextView = findViewById(R.id.total_pages_text_view);
        isbnTextView = findViewById(R.id.isbn_text_view);
        publisherNameTextView = findViewById(R.id.publisher_name_text_view);
        publishedYearTextView = findViewById(R.id.published_year_text_view);
        updateBookBtn = findViewById(R.id.update_btn);
        deleteBookBtn = findViewById(R.id.delete_btn);

        setTitle(R.string.book_view);

        Long bookId = getIntent().getLongExtra("BOOK_ID", 0L);
        updateBookBtn.setOnClickListener(v -> bookUpdateClicked(bookId));
        deleteBookBtn.setOnClickListener(v -> bookDeleteClicked(bookId));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.i("BookViewActivity", "onResume()");

        Long bookId = getIntent().getLongExtra("BOOK_ID", 0L);
        //Log.i("BookViewActivity", "bookId="+ String.valueOf(bookId));

        loadBookDetails(bookId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // respond to the toolbar back button
        // with this code when going back onResume() is called, NOT onCreate()
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadBookDetails(Long bookId){

        BookService bookService = RestClient.createService(BookService.class);
        Call<Book> getBook = bookService.getBookById(bookId);

        progressBar.setVisibility(View.VISIBLE);
        viewLayout.setVisibility(View.GONE);

        getBook.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {

                progressBar.setVisibility(View.GONE);
                viewLayout.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    String successMsg = getString(R.string.load_book_success, bookId);
                    Toasty.success(BookViewActivity.this, successMsg, Toast.LENGTH_SHORT, true).show();
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
        genreTextView.setText(book.getGenreDetails().getName());
        totalPagesTextView.setText(String.valueOf(book.getTotalPages()));
        isbnTextView.setText(book.getIsbn());;
        publisherNameTextView.setText(book.getPublisherDetails().getName());
        publishedYearTextView.setText(String.valueOf(book.getPublishedYear()));
    }

    private void bookUpdateClicked(Long bookId){
        Intent intent = new Intent(this, BookEditActivity.class);
        intent.putExtra("BOOK_ID", bookId);
        startActivity(intent);
    }

    private void bookDeleteClicked(Long bookId){
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.delete)
                .setMessage(R.string.delete_dialog_message)
                .setNegativeButton(getString(R.string.dialog_option_cancel), (dialog, which) -> {})
                .setPositiveButton(getString(R.string.dialog_option_ok), (dialog, which) -> deleteBook(bookId))
                .show();
    }

    private void deleteBook(Long bookId) {

        progressBar.setVisibility(View.VISIBLE);

        updateBookBtn.setEnabled(false);
        deleteBookBtn.setEnabled(false);

        BookService bookService = RestClient.createService(BookService.class);
        Call<Void> deleteBook = bookService.delete(bookId);

        deleteBook.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    String successMsg = getString(R.string.delete_book_success, bookId);
                    Toasty.success(BookViewActivity.this, successMsg, Toast.LENGTH_SHORT, true).show();

                    // go to list activity
                    finish();
                } else {
                    Toasty.error(BookViewActivity.this, R.string.delete_book_fail, Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(RestClient.LOG_TAG, t.getMessage());
                Toasty.error(BookViewActivity.this, getString(R.string.request_fail), Toast.LENGTH_LONG, true).show();
            }
        });
    }
}