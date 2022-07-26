package com.springcrud.android.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.springcrud.android.R;
import com.springcrud.android.model.Book;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private List<Book> books;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.book_title);
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param books List<Book> containing the data to populate views to be used by RecyclerView.
     */
    public BookListAdapter(List<Book> books) {
        this.books = books;
    }

    public void updateBookList(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_book, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTitleTextView().setText(books.get(position).getTitle());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return books.size();
    }
}

