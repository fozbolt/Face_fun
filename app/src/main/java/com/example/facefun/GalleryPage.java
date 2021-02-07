package com.example.facefun;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

public class GalleryPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_page);

        GridView gridView = (GridView) findViewById(R.id.GalleryGrind);
        gridView.setAdapter(new ImageAdapter(this));
    }
}