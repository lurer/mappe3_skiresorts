package com.example.s198599.s198599_mappe3.models;

import android.graphics.Bitmap;

/**
 * Created by espen on 11/1/15.
 */
public class Images {


    private Bitmap image_1_1;
    private Bitmap image_16_9;
    private ImageScaled scaled;

    public Images(Bitmap image_1_1, Bitmap image_16_9) {

        this.image_1_1 = image_1_1;

        this.image_16_9 = image_16_9;
        scaled = new ImageScaled();
    }

    public Images() {
        scaled = new ImageScaled();
    }


    public Bitmap getImage_1_1() {
        return image_1_1;
    }

    public void setImage_1_1(Bitmap image_1_1) {
        this.image_1_1 = image_1_1;
    }

    public Bitmap getImage_16_9() {
        return image_16_9;
    }

    public void setImage_16_9(Bitmap image_16_9) {
        this.image_16_9 = image_16_9;
    }


    public ImageScaled getScaled() {
        return scaled;
    }

    public void setScaled(ImageScaled scaled) {
        this.scaled = scaled;
    }
}
