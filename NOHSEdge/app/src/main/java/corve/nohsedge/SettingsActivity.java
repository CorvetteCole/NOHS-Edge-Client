package corve.nohsedge;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private NumberPicker mNumberPicker;
    private Switch mNotify;
    private Switch mAutoLogin;
    private TextView mNumberPickerTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings);
        mNumberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        mNotify = (Switch) findViewById(R.id.NotificationCheckbox);
        mAutoLogin = (Switch) findViewById(R.id.AutoLoginSwitch);
        mNumberPickerTextView = (TextView) findViewById(R.id.numberPickerTextView);
        String[] nums = new String[40];
        for (int i = 0; i < nums.length; i++)
            nums[i] = Integer.toString(i);
        mAutoLogin.setChecked(MainActivity.AutologinValue);
        mNotify.setChecked(MainActivity.NotificationValue);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(40);
        mNumberPicker.setWrapSelectorWheel(true);
        mNumberPicker.setDisplayedValues(nums);
        mNumberPicker.setValue((MainActivity.notifyMinutes + 1));
        mNumberPickerTextView.setText("Send notification " + (MainActivity.notifyMinutes) + " minutes before class");
        mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {


                mNumberPickerTextView.setText("Send notification " + (newVal - 1) + " minutes before class");
                MainActivity.notifyMinutes = newVal - 1;
            }
        });
    }
    @Override
    public void onBackPressed() {
        MainActivity.AutologinValue = mAutoLogin.isChecked();
        MainActivity.NotificationValue = mNotify.isChecked();
        savePreferences();
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivity.AutologinValue = mAutoLogin.isChecked();
        MainActivity.NotificationValue = mNotify.isChecked();
        savePreferences();
        super.onBackPressed();
        /*switch (item.getItemId()) {
            case android.R.id.home:
                // do something useful
                return(true);
        }*/

        return(super.onOptionsItemSelected(item));
    }


    private void savePreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        MainActivity.MinValue = MainActivity.notifyMinutes;
        editor.putBoolean(MainActivity.PREF_NOTIFY, MainActivity.NotificationValue);
        editor.putBoolean(MainActivity.PREF_AUTOLOGIN, MainActivity.AutologinValue);
        editor.putInt(MainActivity.PREF_MIN, MainActivity.MinValue);
        editor.apply();
    }
}
