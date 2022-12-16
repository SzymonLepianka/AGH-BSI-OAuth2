package bgs.oauthclientserver1.model;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.request.*;
import org.springframework.web.server.*;
import org.springframework.web.util.*;

import javax.servlet.http.*;
import javax.xml.bind.*;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.*;
import java.time.*;
import java.util.*;

@Service("Authorization")
public class Authorization {

    @Value("${app.secret}")
    private String APP_SECRET;
    @Value("${oauth_server.domain}")
    private String OAUTH_SERVER_DOMAIN;
    @Value("${client_id}")
    private String CLIENT_ID;


    public String authorizeOnUsername(String username) throws ResponseStatusException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var accessTokenCookie = WebUtils.getCookie(request, "AccessToken" + CLIENT_ID);
        if (accessTokenCookie == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessTokenCookie is null");
        } else {
            var accessToken = accessTokenCookie.getValue();
            Claims claims;
            try {
                claims = Jwts.parser()
                        .setSigningKey(DatatypeConverter.parseBase64Binary(APP_SECRET))
                        .parseClaimsJws(accessToken).getBody();
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "thrown in Authorization, accessToken is invalid");
            }
            String username1 = (String) claims.get("username");
            if (!username1.equals(username)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username does not match");
            }
            if (claims.getExpiration().before(Date.from(Instant.now()))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessToken is expired");
            }
            if (!validateToken(accessToken)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessToken is invalid, after checking with oauth server");
            }
        }
        return accessTokenCookie.getValue();
    }

    public String authorize() throws ResponseStatusException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var accessTokenCookie = WebUtils.getCookie(request, "AccessToken" + CLIENT_ID);
        if (accessTokenCookie == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessTokenCookie is null");
        } else {
            var accessToken = accessTokenCookie.getValue();
            Claims claims;
            try {
                claims = Jwts.parser()
                        .setSigningKey(DatatypeConverter.parseBase64Binary(APP_SECRET))
                        .parseClaimsJws(accessToken).getBody();
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "thrown in Authorization, accessToken is invalid");
            }
            if (claims.getExpiration().before(Date.from(Instant.now()))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessToken is expired");
            }
            if (!validateToken(accessToken)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessToken is invalid, after checking with oauth server");
            }
        }
        return accessTokenCookie.getValue();
    }


    private boolean validateToken(String accessToken) throws ResponseStatusException {
        String url = OAUTH_SERVER_DOMAIN + "/api/validateToken?accessToken=" + accessToken;
        HttpClient client = HttpClient.newBuilder().cookieHandler(new CookieManager()).build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().equals("Access Token is valid");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}