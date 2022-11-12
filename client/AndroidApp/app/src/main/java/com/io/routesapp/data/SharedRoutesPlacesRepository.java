package com.io.routesapp.data;

import android.text.style.UpdateLayout;

import com.io.routesapp.ui.places.model.Place;
import com.io.routesapp.ui.places.model.PlaceReview;
import com.io.routesapp.ui.routes.model.Route;
import com.io.routesapp.ui.routes.model.RouteReview;

import java.util.ArrayList;

public class SharedRoutesPlacesRepository {
    public static ArrayList<Place> placesAvailable;
    public static ArrayList<Route> routesAvailable;
    public static ArrayList<PlaceReview> placeReviews;
    public static ArrayList<RouteReview> routeReviews;
    public static ArrayList<Place> favouritePlaces;
    public static ArrayList<Route> favouriteRoutes;

    public SharedRoutesPlacesRepository() {
        this.init();
    }

    public void init(){
        favouritePlaces = new ArrayList<>();
        favouriteRoutes = new ArrayList<>();
    }

    public static ArrayList<String> getFavouritePlacesNames(){
        ArrayList<String> names = new ArrayList<>();
        for (Place place: favouritePlaces){
            names.add(place.getName());
        }
        return names;
    }

    public static ArrayList<String> getFavouriteRoutesNames(){
        ArrayList<String> names = new ArrayList<>();
        for (Route route: favouriteRoutes){
            names.add(route.getName());
        }
        return names;
    }
}
