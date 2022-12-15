package bgs.oauthclientserver1.model;

import io.jsonwebtoken.*;
import org.riversun.okhttp3.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.request.*;
import org.springframework.web.server.*;
import org.springframework.web.util.*;

import javax.servlet.http.*;
import javax.xml.bind.*;
import java.io.*;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.*;
import java.time.*;
import java.util.*;

@Service("Authorization")
public class Authorization {

    public String authorizeOnUsername(String username, HttpServletResponse httpServletResponse) throws ResponseStatusException, IOException, InterruptedException {
        String appSecret = "222222";

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var accessTokenCookie = WebUtils.getCookie(request, "AccessToken2");
        if (accessTokenCookie == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessTokenCookie is null");
        } else {
            var accessToken = accessTokenCookie.getValue();
            Claims claims;
            try {
                claims = Jwts.parser()
                        .setSigningKey(DatatypeConverter.parseBase64Binary(appSecret))
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
            if (!validateToken(accessToken, httpServletResponse)){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessToken is invalid, after checking with oauth server");
            }
        }
        return accessTokenCookie.getValue();
    }

    public String authorize(HttpServletResponse httpServletResponse) throws ResponseStatusException, IOException, InterruptedException {
        String appSecret = "222222";

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var accessTokenCookie = WebUtils.getCookie(request, "AccessToken2");
        if (accessTokenCookie == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessTokenCookie is null");
        } else {
            var accessToken = accessTokenCookie.getValue();
            Claims claims;
            try {
                claims = Jwts.parser()
                        .setSigningKey(DatatypeConverter.parseBase64Binary(appSecret))
                        .parseClaimsJws(accessToken).getBody();
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "thrown in Authorization, accessToken is invalid");
            }
//            String username1 = (String) claims.get("username");
//            if (!username1.equals(username)) {
//                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username does not match");
//            }
            if (claims.getExpiration().before(Date.from(Instant.now()))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessToken is expired");
            }
            if (!validateToken(accessToken, httpServletResponse)){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessToken is invalid, after checking with oauth server");
            }
        }
        return accessTokenCookie.getValue();
    }


    private boolean validateToken(String accessToken, HttpServletResponse httpServletResponse) throws ResponseStatusException, IOException, InterruptedException {

        String url = "http://localhost:8080/api/validateToken?clientID=2&accessToken=" + accessToken;
        OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
//        cookieHelper.setCookie(url, "AccessToken", accessToken);
        HttpClient client = HttpClient.newBuilder().cookieHandler(new CookieManager()).build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
//        CookieStore cookieStore = ((CookieManager) (client.cookieHandler().get())).getCookieStore();
//        cookieStore.add(URI.create(url), new HttpCookie("AccessToken", accessToken));
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body().equals("Access Token is valid");
    }
}