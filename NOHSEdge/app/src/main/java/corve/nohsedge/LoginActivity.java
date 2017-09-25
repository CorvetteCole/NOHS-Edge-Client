package corve.nohsedge;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import static corve.nohsedge.MainActivity.PRememValue;
import static corve.nohsedge.MainActivity.PasswordValue;
import static corve.nohsedge.MainActivity.UnameValue;

public class LoginActivity extends AppCompatActivity {
    private TextView mUsername;
    private TextView mPassword;
    private CheckBox mRemember;
    private Button mRegister;
    private TextView mEmail;
    private TextView mActivateRegister;
    private Button mLogin;
    private TextView mCredit;
    private int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLogin = (Button) findViewById(R.id.loginButton);
        mCredit = (TextView) findViewById(R.id.creditText);
        mUsername = (TextView) findViewById(R.id.usernameField);
        mPassword = (TextView) findViewById(R.id.passwordField);
        mRemember = (CheckBox) findViewById(R.id.rememberPassword);
        mRegister = (Button) findViewById(R.id.RegisterButton);
        mEmail = (TextView) findViewById(R.id.emailField);
        mActivateRegister = (TextView) findViewById(R.id.ActivateRegister);
        mRemember.setChecked(PRememValue);
        if (PRememValue) {
            mUsername.setText(UnameValue);
            mPassword.setText(PasswordValue);
        }
        mActivateRegister.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if (mActivateRegister.getText().equals("Back to login")) {
                            a = 1;
                        }
                        if (mActivateRegister.getText().equals("Need to register?")) {
                            a = 0;
                        }

                        if (a == 0) {
                            mRegister.setVisibility(View.VISIBLE);
                            mEmail.setVisibility(View.VISIBLE);
                            mActivateRegister.setText("Back to login");
                            mLogin.setVisibility(View.INVISIBLE);

                        }
                        if (a == 1) {
                            mRegister.setVisibility(View.INVISIBLE);
                            mEmail.setVisibility(View.INVISIBLE);
                            mActivateRegister.setText("Need to register?");
                            mLogin.setVisibility(View.VISIBLE);

                        }
                    }
                }
        );

        mLogin.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (getCurrentFocus() != null) {
                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                            MainActivity.currentSet = 0;
                            setContentView(R.layout.activity_login);
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            MainActivity.Login = 1;
                            MainActivity.calledForeign = true;
                            UnameValue = mUsername.getText().toString();
                            PasswordValue = mPassword.getText().toString();
                            MainActivity.PRememValue = mRemember.isChecked();
                            startActivity(intent);
                        }
                    }
                }
        );
        mRegister.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (getCurrentFocus() != null) {
                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        MainActivity.currentSet = 0;
                        setContentView(R.layout.activity_login);
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        MainActivity.Register = 1;
                        MainActivity.calledForeign = true;
                        UnameValue = mUsername.getText().toString();
                        PasswordValue = mPassword.getText().toString();
                        MainActivity.PRememValue = mRemember.isChecked();
                        MainActivity.EmailValue = mEmail.getText().toString();
                        startActivity(intent);
                    }
                }
        );
    }
}
