package com.example.facefun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.List;


public class UploadPhoto extends AppCompatActivity {

    public static final int  LOAD_IMAGE_REQUEST = 1;
    private static final int TAKE_PICTURE_REQUEST = 2;
    public  static final int PERMISSION_CODE_REQUEST  = 3 ;
    public byte[] picture;
    public byte[] picture2;
    String ImageFilePath;
    String ImageFilePath2;
    ImageButton imageButton ;
    ImageView imageView ;
    Bitmap bitmap;
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

            TextView txt = (TextView) findViewById(R.id.resultText);
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



    public void LaunchResultPage(View v){

        if (!Python.isStarted()) { Python.start(new AndroidPlatform(this)); }
        //stvaranje python instance
        Python py = Python.getInstance();
        //stvaranje python objekta
        PyObject py_obj = null;

        try {
            py_obj = py.getModule(choice);
        }
        catch (Error e){
            System.out.print("Unable to load python module");
        }

        Intent i = new Intent(this, ResultPage.class);


        switch (choice){
            case "face_swap":{

                //ako su obje slike uspješno učitane
                if(picture!= null && picture2 != null){

                    PyObject object = py_obj.callAttr("main", picture, picture2, ImageFilePath, ImageFilePath2);
                    String str = object.toString();
                    byte data[] = android.util.Base64.decode(str, Base64.DEFAULT);

                    i.putExtra("image",data);
                    i.putExtra("result", "You can save result image or try again");
                }

                break;
            }

            case "age_and_gender_detection":{
                //int[] data = pyobj_age_and_gender.callAttr("main", upload_image).toJava(int[].class);  --> ako želimo slati array

                String result = py_obj.callAttr("main",ImageFilePath, picture).toString();
                i.putExtra("result", result);

                break;
            }

            case "celebrity_face_recognition":{
                String result = py_obj.callAttr("main", ImageFilePath).toString();
                i.putExtra("result", result);
                break;
            }

            case "celebrity_look_alike":{
                List<PyObject> celebritys = py_obj.callAttr("main", ImageFilePath).asList(); //java list
                String [] celeb_array = new String[5];
                for(int x=0; x < celebritys.size(); x++){
                    String temp = celebritys.get(x).toString();
                    celeb_array[x] = temp;
                }
                i.putExtra("result", celeb_array);
                break;
            }
        }


        if(!choice.equals("face_swap")){

            //uz rezultat na sljedeći activity šaljemo i choice da bi znali kako prikazati rezultat
            i.putExtra("choice", choice);
            //Convert to byte array -> slanje slike
            i.putExtra("image",picture);

            picture = null;
            ImageFilePath = null;

            startActivity(i);
        }

        else if(choice.equals("face_swap") && picture!= null && picture2 != null){

            i.putExtra("choice", choice);
            picture = null;
            ImageFilePath = null;
            startActivity(i);
        }
        else if(picture!= null && picture2 ==null){

            TextView txt = (TextView) findViewById(R.id.resultText);
            txt.setText(this.getString(R.string.upload_message_face_swap2_before));
            imageView.setImageResource(R.drawable.upload_image_icon);
        }
        else{
            System.out.println("Unknown error");
        }

    }

/*
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private void createFolder() {
        if (isStoragePermissionGranted()) {
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "DebugData");

            if (!folder.exists()) {
                folder.mkdir();
            }
        }
    }
*/

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

    private String createDirectoryAndSaveFile(Bitmap imageToSave) {
        File imagefile;
        File directory = new File(Environment.getExternalStorageDirectory() + "/Image_Storage");
        if (!directory.exists()) { directory.mkdir(); }

        if(ImageFilePath == null) imagefile= new File(directory,"Upload_image.png");
        else imagefile= new File(directory,"Upload_image2.png");

        if (imagefile.exists()) { imagefile.delete(); }
        String ImageFilePath = imagefile.toString();

        try {
            FileOutputStream out = new FileOutputStream(imagefile);
            imageToSave.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) { e.printStackTrace(); }

        return ImageFilePath;
    }

    @Override
    // handleri nakon fotografiranja ili odabira fotografije
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");

            if(ImageFilePath == null) ImageFilePath = createDirectoryAndSaveFile(bitmap);
            else ImageFilePath2  = createDirectoryAndSaveFile(bitmap);

            //Konvertiranje u byte array
            ByteArrayOutputStream stream = null;
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            if(picture == null) picture = stream.toByteArray();
            else picture2 = stream.toByteArray();


            if(imageView!=null){
                imageView.setImageBitmap(bitmap);

                TextView text = (TextView) findViewById(R.id.resultText);
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

            //imageView = findViewById(R.id.imageView_upload_icon);

            imageView.setImageDrawable(Drawable.createFromPath(picturePath));
            //Promijeni tekstualne upute ispod slike
            TextView text = (TextView) findViewById(R.id.resultText);
            text.setText(this.getString(R.string.upload_message_face_swap_after));

            imageView.setDrawingCacheEnabled(true);
            bitmap = imageView.getDrawingCache();


            if(ImageFilePath == null) ImageFilePath = createDirectoryAndSaveFile(bitmap);
            else ImageFilePath2 = createDirectoryAndSaveFile(bitmap);

            //Konvertiranje u byte array za 2. način obrade
            ByteArrayOutputStream stream = null;
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            if(picture == null) picture = stream.toByteArray();
            else picture2 = stream.toByteArray();
        }

    }

}
