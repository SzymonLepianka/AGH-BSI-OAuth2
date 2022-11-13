package Group28.OAuth.Model.State;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.token.TokenDecoder;
import io.jsonwebtoken.Claims;

import java.sql.SQLException;
import java.util.Map;

public class RefreshingAccessToken extends State {

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
        IDatabaseEditor db = DatabaseEditor.getInstance();
        Long userID = db.getAccessTokensAccessObject().readById(accessTokenID).getUser().getId();
        params.put("userID", userID.toString());

        // usuwam były accessToken
        db.getAccessTokensAccessObject().remove(db.getAccessTokensAccessObject().readById(accessTokenID));

        // zmieniam stan na CreatingAccessToken
        context.changeState(new CreatingAccessToken());
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "RefreshingAccessToken";
    }
}
