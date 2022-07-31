package com.springcrud.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.springcrud.android.R;
import com.springcrud.android.adapters.GenreDropdownAdapter;
import com.springcrud.android.adapters.PublisherDropdownAdapter;
import com.springcrud.android.model.Book;
import com.springcrud.android.model.Genre;
import com.springcrud.android.model.Publisher;
import com.springcrud.android.model.spring.CollectionResponse;
import com.springcrud.android.rest.BookService;
import com.springcrud.android.rest.GenreService;
import com.springcrud.android.rest.PublisherService;
import com.springcrud.android.rest.RestClient;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookEditActivity extends AppCompatActivity implements Validator.ValidationListener{

    // get them all - to show in dropdowns
    private static final int GENRES_SIZE = 30;
    private static final int PUBLISHERS_SIZE = 50;

    private ProgressBar progressBar;

    @NotEmpty
    private TextInputEditText titleEditText;

    @NotEmpty
    private TextInputEditText authorsEditText1;
    private TextInputEditText authorsEditText2;

    @NotEmpty
    private AutoCompleteTextView genreAutoCompleteTextView;

    @NotEmpty
    private TextInputEditText totalPagesEditText;

    @NotEmpty
    private TextInputEditText isbnEditText;

    @NotEmpty
    private AutoCompleteTextView publisherNameAutoCompleteTextView;

    @NotEmpty
    private TextInputEditText publishedYearEditText;

    private Button updateBookBtn;

    private GenreDropdownAdapter genreDropdownAdapter;
    private int SELECTED_GENRE_POSITION;

    private PublisherDropdownAdapter publisherDropdownAdapter;
    private int SELECTED_PUBLISHER_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);

        progressBar = findViewById(R.id.progress_bar);
        titleEditText = findViewById(R.id.title_edit_text);
        authorsEditText1 = findViewById(R.id.author_edit_text_1);
        authorsEditText2 = findViewById(R.id.author_edit_text_2);
        genreAutoCompleteTextView = findViewById(R.id.genre_auto_complete_text_view);
        totalPagesEditText = findViewById(R.id.total_pages_edit_text);
        isbnEditText = findViewById(R.id.isbn_edit_text);
        publisherNameAutoCompleteTextView = findViewById(R.id.publisher_name_auto_complete_text_view);
        publishedYearEditText = findViewById(R.id.published_year_edit_text);
        updateBookBtn = findViewById(R.id.update_btn);

        setTitle(R.string.book_edit);

        // fields validation
        Validator validator = new Validator(this);
        validator.setValidationListener(this);

        updateBookBtn.setOnClickListener(v -> validator.validate());
    }

    @Override
    protected void onResume() {
        super.onResume();

        Long bookId = getIntent().getLongExtra("BOOK_ID", 0L);

        // 3 chained requests
        // - a) getBook(bookId)
        // - b) getGenres()
        // - c) getPublishers()
        getBookById(bookId);
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

    private void getBookById(Long bookId){

        progressBar.setVisibility(View.VISIBLE);

        BookService bookService = RestClient.createService(BookService.class);
        Call<Book> getBook = bookService.getBookById(bookId);

        getBook.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {


                if (response.isSuccessful()) {
                    Book book = response.body();
                    showBookDetails(book);
                    getGenres(book);
                } else {
                    updateBookBtn.setEnabled(false);
                    progressBar.setVisibility(View.GONE);
                    Toasty.error(BookEditActivity.this, R.string.load_book_fail, Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                updateBookBtn.setEnabled(false);
                progressBar.setVisibility(View.GONE);
                Log.e(RestClient.LOG_TAG, t.getMessage());
                Toasty.error(BookEditActivity.this, getString(R.string.request_fail), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    private void getGenres(Book book) {

        GenreService genreService = RestClient.createService(GenreService.class);
        Call<CollectionResponse<Genre>> getGenres = genreService.read(GENRES_SIZE);

        getGenres.enqueue(new Callback<CollectionResponse<Genre>>() {
            @Override
            public void onResponse(Call<CollectionResponse<Genre>> call, Response<CollectionResponse<Genre>> response) {

                if (response.isSuccessful()) {
                    List<Genre> genres = response.body().getEmbedded().getCollection();
                    showBookGenres(genres, book.getGenre());
                    getPublishers(book);
                } else {
                    updateBookBtn.setEnabled(false);
                    progressBar.setVisibility(View.GONE);
                    Toasty.error(BookEditActivity.this, R.string.load_genres_fail, Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<CollectionResponse<Genre>> call, Throwable t) {
                updateBookBtn.setEnabled(false);
                progressBar.setVisibility(View.GONE);
                Log.e(RestClient.LOG_TAG, t.getMessage());
                Toasty.error(BookEditActivity.this, getString(R.string.request_fail), Toast.LENGTH_LONG, true).show();
            }
        });

    }

    private void getPublishers(Book book) {

        PublisherService publisherService = RestClient.createService(PublisherService.class);
        Call<CollectionResponse<Publisher>> getPublishers = publisherService.read(PUBLISHERS_SIZE);

        getPublishers.enqueue(new Callback<CollectionResponse<Publisher>>() {
            @Override
            public void onResponse(Call<CollectionResponse<Publisher>> call, Response<CollectionResponse<Publisher>> response) {

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<Publisher> publishers = response.body().getEmbedded().getCollection();
                    showBookPublishers(publishers, book.getPublisher());
                } else {
                    updateBookBtn.setEnabled(false);
                    Toasty.error(BookEditActivity.this, R.string.load_Publishers_fail, Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<CollectionResponse<Publisher>> call, Throwable t) {
                updateBookBtn.setEnabled(false);
                progressBar.setVisibility(View.GONE);
                Log.e(RestClient.LOG_TAG, t.getMessage());
                Toasty.error(BookEditActivity.this, getString(R.string.request_fail), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    private void showBookGenres(List<Genre> genres, Genre bookGenre) {

        genreDropdownAdapter = new GenreDropdownAdapter(this, genres);
        genreAutoCompleteTextView.setAdapter(genreDropdownAdapter);

        genreAutoCompleteTextView.setText(bookGenre.getName());
        SELECTED_GENRE_POSITION = genreDropdownAdapter.getPosition(bookGenre);

        genreAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            genreAutoCompleteTextView.setText(genreDropdownAdapter.getItem(position).getName());
            SELECTED_GENRE_POSITION = position;
        });
    }

    private void showBookPublishers(List<Publisher> publishers, Publisher bookPublisher) {

        publisherDropdownAdapter = new PublisherDropdownAdapter(this, publishers);
        publisherNameAutoCompleteTextView.setAdapter(publisherDropdownAdapter);

        publisherNameAutoCompleteTextView.setText(bookPublisher.getName());
        SELECTED_PUBLISHER_POSITION = publisherDropdownAdapter.getPosition(bookPublisher);

        publisherNameAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            publisherNameAutoCompleteTextView.setText(publisherDropdownAdapter.getItem(position).getName());
            SELECTED_PUBLISHER_POSITION = position;
        });
    }

    private void showBookDetails(Book book) {

        titleEditText.setText(book.getTitle());
        authorsEditText1.setText(book.getAuthors().get(0).getFullName());

        if (book.getAuthors().size() == 2){
            authorsEditText2.setText(book.getAuthors().get(1).getFullName());
        }else{
            authorsEditText2.setEnabled(false);
        }

        totalPagesEditText.setText(String.valueOf(book.getTotalPages()));
        isbnEditText.setText(book.getIsbn());
        publishedYearEditText.setText(String.valueOf(book.getPublishedYear()));
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            EditText view = (EditText) error.getView();
            String message = error.getCollatedErrorMessage(this);
            view.setError(message);
        }

        // set focus on the first field that has error
        int position = errors.size()-1;
        EditText view = (EditText) errors.get(position).getView();
        view.requestFocus();
    }

    @Override
    public void onValidationSucceeded() {

        Long bookId = getIntent().getLongExtra("BOOK_ID", 0L);

        String title = titleEditText.getText().toString();
        String author1 = authorsEditText1.getText().toString();
        String author2 = authorsEditText2.getText().toString();
        String isbn = isbnEditText.getText().toString();
        Integer totalPages = Integer.valueOf(totalPagesEditText.getText().toString());
        Integer publishedYear = Integer.valueOf(publishedYearEditText.getText().toString());

        //Log.i("SELECTED_GENRE_POSITION", String.valueOf(SELECTED_GENRE_POSITION));
        Genre genre = genreDropdownAdapter.getItem(SELECTED_GENRE_POSITION);

        //Log.i("SELECTED_PUBLISHER_POSITION", String.valueOf(SELECTED_PUBLISHER_POSITION));
        Publisher publisher = publisherDropdownAdapter.getItem(SELECTED_PUBLISHER_POSITION);

        // TODO handle authors
        // TODO update book
    }
}