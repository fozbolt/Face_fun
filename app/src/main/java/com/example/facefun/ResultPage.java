package com.example.facefun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;


public class ResultPage extends AppCompatActivity {
    Button save;
    Bitmap bitmap;
    String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        Intent i = getIntent();

        choice = i.getExtras().getString("choice");

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView im = findViewById(R.id.resultPic);
        im.setImageBitmap(bitmap);

        if(choice.equals("celebrity_look_alike")){
            String [] result;
            result = i.getExtras().getStringArray("result");


            TextView txtView= findViewById(R.id.resultText);
            //ne funkcionira s replaceAll zbog "["
            String res = Arrays.toString(result);
            String strNew = res.replace("b'", "");
            strNew = strNew.replace("[", "");
            strNew = strNew.replace("]", "");
            strNew = strNew.replace("'", "");
            String[] parts = strNew.split(",");


            System.out.print("rezz:" + parts[0]);
            String rez = "1. " + parts[0] + "\n" + "2. " + parts[1] + "\n" + "3. " + parts[2] + "\n" + "4. " + parts[3] + "\n" + "5. " + parts[4];
            txtView.setText(rez);
        }
    }

    public void goBack(View v){
        super.onBackPressed();
    }


    //spremanje obrađene slike na mobitel
    public void saveImage(View v){
        //ovo promijeniti u obradenu sliku kada spojimo
        ImageView iv = (ImageView)findViewById(R.id.resultPic);


        iv.buildDrawingCache();
        Bitmap bmp = iv.getDrawingCache();

        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //context.getExternalFilesDir(null);

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        File file = new File(storageLoc, timeStamp + ".jpg");

        try{
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            //toast ne radi
            Toast.makeText(getApplicationContext(),"Image saved succesfully",Toast.LENGTH_SHORT).show();

            Toast toast = Toast.makeText(ResultPage.this, "Task Saved", Toast.LENGTH_SHORT);
            toast.show();

            Log.d("success", "true");
            Log.d("description", "image successfuly saved");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //intent koji vodi na početnu stranu
    public void launchMainActivity(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    //refresh gallerije
    // Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    // intent.setData(Uri.fromFile(file));
    //sendBroadcast(intent);

}