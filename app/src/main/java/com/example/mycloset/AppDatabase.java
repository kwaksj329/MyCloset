package com.example.mycloset;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Cloth.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ClothDao clothDao();
}