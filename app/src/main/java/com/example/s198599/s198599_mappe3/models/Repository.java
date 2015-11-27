package com.example.s198599.s198599_mappe3.models;

import com.google.android.gms.maps.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by espen on 11/5/15.
 */
public class Repository {

    private static Repository instance;
    private static List<Resort> resorts;
    private static boolean isLoaded;        //Alle data er lastet
    private static int resortMarkerClicked;
    private static LatLng customMarkerLatLng;
    private static Marker customMapMarker;
    private static String apiError;

    public String getApiError() {
        return apiError;
    }

    public void setApiError(String apiError) {
        this.apiError = apiError;
    }

    public Marker getCustomMapMarker() {
        return customMapMarker;
    }

    public void setCustomMapMarker(Marker customMapMarker) {
        this.customMapMarker = customMapMarker;
    }



    public LatLng getCustomMarkerLatLng() {
        return customMarkerLatLng;
    }

    public void setCustomMarkerLatLng(LatLng customMarkerPosition) {
        this.customMarkerLatLng = customMarkerPosition;
    }




    public int getResortMarkerClicked() {
        return resortMarkerClicked;
    }

    public void setResortMarkerClicked(int resortMarkerClicked) {
        this.resortMarkerClicked = resortMarkerClicked;
    }

    public boolean isLoaded() {
        return isLoaded;
    }




    public void setIsLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    private Repository(){
        if(resorts == null)
            resorts = new ArrayList<>();
    }


    public static Repository getInstance(){
        if(instance == null)
            instance = new Repository();
        return instance;
    }


    public void addResortToList(Resort resort){
        resorts.add(resort);
    }

    public void removeResortFromList(int id){
        for(Resort r : resorts){
            if(r.getId() == id){
                resorts.remove(r);
                break;
            }

        }
    }


    public List<Resort> getResorts(){
        return resorts;
    }

    public void copyNewListToRepository(List<Resort> newList){
        resorts.clear();
        for(Resort r : newList){
            resorts.add(r);
        }
    }


    public Resort matchLocationinformation(LatLng innLL){
        if(innLL != null){
            for(Resort r : resorts){
                if(r.getDistance().compareLocations(innLL))
                    return r;
            }
        }

        return null;
    }


    public Resort getResortById(int id){
        for(Resort r : resorts){
            if(r.getId() == id)
                return r;
        }
        return null;
    }


    public void emptyResortList(){
        if(resorts != null)
            resorts.clear();
    }
}
