package com.io.routesapp.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.io.routesapp.MainActivity;
import com.io.routesapp.R;
import com.io.routesapp.ui.start.StartActivity;
import com.io.routesapp.data.model.LoggedInUser;

import org.json.JSONException;

public class ProfileFragment extends Fragment {

    private LoggedInUser loggedInUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loggedInUser = MainActivity.getLoggedInUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            loggedInUser = MainActivity.HTTPClient.getUserData(MainActivity.getLoggedInUser().getUsername());
        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        } catch (VerifyError e){
            Toast.makeText(requireContext(), "Not authorized. Please, log in.", Toast.LENGTH_SHORT).show();
            SharedPreferences preferences = requireActivity()
                    .getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            MainActivity.logoutButtonPressed = true; //a flag to signal that main activity must not save user's data
            Intent intent = new Intent(getContext(), StartActivity.class);
            startActivity(intent);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView username = root.findViewById(R.id.username);
        final TextView displayName = root.findViewById(R.id.displayName);
        username.setText(loggedInUser.getUsername());
        displayName.setText(loggedInUser.getDisplayName());
        Button logout_button = root.findViewById(R.id.log_out);

        //delete username and authorization and refresh token on logout
        logout_button.setOnClickListener(v -> {
            SharedPreferences preferences = requireActivity()
                    .getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            MainActivity.logoutButtonPressed = true; //a flag to signal that main activity must not save user's data
            Intent intent = new Intent(getContext(), StartActivity.class);
            startActivity(intent);
        });
        return root;
    }
}