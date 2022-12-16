package bgs.oauth_server.model;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.request.*;
import org.springframework.web.server.*;

import javax.servlet.http.*;

@Service("Authorization")
public class Authorization {

    @Autowired
    private ValidateToken validateToken;

    public boolean authorizeOnCookie() throws ResponseStatusException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().startsWith("AccessToken")) {
                    if (validateToken.validateToken(cookie.getValue())) {
                        return true;
                    } else {
                        System.out.println("Walidacja nie przeszła");
                    }
                }
            }
        }
        return false;
    }

    public boolean authorizeOnClientID(String clientID) throws ResponseStatusException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().equals("AccessToken" + clientID)) {
                    if (validateToken.validateToken(cookie.getValue())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean authorizeOnClientIDAndAccessToken(String accessToken) throws ResponseStatusException {
        return validateToken.validateToken(accessToken);
    }
}
