package com.io.routesapp.data;

import android.content.Context;
import android.util.Log;

import com.io.routesapp.MainActivity;
import com.io.routesapp.R;
import com.io.routesapp.data.model.LoggedInUser;
import com.io.routesapp.ui.places.model.Place;
import com.io.routesapp.ui.places.model.PlaceReview;
import com.io.routesapp.ui.routes.model.Route;
import com.io.routesapp.ui.routes.model.RouteReview;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.riversun.okhttp3.OkHttp3CookieHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//class used to communicate with servers

public class httpClient {
    private static JSONObject responseJSON;
    private String baseURL;
    private String accessToken;

    private boolean failure = false;

    public httpClient(Context context, String activityName) {
        baseURL = context.getResources().getString(R.string.baseUrl);
        if (activityName.equals("MainActivity")) {
            accessToken = MainActivity.getLoggedInUser().getCookies().get("AccessToken2");
        }
    }

    public ArrayList<Place> getPlaces() throws JSONException, InterruptedException {
        responseJSON = null;

        String url = baseURL + "/places"; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken2" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("OKHTTP3", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()){
                    Log.d("OKHTTP3", String.valueOf(response.code()));
                    return;
                }
                try {
                    responseJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        while (responseJSON == null) {
            Thread.sleep(10);
        }

        ArrayList<Place> placesList = new ArrayList<>();
        JSONArray idArray =responseJSON.names();
        if (idArray != null) {
            for (int i = 0; i < idArray.length(); i++) {
                String id = (String) idArray.get(i);
                JSONObject placeInfo = responseJSON.getJSONObject(String.valueOf(id));
                Boolean valid = placeInfo.getBoolean("valid");
                Double latitude = placeInfo.getDouble("latitude");
                Double longitude = placeInfo.getDouble("longitude");
                String name = placeInfo.getString("name");
                String description = placeInfo.getString("description");
                int accumulatedScore = placeInfo.getInt("accumulatedScore");
                int usersVoted = placeInfo.getInt("usersVoted");
                Place newPlaceFromJSON = new Place(Integer.parseInt(id), name, valid,
                        latitude, longitude, 0,
                        accumulatedScore, usersVoted, description);
                placesList.add(newPlaceFromJSON);
            }
        }
        return placesList;
    }

    public Place getPlace(int id) throws JSONException, InterruptedException {
        //setting json to null so as to wait for the new response
        responseJSON = null;

        // send request to server to get place details, parse place information
        // and return new place object

        //URL wymagany path=places/{id}
        String url = baseURL + "/places/" + id; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken2" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("OKHTTP3", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()){
                    Log.d("OKHTTP3", String.valueOf(response.code()));
                    return;
                }
                try {
                    responseJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        while (responseJSON == null) {
            Thread.sleep(10);
        }

        JSONObject placeInfo = responseJSON.getJSONObject(String.valueOf(id));
        Boolean valid = placeInfo.getBoolean("valid");
        Double latitude = placeInfo.getDouble("latitude");
        Double longitude = placeInfo.getDouble("longitude");
        String name = placeInfo.getString("name");
        String description = placeInfo.getString("description");
        int accumulatedScore = placeInfo.getInt("accumulatedScore");
        int usersVoted = placeInfo.getInt("usersVoted");

        return new Place(id, name, valid,
                latitude, longitude, 0,
                accumulatedScore, usersVoted, description);
    }

