package com.example.mycloset.cody;

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
import com.example.mycloset.entity.Cody;

import java.util.ArrayList;

public class CodyGridListAdapter extends BaseAdapter {
    ArrayList<Cody> codys = new ArrayList<Cody>();
    Context context;

    public void addItem(Cody cody){
        codys.add(cody);
    }

    public void clear(){
        codys.clear();
    }

    @Override
    public int getCount(){
        return codys.size();
    }

    @Override
    public Object getItem(int position){
        return codys.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent){
        context = parent.getContext();
        Cody listItem = codys.get(position);

        if(convertview==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertview = inflater.inflate(R.layout.list_item, parent, false);
        }

        ImageView clothImageview = convertview.findViewById(R.id.clothImageview);

        Bitmap clothBitmap  = BitmapFactory.decodeByteArray(listItem.codyImage, 0, listItem.codyImage.length);

        clothImageview.setImageBitmap(clothBitmap);

        return convertview;
    }
}
