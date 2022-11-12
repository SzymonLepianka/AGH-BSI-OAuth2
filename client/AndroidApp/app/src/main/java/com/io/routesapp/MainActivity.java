package com.io.routesapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.io.routesapp.data.SharedRoutesPlacesRepository;
import com.io.routesapp.data.model.LoggedInUser;
import com.io.routesapp.data.httpClient;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private static LoggedInUser loggedInUser;
    public static httpClient HTTPClient;
    public static boolean logoutButtonPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logoutButtonPressed = false;

        Intent intent = getIntent();

        loggedInUser = new LoggedInUser(0, "", "", "", new HashMap<>());
        loggedInUser.setCookie("AccessToken2", intent.getStringExtra("AccessToken2"));
        loggedInUser.setCookie("RefreshToken2", intent.getStringExtra("RefreshToken2"));

        HTTPClient = new httpClient(this.getApplicationContext(), "MainActivity");

        if (Objects.requireNonNull(intent.getStringExtra("PreviousActivity")).equals("login")) {
            loggedInUser.setUserId(intent.getIntExtra("userId", 0));
            loggedInUser.setUsername(intent.getStringExtra("username"));
            loggedInUser.setDisplayName(intent.getStringExtra("displayName"));
            loggedInUser.setEmail(intent.getStringExtra("email"));
        }
        else if (Objects.requireNonNull(intent.getStringExtra("PreviousActivity")).equals("start")) {
            String username = intent.getStringExtra("username");
            try {
                loggedInUser = HTTPClient.getUserData(username);
            } catch (InterruptedException | JSONException e) {
                e.printStackTrace();
            }
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //initializing a shared repository
        SharedRoutesPlacesRepository sharedRepo = new SharedRoutesPlacesRepository();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_my_profile,
                R.id.nav_discover_places, R.id.nav_discover_routes,
                R.id.nav_my_fav_places, R.id.nav_my_fav_routes, R.id.nav_help)
                .setDrawerLayout(mDrawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.drawer_name);
        name.setText(loggedInUser.getDisplayName());
        TextView email = headerView.findViewById((R.id.drawer_email));
        email.setText(loggedInUser.getEmail());

        CheckAccessTokenThread thread = new CheckAccessTokenThread();
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!logoutButtonPressed) {
            SharedPreferences mySharedPreferences = getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("AccessToken2", loggedInUser.getCookies().get("AccessToken2"));
            editor.putString("RefreshToken2", loggedInUser.getCookies().get("RefreshToken2"));
            editor.putString("username", loggedInUser.getUsername());
            editor.apply();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!logoutButtonPressed) {
            SharedPreferences mySharedPreferences = getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("AccessToken2", loggedInUser.getCookies().get("AccessToken2"));
            editor.putString("RefreshToken2", loggedInUser.getCookies().get("RefreshToken2"));
            editor.putString("username", loggedInUser.getUsername());
            editor.apply();
        }
    }

    public static LoggedInUser getLoggedInUser() {
        return loggedInUser;
    }
}