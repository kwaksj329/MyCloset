package com.example.mycloset;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        moveMain(1);	//1초 후 main activity 로 넘어감
    }

    private void moveMain(int sec) {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                File file = new File(getApplicationContext().getFilesDir(), "userInfo.dat");
                if (file.exists()){
                    //new Intent(현재 context, 이동할 activity)
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(intent);	//intent 에 명시된 액티비티로 이동

                    finish();	//현재 액티비티 종료
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                    startActivity(intent);	//intent 에 명시된 액티비티로 이동

                    finish();	//현재 액티비티 종료
                }
            }
        }, 1500 * sec); // sec초 정도 딜레이를 준 후 시작
    }
}

