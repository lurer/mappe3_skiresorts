package com.example.s198599.s198599_mappe3.api_tools;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.s198599.s198599_mappe3.R;
import com.example.s198599.s198599_mappe3.models.Distance;
import com.example.s198599.s198599_mappe3.models.Resort;
import com.example.s198599.s198599_mappe3.models.Repository;
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

/**
 * Created by espen on 11/17/15.
 */
public class GoogleDistanceAPI  extends AsyncTask<LatLng, Void, Void>{

    private Repository repository;
    private String jsonResult;
    private JsonParser parser;
    private JsonElement root;
    private JsonArray destinationArray;
    private JsonArray elementsArray;
    private DistanceCallback callback;
    private String apiUrl;
    private LatLng myLocation;
    private Context context;

    public GoogleDistanceAPI(Context context, DistanceCallback callback){
        this.context = context;
        this.callback = callback;}


    @Override
    protected Void doInBackground(LatLng... param) {


        repository = Repository.getInstance();

        myLocation = param[0]; //Min lokasjon
        Log.d("RESORT", "MyLocation " + myLocation.toString());

        destinationArray = new JsonArray();
        elementsArray = new JsonArray();

        int listSize = repository.getResorts().size();
        Log.d("RESORT", "ResortListSize " + listSize);
        int increment = 20;
        int counterLower = 0;
        int counterUpper = increment;

        do{
            generateApiUrl(counterLower, counterUpper);

            try {

                URL url = new URL(apiUrl);
                Log.d("RESORT", "GoogleDistanceURL: " + url.toString());

                jsonResult = IOUtils.toString(url);             //Kjør API-kallet
                //Log.d("RESORT", "JsonResult " + jsonResult);
                parser = new JsonParser();
                root = parser.parse(jsonResult);                //Finn rot-elementer i Json-objektet

                destinationArray.addAll(root.getAsJsonObject().get("destination_addresses").getAsJsonArray());
                elementsArray.addAll(root.getAsJsonObject().getAsJsonArray("rows")
                        .get(0).getAsJsonObject().getAsJsonArray("elements"));

            }catch(NullPointerException npe){
                Log.d("RESORT", "Exception - GoogleAPI - NullpointerException");
            }catch(MalformedURLException me){
                Log.d("RESORT", "Exception - GoogleAPI - MalformedURLException");
            }catch (IOException ioe){
                Log.d("RESORT", "Exception - GoogleAPI - IOException");
            }

            if(counterLower + increment >= listSize)        //Man har allerede nådd slutten av listen i forrige omgang
                break;

            Log.d("RESORT", "Lower: " + counterLower + ", Upper: " + counterUpper);
            counterLower = counterUpper;
            if(counterUpper + increment > listSize)
                counterUpper = listSize;
            else
                counterUpper += increment;

        }while(counterLower < counterUpper);//while



        saveRelevantDataFromJson();
        repository.setIsLoaded(true); //////////Hindrer Google API å bli lastet før den er false igjen
        Log.d("RESORT", "Repository isLoaded er satt til true: " + repository.isLoaded());
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        callback.notifyDistanceResult();
    }



    /**
     * Lager URL med alle skistedene som destinasjonsparamtere
      */
    public void generateApiUrl(int from, int to){
        String myLat = String.valueOf(myLocation.latitude);
        String myLng = String.valueOf(myLocation.longitude);
        String baseUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
        StringBuilder sb = new StringBuilder();

        sb.append(baseUrl);
        sb.append(myLat).append(",").append(myLng).append("&destinations=");
        List<Resort> list = repository.getResorts();
        for(int i = from; i < to; i++){
            if(i < list.size()){
                Resort r = list.get(i);
                String rLat = String.valueOf(r.getLocation().latitude);
                String rLng = String.valueOf(r.getLocation().longitude);
                sb.append(rLat).append(",").append(rLng).append("|");
            }

        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("&key=AIzaSyCrMqTpEHh1grz3TvsalZoUYry8pNUIJmk");
        apiUrl = sb.toString();
    }


    public void saveRelevantDataFromJson(){
        Log.d("RESORT", "GoogleApi - Lagrer data fr GoogleApi");

        List<Resort> list = repository.getResorts();
        //Log.d("RESORT", "Resortlist og GoogleJson-resultat er like lang: " + list.size() + " " + elementsArray.size());

        try{
            for(int i = 0; i < list.size(); i++){

                JsonElement elem = elementsArray.get(i);
                JsonObject dist = elem.getAsJsonObject().get("distance").getAsJsonObject();
                JsonObject dura = elem.getAsJsonObject().get("duration").getAsJsonObject();


                Distance distance = new Distance(list.get(i).getLocation()
                        ,dist.get("value").getAsInt()
                        ,dura.get("value").getAsInt());
                list.get(i).setDistance(distance);

            }
        }catch (NullPointerException npe){
            Log.d("RESORT", "Google API - Nullpointer. Sannsynlig at My location var ugyldig");
            repository.setApiError(context.getString(R.string.errorLocationNA));
        }



    }

}
