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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
    String ImageFilePath;
    public byte[] picture;
    int proba = 0;
    public byte[] picture2;
    public int[] pic_arr;
    ImageButton imageButton ;
    ImageView imageView ;
    Bitmap bitmap;
    Intent intent ;
    Intent i;
    String choice;
    private static final int PERMISSION_REQUEST_CODE = 1;

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

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));

            //stvaranje python instance
            Python py = Python.getInstance();
            //stvaranje python objekta
            PyObject upload_image = null;
            PyObject py_obj = null;

            try {
                upload_image = PyObject.fromJava(picture);
                py_obj = py.getModule(choice);
            }
            catch (Error e){
                System.out.print("Unable to load python module");
            }

            Intent i = new Intent(this, ResultPage.class);


            switch (choice){
                case "face_swap":{

                    //int[][][] image_arr = py_obj.callAttr("main", upload_image).toJava(int[][][].class);
                    List<PyObject> obj = py_obj.callAttr("main", upload_image).asList();
                    int res1 = obj.get(0).toJava(int.class);
                    //PyObject object = py_obj.callAttr("main", upload_image).to;
                    //System.out.print("rezz:" + Arrays.deepToString(image_arr[0]));

                    //int[] image_arr = py_obj.callAttr("main", upload_image).toJava(int[].class);
                    System.out.print(res1);
                    String a = "a";
                    i.putExtra("rezultat", a);
                    break;
                }
                case "age_and_gender_detection":{
                    //int[] data = pyobj_age_and_gender.callAttr("main", upload_image).toJava(int[].class);  --> ako želimo slati array
                    String rezultat = py_obj.callAttr("main",ImageFilePath).toString();
                    i.putExtra("result", rezultat);
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

            //uz rezultat na sljedeći activity šaljemo i choice da bi znali kako prikazati rezultat
            i.putExtra("choice", choice);

            //Convert to byte array -> slanje slike
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            i.putExtra("image",byteArray);

            startActivity(i);
        }


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

    private String createDirectoryAndSaveFile(Bitmap imageToSave) {

        File directory = new File(Environment.getExternalStorageDirectory() + "/Image_Storage");
        if (!directory.exists()) { directory.mkdir(); }
        File imagefile = new File(directory,"Upload_image.jpg");
        if (imagefile.exists()) { imagefile.delete(); }
        String ImageFilePath = imagefile.toString();

        try {
            FileOutputStream out = new FileOutputStream(imagefile);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
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
            ImageFilePath = createDirectoryAndSaveFile(bitmap);

            //Konvertiranje u byte array
            //ByteArrayOutputStream stream = null;
            //stream = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            //picture = stream.toByteArray();

            //cudan uvjet? kako ako nije null, ?
            if(imageView!=null){
                imageView.setImageBitmap(bitmap);
                //Promijeni tekstualne upute ispod slike
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


        }

    }

}
