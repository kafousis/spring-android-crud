package com.springcrud.android.model;

import java.util.List;

public class Author {

    private String fullName;
    private String country;
    private List<Book> books;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Author{" +
                "fullName='" + fullName + '\'' +
                ", country='" + country + '\'' +
                ", books=" + books +
                '}';
    }
}
