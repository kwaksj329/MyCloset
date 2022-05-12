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

    @Query("SELECT * FROM cloth WHERE category = :category" + " AND spring=1")
    List<Cloth> getSpringSelected(String category);
    @Query("SELECT * FROM cloth WHERE category = :category" + " AND summer=1")
    List<Cloth> getSummerSelected(String category);
    @Query("SELECT * FROM cloth WHERE category = :category" + " AND fall=1")
    List<Cloth> getFallSelected(String category);
    @Query("SELECT * FROM cloth WHERE category = :category" + " AND winter=1")
    List<Cloth> getWinterSelected(String category);
}