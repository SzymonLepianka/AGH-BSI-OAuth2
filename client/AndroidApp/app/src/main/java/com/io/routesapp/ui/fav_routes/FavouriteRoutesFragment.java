package com.io.routesapp.ui.fav_routes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.io.routesapp.R;
import com.io.routesapp.data.SharedRoutesPlacesRepository;
import com.io.routesapp.ui.routes.model.RouteAdapter;


//fragment displaying user's favourite routes as a list
public class FavouriteRoutesFragment extends Fragment {

    RecyclerView mRecyclerView;
    RouteAdapter routeAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourite_routes, container, false);
        mRecyclerView = root.findViewById(R.id.routes_list);
        layoutManager = new LinearLayoutManager(getActivity());

        routeAdapter = new RouteAdapter(SharedRoutesPlacesRepository.favouriteRoutes);
        mRecyclerView.setAdapter(routeAdapter);

        return root;
    }
}