package corve.nohsedge;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
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
import android.webkit.JavascriptInterface;
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
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private static final String PREF_PREMEM = "RememPass";
    private static final String PREF_NOTIFY = "NOTIFICATIONS";
    private static final String PREF_AUTOLOGIN = "Autologin";
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
    private Button mLogout;
    private int ClassGatherRan;



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
        if (mNotify.isChecked()) {
            setNotifications();
        }
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
        mLoadingText.setVisibility(View.VISIBLE);
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
                    mLoadingText.setVisibility(View.INVISIBLE);
                    x = 0;
                }
                if ((cm.message().toLowerCase().contains("ok".toLowerCase())) && (cm.message().toLowerCase().contains(mUsername.getText().toString())) && x == 1) {
                    mLoadingCircle.setVisibility(View.INVISIBLE);
                    mLoginPage.setVisibility(View.VISIBLE);
                    mUsername.setVisibility(View.GONE);
                    mPassword.setVisibility(View.GONE);
                    mRemember.setVisibility(View.GONE);
                    mEmail.setVisibility(View.GONE);
                    mLoadingText.setVisibility(View.INVISIBLE);
                    x = 2;
                    //confirmLogin();
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
                    mLoadingText.setVisibility(View.INVISIBLE);
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
                    mLoadingCircle.setVisibility(View.VISIBLE);
                    mLoginPage.setVisibility(View.INVISIBLE);
                    mLoadingText.setText("Retrieving Edge Classes...");
                    mLoadingText.setVisibility(View.VISIBLE);
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
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = mUsername.getText().toString();
        PasswordValue = mPassword.getText().toString();
        PRememValue = mRemember.isChecked();
        NotificationValue = mNotify.isChecked();
        AutologinValue = mAutoLogin.isChecked();
        if (mRemember.isChecked()) {
            editor.putString(PREF_UNAME, UnameValue);
            editor.putString(PREF_PASSWORD, PasswordValue);
        }
        editor.putBoolean(PREF_PREMEM, PRememValue);
        editor.putBoolean(PREF_AUTOLOGIN, AutologinValue);
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
        mAutoLogin.setChecked(AutologinValue);
        mRemember.setChecked(PRememValue);
        mNotify.setChecked(NotificationValue);
        if (mRemember.isChecked()) {
            mUsername.setText(UnameValue);
            mPassword.setText(PasswordValue);
        }
        if (mAutoLogin.isChecked() && !mUsername.getText().toString().equals("") && !mPassword.getText().toString().equals("")) {
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#login");
                openLoginpage();
            }
    }
    private void setNotifications() {
        Intent intent = new Intent(MainActivity.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, REQUEST_CODE, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.DAY_OF_WEEK, 5); // Thursday
        calendar3.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1); // First Thursday of
        // Each Month
        // Thursday
        calendar3.set(Calendar.HOUR_OF_DAY, 5);
        calendar3.set(Calendar.MINUTE, 0);
        calendar3.set(Calendar.SECOND, 0);
        calendar3.set(Calendar.AM_PM, Calendar.PM);
        am.set(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent);
    }
    public void getEdgeClasses() {
        ClassGatherRan++;
        if (ClassGatherRan == 1) {
            int ClassElement = 0;

            while (ClassElement != 5) {
                mLoginPage.loadUrl("javascript:(function(){" +
                        "if (document.getElementsByClassName('class user-in-class')['" + ClassElement + "'] != undefined){" +
                        "console.log('Edge Class '" + ClassElement + "')' + document.getElementsByClassName('class user-in-class')['" + ClassElement + "'].innerHTML);}" +
                        "})()");
            ClassElement++;
            ClassGatherRan = 0;
         }
        }
    }
}
