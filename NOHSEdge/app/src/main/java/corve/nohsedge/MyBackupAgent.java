package corve.nohsedge;

/**
 * Created by corve on 9/13/2017.
 */

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class MyBackupAgent extends BackupAgentHelper {

    // The name of the SharedPreferences file
    static final String PREFS = "preferences";

    // A key to uniquely identify the set of backup data
    static final String PREFS_BACKUP_KEY = "EdgePrefs";

    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS);
        addHelper(PREFS_BACKUP_KEY, helper);
    }

}
