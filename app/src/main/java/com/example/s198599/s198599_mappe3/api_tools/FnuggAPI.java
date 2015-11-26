package com.example.s198599.s198599_mappe3.api_tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.example.s198599.s198599_mappe3.MainActivity;
import com.example.s198599.s198599_mappe3.models.Contact;
import com.example.s198599.s198599_mappe3.models.ImageScaled;
import com.example.s198599.s198599_mappe3.models.Images;
import com.example.s198599.s198599_mappe3.models.Lifts;
import com.example.s198599.s198599_mappe3.models.Resort;
import com.example.s198599.s198599_mappe3.models.Slopes;
import com.google.android.gms.maps.model.LatLng;
import com.example.s198599.s198599_mappe3.models.Repository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lib.FnuggUrlBuilder;
import lib.Static_lib.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by espen on 11/3/15.
 */
public class FnuggAPI extends AsyncTask<USE_API, Void, String> {

    private Repository repository;
    private String jsonResult;
    private JsonParser parser;
    private JsonElement root;
    private JsonArray outerArray;
    private FnuggCallback callback;
    Context context;

    private Set<String> selectedRegionsPref;



    public FnuggAPI(Context context, FnuggCallback callback){
        this.context = context;
        this.callback = callback;
        readPreferenceRegionsSelected();
        Log.d("RESORT", "FnuggAPI - Prøver å lese hvilke regioner som er valgt i Preferences");
        for(String s : selectedRegionsPref){
            Log.d("RESORT", "Selected region: " + s);
        }
    }

    public FnuggAPI(){

    }


    public void readPreferenceRegionsSelected(){
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selectedRegionsPref = prefs.getStringSet("regionsSelected", new HashSet<String>());
    }


    @Override
    protected String doInBackground(USE_API... params) {

        repository = Repository.getInstance();        //Få tak i repository

        for(USE_API param : params){
            //Finner ut hvilken jobb som skal gjøres med Jsonresultatet
            switch (param){
                case FNUGG_INIT:
                    repository.emptyResortList();       //Sletter listen før man importerer på nytt.
                    getJsonStringForAllResorts();
                    //Log.d("RESORT", outerArray.toString());
                    initialImportLocationAndIds();
                    break;
                case FNUGG_DETAIL:
                    Log.d("RESORT", "FNUGG_DETAIL er valgt");
                    getResortDetails();
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


    public void getJsonStringForAllResorts(){
        //http://fnuggapi.cloudapp.net/search?sourceFields=_id,name,location&range=id:11|20
        String urlTemplate = "http://fnuggapi.cloudapp.net/search?sourceFields=_id,name,location&range=id:";
        try{
            URL url;
            outerArray = new JsonArray();
            parser = new JsonParser();
            int from = 91;
            int to = 100;
            boolean doUntil = true;
            while( doUntil ){
                String apiUrl = urlTemplate;
                apiUrl = apiUrl + (from +"|" + to);
                url = new URL(apiUrl);
                //Log.d("RESORT", "URL: " + url.toString());

                jsonResult = IOUtils.toString(url);             //Kjør API-kallet
                root = parser.parse(jsonResult);                //Finn rot-elementer i Json-objektet

                if(root.getAsJsonObject().get("hits").getAsJsonObject().get("total").getAsInt() == 0)
                    doUntil = false;

                outerArray.addAll(root.getAsJsonObject().get("hits") //OuterArray er startpunktet for all data.
                        .getAsJsonObject().getAsJsonArray("hits"));
                from = to +1;
                to += 10;
            };


            Log.d("RESORT", "Klar for JSON-konvertering til Java-objekter");
            //Log.d("RESORT", jsonResult.toString());
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


    public void getResortDetails(){
        int selectedId = repository.getResortMarkerClicked();

        String apiDetailFirstPart = "http://fnuggapi.cloudapp.net/get/resort/";
        String apiDetailLastPart = "?sourceFields=_id,name,contact,lifts,slopes,images";
        String detailUrl = apiDetailFirstPart + selectedId + apiDetailLastPart;
        Resort selectedResort = repository.getResortById(selectedId);

        try{
            URL url;

            url = new URL(detailUrl);
            Log.d("RESORT", "URL: " + url.toString());

            String jsonResult = IOUtils.toString(url);             //Kjør API-kallet
            JsonParser parser = new JsonParser();
            JsonElement root = parser.parse(jsonResult);                //Finn rot-elementer i Json-objektet
            Log.d("RESORT", jsonResult.toString());

            JsonObject outerObject = root.getAsJsonObject().get("_source").getAsJsonObject();

            saveLiftsAndSlopes(outerObject, selectedResort);
            saveImages(outerObject, selectedResort);
            saveContactInfo(outerObject, selectedResort);

            Log.d("RESORT", "Klar for JSON-konvertering til Java-objekter");

        }catch(MalformedURLException me){
            Log.d("RESORT", "Exception - FnuggAPI - MalformedURLException");
        }catch (IOException ioe){
            Log.d("RESORT", "Exception - FnuggAPI - IOException");
        }
    }

    public void saveLiftsAndSlopes(JsonObject outerObject, Resort selectedResort) throws IOException{
        Lifts lifts = new Lifts();
        Slopes slopes = new Slopes();

        JsonObject lift = outerObject.getAsJsonObject("lifts");
        JsonObject slope = outerObject.getAsJsonObject("slopes");

        lifts.setTotal(lift.get("count").getAsInt());
        lifts.setOpen(lift.get("open").getAsInt());

        slopes.setTotal(slope.get("count").getAsInt());
        slopes.setOpen(slope.get("open").getAsInt());

        selectedResort.setLifts(lifts);
        selectedResort.setSlopes(slopes);

    }

    public void saveImages(JsonObject outerObject, Resort selectedResort) throws IOException{


        DisplayMetrics dm = new DisplayMetrics();
        Display display = MainActivity.DISPLAY;
        display.getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        Log.d("RESORT", "Screen Width: " + width);
        Images images = new Images();
        ImageScaled scale = new ImageScaled();
        images.setScaled(scale);
        selectedResort.setImages(images);

        JsonObject jsonImages = outerObject.get("images").getAsJsonObject();

        String imgSize = "";
        if(width <= 480){
            imgSize = "image_16_9_s";
        }else if(width <= 1024) {
            imgSize = "image_16_9_m";
        }else{
            imgSize = "image_16_9_xl";
        }
        Bitmap image_16_9 = BitmapFactory.decodeStream((InputStream) new URL(jsonImages.get(imgSize).getAsString()).getContent());
        images.setImage_16_9(image_16_9);

    }

    public void saveContactInfo(JsonObject outerObject, Resort selectedResort) throws IOException{

        String address = outerObject.get("contact").getAsJsonObject().get("address").getAsString();
        String zipCode = outerObject.get("contact").getAsJsonObject().get("zip_code").getAsString();
        String city = outerObject.get("contact").getAsJsonObject().get("city").getAsString();
        String phoneService = outerObject.get("contact").getAsJsonObject().get("phone_servicecenter").getAsString();
        String phonePatrol = outerObject.get("contact").getAsJsonObject().get("phone_skipatrol").getAsString();
        String callNumber = outerObject.get("contact").getAsJsonObject().get("call_number").getAsString();
        String email = outerObject.get("contact").getAsJsonObject().get("email").getAsString();
        Contact contact = new Contact(address, zipCode, city, phoneService, phonePatrol, callNumber, email);
        selectedResort.setContact(contact);
    }

}
