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
import com.springcrud.android.model.Genre;

import java.util.List;

public class GenreDropdownAdapter extends ArrayAdapter<Genre> {

    private Context context;
    private List<Genre> genres;

    public GenreDropdownAdapter(@NonNull Context context, List<Genre> genres) {
        super(context, 0, genres);
        this.context = context;
        this.genres = genres;
    }

    @Override
    public int getCount() {
        return genres.size();
    }

    @Nullable
    @Override
    public Genre getItem(int position) {
        return genres.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dropdown_item, parent, false);
        }
        TextView dropDownItemTextView = convertView.findViewById(R.id.dropdown_item_text_view);
        dropDownItemTextView.setText(getItem(position).getName());
        return convertView;
    }
}
