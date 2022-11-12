package com.io.routesapp.ui.places.model;

import com.io.routesapp.ui.reviews.Review;

public class PlaceReview extends Review {
    String placeID;

    public PlaceReview(String placeID, String authorID, String content) {
        this.placeID = placeID;
        this.setAuthorID(authorID);
        this.setContent(content);
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

}
