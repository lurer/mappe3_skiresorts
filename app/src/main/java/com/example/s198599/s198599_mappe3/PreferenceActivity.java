package com.example.s198599.s198599_mappe3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.s198599.s198599_mappe3.models.ResortRepository;

public class PreferenceActivity extends AppCompatActivity{

    public static SharedPreferences prefs;

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

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);

            prefZoom = (CheckBoxPreference)findPreference("zoom_checkbox");
            prefZoom.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    ResortRepository.getInstance().setIsLoaded(false);

                    SharedPreferences.Editor editor= PreferenceActivity.prefs.edit();

                    if(prefZoom.isChecked())
                        editor.putFloat("zoomLevel", 9.0f);
                    else
                        editor.putFloat("zoomLevel", 7.0f);

                    editor.commit();

                    return true;
                }
            });
        }
    }
}
