package com.example.s198599.s198599_mappe3.models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

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

        distanceKm = Double.parseDouble(Static_lib.DECIMAL_FORMAT1.format(distanceMeter / 1000));

    }

    private void getTimefromSec(){

        int tempminutter = (int) durationSec / 60;
        int sec = durationSec % 60;

        int hours = (int)tempminutter / 60;
        int minutter = tempminutter % 60;
        durationTime = String.valueOf(hours) + ": " + String.valueOf(minutter);
    }
}
