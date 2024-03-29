package bgs.oauth_server.model;

import bgs.oauth_server.access_services.*;
import bgs.oauth_server.domain.*;
import bgs.oauth_server.model.State.*;
import bgs.oauth_server.token.*;
import io.jsonwebtoken.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.util.*;
import java.util.stream.*;

@Service("LogInUser")
public class LogInUser {

    @Autowired
    private UsersAccessService usersAccessService;
    @Autowired
    private ScopesAccessService scopesAccessService;
    @Autowired
    private AppsAccessService appsAccessService;
    @Autowired
    private PermissionsAccessService permissionsAccessService;
    @Autowired
    private AuthenticatingClient authenticatingClient;

    public String getClientID(String accessToken) {

        String[] split_string = accessToken.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64 base64Url = new Base64(true);
        String body = new String(base64Url.decode(base64EncodedBody));

        String[] split = body.split(",");

        if (!split[0].startsWith("clientID", 2)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access Token should have 'clientID' instead of " + split[0].substring(2, 10));
        }
        return split[0].substring(12);
    }

    private Integer getUserIDFromToken(String accessToken) {
        Integer clientIDFromToken = Integer.parseInt(getClientID(accessToken));

        // czytam z danych danych appSecret clienta z danym clientID
        Integer appSecret = appsAccessService.readById(clientIDFromToken).getAppSecret();

        //dekoduję z otrzymanego tokenu subject(userID)
        TokenDecoder tokenDecoder = new TokenDecoder();
        Claims claims = tokenDecoder.decodeToken(accessToken, appSecret.toString());
        Integer userID = Integer.parseInt(claims.getSubject());
        return userID;
    }


    public Response handleLoginWithClientID(String accessToken, String clientID) {
        Integer userID = getUserIDFromToken(accessToken);

        var users = usersAccessService.readAll();
        var user = users.stream().filter(x -> x.getUserId() == userID).findFirst();

        var params = new HashMap<String, String>();
        params.put("clientID", clientID);
        var allPermission = permissionsAccessService.readAll();
        List<Permission> userPermissionForApp = allPermission.stream().filter(x -> x.getUser().getUserId().equals(user.get().getUserId()) && String.valueOf(x.getClientApp().getClientAppId()).equals(clientID)).collect(Collectors.toList());

        // Add all permissions for user, if user doesn't have any
        if (userPermissionForApp.isEmpty()) {
            var scopes = scopesAccessService.readAll();
            var clientApp = appsAccessService.readById(Integer.parseInt(clientID));
            for (var scope : scopes) {
                var permission = new Permission();
                permission.setClientApp(clientApp);
                permission.setUser(user.get());
                permission.setScope(scope);
                permissionsAccessService.create(permission);
                userPermissionForApp.add(permission);
            }
        }
        var scopesBuilder = new StringBuilder();
        for (var permission : userPermissionForApp) {
            scopesBuilder.append(permission.getScope().getName()).append(",");
        }
        scopesBuilder.delete(scopesBuilder.length() - 1, scopesBuilder.length());
        params.put("scopes", scopesBuilder.toString());
        params.put("userID", String.valueOf(user.get().getUserId()));
        return authenticatingClient.handle(params);
    }

    public Response handleLoginForAuthCode(String username, String password, String clientID, PasswordEncoder passwordEncoder) {
        var users = usersAccessService.readAll();
        var user = users.stream().filter(x -> x.getUsername().equals(username)).findFirst();
        if (user.isEmpty()) {
            System.out.println("User " + username + " does not exist in the database (LogInUser)");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password for user: " + username);
        }
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            System.out.println("The password is incorrect (LogInUser)");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password for user: " + username);
        }
        var params = new HashMap<String, String>();
        params.put("clientID", clientID);
        var allPermission = permissionsAccessService.readAll();
        var userPermissionForApp = allPermission.stream().filter(x -> x.getUser().getUserId().equals(user.get().getUserId()) && String.valueOf(x.getClientApp().getClientAppId()).equals(clientID)).collect(Collectors.toList());

        // Add all permissions for user, if user doesn't have any
        if (userPermissionForApp.isEmpty()) {
            var scopes = scopesAccessService.readAll();
            var clientApp = appsAccessService.readById(Integer.parseInt(clientID));
            for (var scope : scopes) {
                var permission = new Permission();
                permission.setClientApp(clientApp);
                permission.setUser(user.get());
                permission.setScope(scope);
                permissionsAccessService.create(permission);
                userPermissionForApp.add(permission);
            }
        }
        var scopesBuilder = new StringBuilder();
        for (var permission : userPermissionForApp) {
            scopesBuilder.append(permission.getScope().getName()).append(",");
        }
        scopesBuilder.delete(scopesBuilder.length() - 1, scopesBuilder.length());
        params.put("scopes", scopesBuilder.toString());
        params.put("userID", String.valueOf(user.get().getUserId()));
        return authenticatingClient.handle(params);
    }

    public boolean handleLogin(String username, String password, String clientID, PasswordEncoder passwordEncoder) {
        var users = usersAccessService.readAll();
        var user = users.stream().filter(x -> x.getUsername().equals(username)).findFirst();

        if (user.isEmpty()) {
            System.out.println("User " + username + " does not exist in the database (LogInUser)");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password for user: " + username);
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            System.out.println("The password is incorrect (LogInUser)");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password for user: " + username);
        }

        // sprawdzam czy klient o danych clientId w params istnieje w bazie danych
        if (appsAccessService.readById((Integer.parseInt(clientID))) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client with clientID=" + clientID + " does not exist");
        }

        return true;
    }
}
