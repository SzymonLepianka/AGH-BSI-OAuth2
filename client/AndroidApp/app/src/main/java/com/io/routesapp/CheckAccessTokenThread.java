package com.io.routesapp;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckAccessTokenThread extends Thread {
    String DPRefreshTokenUrl = "http://10.0.2.2:8081/api/refreshToken?clientID=2&refreshToken=";

    @Override
    public void run() {
        String accessToken = MainActivity.getLoggedInUser().getCookies().get("AccessToken2");

        while (true) {
            while (!ifExpiresSoon(accessToken)) {
                try {
                    Thread.sleep(600000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            refreshTokens();
        }
    }

    private Boolean ifExpiresSoon(String accessToken) {
        String[] split_string = accessToken.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));

        String[] split = body.split(",");

        if (split[2].startsWith("exp", 1)){
            long expiresAt = Long.parseLong(split[2].substring(6));
            System.out.println(expiresAt);
            long currentTime = System.currentTimeMillis()/1000L;
            return  (expiresAt - currentTime) < 600 ;
        }

        return false;
    }

    private void refreshTokens() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(DPRefreshTokenUrl + MainActivity.getLoggedInUser().getCookies().get("RefreshToken2"))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Error in response from DP Server :( error code: " + String.valueOf(response.code()));
                } else {
                    String cookie = (response.headers().values("Set-Cookie").get(0).split(";")[0]);
                    MainActivity.getLoggedInUser().getCookies().put(cookie.split("=")[0], cookie.split("=")[1]);
                    cookie = (response.headers().values("Set-Cookie").get(1).split(";")[0]);
                    MainActivity.getLoggedInUser().getCookies().put(cookie.split("=")[0], cookie.split("=")[1]);
                }
            }
        });

        while (MainActivity.getLoggedInUser().getCookies().size() != 3) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
