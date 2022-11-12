package com.io.routesapp.ui.places.discoverPlaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.io.routesapp.MainActivity;
import com.io.routesapp.R;
import com.io.routesapp.ui.places.model.Place;
import com.io.routesapp.ui.places.model.PlaceAdapter;
import org.json.JSONException;

import java.util.ArrayList;


public class DiscoverPlacesFragment extends Fragment {

    RecyclerView mRecyclerView;
    PlaceAdapter placeAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Place> placesList = new ArrayList<>();
    FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getPlacesFromDataSource();
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discover_places, container, false);

        mRecyclerView = root.findViewById(R.id.places_list);
        layoutManager = new LinearLayoutManager(getActivity());

        placeAdapter = new PlaceAdapter(placesList);
        mRecyclerView.setAdapter(placeAdapter);

        fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_nav_discover_places_to_discover_places_map)
        );

        return root;
    }


    private void getPlacesFromDataSource() throws JSONException, InterruptedException {
        placesList = MainActivity.HTTPClient.getPlaces();
    }

}