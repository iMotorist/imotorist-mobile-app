package com.madushanka.imotorist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.madushanka.imotorist.controllers.LoginManager;
import com.madushanka.imotorist.controllers.TokenManager;
import com.madushanka.imotorist.entities.AccessToken;
import com.madushanka.imotorist.entities.ApiError;
import com.madushanka.imotorist.entities.Login;
import com.madushanka.imotorist.network.ApiService;
import com.madushanka.imotorist.network.RetrofitBuilder;

import net.bohush.geometricprogressview.GeometricProgressView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static int SPLASH_TIME_OUT = 3000;
    ApiService service;
    ApiService authService;
    TokenManager tokenManager;
    LoginManager loginManager;
    Call<AccessToken> call;
    GeometricProgressView progressView;
    private EditText mUsernameField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        service = RetrofitBuilder.createService(ApiService.class);


        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        loginManager = LoginManager.getInstance(getSharedPreferences("pref_login", MODE_PRIVATE));
        authService = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);


        super.onCreate(savedInstanceState);

        if (loginManager.getLogin().getUsername() == null) {
            setContentView(R.layout.activity_main);
            mUsernameField = (EditText) findViewById(R.id.username);
            mPasswordField = (EditText) findViewById(R.id.password);
            progressView = (GeometricProgressView) findViewById(R.id.progressView);
        } else {
            setContentView(R.layout.activity_main_user);
            progressView = (GeometricProgressView) findViewById(R.id.progressView_user);
            loginUserReturn();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();





    }

    public void goPhone(View v) {
        Intent i = new Intent(this, PhoneAuthActivity.class);
        startActivity(i);


    }

    public void goHome(View v) {
        login();

    }


    void login() {

        if (validate()) {
            progressView.setVisibility(View.VISIBLE);
            final String email = mUsernameField.getText().toString().trim();
            final String password = mPasswordField.getText().toString().trim();

            call = service.login(email, password);


            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        loginManager.saveLogin(new Login(email, password));
                        tokenManager.saveToken(response.body());
                        progressView.setVisibility(View.INVISIBLE);
                        // Toast.makeText(MainActivity.this, response.body() + "", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        if (response.code() == 422) {

                            progressView.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Invalid Username or Password ", Toast.LENGTH_LONG).show();

                        }
                        if (response.code() == 401) {
                            ApiError apiError = Utils.convertErrors(response.errorBody());
                            progressView.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());

                }
            });


        }
    }

    void loginUserReturn() {


        progressView.setVisibility(View.VISIBLE);
        final String email = loginManager.getLogin().getUsername();
        final String password = loginManager.getLogin().getPassword();

        call = service.login(email, password);


        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {
                    loginManager.saveLogin(new Login(email, password));
                    tokenManager.saveToken(response.body());
                    progressView.setVisibility(View.INVISIBLE);
                    // Toast.makeText(MainActivity.this, response.body() + "", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                } else {
                    if (response.code() == 422) {

                        progressView.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Invalid Username or Password ", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());
                        progressView.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());

            }
        });


    }


    private boolean validate() {

        String username = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();


        if (TextUtils.isEmpty(username)) {
            mUsernameField.setError("Username is empty ");
            return false;

        }

        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Password is empty ");
            return false;

        }

        return true;
    }


}
