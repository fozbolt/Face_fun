package com.example.facefun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        ImageAdapter adapter = new ImageAdapter(this);
        ImageView imgview = findViewById(R.id.full_picture);

        try {
            InputStream ims = getAssets().open("Gallery_images/" + adapter.list[position]);
            Bitmap bitmap = BitmapFactory.decodeStream(ims);
            imgview.setImageBitmap(bitmap);
            imgview.setRotation(90);
        } catch (IOException e) { e.printStackTrace(); }
    }
}