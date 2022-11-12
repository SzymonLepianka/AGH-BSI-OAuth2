package com.io.routesapp.data.model;

import java.util.HashMap;


//Data class that captures user information for logged in users retrieved from LoginRepository

public class LoggedInUser {

    private int userId;
    private String username;
    private String displayName;
    private String email;
    private HashMap<String, String> cookies;

    public LoggedInUser(int userID, String username, String displayName, String email, HashMap<String, String> cookies) {
        this.userId = userID;
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.cookies = cookies;
    }

    public int getUserId() { return userId; }

    public String getUsername() {return  username; }

    public String getDisplayName() { return displayName; }

    public String getEmail() { return email; }

    public HashMap<String, String> getCookies() { return cookies; }

    public void setUserId(Integer userId) { this.userId = userId; }

    public void setUsername(String username) { this.username = username; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public void setEmail(String email) { this.email = email; }

    public void setCookie(String key, String value) { cookies.put(key, value); }

    public void setCookies(HashMap<String, String> cookies) { this.cookies = cookies; }

    }