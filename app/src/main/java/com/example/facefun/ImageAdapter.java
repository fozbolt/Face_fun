package com.example.facefun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    public String[] list;

    public ImageAdapter(Context c){
        context = c;
        try { list = context.getAssets().list("Gallery_images"); }
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
