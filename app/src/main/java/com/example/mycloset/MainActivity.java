package com.example.mycloset;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;

public class MainActivity extends AppCompatActivity {

    final String TAG = this.getClass().getSimpleName();

    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        SettingListener();

        bottomNavigationView.setSelectedItemId(R.id.tab_cody);
    }

    private void init(){
        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void SettingListener(){
        bottomNavigationView.setOnItemSelectedListener(new TabSelectedListener());
    }

    class TabSelectedListener implements BottomNavigationView.OnItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
            switch (menuItem.getItemId()){
                case R.id.tab_cody:{
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new CodyFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_closet:{
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new ClosetFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_add: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new AddFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_weather:{
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new WeatherFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_setting:{
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_ly, new SettingFragment())
                            .commit();
                    return true;
                }
            }
            return false;
        }
    }

}