package bgs.oauth_server.model;

import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.request.*;
import org.springframework.web.server.*;
import org.springframework.web.util.*;

import javax.servlet.http.*;
import java.sql.*;

@Service("CheckAuthCodeCookie")
public class CheckAuthCodeCookie {

    public String Check(HttpServletResponse httpServletResponse) throws ResponseStatusException, SQLException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var authCodeCookie = WebUtils.getCookie(request, "AuthCode");
        if (authCodeCookie == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } else {
            return authCodeCookie.getValue();
        }
    }
}
