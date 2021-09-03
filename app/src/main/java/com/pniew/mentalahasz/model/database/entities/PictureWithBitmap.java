package com.pniew.mentalahasz.model.database.entities;

import android.graphics.Bitmap;

import java.util.Objects;

public class PictureWithBitmap {
    Picture picture;
    Bitmap bitmap;

    public PictureWithBitmap(Picture picture, Bitmap bitmap) {
        this.picture = picture;
        this.bitmap = bitmap;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PictureWithBitmap that = (PictureWithBitmap) o;
        return Objects.equals(getPicture(), that.getPicture()) &&
                Objects.equals(getBitmap(), that.getBitmap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPicture(), getBitmap());
    }
}
