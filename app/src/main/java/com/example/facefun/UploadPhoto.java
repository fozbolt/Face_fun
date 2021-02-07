package com.example.facefun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadPhoto extends AppCompatActivity {

    public static final int  LOAD_IMAGE_REQUEST = 1;
    private static final int TAKE_PICTURE_REQUEST = 2;
    public  static final int PERMISSION_CODE_REQUEST  = 3 ;
    ImageButton imageButton ;
    ImageView imageView ;
    Intent intent ;
    Intent i;
    String choice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        //dohvacanje stringa da znamo koju aktivnost je korisnik odabrao
        i = getIntent();
        String activity = i.getExtras().getString("activity");
        choice = activity;

        if (activity.equals("face_swap")){
            //promijeniti WarningMessage u nešto blaže
            TextView txt = (TextView) findViewById(R.id.WarningMessage);
            txt.setText(this.getString(R.string.upload_message_face_swap_before));

        }

        //API za učitavanje slike iz galerije
        // https://www.viralpatel.net/pick-image-from-galary-android-app/
        ImageButton buttonLoadImage = (ImageButton) findViewById(R.id.button_gallery);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, LOAD_IMAGE_REQUEST);
            }
        });


        //API za fotografiranje
        // https://www.android-examples.com/capture-image-from-camera-and-display-in-imageview-android/
        imageButton = (ImageButton) findViewById(R.id.take_picture_button);

        imageView = (ImageView)findViewById(R.id.imageView_upload_icon);
        EnableRuntimePermission();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PICTURE_REQUEST);

            }
        });
    }

    //tu ide python API logika
    public void processImage(){
        System.out.println("Privremeni test");
        switch (choice){
            case "face_swap":{
                System.out.println("Test 1");
                break;
            }
            case "age_detection":{
                System.out.println("Test 2");
                break;
            }
            case "celeb_fr":{
                System.out.println("Test 3");
                break;
            }
            case "celeb_lookalike":{
                System.out.println("Test 4");
                break;
            }
        }

    };

    //intent koji se triggera klikom na "next" button
    public void LaunchResultPage(View v){

        //nekako jos staviti da tu returna rezultat i salje ga u sljedeci intent
        processImage();

        Intent i = new Intent(this, ResultPage.class);
        startActivity(i);
    }



    //za novije verzije kao što su API 23 +, ANDROID 6.0 +
    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(UploadPhoto.this,
                Manifest.permission.CAMERA))
        {
            Log.d("camera_permission", "CAMERA permission allows us to Access CAMERA app");
            //Toast.makeText(UploadPhoto.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(UploadPhoto.this,new String[]{
                    Manifest.permission.CAMERA}, PERMISSION_CODE_REQUEST);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case PERMISSION_CODE_REQUEST:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("camera_permission", "Permission Granted, Now your application can access CAMERA. ");
                    //Toast.makeText(UploadPhoto.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {
                    Log.d("camera_permission", "Permission Canceled, Now your application cannot access CAMERA.");
                    //Toast.makeText(UploadPhoto.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }



    @Override
    // handleri nakon fotografiranja ili odabira fotografije
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            if(imageView!=null){
                imageView.setImageBitmap(bitmap);
                //Promijeni tekstualne upute ispod slike
                TextView text = (TextView) findViewById(R.id.WarningMessage);
                text.setText(this.getString(R.string.upload_message_face_swap_after));
            }
        }

        else if (requestCode == LOAD_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageDrawable(Drawable.createFromPath(picturePath));
            //Promijeni tekstualne upute ispod slike
            TextView text = (TextView) findViewById(R.id.WarningMessage);
            text.setText(this.getString(R.string.upload_message_face_swap_after));

        }
    }

}
