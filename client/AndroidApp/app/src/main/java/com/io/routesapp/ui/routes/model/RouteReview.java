package com.io.routesapp.ui.routes.model;

import com.io.routesapp.ui.reviews.Review;

public class RouteReview extends Review {
    String routeID;

    public RouteReview(String routeID, String authorID, String content) {
        this.routeID = routeID;
        this.setAuthorID(authorID);
        this.setContent(content);
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

}
