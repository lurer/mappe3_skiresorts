package com.example.s198599.s198599_mappe3;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.s198599.s198599_mappe3.models.Repository;

import java.util.HashSet;
import java.util.Set;

import lib.Static_lib;

public class PreferenceActivity extends AppCompatActivity{

    public static SharedPreferences prefs;
    public static Set<String> regions;
    static SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        getFragmentManager().beginTransaction().
                replace(android.R.id.content, new PrefFragment()).
                commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_preference, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Repository.getInstance().setIsLoaded(false);
                PrefFragment.updateRegionsSelected();
                editor.putStringSet("regionsSelected", regions).commit();
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.quit:
                finishAffinity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class PrefFragment extends PreferenceFragment{

        private CheckBoxPreference prefZoom;
        private static CheckBoxPreference prefNordNorge;
        private static CheckBoxPreference prefMidtNorge;
        private static CheckBoxPreference prefNordVestlandet;
        private static CheckBoxPreference prefSorVestlandet;
        private static CheckBoxPreference prefSorlandet;
        private static CheckBoxPreference prefOstlandet;




        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            editor= PreferenceActivity.prefs.edit();
            regions = new HashSet<>();

            addPreferencesFromResource(R.xml.preferences);

            prefZoom = (CheckBoxPreference)findPreference("zoom_checkbox");
            prefNordNorge= (CheckBoxPreference)findPreference("prefNordNorge");
            prefMidtNorge = (CheckBoxPreference)findPreference("prefMidtNorge");
            prefNordVestlandet = (CheckBoxPreference)findPreference("prefNordVestlandet");
            prefSorVestlandet = (CheckBoxPreference)findPreference("prefSorVestlandet");
            prefSorlandet = (CheckBoxPreference)findPreference("prefSorlandet");
            prefOstlandet = (CheckBoxPreference)findPreference("prefOstlandet");

            prefZoom.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {


                    if(prefZoom.isChecked())
                        editor.putFloat("zoomLevel", 9.0f);
                    else
                        editor.putFloat("zoomLevel", 7.0f);

                    editor.commit();

                    return true;
                }
            });



        }


        public static void updateRegionsSelected(){
            Log.d("RESORT", "Preferences - Selecting Regions");

            if(prefNordNorge.isChecked())
                regions.add(Static_lib.prefNordNorge);
            else if(regions.contains(Static_lib.prefNordNorge))
                regions.remove(Static_lib.prefNordNorge);

            if(prefMidtNorge.isChecked())
                regions.add(Static_lib.prefMidtNorge);
            else if(regions.contains(Static_lib.prefMidtNorge))
                regions.remove(Static_lib.prefMidtNorge);

            if(prefNordVestlandet.isChecked())
                regions.add(Static_lib.prefNordVestlandet);
            else if(regions.contains(Static_lib.prefNordVestlandet))
                regions.remove(Static_lib.prefNordVestlandet);

            if(prefSorVestlandet.isChecked())
                regions.add(Static_lib.prefSorVestlandet);
            else if(regions.contains(Static_lib.prefSorVestlandet))
                regions.remove(Static_lib.prefSorVestlandet);

            if(prefSorlandet.isChecked())
                regions.add(Static_lib.prefSorlandet);
            else if(regions.contains(Static_lib.prefSorlandet))
                regions.remove(Static_lib.prefSorlandet);

            if(prefOstlandet.isChecked())
                regions.add(Static_lib.prefOstlandet);
            else if(regions.contains(Static_lib.prefOstlandet))
                regions.remove(Static_lib.prefOstlandet);

        }
    }
}
