package com.example.facefun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

public class GalleryPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_page);

        GridView gridView = findViewById(R.id.GalleryGrind);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent (getApplicationContext(), FullScreenImageActivity.class);
            i.putExtra("id",position);
            startActivity(i);
        });
    }
}