package corve.nohsedge;


import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Icon;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.IconCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;

import de.cketti.mailto.EmailIntentBuilder;

import static android.view.View.VISIBLE;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final String PREFS_NAME = "preferences";
    static final String PREF_UNAME = "Username";
    static final String PREF_PASSWORD = "Password";
    static final String PREF_PREMEM = "RememPass";
    static final String PREF_NOTIFYEDGE = "NotifyEdge";
    static final String PREF_NOTIFYWEEKLY = "NotifyWeekly";
    static final String PREF_AUTOLOGIN = "AutoLogin";
    static final String PREF_EDGE1 = "Edge 1";
    static final String PREF_EDGE2 = "Edge 2";
    static final String PREF_EDGE3 = "Edge 3";
    static final String PREF_EDGE4 = "Edge 4";
    static final String PREF_EDGE5 = "Edge 5";
    static final String PREF_MIN = "Notify_min";
    static final String PREF_EDGE5Cur = "Current Friday Edge Class";
    static final String PREF_FIRSTLOAD = "FirstLoad";
    private String webUrl;
    String newUrl = "";
    static boolean imageLoadOnWiFiValue;

    private Boolean FirstLoadValue = true;
    private final Boolean DefaultFirstLoadVaue = true;
    private final String DefaultUnameValue = "";
    private final String DefaultPasswordValue = "";
    private final boolean DefaultPRememValue = false;
    private final boolean DefaultEdgeNotificationValue = true;
    private final boolean DefaultWeeklyNotificationValue = true;
    private final boolean DefaultAutologinValue = false;

    static String unameValue;
    static String passwordValue;
    static boolean pRememValue;
    static boolean edgeNotificationValue;
    static boolean weeklyNotificationValue;
    static boolean autoLoginValue;
    static final int DefaultMinValue = 5;
    static int minValue;
    final static String DefaultEdgeDay1Value = "";
    final static String DefaultEdgeDay2Value = "";
    final static String DefaultEdgeDay3Value = "";
    final static String DefaultEdgeDay4Value = "";
    final static String DefaultEdgeDay5Value = "";
    final static String DefaultEdgeDay5CurValue = "";
    static String emailValue;
    ProgressBar mLoadingCircle;
    private static final String TAG = "MainActivity";
    private WebView mLoginPage;
    private TextView mLoadingText;
    static int REQUEST_CODE = 0;
    static int REQUEST_CODE_EDGE = 1;
    static int REQUEST_CODE_WEEKLY = 2;
    static String[] mEdgeDay = new String[7];
    static String[] mDay = new String[7];

    @Nullable
    static String mEdgeDay5Cur;
    static int notifyMinutes = 5;
    static int login = 0;
    static int register = 0;
    static boolean calledForeign;
    private TextView mWelcome;
    private String edgePage;
    private int id;
    @Nullable
    static String uuid;
    private TextView mEdgeTitle;
    private TextView mEdgeText;
    private TextView mEdgeTime;
    private TextView mEdgeTitleConst;
    private TextView mEdgeTextConst;
    private TextView mEdgeTimeConst;
    @Nullable
    private ConnectivityManager cm;
    @Nullable
    private String fullName = "";
    @NonNull
    private Context context = this;
    private boolean loggedIn = false;


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
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!calledForeign) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        if (calledForeign) {
            //setupDrawer();
            mLoginPage = findViewById(R.id.loginWebview);
            mLoadingCircle = findViewById(R.id.progressBar);
            mLoadingText = findViewById(R.id.LoadingText);
            mWelcome = findViewById(R.id.helloTextView);
            mEdgeTitle = findViewById(R.id.edgeClassTitle);
            mEdgeText = findViewById(R.id.edgeClassText);
            mEdgeTime = findViewById(R.id.edgeClassTime);
            mEdgeTitleConst = findViewById(R.id.edgeTitleTextView);
            mEdgeTextConst = findViewById(R.id.edgeTextTextView);
            mEdgeTimeConst = findViewById(R.id.edgeTimeTextView);
            if (login == 1) {
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#login");
                openLoginpage();
            }
            if (register == 1) {
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#register");
                openLoginpage();
            }

            activateEdgeHelper();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
                ShortcutInfo wNOHSShortcut = new ShortcutInfo.Builder(context, "shortcut_nohs")
                        .setShortLabel("NOHS Website")
                        .setLongLabel("Open NOHS Website")
                        .setIcon(Icon.createWithResource(context, R.mipmap.ic_nohs_shortcut))
                        .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.oldham.kyschools.us/nohs/")))
                        .build();
                ShortcutInfo wCampusShortcut = new ShortcutInfo.Builder(context, "shortcut_campus")
                        .setShortLabel("Campus Portal")
                        .setLongLabel("Open Campus Portal")
                        .setIcon(Icon.createWithResource(context, R.mipmap.ic_infinitecampus_shortcut))
                        .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://kyede10.infinitecampus.org/campus/portal/oldham.jsp")))
                        .build();
                shortcutManager.setDynamicShortcuts(Arrays.asList(wNOHSShortcut, wCampusShortcut));
            }
        }
    }
    private void setupDrawer(){
        //setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_homescreen);
        getSupportActionBar().setTitle("Home");
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

    @Nullable
    public String getCookie(String siteName, @NonNull String CookieName) {
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        if (cookies != null){
        String[] temp = cookies.split(";");
        for (String ar1 : temp) {
            if (ar1.contains(CookieName)) {
                String[] temp1 = ar1.split("=");
                CookieValue = temp1[1];
                break;
            }
          }
        }
        return CookieValue;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        try {
            if (mLoginPage.canGoBack()) {
                mLoginPage.goBack();
            } else {
                super.onBackPressed();
            }
        }
        catch (NullPointerException e){
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();
        boolean drawerClose = true;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        //boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        Log.d("isWiFi?", isWiFi + "");
        if (!imageLoadOnWiFiValue){
            WebSettings webSettings = mLoginPage.getSettings();
            webSettings.setLoadsImagesAutomatically(true);
            Log.d("ImgLoadStatus", "Cell&WiFi");
        }
        if (imageLoadOnWiFiValue && isWiFi){
            WebSettings webSettings = mLoginPage.getSettings();
            webSettings.setLoadsImagesAutomatically(true);
            Log.d("ImgLoadStatus", "WiFi");
        } if (imageLoadOnWiFiValue && !isWiFi) {
            WebSettings webSettings = mLoginPage.getSettings();
            webSettings.setLoadsImagesAutomatically(false);
            Log.d("ImgLoadStatus", "Disabled");
        }

        if (id == R.id.nav_schedule) {
            drawerClose = false;
            uuid = getCookie("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen", "UUID");
            Intent intent = new Intent(getBaseContext(), EdgeViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signup) {
            mLoginPage.stopLoading();
            EdgeSignupActivity.showPage = true;
            drawerClose = false;
            uuid = getCookie("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen", "UUID");
            Intent intent = new Intent(getBaseContext(), EdgeSignupActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gear){
            drawerClose = false;
            Uri uri = Uri.parse("https://sideline.bsnsports.com/schools/kentucky/goshen/north-oldham-high-school");
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
            intentBuilder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
            intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            CustomTabsIntent customTabsIntent = intentBuilder.build();
            customTabsIntent.launchUrl(this, uri);

            //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sideline.bsnsports.com/schools/kentucky/goshen/north-oldham-high-school"));
            //startActivity(browserIntent);
        } else if (id == R.id.nav_feedback){
            Intent emailIntent = EmailIntentBuilder.from(this)
                    .to("corvettecole@gmail.com")
                    .subject("NOHS Stampede App")
                    .body("[insert feature request or bug report here]")
                    .build();
            startActivity(emailIntent);
        } else if (id == R.id.nav_profile) {
            getSupportActionBar().setTitle("Profile");
            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#profile-edit");
            edgePage = "profile-edit";
            mLoginPage.setVisibility(VISIBLE);
            setWelcomeVisible(false);
        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(this, PreferencesActivity.class);
            startActivity(i);
            drawerClose = false;
        } else if (id == R.id.nav_homescreen){
            getSupportActionBar().setTitle("Home");
            mLoginPage.setVisibility(View.INVISIBLE);
            setWelcomeVisible(true);
        } else if (id == R.id.nav_notifications){
            getSupportActionBar().setTitle("Notifications");
            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#notifications");
            edgePage = "notifications";
            mLoginPage.setVisibility(VISIBLE);
            setWelcomeVisible(false);
        } else if (id == R.id.nav_events){
            getSupportActionBar().setTitle("Events");
            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#events");
            mLoginPage.setVisibility(VISIBLE);
            edgePage = "events";
            setWelcomeVisible(false);
        } else if (id == R.id.nav_leaderboard){
            getSupportActionBar().setTitle("Leaderboard");
            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#leaderboard");
            mLoginPage.setVisibility(VISIBLE);
            edgePage = "leaderboard";
            setWelcomeVisible(false);
        } else if (id == R.id.nav_fancam){
            getSupportActionBar().setTitle("Fancam");
            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#fancam");
            mLoginPage.setVisibility(VISIBLE);
            edgePage = "fancam";
            setWelcomeVisible(false);
        } else if (id == R.id.nav_logout){
            autoLoginValue = false;
            pRememValue = false;
            unameValue = "";
            passwordValue = "";
            for (int i = 0; i < mEdgeDay.length; i++){
                mEdgeDay[i] = "";
            }
            mEdgeDay5Cur = "";
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PREF_PASSWORD, "");
            editor.putString(PREF_UNAME, "");
            editor.putBoolean(PREF_AUTOLOGIN, false);
            editor.putBoolean(PREF_PREMEM, false);
            editor.putString(PREF_EDGE1, "");
            editor.putString(PREF_EDGE2, "");
            editor.putString(PREF_EDGE3, "");
            editor.putString(PREF_EDGE4, "");
            editor.putString(PREF_EDGE5, "");
            editor.putString(PREF_EDGE5Cur, "");
            editor.putInt(PREF_MIN, 5);
            editor.commit();
            mLoginPage.clearHistory();
            mLoginPage.clearCache(true);
            clearCookies(this);
            WebView obj = mLoginPage;
            obj.clearCache(true);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        if (drawerClose) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    private void openLoginpage() {
        //mLoginPage.setVisibility(VISIBLE);
        mLoadingCircle.setVisibility(VISIBLE);
        mLoadingText.setText("Checking login details...");
        WebSettings webSettings = mLoginPage.getSettings();
        webSettings.setLoadsImagesAutomatically(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        //webSettings.setAllowContentAccess(true);
        //webSettings.setAllowFileAccess(true);
        //webSettings.setAllowUniversalAccessFromFileURLs(true);
        //webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);

        mLoginPage.clearHistory();
        clearCookies(this);
        mLoginPage.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                mLoadingText.setText(progress + "%");
                if (progress == 100) {
                    mLoadingText.setVisibility(View.INVISIBLE);
                } else {
                    mLoadingText.setVisibility(VISIBLE);
                    mLoadingCircle.setVisibility(VISIBLE);
                }

            }
            public boolean onConsoleMessage(@NonNull ConsoleMessage cm) {
                Log.d(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                if (cm.message().toLowerCase().contains("pass did not match".toLowerCase())) {
                    autoLoginValue = false;
                    pRememValue = false;
                    unameValue = "";
                    passwordValue = "";
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("invalid", true);
                    editor.putString(PREF_UNAME, unameValue);
                    editor.putString(PREF_PASSWORD, passwordValue);
                    editor.putBoolean(PREF_PREMEM, pRememValue);
                    editor.putBoolean(PREF_AUTOLOGIN, false);
                    editor.commit();
                    /*Intent mStartActivity = new Intent(getBaseContext(), LoginActivity.class);
                    int mPendingIntentId = 123456;
                    PendingIntent mPendingIntent = PendingIntent.getActivity(getBaseContext(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager mgr = (AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE);
                    mgr.setExact(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);*/
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                if ((cm.message().toLowerCase().contains("ok".toLowerCase())) && (cm.message().toLowerCase().contains(unameValue.toLowerCase())&& !loggedIn)) {
                    if (mLoginPage.getUrl().toLowerCase().contains("#homescreen")) {
                        setupDrawer();
                        mLoadingCircle.setVisibility(View.INVISIBLE);
                        setHeaderDetails(cm.message());
                        setWelcomeVisible(true);
                        if (!mEdgeDay5Cur.toLowerCase().contains("Fri".toLowerCase()) && !mEdgeDay[6].toLowerCase().contains("Fri".toLowerCase())){
                            EdgeSignupActivity.showPage = false;
                            uuid = getCookie("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen", "UUID");
                            Intent intent = new Intent(getBaseContext(), EdgeSignupActivity.class);
                            startActivity(intent);
                        }
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("invalid", false);
                        editor.apply();
                        loggedIn = true;
                    }
                }
                return true;
            }

            public void onGeolocationPermissionsShowPrompt(String origin, @NonNull GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        mLoginPage.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mLoginPage.getUrl().toLowerCase().contains("login")) {
                    mLoginPage.loadUrl("javascript:(function(){" +
                            "document.getElementById('login-username').value = '" + unameValue + "';" +
                            "document.getElementById('login-password').value = '" + passwordValue + "';" +
                            "l=document.getElementById('login-btn');" +
                            "e=document.createEvent('HTMLEvents');" +
                            "e.initEvent('click',true,true);" +
                            "l.dispatchEvent(e);" +
                            "})()");
                    loggedIn = false;
                }
                if (mLoginPage.getUrl().toLowerCase().contains("register")) {
                    mLoginPage.loadUrl("javascript:(function(){" +
                            "document.getElementById('register-username').value = '" + unameValue + "';" +
                            "document.getElementById('register-password').value = '" + passwordValue + "';" +
                            "document.getElementById('register-email').value = '" + emailValue + "';" +
                            "l=document.getElementById('register-btn');" +
                            "e=document.createEvent('HTMLEvents');" +
                            "e.initEvent('click',true,true);" +
                            "l.dispatchEvent(e);" +
                            "})()");
                    loggedIn = false;
                }
                if ((mLoginPage.getUrl().toLowerCase().contains("nohsstampede")) && (loggedIn)) {
                    mLoadingCircle.setVisibility(View.INVISIBLE);
                    newUrl = "";
                }
                if (mLoginPage.getUrl().toLowerCase().contains("homescreen") && id != R.id.nav_homescreen && edgePage != null){
                    mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + edgePage);
                }


            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                webUrl = mLoginPage.getUrl();
                Log.d("!URL", webUrl);
                if ((!webUrl.toLowerCase().contains("edgetime".toLowerCase())) && (!webUrl.toLowerCase().contains("nohs".toLowerCase()))) {
                    //check to make sure web page hasn't been opened already (avoids opening 20+ chrome tabs upon one button click)
                    mLoginPage.stopLoading();
                    mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + edgePage);
                    if (!webUrl.equals(newUrl)) {
                        Uri uri = Uri.parse(webUrl);
                        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                        intentBuilder.setStartAnimations(getBaseContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                        intentBuilder.setExitAnimations(getBaseContext(), R.anim.slide_in_left, R.anim.slide_out_right);
                        intentBuilder.setToolbarColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
                        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
                        CustomTabsIntent customTabsIntent = intentBuilder.build();
                        customTabsIntent.launchUrl(context, uri);
                        newUrl = webUrl;
                    }
                }
            }
        });
    }

    private void savePreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        if (pRememValue) {
            editor.putString(PREF_UNAME, unameValue);
            editor.putString(PREF_PASSWORD, passwordValue);
        }
        editor.putBoolean(PREF_PREMEM, pRememValue);
        editor.putString(PREF_EDGE1, mEdgeDay[2]);
        editor.putString(PREF_EDGE2, mEdgeDay[3]);
        editor.putString(PREF_EDGE3, mEdgeDay[4]);
        editor.putString(PREF_EDGE4, mEdgeDay[5]);
        editor.putString(PREF_EDGE5, mEdgeDay[6]);
        editor.putString(PREF_EDGE5Cur, mEdgeDay5Cur);
        editor.putString("fullName", fullName);
        editor.apply();
    }

    public void loadPreferences() {
        mDay[2] = "Mon";
        mDay[3] = "Tue";
        mDay[4] = "Wed";
        mDay[5] = "Thu";
        mDay[6] = "Fri";
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (!calledForeign) {
            unameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
            passwordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
            pRememValue = settings.getBoolean(PREF_PREMEM, DefaultPRememValue);
        }
        edgeNotificationValue = settings.getBoolean(PREF_NOTIFYEDGE, DefaultEdgeNotificationValue);
        autoLoginValue = settings.getBoolean(PREF_AUTOLOGIN, DefaultAutologinValue);
        weeklyNotificationValue = settings.getBoolean(PREF_NOTIFYWEEKLY, DefaultWeeklyNotificationValue);
        mEdgeDay[2] = settings.getString(PREF_EDGE1, DefaultEdgeDay1Value);
        mEdgeDay[3] = settings.getString(PREF_EDGE2, DefaultEdgeDay2Value);
        mEdgeDay[4] = settings.getString(PREF_EDGE3, DefaultEdgeDay3Value);
        mEdgeDay[5] = settings.getString(PREF_EDGE4, DefaultEdgeDay4Value);
        mEdgeDay[6] = settings.getString(PREF_EDGE5, DefaultEdgeDay5Value);
        mEdgeDay5Cur = settings.getString(PREF_EDGE5Cur, DefaultEdgeDay5CurValue);
        FirstLoadValue = settings.getBoolean(PREF_FIRSTLOAD, DefaultFirstLoadVaue);
        minValue = settings.getInt(PREF_MIN, DefaultMinValue);
        LoginActivity.invalid = settings.getBoolean("invalid", false);
        fullName = settings.getString("fullName", " ");
        imageLoadOnWiFiValue = settings.getBoolean("ImageLoad", false);
        Log.d("LoadImagesOnWiFi", imageLoadOnWiFiValue + "");
        Log.d("edgeNotificationValue", edgeNotificationValue + "");
        if (weeklyNotificationValue) {
            Log.d("SettingWeeklyNotif", "SettingWeeklyNotif");
            setWeeklyNotifications();
        }
        notifyMinutes = minValue;
        if (calledForeign && mEdgeDay5Cur.toLowerCase().contains("Fri".toLowerCase())) {
            Log.d("loadPrefs", "interpreting edge data...");
            //Calendar.Friday equals 6, thursday equals 5, use this in the future with the edgeday arrays
            int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.FRIDAY && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY){
                if (mEdgeDay[dayOfWeek].toLowerCase().contains(mDay[dayOfWeek].toLowerCase())) {
                    setEdgeMessage(mEdgeDay[dayOfWeek]);
                    setEdgeNotifications(parseEdgeTitle(mEdgeDay[dayOfWeek]), parseEdgeText(mEdgeDay[dayOfWeek]), parseEdgeSession(mEdgeDay[dayOfWeek]));
                }
            } else if (dayOfWeek == Calendar.FRIDAY){
                setEdgeMessage(mEdgeDay5Cur);
                setEdgeNotifications(parseEdgeTitle(mEdgeDay5Cur), parseEdgeText(mEdgeDay5Cur), parseEdgeSession(mEdgeDay5Cur));
            }
        }
        if (FirstLoadValue && unameValue == null){
            SharedPreferences oldSettings = getSharedPreferences("preferences",
                    Context.MODE_PRIVATE);
            unameValue = oldSettings.getString(PREF_UNAME, DefaultUnameValue);
            passwordValue = oldSettings.getString(PREF_PASSWORD, DefaultPasswordValue);
            pRememValue = oldSettings.getBoolean(PREF_PREMEM, DefaultPRememValue);
            edgeNotificationValue = oldSettings.getBoolean(PREF_NOTIFYEDGE, DefaultEdgeNotificationValue);
            autoLoginValue = oldSettings.getBoolean(PREF_AUTOLOGIN, DefaultAutologinValue);
            minValue = oldSettings.getInt(PREF_MIN, DefaultMinValue);
            FirstLoadValue = false;
            SharedPreferences.Editor editor = settings.edit();
            if (pRememValue) {
                editor.putString(PREF_UNAME, unameValue);
                editor.putString(PREF_PASSWORD, passwordValue);
            }
            editor.putBoolean(PREF_NOTIFYEDGE, edgeNotificationValue);
            editor.putBoolean(PREF_PREMEM, pRememValue);
            editor.putBoolean(PREF_AUTOLOGIN, autoLoginValue);
            editor.putBoolean(PREF_FIRSTLOAD, FirstLoadValue);
            editor.putInt(PREF_MIN, minValue);
            editor.apply();
        }
    }


    private void setWeeklyNotifications() {
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.DAY_OF_WEEK, 5); // Thursday
        calendar3.set(Calendar.HOUR, 5);
        calendar3.set(Calendar.MINUTE, 0);
        calendar3.set(Calendar.SECOND, 0);
        calendar3.set(Calendar.AM_PM, Calendar.PM);
        if ((calendar3.getTimeInMillis() - System.currentTimeMillis()) > 0 && weeklyNotificationValue) {
            Intent intent3 = new Intent(this, WeeklyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                    REQUEST_CODE_WEEKLY, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent);
            }
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
        Log.d("!edgehelp c2", calendar2.getTimeInMillis() + "");
        Log.d("!edgehelp s", System.currentTimeMillis() + "");
        if (calendar2.getTimeInMillis() < System.currentTimeMillis()) {
            activateTime = (calendar2.getTimeInMillis() + ONE_DAY);
        } else {
            activateTime = calendar2.getTimeInMillis();
        }

        Log.d("edgehelptime", (activateTime - System.currentTimeMillis()) + "");
        if (edgeNotificationValue) {
            Intent intent2 = new Intent(this, EdgeClassNotifHelper.class);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this,
                    REQUEST_CODE, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, activateTime, pendingIntent2);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, activateTime, pendingIntent2);
            }
        }
    }

    private void setEdgeNotifications(String EdgeTitle, String EdgeText, int EdgeSession) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int edgeMin1 = 43 - notifyMinutes;
        int edgeMin2;
        int edgeHour2;
        if (notifyMinutes > 9) {
            int x = notifyMinutes - 9;
            edgeMin2 = 60 - x;
            edgeHour2 = 0;
        } else {
            edgeMin2 = 9 - notifyMinutes;
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
        editor.apply();
        Log.d("edgeclasstime", (calendar.getTimeInMillis() - System.currentTimeMillis()) + "");
        if ((calendar.getTimeInMillis() - System.currentTimeMillis()) > 0 && edgeNotificationValue) {
            Log.d("NotificationSet", EdgeTitle);
            Intent intent1 = new Intent(this, EdgeReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                    REQUEST_CODE_EDGE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    public static String parseEdgeTitle(String EdgeString) {
        EdgeString = EdgeString.substring(EdgeString.indexOf(">") + 1);
        EdgeString = EdgeString.substring(0, EdgeString.indexOf("</h3>"));
        return EdgeString;
    }

    public static int parseEdgeSession(@NonNull String EdgeString) {
        int session = 0;
        if (EdgeString.toLowerCase().contains("12:43")) {
            session = 1;
        }
        if (EdgeString.toLowerCase().contains("1:09")) {
            session = 2;
        }
        return session;
    }

    public static String parseEdgeText(String EdgeString) {
        EdgeString = EdgeString.substring(EdgeString.indexOf("g>") + 2);
        EdgeString = EdgeString.substring(0, EdgeString.indexOf("</"));
        return EdgeString;
    }

    private void setHeaderDetails(@NonNull String message){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = hView.findViewById(R.id.HeaderName);
        TextView nav_username = hView.findViewById(R.id.HeaderUsername);
        //ImageView nav_image = hView.findViewById(R.id.HeaderImage);
        String name;
        String username;
        String imageurl;
        name = message.substring(message.indexOf("\"name\":\"") + 8);
        name = name.substring(0, name.indexOf("\","));
        Log.d("name", name);
        nav_user.setText(name);
        fullName = name;
        mWelcome.setText("Hello, " + fullName);
        username = message.substring(message.indexOf("\"username\":\"") + 12);
        username = username.substring(0, username.indexOf("\","));
        Log.d("username", username);
        nav_username.setText(username);
        imageurl = message.substring(message.indexOf("\"profile_pic\":\"") + 15);
        imageurl = imageurl.substring(0, imageurl.indexOf("\","));
        imageurl = imageurl.replace("/","");
        Log.d("imageurl", imageurl);
        imageurl = "http://objects-us-west-1.dream.io/sfu-profiles/default.jpg";
        /*new DownloadImageTask(nav_image)
                .execute(imageurl);*/
    }

    @NonNull
    public String parseEdgeTime(@NonNull String EdgeString) {
        String time = "";
        if (EdgeString.toLowerCase().contains("12:43")) {
            time = "12:43";
        }
        if (EdgeString.toLowerCase().contains("1:09")) {
            time = "1:09";
        }
        return time;
    }

    private void setWelcomeVisible(Boolean visible){
        if (!visible) {
            mEdgeTitleConst.setVisibility(View.INVISIBLE);
            mEdgeTimeConst.setVisibility(View.INVISIBLE);
            mEdgeTextConst.setVisibility(View.INVISIBLE);
            mWelcome.setVisibility(View.INVISIBLE);
            mEdgeTitle.setVisibility(View.INVISIBLE);
            mEdgeText.setVisibility(View.INVISIBLE);
            mEdgeTime.setVisibility(View.INVISIBLE);
        }
        if (visible) {
            mEdgeTitleConst.setVisibility(View.VISIBLE);
            mEdgeTimeConst.setVisibility(View.VISIBLE);
            mEdgeTextConst.setVisibility(View.VISIBLE);
            mWelcome.setVisibility(View.VISIBLE);
            mEdgeTitle.setVisibility(View.VISIBLE);
            mEdgeText.setVisibility(View.VISIBLE);
            mEdgeTime.setVisibility(View.VISIBLE);
        }
    }

    private void setEdgeMessage(@NonNull String consoleMessage){
        mEdgeTitle.setText(parseEdgeTitle(consoleMessage));
        mEdgeText.setText(parseEdgeText(consoleMessage));
        mEdgeTime.setText(parseEdgeTime(consoleMessage));
        mWelcome.setText("Hello, " + fullName);
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Nullable
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}



