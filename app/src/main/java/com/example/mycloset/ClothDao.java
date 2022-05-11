package com.example.mycloset;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface ClothDao {
    @Insert
    void insertAll(Cloth... cloths);

    @Delete
    void delete(Cloth cloth);

    @Query("SELECT * FROM cloth")
    List<Cloth> getAll();
}