package com.example.s198599.s198599_mappe3.models;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import lib.Static_lib;

/**
 * Created by espen on 11/17/15.
 */
public class Distance {

    private LatLng location;
    private int distanceMeter;
    private int durationSec;

    private double distanceKm;
    private String durationTime;

    public Distance(){}

    public Distance(LatLng location, int distanceMeter, int durationSec){
        this.location = location;
        this.distanceMeter = distanceMeter;
        this.durationSec = durationSec;

        getDistanceInKm();
        getTimefromSec();
    }


    public boolean compareLocations(LatLng inLoc){
        if(location.latitude == inLoc.latitude && location.longitude == inLoc.longitude)
            return true;
        return false;
    }

    public LatLng getLocation(){
        return location;
    }

    public double getDistanceKm() {
        return distanceKm;
    }


    public String getDurationTime() {
        return durationTime;
    }


    private void getDistanceInKm(){

        try {
            distanceKm = Double.parseDouble(Static_lib.DECIMAL_FORMAT_EN.format(distanceMeter / 1000));
        }catch (NumberFormatException npe){
            Log.d("RESORT", "Numberformat Exception i Distance - Get Distance");
            distanceKm = distanceMeter / 1000;
        }
    }

    private void getTimefromSec(){

        int tempminutter = (int) durationSec / 60;
        int sec = durationSec % 60;

        int hours = (int)tempminutter / 60;
        int minutter = tempminutter % 60;
        durationTime = String.valueOf(hours) + "h " + String.valueOf(minutter) +"m";

    }

    public String getDistanceKmString(){
        return " " + getDistanceKm() + " km";
    }

    public String getDurationString(){
        return " " + durationTime;
    }
}
