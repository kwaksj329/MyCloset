package com.example.mycloset.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mycloset.entity.Cody;

import java.util.List;

@Dao
public interface CodyDao {

    @Insert
    void insertAll(Cody... codys);

    @Delete
    void delete(Cody cody);

    @Query("SELECT * FROM cody")
    List<Cody> getAll();

    @Query("SELECT * FROM cody WHERE spring=1")
    List<Cody> getSpringSelected();

    @Query("SELECT * FROM cody WHERE summer=1")
    List<Cody> getSummerSelected();
    @Query("SELECT * FROM cody WHERE fall=1")
    List<Cody> getFallSelected();
    @Query("SELECT * FROM cody WHERE winter=1")
    List<Cody> getWinterSelected();
}