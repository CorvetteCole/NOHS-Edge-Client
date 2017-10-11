package corve.nohsedge;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import static corve.nohsedge.R.attr.colorAccent;

/**
 * Created by Cole on 9/14/2017.
 */

public class WeeklyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    /*@Override
    public boolean onStartJob(JobParameters params) {
        showNotification(this);
        return true;
    }
    @Override
    public boolean onStopJob(JobParameters params) {
        // whether or not you would like JobScheduler to automatically retry your failed job.
        return true;
    }
    */
        private void showNotification(Context context){
        String Title="Schedule your Edge classes today!";
        String Text="Get ahead of the crowd!";
        int reqCode = 0;
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, reqCode, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "weekly_notif_id";
            CharSequence channelName = "Sign up Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(colorAccent);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
            Notification notification = new Notification.Builder(context)
                    .setContentTitle(Title)
                    .setContentText(Text)
                    .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .setSmallIcon(R.drawable.nohsnotif)
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
                    .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .setContentText(Text);
            mBuilder.setContentIntent(pi);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setAutoCancel(true);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(reqCode, mBuilder.build());
        }
    }
}