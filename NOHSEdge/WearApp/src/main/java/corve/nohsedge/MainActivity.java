package corve.nohsedge;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends WearableActivity implements DataClient.OnDataChangedListener {
    //private ListView mList;
    private ArrayList<EdgeClass> classes;
    private static final String TAG = "MainActivity";
    //object that contains title, text, time, and date for an edge class

    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(classes);
        editor.putString("edge", json);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mList = findViewById(R.id.listview);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("edge", "");
        Type type = new TypeToken<ArrayList<EdgeClass>>(){}.getType();
        classes = gson.fromJson(json, type);
        if (classes == null){
            classes = new ArrayList<>();
        }
        updateText();
        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "Data changed");
        ArrayList<DataMap> dataMaps = new ArrayList<>();
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                Log.d(TAG, "DataItem uri: " + item.getUri());
                if (item.getUri().getPath().compareTo("/edge") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Log.d(TAG, dataMap.toString());
                    dataMaps = dataMap.getDataMapArrayList("corve.nohsedge.edge");
                    Log.d(TAG, dataMaps.get(0).toString());
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
        updateEdgeClasses(dataMaps);
    }

    private void updateEdgeClasses(ArrayList<DataMap> dataMaps) {
        for (DataMap dataMap : dataMaps) {
            /*if (dataMap.getLong("timestamp") != 0) {
                Log.d(TAG, "timestamp: " + dataMap.getLong("timestamp"));
            } else {*/
                Log.d(TAG, dataMap.getString("title"));
                EdgeClass edgeClass = new EdgeClass(dataMap.getString("title"), dataMap.getString("teacher"), dataMap.getInt("date"), dataMap.getInt("day"), dataMap.getString("time"));
                classes.add(edgeClass);
            //}
        }
        updateText();
    }

    private void updateText() {
        TextView edgeTitle = findViewById(R.id.edgeTitle);
        TextView edgeTeacher = findViewById(R.id.edgeTeacher);
        for (EdgeClass edgeClass : classes){
            int dayOfWeek = 0;
            switch (edgeClass.getDay()){
                case "Monday":
                    dayOfWeek = 2;
                    break;
                case "Tuesday":
                    dayOfWeek = 3;
                    break;
                case "Wednesday":
                    dayOfWeek = 4;
                    break;
                case "Thursday":
                    dayOfWeek = 5;
                    break;
                case "Friday":
                    dayOfWeek = 6;
                    break;
            }
            if (dayOfWeek == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
                edgeTitle.setText(edgeClass.getTitle());
                edgeTeacher.setText(edgeClass.getTeacher());
            }
        }

    }

    /*private void updateList() {
        try {
            Log.d(TAG, "Updating list...");
            String[] edgeTitle = new String[classes.size()];
            String[] edgeTeacher = new String[classes.size()];
            String[] edgeTime = new String[classes.size()];
            String[] edgeDay = new String[classes.size()];
            //edgeclass object actually contains title, teacher, date (as an integer), day, and time
            for (int i = 0; i < classes.size(); i++) {
                EdgeClass edgeClass = classes.get(i);
                edgeTitle[i] = edgeClass.getTitle();
                edgeTeacher[i] = edgeClass.getTeacher();
                edgeTime[i] = edgeClass.getTime();
                edgeDay[i] = edgeClass.getDay();
            }
            Log.d(TAG, "setAdapter triggered");
            mList.setAdapter(new EdgeBaseAdapter(this, edgeTitle, edgeTeacher, edgeTime, edgeDay));
        } catch (NullPointerException e){
            Log.d(TAG, "classes ArrayList is empty!");
        }
    }*/

}

