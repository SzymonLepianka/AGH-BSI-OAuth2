package bgs.oauth_server.model;

import bgs.oauth_server.dao.*;
import bgs.oauth_server.domain.*;
import bgs.oauth_server.token.*;

import io.jsonwebtoken.*;
import org.json.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.sql.*;

@Service("GetUserData")
public class GetUserData {

    @Autowired
    private ValidateToken validateToken;
    @Autowired
    private AppsAccessService appsAccessService;
    @Autowired
    private UsersAccessService usersAccessService;

    public JSONObject getUserData(Integer clientID, String accessToken) throws SQLException {

        // tworzę JSONObject (zostanie zwrócony)
        JSONObject userData = new JSONObject();

        // waliduję token
        if (!validateToken.validateToken(accessToken)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Access Token");
        }

        // czytam z bazy danych appSecret clienta z danym clientID
        Integer appSecret = appsAccessService.readById(clientID).getAppSecret();

        // dekoduję otrzymany token (scopes i userID)
        TokenDecoder tokenDecoder = new TokenDecoder();
        Claims claims = tokenDecoder.decodeToken(accessToken, appSecret.toString());
        String scopes = (String) claims.get("scopes");
        Integer userID = Integer.parseInt(claims.getSubject());

        // rozdzielam scopes do tablicy
        String[] scopesSeparated = scopes.split(",");

        // identyfikuję dany scope i zapisuję daną informację do JSONObject
        for (String scope : scopesSeparated) {
            switch (scope) {
                case "user_birthdate" ->
                        userData.put("user_birthdate", usersAccessService.readById(userID).getBirthDate());
                case "user_email" -> userData.put("user_email", usersAccessService.readById(userID).getEmail());
                case "user_firstname" ->
                        userData.put("user_firstname", usersAccessService.readById(userID).getFirstName());
                case "user_phonenumber" ->
                        userData.put("user_phonenumber", usersAccessService.readById(userID).getPhoneNumber());
                case "user_surname" -> userData.put("user_surname", usersAccessService.readById(userID).getSurname());
                case "user_username" ->
                        userData.put("user_username", usersAccessService.readById(userID).getUsername());
                default -> throw new IllegalStateException("Invalid scope: " + scope + " (while GetUserData)");
            }
        }
        // Zwraca JSONObject
        return userData;

    }

}
