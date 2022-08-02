package com.springcrud.android.model;

import java.util.List;

public class Book {

    private Long id;
    private String title;
    private String isbn;
    private Integer totalPages;
    private Integer publishedYear;

    // -- association URIs used for POST/PUT/PATCH
    private String publisher;
    private String genre;
    private List<String> authors;

    // -- projection fields
    private Publisher publisherDetails;
    private Genre genreDetails;
    private List<Author> authorsDetails;
    private String allAuthors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public Publisher getPublisherDetails() {
        return publisherDetails;
    }

    public void setPublisherDetails(Publisher publisherDetails) {
        this.publisherDetails = publisherDetails;
    }

    public Genre getGenreDetails() {
        return genreDetails;
    }

    public void setGenreDetails(Genre genreDetails) {
        this.genreDetails = genreDetails;
    }

    public List<Author> getAuthorsDetails() {
        return authorsDetails;
    }

    public void setAuthorsDetails(List<Author> authorsDetails) {
        this.authorsDetails = authorsDetails;
    }

    public String getAllAuthors() {
        return allAuthors;
    }

    public void setAllAuthors(String allAuthors) {
        this.allAuthors = allAuthors;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", totalPages=" + totalPages +
                ", publishedYear=" + publishedYear +
                ", publisher='" + publisher + '\'' +
                ", genre='" + genre + '\'' +
                ", authors='" + authors + '\'' +
                ", publisherDetails=" + publisherDetails +
                ", genreDetails=" + genreDetails +
                ", authorsDetails=" + authorsDetails +
                ", allAuthors='" + allAuthors + '\'' +
                '}';
    }
}
