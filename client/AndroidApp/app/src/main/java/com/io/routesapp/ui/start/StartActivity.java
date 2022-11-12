package com.io.routesapp.ui.start;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.io.routesapp.MainActivity;
import com.io.routesapp.R;
import com.io.routesapp.data.httpClient;
import com.io.routesapp.ui.login.LoginActivity;

import java.util.HashMap;

public class StartActivity extends AppCompatActivity {
    HashMap<String, String> cookies;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button login_button = findViewById(R.id.login_button);
        Button sign_up_button = findViewById(R.id.create_account);

        httpClient HTTPClient = new httpClient(this.getApplicationContext(), "StartActivity");

        SharedPreferences mySharedPreferences = getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE);
        cookies = new HashMap<>();
        if(mySharedPreferences.contains("AccessToken2")) {
            cookies.put("AccessToken2", mySharedPreferences.getString("AccessToken2", ""));
            try {
                if (HTTPClient.isTokenValid(cookies.get("AccessToken2"))) {
                    if (mySharedPreferences.contains("RefreshToken2")) {
                        cookies.put("RefreshToken2", mySharedPreferences.getString("RefreshToken2", ""));
                    }
                    String username = null;
                    if (mySharedPreferences.contains("RefreshToken2")) {
                        username = mySharedPreferences.getString("username", "");
                    }

                    try {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("PreviousActivity", "start");
                        intent.putExtra("username", username);
                        intent.putExtra("AccessToken2", cookies.get("AccessToken2"));
                        intent.putExtra("RefreshToken2", cookies.get("RefreshToken2"));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        login_button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
       });
    }

}
