package bgs.oauth_server.model.State;

import org.springframework.http.*;
import org.springframework.web.server.*;

import java.sql.*;
import java.util.*;

public class Failure extends State {

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("Failure");

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Other failure (Failure)");
    }

    @Override
    public String toString() {
        return "Failure";
    }

}
