package com.madushanka.imotorist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.madushanka.imotorist.controllers.LoginManager;
import com.madushanka.imotorist.controllers.TokenManager;
import com.madushanka.imotorist.controllers.UserManager;
import com.madushanka.imotorist.entities.AccessToken;
import com.madushanka.imotorist.entities.ApiError;
import com.madushanka.imotorist.entities.User;
import com.madushanka.imotorist.network.ApiService;
import com.madushanka.imotorist.network.RetrofitBuilder;

import net.bohush.geometricprogressview.GeometricProgressView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    TokenManager tokenManager;
    UserManager userManager;
    Call<AccessToken> call;
    Call<User> user_call;
    Call<String> call1;
    LoginManager loginManager;
    GeometricProgressView progressView;
    ApiService authService;
    Call<String> firebase_call;
    Fragment fragment = null;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        loginFirebase();

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        userManager = UserManager.getInstance(getSharedPreferences("prefsu", MODE_PRIVATE));
        loginManager = LoginManager.getInstance(getSharedPreferences("pref_login", MODE_PRIVATE));
        authService = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        progressView = (GeometricProgressView) findViewById(R.id.progressView);

/*
        fragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_view, fragment);
        ft.commit();*/

        getUser();


    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();


        if (count == 0) {
            super.onBackPressed();
            //mAuth.signOut();

        } else {

            final Fragment homefragment = getSupportFragmentManager().findFragmentByTag("home");
            final Fragment ticketfragment = getSupportFragmentManager().findFragmentByTag("ticket");

            if (homefragment != null && homefragment.isVisible()) {
                super.onBackPressed();
                //mAuth.signOut();
            }

            getFragmentManager().popBackStack();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            // mAuth.signOut();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String s = "";

        int id = item.getItemId();

        if (id == R.id.nav_home) {

            fragment = new HomeFragment();
            s = "home";
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (id == R.id.nav_profile) {
            fragment = new AccountFragment();
        } else if (id == R.id.nav_fine) {
            // Toast.makeText(HomeActivity.this,   userManager.getUser().getEmail(), Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_logout) {

            logout();

        }


        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_view, fragment, s);
            ft.addToBackStack(s);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openNav(View v) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.LEFT);
    }

    void logout() {

        call = authService.logout();


        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    if (response.code() == 204) {


                        Toast.makeText(HomeActivity.this, "Logout Successful ", Toast.LENGTH_LONG).show();
                        tokenManager.deleteToken();
                        userManager.deleteUser();
                        loginManager.deleteLogin();
                        mAuth.signOut();

                        startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        finish();

                    }

                } else {
                    if (response.code() == 422) {


                        Toast.makeText(HomeActivity.this, "Error ", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());

                        Toast.makeText(HomeActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());

            }
        });

    }

    void getUser() {

        user_call = authService.user();


        user_call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> user_call, Response<User> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    // Toast.makeText(HomeActivity.this, response.body().getEmail(), Toast.LENGTH_LONG).show();
                    User u = new User(response.body().getId(), response.body().getEmail(), response.body().getName());
                    userManager.saveUser(u);

                    TextView full_name = (TextView) findViewById(R.id.full_name);
                    TextView nic = (TextView) findViewById(R.id.nic);

                    full_name.setText(response.body().getFull_name());
                    nic.setText(response.body().getEmail());


                    if (response.code() == 204) {


                        //  Toast.makeText(HomeActivity.this, response.body().getFull_name(), Toast.LENGTH_LONG).show();


                    }

                } else {
                    if (response.code() == 422) {


                        Toast.makeText(HomeActivity.this, "Error ", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());

                        Toast.makeText(HomeActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<User> user_call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());

            }
        });


    }


    public void loginFirebase() {

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d("app", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String s = FirebaseInstanceId.getInstance().getToken();
                            updateFirebase(s);

                            //  Toast.makeText(DashBoardActivity.this, s, Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("app", "signInAnonymously:failure", task.getException());
                            //   Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }

    void updateFirebase(String token) {


        firebase_call = authService.firebase(token);


        firebase_call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> firebase_call, Response<String> response) {

                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                    //Toast.makeText(DashBoardActivity.this,"Saved", Toast.LENGTH_LONG).show();

                } else {
                    if (response.code() == 422) {


                        //  Toast.makeText(DashBoardActivity.this, "Invalid", Toast.LENGTH_LONG).show();

                    }
                    if (response.code() == 401) {
                        ApiError apiError = Utils.convertErrors(response.errorBody());

                        // Toast.makeText(DashBoardActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<String> firebase_call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());

            }
        });


    }

}
