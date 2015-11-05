package com.example.s198599.s198599_mappe3.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lib.Static_lib.*;

/**
 * Created by espen on 11/1/15.
 */
public class Resort implements Serializable{

    private static final long serialVerionUID = -3676104445269059528L;


    private int id;
    private String name;
    private String description;
    private ResortLocation location;


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

    public ResortLocation getLocation() {
        return location;
    }

    public void setLocation(ResortLocation location) {
        this.location = location;
    }
}
