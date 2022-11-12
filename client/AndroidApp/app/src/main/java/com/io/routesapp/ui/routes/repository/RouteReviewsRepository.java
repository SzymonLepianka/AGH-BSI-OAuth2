package com.io.routesapp.ui.routes.repository;

import com.io.routesapp.ui.routes.model.RouteReview;

import java.util.ArrayList;

public class RouteReviewsRepository {
    ArrayList<RouteReview> reviewsList = new ArrayList<>();

    void init() {
        for (int i = 0; i < 5; i++) {
            reviewsList.add(new RouteReview(
                    "0", "", "I loved this route, although it was a bit too short!"
            ));
        }
    }

    public ArrayList<RouteReview> getReviewsList(){
        this.init();
        return reviewsList;
    }
}
