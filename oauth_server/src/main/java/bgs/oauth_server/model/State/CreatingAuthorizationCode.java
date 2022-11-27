package bgs.oauth_server.model.State;

import bgs.oauth_server.access_services.*;

import bgs.oauth_server.domain.*;

import bgs.oauth_server.domain.Permission;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.security.*;
import java.sql.Timestamp;
import java.sql.*;
import java.time.*;
import java.util.*;

@Service("CreatingAuthorizationCode")
public class CreatingAuthorizationCode implements State {

    @Autowired
    private UsersAccessService usersAccessService;
    @Autowired
    private AppsAccessService appsAccessService;
    @Autowired
    private AuthCodesAccessService authCodesAccessService;
    @Autowired
    private ScopesAccessService scopesAccessService;
    @Autowired
    private PermissionsAccessService permissionsAccessService;
    @Autowired
    private RedirectingToAppRedirectURL redirectingToAppRedirectURL;


    @Override
    public Response handle(Map<String, String> params) throws SQLException {

        System.out.println("CreatingAuthorizationCode");

        Integer clientID = Integer.parseInt(params.get("clientID"));

        // pobieram z bazy obiekt User dla danego 'username'
        var user = usersAccessService.readById(Integer.parseInt(params.get("userID")));
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        var username = user.getUsername();
        List<User> users = usersAccessService.readAll();
        User user1 = users.stream()
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
        authCode.setClientApp(appsAccessService.readById(clientID));
        authCode.setUser(user1);

        // zapisuję stworzony obiekt authCode do bazy danych
        authCodesAccessService.create(authCode);

        //zmieniam stan na RedirectingToAppRedirectURL (tam wyślę code do klienta)
//        context.changeState(new RedirectingToAppRedirectURL());

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
        List<Scope> scopesFromDataBase = scopesAccessService.readAll();

        // biorę wszystkie permissions z database
        List<Permission> permissionsFromDataBase = permissionsAccessService.readAll();

        // sprawdzam czy istnieją takie scope co są w scopesSeparated
        for (String scope : scopesSeparated) {
            Scope scope1 = scopesFromDataBase.stream()
                    .filter(s -> scope.equals(s.getName()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scope " + scope + " does not exists (thrown in CreatingAuthorizationCode)"));

            // szukam czy permission z danymi parametrami już istnieje
            Permission permissionFound = permissionsFromDataBase.stream()
                    .filter(p -> scope1.getScopeId().equals(p.getScope().getScopeId()) && clientID.equals(p.getClientApp().getClientAppId()) && user1.getUserId().equals(p.getUser().getUserId()))
                    .findFirst()
                    .orElse(null);

            // jeśli dany permission z danymi parametrami nie istnieje to go tworzę
            if (permissionFound == null) {

                // tworzę obiekt Permission
                Permission permission = new Permission();
                permission.setClientApp(appsAccessService.readById(clientID));
                permission.setScope(scope1);
                permission.setUser(user1);

                // zapisuję stworzony obiekt permission do bazy danych
                permissionsAccessService.create(permission);
            }
        }

        // dopisuję 'code' (content) do params
        params.put("code", authCode.getContent());

        return redirectingToAppRedirectURL.handle(params);
    }

    @Override
    public String toString() {
        return "CreatingAuthorizationCode";
    }
}
