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
import static corve.nohsedge.MainActivity.pRememValue;
import static corve.nohsedge.MainActivity.passwordValue;
import static corve.nohsedge.MainActivity.unameValue;

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
        mRemember.setChecked(pRememValue);
        loginIntent = new Intent(getBaseContext(), MainActivity.class);
        if (pRememValue) {
            mUsername.setText(unameValue);
            mPassword.setText(passwordValue);
        }
        if (invalid) {
            TextView mInvalid = findViewById(R.id.invalidLogin);
            mInvalid.setVisibility(View.VISIBLE);
        }
        //Automatic login code
        if (MainActivity.autoLoginValue && !MainActivity.FirstLoadValue && !invalid && pRememValue) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null) {
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            MainActivity.login = 1;
                MainActivity.calledForeign = true;
                unameValue = mUsername.getText().toString();
                passwordValue = mPassword.getText().toString();
                MainActivity.pRememValue = mRemember.isChecked();
                startActivity(loginIntent);
                finish();
            }

            mActivateRegister.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View view) {
                            if (mActivateRegister.getText().equals("Need to register?")) {
                                mRegister.setVisibility(View.VISIBLE);
                                mEmail.setVisibility(View.VISIBLE);
                                mActivateRegister.setText("Back to login");
                                mLogin.setVisibility(View.INVISIBLE);

                            } else if (mActivateRegister.getText().equals("Back to login")) {
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
                            }

                                MainActivity.login = 1;
                                MainActivity.calledForeign = true;
                                unameValue = mUsername.getText().toString();
                                passwordValue = mPassword.getText().toString();
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                SharedPreferences.Editor editor = settings.edit();
                                if (mRemember.isChecked()) {
                                    editor.putString(PREF_UNAME, mUsername.getText().toString());
                                    editor.putString(PREF_PASSWORD, mPassword.getText().toString());
                                }
                                editor.putBoolean(PREF_PREMEM, mRemember.isChecked());
                                editor.apply();
                                MainActivity.pRememValue = mRemember.isChecked();
                                setContentView(R.layout.activity_main);
                                startActivity(loginIntent);
                                finish();
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
                            MainActivity.register = 1;
                            MainActivity.calledForeign = true;
                            unameValue = mUsername.getText().toString();
                            passwordValue = mPassword.getText().toString();
                            MainActivity.pRememValue = mRemember.isChecked();
                            MainActivity.emailValue = mEmail.getText().toString();
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
