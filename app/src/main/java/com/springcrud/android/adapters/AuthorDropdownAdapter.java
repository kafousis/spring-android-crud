package com.springcrud.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.springcrud.android.R;
import com.springcrud.android.model.Author;

import java.util.List;

public class AuthorDropdownAdapter extends ArrayAdapter<Author> {

    private Context context;
    private List<Author> authors;

    public AuthorDropdownAdapter(@NonNull Context context, @NonNull List<Author> authors) {
        super(context, 0, authors);
        this.context = context;
        this.authors = authors;
    }

    @Override
    public int getCount() {
        return authors.size();
    }

    @Nullable
    @Override
    public Author getItem(int position) {
        return authors.get(position);
    }

    @Override
    public int getPosition(@Nullable Author item) {

        for (Author author : authors){
            if (item.getId().equals(author.getId())){
                return authors.indexOf(author);
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dropdown_item, parent, false);
        }
        TextView dropDownItemTextView = convertView.findViewById(R.id.dropdown_item_text_view);
        dropDownItemTextView.setText(getItem(position).getFullName());
        return convertView;
    }
}
