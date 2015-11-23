package com.example.s198599.s198599_mappe3.models;

import android.graphics.Bitmap;

/**
 * Created by espen on 11/1/15.
 */
public class ImageScaled {

    private Bitmap smallThumb;
    private Bitmap bigThumb;
    private Bitmap cover;

    public ImageScaled(Bitmap smallThumb, Bitmap bigThumb, Bitmap cover) {
        this.smallThumb = smallThumb;
        this.bigThumb = bigThumb;
        this.cover = cover;
    }

    public ImageScaled() {
    }

    public Bitmap getSmallThumb() {
        return smallThumb;
    }

    public void setSmallThumb(Bitmap smallThumb) {
        this.smallThumb = smallThumb;
    }

    public Bitmap getBigThumb() {
        return bigThumb;
    }

    public void setBigThumb(Bitmap bigThumb) {
        this.bigThumb = bigThumb;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }
}
