package bgs.oauth_server.controllers;

import bgs.oauth_server.model.*;
import bgs.oauth_server.model.State.*;
import bgs.oauth_server.view.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.server.*;
import org.springframework.web.util.*;

import javax.servlet.http.*;
import java.sql.*;
import java.util.*;


@Controller
@RequestMapping("/api")
public class APIController {

    private final Context context;
    private final APIView view;

    @Autowired
    private ValidateToken validateToken;
    @Autowired
    private RevokeGrantType revokeGrantType;
    @Autowired
    private CheckAuthCodeCookie checkAuthCodeCookie;
    @Autowired
    private Authorization authorization;
    @Autowired
    private RevokeToken revokeToken;
    @Autowired
    private GetUserData getUserData;


    public APIController() {
        this.context = new Context();
        this.view = new APIView();
    }

    private static String getClientID(String accessToken) {

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

    @GetMapping("/validateToken")
    public @ResponseBody
    String validateToken(@RequestParam String accessToken) throws SQLException {

        boolean response = validateToken.validateToken(accessToken);
        /* funkcja zwraca false gdy:
            - minął expiration time
            - nie ma tokenu o takich parametrach
         */
        return view.validToken(response);
    }

    @GetMapping(value = "/createToken", params = "authCode")
    public @ResponseBody
    String createToken(@RequestParam String clientID, @RequestParam String authCode, HttpServletResponse httpServletResponse) throws SQLException {
        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientID);
        params.put("code", authCode);
        context.changeState(new AuthenticatingClient());
        Response response = context.handle(params);
        // response.content to obiekt AuthCode
        view.createToken(response, httpServletResponse, clientID);
        return "Token was created successfully";
    }

    @GetMapping("/createToken")
    public @ResponseBody
    String createTokenFromCookie(@RequestParam String clientID, HttpServletResponse httpServletResponse) throws SQLException {
        String authCode = "";
        try {
            authCode = checkAuthCodeCookie.Check(httpServletResponse);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AuthCode cookie is not set");
            }
        }
        return createToken(clientID, authCode, httpServletResponse);
    }

    @GetMapping("/refreshToken")
    public @ResponseBody
    String refreshToken(@RequestParam String clientID, @RequestParam String refreshToken, HttpServletResponse httpServletResponse) throws SQLException {
        authorization.Authorize(httpServletResponse, clientID);
        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientID);
        params.put("refreshToken", refreshToken);
        context.changeState(new AuthenticatingClient());
        Response response = context.handle(params);
        view.refreshToken(response, httpServletResponse, clientID);
        return "Token was refreshed successfully";
    }

    @GetMapping("/revokeToken")
    public @ResponseBody
    String revokeToken(@RequestParam String clientID, @RequestParam String accessToken, HttpServletResponse httpServletResponse) throws SQLException {

        try {
            authorization.Authorize(httpServletResponse, clientID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot revoke token");
            }
        }
        boolean response = revokeToken.revokeToken(Integer.parseInt(clientID), accessToken);
        System.out.println(response);

        /* funkcja zwraca true gdy udało się zrobić revoke
           w przeciwnym przypadku wyrzuca Bad Request / IllegalStateException
         */

        return view.revokeToken(response);
    }

    @GetMapping("/revokeAllTokens")
    public @ResponseBody
    String revokeAllTokens(@RequestParam String clientID, HttpServletResponse httpServletResponse) throws SQLException {

        try {
            authorization.Authorize(httpServletResponse, clientID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot revoke all tokens");
            }
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().startsWith("AccessToken")) {
                    var accessToken = cookie.getValue();
                    var clientIDForAccessToken = getClientID(accessToken);
                    revokeToken.revokeToken(Integer.parseInt(clientIDForAccessToken), accessToken);
                }
            }
        }
        return view.revokeAllTokens(true);
    }

    @GetMapping("/revokeGrantType")
    public @ResponseBody
    String revokeGrantType(@RequestParam String clientID, @RequestParam String authCode, HttpServletResponse httpServletResponse) throws SQLException {

        try {
            authorization.Authorize(httpServletResponse, clientID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot revoke grant type");
            }
        }

        boolean response = revokeGrantType.revokeGrantType(Integer.parseInt(clientID), authCode);
        System.out.println(response);

        /* funkcja zwraca true gdy udało się zrobić revoke
           w przeciwnym przypadku wyrzuca Bad Request
         */

        return view.revokeGrantType(response);
    }

    @GetMapping("/getUserData")
    public @ResponseBody
    String getUserData(@RequestParam String clientID, HttpServletResponse httpServletResponse) throws SQLException {

        try {
            authorization.Authorize(httpServletResponse, clientID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot get user data");
            }
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var accessTokenCookie = WebUtils.getCookie(request, "AccessToken" + clientID);
        var accessToken = accessTokenCookie.getValue();
        JSONObject userData = getUserData.getUserData(Integer.parseInt(clientID), accessToken);
        System.out.println(userData);

        /* funkcja zwraca JSONObject gdy accessToken jest valid
           przykład: {"user_email":"slepianka@wp.pl2","user_username":"slepianka2"}
           scopes muszą być zdefinowane w bazie: user_birthdate, user_email, user_firstname, user_phonenumber, user_surname, user_username
           w przeciwnym przypadku wyrzuca Bad Request
         */

        return userData.toString();
    }

    @GetMapping(value = "/getUserData", params = "accessToken")
    public @ResponseBody
    String getUserData(@RequestParam String clientID, @RequestParam String accessToken, HttpServletResponse httpServletResponse) throws SQLException {

        try {
            authorization.Authorize(httpServletResponse, clientID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ResponseStatusException responseStatusException) {
            if (responseStatusException.getStatus() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot get user data");
            }
        }

        JSONObject userData = getUserData.getUserData(Integer.parseInt(clientID), accessToken);
        System.out.println(userData);

        /* funkcja zwraca JSONObject gdy accessToken jest valid
           przykład: {"user_email":"slepianka@wp.pl2","user_username":"slepianka2"}
           scopes muszą być zdefinowane w bazie: user_birthdate, user_email, user_firstname, user_phonenumber, user_surname, user_username
           w przeciwnym przypadku wyrzuca Bad Request
         */

        return userData.toString();
    }
}
