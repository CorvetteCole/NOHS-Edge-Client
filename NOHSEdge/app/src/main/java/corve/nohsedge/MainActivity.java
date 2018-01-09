package corve.nohsedge;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.io.FileUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;

import de.cketti.mailto.EmailIntentBuilder;

import static android.view.View.VISIBLE;
import static corve.nohsedge.EdgeSignupActivity.classSelected;
import static corve.nohsedge.EdgeSignupActivity.exit;
import static corve.nohsedge.EdgeSignupActivity.loadingProgress;
import static corve.nohsedge.EdgeSignupActivity.showPage;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

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
    static boolean imageLoadOnWiFiValue;

    static Boolean FirstLoadValue = true;
    private final Boolean DefaultFirstLoadValue = true;
    static final String DefaultUnameValue = "";
    private final String DefaultPasswordValue = "";
    private final boolean DefaultPRememValue = true;
    private final boolean DefaultEdgeNotificationValue = true;
    private final boolean DefaultWeeklyNotificationValue = true;
    private final boolean DefaultAutologinValue = true;

    static String unameValue;
    static String passwordValue;
    static boolean pRememValue;
    static boolean edgeNotificationValue;
    static boolean weeklyNotificationValue;
    static boolean autoLoginValue;
    static final int DefaultMinValue = 6;
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
    static String currentPage = "homescreen";
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
    private boolean autoEdgeRan = false;
    private boolean atHome = false;
    private boolean inEdge = false, refresh = false;
    private android.support.v4.app.Fragment EdgeSignupActivityFragment = new EdgeSignupActivity();
    private android.support.v4.app.Fragment EdgeViewActivityFragment = new EdgeViewActivity();
    private FrameLayout fragmentFrame;
    private ConstraintLayout contentMain;
    static boolean inEdgeView = false, inEdgeShortcut = false;
    private FirebaseAuth mAuth;
    static final String mEdgeTimeString = "1:09";


    @Override
    public void onResume() {
        super.onResume();
            loadPreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        /*if (inEdge) {
            EdgeSignupActivity.save = true;
            EdgeSignupActivity.getEdgeClasses(mEdgePage);
        } else */if (inEdgeShortcut){
            Log.d(TAG, "I've got crippling depression");
            finish();
            inEdgeView = false;
        } else {
            saveEdgeToFirebase(mEdgeDay, mEdgeDay5Cur, unameValue.toLowerCase());
            savePreferences();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (!settings.getBoolean("KeepCache", true)) {
            FileUtils.deleteQuietly(context.getCacheDir());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if ("corve.nohsedge.view.schedule".equals(getIntent().getAction())){
            Log.d(TAG, getIntent().getAction() + "");
            inEdgeView = true;
            inEdgeShortcut = true;
        }
        if (calledForeign || inEdgeView) {
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
            fragmentFrame = findViewById(R.id.fragmentFrame);
            contentMain = findViewById(R.id.contentMain);

            if (inEdgeView) {
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle("Edge Schedule");
                contentMain.setVisibility(View.INVISIBLE);
                fragmentFrame.setVisibility(View.VISIBLE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentFrame, EdgeViewActivityFragment);
                transaction.commit();
            } else {
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
                    ShortcutInfo wEdgeCheckInShortcut = new ShortcutInfo.Builder(context, "shortcut_checkIn")
                            .setShortLabel("Edge Check in")
                            .setLongLabel("Open Edge Check in")
                            .setIcon(Icon.createWithResource(context, R.mipmap.ic_checkin))
                            .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSdHTjMmxYnt6luiBD5u6lYhqNeEctKYUlHZ1mv8NsPrWtRmOQ/viewform")))
                            .build();
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
                    shortcutManager.setDynamicShortcuts(Arrays.asList(wEdgeCheckInShortcut, wCampusShortcut, wNOHSShortcut));
                }
            }
        }
        PackageManager pm = context.getPackageManager();
        if (!isPackageInstalled("com.android.chrome", pm)){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("This app requires Chrome to work optimally");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Install",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.android.chrome"));
                            marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(marketIntent);
                        }
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!calledForeign && !inEdgeView) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        //Firebase stuff
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();



    }
    private void setupDrawer(){
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
        atHome = true;
        getSupportActionBar().setTitle("Home");
    }



    public static void clearCookies() {
        try {
            Log.d(TAG, "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } catch(StringIndexOutOfBoundsException e){
            Log.d(TAG, "clearCookies failed!" + e);
        }
    }


    public String getCookie(String siteName, String CookieName) {
        try {
            String CookieValue = null;
            CookieManager cookieManager = CookieManager.getInstance();
            String cookies = cookieManager.getCookie(siteName);
            if (cookies != null) {
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
        } catch (StringIndexOutOfBoundsException e){
            Log.d(TAG, "getCookie failed!" + e);
            return null;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //do something to sync edge classes maybe
    }

    @Override
    public void onBackPressed() {
        if (inEdge && !showPage) {
            /*if (classSelected && mEdgePage.canGoBack()) {

                mEdgePage.goBack();
                mLoadingText.setText("Loading Edge Classes...");
                classSelected = false;
            } else if (!showPage && !exit && !classSelected) {*/
                fragmentFrame.setVisibility(View.INVISIBLE);
                contentMain.setVisibility(VISIBLE);
                loadPreferences();
                inEdge = false;
                getSupportActionBar().show();
                loadingProgress = 0;
                EdgeSignupActivity.edgeLoaded = false;
                EdgeSignupActivity.doneLoading = 0;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(EdgeSignupActivityFragment);
                transaction.commit();
                saveEdgeToFirebase(mEdgeDay, mEdgeDay5Cur, unameValue.toLowerCase());

            /*} else if (exit) {
                fragmentFrame.setVisibility(View.INVISIBLE);
                contentMain.setVisibility(VISIBLE);
                loadPreferences();
                inEdge = false;
                getSupportActionBar().show();
                loadingProgress = 0;
                EdgeSignupActivity.edgeLoaded = false;
                EdgeSignupActivity.doneLoading = 0;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(EdgeSignupActivityFragment);
                transaction.commit();
            }*/
        } else if (inEdgeView && inEdgeShortcut){
            super.onBackPressed();
        } else {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            try {
                if (mLoginPage.canGoBack() && !inEdge &&  !inEdgeView) {
                    mLoginPage.goBack();
                } /*else {
                    super.onBackPressed();
                }*/
            } catch (NullPointerException e) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            if (atHome){
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                mEdgeDay[2] = settings.getString(PREF_EDGE1, DefaultEdgeDay1Value);
                mEdgeDay[3] = settings.getString(PREF_EDGE2, DefaultEdgeDay2Value);
                mEdgeDay[4] = settings.getString(PREF_EDGE3, DefaultEdgeDay3Value);
                mEdgeDay[5] = settings.getString(PREF_EDGE4, DefaultEdgeDay4Value);
                mEdgeDay[6] = settings.getString(PREF_EDGE5, DefaultEdgeDay5Value);
                mEdgeDay5Cur = settings.getString(PREF_EDGE5Cur, DefaultEdgeDay5CurValue);
                Log.d(TAG, "Reloading homescreen");
                //Log.d(TAG, mEdgeDay5Cur);
                //Calendar.Friday equals 6, thursday equals 5, use this in the future with the edgeday arrays
                int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek != Calendar.FRIDAY && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && !mEdgeDay[dayOfWeek].toLowerCase().contains("undefined") && mEdgeDay[dayOfWeek] != null && !mEdgeDay[dayOfWeek].isEmpty()){
                    Log.d(TAG, "Edge Class for today: " + mEdgeDay[dayOfWeek]);
                    if (mEdgeDay[dayOfWeek].toLowerCase().contains(mDay[dayOfWeek].toLowerCase())) {
                        setEdgeMessage(mEdgeDay[dayOfWeek]);
                        setEdgeNotifications(parseEdgeTitle(mEdgeDay[dayOfWeek]), parseEdgeText(mEdgeDay[dayOfWeek]));
                        }
                } else if (dayOfWeek == Calendar.FRIDAY && mEdgeDay5Cur.contains(mDay[dayOfWeek]) && !mEdgeDay5Cur.toLowerCase().contains("undefined")){
                    Log.d(TAG, "setting edge message for friday");
                    setEdgeMessage(mEdgeDay5Cur);
                    setEdgeNotifications(parseEdgeTitle(mEdgeDay5Cur), parseEdgeText(mEdgeDay5Cur));
                }
            } else if (!inEdge && !inEdgeView) {
                refresh = true;
                loggedIn = false;
                mLoginPage.loadUrl("about:blank");
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen");
                //mLoginPage.loadUrl(mLoginPage.getUrl());
                //mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + currentPage);
            }
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
        atHome = false;
        mLoginPage.getSettings().setBuiltInZoomControls(false);
        WebSettings webSettings = mLoginPage.getSettings();
        if (imageLoadOnWiFiValue){
            webSettings.setLoadsImagesAutomatically(onWifi());
        } else {
            webSettings.setLoadsImagesAutomatically(true);
        }

        if (id != R.id.nav_homescreen && id != R.id.nav_schedule && id != R.id.nav_signup && id != R.id.nav_gear && id != R.id.nav_settings && id != R.id.nav_feedback && id != R.id.nav_signin && mLoginPage.getVisibility() == View.INVISIBLE){
            mLoginPage.setVisibility(VISIBLE);

        }
        if ((inEdge || inEdgeView) && id != R.id.nav_gear && id != R.id.nav_signin && id != R.id.nav_settings && id != R.id.nav_feedback){
            if (inEdge && id != R.id.nav_signup){
                EdgeSignupActivity.save = true;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(EdgeSignupActivityFragment);
                transaction.commit();
            } else if (id != R.id.nav_schedule) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(EdgeViewActivityFragment);
                transaction.commit();
            }
            loadPreferences();
            fragmentFrame.setVisibility(View.INVISIBLE);
            contentMain.setVisibility(VISIBLE);
            inEdge = false;
            inEdgeView = false;
        }
        if (id == R.id.nav_schedule) {
            getSupportActionBar().setTitle("Edge Schedule");
            mLoginPage.getSettings().setJavaScriptEnabled(false);
            mLoginPage.stopLoading();
            mLoginPage.getSettings().setJavaScriptEnabled(true);
            contentMain.setVisibility(View.INVISIBLE);
            fragmentFrame.setVisibility(View.VISIBLE);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentFrame, EdgeViewActivityFragment);
            transaction.commit();
            inEdgeView = true;

            /*uuid = getCookie("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen", "UUID");
            Intent intent = new Intent(getBaseContext(), EdgeViewActivity.class);
            startActivity(intent);*/

        } else if (id == R.id.nav_signup) {
            EdgeSignupActivity.save = false;
            EdgeSignupActivity.loadingProgress = 0;
            EdgeSignupActivity.doneLoading = 0;
            EdgeSignupActivity.classesRetrieved = false;
            getSupportActionBar().setTitle("Edge Sign up");
            EdgeSignupActivity.showPage = true;
            //drawerClose = false;
            inEdge = true;
            uuid = getCookie("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen", "UUID");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                mLoginPage.getSettings().setJavaScriptEnabled(false);
                mLoginPage.stopLoading();
                mLoginPage.getSettings().setJavaScriptEnabled(true);
                //mLoginPage.loadUrl("about:blank");
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentFrame, EdgeSignupActivityFragment);
            transaction.commit();
            contentMain.setVisibility(View.INVISIBLE);
            fragmentFrame.setVisibility(View.VISIBLE);

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

        } else if (id == R.id.nav_signin) {
            drawerClose = false;
            Uri uri = Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSdHTjMmxYnt6luiBD5u6lYhqNeEctKYUlHZ1mv8NsPrWtRmOQ/viewform");
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
            intentBuilder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
            intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            CustomTabsIntent customTabsIntent = intentBuilder.build();
            customTabsIntent.launchUrl(this, uri);
        } else if (id == R.id.nav_feedback){
            Intent emailIntent = EmailIntentBuilder.from(this)
                    .to("corvettecole@gmail.com")
                    .subject("NOHS Stampede App")
                    .body("[insert feature request or bug report here]")
                    .build();
            startActivity(emailIntent);

        } else if (id == R.id.nav_profile) {
            getSupportActionBar().setTitle("Profile");
            currentPage = "profile-edit";
            if (!mLoginPage.getUrl().contains(currentPage)) {
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + currentPage);
            }
            //mLoginPage.setVisibility(VISIBLE);
            setWelcomeVisible(false);

        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(this, PreferencesActivity.class);
            startActivity(i);
            drawerClose = false;

        } else if (id == R.id.nav_homescreen){
            getSupportActionBar().setTitle("Home");
            atHome = true;
            mLoginPage.setVisibility(View.INVISIBLE);
            setWelcomeVisible(true);

        } else if (id == R.id.nav_notifications){
            getSupportActionBar().setTitle("Notifications");
            currentPage = "notifications";
            if (!mLoginPage.getUrl().contains(currentPage)) {
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + currentPage);
            }
            //mLoginPage.setVisibility(VISIBLE);
            setWelcomeVisible(false);

        } else if (id == R.id.nav_events){
            getSupportActionBar().setTitle("Events");
            //mLoginPage.setVisibility(VISIBLE);
            currentPage = "events";
            if (!mLoginPage.getUrl().contains(currentPage)) {
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + currentPage);
            }
            setWelcomeVisible(false);

        } else if (id == R.id.nav_leaderboard){
            getSupportActionBar().setTitle("Leaderboard");
            //mLoginPage.setVisibility(VISIBLE);
            currentPage = "leaderboard";
            if (!mLoginPage.getUrl().contains(currentPage)) {
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + currentPage);
            }
            setWelcomeVisible(false);

        } else if (id == R.id.nav_fancam){
            getSupportActionBar().setTitle("Fancam");
            mLoginPage.getSettings().setBuiltInZoomControls(true);
            mLoginPage.getSettings().setDisplayZoomControls(false);
            //mLoginPage.setVisibility(VISIBLE);
            currentPage = "fancam";
            if (!mLoginPage.getUrl().contains(currentPage)) {
                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + currentPage);
            }
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
            //editor.putBoolean(PREF_AUTOLOGIN, false);
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
            clearCookies();
            android.os.Process.killProcess(android.os.Process.myPid());
        }

        if (drawerClose) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private boolean onWifi() {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private boolean edgeRetrieved() {
        for (int i = 1; i < mEdgeDay.length; i++){
            Log.d("mEdgeDay[]", i + "  :  " + mEdgeDay[i]);
            if (mEdgeDay[i] != null && mEdgeDay[i].contains(mDay[i])){
                return true;
            }
        }
        return mEdgeDay5Cur != null && mEdgeDay5Cur.contains("Fri");
    }


    private void openLoginpage() {
        mLoadingCircle.setVisibility(VISIBLE);
        mLoadingText.setText("Checking login details...");
        WebSettings webSettings = mLoginPage.getSettings();
        webSettings.setLoadsImagesAutomatically(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setGeolocationEnabled(true);
        mLoginPage.clearHistory();
        clearCookies();
        mLoginPage.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                mLoadingText.setText(progress + "%");
                if (progress == 100) {
                    mLoadingText.setVisibility(View.INVISIBLE);
                    if (!currentPage.equals("homescreen") && mLoginPage.getUrl().contains("http://sites.superfanu.com/nohsstampede/6.0.0/#" + currentPage) && !atHome){
                        mLoadingCircle.setVisibility(View.INVISIBLE);
                        mLoginPage.setVisibility(VISIBLE);
                    } else if (atHome && !currentPage.equals("homescreen")){
                        mLoadingCircle.setVisibility(View.INVISIBLE);
                    }
                } else if (!currentPage.equals("fancam")){
                    if (!currentPage.equals("homescreen")){
                        mLoadingCircle.setVisibility(VISIBLE);
                    }
                    mLoadingText.setVisibility(VISIBLE);
                    mLoginPage.setVisibility(View.INVISIBLE);
                }

            }
            public boolean onConsoleMessage(@NonNull ConsoleMessage cm) {
                Log.d(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                if (cm.message().toLowerCase().contains("pass did not match".toLowerCase())) {
                    autoLoginValue = false;
                    unameValue = "";
                    passwordValue = "";
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("invalid", true);
                    editor.putString(PREF_UNAME, unameValue);
                    editor.putString(PREF_PASSWORD, passwordValue);
                    editor.commit();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                if ((cm.message().contains("\"ok\"")) && (cm.message().toLowerCase().contains(unameValue.toLowerCase())) && !loggedIn) {
                    loggedIn = true;

                    if (!refresh) {
                        setupDrawer();
                        setHeaderDetails(cm.message());
                        setWelcomeVisible(true);
                        mLoadingCircle.setVisibility(View.INVISIBLE);
                        //Firebase doesn't support purely username based login. So I append my domain name at the end to solve this
                        Log.d(TAG, "username: " + unameValue + "  password: " + passwordValue);
                        initiateFirebaseLogin(unameValue + "@coleg.tk", passwordValue);
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("invalid", false);
                        editor.apply();
                    } else {
                        mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + currentPage);
                        //mLoadingCircle.setVisibility(View.INVISIBLE);
                        refresh = false;
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
                try {
                    if (url.toLowerCase().contains("login")) {
                        mLoginPage.loadUrl("javascript:(function(){" +
                                "document.getElementById('login-username').value = '" + unameValue + "';" +
                                "document.getElementById('login-password').value = '" + passwordValue + "';" +
                                "l=document.getElementById('login-btn');" +
                                "e=document.createEvent('HTMLEvents');" +
                                "e.initEvent('click',true,true);" +
                                "l.dispatchEvent(e);" +
                                "})()");
                    }
                    if (url.toLowerCase().contains("register")) {
                        mLoginPage.loadUrl("javascript:(function(){" +
                                "document.getElementById('register-username').value = '" + unameValue + "';" +
                                "document.getElementById('register-password').value = '" + passwordValue + "';" +
                                "document.getElementById('register-email').value = '" + emailValue + "';" +
                                "l=document.getElementById('register-btn');" +
                                "e=document.createEvent('HTMLEvents');" +
                                "e.initEvent('click',true,true);" +
                                "l.dispatchEvent(e);" +
                                "})()");
                    }
                    //if (url.contains("homescreen") && id != R.id.nav_homescreen && currentPage != null && !currentPage.equals("homescreen") && !atHome) {
                    //    mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + currentPage);
                    //}
                    if (url.contains("fancam") || url.contains("leaderboard") || url.contains("notifications") || url.contains("events")) {
                        int ClassElement = 0;
                        if (!url.contains("events")) {
                            mLoginPage.loadUrl("javascript:(function(){" +
                                    "var element = document.getElementsByClassName('nav-bar-color ui-header ui-bar-inherit')['" + ClassElement + "'];" +
                                    "element.parentNode.removeChild(element);" +
                                    "})()");
                        }
                        mLoginPage.loadUrl("javascript:(function(){" +
                                "var element = document.getElementsByClassName('ad-footer ui-footer ui-bar-inherit ui-footer-fixed slideup')['" + ClassElement + "'];" +
                                "element.parentNode.removeChild(element);" +
                                "})()");
                    }
                    if (url.contains("fancam")) {
                        mLoginPage.loadUrl("javascript:document.getElementsByName('viewport')[0].setAttribute('content', 'initial-scale=1.0,maximum-scale=10.0');");
                    }
                } catch (NullPointerException e){
                    Log.d(TAG, "Something broke in onPageFinished! " + e.toString());
                }

            }

            @Override
            public void onLoadResource(WebView view, String url){
                super.onLoadResource(view, url);
                /*if (mLoginPage.getUrl().toLowerCase().contains("homescreen") && id != R.id.nav_homescreen && currentPage != null && !currentPage.equals("homescreen") && !atHome){
                    Log.d("Unwanted resource, url:", url);
                    mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + currentPage);
                }*/
                if (mLoginPage.getUrl().toLowerCase().contains("#homescreen") && loggedIn && !autoEdgeRan) {
                    autoEdgeRan = true;
                    if (!edgeRetrieved()){
                        inEdge = true;
                        EdgeSignupActivity.showPage = false;
                        uuid = getCookie("http://sites.superfanu.com/nohsstampede/6.0.0/#homescreen", "UUID");
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                            WebSettings webSettings = mLoginPage.getSettings();
                            webSettings.setJavaScriptEnabled(false);
                            mLoginPage.stopLoading();
                            //mLoginPage.loadUrl("about:blank");
                        }
                        getSupportActionBar().hide();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentFrame, EdgeSignupActivityFragment);
                        transaction.commit();
                        contentMain.setVisibility(View.INVISIBLE);
                        fragmentFrame.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
                super.shouldOverrideUrlLoading(view, request);
                webUrl = request.getUrl().toString();
                Log.d("!URL", webUrl);
                if (((!webUrl.toLowerCase().contains("edgetime".toLowerCase())) && (!webUrl.toLowerCase().contains("nohs".toLowerCase()))) || webUrl.contains("https://api.superfanu.com/revpass/landers/nohs/")) {
                    Uri uri = request.getUrl();
                    Log.d("!URI", uri.toString());
                    CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                    intentBuilder.setStartAnimations(getBaseContext(), R.anim.slide_in_right, R.anim.slide_out_left);
                    intentBuilder.setExitAnimations(getBaseContext(), R.anim.slide_in_left, R.anim.slide_out_right);
                    intentBuilder.setToolbarColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
                    intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
                    CustomTabsIntent customTabsIntent = intentBuilder.build();
                    customTabsIntent.launchUrl(context, uri);
                    return true;
                } else {
                    return false;
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
        /*editor.putString(PREF_EDGE1, mEdgeDay[2]);
        editor.putString(PREF_EDGE2, mEdgeDay[3]);
        editor.putString(PREF_EDGE3, mEdgeDay[4]);
        editor.putString(PREF_EDGE4, mEdgeDay[5]);
        editor.putString(PREF_EDGE5, mEdgeDay[6]);
        editor.putString(PREF_EDGE5Cur, mEdgeDay5Cur);*/
        editor.putString("fullName", fullName);
        editor.apply();
    }

    private void loadPreferences() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O && mLoginPage != null) {
            WebSettings webSettings = mLoginPage.getSettings();
            webSettings.setJavaScriptEnabled(true);
            //mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/#" + currentPage);
        }
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
            //Log.d(TAG, "Remember Password? " + pRememValue);
        }
        edgeNotificationValue = settings.getBoolean(PREF_NOTIFYEDGE, DefaultEdgeNotificationValue);
        autoLoginValue = settings.getBoolean(PREF_AUTOLOGIN, DefaultAutologinValue);
        Log.d(TAG, "AutoLoginValue " + autoLoginValue);
        weeklyNotificationValue = settings.getBoolean(PREF_NOTIFYWEEKLY, DefaultWeeklyNotificationValue);
        mEdgeDay[2] = settings.getString(PREF_EDGE1, DefaultEdgeDay1Value);
        mEdgeDay[3] = settings.getString(PREF_EDGE2, DefaultEdgeDay2Value);
        mEdgeDay[4] = settings.getString(PREF_EDGE3, DefaultEdgeDay3Value);
        mEdgeDay[5] = settings.getString(PREF_EDGE4, DefaultEdgeDay4Value);
        mEdgeDay[6] = settings.getString(PREF_EDGE5, DefaultEdgeDay5Value);
        mEdgeDay5Cur = settings.getString(PREF_EDGE5Cur, DefaultEdgeDay5CurValue);
        FirstLoadValue = settings.getBoolean(PREF_FIRSTLOAD, DefaultFirstLoadValue);
        minValue = settings.getInt(PREF_MIN, DefaultMinValue);
        LoginActivity.invalid = settings.getBoolean("invalid", false);
        fullName = settings.getString("fullName", " ");
        imageLoadOnWiFiValue = settings.getBoolean("ImageLoad", false);
        Log.d("LoadImagesOnWiFiOnly", imageLoadOnWiFiValue + "");
        Log.d("edgeNotificationValue", edgeNotificationValue + "");
        if (weeklyNotificationValue) {
            Log.d(TAG, "SettingWeeklyNotif");
            setWeeklyNotifications();
        }
        notifyMinutes = minValue;
        if (calledForeign) {
            Log.d("loadPrefs", "interpreting edge data...");
            Log.d(TAG, mEdgeDay5Cur);
            //Calendar.Friday equals 6, thursday equals 5, use this in the future with the edgeday arrays
            int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.FRIDAY && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY && !mEdgeDay[dayOfWeek].toLowerCase().contains("undefined") && mEdgeDay[dayOfWeek] != null && !mEdgeDay[dayOfWeek].isEmpty()){
                if (mEdgeDay[dayOfWeek].toLowerCase().contains(mDay[dayOfWeek].toLowerCase())) {
                    setEdgeMessage(mEdgeDay[dayOfWeek]);
                    setEdgeNotifications(parseEdgeTitle(mEdgeDay[dayOfWeek]), parseEdgeText(mEdgeDay[dayOfWeek]));
                }
            } else if (dayOfWeek == Calendar.FRIDAY && mEdgeDay5Cur.contains(mDay[dayOfWeek]) && !mEdgeDay5Cur.toLowerCase().contains("undefined")){
                Log.d(TAG, "setting edge message for friday");
                setEdgeMessage(mEdgeDay5Cur);
                setEdgeNotifications(parseEdgeTitle(mEdgeDay5Cur), parseEdgeText(mEdgeDay5Cur));
            }
        }
        if (FirstLoadValue && calledForeign){
            FirstLoadValue = false;
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(PREF_FIRSTLOAD, FirstLoadValue);
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

    private void setEdgeNotifications(String EdgeTitle, String EdgeText) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, 1);
        calendar.set(Calendar.MINUTE, 9);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.setTimeInMillis(calendar.getTimeInMillis() - (notifyMinutes * 60000));

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
        try {
            EdgeString = EdgeString.substring(EdgeString.indexOf(">") + 1);
            EdgeString = EdgeString.substring(0, EdgeString.indexOf("</h3>"));
            return EdgeString;
        } catch (Exception e) {
            return "unscheduled";
        }
    }

    public static String parseEdgeText(String EdgeString) {
        try {
            EdgeString = EdgeString.substring(EdgeString.indexOf("g>") + 2);
            EdgeString = EdgeString.substring(0, EdgeString.indexOf("</"));
            return EdgeString;
        } catch (Exception e){
            return "unscheduled";
        }
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
        //imageurl = "http://objects-us-west-1.dream.io/sfu-profiles/default.jpg";
        /*new DownloadImageTask(nav_image)
                .execute(imageurl);*/
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
        mEdgeTime.setText(mEdgeTimeString);
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

    private void initiateFirebaseLogin(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        //Toast.makeText(MainActivity.this, "Firebase login worked!",
                        //        Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveEdgeToFirebase(mEdgeDay, mEdgeDay5Cur, unameValue.toLowerCase());
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.v(TAG, "signInWithEmail:failure", task.getException());
                        //Toast.makeText(MainActivity.this, "Firebase login failed",
                        //        Toast.LENGTH_SHORT).show();
                        createFirebaseUser(email, password);

                    }
                }
            });
    }

    static void saveEdgeToFirebase(String[] edge, String friEdge, String userName){
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (userName.contains(".")) {
            userName = userName.replaceAll("\\.", "-");
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        if (dayOfWeek != Calendar.FRIDAY && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY){
            mDatabase.child("users").child(userName).child("Edge").child("Title").setValue(parseEdgeTitle(edge[dayOfWeek]));
            mDatabase.child("users").child(userName).child("Edge").child("Time").setValue(mEdgeTimeString);
            mDatabase.child("users").child(userName).child("Edge").child("Teacher").setValue(parseEdgeText(edge[dayOfWeek]));
        } else if (dayOfWeek == Calendar.FRIDAY){
            mDatabase.child("users").child(userName).child("Edge").child("Title").setValue(parseEdgeTitle(friEdge));
            mDatabase.child("users").child(userName).child("Edge").child("Time").setValue(mEdgeTimeString);
            mDatabase.child("users").child(userName).child("Edge").child("Teacher").setValue(parseEdgeText(friEdge));
        }
        mDatabase.child("users").child(userName).child("Edge").child("Day").setValue(mDay[dayOfWeek]);
        //mDatabase.child("users").child(userName).child("Name").setValue(fullName);
    }

    private void createFirebaseUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            //Toast.makeText(MainActivity.this, "Firebase sign up worked!",
                            //        Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.v(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Firebase sign up failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}



