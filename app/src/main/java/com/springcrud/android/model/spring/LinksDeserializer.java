package com.springcrud.android.model.spring;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashMap;

public class LinksDeserializer implements JsonDeserializer<HashMap<String, String>> {

    @Override
    public HashMap<String, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final JsonObject linkJsonObject = json.getAsJsonObject();
        HashMap<String, String> links = new HashMap<>();

        linkJsonObject.entrySet().forEach(linkEntry -> {
            String linkName = linkEntry.getKey();
            String linkHref = linkEntry.getValue().getAsJsonObject().get("href").toString();
            links.put(linkName, linkHref);
        });
        return links;
    }
}
