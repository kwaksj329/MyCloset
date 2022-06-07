package com.example.mycloset.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cody {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    public byte[] codyImage;

    public boolean spring;
    public boolean summer;
    public boolean fall;
    public boolean winter;
    public String title;
    public String description;
}