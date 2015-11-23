package com.example.s198599.s198599_mappe3;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s198599.s198599_mappe3.models.Resort;
import com.example.s198599.s198599_mappe3.models.ResortRepository;

import java.util.HashMap;
import java.util.Map;

public class ResortInfoActivity extends AppCompatActivity {

    private int resortID;
    private ResortRepository repository;
    private Resort resort;
    private Map<String, View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resort_info);
        Log.d("RESORT", "ResortInfoActivity");

        repository = ResortRepository.getInstance();

        Intent thisIntent = this.getIntent();
        resortID = thisIntent.getIntExtra("ResortID", -1);
        Log.d("RESORT", "ResortId i ResortInfoActivity er: " + resortID);

        if(resortID != -1){
            resort = repository.getResortById(resortID);
            setTitle(resort.getName());
            views = new HashMap<>();
            createTextViews();

            fillOutResortInformation();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resort_info, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()){
            case android.R.id.home:
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



    public void createTextViews(){
        views.put("rName", findViewById(R.id.rName));
        views.put("rDistance", findViewById(R.id.rDistance));
        views.put("rDuration", findViewById(R.id.rDuration));
        views.put("rLifts", findViewById(R.id.rLifts));
        views.put("rSlopes", findViewById(R.id.rSlopes));
        views.put("rImage", findViewById(R.id.detailImage));
        views.put("rAddress", findViewById(R.id.rAddress));
        views.put("rZip_code", findViewById(R.id.rZip_code));
        views.put("rCity", findViewById(R.id.rCity));
        views.put("rService", findViewById(R.id.rService));
        views.put("rPatrol", findViewById(R.id.rPatrol));
        views.put("rPhone", findViewById(R.id.rPhone));
        views.put("rEmail", findViewById(R.id.rEmail));

    }

    public void fillOutResortInformation(){
        for(Map.Entry t: views.entrySet()){
            switch ((String)t.getKey()){
                case "rName":
                    ((TextView)t.getValue()).setText(" " +resort.getName());
                    break;
                case "rDistance":
                    ((TextView)t.getValue()).setText(resort.getDistance().getDistanceKmString());
                    break;
                case "rDuration":
                    ((TextView)t.getValue()).setText(resort.getDistance().getDurationString());
                    break;
                case "rLifts":
                    ((TextView)t.getValue()).setText(resort.getLifts().toString());
                    break;
                case "rSlopes":
                    ((TextView)t.getValue()).setText(resort.getSlopes().toString());
                    break;
                case "rImage":
                    ((ImageView)t.getValue()).setImageBitmap(resort.getImages().getImage_16_9());
                    break;
                case "rAddress":
                    ((TextView)t.getValue()).setText(resort.getContact().getAddress());
                    break;
                case "rZip_code":
                    ((TextView)t.getValue()).setText(resort.getContact().getZip_code());
                    break;
                case "rCity":
                    ((TextView)t.getValue()).setText(resort.getContact().getCity());
                    break;
                case "rService":
                    ((TextView)t.getValue()).setText(resort.getContact().getPhone_servicecenter());
                    break;
                case "rPatrol":
                    ((TextView)t.getValue()).setText(resort.getContact().getPhone_skipatrol());
                    break;
                case "rPhone":
                    ((TextView)t.getValue()).setText(resort.getContact().getCall_number());
                    break;
                case "rEmail":
                    ((TextView)t.getValue()).setText(resort.getContact().getEmail());
                    break;
            }
        }
    }
}
