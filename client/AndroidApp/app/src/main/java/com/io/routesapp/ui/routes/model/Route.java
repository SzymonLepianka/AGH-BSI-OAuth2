package com.io.routesapp.ui.routes.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.io.routesapp.ui.places.model.Place;

import java.util.ArrayList;

public class Route {
    int id;
    int accumulatedScore;
    int usersVoted;
    String name;
    Boolean isPublic;
    ArrayList<Place> placesOfRoute;

    public Route(int id, int accumulatedScore, int usersVoted, Boolean isPublic, ArrayList<Place> placesOfRoute) {
        this.id = id;
        this.accumulatedScore = accumulatedScore;
        this.usersVoted = usersVoted;
        this.isPublic = isPublic;
        this.placesOfRoute = placesOfRoute;
        this.name = "Route " + id;
    }

    public Route(int id) {
        placesOfRoute = new ArrayList<>();
        this.id = id;
        name = "Route " + id;
    }

    public Route(int id, int accumulatedScore){
        placesOfRoute = new ArrayList<>();
        this.id = id;
        name = "Route " + id;
        this.accumulatedScore = accumulatedScore;
    }

    //TODO remove this later
    public Route(String name){
        id = 12452;
        this.name = name;
        placesOfRoute = new ArrayList<>();
        placesOfRoute.add(new Place(
                0,
                "Wawel",
                true,50.054316,19.9350402,
                0,
                0,
                0,
                "Wawel Castle"));
        placesOfRoute.add(new Place(
                1,
                "Sukiennice",
                true,50.0613741,19.9361222,
                0,
                0,
                0,
                "The Cloth Hall"));
        placesOfRoute.add(new Place(
                2,
                "Barbakan Krakowski",
                true,50.065569, 19.941689,
                0,
                0,
                0,
                "Kraków Barbican"));
    }

    public Route(int id, int accumulatedScore, ArrayList<Place> pointsOfRoute) {
        this.id = id;
        this.accumulatedScore = accumulatedScore;
        this.placesOfRoute = pointsOfRoute;
        this.name = "Route " + id;
        this.isPublic = true;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAccumulatedScore() {
        return accumulatedScore;
    }

    public int getUsersVoted() {
        return usersVoted;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public ArrayList<Place> getPlacesOfRoute() {
        return placesOfRoute;
    }

    private ArrayList<String> getPlacesNames(){
        ArrayList<String> placesNames = new ArrayList<>();
        if (!placesOfRoute.isEmpty()){
            for (Place place : placesOfRoute){
                placesNames.add(place.getName());
            }
        }
        return placesNames;
    }

    public void setPlacesOfRoute(ArrayList<Place> placesOfRoute) {
        this.placesOfRoute = placesOfRoute;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String generateDescription(){
        return String.join(" -> ", this.getPlacesNames());
    }
}
