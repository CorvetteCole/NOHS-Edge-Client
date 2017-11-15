package corve.nohsedge;

import android.content.SharedPreferences;
import android.net.http.SslError;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Calendar;
import static corve.nohsedge.MainActivity.PREF_EDGE1;
import static corve.nohsedge.MainActivity.PREF_EDGE2;
import static corve.nohsedge.MainActivity.PREF_EDGE3;
import static corve.nohsedge.MainActivity.PREF_EDGE4;
import static corve.nohsedge.MainActivity.PREF_EDGE5;
import static corve.nohsedge.MainActivity.PREF_EDGE5Cur;
import static corve.nohsedge.MainActivity.uuid;

public class EdgeSignupActivity extends AppCompatActivity {

    private WebView mEdgePage;
    private final String TAG = "EdgeSignupActivity";
    private TextView mLoadingText;
    private ProgressBar mLoadingCircle;
    static boolean showPage = false;
    @NonNull
    private String[] edgeDay = new String[7];
    private String[] edgeDayFriday = new String[2];
    private String edgeDay5Cur;


    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edge_signup);
        mEdgePage = findViewById(R.id.edgePage);
        mLoadingText = findViewById(R.id.LoadingTextEdge);
        mLoadingCircle = findViewById(R.id.LoadingCircleEdge);
        Button mSkipButton = findViewById(R.id.skipButton);
        ConstraintLayout mSkipLayout = findViewById(R.id.skipLayout);

        MainActivity.calledForeign = true;
        if (showPage){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mSkipLayout.setVisibility(View.INVISIBLE);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().hide();
            mSkipLayout.setVisibility(View.VISIBLE);
        }
        mSkipButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        mEdgePage.stopLoading();
                        mEdgePage.clearHistory();
                        mEdgePage.clearCache(true);
                        WebView obj = mEdgePage;
                        obj.clearCache(true);
                        finish();
                    }
                });
        openEdgepage();
    }
    @Override
    public void onBackPressed() {
        if (mEdgePage.canGoBack()) {
            mEdgePage.goBack();
        }  else {
            showPage = false;
            getEdgeClasses();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showPage = false;
        getEdgeClasses();
        return(super.onOptionsItemSelected(item));
    }

    private void openEdgepage() {
        edgeDayFriday[0] = "notSet";
        mEdgePage.clearHistory();
        mEdgePage.clearCache(true);
        //clearCookies(this);
        WebView obj = mEdgePage;
        obj.clearCache(true);
        mLoadingText.setVisibility(View.VISIBLE);
        mLoadingCircle.setVisibility(View.VISIBLE);
        if(showPage) {
            mEdgePage.setVisibility(View.VISIBLE);
        }
        mEdgePage.loadUrl("http://api.superfanu.com/6.0.0/gen/link_track.php?platform=Web:%20chrome&uuid=" + uuid + "&nid=305&lkey=nohsstampede-edgetime-module");
        WebSettings webSettings = mEdgePage.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(false);
        mEdgePage.clearHistory();

        mEdgePage.setWebChromeClient(new WebChromeClient() {
            /*public void onProgressChanged(WebView view, int progress) {
                mLoadingText.setText(progress + "%");
                if (progress == 100) {
                    mLoadingText.setVisibility(View.INVISIBLE);
                } else {
                    mLoadingText.setVisibility(View.VISIBLE);
                    mLoadingCircle.setVisibility(View.VISIBLE);
                }

            }*/

            public boolean onConsoleMessage(@NonNull ConsoleMessage cm) {
                Log.d(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                if (cm.message().toLowerCase().contains("RetrievedEdgeClass".toLowerCase())) {
                    String consoleMessage = cm.message();
                    InterpretEdgeData(consoleMessage);
                }
                if (cm.message().toLowerCase().contains("post_queue")/* && (cm.lineNumber() == 419)*/) {
                    if (showPage) {
                        mEdgePage.setVisibility(View.VISIBLE);
                        mLoadingCircle.setVisibility(View.INVISIBLE);
                        mLoadingText.setVisibility(View.INVISIBLE);
                    }
                    getEdgeClasses();
                }
                return true;
            }

            public void onGeolocationPermissionsShowPrompt(String origin, @NonNull GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        mEdgePage.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                String webUrl = mEdgePage.getUrl();
                Log.d("!EdgeURL", webUrl);
                if (webUrl.toLowerCase().contains("edgetime".toLowerCase())) {
                    if (showPage) {
                        mLoadingCircle.setVisibility(View.VISIBLE);
                        mEdgePage.setVisibility(View.INVISIBLE);
                        mLoadingText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
    private void InterpretEdgeData(@NonNull String consoleMessage) {
        if (consoleMessage.toLowerCase().contains("Mon".toLowerCase())) {
            edgeDay[2] = consoleMessage;
            Log.d("Monday Edge Class", edgeDay[2]);
        }
        if (consoleMessage.toLowerCase().contains("Tue".toLowerCase())) {
            edgeDay[3] = consoleMessage;
            Log.d("Tuesday Edge Class", edgeDay[3]);
        }
        if (consoleMessage.toLowerCase().contains("Wed".toLowerCase())) {
            edgeDay[4] = consoleMessage;
            Log.d("Wednesday Edge Class", edgeDay[4]);

        }
        if (consoleMessage.toLowerCase().contains("Thu".toLowerCase())) {
            edgeDay[5] = consoleMessage;   //Thursday
            Log.d("Thursday Edge Class", edgeDay[5]);
        }
        if (consoleMessage.toLowerCase().contains("Fri".toLowerCase())) {
            Log.d(TAG, consoleMessage);
            if (edgeDayFriday[0].equals("notSet")){
                edgeDayFriday[0] = consoleMessage;
                Log.d(TAG, "Friday Array 0 set");
            }
            if (!edgeDayFriday[0].equals(consoleMessage) && !isSameWeek(edgeDayFriday[0], consoleMessage)){
                Log.d(TAG, "Friday Array 1 set");
                edgeDayFriday[1] = consoleMessage;
                if (!showPage) {
                    savePreferences();
                    finish();
                }
                edgeDay[6] = edgeDayFriday[1];
                edgeDay5Cur = edgeDayFriday[0];
                Log.d("Current Friday Edge", edgeDay5Cur);
                Log.d("Next Friday Edge", edgeDay[6]);
            } else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY){
                edgeDay5Cur = edgeDayFriday[0];
                if (!showPage) {
                    savePreferences();
                    finish();
                }
            } else if (isAfterEdgeClasses() && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
                edgeDay[6] = edgeDayFriday[0];
                Log.d(TAG, "is after edge classes, only next is set");
                if (!showPage) {
                    savePreferences();
                    finish();
                }
            }
        }
    }
    static boolean isAfterEdgeClasses(){
        boolean after = false;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, 1);
        calendar.set(Calendar.MINUTE, 9);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.AM_PM, Calendar.PM);
        Log.d("edgeclasstime", (calendar.getTimeInMillis() - System.currentTimeMillis()) + "");
        if (calendar.getTimeInMillis() - System.currentTimeMillis() > 0){
            after = false;
        } else if (calendar.getTimeInMillis() - System.currentTimeMillis() < 0){
            after = true;
        }
        return after;
    }

    private void getEdgeClasses() {
        int ClassElement = 0;
        while (ClassElement != 6) {
            mEdgePage.loadUrl("javascript:(function(){" +
                    "if (document.getElementsByClassName('class user-in-class')['" + ClassElement + "'] != undefined){" +
                    "console.log('RetrievedEdgeClass' + document.getElementsByClassName('class user-in-class')['" + ClassElement + "'].innerHTML);}" +
                    "})()");
            mEdgePage.loadUrl("javascript:(function(){" +
                    "if (document.getElementsByClassName('class user-in-class')['" + ClassElement + "'] == undefined){" +
                    "console.log('RetrievedEdgeClass' + 'Undefined');}" +
                    "})()");
            ClassElement++;
        }
    }
    private boolean isSameWeek (@NonNull String edge1, @NonNull String edge2){
        String date1 = edge1.substring(edge1.indexOf("\"datetime\">") + 16);
        date1 = date1.substring(date1.indexOf("/") + 1);
        if (edge1.contains("12:")) {
            date1 = date1.substring(0, (date1.indexOf("pm") - 6));
        } else {
            date1 = date1.substring(0, (date1.indexOf("pm") - 5));
        }
        Log.d("edge1 date", date1);
        String date2 = edge2.substring(edge2.indexOf("\"datetime\">") + 16);
        date2 = date2.substring(date2.indexOf("/") + 1);
        if (edge2.contains("12:")) {
            date2 = date2.substring(0, (date2.indexOf("pm") - 6));
        } else {
            date2 = date2.substring(0, (date2.indexOf("pm") - 5));
        }
        Log.d("edge2 date", date2);
        return Math.abs(Integer.parseInt(date1) - Integer.parseInt(date2)) < 7;
    }

    private void savePreferences() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        if (edgeDay5Cur != null) {
            editor.putString(PREF_EDGE5Cur, edgeDay5Cur);
        }
        editor.putString(PREF_EDGE1, edgeDay[2]);
        editor.putString(PREF_EDGE2, edgeDay[3]);
        editor.putString(PREF_EDGE3, edgeDay[4]);
        editor.putString(PREF_EDGE4, edgeDay[5]);
        editor.putString(PREF_EDGE5, edgeDay[6]);

        editor.apply();
    }
}
