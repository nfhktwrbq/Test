package ru.andrew.test;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {

    ListView lvList;
    TextView tvInception;
    TextView tvDestination;
    TextView tvDate;
    ArrayList<String> inceptionPlace = new ArrayList<String>();
    ArrayList<String> destinationPlace = new ArrayList<String>();
    ArrayAdapter adapterInception;
    ArrayAdapter adapterDestination;
    String tvPressName;
    boolean d = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvList = (ListView) findViewById(R.id.listViewIncept);
        tvInception = (TextView) findViewById(R.id.inceptionTextView);
        tvDestination = (TextView) findViewById(R.id.destinationTextView);
        tvDate = (TextView) findViewById(R.id.dateTextView);

        tvInception.setTextColor(Color.rgb(150, 150, 150));
        tvDestination.setTextColor(Color.rgb(150, 150, 150));
        //creating JSONObject and read data from json
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArrayFrom = obj.getJSONArray("citiesFrom");
            JSONArray m_jArrayTo = obj.getJSONArray("citiesTo");
           // Log.e("Test", m_jArray.getJSONObject(1).getString("countryTitle"));
           //ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i < m_jArrayFrom.length(); i++) {
                JSONObject jo_inside = m_jArrayFrom.getJSONObject(i);
                if(d)
                {
                    Log.d("Details-->", jo_inside.getString("countryTitle"));
                }
                inceptionPlace.add(jo_inside.getString("countryTitle") + ", " + jo_inside.getString("cityTitle"));
                if(d)
                {
                    Log.d("Details-->", jo_inside.getString("cityTitle"));
                }

            }

            for (int i = 0; i < m_jArrayTo.length(); i++) {
                JSONObject jo_inside = m_jArrayTo.getJSONObject(i);
                if(d)
                {
                    Log.d("Details-->", jo_inside.getString("countryTitle"));
                }

                destinationPlace.add(jo_inside.getString("countryTitle") + ", " + jo_inside.getString("cityTitle"));
                if(d)
                {
                    Log.d("Details-->", jo_inside.getString("cityTitle"));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Test", "exception occur");
        }

        adapterInception = new ArrayAdapter(this, android.R.layout.simple_list_item_1, inceptionPlace);
        adapterDestination = new ArrayAdapter(this, android.R.layout.simple_list_item_1, destinationPlace);

        lvList.setVisibility(View.GONE);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(tvPressName == "tvDestination")
                {
                    tvDestination.setText(getResources().getString(R.string.To) + " " + destinationPlace.get((int) id));
                    tvDestination.setTextColor(Color.rgb(0, 0, 0));
                    lvList.setVisibility(View.GONE);
                }
                if(tvPressName == "tvInception")
                {
                    tvInception.setText(getResources().getString(R.string.From)+ " " + inceptionPlace.get((int)id));
                    tvInception.setTextColor(Color.rgb(0, 0, 0));
                    lvList.setVisibility(View.GONE);
                }

            }
        });

    }

    //getting name of json file from assets
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

    public void tvInceptionOnClick(View v)
    {
        if(lvList.getVisibility() == View.GONE)
        {
            tvInception.setText(getResources().getString(R.string.select_incept));
            tvInception.setTextColor(Color.rgb(150, 150, 150));
            lvList.setAdapter(adapterInception);
            lvList.setVisibility(View.VISIBLE);
            tvPressName = "tvInception";
        }

    }

    public void tvDestinationOnClick(View v)
    {
        if(lvList.getVisibility() == View.GONE)
        {
            tvDestination.setText(getResources().getString(R.string.select_dest));
            tvDestination.setTextColor(Color.rgb(150, 150, 150));
            lvList.setAdapter(adapterInception);
            lvList.setVisibility(View.VISIBLE);
            tvPressName = "tvDestination";
        }

    }

    public void tvDateOnClick(View v)
    {

    }
}
