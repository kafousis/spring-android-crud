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
import com.springcrud.android.model.Publisher;

import java.util.List;

public class PublisherDropdownAdapter extends ArrayAdapter<Publisher> {

    private Context context;
    private List<Publisher> publishers;

    public PublisherDropdownAdapter(@NonNull Context context, @NonNull List<Publisher> publishers) {
        super(context, 0, publishers);
        this.context = context;
        this.publishers = publishers;
    }

    @Override
    public int getCount() {
        return publishers.size();
    }

    @Nullable
    @Override
    public Publisher getItem(int position) {
        return publishers.get(position);
    }

    @Override
    public int getPosition(@Nullable Publisher item) {

        for (Publisher publisher : publishers){
            if (item.getId().equals(publisher.getId())){
                return publishers.indexOf(publisher);
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
        dropDownItemTextView.setText(getItem(position).getName());
        return convertView;
    }
}
