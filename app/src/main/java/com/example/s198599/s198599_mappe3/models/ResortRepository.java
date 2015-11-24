package com.example.s198599.s198599_mappe3.models;

import com.google.android.gms.maps.model.*;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.model.*;
/**
 * Created by espen on 11/5/15.
 */
public class ResortRepository {

    private static ResortRepository instance;
    private static List<Resort> resorts;
    private static boolean isLoaded;        //Alle data er lastet
    private static int resortMarkerClicked;
    private static LatLng customMarkerPosition;
    private static boolean disableGoogleApi;

    public boolean isDisableGoogleApi() {
        return disableGoogleApi;
    }

    public void setDisableGoogleApi(boolean disableGoogleApi) {
        ResortRepository.disableGoogleApi = disableGoogleApi;
    }

    public LatLng getCustomMarkerPosition() {
        return customMarkerPosition;
    }

    public void setCustomMarkerPosition(LatLng customMarkerPosition) {
        ResortRepository.customMarkerPosition = customMarkerPosition;
    }




    public int getResortMarkerClicked() {
        return resortMarkerClicked;
    }

    public void setResortMarkerClicked(int resortMarkerClicked) {
        ResortRepository.resortMarkerClicked = resortMarkerClicked;
    }

    public boolean isLoaded() {
        return isLoaded;
    }




    public void setIsLoaded(boolean isLoaded) {
        ResortRepository.isLoaded = isLoaded;
    }

    private ResortRepository(){
        if(resorts == null)
            resorts = new ArrayList<>();
    }


    public static ResortRepository getInstance(){
        if(instance == null)
            instance = new ResortRepository();
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
}
