package bgs.oauth_server.controllers;

import bgs.oauth_server.model.*;
import bgs.oauth_server.model.State.*;
import bgs.oauth_server.view.*;
import org.json.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.server.*;

import javax.servlet.http.*;
import java.util.*;


@Controller
@RequestMapping("/api")
public class APIController {

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
    @Autowired
    private LogInUser logInUser;
    @Autowired
    private AuthenticatingClient authenticatingClient;


    public APIController() {
        this.view = new APIView();
    }

    @GetMapping("/validateToken")
    public @ResponseBody ResponseEntity<String> validateToken(@RequestParam String accessToken) {
        try {
            boolean response = validateToken.validateToken(accessToken);
            /* funkcja zwraca false gdy:
                - minął expiration time
                - nie ma tokenu o takich parametrach
             */
            return new ResponseEntity<>(view.validToken(response), HttpStatus.OK);
        } catch (ResponseStatusException rse) {
            rse.printStackTrace();
            return new ResponseEntity<>(rse.getReason(), rse.getStatus());
        }
    }

    @GetMapping(value = "/createToken", params = "authCode")
    public @ResponseBody ResponseEntity<String> createToken(@RequestParam String clientID, @RequestParam String authCode, HttpServletResponse httpServletResponse) {

        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientID);
        params.put("code", authCode);

        try {
            Response response = authenticatingClient.handle(params);
            view.createToken(response, httpServletResponse, clientID);
            return new ResponseEntity<>("Token was created successfully", HttpStatus.OK);
        } catch (ResponseStatusException rse) {
            rse.printStackTrace();
            return new ResponseEntity<>(rse.getReason(), rse.getStatus());
        }
    }

    @GetMapping("/createToken")
    public @ResponseBody ResponseEntity<String> createTokenFromCookie(@RequestParam String clientID, HttpServletResponse httpServletResponse) {
        String authCode = "";
        try {
            authCode = checkAuthCodeCookie.check();
        } catch (ResponseStatusException rse) {
            System.out.println("AuthCode cookie is not set (createTokenFromCookie)");
            rse.printStackTrace();
            return new ResponseEntity<>(rse.getReason(), rse.getStatus());
        }
        return createToken(clientID, authCode, httpServletResponse);
    }

    @GetMapping("/refreshToken")
    public @ResponseBody ResponseEntity<String> refreshToken(@RequestParam String clientID, @RequestParam String refreshToken, HttpServletResponse httpServletResponse) {

        // autoryzacja
        if (!authorization.authorizeOnClientID(clientID)) {
            return new ResponseEntity<>("Authorization failed", HttpStatus.UNAUTHORIZED);
        }

        // setting params
        Map<String, String> params = new HashMap<>();
        params.put("clientID", clientID);
        params.put("refreshToken", refreshToken);

        // refreshing token
        try {
            Response response = authenticatingClient.handle(params);
            view.refreshToken(response, httpServletResponse, clientID);
            return new ResponseEntity<>("Token was refreshed successfully", HttpStatus.OK);
        } catch (ResponseStatusException rse) {
            rse.printStackTrace();
            return new ResponseEntity<>(rse.getReason(), rse.getStatus());
        }
    }

    @GetMapping("/revokeToken")
    public @ResponseBody ResponseEntity<String> revokeToken(@RequestParam String clientID, @RequestParam String accessToken) {

        // autoryzacja
        if (!authorization.authorizeOnClientID(clientID)) {
            return new ResponseEntity<>("Authorization failed - cannot revoke token", HttpStatus.UNAUTHORIZED);
        }

        // revoking token
        try {
            boolean response = revokeToken.revokeToken(Integer.parseInt(clientID), accessToken);
            System.out.println(response);

            /* funkcja zwraca true gdy udało się zrobić revoke
               w przeciwnym przypadku wyrzuca Bad Request / IllegalStateException
             */
            return new ResponseEntity<>(view.revokeToken(response), HttpStatus.OK);
        } catch (ResponseStatusException rse) {
            rse.printStackTrace();
            return new ResponseEntity<>(rse.getReason(), rse.getStatus());
        }
    }

    @GetMapping("/revokeAllTokens")
    public @ResponseBody ResponseEntity<String> revokeAllTokens(@RequestParam String clientID) {

        // autoryzacja
        if (!authorization.authorizeOnClientID(clientID)) {
            return new ResponseEntity<>("Authorization failed - cannot revoke all tokens", HttpStatus.UNAUTHORIZED);
        }

        // revoking all tokens
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().startsWith("AccessToken")) {
                    var accessToken = cookie.getValue();
                    var clientIDForAccessToken = logInUser.getClientID(accessToken);

                    // revoking token
                    try {
                        revokeToken.revokeToken(Integer.parseInt(clientIDForAccessToken), accessToken);
                    } catch (ResponseStatusException rse) {
                        rse.printStackTrace();
                        return new ResponseEntity<>(rse.getReason(), rse.getStatus());
                    }
                }
            }
        }
        return new ResponseEntity<>(view.revokeAllTokens(true), HttpStatus.OK);
    }

    @GetMapping("/revokeGrantType")
    public @ResponseBody ResponseEntity<String> revokeGrantType(@RequestParam String clientID, @RequestParam String authCode) {

        // autoryzacja
        if (!authorization.authorizeOnClientID(clientID)) {
            return new ResponseEntity<>("Authorization failed - cannot revoke grant type", HttpStatus.UNAUTHORIZED);
        }

        // revoking grant type
        try {
            /* funkcja zwraca true gdy udało się zrobić revoke
               w przeciwnym przypadku wyrzuca Bad Request
             */
            boolean response = revokeGrantType.revokeGrantType(Integer.parseInt(clientID), authCode);
            System.out.println("Revoke grant type response: " + response);
            return new ResponseEntity<>(view.revokeGrantType(response), HttpStatus.OK);
        } catch (ResponseStatusException rse) {
            rse.printStackTrace();
            return new ResponseEntity<>(rse.getReason(), rse.getStatus());
        }
    }

    @GetMapping(value = "/getUserData", params = "accessToken")
    public @ResponseBody ResponseEntity<String> getUserData(@RequestParam String clientID, @RequestParam String accessToken) {

        // autoryzacja
        if (!authorization.authorizeOnClientIDAndAccessToken(accessToken)) {
            return new ResponseEntity<>("User is unauthorized", HttpStatus.UNAUTHORIZED);
        }

        // getting user data
        try {
            JSONObject userData = getUserData.getUserData(Integer.parseInt(clientID), accessToken);
            System.out.println("User data (clientID=" + clientID + "): " + userData);
            /* funkcja zwraca JSONObject gdy accessToken jest valid
               przykład: {"user_email":"slepianka@wp.pl2","user_username":"slepianka2"}
               scopes muszą być zdefiniowane w bazie: user_birthdate, user_email, user_firstname, user_phonenumber, user_surname, user_username
               w przeciwnym przypadku wyrzuca Bad Request
             */
            return new ResponseEntity<>(userData.toString(), HttpStatus.OK);
        } catch (ResponseStatusException rse) {
            rse.printStackTrace();
            return new ResponseEntity<>(rse.getReason(), rse.getStatus());
        }
    }
}
