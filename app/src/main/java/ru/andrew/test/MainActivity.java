package ru.andrew.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "allStations.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) parser.parse(
                    new FileReader(FILENAME));
            // Получаем имя
           /* String country = (String) object.get("countryTitle");
            Log.e("Test", "Country: " + name);
            //System.out.println("Name: " + name);
            // Получаем сайт
            String city = (String) object.get("cityTitle");
            Log.e("Test", "City: " + city);
            //System.out.println("Site: " + site);
            // Получаем возраст
            // Получаем сообщения*/
            JSONArray messages = (JSONArray) object.get("citiesFrom");
            Log.e("Test", "citiesFrom:");
            Iterator<String> iterator = messages.iterator();
            while(iterator.hasNext()) {
                Log.e("Test", "> " + iterator.next());
            }
        } catch (IOException | ParseException ex) {
            /*Logger.getLogger(MainActivity.class.getName())
                    .log(Level.SEVERE, null, ex);*/

        }


    }
}