    public ArrayList<PlaceReview> getPlaceReviews(int id) throws JSONException, InterruptedException{
        //the same for reviews
        // review constructor: PlaceReview(int placeID, int authorID, String content)
        responseJSON = null;


        String url = baseURL + "/places/" + id + "/comments"; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken2" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("OKHTTP3", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()){
                    Log.d("OKHTTP3", String.valueOf(response.code()));
                    return;
                }
                try {
                    responseJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        while (responseJSON == null) {
            Thread.sleep(10);
        }

        //dla każdego comment zwraca JSON z CommentID, UserID, Content
        ArrayList<PlaceReview> placeReviewList = new ArrayList<>();
        JSONArray idArray = responseJSON.names();
        if (idArray != null) {
            for (int i = 0; i < idArray.length(); i++) {
                String reviewId = String.valueOf(idArray.get(i));
                JSONObject placeInfo = responseJSON.getJSONObject(reviewId);
                String authorID = placeInfo.getString("username");
                String content = placeInfo.getString("content");
                PlaceReview newPlaceReviewFromJSON = new PlaceReview(reviewId, authorID, content);
                placeReviewList.add(newPlaceReviewFromJSON);
            }
        }
        return  placeReviewList;
    }

    public void addPlaceReview(PlaceReview review){

        String url = baseURL + "/places/" + review.getPlaceID() + "/comments"; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken2" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        RequestBody body = new FormBody.Builder()
                .add("placeId", review.getPlaceID())
                .add("userId", String.valueOf(MainActivity.getLoggedInUser().getUserId()))
                .add("content", review.getContent())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Objects.requireNonNull(e).printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Error in response from IO Server :( error code: " + response.code());
                }
            }
        });
    }

    //nie było mapowania dla ulubonych w Server \Gosia
    public ArrayList<Place> getFavouritePlaces(int userID){

        //TODO get list of favourite places IDs for the user
        // then get all that places information and return list of favourite places

        return new ArrayList<>();
    }

    public ArrayList<Route> getRoutes() throws JSONException, InterruptedException {
        responseJSON = null;
        //TODO get all public routes
        // route constructor: Route(int id, int accumulated score)
        // return list of routes
        String url = baseURL + "/routes"; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken2" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("OKHTTP3", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()){
                    Log.d("OKHTTP3", String.valueOf(response.code()));
                    return;
                }
                try {
                    responseJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        while (responseJSON == null) {
            Thread.sleep(10);
        }

        ArrayList<Route> routesList = new ArrayList<>();
        JSONArray idArray = responseJSON.names();
        if (idArray != null) {
            for (int i = 0; i < idArray.length(); i++) {
                String id = (String) idArray.get(i);
                JSONObject placeInfo = responseJSON.getJSONObject(String.valueOf(id));
                int score = placeInfo.getInt("accumulatedScore");
                Route newRouteFromJSON = new Route(Integer.parseInt(id), score, getPointsOfRoute(Integer.parseInt(id)));
                routesList.add(newRouteFromJSON);
            }
        }
        return routesList;
    }

    public Route getRoute(int id) throws InterruptedException, JSONException {
        //setting json to null so as to wait for the new response
        responseJSON = null;

        String url = baseURL + "/routes/" + id; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken2" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("OKHTTP3", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()){
                    Log.d("OKHTTP3", String.valueOf(response.code()));
                    return;
                }
                try {
                    responseJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        while (responseJSON == null) {
            Thread.sleep(10);
        }

        JSONObject routeInfo = responseJSON.getJSONObject(String.valueOf(id));
        Boolean isPublic = routeInfo.getBoolean("public");
        int accumulatedScore = routeInfo.getInt("accumulatedScore");
        int usersVoted = routeInfo.getInt("usersVoted");

        return new Route(
                id, accumulatedScore, usersVoted, isPublic,
                getPointsOfRoute(id)
        );
    }

    public ArrayList<Place> getPointsOfRoute(int id) throws InterruptedException, JSONException {
        //TODO get points of route
        responseJSON = null;
        String url = baseURL + "/routes/" + id + "/pointsOfRoute"; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken2" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("OKHTTP3", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()){
                    Log.d("OKHTTP3", String.valueOf(response.code()));
                    return;
                }
                try {
                    responseJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        while (responseJSON == null) {
            Thread.sleep(10);
        }
        ArrayList<Place> pointsOfRoute = new ArrayList<>();
        JSONArray idArray = responseJSON.names();
        JSONObject routesListJSON = responseJSON;
        if (idArray != null) {
            for (int i = 0; i < idArray.length(); i++) {
                String pointId = String.valueOf(idArray.get(i));
                JSONObject pointInfo = routesListJSON.getJSONObject(pointId);
                int placeID = pointInfo.getInt("placeId");
                pointsOfRoute.add(getPlace(placeID));
            }
        }
        return pointsOfRoute;
    }

    public ArrayList<RouteReview> getRouteReviews(int id)  throws JSONException, InterruptedException{
        // get reviews for route
        // constructor: RouteReview(int routeID, int authorID, String content)
        responseJSON = null;
        String url = baseURL + "routes/" + id + "/comments"; //10.0.2.2 - localhost

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken2" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("OKHTTP3", Objects.requireNonNull(e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()){
                    Log.d("OKHTTP3", String.valueOf(response.code()));
                    return;
                }
                try {
                    responseJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseJSON = new JSONObject();
                }
            }
        });

        while (responseJSON == null) {
            Thread.sleep(10);
        }

        ArrayList<RouteReview> routeReviewList = new ArrayList<>();
        JSONArray idArray = responseJSON.names();
        if (idArray != null) {
            for (int i = 0; i < idArray.length(); i++) {
                String reviewId = String.valueOf(idArray.get(i));
                JSONObject routeInfo = responseJSON.getJSONObject(reviewId);
                String authorID = routeInfo.getString("userId");
                String content = routeInfo.getString("content");
                RouteReview newRouteReviewFromJSON = new RouteReview(reviewId, authorID, content);
                routeReviewList.add(newRouteReviewFromJSON);
            }
        }
        return  routeReviewList;
    }

    public void addRouteReview(RouteReview review){
        String url = baseURL + "routes/" + review.getRouteID() + "/comments";
        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken2" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        RequestBody body = new FormBody.Builder()
                .add("id", review.getRouteID())
                .add("userId", String.valueOf(MainActivity.getLoggedInUser().getUserId()))
                .add("content", review.getContent())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Objects.requireNonNull(e).printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Error in response from IO Server :( error code: " + response.code());
                }
            }
        });
    }

    public ArrayList<Route> getFavouriteRoutes(int userID){
        //TODO get favourite routes list for this user
        return  new ArrayList<>();
    }

    public LoggedInUser getUserData(String username) throws InterruptedException, JSONException, VerifyError {
        String url = baseURL + "users/" + username;

        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken2" , accessToken);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieHelper.cookieJar())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if (!Objects.requireNonNull(response).isSuccessful()) {

                    if (response.code() == 401){
                        failure = true;
                    }
                    throw new IOException(String.valueOf(response.code()));
                } else {
                    try {
                        responseJSON = new JSONObject(Objects.requireNonNull(response.body()).string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (failure) {
            throw new VerifyError("User unauthorized");
        }

        while (responseJSON == null && !failure) {
            Thread.sleep(10);
        }

        int userID = responseJSON.getInt("user_id");
        String displayName = responseJSON.getString("first_name") +
                " " + responseJSON.getString("surname");
        String email = responseJSON.getString("email");
        return new LoggedInUser(userID, username, displayName, email, MainActivity.getLoggedInUser().getCookies());
    }

    public boolean isTokenValid(String accessToken) throws InterruptedException {
        String url = "http://10.0.2.2:8081/api/validateToken?clientID=2&accessToken=" + accessToken;

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        final String[] responseMessage = {null};

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Objects.requireNonNull(e).printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException(String.valueOf(response.code()));
                } else {
                    responseMessage[0] = Objects.requireNonNull(response.body()).string();
                }
            }
        });

        while (responseMessage[0] == null){
            Thread.sleep(10);
        }

        return responseMessage[0].equals("Access Token is valid");
    }
}
