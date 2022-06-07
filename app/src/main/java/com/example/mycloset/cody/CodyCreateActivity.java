package com.example.mycloset.cody;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycloset.R;
import com.example.mycloset.dao.ClothDao;
import com.example.mycloset.dao.CodyDao;
import com.example.mycloset.database.AppDatabase;
import com.example.mycloset.entity.Cloth;
import com.example.mycloset.entity.Cody;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class CodyCreateActivity  extends AppCompatActivity implements StickerBSFragment.StickerListener{
    PhotoEditor mPhotoEditor;
    private StickerBSFragment mStickerBSFragment;
    private PhotoEditorView mPhotoEditorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cody_activity);
        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mPhotoEditor = new PhotoEditor.Builder(getApplicationContext(), mPhotoEditorView)
                .setPinchTextScalable(true)
                .build();
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);
        Button ClothBtn = findViewById(R.id.newClothBtn);
        ClothBtn.setOnClickListener(view -> {
            setClothList();
            showBottomSheetDialogFragment(mStickerBSFragment);
        });
        Button SaveBtn = findViewById(R.id.codySaveBtn);
        SaveBtn.setOnClickListener(view -> {
            mPhotoEditor.saveAsBitmap(new OnSaveBitmap() {
                @Override
                public void onBitmapReady(@NonNull Bitmap saveBitmap) {
                    EditText codyNameEditText = findViewById(R.id.codyNameEditText);
                    EditText codyDescriptionEditText = findViewById(R.id.codyDescriptionEditText);
                    CheckBox springSelection = findViewById(R.id.springSelectCheckBox);
                    CheckBox summerSelection = findViewById(R.id.summerSelectCheckBox);
                    CheckBox fallSelection = findViewById(R.id.fallSelectCheckBox);
                    CheckBox winterSelection = findViewById(R.id.winterSelectCheckBox);
                    Cody newCody = new Cody();
                    newCody.title = codyNameEditText.getText().toString();
                    newCody.description = codyDescriptionEditText.getText().toString();
                    newCody.spring = springSelection.isChecked();
                    newCody.summer = summerSelection.isChecked();
                    newCody.fall = fallSelection.isChecked();
                    newCody.winter = winterSelection.isChecked();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    newCody.codyImage = stream.toByteArray();
                    AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                    CodyDao codyDao = db.codyDao();
                    codyDao.insertAll(newCody);
                    Log.e("PhotoEditor","Image Saved Successfully");
                    finish();
                }

                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("PhotoEditor","Failed to save Image");
                }
            });
        });
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
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        ClothDao clothDao = db.clothDao();
        List<Cloth> temp = clothDao.getAll();
        ArrayList<Bitmap> tempStickerList = new ArrayList<>();
        for (Cloth tempCloth: temp) {
            Bitmap clothBitmap  = BitmapFactory.decodeByteArray(tempCloth.clothImage, 0, tempCloth.clothImage.length);
            tempStickerList.add(clothBitmap);
        }
        mStickerBSFragment.setClothList(tempStickerList);
    }
}
