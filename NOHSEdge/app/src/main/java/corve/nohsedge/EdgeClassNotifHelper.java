package corve.nohsedge;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static corve.nohsedge.MainActivity.DefaultEdgeDay1Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay2Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay3Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay4Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay5CurValue;
import static corve.nohsedge.MainActivity.DefaultEdgeDay5Value;
import static corve.nohsedge.MainActivity.DefaultMinValue;
import static corve.nohsedge.MainActivity.DefaultUnameValue;
import static corve.nohsedge.MainActivity.PREF_EDGE1;
import static corve.nohsedge.MainActivity.PREF_EDGE2;
import static corve.nohsedge.MainActivity.PREF_EDGE3;
import static corve.nohsedge.MainActivity.PREF_EDGE4;
import static corve.nohsedge.MainActivity.PREF_EDGE5;
import static corve.nohsedge.MainActivity.PREF_EDGE5Cur;
import static corve.nohsedge.MainActivity.PREF_MIN;
import static corve.nohsedge.MainActivity.PREF_NOTIFYEDGE;
import static corve.nohsedge.MainActivity.PREF_UNAME;
import static corve.nohsedge.MainActivity.mEdgeTimeString;
import static corve.nohsedge.MainActivity.unameValue;

/**
 * Created by Cole on 9/18/2017.
 */


public class EdgeClassNotifHelper extends BroadcastReceiver {
    private static final String TAG = "EdgeClassNotifHelper";
    private int notifyMinutes;
    private Context context1;
    private Boolean NotificationEnabled;
    private String[] mDay = new String[7];

    @Override
    public void onReceive(Context context, Intent intent) {
        context1 = context;
        mDay[2] = "Mon";
        mDay[3] = "Tue";
        mDay[4] = "Wed";
        mDay[5] = "Thu";
        mDay[6] = "Fri";
        loadPreferences();
    }

