package com.io.routesapp.ui.fav_places;

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
import com.io.routesapp.R;
import com.io.routesapp.data.SharedRoutesPlacesRepository;
import com.io.routesapp.ui.places.model.PlaceAdapter;

// fragment displaying user's favourite places as a list
public class FavouritePlacesFragment extends Fragment {
    RecyclerView mRecyclerView;
    PlaceAdapter placeAdapter;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourite_places, container, false);

        mRecyclerView = root.findViewById(R.id.places_list);
        layoutManager = new LinearLayoutManager(getActivity());

        placeAdapter = new PlaceAdapter(SharedRoutesPlacesRepository.favouritePlaces);
        mRecyclerView.setAdapter(placeAdapter);

        fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_my_fav_places_to_nav_fav_places_map));
        return root;
    }

}