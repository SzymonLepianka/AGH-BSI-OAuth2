package bgs.oauth_server.model.State;

import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.sql.*;
import java.util.*;

@Service("Failure")
public class Failure implements State {

    @Override
    public Response handle(Map<String, String> params) throws SQLException {

        System.out.println("Failure");

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Other failure (Failure)");
    }

    @Override
    public String toString() {
        return "Failure";
    }

}
