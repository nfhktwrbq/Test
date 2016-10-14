package ru.andrew.test;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File sdDir = android.os.Environment.getExternalStorageDirectory();
        Log.e("Test", sdDir.getAbsolutePath());
      /*  try {
            JSONObject object = (JSONObject) parser.parse(
                    new FileReader(loadJSONFromAsset()));
            JSONArray messages = (JSONArray) object.get("citiesFrom");
            Log.e("Test", "citiesFrom:");
            Iterator<String> iterator = messages.iterator();
            while(iterator.hasNext()) {
                Log.e("Test", "> " + iterator.next());
            }
        } catch (IOException | ParseException ex) {
            Logger.getLogger(MainActivity.class.getName())
                    .log(Level.SEVERE, null, ex);
            Log.e("Test", "exception occur");
        }*/


        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArray = obj.getJSONArray("citiesFrom");
           // Log.e("Test", m_jArray.getJSONObject(1).getString("countryTitle"));
           ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArray.length(); i++) {
                JSONObject jo_inside = m_jArray.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("countryTitle"));
                String formula_value = jo_inside.getString("countryTitle");
                Log.d("Details-->", jo_inside.getString("cityTitle"));
                String url_value = jo_inside.getString("cityTitle");

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("countryTitle", formula_value);
                m_li.put("cityTitle", url_value);

                formList.add(m_li);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Test", "exception occur");
            Log.e("Test", "exception occur");
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("allStations.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}
