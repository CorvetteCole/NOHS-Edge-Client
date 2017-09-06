package corve.nohsedge;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.concurrent.locks.Condition;
import java.util.UUID;



public class MainActivity extends AppCompatActivity {

    Button mLogin;
    TextView mCredit;
    ProgressBar mLoading;
    private static final String TAG = "MainActivity";
    int x = 0;
    private WebView mLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mUsername = (EditText) findViewById(R.id.usernameText);
        mLogin = (Button) findViewById(R.id.loginButton);
        //mPassword = (EditText) findViewById(R.id.passwordText);
        mLoginPage = (WebView) findViewById(R.id.loginWebview);
        mCredit = (TextView) findViewById(R.id.creditText);
        mLoading = (ProgressBar) findViewById(R.id.progressBar);


        mLogin.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        //username = mUsername.getText().toString());
                        //password = mPassword.getText().toString());
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
                if (cm.lineNumber() == 128) {
                    if (x == 1) {
                        mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/index.html#homescreen");
                        x = 2;
                    }
                    if (x == 0) {
                        x = 1;
                    }
                }
                return true;

            }
        });
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);//XSS vulnerable
        //webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //mLoginPage.setVisibility(View.VISIBLE);

        mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/index.html#login");

        //mLoginPage.loadUrl("file:///android_asset/index.html");
        mLoginPage.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mLoading.setVisibility(View.INVISIBLE);
                mLoginPage.setVisibility(View.VISIBLE);
            }
        });
    }



}
