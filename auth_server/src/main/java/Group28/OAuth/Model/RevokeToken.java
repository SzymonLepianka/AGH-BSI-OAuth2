package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AccessToken;
import Group28.OAuth.Domain.RefreshToken;
import Group28.OAuth.token.TokenDecoder;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class RevokeToken {
    public static boolean revokeToken(Long clientID, String accessToken) throws SQLException {

        // czytam z bazy danych appSecret clienta z danym clientID
        IDatabaseEditor db = DatabaseEditor.getInstance();
        Long appSecret = db.getAppsAccessObject().readById(clientID).getAppSecret();

        //dekoduję z otrzymanego tokenu issuedAt, expiration, scopes i subject (czyli userID)
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expiration time passed");
        }

        // pobieram z bazy danych accessTokens i szukam przekazanego w params 'accessToken'
        List<AccessToken> accessTokens = db.getAccessTokensAccessObject().readAll();
        AccessToken accessTokenFound = accessTokens.stream()
                .filter(at -> (userID.equals(at.getUser().getId()) &&
                        (issuedAt.equals(at.getCreatedAt()) ||
                                Timestamp.valueOf(issuedAt.toLocalDateTime().plusSeconds(1)).equals(at.getCreatedAt())) &&
                        (expiration.equals(at.getExpiresAt()) ||
                                Timestamp.valueOf(expiration.toLocalDateTime().plusSeconds(1)).equals(at.getExpiresAt())) &&
                        clientID.equals(at.getClientApp().getId()) && scopes.equals(at.getScopes())))
                .findFirst()
                .orElse(null);

        if (accessTokenFound == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access Token does not exist in data base");
        }

        // robię update Access Tokenu  w bazie danych
        accessTokenFound.setRevoked(true);
        db.getAccessTokensAccessObject().update(accessTokenFound);

        // pobieram z bazy danych refreshTokens i szukam posiadającego accessTokenID przekazanego w params 'accessToken'
        List<RefreshToken> refreshTokens = db.getRefreshTokensAccessObject().readAll();
        RefreshToken refreshTokenFound = refreshTokens.stream()
                .filter(rt -> (accessTokenFound.getId().equals(rt.getAccessToken().getId())))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Refresh Token with " + accessTokenFound.getId() + " does not exist (while RevokeToken)"));

        if (refreshTokenFound == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh Token associated with the provided token does not exist in data base");
        }

        // robię update Refresh Tokenu  w bazie danych
        refreshTokenFound.setRevoked(true);
        db.getRefreshTokensAccessObject().update(refreshTokenFound);

        return true;
    }
}
