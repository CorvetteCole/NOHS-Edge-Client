package corve.nohsedge;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";
    private static final String PREF_PREMEM = "RememPass";
    private String WrongPassword = "pass did not match";
    private String webUrl;


    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;

    private final boolean DefaultPRememValue = false;
    private boolean PRememValue;

    Button mLogin;
    TextView mCredit;
    ProgressBar mLoading;
    private static final String TAG = "MainActivity";
    int x = 0;
    private WebView mLoginPage;
    private String uuid;
    private TextView mUsername;
    private TextView mPassword;
    private CheckBox mRemember;
    private String edgeUrl;

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
        mLoading = (ProgressBar) findViewById(R.id.progressBar);
        mUsername = (TextView) findViewById(R.id.usernameField);
        mPassword = (TextView) findViewById(R.id.passwordField);
        mRemember = (CheckBox) findViewById(R.id.rememberPassword);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }



        mLogin.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        openLoginpage();
                    }
                }
        );
    }

    public String getCookie(String siteName,String CookieName){
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        String[] temp=cookies.split(";");
        for (String ar1 : temp ){
            if(ar1.contains(CookieName)){
                String[] temp1=ar1.split("=");
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
        mLogin.setVisibility(View.INVISIBLE);
        mCredit.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.VISIBLE);
        WebSettings webSettings = mLoginPage.getSettings();
        mLoginPage.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId() );
                if (cm.message().toLowerCase().contains(WrongPassword.toLowerCase())) {
                    mLoading.setVisibility(View.INVISIBLE);
                    mLogin.setVisibility(View.VISIBLE);
                    x = 0;
                }
                return true;
            }
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);

        if (x == 0) {
            mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/index.html#login");
        }
        mLoginPage.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (x == 0) {
                    mLoginPage.loadUrl("javascript:(function(){" +
                            "document.getElementById('login-username').value = '" + mUsername.getText().toString() + "';" +
                            "document.getElementById('login-password').value = '" + mPassword.getText().toString() + "';" +
                            "l=document.getElementById('login-btn');" +
                            "e=document.createEvent('HTMLEvents');" +
                            "e.initEvent('click',true,true);" +
                            "l.dispatchEvent(e);" +
                            "})()");
                }

            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                webUrl = mLoginPage.getUrl();
                Log.d("!URL", webUrl);
                if (webUrl.equals("http://sites.superfanu.com/nohsstampede/6.0.0/index.html#homescreen") && x == 0) {
                    mLoading.setVisibility(View.INVISIBLE);
                    mLoginPage.setVisibility(View.VISIBLE);
                    x = 1;
                }
                if (webUrl.toLowerCase().contains("edgetime".toLowerCase())) {
                    edgeUrl = webUrl;
                    addShortcut();
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
        if (mRemember.isChecked()) {
            editor.putString(PREF_UNAME, UnameValue);
            editor.putString(PREF_PASSWORD, PasswordValue);
        }
        editor.putBoolean(PREF_PREMEM, PRememValue);
        editor.apply();
    }
    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        PRememValue = settings.getBoolean(PREF_PREMEM, DefaultPRememValue);
        mRemember.setChecked(PRememValue);
        if (mRemember.isChecked()) {
            mUsername.setText(UnameValue);
            mPassword.setText(PasswordValue);
        }
    }

    public void addShortcut(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            ShortcutInfo webShortcut = new ShortcutInfo.Builder(this, "shortcut_web")
                    .setShortLabel("Edge")
                    .setLongLabel("Open Edge")
                    .setIcon(Icon.createWithResource(this, R.drawable.icon))
                    .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(edgeUrl)))
                    .build();

            shortcutManager.setDynamicShortcuts(Collections.singletonList(webShortcut));
        }
    }
}
