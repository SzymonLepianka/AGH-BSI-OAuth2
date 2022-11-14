package bgs.oauth_server.model.State;


import bgs.oauth_server.dao.*;
import bgs.oauth_server.domain.*;
import bgs.oauth_server.token.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.sql.*;
import java.util.*;

@Service("CreatingRefreshToken")
public class CreatingRefreshToken implements State {

    @Autowired
    private AccessTokensAccessService accessTokensAccessService;
    @Autowired
    private RefreshTokensAccessService refreshTokensAccessService;
    @Autowired
    private AppsAccessService appsAccessService;

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("CreatingRefreshToken");

        // odczytuję expiresAt z params
        // pozbywam się z nanosekund, trzeba było zaokrąglić
        Timestamp expiresAtTemp = Timestamp.valueOf(params.get("expiresAt"));
        if (expiresAtTemp.getNanos() >= 500000000) {
            expiresAtTemp = Timestamp.valueOf(expiresAtTemp.toLocalDateTime().plusSeconds(1));
        }
        expiresAtTemp.setNanos(0);
        Timestamp expiresAt = expiresAtTemp;

        // odczytuję potrzebne parametry z 'params'
        Timestamp createdAt = Timestamp.valueOf(params.get("createdAt"));
        String scopes = params.get("scopes");
        Integer clientID = Integer.parseInt(params.get("clientID"));

        // odczytuję stworzony w CreatingAccessToken accessToken
        List<AccessToken> accessTokenList = accessTokensAccessService.readAll();
        AccessToken accessToken = accessTokenList.stream()
                .filter(at -> expiresAt.equals(at.getExpiresAt()) && clientID.equals(at.getClientApp().getClientAppId()) && scopes.equals(at.getScopes()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access Token with expiresAt=" + expiresAt + " does not exists (while CreatingRefreshToken)"));

        // tworzę obiekt refreshToken - zapisuję do niego parametry i zapisuję do bazy danych
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccessToken(accessToken);
        refreshToken.setExpiresAt(Timestamp.valueOf(createdAt.toLocalDateTime().plusDays(1)));
        refreshToken.setRevoked(false);
        refreshTokensAccessService.create(refreshToken);

        // czytam z danych danych appSecret clienta z danym clientID
        Integer appSecret = appsAccessService.readById(clientID).getAppSecret();

        // buduję refreshToken
        RefreshTokenBuilder refreshTokenBuilder = new RefreshTokenBuilder(refreshToken.getExpiresAt(), refreshToken.getAccessToken().getAccessTokenId(), appSecret);
        String createdRefreshToken = refreshTokenBuilder.generateToken();
        System.out.println("Created Refresh Token: " + createdRefreshToken);

        // dopisuję do 'params' stworzony refreshToken
        params.put("createdRefreshToken", createdRefreshToken);

        // zmieniam stan na RedirectingToAppRedirectURL
        context.changeState(new RedirectingToAppRedirectURL());
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "CreatingRefreshToken";
    }
}
