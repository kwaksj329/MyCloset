package com.example.mycloset;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cloth {
    @PrimaryKey
    public int uid;

    public String season;

    @ColumnInfo(name = "last_name")
    public String lastName;

    public Cloth (int uid, String season, String lastName) {
        this.uid = uid;
        this.season = season;
        this.lastName = lastName;
    }
}