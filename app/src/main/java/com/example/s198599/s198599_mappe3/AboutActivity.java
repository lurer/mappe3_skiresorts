package com.example.s198599.s198599_mappe3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView provides = (TextView)findViewById(R.id.aboutProvides);
        TextView information = (TextView)findViewById(R.id.aboutInformation);

        provides.setText(getTextFromFile("provides"));
        information.setText(getTextFromFile("information"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.quit:
                finishAffinity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public String getTextFromFile(String type){
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        switch (type){
            case "provides":
                is = getResources().openRawResource(R.raw.about_provides);
                break;
            case "information":
                is = getResources().openRawResource(R.raw.about_information);
                break;
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            if(reader != null){
                String line = reader.readLine();

                while (line != null){
                    sb.append(line);
                    sb.append("\n");
                    line = reader.readLine();
                }
            }

        }catch(Exception ioe){
            sb.append("Error. Can not find the rules text.");

        }


        return sb.toString();
    }
}
