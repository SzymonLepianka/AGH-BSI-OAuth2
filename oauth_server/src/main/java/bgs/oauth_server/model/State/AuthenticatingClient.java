package bgs.oauth_server.model.State;

import bgs.oauth_server.access_services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.util.*;

@Service("AuthenticatingClient")
public class AuthenticatingClient implements State {

    @Autowired
    private AppsAccessService appsAccessService;
    @Autowired
    private VerifyingDataFromClient verifyingDataFromClient;

    @Override
    public Response handle(Map<String, String> params) throws ResponseStatusException {

        System.out.println("AuthenticatingClient");

        // sprawdzam czy klient o danych clientId w params istnieje w bazie danych
        if (appsAccessService.readById((Integer.parseInt(params.get("clientID")))) != null) {
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client with clientID=" + params.get("clientID") + " does not exist");
        }
        // wywołuję VerifyingDataFromClient w przypadku powodzenia
        return verifyingDataFromClient.handle(params);
    }

    @Override
    public String toString() {
        return "AuthenticatingClient";
    }
}
