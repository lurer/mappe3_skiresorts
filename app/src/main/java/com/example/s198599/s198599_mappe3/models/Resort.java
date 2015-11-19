package com.example.s198599.s198599_mappe3.models;

import java.io.Serializable;
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

    private int markerId;

    private Map<WEEKDAY, OpeningHours> openingHours;
    private LiftTicketPrices prices;
    private Lifts lift;
    private Slopes slopes;
    private Images images;
    private List<SOCIALMEDIA> socialmedias;



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
}
