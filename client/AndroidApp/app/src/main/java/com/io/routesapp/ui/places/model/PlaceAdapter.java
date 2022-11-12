package com.io.routesapp.ui.places.model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.io.routesapp.MainActivity;
import com.io.routesapp.R;
import com.io.routesapp.data.SharedRoutesPlacesRepository;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;

// Class converting ArrayList of Place objects into a RecyclerView
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private ArrayList<Place> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView placeNameView;
        private final TextView placeDescriptionView;
        private final RatingBar placeRatingBar;
        private int placeID;

        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.place_card_view);

            // Define click listener for the ViewHolder's View
            cardView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt("id", placeID);
                Navigation.findNavController(v).navigate(R.id.nav_place_information, bundle);
            });

            placeNameView = view.findViewById(R.id.place_name);
            placeDescriptionView = view.findViewById(R.id.place_description);
            placeRatingBar = view.findViewById(R.id.place_rating_bar);

            cardView.setOnLongClickListener(v -> {
                Place place = null;
                try {
                    place = MainActivity.HTTPClient.getPlace(placeID);
                } catch (JSONException | InterruptedException e) {
                    e.printStackTrace();
                }

                assert place != null;
                if (!SharedRoutesPlacesRepository.getFavouritePlacesNames().contains(place.name)) {
                    SharedRoutesPlacesRepository.favouritePlaces.add(place);
                }
                else{
                    int index = SharedRoutesPlacesRepository.getFavouritePlacesNames().indexOf(place.name);
                    SharedRoutesPlacesRepository.favouritePlaces.remove(SharedRoutesPlacesRepository.favouritePlaces.get(index));
                }
                Navigation.findNavController(v).navigate(R.id.nav_my_fav_places);
                return true;
            });
        }

        public TextView getPlaceNameView() {
            return placeNameView;
        }

        public TextView getPlaceDescriptionView() {
            return placeDescriptionView;
        }

        public void setPlaceID(int id){ placeID = id; }

        public void setRating(float rating){
            placeRatingBar.setRating(rating);
        }

    }

    public PlaceAdapter(ArrayList<Place> dataSet) {
        localDataSet = dataSet;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_place, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getPlaceNameView().setText(localDataSet.get(position).getName());
        viewHolder.getPlaceDescriptionView().setText(localDataSet.get(position).getDescription());
        viewHolder.setPlaceID(localDataSet.get(position).getId());
        viewHolder.setRating(
                (float) localDataSet.get(position).accumulatedScore/localDataSet.get(position).usersVoted
        );
    }

    @Override
    public int getItemCount() {
        if (localDataSet.isEmpty()){ return 0; }
        return localDataSet.size();
    }
}
