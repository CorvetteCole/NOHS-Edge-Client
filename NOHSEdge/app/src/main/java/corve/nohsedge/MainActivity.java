package corve.nohsedge;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;

import static android.view.View.VISIBLE;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private static final String PREF_PREMEM = "RememPass";
    static final String PREF_NOTIFY = "Notify";
    static final String PREF_AUTOLOGIN = "AutoLogin";
    public static final String PREF_EDGE1 = "Edge 1";
    public static final String PREF_EDGE2 = "Edge 2";
    public static final String PREF_EDGE3 = "Edge 3";
    public static final String PREF_EDGE4 = "Edge 4";
    public static final String PREF_EDGE5 = "Edge 5";
    public static final String PREF_MIN = "Notify_min";
    public static final String PREF_EDGE5Cur = "Current Friday Edge Class";
    public static final String PREF_IMAGELOAD = "ImageLoad";
    private String WrongPassword = "WrongPass";
    static final String PREF_FIRSTLOAD = "FirstLoad";
    private String webUrl;
    String newUrl = "";
    public static int currentSet = 0;

    static boolean ImageLoadOnWiFiValue;

    private final Boolean DefaultFirstLoadVaue = true;
    private Boolean FirstLoadValue = true;

    private final String DefaultUnameValue = "";
    static String UnameValue;

    private final String DefaultPasswordValue = "";
    static String PasswordValue;

    private final boolean DefaultPRememValue = false;
    static boolean PRememValue;

    private final boolean DefaultNotificationValue = true;
    static boolean NotificationValue;

    private final boolean DefaultAutologinValue = false;
    static boolean AutologinValue;

    public static final int DefaultMinValue = 5;
    static int MinValue;


    public final static String DefaultEdgeDay1Value = "";
    public final static String DefaultEdgeDay2Value = "";
    public final static String DefaultEdgeDay3Value = "";
    public final static String DefaultEdgeDay4Value = "";
    public final static String DefaultEdgeDay5Value = "";
    public final static String DefaultEdgeDay5CurValue = "";
    static String EdgeDay1Value;
    static String EdgeDay2Value;
    static String EdgeDay3Value;
    static String EdgeDay4Value;
    static String EdgeDay5Value;
    static String EdgeDay5CurValue;
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
    static String EdgeDay1;
    static String EdgeDay2;
    static String EdgeDay3;
    static String EdgeDay4;
    static String EdgeDay5;
    static String EdgeDay5Cur;
    public int NotificationSet;
    static int notifyMinutes = 5;
    private boolean settingsOpen = false;
    private NumberPicker mNumberPicker;
    private TextView mNumberPickerTextView;
    static String[] EdgeDay5Ar = new String[2];
    public static int Login = 0;
    public static int Register = 0;
    public static boolean calledForeign;
    private TextView mWelcome;
    private String edgePage;
    private int id;
    private Context MainActivityContext = this;
    static String uuid;
    private TextView mEdgeTitle;
    private TextView mEdgeText;
    private TextView mEdgeTime;
    private TextView mEdgeTitleConst;
    private TextView mEdgeTextConst;
    private TextView mEdgeTimeConst;


    @Override
    public void onResume() {
        super.onResume();
        //if (!calledForeign){
            loadPreferences();
        //}
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
        if (calledForeign) {
            setupDrawer();
            mLoginPage = (WebView) findViewById(R.id.loginWebview);
            mLoadingCircle = (ProgressBar) findViewById(R.id.progressBar);
            mLoadingText = (TextView) findViewById(R.id.LoadingText);
            mWelcome = (TextView) findViewById(R.id.helloTextView);
            mEdgeTitle = (TextView) findViewById(R.id.edgeClassTitle);
            mEdgeText = (TextView) findViewById(R.id.edgeClassText);
            mEdgeTime = (TextView) findViewById(R.id.edgeClassTime);
            mEdgeTitleConst = (TextView) findViewById(R.id.edgeTitleTextView);
            mEdgeTextConst = (TextView) findViewById(R.id.edgeTextTextView);
            mEdgeTimeConst = (TextView) findViewById(R.id.edgeTimeTextView);


            NotificationSet = 0;
            //getSupportActionBar().hide();
            if (Login == 1) {
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#login");
                openLoginpage();
            }
            if (Register == 1) {
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#register");
                openLoginpage();
            }

            activateEdgeHelper();

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //window.setStatusBarColor(Color.BLACK);
            }*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
                ShortcutInfo wNOHSShortcut = new ShortcutInfo.Builder(this, "shortcut_nohs")
                        .setShortLabel("NOHS Website")
                        .setLongLabel("Open NOHS Website")
                        .setIcon(Icon.createWithResource(this, R.drawable.nohs))
                        .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.oldham.kyschools.us/nohs/")))
                        .build();
                ShortcutInfo wCampusShortcut = new ShortcutInfo.Builder(this, "shortcut_campus")
                        .setShortLabel("Campus Portal")
                        .setLongLabel("Open Campus Portal")
                        .setIcon(Icon.createWithResource(this, R.drawable.infinitecampus))
                        .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://kyede10.infinitecampus.org/campus/portal/oldham.jsp")))
                        .build();
                shortcutManager.setDynamicShortcuts(Arrays.asList(wNOHSShortcut, wCampusShortcut));
            }

        /*mLogout.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        AutologinValue = false;
                        setContentView(R.layout.activity_login);
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                        Log.d("broken lol", "im not a god... yet");
                    }
                });*/
        }
    }
    private void setupDrawer(){
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (mLoginPage.canGoBack() && !settingsOpen) {
            mLoginPage.goBack();
            if (mLoginPage.getUrl().toLowerCase().contains("edgetime".toLowerCase())) {
                Toast.makeText(this, "Click again to exit Edge", Toast.LENGTH_SHORT).show();
            }
        }  else {
            Log.d("not good", "kill me");
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();
        boolean drawerClose = true;
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        Log.d("isWiFi?", isWiFi + "");
        if (!ImageLoadOnWiFiValue){
            WebSettings webSettings = mLoginPage.getSettings();
            webSettings.setLoadsImagesAutomatically(true);
            Log.d("ImgLoadStatus", "Cell");
        }
        if (ImageLoadOnWiFiValue && isWiFi){
            WebSettings webSettings = mLoginPage.getSettings();
            webSettings.setLoadsImagesAutomatically(true);
            Log.d("ImgLoadStatus", "WiFi");
        } if (ImageLoadOnWiFiValue && !isWiFi) {
            WebSettings webSettings = mLoginPage.getSettings();
            webSettings.setLoadsImagesAutomatically(false);
            Log.d("ImgLoadStatus", "Disabled");
        }

        if (id == R.id.nav_schedule) {
            drawerClose = false;
            Intent intent = new Intent(getBaseContext(), EdgeViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signup) {
            EdgeSignupActivity.showPage = true;
            drawerClose = false;
            uuid = getCookie("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen", "UUID");
            Intent intent = new Intent(getBaseContext(), EdgeSignupActivity.class);
            startActivity(intent);
            /*mLoginPage.loadUrl("https://api.superfanu.com/6.0.0/gen/link_track.php?platform=Web:%20chrome&uuid=" + getCookie("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen", "UUID") + "&nid=305&lkey=nohsstampede-edgetime-module");
            mLoginPage.setVisibility(View.VISIBLE);
            mEdgeMessage.setVisibility(View.INVISIBLE);
            mWelcome.setVisibility(View.INVISIBLE);*/
        } else if (id == R.id.nav_profile) {
            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#profile-edit");
            edgePage = "profile-edit";
            mLoginPage.setVisibility(VISIBLE);
            setWelcomeVisible(false);
        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(this, PreferencesActivity.class);
            startActivity(i);
            /*Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(intent);*/
            drawerClose = false;
        } else if (id == R.id.nav_homescreen){
            mLoginPage.setVisibility(View.INVISIBLE);
            setWelcomeVisible(true);
        } else if (id == R.id.nav_notifications){
            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#notifications");
            edgePage = "notifications";
            mLoginPage.setVisibility(VISIBLE);
            setWelcomeVisible(false);
        } else if (id == R.id.nav_events){
            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#events");
            mLoginPage.setVisibility(VISIBLE);
            edgePage = "events";
            setWelcomeVisible(false);
        } else if (id == R.id.nav_leaderboard){
            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#leaderboard");
            mLoginPage.setVisibility(VISIBLE);
            edgePage = "leaderboard";
            setWelcomeVisible(false);
        } else if (id == R.id.nav_fancam){
            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#fancam");
            mLoginPage.setVisibility(VISIBLE);
            edgePage = "fancam";
            setWelcomeVisible(false);
        } else if (id == R.id.nav_logout){
            AutologinValue = false;
            PRememValue = false;
            UnameValue = "";
            PasswordValue = "";
            EdgeDay1 = "";
            EdgeDay2 = "";
            EdgeDay3 = "";
            EdgeDay4 = "";
            EdgeDay5 = "";
            EdgeDay5Cur = "";
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PREF_EDGE1, "");
            editor.putString(PREF_EDGE2, "");
            editor.putString(PREF_EDGE3, "");
            editor.putString(PREF_EDGE4, "");
            editor.putString(PREF_EDGE5, "");
            editor.putString(PREF_EDGE5Cur, "");
            editor.putInt(PREF_MIN, 5);
            editor.commit();
            setContentView(R.layout.activity_login);
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            drawerClose = false;
        }
        if (drawerClose) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    private void openLoginpage() {
        //mLoginPage.setVisibility(VISIBLE);
        mLoadingCircle.setVisibility(VISIBLE);
        mLoadingText.setText("Checking login details...");
        //mLoadingText.setVisibility(View.VISIBLE);
        WebSettings webSettings = mLoginPage.getSettings();
        webSettings.setLoadsImagesAutomatically(false);
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
            public void onProgressChanged(WebView view, int progress) {
                mLoadingText.setText(progress + "%");
                if (progress == 100) {
                    mLoadingText.setVisibility(View.INVISIBLE);
                } else {
                    mLoadingText.setVisibility(VISIBLE);
                    mLoadingCircle.setVisibility(VISIBLE);
                }

            }

            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                if (cm.message().toLowerCase().contains(WrongPassword.toLowerCase())) {
                    AutologinValue = false;
                    PRememValue = false;
                    UnameValue = "";
                    PasswordValue = "";
                    LoginActivity.invalid = true;
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
                if ((cm.message().toLowerCase().contains("ok".toLowerCase())) && (cm.message().toLowerCase().contains(UnameValue.toLowerCase())&& x == 1)) {
                    if (mLoginPage.getUrl().toLowerCase().contains("#homescreen")) {
                        mLoadingCircle.setVisibility(View.INVISIBLE);
                        setHeaderDetails(cm.message());
                        setWelcomeVisible(true);
                        if (!EdgeDay5Value.toLowerCase().contains("Fri".toLowerCase())){
                            EdgeSignupActivity.showPage = false;
                            uuid = getCookie("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen", "UUID");
                            Intent intent = new Intent(getBaseContext(), EdgeSignupActivity.class);
                            startActivity(intent);
                        }

                        // /mLoginPage.setVisibility(View.VISIBLE);

                        //THE KEY IS BELOW. THE KEY I TELL YOU!
                        //mLoginPage.loadUrl("https://api.superfanu.com/6.0.0/gen/link_track.php?platform=Web:%20chrome&uuid=" + getCookie("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen", "UUID") + "&nid=305&lkey=nohsstampede-edgetime-module");
                        x = 2;
                    }
                }
                if (cm.message().toLowerCase().contains("RetrievedEdgeClass".toLowerCase())) {
                    InterpretEdgeData(cm.message());
                }
                if (cm.message().toLowerCase().contains("post_queue")/* && (cm.lineNumber() == 419)*/) {
                    mLoadingCircle.setVisibility(View.INVISIBLE);
                    mLoginPage.setVisibility(VISIBLE);
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
                    //mLoginPage.setVisibility(View.VISIBLE);
                    newUrl = "";
                }
                if ((mLoginPage.getUrl().contains("edgetime")) && (x == 2)) {
                    mLoadingCircle.setVisibility(View.INVISIBLE);
                    mLoginPage.setVisibility(VISIBLE);
                    //mLoadingText.setVisibility(View.INVISIBLE);
                }
                if (mLoginPage.getUrl().contains("homescreen") && id != R.id.nav_homescreen && edgePage != null){
                    mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + edgePage);
                }

            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                webUrl = mLoginPage.getUrl();
                Log.d("!URL", webUrl);
                if ((webUrl.toLowerCase().contains("edgetime".toLowerCase())) && (x == 2)) {
                    edgeUrl = webUrl;
                    mLoadingCircle.setVisibility(VISIBLE);
                    //mLoginPage.setVisibility(View.INVISIBLE);
                    mLoadingText.setText("Retrieving Edge Classes...");
                    //mLoadingText.setVisibility(View.VISIBLE);
                }
                if ((!webUrl.toLowerCase().contains("edgetime".toLowerCase())) && (!webUrl.toLowerCase().contains("nohs".toLowerCase()))) {
                    //check to make sure web page hasn't been opened already (avoids opening 20+ chrome tabs upon one button click)
                    mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + edgePage);
                    if (!webUrl.equals(newUrl)) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                        startActivity(browserIntent);
                        newUrl = webUrl;
                    }
                }

                //if (webUrl.toLowerCase().contains("#login".toLowerCase())){
                //    mLoginPage.setVisibility(View.INVISIBLE);
                //    mLoadingCircle.setVisibility(View.VISIBLE);
                //}

            }
        });
    }

    private void savePreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        EdgeDay1Value = EdgeDay1;
        EdgeDay2Value = EdgeDay2;
        EdgeDay3Value = EdgeDay3;
        EdgeDay4Value = EdgeDay4;
        EdgeDay5Value = EdgeDay5;
        EdgeDay5CurValue = EdgeDay5Cur;
        if (PRememValue) {
            editor.putString(PREF_UNAME, UnameValue);
            editor.putString(PREF_PASSWORD, PasswordValue);
        }
        //editor.putBoolean(PREF_NOTIFY, NotificationValue);
        editor.putBoolean(PREF_PREMEM, PRememValue);
        //editor.putBoolean(PREF_AUTOLOGIN, AutologinValue);
        editor.putString(PREF_EDGE1, EdgeDay1Value);
        editor.putString(PREF_EDGE2, EdgeDay2Value);
        editor.putString(PREF_EDGE3, EdgeDay3Value);
        editor.putString(PREF_EDGE4, EdgeDay4Value);
        editor.putString(PREF_EDGE5, EdgeDay5Value);
        editor.putString(PREF_EDGE5Cur, EdgeDay5CurValue);
        //editor.putInt(PREF_MIN, MinValue);
        editor.apply();
    }

    public void loadPreferences() {

        /*SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);*/
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        // Get value
        if (!calledForeign) {
            UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
            PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
            PRememValue = settings.getBoolean(PREF_PREMEM, DefaultPRememValue);
        }
        NotificationValue = settings.getBoolean(PREF_NOTIFY, DefaultNotificationValue);
        AutologinValue = settings.getBoolean(PREF_AUTOLOGIN, DefaultAutologinValue);
        EdgeDay1Value = settings.getString(PREF_EDGE1, DefaultEdgeDay1Value);
        EdgeDay2Value = settings.getString(PREF_EDGE2, DefaultEdgeDay2Value);
        EdgeDay3Value = settings.getString(PREF_EDGE3, DefaultEdgeDay3Value);
        EdgeDay4Value = settings.getString(PREF_EDGE4, DefaultEdgeDay4Value);
        EdgeDay5Value = settings.getString(PREF_EDGE5, DefaultEdgeDay5Value);
        EdgeDay5CurValue = settings.getString(PREF_EDGE5Cur, DefaultEdgeDay5CurValue);
        //FirstLoadValue = settings.getBoolean(PREF_FIRSTLOAD, DefaultFirstLoadVaue);
        MinValue = settings.getInt(PREF_MIN, DefaultMinValue);
        ImageLoadOnWiFiValue = settings.getBoolean("ImageLoad", false);
        Log.d("LoadImagesOnWiFi", ImageLoadOnWiFiValue + "");
        Log.d("NotificationValue", NotificationValue + "");
        if (NotificationValue) {
            Log.d("Setting notification", " ");
            setWeeklyNotifications();
        }
        EdgeDay1 = EdgeDay1Value;
        EdgeDay2 = EdgeDay2Value;
        EdgeDay3 = EdgeDay3Value;
        EdgeDay4 = EdgeDay4Value;
        EdgeDay5 = EdgeDay5Value;
        EdgeDay5Cur = EdgeDay5CurValue;
        notifyMinutes = MinValue;
        if (calledForeign) {
            InterpretEdgeData(EdgeDay1);
            InterpretEdgeData(EdgeDay2);
            InterpretEdgeData(EdgeDay3);
            InterpretEdgeData(EdgeDay4);
            InterpretEdgeData(EdgeDay5);
        }
        /*if (FirstLoadValue){
            SharedPreferences oldSettings = getSharedPreferences("preferences",
                    Context.MODE_PRIVATE);
            UnameValue = oldSettings.getString(PREF_UNAME, DefaultUnameValue);
            PasswordValue = oldSettings.getString(PREF_PASSWORD, DefaultPasswordValue);
            PRememValue = oldSettings.getBoolean(PREF_PREMEM, DefaultPRememValue);
            NotificationValue = oldSettings.getBoolean(PREF_NOTIFY, DefaultNotificationValue);
            AutologinValue = oldSettings.getBoolean(PREF_AUTOLOGIN, DefaultAutologinValue);
            MinValue = oldSettings.getInt(PREF_MIN, DefaultMinValue);
            FirstLoadValue = false;
            SharedPreferences.Editor editor = settings.edit();
            if (PRememValue) {
                editor.putString(PREF_UNAME, UnameValue);
                editor.putString(PREF_PASSWORD, PasswordValue);
            }
            editor.putBoolean(PREF_NOTIFY, NotificationValue);
            editor.putBoolean(PREF_PREMEM, PRememValue);
            editor.putBoolean(PREF_AUTOLOGIN, AutologinValue);
            editor.putBoolean(PREF_FIRSTLOAD, FirstLoadValue);
            editor.putInt(PREF_MIN, MinValue);
            editor.apply();
        }*/
        currentSet = 0;
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent);
            }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
        }
        /*JobInfo.Builder builder = new JobInfo.Builder(REQUEST_CODE, component)
                .setPersisted(true)
                .setMinimumLatency((calendar2.getTimeInMillis()) - System.currentTimeMillis())
                .setOverrideDeadline(ONE_MIN * 180);

        JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());*/
    }

    private void setEdgeNotifications(String EdgeTitle, String EdgeText, int EdgeSession, int DayofWeek) {
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
        if ((calendar.getTimeInMillis() - System.currentTimeMillis()) > 0 && NotificationValue) {
            Intent intent1 = new Intent(this, Receiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                    REQUEST_CODE_EDGE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

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

    public static String parseEdgeTitle(String EdgeString) {
        EdgeString = EdgeString.substring(EdgeString.indexOf(">") + 1);
        EdgeString = EdgeString.substring(0, EdgeString.indexOf("</h3>"));
        return EdgeString;
    }

    public static int parseEdgeSession(String EdgeString) {
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

    private void setHeaderDetails(String message){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        mWelcome.setText("Hello, " + name);
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

     private void InterpretEdgeData(String consoleMessage) {
        if (consoleMessage.toLowerCase().contains("Mon".toLowerCase())) {
            EdgeDay1 = consoleMessage;
            Log.d("Monday Edge Class", EdgeDay1);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                setEdgeMessage(consoleMessage);
                setEdgeNotifications(parseEdgeTitle(EdgeDay1), parseEdgeText(EdgeDay1), parseEdgeSession(EdgeDay1), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Tue".toLowerCase())) {
            EdgeDay2 = consoleMessage;
            Log.d("Tuesday Edge Class", EdgeDay2);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                setEdgeMessage(consoleMessage);
                setEdgeNotifications(parseEdgeTitle(EdgeDay2), parseEdgeText(EdgeDay2), parseEdgeSession(EdgeDay2), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Wed".toLowerCase())) {
            EdgeDay3 = consoleMessage;
            Log.d("Wednesday Edge Class", EdgeDay3);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                setEdgeMessage(consoleMessage);
                setEdgeNotifications(parseEdgeTitle(EdgeDay3), parseEdgeText(EdgeDay3), parseEdgeSession(EdgeDay3), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }
        }
        if (consoleMessage.toLowerCase().contains("Thu".toLowerCase())) {
            EdgeDay4 = consoleMessage;   //Thursday
            Log.d("Thursday Edge Class", EdgeDay4);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                setEdgeMessage(consoleMessage);
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
            if (!consoleMessage.contains(EdgeDay5Ar[0]) && consoleMessage != null) {
                EdgeDay5Ar[1] = consoleMessage;
            }
            EdgeDay5Cur = EdgeDay5Ar[0];
            Log.d("Cur Friday Edge Class", EdgeDay5Cur);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                setEdgeMessage(consoleMessage);
                setEdgeNotifications(parseEdgeTitle(EdgeDay5Cur), parseEdgeText(EdgeDay5Cur), parseEdgeSession(EdgeDay5Cur), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                EdgeDay5CurValue = EdgeDay5Cur;
                //savePreferences();
            }
        }
    }
    public String parseEdgeTime(String EdgeString) {
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

    private void setEdgeMessage(String consoleMessage){
        mEdgeTitle.setText(parseEdgeTitle(consoleMessage));
        mEdgeText.setText(parseEdgeText(consoleMessage));
        mEdgeTime.setText(parseEdgeTime(consoleMessage));
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

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



