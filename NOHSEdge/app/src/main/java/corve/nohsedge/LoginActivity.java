package corve.nohsedge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static corve.nohsedge.MainActivity.PREF_PASSWORD;
import static corve.nohsedge.MainActivity.PREF_PREMEM;
import static corve.nohsedge.MainActivity.PREF_UNAME;
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
    static boolean invalid;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

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
        if (invalid) {
            TextView mInvalid = (TextView) findViewById(R.id.invalidLogin);
            mInvalid.setVisibility(View.VISIBLE);
        }
        if (MainActivity.AutologinValue) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null) {
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
                MainActivity.currentSet = 0;
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                MainActivity.Login = 1;
                MainActivity.calledForeign = true;
                UnameValue = mUsername.getText().toString();
                PasswordValue = mPassword.getText().toString();
                MainActivity.PRememValue = mRemember.isChecked();
                startActivity(intent);
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
                                MainActivity.Login = 1;
                                MainActivity.calledForeign = true;
                                UnameValue = mUsername.getText().toString();
                                PasswordValue = mPassword.getText().toString();
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                SharedPreferences.Editor editor = settings.edit();
                                if (mRemember.isChecked()) {
                                    editor.putString(PREF_UNAME, mUsername.getText().toString());
                                    editor.putString(PREF_PASSWORD, mPassword.getText().toString());
                                }
                                editor.putBoolean(PREF_PREMEM, mRemember.isChecked());
                                editor.apply();
                                MainActivity.PRememValue = mRemember.isChecked();
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
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
                            MainActivity.Register = 1;
                            MainActivity.calledForeign = true;
                            UnameValue = mUsername.getText().toString();
                            PasswordValue = mPassword.getText().toString();
                            MainActivity.PRememValue = mRemember.isChecked();
                            MainActivity.EmailValue = mEmail.getText().toString();
                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor editor = settings.edit();
                            if (mRemember.isChecked()) {
                                editor.putString(PREF_UNAME, mUsername.getText().toString());
                                editor.putString(PREF_PASSWORD, mPassword.getText().toString());
                            }
                            editor.putBoolean(PREF_PREMEM, mRemember.isChecked());
                            editor.apply();

                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
            );
        }
    }
