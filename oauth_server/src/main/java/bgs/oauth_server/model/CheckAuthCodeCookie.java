package bgs.oauth_server.model;

import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.request.*;
import org.springframework.web.server.*;
import org.springframework.web.util.*;

import javax.servlet.http.*;

@Service("CheckAuthCodeCookie")
public class CheckAuthCodeCookie {

    public String check() throws ResponseStatusException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var authCodeCookie = WebUtils.getCookie(request, "AuthCode");
        if (authCodeCookie == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "AuthCode Cookie is not set");
        } else {
            return authCodeCookie.getValue();
        }
    }
}
