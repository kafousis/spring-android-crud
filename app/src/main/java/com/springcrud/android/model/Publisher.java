package com.springcrud.android.model;

import java.util.List;

public class Publisher {

    private String name;
    private String country;
    private List<Book> books;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Publisher{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", books=" + books +
                '}';
    }
}
