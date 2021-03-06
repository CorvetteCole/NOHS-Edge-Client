package corve.nohsedge;

import android.app.SearchManager;
import android.content.res.Resources;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Calendar;

import static android.view.View.VISIBLE;
import static corve.nohsedge.MainActivity.PREF_EDGE1;
import static corve.nohsedge.MainActivity.PREF_EDGE2;
import static corve.nohsedge.MainActivity.PREF_EDGE3;
import static corve.nohsedge.MainActivity.PREF_EDGE4;
import static corve.nohsedge.MainActivity.PREF_EDGE5;
import static corve.nohsedge.MainActivity.PREF_EDGE5Cur;
import static corve.nohsedge.MainActivity.uuid;

public class EdgeSignupActivity extends Fragment {

    private WebView mEdgePage;
    private final String TAG = "EdgeSignupActivity";
    private TextView mEdgeLoadingText;
    private ProgressBar mEdgeLoadingCircle;
    static boolean save = false;
    static boolean showPage = false;
    @NonNull
    private String[] edgeDay = new String[7];
    private String[] edgeDayFriday = new String[2];
    private String edgeDay5Cur = "";
    static boolean classSelected = false, edgeLoaded = false, exit = false;
    static boolean classesRetrieved = false;
    static int doneLoading = 0;
    Button mSkipButton;
    ConstraintLayout mSkipLayout;
    static int loadingProgress = 0;
    private boolean backPressed = false;
    private SharedPreferences settings;


    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "Fragment paused... saving edge classes");
        savePreferences();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mEdgePage.loadUrl("about:blank");
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.findItem(R.id.action_refresh).setVisible(false);
        inflater.inflate(R.menu.search, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint("Search Edge");
        searchView.setMaxWidth(dpToPx(230));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                Log.d(TAG, "User requests search of: " + query);
                mEdgePage.findAllAsync(query);
                /*if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }*/
                //myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "User request changed to: " + s);
                mEdgePage.findAllAsync(s);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            doneLoading = 0;
            edgeLoaded = false;
            classSelected = false;
            mEdgeLoadingCircle.setVisibility(VISIBLE);
            mEdgeLoadingText.setText("Getting Edge Classes...");
            mEdgeLoadingText.setVisibility(VISIBLE);
            mEdgePage.setVisibility(View.INVISIBLE);
            EdgeSignupActivity.loadingProgress = 0;
            openEdgepage();
        }
        if (id == R.id.action_next){
            mEdgePage.findNext(true);
        } else if (id == R.id.action_last){
            mEdgePage.findNext(false);
        }
        return true;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        View RootView = inflater.inflate(R.layout.activity_edge_signup, container, false);
        mEdgePage = RootView.findViewById(R.id.edgePage);
        mEdgeLoadingText = RootView.findViewById(R.id.LoadingTextEdge);
        mEdgeLoadingCircle = RootView.findViewById(R.id.LoadingCircleEdge);
        mSkipButton = RootView.findViewById(R.id.skipButton);
        mSkipLayout = RootView.findViewById(R.id.skipLayout);
        setHasOptionsMenu(true);
        MainActivity.calledForeign = true;
        if (showPage){
            mSkipLayout.setVisibility(View.INVISIBLE);
        } else {

            mSkipLayout.setVisibility(VISIBLE);
        }
        mSkipButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        exit = true;
                        getEdgeClasses(mEdgePage);
                    }
                });
        openEdgepage();
        return RootView;
    }

    private void openEdgepage() {
        edgeDayFriday[0] = "notSet";
        edgeDayFriday[1] = "notSet";
        Log.d(TAG, "Clearing edgeDay Array");
        classesRetrieved = false;
        for (int i = 0; i < edgeDay.length; i++){
            edgeDay[i] = "";
        }
        mEdgePage.clearHistory();
        mEdgeLoadingText.setVisibility(VISIBLE);
        mEdgeLoadingCircle.setVisibility(VISIBLE);
        mEdgePage.loadUrl("http://api.superfanu.com/6.0.0/gen/link_track.php?platform=Web:%20chrome&uuid=" + uuid + "&nid=305&lkey=nohsstampede-edgetime-module");
        WebSettings webSettings = mEdgePage.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(false);

        mEdgePage.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    WebView webView = (WebView) v;

                    switch(keyCode)
                    {
                        case KeyEvent.KEYCODE_BACK:
                            if (!classSelected){
                                getActivity().onBackPressed();
                            } else if (webView.canGoBack() && classSelected)
                            {
                                webView.goBack();
                                mEdgeLoadingText.setText("Loading Edge Classes...");
                                classSelected = false;
                                getEdgeClasses(mEdgePage);
                            }
                            break;
                    }
                }

                return false;
            }
        });

        mEdgePage.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                /*time = System.currentTimeMillis();
                if (previousTime == 0){
                    previousTime = time;
                }
                timeElapsed = time - previousTime;
                if (timeElapsed == 100) {
                    previousTime = time;
                    loadingProgress++;
                    mEdgeLoadingText.setText("Getting Edge Classes... " + loadingProgress + "%");
                }*/
                /*if (progress < loadingProgress) {
                    loadingProgress = (int)progress;
                } else {
                    loadingProgress = (progress - (100 - (int)((double)100/doneLoading)));
                }*/
                updateLoading();
            }

            public boolean onConsoleMessage(ConsoleMessage cm) {

                Log.d(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                if (cm.message().toLowerCase().contains("RetrievedEdgeClass".toLowerCase())) {
                    String consoleMessage = cm.message();
                    InterpretEdgeData(consoleMessage);
                }
                if (cm.message().contains("object Object") && edgeLoaded){
                    classSelected = true;
                }
                return true;
            }
        });
        mEdgePage.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                doneLoading++;
                loadingProgress = (int)(100 *((double)doneLoading/3));
                updateLoading();
                if (doneLoading == 2) {
                    runThread();
                }
                if (doneLoading == 3) {
                    Log.d(TAG, "Done loading");
                    if (showPage) {
                        //mEdgePage.loadUrl("javascript:(function(){" +
                        //        "var element = document.getElementsByClassName('ui-header ui-bar-inherit ui-header-fixed slidedown')['" + 0 + "'];" +
                        //        "element.parentNode.removeChild(element);" +
                        //        "})()");
                        mEdgePage.setVisibility(VISIBLE);
                        mEdgeLoadingCircle.setVisibility(View.INVISIBLE);
                        mEdgeLoadingText.setVisibility(View.INVISIBLE);
                    } /*else if (!classesRetrieved){
                        getEdgeClasses(mEdgePage);
                    }*/
                    edgeLoaded = true;
                    getEdgeClasses(mEdgePage);

                }
                if (doneLoading == 4){
                    doneLoading = 0;
                }

            }

            /*@Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                String webUrl = mEdgePage.getUrl();
                Log.d("!EdgeURL", webUrl);
                if (webUrl.toLowerCase().contains("edgetime".toLowerCase())) {
                    if (showPage) {
                        mEdgeLoadingCircle.setVisibility(View.VISIBLE);
                        mEdgePage.setVisibility(View.INVISIBLE);
                        mEdgeLoadingText.setVisibility(View.VISIBLE);
                    }
                }
            }*/
        });
    }
    private void InterpretEdgeData(@NonNull String consoleMessage) {
        if (consoleMessage.toLowerCase().contains("Mon".toLowerCase())) {
            edgeDay[2] = consoleMessage;
            classesRetrieved = true;
            Log.d("Monday Edge Class", edgeDay[2]);
        }
        if (consoleMessage.toLowerCase().contains("Tue".toLowerCase())) {
            edgeDay[3] = consoleMessage;
            classesRetrieved = true;
            Log.d("Tuesday Edge Class", edgeDay[3]);
        }
        if (consoleMessage.toLowerCase().contains("Wed".toLowerCase())) {
            edgeDay[4] = consoleMessage;
            classesRetrieved = true;
            Log.d("Wednesday Edge Class", edgeDay[4]);

        }
        if (consoleMessage.toLowerCase().contains("Thu".toLowerCase())) {
            edgeDay[5] = consoleMessage;   //Thursday
            classesRetrieved = true;
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
                classesRetrieved = true;
                edgeDay[6] = edgeDayFriday[1];
                edgeDay5Cur = edgeDayFriday[0];
                Log.d("Current Friday Edge", edgeDay5Cur);
                Log.d("Next Friday Edge", edgeDay[6]);
            } else if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY){
                edgeDay5Cur = edgeDayFriday[0];
                classesRetrieved = true;
            } else if (isAfterEdgeClasses() && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
                edgeDay[6] = edgeDayFriday[0];
                Log.d(TAG, "is after edge classes, only next is set");
                classesRetrieved = true;
            }
        }
        Log.d("classesRetrieved", classesRetrieved + "");
        if (consoleMessage.toLowerCase().contains("undefined")){

            if (classesRetrieved) {
                mEdgeLoadingText.setText("Loading Edge Class...");
                if (save){
                    savePreferences();
                    mEdgePage.getSettings().setJavaScriptEnabled(false);
                    mEdgePage.stopLoading();
                    mEdgePage.getSettings().setJavaScriptEnabled(true);
                }
                if (!showPage && !backPressed) {
                    savePreferences();
                    //mEdgePage.loadUrl("about:blank");
                    getActivity().onBackPressed();
                    backPressed = true;
                }
            } else if (exit && !backPressed) {
                savePreferences();
                //mEdgePage.loadUrl("about:blank");
                getActivity().onBackPressed();
                backPressed = true;
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

    static void getEdgeClasses(WebView webView) {
        int ClassElement = 0;
        while (ClassElement != 7) {
            webView.loadUrl("javascript:(function(){" +
                    "if (document.getElementsByClassName('class user-in-class')['" + ClassElement + "'] != undefined){" +
                    "console.log('RetrievedEdgeClass' + document.getElementsByClassName('class user-in-class')['" + ClassElement + "'].innerHTML);}" +
                    "})()");
            webView.loadUrl("javascript:(function(){" +
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
        if (classesRetrieved) {
            Log.d(TAG, "Saving edge classes");
            //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = settings.edit();
            // Edit and commit
            editor.putString(PREF_EDGE5Cur, edgeDay5Cur);
            editor.putString(PREF_EDGE1, edgeDay[2]);
            editor.putString(PREF_EDGE2, edgeDay[3]);
            editor.putString(PREF_EDGE3, edgeDay[4]);
            editor.putString(PREF_EDGE4, edgeDay[5]);
            editor.putString(PREF_EDGE5, edgeDay[6]);
            //mEdgePage.loadUrl("about:blank");
            editor.commit();
        } else {
            Log.d(TAG, "Not loaded, saving skipped");
        }

    }

    private void updateLoading(){
        mEdgeLoadingText.setText("Getting Edge Classes... " + loadingProgress + "%");
        loadingProgress++;
    }

    private void runThread() {

        new Thread() {
            public void run() {
                while (loadingProgress < 96 && getActivity() != null) {
                    Log.d(TAG, "Loading " + loadingProgress + "%");
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateLoading();
                            }
                        });
                        Thread.sleep(85);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "Loaded " + loadingProgress + "%");
                loadingProgress = 0;

            }
        }.start();
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
