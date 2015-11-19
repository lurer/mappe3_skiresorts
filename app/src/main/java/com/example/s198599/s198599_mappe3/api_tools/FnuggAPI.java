package com.example.s198599.s198599_mappe3.api_tools;

import android.os.AsyncTask;

import android.util.Log;

import com.example.s198599.s198599_mappe3.models.Resort;
import com.google.android.gms.maps.model.LatLng;
import com.example.s198599.s198599_mappe3.models.ResortRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lib.Static_lib.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import lib.Static_lib;


/**
 * Created by espen on 11/3/15.
 */
public class FnuggAPI extends AsyncTask<USE_API, Void, String> {

    private ResortRepository repository;
    private String jsonResult;
    private JsonParser parser;
    private JsonElement root;
    private JsonArray outerArray;
    private FnuggCallback callback;

    //public static final String API_URL_INIT = "http://fnuggapi.cloudapp.net/search?sourceFields=_id,name,location&page=2&size=15";
    //public static final String API_URL_INIT = "http://fnuggapi.cloudapp.net/search?facet=region:%C3%98stlandet&sourceFields=_id,name,location";
    public static final String API_URL_SEARCH_ID = "http://fnuggapi.cloudapp.net/search?q=id:";
    public static final int API_NUM_RESORTS = 15;

    public FnuggAPI(FnuggCallback callback){
        this.callback = callback;
    }


    @Override
    protected String doInBackground(USE_API... params) {

        getRespositoryInstance();        //Få tak i repository

        for(USE_API param : params){


            //Finner ut hvilken jobb som skal gjøres med Jsonresultatet
            switch (param){
                case FNUGG_INIT:
                    getJsonStringForAllResorts();
                    initialImportLocationAndIds();
                    break;
            }

        }
        Log.d("RESORT", "Async - Returnerer tom ArrayList");

        return "";
    }



    @Override
    protected void onPostExecute(String result) {
        callback.notifyFnuggResult();
    }




    public void getRespositoryInstance(){
        repository = ResortRepository.getInstance();
    }



    public void getJsonStringForAllResorts(){

        String apiUrl = "http://fnuggapi.cloudapp.net/search?sourceFields=_id,name,location&size="+API_NUM_RESORTS+"&page=0";
        try{
            URL url;
            outerArray = new JsonArray();
            parser = new JsonParser();
            int page = 1;
            boolean doUntil = true;
            do{

                apiUrl = apiUrl.substring(0, apiUrl.length()-1);
                apiUrl += String.valueOf(page);
                url = new URL(apiUrl);
                Log.d("RESORT", "URL: " + url.toString());

                jsonResult = IOUtils.toString(url);             //Kjør API-kallet

                root = parser.parse(jsonResult);                //Finn rot-elementer i Json-objektet


                outerArray.addAll(root.getAsJsonObject().get("hits") //OuterArray er startpunktet for all data.
                        .getAsJsonObject().getAsJsonArray("hits"));
                page++;
            }while( doUntil);


            Log.d("RESORT", "Klar for JSON-konvertering til Java-objekter");
            Log.d("RESORT", jsonResult.toString());
        }catch(MalformedURLException me){
            Log.d("RESORT", "Exception - FnuggAPI - MalformedURLException");
        }catch (IOException ioe){
            Log.d("RESORT", "Exception - FnuggAPI - IOException");
        }
    }




    /**
     * Importerer alle Resorts, med Id'er og GPS-data
     */
    public void initialImportLocationAndIds(){
        Log.d("RESORT", "Starter import av Resort ID og lokasjon");

        for(JsonElement element: outerArray){
            Resort newResort = new Resort();
            JsonObject jsonResort = element.getAsJsonObject();

            int id = jsonResort.get("_id").getAsInt();
            String name = jsonResort.get("_source").getAsJsonObject().get("name").getAsString();


            JsonObject location = jsonResort.get("_source").getAsJsonObject().getAsJsonObject("location");
            LatLng loc = new LatLng(location.get("lat").getAsFloat(), location.get("lon").getAsFloat());

            newResort.setId(id);
            newResort.setName(name);
            newResort.setLocation(loc);
            repository.addResortToList(newResort);
            //Log.d("RESORT", "New Resort; " + id + " " + name);
        }
    }
}
