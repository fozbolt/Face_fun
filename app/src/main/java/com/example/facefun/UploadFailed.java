package com.example.facefun;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class UploadFailed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        Intent i = getIntent();

    }

    public void goBack(View v){
        super.onBackPressed();
    }


    //spremanje obrađene slike na mobitel
    public void saveImage(View v){

         //toast ne radi
        Toast.makeText(getApplicationContext(),"No image available to save",Toast.LENGTH_SHORT).show();

        Toast toast = Toast.makeText(UploadFailed.this, "Task Saved", Toast.LENGTH_SHORT);
        toast.show();


    }

    //intent koji vodi na početnu stranu
    public void launchMainActivity(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }



}