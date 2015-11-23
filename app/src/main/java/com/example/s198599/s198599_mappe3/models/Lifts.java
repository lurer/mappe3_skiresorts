package com.example.s198599.s198599_mappe3.models;

/**
 * Created by espen on 11/1/15.
 */
public class Lifts{


    private int total;
    private int open;

    public Lifts(int total, int open) {
        this.total = total;
        this.open = open;
    }

    public Lifts() {
    }

    public int getTotal() {
        return total;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return " " + open + "/" + total;
    }
}
