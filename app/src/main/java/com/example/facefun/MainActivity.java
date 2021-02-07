package com.example.facefun;

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



    public void launchPhotoUpload(View v){
        View swap = findViewById(R.id.LinearLayout_face_swap);
        View age = findViewById(R.id.LinearLayout_age_detection);
        View celeb = findViewById(R.id.LinearLayout_celeb_fr);
        View lookalike = findViewById(R.id.LinearLayout_celeb_lookalike);

        String activity = "";
        if (v.getId() == swap.getId())   activity = "face_swap";
        else if (v.getId() == age.getId())   activity= "age_detection";
        else if (v.getId() == celeb.getId())  activity= "celeb_fr";
        else if (v.getId() == lookalike.getId())  activity= "celeb_lookalike";


        Intent i = new Intent(this, UploadPhoto.class);
        i.putExtra("activity", activity);
        startActivity(i);
    }

}