package com.example.facefun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView myImageView;
    private static final int REQUEST_IMAGE_CAPTURE = 101; //nezz zasto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Face fun"); //za main activity

        myImageView = findViewById(R.id.button_gallery);

        // https://stackoverflow.com/questions/6016000/how-to-open-phones-gallery-through-code

    }

    public void launchPhotoUpload(View v){
        Intent i = new Intent(this, UploadPhoto.class);
        startActivity(i);
    }
    public void launchGallery(View v){
        Intent i = new Intent(this, GalleryPage.class);
        startActivity(i);
    }
}