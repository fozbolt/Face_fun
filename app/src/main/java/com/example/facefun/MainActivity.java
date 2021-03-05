package com.example.facefun;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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


    public void launchGallery(View v){
        Intent i = new Intent(this,GalleryPage.class);
        startActivity(i);
    }



    public void launchPhotoUpload(View v){
        View swap = findViewById(R.id.LinearLayout_face_swap);
        View age = findViewById(R.id.LinearLayout_age_detection);
        View celeb = findViewById(R.id.LinearLayout_celeb_fr);
        View lookalike = findViewById(R.id.LinearLayout_celeb_lookalike);

        String activity = "";
        if (v.getId() == swap.getId())   activity = "face_swap";
        else if (v.getId() == age.getId())   activity= "age_and_gender_detection";
        else if (v.getId() == celeb.getId())  activity= "celebrity_face_recognition";
        else if (v.getId() == lookalike.getId())  activity= "celebrity_look_alike";


        Intent i = new Intent(this, UploadPhoto.class);
        i.putExtra("activity", activity);
        startActivity(i);
    }

}