package corve.nohsedge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.Calendar;

import static corve.nohsedge.MainActivity.DefaultEdgeDay1Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay2Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay3Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay4Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay5CurValue;
import static corve.nohsedge.MainActivity.DefaultEdgeDay5Value;
import static corve.nohsedge.MainActivity.PREF_EDGE1;
import static corve.nohsedge.MainActivity.PREF_EDGE2;
import static corve.nohsedge.MainActivity.PREF_EDGE3;
import static corve.nohsedge.MainActivity.PREF_EDGE4;
import static corve.nohsedge.MainActivity.PREF_EDGE5;
import static corve.nohsedge.MainActivity.PREF_EDGE5Cur;

public class EdgeViewActivity extends AppCompatActivity {
    private String[] EdgeDay = new String[5];
    private ListView mList;
    public static String[] EdgeTitle = new String[5];
    public static String[] EdgeText = new String[5];
    public static String[] EdgeTime = new String[5];
    public static String[] EdgeDate = new String[5];
    private String EdgeDay5Cur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edge_view);
        mList = (ListView) findViewById(R.id.listview);
        loadPreferences();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            EdgeSignupActivity.showPage = false;
            Intent intent = new Intent(getBaseContext(), EdgeSignupActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void loadPreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // Get value
        EdgeDay[0] = settings.getString(PREF_EDGE1, DefaultEdgeDay1Value);
        EdgeDay[1] = settings.getString(PREF_EDGE2, DefaultEdgeDay2Value);
        EdgeDay[2] = settings.getString(PREF_EDGE3, DefaultEdgeDay3Value);
        EdgeDay[3] = settings.getString(PREF_EDGE4, DefaultEdgeDay4Value);
        EdgeDay[4] = settings.getString(PREF_EDGE5, DefaultEdgeDay5Value);
        EdgeDay5Cur = settings.getString(PREF_EDGE5Cur, DefaultEdgeDay5CurValue);
        int i = 0;
        while (i <= 4) {
            InterpretEdgeData(EdgeDay[i]);
            i++;
        }
    }


    public void InterpretEdgeData(String consoleMessage) {
        if (consoleMessage.toLowerCase().contains("Mon".toLowerCase())) {
            EdgeDay[0] = consoleMessage;
            Log.d("Monday Edge Class", EdgeDay[0]);
        }
        if (consoleMessage.toLowerCase().contains("Tue".toLowerCase())) {
            EdgeDay[1] = consoleMessage;
            EdgeDate[1] = "Tuesday";
            Log.d("Tuesday Edge Class", EdgeDay[1]);
        }
        if (consoleMessage.toLowerCase().contains("Wed".toLowerCase())) {
            EdgeDay[2] = consoleMessage;
            EdgeDate[2] = "Wednesday";
            Log.d("Wednesday Edge Class", EdgeDay[2]);
        }
        if (consoleMessage.toLowerCase().contains("Thu".toLowerCase())) {
            EdgeDay[3] = consoleMessage;   //Thursday
            EdgeDate[3] = "Thursday";
            Log.d("Thursday Edge Class", EdgeDay[3]);
        }
        if (consoleMessage.toLowerCase().contains("Fri".toLowerCase())) {
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                EdgeDay[4] = consoleMessage;
            }
            //Log.d("Friday Edge Class", EdgeDay5);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
                //Log.d("!test1!", EdgeDay5Cur);
                EdgeDay[4] = EdgeDay5Cur;
            }
        }
        int i = 0;
        while (i != 5) {
            EdgeDate[i] = parseEdgeDate(i);
            if (EdgeDay[i] != null) {
                EdgeTitle[i] = parseEdgeTitle(EdgeDay[i]);
                EdgeText[i] = parseEdgeText(EdgeDay[i]);
                EdgeTime[i] = parseEdgeTime(EdgeDay[i]);
            }
            else {
                EdgeTitle[i] = "Not Scheduled";
                EdgeTime[i] = "N/A";
                EdgeText[i] = "Not Scheduled";
            }
            i++;
        }
        setList();
    }

    public String parseEdgeDate (int i){
        String date = "N/A";
        switch(i){
            case 0:
               date = "Monday";
                break;
            case 1:
                date = "Tuesday";
                break;
            case 2:
                date = "Wednesday";
                break;
            case 3:
                date = "Thursday";
                break;
            case 4:
                date = "Friday";
                break;
        }
        return date;
    }
    public String parseEdgeTitle(String EdgeString) {
        if (EdgeString.toLowerCase().contains("h3")) {
            EdgeString = EdgeString.substring(EdgeString.indexOf(">") + 1);
            EdgeString = EdgeString.substring(0, EdgeString.indexOf("</h3>"));
        }
        else {
            EdgeString = "Not Scheduled";
        }
        return EdgeString;
    }

    public String parseEdgeTime(String EdgeString) {
        String session = "";
        if (EdgeString.toLowerCase().contains("12:43")) {
            session = "12:43";
        }
        if (EdgeString.toLowerCase().contains("1:09")) {
            session = "1:09";
        }
        return session;
    }

    public String parseEdgeText(String EdgeString) {
        if (EdgeString.toLowerCase().contains("g>")) {
            EdgeString = EdgeString.substring(EdgeString.indexOf("g>") + 2);
            EdgeString = EdgeString.substring(0, EdgeString.indexOf("</"));
        } else {
            EdgeString = "Not Scheduled";
        }
        return EdgeString;
    }

    public void setList() {
        int i = 0;
        while (i <= 4) {
            Log.d("EdgeTitle" + i, EdgeTitle[i]);
            Log.d("EdgeDate" + i, EdgeDate[i]);
            i++;
        }

        mList.setAdapter(new EdgeBaseAdapter(this, EdgeTitle, EdgeText, EdgeTime, EdgeDate));

        }

    }

