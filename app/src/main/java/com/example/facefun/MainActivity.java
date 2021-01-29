package com.example.facefun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Face fun"); //za main activity
    }

    public void launchPhotoUpload(View v){
        Intent i = new Intent(this, UploadPhoto.class);
        startActivity(i);
    }
}