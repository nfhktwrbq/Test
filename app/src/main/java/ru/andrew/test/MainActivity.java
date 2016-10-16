package ru.andrew.test;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_TIMETABLE = 1;
    private TextView tvFrom;
    private TextView tvTo;
    private TextView tvDate;
    private String FromS;
    private String ToS;
    private String DateS;
    private int gpd;
    private int gpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Test", "onCreate");

        tvFrom = (TextView) findViewById(R.id.textView3);
        tvTo = (TextView) findViewById(R.id.textView2);
        tvDate = (TextView) findViewById(R.id.textView);
    }

    //menu with timetable and about
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent TimetableIntent;
        switch (item.getItemId())
        {
            //for timetable - start another activity
            case R.id.action_timetable:
            TimetableIntent = new Intent(this, TimetableActivity.class) ;
                startActivityForResult(TimetableIntent, REQUEST_TIMETABLE);
                return true;

            case R.id.action_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getResources().getString(R.string.action_about))
                        .setMessage(getResources().getString(R.string.message_about))
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
        }
        return false;
    }

    //get results data from timetable activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
        {
            if(requestCode == REQUEST_TIMETABLE)
            {
                FromS = data.getStringExtra("from");
                ToS = data.getStringExtra("to");
                DateS = data.getStringExtra("date");
                gpd = data.getIntExtra("gpd", 0);
                gpi = data.getIntExtra("gpi", 0);
                tvFrom.setText("From: " + FromS);
                tvTo.setText("To: " + ToS);
                tvDate.setText(DateS);
            }
        }

    }

    //function to save activity data during destroying activity at change orientation
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("groupPositionDest", gpd);
        outState.putInt("groupPositionInc", gpi);
        outState.putString("from", FromS);
        outState.putString("to", ToS);
        outState.putString("date", DateS);

    }

    //function to restore activity data during destroying activity at change orientation
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gpd = savedInstanceState.getInt("groupPositionDest");
        gpi = savedInstanceState.getInt("groupPositionInc");
        FromS = savedInstanceState.getString("from");
        ToS = savedInstanceState.getString("to");
        DateS = savedInstanceState.getString("date");
        tvFrom.setText("From: " + FromS);
        tvTo.setText("To: " + ToS);
        tvDate.setText(DateS);
    }
}
