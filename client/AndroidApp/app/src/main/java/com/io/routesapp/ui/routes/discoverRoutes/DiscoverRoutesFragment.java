package com.io.routesapp.ui.routes.discoverRoutes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.io.routesapp.MainActivity;
import com.io.routesapp.R;
import com.io.routesapp.data.SharedRoutesPlacesRepository;
import com.io.routesapp.ui.routes.model.Route;
import com.io.routesapp.ui.routes.model.RouteAdapter;

import org.json.JSONException;

import java.util.ArrayList;

//Class displaying list of routes to the user
public class DiscoverRoutesFragment extends Fragment {

    RecyclerView mRecyclerView;
    RouteAdapter routeAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Route> routesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getRoutesFromDataSource();
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discover_routes, container, false);
        mRecyclerView = root.findViewById(R.id.routes_list);
        layoutManager = new LinearLayoutManager(getActivity());

        routeAdapter = new RouteAdapter(routesList);
        mRecyclerView.setAdapter(routeAdapter);

        return root;
    }

    public void getRoutesFromDataSource() throws JSONException, InterruptedException {
        routesList = MainActivity.HTTPClient.getRoutes();
    }

}