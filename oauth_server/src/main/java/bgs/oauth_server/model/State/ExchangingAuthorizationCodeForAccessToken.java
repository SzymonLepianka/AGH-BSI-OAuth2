package bgs.oauth_server.model.State;

import java.sql.*;
import java.util.*;

public class ExchangingAuthorizationCodeForAccessToken extends State {

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("ExchangingAuthorizationCodeForAccessToken");

        // ustawiam stan na CreatingAccessToken
        context.changeState(new CreatingAccessToken());

        return context.handle(params);
    }

    @Override
    public String toString() {
        return "ExchangingAuthorizationCodeForAccessToken";
    }
}
