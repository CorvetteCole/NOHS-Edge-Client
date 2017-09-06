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


    private String uuid = "b39de08ded8ac21c";
    Button mLogin;
    TextView mCredit;
    ProgressBar mLoading;
    Button mNext;
    private static final String TAG = "MainActivity";
    int x = 0;


    public String getUuid() {
        uuid = getCookie("http://sites.superfanu.com/nohsstampede/6.0.0/index.html#homescreen","UUID");
        return uuid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mUsername = (EditText) findViewById(R.id.usernameText);
        mLogin = (Button) findViewById(R.id.loginButton);
        //mPassword = (EditText) findViewById(R.id.passwordText);
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



    public void openBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.superfanu.com/6.0.0/gen/link_track.php?platform=Android&uuid=" + getUuid() + "&nid=305&lkey=nohsstampede-edgetime-module"));
        startActivity(browserIntent);

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

    public void openLoginpage() {
        final WebView mLoginPage = (WebView) findViewById(R.id.loginWebview);
        mLogin.setVisibility(View.INVISIBLE);
        mCredit.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.VISIBLE);
        WebSettings webSettings = mLoginPage.getSettings();
        mLoginPage.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d(TAG, cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId() );
                return true;
            }
        });
        webSettings.setDomStorageEnabled(true);

        webSettings.setJavaScriptEnabled(true);//XSS vulnerable
        //webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //mLoginPage.setVisibility(View.VISIBLE);
        mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/index.html#login"); //http://sites.superfanu.com/nohsstampede/6.0.0/#login     //file:///android_asset/index.html
        //mLoginPage.loadUrl("file:///android_asset/index.html");
        mLoginPage.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mLoginPage.loadUrl(
                        "javascript:(function() { " +
                                "file:///android_asset/js/sfu.js" +
                                "})()");
                mLoading.setVisibility(View.INVISIBLE);
                mLoginPage.setVisibility(View.VISIBLE);
                mNext = (Button) findViewById(R.id.nextButton);
                if (x == 0) {
                    mNext.setVisibility(View.VISIBLE);
                }
                else mNext.setVisibility(View.INVISIBLE);

                mNext.setOnClickListener(
                        new View.OnClickListener()
                        {
                            public void onClick(View view)
                            {
                                //username = mUsername.getText().toString());
                                //password = mPassword.getText().toString());
                                mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/index.html#homescreen");
                                x = 1;

                            }
                        }
                );
            }
        });
    }



}
