package bgs.oauthclientserver1.model;

import bgs.oauthclientserver1.access_services.*;
import bgs.oauthclientserver1.domain.*;
import org.json.*;
import org.riversun.okhttp3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.net.HttpCookie;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.*;
import java.text.*;
import java.util.*;

@Service("GetUserData")
public class GetUserData {

    @Value("${oauth_server.domain}")
    private String OAUTH_SERVER_DOMAIN;
    @Value("${client_id}")
    private String CLIENT_ID;

    @Autowired
    private UsersAccessService usersAccessService;
    @Autowired
    private GetScopes getScopes;

    public User getUserData(String accessToken) {
        List<User> allUsersFromDataBase = usersAccessService.readAll();
        List<String> scopesFromAccessToken = getScopes.getScopes(accessToken);
        String url = OAUTH_SERVER_DOMAIN + "/api/getUserData?clientID=" + CLIENT_ID + "&accessToken=" + accessToken;
        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
        cookieHelper.setCookie(url, "AccessToken" + CLIENT_ID, accessToken);
        HttpClient client = HttpClient.newBuilder().cookieHandler(new CookieManager()).build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        CookieStore cookieStore = ((CookieManager) (client.cookieHandler().get())).getCookieStore();
        HttpCookie accessToken2Cookie = new HttpCookie("AccessToken" + CLIENT_ID, accessToken);
        accessToken2Cookie.setPath("/");
        cookieStore.add(URI.create(url), accessToken2Cookie);
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        if (response.statusCode() != 200) {
            throw new ResponseStatusException(Objects.requireNonNull(HttpStatus.resolve(response.statusCode())), "Bad response status from oauth_server - " + response.body());
        }

        JSONObject jsonObject1 = new JSONObject(response.body());
        String user_email = null;
        String user_firstname = null;
        String user_username = null;
        String user_surname = null;
        Date user_birthdate = null;
        if (scopesFromAccessToken.contains("user_email")) {
            user_email = (String) jsonObject1.get("user_email");
        }
        if (scopesFromAccessToken.contains("user_firstname")) {
            user_firstname = (String) jsonObject1.get("user_firstname");
        }
        if (scopesFromAccessToken.contains("user_username")) {
            user_username = (String) jsonObject1.get("user_username");
        }
        if (scopesFromAccessToken.contains("user_surname")) {
            user_surname = (String) jsonObject1.get("user_surname");
        }
        if (scopesFromAccessToken.contains("user_birthdate")) {
            try {
                user_birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject1.get("user_birthdate").toString());
            } catch (ParseException pe) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, pe.getMessage());
            }
        }
        User user = new User();
        user.setEmail(user_email);
        user.setFirstName(user_firstname);
        user.setUsername(user_username);
        user.setSurname(user_surname);
        user.setBirthDate(user_birthdate);
        if (allUsersFromDataBase.stream().anyMatch(x -> x.getUsername().equals(user.getUsername()))) {
            usersAccessService.updateOnUsername(user);
        } else {
            usersAccessService.create(user);
        }
        return user;
    }
}
