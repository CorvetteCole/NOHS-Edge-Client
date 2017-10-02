package corve.nohsedge;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;

import static corve.nohsedge.MainActivity.EdgeDay1;
import static corve.nohsedge.MainActivity.EdgeDay1Value;
import static corve.nohsedge.MainActivity.EdgeDay2;
import static corve.nohsedge.MainActivity.EdgeDay2Value;
import static corve.nohsedge.MainActivity.EdgeDay3;
import static corve.nohsedge.MainActivity.EdgeDay3Value;
import static corve.nohsedge.MainActivity.EdgeDay4;
import static corve.nohsedge.MainActivity.EdgeDay4Value;
import static corve.nohsedge.MainActivity.EdgeDay5;
import static corve.nohsedge.MainActivity.EdgeDay5Ar;
import static corve.nohsedge.MainActivity.EdgeDay5Cur;
import static corve.nohsedge.MainActivity.EdgeDay5CurValue;
import static corve.nohsedge.MainActivity.EdgeDay5Value;
import static corve.nohsedge.MainActivity.EdgeNotificationValue;
import static corve.nohsedge.MainActivity.PREF_EDGE1;
import static corve.nohsedge.MainActivity.PREF_EDGE2;
import static corve.nohsedge.MainActivity.PREF_EDGE3;
import static corve.nohsedge.MainActivity.PREF_EDGE4;
import static corve.nohsedge.MainActivity.PREF_EDGE5;
import static corve.nohsedge.MainActivity.PREF_EDGE5Cur;
import static corve.nohsedge.MainActivity.currentSet;
import static corve.nohsedge.MainActivity.uuid;

public class EdgeSignupActivity extends AppCompatActivity {

    private WebView mEdgePage;
    private final String TAG = "EdgeSignupActivity";
    private TextView mLoadingText;
    private ProgressBar mLoadingCircle;
    static boolean showPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (showPage){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else if (!showPage){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_edge_signup);
        mEdgePage = (WebView) findViewById(R.id.edgePage);
        mLoadingText = (TextView) findViewById(R.id.LoadingTextEdge);
        mLoadingCircle = (ProgressBar) findViewById(R.id.LoadingCircleEdge);
        MainActivity.calledForeign = true;
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
        mLoadingText.setVisibility(View.VISIBLE);
        mLoadingCircle.setVisibility(View.VISIBLE);
        if(showPage) {
            mEdgePage.setVisibility(View.VISIBLE);
        }
        mEdgePage.loadUrl("https://api.superfanu.com/6.0.0/gen/link_track.php?platform=Web:%20chrome&uuid=" + uuid + "&nid=305&lkey=nohsstampede-edgetime-module");
        WebSettings webSettings = mEdgePage.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
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

            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                if (cm.message().toLowerCase().contains("RetrievedEdgeClass".toLowerCase())) {
                    String consoleMessage = cm.message();
                    if (!EdgeDay1.toLowerCase().contains("mon")){
                        consoleMessage = "MonUndefined";
                    } else if (!EdgeDay2.toLowerCase().contains("tue")){
                        consoleMessage = "TueUndefined";
                    } else if (!EdgeDay3.toLowerCase().contains("wed")){
                        consoleMessage = "WedUndefined";
                    } else if (!EdgeDay4.toLowerCase().contains("thu")){
                        consoleMessage = "ThurUndefined";
                    } else if (!EdgeDay5.toLowerCase().contains("fri")){
                        consoleMessage = "FriUndefined";
                    }
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

            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        mEdgePage.setWebViewClient(new WebViewClient() {
            /*@Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mLoginPage.getUrl().contains("edgetime")) {
                    mLoadingCircle.setVisibility(View.INVISIBLE);
                    mLoginPage.setVisibility(View.VISIBLE);
                }

            }*/

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                String webUrl = mEdgePage.getUrl();
                Log.d("!URL", webUrl);
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
    private void InterpretEdgeData(String consoleMessage) {
        if (consoleMessage.toLowerCase().contains("Mon".toLowerCase())) {
            EdgeDay1 = consoleMessage;
            Log.d("Monday Edge Class", EdgeDay1);
            /*if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {

            }*/
        }
        if (consoleMessage.toLowerCase().contains("Tue".toLowerCase())) {
            EdgeDay2 = consoleMessage;
            Log.d("Tuesday Edge Class", EdgeDay2);
            /*if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                setEdgeMessage(consoleMessage);
                setEdgeNotifications(parseEdgeTitle(EdgeDay2), parseEdgeText(EdgeDay2), parseEdgeSession(EdgeDay2), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }*/
        }
        if (consoleMessage.toLowerCase().contains("Wed".toLowerCase())) {
            EdgeDay3 = consoleMessage;
            Log.d("Wednesday Edge Class", EdgeDay3);
            /*if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                setEdgeMessage(consoleMessage);
                setEdgeNotifications(parseEdgeTitle(EdgeDay3), parseEdgeText(EdgeDay3), parseEdgeSession(EdgeDay3), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }*/
        }
        if (consoleMessage.toLowerCase().contains("Thu".toLowerCase())) {
            EdgeDay4 = consoleMessage;   //Thursday
            Log.d("Thursday Edge Class", EdgeDay4);
            /*if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                setEdgeMessage(consoleMessage);
                setEdgeNotifications(parseEdgeTitle(EdgeDay4), parseEdgeText(EdgeDay4), parseEdgeSession(EdgeDay4), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            }*/
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
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                if (!showPage) {
                    savePreferences();
                    super.onBackPressed();
                }
            }
            EdgeDay5Cur = EdgeDay5Ar[0];
            Log.d("!test!", EdgeDay5Cur);
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                /*setEdgeMessage(consoleMessage);
                setEdgeNotifications(parseEdgeTitle(EdgeDay5Cur), parseEdgeText(EdgeDay5Cur), parseEdgeSession(EdgeDay5Cur), Calendar.getInstance().get(Calendar.DAY_OF_WEEK));*/
                EdgeDay5CurValue = EdgeDay5Cur;
                if (!showPage) {
                    savePreferences();
                    super.onBackPressed();
                }
            }
        }
    }
    private void getEdgeClasses() {
        int ClassElement = 0;
        while (ClassElement != 5) {
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
        editor.putString(PREF_EDGE1, EdgeDay1Value);
        editor.putString(PREF_EDGE2, EdgeDay2Value);
        editor.putString(PREF_EDGE3, EdgeDay3Value);
        editor.putString(PREF_EDGE4, EdgeDay4Value);
        editor.putString(PREF_EDGE5, EdgeDay5Value);
        editor.putString(PREF_EDGE5Cur, EdgeDay5CurValue);
        editor.apply();
    }
}
