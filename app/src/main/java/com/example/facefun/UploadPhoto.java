package com.example.facefun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class UploadPhoto extends AppCompatActivity {

    //private ImageView myImageView;
    //private static final int REQUEST_IMAGE_CAPTURE = 101; //nezz zasto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
     
    }

    public void LaunchResultPage(View v){
        Intent i = new Intent(this, ResultPage.class);
        startActivity(i);
    }
/*
    //youtube staviti link
    public void takePicture(View v){
        Intent imageTakenIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageTakenIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(imageTakenIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode ==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK)
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        myImageView.setImageBitmap(imageBitmap);

    }

    */


    /* https://stackoverflow.com/questions/6016000/how-to-open-phones-gallery-through-code */

}