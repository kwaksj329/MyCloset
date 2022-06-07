package com.example.mycloset.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mycloset.dao.ClothDao;
import com.example.mycloset.dao.CodyDao;
import com.example.mycloset.entity.Cloth;
import com.example.mycloset.entity.Cody;

@Database(entities = {Cloth.class, Cody.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase db;
    private static final Object sLock = new Object();
    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if(db==null) {
                db= Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "closet").allowMainThreadQueries()
                        .build();
            }
            return db;
        }
    }
    public abstract ClothDao clothDao();
    public abstract CodyDao codyDao();
}