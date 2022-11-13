package bgs.oauth_server.model.State;

import org.springframework.stereotype.*;

import java.sql.*;
import java.util.*;

@Service("ExchangingAuthorizationCodeForAccessToken")
public class ExchangingAuthorizationCodeForAccessToken implements State {

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
