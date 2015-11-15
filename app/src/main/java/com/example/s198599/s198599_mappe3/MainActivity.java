package com.example.s198599.s198599_mappe3;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.s198599.s198599_mappe3.api_tools.FnuggAPI;
import com.example.s198599.s198599_mappe3.api_tools.JSONCallback;
import com.example.s198599.s198599_mappe3.models.Resort;
import com.example.s198599.s198599_mappe3.models.ResortRepository;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import lib.Static_lib;

public class MainActivity extends AppCompatActivity implements JSONCallback, OnMapReadyCallback{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FnuggAPI api = new FnuggAPI(this);              //Grensesnittet mot APIet
        api.execute(Static_lib.API_URL_INIT);       //Starter AsyncTask

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.showList:
                break;
            case R.id.showMap:
                break;
            case R.id.showInfo:
                break;
            case R.id.settings:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void notifyJsonResult() {
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("RESORT", "I onMapReady");
        ResortRepository repo = ResortRepository.getInstance();
        List<Resort> list = repo.getResorts();

        for(Resort r : list){
            Log.d("RESORT", "I onMapReady " + r.getLocation().toString());
            LatLng ll = r.getLocation();
            googleMap.addMarker(new MarkerOptions().position(ll).title(r.getName()));

        }
        googleMap.setMyLocationEnabled(true);

        try{
            LocationManager locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Criteria crit = new Criteria();
            Location myLocation = locMan.getLastKnownLocation(locMan.getBestProvider(crit, false));
            CameraPosition camPos = new CameraPosition.Builder()
                    .target(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                    .zoom(8.0f)
                    .build();
            CameraUpdate camUpate = CameraUpdateFactory.newCameraPosition(camPos);
            googleMap.moveCamera(camUpate);
        }catch(SecurityException se){
            Log.d("RESORT", "Security xception - Kan ikke få tak i siste lokasjon");
        }


    }
}
