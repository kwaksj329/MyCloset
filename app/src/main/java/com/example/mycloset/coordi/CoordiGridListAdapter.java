package com.example.mycloset.coordi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.mycloset.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CoordiGridListAdapter extends BaseAdapter {
    ArrayList<QueryDocumentSnapshot> coordinates = new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    Context context;

    public void addItem(QueryDocumentSnapshot coordinate){
        coordinates.add(coordinate);
    }

    public void clear(){
        coordinates.clear();
    }

    @Override
    public int getCount(){
        return coordinates.size();
    }

    @Override
    public Object getItem(int position){
        return coordinates.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent){
        context = parent.getContext();

        if(convertview==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertview = inflater.inflate(R.layout.list_item, parent, false);
        }

        ImageView clothImageview = convertview.findViewById(R.id.clothImageview);

        new Thread(() -> {
            QueryDocumentSnapshot listItem = coordinates.get(position);
            StorageReference mountainsRef = storageRef.child("coordinates/" + listItem.getId() + ".png");
            final long ONE_MEGABYTE = 1024 * 1024;
            mountainsRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                Bitmap clothBitmap  = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                clothImageview.setImageBitmap(clothBitmap);
            }).addOnFailureListener(Throwable::printStackTrace);
        }).start();
        return convertview;
    }
}
