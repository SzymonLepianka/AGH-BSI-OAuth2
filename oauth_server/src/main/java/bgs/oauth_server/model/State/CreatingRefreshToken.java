package bgs.oauth_server.model.State;


import bgs.oauth_server.access_services.*;
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
    @Autowired
    private RedirectingToAppRedirectURL redirectingToAppRedirectURL;

    @Override
    public Response handle(Map<String, String> params) {

        System.out.println("CreatingRefreshToken");

        // odczytuję expiresAt z params
        // pozbywam się z nanosekund
        Timestamp expiresAt = Timestamp.valueOf(params.get("expiresAt"));
        expiresAt.setNanos(0);

        // odczytuję potrzebne parametry z 'params'
        Timestamp createdAt = Timestamp.valueOf(params.get("createdAt"));
        String scopes = params.get("scopes");
        Integer clientID = Integer.parseInt(params.get("clientID"));

        // odczytuję stworzony w CreatingAccessToken accessToken
        List<AccessToken> accessTokenList = accessTokensAccessService.readAll();
        AccessToken accessToken = accessTokenList.stream().filter(at -> expiresAt.equals(at.getExpiresAt()) && clientID.equals(at.getClientApp().getClientAppId()) && scopes.equals(at.getScopes())).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access Token with expiresAt=" + expiresAt + " does not exists (while CreatingRefreshToken)"));

        // tworzę obiekt refreshToken - zapisuję do niego parametry i zapisuję do bazy danych
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccessToken(accessToken);
        Timestamp refreshTokenExpiresAt = Timestamp.valueOf(createdAt.toLocalDateTime().plusDays(1));
        refreshTokenExpiresAt.setNanos(0);
        refreshToken.setExpiresAt(refreshTokenExpiresAt);
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
//        context.changeState(new RedirectingToAppRedirectURL());
        return redirectingToAppRedirectURL.handle(params);
    }

    @Override
    public String toString() {
        return "CreatingRefreshToken";
    }
}
