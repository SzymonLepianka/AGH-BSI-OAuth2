package bgs.oauthclientserver1.controllers;

import bgs.oauthclientserver1.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

//this is for testing
@Controller
@RequestMapping(path = "/demo")
public class TestController {
    @Autowired
    private AddUser addUser;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewUser(@RequestParam String username, @RequestParam String password, @RequestParam String email, @RequestParam String firstName, @RequestParam String surname, @RequestParam String birthDate) {
        boolean b = addUser.addUser(username, password, email, firstName, surname, birthDate);
        if (b) {
            return "Saved";
        } else {
            return "Not saved";
        }
    }
}