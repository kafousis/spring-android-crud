package com.springcrud.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.springcrud.android.R;
import com.springcrud.android.activities.BookViewActivity;
import com.springcrud.android.model.Book;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private Context context;
    private List<Book> books;

    public BookListAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    public void updateListData(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        private final TextView titleTextView;
        private final TextView allAuthorsTextView;

        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.book_card);
            titleTextView = view.findViewById(R.id.book_title);
            allAuthorsTextView = view.findViewById(R.id.book_authors);
        }

        public CardView getCardView() {
            return cardView;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getAllAuthorsTextView() {
            return allAuthorsTextView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_book_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.cardView.setOnClickListener(v -> bookClicked(books.get(position).getId()));
        viewHolder.getTitleTextView().setText(books.get(position).getTitle());
        viewHolder.getAllAuthorsTextView().setText(books.get(position).getAllAuthors());
    }

    private void bookClicked(Long bookId) {
        Intent intent = new Intent(context, BookViewActivity.class);
        intent.putExtra("BOOK_ID", bookId);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}

