package com.springcrud.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.springcrud.android.R;

public class BookEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);

        setTitle(R.string.book_edit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // respond to the toolbar back button
        // with this code when going back onResume() is called, NOT onCreate()
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}