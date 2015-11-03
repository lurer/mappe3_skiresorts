package com.example.s198599.s198599_mappe3.models;

import java.util.List;
import java.util.Map;

import lib.Static_lib.*;

/**
 * Created by espen on 11/1/15.
 */
public class Resort {

    private int id;
    private String name;
    private String description;

    private Map<WEEKDAY, OpeningHours> openingHours;
    private LiftTicketPrices prices;
    private Lifts lift;
    private Slopes slopes;
    private Images images;
    private List<SOCIALMEDIA> socialmedias;
    private ResortLocation location;
}
