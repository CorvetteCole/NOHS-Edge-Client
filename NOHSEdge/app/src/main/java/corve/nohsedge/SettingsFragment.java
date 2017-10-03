package corve.nohsedge;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import static corve.nohsedge.MainActivity.MinValue;

/**
 * Created by Cole on 9/27/2017.
 */

public class SettingsFragment extends PreferenceFragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final Preference NumberPickerPref = this.findPreference("Notify_min");
        NumberPickerPref.setSummary("Notifications will be sent " + MinValue+ " minutes before class starts");
        NumberPickerPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                NumberPickerPref.setSummary("Notifications will be sent " + newValue + " minutes before class starts");
                return true;
            }
        });
        }
    }