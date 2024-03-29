package com.example.facefun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    public String[] list;

    public ImageAdapter(Context c){
        context = c;
        File directory = new File(Environment.getExternalStorageDirectory() + "/gallery_images");
        String directory_path = directory.toString();
        File f = new File(directory_path);
        File imageFiles[] = f.listFiles();
        try { list = context.getAssets().list("Gallery_images"); System.out.println(c); System.out.println(context); System.out.println("Radi");  }
        catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView img = new ImageView(context);
        System.out.println(img);
        System.out.println("dobro");
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.setRotation(90);
        img.setLayoutParams(new GridView.LayoutParams(240,240));

        try{
            InputStream ims = context.getAssets().open("Gallery_images/" + list[position]);
            Bitmap bitmap = BitmapFactory.decodeStream(ims);
            img.setImageBitmap(bitmap);
        } catch (IOException e){ e.printStackTrace(); }
        return img;
    }
}

//https://www.zidsworld.com/android-tutorials/read-all-images-from-assets-folder-and-show-in-gridview-android-java/
