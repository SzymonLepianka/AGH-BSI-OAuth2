package bgs.oauth_server.model.State;

import bgs.oauth_server.access_services.*;
import bgs.oauth_server.token.*;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service("RefreshingAccessToken")
public class RefreshingAccessToken implements State {

    @Autowired
    private AccessTokensAccessService accessTokensAccessService;
    @Autowired
    private CreatingAccessToken creatingAccessToken;

    @Override
    public Response handle(Map<String, String> params) {
        System.out.println("RefreshingAccessToken");

        // pobieram 'refreshToken' i 'clientID' z 'params'
        String refreshToken = params.get("refreshToken");
        Integer clientID = Integer.parseInt(params.get("clientID"));
        Integer appSecret = Integer.parseInt(params.get("appSecret"));

        //dekoduję z otrzymanego tokenu accessTokenID
        TokenDecoder tokenDecoder = new TokenDecoder();
        Claims claims = tokenDecoder.decodeToken(refreshToken, Integer.toString(appSecret));
        Integer accessTokenID = Integer.parseInt(claims.get("access_token_id").toString());

        // pobieram z bazy danych userID i dodaję do params
        Integer userID = accessTokensAccessService.readById(accessTokenID).getUser().getUserId();
        params.put("userID", userID.toString());

        // usuwam były accessToken
        accessTokensAccessService.remove(accessTokensAccessService.readById(accessTokenID));

        // zmieniam stan na CreatingAccessToken
        return creatingAccessToken.handle(params);
    }

    @Override
    public String toString() {
        return "RefreshingAccessToken";
    }
}
