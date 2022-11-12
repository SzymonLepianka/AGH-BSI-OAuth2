package bgs.oauth_server.controllers;

import bgs.oauth_server.dao.*;
import bgs.oauth_server.domain.*;
import bgs.oauth_server.model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.*;

import javax.servlet.http.*;
import java.sql.Date;
import java.sql.*;
import java.util.*;

@Controller
@RequestMapping("/testApi")
public class TestAPIController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public @ResponseBody String getAllUsers() throws SQLException {
        IDatabaseEditor dbEditor = DatabaseEditor.getInstance();
        StringBuilder sb = new StringBuilder();
        List<User> users = dbEditor.getUsersAccessObject().readAll();
        for (var user : users) {
            sb.append(user.getFirstName());
        }
        return sb.toString();
    }

    @GetMapping("/users/add")
    public @ResponseBody String addUser(@RequestParam String birth_date,
                                        @RequestParam String email,
                                        @RequestParam String first_name,
                                        @RequestParam Boolean is_developer,
                                        @RequestParam String password,
                                        @RequestParam String phone_number,
                                        @RequestParam String surname,
                                        @RequestParam String username) throws SQLException {
        User newUser = new User();
        var birth_dateSQL = Date.valueOf(birth_date);
        newUser.setBirthDate(birth_dateSQL);
        newUser.setEmail(email);
        newUser.setFirstName(first_name);
        newUser.setDeveloper(is_developer);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setPhoneNumber(phone_number);
        newUser.setSurname(surname);
        newUser.setUsername(username);
        IDatabaseEditor db = DatabaseEditor.getInstance();
        db.getUsersAccessObject().create(newUser);
        return "Stworzono użytkownika";
    }

    @GetMapping("/clients")
    public @ResponseBody String getAllApps() throws SQLException {
        IDatabaseEditor dbEditor = DatabaseEditor.getInstance();
        StringBuilder sb = new StringBuilder();
        List<ClientApp> clientAppList = dbEditor.getAppsAccessObject().readAll();
        for (var clientApp : clientAppList) {
            sb.append(clientApp.getRedirectURL());
        }
        return sb.toString();
    }

    @GetMapping("/clients/add")
    public @ResponseBody String addClient() throws SQLException {
        ClientApp clientApp = new ClientApp();
        IDatabaseEditor db = DatabaseEditor.getInstance();
        User user = db.getUsersAccessObject().readById(2L);
        clientApp.setUser(user);
        clientApp.setAppSecret(9878654123L);
        clientApp.setRedirectURL("onet.pl/xd");
        db.getAppsAccessObject().create(clientApp);
        return "new application added!";
    }

    @GetMapping("/authorizationTest")
    public  @ResponseBody String authorizationTest(HttpServletResponse httpServletResponse) throws SQLException {
        try {
            Authorization.Authorize(httpServletResponse);
        } catch (ResponseStatusException exception) {
            if (exception.getStatus() != HttpStatus.UNAUTHORIZED) {
                exception.printStackTrace();
            }
        }
        return "ok";
    }
}