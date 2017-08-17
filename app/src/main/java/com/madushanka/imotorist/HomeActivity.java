package com.madushanka.imotorist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.madushanka.imotorist.entities.AccessToken;
import com.madushanka.imotorist.entities.ApiError;
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
    Call<AccessToken> call;
    Call<String> call1;
    GeometricProgressView progressView;
    ApiService authService;

    Fragment fragment = null;
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


        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        authService = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        progressView = (GeometricProgressView) findViewById(R.id.progressView);



        fragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_view, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.



        int id = item.getItemId();

        if (id == R.id.nav_home) {
           fragment = new HomeFragment();
        } else if (id == R.id.nav_profile) {
           fragment = new AccountFragment();
        } else if (id == R.id.nav_fine) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_logout) {

            logout();

        }

       if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_view, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openNav(View v){

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

}
