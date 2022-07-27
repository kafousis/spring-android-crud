package com.springcrud.android.model.spring;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class CollectionResponse<T> {

    @SerializedName("_embedded")
    private Embedded<T> embedded;

    @SerializedName("_links")
    private HashMap<String, String> links;

    private Page page;

    public Embedded<T> getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded<T> embedded) {
        this.embedded = embedded;
    }

    public HashMap<String, String> getLinks() {
        return links;
    }

    public void setLinks(HashMap<String, String> links) {
        this.links = links;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "CollectionResponse{" +
                "embedded=" + embedded +
                ", links=" + links +
                ", page=" + page +
                '}';
    }
}
