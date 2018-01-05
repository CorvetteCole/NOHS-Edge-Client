package corve.nohsedge;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
import static corve.nohsedge.MainActivity.saveEdgeToFirebase;
import static corve.nohsedge.MainActivity.unameValue;

/**
 * Created by Cole on 9/18/2017.
 */


public class EdgeClassNotifHelper extends BroadcastReceiver {
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
}
