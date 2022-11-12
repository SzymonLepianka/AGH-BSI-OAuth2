package Server.Model;

import Server.Domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public class DataFromDB {

    public static User getUserFromDB(String user_id, UsersRepository usersRepository) {
        var dbResponseUser = usersRepository.findById(Long.parseLong(user_id));
        if (dbResponseUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return dbResponseUser.get();
    }

    public static Route getRouteFromDB(String route_id, RoutesRepository routesRepository) {
        var dbResponseRoute = routesRepository.findById(Long.parseLong(route_id));
        if (dbResponseRoute.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
        }
        return dbResponseRoute.get();
    }

    public static Place getPlaceFromDB(String place_id, PlacesRepository placesRepository) {
        var dbResponsePlace = placesRepository.findById(Long.parseLong(place_id));
        if (dbResponsePlace.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        return dbResponsePlace.get();
    }

    public static String getUsernameById(String id, UsersRepository usersRepository) {
        Optional<User> byId = usersRepository.findById(Long.parseLong(id));
        if (byId.isPresent()) {
            return byId.get().getUsername();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no user with the given id");
        }
    }
}
