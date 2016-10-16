package ru.andrew.test;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimetableActivity extends AppCompatActivity {

    private ExpandableListView elvList;
    private TextView tvInception;
    private TextView tvDestination;
    private TextView tvDate;
    private Button prevButton;
    private Button nextButton;
    private Button confButton;
    private EditText mEditText;

    private static final String SFSTR = "Select from:";
    private static final String STSTR = "Select to:";
    private static final String NAME = "name";
    private String locTo;
    private String locFrom;
    private String dateS;

    List<Map<String, String>> groupDataF;
    List<Map<String, String>> groupDataT;
    List<Map<String, String>> groupData;
    List<Map<String, String>> childDataItem;
    List<List<Map<String, String>>> childDataF;
    List<List<Map<String, String>>> childDataT;
    Map<String, String> m;
    Map<String, String> mCh;


    private SimpleExpandableListAdapter adapterDestination;
    private SimpleExpandableListAdapter adapterInception;
    private String tvPressName;
    private  static final boolean d = false;
    private Calendar date = Calendar.getInstance();
    private Context context;
    int childPositionInc;
    int childPositionDest;
    int groupPositionInc;
    int groupPositionDest;
    int iter ;
    boolean destRed = false;
    boolean incRed = false;
    boolean dateRed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        //ExpandableListView for list of stations
        elvList = (ExpandableListView) findViewById(R.id.elvStations);

        //collections for list of stations
        groupDataF = new ArrayList<Map<String, String>>();
        childDataF = new ArrayList<List<Map<String, String>>>();
        groupDataT = new ArrayList<Map<String, String>>();
        childDataT = new ArrayList<List<Map<String, String>>>();

        //register context menu for child of ExpandableListView for detail station information
        registerForContextMenu(elvList);

        tvInception = (TextView) findViewById(R.id.inceptionTextView);
        tvDestination = (TextView) findViewById(R.id.destinationTextView);
        tvDate = (TextView) findViewById(R.id.dateTextView);
        context = this;

        //set gray color for not selected items
        tvInception.setTextColor(Color.rgb(150, 150, 150));
        tvDestination.setTextColor(Color.rgb(150, 150, 150));
        tvDate.setTextColor(Color.rgb(150, 150, 150));

        prevButton = (Button) findViewById(R.id.prevButton);
        nextButton = (Button) findViewById(R.id.nextButton);
        confButton = (Button) findViewById(R.id.confirmButton);
        mEditText = (EditText) findViewById(R.id.editText);

        prevButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        confButton.setVisibility(View.GONE);
        mEditText.setVisibility(View.GONE);

        //this int for search procedure
        iter = 0;

        //creating JSONObject and read data from json
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArrayCitiesFrom = obj.getJSONArray("citiesFrom");
            JSONArray m_jArrayCitiesTo = obj.getJSONArray("citiesTo");

            for (int i = 0; i < m_jArrayCitiesFrom.length(); i++) {
                JSONObject jo_City = m_jArrayCitiesFrom.getJSONObject(i);
                m = new HashMap<String, String>();
                groupDataF.add(m);
                m.put(NAME, jo_City.getString("countryTitle") + ", " + jo_City.getString("cityTitle"));


                JSONArray jo_Stations = jo_City.getJSONArray("stations");
                childDataItem = new ArrayList<Map<String, String>>();
                for (int j = 0; j < jo_Stations.length(); j++) {
                    JSONObject jo_stationsAtr = jo_Stations.getJSONObject(j);

                    mCh = new HashMap<String, String>();
                    childDataItem.add(mCh);
                    mCh.put(SFSTR, jo_stationsAtr.getString("stationTitle"));
                }
                childDataF.add(childDataItem);
            }

            //make ExpandableListAdapter adapter for "from" list of station
            adapterInception = new SimpleExpandableListAdapter(
                    this, groupDataF,
                    android.R.layout.simple_expandable_list_item_1,
                    new String[] { NAME }, new int[] { android.R.id.text1 },
                    childDataF, android.R.layout.simple_expandable_list_item_2,
                    new String[] { SFSTR }, new int[] { android.R.id.text1 });

            for (int i = 0; i < m_jArrayCitiesTo.length(); i++) {
                JSONObject jo_City = m_jArrayCitiesTo.getJSONObject(i);
                m = new HashMap<String, String>();
                groupDataT.add(m);
                m.put(NAME, jo_City.getString("countryTitle") + ", " + jo_City.getString("cityTitle"));


                JSONArray jo_Stations = jo_City.getJSONArray("stations");
                childDataItem = new ArrayList<Map<String, String>>();
                for (int j = 0; j < jo_Stations.length(); j++) {
                    JSONObject jo_stationsAtr = jo_Stations.getJSONObject(j);

                    mCh = new HashMap<String, String>();
                    childDataItem.add(mCh);
                    mCh.put(STSTR, jo_stationsAtr.getString("stationTitle"));

                }
                childDataT.add(childDataItem);
            }

            //make ExpandableListAdapter adapter for "to" list of station
            adapterDestination = new SimpleExpandableListAdapter(
                    this, groupDataT,
                    android.R.layout.simple_expandable_list_item_1,
                    new String[] { NAME }, new int[] { android.R.id.text1 },
                    childDataT, android.R.layout.simple_expandable_list_item_2,
                    new String[] {STSTR }, new int[] { android.R.id.text1 });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Test", "exception occur");
        }

        elvList.setVisibility(View.GONE);

        elvList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if(tvPressName == "tvDestination")
                {
                    //get station "to"
                    locTo = groupDataT.get(groupPosition).toString().substring(NAME.length() + 2,
                            groupDataT.get(groupPosition).toString().length()-1) + ", " +
                            childDataT.get(groupPosition).get(childPosition).toString().substring(STSTR.length()+2,
                                    childDataT.get(groupPosition).get(childPosition).toString().length() - 1);
                    tvDestination.setText(getResources().getString(R.string.To) + " " + locTo);
                    tvDestination.setTextColor(Color.rgb(0, 0, 0));
                    if(incRed && dateRed)
                    {
                        confButton.setVisibility(View.VISIBLE);
                    }
                    elvList.setVisibility(View.GONE);
                    prevButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.GONE);
                    mEditText.setVisibility(View.GONE);
                    childPositionInc = childPosition;
                    groupPositionInc = groupPosition;
                    destRed = true;
                    return true;
                }
                if(tvPressName == "tvInception")
                {
                    //get station "from"
                    locFrom = groupDataF.get(groupPosition).toString().substring(NAME.length()+2,
                            groupDataF.get(groupPosition).toString().length()-1) + ", " +
                            childDataF.get(groupPosition).get(childPosition).toString().substring(SFSTR.length()+2,
                                    childDataF.get(groupPosition).get(childPosition).toString().length()-1);
                    tvInception.setText(getResources().getString(R.string.From) + " " + locFrom);
                    tvInception.setTextColor(Color.rgb(0, 0, 0));
                    if(destRed && dateRed)
                    {
                        confButton.setVisibility(View.VISIBLE);
                    }
                    elvList.setVisibility(View.GONE);
                    prevButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.GONE);
                    mEditText.setVisibility(View.GONE);
                    childPositionInc = childPosition;
                    groupPositionInc = groupPosition;
                    incRed = true;
                    return true;
                }
                return false;
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

    //fill context menu of expanded list view with detailed station information from JSON
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
        int type =  ExpandableListView.getPackedPositionType(info.packedPosition);
        int group =  ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int child =   ExpandableListView.getPackedPositionChild(info.packedPosition);
        //Only create a context menu for child items
        if (type == 1) {
            try {
                JSONObject obj = new JSONObject(loadJSONFromAsset());
                if(tvPressName == "tvInception")
                {
                    JSONArray m_jArrayCities = obj.getJSONArray("citiesFrom");
                    JSONObject jo_City = m_jArrayCities.getJSONObject(group);
                    JSONArray jo_Stations = jo_City.getJSONArray("stations");
                    JSONObject jo_stationsAtr = jo_Stations.getJSONObject(child);
                    //Array created earlier when we built the expandable list
                    String page ="Detail"; /*mListStringArray[group][child];*/
                    menu.setHeaderTitle(page);
                    menu.add(0, 0, 0, "Contry: " + jo_stationsAtr.getString("countryTitle"));
                    menu.add(0,0,0, "District: " + jo_stationsAtr.getString("districtTitle"));
                    menu.add(0,0,0,"City: " +  jo_stationsAtr.getString("cityTitle"));
                    menu.add(0,0, 0,"Region: " +  jo_stationsAtr.getString("regionTitle"));
                    menu.add(0,0, 0,"Id: " +  jo_stationsAtr.getString("regionTitle"));
                }
                if(tvPressName == "tvDestination")
                {
                    JSONArray m_jArrayCities = obj.getJSONArray("citiesTo");
                    JSONObject jo_City = m_jArrayCities.getJSONObject(group);
                    JSONArray jo_Stations = jo_City.getJSONArray("stations");
                    JSONObject jo_stationsAtr = jo_Stations.getJSONObject(child);
                    //Array created earlier when we built the expandable list
                    String page ="Detail"; /*mListStringArray[group][child];*/
                    menu.setHeaderTitle(page);
                    menu.add(0, 0, 0, "Contry: " + jo_stationsAtr.getString("countryTitle"));
                    menu.add(0, 0, 0, "District: " + jo_stationsAtr.getString("districtTitle"));
                    menu.add(0,0,0, "City: " + jo_stationsAtr.getString("cityTitle"));
                    menu.add(0,0, 0, "Region: " + jo_stationsAtr.getString("regionTitle"));
                    menu.add(0,0, 0, "Id: " + jo_stationsAtr.getString("stationId"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Test", "exception occur");
            }
        }
    }

    //handler for textView for "from" data
    public void tvInceptionOnClick(View v)
    {
        if(elvList.getVisibility() == View.GONE)
        {
            tvInception.setText(getResources().getString(R.string.select_incept));
            tvInception.setTextColor(Color.rgb(150, 150, 150));
            elvList.setAdapter(adapterInception);
            elvList.setVisibility(View.VISIBLE);
            prevButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            mEditText.setVisibility(View.VISIBLE);
            confButton.setVisibility(View.GONE);
            tvPressName = "tvInception";
            groupData = groupDataF;
        }
    }

    //handler for textView for "to" data
    public void tvDestinationOnClick(View v)
    {
        if(elvList.getVisibility() == View.GONE)
        {
            tvDestination.setText(getResources().getString(R.string.select_dest));
            tvDestination.setTextColor(Color.rgb(150, 150, 150));
            elvList.setAdapter(adapterDestination);
            elvList.setVisibility(View.VISIBLE);
            prevButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            mEditText.setVisibility(View.VISIBLE);
            confButton.setVisibility(View.GONE);
            tvPressName = "tvDestination";
            groupData = groupDataT;
        }

    }

    //handler for search previous button
    public void PrevOnClick(View v)
    {
        String searchStr = mEditText.getText().toString();
        searchStr = searchStr.toLowerCase();
        Log.d("Search","" + groupData.size() );
        if(!searchStr.equals(""))
        {
            do{
                iter--;
                Log.d("Search","iter = " + iter);
            }while ( iter >=0 && !groupData.get(iter).toString().toLowerCase().contains(searchStr) && iter < groupData.size());
            if(iter < 0)
            {
                iter = -1;
            }
            else
            {
                elvList.setSelectedGroup(iter);
            }
        }
        else
        {
            Log.d("Search","Empty Search");
        }
    }

    //handler for search next button
    public void NextOnClick(View v)
    {
        String searchStr = mEditText.getText().toString();

        searchStr = searchStr.toLowerCase();
        Log.d("Search","" +groupData.size() );
        if(!searchStr.equals(""))
        {
            do{
                iter++;
                Log.d("Search","iter = " + iter);
            }while (  iter >=0 && iter < groupData.size() && !groupData.get(iter).toString().toLowerCase().contains(searchStr) );
            if(iter == groupData.size())
            {
                iter = groupData.size();
            }
            else
            {
                elvList.setSelectedGroup(iter);
            }
        }
        else
        {
            Log.d("Search","Empty Search");
        }
    }

    //handler for textView for date data
    public void tvDateOnClick(View v)
    {
        tvDate.setTextColor(Color.rgb(150, 150, 150));
        confButton.setVisibility(View.GONE);
        SelectDate(tvDate);
    }

    //handler for button for confirm data and send data to main activity
    public void buttonConfirmOnClick(View v)
    {
        Intent intent = new Intent();
        intent.putExtra("from", locFrom);
        intent.putExtra("to", locTo);
        intent.putExtra("date", dateS);
        intent.putExtra("gpd", groupPositionDest);
        intent.putExtra("gpi", groupPositionInc);
        setResult(RESULT_OK, intent);
        finish();
    }

    //dialog for date piking
    public void SelectDate(View v) {
        new DatePickerDialog(TimetableActivity.this, dpdodl,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener dpdodl = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateS = DateUtils.formatDateTime(context,
                    date.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
            tvDate.setText(dateS);
            tvDate.setTextColor(Color.rgb(0, 0, 0));
            if(destRed && incRed)
            {
                confButton.setVisibility(View.VISIBLE);
            }
            dateRed = true;
        }
    };

    //function to save activity data during destroying activity at change orientation
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("groupPositionDest", groupPositionDest);
        outState.putInt("groupPositionInc", groupPositionInc);
        outState.putInt("childPositionDest", childPositionDest);
        outState.putInt("childPositionInc", childPositionInc);
        outState.putInt("dayOfMonth", date.get(Calendar.DAY_OF_MONTH));
        outState.putInt("monthOfYear",  date.get(Calendar.MONTH));
        outState.putInt("year",  date.get(Calendar.YEAR));
        outState.putBoolean("destRed", destRed);
        outState.putBoolean("incRed", incRed);
        outState.putBoolean("dateRed", dateRed);
        // if(d)
        //  {
        Log.d("Test", "onSaveInstanceState");
        //  }

    }

    //function to restore activity data during destroying activity at change orientation
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        groupPositionDest = savedInstanceState.getInt("groupPositionDest");
        groupPositionInc = savedInstanceState.getInt("groupPositionInc");
        childPositionDest = savedInstanceState.getInt("childPositionDest");
        childPositionInc = savedInstanceState.getInt("childPositionInc");
        incRed = savedInstanceState.getBoolean("incRed");
        destRed = savedInstanceState.getBoolean("destRed");
        dateRed = savedInstanceState.getBoolean("dateRed");

        if(incRed )
        {
            tvInception.setText( getResources().getString(R.string.From) + " " + adapterInception.getChild(groupPositionInc, childPositionInc).toString());
            tvInception.setTextColor(Color.rgb(0, 0, 0));
        }

        if(destRed)
        {
            tvDestination.setText( getResources().getString(R.string.To) + " " + adapterDestination.getChild(groupPositionDest, childPositionDest).toString());
            tvDestination.setTextColor(Color.rgb(0, 0, 0));
        }

        if(dateRed)
        {
            date.set(Calendar.YEAR, savedInstanceState.getInt("year"));
            date.set(Calendar.MONTH, savedInstanceState.getInt("monthOfYear"));
            date.set(Calendar.DAY_OF_MONTH, savedInstanceState.getInt("dayOfMonth"));
            tvDate.setText(DateUtils.formatDateTime(context,
                    date.getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
            tvDate.setTextColor(Color.rgb(0, 0, 0));
        }
        //  if(d)
        //  {
        Log.d("Test", "onRestoreInstanceState");
        //  }

    }
}
