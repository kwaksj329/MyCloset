package com.example.mycloset.add;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mycloset.R;
import com.example.mycloset.dao.ClothDao;
import com.example.mycloset.database.AppDatabase;
import com.example.mycloset.entity.Cloth;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    Bitmap resultingImage;
    private String[] categoryArray;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryArray = getResources().getStringArray(R.array.category_array);

        setContentView(R.layout.resultview);

        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = categoryArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ImageView result = findViewById(R.id.result);
        int mode = getIntent().getIntExtra("mode", AddFragment.MANUAL);

        if (mode == AddFragment.MANUAL){
            byte[] arr = getIntent().getByteArrayExtra("image");
            Bitmap bitmap2  = BitmapFactory.decodeByteArray(arr, 0, arr.length);

            resultingImage = Bitmap.createBitmap(bitmap2.getWidth(),
                    bitmap2.getHeight(), bitmap2.getConfig());

            Canvas canvas = new Canvas(resultingImage);
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            Path path = new Path();
            PinPoint currentPoint;
            for(currentPoint = CropTouchView.head.getNextPoint(); currentPoint != null; currentPoint = currentPoint.getNextPoint()){
                float startX = currentPoint.getFirst();
                float startY = currentPoint.getSecond();
                path.lineTo(startX, startY);
            }

            currentPoint = CropTouchView.head.getNextPoint();

            path.lineTo(currentPoint.getFirst(), currentPoint.getSecond());

            canvas.drawPath(path, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            canvas.drawBitmap(bitmap2, 0, 0, paint);
        }
        else if (mode == AddFragment.AUTO){
            byte[] arr = getIntent().getByteArrayExtra("image");
            resultingImage = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        }

        result.setImageBitmap(resultingImage);

        findViewById(R.id.save_btn).setOnClickListener(view -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            ClothDao clothDao = db.clothDao();
            Cloth cloth = new Cloth();
            EditText clothNameEditText = findViewById(R.id.clothName_editText);
            CheckBox spring = findViewById(R.id.spring_checkBox);
            CheckBox summer = findViewById(R.id.summer_checkBox);
            CheckBox fall = findViewById(R.id.fall_checkBox);
            CheckBox winter = findViewById(R.id.winter_checkBox);
            cloth.cloth_name = clothNameEditText.getText().toString();
            cloth.spring = spring.isChecked();
            cloth.summer = summer.isChecked();
            cloth.fall = fall.isChecked();
            cloth.winter = winter.isChecked();
            cloth.category = selectedCategory;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resultingImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            cloth.clothImage = stream.toByteArray();
            clothDao.insertAll(cloth);
            List<Cloth> temp = clothDao.getSpringSelected("상의");
            for(Cloth tempCloth : temp){
                Log.d("result", tempCloth.toString());
            }

            finish();
        });
    }
}