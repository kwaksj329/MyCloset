package com.example.mycloset.add;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.ByteArrayBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    Bitmap resultingImage;
    private String[] categoryArray;
    private String selectedCategory;
    ImageView result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("hello");
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

        result = findViewById(R.id.result);
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
            result.setImageBitmap(resultingImage);
        }
        else if (mode == AddFragment.AUTO){
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
                        final HttpPost httppost = new HttpPost("http://192.168.123.5:8000" +
                                "/post");
                        byte[] arr = getIntent().getByteArrayExtra("image");

                        final ByteArrayBody image = new ByteArrayBody(arr, "image");
                        final HttpEntity reqEntity = MultipartEntityBuilder.create()
                                .addPart("image", image)
                                .build();


                        httppost.setEntity(reqEntity);

                        System.out.println("executing request " + httppost);
                        try (final CloseableHttpResponse response = httpclient.execute(httppost)) {
                            System.out.println("----------------------------------------");
                            System.out.println(response);
                            final HttpEntity resEntity = response.getEntity();
                            if (resEntity != null) {
                                System.out.println("Response content length: " + resEntity.getContentLength());
                                InputStream ins = resEntity.getContent();
                                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                                int nRead;
                                byte[] data = new byte[16384];

                                while ((nRead = ins.read(data, 0, data.length)) != -1) {
                                    buffer.write(data, 0, nRead);
                                }
                                arr = buffer.toByteArray();
                                resultingImage = BitmapFactory.decodeByteArray(arr, 0, arr.length);
                            }
                            EntityUtils.consume(resEntity);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            result.setImageBitmap(resultingImage);
                        }
                    });
                }
            }).start();
        }

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