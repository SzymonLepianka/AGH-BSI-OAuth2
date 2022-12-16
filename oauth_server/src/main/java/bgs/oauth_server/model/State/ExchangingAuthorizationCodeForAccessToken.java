package bgs.oauth_server.model.State;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.sql.*;
import java.util.*;

@Service("ExchangingAuthorizationCodeForAccessToken")
public class ExchangingAuthorizationCodeForAccessToken implements State {

    @Autowired
    private CreatingAccessToken creatingAccessToken;

    @Override
    public Response handle(Map<String, String> params)   {

        System.out.println("ExchangingAuthorizationCodeForAccessToken");

        // ustawiam stan na CreatingAccessToken
//        context.changeState(new CreatingAccessToken());
        return creatingAccessToken.handle(params);
    }

    @Override
    public String toString() {
        return "ExchangingAuthorizationCodeForAccessToken";
    }
}
