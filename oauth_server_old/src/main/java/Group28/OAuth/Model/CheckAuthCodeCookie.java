package Group28.OAuth.Model;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class CheckAuthCodeCookie {

    public static String Check(HttpServletResponse httpServletResponse) throws ResponseStatusException, SQLException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var authCodeCookie = WebUtils.getCookie(request, "AuthCode");
        if (authCodeCookie == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } else {
            return authCodeCookie.getValue();
        }
    }
}
