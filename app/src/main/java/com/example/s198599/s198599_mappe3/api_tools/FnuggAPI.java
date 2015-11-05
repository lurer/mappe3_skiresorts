package com.example.s198599.s198599_mappe3.api_tools;

import android.location.Location;
import android.os.AsyncTask;

import android.util.Log;

import com.example.s198599.s198599_mappe3.models.Resort;
import com.example.s198599.s198599_mappe3.models.ResortLocation;
import com.example.s198599.s198599_mappe3.models.ResortRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lib.Static_lib;


/**
 * Created by espen on 11/3/15.
 */
public class FnuggAPI extends AsyncTask<String, Void, String> {

    private ResortRepository repository;
    private String jsonResult;
    private JsonParser parser;
    private JsonElement root;
    private JsonArray outerArray;
    private JSONCallback callback;



    public FnuggAPI(JSONCallback callback){
        this.callback = callback;
    }


    @Override
    protected String doInBackground(String... urls) {

        readyingListForImport();        //Få tak i repository

        for(String apiUrl : urls){

            try{

                URL url = new URL(apiUrl);
                Log.d("RESORT", "URL: " + url.toString());

                jsonResult = IOUtils.toString(url);
                parser = new JsonParser();
                root = parser.parse(jsonResult);
                outerArray = root.getAsJsonObject().get("hits")
                        .getAsJsonObject().getAsJsonArray("hits");
                Log.d("RESORT", "Klar for JSON-konvertering til Java-objekter");

            }catch(MalformedURLException me){
                Log.d("RESORT", "Exception - FnuggAPI - MalformedURLException");

            }catch (IOException ioe){
                Log.d("RESORT", "Exception - FnuggAPI - IOException");
            }


            //Finner ut hvilken jobb som skal gjøres med Jsonresultatet
            switch (apiUrl){
                case Static_lib.API_URL_INIT:
                    initialImportLocationAndIds();
                    break;
            }

        }
        Log.d("RESORT", "Async - Returnerer tom ArrayList");

        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        //callback.notifyJsonResult(result);
    }




    public void readyingListForImport(){
        repository = ResortRepository.getInstance();
    }


    /**
     * Importerer alle Resorts
     */
    public void initialImportLocationAndIds(){
        Log.d("RESORT", "Starter import av Resort ID og lokasjon");
        for(JsonElement element: outerArray){
            Resort newResort = new Resort();
            JsonObject jsonResort = element.getAsJsonObject();

            int id = jsonResort.get("_id").getAsInt();
            String name = jsonResort.get("_source")
                    .getAsJsonObject().get("name").getAsString();

            ResortLocation loc = new ResortLocation();
            JsonObject location = jsonResort.get("_source").getAsJsonObject()
                    .getAsJsonObject("location");
            loc.setLon(location.get("lon").getAsFloat());
            loc.setLat(location.get("lat").getAsFloat());

            newResort.setId(id);
            newResort.setName(name);
            newResort.setLocation(loc);
            repository.addResortToList(newResort);
            Log.d("RESORT", "New Resort; " + id + " " + name);
        }
    }
}
