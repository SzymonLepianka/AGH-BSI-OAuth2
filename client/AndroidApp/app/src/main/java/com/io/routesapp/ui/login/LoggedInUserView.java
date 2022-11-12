package com.io.routesapp.ui.login;

import java.util.HashMap;


// Class exposing authenticated user details to the UI.
class LoggedInUserView {
    private int userId;
    private String displayName;
    private String username;
    private String email;
    private HashMap<String, String> cookies;

    LoggedInUserView(int userId, String displayName, String username, String email, HashMap<String, String> cookies) {
        this.userId = userId;
        this.displayName = displayName;
        this.username = username;
        this.email = email;
        this.cookies = cookies;
    }

    public int getUserId() { return userId; }
    String getDisplayName() { return displayName; }
    String getUsername(){ return username; }
    String getEmail(){ return  email; }
    HashMap<String, String> getCookies(){ return cookies; }

}