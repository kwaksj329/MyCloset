package com.example.mycloset.add;

import android.app.Activity;
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

public class GridListAdapter extends BaseAdapter {
    ArrayList<QueryDocumentSnapshot> clothes = new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    Context context;

    public GridListAdapter(Activity context ) {
        super();
        this.context = context;
    }

    public void addItem(QueryDocumentSnapshot data){
        clothes.add(data);
    }

    public void clear(){
        clothes.clear();
    }

    @Override
    public int getCount(){
        return clothes.size();
    }

    @Override
    public Object getItem(int position){
        return clothes.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent){

        if(convertview==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertview = inflater.inflate(R.layout.list_item, parent, false);
        }
        ImageView clothImageview = convertview.findViewById(R.id.clothImageview);

        new Thread(() -> {
            QueryDocumentSnapshot listItem = clothes.get(position);
            StorageReference mountainsRef = storageRef.child("clothes/" + listItem.getId() + ".png");
            final long ONE_MEGABYTE = 1024 * 1024;
            mountainsRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                Bitmap clothBitmap  = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                clothImageview.setImageBitmap(clothBitmap);
            }).addOnFailureListener(Throwable::printStackTrace);
        }).start();
        return convertview;
    }
}
