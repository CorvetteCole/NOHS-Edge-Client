package corve.nohsedge;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import static android.R.attr.colorAccent;

/**
 * Created by Cole on 9/11/2017.
 */

public class EdgeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(@NonNull Context context, Intent intent) {
        showNotification(context);
    }

    /*@Override
    public boolean onStartJob(JobParameters params) {
        Log.d("maybe work", "or not");
        showNotification(this);
                return false;
    }
    @Override
    public boolean onStopJob(JobParameters params) {
        // whether or not you would like JobScheduler to automatically retry your failed job.
        return true;
    }
*/
    public void showNotification(@NonNull Context context) {

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context);
        String Title=(mSharedPreference.getString("TITLE", "Title"));
        String Text=(mSharedPreference.getString("TEXT", "Text"));
        int reqCode = 0;
        Intent intent = new Intent(context, EdgeViewActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, reqCode, intent, 0);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "edge_class_id";
            CharSequence channelName = "Edge Class Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(colorAccent);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
            Notification notification = new Notification.Builder(context)
                    .setContentTitle(Title)
                    .setContentText(Text)
                    .setSmallIcon(R.drawable.nohsnotif)
                    .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .setChannelId(channelId)
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(reqCode, notification);
        }


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.nohsnotif)
                    .setContentTitle(Title)
                    .setContentText(Text)
                    .setContentIntent(pi)
                    .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(reqCode, mBuilder.build());
        }
    }

}