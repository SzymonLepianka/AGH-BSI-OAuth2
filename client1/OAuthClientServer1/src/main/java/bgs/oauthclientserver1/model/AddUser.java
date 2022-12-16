package bgs.oauthclientserver1.model;

import bgs.oauthclientserver1.access_services.*;
import bgs.oauthclientserver1.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.math.*;
import java.security.*;
import java.sql.*;
import java.text.*;

@Service("AddUser")
public class AddUser {

    @Autowired
    private UsersAccessService usersAccessService;

    public boolean addUser(String username, String email, String firstName, String surname, String birthDate) {
        User n = new User();
        n.setUsername(username);
        n.setEmail(email);
        n.setFirstName(firstName);
        n.setSurname(surname);
        try {
            n.setBirthDate(new SimpleDateFormat("dd.MM.yyyy").parse(birthDate));
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date parse error");
        }
        usersAccessService.create(n);
        return true;
    }
}
