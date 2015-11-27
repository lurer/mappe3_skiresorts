package com.example.s198599.s198599_mappe3.api_tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;

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

    private Set<String> selectedRegions;
    private int numLifts;
    private int numSlopes;
    private boolean debugEnabled;
    private FnuggUrlBuilder urlBuilder;

    public FnuggAPI(Context context, FnuggCallback callback){
        this.context = context;
        this.callback = callback;
        getSelectedPreferences();
        urlBuilder = new FnuggUrlBuilder();

    }

    public FnuggAPI(){

    }

    /**
     * Leser av SharedPreferences.
     */
    public void getSelectedPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String lifts = prefs.getString("prefLiftFilterList", "1");

        String slopes = prefs.getString("prefSlopeFilterList", "1");
        Log.d("RESORT", "Lifts selected: " + lifts + ", Slopes selected: " + slopes);
        selectedRegions = prefs.getStringSet("prefMultichoiceRegions", new HashSet<String>());
        debugEnabled = prefs.getBoolean("prefDebug", true);
        numLifts = Integer.parseInt(lifts);
        numSlopes = Integer.parseInt(slopes);
    }


    @Override
    protected String doInBackground(USE_API... params) {

        repository = Repository.getInstance();        //Få tak i repository

        for(USE_API param : params){
            //Finner ut hvilken jobb som skal gjøres med Jsonresultatet
            switch (param){
                case FNUGG_INIT:
                    repository.emptyResortList();       //Sletter listen før man importerer på nytt.
                    buildJsonStringForAllResorts();     //Kjører API-kall og finner root-elementet
                    //Log.d("RESORT", outerArray.toString());
                    importLocationAndIdsToRepo();       //Importerer til Repository
                    break;
                case FNUGG_DETAIL:
                    Log.d("RESORT", "FNUGG_DETAIL er valgt");
                    getResortDetails();                 //Henter detaljer om et spesifikt skisted
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



    public void buildJsonStringForAllResorts(){


        try{
            URL url;
            outerArray = new JsonArray();
            parser = new JsonParser();

            boolean doUntil = true;
            int page = 1;

            int totalResults = testExpectedApiResult();     //Kjører test for å avgjøre forventet resultat

            if(totalResults > 0){           //Bare hvis det finnes noe resultat

                while( doUntil ){

                    String apiUrl = urlBuilder.startUrl()
                            .sourceFields()
                            .addRegions(selectedRegions)
                            .addLiftFilter(numLifts, 200)
                            .addSlopeFilter(numSlopes, 200)
                            .page(page)
                            .build();

                    url = new URL(apiUrl);
                    Log.d("RESORT", "URL: " + url.toString());

                    jsonResult = IOUtils.toString(url);             //Kjør API-kallet
                    root = parser.parse(jsonResult);                //Finn rot-elementer i Json-objektet

                    JsonArray hits = root.getAsJsonObject().get("hits").getAsJsonObject().getAsJsonArray("hits");

                    if(hits.size() == 0 ){
                        doUntil = false;
                    }else{
                        outerArray.addAll(hits);
                    }
                    if(debugEnabled){
                        if(outerArray.size() > 15)
                            doUntil = false;
                    }
                    Log.d("RESORT", "Page = " + page);
                    page++;
                };
            }


            Log.d("RESORT", "Klar for JSON-konvertering til Java-objekter");
            //Log.d("RESORT", jsonResult.toString());
        }catch(MalformedURLException me){
            Log.d("RESORT", "Exception - FnuggAPI - MalformedURLException");
        }catch (IOException ioe){
            Log.d("RESORT", "Exception - FnuggAPI - IOException");
        }
    }


    /**
     * Testmetode som returnerer forventet antall skisteder.
     * @return
     */
    public int testExpectedApiResult(){
        String test = urlBuilder.startUrl()
                .sourceFields()
                .addRegions(selectedRegions)
                .addLiftFilter(numLifts, 200)
                .addSlopeFilter(numSlopes, 200)
                .build();

        try {
            URL url = new URL(test);
            Log.d("RESORT", "Tester Fnugg-URL: " + url.toString());

            jsonResult = IOUtils.toString(url);             //Kjør API-kallet
            root = parser.parse(jsonResult);                //Finn rot-elementer i Json-objektet
            int testresultat =  root.getAsJsonObject().get("hits").getAsJsonObject().get("total").getAsInt();

            Log.d("RESORT", "Resultat av testkjøring er: " + testresultat);
            return testresultat;
        }catch(MalformedURLException me){
            Log.d("RESORT", "Exception - FnuggAPI - MalformedURLException");
        }catch (IOException ioe){
            Log.d("RESORT", "Exception - FnuggAPI - IOException");
        }
        return 0;
    }




    /**
     * Importerer alle Resorts, med Id'er og GPS-data
     */
    public void importLocationAndIdsToRepo(){
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
            //Log.d("RESORT", jsonResult.toString());

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


    /**
     * Avgjør hvilket bilde som passer best til enheten og importer det.
     * @param outerObject
     * @param selectedResort
     * @throws IOException
     */
    public void saveImages(JsonObject outerObject, Resort selectedResort) throws IOException{


        DisplayMetrics dm = new DisplayMetrics();
        Display display = MainActivity.DISPLAY;
        display.getMetrics(dm);
        int rotationAngle = display.getRotation();
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
            if(rotationAngle == Surface.ROTATION_90 || rotationAngle == Surface.ROTATION_270)
                imgSize = "image_1_1_l";
            else
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
