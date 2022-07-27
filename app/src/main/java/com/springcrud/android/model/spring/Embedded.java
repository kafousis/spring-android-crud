package com.springcrud.android.model.spring;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Embedded<T> {

    // the other option is to change the payload name at the backend
    @SerializedName(value = "books", alternate = {"authors", "publishers", "genres"})
    private List<T> collection;

    public List<T> getCollection() {
        return collection;
    }

    public void setCollection(List<T> collection) {
        this.collection = collection;
    }

    @Override
    public String toString() {
        return "Embedded{" +
                "collection=" + collection +
                '}';
    }
}
