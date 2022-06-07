package com.example.mycloset.add;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.mycloset.R;
import com.example.mycloset.entity.Cloth;

import java.util.ArrayList;

public class GridListAdapter extends BaseAdapter {
    ArrayList<Cloth> cloths = new ArrayList<Cloth>();
    Context context;

    public void addItem(Cloth cloth){
        cloths.add(cloth);
    }

    public void clear(){
        cloths.clear();
    }

    @Override
    public int getCount(){
        return cloths.size();
    }

    @Override
    public Object getItem(int position){
        return cloths.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent){
        context = parent.getContext();
        Cloth listItem = cloths.get(position);

        if(convertview==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertview = inflater.inflate(R.layout.list_item, parent, false);
        }

        ImageView clothImageview = convertview.findViewById(R.id.clothImageview);

        Bitmap clothBitmap  = BitmapFactory.decodeByteArray(listItem.clothImage, 0, listItem.clothImage.length);

        clothImageview.setImageBitmap(clothBitmap);

        return convertview;
    }
}
