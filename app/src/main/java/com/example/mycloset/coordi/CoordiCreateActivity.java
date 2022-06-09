package com.example.mycloset.coordi;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycloset.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class CoordiCreateActivity  extends AppCompatActivity implements StickerBSFragment.StickerListener{
    PhotoEditor mPhotoEditor;
    private StickerBSFragment mStickerBSFragment;
    private static final String TAG = "CoordiCreate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coordi_activity);
        PhotoEditorView mPhotoEditorView = findViewById(R.id.photoEditorView);
        mPhotoEditor = new PhotoEditor.Builder(getApplicationContext(), mPhotoEditorView)
                .setPinchTextScalable(true)
                .build();
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);
        Button ClothBtn = findViewById(R.id.newClothBtn);
        ClothBtn.setOnClickListener(view -> showBottomSheetDialogFragment(mStickerBSFragment));
        Button SaveBtn = findViewById(R.id.codySaveBtn);
        SaveBtn.setOnClickListener(view -> mPhotoEditor.saveAsBitmap(new OnSaveBitmap() {
            @Override
            public void onBitmapReady(@NonNull Bitmap saveBitmap) {
                EditText codyNameEditText = findViewById(R.id.codyNameEditText);
                CheckBox springSelection = findViewById(R.id.springSelectCheckBox);
                CheckBox summerSelection = findViewById(R.id.summerSelectCheckBox);
                CheckBox fallSelection = findViewById(R.id.fallSelectCheckBox);
                CheckBox winterSelection = findViewById(R.id.winterSelectCheckBox);
                CheckBox publicSelection = findViewById(R.id.publicSelectCheckBox);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                Map<String, Object> coordinate = new HashMap<>();

                coordinate.put("title", codyNameEditText.getText().toString());
                coordinate.put("spring", springSelection.isChecked());
                coordinate.put("summer", summerSelection.isChecked());
                coordinate.put("fall", fallSelection.isChecked());
                coordinate.put("winter", winterSelection.isChecked());
                coordinate.put("user", user.getUid());
                coordinate.put("public", publicSelection.isChecked());

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("coordinates")
                        .add(coordinate)
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId() + ", " + documentReference);

                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();
                            StorageReference mountainsRef = storageRef.child("coordinates/" + documentReference.getId() + ".png");

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] data = stream.toByteArray();
                            mountainsRef.putBytes(data);
                        })
                        .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                finish();
            }
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("PhotoEditor","Failed to save Image");
            }
        }));
        setClothList();
    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        mPhotoEditor.addImage(bitmap);
    }
    private void showBottomSheetDialogFragment(BottomSheetDialogFragment fragment) {
        if (fragment == null || fragment.isAdded()) {
            return;
        }
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }
    public void setClothList(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference clothesRef = db.collection("clothes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<QueryDocumentSnapshot> tempStickerList = new ArrayList<>();

        Query query = clothesRef
                .whereEqualTo("user", user.getUid());
        query
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            tempStickerList.add(document);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        mStickerBSFragment.setClothesList(tempStickerList);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}
