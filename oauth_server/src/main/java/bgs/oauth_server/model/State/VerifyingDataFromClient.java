package bgs.oauth_server.model.State;


import bgs.oauth_server.dao.*;
import bgs.oauth_server.domain.*;
import bgs.oauth_server.token.*;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.sql.*;
import java.time.*;
import java.util.*;

@Service("VerifyingDataFromClient")
public class VerifyingDataFromClient implements State {

    @Autowired
    private AuthCodesAccessService authCodesAccessService;
    @Autowired
    private AppsAccessService appsAccessService;
    @Autowired
    private RefreshTokensAccessService refreshTokensAccessService;


    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("VerifyingDataFromClient");

        // jeśli params zawierają "scopes" wtedy CreatingAuthorizationCode
        if (params.containsKey("scopes") && params.containsKey("userID")) {
            context.changeState(new CreatingAuthorizationCode());

            // jeśli params zawierają "code" wtedy ExchangingAuthorizationCodeForAccessToken
        } else if (params.containsKey("code")) {

            // pobieram 'code' i 'clientID' z 'params'
            String code = params.get("code");
            Long clientID = Long.parseLong(params.get("clientID"));

            // pobieram z bazy danych AuthCodes i szukam przekazanego w params 'code'
            List<AuthCode> codesFromDataBase = authCodesAccessService.readAll();
            AuthCode authCode = codesFromDataBase.stream()
                    .filter(c -> code.equals(c.getContent()) && clientID.equals(c.getClientApp().getId()))
                    .findFirst()
                    .orElse(null);

            // jeśli udało się znaleźć AuthCode zmianiam stan na ExchangingAuthorizationCodeForAccessToken
            // w przyciwnym wypadku wyrzucam wyjątek
            if (authCode != null) {
                context.changeState(new ExchangingAuthorizationCodeForAccessToken());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Auth Code does not exist in data base");
            }

            // sprawdzam czy AuthCode nie jest revoked
            if (authCode.isRevoked()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Auth Code is revoked");
            }

            // sprawdzam czy AuthCode nie jest expired
            if (authCode.getExpiresAt().before(Timestamp.valueOf(LocalDateTime.now()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Auth Code is expired");
            }

        } else if (params.containsKey("refreshToken")) {

            // pobieram 'refreshToken' i 'clientID' z 'params'
            String refreshToken = params.get("refreshToken");
            Long clientID = Long.parseLong(params.get("clientID"));

            // czytam z danych danych appSecret clienta z danym clientID
            Long appSecret = appsAccessService.readById(clientID).getAppSecret();
            params.put("appSecret", appSecret.toString());

            //dekoduję z otrzymanego tokenu accessTokenID i expiration
            TokenDecoder tokenDecoder = new TokenDecoder();
            Claims claims = tokenDecoder.decodeToken(refreshToken, appSecret.toString());
            Long accessTokenID = Long.parseLong(claims.get("access_token_id").toString());
            // ustawiam format Timestamp
            String date = String.valueOf(claims.getExpiration().toInstant()).substring(0, 10);
            String time = String.valueOf(claims.getExpiration().toInstant()).substring(11, 19);
            Timestamp expiration = Timestamp.valueOf(Timestamp.valueOf(date + " " + time).toLocalDateTime().plusHours(1));

            // pobieram z bazy danych refreshTokens i szukam przekazanego w params 'refreshToken'
            List<RefreshToken> refreshTokens = refreshTokensAccessService.readAll();
            RefreshToken findRefreshToken = refreshTokens.stream()
                    .filter(rt -> accessTokenID.equals(rt.getAccessToken().getId()) && (expiration.equals(rt.getExpiresAt()) || Timestamp.valueOf(expiration.toLocalDateTime().plusSeconds(1)).equals(rt.getExpiresAt())) && clientID.equals(rt.getAccessToken().getClientApp().getId()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh Token with expiresAt=" + expiration + ", accessTokenID=" + accessTokenID + ", clientID=" + clientID + " does not exists (while VerifyingDataFromClient)"));

            // sprawdzam czy refreshToken nie jest przeterminowany
            if (!findRefreshToken.getExpiresAt().after(Timestamp.valueOf(LocalDateTime.now()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expiration time passed");
            }

            // sprawdzam czy refreshToken nie jest revoked
            if (findRefreshToken.isRevoked()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh Token is revoked");
            }

            // usuwam były refreshtoken
            refreshTokensAccessService.remove(findRefreshToken);

            context.changeState(new RefreshingAccessToken());

            // jeśli żadne parametry w params nie pasuje wtedy -> Failure
        } else {
            context.changeState(new Failure());
        }

        // wywołuję CreatingAuthorizationCode / ExchangingAuthorizationCodeForAccessToken / RefreshingAccessToken w przypadku gdy powodzenia / Failure w przeciwym przypadku
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "VerifyingDataFromClient";
    }
}
