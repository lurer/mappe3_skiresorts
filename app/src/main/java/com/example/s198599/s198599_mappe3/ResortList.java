package com.example.s198599.s198599_mappe3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.s198599.s198599_mappe3.api_tools.FnuggAPI;
import com.example.s198599.s198599_mappe3.api_tools.FnuggCallback;
import com.example.s198599.s198599_mappe3.models.Resort;
import com.example.s198599.s198599_mappe3.models.ResortAdapterCallback;
import com.example.s198599.s198599_mappe3.models.ResortListFragment;
import com.example.s198599.s198599_mappe3.models.Repository;

import lib.Static_lib;

/**
 * Created by espen on 11/23/15.
 */
public class ResortList extends AppCompatActivity implements ResortAdapterCallback, FnuggCallback{


    private Resort selectedResort;
    private FnuggAPI fnuggApi;
    private Repository repository;
    private ResortListFragment listFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resort_list);
        repository = Repository.getInstance();
        listFragment = (ResortListFragment)getFragmentManager().findFragmentById(R.id.resort_list_view_activity);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_resort_list, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i;
        switch(item.getItemId()){

            case R.id.showMap:
                onBackPressed();
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


    @Override
    public void onItemClicked(Resort resort) {
        Log.d("RESORT", "Selected resort != null");
        selectedResort = resort;
        fnuggApi = new FnuggAPI(this, this);              //Grensesnittet mot APIet
        repository.setResortMarkerClicked(selectedResort.getId());
        fnuggApi.execute(Static_lib.USE_API.FNUGG_DETAIL);
    }


    @Override
    public void notifyFnuggResult() {

        Intent i = new Intent(this, ResortInfoActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("ResortID", selectedResort.getId());
        startActivity(i);
    }


    public void sortAlphabeticallyAsc(View view){
        if(repository.isLoaded())
            Toast.makeText(this, getString(R.string.sortAlphaAsc), Toast.LENGTH_SHORT).show();
            listFragment.sortAlphabeticallyAsc();
    }

    public void sortAlphabeticallyDesc(View view){
        if(repository.isLoaded())
            Toast.makeText(this, getString(R.string.sortAlphaDesc), Toast.LENGTH_SHORT).show();
            listFragment.sortAlphabeticallyDesc();
    }

    public void sortDistanceAsc(View view){
        if(repository.isLoaded())
            Toast.makeText(this, getString(R.string.sortDistAsc), Toast.LENGTH_SHORT).show();
            listFragment.sortDistanceAsc();
    }

    public void sortDistanceDesc(View view){
        if(repository.isLoaded())
            Toast.makeText(this, getString(R.string.sortDistDesc), Toast.LENGTH_SHORT).show();
            listFragment.sortDistanceDesc();
    }

}
