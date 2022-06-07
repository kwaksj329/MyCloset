package com.example.mycloset.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Cloth {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    public String cloth_name;

    public byte[] clothImage;

    public String category;
    public boolean spring;
    public boolean summer;
    public boolean fall;
    public boolean winter;

    @Override
    public String toString() {
        return "uid => " + this.uid + ", name => " + this.cloth_name + ", category => " + this.category + ", spring => " + this.spring + ", summer => " + this.summer + ", fall => " + this.fall + ", winter => " + this.winter;
    }
}