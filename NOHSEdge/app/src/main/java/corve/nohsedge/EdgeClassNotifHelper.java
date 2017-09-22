package corve.nohsedge;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import static corve.nohsedge.MainActivity.PREF_EDGE1;
import static corve.nohsedge.MainActivity.PREF_EDGE2;
import static corve.nohsedge.MainActivity.PREF_EDGE3;
import static corve.nohsedge.MainActivity.PREF_EDGE4;
import static corve.nohsedge.MainActivity.PREF_EDGE5;
import static corve.nohsedge.MainActivity.PREF_EDGE5Cur;
import static corve.nohsedge.MainActivity.PREF_MIN;

/**
 * Created by Cole on 9/18/2017.
 */


public class EdgeClassNotifHelper extends BroadcastReceiver {
    private int notifyMinutes;
    private Context context1;
    private String EdgeDay5Cur;
    @Override
    public void onReceive(Context context, Intent intent) {
        context1 = context;
        setNewEdgeNotif();


    }
    /*@Override
    public boolean onStartJob(JobParameters params) {
        Log.d("maybe it'll", "work");
        //setNewEdgeNotif();
        return true;
    }
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("please kill me", "death");
        // whether or not you would like JobScheduler to automatically retry your failed job.
        return true;
    }
    */
    public void setNewEdgeNotif(){
        Log.d("!edgehelper worked", "im ok with life");
        loadPreferences();

    }
    public void loadPreferences() {
        SharedPreferences settings = context1.getSharedPreferences(MainActivity.PREFS_NAME,
                MainActivity.MODE_PRIVATE);

        // Get value
        String EdgeDay1Value = settings.getString(PREF_EDGE1, DefaultEdgeDay1Value);
        String EdgeDay2Value = settings.getString(PREF_EDGE2, DefaultEdgeDay2Value);
        String EdgeDay3Value = settings.getString(PREF_EDGE3, DefaultEdgeDay3Value);
        String EdgeDay4Value = settings.getString(PREF_EDGE4, DefaultEdgeDay4Value);
        String EdgeDay5Value = settings.getString(PREF_EDGE5, DefaultEdgeDay5Value);
        EdgeDay5Cur = settings.getString(PREF_EDGE5Cur, DefaultEdgeDay5CurValue);
        notifyMinutes = settings.getInt(PREF_MIN, DefaultMinValue);

        InterpretEdgeData(EdgeDay1Value);
        InterpretEdgeData(EdgeDay2Value);
        InterpretEdgeData(EdgeDay3Value);
        InterpretEdgeData(EdgeDay4Value);
        InterpretEdgeData(EdgeDay5Value);
    }




    public void setEdgeNotifications(String EdgeTitle, String EdgeText, int EdgeSession, int DayofWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int edgeMin1 = 43 - notifyMinutes;
        int edgeMin2;
        int edgeHour2;
        if (notifyMinutes > 4){
            int x = notifyMinutes - 4;
            edgeMin2 = 60 - x;
            edgeHour2 = 0;
        } else {
            edgeMin2 = 4 - notifyMinutes;
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
        editor.commit();
        Log.d("Notification set", EdgeTitle);
        Log.d("!helper!", "notification set");
        Log.d("edgeclasstime", (calendar.getTimeInMillis() - System.currentTimeMillis()) + "");
        if ((calendar.getTimeInMillis() - System.currentTimeMillis()) > 0) {
            Intent intent1 = new Intent(context1, Receiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context1,
                    MainActivity.REQUEST_CODE_EDGE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context1.getSystemService(ALARM_SERVICE);
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

       /* ComponentName component = new ComponentName(this, Receiver.class);
        JobInfo.Builder builder = new JobInfo.Builder(REQUEST_CODE_EDGE, component)
                .setMinimumLatency(calendar.getTimeInMillis() - System.currentTimeMillis())
                .setPersisted(true)
                .setOverrideDeadline((calendar.getTimeInMillis() - System.currentTimeMillis()) + 60000);
            JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (calendar.getTimeInMillis() - System.currentTimeMillis() > 0){
                jobScheduler.schedule(builder.build());
            }
            */

        }
    }

    public void InterpretEdgeData(String consoleMessage){
        if (consoleMessage.toLowerCase().contains("Mon".toLowerCase())){
            String EdgeDay1 = consoleMessage;
            Log.d("Monday Edge Class", EdgeDay1);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
                setEdgeNotifications(parseEdgeTitle(EdgeDay1), parseEdgeText(EdgeDay1), parseEdgeSession(EdgeDay1), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Tue".toLowerCase())){
            String EdgeDay2 = consoleMessage;
            Log.d("Tuesday Edge Class", EdgeDay2);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
                setEdgeNotifications(parseEdgeTitle(EdgeDay2), parseEdgeText(EdgeDay2), parseEdgeSession(EdgeDay2), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Wed".toLowerCase())){
            String EdgeDay3 = consoleMessage;
            Log.d("Wednesday Edge Class", EdgeDay3);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
                setEdgeNotifications(parseEdgeTitle(EdgeDay3), parseEdgeText(EdgeDay3), parseEdgeSession(EdgeDay3), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Thu".toLowerCase())){
            String EdgeDay4 = consoleMessage;   //Thursday
            Log.d("Thursday Edge Class", EdgeDay4);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
                setEdgeNotifications(parseEdgeTitle(EdgeDay4), parseEdgeText(EdgeDay4), parseEdgeSession(EdgeDay4), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Fri".toLowerCase())){
            String EdgeDay5;
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                EdgeDay5 = consoleMessage;
            }
            //Log.d("Friday Edge Class", EdgeDay5);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
                setEdgeNotifications(parseEdgeTitle(EdgeDay5Cur), parseEdgeText(EdgeDay5Cur), parseEdgeSession(EdgeDay5Cur), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                Log.d("Edge Notification set", EdgeDay5Cur);
            }
        }
        Log.d("called from helper", "!h!");


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
