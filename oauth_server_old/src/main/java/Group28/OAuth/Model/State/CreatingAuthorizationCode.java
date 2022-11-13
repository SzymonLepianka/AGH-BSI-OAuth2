package Group28.OAuth.Model.State;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Domain.Permission;
import Group28.OAuth.Domain.Scope;
import Group28.OAuth.Domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CreatingAuthorizationCode extends State {

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("CreatingAuthorizationCode");

        Long clientID = Long.parseLong(params.get("clientID"));

        // pobieram z bazy obiekt User dla danego 'username'
        IDatabaseEditor db = DatabaseEditor.getInstance();
        var user = db.getUsersAccessObject().readById(Long.parseLong(params.get("userID")));
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        var username = user.getUsername();
        List<User> users = db.getUsersAccessObject().readAll();
        Group28.OAuth.Domain.User user1 = users.stream()
                .filter(x -> username.equals(x.getUsername()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student " + username + " does not exists (while creating auth code)"));

        // tworzę treść AuthCode - ciąg losowych znaków o zadanej długości codeLength
        int codeLength = 10;
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        String generatedString = sb.toString();

        // tworzę obiekt AuthCode
        AuthCode authCode = new AuthCode();
        authCode.setContent(generatedString);
        authCode.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));
        authCode.setRevoked(false);
        authCode.setClientApp(db.getAppsAccessObject().readById(clientID));
        authCode.setUser(user1);

        // zapisuję stworzony obiekt authCode do bazy danych
        db.getAuthCodesAccessObject().create(authCode);

        //zmieniam stan na RedirectingToAppRedirectURL (tam wyślę code do klienta)
        context.changeState(new RedirectingToAppRedirectURL());

        ///////////////////////
        //PERMISSIONS i SCOPE//
        ///////////////////////

        // biorę 'scopes' (jest jednym polem w 'params')
        String scopesRaw = params.get("scopes");

        // sprawdzam czy podano jakiekolwiek scope
        if (scopesRaw.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An empty scopes field was specified in the request");
        }

        // robię listę ze scopes
        String[] scopesSeparated;
        if (scopesRaw.contains(",")) {
            scopesSeparated = scopesRaw.split(",");
        } else {
            scopesSeparated = new String[]{scopesRaw};
        }

        // biorę wszystkie scopes z database
        List<Scope> scopesFromDataBase = db.getScopesAccessObject().readAll();

        // biorę wszystkie permissions z database
        List<Permission> permissionsFromDataBase = db.getPermissionsAccessObject().readAll();

        // sprawdzam czy istnieją takie scope co są w scopesSeparated
        for (String scope : scopesSeparated) {
            Scope scope1 = scopesFromDataBase.stream()
                    .filter(s -> scope.equals(s.getName()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scope " + scope + " does not exists (thrown in CreatingAuthorizationCode)"));

            // szukam czy permission z danymi parametrami już istnieje
            Permission permissionFound = permissionsFromDataBase.stream()
                    .filter(p -> scope1.getId().equals(p.getScope().getId()) && clientID.equals(p.getClientApp().getId()) && user1.getId().equals(p.getUser().getId()))
                    .findFirst()
                    .orElse(null);

            // jeśli dany permission z danymi parametrami nie istnieje to go tworzę
            if (permissionFound == null) {

                // tworzę obiekt Permission
                Permission permission = new Permission();
                permission.setClientApp(db.getAppsAccessObject().readById(clientID));
                permission.setScope(scope1);
                permission.setUser(user1);

                // zapisuję stworzony obiekt permission do bazy danych
                db.getPermissionsAccessObject().create(permission);
            }
        }

        // dopisuję 'code' (content) do params
        params.put("code", authCode.getContent());

        return context.handle(params);
    }

    @Override
    public String toString() {
        return "CreatingAuthorizationCode";
    }
}
