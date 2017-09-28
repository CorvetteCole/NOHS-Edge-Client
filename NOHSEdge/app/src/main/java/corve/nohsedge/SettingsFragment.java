package corve.nohsedge;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Cole on 9/27/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }}