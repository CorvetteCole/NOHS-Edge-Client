package corve.nohsedge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

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
    static boolean invalid;
    private Intent loginIntent;
    private int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLogin = findViewById(R.id.loginButton);
        mUsername = findViewById(R.id.usernameField);
        mPassword = findViewById(R.id.passwordField);
        mRemember = findViewById(R.id.rememberPassword);
        mRegister = findViewById(R.id.RegisterButton);
        mEmail = findViewById(R.id.emailField);
        mActivateRegister = findViewById(R.id.ActivateRegister);
        mRemember.setChecked(PRememValue);
        loginIntent = new Intent(getBaseContext(), MainActivity.class);
        if (PRememValue) {
            mUsername.setText(UnameValue);
            mPassword.setText(PasswordValue);
        }
        if (invalid) {
            TextView mInvalid = findViewById(R.id.invalidLogin);
            mInvalid.setVisibility(View.VISIBLE);
        }
        if (MainActivity.AutologinValue) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null) {
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            MainActivity.Login = 1;
                MainActivity.calledForeign = true;
                UnameValue = mUsername.getText().toString();
                PasswordValue = mPassword.getText().toString();
                MainActivity.PRememValue = mRemember.isChecked();
                startActivity(loginIntent);
                finish();
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
                            Log.d("ITS BEEN CLICKED", " (the login button I mean)");
                            InputMethodManager inputManager = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (getCurrentFocus() != null) {
                                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);

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
                                setContentView(R.layout.activity_main);
                                startActivity(loginIntent);
                                finish();
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
                            startActivity(loginIntent);
                            finish();
                        }
                    }
            );
        }
    }
