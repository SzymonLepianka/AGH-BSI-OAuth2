package Server.Model;

import Server.Domain.User;
import Server.Domain.UsersRepository;
import org.json.JSONObject;
import org.riversun.okhttp3.OkHttp3CookieHelper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GetUserData {
    public static User getUserData(String username, String accessToken, UsersRepository usersRepository, HttpServletResponse httpServletResponse) throws ResponseStatusException, IOException, InterruptedException, ParseException {

        List<User> allUsersFromDataBase = (List<User>) usersRepository.findAll();
        User userFound = allUsersFromDataBase.stream()
                .filter(x -> username.equals(x.getUsername()))
                .findFirst()
                .orElse(null);

        List<String> scopesFromAccessToken = GetScopes.getScopes(accessToken);
        User user;
        if (userFound != null) {
            var dbResponse = usersRepository.findById(userFound.getId());
            if (dbResponse.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found in data base");
            }
            System.out.println("User exists in the database (no request to aouth service)");
            user = dbResponse.get();
        } else {
            System.out.println("User does not exist in the database, request to oauth service is executed");
            String url = "http://localhost:8081/api/getUserData?clientID=2&accessToken=" + accessToken;
            OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
            cookieHelper.setCookie(url, "AccessToken2", accessToken);
            HttpClient client = HttpClient.newBuilder().cookieHandler(new CookieManager()).build();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            CookieStore cookieStore = ((CookieManager) (client.cookieHandler().get())).getCookieStore();
            HttpCookie accessToken2Cookie = new HttpCookie("AccessToken2", accessToken);
            accessToken2Cookie.setPath("/");
            cookieStore.add(URI.create(url), accessToken2Cookie);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
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
                user_birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject1.get("user_birthdate").toString());
            }
            user = new User();
            user.setEmail(user_email);
            user.setFirstName(user_firstname);
            user.setUsername(user_username);
            user.setSurname(user_surname);
            user.setBirthDate(user_birthdate);
            usersRepository.save(user);
        }
        return user;
    }
}
