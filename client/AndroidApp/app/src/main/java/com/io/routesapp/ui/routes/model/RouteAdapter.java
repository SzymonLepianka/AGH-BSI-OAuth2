package com.io.routesapp.ui.routes.model;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.io.routesapp.R;
import com.io.routesapp.data.SharedRoutesPlacesRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private ArrayList<Route> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView routeNameView;
        private final TextView routeDescriptionView;
        private int routeID;
        private final RatingBar routeRatingBar;

        public ViewHolder(View view) {
            super(view);
            routeNameView = view.findViewById(R.id.route_name);
            routeDescriptionView = view.findViewById(R.id.route_description);
            routeRatingBar = view.findViewById(R.id.route_rating_bar);
            CardView cardView = view.findViewById(R.id.route_card_view);
            cardView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt("id", routeID);
                Navigation.findNavController(v).navigate(R.id.nav_route_information, bundle);
            });
            cardView.setOnLongClickListener(v -> {
                Route route = new Route(routeNameView.getText().toString());
                if (!SharedRoutesPlacesRepository.getFavouriteRoutesNames().contains(route.name)) {
                    SharedRoutesPlacesRepository.favouriteRoutes.add(route);
                }
                else{
                    int index = SharedRoutesPlacesRepository.getFavouriteRoutesNames().indexOf(route.name);
                    SharedRoutesPlacesRepository.favouriteRoutes.remove(
                            SharedRoutesPlacesRepository.favouriteRoutes.get(index)
                    );
                }
                Navigation.findNavController(v).navigate(R.id.nav_my_fav_routes);
                return true;
            });
        }

        public TextView getRouteNameView() {
            return routeNameView;
        }

        public TextView getRouteDescriptionView() {
            return routeDescriptionView;
        }

        public void setRouteID(int id) { this.routeID = id; }

        public void setRating(float rating){
            routeRatingBar.setRating(rating);
        }
    }

    public RouteAdapter(ArrayList<Route> dataSet) {
        localDataSet = dataSet;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_route, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getRouteNameView().setText(localDataSet.get(position).getName());
        viewHolder.getRouteDescriptionView().setText(localDataSet.get(position).generateDescription());
        viewHolder.setRouteID(localDataSet.get(position).getId());
        viewHolder.setRating(
                (float) localDataSet.get(position).accumulatedScore/localDataSet.get(position).usersVoted
        );
    }

    @Override
    public int getItemCount() {
        if (localDataSet.isEmpty()){return 0;}
        return localDataSet.size();
    }
}