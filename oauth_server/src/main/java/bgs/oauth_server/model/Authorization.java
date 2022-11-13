package bgs.oauth_server.model;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.request.*;
import org.springframework.web.server.*;

import javax.servlet.http.*;
import java.sql.*;

@Service("Authorization")
public class Authorization {

    @Autowired
    private ValidateToken validateToken;

    public void Authorize(HttpServletResponse httpServletResponse) throws ResponseStatusException, SQLException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().startsWith("AccessToken")) {
                    if (validateToken.validateToken(cookie.getValue())) {
                        return;
                    }
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    public void Authorize(HttpServletResponse httpServletResponse, String clientID) throws ResponseStatusException, SQLException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var cookies = request.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().equals("AccessToken" + clientID)) {
                    if (validateToken.validateToken(cookie.getValue())) {
                        return;
                    }
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
