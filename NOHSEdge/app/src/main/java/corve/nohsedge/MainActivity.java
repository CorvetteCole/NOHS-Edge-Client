package corve.nohsedge;


import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private static final String PREF_PREMEM = "RememPass";
    private static final String PREF_NOTIFY = "NOTIFICATIONS";
    private static final String PREF_AUTOLOGIN = "Autologin";
    private static final String PREF_TITLE = "NotificationTitle";
    private static final String PREF_TEXT = "NotificationText";
    private static final String PREF_EDGE1 = "Edge 1";
    private static final String PREF_EDGE2 = "Edge 2";
    private static final String PREF_EDGE3 = "Edge 3";
    private static final String PREF_EDGE4 = "Edge 4";
    private static final String PREF_EDGE5 = "Edge 5";
    private String WrongPassword = "pass did not match";
    private String webUrl;
    private String usernameCheck;
    String newUrl = "";


    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;

    private final boolean DefaultPRememValue = false;
    private boolean PRememValue;

    private final boolean DefaultNotificationValue = true;
    private boolean NotificationValue;

    private final boolean DefaultAutologinValue = false;
    private boolean AutologinValue;
    private String Title;
    private String Text;

    private final String DefaultEdgeDay1Value = "";
    private final String DefaultEdgeDay2Value = "";
    private final String DefaultEdgeDay3Value = "";
    private final String DefaultEdgeDay4Value = "";
    private final String DefaultEdgeDay5Value = "";
    private String EdgeDay1Value;
    private String EdgeDay2Value;
    private String EdgeDay3Value;
    private String EdgeDay4Value;
    private String EdgeDay5Value;

    Button mLogin;
    TextView mCredit;
    ProgressBar mLoadingCircle;
    private static final String TAG = "MainActivity";
    int x = 0;
    private WebView mLoginPage;
    private String uuid;
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
    int a = 0;
    int REQUEST_CODE = 0;
    int REQUEST_CODE_EDGE = 1;
    private Button mLogout;
    private String EdgeDay2;
    private String EdgeDay3;
    private String EdgeDay4;
    private String EdgeDay5;
    private String EdgeDay6;
    private String EdgeDay1;


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
        setContentView(R.layout.activity_main);
        mLogin = (Button) findViewById(R.id.loginButton);
        mLoginPage = (WebView) findViewById(R.id.loginWebview);
        mCredit = (TextView) findViewById(R.id.creditText);
        mLoadingCircle = (ProgressBar) findViewById(R.id.progressBar);
        mUsername = (TextView) findViewById(R.id.usernameField);
        mPassword = (TextView) findViewById(R.id.passwordField);
        mRemember = (CheckBox) findViewById(R.id.rememberPassword);
        mLoadingText = (TextView) findViewById(R.id.LoadingText);
        mRegister = (Button) findViewById(R.id.RegisterButton);
        mEmail = (TextView) findViewById(R.id.emailField);
        mActivateRegister = (TextView) findViewById(R.id.ActivateRegister);
        mNotify = (Switch) findViewById(R.id.NotificationCheckbox);
        mLogout = (Button) findViewById(R.id.logoutButton);
        mAutoLogin = (Switch) findViewById(R.id.AutoLoginSwitch);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            ShortcutInfo wNOHSShortcut = new ShortcutInfo.Builder(this, "shortcut_web")
                    .setShortLabel("NOHS Website")
                    .setLongLabel("Open the NOHS Website")
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

        mActivateRegister.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if (mActivateRegister.getText().equals("Back to login")) {
                            a = 1;
                        }
                        if (mActivateRegister.getText().equals("Need to register?")) {
                            a = 0;
                        }

                        if (a == 0) {
                            mRegister.setVisibility(View.VISIBLE);
                            mEmail.setVisibility(View.VISIBLE);
                            mActivateRegister.setText("Back to login");
                            mLogin.setVisibility(View.INVISIBLE);

                        }
                        if (a == 1) {
                            mRegister.setVisibility(View.INVISIBLE);
                            mEmail.setVisibility(View.INVISIBLE);
                            mActivateRegister.setText("Need to register?");
                            mLogin.setVisibility(View.VISIBLE);

                        }
                    }
                }
        );

        mLogin.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (getCurrentFocus() != null) {
                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#login");
                            openLoginpage();
                        }
                    }
                }
        );
        mRegister.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (getCurrentFocus() != null) {
                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#register");
                        openLoginpage();
                    }
                }
        );
        mLogout.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        mLoginPage.setVisibility(View.INVISIBLE);
                        mLogin.setVisibility(View.VISIBLE);
                        mUsername.setText("");
                        mPassword.setText("");
                        mRemember.setChecked(false);
                        mUsername.setVisibility(View.VISIBLE);
                        mPassword.setVisibility(View.VISIBLE);
                        mRemember.setVisibility(View.VISIBLE);
                        mAutoLogin.setVisibility(View.VISIBLE);
                        mNotify.setVisibility(View.VISIBLE);
                        mLogout.setVisibility(View.INVISIBLE);
                    }
                });

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
        if (mLoginPage.canGoBack()) {
            mLoginPage.goBack();
        } else {
            super.onBackPressed();
        }
    }


    public void openLoginpage() {
        mAutoLogin.setVisibility(View.INVISIBLE);
        mNotify.setVisibility(View.INVISIBLE);
        mActivateRegister.setVisibility(View.GONE);
        mEmail.setVisibility(View.INVISIBLE);
        mRegister.setVisibility(View.GONE);
        mLogin.setVisibility(View.INVISIBLE);
        mCredit.setVisibility(View.INVISIBLE);
        mRemember.setVisibility(View.INVISIBLE);
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
        mLoginPage.clearHistory();
        clearCookies(this);
        mLoginPage.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress){
                mLoadingText.setText(progress + "%");
                if (progress == 100){
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
                    mLogin.setVisibility(View.VISIBLE);
                    mRemember.setVisibility(View.VISIBLE);
                    mUsername.setVisibility(View.VISIBLE);
                    mPassword.setVisibility(View.VISIBLE);
                    //mLoadingText.setVisibility(View.INVISIBLE);
                    x = 0;
                }
                if ((cm.message().toLowerCase().contains("ok".toLowerCase())) && (cm.message().toLowerCase().contains(mUsername.getText().toString())) && x == 1) {
                    mLoadingCircle.setVisibility(View.INVISIBLE);
                    mLoginPage.setVisibility(View.VISIBLE);
                    mUsername.setVisibility(View.GONE);
                    mPassword.setVisibility(View.GONE);
                    mRemember.setVisibility(View.GONE);
                    mEmail.setVisibility(View.GONE);
                    //mLoadingText.setVisibility(View.INVISIBLE);
                    x = 2;
                    //confirmLogin();
                }
                if (cm.message().toLowerCase().contains("RetrievedEdgeClass".toLowerCase()) && mNotify.isChecked()){
                    InterpretEdgeData(cm.message());
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
                            "document.getElementById('login-username').value = '" + mUsername.getText().toString() + "';" +
                            "document.getElementById('login-password').value = '" + mPassword.getText().toString() + "';" +
                            "l=document.getElementById('login-btn');" +
                            "e=document.createEvent('HTMLEvents');" +
                            "e.initEvent('click',true,true);" +
                            "l.dispatchEvent(e);" +
                            "})()");
                    x = 1;
                }
                if (mLoginPage.getUrl().contains("register")) {
                    mLoginPage.loadUrl("javascript:(function(){" +
                            "document.getElementById('register-username').value = '" + mUsername.getText().toString() + "';" +
                            "document.getElementById('register-password').value = '" + mPassword.getText().toString() + "';" +
                            "document.getElementById('register-email').value = '" + mEmail.getText().toString() + "';" +
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
                    getEdgeClasses();
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                webUrl = mLoginPage.getUrl();
                Log.d("!URL", webUrl);
                if ((webUrl.toLowerCase().contains("edgetime".toLowerCase())) && (x == 2)) {
                    edgeUrl = webUrl;
                    //mLoadingCircle.setVisibility(View.VISIBLE);
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
                if (webUrl.toLowerCase().contains("#profile".toLowerCase())) {
                    mLogout.setVisibility(View.VISIBLE);
                }
                if (!webUrl.toLowerCase().contains("#profile".toLowerCase())) {
                    mLogout.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    private void savePreferences() {
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
        if (mRemember.isChecked()) {
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
        editor.apply();
    }

    private void loadPreferences() {

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
        if (NotificationValue) {
                Log.d("Setting notification", " ");
                //setWeeklyNotifications();  //disabled temporarily so weekly notifications can be moved to their own receiver
        }
        mAutoLogin.setChecked(AutologinValue);
        mRemember.setChecked(PRememValue);
        mNotify.setChecked(NotificationValue);
        EdgeDay1 = EdgeDay1Value;
        EdgeDay2 = EdgeDay2Value;
        EdgeDay3 = EdgeDay3Value;
        EdgeDay4 = EdgeDay4Value;
        EdgeDay5 = EdgeDay5Value;

        InterpretEdgeData(EdgeDay1);
        InterpretEdgeData(EdgeDay2);
        InterpretEdgeData(EdgeDay3);
        InterpretEdgeData(EdgeDay4);
        InterpretEdgeData(EdgeDay5);

        if (mRemember.isChecked()) {
            mUsername.setText(UnameValue);
            mPassword.setText(PasswordValue);
        }
        if (mAutoLogin.isChecked() && !mUsername.getText().toString().equals("") && !mPassword.getText().toString().equals("")) {
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#login");
                openLoginpage();
            }
    }
    private void setWeeklyNotifications() {
        Intent intent = new Intent(MainActivity.this, WeeklyReceiver.class);
        Title =  "Schedule your NOHS classes today!";
        Text = "Get ahead of the crowd!";
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, REQUEST_CODE, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        calendar3.set(Calendar.DAY_OF_WEEK, 5); // Thursday
        // Thursday
        calendar3.set(Calendar.HOUR_OF_DAY, 4);
        calendar3.set(Calendar.MINUTE, 15);
        calendar3.set(Calendar.SECOND, 0);
        calendar3.set(Calendar.AM_PM, Calendar.PM);
        am.set(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent);
    }

    private void setEdgeNotifications(String EdgeTitle, String EdgeText, int EdgeSession, int DayofWeek) {
        Intent intent = new Intent(MainActivity.this, Receiver.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("TITLE", EdgeTitle);
        editor.putString("TEXT", EdgeText);
        editor.commit();
        PendingIntent pendingIntentEdge = PendingIntent.getBroadcast(MainActivity.this, REQUEST_CODE_EDGE, intent, 0);
        AlarmManager amEdge = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar3Edge = Calendar.getInstance();
        calendar3Edge.set(Calendar.DAY_OF_WEEK, DayofWeek); // 5 = Thursday
        calendar3Edge.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        if (EdgeSession == 1) {
            calendar3Edge.set(Calendar.HOUR, 12);
            calendar3Edge.set(Calendar.MINUTE, 38);
        }
        if (EdgeSession == 2) {
            calendar3Edge.set(Calendar.HOUR, 1);
            calendar3Edge.set(Calendar.MINUTE, 4);
        }
        calendar3Edge.set(Calendar.SECOND, 0);
        calendar3Edge.set(Calendar.AM_PM, Calendar.PM);
        amEdge.set(AlarmManager.RTC_WAKEUP, calendar3Edge.getTimeInMillis(), pendingIntentEdge);
        Log.d("Edge notification set!", EdgeTitle);
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
    public void InterpretEdgeData(String consoleMessage){
        if (consoleMessage.toLowerCase().contains("Mon".toLowerCase())){
            EdgeDay1 = consoleMessage;
            Log.d("Monday Edge Class", EdgeDay1);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
                setEdgeNotifications(parseEdgeTitle(EdgeDay1), parseEdgeText(EdgeDay1), parseEdgeSession(EdgeDay1), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Tue".toLowerCase())){
            EdgeDay2 = consoleMessage;
            Log.d("Tuesday Edge Class", EdgeDay2);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
                setEdgeNotifications(parseEdgeTitle(EdgeDay2), parseEdgeText(EdgeDay2), parseEdgeSession(EdgeDay2), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Wed".toLowerCase())){
            EdgeDay3 = consoleMessage;
            Log.d("Wednesday Edge Class", EdgeDay3);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
                setEdgeNotifications(parseEdgeTitle(EdgeDay3), parseEdgeText(EdgeDay3), parseEdgeSession(EdgeDay3), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Thu".toLowerCase())){
            EdgeDay4 = consoleMessage;   //Thursday
            Log.d("Thursday Edge Class", EdgeDay4);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
                setEdgeNotifications(parseEdgeTitle(EdgeDay4), parseEdgeText(EdgeDay4), parseEdgeSession(EdgeDay4), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Fri".toLowerCase())){
            EdgeDay5 = consoleMessage;
            Log.d("Friday Edge Class", EdgeDay5);

            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
                setEdgeNotifications(parseEdgeTitle(EdgeDay5), parseEdgeText(EdgeDay5), parseEdgeSession(EdgeDay5), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        Log.d("!DAY", Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + "");
        //Log.d("!!", parseEdgeText(EdgeDay5));
    }
}
