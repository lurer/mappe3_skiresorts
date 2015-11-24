package com.example.s198599.s198599_mappe3.models;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import com.google.android.gms.maps.model.LatLng;
import lib.Static_lib.*;

/**
 * Created by espen on 11/1/15.
 */
public class Resort implements Serializable{

    private static final long serialVerionUID = -3676104445269059528L;


    private int id;
    private String name;
    private String description;
    private LatLng location;
    private Distance distance;
    private Contact contact;
    private int markerId;


    private Lifts lifts;
    private Slopes slopes;
    private Images images;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getMarkerId() {
        return markerId;
    }

    public void setMarkerId(int markerId) {
        this.markerId = markerId;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Lifts getLifts() {
        return lifts;
    }

    public void setLifts(Lifts lifts) {
        this.lifts = lifts;
    }

    public Slopes getSlopes() {
        return slopes;
    }

    public void setSlopes(Slopes slopes) {
        this.slopes = slopes;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }


    public static Comparator<Resort> SortAlphabeticallyAsc = new Comparator<Resort>(){

        @Override
        public int compare(Resort r1, Resort r2) {
            return r1.getName().compareTo(r2.getName());
        }
    };

    public static Comparator<Resort> SortAlphabeticallyDesc = new Comparator<Resort>(){

        @Override
        public int compare(Resort r1, Resort r2) {
            return r2.getName().compareTo(r1.getName());
        }
    };

    public static Comparator<Resort> SortDistanceAsc = new Comparator<Resort>(){

        @Override
        public int compare(Resort r1, Resort r2) {
            return (int)r1.getDistance().getDistanceKm() - (int)r2.getDistance().getDistanceKm();
        }
    };

    public static Comparator<Resort> SortDistanceDesc = new Comparator<Resort>(){

        @Override
        public int compare(Resort r1, Resort r2) {
            return (int)r2.getDistance().getDistanceKm() - (int)r1.getDistance().getDistanceKm();
        }
    };
}
