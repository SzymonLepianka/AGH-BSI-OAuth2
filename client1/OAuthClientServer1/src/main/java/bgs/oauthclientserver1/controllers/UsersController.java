package bgs.oauthclientserver1.controllers;


import bgs.oauthclientserver1.access_services.*;
import bgs.oauthclientserver1.domain.*;
import bgs.oauthclientserver1.model.*;
import bgs.oauthclientserver1.view.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.*;

import javax.servlet.http.*;
import java.io.*;

@Controller
@RequestMapping(path = "/users")
public class UsersController {

    private final APIView view;
    @Autowired
    private UsersAccessService usersAccessService;
    @Autowired
    private Authorization authorization;
    @Autowired
    private GetUserData getUserData;
    @Autowired
    private AddUser addUser;
    @Autowired
    private DataFromDB dataFromDB;

    public UsersController() {
        this.view = new APIView();
    }

    @GetMapping(path = "/getUserData")
    public @ResponseBody ResponseEntity<String> getUser(HttpServletResponse httpServletResponse) throws IOException, InterruptedException {

        //autoryzacja
        String accessToken = authorization.authorize(httpServletResponse);

        // pobieranie danych użytkownika
        try {
            User userData = getUserData.getUserData(accessToken);
            return new ResponseEntity<>(view.getUserData(userData), HttpStatus.OK);
        } catch (ResponseStatusException rse) {
            rse.printStackTrace();
            return new ResponseEntity<>(rse.getReason(), rse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addUser(@RequestParam String username, @RequestParam String password, @RequestParam String email, @RequestParam String firstName, @RequestParam String surname, @RequestParam String birthDate) {
        boolean b = addUser.addUser(username, password, email, firstName, surname, birthDate);
        if (b) {
            return "Saved";
        } else {
            return "Not saved";
        }
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody String deleteUser(@PathVariable String id, HttpServletResponse httpServletResponse) throws IOException, InterruptedException {

        //autoryzacja
        String username = dataFromDB.getUsernameById(id);
        authorization.authorizeOnUsername(username, httpServletResponse);

        var user = dataFromDB.getUserFromDB(id);
        usersAccessService.remove(user);
        return "ok";
    }
}
