package bgs.oauth_server.model;

import bgs.oauth_server.domain.*;
import bgs.oauth_server.access_services.*;
import bgs.oauth_server.token.*;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.sql.*;
import java.time.*;
import java.util.*;

@Service("RevokeToken")
public class RevokeToken {

    @Autowired
    private AppsAccessService appsAccessService;
    @Autowired
    private AccessTokensAccessService accessTokensAccessService;
    @Autowired
    private RefreshTokensAccessService refreshTokensAccessService;

    public boolean revokeToken(Integer clientID, String accessToken) throws SQLException {

        // czytam z bazy danych appSecret clienta z danym clientID
        Integer appSecret = appsAccessService.readById(clientID).getAppSecret();

        //dekoduję z otrzymanego tokenu issuedAt, expiration, scopes i subject (czyli userID)
        TokenDecoder tokenDecoder = new TokenDecoder();
        Claims claims = tokenDecoder.decodeToken(accessToken, appSecret.toString());
        String scopes = (String) claims.get("scopes");
        Integer userID = Integer.parseInt(claims.getSubject());
        // ustawiam format Timestamp issuedAt i expiration
        String date = String.valueOf(claims.getIssuedAt().toInstant()).substring(0, 10);
        String time = String.valueOf(claims.getIssuedAt().toInstant()).substring(11, 19);
        Timestamp issuedAt = Timestamp.valueOf(Timestamp.valueOf(date + " " + time).toLocalDateTime().plusHours(1));
        date = String.valueOf(claims.getExpiration().toInstant()).substring(0, 10);
        time = String.valueOf(claims.getExpiration().toInstant()).substring(11, 19);
        Timestamp expiration = Timestamp.valueOf(Timestamp.valueOf(date + " " + time).toLocalDateTime().plusHours(1));

        // sprawdzam czy expiration nie minął
        if (!expiration.after(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expiration time passed");
        }

        // pobieram z bazy danych accessTokens i szukam przekazanego w params 'accessToken'
        List<AccessToken> accessTokens = accessTokensAccessService.readAll();
        AccessToken accessTokenFound = accessTokens.stream()
                .filter(at -> (userID.equals(at.getUser().getUserId()) &&
                        (issuedAt.equals(at.getCreatedAt()) ||
                                Timestamp.valueOf(issuedAt.toLocalDateTime().plusSeconds(1)).equals(at.getCreatedAt())) &&
                        (expiration.equals(at.getExpiresAt()) ||
                                Timestamp.valueOf(expiration.toLocalDateTime().plusSeconds(1)).equals(at.getExpiresAt())) &&
                        clientID.equals(at.getClientApp().getClientAppId()) && scopes.equals(at.getScopes())))
                .findFirst()
                .orElse(null);

        if (accessTokenFound == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access Token does not exist in data base");
        }

        // robię update Access Tokenu  w bazie danych
        accessTokenFound.setRevoked(true);
        accessTokensAccessService.update(accessTokenFound);

        // pobieram z bazy danych refreshTokens i szukam posiadającego accessTokenID przekazanego w params 'accessToken'
        List<RefreshToken> refreshTokens = refreshTokensAccessService.readAll();
        RefreshToken refreshTokenFound = refreshTokens.stream()
                .filter(rt -> (accessTokenFound.getAccessTokenId().equals(rt.getAccessToken().getAccessTokenId())))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Refresh Token with " + accessTokenFound.getAccessTokenId() + " does not exist (while RevokeToken)"));

        if (refreshTokenFound == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh Token associated with the provided token does not exist in data base");
        }

        // robię update Refresh Tokenu  w bazie danych
        refreshTokenFound.setRevoked(true);
        refreshTokensAccessService.update(refreshTokenFound);

        return true;
    }
}
