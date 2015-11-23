package com.example.s198599.s198599_mappe3.models;

/**
 * Created by espen on 11/1/15.
 */
public class Slopes{

    private int total;
    private int open;


    public Slopes(int total, int open) {
        this.total = total;
        this.open = open;
    }

    public Slopes() {
    }

    public int getTotal() {
        return total;
    }

    public int getOpen() {
        return open;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return " " + getOpen() + "/" + getTotal();
    }
}
