package Group28.OAuth.Model.State;

import java.sql.SQLException;
import java.util.Map;

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
