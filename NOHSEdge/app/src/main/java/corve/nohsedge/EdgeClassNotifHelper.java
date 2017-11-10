package corve.nohsedge;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static corve.nohsedge.MainActivity.DefaultEdgeDay1Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay2Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay3Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay4Value;
import static corve.nohsedge.MainActivity.DefaultEdgeDay5CurValue;
import static corve.nohsedge.MainActivity.DefaultEdgeDay5Value;
import static corve.nohsedge.MainActivity.DefaultMinValue;
import static corve.nohsedge.MainActivity.EdgeDay5Cur;
import static corve.nohsedge.MainActivity.PREF_EDGE1;
import static corve.nohsedge.MainActivity.PREF_EDGE2;
import static corve.nohsedge.MainActivity.PREF_EDGE3;
import static corve.nohsedge.MainActivity.PREF_EDGE4;
import static corve.nohsedge.MainActivity.PREF_EDGE5;
import static corve.nohsedge.MainActivity.PREF_EDGE5Cur;
import static corve.nohsedge.MainActivity.PREF_MIN;
import static corve.nohsedge.MainActivity.PREF_NOTIFYEDGE;

/**
 * Created by Cole on 9/18/2017.
 */


public class EdgeClassNotifHelper extends BroadcastReceiver {
    private int notifyMinutes;
    private Context context1;
    private String EdgeDay5Cur;
    private Boolean NotificationEnabled;
    @Override
    public void onReceive(Context context, Intent intent) {
        context1 = context;
        setNewEdgeNotif();


    }
    public void setNewEdgeNotif(){
        Log.d("!edgehelper worked", "im ok with life");
        loadPreferences();

    }
    public void loadPreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context1);

        // Get value
        String EdgeDay1 = settings.getString(PREF_EDGE1, DefaultEdgeDay1Value);
        String EdgeDay2 = settings.getString(PREF_EDGE2, DefaultEdgeDay2Value);
        String EdgeDay3 = settings.getString(PREF_EDGE3, DefaultEdgeDay3Value);
        String EdgeDay4 = settings.getString(PREF_EDGE4, DefaultEdgeDay4Value);
        String EdgeDay5 = settings.getString(PREF_EDGE5, DefaultEdgeDay5Value);
        EdgeDay5Cur = settings.getString(PREF_EDGE5Cur, DefaultEdgeDay5CurValue);
        notifyMinutes = settings.getInt(PREF_MIN, DefaultMinValue);
        NotificationEnabled = settings.getBoolean(PREF_NOTIFYEDGE, true);

        //Calendar.Friday equals 6, thursday equals 5, use this in the future with the edgeday arrays
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                setEdgeNotifications(parseEdgeTitle(EdgeDay1), parseEdgeText(EdgeDay1), parseEdgeSession(EdgeDay1), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                break;
            case Calendar.TUESDAY:
                setEdgeNotifications(parseEdgeTitle(EdgeDay2), parseEdgeText(EdgeDay2), parseEdgeSession(EdgeDay2), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                break;
            case Calendar.WEDNESDAY:
                setEdgeNotifications(parseEdgeTitle(EdgeDay3), parseEdgeText(EdgeDay3), parseEdgeSession(EdgeDay3), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                break;
            case Calendar.THURSDAY:
                setEdgeNotifications(parseEdgeTitle(EdgeDay4), parseEdgeText(EdgeDay4), parseEdgeSession(EdgeDay4), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                break;
            case Calendar.FRIDAY:
                setEdgeNotifications(parseEdgeTitle(EdgeDay5Cur), parseEdgeText(EdgeDay5Cur), parseEdgeSession(EdgeDay5Cur), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                break;
            case Calendar.SATURDAY:
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PREF_EDGE5Cur, EdgeDay5);
                editor.apply();
        }
    }




    public void setEdgeNotifications(String EdgeTitle, String EdgeText, int EdgeSession, int DayofWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int edgeMin1 = 43 - notifyMinutes;
        int edgeMin2;
        int edgeHour2;
        if (notifyMinutes > 9){
            int x = notifyMinutes - 9;
            edgeMin2 = 60 - x;
            edgeHour2 = 0;
        } else {
            edgeMin2 = 9 - notifyMinutes;
            edgeHour2 = 1;
        }
        if (EdgeSession == 1) {
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, edgeMin1);
        }
        if (EdgeSession == 2) {
            calendar.set(Calendar.HOUR, edgeHour2);
            calendar.set(Calendar.MINUTE, edgeMin2);
        }
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.AM_PM, Calendar.PM);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context1);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("TITLE", EdgeTitle);
        editor.putString("TEXT", EdgeText);
        editor.apply();
        Log.d("Notification set", EdgeTitle);
        Log.d("!helper!", "notification set");
        Log.d("helperedgeclasstime", (calendar.getTimeInMillis() - System.currentTimeMillis()) + "");
        if ((calendar.getTimeInMillis() - System.currentTimeMillis()) > 0 && NotificationEnabled) {
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
        EdgeString = EdgeString.substring(EdgeString.indexOf(">") + 1);
        EdgeString = EdgeString.substring(0, EdgeString.indexOf("</h3>"));
        return EdgeString;
    }

    public int parseEdgeSession(String EdgeString){
        int session = 0;
        if (EdgeString.toLowerCase().contains("12:43")){
            session = 1;
        }
        if (EdgeString.toLowerCase().contains("1:09")){
            session = 2;
        }
        return session;
    }
    public String parseEdgeText(String EdgeString){
        EdgeString = EdgeString.substring(EdgeString.indexOf("g>") + 2);
        EdgeString = EdgeString.substring(0, EdgeString.indexOf("</"));
        return EdgeString;
    }
}
