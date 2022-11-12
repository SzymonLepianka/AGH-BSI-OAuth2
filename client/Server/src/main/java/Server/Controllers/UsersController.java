package Server.Controllers;

import Server.Domain.*;
import Server.Model.AddUser;
import Server.Model.Authorization;
import Server.Model.DataFromDB;
import Server.Model.GetUserData;
import Server.View.APIView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@Controller
@RequestMapping(path = "/users")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PlacesRepository placesRepository;

    @Autowired
    private RoutesRepository routesRepository;

    @Autowired
    private UserPlaceVotesRepository userPlaceVotesRepository;

    @Autowired
    private UserRouteVotesRepository userRouteVotesRepository;

    private final APIView view;

    public UsersController() {
        this.view = new APIView();
    }

    @GetMapping(path = "/{username}")
    public @ResponseBody
    String getUser(@PathVariable String username, HttpServletResponse httpServletResponse) throws IOException, InterruptedException, ParseException {

        //autoryzacja
        String accessToken = Authorization.Authorize(username, httpServletResponse);

        User userData = GetUserData.getUserData(username, accessToken, usersRepository, httpServletResponse);
        return view.getUserData(userData);
    }

    @PostMapping(path = "/add")
    public @ResponseBody
    String addUser(@RequestParam String username, @RequestParam String password, @RequestParam String email,
                   @RequestParam String firstName, @RequestParam String surname, @RequestParam String birthDate) {
        boolean b = AddUser.addUser(username, password, email, firstName, surname, birthDate, usersRepository);
        if (b) {
            return "Saved";
        } else {
            return "Not saved";
        }
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody
    String deleteUser(@PathVariable String id, HttpServletResponse httpServletResponse) throws IOException, InterruptedException {

        //autoryzacja
        String username = DataFromDB.getUsernameById(id, usersRepository);
        Authorization.Authorize(username, httpServletResponse);

        var user = DataFromDB.getUserFromDB(id, usersRepository);
        this.usersRepository.delete(user);
        return "ok";
    }

    @GetMapping(path = "/{user_id}/placeVotes/{place_id}")
    public @ResponseBody
    String didUserVoteForPlace(@PathVariable String user_id, @PathVariable String place_id, HttpServletResponse httpServletResponse) throws IOException, InterruptedException {

        //autoryzacja
        String username = DataFromDB.getUsernameById(user_id, usersRepository);
        Authorization.Authorize(username, httpServletResponse);

        var user = DataFromDB.getUserFromDB(user_id, usersRepository);
        var place = DataFromDB.getPlaceFromDB(place_id, placesRepository);

        var result = false;
        for (var votePlace : user.getUserPlaceVotes()) {
            if (votePlace.getPlace() == place) {
                result = true;
                break;
            }
        }
        return String.valueOf(result);
    }

    @PostMapping(path = "/{user_id}/placeVotes/{place_id}")
    public @ResponseBody
    String userVoteForPlace(@PathVariable String user_id, @PathVariable String place_id, @RequestParam String vote, HttpServletResponse httpServletResponse) throws IOException, InterruptedException {

        //autoryzacja
        String username = DataFromDB.getUsernameById(user_id, usersRepository);
        Authorization.Authorize(username, httpServletResponse);

        var user = DataFromDB.getUserFromDB(user_id, usersRepository);
        var place = DataFromDB.getPlaceFromDB(place_id, placesRepository);

        for (var votePlace : user.getUserPlaceVotes()) {
            if (votePlace.getPlace() == place) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already voted for that place");
            }
        }
        var placeUserVotedFor = new UserPlaceVote();
        place.setAccumulatedScore(place.getAccumulatedScore() + Integer.parseInt(vote));
        place.setUsersVoted(place.getUsersVoted() + 1);
        placeUserVotedFor.setPlace(place);
        placeUserVotedFor.setUser(user);
        this.placesRepository.save(place);
        this.userPlaceVotesRepository.save(placeUserVotedFor);
        return "ok";
    }

    @GetMapping(path = "/{user_id}/routeVotes/{route_id}")
    public @ResponseBody
    String didUserVoteForRoute(@PathVariable String user_id, @PathVariable String route_id, HttpServletResponse httpServletResponse) throws IOException, InterruptedException {

        //autoryzacja
        String username = DataFromDB.getUsernameById(user_id, usersRepository);
        Authorization.Authorize(username, httpServletResponse);

        var user = DataFromDB.getUserFromDB(user_id, usersRepository);
        var route = DataFromDB.getRouteFromDB(route_id, routesRepository);

        var result = false;
        for (var voteRoute : user.getUserRouteVotes()) {
            if (voteRoute.getRoute() == route) {
                result = true;
                break;
            }
        }
        return String.valueOf(result);
    }

    @PostMapping(path = "/{user_id}/routeVotes/{route_id}")
    public @ResponseBody
    String userVoteForRoute(@PathVariable String user_id, @PathVariable String route_id, @RequestParam String vote, HttpServletResponse httpServletResponse) throws IOException, InterruptedException {

        //autoryzacja
        String username = DataFromDB.getUsernameById(user_id, usersRepository);
        Authorization.Authorize(username, httpServletResponse);

        var user = DataFromDB.getUserFromDB(user_id, usersRepository);
        var route = DataFromDB.getRouteFromDB(route_id, routesRepository);

        for (var voteRoute : user.getUserRouteVotes()) {
            if (voteRoute.getRoute() == route) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already voted for that route");
            }
        }
        var routeUserVotedFor = new UserRouteVote();
        route.setAccumulatedScore(route.getAccumulatedScore() + Integer.parseInt(vote));
        route.setUsersVoted(route.getUsersVoted() + 1);
        routeUserVotedFor.setRoute(route);
        routeUserVotedFor.setUser(user);
        this.routesRepository.save(route);
        this.userRouteVotesRepository.save(routeUserVotedFor);
        return "ok";
    }
}
