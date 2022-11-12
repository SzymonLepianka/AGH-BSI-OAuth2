package Server.Controllers;

import Server.Domain.UsersRepository;
import Server.Model.AddUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//this is for testing
@Controller
@RequestMapping(path = "/demo")
public class TestController {
    @Autowired
    private UsersRepository usersRepository;

    @PostMapping(path = "/add")
    public @ResponseBody
    String addNewUser(@RequestParam String username, @RequestParam String password, @RequestParam String email,
                      @RequestParam String firstName, @RequestParam String surname, @RequestParam String birthDate) {
        boolean b = AddUser.addUser(username, password, email, firstName, surname, birthDate, usersRepository);
        if (b) {
            return "Saved";
        } else {
            return "Not saved";
        }
    }
}