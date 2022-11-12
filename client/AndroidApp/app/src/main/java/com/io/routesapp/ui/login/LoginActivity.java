package com.io.routesapp.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.io.routesapp.MainActivity;
import com.io.routesapp.R;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final TextInputEditText usernameEditText = findViewById(R.id.email_input);
        final TextInputEditText passwordEditText = findViewById(R.id.password_input);
        final Button loginButton = findViewById(R.id.login);
        loginButton.setEnabled(false);

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
            }
            setResult(Activity.RESULT_OK);

            finish();
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(
                        Objects.requireNonNull(usernameEditText.getText()).toString(),
                        Objects.requireNonNull(passwordEditText.getText()).toString()
                );
                loginButton.setEnabled(true);
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                        Objects.requireNonNull(usernameEditText.getText()).toString(),
                        Objects.requireNonNull(passwordEditText.getText()).toString()
                );
            }
            return false;
        });

        loginButton.setOnClickListener(v ->
                loginViewModel.login(
                        Objects.requireNonNull(usernameEditText.getText()).toString(),
                        Objects.requireNonNull(passwordEditText.getText()).toString()
                ));
    }

    private void updateUiWithUser(LoggedInUserView model) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("PreviousActivity", "login");
        intent.putExtra("userId", model.getUserId());
        intent.putExtra("username", model.getUsername());
        intent.putExtra("displayName", model.getDisplayName());
        intent.putExtra("email", model.getEmail());
        intent.putExtra("AccessToken2", model.getCookies().get("AccessToken2"));
        intent.putExtra("RefreshToken2", model.getCookies().get("RefreshToken2"));
        startActivity(intent);
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

}