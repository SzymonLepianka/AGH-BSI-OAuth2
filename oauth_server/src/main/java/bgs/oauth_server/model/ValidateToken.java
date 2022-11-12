package bgs.oauth_server.model;

import bgs.oauth_server.dao.*;
import bgs.oauth_server.domain.*;
import bgs.oauth_server.token.*;

import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.server.*;

import java.sql.*;
import java.time.*;
import java.util.*;

public class ValidateToken {

    public static boolean validateToken(String accessToken) throws SQLException {

        Long clientID = getClientID(accessToken);

        // czytam z danych danych appSecret clienta z danym clientID
        IDatabaseEditor db = DatabaseEditor.getInstance();
        Long appSecret = db.getAppsAccessObject().readById(clientID).getAppSecret();

        //dekoduję z otrzymanego tokenu issuedAt, expiration, scopes i subject(userID)
        TokenDecoder tokenDecoder = new TokenDecoder();
        Claims claims = tokenDecoder.decodeToken(accessToken, appSecret.toString());
        String scopes = (String) claims.get("scopes");
        Long userID = Long.parseLong(claims.getSubject());
        // ustawiam format Timestamp issuedAt i expiration
        String date = String.valueOf(claims.getIssuedAt().toInstant()).substring(0, 10);
        String time = String.valueOf(claims.getIssuedAt().toInstant()).substring(11, 19);
        Timestamp issuedAt = Timestamp.valueOf(Timestamp.valueOf(date + " " + time).toLocalDateTime().plusHours(1));
        date = String.valueOf(claims.getExpiration().toInstant()).substring(0, 10);
        time = String.valueOf(claims.getExpiration().toInstant()).substring(11, 19);
        Timestamp expiration = Timestamp.valueOf(Timestamp.valueOf(date + " " + time).toLocalDateTime().plusHours(1));

        // sprawdzam czy expiration nie minął
        if (!expiration.after(Timestamp.valueOf(LocalDateTime.now()))) {
            return false;
        }
        // pobieram z bazy danych accessTokens i szukam przekazanego w params 'accessToken'
        List<AccessToken> accessTokens = db.getAccessTokensAccessObject().readAll();
        AccessToken accessTokenFound = accessTokens.stream()
                .filter(at -> (userID.equals(at.getUser().getId()) && issuedAt.equals(at.getCreatedAt()) || Timestamp.valueOf(issuedAt.toLocalDateTime().plusSeconds(1)).equals(at.getCreatedAt())) && (expiration.equals(at.getExpiresAt()) || Timestamp.valueOf(expiration.toLocalDateTime().plusSeconds(1)).equals(at.getExpiresAt())) && clientID.equals(at.getClientApp().getId()) && scopes.equals(at.getScopes()))
                .findFirst()
                .orElse(null);

        // sprawdzam czy token nie jest revoked
        if (accessTokenFound != null){
            {
                return !accessTokenFound.isRevoked();
            }
        }
        else {
            return false;
        }
    }

    private static Long getClientID(String accessToken) {

        String[] split_string = accessToken.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));

        String[] split = body.split(",");

        if (!split[0].startsWith("clientID", 2)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access Token should have 'clientID' instead of " + split[0].substring(2,10));
        }

// KODZIK dający expiresAt
//        if (split[2].startsWith("exp", 1)){
//            long expiresAt = Long.parseLong(split[2].substring(6));
//            System.out.println(expiresAt);
//            // System.currentTimeMillis() / 1000L - obecny czas (ilość sekund od 1970 roku)
//        }
//        else{
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access Token should have 'exp' instead of " + split[2].substring(1,4));
//        }

        return Long.parseLong(split[0].substring(12));
    }
}
