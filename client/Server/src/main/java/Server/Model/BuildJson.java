package Server.Model;

import Server.Domain.*;
import org.json.JSONObject;

public class BuildJson {
    public static JSONObject buildJsonPlace(Place place) {
        var placeJson = new JSONObject();
        placeJson
                .put("name", place.getName())
                .put("description", place.getDescription())
                .put("latitude", place.getLatitude())
                .put("longitude", place.getLongitude())
                .put("accumulatedScore", place.getAccumulatedScore())
                .put("usersVoted", place.getUsersVoted())
                .put("valid", place.getValidPlace());
        return placeJson;
    }

    public static JSONObject buildJsonComment(PlaceComment comment) {
        var commentJson = new JSONObject();
        commentJson
                .put("username", comment.getUser().getUsername())
                .put("content", comment.getContent());
        return commentJson;
    }

    public static JSONObject buildJsonRoute(Route route) {
        var routeJSON = new JSONObject();
        routeJSON
                .put("accumulatedScore", route.getAccumulatedScore())
                .put("public", route.getPublic())
                .put("usersVoted", route.getUsersVoted())
                .put("userID", route.getUser().getId());
        return routeJSON;
    }

    public static JSONObject buildJsonPointsOfRoute(PointOfRoute pointOfRoute) {
        var pointsJSON = new JSONObject();
        pointsJSON
                .put("pointNumber", pointOfRoute.getPointNumber())
                .put("routeId", pointOfRoute.getRoute().getId())
                .put("placeId", pointOfRoute.getPlace().getId());
        return pointsJSON;
    }

    public static JSONObject buildJsonRouteComment(RouteComment routeComment) {
        var pointsJSON = new JSONObject();
        pointsJSON
                .put("routeId", routeComment.getRoute().getId())
                .put("userId", routeComment.getUser().getId())
                .put("content", routeComment.getContent());
        return pointsJSON;
    }

}
