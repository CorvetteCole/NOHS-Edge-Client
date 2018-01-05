package corve.nohsedge;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import static corve.nohsedge.MainActivity.mEdgeTimeString;

public class EdgeViewActivity extends android.support.v4.app.Fragment {
    @NonNull
    private static String[] EdgeDay = new String[5];
    private ListView mList;
    @NonNull
    public static String[] EdgeTitle = new String[5];
    @NonNull
    public static String[] EdgeText = new String[5];
    @NonNull
    public static String[] EdgeTime = new String[5];
    @NonNull
    public static String[] EdgeDate = new String[5];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View RootView = inflater.inflate(R.layout.activity_edge_view, container, false);
        //setContentView(R.layout.activity_edge_view);
        mList = RootView.findViewById(R.id.listview);
        loadPreferences(getActivity());
        setHasOptionsMenu(true);
        return RootView;
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        finish();
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.clear_all, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage("Clear saved Edge schedule? (This will not make you leave any of your classes)");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Continue",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(PREF_EDGE1, "");
                            editor.putString(PREF_EDGE2, "");
                            editor.putString(PREF_EDGE3, "");
                            editor.putString(PREF_EDGE4, "");
                            editor.putString(PREF_EDGE5, "");
                            editor.putString(PREF_EDGE5Cur, "");
                            editor.commit();
                            dialog.cancel();
                            loadPreferences(getActivity());
                        }
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadPreferences(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        // Get value
        EdgeDay[0] = settings.getString(PREF_EDGE1, DefaultEdgeDay1Value);
        EdgeDay[1] = settings.getString(PREF_EDGE2, DefaultEdgeDay2Value);
        EdgeDay[2] = settings.getString(PREF_EDGE3, DefaultEdgeDay3Value);
        EdgeDay[3] = settings.getString(PREF_EDGE4, DefaultEdgeDay4Value);
        EdgeDay[4] = settings.getString(PREF_EDGE5Cur, DefaultEdgeDay5CurValue);
        String EdgeDay5Next = settings.getString(PREF_EDGE5, DefaultEdgeDay5Value);
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && isAfterEdgeClass()) {
            EdgeDay[4] = EdgeDay5Next;
        }
        int i = 0;
        while (i != 5) {
            EdgeDate[i] = parseEdgeDate(i);
            if (EdgeDay[i] != null) {
                EdgeTitle[i] = parseEdgeTitle(EdgeDay[i]);
                EdgeText[i] = parseEdgeText(EdgeDay[i]);
                EdgeTime[i] = mEdgeTimeString;
            }
            else {
                EdgeTitle[i] = "Not Scheduled";
                EdgeTime[i] = "N/A";
                EdgeText[i] = "Not Scheduled";
            }
            i++;
        }
        setList(context);
    }

    private static boolean isAfterEdgeClass(){
        boolean after = false;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, 1);
        calendar.set(Calendar.MINUTE, 9);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        Log.d("edgeclasstime", (calendar.getTimeInMillis() - System.currentTimeMillis()) + "");
        if (calendar.getTimeInMillis() - System.currentTimeMillis() > 0){
            after = false;
        } else if (calendar.getTimeInMillis() - System.currentTimeMillis() < 0){
            after = true;
        }
        return after;
    }

    @NonNull
    public static String parseEdgeDate(int i){
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
    @NonNull
    public static String parseEdgeTitle(@NonNull String EdgeString) {
        if (EdgeString.toLowerCase().contains("h3")) {
            EdgeString = EdgeString.substring(EdgeString.indexOf(">") + 1);
            EdgeString = EdgeString.substring(0, EdgeString.indexOf("</h3>"));
        }
        else {
            EdgeString = "Not Scheduled";
        }
        return EdgeString;
    }


    @NonNull
    public static String parseEdgeText(@NonNull String EdgeString) {
        if (EdgeString.toLowerCase().contains("g>")) {
            EdgeString = EdgeString.substring(EdgeString.indexOf("g>") + 2);
            EdgeString = EdgeString.substring(0, EdgeString.indexOf("</"));
        } else {
            EdgeString = "Not Scheduled";
        }
        return EdgeString;
    }

    public void setList(Context context) {
        int i = 0;
        while (i <= 4) {
            Log.d("EdgeTitle" + i, EdgeTitle[i]);
            Log.d("EdgeDate" + i, EdgeDate[i]);
            i++;
        }

        mList.setAdapter(new EdgeBaseAdapter(context, EdgeTitle, EdgeText, EdgeTime, EdgeDate));

        }

    }

