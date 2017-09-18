package corve.nohsedge;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

/**
 * Created by Cole on 9/18/2017.
 */

public class EdgeClassNotifHelper extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("lalalalala", "urmom");
        setNewEdgeNotif();
        return true;
    }
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("please kill me", "death");
        // whether or not you would like JobScheduler to automatically retry your failed job.
        return false;
    }
    public void setNewEdgeNotif(){
        Log.d("!edgehelper worked", "im ok with life");
        new MainActivity().loadPreferences(true);
        new MainActivity().activateEdgeHelper();
    }
}