    public void loadPreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context1);
        // Get value
        String mEdgeDay[] = new String[7];
        mEdgeDay[2] = settings.getString(PREF_EDGE1, DefaultEdgeDay1Value);
        mEdgeDay[3] = settings.getString(PREF_EDGE2, DefaultEdgeDay2Value);
        mEdgeDay[4] = settings.getString(PREF_EDGE3, DefaultEdgeDay3Value);
        mEdgeDay[5] = settings.getString(PREF_EDGE4, DefaultEdgeDay4Value);
        mEdgeDay[6] = settings.getString(PREF_EDGE5, DefaultEdgeDay5Value);
        String edgeDay5Cur = settings.getString(PREF_EDGE5Cur, DefaultEdgeDay5CurValue);
        notifyMinutes = settings.getInt(PREF_MIN, DefaultMinValue);
        unameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        NotificationEnabled = settings.getBoolean(PREF_NOTIFYEDGE, true);
        saveEdgeToFirebase(mEdgeDay, edgeDay5Cur, unameValue.toLowerCase());
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek != Calendar.FRIDAY && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY){
            if (mEdgeDay[dayOfWeek].toLowerCase().contains(mDay[dayOfWeek].toLowerCase())) {
                setEdgeNotifications(parseEdgeTitle(mEdgeDay[dayOfWeek]), parseEdgeText(mEdgeDay[dayOfWeek]));
            }
        } else if (dayOfWeek == Calendar.FRIDAY) {
            setEdgeNotifications(parseEdgeTitle(edgeDay5Cur), parseEdgeText(edgeDay5Cur));
        }
    }




    public void setEdgeNotifications(String EdgeTitle, String EdgeText) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, 1);
        calendar.set(Calendar.MINUTE, 9);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.setTimeInMillis(calendar.getTimeInMillis() - (notifyMinutes * 60000));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context1);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("TITLE", EdgeTitle);
        editor.putString("TEXT", EdgeText);
        editor.apply();
        Log.d("Notification set", EdgeTitle);
        Log.d("!helper!", "notification set");
        Log.d("helperedgeclasstime", (calendar.getTimeInMillis() - System.currentTimeMillis()) + "");
        if (calendar.getTimeInMillis() - System.currentTimeMillis() > 0 && NotificationEnabled) {
            Intent intent1 = new Intent(context1, EdgeReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context1,
                    MainActivity.REQUEST_CODE_EDGE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context1.getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    public String parseEdgeTitle(String EdgeString){
        try {
            EdgeString = EdgeString.substring(EdgeString.indexOf(">") + 1);
            EdgeString = EdgeString.substring(0, EdgeString.indexOf("</h3>"));
        }
        catch (StringIndexOutOfBoundsException e){
            EdgeString = "Undefined";
        }
        return EdgeString;
    }

    public String parseEdgeText(String EdgeString){
        try {
            EdgeString = EdgeString.substring(EdgeString.indexOf("g>") + 2);
            EdgeString = EdgeString.substring(0, EdgeString.indexOf("</"));
        }
        catch (StringIndexOutOfBoundsException e){
            EdgeString = "Undefined";
        }
        return EdgeString;
    }

    private void saveEdgeToFirebase(String[] edge, String friEdge, String userName){
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (userName.contains(".")) {
            userName = userName.replaceAll("\\.", "-");
        }
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        ArrayList<EdgeClass> classes = new ArrayList<>();
        for (int i = 2; i < 6; i++){
            classes.add(new EdgeClass(parseEdgeTitle(edge[i]), parseEdgeText(edge[i]), parseEdgeDate(edge[i]), parseEdgeDay(i), mEdgeTimeString));
        }
        classes.add(new EdgeClass(parseEdgeTitle(friEdge), parseEdgeText(friEdge), parseEdgeDate(friEdge), parseEdgeDay(6), mEdgeTimeString));
        mDatabase.child("users").child(userName).child("Edge").setValue(classes);
        if (MainActivity.wearInUse){
            syncToWear(classes);
        }
    }

    private void syncToWear(ArrayList<EdgeClass> classes){
        DataClient mDataClient = Wearable.getDataClient(context1);
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/edge");
        ArrayList<DataMap> dataMaps = new ArrayList<>();
        for (EdgeClass edgeClass : classes) {
            DataMap dataMap = new DataMap();
            dataMap.putString("title", edgeClass.getTitle());
            dataMap.putString("teacher", edgeClass.getTeacher());
            dataMap.putInt("date", edgeClass.getDate());
            dataMap.putString("day", edgeClass.getDay());
            dataMap.putString("time", edgeClass.getTime());
            dataMaps.add(dataMap);
        }
        putDataMapReq.getDataMap().putDataMapArrayList("corve.nohsedge.edge", dataMaps);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        putDataReq.setUrgent();
        Task<DataItem> putDataTask = mDataClient.putDataItem(putDataReq);
        if (putDataTask.isSuccessful()){
            Log.d(TAG, "Classes synced to wearable");
        }
    }

    private static String parseEdgeDay(int i){
        String date = "N/A";
        switch(i){
            case 2:
                date = "Monday";
                break;
            case 3:
                date = "Tuesday";
                break;
            case 4:
                date = "Wednesday";
                break;
            case 5:
                date = "Thursday";
                break;
            case 6:
                date = "Friday";
                break;
        }
        return date;
    }

    private static int parseEdgeDate(String edgeDay){
        try {
            String date = edgeDay.substring(edgeDay.indexOf("\"datetime\">") + 16);
            date = date.substring(date.indexOf("/") + 1);
            if (edgeDay.contains("12:")) {
                date = date.substring(0, (date.indexOf("pm") - 6));
            } else {
                date = date.substring(0, (date.indexOf("pm") - 5));
            }
            return Integer.parseInt(date);
        } catch (Exception e){
            Log.e(TAG, "Error in parseEdgeDate: " + e);
            return 0;
        }
    }


}
