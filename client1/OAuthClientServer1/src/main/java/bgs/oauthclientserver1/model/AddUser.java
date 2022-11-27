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

    private String hashPassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Password hashing problem");
        }
    }

    public boolean addUser(String username, String password, String email, String firstName, String surname, String birthDate) {
        User n = new User();
        n.setUsername(username);
        n.setPassword(hashPassword(password));
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
