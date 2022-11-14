package bgs.oauth_server.model.State;

import bgs.oauth_server.dao.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.sql.*;
import java.util.*;

@Service("AuthenticatingClient")
public class AuthenticatingClient implements State {

    @Autowired
    private AppsAccessService appsAccessService;

//    Singleton
//    private static AuthenticatingClient instance = new AuthenticatingClient();
//    private AuthenticatingClient() {}
//    public static AuthenticatingClient instance() {
//        return instance;
//    }
//
//    // Business logic and state transition
//    @Override
//    public void updateState(Context context,  Map<String, String> params)
//    {
////        System.out.println("AuthenticatingClient");
////        context.setCurrentState([nowy stan].instance());
//    }

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("AuthenticatingClient");

        // sprawdzam czy klient o danych clientId w params istnieje w bazie danych
        if (appsAccessService.readById((Integer.parseInt(params.get("clientID")))) != null) {
            context.changeState(new VerifyingDataFromClient());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client with clientID=" + params.get("clientID") + " does not exist");
        }
        // wywołuję VerifyingDataFromClient w przypadku powodzenia
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "AuthenticatingClient";
    }
}
