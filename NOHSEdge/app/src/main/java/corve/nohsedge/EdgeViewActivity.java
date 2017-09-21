package corve.nohsedge;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

import static corve.nohsedge.MainActivity.DefaultEdgeDay1Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay2Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay3Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay4Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay5Value;
import static corve.nohsedge.MainActivity.DefaultMinValue;
import static corve.nohsedge.MainActivity.PREF_EDGE1;
import static corve.nohsedge.MainActivity.PREF_EDGE2;
import static corve.nohsedge.MainActivity.PREF_EDGE3;
import static corve.nohsedge.MainActivity.PREF_EDGE4;
import static corve.nohsedge.MainActivity.PREF_EDGE5;
import static corve.nohsedge.MainActivity.PREF_MIN;

public class EdgeViewActivity extends AppCompatActivity {
private int notifyMinutes;
    private String EdgeDay1;
    private String EdgeDay2;
    private String EdgeDay3;
    private String EdgeDay4;
    private String EdgeDay5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edge_view);
    }
    public void loadPreferences() {
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME,
                MainActivity.MODE_PRIVATE);

        // Get value
        String EdgeDay1Value = settings.getString(PREF_EDGE1, DefaultEdgeDay1Value);
        String EdgeDay2Value = settings.getString(PREF_EDGE2, DefaultEdgeDay2Value);
        String EdgeDay3Value = settings.getString(PREF_EDGE3, DefaultEdgeDay3Value);
        String EdgeDay4Value = settings.getString(PREF_EDGE4, DefaultEdgeDay4Value);
        String EdgeDay5Value = settings.getString(PREF_EDGE5, DefaultEdgeDay5Value);
        notifyMinutes = settings.getInt(PREF_MIN, DefaultMinValue);

        InterpretEdgeData(EdgeDay1Value);
        InterpretEdgeData(EdgeDay2Value);
        InterpretEdgeData(EdgeDay3Value);
        InterpretEdgeData(EdgeDay4Value);
        InterpretEdgeData(EdgeDay5Value);
    }


    public void InterpretEdgeData(String consoleMessage){
        if (consoleMessage.toLowerCase().contains("Mon".toLowerCase())){
            EdgeDay1 = consoleMessage;
            Log.d("Monday Edge Class", EdgeDay1);
        }
        if (consoleMessage.toLowerCase().contains("Tue".toLowerCase())){
            EdgeDay2 = consoleMessage;
            Log.d("Tuesday Edge Class", EdgeDay2);
        }
        if (consoleMessage.toLowerCase().contains("Wed".toLowerCase())){
            EdgeDay3 = consoleMessage;
            Log.d("Wednesday Edge Class", EdgeDay3);
        }
        if (consoleMessage.toLowerCase().contains("Thu".toLowerCase())){
            EdgeDay4 = consoleMessage;   //Thursday
            Log.d("Thursday Edge Class", EdgeDay4);
        }
        if (consoleMessage.toLowerCase().contains("Fri".toLowerCase())){
            EdgeDay5 = consoleMessage;
            Log.d("Friday Edge Class", EdgeDay5);
        }
    }


    public String parseEdgeTitle(String EdgeString){
        EdgeString = EdgeString.substring(EdgeString.indexOf(">") + 1);
        EdgeString = EdgeString.substring(0, EdgeString.indexOf("</h3>"));
        return EdgeString;
    }

    public String parseEdgeTime(String EdgeString){
        String session = "";
        if (EdgeString.toLowerCase().contains("12:43")){
            session = "12:43";
        }
        if (EdgeString.toLowerCase().contains("1:09")){
            session = "1:09";
        }
        return session;
    }
    public String parseEdgeText(String EdgeString){
        EdgeString = EdgeString.substring(EdgeString.indexOf("g>") + 2);
        EdgeString = EdgeString.substring(0, EdgeString.indexOf("</"));
        return EdgeString;
    }

}
