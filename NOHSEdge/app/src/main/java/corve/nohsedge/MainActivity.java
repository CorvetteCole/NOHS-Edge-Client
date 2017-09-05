package corve.nohsedge;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.concurrent.locks.Condition;
import java.util.UUID;



public class MainActivity extends AppCompatActivity {


    private String uuid = "b39de08ded8ac21c";
    EditText mUsername;
    EditText mPassword;
    Button mLogin;
    TextView mCredit;
    Button mNext;
    TextView mWarning;


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
        mNext = (Button) findViewById(R.id.NextButton);
        //mPassword = (EditText) findViewById(R.id.passwordText);
        mCredit = (TextView) findViewById(R.id.creditText);
        mWarning = (TextView) findViewById(R.id.warningText);

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
        mNext.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        //username = mUsername.getText().toString());
                        //password = mPassword.getText().toString());
                        openHomescreen();

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
        WebView mLoginPage = (WebView) findViewById(R.id.loginWebview);
        mLogin.setVisibility(View.INVISIBLE);
        mCredit.setVisibility(View.INVISIBLE);
        //mCredit.setText("Please wait 5 seconds after clicking login, then click next");
        mNext.setVisibility(View.VISIBLE);
        WebSettings webSettings = mLoginPage.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        mLoginPage.setVisibility(View.VISIBLE);
        mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/index.html#login"); //http://sites.superfanu.com/nohsstampede/6.0.0/#login     //file:///android_asset/index.html
        //mLoginPage.loadUrl("file:///android_asset/index.html");
    }
    public void openHomescreen() {
        mLogin.setVisibility(View.INVISIBLE);
        WebView mLoginPage = (WebView) findViewById(R.id.loginWebview);
        //WebView mHomePage = (WebView) findViewById(R.id.HomescreenWebview);
        mWarning.setVisibility(View.INVISIBLE);
        mNext.setVisibility(View.INVISIBLE);
        WebSettings webSettings = mLoginPage.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        //mHomePage.setVisibility(View.VISIBLE);
        mLoginPage.loadUrl("http://sites.superfanu.com/nohsstampede/6.0.0/index.html#homescreen");

    }


}
