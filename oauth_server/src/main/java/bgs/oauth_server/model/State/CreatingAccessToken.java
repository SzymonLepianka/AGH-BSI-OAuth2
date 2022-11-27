package bgs.oauth_server.model.State;

import bgs.oauth_server.access_services.*;
import bgs.oauth_server.domain.*;
import bgs.oauth_server.token.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.sql.*;
import java.time.*;
import java.util.*;

@Service("CreatingAccessToken")
public class CreatingAccessToken implements State {

    @Autowired
    private AuthCodesAccessService authCodesAccessService;
    @Autowired
    private PermissionsAccessService permissionsAccessService;
    @Autowired
    private AppsAccessService appsAccessService;
    @Autowired
    private UsersAccessService usersAccessService;
    @Autowired
    private AccessTokensAccessService accessTokensAccessService;
    @Autowired
    private CreatingRefreshToken creatingRefreshToken;

    @Override
    public Response handle(Map<String, String> params) throws SQLException {

        System.out.println("CreatingAccessToken");

        Integer clientID, userID;
        // przypadek CreatingAuthorizationCode
        if (params.containsKey("code")) {
            //pobieram z params 'code' i 'clientID'
            String code = params.get("code");
            clientID = Integer.parseInt(params.get("clientID"));

            // pobieram z bazy danych AuthCodes i szukam przekazanego w params 'code'
            List<AuthCode> codesFromDataBase = authCodesAccessService.readAll();
            AuthCode authCode = codesFromDataBase.stream()
                    .filter(c -> code.equals(c.getContent()) && clientID.equals(c.getClientApp().getClientAppId()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code " + code + " does not exists (while CreatingAccessToken)"));

            //ID użytkownika przypisanego do znalezionego AuthCode
            userID = authCode.getUser().getUserId();
        }

        // przypadek RefreshingAccessToken
        else {
            clientID = Integer.parseInt(params.get("clientID"));
            userID = Integer.parseInt(params.get("userID"));
        }

        // ustalam createdAt oraz expiresAt (parametry tokenu)
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        Timestamp expiresAt = Timestamp.valueOf(LocalDateTime.now().plusMinutes(15));

        // tworzę String scopes (parametr tokenu)
        // - pobieram wszsytkie permissions
        // - jeśli w danym permission zgadza się clientID i userID dodaję do 'scopes'
        List<Permission> permissionsFromDataBase = permissionsAccessService.readAll();
        StringBuilder scopes = new StringBuilder();
        for (Permission permission : permissionsFromDataBase) {
            if (permission.getClientApp().getClientAppId().equals(clientID) && permission.getUser().getUserId().equals(userID)) {
                String name = permission.getScope().getName();
                // sprawdzam czy danego scope już nie przypisałem
                if (!scopes.toString().contains(name)) {
                    if (!scopes.toString().equals("")) {
                        scopes.append(",");
                    }
                    scopes.append(name);
                }
            }
        }

        // jeśli w params nie na scopes -> dodaj (przypadek RefreshingAccessToken)
        if (!params.containsKey("scopes")) {
            params.put("scopes", scopes.toString());
        }

//        //sprawdzam czy taki accesstoken już istnieje
//        List<AccessToken> accessTokensFromDataBase = db.getAccessTokensAccessObject().readAll();
//        AccessToken accessToken1 = accessTokensFromDataBase.stream()
//                .filter(at -> scopes.toString().equals(at.getScopes()) &&
//                        clientID.equals(at.getClientApp().getId()) &&
//                        userID.equals(at.getUser().getId()) && at.getExpiresAt().after(Timestamp.valueOf(LocalDateTime.now())))
//                .findFirst()
//                .orElse(null);
//        if (accessToken1 != null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access Token with scopes=" + scopes + ", clientID=" + clientID + ", userID=" + userID + " already exists");
//        }

        // tworzę obiekt accessToken - zapisuję do niego parametry i zapisuję do bazy danych
        AccessToken accessToken = new AccessToken();
        accessToken.setClientApp(appsAccessService.readById(clientID));
        accessToken.setCreatedAt(createdAt);
        accessToken.setExpiresAt(expiresAt);
        accessToken.setRevoked(false);
        accessToken.setScopes(scopes.toString());
        accessToken.setUser(usersAccessService.readById(userID));
        accessToken.setUpdatedAt(createdAt);
        accessTokensAccessService.create(accessToken);

        // czytam z danych appSecret clienta z danym clientID
        Integer appSecret = appsAccessService.readById(clientID).getAppSecret();

        // czytam z danych username z danym userID
        String username = usersAccessService.readById(userID).getUsername();

        // buduję accessToken
        AccessTokenBuilder accessTokenBuilder = new AccessTokenBuilder(createdAt, expiresAt, scopes.toString(), clientID, userID, username, appSecret);
        String createdAccessToken = accessTokenBuilder.generateToken();
        System.out.println("Created Access Token: " + createdAccessToken);

        // dopisuję do 'params' konieczne dane (w tym stworzony accessToken)
        params.put("createdAt", createdAt.toString());
        params.put("expiresAt", expiresAt.toString());
        params.put("scopes", scopes.toString());
        params.put("createdAccessToken", createdAccessToken);

        // zmieniam stan na CreatingRefreshToken
//        context.changeState(new CreatingRefreshToken());
        return creatingRefreshToken.handle(params);
    }

    @Override
    public String toString() {
        return "CreatingAccessToken";
    }
}
