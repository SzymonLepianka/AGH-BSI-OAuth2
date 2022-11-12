package com.io.routesapp.ui.fav_places;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.io.routesapp.MainActivity;
import com.io.routesapp.ui.places.model.Place;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import com.io.routesapp.R;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

// fragment displaying user's favourite places on a map
public class FavouritePlacesMapFragment extends Fragment {
        ArrayList<Place> placesList;
        ArrayList<LatLng> placesCoordinates;
        ArrayList<String> placesNames;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            placesCoordinates = new ArrayList<>();
            placesNames = new ArrayList<>();
        }

        private OnMapReadyCallback callback = new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                for (int i = 0; i < placesCoordinates.size(); i++){
                    LatLng coordinates = placesCoordinates.get(i);
                    googleMap.addMarker(new MarkerOptions().position(coordinates).title(placesNames.get(i)));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12));
                }
            }
        };

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            placesList = MainActivity.HTTPClient.getFavouritePlaces(1);
            getPlacesNamesAndCoordinates(placesList,
                    placesNames, placesCoordinates);
            return inflater.inflate(R.layout.fragment_discover_places_map, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
        }

    private void getPlacesNamesAndCoordinates(@NotNull ArrayList<Place> placesList, ArrayList<String> placesNames, ArrayList<LatLng> placesCoordinates){
        for (Place place : placesList){
            placesCoordinates.add(new LatLng(place.getLatitude(), place.getLongitude()));
            placesNames.add(place.getName());
        }
    }
    }
