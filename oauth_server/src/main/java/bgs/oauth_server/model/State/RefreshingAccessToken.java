package bgs.oauth_server.model.State;

import bgs.oauth_server.dao.*;
import bgs.oauth_server.token.*;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;

import java.sql.*;
import java.util.*;

@Service("RefreshingAccessToken")
public class RefreshingAccessToken implements State {

    @Autowired
    private AccessTokensAccessService accessTokensAccessService;

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("RefreshingAccessToken");

        // pobieram 'refreshToken' i 'clientID' z 'params'
        String refreshToken = params.get("refreshToken");
        long clientID = Long.parseLong(params.get("clientID"));
        long appSecret = Long.parseLong(params.get("appSecret"));

        //dekoduję z otrzymanego tokenu accessTokenID
        TokenDecoder tokenDecoder = new TokenDecoder();
        Claims claims = tokenDecoder.decodeToken(refreshToken, Long.toString(appSecret));
        Long accessTokenID = Long.parseLong(claims.get("access_token_id").toString());

        // pobieram z bazy danych userID i dodaję do params
        Long userID = accessTokensAccessService.readById(accessTokenID).getUser().getId();
        params.put("userID", userID.toString());

        // usuwam były accessToken
        accessTokensAccessService.remove(accessTokensAccessService.readById(accessTokenID));

        // zmieniam stan na CreatingAccessToken
        context.changeState(new CreatingAccessToken());
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "RefreshingAccessToken";
    }
}
