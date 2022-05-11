package com.example.mycloset;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class ResultActivity extends AppCompatActivity {
    Bitmap resultingImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.resultview);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.seasons_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        ImageView result = findViewById(R.id.result);

        byte[] arr = getIntent().getByteArrayExtra("image");
        Bitmap bitmap2  = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        //Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),
        //        R.drawable.example_image2);

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

        result.setImageBitmap(resultingImage);

        findViewById(R.id.save_btn).setOnClickListener(view -> {
            File fileDataBase = getDatabasePath("Android"); String getDatabasePath = fileDataBase.getPath();

            finish();
        });
    }
}
