package com.example.s198599.s198599_mappe3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.s198599.s198599_mappe3.api_tools.DistanceCallback;
import com.example.s198599.s198599_mappe3.api_tools.FnuggAPI;
import com.example.s198599.s198599_mappe3.api_tools.FnuggCallback;
import com.example.s198599.s198599_mappe3.api_tools.GoogleDistanceAPI;
import com.example.s198599.s198599_mappe3.models.Resort;
import com.example.s198599.s198599_mappe3.models.Repository;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import lib.Static_lib;

public class MainActivity extends AppCompatActivity implements FnuggCallback{


    private Repository repository;
    private MyMapHandler mapHandler;
    public static Display DISPLAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DISPLAY = getWindowManager().getDefaultDisplay();
        repository = Repository.getInstance();
        mapHandler = new MyMapHandler();

        startImport();
    }

    public void startImport(){
        Log.d("RESORT", "MainActivity - Starting import");
        if(!repository.isLoaded()){                 //Resette alt før ny import
            repository.setIsLoaded(false);
            repository.setCustomMarkerLatLng(null);
            repository.setCustomMapMarker(null);
            new FnuggAPI(this, this).execute(Static_lib.USE_API.FNUGG_INIT); //Grensesnittet mot APIet

        }else{
            notifyFnuggResult();        //Bruk eksisterende data
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i;
        switch(item.getItemId()){
            case R.id.showList:
                i = new Intent(this, ResortList.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.reload:
                Log.d("RESORT", "Reload is pressed");
                mapHandler.clearMap();
                repository.setIsLoaded(false);
                startImport();
                break;
            case R.id.showInfo:
                i = new Intent(this, AboutActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.settings:
                i = new Intent(this, PreferenceActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.quit:
                finishAffinity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Callback fra FnuggAPI i det den er ferdig med import.
     * Starter så kartet.
     */
    @Override
    public void notifyFnuggResult() {
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapHandler);
    }





    /**
     * Klassen som håndterer alle kartting
     */
    private class MyMapHandler implements OnMapReadyCallback, DistanceCallback, FnuggCallback{


        private List<Resort> list;
        private GoogleMap map;
        private LatLng myLocation;
        private Resort resort;
        private float zoomLevel;
        private FnuggAPI fnuggApi;

        public MyMapHandler(){
            list = repository.getResorts();
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            zoomLevel = prefs.getFloat("zoomLevel", 9.0f);

        }

        /**
         * Når kartet er klart lastes de importerte dataene inn.
         * @param googleMap
         */
        @Override
        public void onMapReady(final GoogleMap googleMap) {
            Log.d("RESORT", "I onMapReady");

            map = googleMap;

            runLocationAndMapOperations();     //Bestem


            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    resort = repository.matchLocationinformation(marker.getPosition());
                    if (resort != null) {
                        Log.d("RESORT", "Marker clicked. Info: = " + resort.getId() + " " + resort.getName());

                        repository.setResortMarkerClicked(resort.getId());
                        fnuggApi = new FnuggAPI(getBaseContext(), mapHandler);              //Grensesnittet mot APIet
                        fnuggApi.execute(Static_lib.USE_API.FNUGG_DETAIL);

                    }
                    return false;
                }
            });

            map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {

                    myLocation = latLng;                            //MyLocation er nå "custom"
                    repository.setCustomMarkerLatLng(latLng);
                    Toast.makeText(getBaseContext(),
                            getString(R.string.positionUpdate), Toast.LENGTH_SHORT).show();

                    addCustomMarkerOnMap();         //Setter eller bytter ut Custom marker på kartet
                    runGoogleDistanceApi();         //Kjører nye beregninger på distanse.
                }
            });

        }


        public void runLocationAndMapOperations(){
            if (repository.getCustomMarkerLatLng() == null){
                getMyLocationUsingGps();
            }else{
                myLocation = repository.getCustomMarkerLatLng();
                addCustomMarkerOnMap();

                repository.setIsLoaded(false);  //Åpne for å kalkulere distanser på nytt
            }


            addMarkersOnMap();      //Legger til alle markers på kartet
            if(!repository.isLoaded()) {
                runGoogleDistanceApi();
            }
        }


        public void runGoogleDistanceApi(){

            new GoogleDistanceAPI(this).execute(myLocation);
        }


        /**
         * Setter CustomMarker på kartet og zoomer inn dit.
         * @return
         */
        public void addCustomMarkerOnMap(){

            if(repository.getCustomMapMarker() != null){
                repository.getCustomMapMarker().remove();
            }

            Marker customMarker = map.addMarker(new MarkerOptions()
                    .position(myLocation)
                    .title(getString(R.string.myLocation))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            repository.setCustomMapMarker(customMarker);

            zoomMapToMyLocation();
        }

        /**
         * Løper gjennom dataene som er importert og legger markers på kartet
         */
        public void addMarkersOnMap() {
            for (Resort r : list) {
                LatLng ll = r.getLocation();
                map.addMarker(new MarkerOptions().position(ll).title(r.getName()));
            }
            map.setMyLocationEnabled(true);
        }


        public void clearMap(){
            map.clear();
        }



        public void getMyLocationUsingGps(){
            try{
                LocationManager locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                Criteria crit = new Criteria();
                Location loc = locMan.getLastKnownLocation(locMan.getBestProvider(crit, false));
                myLocation = new LatLng(loc.getLatitude(), loc.getLongitude());

                zoomMapToMyLocation();
            } catch (Exception se){
                Log.d("RESORT", "Security xception - Kan ikke få tak i siste lokasjon");

            }
        }


        /**
         * Posisjonerer kartet til min lokasjon
         */
        public void zoomMapToMyLocation(){
            try{
                CameraPosition camPos = new CameraPosition.Builder()
                        .target(myLocation)
                        .zoom(zoomLevel)
                        .build();
                CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);
                map.moveCamera(camUpdate);
            } catch (SecurityException se){
                Log.d("RESORT", "Security xception - Kan ikke få tak i siste lokasjon");
            }
        }

        /**
         * Callback fra GoogleDistanceAPI
         */
        @Override
        public void notifyDistanceResult() {

        }


        @Override
        public void notifyFnuggResult() {

            Intent i = new Intent(getBaseContext(), ResortInfoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("ResortID", resort.getId());
            startActivity(i);
        }
    }//MyMapHandler
}
