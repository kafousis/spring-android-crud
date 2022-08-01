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
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.springcrud.android.R;
import com.springcrud.android.adapters.AuthorDropdownAdapter;
import com.springcrud.android.adapters.GenreDropdownAdapter;
import com.springcrud.android.adapters.PublisherDropdownAdapter;
import com.springcrud.android.model.Author;
import com.springcrud.android.model.Book;
import com.springcrud.android.model.Genre;
import com.springcrud.android.model.Publisher;
import com.springcrud.android.model.spring.CollectionResponse;
import com.springcrud.android.rest.AuthorService;
import com.springcrud.android.rest.BookService;
import com.springcrud.android.rest.GenreService;
import com.springcrud.android.rest.PublisherService;
import com.springcrud.android.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookEditActivity extends AppCompatActivity implements Validator.ValidationListener{

    // web services size parameter
    private static final int AUTHORS_SIZE = 100;
    private static final int GENRES_SIZE = 30;
    private static final int PUBLISHERS_SIZE = 50;
    // --

    @NotEmpty
    private TextInputEditText titleEditText;

    @NotEmpty
    private AutoCompleteTextView authorsTextView1;
    private AutoCompleteTextView authorsTextView2;

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
    private ProgressBar progressBar;

    private AuthorDropdownAdapter author1_dropdownAdapter;
    private int SELECTED_AUTHOR_1_POSITION;

    private AuthorDropdownAdapter author2_dropdownAdapter;
    private int SELECTED_AUTHOR_2_POSITION;

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
        authorsTextView1 = findViewById(R.id.author_textview_1);
        authorsTextView2 = findViewById(R.id.author_textview_2);
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

        updateBookBtn.setOnClickListener(v -> {

            Author author1 = author1_dropdownAdapter.getItem(SELECTED_AUTHOR_1_POSITION);
            Author author2 = author2_dropdownAdapter.getItem(SELECTED_AUTHOR_2_POSITION);

            if (author1.getId().equals(author2.getId())){
                authorsTextView2.setError(getString(R.string.duplicated_author_error));
                authorsTextView2.requestFocus();
            } else{
                validator.validate();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Long bookId = getIntent().getLongExtra("BOOK_ID", 0L);

        // 4 chained requests
        // - a) getBook(bookId)
        // - b) getAuthors()
        // - c) getGenres()
        // - d) getPublishers()
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
                    getAuthors(book);
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

    private void getAuthors(Book book) {

        AuthorService authorService = RestClient.createService(AuthorService.class);
        Call<CollectionResponse<Author>> getAuthors = authorService.get(AUTHORS_SIZE);

        getAuthors.enqueue(new Callback<CollectionResponse<Author>>() {
            @Override
            public void onResponse(Call<CollectionResponse<Author>> call, Response<CollectionResponse<Author>> response) {

                if (response.isSuccessful()) {
                    List<Author> authors = response.body().getEmbedded().getCollection();
                    showBookAuthors(authors, book.getAuthorsDetails());
                    getGenres(book);
                }else{
                    updateBookBtn.setEnabled(false);
                    progressBar.setVisibility(View.GONE);
                    Toasty.error(BookEditActivity.this, R.string.load_authors_fail, Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<CollectionResponse<Author>> call, Throwable t) {
                updateBookBtn.setEnabled(false);
                progressBar.setVisibility(View.GONE);
                Log.e(RestClient.LOG_TAG, t.getMessage());
                Toasty.error(BookEditActivity.this, getString(R.string.request_fail), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    private void getGenres(Book book) {

        GenreService genreService = RestClient.createService(GenreService.class);
        Call<CollectionResponse<Genre>> getGenres = genreService.get(GENRES_SIZE);

        getGenres.enqueue(new Callback<CollectionResponse<Genre>>() {
            @Override
            public void onResponse(Call<CollectionResponse<Genre>> call, Response<CollectionResponse<Genre>> response) {

                if (response.isSuccessful()) {
                    List<Genre> genres = response.body().getEmbedded().getCollection();
                    showBookGenres(genres, book.getGenreDetails());
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
        Call<CollectionResponse<Publisher>> getPublishers = publisherService.get(PUBLISHERS_SIZE);

        getPublishers.enqueue(new Callback<CollectionResponse<Publisher>>() {
            @Override
            public void onResponse(Call<CollectionResponse<Publisher>> call, Response<CollectionResponse<Publisher>> response) {

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<Publisher> publishers = response.body().getEmbedded().getCollection();
                    showBookPublishers(publishers, book.getPublisherDetails());
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

    private void showBookDetails(Book book) {
        titleEditText.setText(book.getTitle());
        totalPagesEditText.setText(String.valueOf(book.getTotalPages()));
        isbnEditText.setText(book.getIsbn());
        publishedYearEditText.setText(String.valueOf(book.getPublishedYear()));
    }

    private void showBookAuthors(List<Author> authors, List<Author> bookAuthors) {

        author1_dropdownAdapter = new AuthorDropdownAdapter(this, authors);
        authorsTextView1.setAdapter(author1_dropdownAdapter);

        authorsTextView1.setText(bookAuthors.get(0).getFullName());
        SELECTED_AUTHOR_1_POSITION = author1_dropdownAdapter.getPosition(bookAuthors.get(0));

        authorsTextView1.setOnItemClickListener((parent, view, position, id) -> {
            authorsTextView1.setText(author1_dropdownAdapter.getItem(position).getFullName());
            SELECTED_AUTHOR_1_POSITION = position;
        });

        // --

        // "No author" dummy option item
        List<Author> secondaryAuthors = new ArrayList<>();
        Author noAuthorDropdownOption = new Author();
        noAuthorDropdownOption.setFullName(" - No Author -");
        noAuthorDropdownOption.setId(0L);

        secondaryAuthors.add(0, noAuthorDropdownOption);
        secondaryAuthors.addAll(authors);

        // --

        author2_dropdownAdapter = new AuthorDropdownAdapter(this, secondaryAuthors);
        authorsTextView2.setAdapter(author2_dropdownAdapter);

        if (bookAuthors.size() == 2){
            authorsTextView2.setText(bookAuthors.get(1).getFullName());
            SELECTED_AUTHOR_2_POSITION = author2_dropdownAdapter.getPosition(bookAuthors.get(1));
        }else{
            authorsTextView2.setText(author2_dropdownAdapter.getItem(0).getFullName());
            SELECTED_AUTHOR_2_POSITION = 0;
        }

        authorsTextView2.setOnItemClickListener((parent, view, position, id) -> {
            authorsTextView2.setText(author2_dropdownAdapter.getItem(position).getFullName());
            SELECTED_AUTHOR_2_POSITION = position;
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
        String isbn = isbnEditText.getText().toString();
        Integer totalPages = Integer.valueOf(totalPagesEditText.getText().toString());
        Integer publishedYear = Integer.valueOf(publishedYearEditText.getText().toString());

        List<Author> authors = new ArrayList<>();
        //Log.i("SELECTED_AUTHOR_1_POSITION", String.valueOf(SELECTED_AUTHOR_1_POSITION));
        //Log.i("SELECTED_AUTHOR_2_POSITION", String.valueOf(SELECTED_AUTHOR_2_POSITION));

        Author firstAuthor = author1_dropdownAdapter.getItem(SELECTED_AUTHOR_1_POSITION);
        authors.add(firstAuthor);

        if (SELECTED_AUTHOR_2_POSITION > 0){
            Author secondAuthor = author2_dropdownAdapter.getItem(SELECTED_AUTHOR_2_POSITION);
            authors.add(secondAuthor);
        }

        //Log.i("SELECTED_GENRE_POSITION", String.valueOf(SELECTED_GENRE_POSITION));
        Genre genre = genreDropdownAdapter.getItem(SELECTED_GENRE_POSITION);

        //Log.i("SELECTED_PUBLISHER_POSITION", String.valueOf(SELECTED_PUBLISHER_POSITION));
        Publisher publisher = publisherDropdownAdapter.getItem(SELECTED_PUBLISHER_POSITION);

        // --

        Book updatedBook = new Book();
        updatedBook.setTitle(title);
        updatedBook.setIsbn(isbn);
        updatedBook.setTotalPages(totalPages);
        updatedBook.setPublishedYear(publishedYear);

        String publisherUri = RestClient.BASE_URL + "/api/publishers/" + String.valueOf(publisher.getId());
        updatedBook.setPublisher(publisherUri);

        String genreUri = RestClient.BASE_URL + "/api/genres/" + String.valueOf(genre.getId());
        updatedBook.setGenre(genreUri);

        List<String> authorsUris = new ArrayList<>();
        String author_1_uri = RestClient.BASE_URL + "/api/authors/" + String.valueOf(authors.get(0).getId());
        authorsUris.add(author_1_uri);

        if (authors.size() == 2){
            String author_2_uri = RestClient.BASE_URL + "/api/authors/" + String.valueOf(authors.get(1).getId());
            authorsUris.add(author_2_uri);
        }
        updatedBook.setAuthors(authorsUris);
        updateBook(updatedBook, bookId);
    }

    private void updateBook(Book updatedBook, Long bookId) {

        BookService bookService = RestClient.createService(BookService.class);
        Call<Book> updateBook = bookService.update(updatedBook, bookId);
        progressBar.setVisibility(View.VISIBLE);

        updateBook.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {

                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toasty.success(BookEditActivity.this, R.string.book_update_success, Toast.LENGTH_SHORT, true).show();
                }else{
                    Toasty.error(BookEditActivity.this, R.string.book_update_fail, Toast.LENGTH_LONG, true).show();
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(RestClient.LOG_TAG, t.getMessage());
                Toasty.error(BookEditActivity.this, getString(R.string.request_fail), Toast.LENGTH_LONG, true).show();
            }
        });
    }
}