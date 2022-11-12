package Group28.OAuth.Model.State;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AccessToken;
import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Domain.Permission;
import Group28.OAuth.token.AccessTokenBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CreatingAccessToken extends State {

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("CreatingAccessToken");

        IDatabaseEditor db = DatabaseEditor.getInstance();

        Long clientID, userID;
        // przypadek CreatingAuthorizationCode
        if (params.containsKey("code")) {
            //pobieram z params 'code' i 'clientID'
            String code = params.get("code");
            clientID = Long.parseLong(params.get("clientID"));

            // pobieram z bazy danych AuthCodes i szukam przekazanego w params 'code'
            List<AuthCode> codesFromDataBase = db.getAuthCodesAccessObject().readAll();
            AuthCode authCode = codesFromDataBase.stream()
                    .filter(c -> code.equals(c.getContent()) && clientID.equals(c.getClientApp().getId()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code " + code + " does not exists (while CreatingAccessToken)"));

            //ID użytkownika przypisanego do znalezionego AuthCode
            userID = authCode.getUser().getId();
        }

        // przypadek RefreshingAccessToken
        else {
            clientID = Long.parseLong(params.get("clientID"));
            userID = Long.parseLong(params.get("userID"));
        }

        // ustalam createdAt oraz expiresAt (parametry tokenu)
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        Timestamp expiresAt = Timestamp.valueOf(LocalDateTime.now().plusMinutes(15));

        // tworzę String scopes (parametr tokenu)
        // - pobieram wszsytkie permissions
        // - jeśli w danym permission zgadza się clientID i userID dodaję do 'scopes'
        List<Permission> permissionsFromDataBase = db.getPermissionsAccessObject().readAll();
        StringBuilder scopes = new StringBuilder();
        for (Permission permission : permissionsFromDataBase) {
            if (permission.getClientApp().getId().equals(clientID) && permission.getUser().getId().equals(userID)) {
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
        accessToken.setClientApp(db.getAppsAccessObject().readById(clientID));
        accessToken.setCreatedAt(createdAt);
        accessToken.setExpiresAt(expiresAt);
        accessToken.setRevoked(false);
        accessToken.setScopes(scopes.toString());
        accessToken.setUser(db.getUsersAccessObject().readById(userID));
        accessToken.setUpdatedAt(createdAt);
        db.getAccessTokensAccessObject().create(accessToken);

        // czytam z danych appSecret clienta z danym clientID
        Long appSecret = db.getAppsAccessObject().readById(clientID).getAppSecret();

        // czytam z danych username z danym userID
        String username = db.getUsersAccessObject().readById(userID).getUsername();

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
        context.changeState(new CreatingRefreshToken());
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "CreatingAccessToken";
    }
}
