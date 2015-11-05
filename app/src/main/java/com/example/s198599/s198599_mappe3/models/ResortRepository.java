package com.example.s198599.s198599_mappe3.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by espen on 11/5/15.
 */
public class ResortRepository {

    private static ResortRepository instance;
    private static List<Resort> resorts;


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
}
