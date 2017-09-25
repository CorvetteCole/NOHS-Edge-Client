package corve.nohsedge;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private static final String PREF_PREMEM = "RememPass";
    private static final String PREF_NOTIFY = "NOTIFICATIONS";
    private static final String PREF_AUTOLOGIN = "Autologin";
    public static final String PREF_EDGE1 = "Edge 1";
    public static final String PREF_EDGE2 = "Edge 2";
    public static final String PREF_EDGE3 = "Edge 3";
    public static final String PREF_EDGE4 = "Edge 4";
    public static final String PREF_EDGE5 = "Edge 5";
    public static final String PREF_MIN = "Notify min";
    public static final String PREF_EDGE5Cur = "Current Friday Edge Class";
    private String WrongPassword = "pass did not match";
    private String webUrl;
    String newUrl = "";
    public static int currentSet = 0;


    private final String DefaultUnameValue = "";
    static String UnameValue;

    private final String DefaultPasswordValue = "";
    static String PasswordValue;

    private final boolean DefaultPRememValue = false;
    static boolean PRememValue;

    private final boolean DefaultNotificationValue = true;
    private boolean NotificationValue;

    private final boolean DefaultAutologinValue = false;
    private boolean AutologinValue;

    public static final int DefaultMinValue = 5;
    private int MinValue;


    public final static String DefaultEdgeDay1Value = "";
    public final static String DefaultEdgeDay2Value = "";
    public final static String DefaultEdgeDay3Value = "";
    public final static String DefaultEdgeDay4Value = "";
    public final static String DefaultEdgeDay5Value = "";
    public final static String DefaultEdgeDay5CurValue = "";
    private String EdgeDay1Value;
    private String EdgeDay2Value;
    private String EdgeDay3Value;
    private String EdgeDay4Value;
    private String EdgeDay5Value;
    private String EdgeDay5CurValue;
    private int getEdgeClassesRan = 0;
    static String EmailValue;

    Button mLogin;
    TextView mCredit;
    ProgressBar mLoadingCircle;
    private static final String TAG = "MainActivity";
    int x = 0;
    private WebView mLoginPage;
    private TextView mUsername;
    private TextView mPassword;
    private CheckBox mRemember;
    private String edgeUrl;
    private TextView mLoadingText;
    private Button mRegister;
    private TextView mEmail;
    private TextView mActivateRegister;
    private Switch mNotify;
    private Switch mAutoLogin;
    private Button mSettings;
    int a = 0;
    static int REQUEST_CODE = 0;
    static int REQUEST_CODE_EDGE = 1;
    static int REQUEST_CODE_WEEKLY = 2;
    private Button mLogout;
    private String EdgeDay1;
    private String EdgeDay2;
    private String EdgeDay3;
    private String EdgeDay4;
    private String EdgeDay5;
    private String EdgeDay5Cur;
    public int NotificationSet;
    public int notifyMinutes = 5;
    private boolean settingsOpen = false;
    private NumberPicker mNumberPicker;
    private TextView mNumberPickerTextView;
    private String[] EdgeDay5Ar = new String[2];
    public static int Login = 0;
    public static int Register = 0;
    public static boolean calledForeign;


    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!calledForeign) {
            setContentView(R.layout.activity_login);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }


        setContentView(R.layout.activity_main);
        mLoginPage = (WebView) findViewById(R.id.loginWebview);
        mLoadingCircle = (ProgressBar) findViewById(R.id.progressBar);
        mLoadingText = (TextView) findViewById(R.id.LoadingText);
        mNotify = (Switch) findViewById(R.id.NotificationCheckbox);
        mLogout = (Button) findViewById(R.id.logoutButton);
        mAutoLogin = (Switch) findViewById(R.id.AutoLoginSwitch);
        mSettings = (Button) findViewById(R.id.settingsButton);
        mNumberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        mNumberPickerTextView = (TextView) findViewById(R.id.numberPickerTextView);
        NotificationSet = 0;
        String[] nums = new String[40];
        for (int i = 0; i < nums.length; i++)
            nums[i] = Integer.toString(i);

        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(40);
        mNumberPicker.setWrapSelectorWheel(true);
        mNumberPicker.setDisplayedValues(nums);

        if (calledForeign) {
            if (Login == 1){
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#login");
                openLoginpage();
            }
            if (Register == 1){
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#register");
                openLoginpage();
            }
        }

        activateEdgeHelper();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            ShortcutInfo wNOHSShortcut = new ShortcutInfo.Builder(this, "shortcut_web")
                    .setShortLabel("NOHS Website")
                    .setLongLabel("Open NOHS Website")
                    .setIcon(Icon.createWithResource(this, R.drawable.nohs))
                    .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.oldham.kyschools.us/nohs/")))
                    .build();
            ShortcutInfo wCampusShortcut = new ShortcutInfo.Builder(this, "shortcut_dynamic")
                    .setShortLabel("Campus Portal")
                    .setLongLabel("Open Campus Portal")
                    .setIcon(Icon.createWithResource(this, R.drawable.infinitecampus))
                    .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://kyede10.infinitecampus.org/campus/portal/oldham.jsp")))
                    .build();
            shortcutManager.setDynamicShortcuts(Arrays.asList(wNOHSShortcut, wCampusShortcut));
        }

        mLogout.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        Log.d("broken lol", "im not a god... yet");
                    }
                });
        mSettings.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if (settingsOpen) {
                            a = 1;
                        }
                        if (!settingsOpen) {
                            a = 0;
                        }

                        if (a == 0) {
                            mLoginPage.setVisibility(View.INVISIBLE);
                            mNotify.setVisibility(View.VISIBLE);
                            mAutoLogin.setVisibility(View.VISIBLE);
                            mSettings.setText("Back");
                            settingsOpen = true;
                            mLoadingCircle.setVisibility(View.INVISIBLE);
                            mNumberPicker.setVisibility(View.VISIBLE);
                            mNumberPickerTextView.setVisibility(View.VISIBLE);
                            mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                @Override
                                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {


                                    mNumberPickerTextView.setText("Send notification " + (newVal - 1) + " minutes before class");
                                    notifyMinutes = newVal - 1;
                                }
                            });


                        }
                        if (a == 1) {
                            mLoginPage.setVisibility(View.VISIBLE);
                            mNotify.setVisibility(View.INVISIBLE);
                            mAutoLogin.setVisibility(View.INVISIBLE);
                            mSettings.setText("Settings");
                            settingsOpen = false;
                            mLoadingCircle.setVisibility(View.INVISIBLE);
                            mNumberPicker.setVisibility(View.INVISIBLE);
                            mNumberPickerTextView.setVisibility(View.INVISIBLE);
                            savePreferences();

                        }
                    }
                }
        );


    }


    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d(TAG, "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            Log.d(TAG, "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    public String getCookie(String siteName, String CookieName) {
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        String[] temp = cookies.split(";");
        for (String ar1 : temp) {
            if (ar1.contains(CookieName)) {
                String[] temp1 = ar1.split("=");
                CookieValue = temp1[1];
                break;
            }
        }
        return CookieValue;
    }

    @Override
    public void onBackPressed() {
        /*if (mLoginPage.getUrl().toLowerCase().contains("edgetime".toLowerCase()) && mLoginPage.canGoBack()){
            mLoginPage.goBack();
            mLoadingText.setVisibility(View.VISIBLE);
            mLoadingText.setText("Loading homescreen...");
            int t = 0;
            while (t < 1000){
                t++;
            }
            mLoginPage.goBack();
            //mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen");
        }*/


        if (mLoginPage.canGoBack() && !settingsOpen) {
            mLoginPage.goBack();
            if (mLoginPage.getUrl().toLowerCase().contains("edgetime".toLowerCase())) {
                Toast.makeText(this, "Click again to exit Edge", Toast.LENGTH_SHORT).show();
            }
        } else if (settingsOpen) {
            mNotify.setVisibility(View.INVISIBLE);
            mAutoLogin.setVisibility(View.INVISIBLE);
            mLoginPage.setVisibility(View.VISIBLE);
            mSettings.setText("Settings");
            mLoadingCircle.setVisibility(View.INVISIBLE);
            settingsOpen = false;
            mNumberPicker.setVisibility(View.INVISIBLE);
            mNumberPickerTextView.setVisibility(View.INVISIBLE);
        } else {
            Log.d("not good", "kill me");
            super.onBackPressed();
        }
    }


    public void openLoginpage() {
        mLoadingCircle.setVisibility(View.VISIBLE);
        mLoadingText.setText("Checking login details...");
        //mLoadingText.setVisibility(View.VISIBLE);
        WebSettings webSettings = mLoginPage.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        //webSettings.setLoadsImagesAutomatically(false);
        mLoginPage.clearHistory();
        clearCookies(this);
        mLoginPage.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                mLoadingText.setText(progress + "%");
                if (progress == 100) {
                    mLoadingText.setVisibility(View.INVISIBLE);
                } else {
                    mLoadingText.setVisibility(View.VISIBLE);
                    mLoadingCircle.setVisibility(View.VISIBLE);
                }

            }

            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                if (cm.message().toLowerCase().contains(WrongPassword.toLowerCase())) {
                    mLoadingCircle.setVisibility(View.INVISIBLE);
                    //***REALLY****
                    //Add code to switch back to other activity here
                    //getSupportActionBar().show();
                    //mLoadingText.setVisibility(View.INVISIBLE);
                    x = 0;
                }
                if ((cm.message().toLowerCase().contains("ok".toLowerCase())) && (cm.message().toLowerCase().contains(mUsername.getText().toString())&& x == 1)) {
                    if (mLoginPage.getUrl().toLowerCase().contains("#homescreen")) {
                        mLoadingCircle.setVisibility(View.INVISIBLE);
                        mLoginPage.setVisibility(View.VISIBLE);
                        //THE KEY IS BELOW. THE KEY I TELL YOU!
                        //mLoginPage.loadUrl("https://api.superfanu.com/6.0.0/gen/link_track.php?platform=Web:%20chrome&uuid=" + getCookie("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen", "UUID") + "&nid=305&lkey=nohsstampede-edgetime-module");
                        x = 2;
                    }
                }
                if (cm.message().toLowerCase().contains("RetrievedEdgeClass".toLowerCase()) && mNotify.isChecked()) {
                    InterpretEdgeData(cm.message());
                }
                if (cm.message().toLowerCase().contains("post_queue")/* && (cm.lineNumber() == 419)*/) {
                    mLoadingCircle.setVisibility(View.INVISIBLE);
                    mLoginPage.setVisibility(View.VISIBLE);
                    getEdgeClasses();
                }
                return true;
            }

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        mLoginPage.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mLoginPage.getUrl().contains("login")) {
                    mLoginPage.loadUrl("javascript:(function(){" +
                            "document.getElementById('login-username').value = '" + UnameValue + "';" +
                            "document.getElementById('login-password').value = '" + PasswordValue + "';" +
                            "l=document.getElementById('login-btn');" +
                            "e=document.createEvent('HTMLEvents');" +
                            "e.initEvent('click',true,true);" +
                            "l.dispatchEvent(e);" +
                            "})()");
                    x = 1;
                }
                if (mLoginPage.getUrl().contains("register")) {
                    mLoginPage.loadUrl("javascript:(function(){" +
                            "document.getElementById('register-username').value = '" + UnameValue + "';" +
                            "document.getElementById('register-password').value = '" + PasswordValue + "';" +
                            "document.getElementById('register-email').value = '" + EmailValue + "';" +
                            "l=document.getElementById('register-btn');" +
                            "e=document.createEvent('HTMLEvents');" +
                            "e.initEvent('click',true,true);" +
                            "l.dispatchEvent(e);" +
                            "})()");
                    x = 1;
                }
                if ((mLoginPage.getUrl().contains("nohsstampede")) && (x == 2)) {
                    mLoadingCircle.setVisibility(View.INVISIBLE);
                    mLoginPage.setVisibility(View.VISIBLE);
                    newUrl = "";
                }
                if ((mLoginPage.getUrl().contains("edgetime")) && (x == 2)) {
                    mLoadingCircle.setVisibility(View.INVISIBLE);
                    mLoginPage.setVisibility(View.VISIBLE);
                    //mLoadingText.setVisibility(View.INVISIBLE);
                }
                if (mLoginPage.getUrl().toLowerCase().contains("#homescreen".toLowerCase()) && mLoginPage.getVisibility() == View.VISIBLE) {
                    mSettings.setVisibility(View.VISIBLE);
                    mLogout.setVisibility(View.VISIBLE);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().hide();
                    }
                }
                if (!mLoginPage.getUrl().toLowerCase().contains("#homescreen".toLowerCase())) {
                    mSettings.setVisibility(View.INVISIBLE);
                    mLogout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                webUrl = mLoginPage.getUrl();
                Log.d("!URL", webUrl);
                if ((webUrl.toLowerCase().contains("edgetime".toLowerCase())) && (x == 2)) {
                    edgeUrl = webUrl;
                    mLoadingCircle.setVisibility(View.VISIBLE);
                    mLoginPage.setVisibility(View.INVISIBLE);
                    mLoadingText.setText("Retrieving Edge Classes...");
                    //mLoadingText.setVisibility(View.VISIBLE);
                }
                if ((!webUrl.toLowerCase().contains("edgetime".toLowerCase())) && (!webUrl.toLowerCase().contains("nohs".toLowerCase()))) {
                    //check to make sure web page hasn't been opened already (avoids opening 20+ chrome tabs upon one button click)
                    mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen");
                    if (!webUrl.equals(newUrl)) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                        startActivity(browserIntent);
                        newUrl = webUrl;
                    }
                }
                if (webUrl.toLowerCase().contains("#homescreen".toLowerCase()) && mLoadingCircle.getVisibility() == View.INVISIBLE) {
                    if (mLoginPage.getVisibility() == View.VISIBLE) {
                        mSettings.setVisibility(View.VISIBLE);
                        mLogout.setVisibility(View.VISIBLE);
                    }
                }
                if (!webUrl.toLowerCase().contains("#homescreen".toLowerCase())) {
                    mSettings.setVisibility(View.INVISIBLE);
                    mLogout.setVisibility(View.INVISIBLE);
                }
                //if (webUrl.toLowerCase().contains("#login".toLowerCase())){
                //    mLoginPage.setVisibility(View.INVISIBLE);
                //    mLoadingCircle.setVisibility(View.VISIBLE);
                //}

            }
        });
    }

    public void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_APPEND);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = mUsername.getText().toString();
        PasswordValue = mPassword.getText().toString();
        PRememValue = mRemember.isChecked();
        NotificationValue = mNotify.isChecked();
        AutologinValue = mAutoLogin.isChecked();
        EdgeDay1Value = EdgeDay1;
        EdgeDay2Value = EdgeDay2;
        EdgeDay3Value = EdgeDay3;
        EdgeDay4Value = EdgeDay4;
        EdgeDay5Value = EdgeDay5;
        EdgeDay5CurValue = EdgeDay5Cur;
        MinValue = notifyMinutes;
        if (PRememValue) {
            editor.putString(PREF_UNAME, UnameValue);
            editor.putString(PREF_PASSWORD, PasswordValue);
        }
        editor.putBoolean(PREF_NOTIFY, NotificationValue);
        editor.putBoolean(PREF_PREMEM, PRememValue);
        editor.putBoolean(PREF_AUTOLOGIN, AutologinValue);
        editor.putString(PREF_EDGE1, EdgeDay1Value);
        editor.putString(PREF_EDGE2, EdgeDay2Value);
        editor.putString(PREF_EDGE3, EdgeDay3Value);
        editor.putString(PREF_EDGE4, EdgeDay4Value);
        editor.putString(PREF_EDGE5, EdgeDay5Value);
        editor.putString(PREF_EDGE5Cur, EdgeDay5CurValue);
        editor.putInt(PREF_MIN, MinValue);
        editor.apply();
    }

    public void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        PRememValue = settings.getBoolean(PREF_PREMEM, DefaultPRememValue);
        NotificationValue = settings.getBoolean(PREF_NOTIFY, DefaultNotificationValue);
        AutologinValue = settings.getBoolean(PREF_AUTOLOGIN, DefaultAutologinValue);
        EdgeDay1Value = settings.getString(PREF_EDGE1, DefaultEdgeDay1Value);
        EdgeDay2Value = settings.getString(PREF_EDGE2, DefaultEdgeDay2Value);
        EdgeDay3Value = settings.getString(PREF_EDGE3, DefaultEdgeDay3Value);
        EdgeDay4Value = settings.getString(PREF_EDGE4, DefaultEdgeDay4Value);
        EdgeDay5Value = settings.getString(PREF_EDGE5, DefaultEdgeDay5Value);
        EdgeDay5CurValue = settings.getString(PREF_EDGE5Cur, DefaultEdgeDay5CurValue);
        MinValue = settings.getInt(PREF_MIN, DefaultMinValue);
        if (NotificationValue) {
            Log.d("Setting notification", " ");
            setWeeklyNotifications();
        }
        mAutoLogin.setChecked(AutologinValue);
        mRemember.setChecked(PRememValue);
        mNotify.setChecked(NotificationValue);
        EdgeDay1 = EdgeDay1Value;
        EdgeDay2 = EdgeDay2Value;
        EdgeDay3 = EdgeDay3Value;
        EdgeDay4 = EdgeDay4Value;
        EdgeDay5 = EdgeDay5Value;
        EdgeDay5Cur = EdgeDay5CurValue;
        notifyMinutes = MinValue;
        mNumberPicker.setValue((notifyMinutes + 1));
        mNumberPickerTextView.setText("Send notification " + (notifyMinutes) + " minutes before class");

        InterpretEdgeData(EdgeDay1);
        InterpretEdgeData(EdgeDay2);
        InterpretEdgeData(EdgeDay3);
        InterpretEdgeData(EdgeDay4);
        InterpretEdgeData(EdgeDay5);
        currentSet = 0;

        if (mRemember.isChecked()) {
            mUsername.setText(UnameValue);
            mPassword.setText(PasswordValue);
        }
        if (mAutoLogin.isChecked() && !mUsername.getText().toString().equals("") && !mPassword.getText().toString().equals("")) {
            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#login");
            openLoginpage();
            getSupportActionBar().hide();
        }

    }

    private void setWeeklyNotifications() {
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.DAY_OF_WEEK, 5); // Thursday
        calendar3.set(Calendar.HOUR, 5);
        calendar3.set(Calendar.MINUTE, 0);
        calendar3.set(Calendar.SECOND, 0);
        calendar3.set(Calendar.AM_PM, Calendar.PM);
        if ((calendar3.getTimeInMillis() - System.currentTimeMillis()) > 0) {
            Intent intent3 = new Intent(this, WeeklyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                    REQUEST_CODE_WEEKLY, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
            am.setExact(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent);
        /*ComponentName component = new ComponentName(this, WeeklyReceiver.class);
        JobInfo.Builder builder = new JobInfo.Builder(REQUEST_CODE_WEEKLY, component)
                .setMinimumLatency(calendar3.getTimeInMillis() - System.currentTimeMillis())
                .setPersisted(true)
                .setOverrideDeadline((calendar3.getTimeInMillis() - System.currentTimeMillis()) + 1800000);
        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (calendar3.getTimeInMillis() - System.currentTimeMillis() > 0){
            jobScheduler.schedule(builder.build());
            */
        }
    }

    public void activateEdgeHelper() {
        int ONE_MIN = 60000;
        int ONE_DAY = 86400000;
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR, 1);
        calendar2.set(Calendar.MINUTE, 10);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.AM_PM, Calendar.AM);
        long activateTime;
        //ComponentName component = new ComponentName(this, EdgeClassNotifHelper.class);
        Log.d("!edgehelp c2", calendar2.getTimeInMillis() + "");
        Log.d("!edgehelp s", System.currentTimeMillis() + "");
        if (calendar2.getTimeInMillis() < System.currentTimeMillis()) {
            activateTime = (calendar2.getTimeInMillis() + ONE_DAY);
        } else {
            activateTime = calendar2.getTimeInMillis();
        }

        Log.d("edgehelptime", (activateTime - System.currentTimeMillis()) + "");
        Intent intent2 = new Intent(this, EdgeClassNotifHelper.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                REQUEST_CODE, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        //am.setInexactRepeating(AlarmManager.RTC_WAKEUP, activateTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        am.setExact(AlarmManager.RTC_WAKEUP, activateTime, pendingIntent);
        /*JobInfo.Builder builder = new JobInfo.Builder(REQUEST_CODE, component)
                .setPersisted(true)
                .setMinimumLatency((calendar2.getTimeInMillis()) - System.currentTimeMillis())
                .setOverrideDeadline(ONE_MIN * 180);

        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());*/
    }

    public void setEdgeNotifications(String EdgeTitle, String EdgeText, int EdgeSession, int DayofWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int edgeMin1 = 43 - notifyMinutes;
        int edgeMin2;
        int edgeHour2;
        if (notifyMinutes > 4) {
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("TITLE", EdgeTitle);
        editor.putString("TEXT", EdgeText);
        editor.commit();
        Log.d("Notification set", EdgeTitle);
        Log.d("edgeclasstime", (calendar.getTimeInMillis() - System.currentTimeMillis()) + "");
        if ((calendar.getTimeInMillis() - System.currentTimeMillis()) > 0) {
            Intent intent1 = new Intent(this, Receiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                    REQUEST_CODE_EDGE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
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


    public void getEdgeClasses() {
        int ClassElement = 0;
        while (ClassElement != 5) {
            mLoginPage.loadUrl("javascript:(function(){" +
                    "if (document.getElementsByClassName('class user-in-class')['" + ClassElement + "'] != undefined){" +
                    "console.log('RetrievedEdgeClass' + document.getElementsByClassName('class user-in-class')['" + ClassElement + "'].innerHTML);}" +
                    "})()");
            ClassElement++;
        }
    }

    public String parseEdgeTitle(String EdgeString) {
        EdgeString = EdgeString.substring(EdgeString.indexOf(">") + 1);
        EdgeString = EdgeString.substring(0, EdgeString.indexOf("</h3>"));
        return EdgeString;
    }

    public int parseEdgeSession(String EdgeString) {
        int session = 0;
        if (EdgeString.toLowerCase().contains("12:43")) {
            session = 1;
        }
        if (EdgeString.toLowerCase().contains("1:09")) {
            session = 2;
        }
        return session;
    }

    public String parseEdgeText(String EdgeString) {
        EdgeString = EdgeString.substring(EdgeString.indexOf("g>") + 2);
        EdgeString = EdgeString.substring(0, EdgeString.indexOf("</"));
        return EdgeString;
    }

    public void InterpretEdgeData(String consoleMessage) {
        if (consoleMessage.toLowerCase().contains("Mon".toLowerCase())) {
            EdgeDay1 = consoleMessage;
            Log.d("Monday Edge Class", EdgeDay1);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                setEdgeNotifications(parseEdgeTitle(EdgeDay1), parseEdgeText(EdgeDay1), parseEdgeSession(EdgeDay1), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Tue".toLowerCase())) {
            EdgeDay2 = consoleMessage;
            Log.d("Tuesday Edge Class", EdgeDay2);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                setEdgeNotifications(parseEdgeTitle(EdgeDay2), parseEdgeText(EdgeDay2), parseEdgeSession(EdgeDay2), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Wed".toLowerCase())) {
            EdgeDay3 = consoleMessage;
            Log.d("Wednesday Edge Class", EdgeDay3);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                setEdgeNotifications(parseEdgeTitle(EdgeDay3), parseEdgeText(EdgeDay3), parseEdgeSession(EdgeDay3), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Thu".toLowerCase())) {
            EdgeDay4 = consoleMessage;   //Thursday
            Log.d("Thursday Edge Class", EdgeDay4);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                setEdgeNotifications(parseEdgeTitle(EdgeDay4), parseEdgeText(EdgeDay4), parseEdgeSession(EdgeDay4), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Fri".toLowerCase())) {
            EdgeDay5 = consoleMessage;
            if (currentSet == 0) {
                EdgeDay5Ar[0] = consoleMessage;
                currentSet = 1;
            }
            else {
                Log.d("TRIGGERRRREEEDDD", "");
                EdgeDay5Ar[1] = EdgeDay5;
            }
            if (!consoleMessage.contains(EdgeDay5Ar[0])) {
                EdgeDay5Ar[1] = consoleMessage;
            }
            EdgeDay5Cur = EdgeDay5Ar[0];
            Log.d("!test!", EdgeDay5Cur);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                setEdgeNotifications(parseEdgeTitle(EdgeDay5Cur), parseEdgeText(EdgeDay5Cur), parseEdgeSession(EdgeDay5Cur), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_APPEND);
                SharedPreferences.Editor editor = settings.edit();
                EdgeDay5CurValue = EdgeDay5Cur;
                editor.putString(PREF_EDGE5Cur, EdgeDay5CurValue);
                editor.apply();
            }
        }
    }
}



