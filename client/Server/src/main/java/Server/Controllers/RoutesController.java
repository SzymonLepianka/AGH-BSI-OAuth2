package Server.Controllers;

import Server.Domain.*;
import Server.Model.BuildJson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(path = "/routes")
public class RoutesController {
    @Autowired
    private RoutesRepository routesRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PlacesRepository placesRepository;
    @Autowired
    private PointsOfRoutesRepository pointsOfRoutesRepository;
    @Autowired
    private RouteCommentsRepository routeCommentsRepository;

    @GetMapping(path = "")
    public @ResponseBody
    String getAllRoutes() {
        var routes = routesRepository.findAll();
        var response = new JSONObject();
        for (var route : routes) {
            response.put(route.getId().toString(), BuildJson.buildJsonRoute(route));
        }
        return response.toString();
    }


    @GetMapping(path = "/{id}")
    public @ResponseBody
    String getRoute(@PathVariable String id) {
        var dbResponse = routesRepository.findById(Long.parseLong(id));
        if (dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
        }
        var route = dbResponse.get();
        var response = new JSONObject();
        response.put(route.getId().toString(), BuildJson.buildJsonRoute(route));
        return response.toString();
    }

    @PostMapping(path = "")
    public @ResponseBody
    String addRoute(@RequestParam String ispublic, @RequestParam String userID) {
        var route = new Route();
        route.setPublic(Boolean.parseBoolean(ispublic));
        var dbResponse = usersRepository.findById(Long.parseLong(userID));
        if (dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        route.setUser(dbResponse.get());
        this.routesRepository.save(route);
        return "ok";
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    String deleteRoute(@PathVariable String id) {
        var dbResponse = routesRepository.findById(Long.parseLong(id));
        if (dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
        }
        var route = dbResponse.get();
        routesRepository.delete(route);
        return "ok";
    }

    @GetMapping(path = "/{id}/pointsOfRoute")
    public @ResponseBody
    String getAllPoints(@PathVariable String id) {
        var dbResponse = this.routesRepository.findById(Long.parseLong(id));
        if (dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
        }
        var route = dbResponse.get();
        var points = route.getPointsOfRoutes();
        var response = new JSONObject();
        for (var point : points) {
            response.put(point.getId().toString(), BuildJson.buildJsonPointsOfRoute(point));
        }
        return response.toString();
    }

    @PostMapping(path = "/{routeId}/pointsOfRoute")
    public @ResponseBody
    String addPoint(@PathVariable String routeId, @RequestParam String pointNumber, @RequestParam String placeID) {
        var dbResponseRoute = this.routesRepository.findById(Long.parseLong(routeId));
        if (dbResponseRoute.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
        }
        var dbResponsePlace = this.placesRepository.findById(Long.parseLong(placeID));
        if (dbResponsePlace.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        var point = new PointOfRoute();
        point.setPlace(dbResponsePlace.get());
        point.setRoute(dbResponseRoute.get());
        point.setPointNumber(Long.parseLong(pointNumber));
        pointsOfRoutesRepository.save(point);
        return "ok";
    }

    @DeleteMapping(path = "/{routeId}/PointsOfRoute/{pointId}")
    public @ResponseBody
    String deletePoint(@PathVariable String pointId, @PathVariable String routeId) {
        var dbResponseRoute = this.routesRepository.findById(Long.parseLong(routeId));
        if (dbResponseRoute.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
        }
        var dbResponsePoint = this.pointsOfRoutesRepository.findById(Long.parseLong(pointId));
        if (dbResponsePoint.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Point not found");
        }
        var point = dbResponsePoint.get();
        this.pointsOfRoutesRepository.delete(point);
        return "ok";
    }

    @GetMapping(path = "/{id}/comments")
    public @ResponseBody
    String getRouteComments(@PathVariable String id) {
        var dbResponse = routesRepository.findById(Long.parseLong(id));
        if (dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
        }
        var route = dbResponse.get();
        var response = new JSONObject();
        for (var comment : route.getRouteComments()) {
            response.put(comment.getId().toString(), BuildJson.buildJsonRouteComment(comment));
        }
        return response.toString();
    }

    @PostMapping(path = "/{id}/comments")
    public @ResponseBody
    String addRouteComment(@PathVariable String id, @RequestParam String userId, @RequestParam String content) {
        var comment = new RouteComment();
        var dbResponseUser = this.usersRepository.findById(Long.parseLong(userId));
        if (dbResponseUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        comment.setUser(dbResponseUser.get());
        var dbResponseRoute = this.routesRepository.findById(Long.parseLong(id));
        if (dbResponseRoute.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
        }
        comment.setRoute(dbResponseRoute.get());
        comment.setContent(content);
        routeCommentsRepository.save(comment);
        return "ok";
    }

    @DeleteMapping(path = "/{id}/comments/{commentId}")
    public @ResponseBody
    String deleteRouteComment(@PathVariable String commentId) {
        var dbResponse = this.routeCommentsRepository.findById(Long.parseLong(commentId));
        if (dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        var comment = dbResponse.get();
        this.routeCommentsRepository.delete(comment);
        return "ok";
    }
}
