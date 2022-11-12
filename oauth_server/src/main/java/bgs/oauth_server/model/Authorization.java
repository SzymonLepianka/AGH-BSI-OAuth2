package bgs.oauth_server.model;

import org.springframework.http.*;
import org.springframework.web.context.request.*;
import org.springframework.web.server.*;

import javax.servlet.http.*;
import java.sql.*;

public class Authorization {

    public static void Authorize(HttpServletResponse httpServletResponse) throws ResponseStatusException, SQLException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().startsWith("AccessToken")) {
                    if (ValidateToken.validateToken(cookie.getValue())) {
                        return;
                    }
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    public static void Authorize(HttpServletResponse httpServletResponse, String clientID) throws ResponseStatusException, SQLException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().equals("AccessToken" + clientID)) {
                    if (ValidateToken.validateToken(cookie.getValue())) {
                        return;
                    }
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
