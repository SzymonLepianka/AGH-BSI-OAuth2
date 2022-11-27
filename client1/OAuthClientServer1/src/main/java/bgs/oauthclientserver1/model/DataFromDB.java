package bgs.oauthclientserver1.model;

import bgs.oauthclientserver1.access_services.*;
import bgs.oauthclientserver1.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

@Service("DataFromDB")
public class DataFromDB {

    @Autowired
    private UsersAccessService usersAccessService;

    public User getUserFromDB(String user_id) {
        var dbResponseUser = usersAccessService.readById(Integer.parseInt(user_id));
        if (dbResponseUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return dbResponseUser;
    }

    public String getUsernameById(String id) {
        User user = usersAccessService.readById(Integer.parseInt(id));
        if (user != null) {
            return user.getUsername();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no user with the given id");
        }
    }
}
