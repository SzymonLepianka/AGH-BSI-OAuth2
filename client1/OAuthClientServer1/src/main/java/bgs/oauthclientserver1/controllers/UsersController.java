package bgs.oauthclientserver1.controllers;


import bgs.oauthclientserver1.access_services.*;
import bgs.oauthclientserver1.domain.*;
import bgs.oauthclientserver1.model.*;
import bgs.oauthclientserver1.view.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.io.*;
import java.text.*;

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
    public @ResponseBody String getUser(HttpServletResponse httpServletResponse) throws IOException, InterruptedException, ParseException {

        //autoryzacja
        String accessToken = authorization.authorize(httpServletResponse);

        User userData = getUserData.getUserData(accessToken);
        return view.getUserData(userData);
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
