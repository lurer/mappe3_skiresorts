package com.example.s198599.s198599_mappe3.api_tools;

import android.os.AsyncTask;

import android.util.Log;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by espen on 11/3/15.
 */
public class FnuggAPI extends AsyncTask<String, Void, String> {

    private StringBuilder sb;


    public FnuggAPI(){}


    @Override
    protected String doInBackground(String... urls) {

        for(String u : urls){
            try{
                sb = new StringBuilder();
                String s;
                URL url = new URL(u);
                Log.d("RESORT", "URL: " + url.toString());
                HttpURLConnection conn = (HttpURLConnection)
                        url.openConnection();
                conn.setRequestMethod(("GET"));
                conn.setRequestProperty("Accept", "application/json");


                if(conn.getResponseCode() != 200){
                    Log.d("JSON", "Errorcode forskjellig fra 200");
                    throw new RuntimeException("Failed:HTTP error code: "
                            + conn.getResponseCode());
                }

                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                Gson gson = new Gson();

                System.out.println("Output from Server... \n");
/*                while ((s = reader.readLine()) != null){
                    sb.append(s);
                } */

                conn.disconnect();
                return sb.toString();
            }catch(Exception e){
                Log.d("RESORT", "Exception - FnuggAPI");

            }
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {

    }
}
