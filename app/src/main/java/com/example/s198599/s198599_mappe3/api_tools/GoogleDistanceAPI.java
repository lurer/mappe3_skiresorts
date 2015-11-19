package com.example.s198599.s198599_mappe3.api_tools;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.example.s198599.s198599_mappe3.models.Distance;
import com.example.s198599.s198599_mappe3.models.Resort;
import com.example.s198599.s198599_mappe3.models.ResortRepository;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import lib.Static_lib;

/**
 * Created by espen on 11/17/15.
 */
public class GoogleDistanceAPI  extends AsyncTask<Location, Void, Void>{

    private ResortRepository repository;
    private String jsonResult;
    private JsonParser parser;
    private JsonElement root;
    private JsonArray destinationArray;
    private JsonArray elementsArray;
    private DistanceCallback callback;
    private String apiUrl;
    private Location myLocation;

    public GoogleDistanceAPI(DistanceCallback callback){this.callback = callback;}

    @Override
    protected Void doInBackground(Location... param) {


        getRespositoryInstance(); //Tilgang til Repository av Resorts
        myLocation = param[0]; //Min lokasjon

        destinationArray = new JsonArray();
        elementsArray = new JsonArray();

        int listSize = repository.getResorts().size();
        int increment = 20;
        int counterLower = 0;
        int counterUpper = increment;

        do{
            generateApiUrl(counterLower, counterUpper);

            try {

                URL url = new URL(apiUrl);
                Log.d("RESORT", "GoogleDistanceURL: " + url.toString());

                jsonResult = IOUtils.toString(url);             //Kjør API-kallet
                parser = new JsonParser();
                root = parser.parse(jsonResult);                //Finn rot-elementer i Json-objektet



                destinationArray.addAll(root.getAsJsonObject().get("destination_addresses").getAsJsonArray());
                elementsArray.addAll(root.getAsJsonObject().getAsJsonArray("rows")
                        .get(0).getAsJsonObject().getAsJsonArray("elements"));



                //Log.d("RESORT", "Desitnation array: " + destinationArray.toString());
                //Log.d("RESORT", "Distance array: " + elementsArray.toString());

            }catch(NullPointerException npe){
                Log.d("RESORT", "Exception - GoogleAPI - NullpointerException");
            }catch(MalformedURLException me){
                Log.d("RESORT", "Exception - GoogleAPI - MalformedURLException");
            }catch (IOException ioe){
                Log.d("RESORT", "Exception - GoogleAPI - IOException");
            }

            if(counterLower + increment >= listSize)        //Man har allerede nådd slutten av listen i forrige omgang
                break;

            counterLower = counterUpper;
            if(counterUpper + increment > listSize)
                counterUpper = listSize;
            else
                counterUpper += increment;

        }while(counterLower < counterUpper);//while


        saveRelevantDataFromJson();
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        callback.notifyDistanceResult();
    }

    public void getRespositoryInstance(){
        repository = ResortRepository.getInstance();
    }


    /**
     * Lager URL med alle skistedene som destinasjonsparamtere
      */
    public void generateApiUrl(int from, int to){
        String myLat = String.valueOf(myLocation.getLatitude());
        String myLng = String.valueOf(myLocation.getLongitude());
        String baseUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
        StringBuilder sb = new StringBuilder();

        sb.append(baseUrl);
        sb.append(myLat).append(",").append(myLng).append("&destinations=");
        List<Resort> list = repository.getResorts();
        for(int i = from; i < to; i++){
            Resort r = list.get(i);
            String rLat = String.valueOf(r.getLocation().latitude);
            String rLng = String.valueOf(r.getLocation().longitude);
            sb.append(rLat).append(",").append(rLng).append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("&key=AIzaSyCrMqTpEHh1grz3TvsalZoUYry8pNUIJmk");
        apiUrl = sb.toString();
    }


    public void saveRelevantDataFromJson(){
        Log.d("RESORT", "GoogleApi - Lagrer data fr GoogleApi");

        List<Resort> list = repository.getResorts();
        Log.d("RESORT", "Resortlist og GoogleJson-resultat er like lang: " + list.size() + " " + elementsArray.size());

        for(int i = 0; i < list.size(); i++){
            JsonElement elem = elementsArray.get(i);
            JsonObject dist = elem.getAsJsonObject().get("distance").getAsJsonObject();
            JsonObject dura = elem.getAsJsonObject().get("duration").getAsJsonObject();

            Distance distance = new Distance(list.get(i).getLocation()
                    , dist.get("value").getAsInt()
                    , dura.get("value").getAsInt());
            list.get(i).setDistance(distance);
        }
    }

}
