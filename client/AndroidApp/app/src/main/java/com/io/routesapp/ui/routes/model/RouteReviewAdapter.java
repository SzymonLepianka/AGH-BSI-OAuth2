package com.io.routesapp.ui.routes.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.io.routesapp.R;
import com.io.routesapp.ui.places.model.PlaceReview;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class RouteReviewAdapter extends RecyclerView.Adapter<com.io.routesapp.ui.places.model.PlaceReviewAdapter.ViewHolder> {
    private ArrayList<RouteReview> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView authorNameTextView;
        private final TextView contentTextView;
        private final RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            authorNameTextView = view.findViewById(R.id.author_name);
            ratingBar = view.findViewById(R.id.author_rating);
            contentTextView = view.findViewById(R.id.review_content);
        }

        public TextView getAuthorNameTextView() {
            return authorNameTextView;
        }

        public TextView getContentTextView() {
            return contentTextView;
        }

        public RatingBar getRatingBar() {
            return ratingBar;
        }
    }

    public RouteReviewAdapter(ArrayList<RouteReview> dataSet) {
        localDataSet = dataSet;
    }

    @NotNull
    @Override
    public com.io.routesapp.ui.places.model.PlaceReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_review, viewGroup, false);

        return new com.io.routesapp.ui.places.model.PlaceReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.io.routesapp.ui.places.model.PlaceReviewAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.getAuthorNameTextView().setText(localDataSet.get(position).getAuthorID());
        viewHolder.getRatingBar().setIsIndicator(true);
        viewHolder.getRatingBar().setRating((float) 4.0); //TODO get rating
        viewHolder.getContentTextView().setText(localDataSet.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (localDataSet.isEmpty()) {return 0;}
        return localDataSet.size();
    }
}
